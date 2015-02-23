package data.util.environment;

import data.universe2D.RectangularSolid2D;
import data.universe2D.Universe2D;
import data.vector2D.Direction2D;
import data.vector2D.Vector2D;

public class Block extends RectangularSolid2D {

    {
        setMass(1000);
        setCoFriction(0.7);
    }

    public Block() {
        this(Universe2D.defaultUniverse);
    }

    public Block(Universe2D universe) {
        super(universe);
    }

    @Override
    public double getStrength(Direction2D direc) {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public void harm(double harmFactor) {
        // Cannot harm land.
    }

    @Override
    public void applyForce(Vector2D force) {
        //unmovable
    }

    @Override
    public void applyForce(Vector2D force, double time) {
        //unmovable
    }
}
