package softwareDesign;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.Timer;

/**
 * Animated Dodge "Doge" Game
 * @author Aleksander Sankar and Sohum Dalal
 * Software Design Pd. 7
 * Mr. Fowler
 */
public class DogeGame extends GameTemplate implements Runnable{

	//constant fields
	private static final int BOX_WIDTH = 800;
	private static final int U_SPEED = 8;
	private static final int BOX_HEIGHT = 800;
	private static final int BALL_DELAY_TIME= 700;
	private static final int SPEED_DELAY_TIME= 10000;
	private static Ball character;
	private static Font f;
	private static final String FONT_NAME = "Lucida Console";
	private static int score =0;
	private static int speedIncrement = 1;
	private static int ballSpeed =5;
	private boolean lost = false;
	private Timer ballTimer;
	private Timer speedTimer;
	

	//list that stores all of the balls
	private CopyOnWriteArrayList<Ball> ballList = new CopyOnWriteArrayList<Ball>();

	/**
	 * The main method creates the frame, initializes a DogeGame object, adds it to the frame and displays it.
	 * @param args not used
	 */
	public static void main(String[] args) {
		DogeGame d= new DogeGame();
		d.init();
		createGameFrame(d, BOX_WIDTH,BOX_HEIGHT);
		

	}//end main

	/**
	 * The constructor for the DogeGame object.
	 * It adds a ball to the screen, sets the screen size and begins the thread.
	 */
	public DogeGame() {
		super();
	}//end constructor

	/**
	 * Generates a random ball within the given parameters as limits to the size and velocity and position.
	 * It has a random position, velocity, size, and color.
	 * @param positionBound the maximum position that the ball can be placed
	 * @param maxVelocity the maximum velocity that the ball can initially have
	 * @param maxSize  the maximum size that the ball can have
	 * @return a new ball
	 */
	public Ball randomBall(int positionBound, int maxVelocity, int maxSize){
		return new Ball((float)(Math.random()*positionBound),
				(float)(Math.random()*positionBound),
				(float)(2*Math.random()*maxVelocity-maxVelocity), (float)(2+Math.random()*(maxVelocity-2)),
				(int)(30+Math.random()*maxSize),
				new Color((int)(15+Math.random()*240),(int)(15+Math.random()*240),(int)(15+Math.random()*240),255));
	}//end randomBall


	@Override
	public void init() {

		f = new Font(FONT_NAME, Font.PLAIN, 40);
		ballTimer = new Timer (BALL_DELAY_TIME, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ballList.add(randomBall(-BOX_WIDTH, ballSpeed, 30));

			}

		});
		speedTimer = new Timer (SPEED_DELAY_TIME, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ballSpeed+=speedIncrement;
			}

		});
		start();
	}

	@Override
	public void start() {
		ballTimer.stop();
		speedTimer.stop();
		character = new Ball(BOX_WIDTH/2, BOX_HEIGHT/2, 0, 0, 15, Color.white);
		score = 0;
		ballSpeed = 5;
		ballList.clear();

		ballTimer.start();
		speedTimer.start();
	}

	@Override

	public void updateFrame(Graphics2D g2) {
	

		g2.setColor(Color.black);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.fillRect(0,0,BOX_WIDTH, BOX_HEIGHT);

		for (Ball t: ballList){//draw each ball in the list
			g2.setColor(t.getBallColor());

			g2.fillOval((int) (t.getXposition() - t.getSize()), (int) (t.getYposition() - t.getSize()),(int)(2*t.getSize()), (int)(2*t.getSize()));

		}//end for-each loop
		g2.setColor(Color.WHITE);//field for paddle color
		g2.fillOval((int)(character.getXposition() - character.getSize()), (int)(character.getYposition()-character.getSize()), 2*character.getSize(), 2*character.getSize());
		g2.setColor(Color.white);
		
		g2.setFont(f);
		String s  = "";
		if (!lost){
			s = "Score: "+ score;
			g2.drawString(s,getWidth()/2-getFontMetrics(f).stringWidth(s)/2,getFontMetrics(f).getMaxAscent());
		}
		else {
			
			s= "Game over: "+score;
			g2.drawString(s,getWidth()/2-getFontMetrics(f).stringWidth(s)/2,getFontMetrics(f).getMaxAscent());
			String r = "Press R to Play Again";
			g2.drawString(r,getWidth()/2-getFontMetrics(f).stringWidth(r)/2,40+getFontMetrics(f).getMaxAscent());
		}

		if (!lost){
		for (Ball t: ballList){
		
			if (isCollision(t.getXposition(), t.getYposition(), t.getSize(), character.getXposition(), character.getYposition(),character.getSize())){
							
				lose();
		

			}//end if
			


			t.setXposition(t.getXposition()+t.getXvelocity());
			t.setYposition(t.getYposition()+t.getYvelocity());

			//left wall
			if ( t.getXposition() - t.getSize() < 0) {
				t.setXvelocity(-t.getXvelocity()); //reflect if ball hits a wall
				t.setXposition(t.getSize());

				//right wall
			} else if ( t.getXposition() + t.getSize()> BOX_WIDTH) {
				t.setXvelocity(-t.getXvelocity());
				t.setXposition(BOX_WIDTH - t.getSize());


			}//end if

			if ( t.getYposition() - t.getSize()> BOX_HEIGHT) {
				score++;
				ballList.remove(t);
			}

		}


		}
		if((isAKeyDown(KeyEvent.VK_UP) || isAKeyDown(KeyEvent.VK_W)) && character.getYposition()-character.getSize()>0){
			character.setYposition(character.getYposition()-U_SPEED);
			//uY-=U_SPEED;
		}
		if((isAKeyDown(KeyEvent.VK_DOWN) || isAKeyDown(KeyEvent.VK_S)) && character.getYposition()+character.getSize()<BOX_HEIGHT){
			character.setYposition(character.getYposition()+U_SPEED);
			//uY+=U_SPEED;
		}
		if((isAKeyDown(KeyEvent.VK_RIGHT) || isAKeyDown(KeyEvent.VK_D)) && character.getXposition()+character.getSize()<BOX_WIDTH){
			character.setXposition(character.getXposition()+U_SPEED);
			//uX+=U_SPEED;
		}
		if((isAKeyDown(KeyEvent.VK_LEFT) || isAKeyDown(KeyEvent.VK_A)) && character.getXposition()>0){
			character.setXposition(character.getXposition()-U_SPEED);
			//uX-=U_SPEED;
		}
		if (isAKeyDown(KeyEvent.VK_ESCAPE)){
			System.exit(1);
		}
		if (isAKeyDown(KeyEvent.VK_R)){
			start();
			lost =false;
		}


	}

	/**
	 * find if the distance between the centers of the circles is 
	 * greater or less than the sum of their radiis
	 * @param x1 x coordinate of ball 1 center
	 * @param y1 y coordinate of ball 1 center
	 * @param r1 radius of ball 1
	 * @param x2 x coordinate of ball 2 center
	 * @param y2 y coordinate of ball 2 center
	 * @param r2 radius of ball 2
	 * @return whether 2 Ball objects overlap or "collide"
	 */
	public boolean isCollision(float x1, float y1, float r1, float x2, float y2, float r2)	{

		float a = r1 + r2;
		float dx = x1 -x2;
		float dy = y1 - y2;
		return a * a > (dx * dx + dy * dy);

	}

	public void lose(){

		for (Ball b: ballList){
			b.setXvelocity(0);
			b.setYvelocity(0);
		}
		lost = true;

	}

}//end class