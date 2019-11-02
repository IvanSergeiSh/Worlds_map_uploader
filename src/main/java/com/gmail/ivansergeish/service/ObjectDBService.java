package com.gmail.ivansergeish.service;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gmail.ivansergeish.dto.WaveFrontObject;
import com.gmail.ivansergeish.utils.WaveFrontUtils;

import data.Description;
import data.Geometry;
import data.Location;
import data.Material;
import data.ObjectType;
import repository.DescriptionRepository;
import repository.GeometryRepository;
import repository.LocationRepository;
import repository.MaterialRepository;
/**
 * 
 * @author Ivan Shishkin
 *
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
public class ObjectDBService {
	private WaveFrontUtils utils = new WaveFrontUtils();
	@Autowired
	private DescriptionRepository descriptionRepository;
	@Autowired
	private LocationRepository locationRepository;
	@Autowired
	private GeometryRepository geometryRepository;
	@Autowired
	private MaterialRepository materialRepository;
	@Autowired
	private NamingStrategy namingService;

	
    public void save(WaveFrontObject object, String fName, byte[] materialBytes) throws IOException {
        String name = namingService.getName(object, fName);
        geometryRepository.save(getGeometry(name, object));
        Location location = getLocation(object);
        locationRepository.save(location);
        Description description = 
        		getDescriptionForSingleRadius(location.getObjectId(), name, ObjectType.THREE_D_OBJECT, 1);
        descriptionRepository.save(description);
        Material material = getMaterial(name, materialBytes);
        materialRepository.save(material);
    }
    //TODO implement changeable strategy
    Geometry getGeometry(String name, WaveFrontObject object) throws IOException {
    	return new Geometry(name, utils.writeToByteArray(object));
    }
    
    Location getLocation(WaveFrontObject object) {
    	
    	long id = locationRepository.getNextLocationId();
    	long objectId = locationRepository.getNextObjectId();
    	return new Location(id, objectId, object.getPosition().getX(),
    			object.getPosition().getY(),
    			object.getPosition().getZ(),
    			0,
    			object.getBettaOY(),
    			object.getAlphaOZ());
    }
    
    Description getDescriptionForSingleRadius (long objectId, String name, ObjectType type, double radius) {
    	long id = descriptionRepository.getNextDescriptionId();
    	return new Description(id, name, type, objectId, radius);
    }
    
    Material getMaterial(String name, byte[] bytes) {
    	return new Material(name, bytes);
    }
}
