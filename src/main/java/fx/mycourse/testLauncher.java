package fx.mycourse;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class testLauncher extends Application{
    @Override
    public void start(Stage stage) throws Exception {

        ModelPage p= new ModelPage();
        Scene scene = new Scene(p, 800, 600);

        // stage
        stage.setScene(scene);
        stage.setTitle("Mycourses");
        stage.show();

        // connecte le client au serveur
        //Controller.start();
    }

    /**
     * main crée une instance d'InscriptionLauncher et lance le programme
     * @param args un tableau d'arguments de ligne de commande
     */
    public static void main(String[] args) {
        //tentative de lancement
            launch(args);
            // Arrête le programme si un composant est manquant

    }
}

