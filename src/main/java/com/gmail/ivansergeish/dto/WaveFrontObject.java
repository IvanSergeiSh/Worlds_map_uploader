package com.gmail.ivansergeish.dto;

import java.util.ArrayList;
import java.util.List;

import com.gmail.ivansergeish.dto.point.Point3D;

public class WaveFrontObject {
	private Point3D position;
    private double alphaOZ;
    private double bettaOY;
    private double scale;
    private String materialsFileName;
    
	private List<String> head;
	private String name;
    private List<String> vertexes;
    private List<FacesUseMaterial> faces;
    
    public WaveFrontObject() {
    	vertexes = new ArrayList();
    	faces = new ArrayList();
    	head = new ArrayList();
    }
	public WaveFrontObject(List<String> head, 
			String name, 
			List<String> vertexes, 
			List<FacesUseMaterial> faces) {
		super();
		this.head = head;
		this.name = name;
		this.vertexes = vertexes;
		this.faces = faces;
	}
	public List<String> getHead() {
		return head;
	}
	public void setHead(List<String> head) {
		this.head = head;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getVertexes() {
		return vertexes;
	}
	public void setVertexes(List<String> vertexes) {
		this.vertexes = vertexes;
	}
	public List<FacesUseMaterial> getFaces() {
		return faces;
	}
	public void setFaces(List<FacesUseMaterial> faces) {
		this.faces = faces;
	}
	public Point3D getPosition() {
		return position;
	}
	public void setPosition(Point3D position) {
		this.position = position;
	}
	public String getMaterialsFileName() {
		return materialsFileName;
	}
	public void setMaterialsFileName(String materialsFileName) {
		this.materialsFileName = materialsFileName;
	}
	public double getAlphaOZ() {
		return alphaOZ;
	}
	public void setAlphaOZ(double alphaOZ) {
		this.alphaOZ = alphaOZ;
	}
	public double getBettaOY() {
		return bettaOY;
	}
	public void setBettaOY(double bettaOY) {
		this.bettaOY = bettaOY;
	}
	public double getScale() {
		return scale;
	}
	public void setScale(double scale) {
		this.scale = scale;
	}
    
    
}
