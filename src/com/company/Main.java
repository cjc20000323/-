package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String path = "Lena.jpg";
        MyImage myImage = null;
        myImage = new MyImage(path);
        myImage.writeImage();
        myImage.getGrayImage();
        myImage.writeGrayImage();
        myImage.dealRobert();
        myImage.writeRobertImage();
        myImage.dealSobel();
        myImage.writeSobelImage();
        myImage.dealLaplace();
        myImage.writeLaplaceImage();
        myImage.dealPrewitt();
        myImage.writePrewittImage();
    }
}
