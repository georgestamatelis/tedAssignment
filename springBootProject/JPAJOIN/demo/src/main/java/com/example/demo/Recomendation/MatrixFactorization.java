/*package com.example.demo.Recomendation;

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
        this.users= (int) this.userRepository.count();
        this.appartmentRepository = appartmentRepository;
        this.apartments= (int) this.appartmentRepository.count();
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
        System.out.println("GATHERING KNOWN DATA");
        this.allUsers = (ArrayList<User>) this.userRepository.findAllByOrderByUserNameAsc(); // USERS LIST (SORTED)
        this.allAppartments = (ArrayList<appartment>) this.appartmentRepository.findAll(); // APARTMENT LIST
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
        System.out.println("reviewzz: " + reviewzz);
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
            System.out.printf("e: %f, Sum Of All Errors: %f\n", e, sumOfAllErrors);
//            if (e <= 10)
//                break;
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
        System.out.println("MATRIX FACTORIZATION DONE, CALCULATING top5 USER VECTORS");
        for (int i=0; i<users; i++) { // For every user
            ArrayList<appartment> top5 = new ArrayList<appartment>();
            for (int t=0; t<5; t++) { // Recommend this many apartments
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
                System.out.print("Score: " + result[i][max_rating_found_index] + "_____");
                result[i][max_rating_found_index] = 0;
                if (R[i][max_rating_found_index] > 0) { // Apartment with max rating has actually been rated, we want a predicted one. Continue and don't count this iteration.
                    t--;
                    continue;
                }
                top5.add(allAppartments.get(max_rating_found_index));
            }
            System.out.print("\n");
            UserVector uv1 = new UserVector();
            uv1.setUserName(allUsers.get(i).getUserName());
            ArrayList<Integer> temp=new ArrayList<Integer>();
            for(appartment app:top5)
            {
                temp.add(app.getId());
            }
            uv1.setIds(temp); // User vector is ready
            for (int v=0; v<5; v++) {
                System.out.print("vector spot " + v + ": " + uv1.getIds().get(v) + "_____");
            }
            System.out.print("\n");
            this.userVectorRepository.save(uv1);
        }
        System.out.println("DONE CREATING top5 USER VECTORS");
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
*/