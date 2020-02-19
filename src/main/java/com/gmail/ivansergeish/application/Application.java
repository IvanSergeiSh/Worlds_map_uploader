package com.gmail.ivansergeish.application;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.statemachine.StateMachine;

import com.gmail.ivansergeish.reader.Reader;
import com.gmail.ivansergeish.statemachine.event.Event;

public class Application {
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(StateMachineConfig.class, JpaConfig.class, ServiceConfig.class);
		if (args.length < 1) {
			throw new IllegalArgumentException("No filename in commandline");
		}
		String fName = args[0];		
		String mtlFileName = args[1];
		//TODO read textures 
		Reader reader = context.getBean(Reader.class);
		StateMachineConfig stateMachine = context.getBean(StateMachineConfig.class);
		stateMachine.setfName(fName);
		stateMachine.setMtlFileName(mtlFileName);
		try {
			reader.readFile(fName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
