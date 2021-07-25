package com.example.bande.csc3002app.correlation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import com.example.bande.csc3002app.image.Image;
import com.example.bande.csc3002app.toolkit.Properties;
import static java.lang.Math.sqrt;

/**
 * Created by bande on 01/03/2018.
 * This ImageCorrelation class is used for facial
 * recognition. It uses an algorithm to calculate
 * the correlation of the user's image compared to
 * all the images stored in the database. The person
 * that correlates closest to the users image is chosen
 * as a match. Makes use of the ImageDatabase class to compare
 * image patches.
 */
public class ImageCorrelation {

    // variable declarations
    ImageDatabase idb; // used to load database training images
    Image image;
    Bitmap bitmap;

    // correlation variables
    String [][] persons; // 2D array of persons and their training images
    int sizeOfPatch, totalPersons, imgSideLength, pNumImages;
    int step; // the overlap of each patch being checked
    int range; // the range of pixels to search for a match

    // images & patches
    int [][] userImage; // users image
    int [][][][] trainingImages; // for each person, for each image, [][] pixels. Db training image.
    double userPatch[][]; // a testing patch (of users image)
    double trainingPatch[][]; // a training patch (of db image)

    // calculations
    double [] score; // array of scores for each db person
    double bestScore, maxScore; // the highest score
    double sumTrainingPatch; // some of training patch
    double sigmaTrainingPatch; // sigma of training patch

    // reconstruction
    int nMax, uMax, vMax;
    int reconstructedImage[][][];
    int reconCount[][][];

    /**
     * Constructor method used to initialise various variables defined above.
     * These variables are necessary to complete calculating the correlation
     * between images such as in the calculateCorrelation() method.
     * @param context is the context from the previous activity screen, used
     *                for the image database to read in assets.
     * @param img is the users image we want to try to recognise by calculating
     *            correlation.
     * @param userInputPatchSize is a percentage size of the image given by the user
     *                           to be used in calculating the patch size.
     * @param userInputRange is defined by the user and can have an affect on
     *                       accuracy and run time.
     */
    public ImageCorrelation(Context context, Image img, int userInputPatchSize, int userInputRange){
        // initialisations
        idb = new ImageDatabase(context);
        image = img;
        bitmap = image.getBitmap().copy(image.getBitmap().getConfig().ARGB_8888, true);
        persons = idb.getPersonsDatabase(); // the database of people and images

        sizeOfPatch = getPatchSize(image, userInputPatchSize); // % of the image height
        step = getPatchSize(image, userInputPatchSize) / 2;
        range = userInputRange;
        totalPersons = persons.length; // the number of people to compare with

        // initialise score array and set all to 0
        score = new double[totalPersons];
        for(int p = 0; p < totalPersons; p++) score[p] = 0.;

        imgSideLength = bitmap.getWidth(); // image resolution is imgSideLength * imgSideLength
        userImage = getGreyScaleImage(); // the users image (2D array of pixels)
        trainingImages = idb.getPersonTrainingImages();
        userPatch = new double [2*sizeOfPatch+1][2*sizeOfPatch+1]; // tester patch
        trainingPatch = new double [2*sizeOfPatch+1][2*sizeOfPatch+1]; // trainer patch

        // reconstruction variables initialised
        nMax = 0; uMax = 0; vMax = 0;
        reconstructedImage = new int[persons.length][imgSideLength][imgSideLength];
        reconCount = new int[persons.length][imgSideLength][imgSideLength];

    }

    /**
     * Getter method used to calculate and return the size of the
     * patch to use. (% of the image size)
     * @param image is the users image object.
     * @param userInputPatchPercent percentage patch size given by the
     *                              user used in calculating the actual
     *                              patch size in relation to the image.
     * @return the patch size to use.
     */
    private Integer getPatchSize(Image image, int userInputPatchPercent){
        double percentage = (double) userInputPatchPercent / 100;
        return (int)Math.rint( (image.getHeight() * percentage));
    }

    /**
     * Helper method used in the constructor of this class to convert the users
     * bitmap into a greyscale 2D array for use in the facial recognition (correlation)
     * algorithm.
     * @return a 2D array of grey scale pixels.
     */
    private int [][] getGreyScaleImage(){

        // convert the bitmap to grey scale
        Properties imageProp = new Properties(image);
        bitmap = imageProp.convertToGreyscale();

        int pixelChannel; // grey scale image pixel channel

        int [][] greyImage = new int[image.getWidth()][image.getHeight()];

        for (int i = 0; i < image.getWidth(); i ++){
            for (int j = 0; j < image.getHeight(); j ++){
                pixelChannel = image.getBlueData(i, j);
                greyImage[i][j] = pixelChannel;
            }
        }

        return greyImage;

    }

