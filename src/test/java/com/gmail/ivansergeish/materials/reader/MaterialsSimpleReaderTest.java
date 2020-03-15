package com.gmail.ivansergeish.materials.reader;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gmail.ivansergeish.configuration.ConfigurationTest;
//import com.gmail.ivansergeish.service.ObjectDBService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConfigurationTest.class})
@PropertySource("classpath:application-test.properties")
public class MaterialsSimpleReaderTest {
	@Autowired
	private MaterialSimpleReader reader;
	
	//@Spy
	//private ObjectDBService service;
	
	@Test 
	public void testPathToUrl() {
		Assert.assertEquals("https://10.42.0.1:8443/api-launcher-1.0.0.alpha-RELEASE/map/sprite/sprite.jpg",reader.pathToUrl("c:\\\\abc\\sprite.jpg"));
		Assert.assertEquals("https://10.42.0.1:8443/api-launcher-1.0.0.alpha-RELEASE/map/sprite/sprite.jpg",reader.pathToUrl("/home/abc/sprite.jpg"));
		Assert.assertEquals("https://10.42.0.1:8443/api-launcher-1.0.0.alpha-RELEASE/map/sprite/sprite.jpg",reader.pathToUrl("https:\\\\abc\\sprite.jpg"));
	}
	
}
