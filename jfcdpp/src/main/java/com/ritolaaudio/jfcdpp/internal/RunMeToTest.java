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
package com.ritolaaudio.jfcdpp.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ritolaaudio.jfcdpp.Dongles;
import com.ritolaaudio.jfcdpp.FCDPP;
import com.ritolaaudio.jfcdpp.IFFilter;
import com.ritolaaudio.jfcdpp.RFFilter;
import com.ritolaaudio.jfcdpp.javahidapi.FCDPPImpl;

public class RunMeToTest
	{

	/**
	 * @param args
	 * @since Jun 22, 2013
	 */
	public static void main(String[] args)
		{
		new RunMeToTest();
		}//end main()

	public RunMeToTest()
		{
		List<FCDPP> dongles = new ArrayList<FCDPP>();
		try {Dongles.getDongles(dongles,com.ritolaaudio.jfcdpp.javahidapi.Dongles.class);}
		catch(IOException e){e.printStackTrace();return;}
		
		if(!dongles.isEmpty())
			{
			FCDPP dongle=dongles.get(0);
			dongle.setFrequency(91900000);
			dongle.setLNA(true);
			dongle.setIFGain(6);
			dongle.setMixerGain(true);
			System.out.println("RF Filter: "+dongle.getRFFilter());
			dongle.setRFFilter(RFFilter.FILTER_875_2000);
			dongle.setIFFilter(IFFilter.FILTER_200KHZ);
			System.out.println("Frequency: "+dongle.getFrequency());
			System.out.println("Mixer gain: "+dongle.getMixerGain());
			dongle.close();
			System.out.println("Closed.");
			}
		else{System.out.println("Failed to find any FunCubes.");}
		//Dongles.release();
		System.out.println("Exiting...");
		}//end RunMeToTest
	}//end RunMeToTest
