package data.universe2D;

import data.vector2D.Direction2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class EllipticalSolid2D extends SolidObject2D {

    Ellipse2D objShape;

    public EllipticalSolid2D() {
        this(Universe2D.defaultUniverse);
    }

    public EllipticalSolid2D(Universe2D universe) {
        super(universe);
        objShape = new data.shapes.Ellipse2D();
    }

    @Override
    public void setBounds(Rectangle2D bounds) {
        objShape.setFrame(bounds);
        super.setBounds(bounds);
    }

    @Override
    public Direction2D getNormalDirection(SolidObject2D obj) {
        if(!isTouching(obj)) {
            return new Direction2D(Double.NaN);
        }
        Point2D p1 = getContactPoint(obj);
        if (p1.getY() == objShape.getCenterY()) {
            if (p1.getX() < objShape.getCenterX()) {
                return new Direction2D(180);
            } else if (p1.getX() > objShape.getCenterX()) {
                return new Direction2D(0);
            } else {
                throw new Error("Unexpected error");
            }
        }
        double y2;
        if (p1.getY() < objShape.getCenterY()) {
            y2 = p1.getY() - 1;
        } else {
            y2 = p1.getY() + 1;
        }
        //x2 = (x1 + ((y1-y2)(x1-cx)(b^2)/(y1-cy)/(a^2)
        return new Direction2D(p1, new Point2D.Double((p1.getX() + ((y2 - p1.getY()) * (p1.getX() - objShape.getCenterX()) * (Math.pow((objShape.getHeight() / 2), 2)) / (p1.getY() - objShape.getCenterY()) / (Math.pow((objShape.getWidth() / 2), 2)))), y2));
    }

    @Override
    public boolean intersects(Rectangle2D rect) {
        return objShape.intersects(rect);
    }

    @Override
    public boolean contains(double x, double y) {
        return objShape.contains(x, y);
    }

    @Override
    public SolidObject2D[] getTouchingSolid2Ds() {
        ArrayList<Object2D> objs = new ArrayList<>(Arrays.asList(getUniverse2D().getObject2DsIn(objShape)));
        objs.remove(this);
        for (Object2D obj : objs) {
            if(!(obj instanceof SolidObject2D)) {
                objs.remove(obj);
            }
        }
        return objs.toArray(new SolidObject2D[objs.size()]);
    }

    @Override
    public Point2D getContactPoint(SolidObject2D obj) {
        if(!isTouching(obj)) {
            return null;
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
