package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MyImage {

    private BufferedImage image; //原图
    private BufferedImage grayImage; //灰度图
    private BufferedImage RobertImage; //
    private BufferedImage SobelImage;
    private BufferedImage LaplaceImage;
    private BufferedImage PrewittImage;
    private int[][] grayBox; //灰度图的像素矩阵
    private int[][] GM;
    private int width;
    private int height;

    public MyImage(String filePath) throws IOException {
        File file = new File(filePath);
        image = (BufferedImage) ImageIO.read(file);
        width = image.getWidth();
        height = image.getHeight();
    }

    public void writeImage() throws IOException {
        ImageIO.write(image, "jpg", new File("test.jpg"));
    }

    public void getGrayImage() {
        grayImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        grayBox = new int[width + 2][height + 2];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int r = (image.getRGB(i, j) & 0xff0000) >> 16;
                int g = (image.getRGB(i, j) & 0xff00) >> 8;
                int b = (image.getRGB(i, j) & 0xff);

                int y = (int) (0.3 * r + 0.59 * g + 0.11 * b);
                grayBox[i + 1][j + 1] = y;
                int grayStd = ((y & 0xff) << 16 ) | ((y & 0xff) << 8) | (y & 0xff);
                grayImage.setRGB(i, j, grayStd);
            }
        }
    }

    public void writeGrayImage() throws IOException {
        ImageIO.write(grayImage, "jpg", new File("gray.jpg"));
    }

    public void dealRobert() {
        RobertImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        GM = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (i < width - 1 && j < height - 1) {
                    GM[i][j] = Math.abs(grayBox[i][j] - grayBox[i + 1][j + 1]) + Math.abs(grayBox[i + 1][j] - grayBox[i][j + 1]);
                } else if (i == width - 1 && j < height - 1) {
                    GM[i][j] = Math.abs(grayBox[i][j]) + Math.abs(grayBox[i][j + 1]);
                } else if (i < width - 1 && j == height - 1) {
                    GM[i][j] = Math.abs(grayBox[i][j]) + Math.abs(grayBox[i + 1][j]);
                } else {
                    GM[i][j] = Math.abs(grayBox[i][j]);
                }
                if (GM[i][j] < 0) {
                    GM[i][j] = 0;
                }
                if (GM[i][j] > 255) {
                    GM[i][j] = 255;
                }
                int std = ((GM[i][j] & 0xff) << 16 ) | ((GM[i][j] & 0xff) << 8) | (GM[i][j] & 0xff);
                RobertImage.setRGB(i, j, std);
            }
        }
    }

    public void dealSobel() {
        SobelImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        GM = new int[width][height];
        int[][] GX = new int[width][height];
        int[][] GY = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int leftUp = 0;
                int up = 0;
                int rightUp = 0;
                int leftDown = 0;
                int down = 0;
                int rightDown = 0;
                int left = 0;
                int right = 0;
                if (i == 0 && j > 0 && j < height - 1) {
                    up = grayBox[i][j - 1];
                    down = grayBox[i][j + 1];
                    rightUp = grayBox[i + 1][j - 1];
                    rightDown = grayBox[i + 1][j + 1];
                    right = grayBox[i + 1][j];
                } else if (i == 0 && j == 0) {
                    down = grayBox[i][j + 1];
                    rightDown = grayBox[i + 1][j + 1];
                    right = grayBox[i + 1][j];
                } else if (i == 0 && j == height - 1) {
                    up = grayBox[i][j - 1];
                    rightUp = grayBox[i + 1][j - 1];
                    right = grayBox[i + 1][j];
                } else if (i > 0 && i < width - 1 && j == 0) {
                    leftDown = grayBox[i - 1][j + 1];
                    down = grayBox[i][j + 1];
                    rightDown = grayBox[i + 1][j + 1];
                    left = grayBox[i - 1][j];
                    right = grayBox[i + 1][j];
                } else if (i == width - 1 && j == 0) {
                    leftDown = grayBox[i - 1][j + 1];
                    down = grayBox[i][j + 1];
                    left = grayBox[i - 1][j];
                } else if (i == width - 1 && j == height - 1) {
                    leftUp = grayBox[i - 1][j - 1];
                    up = grayBox[i][j - 1];
                    left = grayBox[i - 1][j];
                } else if (i == width - 1 && j > 0 && j < height - 1) {
                    leftUp = grayBox[i - 1][j - 1];
                    up = grayBox[i][j - 1];
                    leftDown = grayBox[i - 1][j + 1];
                    down = grayBox[i][j + 1];
                    left = grayBox[i - 1][j];
                } else if (i > 0 && i < width - 1 && j == height - 1) {
                    leftUp = grayBox[i - 1][j - 1];
                    up = grayBox[i][j - 1];
                    rightUp = grayBox[i + 1][j - 1];
                    left = grayBox[i - 1][j];
                    right = grayBox[i + 1][j];
                }
                else {
                    leftUp = grayBox[i - 1][j - 1];
                    up = grayBox[i][j - 1];
                    rightUp = grayBox[i + 1][j - 1];
                    leftDown = grayBox[i - 1][j + 1];
                    down = grayBox[i][j + 1];
                    rightDown = grayBox[i + 1][j + 1];
                    left = grayBox[i - 1][j];
                    right = grayBox[i + 1][j];
                }
                GX[i][j] = -leftUp - 2 * up - rightUp + leftDown + 2 * down + rightDown;
                GY[i][j] = -leftUp + rightUp - 2 * left + 2 * right - leftDown + rightDown;
                GM[i][j] = (int) Math.sqrt(Math.pow(GX[i][j], 2) + Math.pow(GY[i][j], 2));
                int std = ((GM[i][j] & 0xff) << 16 ) | ((GM[i][j] & 0xff) << 8) | (GM[i][j] & 0xff);
                SobelImage.setRGB(i, j, std);
            }
        }
    }

    public void dealLaplace() {
        LaplaceImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        GM = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int leftUp = 0;
                int up = 0;
                int rightUp = 0;
                int leftDown = 0;
                int down = 0;
                int rightDown = 0;
                int left = 0;
                int right = 0;
                if (i == 0 && j > 0 && j < height - 1) {
                    up = grayBox[i][j - 1];
                    down = grayBox[i][j + 1];
                    rightUp = grayBox[i + 1][j - 1];
                    rightDown = grayBox[i + 1][j + 1];
                    right = grayBox[i + 1][j];
                } else if (i == 0 && j == 0) {
                    down = grayBox[i][j + 1];
                    rightDown = grayBox[i + 1][j + 1];
                    right = grayBox[i + 1][j];
                } else if (i == 0 && j == height - 1) {
                    up = grayBox[i][j - 1];
                    rightUp = grayBox[i + 1][j - 1];
                    right = grayBox[i + 1][j];
                } else if (i > 0 && i < width - 1 && j == 0) {
                    leftDown = grayBox[i - 1][j + 1];
                    down = grayBox[i][j + 1];
                    rightDown = grayBox[i + 1][j + 1];
                    left = grayBox[i - 1][j];
                    right = grayBox[i + 1][j];
                } else if (i == width - 1 && j == 0) {
                    leftDown = grayBox[i - 1][j + 1];
                    down = grayBox[i][j + 1];
                    left = grayBox[i - 1][j];
                } else if (i == width - 1 && j == height - 1) {
                    leftUp = grayBox[i - 1][j - 1];
                    up = grayBox[i][j - 1];
                    left = grayBox[i - 1][j];
                } else if (i == width - 1 && j > 0 && j < height - 1) {
                    leftUp = grayBox[i - 1][j - 1];
                    up = grayBox[i][j - 1];
                    leftDown = grayBox[i - 1][j + 1];
                    down = grayBox[i][j + 1];
                    left = grayBox[i - 1][j];
                } else if (i > 0 && i < width - 1 && j == height - 1) {
                    leftUp = grayBox[i - 1][j - 1];
                    up = grayBox[i][j - 1];
                    rightUp = grayBox[i + 1][j - 1];
                    left = grayBox[i - 1][j];
                    right = grayBox[i + 1][j];
                }
                else {
                    leftUp = grayBox[i - 1][j - 1];
                    up = grayBox[i][j - 1];
                    rightUp = grayBox[i + 1][j - 1];
                    leftDown = grayBox[i - 1][j + 1];
                    down = grayBox[i][j + 1];
                    rightDown = grayBox[i + 1][j + 1];
                    left = grayBox[i - 1][j];
                    right = grayBox[i + 1][j];
                }
                GM[i][j] = Math.abs(-up - left - right - down + 8 * grayBox[i][j] - leftUp - leftDown - rightUp - rightDown);
                int std = ((GM[i][j] & 0xff) << 16 ) | ((GM[i][j] & 0xff) << 8) | (GM[i][j] & 0xff);
                LaplaceImage.setRGB(i, j, std);
            }
        }
    }

    public void dealPrewitt() {
        PrewittImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        GM = new int[width][height];
        int[][] GX = new int[width][height];
        int[][] GY = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int leftUp = 0;
                int up = 0;
                int rightUp = 0;
                int leftDown = 0;
                int down = 0;
                int rightDown = 0;
                int left = 0;
                int right = 0;
                if (i == 0 && j > 0 && j < height - 1) {
                    up = grayBox[i][j - 1];
                    down = grayBox[i][j + 1];
                    rightUp = grayBox[i + 1][j - 1];
                    rightDown = grayBox[i + 1][j + 1];
                    right = grayBox[i + 1][j];
                } else if (i == 0 && j == 0) {
                    down = grayBox[i][j + 1];
                    rightDown = grayBox[i + 1][j + 1];
                    right = grayBox[i + 1][j];
                } else if (i == 0 && j == height - 1) {
                    up = grayBox[i][j - 1];
                    rightUp = grayBox[i + 1][j - 1];
                    right = grayBox[i + 1][j];
                } else if (i > 0 && i < width - 1 && j == 0) {
                    leftDown = grayBox[i - 1][j + 1];
                    down = grayBox[i][j + 1];
                    rightDown = grayBox[i + 1][j + 1];
                    left = grayBox[i - 1][j];
                    right = grayBox[i + 1][j];
                } else if (i == width - 1 && j == 0) {
                    leftDown = grayBox[i - 1][j + 1];
                    down = grayBox[i][j + 1];
                    left = grayBox[i - 1][j];
                } else if (i == width - 1 && j == height - 1) {
                    leftUp = grayBox[i - 1][j - 1];
                    up = grayBox[i][j - 1];
                    left = grayBox[i - 1][j];
                } else if (i == width - 1 && j > 0 && j < height - 1) {
                    leftUp = grayBox[i - 1][j - 1];
                    up = grayBox[i][j - 1];
                    leftDown = grayBox[i - 1][j + 1];
                    down = grayBox[i][j + 1];
                    left = grayBox[i - 1][j];
                } else if (i > 0 && i < width - 1 && j == height - 1) {
                    leftUp = grayBox[i - 1][j - 1];
                    up = grayBox[i][j - 1];
                    rightUp = grayBox[i + 1][j - 1];
                    left = grayBox[i - 1][j];
                    right = grayBox[i + 1][j];
                }
                else {
                    leftUp = grayBox[i - 1][j - 1];
                    up = grayBox[i][j - 1];
                    rightUp = grayBox[i + 1][j - 1];
                    leftDown = grayBox[i - 1][j + 1];
                    down = grayBox[i][j + 1];
                    rightDown = grayBox[i + 1][j + 1];
                    left = grayBox[i - 1][j];
                    right = grayBox[i + 1][j];
                }
                GX[i][j] = -leftUp - up - rightUp + leftDown + down + rightDown;
                GY[i][j] = -leftUp + rightUp - left + right - leftDown + rightDown;
                GM[i][j] = (int) Math.sqrt(Math.pow(GX[i][j], 2) + Math.pow(GY[i][j], 2));
                int std = ((GM[i][j] & 0xff) << 16 ) | ((GM[i][j] & 0xff) << 8) | (GM[i][j] & 0xff);
                PrewittImage.setRGB(i, j, std);
            }
        }
    }

    public void writeRobertImage() throws IOException {
        ImageIO.write(RobertImage, "jpg", new File("robert.jpg"));
    }

    public void writeSobelImage() throws IOException {
        ImageIO.write(SobelImage, "jpg", new File("sobel.jpg"));
    }

    public void writeLaplaceImage() throws IOException {
        ImageIO.write(LaplaceImage, "jpg", new File("laplace.jpg"));
    }

    public void writePrewittImage() throws IOException {
        ImageIO.write(PrewittImage, "jpg", new File("prewitt.jpg"));
    }

}
