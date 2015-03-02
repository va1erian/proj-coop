/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.polytech.drivers;

/**
 *
 * @author cedri_000
 */
public abstract class Driver {
    
    private byte buffer;
    
    public abstract boolean doConnect();
    
    public abstract void write();
     
    public abstract byte[] read();
}
