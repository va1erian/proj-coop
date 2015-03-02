/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.polytech.drivers.drone;

import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 *
 * @author cedric
 */
public class DroneDriver extends Observable{
   
    //private Thread runningThread;
    private SerialPort serialPort;
    
     /*
     * In this class must implement the method serialEvent, through it we learn about 
     * events that happened to our port. But we will not report on all events but only 
     * those that we put in the mask. In this case the arrival of the data and change the 
     * status lines CTS and DSR
     */
    private final SerialPortEventListener portReader = new SerialPortEventListener()  {        
//        public SerialPortReader(DroneDriver drv){
//            this.drv = drv;
//        }
        
        @Override
        public void serialEvent(SerialPortEvent event){
            try{
               byte buffer[] = serialPort.readBytes(2);
               if (buffer[0] != 13)
                    System.out.println(String.format("%c   %c" ,buffer[0] & 0xFF, buffer[1] & 0xFF));
               
            } catch(Exception e){
                System.err.println(e.getMessage());
            }
            
            if(event.isRXCHAR()){//If data is available
                if(event.getEventValue() == 2){//Check bytes count in the input buffer
                    //Read data, if 4 bytes available 
                    try {
                        byte buffer[] = serialPort.readBytes(2);
                        DroneMessage msg = new DroneMessage(buffer);
                        
                        notifyObservers(msg);
                        
                        switch(msg.id){
                            case LACET:
                                
                            break;
                                
                            case TANGAGE:
                            break;
                                
                            case ALTITUDE:
                            break;
                                
                            case ROULIS:
                            break;
                                
                            default:
                            break;
                        }
                        
                    }
                    catch (SerialPortException ex) {
                        System.out.println(ex);
                    }
                }
            }
        }
    };

    
    
    public static void main(String[] args) {
        try {
            /*String[] portNames = SerialPortList.getPortNames();
            for(int i = 0; i < portNames.length; i++){
            System.out.println(portNames[i]);
            }*/
            DroneDriver driver = new DroneDriver("COM6");
        } catch (SerialPortException ex) {
            Logger.getLogger(DroneDriver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public DroneDriver(String port) throws SerialPortException{
        serialPort = new SerialPort(port);
        serialPort.openPort();//Open port
        serialPort.setParams(SerialPort.BAUDRATE_38400, 8, 1, 0);//Set params
        int mask = SerialPort.MASK_RXCHAR;//Prepare mask
        serialPort.setEventsMask(mask);//Set mask
        serialPort.addEventListener(portReader);//Add SerialPortEventListener
 
        
        
        /* runningThread = new Thread("runningDroneThread"){
            
            @Override
            public void run() {
                byte[] buffer;
                while(true){
                    
                    try {
                        buffer = serialPort.readBytes(2);
                    } catch (SerialPortException ex) {
                        Logger.getLogger(DroneDriver.class.getName()).log(Level.SEVERE, "read bytes : Error", ex);
                    }
               }
            }
        }; */
    }
    
    public void close() throws SerialPortException{
        serialPort.closePort();
    }
    
    
    
}


