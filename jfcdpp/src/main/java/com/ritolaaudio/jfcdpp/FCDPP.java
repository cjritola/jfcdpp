package com.ritolaaudio.jfcdpp;

import java.io.IOException;

public interface FCDPP
	{
	public boolean isLNAOn();
	public int getFrequency();
	public void setFrequency(int freq);
	public void setLNA(boolean state);
	public int getIFGain();
	public void setIFGain(int gainInDecibels);
	public void setMixerGain(boolean on);
	public boolean getMixerGain();
	public void setBiasTee(boolean on);
	public boolean isBiasTeeOn();
	public void setIFFilter(IFFilter filterType);
	public void reset();
	public IFFilter getIFFilter();
	public RFFilter getRFFilter();
	public void setRFFilter(RFFilter filterType);
	
	public void close();

	public String getVendor() throws IOException;

	public String getModel() throws IOException;

	public String getSerial() throws IOException;
	}
