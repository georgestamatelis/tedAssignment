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
   private  UserVectorRepository userVectorRepository;
   private AppViewRepository appViewRepository;
   private  SearchRepository searchRepository;
   ///////////////////////////////////////////////
    ///////////////////////////////////////
   private Long numberOfUsers;
   private  Long numberOfItems;
    private ArrayList<User> allUsers;
    private ArrayList<appartment> allAppartments;
    private ArrayList<Review> allReviews;
    //////////////////////////////////////
    private  Integer k=3 ;//Number of Latent features
    double V[][];
    double F[][];
  double h=0.0004;
    @Autowired
    public MatrixFactorization(UserRepository userRepository, AppartmentRepository appartmentRepository, ReviewRepository reviewRepository,AppViewRepository appViewRepository,SearchRepository searchRepository,UserVectorRepository userVectorRepository) {
        System.out.println("MATRIX FACTORISATION STARTS");
        this.userRepository = userRepository;
        this.appartmentRepository = appartmentRepository;
        this.reviewRepository = reviewRepository;
        this.userVectorRepository=userVectorRepository;
        this.searchRepository=searchRepository;
        this.appViewRepository=appViewRepository;
        /////////////////////////////////
        this.numberOfItems=appartmentRepository.count();
        this.numberOfUsers=userRepository.count();
        this.k=3;
        this.InitialiseVAndFRandomly();
        this.allReviews= (ArrayList<Review>) this.reviewRepository.findAll();
        this.allUsers= (ArrayList<User>) this.userRepository.findAll();
        this.allAppartments= (ArrayList<appartment>) this.appartmentRepository.findAll();
        ArrayList<appartment> KnownAppartments= (ArrayList<appartment>) this.appartmentRepository.findAllKnownAppartments();//new ArrayList<>();
        ArrayList<User>KnownUsers= (ArrayList<User>) this.userRepository.getAllKnownUsers(); //new ArrayList<>();

  /////////////////////
        System.out.println("GATHERED KNOWN DATA");
        double n=0.02;
        while(k>0){
            //STEP A
            //ITERATE OVER EACH KNOWN ELEMENT XIJ
             for(User u:KnownUsers){
                 ArrayList<Review> uReviews= (ArrayList<Review>) this.reviewRepository.findAllByUserName(u.getUserName());
                 ArrayList<appartment> uApps=new ArrayList<appartment>();
                 for(Review r: uReviews){
                     uApps.add(this.appartmentRepository.findById(r.getAppId()).get());
                 }
                 for(appartment ap:uApps){
                     //Now we are iterating over known elements
                  int i=allUsers.indexOf(u);
                  int j=allAppartments.indexOf(ap);
                  //KNOWN xij
                     double KnownXij=this.reviewRepository.findAllByAppartmentAndUser(ap,u).get(0).getNumber();
                     double EstimateXij=0.00;
                     for(int kappa=0;kappa<k;kappa++){
                         EstimateXij+=V[i][kappa]*F[kappa][j];
                     }
                     double Eij=KnownXij-EstimateXij;
                     double gradientv=-2*Eij*F[k-1][j];
                     double gradientf=-2*Eij*V[i][k-1];
                    for(int collumn=0;collumn<this.allUsers.size();collumn++)
                        for(int kappa=0;kappa<k;kappa++)
                            V[i][kappa]+=n*2*Eij*F[kappa][j];
                    for(int row=0;row<this.allAppartments.size();row++)
                        for(int kappa=0;kappa<k;kappa++)
                            F[kappa][j]+=n*2*Eij*V[i][kappa];
                 }
                 System.out.println("Done with user "+KnownUsers.indexOf(u));
             }

            //LOOP OVER KNOWN ELEMENTS DONE CALCULATE MEAN SQUARE ERROR AND CHECK TERMINATING CONDITION HERE
            break;
        }
        //FACTORIZATION DONE CALCULATE USER VECTORS
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
    private  void InitialiseVAndFRandomly(){
        Random r = new Random();
        V=new double[Math.toIntExact(numberOfUsers)][k];
        F=new double[k][Math.toIntExact(numberOfItems)];
        for(int i=0;i<k;i++)
            for(int j=0;j<numberOfItems;j++)
                F[i][j]=(Math.random()*((5-0)+1))+0;
        for(int i=0;i<numberOfUsers;i++)
            for(int j=0;j<k;j++)
                V[i][j]=(Math.random()*((5-0)+1))+0;
    }
}
