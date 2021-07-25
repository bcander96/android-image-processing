package com.example.bande.csc3002app.toolkit;

import android.graphics.Bitmap;
import com.example.bande.csc3002app.image.Image;

/**
 * Created by bande on 27/02/2018.
 * This Properties class is a toolkit class which allows
 * the user to change various image settings such as
 * Brightness, Contrast and Exposure.
 */
public class Properties {

    // variable declarations
    Image image;
    Bitmap updated;

    // image pixel values: blue, green, red
    int b, g, r;

    /**
     * Constructor to load the image into
     * this class for updating.
     * @param img is the image object provided.
     */
    public Properties(Image img){
        image = img;
        updated = image.getBitmap().copy(
                image.getBitmap().getConfig().ARGB_8888, true);
    }

    /**
     * Helper method used to convert the image to greyscale.
     * @return the updated image.
     */
    public Bitmap convertToGreyscale(){

        // modify the image
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // get the pixel value at (i, j)
                b = image.getBlueData(i, j);
                g = image.getGreenData(i, j);
                r = image.getRedData(i, j);

                // take conversion up to one single value
                r = g = b = (int)(0.299 * r + 0.587 * g + 0.114 * b);

                image.setPixel(i, j, b, g, r, updated);
            }
        }

        return updated;
    }

    /**
     * Helper method used to adjust the image brightness.
     * @param increase is a boolean which indicates an increase
     *                 or decrease in brightness.
     * @return the updated bitmap.
     */
    public Bitmap adjustBrightness(boolean increase){
        int change = 0;
        // fixed amount to adjust brightness
        if(increase){ change = 10; }
        else if(!increase){ change = -10; }

        // modify the image
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // get the pixel value at (i, j)
                b = image.getBlueData(i, j) + change;
                g = image.getGreenData(i, j) + change;
                r = image.getRedData(i, j) + change;

                image.setPixel(i, j, b, g, r, updated);
            }
        }

        return updated;
    }

    /**
     * Helper method used to adjust the image contrast.
     * @param increase is a boolean which indicates an increase
     *                 or decrease in contrast.
     * @return the updated bitmap.
     */
    public Bitmap adjustContrast(boolean increase){
        double change = 0;
        // fixed amount to adjust contrast
        if(increase){ change = 5; }
        else if(!increase){ change = -5; }

        double contrast = Math.pow((100 + change) / 100, 2);

        // modify the image
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // get the pixel value at (i, j)
                b = (int)(((((image.getBlueData(i, j) / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                g = (int)(((((image.getGreenData(i, j) / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                r = (int)(((((image.getRedData(i, j) / 255.0) - 0.5) * contrast) + 0.5) * 255.0);

                image.setPixel(i, j, b, g, r, updated);
            }
        }

        return updated;
    }

    /**
     * Helper method used to adjust the pixel value stored in an
     * image channel.
     * @param increase is a boolean which indicates an increase
     *                 or decrease in value.
     * @param channel is the colour channel to be updated.
     * @return the updated bitmap.
     */
    public Bitmap adjustRGB(boolean increase, char channel){

        int change = 0;
        // fixed amount to adjust the pixel value
        if(increase){ change = 20; }
        else if(!increase){ change = -20; }

        // modify the image
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // get the pixel value at (i, j)
                b = image.getBlueData(i, j);
                g = image.getGreenData(i, j);
                r = image.getRedData(i, j);

                // change the required pixel value
                if (channel == 'b') {
                    b += change;
                }
                if (channel == 'g') {
                    g += change;
                }
                if (channel == 'r') {
                    r += change;
                }

                image.setPixel(i, j, b, g, r, updated);
            }
        }
        return updated;

    }

    /**
     * Helper method used to adjust the exposure of the image.
     * @param increase is a boolean which indicates an increase
     *                 or decrease in exposue.
     * @return the updated bitmap.
     */
    public Bitmap adjustExposure(boolean increase){

        double change = 0;
        // fixed amount to adjust the pixel exposure value
        if(increase){ change = 0.1; }
        else if(!increase){ change = -0.1; }

        double exposureFactor = Math.pow(2.0f, change);

        // modify the image
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // get the pixel value at (i, j)
                b = image.getBlueData(i, j);
                g = image.getGreenData(i, j);
                r = image.getRedData(i, j);
                // multiply by the exposure factor
                b *= exposureFactor;
                g *= exposureFactor;
                r *= exposureFactor;

                image.setPixel(i, j, b, g, r, updated);
            }
        }
        return updated;
    }

    /**
     * Helper method used to adjust the saturation of an image.
     * @param increase is a boolean which indicates an increase
     *                 or decrease in exposue.
     * @return the updated bitmap.
     */
    public Bitmap adjustSaturation(boolean increase){

        float change = 0;
        // fixed amount to adjust the pixel saturation value
        if(increase){ change = 20f; }
        else if(!increase){ change = -20f; }

        float saturationFactor = (255.0f + change) / 255.0f;
        float blue, green, red;

        // modify the image
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // get the pixel value at (i, j)
                blue = (float) image.getBlueData(i, j);
                green = (float) image.getGreenData(i, j);
                red = (float) image.getRedData(i, j);
                // adjust the saturation of each colour channel
                float lum = (blue * 0.11f) + (green * 0.59f) + (red * 0.3f);
                blue = lum * (1.0f - saturationFactor) + blue * saturationFactor;
                green = lum * (1.0f - saturationFactor) + green * saturationFactor;
                red = lum * (1.0f - saturationFactor) + red * saturationFactor;

                image.setPixel(i, j, (int) blue, (int) green, (int) red, updated);
            }
        }
        return updated;
    }

    /**
     * A demo method used to demonstrate basic pixel value manipulation.
     * @return the updated bitmap.
     */
    public Bitmap imageProcessingDemo(){
        // modify the image
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // get the pixel value at (i, j)
                b = image.getBlueData(i, j);
                g = image.getGreenData(i, j);
                r = image.getRedData(i, j);

                // modify the value (for example)
                b *= 2; g *= 1.5; r *= 3;

                image.setPixel(i, j, b, g, r, updated);
            }
        }
        return updated;
    }
}
