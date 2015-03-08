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

/**
 *
 * @author hadrien
 */
public class DronePropeller extends Actor {

    
    private final float ROTATION_SPEED = 10.0f;
    private float rotation = 0;
    private boolean inverse;
    
    public DronePropeller(Model model, boolean inverse, Actor parent) {
        super(model, new Vec3(0.5f, 0.5f, 0.5f), parent);
        
        this.inverse = inverse;
    }

    
    @Override
    public void think(float dt) {
        float inv = inverse ? -1 : 1;
        rotation += ROTATION_SPEED * dt * inv;
        setRot(new Vec3(rotation, 0, 0));
    }
    
}
