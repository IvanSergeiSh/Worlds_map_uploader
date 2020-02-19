package com.gmail.ivansergeish.dto.point;

public abstract class Point {
	private PointSubtractionVisitor subtractionVisitor;
	private PointScalarMultiplicationVisitor scalarMultiplicationVisitor;
    private String vertexPrefix;
    
    /**
     * by default face is represented by the following string:
     * f 1/10/1 2/20/2 3/30/3 4/40/4
     * so, number in face is a number of /
     * for example, if numberInFace = 2 then the face is linked to points  10, 20, 30, 40
     */
    private int numberInFace;
    
    public Point(String vertexPrefix, int numberInFace) {
    	this.vertexPrefix = vertexPrefix;
    	this.numberInFace = numberInFace;
    }

    public Point(String vertexPrefix, int numberInFace, PointSubtractionVisitor subtractionVisitor, PointScalarMultiplicationVisitor scalarMultiplicationVisitor) {
    	this.vertexPrefix = vertexPrefix;
    	this.numberInFace = numberInFace;
    	this.setScalarMultiplicationVisitor(scalarMultiplicationVisitor);
    	this.setSubtractionVisitor(subtractionVisitor);
    }
    
	public int getNumberInFace() {
		return numberInFace;
	}
	
	public void setNumberInFace(int numberInFace) {
		this.numberInFace = numberInFace;
	}
	
	public String getVertexPrefix() {
		return vertexPrefix;
	}
	
	public void setVertexPrefix(String vertexPrefix) {
		this.vertexPrefix = vertexPrefix;
	}
	
	public double scalarMult (Point p) {
		return getScalarMultiplicationVisitor().
				scalarMultiplicationVisit(p, this);
	}
	
	public Point subtract(Point p) {
		return getSubtractionVisitor().subtractionVisit(this, p);
	}

	public PointSubtractionVisitor getSubtractionVisitor() {
		return subtractionVisitor;
	}

	public void setSubtractionVisitor(PointSubtractionVisitor subtractionVisitor) {
		this.subtractionVisitor = subtractionVisitor;
	}

	public PointScalarMultiplicationVisitor getScalarMultiplicationVisitor() {
		return scalarMultiplicationVisitor;
	}

	public void setScalarMultiplicationVisitor(PointScalarMultiplicationVisitor scalarMultiplicationVisitor) {
		this.scalarMultiplicationVisitor = scalarMultiplicationVisitor;
	}
}
