package data.universe2D;

import java.awt.Shape;
import java.util.ArrayList;
import java.awt.geom.Point2D;

public class Universe2D {

    private int objCount;
    private ArrayList<Object2D> object2Ds;
    public static final Universe2D defaultUniverse = new Universe2D(10);

    public Universe2D() {
        this(100);
    }

    public Universe2D(int initCapacity) {
        objCount = 0;
        object2Ds = new java.util.ArrayList<>(initCapacity);
    }

    public void addObject2Ds(Object2D... objs) {
        for (Object2D obj : objs) {
            if (obj.getUniverse2D() != null) {
                obj.getUniverse2D().removeObject2Ds(objs);
            }
            object2Ds.add(obj);
            obj.setUniverse2D(this);
            objCount++;
        }
    }

    public void removeObject2Ds(Object2D... objs) {
        for (Object2D obj : objs) {
            object2Ds.remove(obj);
            obj.setUniverse2D(null);
            objCount--;
        }
    }

    public int getObject2DCount() {
        return objCount;
    }

    public Object2D getObject2D(int n) {
        return object2Ds.get(n);
    }

    public Object2D[] getObject2DAt(Point2D p) {
        return getObject2DAt(p.getX(), p.getY());
    }

    public Object2D[] getObject2DAt(double x, double y) {
        ArrayList<Object2D> objs = new ArrayList<>(2);
        for (Object2D obj : object2Ds) {
            if (obj != null) {
                if (obj.contains(x, y)) {
                    objs.add(obj);
                }
            }
        }
        return objs.toArray(new Object2D[objs.size()]);
    }

    public Object2D[] getObject2DsIn(Shape range) {
        if(range == null) {
            return new Object2D[0];
        }
        ArrayList<Object2D> objs = new ArrayList<>(50);
        for (Object2D obj : object2Ds) {
            if (range.intersects(obj.getBounds())) {
                if (obj.intersects(range.getBounds2D().createIntersection
                        (obj.getBounds()))) {
                    objs.add(obj);
                }
            }
        }
        return objs.toArray(new Object2D[objs.size()]);
    }
    
    public void play() {
        for (Object2D obj : object2Ds) {
            obj.play();
        }
    }
    
    public void pause() {
        for (Object2D obj : object2Ds) {
            obj.pause();
        }
    }
}