package ourrobots.ags.polylunar;

import ourrobots.ags.polylunar.core.Moon;

import java.awt.*;

public class Io extends Moon {
    private static final Color c = new Color(204, 48, 20);
    
    @Override
    public void init() {
        super.init();
        this.setColors(c, c, c, c.darker());
    }
}
