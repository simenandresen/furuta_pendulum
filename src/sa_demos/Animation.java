package sa_demos;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import com.jogamp.opengl.util.FPSAnimator;

public class Animation{
	private static String TITLE = "Rotating 3D Shaps (GLCanvas)"; // window's title
	private static final int CANVAS_WIDTH = 1000; // width of the drawable
	private static final int CANVAS_HEIGHT = 700; // height of the drawable
	public static final int FPS = 70; // animator's target frames per second

	public static void main(String[] args) {
		// Run the GUI codes in the event-dispatching thread for thread safety
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ARenderer canvas = new ARenderer();
				canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
				final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);
				KeyInput keyInput = new KeyInput(canvas, animator);
				final JFrame frame = new JFrame(); 
				frame.getContentPane().add(canvas);
				frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						// Use a dedicate thread to run the stop() to ensure that the
						// animator stops before program exits.
						new Thread() {@Override
							public void run() {
								if (animator.isStarted()) animator.stop();
								System.exit(0);
							}
						}.start();
					}
				});
				frame.setTitle(TITLE);
				frame.pack();

				frame.setVisible(true);
				canvas.addKeyListener(keyInput);
				animator.start(); // start the animation loop
			}
		});
	}
}
