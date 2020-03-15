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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.gmail.ivansergeish.dto.FacesUseMaterial;
import com.gmail.ivansergeish.dto.WaveFrontObject;
import com.gmail.ivansergeish.dto.WaveTexturedObject;
import com.gmail.ivansergeish.dto.point.Point3D;
import com.gmail.ivansergeish.dto.point.PointScalarMultiplicationVisitor;
import com.gmail.ivansergeish.dto.point.PointSubtractionVisitor;
/**
 * The class has a number of utill-methods to separate sub-objects 
 * @author ivan
 *
 */
@Service
public class WaveFrontUtils {
	
	private static final int VERTEX_INDEX_IN_FACE = 0;
	
	@Autowired
	private Environment env;
	
	private PointScalarMultiplicationVisitor multVisitor 
	= new PointScalarMultiplicationVisitor();
	private PointSubtractionVisitor subtractionVisitor 
	= new PointSubtractionVisitor();

    List<Integer> getVertexesNumbersFromFacse(String face) {
    	ArrayList<Integer> result = new ArrayList();
    	if (face != null && !face.isEmpty() && face.charAt(0) == 'f') {
    		String[] strs = face.split(" ");
    		for (int i = 1; i < strs.length; i++) {
    			result.add(Integer.valueOf(getIndexFromFaceSubString(strs[i], VERTEX_INDEX_IN_FACE)));
    		}
    	}
    	return result;
    } 
    /**
     * face usualy is written in a format like:
     *  f vertexIndex/faceIndex/normalIndex
     * The method returns appropriate substring of the described above
     * @param subStr
     * @param i
     * @return
     */
    String getIndexFromFaceSubString(String subStr, int i) {
    	String[] strs = subStr.split("/");
    	if (strs.length >= i) {
    		return strs[i];
    	}
    	throw new IllegalArgumentException("index out of bound i: " +
    	i + ", subStr: " + subStr);
    }
    
