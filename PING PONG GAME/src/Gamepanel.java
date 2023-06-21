import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
public class Gamepanel extends JPanel implements Runnable{

    static final int GAME_WIDTH=1000;
    static final int GAME_HEIGHT= (int)(GAME_WIDTH * (0.5555));
    static final Dimension SCREEN_SIZE=new  Dimension(GAME_WIDTH, GAME_HEIGHT );
    static final int BALL_DIAMETER=20;
    static final int PADEL_WIDTH=25;
    static final int PADEL_HEIGHT=100;
    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Padel padel1;
    Padel padel2;
    Ball ball;
    Score score;
    Gamepanel(){
        newPadel();
        newBall();
        score=new Score(GAME_WIDTH,GAME_HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);

        gameThread=new Thread(this);
        gameThread.start();

    }
    public void newBall()
    {
        random=new Random();
        ball=new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2),(GAME_HEIGHT/2)-(BALL_DIAMETER/2),BALL_DIAMETER,BALL_DIAMETER);


    }
    public void newPadel()
    {
        padel1 = new Padel(0, (GAME_HEIGHT/2) - (PADEL_HEIGHT/2), PADEL_WIDTH, PADEL_HEIGHT, 1);
        padel2 = new Padel(GAME_WIDTH - PADEL_WIDTH, (GAME_HEIGHT/2) - (PADEL_HEIGHT/2), PADEL_WIDTH, PADEL_HEIGHT, 2);



    }
    public void paint(Graphics g)
    {
        image=createImage(getWidth(),getHeight());
        graphics= image.getGraphics();
        draw(graphics);
        g.drawImage(image,0,0,this);

    }
    public void draw(Graphics g)
    {
        padel1.draw(g);
        padel2.draw(g);
        ball.draw(g);
        score.draw(g);


    }
    public void move()
    {
        padel1.move();
        padel2.move();
        ball.move();

    }
    public void checkcollision()
    {
//bounce ball off top and bottom edges
        if(ball.y<=0)
        {
            ball.setYDirection(-ball.yVelocity);
        }
        if(ball.y>=GAME_HEIGHT-BALL_DIAMETER)
        {
            ball.setYDirection(-ball.yVelocity);
        }//this bounces ball
        if(ball.intersects(padel1)){
            ball.xVelocity=Math.abs(ball.xVelocity);
            ball.xVelocity++;//for more difficulty
            if(ball.yVelocity>0)
                ball.yVelocity++;
            else
                ball.yVelocity--;
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }

        if(ball.intersects(padel2)){
            ball.xVelocity=Math.abs(ball.xVelocity);
            ball.xVelocity++;//for more difficulty
            if(ball.yVelocity>0)
                ball.yVelocity++;
            else
                ball.yVelocity--;
            ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }

//stop paddle at window edge
        if(padel1.y<=0)
            padel1.y=0;
        if(padel1.y>=(GAME_HEIGHT-PADEL_HEIGHT))
            padel1.y=GAME_HEIGHT-PADEL_HEIGHT;

        if(padel2.y<=0)
            padel2.y=0;
        if(padel2.y>=(GAME_HEIGHT-PADEL_HEIGHT))
            padel2.y=GAME_HEIGHT-PADEL_HEIGHT;
        //give a player 1 point and creates new padel and ball
        if(ball.x<=0)
        {
            score.player2++;
            newPadel();
            newBall();
            System.out.println("player2:"+score.player2);
        }

        if(ball.x>=GAME_WIDTH-BALL_DIAMETER)
        {
            score.player1++;
            newPadel();
            newBall();
            System.out.println("player1:"+score.player1);
        }

    }
    public void run()
    {
        long lastTime=System.nanoTime();
        double amountofticks=60.0;
        double ns=1000000000/amountofticks;
        double delta=0;
        while(true)
        {
            long now=System.nanoTime();
            delta +=(now-lastTime)/ns;
            lastTime=now;
            if(delta>=1)
            {
                move();
                checkcollision();
                repaint();
                delta--;

            }
        }
    }
    public class AL extends KeyAdapter{
        public void keyPressed(KeyEvent e) {
            padel1.keyPressed(e);
            padel2.keyPressed(e);


        }
        public void keyReleased(KeyEvent e) {
            padel1.keyReleased(e);
            padel2.keyReleased(e);

        }
    }

}
