package fx.mycourse.server.models;
import java.io.Serializable;

public class LoginConnect implements Serializable{
    private String Matricule;
    private String Password;
    public LoginConnect (String Matricule, String Password){
        this.Matricule = Matricule;
        this.Password = Password;
    }
    public String getMatricule() {
        return Matricule;
    }

    public void setMatricule(String Matricule) {
        this.Matricule = Matricule;
    }
    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password= Password;
    }
    @Override
    public String toString() {
        return Matricule + " " + Password ;
    }
}
