package fx.mycourse;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import fx.mycourse.server.models.Course;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;


public class InscriptionView extends BorderPane {
    /**
     * Divise la fenêtre en deux parties
     */
    private SplitPane splitPane;

    /**
     * Affiche le conteneur gauche du splitPane
     */
    private VBox leftPane;

    /**
     * Affiche le conteneur gauche du splitPane
     */
    private VBox rightPane;

    /**
     * Représente la session sélectionnée
     */
    private String selectedSession= null;

    /**
     * Champ prénom pour la saisie du client
     */
    public TextField firstName;

    /**
     * Champ nom pour la saisie du client
     */
    public TextField name;

    /**
     * Champ mail pour la saisie du client
     */
    public TextField email;

    /**
     * Champ matricule pour la saisie du client
     */
    public TextField matricule;

    /**
     * Bouton pour sélectionner une session
     */
    private Button session ;

    /**
     * Bouton pour charger le cours d'une session
     */
    private Button charger;

    /**
     * Bouton pour envoyer le formulaire d'inscription
     */
    private Button sendButton ;

    /**
     * Tableau pour afficher les cours
     */
    private TableView<Course> tableView;

    /**
     * Pour afficher le context menu
     */
    private ContextMenu contextMenuSession;

    /**
     * Enregistre le cours sélectionné
     */
    private ObjectProperty<Course> selectedCourse = new SimpleObjectProperty<>();

    /**
     * Organise les champs
     */
    private GridPane grid ;

    /**
     * Pour afficher le titre du formulaire
     */
    private Label titleLabel;


    /**
     * Gère l'interface utilisateur du programme
     */
    public InscriptionView() {
        // lance les deux méthodes pour afficher les elements
        columnLeft();
        columnRight();

        // ajoute les conteurs au splitPane
        splitPane = new SplitPane(leftPane, rightPane);
        //splitPane.setPrefSize(800, 600);
        this.setCenter(splitPane);
    }

    //      LA COLONNE GAUCHE

    /**
     * Crée la colonne gauche
     */
    public void columnLeft() {
        // Crée le tableau
        tableView = new TableView<>();
        createTable();

        // permet à la table de redimensionner les colonnes automatiquement en fonction de la taille de la table
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Ajouter les boutons en bas du tableau
        HBox buttonsBox = createButtonsBox();

        //Crée le conteneur pour le titre et le tableau
        VBox topBox = createTopBox();

        // Place les deux conteneurs dans un conteneur VBox global
        leftPane = new VBox(topBox, buttonsBox);
        leftPane.setPrefSize(400, 300);
        leftPane.setPadding(new Insets(10));

        // Ajoute le conteneur VBox global dans le pane de gauche du SplitPane
        this.setLeft(leftPane);
    }

    //      LES METHODES DE LA COLONNE GAUCHE
    /**
     * Configure le Box contenant le titre de la colonne gauche et le titre de la colonne gauche,
     * @return le Box contenant le titre
     */
    private VBox createTopBox() {
        Label label = new Label("Liste de cours");
        label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        VBox topBox = new VBox(label, tableView);
        topBox.setSpacing(10);
        topBox.setPadding(new Insets(10));
        topBox.setAlignment(Pos.TOP_CENTER);
        return topBox;
    }

    /**
     * Ajoute les boutons en bas du tableau
     * @return le Box Horizontal contenant les deux Boutons
     */
    private HBox createButtonsBox() {
        HBox buttonsBox = new HBox(100);
        buttonsBox.setAlignment(Pos.BOTTOM_CENTER);
        buttonsBox.setPadding(new Insets(10));
        session = new Button("Selectionnez une session");
        charger = new Button("Charger");
        session.setPrefSize(90, 27);
        configureSessionContextMenu();
        buttonsBox.getChildren().addAll(session, charger);
        return buttonsBox;
    }

    /**
     * Configure le context menu de session
     */
    private void configureSessionContextMenu() {
        contextMenuSession = new ContextMenu();
        MenuItem Automne = new MenuItem("Automne");
        MenuItem Hiver = new MenuItem("Hiver");
        MenuItem Ete = new MenuItem("Ete");
        contextMenuSession.getItems().addAll(Automne,Hiver,Ete);
        session.setContextMenu(contextMenuSession);
    }

    /**
     * Configure le tableau (les colonnes et les affichages des éléments dans le tableau)
     */
    private void createTable(){
        tableView = new TableView<>();
        TableColumn<Course, String> columnCode = new TableColumn<>("Code");
        columnCode.setPrefWidth(150);
        columnCode.setStyle("-fx-alignment: CENTER;");
        TableColumn<Course, String> columnCourse = new TableColumn<>("Cours");
        columnCourse.setPrefWidth(250);
        columnCourse.setStyle("-fx-alignment: CENTER;");
        columnCourse.setCellValueFactory(new PropertyValueFactory<Course, String>("name"));
        columnCode.setCellValueFactory(new PropertyValueFactory<Course, String>("code"));
        tableView.getColumns().addAll(columnCode, columnCourse);
    }

    //      LES METHODES POUR CHANGER LE STYLE DES ELEMENTS DE LA COLONNE GAUCHE


    /**
     * Change la bordure du bouton session
     * @param color la couleur avec laquelle on veut changer
     */
    public void changeSessionButtonColor(String color) {
        session.setStyle("-fx-border-color:" + color);
    }

