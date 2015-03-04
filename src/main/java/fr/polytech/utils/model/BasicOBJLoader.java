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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Slow and incomplete loader for OBJ format 3d models
 * @author hadrien
 */
public class BasicOBJLoader implements ModelLoader{

    @Override
    public String getFileExtension() {
        return "obj";
    }

    @Override
    public Model loadModel(String name) throws IOException, ModelLoaderException{
        List<Float> tempVertices = new ArrayList<>();
        List<Float> tempUvs      = new ArrayList<>();
        List<Float> tempNormals  = new ArrayList<>();
        
        List<Float> vertices = new ArrayList<>();
        List<Float> uvs      = new ArrayList<>();
        List<Float> normals  = new ArrayList<>();
        
        URI res;
        try {
            res = getClass().getResource(name).toURI();
        } catch (URISyntaxException ex) {
            throw new ModelLoaderException("failed to open model", ex);
        }
        Path path = Paths.get(res);
        
        Files.lines(path).forEach((String t) -> {
            String[] tokens = t.trim().split(" ");
            if(tokens.length < 2) {
                return;
            }
            
            switch(tokens[0]) {
                case "v":
                for(int i = 0; i < 3; i++)
                    tempVertices.add(Float.parseFloat(tokens[i + 1]));
                break;
                
                case "vt":
                for(int i = 0; i < 2; i++)
                    tempUvs.add(Float.parseFloat(tokens[i + 1]));
                break;    
                
                case "vn":
                for(int i = 0; i < 3; i++)
                    tempNormals.add(Float.parseFloat(tokens[i + 1]));
                break;                                      
            }            
        });
        
        System.out.println("raw data loaded");
        
        Files.lines(path).forEach((String t) -> {
           String[] tokens = t.trim().split(" ");
           
           if(tokens.length < 2) {
               return;
           }
           
           switch(tokens[0]) {
                case "f":
                for(int i = 0; i < 3; i++) {
                    int[] vertex = splitTriplet(tokens[i + 1], true);
                    for(int j = 0; j < 3; j++) {
                        //Add 3 float per vectors. Also OBJ indices starts at 1
                        vertices.add(tempVertices.get((vertex[0] - 1) * 3 + j));
                        normals.add(tempNormals.get((vertex[2] -   1) * 3 + j));
                    }
                    if(!tempUvs.isEmpty()) {
                        //uvs.add(tempUvs.get((vertex[1] - 1) * 2));
                        //uvs.add(tempUvs.get((vertex[1] - 1) * 2 + 1));
                    }
                }
                break;
           }
        });
        System.out.println("faces collected");

        BasicModel model = new BasicModel(listToArray(vertices),listToArray(normals));
            
        return model;
    }
    
    static private int[] splitTriplet(String t , boolean skipUv) {        
        int buf[] = new int[3];
        
        String[] tokens = t.split("/");
        
        if(!skipUv) {
            for(int i = 0; i < 3; i++) {
                buf[i] = Integer.parseInt(tokens[i]);
            }
        } else {
            buf[0] = Integer.parseInt(tokens[0]);
            buf[2] = Integer.parseInt(tokens[2]); 
        }
        return buf;
    }
    
    static private float[] listToArray(List<Float> list) {
        float[] array = new float[list.size()];
        int i = 0;
        for(Float f : list) {
            array[i++] = f;
        }
        return array;
    }
}
