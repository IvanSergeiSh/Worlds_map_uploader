package com.gmail.ivansergeish.utils;

import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gmail.ivansergeish.dto.FacesUseMaterial;
import com.gmail.ivansergeish.dto.Point3D;
import com.gmail.ivansergeish.dto.WaveFrontObject;
@Service
public class WaveFrontUtils {
	/**
	 * This method is intended to creation new WvFObject of file.
	 * reader should start from the next string to object name.
	 * All the vertexes should be read from file fName. 
	 * @param fName 
	 * @param reader
	 * @param objName 
	 * @return
	 * @throws IOException
	 */
    public WaveFrontObject readNextObject(String fName, 
    		BufferedReader reader,
    		String objName) throws IOException {
    	List<WaveFrontObject> results = new ArrayList();
        WaveFrontObject object = new WaveFrontObject(); 
        
    	File file = new File(fName);    	
    	BufferedReader fReader = Files.newBufferedReader(file.toPath());
    	boolean stop = false;
    	String currentObjectName = null;
    	String currentUseMtl = null;
    	while (!stop) {
    		FacesUseMaterial facesUseMtl = null;
    		List<FacesUseMaterial> facesUseMtls = new ArrayList();
    		String str = reader.readLine();
    		stop = str == null;
    		String objectName = getObjectName(str);
    		if (objectName != null && !objectName.isEmpty()) {
    			if (!objectName.equals(currentObjectName)) {
    				object = new WaveFrontObject();
    				results.add(object);
    				currentObjectName = objectName;
    			}
    			object.setName(objectName);
    		}
    		if (isUseMtl(str)) {
    			String useMtlName = getUseMtlName(str);
    			if (!useMtlName.equals(currentUseMtl)) {
    				facesUseMtl = new FacesUseMaterial();
    				currentUseMtl = useMtlName;
    				facesUseMtls.add(facesUseMtl);
    			}
    		}
    		if (str != null && !str.isEmpty() && str.charAt(0) == 'f') {
    			//TODO read all vertexes of object and add to object
    			//maybe error!!!
    			object.getVertexes().addAll(getVertexesByNumbers(fName, getVertexesNumbersFromFace(str)));
    			
    			//TODO convert face to new format
    		}
    	}
    	return null;
    }
    
    List<Integer> getVertexesNumbersFromFace(String face) {
    	ArrayList<Integer> result = new ArrayList();
    	if (face != null && !face.isEmpty() && face.charAt(0) == 'f') {
    		String[] strs = face.split(" ");
    		for (int i = 1; i < strs.length; i++) {
    			result.add(Integer.valueOf(strs[i]));
    		}
    	}
    	return result;
    } 
    
    /**
     * The method is intended to find all the vertexes in file by their numbers
     * @param fName
     * @param numbers
     * @return
     * @throws IOException
     */
    public List<String> getVertexesByNumbers(String fName,
    		List<Integer> numbers) throws IOException {
    	
    	File file = new File(fName);    	
    	BufferedReader reader = Files.newBufferedReader(file.toPath());
    	boolean stop = false;
    	int index = 0;
    	List<String> result = new ArrayList();
    	numbers.sort(new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				return ((Integer) o1).compareTo((Integer) o2);
			}});
			
    	while(!stop) {
    		String str = reader.readLine();
    		if (str != null && 
    				str.charAt(0) == 'v') {
    			index++;
    			if (numbers.contains(Integer.valueOf(index))) {
    				result.add(str);
    			} 
    		}

    		if ( str == null || index > numbers.get(numbers.size()-1)) {
    			stop = true;
    		}
    	}
    	return result;
    }
    /**
     * 
     * @param objVertexes - current numbers of vertexes from *.obj file
     * @param faceVertexes - vertexes of current face in old order
     * @return new face vertexes for new object file
     */
    public List<Integer> getNewFaceRepresentation(List<Integer> objVertexes, 
    		List<Integer> faceVertexes) {
    	return faceVertexes.stream().map((Integer item)-> {
    		//It should be noted that in *.obj file vertex indexes begin with 1
    		return Integer.valueOf(objVertexes.indexOf(item)) + 1;
    	}).collect(Collectors.toList());

    }
    /**
     * As new object to be extracted from the whole file should has all it's vertexes in it,
     * 
     * @param objVertexes
     * @param face
     * @return
     */
    public String processFaceToNewForm (List<Integer> objVertexes, 
    		String face) {
    	if (face == null || face.isEmpty()) {
    		return null;
    	}
    	if (face.charAt(0) != 'f') {
    		return null;
    	}
    	String[] strs = face.split(" ");
    	List<Integer> items = new ArrayList();
    	for (int i = 1; i < strs.length; i++) {
    		items.add(Integer.parseInt(strs[i]));
    	}
    	
    	return getNewFaceRepresentation(objVertexes, items).stream().map((Integer i)-> {
    		return i.toString();
    	}).reduce("f", (String str1, String str2)-> {
    		return str1 + " " + str2;
    	});
    }
    
    /**
     * The method is intended to read a file from the very beginning till the next object description
     * and looking through all the vertexes taking part in current object
     * @param reader should be installed on initial position of the beginning of file
     * @param objName
     * @return list of distinct vertexes numbers of the object
     * Numbers are in context of all file
     * @throws IOException
     */
