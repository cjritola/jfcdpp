package com.ritolaaudio.jfcdpp.usb4javaD;

import java.io.IOException;

public class DeviceAccessException extends IOException
	{
	public DeviceAccessException(){super();}
	public DeviceAccessException(String msg){super(msg);}
	public DeviceAccessException(Exception cause){super(cause);}
	public DeviceAccessException(String msg, Exception cause){super(msg,cause);}
	}
