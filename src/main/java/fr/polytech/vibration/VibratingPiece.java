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
package fr.polytech.vibration;

import com.hackoeur.jglm.Vec3;
import fr.polytech.common.model.Model;
import fr.polytech.common.scene.Actor;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author hadrien
 */ 
public class VibratingPiece extends Actor implements Observer {
    
    private double angle = 0;
    private double counter = 0;

    public VibratingPiece(Model model) {
        super(model, new Vec3(0.9f, 0.3f, 0.5f));
        rotationCenter = new Vec3(-3.4f, 0, 0);
    }

    @Override
    public void think(float dt) {
        counter += 25 * dt;
        angle = Math.cos(counter) * 0.08;
        
        setRot(new Vec3((float) angle, 0, 0));
    }

    @Override
    public void update(Observable o, Object arg) {
    }
    
}
