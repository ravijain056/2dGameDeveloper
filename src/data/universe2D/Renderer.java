package data.universe2D;

import data.universe2D.event.Object2DAdapter;
import data.universe2D.event.Object2DEvent;
import data.universe2D.event.Object2DListener;
import data.universe2D.event.RendererEvent;
import data.universe2D.event.RendererListener;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;

public class Renderer extends Component {

    private Dimension2D scale;
    private Rectangle2D viewPort;
    private Image backgroundImg;
    private Universe2D universe;
    private ArrayList<RendererListener> listeners;
    private ArrayList<Object2D> viewObjs;

    public Renderer(Rectangle2D viewPort) {
        listeners = new ArrayList<>(3);
        setViewPort(viewPort);
        scale = new Dimension(0, 0);
        viewObjs = new ArrayList<>(1);
        setCursor(new Cursor(13));
    }

    @Override
    public void paint(Graphics g) {
        if (backgroundImg != null) {
            g.drawImage(getBackgroundImg(), getX(), getY(), 
                    getWidth(), getHeight(), this);
        } else if (getBackground() != null) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        if (universe != null) {
            imageUpdate(g);
        }
    }

    public Rectangle2D getViewPort() {
        return viewPort;
    }

    public void setViewPort(Rectangle2D viewPort) {
        this.viewPort = viewPort;
        repaint();
        for (RendererListener listener : listeners) {
            listener.viewPortChanged(new RendererEvent(this));
        }
    }

    public void addRendererListener(RendererListener listener) {
        listeners.add(listener);
    }

    public void removeRendererListener(RendererListener listener) {
        listeners.remove(listener);
    }

    public Universe2D getUniverse() {
        return universe;
    }

    public void setUniverse(Universe2D universe) {
        this.universe = universe;
    }

    public Image getBackgroundImg() {
        return backgroundImg;
    }

    public void setBackgroundImg(Image backgroundImg) {
        this.backgroundImg = backgroundImg;
    }

     public Dimension2D getScale() {
        return scale;
    }

    public void setScale(Dimension2D scale) {
        this.scale = scale;
    }

    private void imageUpdate(final Graphics g) {
        ArrayList<Object2D> objs = new ArrayList<>(Arrays.asList
                (getUniverse().getObject2DsIn(viewPort)));
        Object2DListener objAdapter = new Object2DAdapter() {
            @Override
            public void boundsChanged(Object2DEvent e) {
                repaint();
            }
        };
        for (Object2D obj : objs) {
            imageUpdate(g, obj);
            if (!viewObjs.contains(obj)) {
                obj.addObject2DListener(objAdapter);
            }
        }
        for (Object2D obj : viewObjs) {
            if (!objs.contains(obj)) {
                obj.removeObject2DListener(objAdapter);
            }
        }
        viewObjs = objs;
    }

    private void imageUpdate(Graphics g, Object2D obj) {
        Rectangle2D objBounds = obj.getBounds();
        g.drawImage(obj.getImage(), (int) ((objBounds.getX() - viewPort.getX())
                * getScale().getWidth()), (int) ((objBounds.getY() - 
                viewPort.getY()) * getScale().getHeight()), (int) 
                (objBounds.getWidth() * getScale().getWidth()), (int) 
                (objBounds.getHeight() * getScale().getHeight()), this);
    }
}
