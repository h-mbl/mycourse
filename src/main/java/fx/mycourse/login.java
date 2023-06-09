package fx.mycourse;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class Login extends BorderPane{
    private TextField matricule;
    private TextField password;
    private Button connectButton;
    private GridPane grid;
    private Label titleLabel;
    private VBox unique;
    private Stage stage;

    public Login(Stage stage) {
        this.stage = stage;
        titleLabel = getTitleLabel();
        grid = createForm();
        this.connectButton = new Button("Connect");
        this.connectButton.setAlignment(Pos.BASELINE_LEFT);
        unique = new VBox(titleLabel, grid, connectButton);
        unique.setAlignment(Pos.TOP_CENTER);
        unique.setSpacing(80);
        unique.setPadding(new Insets(10,10,10, 10 ));
        this.setCenter(unique);
    }
    
 
    private GridPane createForm() {
        GridPane grid = new GridPane();
        matricule = new TextField();
        password = new TextField();
    
        Label matriculeLabel = new Label("Matricule");
        Label passwordLabel = new Label("Password");
        matriculeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
        passwordLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
        grid.addRow(0, matriculeLabel, matricule);
        grid.addRow(1, passwordLabel, password);
    
        grid.setVgap(10);
        grid.getColumnConstraints().add(new ColumnConstraints(80));
        grid.setAlignment(Pos.CENTER);
        return grid;
    }
    
    private Label getTitleLabel() {
        Label titleLabel = new Label("Welcome to Mycourses");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 17px;");
        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.TOP_CENTER);
        titleBox.setPadding(new Insets(10, 0, 0, 0));
        return titleLabel;
    }
    
    /**
     * Configures the connect button
     */
    public Button getConnectButton() {
        return this.connectButton;
    }
    
    // getters and setters for matricule and password fields
    public TextField getMatricule() {
        return matricule;
    }
    public void setMatricule(TextField matricule) {
        this.matricule = matricule;
    }
    public TextField getPassword() {
        return password;
    }
    public void setPassword(TextField password) {
        this.password = password;
    }
}
