import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLCapabilities;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;

/**
 * 3D Exercises Runner
 * @author Aleksander Sankar
 * Graphics Pd. 2
 * Mr. Fowler
 */
public class Exercises3DRunner {

	private Animator an;

	/**
	 * Instantiate an Exercises3DRunner object
	 * @param args not used
	 */

	public static void main(String[] args) {
		new Exercises3DRunner();

	}//end main

	/**
	 * Constructor which creates a frame, sets the size, windowClosing event, adds the GLCanvas
	 * Also instantiates and starts the animator and finally sets it visible
	 */
	public Exercises3DRunner(){
		Frame f = new Frame("It's rotation!");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}//end window closing event
		});
		f.setBackground(Color.gray);
		f.setSize(1000,1000);
		GLCapabilities glCap = new GLCapabilities();  

		Sankar3DExercises e = new Sankar3DExercises(glCap);
		f.add(e);//add it to the frame

		f.setVisible(true);//show the frame
		an=new FPSAnimator(e, 60);//create animator, add it to the <code>Exercises3DRunner</code> object and set it to 60 frames/second   
		an.start();//start the animator
	}//end constructor

}//end class