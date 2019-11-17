package com.gmail.ivansergeish.dto.point;

public class PointScalarMultiplicationVisitor {
    public double scalarMultiplicationVisit(Point p1, Point p2) {
   	 if (p1.getClass() != p2.getClass()) {
   		 throw new IllegalArgumentException("both arguments p1 and p2 should be of the same type");
   	 }
   	 else if (p1.getClass() == Point2D.class) {
   		 Point2D p12D = (Point2D) p1;
   		 Point2D p22D = (Point2D) p2;
   		 return  p12D.getX() * p22D.getX() + p12D.getY() * p22D.getY();
   	 }
   	 else if (p1.getClass() == Point3D.class) {
   		 Point3D p13D = (Point3D) p1;
   		 Point3D p23D = (Point3D) p2;
   		 return  p13D.getX() * p23D.getX() +
   				 p13D.getY() * p23D.getY() +
   				 p13D.getZ() * p23D.getZ();
   	 }    	
   	 throw new IllegalArgumentException("unexpected type of argument(s)");    	 
    }
}
