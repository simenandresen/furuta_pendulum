package sa_demos;

import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;


import com.jogamp.opengl.util.gl2.GLUT;

@SuppressWarnings("serial")
class ARenderer extends GLCanvas implements GLEventListener {
	private GLUT glut;
	private GLU glu; // for the GL Utility
	private GL2 gl;
	private Furuta_Pendulum furuta_pendulum;
	public long time;
	public float fps;
	public double torque;
	
	public ARenderer() {
		this.addGLEventListener(this);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		
		gl = drawable.getGL().getGL2(); // get the OpenGL graphics context
		glu = new GLU(); // get GL Utilities
		glut = new GLUT();
		furuta_pendulum= new Furuta_Pendulum(this.glut, this.glu, this.gl);

		float mat_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float mat_shininess[] = { 20.0f };
		float light_position[] = { 1.0f, 1.0f, 1.0f, 0.0f };
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);  
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, mat_shininess, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light_position, 0);
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDepthFunc(GL2.GL_LESS);
		
		gl = drawable.getGL().getGL2(); // get the OpenGL 2 graphics context //
		setCamera(gl, glu, 6);
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers

		gl.glLoadIdentity(); // reset the current model-view matrix
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float[] {0, 0.8f, 0, 1}, 0); 
		gl.glPushMatrix();
		gl.glRotatef(90, 0, 1, 0); 
		drawAxis(gl);
		gl.glPopMatrix();
		gl.glRasterPos3d(1.5, 0, 0);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "X");
		
		/* Draw the y-axis in green. */
		gl.glRasterPos3d(0, 1.5, 0);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Y");
		gl.glPushMatrix();
		gl.glRotatef(-90, 1, 0, 0); // drawAxis draws a z-axis; rotate it onto the y-axis.
		drawAxis(gl);
		gl.glPopMatrix();

		/* Draw the z-axis in blue . */
		gl.glRasterPos3d(0, 0, 1.5);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Z");
		gl.glPushMatrix();
		gl.glRotatef(-90, 0, 0, 1); // drawAxis draws a z-axis; rotate it onto the y-axis.
		drawAxis(gl);
		gl.glPopMatrix();

		furuta_pendulum.draw_base_cylinder();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2(); // get the OpenGL 2 graphics context
		if (height == 0) height = 1; // prevent divide by zero
		float aspect = (float) width / height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL_PROJECTION); // choose projection matrix
		gl.glLoadIdentity(); // reset projection matrix
		glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity(); // reset
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		
		gl = drawable.getGL().getGL2(); // get the OpenGL 2 graphics context
		setCamera(gl, glu, 6);
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
		
		gl.glColor3f(1, 0, 0);
		gl.glWindowPos2f(50f, 50f);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, String.format("Press the arrow key to apply torque to the arm. Torque = %1.1f Nm ", torque_output[0]));
		
		gl.glLoadIdentity(); // reset the current model-view matrix
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float[] {0, 0.8f, 0, 1}, 0); 
		gl.glPushMatrix();
		gl.glRotatef(90, 0, 1, 0); 
		drawAxis(gl);
		gl.glPopMatrix();
		gl.glRasterPos3d(1.5, 0, 0);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "X");
		
		/* Draw the y-axis in green. */
		gl.glRasterPos3d(0, 1.5, 0);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Y");
		gl.glPushMatrix();
		gl.glRotatef(-90, 1, 0, 0); // drawAxis draws a z-axis; rotate it onto the y-axis.
		drawAxis(gl);
		draw_torque_direction(torque,gl);
		gl.glPopMatrix();

		/* Draw the z-axis in blue . */
		gl.glRasterPos3d(0, 0, 1.5);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Z");
		gl.glPushMatrix();
		gl.glRotatef(-90, 0, 0, 1); // drawAxis draws a z-axis; rotate it onto the y-axis.
		drawAxis(gl);
		gl.glPopMatrix();
		
		/* Draw  base cylinder */
		furuta_pendulum.draw_base_cylinder();
		furuta_pendulum.redraw_pendulum();
	}


	private void drawAxis(GL2 gl) {
		gl.glPushMatrix();
		glut.glutSolidCylinder(0.005, 1, 6, 10); // Cylinder, radius 0.02, height 1,
		// base at (0,0,0), lying on z-axis.
		gl.glTranslatef(0, 0, 1); // Move the cone to the top of the cylinder
		glut.glutSolidCone(0.05, 0.15, 12, 5); // Cone, radius 0.1, height 0.3,
		// base at (0,0,0), pointing along z-axis.
		gl.glPopMatrix();
	}

	private void setCamera(GL2 gl, GLU glu, float distance) {
		// Change to projection matrix.
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		// Perspective.
		float widthHeightRatio = (float) getWidth() / (float) getHeight();
		glu.gluPerspective(90, widthHeightRatio, 1, 1000);
		glu.gluLookAt(xView, yView, zView, 0, 0, 0, 0, 1, 0);

		// Change back to model view matrix.
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	
	private double torque_input=0;
	private double [] torque_output={0,0};
	// input from keys get low pass filtered through a 1st order IIR filter
	public void draw_torque_direction(double torque, GL2 gl){
		// 0 no torque, 1 = left , -1 = rigth
		torque_output[0]=torque_output[1]*0.8669+torque_input*0.01331;
		torque_input=torque;
		torque_output[1]=torque_output[0];
		draw_torque_arrow(torque_output[0]*8,gl);
	}
	
	private void draw_torque_arrow(double torque, GL2 gl){
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float[] {1, 0.0f, 0, 1}, 0); 
		gl.glPushMatrix();
		double arrow_length=0.02*torque;
		glut.glutSolidCylinder(0.01, arrow_length, 6, 10); // Cylinder, radius 0.02, height 1,
		// base at (0,0,0), lying on z-axis.
		gl.glTranslatef(0, 0, (float)arrow_length); // Move the cone to the top of the cylinder
		if (torque<0){
			gl.glRotatef(180f, 1f,0f,0f);
		}
		glut.glutSolidCone(0.05, 0.2, 12, 5); // Cone, radius 0.1, height 0.3,
		gl.glPopMatrix();
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float[] {0, 0.8f, 0, 1}, 0); 
	}

	private float xView=1.5f;
	private float yView=3.0f;
	private float zView=1.5f;
	private float viewChange=0.5f;
	
	public void zoomOutX(){
		xView=xView+viewChange;	
	}
	public void zoomInX(){
		xView=xView-viewChange;

	}
	public void zoomOutY(){
		yView=yView+viewChange;	
	}
	public void zoomInY(){
		yView=yView-viewChange;

	}
	public void zoomOutZ(){
		zView=zView+viewChange;	
	}
	public void zoomInZ(){
		zView=zView-viewChange;

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}
	
	public void set_tau(double tau){
		this.torque=tau;
		furuta_pendulum.set_tau(tau);
	}



}