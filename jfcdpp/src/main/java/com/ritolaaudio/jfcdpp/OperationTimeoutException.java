package com.ritolaaudio.jfcdpp;

public class OperationTimeoutException extends TunerException
	{
	public OperationTimeoutException(){super();}
	public OperationTimeoutException(Exception cause){super(cause);}
	public OperationTimeoutException(String message){super(message);}
	}
