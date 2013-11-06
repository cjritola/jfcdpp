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
package com.ritolaaudio.jfcdpp.usb4java;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Logger;

import com.ritolaaudio.jfcdpp.FCDPP;
import com.ritolaaudio.jfcdpp.IFFilter;
import com.ritolaaudio.jfcdpp.RFFilter;
import com.ritolaaudio.jfcdpp.TunerException;
import com.ritolaaudio.jfcdpp.internal.Commands;

public class FCDPPImpl implements FCDPP
	{
	private HIDDevice device;
	Logger log=Logger.getGlobal();
	public FCDPPImpl(HIDDevice dev)
		{this.device=dev;}
	
	public boolean isLNAOn(){return getByte(device,Commands.GET_LNA_GAIN)==1;}
	public int getFrequency(){return getInt(device,Commands.GET_FREQUENCY_HZ);}
	public void setFrequency(int freq){setInt(device,Commands.SET_FREQUENCY_HZ,freq);}
	public void setLNA(boolean state){setByte(device,Commands.SET_LNA_GAIN,state?(byte)1:(byte)0);}
	public int getIFGain(){return getByte(device,Commands.GET_IF_GAIN);}
	public void setIFGain(int gainInDecibels){setByte(device,Commands.SET_IF_GAIN,(byte)gainInDecibels);}
	public void setMixerGain(boolean on){setByte(device,Commands.SET_MIXER_GAIN,on?1:(byte)0);}
	public boolean getMixerGain(){return getByte(device,Commands.GET_MIXER_GAIN)!=0;}
	public void setBiasTee(boolean on){setByte(device,Commands.SET_BIAS_TEE,(byte)1);}
	public boolean isBiasTeeOn(){return getByte(device,Commands.GET_BIAS_TEE)!=0;}
	public void setIFFilter(IFFilter filterType)
		{setByte(device,Commands.SET_IF_FILTER,(byte)filterType.ordinal());}
	public void reset(){setByte(device,Commands.RESET,(byte)0);}
	public IFFilter getIFFilter()
		{
		int index=1337;
		try{return IFFilter.values()[index=getByte(device,Commands.GET_IF_FILTER)];}
		catch(ArrayIndexOutOfBoundsException e)
			{log.severe("Value returned by GET_IF_FILTER is: "+index+" ...which is out of bounds for the ordinal array for RFFilter, which is to be less than "+IFFilter.values().length+".\n " +
					"Returning null. Trouble ahead.");}
		return null;
		}
	public RFFilter getRFFilter()
		{
		int index=1337;
		try{return RFFilter.values()[index=getByte(device,Commands.GET_RF_FILTER)];}
		catch(ArrayIndexOutOfBoundsException e)
			{log.severe("Value returned by GET_RF_FILTER is: "+index+" ...which is out of bounds for the ordinal array for RFFilter, which is to be less than "+RFFilter.values().length+".\n " +
					"Returning null. Trouble ahead.");}
		return null;
		}
	public void setRFFilter(RFFilter filterType)
		{
		setByte(device,Commands.SET_RF_FILTER,(byte)filterType.ordinal());
		}//end setRFFilter(...)
	
	private int getInt(HIDDevice device, Commands cmd)
		{
		ByteBuffer payload = ByteBuffer.allocate(65);
		payload.order(ByteOrder.LITTLE_ENDIAN);
		//payload.put((byte)0);
		payload.put(cmd.getCommandID());
		
		try{
		device.write(payload.array());
		byte [] result = new byte[64];
		device.read(result);
		ByteBuffer bb= ByteBuffer.wrap(result).order(ByteOrder.LITTLE_ENDIAN);
		//bb.position(2);
		return bb.getInt();
		}catch(Exception e){e.printStackTrace(); return 666666;}
		}
	private byte getByte(HIDDevice device, Commands cmd)
		{
		ByteBuffer payload = ByteBuffer.allocate(65);
		payload.order(ByteOrder.LITTLE_ENDIAN);
		//payload.put((byte)0);
		payload.put(cmd.getCommandID());
		
		try{
		device.write(payload.array());
		byte [] result = new byte[64];
		device.read(result);
		ByteBuffer bb= ByteBuffer.wrap(result).order(ByteOrder.LITTLE_ENDIAN);
		//bb.position(2);
		return bb.get();
		}catch(Exception e){e.printStackTrace();return 0;}
		}
	private void setInt(HIDDevice device, Commands cmd, int val)
		{
		ByteBuffer payload = ByteBuffer.allocate(65);
		payload.order(ByteOrder.LITTLE_ENDIAN);
		//payload.put((byte)0);
		payload.put(cmd.getCommandID());
		payload.putInt(val);
		
		try{
		device.write(payload.array());
		byte [] result = new byte[64];
		device.read(result);
		ByteBuffer bb= ByteBuffer.wrap(result).order(ByteOrder.LITTLE_ENDIAN);
		bb.position(2);
		}catch(Exception e){e.printStackTrace();}
		}
	private void setByte(HIDDevice device, Commands cmd, byte val)
		{
		ByteBuffer payload = ByteBuffer.allocate(65);
		payload.order(ByteOrder.LITTLE_ENDIAN);
		//payload.put((byte)0);
		payload.put(cmd.getCommandID());
		payload.put(val);
		
		try{
		device.write(payload.array());
		byte [] result = new byte[64];
		device.read(result);
		ByteBuffer bb= ByteBuffer.wrap(result).order(ByteOrder.LITTLE_ENDIAN);
		bb.position(2);
		}catch(Exception e){e.printStackTrace();}
		}
	
	public void close()
		{
		try{device.close();device=null;}
		catch(IOException e){log.severe(e.getLocalizedMessage());}
		}

	public String getVendor() throws IOException
		{
		return device.getVendor();
		}

	public String getModel() throws IOException
		{
		return device.getModel();
		}

	public String getSerial() throws IOException
		{
		return device.getSerial();
		}
	}//FCDPP
