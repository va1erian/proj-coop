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
import fr.polytech.common.model.BasicOBJLoader;
import fr.polytech.common.model.Model;
import fr.polytech.common.model.ModelLoader;
import fr.polytech.common.scene.AbstractScene;
import fr.polytech.common.scene.StaticProp;
import java.util.Observable;
import java.util.Random;

/**
 *
 * @author hadrien
 */
public class DroneScene extends AbstractScene {
 
    private Drone drone;

    public DroneScene() {
        light.setPos(new Vec3(0.0f, 4.0f, 0));
    }
    
    @Override
    public void initResources() throws Exception {
        ModelLoader loader = new BasicOBJLoader();
        Model droneMdl = loader.loadModel("/models/drone.obj");
        
        drone = new Drone(droneMdl);
        eventSource.addObserver(drone);
        props.add(drone);
        
        Model cubeMdl = loader.loadModel("/models/cube2.obj");
        addRandomProps(cubeMdl);
    }

    @Override
    public void update(float dt) {
        drone.think(dt);
    }
    
    private void addRandomProps(Model mdl) {
        Random rand = new Random();
        
        for(int i = 0; i < 100; i++) {
            StaticProp d = new StaticProp(mdl, 
                    new Vec3(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
            Vec3 random = new Vec3(
                    rand.nextFloat() - 0.5f,
                    - 0.2f,
                    rand.nextFloat() - 0.5f).multiply(75.0f);
            
            d.setPos(random);
            
            random = new Vec3(
                    rand.nextFloat() - 0.5f,
                    rand.nextFloat() - 0.5f,
                    rand.nextFloat() - 0.5f).multiply(20.0f);
            
            d.setDir(random);
            props.add(d);
        }
    }
}
