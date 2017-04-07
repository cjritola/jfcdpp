package com.ritolaaudio.jfcdpp.autoselect;

import java.io.IOException;
import java.util.Collection;

import com.ritolaaudio.jfcdpp.FCDPP;

public class Dongles
	{
	public static void getDongles(Collection<FCDPP> result) throws IOException
		{
		final String os = System.getProperty("os.name").toUpperCase();
		if(os.contains("WINDOWS"))
			{com.ritolaaudio.jfcdpp.javahidapi.Dongles.getDongles(result);}
		else
			{com.ritolaaudio.jfcdpp.usb4java.Dongles.getDongles(result);}
		}//end getDongles
	}