    /**
     * Helper method only used to print start up information in relation
     * to the image correlation algorithm. (Patch size, step amount, range,
     * image size)
     */
    private void printStartupInformation(){
        System.out.println("Starting face recognition..");
        System.out.println("Patch size: \t" + sizeOfPatch);
        System.out.println("Step size: \t" + step);
        System.out.println("Range size: \t" + range);
        System.out.println("Image size: \t" + imgSideLength + "x" + imgSideLength);
    }

    /**
     * Method used to calculate the image correlation. This is the main algorithm
     * used in the facial recognition process. It iterates through every training image
     * in the database, searching a variety of pixel patches and calculating the
     * correlation in comparison with the users image. From this we can determine the
     * best/closest match for the image used.
     * @return the index of the matched (closest matched) person from the database.
     */
    public int calculateImageCorrelation(){

        // print some startup information
        printStartupInformation();

        // the total accumulation i.e. maximum possible score for these patches
        int count = 0;

        for(int i = sizeOfPatch; i < imgSideLength - sizeOfPatch; i += step) {
            for(int j = sizeOfPatch; j < imgSideLength - sizeOfPatch; j += step) {

                // incremement the count
                count ++;

                // get a test patch at (i,j)
                for(int l = -sizeOfPatch; l <= sizeOfPatch; l++) {
                    for(int k = -sizeOfPatch; k <= sizeOfPatch; k++) {
                        userPatch[sizeOfPatch+l][sizeOfPatch+k] = userImage[i+l][j+k];
                    }
                }

                // summation of pixels in users image patch
                double sumUserImagePatch = 0.;
                double sumUserImagePatch2 = 0.;
                for(int l = 0; l < (2*sizeOfPatch)+1; l++) {
                    for (int k = 0; k < (2*sizeOfPatch) + 1; k++) {
                        sumUserImagePatch += userPatch[l][k];
                        sumUserImagePatch2 += userPatch[l][k] * userPatch[l][k];
                    }
                }

                // sigma calculated for patch of users image
                double sigmaUserImagePatch = ((2*sizeOfPatch)+1)*((2*sizeOfPatch)+1) *
                        sumUserImagePatch2 - sumUserImagePatch * sumUserImagePatch;
                if(sigmaUserImagePatch > 0.) {
                    sigmaUserImagePatch = sqrt(sigmaUserImagePatch);
                }
                else continue;

                // compare with training patches
                for(int p = 0; p < totalPersons; p++) {
                    pNumImages = idb.getNumberOfImagesForPerson(p);

                    // max correlation patch for person p
                    double corrMax = -1000.;

                    for(int n = 0; n < pNumImages; n++) {

                        // get a training patch at (u,v)
                        for(int u = i-range; u <= i+range; u++) {
                            for(int v = j-range; v <= j + range; v++) {

                                // ensure patches stay within the image constraints
                                if (u < 0 || v < 0 || u >= imgSideLength || v >= imgSideLength
                                        || (u+(-sizeOfPatch)) < 0 || (v+(-sizeOfPatch)) < 0
                                        || (u+(sizeOfPatch)) >= imgSideLength
                                        || (v+(sizeOfPatch)) >= imgSideLength) {
                                    continue;
                                } else {

                                    // get a training patch at (u,v)
                                    for (int l = -sizeOfPatch; l <= sizeOfPatch; l++) {
                                        for (int k = -sizeOfPatch; k <= sizeOfPatch; k++) {
                                            trainingPatch[sizeOfPatch + l][sizeOfPatch + k] =
                                                    trainingImages[p][n][u + l][v + k];
                                        }
                                    }

                                    // calculate sum all pixels for [p][n] in [u][v] - sum_x_pn_uv
                                    // calculate sigma all pixels for [p][n] in [u][v] - sigma_x_pn_uv
                                    preCalculation(trainingPatch);

                                    double dp = 0.;
                                    for (int l = 0; l < (2*sizeOfPatch) + 1; l++) {
                                        for (int k = 0; k < (2 * sizeOfPatch) + 1; k++) {
                                            dp += userPatch[l][k] * trainingPatch[l][k];
                                        }
                                    }

                                    // correlation
                                    double corr = (((2*sizeOfPatch) + 1) * ((2*sizeOfPatch) + 1) *
                                            dp - sumUserImagePatch * sumTrainingPatch) / (sigmaUserImagePatch *
                                            sigmaTrainingPatch);
                                    if (corr > corrMax) {
                                        corrMax = corr;
                                        nMax = n;
                                        uMax = u;
                                        vMax = v;
                                    }//if

                                } // else

                            }//v
                        }//u
                    }//n
                    score[p] += corrMax;

                    // accumulating reconstruction for person p at (i, j)
                    accumulateReconstruction(p, i, j);

                }//p
            } // j
        } // i

        // calculate the reconstruction for person p
        calculateReconstruction();

        // print the person scores
        System.out.println(count);
        for (int p = 0; p < score.length; p ++) {
            System.out.println("Score Person " + p + " " + score[p]);
        }

        // assign the max score
        maxScore = count;

        return getMatchedPersonIndex(score, count);

    } // calculate correlation