    /**
     * change la bordure
     * @param color la couleur avec laquelle on veut changer
     */
    public void changeTableBorder(String color){
        tableView.setStyle("-fx-border-color:" + color );
    }

    //      LES GETTERS DES ELEMENTS DE LA COLONNE GAUCHE
    /**
     * Récupère le menu contextuel
     * @return le menu contextuel
     */
    public ContextMenu getContextMenuSession() {
        return this.contextMenuSession;
    }

    /**
     * @return la session sélectionnée dans l'interface du bouton
     */
    public Button getsessionButton() {
        return this.session;
    }

    /**
     * @return le bouton charger
     */
    public Button getChargerButton(){
        return this.charger;
    }

    /**
     * @return le bouton envoyer
     */
    public Button getSendButton(){
        return this.sendButton;
    }

    /**
     * @return le tableau et permet d'y accéder
     */
    public TableView<Course> getTableView() {
        return tableView;
    }

    /**
     * Récupère le cours sélectionné dans le tableau
     * @return null si aucun n’est sélectionné sinon il retourne le cours sélectionné
     */
    public Course getSelectedCourse() {
        Course selectedCourseObject = selectedCourse.get();
        if (selectedCourseObject == null) {
            return null;
        }
        return new Course( selectedCourseObject.getName(),selectedCourseObject.getCode(),
                selectedCourseObject.getSession());
    }


    //      LES SETTERS DES ELEMENTS DE LA COLONNES
    /**
     * Définit l'item sélectionné dans le menu contextuel comme la valeur du bouton sélectionne
     * @param selectedSession la session selection
     */
    public void setSelectedSession(String selectedSession) {
        this.selectedSession = selectedSession;
    }

    /**
     * Définit le cours sélectionné dans le tableau
     * @param course le cours sélectionné
     */
    public void setSelectedCourse(Course course) {
        selectedCourse.set(course);
    }

    //      LA COLONNE DROITE
    /**
     * Crée la colonne droite
     */
    private void columnRight() {
        // Crée le titre
        titleLabel = titleRight();

        // Crée le titre
        grid = createForm();

        // Crée le bouton d'envoi
        sendButton = sendButtonRight();

        // Crée le conteneur VBox de la partie droite
        VBox vbox = VboxRight();

        // Ajoute le conteneur VBox global dans le pane de gauche du SplitPane
        rightPane = new VBox(vbox);
        rightPane.setPadding(new Insets(10));
        rightPane.setSpacing(10);
    }

    //     LES  METHODES DE LA COLONNE DROITE
    /**
     * configure le formulaire
     * @return grid
     */
    public GridPane createForm() {
        grid = new GridPane();
        firstName = new TextField();
        name = new TextField();
        email = new TextField();
        matricule = new TextField();

        Label firstNameLabel = new Label("Prénom:");
        Label lastNameLabel = new Label("Nom:");
        Label emailLabel = new Label("Email:");
        Label matriculeLabel = new Label("Matricule:");

        grid.addRow(0, firstNameLabel, firstName);
        grid.addRow(1, lastNameLabel, name);
        grid.addRow(2, emailLabel, email);
        grid.addRow(3, matriculeLabel, matricule);

        grid.setVgap(10);
        grid.getColumnConstraints().add(new ColumnConstraints(100));
        return grid;
    }

    /**
     * Configure le bouton envoyer
     * Créer une VBox pour contenir la grille de formulaire et le bouton
     * Ajouter une marge autour de la VBox
     * Ajouter un espace entre les éléments de la VBox
     */
    public VBox VboxRight() {
        VBox vbox = new VBox(titleLabel, grid, sendButton);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(50);
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }

    /**
     * Ajouter le titre et la VBox à la droite du conteneur principal
     */
    public Label titleRight(){
        Label titleLabel = new Label("Formulaire d'Inscription");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(10, 0, 0, 0));
        return titleLabel;
    }

    /**
     * Configure le bouton envoyer
     */
    public Button sendButtonRight() {
        Button sendButton = new Button("Envoyer");
        sendButton.setAlignment(Pos.CENTER);
        return sendButton;
    }

    //      LES GETTERS POUR LA COLONNE DROITE
    /**
     * @return le texte dans le champ nom
     */
    public String getFirstName() {
        return firstName.getText();
    }

    /**
     * @return le texte dans le champ name
     */
    public String getName() {
        return name.getText();
    }

    /**
     * @return le texte dans le champ mail
     */
    public String getEmail() {
        return  email.getText();
    }

    /**
     * @return le texte dans le champ matricule
     */
    public String getMatricule() {
        return matricule.getText();
    }

    //      LES METHODES POUR LES BOITES DE DIALOGUES
    /**
     * Affiche la confirmation de l'inscription dans une boite de dialogue
     * @param firstName le nom du client
     * @param selectedCourse le cours sélectionné
     */
    public void ConfirmationDialog(String firstName, String selectedCourse) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setHeaderText("Message de confirmation");
        alert.setContentText("Félicitations!\nInscription réussie de "  + firstName+  " au cours " +selectedCourse);
        alert.getButtonTypes().remove(ButtonType.CANCEL);
        alert.showAndWait();
    }

    /**
     * Affiche une boite de dialogue alerte lorsque le client n’a pas choisi de cours ou session
     * ou remplit correctement le formulaire
     * @param title le titre de l'alerte
     * @param header le contenu de l'alerte
     */
    public void showAlert(String title, String header) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }
}