package com.example.bande.csc3002app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * The StartupScreen class is an android activity
 * used for managing the user interface functionality of the
 * application. The startup screen loads the initial bitmap
 * chosen by the user into the system.
 */
public class StartupScreen extends Activity {

    // variable declarations
    public static Activity s; // finished in main screen creation
    Intent intent;
    Bitmap bitmap;
    private static int RESULT_LOAD_IMAGE = 0; // result for loading an image
    private static int RESULT_CAPT_IMAGE = 1; // result for capturing image

    /**
     * On create method of this activity used to construct the
     * activity as it loads.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_screen);

        // initialise the activity variable with this
        s = this;
    }

    /**
     * Button listener which when clicked brings the
     * devices camera to capture an image.
     * @param view
     */
    public void onClickCaptureImage(View view){
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RESULT_CAPT_IMAGE);
        System.out.println("Capture Image Button Clicked");
    }

    /**
     * Button listener which when clicked brings the
     * user to the devices gallery to load an image into
     * the application.
     * @param view
     */
    public void onClickUploadImage(View view){
        intent = new Intent(Intent.ACTION_PICK);
        File directory = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        String imagePath = directory.getPath();
        System.out.println(directory+"\n"+imagePath);
        Uri data = Uri.parse(imagePath);
        intent.setDataAndType(data, "image/*");
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
        System.out.println("Upload Image Button Clicked");
    }

    /**
     * Helper method used to maintain an image that is too large for
     * the allocated memory to handle. Results in a scaled down version
     * of the user's image whilst maintaing the aspect ratio.
     * @param bmp is the user's image.
     * @return the maintained version of the image.
     */
    Bitmap getMaintainedImage(Bitmap bmp) {
        // scale down the bitmap and maintain the aspect ratio
        if (bmp.getWidth() > 400 || bmp.getHeight() > 400) {
            if (bmp.getWidth() > 400){
                float aspectRatio = bmp.getWidth() /
                        (float) bmp.getHeight();
                int width = 400;
                int height = Math.round(width / aspectRatio);

                bmp = Bitmap.createScaledBitmap(bmp, width, height, false);
            }
            if (bmp.getHeight() > 400) {
                float aspectRatio = bmp.getWidth() /
                        (float) bmp.getHeight();
                int height = 400;
                int width = Math.round(height * aspectRatio);

                bmp = Bitmap.createScaledBitmap(bmp, width, height, false);
            }
        }
        return bmp;
    }

    /**
     * Activity result method. Checks the result of the button
     * listener to determine whether to open the device camera
     * or open the device image gallery so the user can load an
     * image into the application.
     * @param requestCode
     * @param resultCode
     * @param data
     * Throws an Exception if the bitmap is not found.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check the result of the activity
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri imageUri = data.getData();
            InputStream inputStream;

            boolean loaded = false;

            // load the bitmap chosen by the user
            try {

                // try to decode the image
                try {
                    inputStream = getContentResolver().openInputStream(imageUri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    loaded = true;
                } catch (OutOfMemoryError ofm) {
                    // image was too large
                    ofm.printStackTrace();
                    loaded = false;
                }

                // if the bitmap is too large load a scaled down version
                if (!loaded) {
                    inputStream = getContentResolver().openInputStream(imageUri);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                }

                // scale down the bitmap and maintain the aspect ratio
                bitmap = getMaintainedImage(bitmap);

                // start the app main screen with the scaled down bitmap
                intent = new Intent(this, MainScreen.class);
                intent.putExtra("Bitmap", bitmap);
                startActivity(intent);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
            }

            // if the user chose to capture their own image
        } else if (requestCode == RESULT_CAPT_IMAGE  && resultCode == RESULT_OK && null != data){
            bitmap = (Bitmap)data.getExtras().get("data");

            // scale down the bitmap and maintain aspect ratio
            bitmap = getMaintainedImage(bitmap);

            // start the main screen with the scaled down bitmap
            intent = new Intent(this, MainScreen.class);
            intent.putExtra("Bitmap", bitmap);
            startActivity(intent);
        }
    }
}
