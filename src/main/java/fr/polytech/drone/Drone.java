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

import com.hackoeur.jglm.Mat3;
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

    final private static float MAX_MOV_SPEED = 5.0f; 
    final private static float MOV_FRICTION  = 0.5f;
    
    final private Queue<DroneMessage> messageQueue = new ConcurrentLinkedQueue<>();
    
    
    private float momX, momY, momZ;
    
    public Drone(Model model) {
        super(model, new Vec3(0.4f, 0.6f, 0.9f));
    }

    @Override
    public void think(float dt) {
        processMessage(messageQueue.poll());
        setPos(getPos().add(new Vec3(momX * dt, momY * dt, momZ * dt)));
        
        momX *= MOV_FRICTION * dt;
        momY *= MOV_FRICTION * dt;
        momZ *= MOV_FRICTION * dt;
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
            break;
                
            case ROULIS:
            
            break;
                
            case TANGAGE:
                momX += force;
            break;
        }
        
        
    }
    
    private static float arg2f(int b) {
        b -= 15;
        return b * (1 / 7.0f);
    }
}
