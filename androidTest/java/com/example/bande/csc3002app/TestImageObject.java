package com.example.bande.csc3002app;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.rule.ActivityTestRule;
import com.example.bande.csc3002app.image.Image;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

public class TestImageObject {

    @Rule
    public ActivityTestRule<StartupScreen> rule = new ActivityTestRule<StartupScreen>(
            StartupScreen.class);

    private Image image;
    private Bitmap bitmap = null;
    private StartupScreen screen = null;

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

    @Test
    public void testImageBlueDataLoaded() {

        if (image.blueChannel == null) {
            fail("The blue channel is null.");
        }

        int [][] tempData = new int[64][64];

        int nullCount = 0;
        for (int i = 0; i < 64; i ++){
            for (int j = 0; j < 64; j ++){

                tempData[i][j] = image.getBlueData(i, j);
                if (tempData[i][j] == 0) {
                    nullCount ++;
                }
            }
        }

        if (nullCount == 64) {
            fail("The image data has not been initialised correctly. " +
                    "Too many null values.");
        }

    }

    @Test
    public void testImageGreenDataLoaded() {

        if (image.greenChannel == null) {
            fail("The green channel is null.");
        }

        int [][] tempData = new int[64][64];

        int nullCount = 0;
        for (int i = 0; i < 64; i ++){
            for (int j = 0; j < 64; j ++){

                tempData[i][j] = image.getGreenData(i, j);
                if (tempData[i][j] == 0) {
                    nullCount ++;
                }
            }
        }

        if (nullCount == 64) {
            fail("The image data has not been initialised correctly. " +
                    "Too many null values.");
        }

    }

    @Test
    public void testImageRedDataLoaded() {

        if (image.redChannel == null) {
            fail("The red channel is null.");
        }

        int [][] tempData = new int[64][64];

        int nullCount = 0;
        for (int i = 0; i < 64; i ++){
            for (int j = 0; j < 64; j ++){

                tempData[i][j] = image.getRedData(i, j);
                if (tempData[i][j] == 0) {
                    nullCount ++;
                }
            }
        }

        if (nullCount == 64) {
            fail("The image data has not been initialised correctly. " +
                    "Too many null values.");
        }

    }

    @Test
    public void testCheckImageSize() {

        String expected = "64x64";
        String actual = "" + image.getWidth() + "x" + image.getHeight();
        assertEquals(expected, actual);

    }

    @After
    public void tearDown() throws Exception {
        image = null;
        bitmap = null;
    }
}