    /**
     * The method is intended to find all the vertexes and textureVertexes
     *  in the file by their current numbers
     * @param fName
     * @param numbers
     * @return
     * @throws IOException
     */
    public List<String> getVertexesByNumbers(String fName,
    		List<Integer> numbers, String prefix) throws IOException {
    	
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
    		if (str != null) {
    			String typeDeclaration = str.split(" ")[0];
    			if (typeDeclaration.equals(prefix)) {
        			index++;
        			if (numbers.contains(Integer.valueOf(index))) {
        				result.add(str);
        			}     				
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
     * As new object to be extracted from the whole file
     *  should has all it's vertexes in it,
     * 
     * @param objVertexes - indexes of vertexes in 
     * @param face
     * @return
     */
    public String processFaceToNewForm (List<Integer> objVertexes, 
    		String face, int itemNumber) {
    	if (face == null || face.isEmpty()) {
    		return null;
    	}
    	if (face.charAt(0) != 'f') {
    		return null;
    	}
    	String[] strs = face.split(" ");
    	List<Integer> items = new ArrayList();
    	for (int i = 1; i < strs.length; i++) {
    		if (strs[i].split("/").length > itemNumber) {
    			items.add(Integer.parseInt(getIndexFromFaceSubString(strs[i],itemNumber)));    			
    		}
//    		items.add(Integer.parseInt(getIndexFromFaceSubString(strs[i],itemNumber)));
    	}
    	List<Integer> newFaceVertexes = getNewFaceRepresentation(objVertexes, items);
    	if (newFaceVertexes.size() < 1) {
    		return face;
    	}
    	String result = "f";
    	for (int i = 1; i < strs.length; i++) {
    		result = result + " " + substitute(strs[i], newFaceVertexes.get(i - 1), itemNumber);
    	}
    	return result;
    }
    
    String substitute(String faceSubString, int valToBeSubstituted, int itemNumber) {
    	String result = "";
    	String[] strs = faceSubString.split("/");
    	if (strs.length < itemNumber) {
    		throw new IllegalArgumentException("cannot find place to substitute: itemNumber exceeds substring.length");
    	}
    	strs[itemNumber] = Integer.toString(valToBeSubstituted);
    	for (String str : strs) {
    		if (result != "") {
    			result = result + "/";
    		}
    		result = result  + str;
    	}
    	return result;
    }
    

    boolean isObjectDescription(String str) {
    	return isSpecificString(str, "o");
    }
    
    boolean isUseMtl(String str) {
    	return isSpecificString(str, "usemtl");
    }
    
    boolean isSpecificString(String str, String specific) {
    	if (str == null || str.isEmpty()) {
    		return false;
    	}
    	return str.split(" ")[0].equals(specific);    	
    }
    
    /**
     * Sets up reader position on the line with a specified object name
     * @param reader
     * @param objName
     * @return true if found object with specified name,
     *         false if no.
     * @throws IOException
     */
    public List<Integer> getVertexIndexesFromString(String str, int itemNumber) {
    	String[] strs = str.split(" ");
    	if (strs.length > 1 && strs[0].equals("f")) {
    		List<Integer> result = new ArrayList();
    		for(int i = 1; i < strs.length; i++) {
    			if (strs[i].split("/").length > itemNumber) {
    			    result.add(Integer.parseInt(getIndexFromFaceSubString(strs[i], itemNumber)));
    			}
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
    		return strs[1].replaceAll(".jpg", "");
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
    	}
    	writer.write("o " + object.getName());
    	writer.append('\n');
    	for (String vertex : object.getVertexes()) {
    		writer.write(vertex);
    		writer.append('\n');
    	}
    	if(object instanceof WaveTexturedObject) {
    		for (String vertex : ((WaveTexturedObject)object).getVTextures()) {
    		    writer.write(vertex);
    		    writer.append('\n');
    	    } 
    	}
   	    	
    	for (FacesUseMaterial faceMaterial : object.getFaces()) {
    		writer.write("usemtl " + faceMaterial.getUsemtl());
    		writer.append('\n');
    		for (String head : faceMaterial.getHead()) {
        		writer.write(head);
        		writer.append('\n');
    		}
    		for (String face : faceMaterial.getFaces()) {
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
    	
    	point.setScalarMultiplicationVisitor(multVisitor);
    	point.setSubtractionVisitor(subtractionVisitor);
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
    	return new Point3D(x, y, z, subtractionVisitor, multVisitor);
    }
    /**
     * This method is intended to get appropriate vertex from vertexes 
     * list and face string representation
     * @param vertexes list of vertexes string representation: v 1.00 0.00 0.00
     * @param face - string representation of face: f 1/11 2/12 3/13 4/14 - 
     *               means that current face consists of vertexes
     *               number 0, 1, 2 ,3
     *               
     * @return
     */
    public List<Point3D> vertexesFromFace(List<String> vertexes, String face) {
    	return Arrays.stream(face.split(" ")).filter((String id) -> {
    		return !id.equals("f");
    	}).map((String faceString) -> {
    		return Integer.valueOf(getIndexFromFaceSubString(faceString, VERTEX_INDEX_IN_FACE));
    		//return Integer.valueOf(id);
    	}).map((Integer id) -> {
    		return getPointFromStr(vertexes.get(id - 1));
    	}).collect(Collectors.toList());
    }
    //TODO Add subtractorVisitor and scalarMultiplicatorVisitor
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
    //TODO improve the method including erasing ".jpg" suffix from newmtl 
    // and improve map_Kd path from c:\...\currentimg.jpg to 
    // https://...//map/sprite/currentimg
    
	public byte[] readFileToByteArray(String fName) throws IOException {
		
    	File file = new File(fName);
    	return Files.readAllBytes(file.toPath());
		
	}
//	//TODO implement reading bytes from file and improving lines inside
//	
//	public byte[] readMaterials(String fName) throws IOException{
//		File file = new File(fName);
//		List<String> list = Files.readAllLines(file.toPath());
//		for(int i = 0; i < list.size(); i++) {
//			String[] strs = list.get(i).split(" ");
//			if (strs.length > 0 && strs[0].equals("newmtl")) {
//				String newMtl = "newmtl " + strs[1].replaceAll(".jpg", "");
//			}
//			if (strs.length > 0 && strs[0].equals("map_Kd")) {
//				String newMapKd = "map_Kd " + strs[1].replaceAll(".jpg", "");
//			}
//		}
//	}
}
