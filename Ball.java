package softwareDesign;

import java.awt.Color;

/**
 * Ball Class
 * @author Aleksander Sankar
 * Graphics Pd. 1
 * Mr. Fowler
 */
public class Ball{

	//fields
	private float xposition;
	private float yposition;
	private float xvelocity;
	private float yvelocity;
	private int size;
	private Color ballColor;

	/**
	 * Constructor that makes a new ball with the given parameters
	 * @param x the x position
	 * @param y the y position
	 * @param xv the x velocity
	 * @param yv the y velocity
	 * @param s the size of the ball
	 * @param c the color of the ball
	 */
	public Ball(float x, float y, float xv, float yv, int s, Color c){
		xposition = x;
		yposition = y;
		xvelocity = xv;
		yvelocity = yv;
		size = s;
		ballColor = c;

	}//end constructor

	/**
	 * Get the ball color
	 * @return the ball color
	 */
	public Color getBallColor() {
		return ballColor;
	}//end ballColor

	/**
	 * Set the ballc color
	 * @param ballColor
	 */
	public void setBallColor(Color ballColor) {
		this.ballColor = ballColor;
	}//end setBallColor

	/**
	 * Get the x position of the ball
	 * @return the x position
	 */
	public float getXposition() {
		return xposition;
	}//end getXposition

	/**
	 * Get the y position of the ball
	 * @return the y position
	 */
	public float getYposition() {
		return yposition;
	}//end getYposition

	/**
	 * Get the x velocity of the ball
	 * @return the x velocity
	 */
	public float getXvelocity() {
		return xvelocity;
	}//end getXvelocity

	/**
	 * Get the y velocity of the ball
	 * @return the y velocity
	 */
	public float getYvelocity() {
		return yvelocity;
	}//end getYvelocity

	/**
	 * Get the size of the ball
	 * @return the size
	 */
	public int getSize() {
		return size;
	}//end getSize

	/**
	 * Set the x position of the ball
	 * @param xposition
	 */
	public void setXposition(float xposition) {
		this.xposition = xposition;
	}//end setXposition

	/**
	 * Set the y position of the ball
	 * @param yposition
	 */
	public void setYposition(float yposition) {
		this.yposition = yposition;
	}//end setYposition

	/**
	 * Set the x velocity
	 * @param xvelocity
	 */
	public void setXvelocity(float xvelocity) {
		this.xvelocity = xvelocity;
	}//end setXvelocity

	/**
	 * Set the y velocity
	 * @param yvelocity
	 */
	public void setYvelocity(float yvelocity) {
		this.yvelocity = yvelocity;
	}//end setYvelocity

	/**
	 * Set the size (radius)
	 * @param size
	 */
	public void setSize(int size) {
		this.size = size;
	}//end setSize

}//end class