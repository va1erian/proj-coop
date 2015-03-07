/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.polytech.drivers;

import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 *
 * @author cedri_000
 */
public class PlaqueVibranteDriver extends Driver{
   
    
    //private Thread runningThread;
    
    
    public PlaqueVibranteDriver(String port) throws SerialPortException{
       if (doConnect(port))
        System.out.println("PlaqueVibranteDriver> Now connected to the device on port "+port);
       else
            System.err.println("PlaqueVibranteDriver> Fail to connect to the device on port"+port);
    }

    
     public static void main(String[] args) {
        try {
            String[] portNames = SerialPortList.getPortNames();
            for(int i = 0; i < portNames.length; i++){
            System.out.println(portNames[i]);
            }
            
            PlaqueVibranteDriver driver = new PlaqueVibranteDriver("COM4");
        } catch (SerialPortException ex) {
            Logger.getLogger(PlaqueVibranteDriver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean doConnect(String port) throws SerialPortException{
        boolean connected = false;
         
        serialPort = new SerialPort(port);

        serialPort.openPort();//Open port
        serialPort.setParams(SerialPort.BAUDRATE_9600, 8, 1, 0);//Set params

        /*
            MASK_RXCHAR -> char reception
            MASK_CTS    -> clear to sends
            MASK_DSR    -> Data send ready
        */
        int mask = SerialPort.MASK_RXCHAR;//Prepare mask
        serialPort.setEventsMask(mask);//Set mask
        serialPort.addEventListener(portReader);//Add SerialPortEventListener
        connected = true;
            
        return connected;
    }

    
    @Override
    public void close() throws SerialPortException{
        serialPort.closePort();
    }
    

    /*
     * In this class must implement the method serialEvent, through it we learn about 
     * events that happened to our port. But we will not report on all events but only 
     * those that we put in the mask. In this case the arrival of the data and change the 
     * status lines CTS and DSR
     */
    private final SerialPortEventListener portReader = new SerialPortEventListener()  {

        public void serialEvent(SerialPortEvent event){
            try{
               byte buffer[] = serialPort.readBytes(1);
               // if (buffer[0] != 13)
                    System.out.println(String.format(" value ->%d" ,buffer[0] & 0xFF));
                    
            } catch(Exception e){
                System.err.println(e.getMessage());
            }
           
            
            if(event.isRXCHAR()){//If data is available
                if(event.getEventValue() == 2){//Check bytes count in the input buffer
                    //Read data, if 4 bytes available 
                    try {
                        byte buffer[] = serialPort.readBytes(2);
                        Byte msg = (byte)(buffer[0] & 0xFF);
                        
                        setChanged();
                        notifyObservers(msg);
                    }
                    catch (SerialPortException ex) {
                        System.out.println(ex);
                    }
                }
            }
        }
    };
}
