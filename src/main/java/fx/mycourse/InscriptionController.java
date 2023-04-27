package fx.mycourse;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.paint.Color;
import fx.mycourse.server.models.Course;
import fx.mycourse.server.models.RegistrationForm;
import javafx.geometry.Side;
import javafx.scene.control.MenuItem;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * InscriptionController est destiné à gérer les interactions entre le client et le serveur via:
 * le modèle: RegistrationForm et Course
 * la Vue: InscriptionView
 */
public class InscriptionController {
    /**
     * Permet d'accéder à InscriptionView
     */
    private InscriptionView view;

    /**
     * Sélectionne la session
     */
    private String selectedSession;

    //     LES ATTRIBUTS ET OBJETS DE CONNEXION
    /**
     * Adresse du serveur auquel le client veut se connecter
     */
    private static final String HOST = "127.0.0.1";

    /**
     * Port du serveur auquel le client veut se connecter
     */
    private static final int PORT = 1337;

    /**
     * La sortie des arguments du client vers le serveur
     */
    private ObjectOutputStream objectOutputStream;

    /**
     * L’entrée des arguments du serveur vers le client
     */
    private ObjectInputStream objectInputStream;

    /**
     * La prise du client
     */
    private Socket clientSocket;

    public InscriptionController(InscriptionView view) throws IOException {

        //      LA CONNEXION
        try {
            this.clientSocket = new Socket(HOST, PORT);
            this.view = view;
        }catch (Exception e){
            errorAlertConnexion();
        }

        //      LES ACTIONS
        /*
         * Ajoute une action sur le bouton session dans le but d'afficher le menu contextuel
         * et affiche les sessions dans le menu contextuel.
         */
        this.view.getsessionButton().setOnAction((action) -> {
            view.getContextMenuSession().show(view.getsessionButton(), Side.BOTTOM, 0, 0);
        });

        /*
         * Ajoute une action lorsqu’on clique sur un element dans le menu contextuel
         * changer la valeur afficher dans l'interface du bouton session par l'élément sélectionné
         * dans le menu contextuel
         */
        this.view.getContextMenuSession().setOnAction(this::changeValueBtnSession);

        // lorsqu'on clique sur le bouton charger
        this.view.getChargerButton().setOnAction((action) -> {
            // réinitialise la couleur des bordures du tableau, des boutons et des grilles
            colorInitializer();

            // charge une session
            getCourses();

            // place les cours de la session sélectionnée dans le tableau
            setItemsInTab();

            /*
             * reconnecte le client au serveur
             * @throws IOException
             */
            try {
                reconnect();
            } catch (IOException e) {
                errorAlertConnexion();
            }
        });

        // lorsqu'on clique sur le bouton envoyer
        this.view.getSendButton().setOnAction((action) -> {
            // réinitialise la couleur des bordures du tableau, des boutons et des grilles
            colorInitializer();

            // envoie la requête d'inscription au cours au serveur
            doInscription();

            // reconnecter le client au serveur
            try {
                reconnect();
            } catch (IOException e) {
                errorAlertConnexion();
            }
        });
    }

    //      LES METHODES DE CONNEXION
    /**
     * Reconnecte le client apres une requête vers le serveur
     * Start() recrée une prise avec le meme port et vers le meme serveur
     * @throws IOException si une erreur survient lors de la fermeture de la prise
     */
    private void reconnect() throws IOException {
        // ferme la prise
        if (clientSocket != null) {
            try {
                objectOutputStream.close();
                objectInputStream.close();
                clientSocket.close();
            } catch (IOException e) {
                errorAlertConnexion();
            }
        }
        this.clientSocket = new Socket(HOST, PORT);
        this.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
    }

    /**
     * Créer la prise du client avec le serveur
     * @throws IOException si une erreur arrive lors de l'initialisation de la prise
     */
    public void start() throws IOException {
        try {
            this.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
           errorAlertConnexion();
        }
    }

