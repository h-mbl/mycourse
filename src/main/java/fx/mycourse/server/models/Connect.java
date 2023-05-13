package fx.mycourse.server.models;

import java.io.Serializable;

public class Connect implements Serializable {
    public String name;
    public String prenom;
    public String email;
    public String sexe;
    public int DateDAdmission ;
    public String matricule;
    public int nbCoursComplete;
    public int nbCoursEnCours;
    public int nbCreditComplete;
    public int nbCreditRestant;
    public String Cours;
    public Connect(String name,String prenom,String email,String sexe,int DateDAdmission,String matricule,
    int nbCoursComplete,int nbCoursEnCours,int nbCreditComplete,int nbCreditRestant, String Cours){
        this.name=name;
        this.prenom=prenom;
        this.email=email;
        this.sexe=sexe;
        this.DateDAdmission=DateDAdmission;
        this.matricule=matricule;
        this.nbCoursComplete=nbCoursComplete;
        this.nbCoursEnCours=nbCoursEnCours;
        this.nbCreditComplete=nbCreditComplete;
        this.nbCreditRestant=nbCreditRestant;
        this.Cours=Cours;
        }
}
