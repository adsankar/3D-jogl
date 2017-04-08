import java.awt.Color;
import java.awt.Toolkit;

import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;

/**
 * Terrain Map Runner
 * @author Aleksander Sankar and Sohum Dalal
 * 3D Graphics Pd. 2
 * Mr. Fowler
 */
public class TerrainMapRunner {

	private Animator an;

	/**
	 * Instantiate an TerrainMapRunner object
	 * @param args not used
	 */
	public static void main(String[] args) {
		new TerrainMapRunner();

	}//end main

	/**
	 * Constructor which creates a frame, sets the size, windowClosing event, adds the GLCanvas
	 * Also instantiates and starts the animator and finally sets it visible
	 */
	public TerrainMapRunner(){
		JFrame jf = new JFrame("Terrain Map");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setBackground(Color.gray);
		jf.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
				(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		GLCapabilities glCap = new GLCapabilities();  

		TerrainMap t = new TerrainMap(glCap);
		jf.add(t);//add it to the frame

		jf.setVisible(true);//show the frame
		an=new FPSAnimator(t, 60);//create animator, add it to the <code>TextureRunner</code> object and set it to 60 frames/second   
		an.start();//start the animator
	}//end constructor

}//end class