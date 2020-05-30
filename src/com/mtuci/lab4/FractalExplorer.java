package com.mtuci.lab4;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;


public class FractalExplorer {
    private int dispSize;
    private JImageDisplay image;
    private FractalGenerator fGen;
    private Rectangle2D.Double range;

    public static void main(String[] args) {
        FractalExplorer fracExp = new FractalExplorer(800);
        fracExp.createAndShowGUI();
        fracExp.drawFractal();
    }

    /** Конструктор FractalExplorer **/
    public FractalExplorer(int dispSize) {
        this.dispSize = dispSize;
        this.fGen = new Mandelbrot();
        this.range = new Rectangle2D.Double(0, 0, 0, 0);
        fGen.getInitialRange(this.range);
    }

    /** Инициализация графического интерфейса Swing **/
    public void createAndShowGUI() {
        JFrame frame = new JFrame("Fractal");
        JButton resetBtn = new JButton("Reset Display");
        image = new JImageDisplay(dispSize, dispSize);

        ActionHandler actHand = new ActionHandler();
        MouseHandler mouseHand = new MouseHandler();
        resetBtn.addActionListener(actHand);
        image.addMouseListener(mouseHand);

        frame.add(image, BorderLayout.CENTER);
        frame.add(resetBtn, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    /** Метод для вывода фрактала **/
    private void drawFractal() {
        for (int x = 0; x < dispSize; x++) {
            for (int y = 0; y < dispSize; y++) {
                int nIter = fGen.numIterations(FractalGenerator.getCoord(range.x, range.x +
                        range.width, dispSize, x), FractalGenerator.getCoord(range.y, range.y +
                        range.width, dispSize, y));
                if (nIter == -1) image.drawPixel(x, y, 0);
                else {
                    double hue = 0.7f + nIter / 200f;
                    int rgbColor = Color.HSBtoRGB((float) hue, 1f, 1f);
                    image.drawPixel(x, y, rgbColor);
                }
            }
        }
        image.repaint();
    }

    public class ActionHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            fGen.getInitialRange(range);
            drawFractal();
        }
    }

    /** Обработка событий от мыши **/
    public class MouseHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, dispSize, x);
            int y = e.getY();
            double yCoord = FractalGenerator.getCoord(range.y, range.y + range.height, dispSize, y);
            fGen.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
            drawFractal();
        }
    }



} 