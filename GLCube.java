import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;


public class GLCube extends GLCanvas{

	private int rotateAngle = 0;
	private boolean isRunning = true;
	
	public GLCube(GLCapabilities glCap){
		super(glCap);
		addGLEventListener(new GLEventListener() {

			@Override
			public void reshape(GLAutoDrawable drawable, int x, int y,
					int width, int height) {
				doReshape(drawable.getGL(), width, height);

			}

			@Override
			public void init(GLAutoDrawable drawable) {
				//

			}

			@Override
			public void displayChanged(GLAutoDrawable drawable,
					boolean modeChanged, boolean deviceChanged) {
				// 

			}

			@Override
			public void display(GLAutoDrawable drawable) {
				rotation();
				doDisplay(drawable.getGL(), drawable.getWidth(),
						drawable.getHeight());

			}
		});
	}
	
	public void doReshape(GL myGL, int w, int h){
		GLU myglu = new GLU();
		myGL.glMatrixMode(GL.GL_PROJECTION);
		myGL.glLoadIdentity();
		myglu.gluOrtho2D(-3, 3, -3, 3);
		myGL.glViewport(0, 0, w, h);
	}
	public void doDisplay(GL myGL, int w, int h){
		myGL.glShadeModel(GL.GL_SMOOTH);
		myGL.glClear(GL.GL_COLOR_BUFFER_BIT);
		myGL.glMatrixMode(GL.GL_MODELVIEW);

		myGL.glLoadIdentity();

		myGL.glRotatef(rotateAngle, 0, 0, 1);
		makeCube(myGL);
		
	}
	
	public void makeCube(GL myGL){
		myGL.glBegin(GL.GL_QUADS);
		
		
		myGL.glColor3d(0,1,0);
		
		myGL.glVertex3d(1, 1,1);
		myGL.glVertex3d(1, -1,1);
		myGL.glVertex3d(-1, -1,1);
		myGL.glVertex3d(-1, 1,1);
		
		myGL.glVertex3d(1, 1,-1);
		myGL.glVertex3d(1, -1,-1);
		myGL.glVertex3d(-1, -1,-1);
		myGL.glVertex3d(-1, 1,-1);
		
		myGL.glVertex3d(1, 1,1);
		myGL.glVertex3d(-1, 1,1);
		myGL.glVertex3d(1, -1,-1);
		myGL.glVertex3d(1, -1,-1);
		
		myGL.glVertex3d(1, -1,1);
		myGL.glVertex3d(1, -1,1);
		myGL.glVertex3d(1, -1,1);
		myGL.glVertex3d(1, -1,1);
		
		myGL.glVertex3d(1, -1,1);
		myGL.glVertex3d(1, -1,1);
		myGL.glVertex3d(1, -1,1);
		myGL.glVertex3d(1, -1,1);
		
		myGL.glVertex3d(1, -1,1);
		myGL.glVertex3d(1, -1,1);
		myGL.glVertex3d(1, -1,1);
		
		myGL.glEnd();
	}
	
	public void rotation(){
		if (isRunning) {
			rotateAngle += 1;
			rotateAngle = rotateAngle % 360;
		}
	}
	
}
