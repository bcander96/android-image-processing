package com.example.bande.csc3002app;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.rule.ActivityTestRule;

import com.example.bande.csc3002app.image.Image;
import com.example.bande.csc3002app.toolkit.Filter;
import com.example.bande.csc3002app.toolkit.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class TestImageToolkit {

    @Rule
    public ActivityTestRule<StartupScreen> rule = new ActivityTestRule<StartupScreen>(
            StartupScreen.class);

    private Image image;
    private Bitmap bitmap = null;

    private Bitmap changedBitmap;
    private Image changedImage;

    private StartupScreen screen = null;

    Properties imgProperties;
    Filter imgFilter;

    @Before
    public void setUp() throws Exception {

        screen = rule.getActivity();

        AssetManager am = screen.getAssets();
        InputStream is;
        try {
            is = am.open("Database/Abdullah_Gul/Abdullah_Gul_0001.bmp");
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            fail("The bitmap could not be loaded.");
        }
        try {
            image = new Image(bitmap);
        } catch (Exception e) {
            fail("The image object could not be initialised.");
        }

    }

    // Properties

    @Test
    public void testImageBrightnessInc() {

        int [][] expectedBlue = new int[image.getWidth()][image.getHeight()];
        int [][] expectedGreen = new int[image.getWidth()][image.getHeight()];
        int [][] expectedRed = new int[image.getWidth()][image.getHeight()];

        int change = 10;

        // modify the image
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                expectedBlue[i][j] = (image.getBlueData(i, j) + change);
                expectedGreen[i][j] = (image.getGreenData(i, j) + change);
                expectedRed[i][j] = (image.getRedData(i, j) + change);

                if (expectedBlue[i][j] < 0) expectedBlue[i][j] = 0;
                if (expectedBlue[i][j] > 255) expectedBlue[i][j] = 255;
                if (expectedGreen[i][j] < 0) expectedGreen[i][j] = 0;
                if (expectedGreen[i][j] > 255) expectedGreen[i][j] = 255;
                if (expectedRed[i][j] < 0) expectedRed[i][j] = 0;
                if (expectedRed[i][j] > 255) expectedRed[i][j] = 255;
            }
        }

        imgProperties = new Properties(image);

        changedBitmap = imgProperties.adjustBrightness(true);
        changedImage = new Image(changedBitmap);

        compareResults(expectedBlue, expectedGreen, expectedRed, null, null, null);
    }

    @Test
    public void testImageBrightnessDec() {

        int [][] expectedBlue = new int[image.getWidth()][image.getHeight()];
        int [][] expectedGreen = new int[image.getWidth()][image.getHeight()];
        int [][] expectedRed = new int[image.getWidth()][image.getHeight()];

        int change = -10;

        // modify the image
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                expectedBlue[i][j] = (image.getBlueData(i, j) + change);
                expectedGreen[i][j] = (image.getGreenData(i, j) + change);
                expectedRed[i][j] = (image.getRedData(i, j) + change);

                if (expectedBlue[i][j] < 0) expectedBlue[i][j] = 0;
                if (expectedBlue[i][j] > 255) expectedBlue[i][j] = 255;
                if (expectedGreen[i][j] < 0) expectedGreen[i][j] = 0;
                if (expectedGreen[i][j] > 255) expectedGreen[i][j] = 255;
                if (expectedRed[i][j] < 0) expectedRed[i][j] = 0;
                if (expectedRed[i][j] > 255) expectedRed[i][j] = 255;
            }
        }

        imgProperties = new Properties(image);

        changedBitmap = imgProperties.adjustBrightness(false);
        changedImage = new Image(changedBitmap);

        compareResults(expectedBlue, expectedGreen, expectedRed, null, null, null);
    }

    @Test
    public void testImageContrastInc() {

        int [][] expectedBlue = new int[image.getWidth()][image.getHeight()];
        int [][] expectedGreen = new int[image.getWidth()][image.getHeight()];
        int [][] expectedRed = new int[image.getWidth()][image.getHeight()];

        double change = 5;
        double contrast = Math.pow((100 + change) / 100, 2);

        // modify the image
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // get the pixel value at (i, j)
                expectedBlue[i][j] = (int)(((((image.getBlueData(i, j) / 255.0) - 0.5)
                        * contrast) + 0.5) * 255.0);
                expectedGreen[i][j] = (int)(((((image.getGreenData(i, j) / 255.0) - 0.5)
                        * contrast) + 0.5) * 255.0);
                expectedRed[i][j] = (int)(((((image.getRedData(i, j) / 255.0) - 0.5)
                        * contrast) + 0.5) * 255.0);

                if (expectedBlue[i][j] < 0) expectedBlue[i][j] = 0;
                if (expectedBlue[i][j] > 255) expectedBlue[i][j] = 255;
                if (expectedGreen[i][j] < 0) expectedGreen[i][j] = 0;
                if (expectedGreen[i][j] > 255) expectedGreen[i][j] = 255;
                if (expectedRed[i][j] < 0) expectedRed[i][j] = 0;
                if (expectedRed[i][j] > 255) expectedRed[i][j] = 255;
            }
        }

        imgProperties = new Properties(image);

        changedBitmap = imgProperties.adjustContrast(true);
        changedImage = new Image(changedBitmap);

        compareResults(expectedBlue, expectedGreen, expectedRed, null, null, null);
    }

    @Test
    public void testImageContrastDec() {

        int [][] expectedBlue = new int[image.getWidth()][image.getHeight()];
        int [][] expectedGreen = new int[image.getWidth()][image.getHeight()];
        int [][] expectedRed = new int[image.getWidth()][image.getHeight()];

        double change = -5;
        double contrast = Math.pow((100 + change) / 100, 2);

        // modify the image
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // get the pixel value at (i, j)
                expectedBlue[i][j] = (int)(((((image.getBlueData(i, j) / 255.0) - 0.5)
                        * contrast) + 0.5) * 255.0);
                expectedGreen[i][j] = (int)(((((image.getGreenData(i, j) / 255.0) - 0.5)
                        * contrast) + 0.5) * 255.0);
                expectedRed[i][j] = (int)(((((image.getRedData(i, j) / 255.0) - 0.5)
                        * contrast) + 0.5) * 255.0);

                if (expectedBlue[i][j] < 0) expectedBlue[i][j] = 0;
                if (expectedBlue[i][j] > 255) expectedBlue[i][j] = 255;
                if (expectedGreen[i][j] < 0) expectedGreen[i][j] = 0;
                if (expectedGreen[i][j] > 255) expectedGreen[i][j] = 255;
                if (expectedRed[i][j] < 0) expectedRed[i][j] = 0;
                if (expectedRed[i][j] > 255) expectedRed[i][j] = 255;
            }
        }

        imgProperties = new Properties(image);

        changedBitmap = imgProperties.adjustContrast(false);
        changedImage = new Image(changedBitmap);

        compareResults(expectedBlue, expectedGreen, expectedRed, null, null, null);
    }

    @Test
    public void testImageExposureInc() {

        int [][] expectedBlue = new int[image.getWidth()][image.getHeight()];
        int [][] expectedGreen = new int[image.getWidth()][image.getHeight()];
        int [][] expectedRed = new int[image.getWidth()][image.getHeight()];

        double change = 0.1;
        double exposureFactor = Math.pow(2.0f, change);

        // modify the image
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // get the pixel value at (i, j)
                expectedBlue[i][j] = image.getBlueData(i, j);
                expectedGreen[i][j] = image.getGreenData(i, j);
                expectedRed[i][j] = image.getRedData(i, j);
                // multiply by the exposure factor
                expectedBlue[i][j] *= exposureFactor;
                expectedGreen[i][j] *= exposureFactor;
                expectedRed[i][j] *= exposureFactor;

                if (expectedBlue[i][j] < 0) expectedBlue[i][j] = 0;
                if (expectedBlue[i][j] > 255) expectedBlue[i][j] = 255;
                if (expectedGreen[i][j] < 0) expectedGreen[i][j] = 0;
                if (expectedGreen[i][j] > 255) expectedGreen[i][j] = 255;
                if (expectedRed[i][j] < 0) expectedRed[i][j] = 0;
                if (expectedRed[i][j] > 255) expectedRed[i][j] = 255;
            }
        }

        imgProperties = new Properties(image);

        changedBitmap = imgProperties.adjustExposure(true);
        changedImage = new Image(changedBitmap);

        compareResults(expectedBlue, expectedGreen, expectedRed, null, null, null);
    }

    @Test
    public void testImageExposureDec() {

        int [][] expectedBlue = new int[image.getWidth()][image.getHeight()];
        int [][] expectedGreen = new int[image.getWidth()][image.getHeight()];
        int [][] expectedRed = new int[image.getWidth()][image.getHeight()];

        double change = -0.1;
        double exposureFactor = Math.pow(2.0f, change);

        // modify the image
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // get the pixel value at (i, j)
                expectedBlue[i][j] = image.getBlueData(i, j);
                expectedGreen[i][j] = image.getGreenData(i, j);
                expectedRed[i][j] = image.getRedData(i, j);
                // multiply by the exposure factor
                expectedBlue[i][j] *= exposureFactor;
                expectedGreen[i][j] *= exposureFactor;
                expectedRed[i][j] *= exposureFactor;

                if (expectedBlue[i][j] < 0) expectedBlue[i][j] = 0;
                if (expectedBlue[i][j] > 255) expectedBlue[i][j] = 255;
                if (expectedGreen[i][j] < 0) expectedGreen[i][j] = 0;
                if (expectedGreen[i][j] > 255) expectedGreen[i][j] = 255;
                if (expectedRed[i][j] < 0) expectedRed[i][j] = 0;
                if (expectedRed[i][j] > 255) expectedRed[i][j] = 255;
            }
        }

        imgProperties = new Properties(image);

        changedBitmap = imgProperties.adjustExposure(false);
        changedImage = new Image(changedBitmap);

        compareResults(expectedBlue, expectedGreen, expectedRed, null, null, null);
    }

    @Test
    public void testImageSaturationInc() {

        float [][] expectedBlue = new float[image.getWidth()][image.getHeight()];
        float [][] expectedGreen = new float[image.getWidth()][image.getHeight()];
        float [][] expectedRed = new float[image.getWidth()][image.getHeight()];

        float change = 20f;
        float saturationFactor = (255.0f + change) / 255.0f;

        // modify the image
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // get the pixel value at (i, j)
                expectedBlue[i][j] = (float) image.getBlueData(i, j);
                expectedGreen[i][j] = (float) image.getGreenData(i, j);
                expectedRed[i][j] = (float) image.getRedData(i, j);
                // adjust the saturation of each colour channel
                float lum = (expectedBlue[i][j] * 0.11f) + (expectedGreen[i][j] * 0.59f)
                        + (expectedRed[i][j] * 0.3f);
                expectedBlue[i][j] = lum * (1.0f - saturationFactor) + expectedBlue[i][j]
                        * saturationFactor;
                expectedGreen[i][j] = lum * (1.0f - saturationFactor) + expectedGreen[i][j]
                        * saturationFactor;
                expectedRed[i][j] = lum * (1.0f - saturationFactor) + expectedRed[i][j]
                        * saturationFactor;

                if (expectedBlue[i][j] < 0) expectedBlue[i][j] = 0;
                if (expectedBlue[i][j] > 255) expectedBlue[i][j] = 255;
                if (expectedGreen[i][j] < 0) expectedGreen[i][j] = 0;
                if (expectedGreen[i][j] > 255) expectedGreen[i][j] = 255;
                if (expectedRed[i][j] < 0) expectedRed[i][j] = 0;
                if (expectedRed[i][j] > 255) expectedRed[i][j] = 255;
            }
        }

        imgProperties = new Properties(image);

        changedBitmap = imgProperties.adjustSaturation(true);
        changedImage = new Image(changedBitmap);

        compareResults(null, null, null, expectedBlue, expectedGreen, expectedRed);
    }

    @Test
    public void testImageSaturationDec() {

        float [][] expectedBlue = new float[image.getWidth()][image.getHeight()];
        float [][] expectedGreen = new float[image.getWidth()][image.getHeight()];
        float [][] expectedRed = new float[image.getWidth()][image.getHeight()];

        float change = -20f;
        float saturationFactor = (255.0f + change) / 255.0f;

        // modify the image
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // get the pixel value at (i, j)
                expectedBlue[i][j] = (float) image.getBlueData(i, j);
                expectedGreen[i][j] = (float) image.getGreenData(i, j);
                expectedRed[i][j] = (float) image.getRedData(i, j);
                // adjust the saturation of each colour channel
                float lum = (expectedBlue[i][j] * 0.11f) + (expectedGreen[i][j] * 0.59f)
                        + (expectedRed[i][j] * 0.3f);
                expectedBlue[i][j] = lum * (1.0f - saturationFactor) + expectedBlue[i][j]
                        * saturationFactor;
                expectedGreen[i][j] = lum * (1.0f - saturationFactor) + expectedGreen[i][j]
                        * saturationFactor;
                expectedRed[i][j] = lum * (1.0f - saturationFactor) + expectedRed[i][j]
                        * saturationFactor;

                if (expectedBlue[i][j] < 0) expectedBlue[i][j] = 0;
                if (expectedBlue[i][j] > 255) expectedBlue[i][j] = 255;
                if (expectedGreen[i][j] < 0) expectedGreen[i][j] = 0;
                if (expectedGreen[i][j] > 255) expectedGreen[i][j] = 255;
                if (expectedRed[i][j] < 0) expectedRed[i][j] = 0;
                if (expectedRed[i][j] > 255) expectedRed[i][j] = 255;
            }
        }

        imgProperties = new Properties(image);

        changedBitmap = imgProperties.adjustSaturation(false);
        changedImage = new Image(changedBitmap);

        compareResults(null, null, null, expectedBlue, expectedGreen, expectedRed);
    }

    @Test
    public void testImageConvertToGreyscale() {

        int [][] expectedBlue = new int[image.getWidth()][image.getHeight()];
        int [][] expectedGreen = new int[image.getWidth()][image.getHeight()];
        int [][] expectedRed = new int[image.getWidth()][image.getHeight()];

        // modify the image
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // get the pixel value at (i, j)
                expectedBlue[i][j] = image.getBlueData(i, j);
                expectedGreen[i][j] = image.getGreenData(i, j);
                expectedRed[i][j] = image.getRedData(i, j);

                // take conversion up to one single value
                expectedRed[i][j] = expectedGreen[i][j] = expectedBlue[i][j] = (int)(0.299
                        * expectedRed[i][j] + 0.587 * expectedGreen[i][j] + 0.114
                        * expectedBlue[i][j]);

                if (expectedBlue[i][j] < 0) expectedBlue[i][j] = 0;
                if (expectedBlue[i][j] > 255) expectedBlue[i][j] = 255;
                if (expectedGreen[i][j] < 0) expectedGreen[i][j] = 0;
                if (expectedGreen[i][j] > 255) expectedGreen[i][j] = 255;
                if (expectedRed[i][j] < 0) expectedRed[i][j] = 0;
                if (expectedRed[i][j] > 255) expectedRed[i][j] = 255;
            }
        }

        imgProperties = new Properties(image);

        changedBitmap = imgProperties.convertToGreyscale();
        changedImage = new Image(changedBitmap);

        compareResults(expectedBlue, expectedGreen, expectedRed, null, null, null);

    }

    void compareResults(int [][] b, int [][] g, int [][] r,
                           float [][] fB, float [][] fG, float [][] fR) {

        if (b != null && g != null && r != null) {
            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++) {

                    if (b[i][j] != changedImage.getBlueData(i, j)){
                        fail("Not all blue channel values have changed as expected.");
                    }
                    if (g[i][j] != changedImage.getGreenData(i, j)){
                        fail("Not all green channel values have changed as expected.");
                    }
                    if (r[i][j] != changedImage.getRedData(i, j)){
                        fail("Not all red channel values have changed as expected.");
                    }

                }
            }
        }

        else if (fB != null && fG != null && fR == null) {
            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++) {

                    if ((int)fB[i][j] != changedImage.getBlueData(i, j)){
                        fail("Not all blue channel values have changed as expected.");
                    }
                    if ((int)fG[i][j] != changedImage.getGreenData(i, j)){
                        fail("Not all green channel values have changed as expected.");
                    }
                    if ((int)fR[i][j] != changedImage.getRedData(i, j)){
                        fail("Not all red channel values have changed as expected.");
                    }

                }
            }
        }

    }

    // Filters

    // todo: run method, check is not null, check values aren't the same

    @Test
    public void testImageNoise() {

        imgFilter = new Filter(image);
        changedBitmap = imgFilter.addNoise();
        changedImage = new Image(changedBitmap);

        if (changedImage.getBitmap() == null) {
            fail("Bitmap returned from toolkit function was null.");
        }

        searchForSimilarities(image, changedImage);

    }

    @Test
    public void testImageGaussianBlur() {

        imgFilter = new Filter(image);
        changedBitmap = imgFilter.addBlur(true);
        changedImage = new Image(changedBitmap);

        if (changedImage.getBitmap() == null) {
            fail("Bitmap returned from toolkit function was null.");
        }

        searchForSimilarities(image, changedImage);

    }

    @Test
    public void testImageUniformBlur() {

        imgFilter = new Filter(image);
        changedBitmap = imgFilter.addBlur(false);
        changedImage = new Image(changedBitmap);

        if (changedImage.getBitmap() == null) {
            fail("Bitmap returned from toolkit function was null.");
        }

        searchForSimilarities(image, changedImage);

    }

    @Test
    public void testImageEdgeDetection() {

        imgFilter = new Filter(image);
        changedBitmap = imgFilter.edgeDetection();
        changedImage = new Image(changedBitmap);

        if (changedImage.getBitmap() == null) {
            fail("Bitmap returned from toolkit function was null.");
        }

        searchForSimilarities(image, changedImage);

    }

    void searchForSimilarities(Image image, Image changed) {
        int matches = 0;
        int maxMatches = 0;

        for (int i = 0; i < image.getWidth(); i ++) {
            for (int j = 0; j < image.getHeight(); j ++) {

                if (image.getBlueData(i, j) == changedImage.getBlueData(i, j) &&
                        image.getRedData(i, j) == changedImage.getRedData(i, j) &&
                        image.getGreenData(i, j) == changedImage.getGreenData(i, j)) {
                    matches ++;
                }
                maxMatches ++;
            }
        }

        if (matches == maxMatches) {
            fail("Image pixel values did not change enough to justify to a pass.");
        }
    }

    @After
    public void tearDown() throws Exception {
        image = null;
        bitmap = null;
        imgProperties = null;
        imgFilter = null;
    }
}