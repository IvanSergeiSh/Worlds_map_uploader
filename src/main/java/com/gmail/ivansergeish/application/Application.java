package com.gmail.ivansergeish.application;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.statemachine.StateMachine;

import com.gmail.ivansergeish.reader.Reader;
import com.gmail.ivansergeish.statemachine.event.Event;

public class Application {
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(StateMachineConfig.class);
		Reader reader = context.getBean(Reader.class);
		try {
			reader.readFile("/home/ivan/projects/projects/ObjFileLoader/src/test/resources/cottage2.obj");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