//    public List<Integer> getVertexNumsForObject(BufferedReader reader,
//    		String objName) throws IOException {
//    	boolean eof = false;
//    	boolean stop = false;
//    	List<Integer> vertexes = new ArrayList();
//    	List<Integer> vertexesFromStr = new ArrayList();
//    	if (readUntillObjectName(reader, objName)) {
//        	while (!eof && !stop) {
//	    		String str = reader.readLine();
//	    		if (str != null) {
//	    			stop = isObjectDescription(str);
//	    			vertexesFromStr = getVertexIndexesFromString(str);
//	    			for (Integer item: vertexesFromStr) {
//	    				if (!vertexes.contains(item)) {
//	    					vertexes.add(item);
//	    				}
//	    			}
//	    		}
//	    		eof = str == null;
//	    	}
//    	}
//        return vertexes; 	
//    }
    //TODO combine two methods in one
    boolean isObjectDescription(String str) {
    	if (str == null || str.isEmpty()) {
    		return false;
    	}
    	return str.split(" ")[0].equals("o");
    }
    
    boolean isUseMtl(String str) {
    	if (str == null || str.isEmpty()) {
    		return false;
    	}
    	return str.split(" ")[0].equals("usemtl");
    }
    
    /**
     * Sets up reader position on the line with a specified object name
     * @param reader
     * @param objName
     * @return true if found object with specified name,
     *         false if no.
     * @throws IOException
     */
