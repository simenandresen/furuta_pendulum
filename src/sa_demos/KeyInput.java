package sa_demos;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.jogamp.opengl.util.FPSAnimator;

class KeyInput extends KeyAdapter {
	public ARenderer renderer;
	FPSAnimator animator;
	public double tau;
	

	public KeyInput(ARenderer canvas, FPSAnimator animator) {
		this.renderer = canvas;
		this.animator = animator;
	}
	
	public void keyReleased(KeyEvent e){
		if(e.getKeyChar()=='i'){
			renderer.zoomInX();
		}else if(e.getKeyChar()=='o'){
			renderer.zoomOutX();
		}else if(e.getKeyChar()=='k'){
			renderer.zoomInY();
		}else if(e.getKeyChar()=='l'){
			renderer.zoomOutY();
		}else if(e.getKeyChar()==','){
			renderer.zoomInZ();
		}else if(e.getKeyChar()=='.'){
			renderer.zoomOutZ();
		}else if(e.getKeyCode()==KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_RIGHT){
			renderer.set_tau(0);
		}else if(e.getKeyChar()=='q'){
			animator.stop();
			System.exit(0);
		}
		
	}
	
	public void keyPressed(KeyEvent b){
			int key= b.getKeyCode();
			switch(key){
			case KeyEvent.VK_LEFT:
				tau=100;
				break;
			case KeyEvent.VK_RIGHT:
				tau=-100;
				break;
			case KeyEvent.VK_UP:
				tau=0;
			default:
				tau=0;
				renderer.set_tau(0);
				break;
			}
		renderer.set_tau(tau);
	}
}