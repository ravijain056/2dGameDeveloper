package data.universe2D;

import data.vector2D.Vector2D;
import java.awt.Shape;


public interface NewtonianObject2D {

    public Shape getRange();

    public Vector2D getGravitationalForce(Object2D obj);
}
