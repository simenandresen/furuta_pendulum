package sa_demos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.gl2.GLUT;


public class Furuta_Pendulum {
	GLUT glut;
	GLU glu;
	GL2 gl;
	FP_dynamics fp_dynamics;
	public final float arm1 = 2.0f;
	public final float arm2 = 1.5f;
	public double [] x_init={2 ,1, 180f, 180f};
	public float theta1 = 2.0f;
	public float theta2 = 30f;
	float update_freq;
	float no_mat[] = { 0.0f, 0.0f, 0.0f, 1.0f };
	float mat_ambient[] = { 0.4f, 0.4f, 0.4f, 1.0f };
	float mat_ambient_color[] = { 0.4f, 0.4f, 0.4f, 1.0f };
	float mat_diffuse[] = { 0.1f, 0.1f, 0.8f, 1.0f };
	float mat_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	float no_shininess[] = { 0.0f };
	float low_shininess[] = { 70.0f };
	float high_shininess[] = { 20.0f };
	float mat_emission[] = { 0.3f, 0.2f, 0.2f, 0.0f };
	FileWriter fstream;
	BufferedWriter out;
	PrintWriter outprint;
	
	public Furuta_Pendulum(GLUT aGlut, GLU aGlu, GL2 aGl) {
		this.glut=aGlut;
		this.glu=aGlu;
		this.gl=aGl;
		fp_dynamics=new FP_dynamics(x_init, arm1, arm2);
	}
	
	public void redraw_pendulum(){
		update_dynamics();
		this.gl.glLoadIdentity();
		//draw_base_cylinder();
		draw_arm1();
		draw_arm2();
	}
	
	public void draw_base_cylinder() {
		gl.glRotated(90, 1, 0, 0);
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float []{0.01f, 0.01f,0.4f,1.0f}, 0);
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, high_shininess, 0);
		
	    glut.glutSolidCylinder(0.3,0.5 , 60, 10);
		gl.glLoadIdentity();
	} 

	private void draw_arm1() {
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, high_shininess, 0);
		gl.glRotatef(theta1, 0, 1, 0);
		glut.glutSolidCylinder(0.02, arm1, 6, 10);
		gl.glTranslatef(0, 0, arm1);
	}
	
	private void draw_arm2() {
		gl.glRotatef(-90, 1, 0, 0);
		gl.glRotatef(-theta2, 0, 1, 0);
		gl.glPushMatrix();
	    glut.glutSolidCylinder(0.02,arm2, 6, 10);
		gl.glTranslatef(0, 0, arm2);
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float []{0.7f, 0.1f,0.1f,1.0f}, 0);
	    glut.glutSolidSphere(0.22f, 20, 20);
		gl.glPopMatrix();
	}
	
	private void update_dynamics() {
		double [] temp=fp_dynamics.solve_dyn();
		this.theta1=(float) temp[0];
		this.theta2=(float) (temp[1]);
	}
	
	public void wrapUp(){
		try{
			System.out.println("close file streams");
			out.close();
			outprint.close();
		}catch(Exception e){
			System.err.println(e.getMessage());
		
		}
	}
	
	public void set_tau(double tau){
		this.fp_dynamics.tau=tau;
	}
}
