package com.gmail.ivansergeish.dto.point;

public class Point2D extends Point{
	public static final String VERTEX_PREFIX_VT = "vt";
	public static final int NUMBER_IN_FACE_VT = 2;
	
    private double x;
	private double y;    
    public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
   
    public Point2D(double x, double y) {
    	super(VERTEX_PREFIX_VT, NUMBER_IN_FACE_VT);
    	this.x = x;
    	this.y = y;
    }
    
    public Point2D(double x, double y, String vertexPrefix, int numberInFace) {
    	super(vertexPrefix, numberInFace);
    	this.x = x;
    	this.y = y;
    }
    
    public Point2D(double x, double y, String vertexPrefix, int numberInFace,
    		PointSubtractionVisitor subtractorVisitor,
    		PointScalarMultiplicationVisitor scalarMultiplicatorVisitor) {
    	super(vertexPrefix, numberInFace);
    	this.x = x;
    	this.y = y;
    	setScalarMultiplicationVisitor(scalarMultiplicatorVisitor);
    	setSubtractionVisitor(subtractorVisitor);
    }

    public Point2D(double x, double y,
    		PointSubtractionVisitor subtractorVisitor,
    		PointScalarMultiplicationVisitor scalarMultiplicatorVisitor) {
    	super(VERTEX_PREFIX_VT, NUMBER_IN_FACE_VT);
    	this.x = x;
    	this.y = y;
    	setScalarMultiplicationVisitor(scalarMultiplicatorVisitor);
    	setSubtractionVisitor(subtractorVisitor);
    } 
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point2D other = (Point2D) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}
    
//	public double scalarMult (Point p) {
//		return getScalarMultiplicationVisitor().
//				scalarMultiplicationVisit(p, this);
//	}
//	
//	public Point2D subtract(Point p) {
//		return (Point2D) getSubtractionVisitor().subtractionVisit(this, p);
//	}
    

}
