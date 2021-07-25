package com.example.bande.csc3002app;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.example.bande.csc3002app.image.Image;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

public class TestUserInterface {

    @Rule
    public ActivityTestRule<StartupScreen> activity = new ActivityTestRule<StartupScreen>(
            StartupScreen.class);

    // placeholders for user's image
    private Image image = null;
    private Bitmap bitmap = null;
    private Intent intent;

    @Before
    public void setUp() throws Exception {

        AssetManager am = activity.getActivity().getAssets();
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
    public void testLoadStartupScreen() {

        View view = activity.getActivity().findViewById(R.id.btnUploadImage);
        assertNotNull(view);

    }

    @Test
    public void testNavStartupToMainScreen() {

        intent = new Intent(activity.getActivity(), MainScreen.class);
        intent.putExtra("Bitmap", bitmap);

        ActivityTestRule<MainScreen> nextActivity = new ActivityTestRule<MainScreen>(
                MainScreen.class);

        nextActivity.launchActivity(intent);
        View view = nextActivity.getActivity().findViewById(R.id.btnAdjustImage);
        assertNotNull(view);

    }

    @Test
    public void testNavStartupToMainScreenToAdjustImage() {

        intent = new Intent(activity.getActivity(), MainScreen.class);
        intent.putExtra("Bitmap", bitmap);
        ActivityTestRule<MainScreen> secondActivity = new ActivityTestRule<MainScreen>(
                MainScreen.class);
        secondActivity.launchActivity(intent);

        intent = new Intent(secondActivity.getActivity(), AdjustImageScreen.class);
        intent.putExtra("Bitmap", bitmap);
        ActivityTestRule<AdjustImageScreen> thirdActivity = new ActivityTestRule<AdjustImageScreen>(
                AdjustImageScreen.class);
        thirdActivity.launchActivity(intent);

        View view = thirdActivity.getActivity().findViewById(R.id.btnImageProperties);
        assertNotNull(view);

    }

    @Test
    public void testNavStartupToMainScreenToAdjustImageToImageProperties() {

        intent = new Intent(activity.getActivity(), MainScreen.class);
        intent.putExtra("Bitmap", bitmap);
        ActivityTestRule<MainScreen> secondActivity = new ActivityTestRule<MainScreen>(
                MainScreen.class);
        secondActivity.launchActivity(intent);

        intent = new Intent(secondActivity.getActivity(), AdjustImageScreen.class);
        intent.putExtra("Bitmap", bitmap);
        ActivityTestRule<AdjustImageScreen> thirdActivity = new ActivityTestRule<AdjustImageScreen>(
                AdjustImageScreen.class);
        thirdActivity.launchActivity(intent);

        intent = new Intent(thirdActivity.getActivity(), ImagePropertiesScreen.class);
        intent.putExtra("Bitmap", bitmap);
        ActivityTestRule<ImagePropertiesScreen> fourthActivity = new ActivityTestRule<ImagePropertiesScreen>(
                ImagePropertiesScreen.class);
        fourthActivity.launchActivity(intent);

        View view = fourthActivity.getActivity().findViewById(R.id.btnIncreaseBrightness);
        assertNotNull(view);

    }

    @Test
    public void testNavStartupToMainScreenToAdjustImageToRGBScreen() {

        intent = new Intent(activity.getActivity(), MainScreen.class);
        intent.putExtra("Bitmap", bitmap);
        ActivityTestRule<MainScreen> secondActivity = new ActivityTestRule<MainScreen>(
                MainScreen.class);
        secondActivity.launchActivity(intent);

        intent = new Intent(secondActivity.getActivity(), AdjustImageScreen.class);
        intent.putExtra("Bitmap", bitmap);
        ActivityTestRule<AdjustImageScreen> thirdActivity = new ActivityTestRule<AdjustImageScreen>(
                AdjustImageScreen.class);
        thirdActivity.launchActivity(intent);

        intent = new Intent(thirdActivity.getActivity(), RGBScreen.class);
        intent.putExtra("Bitmap", bitmap);
        ActivityTestRule<RGBScreen> fourthActivity = new ActivityTestRule<RGBScreen>(
                RGBScreen.class);
        fourthActivity.launchActivity(intent);

        View view = fourthActivity.getActivity().findViewById(R.id.btnIncreaseBlue);
        assertNotNull(view);

    }

    @Test
    public void testNavStartupToMainScreenToAdjustImageToFilters() {

        intent = new Intent(activity.getActivity(), MainScreen.class);
        intent.putExtra("Bitmap", bitmap);
        ActivityTestRule<MainScreen> secondActivity = new ActivityTestRule<MainScreen>(
                MainScreen.class);
        secondActivity.launchActivity(intent);

        intent = new Intent(secondActivity.getActivity(), AdjustImageScreen.class);
        intent.putExtra("Bitmap", bitmap);
        ActivityTestRule<AdjustImageScreen> thirdActivity = new ActivityTestRule<AdjustImageScreen>(
                AdjustImageScreen.class);
        thirdActivity.launchActivity(intent);

        intent = new Intent(thirdActivity.getActivity(), FiltersScreen.class);
        intent.putExtra("Bitmap", bitmap);
        ActivityTestRule<FiltersScreen> fourthActivity = new ActivityTestRule<FiltersScreen>(
                FiltersScreen.class);
        fourthActivity.launchActivity(intent);

        View view = fourthActivity.getActivity().findViewById(R.id.btnGaussianBlur);
        assertNotNull(view);

    }

    @Test
    public void testNavStartupToMainScreenToCorrelation() {

        intent = new Intent(activity.getActivity(), MainScreen.class);
        intent.putExtra("Bitmap", bitmap);
        ActivityTestRule<MainScreen> secondActivity = new ActivityTestRule<MainScreen>(
                MainScreen.class);
        secondActivity.launchActivity(intent);

        intent = new Intent(secondActivity.getActivity(), CorrelationScreen.class);
        intent.putExtra("Bitmap", bitmap);
        ActivityTestRule<CorrelationScreen> thirdActivity = new ActivityTestRule<CorrelationScreen>(
                CorrelationScreen.class);
        thirdActivity.launchActivity(intent);

        View view = thirdActivity.getActivity().findViewById(R.id.btnCorrelation);
        assertNotNull(view);

    }

    @After
    public void tearDown() throws Exception {
        image = null;
        bitmap = null;
        intent = null;

        activity = null;

    }
}