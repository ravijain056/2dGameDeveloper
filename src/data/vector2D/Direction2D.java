package data.vector2D;

import java.awt.geom.Point2D;

public class Direction2D {

    private double theta;

    public Direction2D(double theta) {
        this.theta = theta;
        standardise();
    }
    
    public Direction2D(Point2D p1, Point2D p2) {
        if(p1.equals(p2)) {
            System.out.println("NAn Direction 2D");
            theta = Double.NaN;
            return;
        }
        theta = StrictMath.toDegrees(StrictMath.atan2(p1.getY() - 
                p2.getY(), p2.getX() - p1.getX()));
        standardise();
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
        standardise();
    }

    public double getCosine() {
        return Math.cos(Math.toRadians(theta));
    }

    public double getSine() {
        return Math.sin(Math.toRadians(theta));
    }

    public double getTheta(Direction2D direc) {
        return theta - direc.getTheta();
    }

    public void reverse() {
        setTheta(getTheta() + 180);
    }
    
    public static Direction2D getNormal(Direction2D direc) {
        return new Direction2D(direc.getTheta() + 90);
    }

    public static Direction2D getReverse(Direction2D direc) {
        return new Direction2D(direc.getTheta() + 180);
    }

    @Override
    public String toString() {
        return "Direction2D[Theta:" + theta + "].";
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj instanceof Direction2D) {
            return (((Direction2D)obj).getTheta() == getTheta());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + (int) (Double.doubleToLongBits(theta) ^ 
                (Double.doubleToLongBits(theta) >>> 32));
        return hash;
    }

    private void standardise() {
        while (theta >= 360) {
            theta -= 360;
        }
        while (theta <= -360) {
            theta += 360;
        }
        if (theta < 0) {
            theta = 360 + theta;
        }
    }
}
