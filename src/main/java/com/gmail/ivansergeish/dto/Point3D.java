package com.gmail.ivansergeish.dto;

public class Point3D {
    private double x;
    public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}
	private double y;
    private double z;
    
    public Point3D(double x, double y, double z) {
    	this.x = x;
    	this.y = y;
    	this.z = z;
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
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}
	public void setZ(double z) {
		this.z = z;
	}
    
	public double scalarMult (Point3D p) {
		return p.x * x + p.y * y + p.z * z;
	}
	
	public Point3D subtract(Point3D p) {
		return new Point3D(x - p.x, y - p.y, z - p.z);
	}
    
}
