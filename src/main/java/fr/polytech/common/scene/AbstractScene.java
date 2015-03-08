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
package fr.polytech.common.scene;

import com.hackoeur.jglm.Vec3;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 *
 * @author hadrien
 */
public abstract class AbstractScene {
    
    protected final List<Object3D> props  = new ArrayList<>();
    protected Observable eventSource;
    protected Vec3 light = Vec3.VEC3_ZERO;
    
    public abstract void initResources() throws Exception;
    
    public abstract void update(float dt);
    
    public abstract Vec3 getCamFollowPoint();
    
    public void setEventSource(Observable o) {
        this.eventSource = o;
    }
    
    public Iterable<Object3D> objects() {
        return props;
    }
    
    /**
     * @return the light
     */
    public Vec3 getLight() {
        return light;
    }
    
    
}
