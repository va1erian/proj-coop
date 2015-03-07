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
package fr.polytech.common.scene;

import fr.polytech.common.render.Renderer;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import com.hackoeur.jglm.Vec3;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

/**
 *
 * @author hadrien
 */
public class SceneManager implements GLEventListener, KeyListener {

    private static final float CAM_MOVE_SPEED = 30.0f;
    
    private float camX, camY, camZ;
    private float camSpeedX, camSpeedY, camSpeedZ;
    
    private long lastTime = System.currentTimeMillis();
    
    private final AbstractScene scene;
    private Renderer renderer;
    
    public SceneManager(AbstractScene scene) {
        this.scene = scene;
        camX = 4.0f;
        camY = 8.0f;
        camZ = 8.0f;
    }
    
    @Override
    public void init(GLAutoDrawable glad) {
        GL3 gl3 = glad.getGL().getGL3();
        glad.setAutoSwapBufferMode(false);

        renderer = new Renderer(gl3);
        
        System.out.println("Scene init");
        try {
            scene.initResources();
            renderer.loadSceneResources(scene);
        } catch (Exception ex) {
            System.err.println("Failed to load scene resources");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void dispose(GLAutoDrawable glad) {
        
    }

    @Override
    public void display(GLAutoDrawable glad) {
        long currentTime =  System.currentTimeMillis();
        float elapsedTime = (currentTime - lastTime) * 0.001f;
        lastTime = currentTime;
        
        scene.update(elapsedTime);
        updateCamPos(elapsedTime);
        
        renderer.setView(getViewMat());
        renderer.renderScene(scene);
        
        glad.swapBuffers();
    }
    
    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {

    }
    

    
    private void updateCamPos(float dt) {
        camX += camSpeedX * dt;
        camY += camSpeedY * dt;
        camZ += camSpeedZ * dt;
        
        camSpeedX *= 0.9 * dt;
        camSpeedY *= 0.9 * dt;
        camSpeedZ *= 0.9 * dt;

    }
    
    private Mat4 getViewMat() {
        return Matrices.lookAt(
                new Vec3(camX, camY, camZ), 
                Vec3.VEC3_ZERO, 
                new Vec3(0,1,0));
        
    }
    
    @Override
    public void keyPressed(KeyEvent ke) {
       switch( ke.getKeyCode()) {
           case KeyEvent.VK_UP:
           camSpeedZ = CAM_MOVE_SPEED;
           break;
           
        case KeyEvent.VK_DOWN:
           camSpeedZ = -CAM_MOVE_SPEED;
        break;
            
        case KeyEvent.VK_LEFT:
           camSpeedX = -CAM_MOVE_SPEED;
        break;
            
        case KeyEvent.VK_RIGHT:
           camSpeedX = +CAM_MOVE_SPEED;
        break;
            
        case KeyEvent.VK_PAGE_UP:
           camSpeedY = -CAM_MOVE_SPEED;
        break;
            
        case KeyEvent.VK_PAGE_DOWN:
           camSpeedZ = -CAM_MOVE_SPEED;
        break;
       }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }
}

