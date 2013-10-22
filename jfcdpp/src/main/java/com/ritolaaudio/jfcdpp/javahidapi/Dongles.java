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
import java.util.List;

import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDDeviceInfo;
import com.codeminders.hidapi.HIDDeviceNotFoundException;
import com.codeminders.hidapi.HIDManager;

public class Dongles
	{
	static {com.codeminders.hidapi.ClassPathLibraryLoader.loadNativeHIDLibrary();}
	//private static Logger log = Logger.getGlobal();
	private static HIDManager manager;
	
	public static List<FCDPP> getDongles() throws IOException
		{
		ArrayList<FCDPP> result = new ArrayList<FCDPP>();
		//try
		//	{
			if(manager==null)manager = HIDManager.getInstance();
			for(HIDDeviceInfo dev:manager.listDevices())
				{
				if(dev.getProduct_string()!=null)
					{
					if(dev.getProduct_string().contains("FUNcube Dongle") && dev.getManufacturer_string().contains("Hanlincrest Ltd."))
						{try{
							//Found it.
							HIDDevice device = manager.openByPath(dev.getPath());
							FCDPP dongle = new FCDPP(device);
							result.add(dongle);
							}
						catch(HIDDeviceNotFoundException e)
							{throw new AssertionError("Device was listed yet request fails to provide it.");}
						}
					}//end if(!null)
				}//end for(devices)
		//	}//end try{}
		//catch(Exception e){e.printStackTrace();}
		return result;
		}//end getDongles
	
	public static void release()
		{
		if(manager==null)return;
		manager.release();manager=null;
		}
	}//end Dongle
