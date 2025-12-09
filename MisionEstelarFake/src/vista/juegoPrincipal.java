package vista;

import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static vista.Juego.muerto;


public class juegoPrincipal extends Thread {

    ImageIcon raw = new ImageIcon(getClass().getResource("/recursos/navedestruida1175.png"));
    Image scaled = raw.getImage().getScaledInstance(80, 60, Image.SCALE_SMOOTH);
    ImageIcon icon = new ImageIcon(scaled);
   
    public void run(){

        JFrame ventana = new JFrame("MISION ESTELAR");
        Juego game = new Juego();
        ventana.add(game);
        ventana.setSize(613, 350);
        ventana.setVisible(true);
        ventana.setResizable(false);
        ventana.setLocationRelativeTo(null);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setIconImage(new ImageIcon(getClass().getResource("/recursos/meteoroLogo.png")).getImage());
        
        while (true) {

            if (muerto) {
                Juego.crash.play();
                String options[] = {"Juego nuevo", "Salir"};
                int x = JOptionPane.showOptionDialog(null, "Perdiste todas tus vidas\n", "GAME OVER", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, icon, options, options[0]);

                if (x == 0) {
                    game.reiniciaJuego();
                } else if (x == 1){
                    System.exit(0);
                }
            }

         
            game.nave.update();

            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
            }

            game.repaint();
        }
    }
}

