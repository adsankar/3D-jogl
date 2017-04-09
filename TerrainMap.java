import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import com.sun.opengl.util.BufferUtil;
import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.j2d.TextRenderer;

/**
 * Terrain Map Project
 * @author Aleksander Sankar and Sohum Dalal
 * 3D Graphics Pd. 2
 * Mr. Fowler
 */
public class TerrainMap extends GLCanvas{

	private final double MAX_HEIGHT = 40;
	private final int SIZE = 128;
	private final double MOVE_SPEED = 5;
	private TextRenderer renderer;
	private double moveX =0;
	private double moveY =0;
	private int lightAngle=0;
	private boolean wire = false;
	private boolean alternate = false;
	private int rotateX = 0;
	private int rotateY = 0;
	private double scaling =1;
	private double[][] height = new double[SIZE][SIZE];
	private float[] nextNormals = new float[3];
	private float[] lightPosition  = {4f,4f,4f,1};
	private boolean lightsOn = true;
	private float[] diffuse = {0.8f, 0.8f,0.8f, 1};
	private float[] ambient = {0.2f,0.2f,0.2f,1};
	private float[] specular = {1,1,1,1};
	private float[] emission = {0,0,0f, 0};
	private float[]  fogColor= {0,0,0, 1};      // Fog Colors
	private FloatBuffer vert;
	private FloatBuffer colors;
	private FloatBuffer norms;

	/**
	 * Instantiate an TextureRunner object
	 * @param args not used
	 */
	public static void main(String[] args){
		new TerrainMapRunner();
	}//end main
	
	//TODO extending out, sun

