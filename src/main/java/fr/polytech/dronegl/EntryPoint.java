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
package fr.polytech.dronegl;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.FPSAnimator;
import fr.polytech.utils.scene.AbstractScene;
import fr.polytech.utils.scene.SceneRenderer;
import fr.polytech.vibration.VibrationScene;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;

/**
 *
 * @author hadrien
 */
public class EntryPoint {     
    private final static AbstractScene scene = new VibrationScene();
    private final static SceneRenderer view = new SceneRenderer(scene);
    
    public static void main(String[] args) {
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);

        GLWindow window = GLWindow.create(caps);
        window.setSize(800, 600);
        window.setVisible(true);
        window.setTitle("GLdrone");
        
        window.addGLEventListener(view);
        window.addKeyListener(view);
        
        FPSAnimator animator = new FPSAnimator(window, 60);
        animator.start();
        
        window.addWindowListener( new WindowAdapter() {
            @Override
            public void windowDestroyNotify(WindowEvent we) {
                System.exit(0);
            }     
        });
    }
}
