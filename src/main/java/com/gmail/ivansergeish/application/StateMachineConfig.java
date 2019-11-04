package com.gmail.ivansergeish.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import com.gmail.ivansergeish.dto.FacesUseMaterial;
import com.gmail.ivansergeish.dto.Point3D;
import com.gmail.ivansergeish.dto.WaveFrontObject;
import com.gmail.ivansergeish.service.ObjectDBService;
import com.gmail.ivansergeish.statemachine.event.Event;
import com.gmail.ivansergeish.statemachine.state.State;
import com.gmail.ivansergeish.utils.WaveFrontUtils;

import repository.DescriptionRepository;
import repository.GeometryRepository;
import repository.LocationRepository;
import repository.MaterialRepository;

@Configuration
@ComponentScan({"com.gmail.ivansergeish.reader", "com.gmail.ivansergeish.utils"})
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<State, Event> {
	
	private Map<String, WaveFrontObject> objects = new HashMap();
	
	private WaveFrontObject object = new WaveFrontObject();
	
	private WaveFrontUtils utils = new WaveFrontUtils();
	
	private FacesUseMaterial facesGroup = new FacesUseMaterial();
	
	private String currentObjectName;
	
	private String currentFacesGroup;
	
    private byte[] mtlFileBytes;
	
	@Autowired
	private ObjectDBService service;;
	
	private String fName;
    //@Override

	public String getfName() {
		return fName;
	}

	public StateMachineConfig() {}
    
    public void configure(final StateMachineConfigurationConfigurer<State, Event> config) throws Exception {
        config
                .withConfiguration()
                .autoStartup(true);
    }
    public void setObjects(Map<String, WaveFrontObject> objects) {
		this.objects = objects;
	}
	public void setObject(WaveFrontObject object) {
		this.object = object;
	}
	public void setUtils(WaveFrontUtils utils) {
		this.utils = utils;
	}
	public void setFacesGroup(FacesUseMaterial facesGroup) {
		this.facesGroup = facesGroup;
	}
	public void setCurrentObjectName(String currentObjectName) {
		this.currentObjectName = currentObjectName;
	}
	public void setCurrentFacesGroup(String currentFacesGroup) {
		this.currentFacesGroup = currentFacesGroup;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	@Override
    public void configure(final StateMachineStateConfigurer<State, Event> states) throws Exception {
        states
                .withStates()
                .initial(State.START).
                state(State.FILE_HEAD).
                state(State.OBJECT).
                state(State.VERTEX).
                state(State.NEW_FACES_GROUP).
                state(State.UNKNOWN_STR).
                state(State.NEW_FACE).
                end(State.END_OF_FILE);
    }    
    public void configure(
            StateMachineTransitionConfigurer<State, Event> transitions)
            throws Exception {

        transitions.withExternal()
                   .source(State.START)
                   .target(State.FILE_HEAD)
                   .event(Event.FILE_HEAD)
                   .action(addObjectHead())
                   .and()
                   .withExternal()
                   .source(State.FILE_HEAD)
                   .target(State.OBJECT)
                   .event(Event.FIRST_OBJECT)
                   .action(firstObjectAction())
                   .and()
                   .withExternal()
                   .source(State.STRING_TO_SKIP)
                   .target(State.STRING_TO_SKIP)
                   .event(Event.SKIP)
                   .and()
                   .withExternal()
                   .source(State.STRING_TO_SKIP)
                   .target(State.OBJECT)
                   .event(Event.FIRST_OBJECT)
                   .action(firstObjectAction())
                   .and()
                   .withExternal()  
                   .source(State.START)
                   .target(State.OBJECT)
                   .event(Event.FIRST_OBJECT)
                   .action(firstObjectAction())
                   .and()
                   .withExternal()
                   .source(State.OBJECT)
                   .target(State.VERTEX)
                   .event(Event.FIRST_VERTEX)
                   .action(firstVertexAction())
                   .and()
                   .withExternal()
                   .source(State.VERTEX)
                   .target(State.VERTEX)
                   .event(Event.NEW_VERTEX)
                   .action(newVertexAction())
                   .and()
                   .withExternal()
                   .source(State.VERTEX)
                   .target(State.NEW_FACES_GROUP)
                   .event(Event.USE_MTL)
                   .action(facesGroupAction())
                   .and()
                   .withExternal()
                   .source(State.NEW_FACES_GROUP)
                   .target(State.UNKNOWN_STR)
                   .event(Event.UNKNOWN_STR)
                   .action(unknownStrAction())
                   .and()
                   .withExternal()
                   .source(State.UNKNOWN_STR)
                   .target(State.NEW_FACE)
                   .event(Event.FIRST_FACE)
                   .action(newFaceAction())
                   .and()
                   .withExternal()
                   .source(State.NEW_FACE)
                   .target(State.NEW_FACE)
                   .event(Event.NEW_FACE)
                   .action(newFaceAction())
                   .and()
                   .withExternal()
                   .source(State.NEW_FACES_GROUP)
                   .target(State.NEW_FACE)
                   .event(Event.NEW_FACE)
                   .action(newFaceAction())
                   .and()
                   .withExternal()
                   .source(State.NEW_FACE)
                   .target(State.OBJECT)
                   .event(Event.NEW_OBJECT)
                   .action(newObjectAction())
                   .and()
                   .withExternal()
                   .source(State.NEW_FACE)
                   .target(State.END_OF_FILE)
                   .event(Event.EOF)
                   .action(newObjectAction());
    }
    
    private Action<State, Event> firstObjectAction() {  
		return context -> {
			System.out.println("first object");
			List<String> head = object.getHead();
			object = new WaveFrontObject();
			object.setHead(head);
			String name = utils.getObjectName(context.getEvent().getValue());
			object.setName(name.replace('.', '_'));
		};
    }

    private Action<State, Event> newObjectAction() {
    	//TODO process old object, save it and create a new one
		return context -> {
			//TODO first of all we should collect all the vertexes numbers from objects faces
			List<Integer> vertexesNums = object.getFaces().stream().flatMap((FacesUseMaterial fm) -> {
				List<Integer> vertexesIndexes = new ArrayList();
				for (String face : fm.getFaces()) {
					vertexesIndexes.addAll(utils.getVertexIndexesFromString(face));
				}
				return vertexesIndexes.stream();
			}).distinct().sorted().collect(Collectors.toList());
			try {
				List<String> vertexes = utils.getVertexesByNumbers(fName, vertexesNums);
				object.setVertexes(vertexes);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//TODO now we should process faces
			object.getFaces().stream().forEach((FacesUseMaterial fm) -> {
				List<String> faces = new ArrayList();
				for (String face : fm.getFaces()) {
					faces.add(utils.processFaceToNewForm(vertexesNums, face));					
				}
				fm.setFaces(faces);
			});

			//TODO calculate center mass of the object with z = 0 and rewrite all the vertexes according to it
			Point3D center = utils.calcCenterMass(object.getVertexes());
			object.setPosition(center);
			
			//TODO calculate shift according to center of mass
			List<String> vertexes = new ArrayList();
			for (String vertex : object.getVertexes()) {
				vertexes.add(utils.calcVertexAccordingToCenterMass(vertex, center));
			}
			object.setVertexes(vertexes);
			//TODO save object to database !!!!!!!!!!
			//geometryRepository.save(entities)
			try {
				service.save(object, fName, mtlFileBytes);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				//TODO save object name to object names list
			//TODO check all the uploaded objects for equility
			//utils.getNewFaceRepresentation(vertexesNums, faceVertexes)
			//utils.getVertexIndexesFromString(str)
			List<String> head = object.getHead();
			String mtlFileName = object.getMaterialsFileName();
			object = new WaveFrontObject();
			object.setHead(head);
			//object.setName(utils.getObjectName(context.getEvent().getValue()));
			object.setMaterialsFileName(mtlFileName);
			String name = utils.getObjectName(context.getEvent().getValue());
			object.setName(name.replace('.', '_'));			
		};
    }
    
    private Action<State, Event> firstVertexAction() {  
		return context -> {
		};
    }
    private Action<State, Event> newVertexAction() {  
    	
		return context -> {
			System.out.println("new vertex");
		};
    }    
    private Action<State, Event> facesGroupAction() {  
		return context -> {
			facesGroup = new FacesUseMaterial();
			facesGroup.setUsemtl(utils.getUseMtlName(context.getEvent().getValue()));
			object.getFaces().add(facesGroup);
		};
    }
    private Action<State, Event> unknownStrAction() {  
		return context -> {
			facesGroup.getHead().add(context.getEvent().getValue());
			System.out.println(" unknown str");
		};
    }
    private Action<State, Event> newFaceAction() {  
		return context -> {
			facesGroup.getFaces().add(context.getEvent().getValue());
			System.out.println("newFaceAction");
		};
    }
    private Action<State, Event> eofAction() {  
		return context -> {
			//TODO save last object
			System.out.println("eofAction");
		};
    }
    
    private Action<State, Event> addObjectHead() {
		return context -> {
			object.getHead().add(context.getEvent().getValue());
			if (context.getEvent().getValue().contains("mtllib")) {
				object.setMaterialsFileName(context.getEvent().getValue().split(" ")[1]);
			    try {
					mtlFileBytes = utils.readMaterialsFile(object.getMaterialsFileName());
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			System.out.println("object head");
			System.out.println("mtlFileBytes length = " + mtlFileBytes.length);
		};    	
    }
	public WaveFrontObject getObject() {
		return object;
	}
	
}
