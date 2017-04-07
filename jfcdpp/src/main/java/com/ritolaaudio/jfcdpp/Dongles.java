package com.ritolaaudio.jfcdpp;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;

public class Dongles
	{
	public static void getDongles(Collection<FCDPP> collectionToAppend, Class<?> backendToUse) throws IOException
		{
		try
			{
			Method method = backendToUse.getMethod("getDongles", new Class[]{Collection.class});
			method.invoke(null, collectionToAppend);
			}
		catch(Exception e)
			{
			if(e instanceof IOException)
				{throw (IOException)e;}
			e.printStackTrace();
			}
		}//end getDongles(...)
	}//end Dongles
