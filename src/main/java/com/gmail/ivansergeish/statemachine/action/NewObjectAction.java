package com.gmail.ivansergeish.statemachine.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;

import com.gmail.ivansergeish.dto.FacesUseMaterial;
import com.gmail.ivansergeish.dto.VType;
import com.gmail.ivansergeish.dto.WaveFrontObject;
import com.gmail.ivansergeish.dto.WaveTexturedObject;
import com.gmail.ivansergeish.dto.point.Point3D;
import com.gmail.ivansergeish.service.ObjectDBService;
import com.gmail.ivansergeish.statemachine.event.Event;
import com.gmail.ivansergeish.statemachine.state.State;
import com.gmail.ivansergeish.utils.WaveFrontUtils;

@Service
public class NewObjectAction implements Action<State, Event> {
	
	private String fName;
	
	private WaveFrontObject object;

	private byte[] mtlFileBytes;
	
	@Autowired
	private WaveFrontUtils utils;
	
	@Autowired
	private ObjectDBService service;
	

	public void setfName(String fName) {
		this.fName = fName;
	}

	public void setObject(WaveFrontObject object) {
		this.object = object;
	}
	
	public WaveFrontObject getObject() {
		return this.object;
	}

	public void setMtlFileBytes(byte[] mtlFileBytes) {
		this.mtlFileBytes = mtlFileBytes;
	}

	List<Integer> getAllObjectVertexesNumbers(WaveFrontObject object, VType vType) {
		return object.getFaces().stream().flatMap((FacesUseMaterial fm) -> {
			List<Integer> vertexesIndexes = new ArrayList();
			for (String face : fm.getFaces()) {
				vertexesIndexes.addAll(utils.getVertexIndexesFromString(face, vType.getItemOrderInFace()));
			}
			return vertexesIndexes.stream();
		}).distinct().sorted().collect(Collectors.toList());
	}
	
	void processFaces(WaveFrontObject object, List<Integer> vertexesNums, VType vType) {
		object.getFaces().stream().forEach((FacesUseMaterial fm) -> {
			List<String> faces = new ArrayList();
			for (String face : fm.getFaces()) {
				faces.add(utils.processFaceToNewForm(vertexesNums, face, vType.getItemOrderInFace()));					
			}
			fm.setFaces(faces);
		});
	}
	
	void shiftAccordingToMassCenter(WaveFrontObject object) {
		Point3D center = utils.calcCenterMass(object.getVertexes());
		object.setPosition(center);
		List<String> vertexes = new ArrayList();
		for (String vertex : object.getVertexes()) {
			vertexes.add(utils.calcVertexAccordingToCenterMass(vertex, center));
		}
		object.setVertexes(vertexes);
	}
	
	void prepareNewObject(WaveFrontObject object, String objectName) {
		List<String> head = object.getHead();
		String mtlFileName = object.getMaterialsFileName();
		object = new WaveTexturedObject();
		object.setHead(head);
		//object.setName(utils.getObjectName(context.getEvent().getValue()));
		object.setMaterialsFileName(mtlFileName);
		String name = utils.getObjectName(objectName);
		object.setName(name.replace('.', '_'));			
	}
	
	@Override
	public void execute(StateContext<State, Event> context) {
			//first of all we should collect all the vertexes numbers from objects faces
		    List<Integer> vertexesNums = getAllObjectVertexesNumbers(object, VType.VERTEX);
		    List<Integer> vTextureNums = getAllObjectVertexesNumbers(object, VType.V_TEXTURE);
			try {
				List<String> vertexes = utils.getVertexesByNumbers(fName, vertexesNums, "v");
				List<String> vTextures = utils.getVertexesByNumbers(fName, vTextureNums, "vt");
				object.setVertexes(vertexes);
				((WaveTexturedObject)object).setVTextures(vTextures);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//now we should process faces
			processFaces(object, vertexesNums, VType.VERTEX);
			processFaces(object, vTextureNums, VType.V_TEXTURE);
			//calculate center mass of the object with z = 0 and rewrite all the vertexes according to it
			shiftAccordingToMassCenter(object);
			//save object to database

			try {
				service.save(object, fName, mtlFileBytes);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//TODO save object name to object names list
			//TODO check all the uploaded objects for equility
			//prepareNewObject(object, context.getEvent().getValue());
			List<String> head = object.getHead();
			String mtlFileName = object.getMaterialsFileName();
			object = new WaveTexturedObject();
			object.setHead(head);
			//object.setName(utils.getObjectName(context.getEvent().getValue()));
			object.setMaterialsFileName(mtlFileName);
			String name = utils.getObjectName(context.getEvent().getValue());
			object.setName(name.replace('.', '_'));			
		};
	}


