package fx.mycourse;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ModelPage extends BorderPane {
    public ModelPage(){
        // Pas besoin de créer un nouveau BorderPane, car cette classe hérite déjà de BorderPane
        // BorderPane root = new BorderPane();

        VBox topBox = new VBox();
        topBox.setStyle("-fx-background-color: #E6E6E6;");
        topBox.setMinWidth(750);
        setTop(topBox); // Utiliser setTop sur l'instance courante de ModelPage

        // Création de la partie inférieure (20% de la page)
        VBox bottomBox = new VBox();
        bottomBox.setStyle("-fx-background-color: #F2F2F2;");
        setBottom(bottomBox); // Utiliser setBottom sur l'instance courante de ModelPage
        BorderPane.setMargin(bottomBox, new Insets(10, 10, 10, 10));

        SplitPane splitPane = new SplitPane();

        // Création des trois parties horizontales de la partie inférieure
        VBox vbox1 = new VBox();
        vbox1.setStyle("-fx-background-color: #D3D3D3;");
        Button button1 = new Button("My Course");
        VBox.setVgrow(button1, Priority.ALWAYS);
        button1.setAlignment(Pos.CENTER);
        button1.setMinWidth(260);
        vbox1.setMinWidth(220);
        vbox1.getChildren().add(button1);

        VBox vbox2 = new VBox();
        vbox2.setStyle("-fx-background-color: #C0C0C0;");
        Button button2 = new Button("Registration");
        VBox.setVgrow(button2, Priority.ALWAYS);
        button2.setAlignment(Pos.CENTER);
        button2.setMinWidth(266);
        vbox2.setMinWidth(220);
        vbox2.getChildren().add(button2);

        VBox vbox3 = new VBox();
        vbox3.setStyle("-fx-background-color: #A9A9A9;");
        Button button3 = new Button("About");
        button3.setMinWidth(260);
        VBox.setVgrow(button3, Priority.ALWAYS);
        button3.setAlignment(Pos.CENTER);
        vbox3.setMinWidth(220);
        vbox3.getChildren().add(button3);

        // Ajout des Vbox dans le SplitPane
        splitPane.getItems().addAll(vbox1, vbox2, vbox3);

        // Configuration de la taille des Vbox
        splitPane.setDividerPositions(0.33, 0.67);

        // Ajout du SplitPane dans la partie inférieure de la page
        this.setBottom(splitPane);
    }
}
