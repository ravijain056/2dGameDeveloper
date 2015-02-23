package data.util.creature;

import data.universe2D.RectangularSolid2D;
import data.universe2D.SolidObject2D;
import data.universe2D.Universe2D;
import data.vector2D.Direction2D;
import data.vector2D.Vector2D;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer;

public class RectangularUser extends RectangularSolid2D {

    private double strength;
    private Timer keyForceTimer, imageSetter;
    int keyUp, keyDown, keyRight, keyLeft;
    boolean up, down, right, left;
    Image[] leftimg, rightimg;

    public RectangularUser(Component keyObserver) {
        this(Universe2D.defaultUniverse, keyObserver);
    }

    public RectangularUser(Universe2D universe, Component keyObserver) {
        this(universe, keyObserver, 38, 40, 37, 39);
    }

    public RectangularUser(Universe2D universe, Component keyObserver, 
            int keyUp, int keyDown, int keyLeft, int keyRight) {
        super(universe);
        strength = Double.POSITIVE_INFINITY;
        setCoFriction(0.5);
        this.keyLeft = keyLeft;
        this.keyRight = keyRight;
        this.keyUp = keyUp;
        this.keyDown = keyDown;
        imageSetter = new Timer(100, new ActionListener() {
            int i = 0;
            boolean left = true;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (getVelocity().getXComponent() < -0.1 &&
                        checkSolidObject2D(new Direction2D(270))) {
                    if (left) {
                        setImage(leftimg[i++]);
                    } else {
                        i = 0;
                        setImage(leftimg[0]);
                    }
                    left = true;
                } else if (getVelocity().getXComponent() > 0.1 && 
                        checkSolidObject2D(new Direction2D(270))) {
                    if (!left) {
                        setImage(rightimg[i++]);
                    } else {
                        i = 0;
                        setImage(rightimg[0]);
                    }
                    left = false;
                }
                if(i==8){
                    i=0;
                }
            }
        });
        keyForceTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vector2D keyForce = new Vector2D();
                if (up) {
                    if (checkSolidObject2D(new Direction2D(270))) {
                        keyForce.addYComponent(-6000);
                    }
                }
                if (down) {
                    if (checkSolidObject2D(new Direction2D(90))) {
                        keyForce.addYComponent(8000);
                    }
                }
                if (left) {
                    if (checkSolidObject2D(new Direction2D(90)) || 
                            checkSolidObject2D(new Direction2D(270))) {
                        keyForce.addXComponent(-300);
                    }
                    keyForce.addXComponent(-100);
                }
                if (right) {
                    if (checkSolidObject2D(new Direction2D(90)) || 
                            checkSolidObject2D(new Direction2D(270))) {
                        keyForce.addXComponent(300);
                    }
                    keyForce.addXComponent(100);
                }
                applyForce(keyForce);
            }
        });
        keyObserver.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == RectangularUser.this.keyUp) {
                    up = true;
                }
                if (e.getKeyCode() == RectangularUser.this.keyDown) {
                    down = true;
                }
                if (e.getKeyCode() == RectangularUser.this.keyLeft) {
                    left = true;
                }
                if (e.getKeyCode() == RectangularUser.this.keyRight) {
                    right = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == RectangularUser.this.keyUp) {
                    up = false;
                }
                if (e.getKeyCode() == RectangularUser.this.keyDown) {
                    down = false;
                }
                if (e.getKeyCode() == RectangularUser.this.keyLeft) {
                    left = false;
                }
                if (e.getKeyCode() == RectangularUser.this.keyRight) {
                    right = false;
                }
            }
        });
        keyObserver.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                up = false;
                down = false;
                left = false;
                right = false;
            }
        });
    }

    public double getStrength() {
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    @Override
    public double getStrength(Direction2D direc) {
        return strength;
    }

    @Override
    public void pause() {
        super.pause();
        keyForceTimer.stop();
        imageSetter.stop();
    }

    @Override
    public void play() {
        super.play();
        keyForceTimer.start();
        imageSetter.start();
    }

    public void setImageArray(Image[] left, Image[] right) {
        setImage(right[0]);
        leftimg = left;
        rightimg = right;
    }

    public synchronized boolean checkSolidObject2D(Direction2D direc) {
        for (SolidObject2D obj : getTouchingSolid2Ds()) {
            if (getNormalDirection(obj).equals(direc)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void harm(double harmFactor) {
    }
}
