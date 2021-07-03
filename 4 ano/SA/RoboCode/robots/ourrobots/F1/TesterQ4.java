package ourrobots.F1;

import java.awt.*;

public class TesterQ4 extends ITester {

    public void run() {
        setColors(Color.blue,Color.blue,Color.blue); // body,gun,radar

        double width = this.getBattleFieldWidth();
        double height = this.getBattleFieldHeight();

        int round = this.getRoundNumber()%5;

        if(round == 0) //Normal
        {
            finalX = (getRandomNumber(width / 2, width - distanceFromWalls));
            finalY = (getRandomNumber(distanceFromWalls, height / 2 - distanceFromRef));
        }
        else if(round == 1) //Shortcut 1
        {
            finalX = (3 * width / 4);
            finalY = (height / 4);
        }
        else if(round == 2) //Shortcut 2
        {
            finalX = (width - distanceFromWalls);
            finalY = (height / 2 - distanceFromWalls);
        }
        else if(round == 3) //Shortcut 4
        {
            finalX = (width / 2 + distanceFromRef);
            finalY = (height / 2 - distanceFromRef);
        }
        else if(round == 4) //Efficiency
        {
            finalX = (width - distanceFromWalls);
            finalY = (height / 2 - distanceFromRef);
        }

        while(true)
            move(finalX,finalY);

    }

}
