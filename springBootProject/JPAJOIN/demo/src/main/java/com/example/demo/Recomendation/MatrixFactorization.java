package com.example.demo.Recomendation;

import com.example.demo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Random;

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
    int genres = 3;//Number of Latent features
    double P[][];
    double Q[][];
    double R[][];
    int steps = 5000; double h = 0.0004; double beta = 0.02;
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
        P=  new double[users][genres];
        Q=  new double[genres][apartments];
        R = new double[users][apartments];
        ///////////////
        System.out.println("GATHERING KNOWN DATA");
        this.allUsers = (ArrayList<User>) this.userRepository.findAllByOrderByUserNameAsc(); // USERS LIST (SORTED)
        this.allAppartments = (ArrayList<appartment>) this.appartmentRepository.findAll(); // APARTMENT LIST
        for (int j=0; j<apartments; j++) {
            System.out.println("Apartment " + j);
            ArrayList<Review> reviewz = (ArrayList<Review>) this.reviewRepository.findAllByAppartment(allAppartments.get(j));
            for (int r=0; r< reviewz.size(); r++) {
                String user_name = reviewz.get(r).getUserName();
                for (int i=0; i<users; i++) {
                    if (user_name == allUsers.get(i).getUserName()) {
                        if (R[i][j] == 0) // First review of user i for apartment j
                            R[i][j] = reviewz.get(r).getNumber();
                        else
                            R[i][j] = (R[i][j] + reviewz.get(r).getNumber()) / 2;
                    }
                }
            }
        }
        System.out.println("GATHERED KNOWN DATA, STARTING FACTORIZATION");
        for (int i = 0; i < users; ++i) {
            for (int j = 0; j < genres; ++j) {
                P[i][j] = randd.nextDouble();
            }
        }
        for (int i = 0; i < genres; ++i) {
            for (int j = 0; j < apartments; ++j) {
                Q[i][j] = randd.nextDouble();
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
            for (int i=0; i<users; i++) { // possible mistake here
                for (int j=0; j<apartments; j++) {
                    if (R[i][j] > 0) {
                        double dot = 0.0;
                        for (int k=0; k<genres; k++) {
                            dot += P[i][k] * Q[k][j];
                        }
                        e += ((R[i][j] - dot) * (R[i][j] - dot));
                        for (int k=0; k<genres; k++) {
                            e += ((beta / 2) * (P[i][k] * P[i][k] + Q[k][j] * Q[k][j]));
                        }
                    }
                }
            }
            if (e < 0.001)
                break;
            System.out.printf("e: %f\n", e);
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
        System.out.println("MATRIX FACTORIZATION DONE");
    }
    public double RatingFunction(User u,appartment app){
        ArrayList<Review> rv= (ArrayList<Review>) this.reviewRepository.findAllByAppartmentAndUser(app,u);
        double sum=0.00;
        for(Review r:rv){
            System.out.println(r.getAppId()+"\t"+r.getUserName());
            sum+=r.getNumber();
        }
        return sum/rv.size();
    }
}