package com.example.bande.csc3002app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.example.bande.csc3002app.image.Image;
import com.example.bande.csc3002app.toolkit.Properties;

/**
 * The RGBScreen class is an android activity
 * used for managing the user interface functionality of the
 * application.
 */
public class RGBScreen extends Activity {

    // variable declarations
    ImageView imageView;
    Intent intent;
    Bitmap bitmap;
    Image image;
    Properties properties;

    /**
     * On create method of this activity used to construct the
     * activity as it loads.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgb_screen);

        // the users bitmap passed to this activity
        bitmap = (Bitmap)this.getIntent().getParcelableExtra("Bitmap");

        // load the bitmap to the image view
        imageView = (ImageView)findViewById(R.id.imagePreview);
        imageView.setImageBitmap(bitmap);

    }

    /**
     * Button listener which when clicked decreases
     * the image red pixel ratio and portrays the changes
     * made to the image in the preview.
     * @param view
     */
    public void onClickDecreaseRed(View view){
        image = new Image(bitmap);
        properties = new Properties(image);
        bitmap = properties.adjustRGB(false, 'r');
        imageView.setImageBitmap(bitmap);
        System.out.println("Decrease Red Button Clicked");
    }

    /**
     * Button listener which when clicked increases
     * the image red pixel ratio and portrays the changes
     * made to the image in the preview.
     * @param view
     */
    public void onClickIncreaseRed(View view){
        image = new Image(bitmap);
        properties = new Properties(image);
        bitmap = properties.adjustRGB(true, 'r');
        imageView.setImageBitmap(bitmap);
        System.out.println("Increase Red Button Clicked");
    }

    /**
     * Button listener which when clicked decreases
     * the image green pixel ratio and portrays the changes
     * made to the image in the preview.
     * @param view
     */
    public void onClickDecreaseGreen(View view){
        image = new Image(bitmap);
        properties = new Properties(image);
        bitmap = properties.adjustRGB(false, 'g');
        imageView.setImageBitmap(bitmap);
        System.out.println("Decrease Green Button Clicked");
    }

    /**
     * Button listener which when clicked increases
     * the image green pixel ratio and portrays the changes
     * made to the image in the preview.
     * @param view
     */
    public void onClickIncreaseGreen(View view){
        image = new Image(bitmap);
        properties = new Properties(image);
        bitmap = properties.adjustRGB(true, 'g');
        imageView.setImageBitmap(bitmap);
        System.out.println("Increase Green Button Clicked");
    }

    /**
     * Button listener which when clicked decreases
     * the image blue pixel ratio and portrays the changes
     * made to the image in the preview.
     * @param view
     */
    public void onClickDecreaseBlue(View view){
        image = new Image(bitmap);
        properties = new Properties(image);
        bitmap = properties.adjustRGB(false, 'b');
        imageView.setImageBitmap(bitmap);
        System.out.println("Decrease Blue Button Clicked");
    }

    /**
     * Button listener which when clicked increases
     * the image blue pixel ratio and portrays the changes
     * made to the image in the preview.
     * @param view
     */
    public void onClickIncreaseBlue(View view){
        image = new Image(bitmap);
        properties = new Properties(image);
        bitmap = properties.adjustRGB(true, 'b');
        imageView.setImageBitmap(bitmap);
        System.out.println("Increase Blue Button Clicked");
    }

    /**
     * Button listener which when clicked brings the user
     * back to the AdjustImageScreen.
     * @param view
     */
    public void onClickGoBack(View view){
        intent = new Intent(this, AdjustImageScreen.class);
        intent.putExtra("Bitmap", bitmap);
        startActivity(intent);
        System.out.println("Go back button clicked.");
    }

}
