/*
 * Copyright (C) 2015 Cedric
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

import fr.polytech.drivers.Driver;
import static fr.polytech.drivers.Driver.serialPort;
import fr.polytech.drivers.vibration.TestPlaqueVibranteDriver;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPortException;

/**
 *
 * @author Cedric
 */
public class TestDroneDriver extends Driver{
    
    DroneThread droneThread;

    public TestDroneDriver() {
        droneThread = new DroneThread();
    }
    
    @Override
    public boolean doConnect(String port) throws SerialPortException {
        droneThread.start();
        return true;
    }

    @Override
    public void close() throws SerialPortException {
        droneThread.interrupt();
    }
    
    @Override
    public String toString(){
        return "TestDroneDriver ";
    }
    
    public class DroneThread extends Thread{
        public void run() {
            
            while(true){
                try {
                    for(int i = 1; i<=4;i++){
                        byte buffer[] = {   (byte)i, 
                                            (byte) ((int)(Math.random() * 15))
                                        };
                        
                        DroneMessage msg = new DroneMessage(buffer);
                        System.out.println("message -> "+buffer[0]+"   "+buffer[1]);
                        setChanged();
                        notifyObservers(msg);
                        sleep(100);
                    }
                    
                } catch (InterruptedException ex) {
                     return;
                }
            }
                
        }
            
    }
}
