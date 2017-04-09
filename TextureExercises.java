import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.j2d.TextRenderer;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;

/**
 * Texture Tutorial Program
 * @author Aleksander Sankar
 * 3D Graphics Pd. 2
 * Mr. Fowler
 */
public class TextureExercises extends GLCanvas{

	//6 vertices of cube
	private float roomSize = 2.0f;
	private float[][] cubePoints = {{roomSize,roomSize,roomSize},{roomSize,-roomSize,roomSize},{roomSize,-roomSize,-roomSize},{roomSize,roomSize,-roomSize},{-roomSize, roomSize,-roomSize},{-roomSize,-roomSize,-roomSize},{-roomSize, -roomSize,roomSize},{-roomSize,roomSize,roomSize}};

	private int rotateX =0;
	private int rotateY = 0;
	private double scaling =1;
	private float[] lightPosition  = {0f,0f,0f,1};
	private float[] diffuse = {0.8f, 0.8f,0.8f, 1};
	private float[] ambient = {0.2f,0.2f,0.2f,1};
	private float[] specular = {1,1,1,1};
	private float[] emission = {1,1,1,1};
	private float[] zeros = {0,0,0,1};
	private boolean lightsOn = true;
	private TextRenderer renderer;

	//texture fields
	private Texture brickTexture;
	private Texture brickFeel;
	private Texture circuitTexture;
	private Texture plaidTexture;
	private Texture ballTexture;


