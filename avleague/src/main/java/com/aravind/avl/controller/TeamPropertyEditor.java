package com.aravind.avl.controller;

import java.beans.PropertyEditorSupport;

public class TeamPropertyEditor extends PropertyEditorSupport
{
	@Override
	public String getAsText()
	{
		System.err.println("TeamPropertyEditor get as txt");
		return "";
	}

	@Override
	public void setAsText(String incomingId) throws IllegalArgumentException
	{
		System.err.println("TeamPropertyEditor " + incomingId);
	}
}
