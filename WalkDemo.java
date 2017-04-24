package softwareDesign;

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

import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.j2d.TextRenderer;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;


//TODO camera position, mouse control, WASD, wheel for speed adjust, vertex arrays

public class WalkDemo extends GLCanvas{

	private float roomSize = 2.0f;
	private float[][] cubePoints = {{roomSize,roomSize,roomSize},
			{roomSize,-roomSize,roomSize},
			{roomSize,-roomSize,-roomSize},
			{roomSize,roomSize,-roomSize},
			{-roomSize, roomSize,-roomSize},
			{-roomSize,-roomSize,-roomSize},
			{-roomSize, -roomSize,roomSize},
			{-roomSize,roomSize,roomSize}};
	private final int steps =50;
	private int rotateX =0;
	private int rotateY = 0;
	private int moveX =0;
	private int moveY = 0;
	private double scaling =1;
	private float[] lightPosition  = {0f,0f,0f,1};
	private float[] diffuse = {0.8f, 0.8f,0.8f, 1};
	private float[] ambient = {0.2f,0.2f,0.2f,1};
	private float[] specular = {1,1,1,1};
	private float[] emission = {0.8f,0.8f,0,1};
	private float[] zeros = {0,0,0,1};
	private boolean lightsOn = true;
	private TextRenderer renderer;

	private Texture brickTexture;
	private Texture brickFeel;
	private Texture circuitTexture;
	private Texture plaidTexture;
	private Texture ballTexture;


	public static void main(String[] args) {
		new WalkDemoRunner();

	}

	public WalkDemo(GLCapabilities glc){
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
					moveY+=1;
				}

				//rotate y-axis
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					moveY-=1;
				}

				//rotate x-axis
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					moveX-=1;
				}

				//rotate x-axis
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					moveX+=1;
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
	}

	public void doDisplay(GL myGL, int w, int h){
		GLU myglu = new GLU();
		GLUT glut = new GLUT();
		myGL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
		myGL.glLoadIdentity();
		myglu.gluLookAt(0,0,-3,0f,0f,20f,0f,1f,0f);
		myGL.glRotated(90, 1, 0, 0);

		myGL.glRotated(rotateX,0,0,1);
		myGL.glRotated(rotateY,1,0,0);
		myGL.glTranslated(-moveX,0,0);
		myGL.glTranslated(0, -moveY, 0);

		//simulates zoom in/out
		myGL.glScaled(scaling, scaling, scaling);

		myGL.glColor3f(0, .5f, 0.5f);

		glut.glutSolidCube(.3f);

/*
		//TODO floor here
		myGL.glBegin(GL.GL_QUADS);
		myGL.glVertex3f(-50,50,0);
		myGL.glVertex3f(-50,-50,0);
		myGL.glVertex3f(50,-50,0);
		myGL.glVertex3f(50,50,0);
		myGL.glEnd();*/

		//set the position of the light
		myGL.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, new float[]{lightPosition[0],lightPosition[1],lightPosition[2], 1},0);
		myGL.glColorMaterial(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE);

		//simulates zoom in/out
		myGL.glScaled(scaling, scaling, scaling);

		//draw the enclosing room (box)
		drawBox(myGL);



		//start the text renderer, set its color and display text
		renderer.beginRendering(w, h);
		renderer.setColor(1f, 0.85f, 0.15f, 0.8f);//RGBA colors

		//draw text on screen
		renderer.draw("Texture Tutorial (press space for light switch)", 0, 10);
		renderer.draw("Light location: ("+lightPosition[0]+", "+lightPosition[1]+", "+lightPosition[2]+")",0,40);
		renderer.endRendering();
	}//end doDisplay

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

}
