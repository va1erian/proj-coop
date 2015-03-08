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
package fr.polytech.common.gui;

import fr.polytech.common.scene.AbstractScene;
import fr.polytech.common.scene.SceneManager;
import fr.polytech.drivers.Driver;
import fr.polytech.drivers.vibration.TestPlaqueVibranteDriver;
import fr.polytech.drone.VibratingPieceGraph;
import fr.polytech.drivers.drone.TestDroneDriver;
import fr.polytech.drone.DroneScene;
import fr.polytech.vibration.VibrationScene;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GLException;
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
    private static ConfigView mainframe;
    private static MainView dialog;
    
     public static enum Project {
        DRONE,
        PLAQUE_VIBRANTE,
        NULL;
    }
    
     
     
    public static void main(String[] args) {
       
        java.awt.EventQueue.invokeLater(() -> {
            mainframe = new ConfigView();
            mainframe.setLocationRelativeTo(null);
            mainframe.setVisible(true);
            mainframe.addWindowListener(new WindowAdapter() {
                @Override
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
            
            // TODO : en test...
//            driver = (project == Project.DRONE)?
//                        new DroneDriver():new PlaqueVibranteDriver();
            
            driver = (project == Project.DRONE) ?
                        new TestDroneDriver():new TestPlaqueVibranteDriver();
            
            scene.setEventSource(driver);
            
            if (driver.doConnect(port))
                System.out.println(driver+"> Now connected to the device on port "+port);
            else
                System.err.println(driver+"> Fail to connect to the device on port"+port);
            
            mainframe.setVisible(false);
            

            
            dialog = new MainView();
            dialog.initSceneManager(view);
            dialog.setLocationRelativeTo(null);
            
            if (project == Project.PLAQUE_VIBRANTE){
                System.out.println("Create Graph...");
                VibratingPieceGraph graph = new VibratingPieceGraph();
                driver.addObserver(graph);
                dialog.initGraph(graph);
            }
            
            dialog.addWindowListener(new WindowAdapter() {
                    @Override
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
            
        } catch (SerialPortException | GLException e){
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
