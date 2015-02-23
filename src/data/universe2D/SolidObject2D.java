package data.universe2D;

import data.util.environment.Block;
import data.vector2D.Direction2D;
import data.vector2D.Vector2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.Timer;

public abstract class SolidObject2D extends Object2D {

    private Vector2D velocity;
    private double elasticity;
    private double coFriction;
    private Timer positioner;
    private Timer ContactForces;

    public SolidObject2D() {
        this(Universe2D.defaultUniverse);
    }

    public SolidObject2D(Universe2D universe) {
        super(universe);
        velocity = new Vector2D();
        elasticity = 0;
        coFriction = 0;
        positioner = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (velocity.getMagnitude() != 0) {
                    Rectangle2D bounds = new data.shapes.Rectangle2D
                        (getBounds().getX() + (velocity.getXComponent() * 0.01),
                         getBounds().getY() + (velocity.getYComponent() * 0.01),
                         getBounds().getWidth(), getBounds().getHeight());
                    ArrayList<Object2D> a = new ArrayList<>(Arrays.asList
                            (getUniverse2D().getObject2DsIn
                            (bounds.createUnion(getBounds()))));
                  for (Object2D obj : a) {
                      if (obj instanceof SolidObject2D
                                && SolidObject2D.this != obj
                                && !(SolidObject2D.this instanceof Block)) {
                            Rectangle2D intersection =
                                    obj.getBounds().createIntersection
                                    (bounds.createUnion(getBounds()));
                        if (intersection.getMinY() == obj.getBounds().getMinY()
                         && getBounds().getMaxY() <= obj.getBounds().getMinY()){
                                bounds.setRect(bounds.getX(),
                                obj.getBounds().getY() - getBounds().getHeight(),
                                        bounds.getWidth(), bounds.getHeight());
                            } else if (intersection.getMaxY()
                                    == obj.getBounds().getMaxY() && getBounds().
                                    getMinY() >= obj.getBounds().getMaxY()) {
                                bounds.setRect(bounds.getX(), 
                                        obj.getBounds().getMaxY(), 
                                        bounds.getWidth(), bounds.getHeight());
                            } else if (intersection.getMinX() == 
                                    obj.getBounds().getMinX() && getBounds().
                                    getMaxX() <= obj.getBounds().getMinX()) {
                                bounds.setRect(obj.getBounds().getMinX() - 
                                        getBounds().getWidth(), bounds.getY(),
                                        bounds.getWidth(), bounds.getHeight());
                            } else if (intersection.getMaxX() == 
                                    obj.getBounds().getMaxX() && getBounds()
                                    .getMinX() >= obj.getBounds().getMaxX()) {
                               bounds.setRect(obj.getBounds().getMaxX(), bounds.
                                 getY(), bounds.getWidth(), bounds.getHeight());
                            }
                      }
                  }
                    setBounds(bounds);
                }
            }
        });
        ContactForces = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object2D[] objs = getTouchingSolid2Ds();
                for (Object2D obj : objs) {
                    SolidObject2D sobj = (SolidObject2D) obj;
                    Direction2D normaldirec = getNormalDirection(sobj);
                    Vector2D objVelocity = 
                            sobj.getRelativeVelocity(SolidObject2D.this);
                    //Normal Force.
                    double nobjSpeed = 
                            objVelocity.getComponentMagnitude(normaldirec);
                    double N;
                    if (nobjSpeed >= 0) {
                        N = 0;
                    } else {
                      N = (sobj.getMass() * ((-nobjSpeed * sobj.getElasticity())
                              - nobjSpeed)) / 0.6;
                        double strength = getStrength(normaldirec);
                        if (N < strength) {
                            sobj.applyForce(new Vector2D(N, normaldirec));
                        } else {
                           sobj.applyForce(new Vector2D(strength, normaldirec));
                            harm(N - strength);
                        }
                    }
                    //FrictionForce 
                    Vector2D tobjSpeed = objVelocity.getProjection
                            (Direction2D.getNormal(normaldirec));
                    double objForce = sobj.getMass() * tobjSpeed.getMagnitude();
                    double friction = 
                            ((getCoFriction() + sobj.getCoFriction()) / 2) * N;
                    if (objForce > friction) {
                        sobj.applyForce(new Vector2D(friction,
                                Direction2D.getReverse(tobjSpeed)));
                    } else {
                        sobj.applyForce(new Vector2D(objForce,
                                Direction2D.getReverse(tobjSpeed)));
                    }
                }
            }
        });
    }

    public void applyForce(Vector2D force) {
        applyForce(force, 10);
    }

    public void applyForce(final Vector2D force, final double time) {
        if (force == null || force.getMagnitude() == 0) {
            return;
        }
        final Timer t = new Timer(1, null);
        t.addActionListener(new ActionListener() {
            double timePassed = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (timePassed >= time) {
                    t.stop();
                }
                velocity.add(new Vector2D(
                        (force.getMagnitude() / getMass() * 0.01), force));
                timePassed += t.getDelay();
            }
        });
        t.start();
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public Vector2D getRelativeVelocity(SolidObject2D obj) {
        return Vector2D.getSubtraction(getVelocity(), obj.getVelocity());
    }

    public double getElasticity() {
        return elasticity;
    }

    public void setElasticity(double elasticity) {
        if (elasticity < 0) {
           throw new IllegalArgumentException("Elasticity cannot be negative.");
        }
        this.elasticity = elasticity;
    }

    public double getCoFriction() {
        return coFriction;
    }

    public void setCoFriction(double coFriction) {
        this.coFriction = coFriction;
    }

    @Override
    public void pause() {
        super.pause();
        ContactForces.stop();
        positioner.stop();
    }

    @Override
    public void play() {
        super.play();
        ContactForces.start();
        positioner.start();
    }

    public abstract double getStrength(Direction2D direc);

    public abstract Direction2D getNormalDirection(SolidObject2D obj);

    public abstract SolidObject2D[] getTouchingSolid2Ds();

    public abstract Point2D getContactPoint(SolidObject2D obj);

    public abstract void harm(double harmFactor);
}