/*
Version 1.4
Main features:  Reads a text file with limited users/ratings, compared to initial dataset (over10_movielens_simple.rar - MovieLens 100K simple)
                Calculates all data needed to perform NN-algorithm CF (reault a)*
                Calculates all data needed to perform FN-algorithm CF (reverse Pearson - tranform ratings) (result b)
                Compare result a to result b
                Dynamic arrays, during execution to save memory space
                Reads all Movielens dataset (over10_movielens_simple.rar)
                Makes CF Predictions based only on positive Pearson.
                Calculates all data needed to perform FN-algorithm CF (reverse Pearson - negative values) (result c)
                Compare result b to result c, Compare result b to result c, Compare results a, b, c
                Combine results to propose new algorithm
                Exclude values from FN calculations, excluding 3-ratings
                FN calculations, assigning weights to ratings
                Estimate NN and FN based only on K most recent ratings/per user.
                Prints stats
                Partial Separation Logic/Implementation
                Move some methods to sepa  rate files (Phd_Utils).
                New Average Method for FN (inverted similarity)
                Combined FN/NN

Next Version:   Refine simulations for more elaborate results
                Testing algorithms on other than MovieLens 100K simple Data set. 
                Introduce Parallel programming (multithreading)
                Time specific calculations, for improving time execution
                Produce more stats concerning data
                Version 2, has to be more specific in terms of variables values during simulation. Final decisions have to be taken.

ASSUMPTIONS     A:The USER IDs in the text file, starts from 0, and are increasing by step 1.
                  e.g. The 1st user has ID=1, the 2nd user has ID=2.
                B:The MOVIES IDs are like A. Each new movie is assigned a new ID, increasing by step 1.
                
BASE ON         The new proposed algorithm is compared against algorithms presented in paper:
                "Pruning and Aging for User Histories in Collaborative Filtering", D.Margaris, C.Vassilakis
*/

/*

THIS FILE COMPUTES KEEP-N

*/

package phd;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 
//import UserMovie;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Iterator;


/**
 *
 * @author Administrator
 */
