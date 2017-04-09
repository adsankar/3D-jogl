import java.awt.Color;

import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;


public class TerrainMapRunner {

	private Animator an;

	/**
	 * Constructor which creates a frame, sets the size, windowClosing event, adds the GLCanvas
	 * Also instantiates and starts the animator and finally sets it visible
	 */
	public TerrainMapRunner(){
		JFrame jf = new JFrame("Terrain Map");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setBackground(Color.gray);
		jf.setSize(1000,1000);
		GLCapabilities glCap = new GLCapabilities();  

		TerrainMap t = new TerrainMap(glCap);
		jf.add(t);//add it to the frame

		jf.setVisible(true);//show the frame
		an=new FPSAnimator(t, 60);//create animator, add it to the <code>TextureRunner</code> object and set it to 60 frames/second   
		an.start();//start the animator
	}//end constructor

}//end class