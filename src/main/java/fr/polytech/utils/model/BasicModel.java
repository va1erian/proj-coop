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
package fr.polytech.utils.model;

import com.hackoeur.jglm.Vec3;
import java.nio.FloatBuffer;

/**
 *
 * @author hadrien
 */
public class BasicModel implements Model {

    private float[]  vertices;
    private float[]  normals;
    private Vec3     color;
    
    public BasicModel(float[] vertices, float[] normals) {
        this.vertices = vertices;
        this.normals  = normals;
    }
    
    public BasicModel(float[] vertices, float[] normals, Vec3 color) {
        this.vertices = vertices;
        this.normals  = normals; 
        this.color    = color;
    } 
    /**
     * Get the list of Vec3 vertices as a FloatBuffer
     * @return 
     */
    @Override
    public FloatBuffer getVertices() {
        return FloatBuffer.wrap(vertices);
    }

    @Override
    public FloatBuffer getNormals() {
        return FloatBuffer.wrap(normals);
    }

    /**
     * @param vertices the vertices to set
     */
    public void setVertices(float[] vertices) {
        this.vertices = vertices;
    }

    /**
     * @param normals the normals to set
     */
    public void setNormals(float[] normals) {
        this.normals = normals;
    }

    @Override
    public Vec3 getColor() {
        return color;
    }

}
