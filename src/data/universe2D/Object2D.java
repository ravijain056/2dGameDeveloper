package data.universe2D;

import data.universe2D.event.Object2DEvent;
import data.universe2D.event.Object2DListener;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.Timer;

public abstract class Object2D {

    private Universe2D universe;
    private double mass;
    private double charge;
    private double life;
    private Rectangle2D bounds;
    private Rectangle2D prevBounds;
    private Image image;
    private ArrayList<Object2DListener> listeners;
    private Timer gravitationalForce;

    public Object2D() {
        this(Universe2D.defaultUniverse);
    }

    public Object2D(final Universe2D universe) {
        mass = 0;
        charge = 0;
        listeners = new ArrayList<>(3);
        life = 100;
        image = null;
        if (this instanceof NewtonianObject2D) {
            final NewtonianObject2D nobj = (NewtonianObject2D) this;
            gravitationalForce = new Timer(10, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (Object2D obj : 
                            universe.getObject2DsIn(nobj.getRange())) {
                        if (obj instanceof SolidObject2D && 
                                obj != Object2D.this) {
                            ((SolidObject2D) obj).applyForce
                                    (nobj.getGravitationalForce(obj));
                        }
                    }
                }
            });
        }
        universe.addObject2Ds(this);
    }

    public Universe2D getUniverse2D() {
        return universe;
    }

    protected void setUniverse2D(Universe2D universe) {
        this.universe = universe;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    public double getLife() {
        return life;
    }

    public void setLife(double life) {
        if (life > 100 || life < 0) {
            throw new IllegalArgumentException("Invalid argument "
                    + "for life :" + life);
        }
        if (life == 0) {
            destroy();
        }
        this.life = life;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image img) {
        this.image = img;
    }

    public Rectangle2D getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle2D bounds) {
        prevBounds = this.bounds;
        this.bounds = bounds;
        for (Object2DListener listener : listeners) {
            listener.boundsChanged(new Object2DEvent(this));
        }
    }

    public void addObject2DListener(Object2DListener listener) {
        boolean add = listeners.add(listener);
    }

    public void removeObject2DListener(Object2DListener listener) {
        listeners.remove(listener);
    }

    public void destroy() {
        getUniverse2D().removeObject2Ds(this);
        for (Object2DListener listener : listeners) {
            listener.destroyed(new Object2DEvent(this));
        }
    }

    public boolean isTouching(Object2D obj) {
        if (obj.intersects(getBounds()) && intersects(obj.getBounds())) {
            return true;
        }
        return false;
    }

    public void pause() {
        if (this instanceof NewtonianObject2D) {
            gravitationalForce.stop();
        }
    }

    public void play() {
        if (this instanceof NewtonianObject2D) {
            gravitationalForce.start();
        }
    }

    protected Rectangle2D getPreviousBouds() {
        return prevBounds;
    }

    public abstract boolean intersects(Rectangle2D rect);

    public abstract boolean contains(double x, double y);
}