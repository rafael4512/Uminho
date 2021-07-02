package ourrobots.ags.polylunar;

import ourrobots.ags.polylunar.core.Moon;

import java.awt.*;

public class Charon extends Moon {
    private static final Color c = new Color(114, 141, 136);
    
    @Override
    public void init() {
        super.init();
        this.setColors(c, c, c, c.darker());
    }
}
