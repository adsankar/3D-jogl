import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLCapabilities;

import com.sun.opengl.util.Animator;


public class SankarSquareRunner {
	private Animator an;
	
	public SankarSquareRunner() {
		Frame f = new Frame("It's a shape");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}//end window closing event
		});
		f.setBackground(Color.gray);
		f.setSize(800,800);
		GLCapabilities glCap = new GLCapabilities();
		 GLSquarePanel canvas=new  GLSquarePanel(glCap);   
	    
		f.add(canvas);
		
		f.setVisible(true);
		an=new Animator(canvas);     

        an.start();
	}
	
	public static void main(String[] args){
		new SankarSquareRunner();
	}
}
	