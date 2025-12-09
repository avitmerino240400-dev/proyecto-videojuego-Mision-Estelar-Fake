package vista;

import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import javax.swing.ImageIcon;

public class Shoot extends Thread {

    // meteoros 
    public int x1 = -200, y1 = 180;
    public int x2 = -300, y2 = 180;
    public int x3 = -400, y3 = 180;

    // flags de zigzag
    boolean zig1 = false, zig2 = false;
    int vy1 = 0, vy2 = 0;

    // juego y estado
    public int vidas = 5;
    public int puntaje = 0;
    public int nivel = 1;
    public Juego j;
    Random r = new Random();

    // imagenes
    ImageIcon imgNormal = new ImageIcon(getClass().getResource("/recursos/fuego_chico.png"));
    ImageIcon imgThird  = new ImageIcon(getClass().getResource("/recursos/fuego_chicoA.png")); 
    ImageIcon imgBullet = new ImageIcon(getClass().getResource("/recursos/fuego_chicoV.png"));
    ImageIcon imgBala = new ImageIcon(getClass().getResource("/recursos/balaverde.png"));
    
    // balas 
    private final int MAX_BULLETS = 5;
    private int bulletsInReserve = MAX_BULLETS;
    private long lastReloadTime = System.currentTimeMillis();
    private final long RELOAD_MS = 10_000L; // 10 segundos

    // pool de balas activas
    private static class Bullet {
        int x = -500, y = -500;
        boolean active = false;
    }
    private final Bullet[] pool = new Bullet[]{ new Bullet(), new Bullet(), new Bullet(), new Bullet() };
    private final int BULLET_SPEED2 = 11;

    // tamaños
    private final int MW = 20, MH = 15;
    private final int BW = 30, BH = 20;

    public Shoot(Juego juego) {
        this.j = juego;
        x1 = -200; x2 = -350; x3 = -520;
    }

