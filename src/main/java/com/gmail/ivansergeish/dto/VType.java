package com.gmail.ivansergeish.dto;

public enum VType {
    VERTEX("v", 0),
    V_TEXTURE("vt", 1);
	private String prefix;
	private int itemOrderInFace;
	VType(String prefix, int itemOrderInFace) {
		this.prefix = prefix;
		this.itemOrderInFace = itemOrderInFace;
	}
	public String getPrefix() {
		return this.prefix;
	}
	
	public int getItemOrderInFace() {
		return this.itemOrderInFace;
	}
}
