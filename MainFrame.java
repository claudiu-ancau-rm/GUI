import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainFrame
        extends JFrame
        implements GLEventListener {

    private GLCanvas canvas;
    private Animator animator;
    private final int NO_TEXTURES = 1;
    private int texture[] = new int[NO_TEXTURES];
    TextureReader.Texture[] tex = new TextureReader.Texture[NO_TEXTURES];
    private GLU glu;
    String PRIMA_CULOARE;
    String CULOAREA_DOI;
    String CULOAREA_TREI;
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
        PRIMA_CULOARE = culoareRandom();
        CULOAREA_DOI = culoareRandom();
        CULOAREA_TREI = culoareRandom();
        GL2 gl = canvas.getGL().getGL2();
        // Create a new GLU object.
        glu = GLU.createGLU();

        // Generate a name (id) for the texture.
        // This is called once in init no matter how many textures we want to generate in the texture vector
        gl.glGenTextures(NO_TEXTURES, texture, 0);

        // Define the filters used when the texture is scaled.
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);

        // Do not forget to enable texturing.
        gl.glEnable(GL.GL_TEXTURE_2D);

        // The following lines are for creating ONE texture
        // If you want TWO textures modify NO_TEXTURES=2 and copy-paste again the next lines of code
        // up until (and including) this.makeRGBTexture(...)
        // Modify texture[0] and tex[0] to texture[1] and tex[1] in the new code and that's it

        // Bind (select) the texture.
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture[0]);

        // Read the texture from the image.
        try {
            tex[0] = TextureReader.readTexture("a.PNG");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        // Construct the texture and use mipmapping in the process.
        this.makeRGBTexture(gl, glu, tex[0], GL.GL_TEXTURE_2D, true);
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

    //functia care creaza poligoanele pentru verificarea raspunsurilor corecte
    public void cordonateRaspunsuriGresite(float[] combinatieCuloare, float numarIncercare, float variatiaX) {
        float[] cordonate = new float[]{-8.8f + variatiaX, -8.5f + variatiaX, 7.8f - numarIncercare, 7.5f - numarIncercare};
        construirePoligon(combinatieCuloare, cordonate);
    }

    //functia care creaza poligoanele pentru verificarea raspunsurilor corecte
    public void cordonateRaspunsuriCorecte(float[] combinatieCuloare, float numarIncercare, float variatiaX) {
        float[] cordonate = new float[]{-8.8f + variatiaX, -8.5f + variatiaX, 7.4f - numarIncercare, 7.2f - numarIncercare};
        construirePoligon(combinatieCuloare, cordonate);
    }

    //functia care construieste un poligon si il coloreaza
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

    //un strin cu numele celor 3 culori care trebuie ghicite
    public String[] culoareCorecta() {
        String[] listaCuloriCorecte = new String[]{PRIMA_CULOARE, CULOAREA_DOI, CULOAREA_TREI};
        return listaCuloriCorecte;
    }

    //reprezinta culoarea aleasa, acesta functie va primi rezultatul de la eventul de dat click pe una din cele 6 culori
    public String culoareAleasa() {

        return PRIMA_CULOARE;
    }

    //definesc culorile
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
            case "orange":
                combinatieCuloare = new float[]{1, 0.5f, 0};
                break;
            case "mov":
                combinatieCuloare = new float[]{0, 0, 0.5f};
                break;
            default:
                combinatieCuloare = new float[]{0, 0, 0};
                break;
        }

        return combinatieCuloare;
    }

    //genereaza o culoare random, deocamdata nefunctional
    public String culoareRandom() {
        List<String> list = new ArrayList<String>();
        list.add("rosu");
        list.add("galben");
        list.add("verde");
        list.add("albastru");
        list.add("orange");
        list.add("gri");
        Random randomizer = new Random();
        String random = list.get(randomizer.nextInt(list.size()));
        return random;
    }

    public void display(GLAutoDrawable canvas) {
        GL2 gl = canvas.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        //patratele principale unde sunt culorile pe care trebie sa le ghicim
        construirePoligon(culoare(culoareCorecta()[0]), new float[]{0.9f, -3, 1, 5});
        construirePoligon(culoare(culoareCorecta()[1]), new float[]{1, 4.9f, 1, 5});
        construirePoligon(culoare(culoareCorecta()[2]), new float[]{5, 8.9f, 5, 1});


        //zona unde sunt incercarile de pana acum
        construirePoligon(culoare("gri"), new float[]{-9, -7, 8, -7});

        //combinatia de culori incercata
        int i = 0;
        while (i < 15) {
            cordonateRaspunsuriGresite(culoare(culoareAleasa()), i, 0);
            if (culoareAleasa() == culoareCorecta()[0])
                cordonateRaspunsuriCorecte(culoare("verde"), i, 0);
            else
                cordonateRaspunsuriCorecte(culoare("rosu"), i, 0);

            cordonateRaspunsuriGresite(culoare(culoareAleasa()), i, 0.5f);
            if (culoareAleasa() == culoareCorecta()[1])
                cordonateRaspunsuriCorecte(culoare("verde"), i, 0.5f);
            else
                cordonateRaspunsuriCorecte(culoare("rosu"), i, 0.5f);

            cordonateRaspunsuriGresite(culoare(culoareAleasa()), i, 1);
            if (culoareAleasa() == culoareCorecta()[2])
                cordonateRaspunsuriCorecte(culoare("verde"), i, 1);
            else
                cordonateRaspunsuriCorecte(culoare("rosu"), i, 1);
            i++;
        }

        //culorile din care trebuie sa alegem
        construirePoligon(culoare("rosu"), new float[]{-5, -2.1f, -8, -5});
        construirePoligon(culoare("verde"), new float[]{-2, 0.9f, -8, -5});
        construirePoligon(culoare("galben"), new float[]{1, 3.9f, -8, -5});
        construirePoligon(culoare("albastru"), new float[]{4, 6.9f, -8, -5});
        construirePoligon(culoare("orange"), new float[]{7, 9.9f, -8, -5});
        construirePoligon(culoare("mov"), new float[]{10, 12.9f, -8, -5});


    }

    private void makeRGBTexture(GL gl, GLU glu, TextureReader.Texture img, int target, boolean mipmapped) {
        if (mipmapped) {
            glu.gluBuild2DMipmaps(target, GL.GL_RGB8, img.getWidth(), img.getHeight(), GL.GL_RGB, GL.GL_UNSIGNED_BYTE, img.getPixels());
        } else {
            gl.glTexImage2D(target, 0, GL.GL_RGB, img.getWidth(), img.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, img.getPixels());
        }
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
