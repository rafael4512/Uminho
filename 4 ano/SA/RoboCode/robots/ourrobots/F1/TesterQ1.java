package ourrobots.F1;


import java.awt.*;

public class TesterQ1 extends ITester {

    public void run() {
        setColors(Color.green,Color.green,Color.green); // body,gun,radar

        double width = this.getBattleFieldWidth();
        double height = this.getBattleFieldHeight();

        int round = this.getRoundNumber()%5;

        if(round == 0) //Normal
        {
            finalX = (getRandomNumber(width / 2 + distanceFromRef, width - distanceFromWalls));
            finalY = (getRandomNumber(height / 2 + distanceFromRef, height - distanceFromWalls));
        }
        else if(round == 1) //Shortcut 1
        {
            finalX = (width / 2 + distanceFromRef);
            finalY = (height - distanceFromWalls);
        }
        else if(round == 2) //Shortcut 2
        {
            finalX = (width / 2 + distanceFromRef);
            finalY = (height / 2 + distanceFromRef);
        }
        else if(round == 3) //Shortcut 4
        {
            finalX = (width - distanceFromWalls);
            finalY = (height / 2 + distanceFromRef);
        }
        else if(round == 4) //Efficiency
        {
            finalX = width/2 + distanceFromRef/2;
            finalY = height/2 + distanceFromRef/2;
        }

        while(true)
            move(finalX,finalY);

    }

    @Override
    public void onPaint(Graphics2D g) {
        g.setColor(Color.white);
        g.setStroke(new BasicStroke(2.0F));

        double width = getBattleFieldWidth();
        double height = getBattleFieldHeight();

        g.drawLine(0, (int)height/2,(int)width, (int)height/2);
        g.drawLine((int)width/2, 0, (int)width/2, (int)height);
    }
}
