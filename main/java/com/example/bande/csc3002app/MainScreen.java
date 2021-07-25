package com.example.bande.csc3002app;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bande.csc3002app.image.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * The MainScreen class is an android activity
 * used for managing the user interface functionality of the
 * application.
 */
public class MainScreen extends Activity {

    // variable declarations
    ImageView imageView;
    TextView imageResolution;
    TextView imageFileSize;
    Intent intent;
    Bitmap bitmap;
    public Bitmap bitmapReset;
    Image image;

    // used for resetting the bitmap
    public static MainScreen backup = new MainScreen();

    /**
     * On create method of this activity used to construct the
     * activity as it loads.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // load the bitmap from the previous activity
        bitmap = (Bitmap)this.getIntent().getParcelableExtra("Bitmap");
        image = new Image(bitmap);

        // load the bitmap into the preview
        imageView = (ImageView)findViewById(R.id.imagePreview);
        imageView.setImageBitmap(bitmap);

        // load image stats
        imageResolution = (TextView) findViewById(R.id.imageResolution);
        imageFileSize = (TextView) findViewById(R.id.imageFileSize);
        imageResolution.setText(image.getWidth()+"x"+image.getHeight());
        imageFileSize.setText(Integer.toString(image.getByteCount())+"kB");

        // check the current memory usage of the app - debugging purposes
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / 1048576L;
        System.out.println("MEMORY = " + availableMegs);

        // end the startup screen activity to free memory
        StartupScreen.s.finish();
    }

    /**
     * Button listener which when clicked brings the
     * user to the AdjustImageScreen.
     * @param view
     */
    public void onClickAdjustImage(View view){
        backup.bitmapReset = bitmap; // save an image backup
        image = new Image(bitmap);
        intent = new Intent(this, AdjustImageScreen.class);
        intent.putExtra("Bitmap", bitmap);
        startActivity(intent);
        System.out.println("Adjust Image Button Clicked");
    }

    /**
     * Button listener which when clicked brings the
     * user to the CorrelationScreen.
     * @param view
     */
    public void onClickDetectFace(View view){
        image = new Image(bitmap);
        intent = new Intent(this, CorrelationScreen.class);
        intent.putExtra("Bitmap", bitmap);
        startActivity(intent);
        System.out.println("Detect Face Button Clicked");
    }

    /**
     * Button listener which when clicked saves the current users
     * image to the devices storage at directory /imgToolbox/.
     * @param view
     */
    public void onClickSaveToDevice(View view){

        // get permissions to access devices storage
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

        // load the bitmap into an image object
        image = new Image(bitmap);

        // if the apps image directory does not exist, create it
        String path = Environment.getExternalStorageDirectory().toString();
        OutputStream fout;
        File file = new File(path, "/imgToolbox/");
        if (!file.exists()) {
            file.mkdirs();
        }

        // dynamically choose an image name
        int increment = 0;
        file = new File(path, "/imgToolbox/"+"image_" + increment + ".jpg");
        while (file.exists()) {
            increment ++;
            file = new File(path, "/imgToolbox/"+"image_" + increment + ".jpg");
        }

        // try to save the image
        try {
            fout = new FileOutputStream(file);

            image.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, fout);

            fout.flush();
            fout.close();

            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
            Toast.makeText(this, "Image saved.", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Save Failed", Toast.LENGTH_LONG).show();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Save Failed", Toast.LENGTH_LONG).show();
            return;
        }

        System.out.println("Save image to device button clicked");
    }

    /**
     * Button listener which when clicked brings the user to
     * the StartupScreen to load a new image.
     * @param view
     */
    public void onClickNewImage(View view){
        intent = new Intent(this, StartupScreen.class);
        startActivity(intent);
    }

}
