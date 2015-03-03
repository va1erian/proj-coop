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
package fr.polytech.utils.scene;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hadrien
 */
public abstract class AbstractScene {

    protected final Positionnable camera = new Position();
    protected final Positionnable light = new Position();
    protected final List<Prop> props  = new ArrayList<>();
    
    public abstract void initResources() throws Exception;
    
    public abstract void update(float dt);
    
    public AbstractScene() {
    }
    
    public Iterable<Prop> props() {
        return props;
    }
    
    public Positionnable getCam() {
        return camera;
    }

    /**
     * @return the light
     */
    public Positionnable getLight() {
        return light;
    }
    
    
}