import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import com.sun.opengl.util.BufferUtil;


public class GLSquarePanel extends GLCanvas {

	private static float[] colors = {0,0,1,0,1,1,1,0,1,1,0,0};
	private static float[] pos = {0,0,0};
	private int rotateAngle =0;
	//private float[] vertices = new float[] {-1,1,0,-1,-1,0,-1,0,1,1,0,-1,};//1,.5f,-1,-1,.5f,1,-1,.5f,1,1,0.5f,-1,1,1,-1,-1,1,1,-1,1,1,1,1};
	private float[] vertices = new float[] {-1,1,0,-1,-1,0,1,-1,0,1,1,0};
	private FloatBuffer vert = BufferUtil.newFloatBuffer(vertices.length);
	private FloatBuffer col = BufferUtil.newFloatBuffer(colors.length);

	public GLSquarePanel(GLCapabilities glc){
		super(glc);
		this.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println("Recolored!");


				display();

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

			@Override
			public void keyTyped(KeyEvent e) {

			}

		});

		this.addMouseMotionListener(new MouseMotionListener(){

			@Override
			public void mouseDragged(MouseEvent e) {


			}

			@Override
			public void mouseMoved(MouseEvent e) {
				pos[0] = e.getX()-getWidth()/2;	
				pos[1] = e.getY()-getHeight()/2;
			}

		});


		addGLEventListener(new GLEventListener() {

			@Override
			public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
				doReshape(drawable.getGL(),width,height);

			}

			@Override
			public void init(GLAutoDrawable drawable) {
				doInit(drawable.getGL());

			}

			@Override
			public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
					boolean deviceChanged) {

			}

			@Override
			public void display(GLAutoDrawable drawable) {
				doDisplay(drawable.getGL(), drawable.getWidth(), drawable.getHeight());

			}
		});
	}

	public void doInit(GL myGL){
		myGL.glEnable( GL.GL_DEPTH_TEST );

		myGL.glDepthFunc(GL.GL_LEQUAL);
	}

	public void recolor(){
		colors[0] = (float)Math.random();
		colors[1] = (float)Math.random();
		colors[2] = (float)Math.random();
	}

	public void doReshape(GL myGL, int w, int h){
		GLU myglu = new GLU();
		myGL.glMatrixMode(GL.GL_PROJECTION);
		myGL.glLoadIdentity();
		myglu.gluPerspective(60.0,1.0,1.0,15.0);
		myGL.glViewport(0, 0, w, h);

		
		for (int i = 0; i < vertices.length; i++){
			vert.put(vertices[i]);
		}
		vert.rewind();
		for (int j = 0;  j< colors.length; j++){
			col.put(colors[j]);
		}
		col.rewind();
	}

	public void doDisplay(GL myGL, int w, int h){
		//	recolor();
		myGL.glEnableClientState(GL.GL_VERTEX_ARRAY);
		myGL.glEnableClientState(GL.GL_COLOR_ARRAY);
		GLU myglu = new GLU();
	//	GLUT glut = new GLUT();
		myGL.glShadeModel(GL.GL_SMOOTH);
		myGL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
		myGL.glMatrixMode(GL.GL_MODELVIEW);
		myGL.glLoadIdentity();
		myglu.gluLookAt(pos[0]/50,pos[1]/50,10.0f,0f,0f,0f,0f,1f,0f);

		//glut.glutWireTeapot(3);
		rotateAngle+=1;
		rotateAngle=rotateAngle%360;
		myGL.glRotatef(rotateAngle,0, 0, 1);

		myGL.glColorPointer(3, GL.GL_FLOAT, 0, col);
		myGL.glVertexPointer(3, GL.GL_FLOAT, 0, vert);
		myGL.glDrawArrays(GL.GL_QUADS, 0, 4);
		/*
		myGL.glBegin(GL.GL_QUADS);

		myGL.glColor3d(0,0,1);
		myGL.glVertex3d(-1,1,0);
		myGL.glVertex3d(-1,-1,0);
		myGL.glVertex3d(1,-1,0);
		myGL.glVertex3d(1,1,0);

		myGL.glColor3d(1,0,0);
		//recolor();
		myGL.glVertex3d(-1,1,.5);
		myGL.glVertex3d(-1,-1,.5);
		myGL.glVertex3d(1,-1,.5);
		myGL.glVertex3d(1,1,0.5);

		myGL.glColor3d(0,1,0);
		//recolor();
		myGL.glVertex3d(-1,1,1);
		myGL.glVertex3d(-1,-1,1);
		myGL.glVertex3d(1,-1,1);
		myGL.glVertex3d(1,1,1);
		 */
		myGL.glDisableClientState(GL.GL_VERTEX_ARRAY);
		myGL.glDisableClientState(GL.GL_COLOR_ARRAY);
		myGL.glEnd();

	}


}
