package com.example.bande.csc3002app.correlation;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.example.bande.csc3002app.image.Image;
import com.example.bande.csc3002app.toolkit.Properties;
import java.io.IOException;
import java.io.InputStream;
import static android.content.ContentValues.TAG;

/**
 * Created by bande on 02/03/2018.
 * This ImageDatabase class is an asset manager class which
 * facilitates the loading of the image database into suitable
 * memory locations. The image database is used in calculating
 * the correlation between images.
 */
public class ImageDatabase {

    AssetManager assetManager; // used to load assets i.e. training images
    String [] names; // list of people names in db
    String [] images; // list of image names in db
    String [][] persons; // 2d array of images to names

    // variable declarations
    Bitmap bitmap;
    InputStream is;
    Image image;
    Context c; // context from activity, used for asset managing.

    /**
     * Constructor to load in the app context.
     * Constructor then loads the database to the app.
     * @param context is the app activity context.
     */
    public ImageDatabase(Context context){
        c = context;
        loadDatabase();
    }

    /**
     * Helper method used by the class constructor
     * to load the image assets into memory for use
     * within the app. i.e. image correlation.
     * Throws IO Exception if assets not found.
     */
    void loadDatabase() {
        // initialise the asset manager using context
        assetManager = c.getAssets();
        try {
            // load all assets within database
            names = assetManager.list("Database");
            persons = new String[names.length][];

            for (int i=0; i < names.length; i++) {
                images = assetManager.list("Database/"+names[i]);
                // person[i] has multiple training images
                persons[i] = new String[images.length];
                persons[i] = images;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to load the image data of each person into
     * a 4D array for use in image correlation calculations.
     * personTrainingImages[p][n_p][i][j].
     * p is the person from the database.
     * nthImg is the nth training image for that person.
     * i is the horizontal co-ordinate of a pixel in the image.
     * j is the vertical co-ordinate of a pixel in the image.
     * @return the 4D array of person training image pixels.
     * Throws an IO exception if assets are not found.
     */
    public int[][][][] getPersonTrainingImages(){

        // initialise asset manager
        assetManager = c.getAssets();

        // declaring variables
        int [][][][] personTrainingImages = null;
        int [][] trainingImage;
        String path;
        Properties imageProp;
        int pixelChannel; // grey scale image pixels

        // allocating memory for person nth training images
        personTrainingImages = new int[persons.length][][][];
        for(int p = 0; p < persons.length; p ++){
            personTrainingImages[p] = new int[getNumberOfImagesForPerson(p)][0][0];
        }

        // for each person in the database
        for(int p = 0; p < persons.length; p ++){
            // for each training image that person has
            for (int nthImg = 0; nthImg < persons[p].length; nthImg ++){

                // set the path of the nth training image for person p
                path = "Database/" + getPersonName(p) + "/" + persons[p][nthImg];

                // load the nth training image as a bitmap
                try {
                    is = assetManager.open(path);
                    bitmap = BitmapFactory.decodeStream(is);

                    // initialise an image object with the bitmap
                    image = new Image(bitmap);
                    // convert to greyscale
                    imageProp = new Properties(image);
                    bitmap = imageProp.convertToGreyscale();
                    image.setBitmap(bitmap);

                    // allocate memory for nth training image
                    trainingImage = new int[image.getWidth()][image.getHeight()];

                    // load the image data into the 4D array
                    for (int i = 0; i < image.getWidth(); i ++){
                        for (int j = 0; j < image.getHeight(); j ++){
                            pixelChannel = image.getBlueData(i, j);
                            trainingImage[i][j] = pixelChannel;
                            personTrainingImages[p][nthImg] = trainingImage;
                        }
                    }

                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }

        return personTrainingImages;
    }

    /**
     * Helper method used to return the number of training
     * images a given person[i] has in the database.
     * @param i is the person.
     * @return the number of images that person has.
     */
    public Integer getNumberOfImagesForPerson(int i){
        return persons[i].length;
    }

    /**
     * Helper method to return the array of persons. A 2D
     * array of persons and training images.
     * @return
     */
    public String [][] getPersonsDatabase(){
        return persons;
    }

    /**
     * Helper method to get the path of a given training
     * image for a person.
     * @param i is the person name.
     * @param j is the training image for that person.
     * @return the path for the given person's training image.
     */
    public String getPathForPersonImage(int i, int j){
        return "Database/" + names[i] + "/" + persons[i][j];
    }

    /**
     * Helper method to get the name of a given person index.
     * @param i is the index of that person in the person and
     *          names array.
     * @return the name of the given person.
     */
    public String getPersonName(int i){
        if (i == -1) {
            return "No match found.";
        }
        return names[i];
    }

    /**
     * Helper method to list the paths of all training images
     * for a given person. Used for debugging purposes.
     * @param i is a given person index.
     */
    public void listAllPathsForPerson(int i){
        System.out.println(names[i]);
        for (int j = 0; j < persons[i].length; j ++){
            System.out.println("Database/" + getPersonName(i) + "/" + persons[i][j]);
        }

    }

    /**
     * A demo method used to experiment with loading image
     * assets from the database.
     * @param context is the activity context.
     * @param path is the path of the asset to load.
     */
    void loadBitmapFromDB(Context context, String path){
        assetManager = context.getAssets();
        try {
            is = assetManager.open("Abdullah_Gul/Abdullah_Gul_0001.bmp");
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }



}
