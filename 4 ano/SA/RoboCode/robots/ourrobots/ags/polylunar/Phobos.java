package ourrobots.ags.polylunar;

import ourrobots.ags.polylunar.core.Moon;

import java.awt.*;

public class Phobos extends Moon {
    private static final Color c = new Color(201, 162, 162);
    
    @Override
    public void init() {
        super.init();
        this.setColors(c, c, c, c.darker());
    }
}
