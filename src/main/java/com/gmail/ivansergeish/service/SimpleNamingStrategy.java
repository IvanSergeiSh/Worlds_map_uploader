package com.gmail.ivansergeish.service;

import org.springframework.stereotype.Service;

import com.gmail.ivansergeish.dto.WaveFrontObject;

@Service
public class SimpleNamingStrategy implements NamingStrategy {

	@Override
	public String getName(WaveFrontObject object, String fName) {
		return (object.getName() + "_" + fName.hashCode());
	}

}
