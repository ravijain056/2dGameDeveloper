package data.universe2D.event;

import data.universe2D.Renderer;
import java.util.EventObject;

public class RendererEvent extends EventObject {

    public RendererEvent(Renderer source) {
        super(source);
    }

    @Override
    public Renderer getSource() {
        return (Renderer)(super.getSource()); 
    }
    
}
