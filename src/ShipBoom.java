import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;

import java.util.concurrent.ThreadLocalRandom;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ShipBoom extends JFrame{

    private int shipWidth = 150, shipHeight = 100, shipX = 0,
            cannonWidth = 200, cannonHeight = 200,
            kernelY = 400, boomWidth = 200, boomHight = 200, flag = 0;


    private static Image background;
    private static Image kernel;
    private static Image cannon;
    private static Image ship;
    private static Image boom;
    JButton start, shot, restart;

    public ShipBoom() {
        setTitle("Demo app");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        //  Background() - панель, для которой переопределен paintComponent с фоном
        setContentPane(new Background()); // панель устанавливается как contentPane в наследнике JFrame
        Container content = getContentPane(); //теперь все элементы интерфейса будут на этой панели.

        start = new JButton("Старт");
        start.setPreferredSize(new Dimension(1000,50));
        start.setBackground(Color.white);
        start.setForeground(Color.BLACK);


        shot = new JButton("Выстрел");
        shot.setPreferredSize(new Dimension(100,50));
        shot.setBackground(Color.white);
        shot.setForeground(Color.BLACK);

        restart = new JButton("Restart");
        restart.setPreferredSize(new Dimension(100,50));
        restart.setBackground(Color.white);
        restart.setForeground(Color.BLACK);

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start.setVisible(false);
                Thread shipMove = new Thread(new shipThread());
                shipMove.start();
                Thread shipMove1 = new Thread(new kernelThread());
                shipMove1.start();

            }
        });
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread shipMove = new Thread(new shipThread());
                shipMove.start();
                Thread shipMove1 = new Thread(new kernelThread());
                shipMove1.start();

            }
        });
        shot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread shipMove1 = new Thread(new ShotFlakeThread());
                shipMove1.start();

            }
        });
        content.add(start);
        content.add(shot);
        content.add(restart);
        content.add(new ShipCannon());

    }

    private static class Background extends JPanel{ // отрисовка нового фона

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            try {
                background = ImageIO.read(new File("Sea.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            g.drawImage(background,0,0,null);
        }
    }

    private class ShipCannon extends JPanel{

        public ShipCannon() {
            setOpaque(false);
            setPreferredSize(new Dimension(1000, 600));
            try {
                ship = ImageIO.read(new File("ship1.png"));
                cannon= ImageIO.read(new File("cannon1.png"));
                kernel =  ImageIO.read(new File("kernel.png"));
                boom = ImageIO.read(new File("boom.png"));
            }
            catch (IOException exc) {};

        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D graphics2D = (Graphics2D)g;
            if(flag == 0) {
                graphics2D.drawImage(ship, shipX, 100, shipWidth, shipHeight, this);
                graphics2D.drawImage(kernel, 457, kernelY, 75, 50, this);
                graphics2D.drawImage(cannon, 400, 400, cannonWidth, cannonHeight, this);
            }
            if(flag == 1){
                graphics2D.drawImage(boom,400 ,80, boomWidth, boomHight, this);
                graphics2D.drawImage(kernel, 480, kernelY, 25, 25, this);
                graphics2D.drawImage(cannon, 400, 400, cannonWidth, cannonHeight, this);
            }
        }
    }

    public class shipThread implements Runnable{
        @Override
        public void run() {
            flag = 0;
            shipX = 0;
            while (true) {
                if(kernelY == 80 && (shipX > 405 && shipX < 555)){
                    break;
                }
                repaint();
                try {
                    Thread.sleep(0);
                }
                catch (Exception exc) {};
            }
            flag = 1;
            if(flag == 1){
                while (true) {
                    repaint();
                    try {
                        Thread.sleep(0);
                    }
                    catch (Exception exc) {};
                }
            }

        }
    }



    public class kernelThread implements Runnable{
        @Override
        public void run() {
            while (shipX < 1000) {
                if(shipX == 980){
                    shipX = 0;
                }
                shipX +=20;
                try {
                    Thread.sleep(150);
                }
                catch (Exception exc) {};
            }
        }
    }
    public class ShotFlakeThread implements Runnable{
        @Override
        public void run() {
            kernelY = 400;
            while (shipHeight > 0) {
                kernelY -= 20;
                try {
                    Thread.sleep(150);
                }
                catch (Exception exc) {};
            }
        }
    }
    public static void main(String[] args) throws IOException {
        new ShipBoom().setVisible(true);
    }
}
