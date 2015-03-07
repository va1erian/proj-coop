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
package fr.polytech.drone;

import fr.polytech.common.gui.Dialogframe;
import fr.polytech.common.gui.Mainframe;
import fr.polytech.common.scene.AbstractScene;
import fr.polytech.common.scene.SceneManager;
import fr.polytech.drivers.Driver;
import fr.polytech.drivers.PlaqueVibranteDriver;
import fr.polytech.drivers.drone.DroneDriver;
import fr.polytech.vibration.VibrationScene;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.swing.JFrame;
import jssc.SerialPortException;

/**
 *
 * @author hadrien
 */
public class EntryPoint extends Observable{     
    private  static AbstractScene scene;
    private static SceneManager view;
    private static Driver driver;    
    
    
    /* GUI Frames */
    private static JFrame mainframe;
    private static Dialogframe dialog;
    
     public static enum Project {
        DRONE,
        PLAQUE_VIBRANTE,
        NULL;
    }
    
     
     
    public static void main(String[] args) {
       
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                mainframe = new Mainframe();
                mainframe.setLocationRelativeTo(null);
                mainframe.setVisible(true);
                mainframe.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        try {
                            System.out.println("Windows closing...");
                            if (driver != null)
                                driver.close();
                            System.exit(0);
                        } catch (SerialPortException ex) {
                            Logger.getLogger(EntryPoint.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                    public void windowDestroyNotify(WindowEvent we) {
                        System.exit(0);
                    }  
                
                });
            }
        });
         
        
    }
    
    /**
     * 
     * @param port
     * @param project 
     */
    public static void displayProject(String port, Project project){
        try{
            scene = (project == Project.DRONE) ? new DroneScene() : new VibrationScene();
            view = new SceneManager(scene);
            driver = (project == Project.DRONE)?
                        new DroneDriver(port):new PlaqueVibranteDriver(port);
            
            mainframe.setVisible(false);
            
            GLProfile glp = GLProfile.getDefault();
            GLCapabilities caps = new GLCapabilities(glp);
            

            
            dialog = new Dialogframe();
            dialog.initSceneManager(view);
            dialog.setLocationRelativeTo(null);
            dialog.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        try {
                            System.out.println("Windows closing...");
                            driver.close();
                            System.exit(0);
                        } catch (SerialPortException ex) {
                            Logger.getLogger(EntryPoint.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                
                    public void windowDestroyNotify(WindowEvent we) {
                        System.exit(0);
                    }  
                });
            
        } catch (Exception e){
            System.err.println("EntryPoint > displayProject > " +e.getMessage());
        }
    }
    
    
     public static void deconnexionAndDisplayMenu(){
        try {
            System.out.println("Deconnexion...");
            driver.close();
            driver = null;
            dialog.dispose();
            mainframe.setVisible(true);
            mainframe.setLocationRelativeTo(null);
            
        } catch (SerialPortException ex) {
            Logger.getLogger(EntryPoint.class.getName()).log(Level.SEVERE, null, ex);
        }
     }

   
}
