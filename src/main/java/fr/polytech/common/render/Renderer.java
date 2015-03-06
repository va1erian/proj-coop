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
package fr.polytech.common.render;

import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import com.hackoeur.jglm.Vec3;
import fr.polytech.common.model.Model;
import fr.polytech.common.scene.AbstractScene;
import fr.polytech.common.scene.Positionnable;
import fr.polytech.common.scene.Prop;
import glsl.GLSLProgramObject;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import javax.media.opengl.GL;
import javax.media.opengl.GL3;

/**
 *
 * @author hadrien
 */
public class Renderer {
    
    private final GL3 gl3;
    private final Map<Model, Integer> modelVertBuf   = new HashMap<>();
    private final Map<Model, Integer> modelNormalBuf = new HashMap<>();
    private final Mat4 projection = Matrices.perspective(65.0f, 4.0f / 3.0f, 0.1f, 1000.0f);
    private final int[] matBufferId = new int[3];
    private final int[] lightBufferId = new int[1];
    private final int[] colorBufferId = new int[1];
    private GLSLProgramObject basicShader;
    private Mat4 view;
    
    
    public Renderer(GL3 gl) {
        this.gl3 = gl;
        
        gl3.glEnable(GL3.GL_DEPTH_TEST);
        gl3.glDepthFunc(GL3.GL_LESS);
    }
   
    public void loadSceneResources(AbstractScene scene) throws Exception {
        buildShaders();
        
        for(Prop p : scene.props() ) {
            if(!isModelLoaded(p.getModel())) {
                loadModel(p.getModel(), gl3);
            }
        }
        matBufferId[0] = gl3.glGetUniformLocation(basicShader.getProgramId(), "MVP");
        matBufferId[1] = gl3.glGetUniformLocation(basicShader.getProgramId(), "V");
        matBufferId[2] = gl3.glGetUniformLocation(basicShader.getProgramId(), "M");
        lightBufferId[0] = gl3.glGetUniformLocation(basicShader.getProgramId(), "lightPosWorldspace");
        colorBufferId[0] = gl3.glGetUniformLocation(basicShader.getProgramId(), "diffuse");
        System.out.println("model and resources loaded");
    };
    public void loadModel(Model mdl, GL3 gl3) {
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
    

    public void renderScene(AbstractScene scene) {
        gl3.glClearColor(0.4f, 0.4f, 0.4f, 0.0f);
        gl3.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);

        basicShader.bind(gl3);
                
        for(Prop p : scene.props()) {
            renderProp(scene, p);
        }
        
        basicShader.unbind(gl3);
    }
    
    public void renderProp(AbstractScene scene, Prop p) {        
        prepareMVP(gl3, p, p.getRotationCenter());
        
        Positionnable light = scene.getLight();
        gl3.glUniform3fv(lightBufferId[0], 1, light.getPos().getBuffer());
        
        gl3.glUniform3fv(colorBufferId[0], 1, p.getColor().getBuffer());
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
    
    public void dispose() {
        basicShader.destroy(gl3);
    }
    
    public void setView(Mat4 view) {
        this.view = view;
    }
    
    
    private boolean isModelLoaded(Model mdl) {
        return modelVertBuf.containsKey(mdl);
    }
    
    private void buildShaders() {
        basicShader = new GLSLProgramObject(gl3);
        basicShader.attachVertexShader(gl3, "/shaders/vertex_shader.glsl");
        basicShader.attachFragmentShader(gl3,"/shaders/fragment_shader.glsl");
        basicShader.initializeProgram(gl3, true);
    }

    
    private Mat4 getModelMat(Positionnable pos, Vec3 rotAxis) {
        Mat4 rotXMatrix = Matrices.rotate(pos.getDir().getX(), new Vec3( 0, 1, 0));
        Mat4 rotYMatrix = Matrices.rotate(pos.getDir().getY(), new Vec3( 1, 0, 0));
        Mat4 rotZMatrix = Matrices.rotate(pos.getDir().getZ(), new Vec3( 0, 0, 1));

        //rotMatrix = rotMatrix.translate(rotAxis);
        
        return Mat4.MAT4_IDENTITY
                .translate(pos.getPos())
                .translate(rotAxis.getNegated())
                .multiply(rotXMatrix)
                .multiply(rotZMatrix)
                .multiply(rotYMatrix)
                .translate(rotAxis);
                
    }
    
    private void prepareMVP(GL3 gl3, Positionnable pos, Vec3 rotAxis) {
        Mat4 model = getModelMat(pos, rotAxis);
        
        Mat4 mvp = projection.multiply(view.multiply(model));
        
        gl3.glUniformMatrix4fv(matBufferId[0], 1, false, mvp.getBuffer());
        gl3.glUniformMatrix4fv(matBufferId[1], 1, false, view.getBuffer());
        gl3.glUniformMatrix4fv(matBufferId[2], 1, false, model.getBuffer());
    }

}