    // sonido
    private void playSound(String path) {
        try {
            var url = getClass().getResource(path);
            var audio = javax.sound.sampled.AudioSystem.getAudioInputStream(url);
            var clip = javax.sound.sampled.AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // disparo
    public void intentDisparar() {
        if (bulletsInReserve <= 0) return;
        for (Bullet b : pool) {
            if (!b.active) {
                b.x = j.nave.x + 100;
                b.y = j.nave.y + 40;
                b.active = true;

           
                 playSound("/recursos/sonidobala111.wav");

                bulletsInReserve--;
                if (bulletsInReserve == 0) lastReloadTime = System.currentTimeMillis();
                break;
            }
        }
    }

    // dubujos
    public void paint(Graphics2D g) {
        if (nivel >= 1 && x1 > -100) {
            g.drawImage(imgNormal.getImage(), x1, y1, MW, MH, null);
        }
        if (nivel >= 2 && x2 > -100) {
            g.drawImage(imgNormal.getImage(), x2, y2, MW, MH, null);
        }
        if (nivel >= 3 && x3 > -100) {
            g.drawImage(imgThird.getImage(), x3, y3, MW, MH, null);
        }

        for (Bullet b : pool) {
            if (b.active) {
                g.drawImage(imgBala.getImage(), b.x, b.y, BW, BH, null);
            }
        }
    }

    // spawm de meteoros
    private void spawn(int which) {
        int baseX = 620 + r.nextInt(240);
        int y = randomY();
        switch (which) {
            case 1 -> { x1 = baseX; y1 = y; zig1 = false; vy1 = 0; }
            case 2 -> { x2 = baseX; y2 = y; zig2 = false; vy2 = 0; }
            case 3 -> { x3 = baseX; y3 = y; }
        }
    }

    private int randomY() {
        int[] ys = new int[]{
            40,70,100,130,150,170,190,210,230,250,270,290
        };
        return ys[r.nextInt(ys.length)];
    }

    // la hit
    private void handleHit(int which) {
        switch (which) {
            case 1 -> { puntaje++; x1 = -400 - r.nextInt(300); }
            case 2 -> { puntaje++; x2 = -400 - r.nextInt(300); }
            case 3 -> { puntaje++; x3 = -400 - r.nextInt(300); }
        }
    }

    //movilidad
    public void mover() {

        if (bulletsInReserve < MAX_BULLETS) {
            long now = System.currentTimeMillis();
            if (now - lastReloadTime >= RELOAD_MS) {
                bulletsInReserve = MAX_BULLETS;
                lastReloadTime = now;
            }
        }

        Area personaje = new Area(j.nave.getBoundsMario());
        personaje.intersect(getBoundsShoot());

        if (!personaje.isEmpty()) {
               playSound("/recursos/explosionnave11.wav");
            vidas--;
            x1 = x2 = x3 = -400;
            if (vidas <= 0) j.muerto = true;
        }

        // niveles
        if (puntaje >= 10 && nivel < 2) nivel = 2;
        if (puntaje >= 20 && nivel < 3) nivel = 3;
        if (puntaje >= 40 && nivel < 4) nivel = 4;

        int baseSpeed = 4 + (nivel - 1);
        int baseSpeed2 = 6 + (nivel - 1);

        // METEORO 1
        if (nivel >= 1) {
            if (x1 <= -100) {
                spawn(1);
            } else {
                int prevX = x1;
                x1 -= baseSpeed;

                if (prevX > -100 && x1 <= -100) puntaje++;

                if (!zig1 && x1 <= 350) {
                    zig1 = true;
                    vy1 = (r.nextInt(13) - 6);
                    if (vy1 == 0) vy1 = 4;
                }
                if (zig1) {
                    y1 += vy1;
                    if (y1 < 120) { y1 = 120; vy1 = -vy1; }
                    if (y1 > 300) { y1 = 300; vy1 = -vy1; }
                    if (r.nextInt(100) < 10) vy1 = (r.nextInt(13) - 6);
                }
            }
        }

        // METEORO 2
        if (nivel >= 2) {
            if (x2 <= -100) {
                spawn(2);
            } else {
                int prevX = x2;
                x2 -= baseSpeed + r.nextInt(2);

                if (prevX > -100 && x2 <= -100) puntaje++;

                if (!zig2 && x2 <= 340) {
                    zig2 = true;
                    vy2 = (r.nextInt(13) - 6);
                    if (vy2 == 0) vy2 = -5;
                }
                if (zig2) {
                    y2 += vy2;
                    if (y2 < 120) { y2 = 120; vy2 = -vy2; }
                    if (y2 > 300) { y2 = 300; vy2 = -vy2; }
                    if (r.nextInt(100) < 12) vy2 = (r.nextInt(13) - 6);
                }
            }
        }

        // METEORO 3 
        if (nivel >= 3) {
            if (x3 <= -100) {
                spawn(3);
            } else {
                int prevX = x3;
                x3 -= baseSpeed2 + r.nextInt(3);

                if (prevX > -100 && x3 <= -100) puntaje++;
            }
        }

        // BALAS
        for (Bullet b : pool) {
            if (b.active) {
                b.x += BULLET_SPEED2;

                // meteoro 1 hit
                if (x1 > -100) {
                    Rectangle2D rB = new Rectangle2D.Double(b.x, b.y, BW, BH);
                    Ellipse2D e1 = new Ellipse2D.Double(x1, y1, MW, MH);
                    if (rB.intersects(e1.getBounds2D())) {

                        playSound("/recursos/impactometeoro11.wav");

                        b.active = false;
                        handleHit(1);
                        continue;
                    }
                }

                // meteoro 2 hit
                if (x2 > -100) {
                    Rectangle2D rB = new Rectangle2D.Double(b.x, b.y, BW, BH);
                    Ellipse2D e2 = new Ellipse2D.Double(x2, y2, MW, MH);
                    if (rB.intersects(e2.getBounds2D())) {

                        playSound("/recursos/impactometeoro11.wav");

                        b.active = false;
                        handleHit(2);
                        continue;
                    }
                }

                // meteoro 3 hit
                if (x3 > -100) {
                    Rectangle2D rB = new Rectangle2D.Double(b.x, b.y, BW, BH);
                    Ellipse2D e3 = new Ellipse2D.Double(x3, y3, MW, MH);
                    if (rB.intersects(e3.getBounds2D())) {

                        playSound("/recursos/impactometeoro11.wav");

                        b.active = false;
                        handleHit(3);
                        continue;
                    }
                }

                // bala fuera de pantalla
                if (b.x > j.getWidth() + 50) {
                    b.active = false;
                }
            }
        }
    }

    //BOUNDING 
    public Area getBoundsShoot() {
        Area total = new Area();
        if (x1 > -100) total.add(new Area(new Ellipse2D.Double(x1, y1, MW, MH)));
        if (x2 > -100) total.add(new Area(new Ellipse2D.Double(x2, y2, MW, MH)));
        if (x3 > -100) total.add(new Area(new Ellipse2D.Double(x3, y3, MW, MH)));
        return total;
    }

    // GETTERS
    public int obtenerPuntaje() { return puntaje; }
    public int obtenerVidas() { return vidas; }
    public int obtenerBalasEnReserva() { return bulletsInReserve; }
}
