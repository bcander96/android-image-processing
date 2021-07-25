package com.example.bande.csc3002app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.example.bande.csc3002app.toolkit.Filter;
import com.example.bande.csc3002app.image.Image;
import com.example.bande.csc3002app.toolkit.Properties;

/**
 * The FiltersScreen class is an android activity
 * used for managing the user interface functionality of the
 * application.
 */
public class FiltersScreen extends Activity {

    // variable declarations
    ImageView imageView;
    Intent intent;
    Bitmap bitmap;

    // image object used to control bitmap
    Image image;
    // filter object allows filters to be added to the image
    Filter filter;
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
        setContentView(R.layout.activity_filters_screen);

        // load the bitmap from the previous activity
        bitmap = (Bitmap)this.getIntent().getParcelableExtra("Bitmap");

        // load the bitmap into the preview
        imageView = (ImageView)findViewById(R.id.imagePreview);
        imageView.setImageBitmap(bitmap);

    }

    /**
     * Button listener which when clicked converts the
     * current user image to greyscale and portrays the
     * changes in the preview.
     * @param view
     */
    public void onClickFilterGreyscale(View view){
        image = new Image(bitmap);
        imageProp = new Properties(image);
        bitmap = imageProp.convertToGreyscale();
        imageView.setImageBitmap(bitmap);
        System.out.println("Filter greyscale button clicked.");
    }

    /**
     * Button listener which when clicked adds noise
     * to the image and portrays the changes in the
     * preview.
     * @param view
     */
    public void onClickFilterNoise(View view){
        image = new Image(bitmap);
        filter = new Filter(image);
        bitmap = filter.addNoise();
        imageView.setImageBitmap(bitmap);
        System.out.println("Filter noise button clicked.");
    }

    /**
     * Button listener which when clicked adds blur
     * to the image and portrays the changes in the
     * preview.
     * Passing true to the addBlur() method indicates gaussian blur,
     * false indicates uniform blur.
     * @param view
     */
    public void onClickFilterBlurGauss(View view){
        image = new Image(bitmap);
        filter = new Filter(image);
        bitmap = filter.addBlur(true);
        imageView.setImageBitmap(bitmap);
        System.out.println("Filter blur button clicked.");
    }

    /**
     * Button listener which when clicked adds blur
     * to the image and portrays the changes in the
     * preview.
     * Passing true to the addBlur() method indicates gaussian blur,
     * false indicates uniform blur.
     * @param view
     */
    public void onClickFilterBlurUniform(View view){
        image = new Image(bitmap);
        filter = new Filter(image);
        bitmap = filter.addBlur(false);
        imageView.setImageBitmap(bitmap);
        System.out.println("Filter blur button clicked.");
    }

    /**
     * Button listener which when clicked carries out
     * edge detection on the given image and portrays
     * the edges on the preview.
     * @param view
     */
    public void onClickFilterEdge(View view){
        image = new Image(bitmap);
        filter = new Filter(image);
        bitmap = filter.edgeDetection();
        imageView.setImageBitmap(bitmap);
        System.out.println("Filter edge button clicked.");
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
