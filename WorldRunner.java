
/*
 * Jogl Tutorial
 * Copyright (C) 2004 
 * Kevin Conroy <kmconroy@cs.umd.edu>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.media.opengl.GLCapabilities;
//import javax.media
//import javax.media.opengl.awt.GLJPanel;

//import com.jogamp.opengl.util.Animator;
import com.sun.opengl.util.Animator;
/** 
 * Based on NeHe Production Lesson 01 ported by Kevin Duling
 * (jattier@hotmail.com)
 * 
 * @author Kevin Conroy (kmconroy@cs.umd.edu)
 */
public class WorldRunner{
	private JFrame frame ;
	private Animator an;
	
	public WorldRunner(){
		System.out.println("hi");
		frame= new JFrame("Hello World!");

        GLCapabilities capabilities=new GLCapabilities(); //GLProfile.getDefault());
        capabilities.setDoubleBuffered(true);
        //capabilities.set
        SquareWorldPanel canvas=new  SquareWorldPanel (capabilities);
       // frame.addMouseMotionListener(new MouseMotionAdapter(){
        	
       // });
        
        
        frame.add(canvas);
       
        setUpFrame();
        an=new Animator(canvas);     

        an.start();
        
	}
	
    /**
     * Main function which will create a new AWT Frame and add a JOGL Canvas to
     * it
     * 
     * @param args Runtime args
     */
    public static void main(String[] args) {
       new WorldRunner();

    }
    
    public void setUpFrame(){

        frame.setSize(300, 300);
        frame.setBackground(Color.white);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

    	frame.addKeyListener(new KeyAdapter(){ //stop rotation
			public void keyReleased(KeyEvent e){
				if(e.getKeyChar()==' '){
					//if(an.isPaused()) an.resume(); else an.pause();
				}
				
			}
		});
		
		frame.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				//e=e;
			}
		});
		
		
        frame.setVisible(true);
    	
    }
}
