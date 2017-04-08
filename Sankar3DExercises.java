import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.j2d.TextRenderer;


/**
 * 3D Exercises
 * @author Aleksander Sankar
 * 3D Graphics Pd. 2
 * Mr. Fowler
 */
public class Sankar3DExercises extends GLCanvas{

	private float count =0;
	private boolean isRunning = true;
	private TextRenderer renderer;
	private float rotateAngle =0;
	private int rotAxis = 0;
	private float[] pos = {0,0,10};

	//6 vertices of cube (s=1)
	private double[][] cubePoints = {{1,1,1},{1,-1,1},{1,-1,-1},{1,1,-1},{-1, 1,-1},{-1,-1,-1},{-1, -1,1},{-1,1,1},};

	/**
	 * Necessary Constructor for GLCanvases
	 * Also adds the required mouse and key listeners
	 * @param glc the GLCapabilites object used
	 */
	public Sankar3DExercises(GLCapabilities glc){
		super(glc);

		//add a mouseWheelListener
		addMouseWheelListener(new MouseWheelListener(){

			@Override
			/**
			 * Zoom in/out by scrolling
			 */
			public void mouseWheelMoved(MouseWheelEvent e) {
				pos[2]+=.5*e.getWheelRotation();

			}//end mouseWheelMoved

		});//end MouseWheeListener

		//add GLEventListener
		addGLEventListener(new GLEventListener(){

			@Override
			public void display(GLAutoDrawable drawable) {

				doDisplay(drawable.getGL(), drawable.getWidth(),
						drawable.getHeight());
				//draw some text on screen
				renderer.beginRendering(drawable.getWidth(), drawable.getHeight());
				renderer.setColor(.5f, 0.2f, 0.7f, 0.8f);//RGBA colors
				renderer.draw("\"Donut eats a box\"", 0, 5);
				renderer.draw("Press space to pause and enable mouse controls", 0, 80);
			if (isRunning){
				renderer.draw("Camera location: ("+(int)(10*Math.sin(rotateAngle*Math.PI/180))+", "+(int)(10*Math.cos(rotateAngle*Math.PI/180))+", "+pos[2]+")",0,40);
			}
			else renderer.draw("Camera location: ("+pos[0]+", "+pos[1]+", "+pos[2]+")",0,40);
				renderer.endRendering();
			}//end display

			@Override
			/**not used*/
			public void displayChanged(GLAutoDrawable drawable,
					boolean modeChanged, boolean deviceChanged) {

			}//end displayChanged

			@Override
			/**
			 * Initialize the text renderer and call the doInit method
			 */
			public void init(GLAutoDrawable drawable) {
				renderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 36),true,true);
				doInit(drawable.getGL());

			}//end init

			@Override
			/**
			 * Call the doReshape method
			 */
			public void reshape(GLAutoDrawable drawable, int x, int y,
					int width, int height) {
				doReshape(drawable.getGL(), width, height);

			}//end reshape

		});//end GLEventListener

		//add a mouse listener
		this.addMouseListener(new MouseListener(){

			@Override
			/**
			 * Increment counter for mouse clicks
			 */
			public void mouseClicked(MouseEvent arg0) {
				count++;
			}

			@Override
			/**not used*/
			public void mouseEntered(MouseEvent arg0) {}

			@Override
			/**not used*/
			public void mouseExited(MouseEvent arg0) {}

			@Override
			/**not used*/
			public void mousePressed(MouseEvent arg0) {}

			@Override
			/**not used*/
			public void mouseReleased(MouseEvent arg0) {}

		});//end mouse listener

		//add a mouse motion listener
		this.addMouseMotionListener(new MouseMotionListener() {
			@Override
			/**
			 * Move camera position by dragging mouse
			 */
			public void mouseDragged(MouseEvent e) {
				pos[0]  =(float) (e.getX()-getWidth()/2)/50;	
				pos[1]  =(float) (e.getY()-getHeight()/2)/50;
				
			}
			@Override
			/**not used*/
			public void mouseMoved(MouseEvent e) {}
		});//end mouse listener

		//add a key adapter to handle key events
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {

				//quit if Q is pressed
				if (e.getKeyChar() == 'q') {
					System.err.print("Q PRESSED: QUITTING");
					System.exit(1);
				}

				//reset if control is pressed
				if (e.getKeyCode() == KeyEvent.VK_CONTROL) {// ctrl key
					reset();

				}
				
				//change the axis of rotation when R is pressed
				if (e.getKeyChar() == 'r') {
					rotAxis++;
					if (rotAxis >= 6) {
						rotAxis = 0;
					}
				}

				//zoom in 
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					pos[2]-=0.1;
				}
				//zoom out
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					pos[2]+=0.1;
				}
			
				//pause the animation when space is pressed, unpause after second space, also reset
				if (e.getKeyChar() == ' ') {
					isRunning = !isRunning;
					reset();
				}

				//exit when Esc is pressed
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {// esc key
					System.err.print("ESC PRESSED: QUITTING");
					System.exit(1);

				}
			}//end keyPressed
		});//end key adapter

	}//end constructor

	/**
	 * Draw a box of 6 sides (all quadrilaterals)
	 * @param myGL
	 */
	public void drawBox(GL myGL){
		myGL.glBegin(GL.GL_QUADS);
		for (int i=0; i<6; i++){
			drawSide(myGL, i);
		}//end for
	}//end drawBox

	/**
	 * Set the camera back to starting position
	 */
	public void reset(){
		pos[0] = 5;
		pos[1] =5;
		pos[2] = 10f;
		count =0;
	
	}//end reset

	/**
	 * Draw each side of the box
	 * @param myGL the GL used
	 * @param i the number of the side
	 */
	public void drawSide(GL myGL, int i){
		myGL.glBegin(GL.GL_QUADS);

		switch (i){
		case 0:{
			myGL.glColor3d(0, .9, .25);//change color
			myGL.glVertex3d(cubePoints[0][0],cubePoints[0][1],cubePoints[0][2]);
			myGL.glVertex3d(cubePoints[1][0],cubePoints[1][1],cubePoints[1][2]);
			myGL.glVertex3d(cubePoints[2][0],cubePoints[2][1],cubePoints[2][2]);
			myGL.glVertex3d(cubePoints[3][0],cubePoints[3][1],cubePoints[3][2]);
			myGL.glColor3d(.5, 0, 1);//change color
			break;
		}
		case 1:{

			myGL.glVertex3d(cubePoints[2][0],cubePoints[2][1],cubePoints[2][2]);
			myGL.glVertex3d(cubePoints[3][0],cubePoints[3][1],cubePoints[3][2]);
			myGL.glVertex3d(cubePoints[4][0],cubePoints[4][1],cubePoints[4][2]);
			myGL.glVertex3d(cubePoints[5][0],cubePoints[5][1],cubePoints[5][2]);
			myGL.glColor3d(.5,.25, .75);//change color
			break;
		}
		case 2:{

			myGL.glVertex3d(cubePoints[4][0],cubePoints[4][1],cubePoints[4][2]);
			myGL.glVertex3d(cubePoints[5][0],cubePoints[5][1],cubePoints[5][2]);
			myGL.glVertex3d(cubePoints[6][0],cubePoints[6][1],cubePoints[6][2]);
			myGL.glVertex3d(cubePoints[7][0],cubePoints[7][1],cubePoints[7][2]);
			myGL.glColor3d(1, .5, 1);//change color
			break;
		}
		case 3:{

			myGL.glVertex3d(cubePoints[7][0],cubePoints[7][1],cubePoints[7][2]);
			myGL.glVertex3d(cubePoints[0][0],cubePoints[0][1],cubePoints[0][2]);
			myGL.glVertex3d(cubePoints[1][0],cubePoints[1][1],cubePoints[1][2]);
			myGL.glVertex3d(cubePoints[6][0],cubePoints[6][1],cubePoints[6][2]);
			myGL.glColor3d(0, 1, .5);//change color
			break;
		}
		case 4:{

			myGL.glVertex3d(cubePoints[7][0],cubePoints[7][1],cubePoints[7][2]);
			myGL.glVertex3d(cubePoints[0][0],cubePoints[0][1],cubePoints[0][2]);
			myGL.glVertex3d(cubePoints[3][0],cubePoints[3][1],cubePoints[3][2]);
			myGL.glVertex3d(cubePoints[4][0],cubePoints[4][1],cubePoints[4][2]);
			myGL.glColor3d(0, .5, 0);//change color
			break;
		}
		case 5:{

			myGL.glVertex3d(cubePoints[1][0],cubePoints[1][1],cubePoints[1][2]);
			myGL.glVertex3d(cubePoints[2][0],cubePoints[2][1],cubePoints[2][2]);
			myGL.glVertex3d(cubePoints[5][0],cubePoints[5][1],cubePoints[5][2]);
			myGL.glVertex3d(cubePoints[6][0],cubePoints[6][1],cubePoints[6][2]);
			myGL.glColor3d(.5, .25, 1);//change color
			break;
		}

		}//end switch

		myGL.glEnd();
	}//end drawSide
	
	/**
	 * Method for setting up the 3D effects and depth view
	 * @param myGL
	 */
	public void doInit(GL myGL){
		myGL.glEnable( GL.GL_DEPTH_TEST );

		myGL.glDepthFunc(GL.GL_LEQUAL);
	}//end doInit


	/**
	 * Method for drawing shapes in the GLCanvas 
	 * @param myGL the GL being used
	 * @param w the width
	 * @param h the height
	 */
	public void doDisplay(GL myGL, int w, int h){
		GLU myglu = new GLU();
		GLUT glut = new GLUT();
		myGL.glShadeModel(GL.GL_SMOOTH);
		myGL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
		myGL.glMatrixMode(GL.GL_MODELVIEW);
		myGL.glLoadIdentity();
		if (!isRunning){
			myglu.gluLookAt(pos[2],pos[1],pos[0],0f,0f,0f,0f,1f,0f);
		}  
		else {myglu.gluLookAt(10*Math.sin(rotateAngle*Math.PI/180),10*Math.cos(rotateAngle*Math.PI/180),pos[2],0,0,0,0,1,0);
		
		}
		myGL.glColor3d(.4,0, .8);
		glut.glutWireTeapot(2, false);
		myGL.glColor3d(0,.5,1);
		glut.glutWireTorus(count/2%4, 1+count/2%4, 100, 100);
		
		if (isRunning){
			rotateAngle+=1;//represents degrees
			rotateAngle=rotateAngle%360;
			//change axis of rotation
			if (rotAxis == 0) {
				myGL.glRotatef(rotateAngle, 1, 0, 1);
			}

			if (rotAxis == 1) {
				myGL.glRotatef(rotateAngle, 0, 1, 0);
			}
			if (rotAxis == 2) {
				myGL.glRotatef(rotateAngle, 1, 0, 0);
			}
			if (rotAxis == 3) {
				myGL.glRotatef(rotateAngle, 1, 1, 0);
			}
			if (rotAxis == 4) {
				myGL.glRotatef(rotateAngle, 0, 0, 1);
			}
			if (rotAxis == 5) {
				myGL.glRotatef(rotateAngle, 1, 1, 1);
			}
		}//end if

		drawBox(myGL);
		myGL.glTranslated(1.74, 1.5, 1);
		myGL.glScaled(.5, .5, .9);

		myGL.glRotated(-rotateAngle,0,1,1);
		drawBox(myGL);

		myGL.glTranslated(-1.74, 2.5, 1.4);
		myGL.glScaled(.75, 1.5, .3);

		myGL.glRotated(rotateAngle+250,1,0,1);
		myGL.glScaled(count/10, 1, count/20);
		drawBox(myGL);
	
		
		myGL.glEnd();
	}//end doDisplay
	

	/**
	 * Called when the frame is reshaped
	 * @param myGL
	 * @param w
	 * @param h
	 */
	public void doReshape(GL myGL, int w, int h) {
		GLU myglu = new GLU();
		myGL.glMatrixMode(GL.GL_PROJECTION);
		myGL.glLoadIdentity();
		myglu.gluPerspective(60.0,1.0,1.0,15.0);
		myGL.glViewport(0, 0, w, h);
	}//end doReshape

}//end class