	/**
	 * Constructor which creates the GLCanvas and adds the required listeners to it.
	 * @param glc the GLCapabilities used
	 */
	public TerrainMap(GLCapabilities glc){
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
				//rotateX=(e.getX()-getWidth()/2)/2;
				//rotateY=(e.getY()-getHeight()/2)/2;
			}//end mouseMoved
		});//end mouse listener

		//add a key adapter to handle key events
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {

				//toggle wireframe and solid views
				if (e.getKeyChar() == 'l') {
					wire= !wire;
					setupArrays();
				}// end if

				//redraw map if m is pressed
				if (e.getKeyChar() == 'm') {

					doInit(getGL());
				}// end if

				//quit if Q is pressed
				if (e.getKeyChar() == 'q') {
					System.err.print("Q PRESSED: QUITTING");
					System.exit(1);
				}// end if

				//switch to alternate controls is control is pressed
				if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
					alternate = !alternate;

				}// end if

				//rotate y-axis or move
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					if (alternate){
						rotateY+=1;
					}// end if
					else moveY+=MOVE_SPEED;
				}// end if

				//rotate y-axis or move
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					if (alternate){
						rotateY-=1;
					}// end if
					else moveY-=MOVE_SPEED;
				}// end if

				//rotate x-axis or move
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					if (alternate){
						rotateX-=1;
					}// end if
					else moveX+=MOVE_SPEED;
				}// end if

				//rotate x-axis or move
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					if (alternate){
						rotateX+=1;
					}// end if
					else moveX-=MOVE_SPEED;
				}// end if

				//'Light Switch' toggle which turns the light on or off
				if (e.getKeyChar() == ' ') {
					lightsOn = !lightsOn;
				}// end if

				//exit when Esc is pressed
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {// esc key
					System.err.print("ESC PRESSED: QUITTING");
					System.exit(1);
				}// end if

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
	 * Method for drawing shapes, including the map, in the GLCanvas 
	 * @param myGL the GL being used
	 * @param w the width
	 * @param h the height
	 */
	public void doDisplay(GL myGL, int w, int h){
		//enable the light
		myGL.glEnable(GL.GL_LIGHT0);
		myGL.glLightfv(GL.GL_LIGHT0,GL.GL_AMBIENT,ambient,0);
		myGL.glLightfv(GL.GL_LIGHT0,GL.GL_DIFFUSE, diffuse,0);
		myGL.glLightfv(GL.GL_LIGHT0,GL.GL_SPECULAR,specular,0);
		myGL.glLoadIdentity();
		GLU myGLU = new GLU();
		GLUT glut = new GLUT();
		myGL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );

		//fog effects
		myGL.glEnable(GL.GL_FOG);
		myGL.glFogi (GL.GL_FOG_MODE, GL.GL_EXP);
		myGL.glFogf (GL.GL_FOG_DENSITY, .2f);
		myGL.glFogfv(GL.GL_FOG_COLOR, fogColor, 0);
		myGL.glHint(GL.GL_FOG_HINT, GL.GL_NICEST);


		//where the camera is looking at and where the camera is looking from
		myGLU.gluLookAt(0,0,-4,0f,0f,20f,0f,1f,0f);
		myGL.glTranslated(-2,-.75,0);
		myGL.glRotated(45,1,0,0);
		myGL.glScaled(0.03, 0.03, 0.03);


		//handle rotation from key/mouse listeners
		myGL.glRotated(rotateX,0,0,1);
		myGL.glRotated(rotateY,1,0,0);
		myGL.glTranslated(-moveX,0,0);
		myGL.glTranslated(0, -moveY, 0);

		//simulates zoom in/out
		myGL.glScaled(scaling, scaling, scaling);

		//draw the map
		myGL.glPushMatrix();
		drawMap(myGL);
		myGL.glPopMatrix();


		//TODO here
		//set the position and properties of the light
		myGL.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, new float[]{lightPosition[0],lightPosition[1],lightPosition[2], 1},0);
	//	myGL.glColorMaterial(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT);

		myGL.glTranslated(75, 30, -25);
		myGL.glTranslated(lightPosition[0], lightPosition[1], lightPosition[2]);
		//myGL.glMaterialfv(GL.GL_FRONT_AND_BACK,GL.GL_AMBIENT,ambient,0);
		//myGL.glMaterialfv(GL.GL_FRONT_AND_BACK,GL.GL_DIFFUSE,diffuse,0);
		myGL.glMaterialf(GL.GL_FRONT_AND_BACK,GL.GL_SHININESS,0f);
		if (lightsOn){
			myGL.glMaterialfv(GL.GL_FRONT,GL.GL_EMISSION,emission,0);
			myGL.glLightf(GL.GL_LIGHT0, GL.GL_DIFFUSE, 0);
		}// end if
		glut.glutSolidSphere(7,50,50);
		newPosition(myGL, glut);
		myGL.glEnd();

		//start the text renderer, set its color and display text
		renderer.setColor(1,1,1,.7f);//RGBA colors
		renderer.beginRendering(w, h);
		//display some basic information
		renderer.draw("Terrain Map (press \"L\" to switch to mesh)", 0, 40);
		renderer.draw("Use arrow keys to move, press \'m\' to generate a new map",0,10);
		myGL.glClearColor(0f,0f,0f,1f);//so that not all of the shapes have this color
		renderer.endRendering();
	}// end doDisplay

	/**
	 * Calculate the new position of the sun though incremental translations
	 * @param myGL the GL being used
	 * @param glut the glut operated on
	 */
	public void newPosition(GL myGL, GLUT glut){
		myGL.glPushMatrix();

		//movement in a circle
		lightPosition[0] = (float) Math.cos(lightAngle*Math.PI/180)*15;
		lightPosition[1] = (float) Math.sin(lightAngle*Math.PI/180)*15;
		lightPosition[2] = -25;//constant height
		if (lightAngle>360){//don't go over 360 degrees
			lightAngle =0;
		}// end if
		else lightAngle++;
		myGL.glPopMatrix();
	}// end newPosition

	/**
	 * Method for setting up the 3D effects, lighting, vertex arrays
	 * @param myGL the GL used
	 */
	public void doInit(GL myGL){
		//enable vertex arrays
		myGL.glEnableClientState(GL.GL_COLOR_ARRAY);
		myGL.glEnableClientState(GL.GL_VERTEX_ARRAY);
		myGL.glEnableClientState(GL.GL_NORMAL_ARRAY);
		myGL.glEnable(GL.GL_DEPTH_TEST);
		myGL.glEnable(GL.GL_NORMALIZE);
		myGL.glEnable (GL.GL_BLEND); 
		myGL.glEnable(GL.GL_LIGHTING);
		myGL.glEnable (GL.GL_POLYGON_SMOOTH); 
		myGL.glHint(GL.GL_POLYGON_SMOOTH_HINT, GL.GL_NICEST); 
		myGL.glShadeModel(GL.GL_SMOOTH);
		myGL.glEnable(GL.GL_COLOR_MATERIAL);
		myGL.glClearColor(0f,0f,0f,0f);

		//initialize the textRenderer so that text appears on screen
		renderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 30),true,true);
		myGL.glClearColor(0f,0f,0f,1f);//so that not all of the shapes have this color
		height = makeHeightMap((int)(Math.log(SIZE)/Math.log(2)), MAX_HEIGHT*Math.random(), MAX_HEIGHT*Math.random(), MAX_HEIGHT*Math.random(), MAX_HEIGHT*Math.random(), MAX_HEIGHT);
		setupArrays();
		//shmap(maxHeight,maxHeight*Math.random(),maxHeight*Math.random(),maxHeight*Math.random(),maxHeight*Math.random());

	}// end doInit

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

	/**
	 * Add the vertices of the appropriate shapes to the vertex, color and normal arrays.
	 */
	public void setupArrays(){
		//set up the float buffers for each one
		vert = BufferUtil.newFloatBuffer(12*(height[0].length-1)*(height.length-1));
		colors = BufferUtil.newFloatBuffer(12*(height[0].length-1)*(height.length-1));
		norms = BufferUtil.newFloatBuffer(12*(height[0].length-1)*(height.length-1));
		if (wire){//procedure for the wireframe version

			for(int i = 0 ; i < height.length-1 ; i++){
				for(int j = 0 ; j < height[0].length-1 ; j++){
					vert.put(i);
					vert.put(j);
					vert.put((float) height[i][j]);//add vertices
					getColor(height[i][j]);//change the color based on altitude

					vert.put(i+1);
					vert.put(j+1);
					vert.put((float) height[i+1][j+1]);
					getColor(height[i+1][j+1]);

					vert.put(i+1);
					vert.put(j);
					vert.put((float) height[i+1][j]);
					getColor(height[i+1][j]);

					vert.put(i);
					vert.put(j+1);
					vert.put((float) height[i][j+1]);
					getColor(height[i][j+1]);
				}//end for
			}//end for

		}// end if
		else{//for the solid view

			for(int i = 0 ; i < height.length-1 ; i++){
				for(int j = 0 ; j < height[0].length-1 ; j++){

					//almost the same procedure as before
					vert.put(i);
					vert.put(j);
					vert.put((float) height[i][j]);
					getColor(height[i][j]);

					vert.put(i+1);
					vert.put(j);
					vert.put((float) height[i+1][j]);
					getColor(height[i+1][j]);

					vert.put(i+1);
					vert.put(j+1);
					vert.put((float) height[i+1][j+1]);
					getColor(height[i+1][j+1]);

					//calculate surface normals and add them to the normals array
					calculateNormal(i, j, height[i][j], i+1, j, height[i+1][j], i+1, j+1, height[i+1][j+1]);
					norms.put(nextNormals[0]);
					norms.put(nextNormals[1]);
					norms.put(nextNormals[2]);

					vert.put(i);
					vert.put(j+1);
					vert.put((float) height[i][j+1]);
					getColor(height[i][j+1]);

				}//end for
			}//end for
		}//end if

		vert.rewind();
		colors.rewind();
		norms.rewind();
	}// end setupArrays

	/**
	 * Calculate the surface normal given two lines, defined by a total of 9 points 
	 * @param x1 the first x coordinate
	 * @param y1 the first y coordinate
	 * @param z1 the first z coordinate
	 * @param x2 the second x coordinate
	 * @param y2 the second y coordinate
	 * @param z2 the second z coordinate
	 * @param x3 the third x coordinate
	 * @param y3 the third y coordinate
	 * @param z3 the third z coordinate
	 */
	public void calculateNormal(double x1, double y1, double z1,
			double x2, double y2, double z2, double x3, double y3, double z3){
		//cross product calculation
		nextNormals[0] = (float) ((y2-y1)*(z3-z1)-(z2-z1)*(y3-y1));
		nextNormals[1] = (float) ((z2-z1)*(x3-x1)-(x2-x1)*(z3-z1)); 
		nextNormals[2] = (float) ((x2-x1)*(y3-y1)-(y2-y1)*(x3-x1));

	}//end calculateNormal

	/**
	 * Set the color so that it corresponds to the height
	 * @param height the height for which the color is calculated for
	 */
	public void getColor(double height){
		if(height < 0){//highest "snow"
			colors.put(1f);
			colors.put(1f);
			colors.put(1f);
		}else if( height < 30){//in the middle, green "forest"
			colors.put(.0f);
			colors.put(.5f);
			colors.put(.0f);
		}else{//lowest "body of water"
			colors.put(0f);
			colors.put(0.3f);
			colors.put(1f);
		}// end else
	}//end getColor

	/**
	 * Draw the terrain map using vertex arrays
	 * @param myGL the GL being used
	 */
	public void drawMap(GL myGL){
		//enable the arrays
		myGL.glEnableClientState(GL.GL_COLOR_ARRAY);
		myGL.glEnableClientState(GL.GL_VERTEX_ARRAY);
		myGL.glEnableClientState(GL.GL_NORMAL_ARRAY);

		//designate the pointers
		myGL.glNormalPointer(GL.GL_FLOAT, 0, norms);
		myGL.glColorPointer(3, GL.GL_FLOAT, 0, colors);
		myGL.glVertexPointer(3, GL.GL_FLOAT, 0, vert);
		if (wire){
			myGL.glDrawArrays(GL.GL_LINES,0,vert.capacity()/3);
		}// end if
		else myGL.glDrawArrays(GL.GL_QUADS,0,vert.capacity()/3);

		//disable arrays when done
		myGL.glDisableClientState(GL.GL_VERTEX_ARRAY);
		myGL.glDisableClientState(GL.GL_COLOR_ARRAY);
		myGL.glDisableClientState(GL.GL_NORMAL_ARRAY);
	}//end drawMap


	/**
	 * A random value spikey map, mostly for testing
	 * @param seed1 the first corner seed value
	 * @param seed2 the second corner seed value
	 * @param seed3 the third corner seed value
	 * @param seed4 the fourth corner seed value
	 */
	public void randomMap(double seed1, double seed2, double seed3, double seed4){
		height = new double[SIZE][SIZE];
		for (int i=0; i<SIZE; i++){
			for (int j=0; j<SIZE; j++){
				height[i][j] = MAX_HEIGHT*Math.random();
			}//end for
		}//end for

		//"plant" the seeds at the corners of the map
		height[0][0] =seed1;
		height[0][SIZE-1] =seed2;
		height[SIZE-1][0] =seed3;
		height[SIZE-1][SIZE-1] =seed4;
	}//end randomMap

	/**
	 * The first of the square step parts of the diamond-square algorithm for generating the terrain
	 * @param min the minimum value
	 * @param map the map being used
	 * @param size the size of the map
	 * @param max the maximum height
	 * @param variation the maximum variation allowed
	 */
	public static void firstSquareStep(int min, double[][] map, int size, int max, double variation) {
		for (int x = min; x < map.length; x += size) {
			for (int y = 0; y < map.length; y += size) {
				if (y == max) {
					map[x][y] = map[x][0];
					continue;
				}// end if

				int left = x - min;
				int right = x + min;
				int down = y + min;
				int up = 0;

				if (y == 0) {
					up = max - min;
				} else {
					up = y - min;
				}//end else

				// the four corner values
				double val1 = map[left][y]; // left
				double val2 = map[x][up];   // up
				double val3 = map[right][y];// right
				double val4 = map[x][down]; // down

				midpointOffset(val1, val2, val3, val4, variation,
						map, x, y);
			}//end for
		}//end for
	}//end 

	/**
	 * The diamond part of the diamond-square algorithm for generating the terrain
	 * @param min the minimum value
	 * @param size the size of the map used
	 * @param map the map used
	 * @param variation the maximium variation allowed
	 */
	public static void diamondStep(int min, int size, double[][] map, double variation) {
		for (int x = min; x < (map.length - min); x += size) {
			for (int y = min; y < (map.length - min); y += size) {
				int left = x - min;
				int right = x + min;
				int up = y - min;
				int down = y + min;

				// the four corner values
				double val1 = map[left][up];   // upper left
				double val2 = map[left][down]; // lower left
				double val3 = map[right][up];  // upper right
				double val4 = map[right][down];// lower right

				midpointOffset(val1, val2, val3, val4, variation,
						map, x, y);
			}//end for
		}//end for
	}//end diamondStep

	/**
	 * Process for generating the final height map using the diamond-square algorithm
	 * @param iterations the number of passes
	 * @param seed1 the first corner seed value
	 * @param seed2 the second corner seed value
	 * @param seed3 the third corner seed value
	 * @param seed4 the fourth corner seed value
	 * @param variation the maximum variation allowed
	 * @return the completed height map
	 */
	public static double[][] makeHeightMap(int iterations, double seed1, double seed2, double seed3, double seed4, double variation) {
		if (iterations < 1 || variation < 0) {
			return null;
		}// end if

		int size = (1 << iterations) + 1;
		double[][] map = new double[size][size];
		final int maxIndex = map.length - 1;

		// seed the corners
		map[0][0] = seed1;
		map[0][maxIndex] = seed2;
		map[maxIndex][0] = seed3;
		map[maxIndex][maxIndex] = seed4;

		for (int i = 1; i <= iterations; i++) {
			int min = maxIndex >> i;// Minimum coordinate of the current map spaces
		size = min << 1;// Area surrounding the current place in the map

		diamondStep(min, size, map, variation);
		firstSquareStep(min, map, size, maxIndex, variation);
		secondSquareStep(map, size, min, maxIndex, variation);

		variation/=2;// Divide variation by 2
		}//end for

		//the final iteration
		map[0][0] = (map[0][1]+map[1][1]+map[1][0])/3;
		map[0][maxIndex] = (map[0][maxIndex-1]+map[1][maxIndex]+map[1][maxIndex-1])/3;
		map[maxIndex][0] = (map[maxIndex][1]+map[maxIndex-1][1]+map[maxIndex-1][0])/3;
		map[maxIndex][maxIndex] = (map[maxIndex-1][maxIndex-1]+map[maxIndex-1][maxIndex]+map[maxIndex][maxIndex-1])/3;

		return map;
	}//end makeHeightMap

	/**
	 * Offset the middle of a segment so that variation is produced in the height map
	 * @param seed1 the first corner seed value
	 * @param seed2 the second corner seed value
	 * @param seed3 the third corner seed value
	 * @param seed4 the fourth corner seed value
	 * @param variation
	 * @param map the map being used
	 * @param x the x coordinate of where you are
	 * @param y the y coordinate of where you are
	 */
	public static void midpointOffset(double seed1, double seed2, double seed3,
			double seed4, double variation, double[][] map, int x, int y) {
		double avg = (seed1 + seed2 + seed3 + seed4)/4;// average
		double var = (betterRandom() * variation);//random-valued offset
		map[x][y] = avg + var;
	}//end midpointOffset

	/**
	 * The second of the square step parts of the diamond-square algorithm for generating the terrain
	 * @param map the map being used 
	 * @param size the size of the map
	 * @param min the minimum value
	 * @param max the maximum value
	 * @param variation the maximum variation allowed
	 */
	public static void secondSquareStep(double[][] map, int size, int min,
			int max, double variation) {
		for (int x = 0; x < map.length; x += size) {
			for (int y = min; y < map.length; y += size) {
				if (x == max) {
					map[x][y] = map[0][y];
					continue;
				}// end if

				int left = 0;
				int right = x + min;
				int down = y + min;
				int up = y - min;

				if (x == 0) {
					left = max - min;
				} else {
					left = x - min;
				}// end else

				// the four corner values
				double corner1 = map[left][y]; // left
				double corner2 = map[x][up];   // up
				double corner3 = map[right][y];// right
				double corner4 = map[x][down]; // down

				midpointOffset(corner1, corner2, corner3, corner4, variation,
						map, x, y);
			}//end for
		}//end for
	}//end 

	/**
	 * Give a random number between positive and negative 1
	 * @return the random value
	 */
	public static double betterRandom() {
		double sign = Math.random();
		if (sign>0.5){
			return -Math.random();
		}// end if
		return Math.random();
	}//end betterRandom

}//end class