    //      LES METHODES QUI COMMUNIQUENT AVEC MODEL ET VIEW
    /**
     * Envoie la requête au serveur de fournir la liste des cours d'une session
     */
    private void getCourses() {
        ArrayList<Course> courses;

        // vérifie si une session est sélectionnée
        if (selectedSession != null) {

            // essaye si la connexion est établie
            try {
                // envoie la requête CHARGER + le nom de la session au serveur
                objectOutputStream.writeObject("CHARGER " + selectedSession);

                // enregistre la réponse du serveur dans un objet
                Object obj = objectInputStream.readObject();

                // vérifie si l'objet est une instance de cours
                if (obj instanceof ArrayList) {
                    courses = (ArrayList<Course>) obj;

                    // si aucun cours n’est renvoyé, le controller affiche le message d'erreur
                    if (courses.isEmpty()) {
                        errorAlert(2);
                    } else {
                        /*
                         * sinon il transforme la liste de cours en une liste observable
                         * ensuite, il place les éléments dans le tableau
                         */
                        ObservableList<Course> courseShow = FXCollections.observableArrayList(courses);
                        view.getTableView().setItems(courseShow);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            // si aucune session n’est sélectionnée, il affiche un message d'erreur
            errorAlert(1);
        }
    }

    /**
     * Envoie la requête au serveur d'inscrire un étudiant à un cours
     */
    private void doInscription() {
        // envoie les informations écrit dans le formulaire
        RegistrationForm form =
                new RegistrationForm(view.getFirstName(), view.getName(), view.getEmail(), view.getMatricule(),
                        view.getSelectedCourse());

        // crée un tableau d'erreurs
        List<String> errorMsgs = new ArrayList<>();

        // vérifie si un cours et sélectionné dans le tableau
        if (view.getSelectedCourse() != null) {

            // essaye si la connexion est établie
            try {
                // envoie la requête INSCRIRE + l'objet form
                objectOutputStream.writeObject("INSCRIRE");
                objectOutputStream.writeObject(form);

                // lit la réponse du serveur
                Object answer = objectInputStream.readObject();
                if (answer instanceof Boolean) {
                    confirmationRegistration();
                } else if (answer instanceof String) {
                    System.out.println("\n" + answer + "\n");
                } else {
                    // définit la couleur de la bordure
                    BorderStroke borderStroke = new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, new BorderWidths(1));
                    Border border = new Border(borderStroke);

                    /*
                     * condition pour chaque champs du formulaire
                     * ajoute l'erreur dans le tableau
                     * et change la bordure du champ
                     */
                    ArrayList<Integer> info = (ArrayList<Integer>) answer;
                    if (info.get(0) == 1) {
                        view.firstName.setBorder(border);
                        errorMsgs.add("Le prénom est incorrect.");
                    }
                    if (info.get(1) == 1) {
                        view.name.setBorder(border);
                        errorMsgs.add("Le nom est incorrect.");
                    }
                    if (info.get(2) == 1) {
                        view.email.setBorder(border);
                        errorMsgs.add("Le mail est incorrect.");
                    }
                    if (info.get(3) == 1) {
                        view.matricule.setBorder(border);
                        errorMsgs.add("Le matricule est incorrect.");
                    }
                    // affiche les erreurs si la liste n'est vide pas vide
                    if (!errorMsgs.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        for (String msg : errorMsgs) {
                            sb.append(msg).append("\n");
                        }
                        view.showAlert("Ces informations sont incorrecte", sb.toString());
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            // affiche les erreurs lient à la sélection d'un cours
        } else if (selectedSession == null && view.getSelectedCourse() == null) {
            errorAlert(0);
        } else if (selectedSession == null) {
            errorAlert(1);
        } else {
            errorAlert(2);
        }
    }

    //      METHODES
    /**
     * Sauvegarde le cours sélectionné dans le menu Contextuel
     * récupère la valeur deja dans le bouton et la remplace par
     * affiche dans l'interface du bouton, le cours sélectionné dans le menu Contextuel
     */
    private void changeValueBtnSession(ActionEvent action){
        MenuItem item = (MenuItem) action.getTarget();
        selectedSession = item.getText();
        view.getsessionButton().setText(selectedSession);
        view.setSelectedSession(selectedSession);
    }


    /**
     * Initialise la couleur des bordures
     */
    private void colorInitializer() {
        Border transparent = Border.EMPTY;
        view.changeSessionButtonColor("transparent");
        view.changeTableBorder("transparent");
        view.firstName.setBorder(transparent);
        view.name.setBorder(transparent);
        view.email.setBorder(transparent);
        view.matricule.setBorder(transparent);
    }

    /**
     * Affiche les erreurs et change la bordure en rouge
     */
    private void errorAlert(int status) {
        switch (status) {
            case 0 :
                view.changeSessionButtonColor("red");
                view.changeTableBorder("red");
                view.showAlert("Session invalide et cours invalide", "Veuillez sélectionner une session et un cours");
                break;

            case 1 :
                view.changeSessionButtonColor("red");
                view.showAlert("Session invalide", "Veuillez sélectionner une session");
                break;

            case 2 :
                view.changeTableBorder("red");
                view.showAlert("Cours invalide", "Veuillez sélectionner un cours");
                break;

        }
    }

    /**
     * Pour les erreurs lient à la connexion avec le serveur
     */
    private void errorAlertConnexion(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de connexion");
        alert.setHeaderText("Impossible de se connecter au serveur \n Veuillez réessayer");
        alert.showAndWait();
    }

    /**
     * Affiche le message de confirmation
     * efface les elements dans le tableau, le formulaire et réinitialise la valeur du bouton session
     */
    private void confirmationRegistration(){
        view.ConfirmationDialog(view.getFirstName(), view.getSelectedCourse().getName());
        view.getTableView().getItems().clear();
        view.getsessionButton().setText("Selectionnez une session");
        selectedSession = null;
        view.firstName.setText("");
        view.name.setText("");
        view.email.setText("");
        view.matricule.setText("");
    }

    //      SETTER
    /**
     * Place les éléments dans le tableau
     */
    private void setItemsInTab(){
        view.getTableView().getSelectionModel().selectedItemProperty().
                addListener((observable, oldValue, newValue) -> {
                    view.setSelectedCourse(newValue);
                });
    }
}