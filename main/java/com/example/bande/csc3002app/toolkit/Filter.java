package com.example.bande.csc3002app.toolkit;

import android.graphics.Bitmap;
import com.example.bande.csc3002app.image.Image;
import java.util.Random;
import static java.lang.Math.PI;
import static java.lang.Math.exp;

/**
 * Created by bande on 20/01/2018.
 * This Filter class is a toolkit class which allows
 * the user to add an image filter to their image.
 * This class supports filters such as blur, noise
 * and sobel edge detection.
 */
public class Filter {

    // variable declarations
    Image image;
    Bitmap updated;

    // image pixel values: blue, green, red
    int b, g, r;

    /**
     * Constructor to load the image into
     * this class for updating with a filter.
     * @param img is the image object provided.
     */
    public Filter(Image img){
        image = img;
        updated = image.getBitmap().copy(
                image.getBitmap().getConfig().ARGB_8888, true);
    }

    /**
     * Helper method to add noise to the image.
     * @return the updated image.
     */
    public Bitmap addNoise(){

        int min = -1; int max = 1; int range = 5;
        Random rand = new Random();
        int randomNum; int noiseValue;

        // modify the image
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {

                // calculating the noise factor
                randomNum = rand.nextInt((max - min) + 1) + min;
                noiseValue = range * randomNum;

                // get the pixel value at (i, j) and
                // add the noise factor to the pixel
                b = image.getBlueData(i, j) + noiseValue;
                g = image.getGreenData(i, j) + noiseValue;
                r = image.getRedData(i, j) + noiseValue;

                image.setPixel(i, j, b, g, r, updated);
            }
        }

