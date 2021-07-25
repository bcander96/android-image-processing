package com.example.bande.csc3002app.image;

import android.graphics.Bitmap;

/**
 * Created by bande on 06/11/2017.
 * This bitmap header class is used to store
 * data in relation to the image the user has
 * uploaded.
 */
public class BitmapHeader {

    // variable declarations
    int byteCount;
    int reserved;
    int width;
    int height;

    /**
     * Method used when loading the bitmap to
     * initiate the data into variables.
     * @param bitmap is the bitmap loaded by the
     *               user.
     * @return true if successful.
     */
    public boolean read(Bitmap bitmap){

        byteCount = bitmap.getByteCount()/1024;
        reserved = bitmap.getByteCount();
        width = bitmap.getWidth();
        height = bitmap.getHeight();

        return true;
    }

    /**
     * Getter method used by the image object class
     * to return the height of the image in pixels.
     * @return the height in pixels.
     */
    public int getHeight(){
        return height;
    }

    /**
     * Getter method used by the image object class
     * to return the width of the image in pixels.
     * @return the width in pixels.
     */
    public int getWidth(){
        return width;
    }

    /**
     * Getter method used to retrieve the byte count
     * of the image object. This can give an indication of
     * how big the image is.
     * @return the byte count (Kb).
     */
    public int getByteCount(){
        return byteCount;
    }

}
