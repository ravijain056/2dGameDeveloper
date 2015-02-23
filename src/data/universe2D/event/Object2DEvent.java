package data.universe2D.event;

import data.universe2D.Object2D;
import java.util.EventObject;

public class Object2DEvent extends EventObject {

    public Object2DEvent(Object2D source) {
        super(source);
    }

    @Override
    public Object2D getSource() {
        return (Object2D) (super.getSource());
    }
}
