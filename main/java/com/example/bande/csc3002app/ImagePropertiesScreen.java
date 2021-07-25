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
 * The ImagePropertiesScreen class is an android activity
 * used for managing the user interface functionality of the
 * application.
 */
public class ImagePropertiesScreen extends Activity {

    // variable declarations
    ImageView imageView;
    Intent intent;
    Bitmap bitmap;

    // image object used to control bitmap
    Image image;
    // properties object allows various settings to be altered
    Properties imageProp;

    /**
     * On create method of this activity used to construct the
     * activity as it loads.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_properties_screen);

        // load the bitmap from the previous activity
        bitmap = (Bitmap)this.getIntent().getParcelableExtra("Bitmap");

        // load the bitmap into the preview
        imageView = (ImageView)findViewById(R.id.imagePreview);
        imageView.setImageBitmap(bitmap);

    }

    /**
     * Button listener which when clicked decreases
     * the image brightness and portrays the changes
     * made to the image in the preview.
     * @param view
     */
    public void onClickDecreaseBrightness(View view){
        image = new Image(bitmap);
        imageProp = new Properties(image);
        bitmap = imageProp.adjustBrightness(false);
        imageView.setImageBitmap(bitmap);
        System.out.println("Decrease Brightness Button Clicked");
    }

    /**
     * Button listener which when clicked increase
     * the image brightness and portrays the changes
     * made to the image in the preview.
     * @param view
     */
    public void onClickIncreaseBrightness(View view){
        image = new Image(bitmap);
        imageProp = new Properties(image);
        bitmap = imageProp.adjustBrightness(true);
        imageView.setImageBitmap(bitmap);
        System.out.println("Increase Brightness Button Clicked");
    }

    /**
     * Button listener which when clicked decreases
     * the image contrast and portrays the changes
     * made to the image in the preview.
     * @param view
     */
    public void onClickDecreaseContrast(View view){
        image = new Image(bitmap);
        imageProp = new Properties(image);
        bitmap = imageProp.adjustContrast(false);
        imageView.setImageBitmap(bitmap);
        System.out.println("Decrease Contrast Button Clicked");
    }

    /**
     * Button listener which when clicked increases
     * the image contrast and portrays the changes
     * made to the image in the preview.
     * @param view
     */
    public void onClickIncreaseContrast(View view){
        image = new Image(bitmap);
        imageProp = new Properties(image);
        bitmap = imageProp.adjustContrast(true);
        imageView.setImageBitmap(bitmap);
        System.out.println("Increase Contrast Button Clicked");
    }

    /**
     * Button listener which when clicked decreases
     * the image exposure and portrays the changes
     * made to the image in the preview.
     * @param view
     */
    public void onClickDecreaseExposure(View view){
        image = new Image(bitmap);
        imageProp = new Properties(image);
        bitmap = imageProp.adjustExposure(false);
        imageView.setImageBitmap(bitmap);
        System.out.println("Decrease Exposure Button Clicked");
    }

    /**
     * Button listener which when clicked increases
     * the image exposure and portrays the changes
     * made to the image in the preview.
     * @param view
     */
    public void onClickIncreaseExposure(View view){
        image = new Image(bitmap);
        imageProp = new Properties(image);
        bitmap = imageProp.adjustExposure(true);
        imageView.setImageBitmap(bitmap);
        System.out.println("Increase Exposure Button Clicked");
    }

    /**
     * Button listener which when clicked decreases
     * the image saturation and portrays the changes
     * made to the image in the preview.
     * @param view
     */
    public void onClickDecreaseSaturation(View view){
        image = new Image(bitmap);
        imageProp = new Properties(image);
        bitmap = imageProp.adjustSaturation(false);
        imageView.setImageBitmap(bitmap);
        System.out.println("Decrease Saturation Button Clicked");
    }

    /**
     * Button listener which when clicked increases
     * the image saturation and portrays the changes
     * made to the image in the preview.
     * @param view
     */
    public void onClickIncreaseSaturation(View view){
        image = new Image(bitmap);
        imageProp = new Properties(image);
        bitmap = imageProp.adjustSaturation(true);
        imageView.setImageBitmap(bitmap);
        System.out.println("Increase Saturation Button Clicked");
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
