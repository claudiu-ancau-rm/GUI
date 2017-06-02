import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.util.Animator;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainFrame
        extends JFrame
        implements GLEventListener {

    private GLCanvas canvas;
    private Animator animator;

    // For specifying the positions of the clipping planes (increase/decrease the distance) modify this variable.
    // It is used by the glOrtho method.
    private double v_size = 10.0;

    // Application main entry point
    public static void main(String args[]) {
        new MainFrame();
    }

    // Default constructor
    public MainFrame() {
        super("Java OpenGL");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setSize(800, 600);

        this.initializeJogl();

        this.setVisible(true);
    }

    private void initializeJogl() {
        // Creating a new GL profile.
        GLProfile glprofile = GLProfile.getDefault();
        // Creating an object to manipulate OpenGL parameters.
        GLCapabilities capabilities = new GLCapabilities(glprofile);

        // Setting some OpenGL parameters.
        capabilities.setHardwareAccelerated(true);
        capabilities.setDoubleBuffered(true);

        // Try to enable 2x anti aliasing. It should be supported on most hardware.
        capabilities.setNumSamples(2);
        capabilities.setSampleBuffers(true);

        // Creating an OpenGL display widget -- canvas.
        this.canvas = new GLCanvas(capabilities);

        // Adding the canvas in the center of the frame.
        this.getContentPane().add(this.canvas);

        // Adding an OpenGL event listener to the canvas.
        this.canvas.addGLEventListener(this);

        // Creating an animator that will redraw the scene 40 times per second.
        this.animator = new Animator(this.canvas);

        // Starting the animator.
        this.animator.start();
    }

    public void init(GLAutoDrawable canvas) {
        // Obtaining the GL instance associated with the canvas.
        GL2 gl = canvas.getGL().getGL2();

        // Setting the clear color -- the color which will be used to erase the canvas.
        gl.glClearColor(0, 0, 0, 0);

        // Selecting the modelview matrix.
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);

        // Activate the GL_LINE_SMOOTH state variable. Other options include
        // GL_POINT_SMOOTH and GL_POLYGON_SMOOTH.
        gl.glEnable(GL.GL_LINE_SMOOTH);
        gl.glEnable(GL2.GL_POLYGON_SMOOTH);

        // Activate the GL_BLEND state variable. Means activating blending.
        gl.glEnable(GL.GL_BLEND);

        // Set the blend function. For antialiasing it is set to GL_SRC_ALPHA for the source
        // and GL_ONE_MINUS_SRC_ALPHA for the destination pixel.
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        // Control GL_LINE_SMOOTH_HINT by applying the GL_DONT_CARE behavior.
        // Other behaviours include GL_FASTEST or GL_NICEST.
        gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_DONT_CARE);
        // Uncomment the following two lines in case of polygon antialiasing
        gl.glEnable(GL2.GL_POLYGON_SMOOTH);
        gl.glHint(GL2.GL_POLYGON_SMOOTH_HINT, GL.GL_NICEST);

    }

    public void cordonateRaspunsuriGresite(float[] combinatieCuloare, float numarIncercare, float variatiaX) {
        float[] cordonate = new float[]{-8.8f + variatiaX, -8.5f + variatiaX, 7.8f - numarIncercare, 7.5f - numarIncercare};
        construirePoligon(combinatieCuloare, cordonate);
    }

    public void cordonateRaspunsuriCorecte(float[] combinatieCuloare, float numarIncercare, float variatiaX) {
        float[] cordonate = new float[]{-8.8f + variatiaX, -8.5f + variatiaX, 7.4f - numarIncercare, 7.2f - numarIncercare};
        construirePoligon(combinatieCuloare, cordonate);
    }

    public void construirePoligon(float[] combinatieCuloare, float[] cordonate) {
        GL2 gl = canvas.getGL().getGL2();
        gl.glBegin(GL2.GL_POLYGON);
        gl.glColor3f(combinatieCuloare[0], combinatieCuloare[1], combinatieCuloare[2]);
        gl.glVertex2f(cordonate[0], cordonate[2]);
        gl.glColor3f(combinatieCuloare[0], combinatieCuloare[1], combinatieCuloare[2]);
        gl.glVertex2f(cordonate[1], cordonate[2]);
        gl.glColor3f(combinatieCuloare[0], combinatieCuloare[1], combinatieCuloare[2]);
        gl.glVertex2f(cordonate[1], cordonate[3]);
        gl.glColor3f(combinatieCuloare[0], combinatieCuloare[1], combinatieCuloare[2]);
        gl.glVertex2f(cordonate[0], cordonate[3]);
        gl.glEnd();
    }

    public float[] culoareCorecta(int numarulPatratului) {
        String culoare = null;
        switch (numarulPatratului) {
            case 1:
                culoare = "alb";
                break;
            case 2:
                culoare = "verde";
                break;
            case 3:
                culoare = "albastru";
                break;

        }
        return culoare(culoare);
    }

    public float[] culoareAleasa() {

        return culoare("alb");
    }

    public float[] culoare(String culoare) {
        float[] combinatieCuloare;
        switch (culoare) {
            case "rosu":
                combinatieCuloare = new float[]{1, 0, 0};
                break;

            case "galben":
                combinatieCuloare = new float[]{1, 1, 0};
                break;

            case "verde":
                combinatieCuloare = new float[]{0.5f, 0.75f, 0};
                break;
            case "albastru":
                combinatieCuloare = new float[]{0, 0, 1};
                break;
            case "alb":
                combinatieCuloare = new float[]{1, 1, 1};
                break;
            case "gri":
                combinatieCuloare = new float[]{0.5f, 0.5f, 0.5f};
                break;
            default:
                combinatieCuloare = new float[]{0, 0, 0};
                break;
        }

        return combinatieCuloare;
    }

    public String culoareRandom() {
        List<String> list = new ArrayList<String>();
        list.add("rosu");
        list.add("galben");
        list.add("verde");
        list.add("albastru");
        list.add("alb");

        Random randomizer = new Random();
        String random = list.get(randomizer.nextInt(5));
        System.out.println(random);
        return random;
    }

    public void display(GLAutoDrawable canvas) {
        GL2 gl = canvas.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        //patratele principale unde sunt culorile pe care trebie sa le ghicim

        construirePoligon(culoare("alb"), new float[]{1, 4.9f, 1, 5});
        construirePoligon(culoare("alb"), new float[]{5, 8.9f, 5, 1});
        construirePoligon(culoare("alb"), new float[]{0.9f, -3, 1, 5});

        //zona unde sunt incercarile de pana acum

        construirePoligon(culoare("gri"), new float[]{-9, -7, 8, -7});

        //combinatia de culori incercata
        int i = 0;
        while (i < 15) {
            float[] culoare = new float[]{1, 1, 1};
            cordonateRaspunsuriGresite(culoareAleasa(), i, 0);
            if (culoareAleasa() == culoareCorecta(1))
                cordonateRaspunsuriCorecte(culoare("verde"), i, 0);
            else
                cordonateRaspunsuriCorecte(culoare("rosu"), i, 0);

            cordonateRaspunsuriGresite(culoareAleasa(), i, 0.5f);
            if (culoareAleasa() == culoareCorecta(1))
                cordonateRaspunsuriCorecte(culoare("verde"), i, 0.5f);
            else
                cordonateRaspunsuriCorecte(culoare("rosu"), i, 0.5f);

            cordonateRaspunsuriGresite(culoareAleasa(), i, 1);
            if (culoareAleasa() == culoareCorecta(1))
                cordonateRaspunsuriCorecte(culoare("verde"), i, 1);
            else
                cordonateRaspunsuriCorecte(culoare("rosu"), i, 1);
            i++;
        }

        //culorile din care trebuie sa alegem

        construirePoligon(new float[]{1, 1, 1}, new float[]{-5, -2, -8, -5});
        construirePoligon(new float[]{0, 1, 1}, new float[]{-2, 1, -8, -5});
        construirePoligon(new float[]{0, 0, 1}, new float[]{1, 4, -8, -5});
        construirePoligon(new float[]{1, 0, 1}, new float[]{4, 7, -8, -5});
        construirePoligon(new float[]{1, 1, 0}, new float[]{7, 10, -8, -5});
        construirePoligon(new float[]{1, 0, 0}, new float[]{10, 13, -8, -5});


    }


    public void reshape(GLAutoDrawable canvas, int left, int top, int width, int height) {
        GL2 gl = canvas.getGL().getGL2();

        // Selecting the viewport -- the display area -- to be the entire widget.
        gl.glViewport(0, 0, width, height);

        // Determining the width to height ratio of the widget.
        double ratio = (double) width / (double) height;

        // Selecting the projection matrix.
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);

        gl.glLoadIdentity();

        // Selecting the view volume to be x from 0 to 1, y from 0 to 1, z from -1 to 1.
        // But we are careful to keep the aspect ratio and enlarging the width or the height.
        if (ratio < 1)
            gl.glOrtho(-v_size, v_size, -v_size, v_size / ratio, -1, 1);
        else
            gl.glOrtho(-v_size, v_size * ratio, -v_size, v_size, -1, 1);

        // Selecting the modelview matrix.
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
    }

    public void displayChanged(GLAutoDrawable canvas, boolean modeChanged, boolean deviceChanged) {
        return;
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
        // TODO Auto-generated method stub
    }
}
