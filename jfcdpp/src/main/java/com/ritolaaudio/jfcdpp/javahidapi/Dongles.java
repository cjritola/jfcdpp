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
package com.ritolaaudio.jfcdpp.javahidapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDDeviceInfo;
import com.codeminders.hidapi.HIDDeviceNotFoundException;
import com.codeminders.hidapi.HIDManager;
import com.ritolaaudio.jfcdpp.FCDPP;

public class Dongles
	{
	static {com.codeminders.hidapi.ClassPathLibraryLoader.loadNativeHIDLibrary();}
	//private static Logger log = Logger.getGlobal();
	private static HIDManager manager;
	
	public static void getDongles(Collection<FCDPP> result) throws IOException
		{
		//ArrayList<FCDPPImpl> result = new ArrayList<FCDPPImpl>();
		//try
		//	{
			System.out.println("Performing dongle scan...");
			if(manager==null)manager = HIDManager.getInstance();
			if(manager==null){System.err.println("HIDManager.getInstance() passed a null pointer. Unexpected behavior; cannot continue. Freaking out...");throw new NullPointerException("HIDManager.getInstance() passed a null pointer. Unexpected behavior; cannot continue.");}
			HIDDeviceInfo [] devs = manager.listDevices();
			if(devs==null){return;}//Complete lack of devices results in null.
			for(HIDDeviceInfo dev:manager.listDevices())
				{
				if(dev.getProduct_string()!=null)
					{
					if(dev.getProduct_string().contains("FUNcube Dongle") && dev.getManufacturer_string().contains("Hanlincrest Ltd."))
						{try{
							//Found it.
							HIDDevice device = manager.openByPath(dev.getPath());
							FCDPPImpl dongle = new FCDPPImpl(device);
							result.add(dongle);
							}
						catch(HIDDeviceNotFoundException e)
							{throw new AssertionError("Device was listed yet request fails to provide it. Impossible situation encountered. Universe collapsing...");}
						}
					}//end if(!null)
				}//end for(devices)
		//	}//end try{}
		//catch(Exception e){e.printStackTrace();}
		}//end getDongles
	
	public static void release()
		{
		if(manager==null)return;
		manager.release();manager=null;
		}
	}//end Dongle
