package com.gmail.ivansergeish.dto;

import java.util.ArrayList;
import java.util.List;

public class FacesUseMaterial {
	private String usemtl;
    private List<String> head; //s off 
    private List<String> faces;
    
    public FacesUseMaterial() {
    	faces = new ArrayList();
    	head = new ArrayList();
    }
    public FacesUseMaterial(String usemtl, 
    		List<String> head,
    		List<String> faces) {
    	this.usemtl = usemtl;
    	this.head = head;
    	this.faces = faces;
    }
	public String getUsemtl() {
		return usemtl;
	}
	public void setUsemtl(String usemtl) {
		this.usemtl = usemtl;
	}
	public List<String> getHead() {
		return head;
	}
	public void setHead(List<String> head) {
		this.head = head;
	}
	public List<String> getFaces() {
		return faces;
	}
	public void setFaces(List<String> faces) {
		this.faces = faces;
	}
    
    
}
