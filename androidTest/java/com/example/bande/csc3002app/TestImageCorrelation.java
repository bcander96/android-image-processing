package com.example.bande.csc3002app;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.rule.ActivityTestRule;

import com.example.bande.csc3002app.correlation.ImageCorrelation;
import com.example.bande.csc3002app.correlation.ImageDatabase;
import com.example.bande.csc3002app.image.Image;
import com.example.bande.csc3002app.toolkit.Filter;
import com.example.bande.csc3002app.toolkit.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.fail;

public class TestImageCorrelation {

    @Rule
    public ActivityTestRule<StartupScreen> rule = new ActivityTestRule<StartupScreen>(
            StartupScreen.class);

    private Image image;
    private Bitmap bitmap = null;
    private ImageDatabase idb;
    private ImageCorrelation corr;

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
    public void testImageDatabaseConfiguration(){
        idb = new ImageDatabase(rule.getActivity());
        int [][][][] persons = idb.getPersonTrainingImages();

        if (persons == null) {
            fail("Array of database data is empty.");
        }

        for (int p = 0; p < persons.length; p ++) {

            if (persons.length == 0) { fail("No people in the database."); }

            for (int np = 0; np < persons[p].length; np ++) {

                if (persons[p].length == 0) { fail("No training images in the database."); }

            }
        }

    }

    @Test
    public void testImageCorrelation(){

        String expectedName = "Abdullah_Gul";

        idb = new ImageDatabase(rule.getActivity());
        corr = new ImageCorrelation(rule.getActivity(), image, 5,2);

        int matchIndex = corr.calculateImageCorrelation();

        String name = idb.getPersonName(matchIndex);

        if (!name.equals(expectedName)) {
            fail("Algorithm did not find correct match.");
        }

    }

    @After
    public void tearDown() throws Exception {
        image = null;
        bitmap = null;
        idb = null;
        corr = null;
    }
}