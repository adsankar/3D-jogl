import java.awt.Font;
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


public class SankarLighting extends GLCanvas {

	private double scaling =1;
	private float[] mov  = {0,15,30};
	private TextRenderer renderer;

	public SankarLighting(GLCapabilities glc){
		super(glc);
		//add a mouseWheelListener
		addMouseWheelListener(new MouseWheelListener(){

			@Override
			/**
			 * Zoom in/out by scrolling
			 */
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation()>0){
					scaling+=.01;
				}
				else scaling-=0.01;

				//System.out.println(scaling);
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
				renderer.setColor(1f, 0.85f, 0.15f, 0.8f);//RGBA colors
				renderer.draw("Lighting Demonstration", 0, 10);

				renderer.draw("Light location: ("+mov[0]+", "+mov[1]+", "+mov[2]+")",0,40);
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


	}//end constructor

	public void size(GL myGL){
		myGL.glScaled(scaling, scaling, scaling);

	}

	/**
	 * Method for setting up the 3D effects and depth view
	 * @param myGL
	 */
	public void doInit(GL myGL){
		myGL.glEnable( GL.GL_DEPTH_TEST );
		myGL.glDepthFunc(GL.GL_LEQUAL);
		myGL.glEnable(GL.GL_LIGHTING);
		myGL.glEnable(GL.GL_COLOR_MATERIAL);
		myGL.glEnable(GL.GL_LIGHT0);
		renderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 36),true,true);
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

		myGL.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, new float[]{0.2f,0.2f,0.2f,1},0 );
		myGL.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, new float[]{0.7f,0.7f,0.7f,1},0 );
		myGL.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, new float[]{1f,1f,1f,1},0 );
		myGL.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, new float[]{mov[0],mov[1],mov[2]},1);

		myGL.glLoadIdentity();
		myglu.gluLookAt(0,0,0,0f,0f,20f,0f,1f,0f);
		
		//The icosahedron 
		myGL.glTranslated(.2, -.3, 5);
		myGL.glColorMaterial(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE);
		myGL.glColor4fv(new float[]{1,0f,1f},0);
		glut.glutSolidIcosahedron();		 

		//Placing cube
		myGL.glColor4fv(new float[]{.5f,1f,0f},0);
		myGL.glTranslated(3, 0, 0);
		glut.glutSolidCube(1.2f);

		//Placing sphere
		myGL.glColor4fv(new float[]{0,0f,1f},0);
		myGL.glTranslated(-6, 0, 0);
		glut.glutSolidSphere(.8, 100, 360); 

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
		myglu.gluPerspective(60.0,1.0,2,10);
		myGL.glViewport(0, 0, w, h);
	}//end doReshape


}//end class