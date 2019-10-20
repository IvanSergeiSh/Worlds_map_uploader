package com.gmail.ivansergeish.service;

import com.gmail.ivansergeish.dto.WaveFrontObject;

public interface NamingStrategy {
	/**
	 * Strategy creates name of object according to object name in *.obj file
	 *  and name of file
	 * @param object
	 * @param fName
	 * @return
	 */
    public String getName(WaveFrontObject object, String fName);
}
