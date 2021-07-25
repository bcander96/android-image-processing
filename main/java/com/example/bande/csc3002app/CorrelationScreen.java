package com.example.bande.csc3002app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.bande.csc3002app.correlation.ImageCorrelation;
import com.example.bande.csc3002app.correlation.ImageDatabase;
import com.example.bande.csc3002app.image.Image;

/**
 * The CorrelationScreen class is an android activity
 * used for managing the user interface functionality of the
 * application. This screen allows the user to carry out
 * facial recognition on their image.
 */public class CorrelationScreen extends Activity {

    // variable declarations
    ImageView imageView;
    Intent intent;
    Bitmap bitmap;
    Image image;
    public static Activity cs;
    ImageCorrelation imageCorr;
    ImageDatabase idb;
    Toast toast; // on screen message

    EditText edTxtPatch, edTxtRange;
    String strPatchSize, strRange; // user input values

    /**
     * On create method of this activity used to construct the
     * activity as it loads.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_correlation_screen);

        // initialise this activity context
        cs = this;

        // load the bitmap from the previous activity
        bitmap = (Bitmap)this.getIntent().getParcelableExtra("Bitmap");

        // load the bitmap into the preview
        imageView = (ImageView)findViewById(R.id.imagePreview);
        imageView.setImageBitmap(bitmap);

    }

    /**
     * Helper method used to validate the user has input a
     * range to use.
     * @param userInput is the string input by the user.
     * @return true if validation successful.
     */
    boolean checkUserInputValue(String userInput){
        if (userInput.matches("")) {
            Toast.makeText(this, "You are missing an input value.",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * Helper method used to validate the user has input a number value
     * within the suggested range 0-maxRange.
     * @param userInput is the string input by the user.
     * @return true if validation successful.
     */
    boolean checkUserInputIsIntegerInRange(String userInput, int maxRange){
        int temp;
        try {
            temp = Integer.parseInt(userInput);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "You must input a number. E.g. 0-" + maxRange,
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (temp < 0 || temp > maxRange){
            Toast.makeText(this, "Patch size must have a range of 0-100, " +
                            "Range must be 0-15.",
                    Toast.LENGTH_LONG).show();
            return false;
        }
            return true;
    }

    /**
     * Button listener which when clicked calculates
     * the correlation between the users image and all
     * images in the database. Only 64x64 images are suitable.
     * Results of the face recognition are then displayed.
     * @param view
     */
    public void onClickCorrelation(View view){

        // check the user has input a number for patch size (percentage)
        edTxtPatch = (EditText) findViewById(R.id.editTextInputPatchSize);
        strPatchSize = edTxtPatch.getText().toString();

        if (!checkUserInputValue(strPatchSize)){
            return;
        }
        if (!checkUserInputIsIntegerInRange(strPatchSize, 100)){
            return;
        }

        // check the user has input a number for range (0-15)
        edTxtRange = (EditText) findViewById(R.id.editTextInputRange);
        strRange = edTxtRange.getText().toString();

        if (!checkUserInputValue(strRange)){
            return;
        }
        if (!checkUserInputIsIntegerInRange(strRange, 15)){
            return;
        }

        System.out.println("User input... Patch size (%): " + strPatchSize + "Range: " + strRange);

        image = new Image(bitmap);
        idb = new ImageDatabase(this);

        String text; String name; int matchedPersonIndex;

        // the user must use an image of resolution 64x64
        // in-order for it to be compatible with the
        // algorithm and database.
        if (image.getWidth() != 64 && image.getHeight() != 64) {
            text = "To use this feature your image must have a " +
                    "resolution of 64x64.\nUpload a new image and try again.";

            toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        // carry out the image correlation between all images and display
        // the name of the match if found.
        imageCorr = new ImageCorrelation(this, image, Integer.parseInt(strPatchSize),
                Integer.parseInt(strRange));
        matchedPersonIndex = imageCorr.calculateImageCorrelation();
        name = idb.getPersonName(matchedPersonIndex);

        if (name == "No match found.") {
            // could not confidently find a match
            text = "The algorithm was not able to find a confident match. " +
                    "Please try again with a new image.";
            toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
            toast.show();
        } else {
            // match found
            name = name.replace("_", " "); // fix the person name

            // get the image reconstruction
            Bitmap reconstruction = imageCorr.getReconstruction(matchedPersonIndex);

            // pass result data to the results activity
            intent = new Intent(this, CorrelationResultsScreen.class);
            intent.putExtra("PersonName", name);
            intent.putExtra("BestScore", Integer.toString(imageCorr.getBestScore()));
            intent.putExtra("MaxScore", Integer.toString(imageCorr.getMaxScore()));
            intent.putExtra("Bitmap", bitmap);
            intent.putExtra("Reconstruction", reconstruction);

            startActivity(intent);
        }

        System.out.println("Correlation button clicked.");
    }

    /**
     * Button listener which when clicked brings the user
     * back to the MainScreen.
     * @param view
     */
    public void onClickGoBack(View view){
        intent = new Intent(this, MainScreen.class);
        intent.putExtra("Bitmap", bitmap);
        startActivity(intent);
        System.out.println("Save changes button clicked.");
        CorrelationScreen.this.finish();
    }
}
