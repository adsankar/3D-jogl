package softwareDesign;

import java.awt.Color;

import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;

public class WalkDemoRunner {

	private Animator an;

	public WalkDemoRunner(){
		JFrame jf = new JFrame("Walking Program");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setBackground(Color.gray);
		jf.setSize(1000,1000);
		GLCapabilities glCap = new GLCapabilities();  

		WalkDemo w = new WalkDemo(glCap);
		jf.add(w);//add it to the frame

		jf.setVisible(true);//show the frame
		an=new FPSAnimator(w, 60);//create animator, add it to the <code>InteriorLightingRunner</code> object and set it to 60 frames/second   
		an.start();//start the animator
	}

}
