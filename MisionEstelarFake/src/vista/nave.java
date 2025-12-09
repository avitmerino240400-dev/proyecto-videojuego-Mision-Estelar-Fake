
package vista;


import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import javax.swing.ImageIcon;

public class nave {

    public int x, y;
    private ImageIcon sprite;

    // Movimiento
    private boolean moviendoArriba = false;
    private boolean moviendoAbajo  = false;

    private final int SPEED = 4;
    private final int TOP_LIMIT = 20;
    private final int BOTTOM_LIMIT = 250;

   
    private Juego juego;

    public nave(Juego juego) {
        this.juego = juego;
        sprite = new ImageIcon(getClass().getResource("/recursos/navechila.png"));
        x = 20;
        y = 150;
    }

    public void paint(Graphics2D g) {
        // Dibujar nave
        g.drawImage(sprite.getImage(), x, y, 80, 60, null);
       
    }

  
    public synchronized void update() {

        // Movimiento arriba/abajo
        if (moviendoArriba && !moviendoAbajo) {
            if (y > TOP_LIMIT) y -= SPEED;
        }
        if (moviendoAbajo && !moviendoArriba) {
            if (y < BOTTOM_LIMIT) y += SPEED;
        }
    }

    // INPUT 
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                moviendoArriba = true;
                break;

            case KeyEvent.VK_DOWN:
                moviendoAbajo = true;
                break;

            case KeyEvent.VK_SPACE:
                // delega el disparo a Shoot 
                if (juego != null && juego.tiro != null) {
                    juego.tiro.intentDisparar();
                }
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                moviendoArriba = false;
                break;

            case KeyEvent.VK_DOWN:
                moviendoAbajo = false;
                break;
        }
    }

    public void reiniciar() {
        x = 20;
        y = 150;
        moviendoArriba = false;
        moviendoAbajo  = false;
    }

    public Rectangle2D getBoundsMario() {
        return new Rectangle2D.Double(x, y, 70, 50);
    }
}