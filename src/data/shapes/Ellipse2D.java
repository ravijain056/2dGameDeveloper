package data.shapes;

public class Ellipse2D extends java.awt.geom.Ellipse2D.Double {

    public Ellipse2D() {
        super(0, 0, 0, 0);
    }

    public Ellipse2D(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        if (w <= 0.0 || h <= 0.0) {
            return false;
        }
        // Normalize the rectangular coordinates compared to the ellipse
        // having a center at 0,0 and a radius of 0.5.
        double ellw = getWidth();
        if (ellw <= 0.0) {
            return false;
        }
        double normx0 = (x - getX()) / ellw - 0.5;
        double normx1 = normx0 + w / ellw;
        double ellh = getHeight();
        if (ellh <= 0.0) {
            return false;
        }
        double normy0 = (y - getY()) / ellh - 0.5;
        double normy1 = normy0 + h / ellh;
        // find nearest x (left edge, right edge, 0.0)
        // find nearest y (top edge, bottom edge, 0.0)
        // if nearest x,y is inside circle of radius 0.5, then intersects
        double nearx, neary;
        if (normx0 > 0.0) {
            // center to left of X extents
            nearx = normx0;
        } else if (normx1 < 0.0) {
            // center to right of X extents
            nearx = normx1;
        } else {
            nearx = 0.0;
        }
        if (normy0 > 0.0) {
            // center above Y extents
            neary = normy0;
        } else if (normy1 < 0.0) {
            // center below Y extents
            neary = normy1;
        } else {
            neary = 0.0;
        }
        return (nearx * nearx + neary * neary) <= 0.25;
    }
}
