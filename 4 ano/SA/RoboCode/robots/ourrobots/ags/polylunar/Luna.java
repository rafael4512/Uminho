package ourrobots.ags.polylunar;

import ourrobots.ags.polylunar.core.Moon;

import java.awt.*;

public class Luna extends Moon {
    private static final Color c = new Color(162, 198, 201);
    
    @Override
    public void init() {
        super.init();
        this.setColors(c, c, c, c.darker());
    }
}