//    boolean readUntillObjectName(BufferedReader reader,
//    		String objName) throws IOException {
//    	boolean eof = false;
//    	while (!eof) {
//    		String str = reader.readLine();
//    		if (str != null) {
//    			if (getObjectName(str) != null && getObjectName(str).equals(objName)) {
//    				return true;
//    			}
//    		}
//    		eof = str == null;
//    	}    	
//    	return false;
//    }
    
    public List<Integer> getVertexIndexesFromString(String str) {
    	String[] strs = str.split(" ");
    	if (strs.length > 1 && strs[0].equals("f")) {
    		List<Integer> result = new ArrayList();
    		for(int i = 1; i < strs.length; i++) {
    			result.add(Integer.parseInt(strs[i]));
    		}
    		return result;
    	}
    	return new ArrayList();
    }
    
    /**
     * The method is looking for a string like 'o Plane.023'
     * @param str
     * @return second part of string if it is or null
     */
    //TODO combine into one method
    public String getObjectName(String str) {
    	String[] strs = str.split(" ");
    	if (strs.length > 1 && strs[0].equals("o")) {
    		return strs[1];
    	}
    	return null;    	
    }
    
    public String getUseMtlName(String str) {
    	String[] strs = str.split(" ");
    	if (strs.length > 1 && strs[0].equals("usemtl")) {
    		return strs[1];
    	}
    	return null;    	
    }    
    
    public void writeToFile(String fName, WaveFrontObject object) throws IOException {
    	File file = new File(fName);    	
    	BufferedWriter writer = Files.newBufferedWriter(file.toPath());
    	for (String headStr : object.getHead()) {
    		writer.write(headStr);
    		writer.newLine();
    	}
    	writer.write("o " + object.getName());
    	for (String vertex : object.getVertexes()) {
    		writer.newLine();
    		writer.write(vertex);
    	}    	
    	for (FacesUseMaterial faceMaterial : object.getFaces()) {
    		writer.newLine();
    		writer.write("usemtl " + faceMaterial.getUsemtl());
    		for (String head : faceMaterial.getHead()) {
        		writer.newLine();
        		writer.write(head);    			
    		}
    		for (String face : faceMaterial.getFaces()) {
        		writer.newLine();
        		writer.write(face);    			
    		}
    	}
    	writer.flush();
    }
    
    //TODO Please refactor me!
    public byte[] writeToByteArray(WaveFrontObject object) throws IOException {
    	ByteArrayOutputStream stream = new ByteArrayOutputStream();
    	OutputStreamWriter writer = new OutputStreamWriter(stream);  	
    	for (String headStr : object.getHead()) {
    		writer.write(headStr);
    		writer.append('\n');
    		//writer.newLine();
    	}
    	writer.write("o " + object.getName());
    	writer.append('\n');
    	for (String vertex : object.getVertexes()) {
    		//writer.newLine();
    		writer.write(vertex);
    		writer.append('\n');
    	}    	
    	for (FacesUseMaterial faceMaterial : object.getFaces()) {
    		//writer.newLine();
    		writer.write("usemtl " + faceMaterial.getUsemtl());
    		writer.append('\n');
    		for (String head : faceMaterial.getHead()) {
        		//writer.newLine();
        		writer.write(head);
        		writer.append('\n');
    		}
    		for (String face : faceMaterial.getFaces()) {
        		//writer.newLine();
        		writer.write(face); 
        		writer.append('\n');
    		}
    	}
    	writer.flush();
    	byte[] result = stream.toByteArray();
    	writer.close();
    	stream.close();
    	return result;
    	
    }
    
    
    public Point3D calcCenterMass(List<String> vertexes) {
    	Point3D point = vertexes.stream().map((String vertex) -> {
    		String[] coordinates = vertex.split(" ");
    		return new Point3D(Double.valueOf(coordinates[1]), Double.valueOf(coordinates[2]), Double.valueOf(coordinates[3]));
    	}).reduce(new Point3D(0,0,0), ((Point3D p1, Point3D p2) -> {
    		return new Point3D(p1.getX() + p2.getX(), p1.getY() + p2.getY(), p1.getZ() + p2.getZ());
    	}));
    	point.setX(point.getX()/vertexes.size());
    	point.setY(point.getY()/vertexes.size());
    	point.setZ(point.getZ()/vertexes.size());
    	return point;
    }
    
    public String calcVertexAccordingToCenterMass(String vertex, Point3D center) {
    	String[] strs = vertex.split(" ");
    	double x = Double.valueOf(strs[1]) - center.getX();
    	double y = Double.valueOf(strs[2]) - center.getY();
    	double z = Double.valueOf(strs[3]) - center.getZ();
    	return "v " + Double.toString(x) + " " + Double.toString(y) + " " + Double.toString(z);
    }
     
    public Point3D getPointFromStr(String vertex) {
    	String[] strs = vertex.split(" ");
    	if (strs.length < 4) {
    		throw new IllegalArgumentException("Illegal vertex format : " + vertex);
    	}
    	double x = Double.valueOf(strs[1]);
    	double y = Double.valueOf(strs[2]);
    	double z = Double.valueOf(strs[3]);
    	return new Point3D(x, y, z);
    }
    
    public List<Point3D> vertexesFromFace(List<String> vertexes, String face) {
    	return Arrays.stream(face.split(" ")).filter((String id) -> {
    		return !id.equals("f");
    	}).map((String id) -> {
    		return Integer.valueOf(id);
    	}).map((Integer id) -> {
    		return getPointFromStr(vertexes.get(id - 1));
    	}).collect(Collectors.toList());
    }
    
    public boolean isRotated(List<String> vertexes1, List<String> vertexes2, String face1, String face2) {
    	Point3D center1 = calcCenterMass(vertexes1);
    	Point3D center2 = calcCenterMass(vertexes2);
    	List<Point3D> ps1 = vertexesFromFace(vertexes1, face1);
    	List<Point3D> ps2 = vertexesFromFace(vertexes2, face2);
    	if (ps1.size() != ps2.size() ||
    			vertexes1.size() != vertexes2.size()) {
    		return false;
    	}
    	//TODO check for equality with some fixed precision
        for (int i = 0; i < ps1.size() - 1; i++) {
        	if (ps1.get(i).subtract(center1).scalarMult(ps1.get(i+1).subtract(center1)) !=
        			ps2.get(i).subtract(center2).scalarMult(ps2.get(i+1).subtract(center2))) {
        		return false;
        	}
        }
    	if (ps1.get(ps1.size()-1).subtract(center1).scalarMult(ps1.get(0).subtract(center1)) !=
    			ps2.get(ps1.size()-1).subtract(center2).scalarMult(ps2.get(0).subtract(center2))) {
    		return false;
    	}
    	return true;
    }
    
	public byte[] readMaterialsFile(String fName) throws IOException {
    	File file = new File(fName);
    	return Files.readAllBytes(file.toPath());
		
	}
}
