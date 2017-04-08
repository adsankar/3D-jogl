import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import com.sun.opengl.util.Animator;


public class GLTriangles extends GLCanvas{


	private Animator an;
	
	public GLTriangles(GLCapabilities glc){
		super(glc);
		an = new Animator();
		addGLEventListener(new GLEventListener(){

			@Override
			public void display(GLAutoDrawable drawable) {
				doDisplay(drawable.getGL(), drawable.getWidth(), drawable.getHeight());

			}

			@Override
			public void displayChanged(GLAutoDrawable arg0, boolean arg1,
					boolean arg2) {


			}

			@Override
			public void init(GLAutoDrawable drawable) {
				drawable.getGL().glClearColor(0.0f,0.0f,0.0f,0.0f);

			}

			@Override
			public void reshape(GLAutoDrawable drawable, int x, int y,	int width, int height) {
				doReshape(drawable.getGL(),width,height);

			}

		});
	}
	public void doDisplay(GL myGL, int w, int h){
		myGL.glClear(GL.GL_COLOR_BUFFER_BIT);
		myGL.glMatrixMode(GL.GL_MODELVIEW);
		myGL.glLoadIdentity();
		
		//background red
		myGL.glColor3d(.5,.5,1);
		myGL.glBegin(GL.GL_TRIANGLES);
		myGL.glVertex2d(0,0); 
		myGL.glVertex2d(0,1);
		myGL.glVertex2d(-.5,1);
		
		myGL.glColor3d(1,.5,1);
		myGL.glVertex2d(-.5,1);
		myGL.glVertex2d(-.5,0);
		myGL.glVertex2d(0,0);
		
		myGL.glColor3d(.5,0,1);
		myGL.glVertex2d(-.5,0);
		myGL.glVertex2d(-1,0);
		myGL.glVertex2d(-.5,1);
		
		myGL.glColor3d(0,.5,1);
		myGL.glVertex2d(0,1);
		myGL.glVertex2d(-.25,1.5);
		myGL.glVertex2d(-.5,1);
		
		
		myGL.glColor3d(.5,1,1);
		myGL.glVertex2d(0,1);
		myGL.glVertex2d(.5,0);
		myGL.glVertex2d(0,0);
		
		//myGL.glColor3d(0,0,1);
		myGL.glEnd();
	}
	
	public void doReshape(GL myGL, int w, int h){
		GLU myglu = new GLU();
		myGL.glMatrixMode(GL.GL_PROJECTION);
		myGL.glLoadIdentity();
		myglu.gluOrtho2D(-2, 2, -2, 2);
		myGL.glViewport(0, 0, w, h);
	}

}
