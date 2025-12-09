package vista;

import java.awt.Graphics2D;
import javax.swing.ImageIcon;

public class Fondo extends javax.swing.JPanel {

    private Juego myGame;

 
    private final ImageIcon imagenFondo =
            new ImageIcon(getClass().getResource("/recursos/fondoespacio.gif"));

    // tamaño del fondo
    private final int anchoFondo = 613;
    private final int altoFondo = 350;

    // posiciones para scroll 
    private int y1 = 0;
    private int y2 = -altoFondo;

    public Fondo(Juego myGame) {
        this.myGame = myGame;
        this.setSize(anchoFondo, altoFondo);
        this.setName("Fondo");
    }

  

    //fonodo puntaje vidas balas
   
    public void paint(Graphics2D g) {

       
        g.drawImage(imagenFondo.getImage(), 0, y1, anchoFondo, altoFondo, null);
        g.drawImage(imagenFondo.getImage(), 0, y2, anchoFondo, altoFondo, null);

    }
}
