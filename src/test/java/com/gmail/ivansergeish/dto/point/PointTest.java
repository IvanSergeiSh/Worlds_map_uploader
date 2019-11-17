package com.gmail.ivansergeish.dto.point;

import org.junit.Assert;
import org.junit.Test;



public class PointTest {
	private PointSubtractionVisitor subtractorVisitor = new PointSubtractionVisitor();
	private PointScalarMultiplicationVisitor scalarMultiplicatorVisitor = new PointScalarMultiplicationVisitor();

	@Test
    public void testscalarMult() {
    	Point3D p31 = new Point3D(1, 1, 1, subtractorVisitor, scalarMultiplicatorVisitor);
    	Point3D p32 = new Point3D(1, 1, 1, subtractorVisitor, scalarMultiplicatorVisitor);
    	Point2D p21 = new Point2D(1, 1, subtractorVisitor, scalarMultiplicatorVisitor);
    	Point2D p22 = new Point2D(1, 1, subtractorVisitor, scalarMultiplicatorVisitor);
    	Assert.assertEquals(3, p31.scalarMult(p32), 0.001);
    	Assert.assertEquals(2, p21.scalarMult(p22), 0.001);
    }

	@Test
    public void testSubtract() {
    	Point3D p31 = new Point3D(1, 1, 1, subtractorVisitor, scalarMultiplicatorVisitor);
    	Point3D p32 = new Point3D(1, 1, 1, subtractorVisitor, scalarMultiplicatorVisitor);
    	Point2D p21 = new Point2D(1, 1, subtractorVisitor, scalarMultiplicatorVisitor);
    	Point2D p22 = new Point2D(1, 1, subtractorVisitor, scalarMultiplicatorVisitor);
    	Assert.assertEquals(new Point3D(0, 0, 0), p31.subtract(p32));
    	Assert.assertEquals(new Point2D(0, 0), p21.subtract(p22));
    }

	@Test(expected = IllegalArgumentException.class)
	public void testSubtractInappropriateClasses() {
    	Point3D p31 = new Point3D(1, 1, 1, subtractorVisitor, scalarMultiplicatorVisitor);
    	Point3D p32 = new Point3D(1, 1, 1, subtractorVisitor, scalarMultiplicatorVisitor);
    	Point2D p21 = new Point2D(1, 1, subtractorVisitor, scalarMultiplicatorVisitor);
    	Point2D p22 = new Point2D(1, 1, subtractorVisitor, scalarMultiplicatorVisitor);
        p31.subtract(p22);
    	p21.subtract(p32);		
	}

	@Test(expected = IllegalArgumentException.class)
    public void testscalarMultInappropriateClasses() {
    	Point3D p31 = new Point3D(1, 1, 1, subtractorVisitor, scalarMultiplicatorVisitor);
    	Point3D p32 = new Point3D(1, 1, 1, subtractorVisitor, scalarMultiplicatorVisitor);
    	Point2D p21 = new Point2D(1, 1, subtractorVisitor, scalarMultiplicatorVisitor);
    	Point2D p22 = new Point2D(1, 1, subtractorVisitor, scalarMultiplicatorVisitor);
    	p31.scalarMult(p22);
    	p21.scalarMult(p32);
    }
}
