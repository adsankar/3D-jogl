import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;


public class SankarBasic2D extends GLCanvas{
	
	public SankarBasic2D(GLCapabilities glCap){
		super(glCap);
		
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
		myGL.glColor3d(1,0,0);
		myGL.glBegin(GL.GL_QUADS);
		myGL.glVertex2d(-w/2,h/2);
		myGL.glVertex2d(w,h/2);
		myGL.glVertex2d(w,-h/2);
		myGL.glVertex2d(-w/2,-h/2);
		myGL.glColor3d(0,0,1);
		myGL.glEnd();
		
		//blue square
		myGL.glBegin(GL.GL_LINE_LOOP);
		myGL.glVertex2d(-1,1);
		myGL.glVertex2d(-1,-1);
		myGL.glVertex2d(1,-1);
		myGL.glVertex2d(1,1);
		myGL.glColor3d(0,1,0);
		myGL.glEnd();
		
		
		//triangles
		myGL.glBegin(GL.GL_TRIANGLES);
		myGL.glVertex2d(-.3,.3);
		myGL.glColor3d(0,0,1);
		myGL.glVertex2d(.3,.3);
		myGL.glColor3d(1,0,0);
		myGL.glVertex2d(0,-.3);
		myGL.glEnd();
		
		
		//points
		myGL.glPointSize(5);
		myGL.glBegin(GL.GL_POINTS);
		myGL.glColor3d(0,0,1);
		myGL.glVertex2d(-.3,.3);
		myGL.glVertex2d(.3,.3);
		myGL.glVertex2d(0,-.3);
		myGL.glEnd();
		
		//more stuff
		//this part draws a line from the first specified point to the next
		myGL.glColor3d(.7,0,1);
		myGL.glBegin(GL.GL_LINES);
		myGL.glVertex2d(-1.2, 1.5);
		myGL.glVertex2d(1.2, -1.5);
		myGL.glEnd();
		
		//this part draws a set of triangles all of which have the first point specified as a shared vertex
		//each subsequent set of two vertices specifies the other two vertices of the triangle
		myGL.glBegin(GL.GL_TRIANGLE_FAN);
		myGL.glVertex2d(-1.5, -.5);
		myGL.glVertex2d(-1.3, -.3);
		myGL.glVertex2d(-1, -.9);
		myGL.glVertex2d(-.6, -.2);
		myGL.glVertex2d(.2, -.3);
		myGL.glEnd();
		myGL.glColor3d(.2,.2,.8);
		
		//this part draws quadrilaterals using the first two points and uses adjacent edges to form the next quadrialteral
		myGL.glBegin(GL.GL_QUAD_STRIP);
		myGL.glVertex2d(1.5, 1.5);
		myGL.glVertex2d(1.3, 1.3);
		myGL.glVertex2d(1, 1.9);
		myGL.glVertex2d(.6, 1.2);
		myGL.glVertex2d(-1.2, 1.3);
		myGL.glVertex2d(1.7, 1.1);
		myGL.glVertex2d(1.62, 1.3);
		myGL.glEnd();
		myGL.glColor3d(.8,.9,.1);
		
		//this draws a filled in polygon with vertices at the points specified and filled in
		myGL.glBegin(GL.GL_POLYGON);
		myGL.glVertex2d(1.5, -1.5);
		myGL.glVertex2d(1.3, -1.3);
		myGL.glVertex2d(1, -1.9);
		myGL.glVertex2d(.6, -1.2);
		myGL.glVertex2d(2, -1.3);
		myGL.glVertex2d(1.7, -1.1);
		myGL.glVertex2d(1.62, -1.3);
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
