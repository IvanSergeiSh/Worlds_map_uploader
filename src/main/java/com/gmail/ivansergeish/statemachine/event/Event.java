package com.gmail.ivansergeish.statemachine.event;

public enum Event {
	SKIP,
	FILE_HEAD,
    FIRST_OBJECT,
    NEW_OBJECT,
    FIRST_VERTEX,
    FIRST_TEXTURE_VERTEX,
    NEW_VERTEX,
    NEW_TEXTURE_VERTEX,
    USE_MTL,
    UNKNOWN_STR,
    FIRST_FACE,
    NEW_FACE,
    EOF;
	
	private String value;
	
	public void setValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return this.value;
	}
}
