package ourrobots.ags.polylunar.robotdata.log;

import java.util.ArrayList;
import java.util.List;

public class TickLog {
    private List<TickMovie> movies = new ArrayList<TickMovie>();
    private TickMovie currentMovie = null;
    
    public void movieEnded() {
        currentMovie = null;
    }
    
    public void recordTick(TickRecord entry) {
        if (currentMovie == null) {
            currentMovie = new TickMovie();
            movies.add(currentMovie);
        }
        currentMovie.addEntry(entry);
    }
    
    public TickRecord getLastRecord() {
        if (currentMovie == null)
            return null;
        return currentMovie.getLast();
    }
}
