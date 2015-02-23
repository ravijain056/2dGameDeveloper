package data.universe2D;

import data.util.creature.RectangularUser;
import data.vector2D.Direction2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class RectangularSolid2D extends SolidObject2D {

    public RectangularSolid2D() {
        this(Universe2D.defaultUniverse);
    }

    public RectangularSolid2D(Universe2D universe) {
        super(universe);
    }

    @Override
    public boolean intersects(Rectangle2D rect) {
        return getBounds().intersects(rect);
    }

    @Override
    public boolean contains(double x, double y) {
        return getBounds().contains(x, y);
    }

    @Override
    public SolidObject2D[] getTouchingSolid2Ds() {
        ArrayList<Object2D> objs = new ArrayList<>(Arrays.asList
                (getUniverse2D().getObject2DsIn(getBounds())));
        objs.remove(this);
        for (Object2D obj : objs) {
            if (!(obj instanceof SolidObject2D)) {
                objs.remove(obj);
            }
        }
        return objs.toArray(new SolidObject2D[objs.size()]);
    }

    @Override
    public Point2D getContactPoint(SolidObject2D obj) {
        if (!isTouching(obj)) {
            return null;
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Direction2D getNormalDirection(SolidObject2D obj) {
        if (!isTouching(obj)) {
            System.out.println("nan rectsolid");
            return new Direction2D(Double.NaN);
        }
        Rectangle2D bounds = obj.getBounds();
        if (bounds.getMaxY() == getBounds().getMinY()) {
            if (bounds.getMaxX() == getBounds().getMinX()) {
                return new Direction2D(135);
            }
            if (getBounds().getMaxX() == bounds.getMinX()) {
                return new Direction2D(45);
            }
            return new Direction2D(90);
        }
        if (getBounds().getMaxY() == bounds.getMinY()) {
            if (bounds.getMaxX() == getBounds().getMinX()) {
                return new Direction2D(225);
            }
            if (getBounds().getMaxX() == bounds.getMinX()) {
                return new Direction2D(315);
            }
            return new Direction2D(270);
        }
        if (bounds.getMaxX() == getBounds().getMinX()) {
            return new Direction2D(180);
        }
        if (getBounds().getMaxX() == bounds.getMinX()) {
            return new Direction2D(0);
        }
        if (obj instanceof RectangularUser) {
            obj.setBounds(new data.shapes.Rectangle2D(obj.getBounds().getX(),
                    getBounds().getY() - obj.getBounds().getHeight() - 0.05, 
                    obj.getBounds().getWidth(), obj.getBounds().getHeight()));
        }
        return new Direction2D(90);
    }
}