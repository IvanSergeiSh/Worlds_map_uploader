package com.gmail.ivansergeish.configuration;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.gmail.ivansergeish.service.NamingStrategy;
import com.gmail.ivansergeish.service.ObjectDBService;

import repository.DescriptionRepository;
import repository.GeometryRepository;
import repository.LocationRepository;
import repository.MaterialRepository;
import repository.SpriteRepository;

@Configuration
@ComponentScan("com.gmail.ivansergeish.materials.reader")
@ComponentScan("com.gmail.ivansergeish.utils")
@PropertySource("classpath:application-test.properties")
public class ConfigurationTest {
	
	@Bean
	public ObjectDBService objectDBService() {
		return Mockito.mock(ObjectDBService.class);
	}
	
	@Bean
	public DescriptionRepository descriptionRepository() {
		return Mockito.mock(DescriptionRepository.class);
	}
	
	@Bean
	public LocationRepository locationRepository() {
		return Mockito.mock(LocationRepository.class);
	}
	
	@Bean
	public GeometryRepository geometryRepository() {
		return Mockito.mock(GeometryRepository.class);
	}

	@Bean
	public MaterialRepository materialRepository() {
		return Mockito.mock(MaterialRepository.class);
	}	
	
	@Bean
	public NamingStrategy namingStrategy() {
		return Mockito.mock(NamingStrategy.class);
	}
	
	@Bean
	public SpriteRepository spriteRepository() {
		return Mockito.mock(SpriteRepository.class);
	}	

}
