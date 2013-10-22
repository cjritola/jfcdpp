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
package com.ritolaaudio.jfcdpp.usb4javaD;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.usb.UsbConst;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbEndpoint;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfacePolicy;
import javax.usb.UsbPipe;
import javax.usb.UsbServices;


public class Dongles
	{
	static {com.codeminders.hidapi.ClassPathLibraryLoader.loadNativeHIDLibrary();}
	
	public static List<FCDPP> getDongles() throws IOException
		{
		ArrayList<FCDPP> result = new ArrayList<FCDPP>();
		try {
			UsbServices srv = UsbHostManager.getUsbServices();
			UsbHub rootHub = srv.getRootUsbHub();
			
			for(HIDDevice dev:Dongles.getDongles(rootHub, new ArrayList<HIDDevice>()))
				{result.add(new FCDPP(dev));}
			}
		catch(UsbException e){throw new IOException("UsbException",e);}
		return result;
		}
	private static List<HIDDevice> getDongles(UsbDevice device, List<HIDDevice> result) throws UsbException
		{
		UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
        try{if(!device.isUsbHub())
        	{
        	System.out.println(device.getProductString()+" device class: "+desc.bDeviceClass());
        	if(device.getProductString().toUpperCase().contains("FUNCUBE"))
            	{
            	for(final UsbInterface iface:(List<UsbInterface>)device.getActiveUsbConfiguration().getUsbInterfaces())
                	{
                	System.out.println("Interface config string: "+iface.getUsbConfiguration().getConfigurationString()+" Interface class: "+iface.getUsbInterfaceDescriptor().bInterfaceClass());
                	System.out.println("Interface:"+iface.getInterfaceString()+" Num Settings:"+iface.getNumSettings());
                	
                	UsbPipe pipe = null;
                	if(iface.getUsbInterfaceDescriptor().bInterfaceClass()==0x3) //HID
                		{
                		for(UsbEndpoint endpoint:(List<UsbEndpoint>)iface.getUsbEndpoints())
                			{
                			System.out.println((endpoint.getDirection()==UsbConst.ENDPOINT_DIRECTION_IN?"inbound":"outbound")+" endpoint of type "+endpoint.getUsbEndpointDescriptor().bDescriptorType()+" found.");
                			System.out.println("endpoint addr: 0x"+String.format("%x",endpoint.getUsbEndpointDescriptor().bEndpointAddress()));
                			if(endpoint.getDirection()==UsbConst.ENDPOINT_DIRECTION_OUT)
                				{pipe = endpoint.getUsbPipe();}
                			}//end for(endpoints)
                		System.out.println("Claiming interface...");
                		
                		Runtime.getRuntime().addShutdownHook(new Thread()
                			{//Safety measure to make sure we're cleaned up if things go sour.
                			public void run()
                				{
                				try{if(iface.isClaimed())iface.release();}
                				catch(Exception e){e.printStackTrace();}
                				}
                			});
                		result.add(new HIDDevice(device,iface,pipe));
                		}//end interface found
                	//iface.release();
                	/*for(UsbEndpoint endpoint:(List<UsbEndpoint>iface.getUsbEndpoints())
                		{
                		System.out.println("endpoint string: "+endpoint.getUsbEndpointDescriptor().);
                		}
                	for(UsbInterface setting:(List<UsbInterface>)iface.getSettings())
                		{
                		System.out.println("interface string: "+setting.getInterfaceString());
                		}*/
                	System.out.println("Setting:"+iface.getSettings());
                	}//end for(interfaces)
            	}//end if(HID)
        	
        	}}
        catch(UsbException e){if(!e.getMessage().contains("LIBUSB_ERROR_ACCESS"))throw e;}
        catch(UnsupportedEncodingException e){e.printStackTrace();}
        
        if (device.isUsbHub())
	        {
            UsbHub hub = (UsbHub) device;
            for (UsbDevice child : (List<UsbDevice>) hub.getAttachedUsbDevices())
	            {getDongles(child, result);}
	        }
		return result;
		}//end getDongles
	}//end Dongle