    /**
     * Pre calculation method used to gather a sum of pixels for a patch
     * during the correlation algorithm. This method also calculates the sigma.
     * @param x is a training images patch.
     */
    private void preCalculation(double x[][]){

        double sum = 0.;
        double sum2 = 0.;
        for(int l = 0; l < (2*sizeOfPatch) + 1; l++) {
            for (int k = 0; k < (2*sizeOfPatch) + 1; k++) {
                sum += x[l][k];
                sum2 += x[l][k] * x[l][k];
            }
        }
        sumTrainingPatch = sum;
        sigmaTrainingPatch = (2*(sizeOfPatch) + 1)*(2*(sizeOfPatch) + 1)*sum2 - sum * sum;
        if(sigmaTrainingPatch > 0.) sigmaTrainingPatch = sqrt(sigmaTrainingPatch);

    }

    /**
     * Helper method used for accumulating the reconstruction image
     * during the correlation algorithm.
     * @param p is the current person.
     * @param i is the current co-ordinate.
     * @param j is the current co-ordinate.
     */
    void accumulateReconstruction(int p, int i, int j){
        // accumulating reconstruction for person p
        for(int l = -sizeOfPatch; l <= sizeOfPatch; l++) {
            for (int k = -sizeOfPatch; k <= sizeOfPatch; k++) {
                reconstructedImage[p][i + l][j + k] +=
                        trainingImages[p][nMax][uMax + l][vMax + k];
                reconCount[p][i + l][j + k] += 1;
            }
        }
    }

    /**
     * Helper method used to calculate the final image reconstruction.
     * This image reconstruction is used a representation of the accuracy
     * of the results found.
     */
    void calculateReconstruction(){
        for(int p = 0; p < totalPersons; p++) {
            for (int i = sizeOfPatch; i < imgSideLength - sizeOfPatch; i++) {
                for (int j = sizeOfPatch; j < imgSideLength - sizeOfPatch; j++) {
                    reconstructedImage[p][i][j] = reconstructedImage[p][i][j] /
                            reconCount[p][i][j];
                }
            }
        }
    }

    /**
     * Getter method used to generate and return a bitmap from the 2D array image
     * reconstruction.
     * @param matchedPersonIndex the index of the closet matched person
     *                           (from the database).
     * @return the bitmap reconstruction.
     */
    public Bitmap getReconstruction(int matchedPersonIndex){
        Bitmap reconstruction = Bitmap.createBitmap(
                imgSideLength, imgSideLength, Bitmap.Config.ARGB_8888);

        for (int i = 0; i < imgSideLength; i ++){
            for (int j = 0; j < imgSideLength; j ++){
                reconstruction.setPixel(i, j,
                        Color.rgb(reconstructedImage[matchedPersonIndex][i][j],
                        reconstructedImage[matchedPersonIndex][i][j],
                        reconstructedImage[matchedPersonIndex][i][j]));
            }
        }

        return reconstruction;
    }

    /**
     * Helper method used to differentiate through the gathered results and
     * determinte the best match. To do this the highest correlation score is
     * found. Then a check is carried out to ensure no more than one person
     * has obtained the same score, in which case we cannot determine a match
     * with confidence.
     * @param score is an array of each persons correlation score.
     * @param target is the target score i.e. max score.
     * @return the index of the person from the database that is the best
     * match for the users image.
     */
    private int getMatchedPersonIndex(double [] score, int target) {

        int matchIndex = 0;
        int similarMatch = 0;

        // what correlation score is the highest
        double largest = score[0];
        for (int p = 1; p < score.length; p ++){
            if (score[p] > largest) {
                largest = score[p];
                matchIndex = p;
            }
        }

        // check if more than one person is a match
        for (int p = 0; p < score.length; p ++){
            if (p != matchIndex) {
                if (score[p] == score[matchIndex]) {
                    similarMatch = p;
                }
            }
        }

        // more than one match
        if (similarMatch != 0) {
            return -1;
        }

        // assign the best score
        bestScore = score[matchIndex];

        return matchIndex;
    }

    /**
     * Getter method used to get the best score found from the correlation
     * algorithm.
     * @return the best score.
     */
    public int getBestScore(){
        return (int) bestScore;
    }

    /**
     * Getter method used to return the highest possible score that can
     * be obtained by a person in the correlation algorithm.
     * @return the highest possible score.
     */
    public int getMaxScore(){
        return (int) maxScore;
    }

}
