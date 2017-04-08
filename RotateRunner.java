import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLCapabilities;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;

/**
 * Basic 2D Exercises
 * @author Aleksander Sankar
 * 3D Graphics Pd. 2
 * Mr Fowler
 */

public class RotateRunner {
	// the animator used
	private Animator an;
	
	/**
	 * Constructor which creates the frame, sets the size and title, creates the <code>GLCapabilities </code> and <code>SankarRotateSq</code>
	 */
	public RotateRunner() {
		Frame f = new Frame("It's rotation!");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}//end window closing event
		});
		f.setBackground(Color.gray);
		f.setSize(800,800);
		GLCapabilities glCap = new GLCapabilities();  
		
		SankarRotateSq sq = new SankarRotateSq(glCap);
		f.add(sq);//add it to the frame
		
		f.setVisible(true);//show the frame
		an=new FPSAnimator(sq, 60);//create animator, add it to the <code>SankarRotateSq</code> object and set it to 60 frames/second   
        an.start();//start the animator
	}
	
	/**
	 * Instantiate a <code>RototeRunner </code> object
	 * @param args not used
	 */
	
	public static void main(String[] args){
		new RotateRunner();
	}//end main
}//end class

/*Animator
 * The animator class creates a background thread which calls the display method.
 * It draws the drawable object and handles pauses quickly so that the CPU does not get swamped.
 * 
 */
