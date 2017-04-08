
import javax.media.opengl.GL;
//import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLJPanel;
//import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
//import com.jogamp.opengl.util.Animator;
//import com.jogamp.opengl.util.gl2.GLUT;

public class SquareWorldPanel extends GLJPanel {
	private float rotateAngle=0f;

	
	public SquareWorldPanel(GLCapabilities defaultCapab){
		super(defaultCapab);
		
		 
		addGLEventListener(new GLEventListener() {
			 public void display( GLAutoDrawable glautodrawable ) {

		         render( glautodrawable.getGL(), 
		        		 glautodrawable.getWidth(), glautodrawable.getHeight() );
		         
		     }

			
			public void init(GLAutoDrawable drawable) {
				drawable.getGL().glClearColor(0.0f,0.0f,0.0f,0.0f);   
			}

			public void displayChanged(GLAutoDrawable drawable,boolean x,boolean y) {
			
				 
			}
			
			public void reshape(GLAutoDrawable drawable, int x, int y, int width,
					int height) {
				setup( drawable.getGL(), width, height );
				
			}
			
		});
	}
	
	
	public  void setup( GL gl2, int width, int height ) {
        gl2.glMatrixMode( GL.GL_PROJECTION );
        gl2.glLoadIdentity();
      //  gl2.glClearColor(1.0f, 1.0f, 1.0f, 0f);
        // coordinate system origin at lower left with width and height same as the window
        GLU glu = new GLU();
          
        glu.gluOrtho2D(  -2.0f, 2f, -2.0f, 2f);
        

        gl2.glMatrixMode( GL.GL_MODELVIEW );
        gl2.glLoadIdentity();

        gl2.glViewport( 0, 0, width, height );
    }

	public  void render( GL gl2, int width, int height ) {
        gl2.glClear( GL.GL_COLOR_BUFFER_BIT );
 
        
        // draw a triangle filling the window
        gl2.glLoadIdentity();
        
        rotateAngle+=1;
        rotateAngle=rotateAngle%360;
        gl2.glRotatef(rotateAngle,0, 0, 1);
        
        gl2.glBegin(GL.GL_LINE_LOOP);
        gl2.glVertex2f(-0.5f,-0.5f);
        gl2.glVertex2f(-0.5f,0.5f);
        gl2.glVertex2f(0.5f,0.5f);
        gl2.glVertex2f(0.5f,-0.5f);
        gl2.glEnd();

        gl2.glEnd();
    }
}
