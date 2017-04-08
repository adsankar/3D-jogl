import java.awt.Color;

import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;


public class LightingRunner {

	private Animator an;

	/**
	 * Instantiate a LightingRunner object
	 * @param args not used
	 */

	public static void main(String[] args) {
		new LightingRunner();

	}//end main

	/**
	 * Constructor which creates a frame, sets the size, windowClosing event, adds the GLCanvas
	 * Also instantiates and starts the animator and finally sets it visible
	 */
	public LightingRunner(){
		JFrame jf = new JFrame("Basic Lighting Exercise");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setBackground(Color.gray);
		jf.setSize(1000,1000);
		GLCapabilities glCap = new GLCapabilities();  

		SankarLighting l = new SankarLighting(glCap);
		jf.add(l);//add it to the frame

		jf.setVisible(true);//show the frame
		an=new FPSAnimator(l, 60);//create animator, add it to the <code>LightingRunner</code> object and set it to 60 frames/second   
		an.start();//start the animator
	}//end constructor

}//end class