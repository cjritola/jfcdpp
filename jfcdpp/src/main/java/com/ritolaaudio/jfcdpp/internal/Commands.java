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

public enum Commands
	{
	SET_FREQUENCY(100),//3 byte unsigned little endian frequency in khz
	SET_FREQUENCY_HZ(101),//4 in actual hertz
	GET_FREQUENCY_HZ(102),//Returns 4 byte unsigned little endian in Hz
	SET_LNA_GAIN(110),//One byte: 1=on, 0=off
	SET_RF_FILTER(113),//One byte enum
	SET_MIXER_GAIN(114),//One byte 1 on, 0 off
	SET_IF_GAIN(117),//Send one byte value, [0,59]dB
	SET_IF_FILTER(122),//One byte enum
	SET_BIAS_TEE(126),//One byte 1 on 0 off.
	GET_LNA_GAIN(150),//One byte, 1 on 0 off.
	GET_RF_FILTER(153),//One byte enum 
	GET_MIXER_GAIN(154),//Byte, 1 on 0 off
	GET_IF_GAIN(157),//Returns one byte 0 to 59dB
	GET_IF_FILTER(162),//Returns one byte enum
	GET_BIAS_TEE(166),//Returns one byte 1 on 0 off
	RESET(255);//Reset to bootloader
	
	byte commandID;
	Commands(int cmdID)
		{commandID=(byte)cmdID;}
	
	public byte getCommandID(){return commandID;}
	}//end FCDCommands
