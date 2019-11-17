package com.gmail.ivansergeish.dto;

import java.util.ArrayList;
import java.util.List;

public class WaveTexturedObject extends WaveFrontObject {
	private List<String> vTextures;
	
	public WaveTexturedObject() {
		super();
		this.setVTextures(new ArrayList());
	}
	
	public WaveTexturedObject(List<String> head, 
			String name, 
			List<String> vertexes, 
			List<FacesUseMaterial> faces,
			List<String> vTextures) {
		super(head, name, vertexes, faces);
		this.setVTextures(vTextures);
	}

	public List<String> getVTextures() {
		return vTextures;
	}

	public void setVTextures(List<String> vTextures) {
		this.vTextures = vTextures;
	}
	

}
