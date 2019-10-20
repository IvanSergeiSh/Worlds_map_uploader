package com.gmail.ivansergeish.statemachine.state;

public enum State {
	START,
	FILE_HEAD,
	STRING_TO_SKIP,
	FINISH,
    OBJECT,
    VERTEX,
    NEW_FACES_GROUP,
    NEW_FACE,
    UNKNOWN_STR,
    END_OF_FILE
    
}
