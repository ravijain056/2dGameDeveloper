package data.shapes;

public class Rectangle2D extends java.awt.geom.Rectangle2D.Double {

    public Rectangle2D() {
    }

    public Rectangle2D(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        if (isEmpty()) {
            return false;
        }
        double x0 = getX();
        double y0 = getY();
        return (x + w >= x0
                && y + h >= y0
                && x <= x0 + getWidth()
                && y <= y0 + getHeight());
    }
}
