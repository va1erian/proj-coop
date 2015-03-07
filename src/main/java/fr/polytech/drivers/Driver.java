/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.polytech.drivers;

import java.util.Observable;
import jssc.SerialPort;
import jssc.SerialPortException;

/**
 *
 * @author cedri_000
 */
public abstract class Driver extends Observable{
    
    public static SerialPort serialPort;
    
    public abstract boolean doConnect(String port) throws SerialPortException;
    
    public abstract void close() throws SerialPortException;
    
    

}