public class FN_1M_OLD extends Global_Vars{
        

public static void Assign_Values(double[] values, int choice) {

switch(choice) {
       case 1: simNeighbors=(int)values[0]; positivePredictions=(int)values[1];MAE=values[2];break;
       case 2: revSimNeighbors=(int)values[1];revPredictedValues=(int)values[4];RevMAE=values[7];break;
       case 3: NO3RevSimNeighbors=(int)values[2];NO3RevPredictedValues=(int)values[5]; NO3RevMAE=values[8];break;
       case 4: negAverSimNeighbors=(int)values[0];negAverPredictedValues=(int)values[1]; negAverMAE=values[2];break;       
       case 5: combinedNeighbors=(int)values[0]; combinedPredictions=(int)values[1];combinedMAE=values[2];break;
} //switch 

}// Assign_Values

public static void Print_to_File(int choice){

if (choice==1) 
{
    
}
else
{
   
}

}// Methid Print_to_File

public static void main(String[] args) {
        
System.out.println("Hello World !" );
        

UserSimilarity[][] User_Similarities = new UserSimilarity [MAX_USERS][MAX_USERS];
double Similarity=0, KF_NO3_Similarity=0, MaxSimValue=0, MinSimValue=0;
        
List<UserSimilarity> UserList = new ArrayList<>();
List<UserSimilarity>[] US = new List[MAX_USERS];    //Array of list holding for each user the NN
List<UserSimilarity>[] RUS = new List[MAX_USERS];   //Array of list holding for each user the FN
List<UserSimilarity>[] NO3RUS = new List[MAX_USERS];    //Array of list holding for each user the FN
List<UserSimilarity>[] INVUS = new List[MAX_USERS];  
List<UserSimilarity>[] COMBINE = new List[MAX_USERS];  
       
int totalUsers;                                  //The number of users 
int totalMovies;                                 //The number of unique movies in DB

int newTotal,newrevtotal,no3newrevtotal;
double Numerator_Sim_ij, Denominator_Sim_ij;        //Numerator and Denominator of Similarity (Pearson) function.
double KF_NO3_Numerator_Sim_ij, KF_NO3_Denominator_Sim_ij;

double KF_NO3_Denominator_Part_A,KF_NO3_Denominator_Part_B;
double Numerator_Pred, Denominator_Pred;            //Numerator and Denominator of Prediction function.

int TotalPredictedValues;                              //The total number of actually predicted values
int NO3TotalPredictedValues;
        
int temp_prediction;                                //values holding current (rev)predictions
int temp_rev_prediction;
int temp_no3_rev_prediction;
        
long startTime, initTime, simTime1, simTime2, simTime3, sortTime, predTime1, predTime2, predTime3, predTime4, predTime5;
        
int i,j,k, l, m, n, o, p, q;
int RevMode=0;
int aa=0; 
int[] totals = new int[2];

// PART A. INITIALISATION 
//
// -------- Start reading data file. All data are in memory (Tables) ----------- 
//
// Store all ratings in memory                          
// in two tables: a)User_Ratings b)User_Ratings_Summary 
// Also returns two values: totalUsers and totalMovies 
// Afterwards Inverse Data (for FN) are computed

startTime=System.currentTimeMillis();
totals=Initialization.Data_Initialisation_1M_OLD("MovieLens_1M_Old_Sorted.txt", users, userMovies, absMinTimeStamp, absMaxTimeStamp);
initTime=startTime-System.currentTimeMillis();  //Estimate Initialization Time
totalUsers=totals[0];totalMovies=totals[1];
Initialization.Compute_Inverse_Data(totalUsers, totalMovies, users, userMovies);
System.out.println("totalUsers:"+totalUsers); 

// -------- End reading data file. All data are in memory (Tables) ----------- 

        
        
//PART B. MAIN PART I. COMPUTE SIMILARITIES - PART II.MAKE PREDICTIONS
//
//EXPORT RESULTS TO TAB SEPARATED FILE
//
//            CALCULATE SIMPLE COLLABORATIVE FILTERING SIMILARITIES FOR BOTH NNs and KNs

        
try(FileWriter outExcel = new FileWriter( "results_100k_new.txt" )) {

    //Export File HEADINGS
    
    outExcel.write("AA\tSimilarity"+"\tRevSimilarity"+"\tNO3RevSimilarity"+"\tMin Common Movies"+"\tFirst Best Neighs");
    outExcel.write("\tNN Predictions"+"\tNN Coverage"+"\tNN MAE Sum"+"\tNN MAE CF");
    outExcel.write("\tFN Predictions"+"\tFN Coverage"+"\tFN MAE Sum"+"\tFN MAE CF");
    //outExcel.write("\tNO3 FN Predictions"+"\tNO3 FN Coverage"+"\tNO3 FN MAE Sum"+"\tNO3 Rev MAE CF");
    outExcel.write("\tDenFN Predictions"+"\tDenFN Coverage"+"\tDenFN MAE Sum"+"\tDenRev MAE CF");    
    outExcel.write("\tCombined NN FN Predictions"+"\tNN FN Coverage"+"\tNN FN MAE Sum"+"\tNN FN MAE CF");
    outExcel.write("\r\n");  
    
    //Print_to_File(outExcel,1);            
    
    try(FileWriter out = new FileWriter( "Multiple_Repeat_Results_1M_old.txt" ))            //Open file for writing
    {

        //All parameters used fot the simulation process
        
        //for (q=MIN_MOST_RECENT_RATINGS;q<=MIN_MOST_RECENT_RATINGS;q+=10)
        for (p=DOWN_BEST_NEIGH;p<=UPPER_BEST_NEIGH;p+=10)
        for (n=MIN_COMMON_MOVIES;n<=MAX_COMMON_MOVIES;n+=10)
 //       for (o=MIN_SIMILAR_NEIGH;o<=MAX_SIMILAR_NEIGH;o+=10)  //OBSOLETE - NOT USED ANY MORE
        for (l=SIMILARITY_BASE_LIMIT;l<=SIMILARITY_UPPER_LIMIT;l+=20)
        for (m=NEGATIVE_SIMILARITY_BASE_LIMIT;m<=NEGATIVE_SIMILARITY_UPPER_LIMIT;m+=20)    
        {            
            
            //Compute SIMILARITIES
            
            
            System.out.println(" n:"+n+" l:"+l+" m:"+m);

            simNeighbors=0; revSimNeighbors=0;NO3RevSimNeighbors=0;
            positivePredictions=0; revPredictedValues=0; NO3RevPredictedValues=0;    
            MAE=0.0;RevMAE=0.0;NO3RevMAE=0.0;

            TotalPredictedValues=0;NO3TotalPredictedValues=0;            
            NO3TotalMAE=0.0;TotalMAE=0.0;                                    
            
            startTime=System.currentTimeMillis();           //Set new timer
            Similarities.Positive_Similarity(totalUsers, totalMovies, US, users, userMovies, (double)l/100, n); 
            simTime1=startTime-System.currentTimeMillis();
            startTime=System.currentTimeMillis();           //Set new timer
            Similarities.Compute_Similarity(totalUsers, totalMovies, RUS, users, userMovies, 0, (double)-m/100, n);
            simTime2=startTime-System.currentTimeMillis();
            //startTime=System.currentTimeMillis();           //Set new timer
            //Similarities.Compute_Similarity(totalUsers, totalMovies, NO3RUS, users, userMovies, 2, (double)-m/100, n);
            //simTime3=startTime-System.currentTimeMillis();
            startTime=System.currentTimeMillis();           //Set new timer
            Similarities.Inverted_Similarity(totalUsers, totalMovies, INVUS, users, userMovies, (double)m/100, n, absMinTimeStamp, absMaxTimeStamp);
            simTime3=startTime-System.currentTimeMillis();

            //System.out.println("aaa");
            //Similarities.Print_Similarities(totalUsers, INVUS);
            //Similarities.Print_Similarities(totalUsers, US);
            //For each User there is a sorted array with all its NN/FN calculated

            startTime=System.currentTimeMillis();           //Set new timer
            
            for (i=0;i<=totalUsers;i++)
            {
                Collections.sort(US[i],Collections.reverseOrder());
                Collections.sort(RUS[i]);
//                Collections.sort(NO3RUS[i]);
                Collections.sort(INVUS[i],Collections.reverseOrder());
            }
            //System.out.println("bbb");
            //Similarities.Print_Similarities(totalUsers, INVUS);
            //Similarities.Print_Similarities(totalUsers, US);
            sortTime=startTime-System.currentTimeMillis();

            //Keep only Neighbors that have rate LastMovieID
            Phd_Utils.Strict_Similarities(totalUsers, US, users, userMovies);
            Phd_Utils.Strict_Similarities(totalUsers, RUS, users, userMovies);
//            Phd_Utils.Strict_Similarities(totalUsers, NO3RUS, users, userMovies);
            Phd_Utils.Strict_Similarities(totalUsers, INVUS, users, userMovies);     

            //System.out.println("ccc");
            //Similarities.Print_Similarities(totalUsers, INVUS);
            //Similarities.Print_Similarities(totalUsers, US);
            /* 
                CALCULATE USER'S PREDICTION FOR LAST MOVIE FROM NN
            */


            startTime=System.currentTimeMillis();                    //New Timer
            Assign_Values(Predictions.Positive_Prediction(totalUsers, totalMovies, US, users, userMovies, p),1);
            predTime1=startTime-System.currentTimeMillis();                          //Time for the calculation of Predicted ratings 

            startTime=System.currentTimeMillis();                    //New Timer
            Assign_Values(Predictions.Compute_Prediction(totalUsers, totalMovies, RUS, users, userMovies, 1, p),2);            
            predTime2=startTime-System.currentTimeMillis();                          //Time for the calculation of Predicted ratings         

            //startTime=System.currentTimeMillis();                    //New Timer
            //Assign_Values(Predictions.Compute_Prediction(totalUsers, totalMovies, NO3RUS, users, userMovies, 2, p),3);                 
            //predTime3=startTime-System.currentTimeMillis();                          //Time for the calculation of Predicted ratings         

            startTime=System.currentTimeMillis();                    //New Timer
            Assign_Values(Predictions.Inverted_Prediction(totalUsers, totalMovies, INVUS, users, userMovies, p),4);     
            //System.out.println(negAverMAE+" "+negAverPredictedValues);            
            predTime4=startTime-System.currentTimeMillis();    
        
            startTime=System.currentTimeMillis();                    //New Timer
            Assign_Values(Predictions.Combined_Prediction(totalUsers, totalMovies, US, RUS, COMBINE, users, userMovies, p),5);
            predTime5=startTime-System.currentTimeMillis();                          //Time for the calculation of Predicted ratings 

        //Testing the process so far 
            aa++;    

            outExcel.write(aa+"\t"+(double)l/100+"\t"+(double)-m/100+"\t"+(double)-m/100+"\t"+n+"\t"+p);
            outExcel.write("\t"+positivePredictions+"\t"+(double)positivePredictions/(totalUsers+1)+"\t"+MAE+"\t"+(double)(MAE/positivePredictions));
            outExcel.write("\t"+revPredictedValues+"\t"+(double)revPredictedValues/(totalUsers+1)+"\t"+RevMAE+"\t"+(double)(RevMAE/revPredictedValues));
            //outExcel.write("\t"+NO3RevPredictedValues+"\t"+(double)NO3RevPredictedValues/(totalUsers+1)+"\t"+NO3RevMAE+"\t"+(double)(NO3RevMAE/NO3RevPredictedValues));
            outExcel.write("\t"+negAverPredictedValues+"\t"+(double)negAverPredictedValues/(totalUsers+1)+"\t"+negAverMAE+"\t"+(double)(negAverMAE/negAverPredictedValues));            
            outExcel.write("\t"+combinedPredictions+"\t"+(double)combinedPredictions/(totalUsers+1)+"\t"+combinedMAE+"\t"+(double)(combinedMAE/combinedPredictions));
            outExcel.write("\r\n"); 

            //  Print Statistics

            out.write("Max Similarity Value: "+MaxSimValue+" Min Similarity Value:"+MinSimValue);
            out.write("\r\n");            
            out.write("\r\n");                        
            out.write("Initialization time (Read File, Fill in User, UserMovies tables): "+Long.toString(initTime));
            out.write("\r\n");
            out.write("Calculate time to find Similarities (NN): "+Long.toString(simTime1));
            out.write("\r\n");
            out.write("Calculate time to find Similarities (FN): "+Long.toString(simTime2));
            out.write("\r\n");
            //out.write("Calculate time to find Similarities (NO3 FN): "+Long.toString(simTime3));
            //out.write("\r\n");
            out.write("Sort Similarity arrays for all users: "+Long.toString(sortTime));
            out.write("\r\n");
            out.write("Calculate time to make Predictions (NN): "+predTime1);
            out.write("\r\n");
            out.write("Calculate time to make Predictions (FN): "+predTime2);
            out.write("\r\n");
            //out.write("Calculate time to make Predictions (NO3 FN): "+predTime3);
            //out.write("\r\n");
            out.write("Calculate time to make Predictions Combined: "+predTime4);
            out.write("\r\n");
            out.write("********************************************************\r\n");
            out.write("********************************************************\r\n");            
            out.write("\r\n");
        }    
            out.close();     //Close output file
            
        } //try    //try   
        catch (IOException iox) {
            //do stuff with exception
            iox.printStackTrace();
        } //catch
            outExcel.close();
        }
        catch (IOException iox) {
            //do stuff with exception
            iox.printStackTrace();
        } //catch
        
        System.out.println("World ended !" );    

        //System.out.println("dd");        
        //The following are working examples!
        //Similarities.Print_Similarities(totalUsers, US);
        //Similarities.Print_Similarities(totalUsers, INVUS);
        //Similarities.Print_Similarities(totalUsers, COMBINE);
        //Phd_Utils.Print_UserRatings(totalUsers, totalMovies, users, userMovies);
    } //Main
    
} //Class Phd
