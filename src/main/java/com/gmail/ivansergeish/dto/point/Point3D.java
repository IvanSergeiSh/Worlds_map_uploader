package com.gmail.ivansergeish.dto.point;

public class Point3D extends Point2D{
	public static final String VERTEX_PREFIX_V = "v";
	public static final int NUMBER_IN_FACE_V = 1;	
	private double z;
	public double getZ() {
		return z;
	}
    
    
    public Point3D(double x, double y, double z) {
    	super(x, y);
    	this.z = z;
    	setNumberInFace(NUMBER_IN_FACE_V);
    	setVertexPrefix(VERTEX_PREFIX_V);
    }

    public Point3D(double x, double y, double z,
    		PointSubtractionVisitor subtractorVisitor,
    		PointScalarMultiplicationVisitor scalarMultiplicatorVisitor) {
    	super(x, y);
    	this.z = z;
    	setNumberInFace(NUMBER_IN_FACE_V);
    	setVertexPrefix(VERTEX_PREFIX_V);
    	setSubtractionVisitor(subtractorVisitor);
    	setScalarMultiplicationVisitor(scalarMultiplicatorVisitor);
    }
    
    public Point3D(double x, double y, double z, String vertexPrefix, int numberInFace,
    		PointSubtractionVisitor subtractorVisitor,
    		PointScalarMultiplicationVisitor scalarMultiplicatorVisitor) {
    	super(x, y);
    	this.z = z;
    	setNumberInFace(numberInFace);
    	setVertexPrefix(vertexPrefix);
    	setSubtractionVisitor(subtractorVisitor);
    	setScalarMultiplicationVisitor(scalarMultiplicatorVisitor);
    }    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(super.getX());
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(super.getY());
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
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
		Point3D other = (Point3D) obj;
		if (Double.doubleToLongBits(getX()) != Double.doubleToLongBits(other.getX()))
			return false;
		if (Double.doubleToLongBits(getY()) != Double.doubleToLongBits(other.getY()))
			return false;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}
	
	public void setZ(double z) {
		this.z = z;
	}
    //TODO implement visitor template
//	public double scalarMult (Point3D p) {
//		return p.getX() * getX() + p.getY() * getY() + p.getZ() * getZ();
//	}
//	
//	public Point3D subtract(Point3D p) {
//		return new Point3D(getX() - p.getX(), getY() - p.getY(), z - p.z);
//	}
    
}
