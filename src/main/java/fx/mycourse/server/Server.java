package fx.mycourse.server;

import javafx.util.Pair;
import fx.mycourse.server.models.Course;
import fx.mycourse.server.models.RegistrationForm;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {

    /**
     * Commande permettant d'inscrire le client à un cours
     */
    public final static String REGISTER_COMMAND = "INSCRIRE";

    /**
     * Commande permettant de montrer les cours d'une session à un client
     */
    public final static String LOAD_COMMAND = "CHARGER";

    /**
     * La prise du serveur
     */
    private final ServerSocket server;

    /**
     * La prise du client
     */
    private Socket client;

    /**
     * L'entrée des arguments du client vers le serveur
     */
    private ObjectInputStream objectInputStream;

    /**
     * La sortie des arguments du serveur vers le client
     */
    private ObjectOutputStream objectOutputStream;

    /**
     * ArrayList contenant les évènements à gérer
     */
    private final ArrayList<EventHandler> handlers;

    /**
        * Crée un nouveau serveur qui écoute sur le port spécifié.
        * @param port Le port sur lequel le serveur doit écouter.
        * @throws IOException Si une erreur se produit lors de la création du socket du serveur.
    */
    public Server(int port) throws IOException {
        this.server = new ServerSocket(port,1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    /**
     * Ajoute un événement h au géreur d’événements.
     * @param h de type EventHandler, l’événement à ajouter.
     */
    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    /**
     * Mets en action les événements ajoutés.
     * @param cmd la commande à renvoyer
     * @param arg l’argument a donné à la commande.
     * @throws IOException si une erreur survient lors de la gestion des commandes
     */
    private void alertHandlers(String cmd, String arg) throws IOException {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    /**
     * Rend le serveur capable de recevoir les requêtes d'un client
     */
    public void run() {
        while (true) {
            try {
                client = server.accept();
                System.out.println("Connecté au client: " + client);
                objectInputStream = new ObjectInputStream(client.getInputStream());
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                listen();
                disconnect();
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Récupère les entrées du client.
     * @throws IOException si une erreur se produit lors de la lecture des entrées.
     * @throws ClassNotFoundException si une classe n’est pas trouvé
     */
    public void listen() throws IOException, ClassNotFoundException {
        String line;

       if ((line = this.objectInputStream.readObject().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    /**
     * Mets en page la commande obtenue.
     * @param line une commande
     * @return Pair la separation entre le nom de la commande et son argument
     */
    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    /**
     * Déconnecte le client du serveur.
     * @throws IOException si une erreur se produit lors de la déconnection.
     */
    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    /**
     * Gère la commande donnée.
     * @param cmd la commande à traiter
     * @param arg l'argument a utilisé avec la commande.
     * @throws IOException lorsqu'une erreur survient lors de la manipulation des fichiers
     */
    public void handleEvents(String cmd, String arg) throws IOException {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     Lire un fichier texte contenant des informations sur les cours et les transformer en liste d'objets 'Course'.
     La méthode filtre les cours par la session spécifiée en argument.
     Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(String arg) throws IOException {
        ArrayList<Course> relevantClasses = new ArrayList<>();
        try {
            FileReader classes = new FileReader("src/main/java/server/data/cours.txt");
            BufferedReader reader = new BufferedReader(classes);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] content = line.split("\t");
                if (content[2].equals(arg)) {
                    relevantClasses.add(new Course(content[1], content[0], content[2]));
                }
            }
            objectOutputStream.writeObject(relevantClasses);
            reader.close();
        } catch (Exception e) {
            objectOutputStream.writeObject("\nUne erreur est survenue lors de la lecture du fichier\n");
            throw new RuntimeException(e);
        }
    }

    /**
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     et renvoyer un message de confirmation au client.
     La méthode gère les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration() throws IOException {
        String session, code, matricule, prenom, nom, email;
        Course course;
        BufferedWriter writer;
        ArrayList<Integer> errors = new ArrayList<>();
        try {
            Object form = objectInputStream.readObject();
            if (form instanceof RegistrationForm) {
                RegistrationForm rForm = (RegistrationForm) form;
                prenom = rForm.getPrenom();
                nom = rForm.getNom();
                email = rForm.getEmail();
                matricule = rForm.getMatricule();
                course = rForm.getCourse();
                session = course.getSession();
                code = course.getCode();
                if (prenom.trim().isEmpty()) {
                    errors.add(1);
                } else if (!prenom.matches("^[a-zA-Z]+$")) {
                    errors.add(1);
                } else {
                    errors.add(0);
                }
                if (nom.trim().isEmpty()) {
                    errors.add(1);
                } else if (!nom.matches("^[a-zA-Z]+$")) {
                    errors.add(1);
                } else {
                    errors.add(0);
                }
                if (email.trim().isEmpty() || !checkEmail(email)) {
                    errors.add(1);
                } else {
                    errors.add(0);
                }
                if ( matricule.trim().isEmpty() || !isNumeric(matricule) || !checkMatricule(matricule)) {
                    errors.add(1);
                } else {
                    errors.add(0);
                }
                if( !errors.contains(1)) {
                    writer = new BufferedWriter(new FileWriter("data/inscription.txt", true));
                    writer.append(session).append("\t").append(code).append("\t").append(matricule).append("\t").append(prenom).append("\t").append(nom).append("\t").append(email).append("\t\n");
                    writer.close();
                    objectOutputStream.writeObject(true);
                } else {
                    objectOutputStream.writeObject(errors);
                }
            }
        } catch (Exception e) {
            objectOutputStream.writeObject("\nUne erreur est survenue lors de la lecture du fichier\n");
            throw new RuntimeException(e);
        }
    }

    /**
     * Vérifie que le mail donné par le client est valide.
     * @param email le mail entré par le client
     * @return la validité du mail
     */
    private boolean checkEmail(String email) {
        Pattern EMAIL_PATTERN  = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    /**
     * Vérifie que le matricule donné par le client est valide.
     * @param matricule le matricule entré par le client
     * @return la validité du matricule
     */
    private boolean checkMatricule(String matricule) {
        int matriculeInt = Integer.parseInt(matricule);
        return matriculeInt<=99999999 && matriculeInt >=10000000;
    }

    /**
     * Vérifie que les infos du cours donnés par le client existent.
     * @param course le cours entré par le client
     * @return la présence ou non du cours dans cours.txt
     */
    private boolean checkCourse(Course course) {
        ArrayList<Course> Courses = new ArrayList<>();
        String[] content;
        String line;
        try {
            FileReader classes = new FileReader("data/cours.txt");
            BufferedReader reader = new BufferedReader(classes);
            while ((line = reader.readLine()) != null) {
                content = line.split("\t");
                Courses.add(new Course(content[1], content[0], content[2]));
            }
            reader.close();
            for (Course courseI:Courses){
                if (Objects.equals(courseI.getCode(), course.getCode()) &&
                        Objects.equals(courseI.getName(), course.getName()) &&
                        Objects.equals(courseI.getSession(), course.getSession())){
                    return true;
                }
            } return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}