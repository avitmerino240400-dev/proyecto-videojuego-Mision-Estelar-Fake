
package vista;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.FloatControl;
import javax.swing.JPanel;

      
  public class Juego extends JPanel implements Runnable { 

    URL direccionMusica, direccionCrash;
    static AudioClip musicaFondo, crash;
    public static boolean muerto = false;

    public Shoot tiro;
    public nave nave;
    Fondo fondo;

    private Thread gameThread;
    private boolean running = false;
    private FloatControl volumenMusica;
    private FloatControl volumenCrash;
    
    
    
    public Juego() {
   
        this.tiro = new Shoot(this);
        this.nave = new nave(this);
        this.fondo = new Fondo(this);
        try {
            direccionMusica = getClass().getResource("/recursos/ambiente1175.wav");
            musicaFondo = Applet.newAudioClip(direccionMusica);
            musicaFondo.loop();
                    
            direccionCrash = getClass().getResource("/recursos/errorgameover11.wav");
            crash = Applet.newAudioClip(direccionCrash);
         
        } catch (Exception e) {}

    
        setFocusable(true);
        addKeyListener(new KeyListener() {
            @Override public void keyTyped(KeyEvent e) {}
            @Override public void keyPressed(KeyEvent e) { nave.keyPressed(e); }
            @Override public void keyReleased(KeyEvent e) { nave.keyReleased(KeyEvent.VK_UP==e.getKeyCode()? e : e); nave.keyReleased(e); }
        });

        iniciarJuego();
    }

    public void iniciarJuego() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
        requestFocusInWindow();
    }

    @Override
    public void run() {
        final int FPS = 60;
        final long frameTime = 1000 / FPS;

        while (running) {
            long start = System.currentTimeMillis();

            actualizar();
            repaint();

            long delta = System.currentTimeMillis() - start;
            long sleep = frameTime - delta;
            if (sleep < 2) sleep = 2;
            try { Thread.sleep(sleep); } catch (InterruptedException ex) {}
        }
    }

    public void actualizar() {
        if (!muerto) {
            nave.update();
            tiro.mover();
        } else {
           
        }
    }

    // dibujar puntaje, vidas, balas en pantalla
    public void dibujarPuntaje(Graphics2D g) {
        Font score = new Font("console", Font.BOLD, 18);
        g.setFont(score);
        g.setColor(Color.darkGray);
        g.drawString("Vidas: " + tiro.obtenerVidas()
                + "   Puntaje: " + tiro.obtenerPuntaje()
                + "   Balas: " + tiro.obtenerBalasEnReserva(), 10, 30);
    }

    public void reiniciaJuego() {
        tiro.x1 = -200; tiro.y1 = 180;
        tiro.x2 = -300; tiro.y2 = 180;
        tiro.x3 = -400; tiro.y3 = 180;
        nave.reiniciar();
        muerto = false;
        tiro.puntaje = 1;
        tiro.vidas = 3;
        tiro.nivel = 1;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
       
        fondo.paint((Graphics2D) g);
        dibujarPuntaje((Graphics2D) g);
        tiro.paint((Graphics2D) g);
        nave.paint((Graphics2D) g);
    }
}