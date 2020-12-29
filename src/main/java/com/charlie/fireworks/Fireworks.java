package com.charlie.fireworks;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class Fireworks extends JPanel{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static int height = 1000;
    private static int width = 1000;
    static final int MAX_TRKS = 1000;
    static final int MAX_HT = 100000;
    static final long MAX_RANGE = 500000;
    static final long MIN_RANGE = 100;
    static final long MAX_VEL = 10000;
    static final long MIN_VEL = 0;
    static final long LONG_RANGE = MAX_RANGE;
    static final long MEDIUM_RANGE = MAX_RANGE/2;
    static final long SHORT_RANGE = MAX_RANGE/4;
    static final int HISTORY = 6;
    static int newest = -1;
    static long scale = LONG_RANGE;
    static List<Explosion> explosions = new ArrayList<Explosion>();
    static MyThread t;
    static long deltatms = 1000;
    static Point mousep = null;
    static Integer stickyid = null;

    JButton go = new JButton("go");
    JFrame jfrm = new JFrame("Fireworks");
    static int count2 = 0;
    static BufferedImage im = null;
    static BufferedImage im2 = null;
    static BufferedImage im3 = null;
 
    static Fireworks pe;

    /**
     * This is for the animation of the explosions
     * 
     * @author charl
     *
     */
    class Explosion {
        private static final int NO_POINTS = 200;
        private double x[] = null;
        private double y[] = null;
        private double dx[] = null;
        private double dy[] = null;
        private int count = 255;
        int red = (int)Math.round(Math.random()*255.0);
        int green = (int)Math.round(Math.random()*255.0);
        int blue = (int)Math.round(Math.random()*255.0);

        public Explosion(int x, int y) {
            this.x = new double[NO_POINTS];
            this.y = new double[NO_POINTS];
            this.dx = new double[NO_POINTS];
            this.dy = new double[NO_POINTS];
            for (int i = 0; i < NO_POINTS;i++) {
                this.x[i]=x;
                this.y[i] =y;
                double bearing = 2.0 * Math.PI * Math.random();
                double speed = Math.random()*5.0;
                this.dx[i] = speed*Math.sin(bearing);;
                this.dy[i] = speed*Math.cos(bearing);
            }
        }

        void draw(Graphics g) {
            Color c = new Color(red*count/255,green*count/255,blue*count--/255);
            g.setColor(c);
            for (int i = 0; i < NO_POINTS;i++) {
                g.fillRect((int)x[i], (int)y[i], 4,4);
                this.x[i]+=dx[i];
                this.y[i]+=dy[i];
            }
        }
        public int getCount() {
            return count;
        }

    }

    class MyThread extends Thread{
        @Override
        public void run() {
            while(true) {
                if (count2 % 50 == 0) {
                    Explosion e = new Explosion((int)Math.round(Math.random()*Fireworks.width),
                            (int)Math.round(Math.random()*Fireworks.height));
                    explosions.add(e);
                }
                try {
                    t.sleep(20);
                } catch (InterruptedException e) {
                    // e.printStackTrace();
                }
                pe.repaint();
                count2++;

            }
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int height = getHeight();
        int width = getWidth();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        g.drawImage(im, 1000-count2, 0, null);
        g.drawImage(im2, 2500-count2, 0, null);
        g.drawImage(im3, 4000-count2, 0, null);
        if(explosions.size() > 0) {
            int i = 0;
            for (; i < explosions.size();i++) {
                Explosion e = explosions.get(i);
                e.draw(g);
                if(e.getCount() == 0) {
                    explosions.remove(e);
                    i--;
                }
            }
        }

    }
    class PaintDemo{

        PaintDemo(){
            jfrm.setSize(width, height);
            jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


            jfrm.setLayout(new BorderLayout());

            JPanel temp = new JPanel();
            go.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (count2 > 0) {
                        count2 = 0;
                    }else {
                        t = pe.new MyThread();
                        t.start();     
                    }
                 }

            });
            temp.add(go);

            jfrm.add(temp, BorderLayout.NORTH);
            jfrm.add(pe, BorderLayout.CENTER);

            jfrm.setVisible(true);
        }
    }
    public static void main(final String [] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (args.length != 0) {
                    System.out.println("usage: java Fireworks");
                    System.exit(1);
                }
                pe = new Fireworks();
                pe.new PaintDemo();
                try{
                    im = ImageIO.read(new File("src/main/resources/NewYear.png"));
                } catch (Exception e) {
                    e.printStackTrace();
                }               
                try{
                    im2 = ImageIO.read(new File("src/main/resources/CatsX3.png"));
                } catch (Exception e) {
                    e.printStackTrace();
                }               
                try{
                    im3 = ImageIO.read(new File("src/main/resources/CharlieClareCheryl.png"));
                } catch (Exception e) {
                    e.printStackTrace();
                }               
            }
        });
    }
}
