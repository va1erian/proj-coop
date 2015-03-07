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
package fr.polytech.drivers.drone;

/**
 *
 * @author cedric
 */
public class DroneMessage {
    
    public enum Type{
        LACET,
        TANGAGE,
        ALTITUDE,
        ROULIS;
                
        
        public static Type fromByte(byte value){
            switch(value){
                case 1:
                    return Type.LACET;
                case 2:
                    return Type.TANGAGE;
                case 3:
                    return Type.ALTITUDE;
                case 4:
                    return Type.ROULIS;
                default:
                    return null;
            }
        }
    }
    final public Type id;
    final public int value;

    DroneMessage (byte[] data){
        id    = Type.fromByte(data[0]);
        value = (int) data[1];
    }

}