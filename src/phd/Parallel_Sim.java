/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phd;

import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class Parallel_Sim implements Runnable{
 int low; 
 int upper;    
 int totalUsers;
 List<UserSimilarity>[] userSim;
 User[] users;
 UserMovie[][] userMovies;
 HashSet<Integer>[] usersRatingSet;
 double simBase;
 int commonMovies;
 
Parallel_Sim( 
int low, 
int upper,    
int totalUsers,
List<UserSimilarity>[] userSim,
User[] users,
UserMovie[][] userMovies,
HashSet<Integer>[] usersRatingSet,
double simBase,
int commonMovies) {
    
this.low = low;
this.upper=upper;
this.totalUsers=totalUsers;
this.userSim=userSim;
this.users=users;
this.userMovies=userMovies;
this.usersRatingSet=usersRatingSet;
this.simBase=simBase;
this.commonMovies=commonMovies;
} //Constructor Parallel_Sim

public void run() {
System.out.println(low + " "+upper+" "+simBase+" "+commonMovies);
Similarities.Positive_Similarity_Parallel(low,upper, totalUsers, userSim, users, userMovies, usersRatingSet, simBase, commonMovies);
} //run
   
} //Class Parallel_Sim

