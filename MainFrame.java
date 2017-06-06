import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainFrame
        extends JFrame
        implements GLEventListener, MouseListener,
        MouseMotionListener {

    private GLCanvas canvas;
    private Animator animator;
    private GLU glu;
    private GLUT glut;
    String PRIMA_CULOARE;
    String CULOAREA_DOI;
    String CULOAREA_TREI;
    private int mouseX, mouseY;
    String[] culoriAlese = {"", "", ""};
    int nrIncercari = 0;
    String[][] incercari = {{"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}};
    String[][] corect = {{"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}};
    private TextureHandler texture1, texture2, texture3;


    // For specifying the positions of the clipping planes (increase/decrease the distance) modify this variable.
    // It is used by the glOrtho method.
    private double v_size = 10.0;

    // Application main entry point
    public static void main(String args[]) {
        new MainFrame();
    }

    // Default constructor
    public MainFrame() {
        super("Color Breaker");

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
        this.canvas.addMouseListener(this);
        this.canvas.addMouseMotionListener(this);


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
        culoriAlese[0] = "alb";
        culoriAlese[1] = "alb";
        culoriAlese[2] = "alb";
        nrIncercari = 0;
        GL2 gl = canvas.getGL().getGL2();
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 3; j++) {
                incercari[i][j] = "gri";
                corect[i][j] = "gri";
            }
        }
        // Create a new GLU object.
        glu = GLU.createGLU();
        glut = new GLUT();
        texture1 = new TextureHandler(gl, glu, "texture1.jpg", true);
        texture2 = new TextureHandler(gl, glu, "texture2.jpg", true);
        texture3 = new TextureHandler(gl, glu, "texture2.jpg", true);
        // Setting the clear color -- the color which will be used to erase the canvas.
        gl.glClearColor(0, 0.5f, 0.5f, 0);

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
        gl.glTexCoord2d(0, 0);
        gl.glVertex2f(cordonate[0], cordonate[2]);
        gl.glColor3f(combinatieCuloare[0], combinatieCuloare[1], combinatieCuloare[2]);
        gl.glTexCoord2d(1, 0);
        gl.glVertex2f(cordonate[1], cordonate[2]);
        gl.glColor3f(combinatieCuloare[0], combinatieCuloare[1], combinatieCuloare[2]);
        gl.glTexCoord2d(1, 1);
        gl.glVertex2f(cordonate[1], cordonate[3]);
        gl.glColor3f(combinatieCuloare[0], combinatieCuloare[1], combinatieCuloare[2]);
        gl.glTexCoord2d(0, 1);
        gl.glVertex2f(cordonate[0], cordonate[3]);
        gl.glEnd();
    }

    //definesc culorile pe care le folosesc
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

    //genereaza o culoare random
    public String culoareRandom() {
        List<String> list = new ArrayList<String>();
        list.add("rosu");
        list.add("galben");
        list.add("verde");
        list.add("albastru");
        list.add("orange");
        list.add("mov");
        Random randomizer = new Random();
        String random = list.get(randomizer.nextInt(list.size()));
        return random;
    }

    private void construireScena(String[] culoriAlese, String[][] incercari, String[][] corect, TextureHandler texture) {
        GL2 gl = canvas.getGL().getGL2();
        GLUT verifica = new GLUT();
        //patratele principale unde sunt culorile pe care trebie sa le ghicim
        texture.bind();
        texture.enable();
        construirePoligon(culoare(culoriAlese[0]), new float[]{-3, 0.9f, 1, 5});
        construirePoligon(culoare(culoriAlese[1]), new float[]{1, 4.9f, 1, 5});
        construirePoligon(culoare(culoriAlese[2]), new float[]{5, 8.9f, 1, 5});
        texture.disable();
        //zona unde sunt incercarile de pana acum
        construirePoligon(culoare("gri"), new float[]{-9, -7, 8, -7});
        int i = 0;
        while (i < 15) {
            cordonateRaspunsuriGresite(culoare(incercari[i][0]), i, 0);
            cordonateRaspunsuriCorecte(culoare(corect[i][0]), i, 0);

            cordonateRaspunsuriGresite(culoare(incercari[i][1]), i, 0.5f);
            cordonateRaspunsuriCorecte(culoare(corect[i][1]), i, 0.5f);

            cordonateRaspunsuriGresite(culoare(incercari[i][2]), i, 1);
            cordonateRaspunsuriCorecte(culoare(corect[i][2]), i, 1);

            i++;
        }

        //butonul de verificare
        construirePoligon(culoare("gri"), new float[]{9.8f, 12.5f, 3.5f, 2});

        gl.glColor3f(0, 0, 1);
        gl.glRasterPos2d(10, 2.5);
        verifica.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_24, "Verifica");

        //culorile din care trebuie sa alegem
        construirePoligon(culoare("rosu"), new float[]{-5, -2.1f, -8, -5});
        construirePoligon(culoare("verde"), new float[]{-2, 0.9f, -8, -5});
        construirePoligon(culoare("galben"), new float[]{1, 3.9f, -8, -5});
        construirePoligon(culoare("albastru"), new float[]{4, 6.9f, -8, -5});
        construirePoligon(culoare("orange"), new float[]{7, 9.9f, -8, -5});
        construirePoligon(culoare("mov"), new float[]{10, 12.9f, -8, -5});
    }

    //verifica combinatia de culori alese
    public void verificareCulori() {
        int x = 0;
        GL2 gl = canvas.getGL().getGL2();
        incercari[nrIncercari][0] = culoriAlese[0];
        incercari[nrIncercari][1] = culoriAlese[1];
        incercari[nrIncercari][2] = culoriAlese[2];
        if (culoriAlese[0].equals(PRIMA_CULOARE)) {
            x++;
            corect[nrIncercari][0] = "verde";
        } else
            corect[nrIncercari][0] = "rosu";
        if (culoriAlese[1].equals(CULOAREA_DOI)) {
            x++;
            corect[nrIncercari][1] = "verde";
        } else
            corect[nrIncercari][1] = "rosu";
        if (culoriAlese[2].equals(CULOAREA_TREI)) {
            x++;
            corect[nrIncercari][2] = "verde";
        } else
            corect[nrIncercari][2] = "rosu";
        construireScena(culoriAlese, incercari, corect, texture1);
        if (x == 3) {
            gl.glRasterPos2d(0, -0.5);
            glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_24, "Ai castigat");

        } else {
            nrIncercari++;
            culoriAlese[0] = "alb";
            culoriAlese[1] = "alb";
            culoriAlese[2] = "alb";
            if (nrIncercari == 15) {
                gl.glRasterPos2d(0, -0.5);
                glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_24, "Ai pierdut");
            }
        }
    }

    public void alegeCuloare(int k) {
        if (this.mouseX > 166 && this.mouseX < 254 && this.mouseY > 423 && this.mouseY < 501) {
            culoriAlese[k] = "rosu";
            construireScena(culoriAlese, incercari, corect, texture2);
        }
        if (this.mouseX > 264 && this.mouseX < 352 && this.mouseY > 423 && this.mouseY < 501) {

            culoriAlese[k] = "verde";
            construireScena(culoriAlese, incercari, corect, texture2);
        }
        if (this.mouseX > 360 && this.mouseX < 451 && this.mouseY > 423 && this.mouseY < 501) {

            culoriAlese[k] = "galben";
            construireScena(culoriAlese, incercari, corect, texture2);
        }
        if (this.mouseX > 459 && this.mouseX < 551 && this.mouseY > 423 && this.mouseY < 501) {

            culoriAlese[k] = "albastru";
            construireScena(culoriAlese, incercari, corect, texture2);
        }
        if (this.mouseX > 557 && this.mouseX < 650 && this.mouseY > 423 && this.mouseY < 501) {

            culoriAlese[k] = "orange";
            construireScena(culoriAlese, incercari, corect, texture2);
        }
        if (this.mouseX > 657 && this.mouseX < 744 && this.mouseY > 423 && this.mouseY < 501) {

            culoriAlese[k] = "mov";
            construireScena(culoriAlese, incercari, corect, texture2);
        }
    }

    public void display(GLAutoDrawable canvas) {
        GL2 gl = canvas.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        construireScena(culoriAlese, incercari, corect, texture1);
        if (this.mouseX > 649 && this.mouseX < 733 && this.mouseY > 184 && this.mouseY < 221) {

            if (culoriAlese[0].equals("alb") || culoriAlese[1].equals("alb") || culoriAlese[2].equals("alb")) {

                gl.glRasterPos2d(0, -0.5);
                glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_24, "Trebuie sa alegi 3 culori");
            } else {
                verificareCulori();
                culoriAlese[0] = "alb";
                culoriAlese[1] = "alb";
                culoriAlese[2] = "alb";

            }
        }
        if (culoriAlese[0] == "alb") {
            alegeCuloare(0);
        } else if (culoriAlese[1] == "alb") {
            alegeCuloare(1);
        } else if (culoriAlese[2] == "alb") {
            alegeCuloare(2);
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

    @Override
    public void dispose(GLAutoDrawable arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent me) {
        mouseX = me.getX();
        mouseY = me.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseX = 0;
        mouseY = 0;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
