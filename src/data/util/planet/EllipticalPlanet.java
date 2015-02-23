package data.util.planet;

import data.universe2D.EllipticalSolid2D;
import data.universe2D.NewtonianObject2D;
import data.universe2D.Object2D;
import data.universe2D.UniversalConstants;
import data.universe2D.Universe2D;
import data.vector2D.Direction2D;
import data.vector2D.Vector2D;
import java.awt.Shape;
import java.awt.geom.Point2D;

public class EllipticalPlanet extends EllipticalSolid2D implements NewtonianObject2D {

    Shape range;
    double arbitraryRadius;

    public EllipticalPlanet() {
        this(Universe2D.defaultUniverse);
    }

    public EllipticalPlanet(Universe2D universe) {
        super(universe);
        range = null;
        arbitraryRadius = 0;
    }

    @Override
    public double getStrength(Direction2D direc) {
        return Double.POSITIVE_INFINITY;
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
    public Vector2D getGravitationalForce(Object2D obj) {
        Point2D p1 = new Point2D.Double(obj.getBounds().getCenterX(), obj.getBounds().getCenterY()), p2 = new Point2D.Double(getBounds().getCenterX(), getBounds().getCenterY());
        return new Vector2D((UniversalConstants.G * obj.getMass() * getMass() / Math.pow((arbitraryRadius + p1.distance(p2)), 2)), new Direction2D(p1, p2));
    }

    @Override
    public void harm(double harmFactor) {
        //Cannot harm any planet.
    }
}
