package com.gmail.ivansergeish.service;

import org.springframework.stereotype.Service;

import com.gmail.ivansergeish.dto.WaveFrontObject;

@Service
public class SimpleNamingStrategy implements NamingStrategy {
	//TODO remove hardcoded conditions of substring length
	@Override
	public String getName(WaveFrontObject object, String fName) {
		return (object.getName() + "_" + Integer.toString(fName.hashCode()).substring(0, 5));
	}

}
