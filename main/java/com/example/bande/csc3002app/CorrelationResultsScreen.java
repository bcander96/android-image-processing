package com.example.bande.csc3002app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * The CorrelationResultsScreen class is an android activity
 * used to display the results of the correlation carried
 * out by the user.
 */
public class CorrelationResultsScreen extends Activity {

    // variable declarations
    ImageView imageView, imageViewRecon;
    Intent intent;
    Bitmap bitmap, reconstruction;
    TextView resultName, resultScore;
    String matchedPersonName,bestScore, maxScore;

    /**
     * On create method of this activity used to construct the
     * activity as it loads.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correlation_results_screen);

        // load the extras from the previous activity
        matchedPersonName = (String)this.getIntent().getStringExtra("PersonName");
        bestScore = (String)this.getIntent().getStringExtra("BestScore");
        maxScore = (String)this.getIntent().getStringExtra("MaxScore");

        bitmap = (Bitmap) this.getIntent().getParcelableExtra("Bitmap");
        reconstruction = (Bitmap) this.getIntent().getParcelableExtra("Reconstruction");

        // load the data into the results XML
        resultName = (TextView)findViewById(R.id.txtResultName);
        resultName.setText("Best match: " + matchedPersonName);

        resultScore = (TextView)findViewById(R.id.txtResultScore);
        resultScore.setText("Score: " + bestScore + "/" + maxScore);

        imageView = (ImageView) findViewById(R.id.imagePreview);
        imageView.setImageBitmap(bitmap);

        imageViewRecon = (ImageView) findViewById(R.id.imagePreviewRecon);
        imageViewRecon.setImageBitmap(reconstruction);

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
        CorrelationResultsScreen.this.finish();
    }
}