        return updated;
    }

    /**
     * Private filter method used to generate the gaussian kernel used
     * when blurring an image.
     * @param kernelSize is the size of the kernel used. (Odd number)
     * @param sigma is the sigma used. (Odd number)
     * @return the kernel array.
     */
    private double [][] gaussianKernel(int kernelSize, float sigma){

        // allocating space for the kernel
        double [][] kernel = new double [kernelSize][kernelSize];

        int kernelRadius = kernelSize / 2;
        float sigma2 = sigma * sigma;
        double c = 1. / (2. * PI * sigma2);

        // assigning the kernel values
        double sum = 0.;
        for (int k = -kernelRadius; k <= kernelRadius; k ++) {
            for (int m = -kernelRadius; m <= kernelRadius; m ++) {
                kernel[kernelRadius + k][kernelRadius + m] = c * exp(-(k * k + m * m) / (2 * sigma2));
                sum += kernel[kernelRadius + k][kernelRadius + m];
            }
        }

        for (int k = 0; k < kernelSize; k ++) {
            for (int m = 0; m < kernelSize; m ++) {
                kernel[k][m] /= sum;
            }
        }

        return kernel;
    }

    /**
     * Private filter method used to general the uniform kernel used
     * when blurring an image.
     * @param kernelSize is the size of the kernel used. (Odd number)
     * @return the kernel array.
     */
    private double [][] uniformKernel(int kernelSize) {
        double [][] kernel = new double [kernelSize][kernelSize];
        for (int k = 0; k < kernelSize; k ++) {
            for (int m = 0; m < kernelSize; m ++) {
                kernel[k][m] = 1. / (kernelSize * kernelSize);
            }
        }
        return kernel;
    }

    /**
     * Helper method used to add blur to an image using a given kernel.
     * @param gaussian indicates what type of blur to use: gaussian or uniform
     * @return the updated bitmap.
     */
    public Bitmap addBlur(boolean gaussian) {

        double [][] kernel;
        int kernelSize = 3;
        int i0 = kernelSize / 2;
        int j0 = kernelSize / 2;

        if (gaussian) {
            // get the gaussian kernel
            kernel = gaussianKernel(kernelSize, 3);
        } else {
            // get the uniform kernel
            kernel = uniformKernel(kernelSize);
        }

        // create temporary colour channel arrays
        int [][] b = new int [image.getWidth()][image.getHeight()];
        int [][] g = new int [image.getWidth()][image.getHeight()];
        int [][] r = new int [image.getWidth()][image.getHeight()];
        for (int i = 0; i < image.getWidth(); i ++){
            for (int j = 0; j < image.getHeight(); j ++){
                b[i][j] = image.getBlueData(i, j);
                g[i][j] = image.getGreenData(i, j);
                r[i][j] = image.getRedData(i, j);
            }
        }

        // complete the convolution of kernel to image
        for(int i = i0; i < image.getWidth() - i0; i++) {
            for (int j = j0; j < image.getHeight() - j0; j++) {
                // convolution sum_{k,m} kernel[k][m] * image[i-k][j-m]
                float bSum = 0;
                float gSum = 0;
                float rSum = 0;
                for (int k = -i0; k <= i0; k++) {
                    for (int m = -j0; m <= j0; m++) {
                        bSum += kernel[i0 + k][j0 + m] * b[i + k][j + m];
                        gSum += kernel[i0 + k][j0 + m] * g[i + k][j + m];
                        rSum += kernel[i0 + k][j0 + m] * r[i + k][j + m];
                    }
                }
                b[i][j] = (int) bSum;
                g[i][j] = (int) gSum;
                r[i][j] = (int) rSum;

                image.setPixel(i, j, b[i][j], g[i][j], r[i][j], updated);
            }
        }

        return updated;
    }

    /**
     * Private filter method to generate the X sobel kernel used
     * for edge detection.
     * @return the kernel array.
     */
    private double [][] sobelKernelGx(){
        double [][] kernelX = new double[3][3];
        kernelX[0][0] = -1.; kernelX[0][1] = 0.; kernelX[0][2] = 1.;
        kernelX[1][0] = -2.; kernelX[1][1] = 0.; kernelX[1][2] = 2.;
        kernelX[2][0] = -1.; kernelX[2][1] = 0.; kernelX[2][2] = 1.;

        return kernelX;
    }

    /**
     * Private filter method to generate the Y sobel kernel used
     * for edge detection.
     * @return the kernel array.
     */
    private double [][] sobelKernelGy(){
        double [][] kernelY = new double[3][3];
        kernelY[0][0] = -1.0; kernelY[0][1] = -2.; kernelY[0][2] = -1.;
        kernelY[1][0] = 0.; kernelY[1][1] = 0.; kernelY[1][2] = 0.;
        kernelY[2][0] = 1.; kernelY[2][1] = 2.; kernelY[2][2] = 1.;

        return kernelY;
    }

    /**
     * Helper method used to carry out sobel edge detection on an image
     * by using an X and Y kernel.
     * @return the updated bitmap.
     */
    public Bitmap edgeDetection(){

        // get the sobel kernels (x and y)
        double [][] Gx = sobelKernelGx();
        double [][] Gy = sobelKernelGy();
        int kernelSize = 3;
        int i0 = kernelSize / 2;
        int j0 = kernelSize / 2;

        // create temporary colour channel arrays
        int [][] b = new int [image.getWidth()][image.getHeight()];
        int [][] g = new int [image.getWidth()][image.getHeight()];
        int [][] r = new int [image.getWidth()][image.getHeight()];
        for (int i = 0; i < image.getWidth(); i ++){
            for (int j = 0; j < image.getHeight(); j ++){
                b[i][j] = image.getBlueData(i, j);
                g[i][j] = image.getGreenData(i, j);
                r[i][j] = image.getRedData(i, j);
            }
        }

        // complete the convolution of kernel to image
        for(int i = i0; i < image.getWidth() - i0; i++) {
            for (int j = j0; j < image.getHeight() - j0; j++) {
                // convolution sum_{k,m} kernel[k][m] * image[i-k][j-m]
                float bX_sum = 0; float bY_sum = 0;
                float gX_sum = 0; float gY_sum = 0;
                float rX_sum = 0; float rY_sum = 0;

                for (int k = -i0; k <= i0; k++) {
                    for (int m = -j0; m <= j0; m++) {
                        bX_sum += Gx[i0 + k][j0 + m] * b[i + k][j + m];
                        bY_sum += Gy[i0 + k][j0 + m] * b[i + k][j + m];

                        gX_sum += Gx[i0 + k][j0 + m] * g[i + k][j + m];
                        gY_sum += Gy[i0 + k][j0 + m] * g[i + k][j + m];

                        rX_sum += Gx[i0 + k][j0 + m] * r[i + k][j + m];
                        rY_sum += Gy[i0 + k][j0 + m] * r[i + k][j + m];

                    }
                }

                int b_sum = (int)Math.abs(bX_sum) + (int)Math.abs(bY_sum);
                if (b_sum > 225) {b_sum = 255;} else if (b_sum < 0) {b_sum = 0;}
                b_sum = 255 - b_sum;

                int g_sum = (int)Math.abs(gX_sum) + (int)Math.abs(gY_sum);
                if (g_sum > 225) {g_sum = 255;} else if (g_sum < 0) {g_sum = 0;}
                g_sum = 255 - g_sum;

                int r_sum = (int)Math.abs(rX_sum) + (int)Math.abs(rY_sum);
                if (r_sum > 225) {r_sum = 255;} else if (r_sum < 0) {r_sum = 0;}
                r_sum = 255 - r_sum;

                b[i][j] = b_sum;
                g[i][j] = g_sum;
                r[i][j] = r_sum;

                image.setPixel(i, j, b[i][j], g[i][j], r[i][j], updated);
            }
        }

        return updated;
    }

}