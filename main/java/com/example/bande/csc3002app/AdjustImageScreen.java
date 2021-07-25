package com.example.bande.csc3002app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * The AdjustImageScreen class is an android activity
 * used for managing the user interface functionality of the
 * application.
 */
public class AdjustImageScreen extends Activity {

    // variable declarations
    ImageView imageView;
    Intent intent;
    Bitmap bitmap;

    /**
     * On create method of this activity used to construct the
     * activity as it loads.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust_image_screen);

        // the users bitmap passed to this activity
        bitmap = (Bitmap)this.getIntent().getParcelableExtra("Bitmap");

        // load the bitmap to the image view
        imageView = (ImageView)findViewById(R.id.imagePreview);
        imageView.setImageBitmap(bitmap);

    }

    /**
     * Button listener which when clicked brings the
     * user to the ImageProperties screen.
     * @param view
     */
    public void onClickImageProperties(View view){
        intent = new Intent(this, ImagePropertiesScreen.class);
        intent.putExtra("Bitmap", bitmap);
        startActivity(intent);
        System.out.println("Image properties button clicked.");
    }

    /**
     * Button listener which when clicked brings the
     * user to the RGB adjustment screen.
     * @param view
     */
    public void onClickRGBValues(View view){
        intent = new Intent(this, RGBScreen.class);
        intent.putExtra("Bitmap", bitmap);
        startActivity(intent);
        System.out.println("RGB values button clicked.");
    }

    /**
     * Button listener which when clicked brings the
     * user to the Image Filters screen.
     * @param view
     */
    public void onClickFilters(View view){
        intent = new Intent(this, FiltersScreen.class);
        intent.putExtra("Bitmap", bitmap);
        startActivity(intent);
        System.out.println("Filters button clicked.");
    }

    /**
     * Button listener which when clicked resets all changes
     * made to the image to the original loaded image.
     * @param view
     */
    public void onClickResetImage(View view){
        bitmap = MainScreen.backup.bitmapReset;
        imageView.setImageBitmap(bitmap);
        System.out.println("Reset image button clicked.");
    }

    /**
     * Button listener which when clicked saves all changes made
     * to the image and returns the user to the main screen.
     * @param view
     */
    public void onClickSaveChanges(View view){
        intent = new Intent(this, MainScreen.class);
        intent.putExtra("Bitmap", bitmap);
        startActivity(intent);
        System.out.println("Save changes button clicked.");
    }
}
