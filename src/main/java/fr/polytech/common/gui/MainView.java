/*
 * Copyright (C) 2015 Cedric
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

import com.jogamp.opengl.util.FPSAnimator;
import fr.polytech.common.scene.SceneManager;
import java.awt.BorderLayout;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JComponent;

/**
 *
 * @author Cedric
 */
public class MainView extends javax.swing.JFrame {

    private final GLProfile glp = GLProfile.getMaxProgrammableCore(true);
    private final GLCapabilities caps; 
    private final FPSAnimator animator;
    /**
     * Creates new form MainFrame
     */
    public MainView() {
        caps = new GLCapabilities(glp);
        caps.setSampleBuffers(true);
        caps.setNumSamples(4);
        
        initComponents();
        animator = new FPSAnimator(sceneView, 60);
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sceneView = new GLJPanel(caps);
        pnlBas = new javax.swing.JPanel();
        btnDeconnexion = new javax.swing.JButton();
        pnlGraph = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("View");

        javax.swing.GroupLayout sceneViewLayout = new javax.swing.GroupLayout(sceneView);
        sceneView.setLayout(sceneViewLayout);
        sceneViewLayout.setHorizontalGroup(
            sceneViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 561, Short.MAX_VALUE)
        );
        sceneViewLayout.setVerticalGroup(
            sceneViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 461, Short.MAX_VALUE)
        );

        getContentPane().add(sceneView, java.awt.BorderLayout.CENTER);

        pnlBas.setLayout(new java.awt.BorderLayout());

        btnDeconnexion.setText("Deconnexion");
        btnDeconnexion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeconnexionActionPerformed(evt);
            }
        });
        pnlBas.add(btnDeconnexion, java.awt.BorderLayout.PAGE_END);

        pnlGraph.setLayout(new java.awt.BorderLayout());
        pnlBas.add(pnlGraph, java.awt.BorderLayout.CENTER);

        getContentPane().add(pnlBas, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDeconnexionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeconnexionActionPerformed
        EntryPoint.deconnexionAndDisplayMenu();
    }//GEN-LAST:event_btnDeconnexionActionPerformed

    
    public void initSceneManager(SceneManager manager){
        sceneView.addGLEventListener(manager);
        sceneView.setAnimator(animator);
        sceneView.addMouseMotionListener(manager);
        animator.start();
    }
    
    public void initGraph(JComponent panel){
        pnlGraph.add(panel, BorderLayout.SOUTH);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDeconnexion;
    private javax.swing.JPanel pnlBas;
    private javax.swing.JPanel pnlGraph;
    private javax.media.opengl.awt.GLJPanel sceneView;
    // End of variables declaration//GEN-END:variables
}