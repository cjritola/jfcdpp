package com.ritolaaudio.jfcdpp.usb4java;

import java.io.IOException;

import javax.usb.UsbConst;
import javax.usb.UsbControlIrp;
import javax.usb.UsbDevice;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfacePolicy;
import javax.usb.UsbPipe;

import com.ritolaaudio.jfcdpp.TunerException;

public class HIDDevice
	{
	//private final UsbControlIrp irp;
	private final UsbDevice device;
	private final UsbInterface iface;
	private final UsbPipe pipe;
	
	public static void main(String [] args)
		{
		try{Dongles.getDongles();}
		catch(IOException e){e.printStackTrace();}
		}
	
	public HIDDevice(UsbDevice d, UsbInterface iface, UsbPipe pipe)
		{
		device=d;
		this.iface=iface;
		this.pipe=pipe;
		}//end constructor

	public void write(byte[] array)
		{
		claim();
		UsbControlIrp irp=pipe.createUsbControlIrp(
				(byte) (//UsbConst.REQUESTTYPE_DIRECTION_IN
						   UsbConst.REQUESTTYPE_DIRECTION_OUT
				          | UsbConst.REQUESTTYPE_TYPE_STANDARD
				          | UsbConst.REQUESTTYPE_RECIPIENT_DEVICE), 
				          UsbConst.REQUEST_SET_INTERFACE,
				         (short)0, 
				         (short)0);
		irp.setData(array);
		StringBuilder sb = new StringBuilder();
		for(byte b:irp.getData())
			{sb.append(b+" ");}
		System.out.println("sent "+sb);
		try{pipe.syncSubmit(irp);irp.waitUntilComplete();}
		catch(UsbException e){e.printStackTrace();}
		sb = new StringBuilder();
		for(byte b:irp.getData())
			{sb.append(b+" ");}
		System.out.println("response "+sb);
		release();
		}

	private void claim()
		{
		UsbInterfacePolicy policy = new UsbInterfacePolicy()
			{
			public boolean forceClaim(
					UsbInterface usbInterface)
				{return true;}//Take no prisoners.
			};
		try{iface.claim(policy);pipe.open();}
		catch(Exception e){e.printStackTrace();}
		}
	private void release()
		{
		try{pipe.abortAllSubmissions();pipe.close();if(iface.isClaimed())iface.release();}
		catch(Exception e){e.printStackTrace();}
		}
	
	public void read(byte[] result)
		{
		claim();
		UsbControlIrp irp=pipe.createUsbControlIrp(
		(byte) (UsbConst.REQUESTTYPE_DIRECTION_IN
				  | UsbConst.REQUESTTYPE_DIRECTION_OUT
		          | UsbConst.REQUESTTYPE_TYPE_STANDARD
		          | UsbConst.REQUESTTYPE_RECIPIENT_DEVICE), 
		          UsbConst.REQUEST_GET_CONFIGURATION, 
		         (short)0, 
		         (short)0);
		irp.setData(new byte[65]);
		try{pipe.syncSubmit(irp);}
		catch(UsbException e){e.printStackTrace();}
		//System.out.println("data:"+irp.getData()[0]);
		byte [] data = irp.getData();
		StringBuilder sb = new StringBuilder();
		for(byte b:data)
			{sb.append(b+" ");}
		System.out.println("read "+sb);
		System.arraycopy(data, 0, result, 0, result.length<data.length?result.length:data.length);
		release();
		}

	public void close() throws IOException
		{
		release();
		}

	public String getVendor() throws IOException
		{
		try{return device.getManufacturerString();}
		catch(UsbException e)
			{accessCheck(e); return null;}//The return is unreachable; compiler isn't smart enough to realize that.
		}

	public String getModel() throws IOException
		{
		try{return device.getManufacturerString();}
		catch(UsbException e)
			{accessCheck(e); return null;}//The return is unreachable; compiler isn't smart enough to realize that.
		}
	
	public String getSerial() throws IOException
		{
		try{return device.getSerialNumberString();}
		catch(UsbException e)
			{accessCheck(e); return null;}//The return is unreachable; compiler isn't smart enough to realize that.
		}
	
	private void accessCheck(UsbException e) throws IOException
		{
		if(!e.getMessage().contains("LIBUSB_ERROR_ACCESS"))throw new IOException(e); 
		else throw new DeviceAccessException("You lack access priveleges to use this device. (LIBUSB_ERROR_ACCESS)"
				+(System.getProperty("os.name").toUpperCase().contains("LINUX")?"Double check your udev rules.":""),e);
		}//end accessCheck
	}//end HIDDevice
