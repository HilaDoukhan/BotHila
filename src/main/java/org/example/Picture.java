package org.example;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Picture {

    public void createPictureByParameter(String[] parameters){
        BufferedImage processed = new BufferedImage(Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]), BufferedImage.TYPE_INT_RGB);
        if (parameters[2].equals("r")){
            processed = twoRectangles(processed,Integer.parseInt(parameters[3]),Integer.parseInt(parameters[4]));
        }else {
            processed = twoTriangles(processed,Integer.parseInt(parameters[3]),Integer.parseInt(parameters[4]));
        }

        try {
            ImageIO.write(processed, "png", new File("Picture.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private BufferedImage twoRectangles(BufferedImage processed , int upColor , int downColor){
        int mid = processed.getHeight()/2;
        for (int i=0; i<processed.getWidth(); i++){
            for (int j=0; j<processed.getHeight(); j++){
                if (j<mid){
                    processed.setRGB(i,j,upColor);
                }else{
                    processed.setRGB(i,j,downColor);
                }
            }
        }

        return processed;
    }
    private BufferedImage twoTriangles (BufferedImage processed,int upColor,int downColor){
        for (int i=0; i<processed.getWidth(); i++){
            for (int j=0; j<processed.getHeight(); j++){
                if(j <= (i+j)/2){
                    processed.setRGB(j,i,upColor);
                }else{
                    processed.setRGB(j,i,downColor);
                }
            }
    }
        return processed;
    }
}
