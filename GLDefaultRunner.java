import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLCapabilities;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;


public class GLDefaultRunner {
	
	private Animator an;
	
	public  GLDefaultRunner() {
		
		Frame f = new Frame("TESTING WINDOW");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}//end window closing event
		});
		f.setBackground(Color.gray);
		f.setSize(800,800);
		GLCapabilities glCap = new GLCapabilities();  
		
		//change the class here
		GLSquarePanel sq = new GLSquarePanel(glCap);
		f.add(sq);
		
		f.setVisible(true);
		an=new FPSAnimator(sq, 60);     
		an.add(sq);
        an.start();
	}
	
	public static void main(String[] args){
		System.err.println("TESTING WINDOW");
		new GLDefaultRunner();
	}
}
