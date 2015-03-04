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
package fr.polytech.common.model;

import java.io.IOException;

/**
 * Interface for 3d model loading
 * 
 * @author hadrien
 */
public interface ModelLoader {

    /**
     * Thrown if loading fail for some reason
     * 
     */
    static class ModelLoaderException extends Exception {
        public ModelLoaderException(String message, Throwable cause) {
            super(message, cause);
        }
    }    
    
    String getFileExtension();
    Model  loadModel(String name) throws IOException, ModelLoaderException;
    
}
