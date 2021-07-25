package com.example.bande.csc3002app.image;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by bande on 01/11/2017.
 * The image class is used to organise various helper methods
 * that enable the user to get or modify direct pixel values
 * in the bitmap. It also holds information useful to the user
 * such as the resolution or file size of the image.
 */
public class Image {

    // variable declarations
    BitmapHeader header = new BitmapHeader();
    Bitmap bitmap;

    // the image pixel colour channels (RGB)
    public int [][] blueChannel, greenChannel, redChannel;
    int colour;

    /**
     * Constructor used to load the given bitmap data into
     * the image object.
     * @param bmp is assigned to the image object bitmap.
     */
    public Image(Bitmap bmp){
        bitmap = bmp;
        read(bitmap);
    }

    /**
     * Construction method used to load the given bitmap in to
     * the image object.
     * @param bitmap is the given bitmap to be loaded.
     */
    void read(Bitmap bitmap){
        // load the bitmap into the header
        header.read(bitmap);

        // allocate space for image data
        blueChannel  = new int[getWidth()][getHeight()];
        greenChannel = new int[getWidth()][getHeight()];
        redChannel   = new int[getWidth()][getHeight()];

        // load the image data into the allocated space i.e. pixels
        for (int i = 0; i < getWidth(); i ++) {
            for (int j = 0; j < getHeight(); j ++) {
                colour = bitmap.getPixel(i, j);
                blueChannel[i][j]  = Color.blue(colour);
                greenChannel[i][j] = Color.green(colour);
                redChannel[i][j]   = Color.red(colour);
            }
        }
    }

    /**
     * Getter method to return the pixel value of the images
     * blue channel at co-ordinates (i,j)
     * @param i is the horizontal co-ordinate in the image.
     * @param j i the vertical co-ordinate in the image.
     * @return the pixel value at (i,j)
     */
    public int getBlueData(int i, int j) {
        return blueChannel[i][j];
    }

    /**
     * Getter method to return the pixel value of the images
     * green channel at co-ordinates (i,j)
     * @param i is the horizontal co-ordinate in the image.
     * @param j i the vertical co-ordinate in the image.
     * @return the pixel value at (i,j)
     */
    public int getGreenData(int i, int j) {
        return greenChannel[i][j];
    }

    /**
     * Getter method to return the pixel value of the images
     * red channel at co-ordinates (i,j)
     * @param i is the horizontal co-ordinate in the image.
     * @param j i the vertical co-ordinate in the image.
     * @return the pixel value at (i,j)
     */
    public int getRedData(int i, int j) {
        return redChannel[i][j];
    }

    /**
     * Getter method to return the bitmap loaded into the
     * image object.
     * @return the bitmap stored in the image object.
     */
    public Bitmap getBitmap(){
        return bitmap;
    }

    /**
     * Setter method to load a new image into an image
     * object.
     * @param bmp is the bitmap to be set.
     */
    public void setBitmap(Bitmap bmp){
        bitmap = bmp;
        read(bitmap);
    }

    /**
     * Getter method to return the width of the image.
     * @return the width of the image.
     */
    public int getWidth(){
        return header.getWidth();
    }

    /**
     * Getter method to return the height of the image.
     * @return the height of the image.
     */
    public int getHeight(){
        return header.getHeight();
    }

    /**
     * Getter method to get the image file size.
     * @return the file size of the image according to the header.
     */
    public int getByteCount(){
        return header.getByteCount();
    }

    /**
     * Method used to set a new pixel value in a bitmap.
     * @param i is the horizontal co-ordinate in the image.
     * @param j is the vertical co-ordinate in the image.
     * @param b is the blue pixel value.
     * @param g is the green pixel value.
     * @param r is the red pixel value.
     * @param bmp is the bitmap being changed.
     * @return the bitmap after the pixel values have been changed.
     */
    public Bitmap setPixel(int i, int j, int b, int g, int r, Bitmap bmp){
        if (i >= 0 && i < getWidth() && j >= 0 && j < getHeight()) {
            // pixel is coded by one byte and so value must be within [0, 255]
            if (b < 0) b = 0; if (b > 255) b = 255;
            if (g < 0) g = 0; if (g > 255) g = 255;
            if (r < 0) r = 0; if (r > 255) r = 255;
            bmp.setPixel(i, j, Color.rgb(r, g, b));
        }
        return bmp;
    }

}