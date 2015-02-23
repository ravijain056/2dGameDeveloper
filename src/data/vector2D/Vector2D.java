package data.vector2D;

public class Vector2D extends Direction2D {

    private double magnitude;

    public Vector2D() {
        this(0, 0);
    }

    public Vector2D(double x, double y) {
        this(StrictMath.hypot(x, y), 
                new Direction2D(StrictMath.toDegrees(StrictMath.atan2(-y, x))));
    }

    public Vector2D(double magnitude, Direction2D direc) {
        super(direc.getTheta());
        this.magnitude = magnitude;
        standardise();
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
        standardise();
    }

    public double getComponentMagnitude(Direction2D direc) {
       return magnitude * StrictMath.cos(StrictMath.toRadians(getTheta(direc)));
    }

    public void setComponentMagnitude(Direction2D direc, double magnitude) {
        subtract(getProjection(direc));
        add(new Vector2D(magnitude, direc));
    }

    public double getXComponent() {
        return (magnitude * getCosine());
    }

    public double getYComponent() {
        return -(magnitude * getSine());
    }

    public void setXComponent(double x) {
        double y = getYComponent();
        magnitude = StrictMath.hypot(x, y);
        setTheta(StrictMath.toDegrees(StrictMath.atan2(-y, x)));
        standardise();
    }

    public void setYComponent(double y) {
        double x = getXComponent();
        magnitude = StrictMath.hypot(x, y);
        setTheta(StrictMath.toDegrees(StrictMath.atan2(-y, x)));
        standardise();
    }

    public void addXComponent(double x) {
        setXComponent(getXComponent() + x);
    }

    public void addYComponent(double y) {
        setYComponent(getYComponent() + y);
    }

    public void add(Vector2D vector) {
        addXComponent(vector.getXComponent());
        addYComponent(vector.getYComponent());
    }

    public void subtract(Vector2D vector) {
        addXComponent(-vector.getXComponent());
        addYComponent(-vector.getYComponent());
    }

    public void multiply(double scalar) {
        magnitude *= scalar;
        standardise();
    }

    public Vector2D getProjection(Direction2D direc) {
        return new Vector2D(magnitude * StrictMath.cos
                (StrictMath.toRadians(getTheta(direc))), direc);
    }

    public Vector2D getNormalProjection(Direction2D direc) {
        return new Vector2D(magnitude * StrictMath.sin(StrictMath.toRadians
                (getTheta(direc))), Direction2D.getNormal(direc));
    }

    public static Vector2D getAddition(Vector2D vec1, Vector2D vec2) {
        return new Vector2D(vec1.getXComponent() + vec2.getXComponent(), 
                vec1.getYComponent() + vec2.getYComponent());
    }

    public static Vector2D getSubtraction(Vector2D vec1, Vector2D vec2) {
        return new Vector2D(vec1.getXComponent() - vec2.getXComponent(),
                vec1.getYComponent() - vec2.getYComponent());
    }

    public static double getDotProduct(Vector2D vec1, Vector2D vec2) {
        return vec1.getMagnitude() * vec2.getMagnitude() * 
                StrictMath.cos(StrictMath.toRadians(vec1.getTheta(vec2)));
    }

    @Override
    public String toString() {
      return "Vector2D[Magnitude:" + magnitude + ", Theta:" + getTheta() + "].";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Vector2D) {
            if (super.equals(obj)) {
                Vector2D vec = (Vector2D) obj;
                if (vec.getMagnitude() == getMagnitude()) {
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (int) (Double.doubleToLongBits(magnitude)
                ^ (Double.doubleToLongBits(magnitude) >>> 32)
                + (int) (Double.doubleToLongBits(getTheta())) ^ 
                (Double.doubleToLongBits(getTheta()) >>> 32));
        return hash;
    }

    private void standardise() {
        if (magnitude < 0) {
            magnitude = -magnitude;
            setTheta(getTheta() + 180);
        }
    }
}
