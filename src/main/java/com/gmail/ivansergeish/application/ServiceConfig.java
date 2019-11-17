package com.gmail.ivansergeish.application;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.gmail.ivansergeish.service", "com.gmail.ivansergeish.statemachine.action"})
public class ServiceConfig {

}
