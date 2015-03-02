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

import fr.polytech.drivers.drone.DroneMessage;
import fr.polytech.utils.model.Model;
import fr.polytech.utils.scene.Position;
import fr.polytech.utils.scene.Prop;
import fr.polytech.utils.scene.Thinker;

/**
 *
 * @author hadrien
 */
public class Drone extends Position implements Prop, Thinker<DroneMessage> {

    private final Model mdl;
    
    public Drone(Model model) {
        mdl = model;
    }
    
    @Override
    public void think(float dt, DroneMessage arg) {
        //TODO
    }

    @Override
    public Model getModel() {
        return mdl;
    }
    
}
