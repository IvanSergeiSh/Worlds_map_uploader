package com.gmail.ivansergeish.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

import com.gmail.ivansergeish.dto.WaveFrontObject;
import com.gmail.ivansergeish.statemachine.event.Event;
import com.gmail.ivansergeish.statemachine.state.State;
import com.gmail.ivansergeish.utils.WaveFrontUtils;

@Component
public class Reader {
    private List<WaveFrontObject> objects = new ArrayList();
    
    @Autowired
    private StateMachine<State, Event> machine;
    
    public List<WaveFrontObject> readFile(String fName) throws IOException {
    	File file = new File(fName);    	
    	BufferedReader fReader = Files.newBufferedReader(file.toPath());
    	String str = "Hello!";
    	
    	while (str != null) {
    		str = fReader.readLine();
        	machine.sendEvent(newEventByString(str, machine.getState().getId()));
    	}
    	// send EOF event
    	machine.sendEvent(newEventByString(str, machine.getState().getId()));
    	return objects;
    }
    
    private Event newEventByString(String str, State state) {
    	Event event = Event.SKIP;
    	event.setValue("skip");
    	if (str != null && !str.isEmpty()) {
    		if (str.charAt(0) == 'o' && (state == State.START || state == State.STRING_TO_SKIP || state == State.FILE_HEAD ) ) {
    			event = Event.FIRST_OBJECT;
    			event.setValue(str);
    		}
    		if (str.charAt(0) == 'o' && state == State.NEW_FACE) {
    			event = Event.NEW_OBJECT;
    			event.setValue(str);
    		}    		
    		if (str.charAt(0) == 'v' && state == State.OBJECT) {
    			event = Event.FIRST_VERTEX;
    			event.setValue(str);    			
    		}
    		if (str.charAt(0) == 'v' && state == State.VERTEX) {
    			event = Event.NEW_VERTEX;
    			event.setValue(str);    			
    		}    		
    		if (str.charAt(0) == 'f' && state == State.NEW_FACES_GROUP) {
    			event = Event.FIRST_FACE;
    			event.setValue(str);     			
    		}
    		if (str.charAt(0) == 'f' && state == State.UNKNOWN_STR) {
    			event = Event.FIRST_FACE;
    			event.setValue(str);     			
    		}    		
    		if (str.charAt(0) == 'f' && state == State.NEW_FACE) {
    			event = Event.NEW_FACE;
    			event.setValue(str);     			
    		}
    		if (str.split(" ")[0].equals("usemtl")) {
       			event = Event.USE_MTL;
       			event.setValue(str);    			
    		}
    		if (str.contains("mtllib")) {
    			event = Event.FILE_HEAD;
    			event.setValue(str);
    		}    		
    		if (event ==Event.SKIP && state == State.NEW_FACES_GROUP) {
    			event = Event.UNKNOWN_STR;
    			event.setValue(str);
    		}
    	} else if (str == null) {
    		event = Event.EOF;
    	}
		return event;
    }
}
