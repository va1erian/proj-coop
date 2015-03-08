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

import com.hackoeur.jglm.Vec3;
import fr.polytech.common.model.Model;
import fr.polytech.common.scene.Actor;
import fr.polytech.drivers.drone.DroneMessage;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author hadrien
 */
public class Drone extends Actor implements Observer {

    final private static float MAX_MOV_SPEED = 30.0f; 
    
    final private Queue<DroneMessage> messageQueue = new ConcurrentLinkedQueue<>();
  
    private float speed, momY, yawMom, rollMom;
    private double yawAngle;
    
    public Drone(Model model) {
        super(model, new Vec3(0.4f, 0.6f, 0.9f));
    }

    @Override
    public void think(float dt) {
        processMessage(messageQueue.poll());        
        
        yawAngle += yawMom * dt;
        float xVel = (float) (Math.sin(yawAngle) * speed * dt);
        float zVel = (float) (Math.cos(yawAngle) * speed * dt);
        
        xVel += (float) (Math.sin(yawAngle + Math.PI/2) * rollMom * dt);
        zVel += (float) (Math.cos(yawAngle + Math.PI/2) * rollMom * dt);
        
        float pitchAngle = (speed / MAX_MOV_SPEED )  / 1.39f;
        float rollAngle  = (rollMom / MAX_MOV_SPEED) / 1.39f;
        
        setPos(getPos().add(new Vec3(xVel, momY * dt, zVel)));
        setRot(new Vec3((float) yawAngle, pitchAngle, rollAngle));
        
        speed   *= 0.95;
        momY    *= 0.90;
        yawMom  *= 0.90;
        rollMom *= 0.90;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof DroneMessage) {
            DroneMessage msg = (DroneMessage)arg;
            messageQueue.add(msg);
        }
    }
    
    private void processMessage(DroneMessage msg) {
        if(msg == null) return;
        
        float force = arg2f(msg.value) * MAX_MOV_SPEED;
        switch(msg.id) {
            case ALTITUDE:
                momY += force;
            break;
                
            case LACET:
                yawMom += force / 2.5;
            break;
                
            case ROULIS:
                rollMom += force;
            break;
                
            case TANGAGE:
                speed += force;
            break;
        }
        
        
    }
    
    private static float arg2f(int b) {
        b -= 7;
        return b * (1 / 7.0f);
    }
}
