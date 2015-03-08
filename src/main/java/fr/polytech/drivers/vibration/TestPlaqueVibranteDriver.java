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
package fr.polytech.drivers.vibration;

import fr.polytech.drivers.Driver;
import jssc.SerialPortException;

/**
 *
 * @author Cedric
 */
public class TestPlaqueVibranteDriver extends Driver{

    PlaqueThread plaqueThread;

    public TestPlaqueVibranteDriver() {
        plaqueThread = new PlaqueThread();
    }
    
    @Override
    public boolean doConnect(String port) throws SerialPortException {
        plaqueThread.start();
        return true;
    }

    @Override
    public void close() throws SerialPortException {
        plaqueThread.interrupt();
    }
    
    @Override
    public String toString(){
        return "TestPlaqueVibranteDriver ";
    }
    
    public static byte IntToByte(int nb)
    {
        return (byte)(nb & 255);	
    }

    public static int ByteToInt(byte nb)
    {
        return (nb & 255);
    }
    
    public class PlaqueThread extends Thread{
        public void run() {
            Double cpt = 0.0d;
            Double result;
            while(true){
                try {
                    result =(Math.cos(cpt)*127);
                    cpt+=0.1;
                    Byte b = IntToByte(((int)Math.round(result)));
                    setChanged();
                    notifyObservers(b);
                    Thread.sleep(20);
                    System.out.println(result);
                } catch (InterruptedException ex) {
                    return;
                }
            }
                
        }
            
    }
}
