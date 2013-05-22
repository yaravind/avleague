package com.aravind.avl.service;

public class CannotCreateLeagueException extends Exception
{

	public CannotCreateLeagueException(Exception e1)
	{
		super(e1);
	}

	public CannotCreateLeagueException(String msg, Exception e1)
	{
		super(msg, e1);
	}
}
