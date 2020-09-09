

package com.example.demo.Recomendation;

import com.example.demo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class MatrixFactorization {
    private UserRepository userRepository;
    private AppartmentRepository appartmentRepository;
    private ReviewRepository reviewRepository;
    private UserVectorRepository userVectorRepository;
    private AppViewRepository appViewRepository;
    private SearchRepository searchRepository;
    int users ;//= (int) userRepository.count();
    int apartments ;//= (int) appartmentRepository.count();
    private ArrayList<User> allUsers;
    private ArrayList<appartment> allAppartments;
    int genres = 5;//Number of Latent features
    double P[][];
    double Q[][];
    double R[][];
    int steps = 1000; double h = 0.007; double beta = 0.000002;
    Random randd = new Random();

    @Autowired
    public MatrixFactorization(UserRepository userRepository, AppartmentRepository appartmentRepository, ReviewRepository reviewRepository, AppViewRepository appViewRepository, SearchRepository searchRepository, UserVectorRepository userVectorRepository) {
        System.out.println("MATRIX FACTORIZATION STARTS");
        this.userRepository = userRepository;
        System.out.println("Counting users");
        this.users= (int) this.userRepository.count();
        System.out.println("Counted %d users" + this.users);
        this.appartmentRepository = appartmentRepository;
        System.out.println("Counting apartments");
        this.apartments= (int) this.appartmentRepository.count();
        System.out.println("Counted %d apartments" + this.apartments);
        this.reviewRepository = reviewRepository;
        this.userVectorRepository = userVectorRepository;
        this.searchRepository = searchRepository;
        this.appViewRepository = appViewRepository;
        /////////////////////
        P = new double[users][genres];
        Q = new double[genres][apartments];
        R = new double[users][apartments];
        ///////////////
        int reviewzz = 0;
        int app_viewz = 0;
        int search_viewz = 0;
        System.out.println("GATHERING KNOWN DATA");
        this.allUsers = (ArrayList<User>) this.userRepository.findAllByOrderByUserNameAsc(); // USERS LIST (SORTED)
        System.out.println("Users gathered");
        this.allAppartments = (ArrayList<appartment>) this.appartmentRepository.findAll(); // APARTMENT LIST
        System.out.println("Apartments gathered");
        for (int j=0; j<apartments; j++) {
            System.out.println("Apartment " + j);
            ArrayList<Review> reviewz = (ArrayList<Review>) this.reviewRepository.findAllByAppartment(allAppartments.get(j));
            for (int r=0; r< reviewz.size(); r++) {
                String user_name = reviewz.get(r).getUserName();
                for (int i=0; i<users; i++) {
                    if (user_name.equals(allUsers.get(i).getUserName())) {
                        reviewzz++;
                        if (R[i][j] == 0) // First review of user i for apartment j
                            R[i][j] = reviewz.get(r).getNumber();
                        else
                            R[i][j] = (R[i][j] + reviewz.get(r).getNumber()) / 2;
                        //   System.out.println(R[i][j]);
                    }
                }
            }
        }
        //       Data sparsity compensation
        // For every user, find apartments he's seen and mark them as a 3 (if he hasn't reviewed them)
        System.out.println("Calculating apartment views");
        for (int i=0; i<users; i++) {
//            System.out.println("User " + i);
            ArrayList<AppView> appViewz = (ArrayList<AppView>) this.appViewRepository.findAllByUser(allUsers.get(i));
            for (int r=0; r<appViewz.size(); r++) {
                appartment ap = appViewz.get(r).getApp();
                for (int j=0; j<apartments; j++) {
                    if (ap.getId().equals(allAppartments.get(j).getId())) {
                        app_viewz++;
                        if (R[i][j] == 0) // User i has not rated apartment j
                            R[i][j] = 3;
                    }
                }
            }
        }
        // For every user, find apartments that match the searches he's made, and mark them as 2 (if he hasn't reviewed or visited them)
        System.out.println("Calculating searches made");
        for (int i=0; i<users; i++) {
//            System.out.println("User " + i);
            ArrayList<Search> searchViewz = this.searchRepository.findAllByUser(allUsers.get(i));
            for (int r=0; r<searchViewz.size(); r++) {
                String lokation = searchViewz.get(r).getSearchlocation();
                for (int j=0; j<apartments; j++) {
                    if (lokation.equals(allAppartments.get(j).getLocation())) {
                        search_viewz++;
                        if (R[i][j] == 0) // User i has not rated apartment j
                            R[i][j] = 2.3;
                    }
                }
            }
        }
        System.out.println("reviewzz: " + reviewzz);
        System.out.println("app_Viewz: " + app_viewz);
        System.out.println("search_Viewz: " + search_viewz);
        System.out.println("GATHERED KNOWN DATA, STARTING FACTORIZATION");
        for (int i = 0; i < users; ++i) {
            for (int j = 0; j < genres; ++j) {
                P[i][j] = ThreadLocalRandom.current().nextGaussian();
//                System.out.println(P[i][j]);
            }
        }
        for (int i = 0; i < genres; ++i) {
            for (int j = 0; j < apartments; ++j) {
                Q[i][j] = ThreadLocalRandom.current().nextGaussian();
            }
        }
        double previous_e = 0;
        for (int s=0; s<steps; s++) {
            System.out.println("Factorization step " + s + " of " + steps);
            for (int i=0; i<users; i++) {
                for (int j=0; j<apartments; j++) {
                    if (R[i][j] > 0) {
                        double dot = 0.0;
                        for (int k=0; k<genres; k++) {
                            dot += P[i][k] * Q[k][j];
                        }
                        double eij = R[i][j] - dot;
                        for (int k=0; k<genres; k++) {
                            P[i][k] += h * (2 * eij * Q[k][j] - beta * P[i][k]);
                            Q[k][j] += h * (2 * eij * P[i][k] - beta * Q[k][j]);
                        }
                    }
                }
            }
            double e = 0;
            double sumOfAllErrors = 0; // ΓΙΑ ΝΑ ΞΕΡΟΥΜ ΤΙ ΜΑΣ ΓΙΝΕΤΑΙ
//            System.out.printf("%d\t, %d\n",users,apartments);
            for (int i=0; i<users; i++) { // possible mistake here
                for (int j=0; j<apartments; j++) {
                    if (R[i][j] > 0) {
                        double dot = 0.0;
                        for (int k=0; k<genres; k++) {
                            dot += P[i][k] * Q[k][j];
                        }
                        sumOfAllErrors += R[i][j] - dot;
                        e += ((R[i][j] - dot) * (R[i][j] - dot));
//                        for (int k=0; k<genres; k++) {
//                            e += ((beta / 2) * (P[i][k] * P[i][k] + Q[k][j] * Q[k][j]));
//                        }
                    }
                }
            }
            e = e / reviewzz; // 34.670 known reviews
            e = java.lang.Math.sqrt(e);
            double difference = previous_e - e;
            System.out.printf("e: %9f, Sum Of All Errors: %f\n", e, sumOfAllErrors);
            if (difference < 0.0000001 && s > 10) // If error hasn't been reduced, and we've done a small amount of steps (at least 2, let's say 10. Doesn't matter.)
                break;
            System.out.printf("Reduction of e: %9f\n", difference);
            previous_e = e;
        }
        double[][] result = new double[users][apartments];
        for (int i=0; i<users; i++) {
            for (int j=0; j<apartments; j++) {
                result[i][j] = 0;
                for (int k=0; k<genres; k++) {
                    result[i][j] += P[i][k] * Q[k][j];
                }
            }
        }
        System.out.println("MATRIX FACTORIZATION DONE, CALCULATING top6 USER VECTORS");
        for (int i=0; i<users; i++) { // For every user
            ArrayList<appartment> top6 = new ArrayList<appartment>();
            for (int t=0; t<6; t++) { // Recommend this many apartments
                // Find this user's highest rated apartment.
                // If it's an actual rating, not a prediction, make it 0.
                // If it's a prediction, save it to the "top 5" list and make it 0 (so we don't find it again in the next iteration to get 5 apartments).
                // If 5 non-rated apartments are not found, complete 5 recommendations with apartments he's actually rated -------TO BE IMPLEMENTED------------------
                double max_rating_found = 0;
                int max_rating_found_index = 0; 
                for (int j=0; j<apartments; j++) {
                    if (result[i][j] > max_rating_found) {
                        max_rating_found = result[i][j];
                        max_rating_found_index = j;
                    }
                }
//                System.out.print("Score: " + result[i][max_rating_found_index] + "_____");
                result[i][max_rating_found_index] = 0;
                if (R[i][max_rating_found_index] > 0) { // Apartment with max rating has actually been rated, we want a predicted one. Continue and don't count this iteration.
                    t--;
                    continue;
                }
                top6.add(allAppartments.get(max_rating_found_index));
            }
//            System.out.print("\n");
            UserVector uv1 = new UserVector();
            uv1.setUserName(allUsers.get(i).getUserName());
            ArrayList<Integer> temp=new ArrayList<Integer>();
            for(appartment app:top6) {
                temp.add(app.getId());
            }
            uv1.setIds(temp); // User vector is ready
//            for (int v=0; v<6; v++) {
//                System.out.print("vector spot " + v + ": " + uv1.getIds().get(v) + "_____");
//            }
//            System.out.print("\n");
            this.userVectorRepository.save(uv1);
        }
        System.out.println("DONE CREATING top6 USER VECTORS");
    }
//    public double RatingFunction(User u,appartment app){
//        ArrayList<Review> rv= (ArrayList<Review>) this.reviewRepository.findAllByAppartmentAndUser(app,u);
//        double sum=0.00;
//        for(Review r:rv){
//            System.out.println(r.getAppId()+"\t"+r.getUserName());
//            sum+=r.getNumber();
//        }
//        return sum/rv.size();
//    }
}
