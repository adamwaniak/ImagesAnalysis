package app;

import app.ransac.Ransac;
import org.apache.commons.math3.linear.RealMatrix;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class MyPanel extends JPanel implements ActionListener {
    private Button selectImage1Btn = new Button("Image 1");
    private Button selectImage2Btn = new Button("Image 2");
    private Button pairPointsBtn = new Button("Pair");
    private Button neighboursBtn = new Button("Neighbours");
    private Button ransacBtn = new Button("app.Ransacnsac");
    private Button clearPairsBtn = new Button("Clear");
    private Button exitBtn = new Button("Exit");

    private String path1;
    private String path2;
    private boolean isEmpty = true;

    private BufferedImage img1;
    private BufferedImage img2;
    private SiftFileReader fr = new SiftFileReader();
    private Point[] imageIPoints, imageJPoints;
    private LinkedList<Point[]> allPairs;
    private LinkedList<Point[]> selectedPairs;


    MyPanel() {
        this.setVisible(true);
        this.setLayout(null);
        this.setBounds(0, 0, 1680, 1050);

        allPairs = new LinkedList<>();
        selectedPairs = new LinkedList<>();

        selectImage1Btn.setBounds(10, 5, 100, 30);
        add(selectImage1Btn);
        selectImage2Btn.setBounds(110, 5, 100, 30);
        add(selectImage2Btn);
        pairPointsBtn.setBounds(220, 5, 100, 30);
        add(pairPointsBtn);
        neighboursBtn.setBounds(330, 5, 100, 30);
        add(neighboursBtn);
        ransacBtn.setBounds(440, 5, 100, 30);
        add(ransacBtn);
        clearPairsBtn.setBounds(550, 5, 100, 30);
        add(clearPairsBtn);
        exitBtn.setBounds(1570, 5, 100, 30);
        add(exitBtn);

        selectImage1Btn.addActionListener(this);
        selectImage2Btn.addActionListener(this);
        pairPointsBtn.addActionListener(this);
        neighboursBtn.addActionListener(this);
        ransacBtn.addActionListener(this);
        clearPairsBtn.addActionListener(this);
        exitBtn.addActionListener(this);
        repaint();
    }

    private BufferedImage selectImage(boolean firstImage) {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("src/res"));
        int returnVal = fc.showOpenDialog(this);

        BufferedImage img = null;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                img = ImageIO.read(file);
                if (firstImage) {
                    String image1Path = file.getAbsolutePath();
                    path1 = image1Path + ".haraff.sift";
                } else {
                    String image2Path = file.getAbsolutePath();
                    path2 = image2Path + ".haraff.sift";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No image selected");
        }
        return img;
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        if (img1 != null) {
            g2d.drawImage(img1, 0, 80, null);
        }
        if (img2 != null && img1 != null) {
            g2d.drawImage(img2, img1.getWidth(), 80, null);
        }
        if (!allPairs.isEmpty() && selectedPairs.isEmpty() && isEmpty) {
            paintLine(g2d, allPairs);
        }
        if (!selectedPairs.isEmpty()) {
            paintLine(g2d, selectedPairs);
        }
    }

    private void paintLine(Graphics2D g2d, LinkedList<Point[]> pairs) {
        for (Point[] pair : pairs) {
            int r = (int) (Math.random() * 255);
            int z = (int) (Math.random() * 255);
            int b = (int) (Math.random() * 255);

            g2d.setColor(new Color(r, z, b));
            g2d.drawLine((int) pair[0].getX(), (int) pair[0].getY() + 80, (int) pair[1].getX() + img1.getWidth(), (int) pair[1].getY() + 80);
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == selectImage1Btn) {
            img1 = selectImage(true);
            imageIPoints = fr.read(path1);
            repaint();
        } else if (event.getSource() == selectImage2Btn) {
            img2 = selectImage(false);
            imageJPoints = fr.read(path2);
            repaint();
        } else if (event.getSource() == pairPointsBtn) {
            if (imageIPoints != null && imageJPoints != null) {
                selectedPairs.clear();
                imageIPoints = fr.read(path1);
                System.out.println("Points number of image I: " + imageIPoints.length);
                imageJPoints = fr.read(path2);
                System.out.println("Points number of image J: " + imageJPoints.length);
                long begin = System.nanoTime();
                FindNeighboursAlgorithm findNeighboursAlgorithm = new FindNeighboursAlgorithm(imageIPoints, imageJPoints);
                allPairs = findNeighboursAlgorithm.pairPoints();
                long end = System.nanoTime();
                System.out.println("Number of neighbour points: " + allPairs.size());
                System.out.println("Time needed for creating allPairs: " + (double) (end - begin) / (double) 1000000000);
                isEmpty = true;
                repaint();
            }
        } else if (event.getSource() == clearPairsBtn) {
            allPairs.clear();
            selectedPairs.clear();
            repaint();
        } else if (event.getSource() == neighboursBtn) {
            imageIPoints = fr.read(path1);
            imageJPoints = fr.read(path2);
            isEmpty = false;

            String s1 = JOptionPane.showInputDialog(null, "Number of checking neighbours: ", " ", JOptionPane.INFORMATION_MESSAGE);
            String s2 = JOptionPane.showInputDialog(null, "app.Cohesion value: ", " ", JOptionPane.INFORMATION_MESSAGE);
            int checkingNeighbours = 100;
            double cohesionValue = 0.8;
            try {
                checkingNeighbours = Integer.parseInt(s1);
                cohesionValue = Double.parseDouble(s2);
            } catch (Exception e) {
                System.out.println("Invalid number");
                e.printStackTrace();
            }
            long begin = System.nanoTime();
            Cohesion coh = new Cohesion(allPairs, cohesionValue);
            selectedPairs = coh.neighbours(checkingNeighbours);
            long end = System.nanoTime();
            System.out.println("Time needed for cohesion: " + (double) (end - begin) / (double) 1000000000);
            System.out.println("Got points: " + selectedPairs.size());
            repaint();
        } else if (event.getSource() == ransacBtn) {
            System.out.println(allPairs.size());
            String s1 = JOptionPane.showInputDialog(null, "Maximum error : ", " ", 1);
            String s2 = JOptionPane.showInputDialog(null, "Number of iterations : ", " ", 1);
            int maxError = 1;
            int iter = 5;
            try {
                maxError = Integer.parseInt(s1);
                iter = Integer.parseInt(s2);
            } catch (Exception e) {
                System.out.println("Invalid number");
                e.printStackTrace();
            }
            long begin = System.nanoTime();
            Ransac ransac = new Ransac();

            System.out.println(iter + " " + maxError);
            RealMatrix model = ransac.run(imageIPoints, imageJPoints, allPairs, iter, maxError);
            selectedPairs = ransac.getSelectedPairs(allPairs, model, maxError);
            long end = System.nanoTime();
            System.out.println("Time needed for app.ransac: " + (double) (end - begin) / (double) 1000000000);
            System.out.println("Got points: " + selectedPairs.size());
            repaint();
        } else if (event.getSource() == exitBtn) {
            System.exit(0);
        }
    }




}

