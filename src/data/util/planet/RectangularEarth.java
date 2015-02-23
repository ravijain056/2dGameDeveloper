package data.util.planet;

import data.universe2D.UniversalConstants;
import data.universe2D.Universe2D;

public class RectangularEarth extends RectangularPlanet{

    {
        setMass(UniversalConstants.Me);
        setArbitraryRadius(UniversalConstants.Re);
        setCoFriction(0.5);
    }
    
    public RectangularEarth() {
        this(Universe2D.defaultUniverse);
    }

    public RectangularEarth(Universe2D universe) {
        super(universe);
    }
    
}
