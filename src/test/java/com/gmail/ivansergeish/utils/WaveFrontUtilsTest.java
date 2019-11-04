package com.gmail.ivansergeish.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.gmail.ivansergeish.dto.Point3D;

public class WaveFrontUtilsTest {
	private WaveFrontUtils utils = new WaveFrontUtils();
    @Test
	public void testGetObjectName() {
		Assert.assertTrue(utils.getObjectName("") == null);
		Assert.assertTrue(utils.getObjectName("j name") == null);
		Assert.assertTrue(utils.getObjectName("1 2") == null);
		Assert.assertEquals("name", utils.getObjectName("o name"));
	}
    
    @Test
    public void testGetVertexIndexesFromString() {
    	
    	Assert.assertEquals(4, utils.getVertexIndexesFromString("f 1 2 3 4").size());
    	Assert.assertTrue(utils.getVertexIndexesFromString("f 1 2 3 4").contains(Integer.valueOf(1)));
    	Assert.assertTrue(utils.getVertexIndexesFromString("f 1 2 3 4").contains(Integer.valueOf(2)));
    	Assert.assertTrue(utils.getVertexIndexesFromString("f 1 2 3 4").contains(Integer.valueOf(3)));
    	Assert.assertTrue(utils.getVertexIndexesFromString("f 1 2 3 4").contains(Integer.valueOf(4)));
    	Assert.assertFalse(utils.getVertexIndexesFromString("f 1 2 3 4").contains(Integer.valueOf(0)));
    	Assert.assertEquals(0, utils.getVertexIndexesFromString("o 1 2 3 4").size());
    	Assert.assertEquals(0, utils.getVertexIndexesFromString("").size());
    }
    
    @Test
    public void testIsObjectDescription() {
    	Assert.assertTrue(utils.isObjectDescription("o name"));
    	Assert.assertFalse(utils.isObjectDescription("name"));
    	Assert.assertFalse(utils.isObjectDescription(""));
    	Assert.assertFalse(utils.isObjectDescription(null));
    }
    
    @Test
    public void testGetVertexesByNumbers() throws IOException {
    	List<Integer> numbers = Arrays.asList(Integer.valueOf(1),Integer.valueOf(8));
    	List<String> vertexes = utils.getVertexesByNumbers(System.getProperty("user.dir") + "/src/test/resources/test.obj", numbers);
    	Assert.assertTrue(vertexes.size() == 2);
    	Assert.assertTrue(vertexes.get(0).equals("v 1.000001 0.000001 0.000001"));
    	Assert.assertTrue(vertexes.get(1).equals("v 0.000001 0.000001 1.000001"));
    }
    
    @Test
    public void testProcessFaceToNewForm() {
    	String result = utils.processFaceToNewForm(new ArrayList(Arrays.asList(
    			Integer.valueOf(9),
    			Integer.valueOf(10),
    			Integer.valueOf(11),
    			Integer.valueOf(12),
    			Integer.valueOf(13),
    			Integer.valueOf(14),
    			Integer.valueOf(15),
    			Integer.valueOf(16))), "f 14 15 11 10");
    	Assert.assertTrue(result.equals("f 6 7 3 2"));

    	result = utils.processFaceToNewForm(new ArrayList(Arrays.asList(
    			Integer.valueOf(2),
    			Integer.valueOf(3),
    			Integer.valueOf(6),
    			Integer.valueOf(8),
    			Integer.valueOf(9))), "f 6 8 9");
    	Assert.assertTrue(result.equals("f 3 4 5"));
    }
    
    @Test
    public void testCalcCenterMass() {
    	List<String> vertexes = new ArrayList();
    	vertexes.add("v 1 1 1");
    	vertexes.add("v -1 -1 -1");
    	vertexes.add("v 1 0 0");
    	vertexes.add("v -1 0 0");
    	Point3D p = utils.calcCenterMass(vertexes);
    	Assert.assertTrue( p.equals(new Point3D(0, 0, 0)));
    }
    
    @Test
    public void testVertexesFromFace() {
    	List<String> vertexes = new ArrayList();
    	vertexes.add("v 1 1 1");
    	vertexes.add("v -1 -1 -1");
    	vertexes.add("v 1 0 0");
    	vertexes.add("v -1 0 0");
    	List<Point3D> points = utils.vertexesFromFace(vertexes, "f 1 2 3 4");
    	Assert.assertTrue(points.size() == 4);
    	Assert.assertTrue(points.get(0).equals(new Point3D(1, 1, 1)));
    	Assert.assertTrue(points.get(1).equals(new Point3D(-1, -1, -1)));
    	Assert.assertTrue(points.get(2).equals(new Point3D(1, 0, 0)));
    	Assert.assertTrue(points.get(3).equals(new Point3D(-1, 0, 0)));
    	
    	points = utils.vertexesFromFace(vertexes, "f 3 4");
    	Assert.assertTrue(points.size() == 2);
    	Assert.assertTrue(points.get(0).equals(new Point3D(1, 0, 0)));
    	Assert.assertTrue(points.get(1).equals(new Point3D(-1, 0, 0)));
    }
    
    @Test
    public void testIsRotated() {
    	List<String> vertexes1 = new ArrayList();   	
    	vertexes1.add("v 1 0 0");
    	vertexes1.add("v 1 1 0");
    	vertexes1.add("v 0 1 0");
    	vertexes1.add("v 0 0 0");
    	List<String> vertexes2 = new ArrayList();
    	vertexes2.add("v 1 0 0");
    	vertexes2.add("v 0 1 0");
    	vertexes2.add("v 0 0 0");
    	vertexes2.add("v 1 1 0");
    	Assert.assertTrue(utils.isRotated(vertexes1, vertexes2, "f 1 2 3 4", "f 3 1 4 2"));
    	
    }

    @Test
    public void testIsRotatedFalse() {
    	List<String> vertexes1 = new ArrayList();   	
    	vertexes1.add("v 1 0 0");
    	vertexes1.add("v 1 1 0");
    	vertexes1.add("v 0 1 0");
    	vertexes1.add("v 0 0 0");
    	List<String> vertexes2 = new ArrayList();
    	vertexes2.add("v 1 0 0");
    	vertexes2.add("v 1 1 0");
    	vertexes2.add("v 0 1 0");
    	vertexes2.add("v 0 0 0");
    	Assert.assertFalse(utils.isRotated(vertexes1, vertexes2, "f 1 2 3 4", "f 3 1 4 2"));
    	
    }
    
    @Test
    public void testCalcVertexAccordingToCenterMass() {
    	Point3D center = new Point3D(0.500000, 0.500000, 0.500000);
    	String[] result = utils.calcVertexAccordingToCenterMass("v 1.0000001 0.0000001 0.0000001", center).split(" ");
    	Assert.assertEquals(0.5, Double.parseDouble(result[1]), 0.01);
    	Assert.assertEquals(-0.49, Double.parseDouble(result[2]), 0.01);
    	Assert.assertEquals(-0.49, Double.parseDouble(result[3]), 0.01);
    }
}
