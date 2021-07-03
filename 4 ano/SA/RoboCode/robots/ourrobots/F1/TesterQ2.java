package ourrobots.F1;


import java.awt.*;

public class TesterQ2 extends ITester {

    public void run() {
        setColors(Color.yellow,Color.yellow,Color.yellow); // body,gun,radar

        double width = this.getBattleFieldWidth();
        double height = this.getBattleFieldHeight();

        int round = this.getRoundNumber()%5;

        if(round == 0) //Normal
        {
            finalX = (getRandomNumber(distanceFromWalls, width / 2 - distanceFromRef));
            finalY = (getRandomNumber(height / 2, height - distanceFromWalls));
        }
        else if(round == 1) //Shortcut 1
        {
            finalX = (width / 2 - distanceFromRef);
            finalY = (height / 2 + distanceFromRef);
        }
        else if(round == 2) //Shortcut 2
        {
            finalX = (width / 2 - distanceFromRef);
            finalY = (height - distanceFromWalls);
        }
        else if(round == 3) //Shortcut 4
        {
            finalX = (width / 4);
            finalY = (3 * height / 4);
        }
        else if(round == 4) //Efficiency
        {
            finalX = (width / 2 - distanceFromRef);
            finalY = (height - distanceFromWalls);
        }

        while(true)
            move(finalX,finalY);
    }

}
