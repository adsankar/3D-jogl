package softwareDesign;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Animated Dodge "Doge" Game
 * @author Aleksander Sankar and Sohum Dalal
 * Software Design Pd. 7
 * Mr. Fowler
 */
public class DogeGame2 extends JPanel implements Runnable{

	//

	//constant fields
	private static final int BOX_WIDTH = 800;
	private static final int SPEED = 5;
	private static final int BOX_HEIGHT = 800;
	private static final int FRAME_RATE = 60; 
	private static int score =0;
	private double uX = BOX_WIDTH/2;
	private double uY = BOX_HEIGHT/2;

	//private boolean skip = false;

	//list that stores all of the balls
	private CopyOnWriteArrayList<Ball> ballList = new CopyOnWriteArrayList<Ball>();
	private Thread animator;


	/**
	 * The main method creates the frame, initializes a ScreenSaver object, adds it to the frame and displays it.
	 * @param args not used
	 */
	public static void main(String[] args) {

		JFrame frame = new JFrame("Dodge Game");//set the title
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DogeGame2 d = new DogeGame2();
		frame.setContentPane(d);
		d.setFocusable(true);
		d.requestFocusInWindow();
		frame.pack();

		frame.setVisible(true);//show the screen saver

	}//end main

	/**
	 * The constructor for the Screensaver object.
	 * It adds a ball to the screen, sets the screen size and begins the thread.
	 */
	public DogeGame2() {
		addMouseListener(new MouseListener(){



			@Override
			public void mouseClicked(MouseEvent arg0) {
				// 
				ballList.add(randomBall(200, 20, 50));
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				//

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// 

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// 
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// 
			}

		});
		addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode()==KeyEvent.VK_UP || e.getKeyCode()==KeyEvent.VK_W){
					uY-=SPEED;
				}
				if (e.getKeyCode()==KeyEvent.VK_DOWN || e.getKeyCode()==KeyEvent.VK_S){
					uY+=SPEED;
				}
				if (e.getKeyCode()==KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_A){
					uX-=SPEED;
				}
				if (e.getKeyCode()==KeyEvent.VK_RIGHT || e.getKeyCode()==KeyEvent.VK_D ){
					uX+=SPEED;
				}

			}
		});
		ballList.add(randomBall(200, 20, 180));
		this.setPreferredSize(new Dimension(BOX_WIDTH, BOX_HEIGHT));
		animator = new Thread(this);
		animator.start();
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
				(float)(Math.random()*maxVelocity), (float)(Math.random()*maxVelocity),
				(int)(30+Math.random()*maxSize),
				new Color((int)(15+Math.random()*240),(int)(15+Math.random()*240),(int)(15+Math.random()*240),255));
	}//end randomBall

	/**
	 * Runs the animation and calls the collision and physics-calculating methods
	 */
	public void run() {
		while (!false) { 

			for (Ball z: ballList){//use the position and velocity to find the new position
				z.setXposition(z.getXposition()+z.getXvelocity());
				z.setYposition(z.getYposition()+z.getYvelocity());
			}//end for-each loop calculating positions
			for (Ball t: ballList){

				if ( t.getXposition() - t.getSize() < 0) {
					t.setXvelocity(-t.getXvelocity()); //reflect if ball hits a wall
					t.setXposition(t.getSize());
					collide();

				} else if ( t.getXposition() + t.getSize()> BOX_WIDTH) {
					t.setXvelocity(-t.getXvelocity());
					t.setXposition(BOX_WIDTH - t.getSize());
					collide();

				}//end if

				if ( t.getYposition() - t.getSize()< 0) {
					t.setYvelocity(-t.getYvelocity());
					t.setYposition(t.getSize());
					collide();


					//

				}//end if
				if ( t.getYposition() - t.getSize()> BOX_HEIGHT) {
					score++;
					ballList.remove(t);
					
					
				}
				


			}//
			repaint();
			try {
				Thread.sleep(1000 / FRAME_RATE);  
			} catch (InterruptedException ex) { }
		}//end try-catch
	}//end run


	/**
	 * Draws the picture on the screen.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.black);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.fillRect(0,0,BOX_WIDTH, BOX_HEIGHT);

		for (Ball t: ballList){//draw each ball in the list
			g2.setColor(t.getBallColor());

			g2.fillOval((int) (t.getXposition() - t.getSize()), (int) (t.getYposition() - t.getSize()),(int)(2*t.getSize()), (int)(2*t.getSize()));

		}//end for-each loop
		g2.setColor(Color.WHITE);//field for paddle color
		g2.fillOval((int)uX, (int)uY, 30, 30);
		g2.setColor(Color.white);
		Font f = new Font("Verdana", Font.PLAIN, 40);
		g2.setFont(f);
		String s = "Score: "+ score;
		g2.drawString(s,getWidth()/2-getFontMetrics(f).stringWidth(s)/2,getFontMetrics(f).getMaxAscent());
	}//end paintComponent


	/**
	 * Process for splitting and reducing size and velocity after the ball hits the wall.
	 */
	public void collide(){
		for (Ball t: ballList){
			if (t.getSize()<5){
				ballList.add(randomBall(200, 20, 20));



			}//end if
			//	t.setXvelocity(-t.getXvelocity());//set velocity again
			//t.setYvelocity(t.getYvelocity());

			//skip = true;
			break;
		}//end for-each loop
	}//end collide


}//end class