package com.aravind.avl.controller;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import com.aravind.avl.domain.Player;
import com.aravind.avl.domain.PlayerRepository;

public class PlayerPropertyEditor extends PropertyEditorSupport
{
	@Autowired
	PlayerRepository playerRepo;

	@Override
	public String getAsText()
	{
		System.err.println("get as txt");
		return ((Player) getValue()).getNodeId().toString();
	}

	@Override
	public void setAsText(String incomingId) throws IllegalArgumentException
	{
		System.err.println(incomingId);
		Player player = playerRepo.findOne(Long.valueOf(incomingId));
		setValue(player);
	}
}