	/**
	 * Constructor which creates the GLCanvas and adds the required listeners to it.
	 * @param glc the GLCapabilities used
	 */
	public TextureExercises(GLCapabilities glc){
		super(glc);
		//add a mouseWheelListener
		addMouseWheelListener(new MouseWheelListener(){

			@Override
			/**
			 * Zoom in/out by scrolling
			 */
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation()>0){
					smaller();
				}
				else bigger();

			}//end mouseWheelMoved

		});//end MouseWheeListener

		//add GLEventListener
		addGLEventListener(new GLEventListener(){

			@Override
			public void display(GLAutoDrawable drawable) {

				doDisplay(drawable.getGL(), drawable.getWidth(),
						drawable.getHeight());

			}//end display

			@Override
			/**not used*/
			public void displayChanged(GLAutoDrawable drawable,
					boolean modeChanged, boolean deviceChanged) {

			}//end displayChanged

			@Override
			/**
			 * Call the doInit method
			 */
			public void init(GLAutoDrawable drawable) {

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

		//add a mouse motion listener
		this.addMouseMotionListener(new MouseMotionListener() {
			@Override
			/**not used*/
			public void mouseDragged(MouseEvent e) {}

			@Override
			/**
			 * Artificially move camera position by moving the mouse to initiate GL rotations
			 */
			public void mouseMoved(MouseEvent e) {
				rotateX=(e.getX()-getWidth()/2)/2;
				rotateY=(e.getY()-getHeight()/2)/2;
			}//end mouseMoved
		});//end mouse listener

		//add a key adapter to handle key events
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {

				//quit if Q is pressed
				if (e.getKeyChar() == 'q') {
					System.err.print("Q PRESSED: QUITTING");
					System.exit(1);
				}

				//rotate y-axis
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					rotateY+=1;
				}

				//rotate y-axis
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					rotateY-=1;
				}

				//rotate x-axis
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					rotateX-=1;
				}

				//rotate x-axis
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					rotateX+=1;
				}

				//'Light Switch' toggle which turns the light on or off
				if (e.getKeyChar() == ' ') {
					lightsOn = !lightsOn;
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
	 * zoom in by increasing shape scale
	 */
	public void bigger() {
		scaling += .1;
	}//end bigger

	/**
	 * zoom out by decreasing shape scale
	 */
	public void smaller() {
		scaling -= .1;
		if (scaling < .01) {//don't get too small
			scaling = 0;
		}
	}//end smaller

	/**
	 * Method for setting up the 3D effects and lighting
	 * @param myGL the GL used
	 */
	public void doInit(GL myGL){
		loadTextures(myGL);
		myGL.glEnable(GL.GL_DEPTH_TEST);
		myGL.glEnable(GL.GL_NORMALIZE);
		myGL.glEnable(GL.GL_LIGHTING);//enable lighting
		myGL.glEnable (GL.GL_BLEND); 
		myGL.glEnable (GL.GL_POLYGON_SMOOTH); 
		myGL.glHint(GL.GL_POLYGON_SMOOTH_HINT, GL.GL_NICEST); 
		myGL.glShadeModel(GL.GL_SMOOTH);
		myGL.glDisable(GL.GL_COLOR_MATERIAL);
		myGL.glEnable(GL.GL_LIGHT0);

		//light gets darker when you move away
		myGL.glLightf(GL.GL_LIGHT0, GL.GL_CONSTANT_ATTENUATION, 2f);
		myGL.glLightf(GL.GL_LIGHT0, GL.GL_LINEAR_ATTENUATION, 0);

		//initialize the textRenderer so that text appears on screen
		renderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 30),true,true);
		myGL.glClearColor(0f,0f,0f,1f);//so that not all of the shapes have this color

		//move the position of the light to where the lamp will be placed
		lightPosition[0] =  (.75f*roomSize);
		lightPosition[1] = (.75f*roomSize);
		lightPosition[2] = (roomSize/2-0.05f);

		//the light bulb emits light
		myGL.glLightfv(GL.GL_LIGHT0, GL.GL_EMISSION, emission,0 );

		//control initial lighting attributes
		myGL.glLightfv(GL.GL_LIGHT0,GL.GL_AMBIENT,new float[] {0.1f,0.1f,0.1f,1f},0);
		myGL.glLightfv(GL.GL_LIGHT0,GL.GL_DIFFUSE,new float[] {0.9f,0.9f,0.9f,1f},0);
		myGL.glLightfv(GL.GL_LIGHT0,GL.GL_SPECULAR,new float[] {0.5f,0.5f,0.5f,1f},0);

	}//end doInit

	/**
	 * Method for drawing shapes in the GLCanvas 
	 * @param myGL the GL being used
	 * @param w the width
	 * @param h the height
	 */
	public void doDisplay(GL myGL, int w, int h){
		//light switch function to turn light on or off
		if (lightsOn){
			myGL.glEnable(GL.GL_LIGHT0);	
		}
		else myGL.glDisable(GL.GL_LIGHT0);

		GLU myGLU = new GLU();
		myGL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
		myGL.glLoadIdentity();

		//where the camera is looking at and where the camera is looking from
		myGLU.gluLookAt(0,0,-3,0f,0f,20f,0f,1f,0f);
		myGL.glRotated(90, 1, 0, 0);
		
		//handle rotation from key/mouse listeners
		myGL.glRotated(rotateX,0,0,1);
		myGL.glRotated(rotateY,1,0,0);
		
		//enable and bind the texture for the sphere
		ballTexture.enable();
		ballTexture.bind();
		GLUquadric globe = myGLU.gluNewQuadric();//create the sphere
		myGLU.gluQuadricTexture(globe, true);//apply quadric texture to it
		myGLU.gluSphere(globe, .3, 20, 20);//set dimensions of the sphere
		ballTexture.disable();

		//set the position of the light
		myGL.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, new float[]{lightPosition[0],lightPosition[1],lightPosition[2], 1},0);
		myGL.glColorMaterial(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE);

		//simulates zoom in/out
		myGL.glScaled(scaling, scaling, scaling);

		//draw the enclosing room (box)
		drawBox(myGL);
		//draw the lamp
		drawLamp(myGL);

		//start the text renderer, set its color and display text
		renderer.beginRendering(w, h);
		renderer.setColor(1f, 0.85f, 0.15f, 0.8f);//RGBA colors
		
		//draw text on screen
		renderer.draw("Texture Tutorial (press space for light switch)", 0, 10);
		renderer.draw("Light location: ("+lightPosition[0]+", "+lightPosition[1]+", "+lightPosition[2]+")",0,40);
		renderer.endRendering();
	}//end doDisplay

	/**
	 * Draw the lamp and its stand using GLUT objects
	 * @param myGL the GL used
	 */
	public void drawLamp(GL myGL){
		GLUT glut = new GLUT();//initialize a GLUT object	

		//light stand
		myGL.glMaterialfv(GL.GL_FRONT_AND_BACK,GL.GL_AMBIENT,ambient ,0);
		myGL.glMaterialfv(GL.GL_FRONT_AND_BACK,GL.GL_DIFFUSE,diffuse,0);
		myGL.glMaterialfv(GL.GL_FRONT_AND_BACK,GL.GL_SPECULAR,specular,0);
		myGL.glMaterialf(GL.GL_FRONT_AND_BACK,GL.GL_SHININESS,128);
		myGL.glMaterialfv(GL.GL_FRONT_AND_BACK,GL.GL_EMISSION,zeros,0);
		myGL.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, new float[]{.8f,0.8f,0.8f},0);


		myGL.glTranslated(.75*roomSize, .75*roomSize, roomSize/2);//move
		glut.glutSolidCylinder(.05, roomSize/2, 20, 50);//draw

		//bulb
		myGL.glTranslated(0, 0, -.05);//move
		myGL.glMaterialfv(GL.GL_FRONT_AND_BACK,GL.GL_AMBIENT,specular,0);
		myGL.glMaterialfv(GL.GL_FRONT_AND_BACK,GL.GL_DIFFUSE,specular,0);
		myGL.glMaterialfv(GL.GL_FRONT_AND_BACK,GL.GL_SPECULAR,zeros,0);
		myGL.glMaterialf(GL.GL_FRONT_AND_BACK,GL.GL_SHININESS,0f);
		if (lightsOn){
			myGL.glMaterialfv(GL.GL_FRONT_AND_BACK,GL.GL_EMISSION,emission,0);
		}
		else {
			myGL.glMaterialfv(GL.GL_FRONT_AND_BACK,GL.GL_EMISSION,zeros,0);
		}
		glut.glutSolidSphere(.09,50,50);//draw

		myGL.glEnd();
	}//end drawLamp

	/**
	 * Draws the walls (box) and specifies normals and colors
	 * @param myGL the GL used
	 */
	public void drawBox(GL myGL){
		//enable an bind the brick texture
		brickTexture.enable();
		brickTexture.bind();

		//set the properties of the wall's colors
		myGL.glMaterialfv(GL.GL_FRONT_AND_BACK,GL.GL_AMBIENT,zeros,0);
		myGL.glMaterialfv(GL.GL_FRONT_AND_BACK,GL.GL_SPECULAR,new float[]{0.3f, 0.3f,0.3f,1},0);
		myGL.glMaterialf(GL.GL_FRONT_AND_BACK,GL.GL_SHININESS,0f);
		myGL.glMaterialfv(GL.GL_FRONT_AND_BACK,GL.GL_EMISSION,zeros,0);

		myGL.glNormal3d(-1, 0, 0);
		myGL.glBegin(GL.GL_QUADS);
		myGL.glTexCoord2d(0,0);//anchor texture to bottom left side of quad
		myGL.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, new float[]{1,1,1,1},0);
		myGL.glVertex3d(cubePoints[0][0],cubePoints[0][1],cubePoints[0][2]);
		myGL.glTexCoord2d(1,0);//anchor texture to bottom right side of quad
		myGL.glVertex3d(cubePoints[1][0],cubePoints[1][1],cubePoints[1][2]);
		myGL.glTexCoord2d(1,1);//anchor texture to top right side of quad
		myGL.glVertex3d(cubePoints[2][0],cubePoints[2][1],cubePoints[2][2]);
		myGL.glTexCoord2d(0,1);//anchor texture to top left side of quad
		myGL.glVertex3d(cubePoints[3][0],cubePoints[3][1],cubePoints[3][2]);
		myGL.glEnd();

		//enable an bind the brick texture (
		brickFeel.enable();
		brickFeel.bind();
		
		myGL.glNormal3d(0, 0, 1);
		myGL.glBegin(GL.GL_QUADS);
		myGL.glTexCoord2d(0,0);
		myGL.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, new float[]{.23f,1f,.1f,1},0);//change color
		myGL.glVertex3d(cubePoints[2][0],cubePoints[2][1],cubePoints[2][2]);
		myGL.glTexCoord2d(1,0);
		myGL.glVertex3d(cubePoints[3][0],cubePoints[3][1],cubePoints[3][2]);
		myGL.glTexCoord2d(1,1); 
		myGL.glVertex3d(cubePoints[4][0],cubePoints[4][1],cubePoints[4][2]);
		myGL.glTexCoord2d(0,1);
		myGL.glVertex3d(cubePoints[5][0],cubePoints[5][1],cubePoints[5][2]);
		myGL.glEnd();


		//enable and bind plaid texture
		plaidTexture.enable();
		plaidTexture.bind();


		myGL.glNormal3d(1, 0, 0);
		myGL.glBegin(GL.GL_QUADS);
		myGL.glTexCoord2d(0,0);
		myGL.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, new float[]{.9f,.9f,.9f,1},0);//change color
		myGL.glVertex3d(cubePoints[4][0],cubePoints[4][1],cubePoints[4][2]);
		myGL.glTexCoord2d(1,0);
		myGL.glVertex3d(cubePoints[5][0],cubePoints[5][1],cubePoints[5][2]);
		myGL.glTexCoord2d(1,1);
		myGL.glVertex3d(cubePoints[6][0],cubePoints[6][1],cubePoints[6][2]);
		myGL.glTexCoord2d(0,1);
		myGL.glVertex3d(cubePoints[7][0],cubePoints[7][1],cubePoints[7][2]);
		myGL.glEnd();



		//enable and bind circuit texture
		circuitTexture.enable();
		circuitTexture.bind();
		//set texture parameters so that it repeats 
		myGL.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
		myGL.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);

		myGL.glNormal3d(0, 0, -1);
		myGL.glBegin(GL.GL_QUADS);
		myGL.glTexCoord2d(0,0);
		myGL.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE,new float[]{1,1,1,1},0);//change color
		myGL.glVertex3d(cubePoints[7][0],cubePoints[7][1],cubePoints[7][2]);
		myGL.glTexCoord2d(2,0);//repeat texture twice
		myGL.glVertex3d(cubePoints[0][0],cubePoints[0][1],cubePoints[0][2]);
		myGL.glTexCoord2d(2,2);//repeat texture twice
		myGL.glVertex3d(cubePoints[1][0],cubePoints[1][1],cubePoints[1][2]);
		myGL.glTexCoord2d(0,2);//repeat texture twice
		myGL.glVertex3d(cubePoints[6][0],cubePoints[6][1],cubePoints[6][2]);
		myGL.glEnd();

		myGL.glNormal3d(0, -1, 0);
		myGL.glBegin(GL.GL_QUADS);
		myGL.glTexCoord2d(0,0);
		myGL.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE,new float[]{1,1,1,1},0);//change color
		myGL.glVertex3d(cubePoints[7][0],cubePoints[7][1],cubePoints[7][2]);
		myGL.glTexCoord2d(1,0);
		myGL.glVertex3d(cubePoints[0][0],cubePoints[0][1],cubePoints[0][2]);
		myGL.glTexCoord2d(1,1);
		myGL.glVertex3d(cubePoints[3][0],cubePoints[3][1],cubePoints[3][2]);
		myGL.glTexCoord2d(0,1);
		myGL.glVertex3d(cubePoints[4][0],cubePoints[4][1],cubePoints[4][2]);
		myGL.glEnd();

		myGL.glNormal3d(0, 1, 0);
		myGL.glBegin(GL.GL_QUADS);
		myGL.glTexCoord2d(0,0);
		myGL.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE,new float[]{1f,0f,0.8f,1},0);//change color
		myGL.glVertex3d(cubePoints[1][0],cubePoints[1][1],cubePoints[1][2]);
		myGL.glTexCoord2d(1,0);
		myGL.glVertex3d(cubePoints[2][0],cubePoints[2][1],cubePoints[2][2]);
		myGL.glTexCoord2d(1,1);
		myGL.glVertex3d(cubePoints[5][0],cubePoints[5][1],cubePoints[5][2]);
		myGL.glTexCoord2d(0,1);
		myGL.glVertex3d(cubePoints[6][0],cubePoints[6][1],cubePoints[6][2]);
		myGL.glEnd();
		circuitTexture.disable();//disable the texture since we are not using it anymore
	}//end drawBox


	/**
	 * Called when the frame is reshaped
	 * @param myGL the GL used
	 * @param w the width
	 * @param h the height
	 */
	public void doReshape(GL myGL, int w, int h) {
		GLU myglu = new GLU();
		myGL.glMatrixMode(GL.GL_PROJECTION);
		myGL.glLoadIdentity();
		myglu.gluPerspective(60.0,1.0,2,10);
		myGL.glMatrixMode(GL.GL_MODELVIEW);
		myGL.glLoadIdentity();
		myGL.glViewport(0, 0, w, h);
	}//end doReshape

	public void loadTextures(GL myGL){
		myGL.glTexParameterf(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR );

		try{
			//load a TextureData object from a picture and then create a texture from it
			//this makes the texture luminance-only
			TextureData td = TextureIO.newTextureData(new File("brickTexture.jpg"), GL.GL_LUMINANCE, GL.GL_RGB,false, null);
			brickFeel = TextureIO.newTexture(td);
			//load textures from images
			brickTexture = TextureIO.newTexture(new File("brickTexture.jpg"),true);
			circuitTexture = TextureIO.newTexture(new File("circuit.jpg"),true);
			plaidTexture =TextureIO.newTexture(new File("plaid.jpg"),true);
			ballTexture =TextureIO.newTexture(new File("earthmap1k.jpg"),true);
		}//end try
		catch(IOException e){
			System.out.println("File not found!");
			System.exit(1);
		}//end catch
		catch(GLException f){
			System.exit(1);
		}//end catch

	}//end loadTextures
}//end class