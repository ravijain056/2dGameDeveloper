package data.util.planet;

import data.universe2D.NewtonianObject2D;
import data.universe2D.Object2D;
import data.universe2D.RectangularSolid2D;
import data.universe2D.UniversalConstants;
import data.universe2D.Universe2D;
import data.vector2D.Direction2D;
import data.vector2D.Vector2D;
import java.awt.Shape;

public abstract class RectangularPlanet extends RectangularSolid2D 
                                            implements NewtonianObject2D {

    Shape range;
    double arbitraryRadius;

    public RectangularPlanet() {
        this(Universe2D.defaultUniverse);
    }

    public RectangularPlanet(Universe2D universe) {
        super(universe);
        range = null;
        arbitraryRadius = 0;
    }

    public double getArbitraryRadius() {
        return arbitraryRadius;
    }

    public void setArbitraryRadius(double arbitraryRadius) {
        this.arbitraryRadius = arbitraryRadius;
    }

    public void setRange(Shape range) {
        this.range = range;
    }

    @Override
    public Shape getRange() {
        return range;
    }

    @Override
    public double getStrength(Direction2D direc) {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public void harm(double harmFactor) {
        // Cannot harm any Planet.
    }

    @Override
    public void applyForce(Vector2D force) {
        //unmovable
    }

    @Override
    public void applyForce(Vector2D force, double time) {
        //unmovable
    }
    
    @Override
    public Vector2D getGravitationalForce(Object2D obj) {
        Direction2D direc;
        double dist;
        if (obj.getBounds().getMaxY() <= getBounds().getMinY()) {
            if (obj.getBounds().getMaxX() <= getBounds().getMinX()) {
                direc = new Direction2D(315);
                dist = Math.sqrt(Math.pow((obj.getBounds().getCenterX() - 
                        getBounds().getMinX()), 2) + 
                        Math.pow((obj.getBounds().getCenterY() - 
                        getBounds().getMinY()), 2));
            } else if (obj.getBounds().getMinX() >= getBounds().getMaxX()) {
                direc = new Direction2D(225);
                dist = Math.sqrt(Math.pow((obj.getBounds().getCenterX() - 
                        getBounds().getMaxX()), 2) + 
                        Math.pow((obj.getBounds().getCenterY() - 
                        getBounds().getMinY()), 2));
            } else {
                direc = new Direction2D(270);
                dist = Math.abs(obj.getBounds().getCenterY() - 
                        getBounds().getMinY());
            }
        } else if (obj.getBounds().getMinY() >= getBounds().getMaxY()) {
            if (obj.getBounds().getMaxX() <= getBounds().getMinX()) {
                direc = new Direction2D(45);
                dist = Math.sqrt(Math.pow((obj.getBounds().getCenterX() - 
                        getBounds().getMinX()), 2) + 
                        Math.pow((obj.getBounds().getCenterY() - 
                        getBounds().getMaxY()), 2));
            } else if (obj.getBounds().getMinX() >= getBounds().getMaxX()) {
                direc = new Direction2D(135);
                dist = Math.sqrt(Math.pow((obj.getBounds().getCenterX() - 
                        getBounds().getMaxX()), 2) + 
                        Math.pow((obj.getBounds().getCenterY() - 
                        getBounds().getMaxY()), 2));
            } else {
                direc = new Direction2D(90);
                dist = Math.abs(obj.getBounds().getCenterY() - 
                        getBounds().getMinY());
            }
        } else if (obj.getBounds().getMaxX() <= getBounds().getMinX()) {
          direc = new Direction2D(0);
          dist = Math.abs(obj.getBounds().getCenterX() - getBounds().getMinX());
        } else if (obj.getBounds().getMinX() >= getBounds().getMaxX()) {
          direc = new Direction2D(180);
          dist = Math.abs(obj.getBounds().getCenterX() - getBounds().getMaxX());
        } else {
            System.out.println("Nan rectplanet");
            return new Vector2D();
        }
        return new Vector2D((UniversalConstants.G * getMass() * obj.getMass()
                / (Math.pow((arbitraryRadius + dist), 2))), direc);
    }
}