/*******************************************************************************
 * jFCDPP
 * Copyright (C) 2013 Chuck Ritola
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 ******************************************************************************/
package com.ritolaaudio.jfcdpp;

import java.io.IOException;

import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDDeviceInfo;
import com.codeminders.hidapi.HIDManager;

public class TEST
{
    static {com.codeminders.hidapi.ClassPathLibraryLoader.loadNativeHIDLibrary(); }
    static final int VENDOR_ID = 1578;
    static final int PRODUCT_ID = 25345;
    private static final int BUFSIZE = 2048;
        
    /**
     * @param args input strings value.
     */
    public static void main(String[] args) throws IOException
    {
     listDevices();
     readDevice();
    }
    
    private static void readDevice()
    {
        HIDDevice dev;
        try
        {
        	HIDManager mgr = HIDManager.getInstance();
            dev = mgr.openById(VENDOR_ID, PRODUCT_ID, null);
            if(dev==null)
            	{System.out.println("No FCDPP found.");System.exit(1);}
            System.err.print("Manufacturer: " + dev.getManufacturerString() + "\n");
            System.err.print("Product: " + dev.getProductString() + "\n");
            System.err.print("Serial Number: " + dev.getSerialNumberString() + "\n");
            try
            {
                byte[] buf = new byte[BUFSIZE];
            } finally
            {
                dev.close();
                mgr.release();    
                System.gc();
            }
            
        } 
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    private static void listDevices()
	    {
	        String property = System.getProperty("java.library.path");
	        System.err.println(property);
	        try
	        {
	           
	            HIDManager manager = HIDManager.getInstance();
	            HIDDeviceInfo[] devs = manager.listDevices();
	            System.err.println("Devices:\n\n");
	            for(int i=0;i<devs.length;i++)
	            {
	                System.err.println(""+i+".\t"+devs[i]);
	                System.err.println("---------------------------------------------\n");
	            }
	            System.gc();
	        }
	        catch(IOException e)
	        {
	            System.err.println(e.getMessage());
	            e.printStackTrace();
	        }
	    }

}
