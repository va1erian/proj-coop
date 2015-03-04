/*
 * Copyright (C) 2015 hadrien
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.polytech.utils.scene;

import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import com.hackoeur.jglm.Vec3;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import fr.polytech.utils.model.Model;
import glsl.GLSLProgramObject;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

/**
 *
 * @author hadrien
 */
public class SceneRenderer implements GLEventListener, KeyListener {

    private float camX, camY, camZ, camDirX, camDirY, camDirZ;
    
    private final AbstractScene scene;
    private final Map<Model, Integer> modelVertBuf   = new HashMap<>();
    private final Map<Model, Integer> modelNormalBuf = new HashMap<>();
    private final Mat4 projection = Matrices.perspective(75.0f, 4.0f / 3.0f, 0.1f, 1000.0f);
    
    private final int[] matBufferId = new int[3];
    private final int[] lightBufferId = new int[1];
    private GLSLProgramObject basicShader;
    
    public SceneRenderer(AbstractScene scene) {
        this.scene = scene;
        
    }
    
    @Override
    public void init(GLAutoDrawable glad) {
        GL3 gl3 = glad.getGL().getGL3();

        System.out.println("Scene init");
        try {
            scene.initResources();
            loadSceneResources(gl3);
        } catch (Exception ex) {
            System.err.println("Failed to load scene resources");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        glad.setAutoSwapBufferMode(false);
        
        gl3.glEnable(GL3.GL_DEPTH_TEST);
        gl3.glDepthFunc(GL3.GL_LESS);
        
        matBufferId[0] = gl3.glGetUniformLocation(basicShader.getProgramId(), "MVP");
        matBufferId[1] = gl3.glGetUniformLocation(basicShader.getProgramId(), "V");
        matBufferId[2] = gl3.glGetUniformLocation(basicShader.getProgramId(), "M");
        lightBufferId[0] = gl3.glGetUniformLocation(basicShader.getProgramId(), "lightPosWorldspace");
    }

    @Override
    public void dispose(GLAutoDrawable glad) {
        basicShader.destroy(glad.getGL().getGL3());
    }

    @Override
    public void display(GLAutoDrawable glad) {
        GL3 gl3 = glad.getGL().getGL3();
        scene.update(0.0016f);
        
        gl3.glClearColor(0.4f, 0.4f, 0.4f, 0.0f);
        gl3.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
        renderScene(gl3);
        glad.swapBuffers();
    }

    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {

    }
    
    public void loadSceneResources(GL3 gl3) throws Exception {
        buildShaders(gl3);
        
        for(Prop p : scene.props() ) {
            if(!isModelLoaded(p.getModel())) {
                loadModel(p.getModel(), gl3);
            }
        }
        System.out.println("Models loaded");
    };
    
    private void loadModel(Model mdl, GL3 gl3) {
        IntBuffer buf = IntBuffer.allocate(2);
        gl3.glGenBuffers(2, buf);
        
        int size = mdl.getVertices().capacity() * Float.BYTES;
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER,buf.get(0));
        gl3.glBufferData(GL3.GL_ARRAY_BUFFER, size, mdl.getVertices(), GL3.GL_STATIC_DRAW);
        
        size = mdl.getNormals().capacity() * Float.BYTES;
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, buf.get(1));
        gl3.glBufferData(GL3.GL_ARRAY_BUFFER, size, mdl.getNormals(), GL3.GL_STATIC_DRAW);
        
        modelVertBuf.put(mdl, buf.get(0));
        modelNormalBuf.put(mdl, buf.get(1));
        
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
    }
    
    private boolean isModelLoaded(Model mdl) {
        return modelVertBuf.containsKey(mdl);
    }
    
    private void buildShaders(GL3 gl3) {
        basicShader = new GLSLProgramObject(gl3);
        basicShader.attachVertexShader(gl3, "/shaders/vertex_shader.glsl");
        basicShader.attachFragmentShader(gl3,"/shaders/fragment_shader.glsl");
        basicShader.initializeProgram(gl3, true);
    }

    private void renderScene(GL3 gl3) {
        basicShader.bind(gl3);
                
        for(Prop p : scene.props()) {
            renderProp(gl3, p);
        }
        
        basicShader.unbind(gl3);
    }
    
    private void renderProp(GL3 gl3, Prop p) {        
        prepareMVP(gl3, p);
        
        Positionnable light = scene.getLight();
        gl3.glUniform3f(lightBufferId[0], 
                light.getPos().getX(), light.getPos().getY(), light.getPos().getZ());
        
        Model mdl = p.getModel();
        
        gl3.glEnableVertexAttribArray(0);
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, modelVertBuf.get(mdl));
        gl3.glVertexAttribPointer(0, 3, GL3.GL_FLOAT, false, 0, 0);
        
        gl3.glEnableVertexAttribArray(1);
        gl3.glBindBuffer(GL.GL_ARRAY_BUFFER, modelNormalBuf.get(mdl));
        gl3.glVertexAttribPointer(1, 3, GL3.GL_FLOAT, false, 0, 0);
        
        int mdlLen =  mdl.getVertices().array().length / 3;
        gl3.glDrawArrays(GL3.GL_TRIANGLES, 0, mdlLen);  
       
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER,0);
        gl3.glDisableVertexAttribArray(0);
        gl3.glDisableVertexAttribArray(1);
    }
    
    private Mat4 getViewMat() {
        System.out.println("Cam " + camX + "," + camY + "," + camZ);
        return Matrices.lookAt(
                new Vec3(camX, camY, camZ), 
                Vec3.VEC3_ZERO, 
                new Vec3(0,1,0));
        
    }
    
    private Mat4 getModelMat(Positionnable pos) {
        Mat4 rotMatrix = Matrices.rotate(pos.getDir().getX(), new Vec3( 0, 1, 0));
        return Mat4.MAT4_IDENTITY.translate(pos.getPos()).multiply(rotMatrix);
    }
    
    private void prepareMVP(GL3 gl3, Positionnable pos) {
        Mat4 view  = getViewMat();
        Mat4 model = getModelMat(pos);
        
        Mat4 mvp = projection.multiply(view.multiply(model));
        
        gl3.glUniformMatrix4fv(matBufferId[0], 1, false, mvp.getBuffer());
        gl3.glUniformMatrix4fv(matBufferId[1], 1, false, view.getBuffer());
        gl3.glUniformMatrix4fv(matBufferId[2], 1, false, model.getBuffer());
    }

    @Override
    public void keyPressed(KeyEvent ke) {
       switch( ke.getKeyCode()) {
           case KeyEvent.VK_UP:
           camX += 2.0f;
           break;
           
        case KeyEvent.VK_DOWN:
           camX -= 2.0f;
        break;
            
        case KeyEvent.VK_LEFT:
           camZ -= 2.0f;
        break;
            
        case KeyEvent.VK_RIGHT:
           camZ += 2.0f;
        break;
            
        case KeyEvent.VK_PAGE_UP:
            camY -= 2.0f;
        break;
            
        case KeyEvent.VK_PAGE_DOWN:
            camY += 2.0f;
        break;
       }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

