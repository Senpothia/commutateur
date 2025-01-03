/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keypadprogrammer;

import java.awt.Color;
import java.awt.Font;
import java.awt.color.ColorSpace;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager;

/**
 *
 * @author Michel
 */
public class Interface extends javax.swing.JFrame implements Observer {

    private File programmerLocation = null;      // emplacement du repertoire programmateur
    private File nouveauBinaire = null;          // emplacement du repertoire du nouveau binaire à ajouter
    private String filePath = null;              // emplacement du binaire pour programmation
    private String programmerPathParamsProperties = null;        // emplacement du programmateur(repertoire plateforme Microchip)
    private String localisationNouveauBinaire = null;
    private String nomNouveauBinaire = null;
    private String devicesParamsProperties = null;               // devices lus dans params.properties
    private String nouveauDevice = null;         // device créé et ajouté à la liste
    private String deviceEnTest = null;          // microcontroleur à programmer sur le produits sélectionné
    private String hexLocationsParamsProperties = null;          // liste lues dans params.properties
    private String binaireLocation = null;
    private String nombreVoiesCommutateurParamsProperties = null;  // nombre de voies du commutateur lues dans params.properties
    private String programmerParamsProperties = null;

    private int limCommutateur = 0;                 // nombre de voies du commutateur converties en int depuis la variable nombreVoiesCommutateurString
    private int intNombreDeVoiesNouvelleCarte = 0;
    private int intNombreDeVoiesCarteEnTest = 0;

    private String nombreDeVoiesEnregistresParamsProperties = null;  // lues dans params.properties
    private String nombreDeVoiesCarteEnTest = null;
    private String nombreDeVoiesNouvelleCarte = null;

    private boolean envVariable = false;
    private String produitAprogrammer = null;   // produit sélectionné pour programmation via l'interface
    private String listeProduitsConnusParamsProperties = null;
    private boolean confirmationParams = false;

    private ArrayList<String> listesProduits = new ArrayList<String>();
    private ArrayList<String> listesVoies = new ArrayList<String>();

    private ArrayList<String> ListeBinairesEnregistres = new ArrayList<String>();
    private ArrayList<String> listeDevicesEnregistres = new ArrayList<String>();
    private int selectedProduct = 0;

    Connecteur connecteur = getConnecteur();            // gére la connexion RS232

    private int baudeRate = 9600;
    private int numDatabits = 8;
    private int parity = 0;
    private int stopBits = 1;
    private int newReadTimeout = 1000;
    private int newWriteTimeout = 0;

    private boolean connexionRS232Active = false;               // état de la connexion RS-232

    private boolean testActif = false;
    private boolean programmationActive = false;
    private boolean auto = true;
    private boolean AttenteReponseOperateur = false;

    public static Initializer initializer = new Initializer();  // Charge les propriétés du fichier properties contenant les paramètres de programmation
    public static Initialisation initialisation;                // Centralise les données rapportées par l'initializer

    private List<String> listePortString = new ArrayList<>();
    private List<JRadioButtonMenuItem> listePorts = new ArrayList<JRadioButtonMenuItem>();

    private ProgController progController = new ProgController();

    /**
     * Creates new form Interface
     */
    public Interface() throws IOException {

        initComponents();
        statutRs232.setBackground(Color.RED);
        statutRs232.setForeground(Color.RED);
        statutRs232.setOpaque(true);

        statutPGRM.setBackground(Color.RED);
        statutPGRM.setForeground(Color.RED);
        statutPGRM.setOpaque(true);

        this.getContentPane().setBackground(new Color(50, 131, 168));

        console.setBackground(new Color(247, 242, 208));
        console.setOpaque(true);
        console.setForeground(Color.red);
        console.setFont(new Font("Serif", Font.BOLD, 20));

        nomProduit.setBackground(new Color(247, 242, 208));
        nomProduit.setOpaque(true);
        nomProduit.setForeground(Color.red);
        nomProduit.setFont(new Font("Serif", Font.BOLD, 20));

        emplacementBinaire.setBackground(new Color(247, 242, 208));
        emplacementBinaire.setOpaque(true);
        emplacementBinaire.setForeground(Color.red);
        emplacementBinaire.setFont(new Font("Serif", Font.BOLD, 20));

        nombreVoies.setBackground(new Color(247, 242, 208));
        nombreVoies.setOpaque(true);
        nombreVoies.setForeground(Color.red);
        nombreVoies.setFont(new Font("Serif", Font.BOLD, 20));

        hexLocalisation.setBackground(new Color(247, 242, 208));
        hexLocalisation.setOpaque(true);
        hexLocalisation.setForeground(Color.red);
        hexLocalisation.setFont(new Font("Serif", Font.BOLD, 20));

        messageCreation.setBackground(new Color(252, 242, 3));
        messageCreation.setOpaque(true);
        messageCreation.setForeground(Color.red);
        messageCreation.setFont(new Font("Serif", Font.BOLD, 20));

        nomNouvelleCarte.setBackground(new Color(252, 242, 3));
        nomNouvelleCarte.setOpaque(true);
        nomNouvelleCarte.setForeground(Color.red);
        nomNouvelleCarte.setFont(new Font("Serif", Font.BOLD, 20));

        messageBinaireSelectionne.setBackground(new Color(252, 242, 3));
        messageBinaireSelectionne.setOpaque(true);
        messageBinaireSelectionne.setForeground(Color.red);
        messageBinaireSelectionne.setFont(new Font("Serif", Font.BOLD, 20));

        nombreVoiesNouvelleCarte.setBackground(new Color(252, 242, 3));
        nombreVoiesNouvelleCarte.setOpaque(true);
        nombreVoiesNouvelleCarte.setForeground(Color.red);
        nombreVoiesNouvelleCarte.setFont(new Font("Serif", Font.BOLD, 20));

        nouveauMicroController.setBackground(new Color(252, 242, 3));
        nouveauMicroController.setOpaque(true);
        nouveauMicroController.setForeground(Color.red);
        nouveauMicroController.setFont(new Font("Serif", Font.BOLD, 20));

        paramsWin.getContentPane().setBackground(new Color(131, 50, 168));
        paramsWin.setSize(1300, 600);

        messageCreation.setBackground(new Color(247, 242, 208));

        progLocLabel.setBackground(new Color(247, 242, 208));
        hexLocalisation.setOpaque(true);

        messageCreation.setOpaque(true);
        progLocLabel.setOpaque(true);
        inhibBtn();

        aide.getContentPane().setBackground(new Color(247, 242, 208));

        rechercherPortsComms();
        initialisationParams();

        // Création repertoire de logs
        int dirCreation = progController.createLogFolder(Constants.LOG_DIRECTORY);
        if (dirCreation != 1) {

            montrerError("Echec à la création du repertoire de logs", "Erreur d'initialisation");
            System.exit(0);

        }

        progBarre.setStringPainted(true);
        progBarre.setForeground(Color.blue);
        progBarre.setOpaque(true);
        progBarre.setVisible(true);

        pcbL01C01.setVisible(false);
        pcbL01C02.setVisible(false);
        pcbL01C03.setVisible(false);
        pcbL01C04.setVisible(false);
        pcbL01C05.setVisible(false);
        pcbL01C06.setVisible(false);
        pcbL01C07.setVisible(false);
        pcbL01C08.setVisible(false);
        pcbL01C09.setVisible(false);
        pcbL01C10.setVisible(false);
        pcbL01C11.setVisible(false);
        pcbL01C12.setVisible(false);

        pcbL02C01.setVisible(false);
        pcbL02C02.setVisible(false);
        pcbL02C03.setVisible(false);
        pcbL02C04.setVisible(false);
        pcbL02C05.setVisible(false);
        pcbL02C06.setVisible(false);
        pcbL02C07.setVisible(false);
        pcbL02C08.setVisible(false);
        pcbL02C09.setVisible(false);
        pcbL02C10.setVisible(false);
        pcbL02C11.setVisible(false);
        pcbL02C12.setVisible(false);

        pcbL03C01.setVisible(false);
        pcbL03C02.setVisible(false);
        pcbL03C03.setVisible(false);
        pcbL03C04.setVisible(false);
        pcbL03C05.setVisible(false);
        pcbL03C06.setVisible(false);
        pcbL03C07.setVisible(false);
        pcbL03C08.setVisible(false);
        pcbL03C09.setVisible(false);
        pcbL03C10.setVisible(false);
        pcbL03C11.setVisible(false);
        pcbL03C12.setVisible(false);

        pcbL04C01.setVisible(false);
        pcbL04C02.setVisible(false);
        pcbL04C03.setVisible(false);
        pcbL04C04.setVisible(false);
        pcbL04C05.setVisible(false);
        pcbL04C06.setVisible(false);
        pcbL04C07.setVisible(false);
        pcbL04C08.setVisible(false);
        pcbL04C09.setVisible(false);
        pcbL04C10.setVisible(false);
        pcbL04C11.setVisible(false);
        pcbL04C12.setVisible(false);

        pcbL05C01.setVisible(false);
        pcbL05C02.setVisible(false);
        pcbL05C03.setVisible(false);
        pcbL05C04.setVisible(false);
        pcbL05C05.setVisible(false);
        pcbL05C06.setVisible(false);
        pcbL05C07.setVisible(false);
        pcbL05C08.setVisible(false);
        pcbL05C09.setVisible(false);
        pcbL05C10.setVisible(false);
        pcbL05C11.setVisible(false);
        pcbL05C12.setVisible(false);

        pcbL06C01.setVisible(false);
        pcbL06C02.setVisible(false);
        pcbL06C03.setVisible(false);
        pcbL06C04.setVisible(false);
        pcbL06C05.setVisible(false);
        pcbL06C06.setVisible(false);
        pcbL06C07.setVisible(false);
        pcbL06C08.setVisible(false);
        pcbL06C09.setVisible(false);
        pcbL06C10.setVisible(false);
        pcbL06C11.setVisible(false);
        pcbL06C12.setVisible(false);

        pcbL07C01.setVisible(false);
        pcbL07C02.setVisible(false);
        pcbL07C03.setVisible(false);
        pcbL07C04.setVisible(false);
        pcbL07C05.setVisible(false);
        pcbL07C06.setVisible(false);
        pcbL07C07.setVisible(false);
        pcbL07C08.setVisible(false);
        pcbL07C09.setVisible(false);
        pcbL07C10.setVisible(false);
        pcbL07C11.setVisible(false);
        pcbL07C12.setVisible(false);

        pcbL08C01.setVisible(false);
        pcbL08C02.setVisible(false);
        pcbL08C03.setVisible(false);
        pcbL08C04.setVisible(false);
        pcbL08C05.setVisible(false);
        pcbL08C06.setVisible(false);
        pcbL08C07.setVisible(false);
        pcbL08C08.setVisible(false);
        pcbL08C09.setVisible(false);
        pcbL08C10.setVisible(false);
        pcbL08C11.setVisible(false);
        pcbL08C12.setVisible(false);

        pcbL09C01.setVisible(false);
        pcbL09C02.setVisible(false);
        pcbL09C03.setVisible(false);
        pcbL09C04.setVisible(false);
        pcbL09C05.setVisible(false);
        pcbL09C06.setVisible(false);
        pcbL09C07.setVisible(false);
        pcbL09C08.setVisible(false);
        pcbL09C09.setVisible(false);
        pcbL09C10.setVisible(false);
        pcbL09C11.setVisible(false);
        pcbL09C12.setVisible(false);

        pcbL10C01.setVisible(false);
        pcbL10C02.setVisible(false);
        pcbL10C03.setVisible(false);
        pcbL10C04.setVisible(false);
        pcbL10C05.setVisible(false);
        pcbL10C06.setVisible(false);
        pcbL10C07.setVisible(false);
        pcbL10C08.setVisible(false);
        pcbL10C09.setVisible(false);
        pcbL10C10.setVisible(false);
        pcbL10C11.setVisible(false);
        pcbL10C12.setVisible(false);

        pcbL11C01.setVisible(false);
        pcbL11C02.setVisible(false);
        pcbL11C03.setVisible(false);
        pcbL11C04.setVisible(false);
        pcbL11C05.setVisible(false);
        pcbL11C06.setVisible(false);
        pcbL11C07.setVisible(false);
        pcbL11C08.setVisible(false);
        pcbL11C09.setVisible(false);
        pcbL11C10.setVisible(false);
        pcbL11C11.setVisible(false);
        pcbL11C12.setVisible(false);

        pcbL12C01.setVisible(false);
        pcbL12C02.setVisible(false);
        pcbL12C03.setVisible(false);
        pcbL12C04.setVisible(false);
        pcbL12C05.setVisible(false);
        pcbL12C06.setVisible(false);
        pcbL12C07.setVisible(false);
        pcbL12C08.setVisible(false);
        pcbL12C09.setVisible(false);
        pcbL12C10.setVisible(false);
        pcbL12C11.setVisible(false);
        pcbL12C12.setVisible(false);

        testParamsProg();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        programmerLoc = new javax.swing.JFileChooser();
        btnSelectLocationProg = new javax.swing.JButton();
        btnSelectBinaryLoc = new javax.swing.JButton();
        binaryLoc = new javax.swing.JFileChooser();
        groupPorts = new javax.swing.ButtonGroup();
        groupBits = new javax.swing.ButtonGroup();
        groupBaud = new javax.swing.ButtonGroup();
        groupParity = new javax.swing.ButtonGroup();
        groupStop = new javax.swing.ButtonGroup();
        paramsWin = new javax.swing.JFrame();
        titreParamsWin = new javax.swing.JLabel();
        progLocLabel = new javax.swing.JLabel();
        btnFermerParams = new javax.swing.JButton();
        titreLabProg = new javax.swing.JLabel();
        EnvVarBox = new javax.swing.JCheckBox();
        labelAjoutCarte = new javax.swing.JLabel();
        comboListeProduits = new javax.swing.JComboBox<>();
        listeProduits = new javax.swing.JLabel();
        btnAjouter = new javax.swing.JButton();
        hexLocalisation = new javax.swing.JLabel();
        nomNouvelleCarte = new javax.swing.JTextField();
        LabfichierBinaire = new javax.swing.JLabel();
        btnSelectionBinaireAjouter = new javax.swing.JButton();
        btnEnregistrer = new javax.swing.JButton();
        messageBinaireSelectionne = new javax.swing.JTextField();
        labelBinaireSelectionne = new javax.swing.JLabel();
        LabNombreVoies = new javax.swing.JLabel();
        nombreVoies = new javax.swing.JLabel();
        LabelNbreDeVoiesNouvelleCarte = new javax.swing.JLabel();
        nombreVoiesNouvelleCarte = new javax.swing.JTextField();
        messageCreation = new javax.swing.JLabel();
        LabelmicroController = new javax.swing.JLabel();
        nouveauMicroController = new javax.swing.JTextField();
        aide = new javax.swing.JFrame();
        btnFermerAide = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        titre = new javax.swing.JLabel();
        btnProg = new javax.swing.JButton();
        btnEffacer = new javax.swing.JButton();
        statutRs232 = new javax.swing.JLabel();
        console = new javax.swing.JLabel();
        version = new javax.swing.JLabel();
        StatutRS232Lab = new javax.swing.JLabel();
        statutPRGLabel = new javax.swing.JLabel();
        statutPGRM = new javax.swing.JLabel();
        progBarre = new javax.swing.JProgressBar();
        btnACQ = new javax.swing.JButton();
        nomProduit = new javax.swing.JLabel();
        emplacementBinaire = new javax.swing.JLabel();
        pcbL01C01 = new javax.swing.JLabel();
        pcbL01C02 = new javax.swing.JLabel();
        pcbL01C03 = new javax.swing.JLabel();
        pcbL01C04 = new javax.swing.JLabel();
        pcbL01C05 = new javax.swing.JLabel();
        pcbL01C06 = new javax.swing.JLabel();
        pcbL01C07 = new javax.swing.JLabel();
        pcbL01C08 = new javax.swing.JLabel();
        pcbL01C09 = new javax.swing.JLabel();
        pcbL01C10 = new javax.swing.JLabel();
        pcbL01C11 = new javax.swing.JLabel();
        pcbL01C12 = new javax.swing.JLabel();
        pcbL02C10 = new javax.swing.JLabel();
        pcbL02C11 = new javax.swing.JLabel();
        pcbL02C12 = new javax.swing.JLabel();
        pcbL02C01 = new javax.swing.JLabel();
        pcbL02C02 = new javax.swing.JLabel();
        pcbL02C03 = new javax.swing.JLabel();
        pcbL02C04 = new javax.swing.JLabel();
        pcbL02C05 = new javax.swing.JLabel();
        pcbL02C06 = new javax.swing.JLabel();
        pcbL02C07 = new javax.swing.JLabel();
        pcbL02C08 = new javax.swing.JLabel();
        pcbL02C09 = new javax.swing.JLabel();
        pcbL03C10 = new javax.swing.JLabel();
        pcbL03C11 = new javax.swing.JLabel();
        pcbL03C12 = new javax.swing.JLabel();
        pcbL03C01 = new javax.swing.JLabel();
        pcbL03C02 = new javax.swing.JLabel();
        pcbL03C03 = new javax.swing.JLabel();
        pcbL03C04 = new javax.swing.JLabel();
        pcbL03C05 = new javax.swing.JLabel();
        pcbL03C06 = new javax.swing.JLabel();
        pcbL03C07 = new javax.swing.JLabel();
        pcbL03C08 = new javax.swing.JLabel();
        pcbL03C09 = new javax.swing.JLabel();
        pcbL04C10 = new javax.swing.JLabel();
        pcbL04C11 = new javax.swing.JLabel();
        pcbL04C12 = new javax.swing.JLabel();
        pcbL04C01 = new javax.swing.JLabel();
        pcbL04C02 = new javax.swing.JLabel();
        pcbL04C03 = new javax.swing.JLabel();
        pcbL04C04 = new javax.swing.JLabel();
        pcbL04C05 = new javax.swing.JLabel();
        pcbL04C06 = new javax.swing.JLabel();
        pcbL04C07 = new javax.swing.JLabel();
        pcbL04C08 = new javax.swing.JLabel();
        pcbL04C09 = new javax.swing.JLabel();
        pcbL05C10 = new javax.swing.JLabel();
        pcbL05C11 = new javax.swing.JLabel();
        pcbL05C12 = new javax.swing.JLabel();
        pcbL06C10 = new javax.swing.JLabel();
        pcbL06C11 = new javax.swing.JLabel();
        pcbL06C12 = new javax.swing.JLabel();
        pcbL06C01 = new javax.swing.JLabel();
        pcbL06C02 = new javax.swing.JLabel();
        pcbL06C03 = new javax.swing.JLabel();
        pcbL06C04 = new javax.swing.JLabel();
        pcbL05C01 = new javax.swing.JLabel();
        pcbL05C02 = new javax.swing.JLabel();
        pcbL05C03 = new javax.swing.JLabel();
        pcbL05C04 = new javax.swing.JLabel();
        pcbL05C05 = new javax.swing.JLabel();
        pcbL05C06 = new javax.swing.JLabel();
        pcbL05C07 = new javax.swing.JLabel();
        pcbL06C05 = new javax.swing.JLabel();
        pcbL06C06 = new javax.swing.JLabel();
        pcbL06C07 = new javax.swing.JLabel();
        pcbL06C08 = new javax.swing.JLabel();
        pcbL06C09 = new javax.swing.JLabel();
        pcbL07C10 = new javax.swing.JLabel();
        pcbL07C11 = new javax.swing.JLabel();
        pcbL07C12 = new javax.swing.JLabel();
        pcbL07C01 = new javax.swing.JLabel();
        pcbL07C02 = new javax.swing.JLabel();
        pcbL07C03 = new javax.swing.JLabel();
        pcbL07C04 = new javax.swing.JLabel();
        pcbL07C05 = new javax.swing.JLabel();
        pcbL07C06 = new javax.swing.JLabel();
        pcbL07C07 = new javax.swing.JLabel();
        pcbL07C08 = new javax.swing.JLabel();
        pcbL07C09 = new javax.swing.JLabel();
        pcbL08C10 = new javax.swing.JLabel();
        pcbL08C11 = new javax.swing.JLabel();
        pcbL08C12 = new javax.swing.JLabel();
        pcbL08C01 = new javax.swing.JLabel();
        pcbL08C02 = new javax.swing.JLabel();
        pcbL08C03 = new javax.swing.JLabel();
        pcbL08C04 = new javax.swing.JLabel();
        pcbL05C08 = new javax.swing.JLabel();
        pcbL08C05 = new javax.swing.JLabel();
        pcbL08C06 = new javax.swing.JLabel();
        pcbL08C07 = new javax.swing.JLabel();
        pcbL08C08 = new javax.swing.JLabel();
        pcbL08C09 = new javax.swing.JLabel();
        pcbL05C09 = new javax.swing.JLabel();
        pcbL09C10 = new javax.swing.JLabel();
        pcbL09C11 = new javax.swing.JLabel();
        pcbL09C12 = new javax.swing.JLabel();
        pcbL10C10 = new javax.swing.JLabel();
        pcbL10C11 = new javax.swing.JLabel();
        pcbL10C12 = new javax.swing.JLabel();
        pcbL10C01 = new javax.swing.JLabel();
        pcbL10C02 = new javax.swing.JLabel();
        pcbL10C03 = new javax.swing.JLabel();
        pcbL10C04 = new javax.swing.JLabel();
        pcbL09C01 = new javax.swing.JLabel();
        pcbL09C02 = new javax.swing.JLabel();
        pcbL09C03 = new javax.swing.JLabel();
        pcbL09C04 = new javax.swing.JLabel();
        pcbL09C05 = new javax.swing.JLabel();
        pcbL09C06 = new javax.swing.JLabel();
        pcbL09C07 = new javax.swing.JLabel();
        pcbL10C05 = new javax.swing.JLabel();
        pcbL10C06 = new javax.swing.JLabel();
        pcbL10C07 = new javax.swing.JLabel();
        pcbL10C08 = new javax.swing.JLabel();
        pcbL10C09 = new javax.swing.JLabel();
        pcbL11C10 = new javax.swing.JLabel();
        pcbL11C11 = new javax.swing.JLabel();
        pcbL11C12 = new javax.swing.JLabel();
        pcbL11C01 = new javax.swing.JLabel();
        pcbL11C02 = new javax.swing.JLabel();
        pcbL11C03 = new javax.swing.JLabel();
        pcbL11C04 = new javax.swing.JLabel();
        pcbL11C05 = new javax.swing.JLabel();
        pcbL11C06 = new javax.swing.JLabel();
        pcbL11C07 = new javax.swing.JLabel();
        pcbL11C08 = new javax.swing.JLabel();
        pcbL11C09 = new javax.swing.JLabel();
        pcbL12C10 = new javax.swing.JLabel();
        pcbL12C11 = new javax.swing.JLabel();
        pcbL12C12 = new javax.swing.JLabel();
        pcbL12C01 = new javax.swing.JLabel();
        pcbL12C02 = new javax.swing.JLabel();
        pcbL12C03 = new javax.swing.JLabel();
        pcbL12C04 = new javax.swing.JLabel();
        pcbL09C08 = new javax.swing.JLabel();
        pcbL12C05 = new javax.swing.JLabel();
        pcbL12C06 = new javax.swing.JLabel();
        pcbL12C07 = new javax.swing.JLabel();
        pcbL12C08 = new javax.swing.JLabel();
        pcbL12C09 = new javax.swing.JLabel();
        pcbL09C09 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuParametres = new javax.swing.JMenu();
        menuVoir = new javax.swing.JMenuItem();
        paramsProg = new javax.swing.JMenuItem();
        menuCreer = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        btnFermer = new javax.swing.JMenuItem();
        menuConnexion = new javax.swing.JMenu();
        menuPort = new javax.swing.JMenu();
        menuBaud = new javax.swing.JMenu();
        baud9600 = new javax.swing.JRadioButtonMenuItem();
        baud19200 = new javax.swing.JRadioButtonMenuItem();
        baud38400 = new javax.swing.JRadioButtonMenuItem();
        baud115200 = new javax.swing.JRadioButtonMenuItem();
        menuBits = new javax.swing.JMenu();
        bits6 = new javax.swing.JRadioButtonMenuItem();
        bits7 = new javax.swing.JRadioButtonMenuItem();
        bits8 = new javax.swing.JRadioButtonMenuItem();
        bits9 = new javax.swing.JRadioButtonMenuItem();
        menuStop = new javax.swing.JMenu();
        stop1 = new javax.swing.JRadioButtonMenuItem();
        stop2 = new javax.swing.JRadioButtonMenuItem();
        menuParity = new javax.swing.JMenu();
        parityNone = new javax.swing.JRadioButtonMenuItem();
        parityOdd = new javax.swing.JRadioButtonMenuItem();
        parityEven = new javax.swing.JRadioButtonMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        btnConnexion = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        btnDeconnexion = new javax.swing.JMenuItem();
        menuAide = new javax.swing.JMenu();
        voirAide = new javax.swing.JMenuItem();

        programmerLoc.setFileFilter(null);
        programmerLoc.setFileSelectionMode(javax.swing.JFileChooser.FILES_AND_DIRECTORIES);

        btnSelectLocationProg.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnSelectLocationProg.setText("Programmer location");
        btnSelectLocationProg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectLocationProgActionPerformed(evt);
            }
        });

        btnSelectBinaryLoc.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnSelectBinaryLoc.setText("Binary location");
        btnSelectBinaryLoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectBinaryLocActionPerformed(evt);
            }
        });

        paramsWin.setTitle("Programmateur keypad - Paramètres système");

        titreParamsWin.setBackground(new java.awt.Color(153, 153, 255));
        titreParamsWin.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        titreParamsWin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titreParamsWin.setText("PARAMETRES SYSTEMES");
        titreParamsWin.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        progLocLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        progLocLabel.setForeground(new java.awt.Color(255, 0, 0));
        progLocLabel.setText("jLabel2");
        progLocLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnFermerParams.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        btnFermerParams.setForeground(new java.awt.Color(255, 0, 51));
        btnFermerParams.setText("Fermer");
        btnFermerParams.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFermerParamsActionPerformed(evt);
            }
        });

        titreLabProg.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        titreLabProg.setText("Répertoire programmateur");

        EnvVarBox.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        EnvVarBox.setText("Variable d'environnement");
        EnvVarBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                EnvVarBoxStateChanged(evt);
            }
        });
        EnvVarBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EnvVarBoxActionPerformed(evt);
            }
        });

        labelAjoutCarte.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        labelAjoutCarte.setText("Indiquez le nom de la nouvelle carte");

        comboListeProduits.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        comboListeProduits.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboListeProduitsItemStateChanged(evt);
            }
        });
        comboListeProduits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboListeProduitsActionPerformed(evt);
            }
        });

        listeProduits.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        listeProduits.setText("Sélection carte");

        btnAjouter.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnAjouter.setForeground(new java.awt.Color(255, 0, 0));
        btnAjouter.setText("Ajouter");
        btnAjouter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAjouterActionPerformed(evt);
            }
        });

        hexLocalisation.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        hexLocalisation.setForeground(new java.awt.Color(255, 0, 51));
        hexLocalisation.setText("Jlabel");
        hexLocalisation.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        nomNouvelleCarte.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nomNouvelleCarte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nomNouvelleCarteActionPerformed(evt);
            }
        });

        LabfichierBinaire.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        LabfichierBinaire.setText("Fichier binaire");

        btnSelectionBinaireAjouter.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnSelectionBinaireAjouter.setForeground(new java.awt.Color(255, 0, 0));
        btnSelectionBinaireAjouter.setText("Sélectionner le binaire");
        btnSelectionBinaireAjouter.setActionCommand("");
        btnSelectionBinaireAjouter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectionBinaireAjouterActionPerformed(evt);
            }
        });

        btnEnregistrer.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        btnEnregistrer.setForeground(new java.awt.Color(255, 0, 0));
        btnEnregistrer.setText("Enregistrer");
        btnEnregistrer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnregistrerActionPerformed(evt);
            }
        });

        messageBinaireSelectionne.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        messageBinaireSelectionne.setForeground(new java.awt.Color(255, 51, 0));
        messageBinaireSelectionne.setText("A définir");
        messageBinaireSelectionne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                messageBinaireSelectionneActionPerformed(evt);
            }
        });

        labelBinaireSelectionne.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        labelBinaireSelectionne.setText("Binaire sélectionné");

        LabNombreVoies.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        LabNombreVoies.setText("Nombre de voies");

        nombreVoies.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nombreVoies.setForeground(new java.awt.Color(255, 0, 51));
        nombreVoies.setText("Jlabel");
        nombreVoies.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        LabelNbreDeVoiesNouvelleCarte.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        LabelNbreDeVoiesNouvelleCarte.setText("Nombre de voies");

        nombreVoiesNouvelleCarte.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nombreVoiesNouvelleCarte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nombreVoiesNouvelleCarteActionPerformed(evt);
            }
        });

        messageCreation.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        messageCreation.setForeground(new java.awt.Color(255, 0, 51));
        messageCreation.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        messageCreation.setText("Veuillez compléter le formulaire ci-dessous");
        messageCreation.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        LabelmicroController.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        LabelmicroController.setText("Microcontrôleur");

        nouveauMicroController.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nouveauMicroController.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nouveauMicroControllerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout paramsWinLayout = new javax.swing.GroupLayout(paramsWin.getContentPane());
        paramsWin.getContentPane().setLayout(paramsWinLayout);
        paramsWinLayout.setHorizontalGroup(
            paramsWinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paramsWinLayout.createSequentialGroup()
                .addGroup(paramsWinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(paramsWinLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(paramsWinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(messageCreation, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(paramsWinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(paramsWinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(nombreVoies, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(progLocLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, paramsWinLayout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addGroup(paramsWinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(EnvVarBox)
                                            .addComponent(titreLabProg, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 856, Short.MAX_VALUE)
                                        .addComponent(btnFermerParams))
                                    .addComponent(btnAjouter, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LabfichierBinaire, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelBinaireSelectionne, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(hexLocalisation, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(paramsWinLayout.createSequentialGroup()
                                    .addGap(482, 482, 482)
                                    .addComponent(titreParamsWin, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(listeProduits, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(LabNombreVoies, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(LabelNbreDeVoiesNouvelleCarte, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(paramsWinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(nombreVoiesNouvelleCarte, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(messageBinaireSelectionne, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1247, Short.MAX_VALUE)
                                    .addComponent(nouveauMicroController))
                                .addComponent(labelAjoutCarte, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(paramsWinLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(comboListeProduits, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(paramsWinLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(LabelmicroController, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(paramsWinLayout.createSequentialGroup()
                        .addGap(466, 466, 466)
                        .addComponent(btnSelectionBinaireAjouter, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(paramsWinLayout.createSequentialGroup()
                        .addGap(470, 470, 470)
                        .addComponent(btnEnregistrer, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(paramsWinLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(nomNouvelleCarte)))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        paramsWinLayout.setVerticalGroup(
            paramsWinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paramsWinLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(titreParamsWin)
                .addGap(18, 18, 18)
                .addGroup(paramsWinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titreLabProg)
                    .addComponent(btnFermerParams))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(EnvVarBox)
                .addGap(18, 18, 18)
                .addComponent(progLocLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(listeProduits)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboListeProduits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(LabfichierBinaire)
                .addGap(18, 18, 18)
                .addComponent(hexLocalisation, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(LabNombreVoies)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nombreVoies, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnAjouter)
                .addGap(27, 27, 27)
                .addComponent(messageCreation)
                .addGap(18, 18, 18)
                .addComponent(labelAjoutCarte)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nomNouvelleCarte, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelNbreDeVoiesNouvelleCarte)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nombreVoiesNouvelleCarte, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LabelmicroController)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nouveauMicroController, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSelectionBinaireAjouter)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelBinaireSelectionne)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(messageBinaireSelectionne, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(btnEnregistrer)
                .addGap(103, 103, 103))
        );

        aide.setTitle("Programmateur keypad - Aide");
        aide.setMinimumSize(new java.awt.Dimension(900, 900));

        btnFermerAide.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnFermerAide.setForeground(new java.awt.Color(255, 0, 0));
        btnFermerAide.setText("Fermer");
        btnFermerAide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFermerAideActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("AIDE DU PROGRAMMATEUR GALEO");
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("1- PARAMETRES");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("2- FONCTIONNALITES DE PROGRAMMATION");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("4- TESTS FONCTIONNELS");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("3- MATERIEL ET CONNEXION");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("Pour que le système puisse fonctionner, il est nécessaire que:\n\n- le programmateur STM32CubeProgrammer soit installer sur votre poste de travail. Il est \ntéléchargeable à partir du site de ST Microelectronics. \n- que le version 8 de Java soit installer sur votre poste\n\nEnsuite, il est nécessaire de spécifier le repertoire d'installation du programmateur. Vous devrez identifier le repertoire bin dans \nce répertoire d'installation. Pour plus de confort, il est recommandé dedéfinir cette emplacement des les variables d'environnement de \nvotre système.\n\nPour finir, il vous sera aussi nécessaire de préciser l'emplacement de votre fichier binaire (extension .hex).\n\nCes paramètres sont indispenables au foncionnement du programamteur GALEO. Il est possible de les définir à partir du menu \nparamètres de l'application.\n\n");
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout aideLayout = new javax.swing.GroupLayout(aide.getContentPane());
        aide.getContentPane().setLayout(aideLayout);
        aideLayout.setHorizontalGroup(
            aideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, aideLayout.createSequentialGroup()
                .addContainerGap(1118, Short.MAX_VALUE)
                .addComponent(btnFermerAide)
                .addGap(22, 22, 22))
            .addGroup(aideLayout.createSequentialGroup()
                .addGap(310, 310, 310)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(aideLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(aideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1131, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        aideLayout.setVerticalGroup(
            aideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, aideLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(30, 30, 30)
                .addComponent(jLabel5)
                .addGap(71, 71, 71)
                .addComponent(jLabel4)
                .addGap(45, 45, 45)
                .addComponent(btnFermerAide)
                .addGap(25, 25, 25))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Programmateur keypad");
        setPreferredSize(new java.awt.Dimension(1135, 1800));

        titre.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        titre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titre.setText("PROGRAMMATEUR MICROCHIP");
        titre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnProg.setBackground(new java.awt.Color(255, 255, 255));
        btnProg.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnProg.setForeground(new java.awt.Color(51, 153, 0));
        btnProg.setText("PROGRAMMER");
        btnProg.setBorderPainted(false);
        btnProg.setContentAreaFilled(false);
        btnProg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProgActionPerformed(evt);
            }
        });

        btnEffacer.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnEffacer.setForeground(new java.awt.Color(255, 51, 51));
        btnEffacer.setText("EFFACER");
        btnEffacer.setBorderPainted(false);
        btnEffacer.setContentAreaFilled(false);
        btnEffacer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEffacerActionPerformed(evt);
            }
        });

        statutRs232.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statutRs232.setText("rs-232");

        console.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        console.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        console.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        console.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        version.setText("V1.0");

        StatutRS232Lab.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        StatutRS232Lab.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        StatutRS232Lab.setText("RS-232");

        statutPRGLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        statutPRGLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statutPRGLabel.setText("PGRM");

        statutPGRM.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statutPGRM.setText("prgm");

        progBarre.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        progBarre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        progBarre.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        progBarre.setOpaque(true);
        progBarre.setStringPainted(true);

        btnACQ.setBackground(new java.awt.Color(255, 255, 255));
        btnACQ.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnACQ.setForeground(new java.awt.Color(51, 0, 255));
        btnACQ.setText("ACQ");
        btnACQ.setBorderPainted(false);
        btnACQ.setContentAreaFilled(false);
        btnACQ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnACQActionPerformed(evt);
            }
        });

        nomProduit.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nomProduit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nomProduit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        nomProduit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        emplacementBinaire.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        emplacementBinaire.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        emplacementBinaire.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        emplacementBinaire.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        pcbL01C01.setText("jLabel6");

        pcbL01C02.setText("jLabel6");

        pcbL01C03.setText("jLabel6");

        pcbL01C04.setText("jLabel6");

        pcbL01C05.setText("jLabel6");

        pcbL01C06.setText("jLabel6");

        pcbL01C07.setText("jLabel6");

        pcbL01C08.setText("jLabel6");

        pcbL01C09.setText("jLabel6");

        pcbL01C10.setText("jLabel6");

        pcbL01C11.setText("jLabel6");

        pcbL01C12.setText("jLabel6");

        pcbL02C10.setText("jLabel6");

        pcbL02C11.setText("jLabel6");

        pcbL02C12.setText("jLabel6");

        pcbL02C01.setText("jLabel6");

        pcbL02C02.setText("jLabel6");

        pcbL02C03.setText("jLabel6");

        pcbL02C04.setText("jLabel6");

        pcbL02C05.setText("jLabel6");

        pcbL02C06.setText("jLabel6");

        pcbL02C07.setText("jLabel6");

        pcbL02C08.setText("jLabel6");

        pcbL02C09.setText("jLabel6");

        pcbL03C10.setText("jLabel6");

        pcbL03C11.setText("jLabel6");

        pcbL03C12.setText("jLabel6");

        pcbL03C01.setText("jLabel6");

        pcbL03C02.setText("jLabel6");

        pcbL03C03.setText("jLabel6");

        pcbL03C04.setText("jLabel6");

        pcbL03C05.setText("jLabel6");

        pcbL03C06.setText("jLabel6");

        pcbL03C07.setText("jLabel6");

        pcbL03C08.setText("jLabel6");

        pcbL03C09.setText("jLabel6");

        pcbL04C10.setText("jLabel6");

        pcbL04C11.setText("jLabel6");

        pcbL04C12.setText("jLabel6");

        pcbL04C01.setText("jLabel6");

        pcbL04C02.setText("jLabel6");

        pcbL04C03.setText("jLabel6");

        pcbL04C04.setText("jLabel6");

        pcbL04C05.setText("jLabel6");

        pcbL04C06.setText("jLabel6");

        pcbL04C07.setText("jLabel6");

        pcbL04C08.setText("jLabel6");

        pcbL04C09.setText("jLabel6");

        pcbL05C10.setText("jLabel6");

        pcbL05C11.setText("jLabel6");

        pcbL05C12.setText("jLabel6");

        pcbL06C10.setText("jLabel6");

        pcbL06C11.setText("jLabel6");

        pcbL06C12.setText("jLabel6");

        pcbL06C01.setText("jLabel6");

        pcbL06C02.setText("jLabel6");

        pcbL06C03.setText("jLabel6");

        pcbL06C04.setText("jLabel6");

        pcbL05C01.setText("jLabel6");

        pcbL05C02.setText("jLabel6");

        pcbL05C03.setText("jLabel6");

        pcbL05C04.setText("jLabel6");

        pcbL05C05.setText("jLabel6");

        pcbL05C06.setText("jLabel6");

        pcbL05C07.setText("jLabel6");

        pcbL06C05.setText("jLabel6");

        pcbL06C06.setText("jLabel6");

        pcbL06C07.setText("jLabel6");

        pcbL06C08.setText("jLabel6");

        pcbL06C09.setText("jLabel6");

        pcbL07C10.setText("jLabel6");

        pcbL07C11.setText("jLabel6");

        pcbL07C12.setText("jLabel6");

        pcbL07C01.setText("jLabel6");

        pcbL07C02.setText("jLabel6");

        pcbL07C03.setText("jLabel6");

        pcbL07C04.setText("jLabel6");

        pcbL07C05.setText("jLabel6");

        pcbL07C06.setText("jLabel6");

        pcbL07C07.setText("jLabel6");

        pcbL07C08.setText("jLabel6");

        pcbL07C09.setText("jLabel6");

        pcbL08C10.setText("jLabel6");

        pcbL08C11.setText("jLabel6");

        pcbL08C12.setText("jLabel6");

        pcbL08C01.setText("jLabel6");

        pcbL08C02.setText("jLabel6");

        pcbL08C03.setText("jLabel6");

        pcbL08C04.setText("jLabel6");

        pcbL05C08.setText("jLabel6");

        pcbL08C05.setText("jLabel6");

        pcbL08C06.setText("jLabel6");

        pcbL08C07.setText("jLabel6");

        pcbL08C08.setText("jLabel6");

        pcbL08C09.setText("jLabel6");

        pcbL05C09.setText("jLabel6");

        pcbL09C10.setText("jLabel6");

        pcbL09C11.setText("jLabel6");

        pcbL09C12.setText("jLabel6");

        pcbL10C10.setText("jLabel6");

        pcbL10C11.setText("jLabel6");

        pcbL10C12.setText("jLabel6");

        pcbL10C01.setText("jLabel6");

        pcbL10C02.setText("jLabel6");

        pcbL10C03.setText("jLabel6");

        pcbL10C04.setText("jLabel6");

        pcbL09C01.setText("jLabel6");

        pcbL09C02.setText("jLabel6");

        pcbL09C03.setText("jLabel6");

        pcbL09C04.setText("jLabel6");

        pcbL09C05.setText("jLabel6");

        pcbL09C06.setText("jLabel6");

        pcbL09C07.setText("jLabel6");

        pcbL10C05.setText("jLabel6");

        pcbL10C06.setText("jLabel6");

        pcbL10C07.setText("jLabel6");

        pcbL10C08.setText("jLabel6");

        pcbL10C09.setText("jLabel6");

        pcbL11C10.setText("jLabel6");

        pcbL11C11.setText("jLabel6");

        pcbL11C12.setText("jLabel6");

        pcbL11C01.setText("jLabel6");

        pcbL11C02.setText("jLabel6");

        pcbL11C03.setText("jLabel6");

        pcbL11C04.setText("jLabel6");

        pcbL11C05.setText("jLabel6");

        pcbL11C06.setText("jLabel6");

        pcbL11C07.setText("jLabel6");

        pcbL11C08.setText("jLabel6");

        pcbL11C09.setText("jLabel6");

        pcbL12C10.setText("jLabel6");

        pcbL12C11.setText("jLabel6");

        pcbL12C12.setText("jLabel6");

        pcbL12C01.setText("jLabel6");

        pcbL12C02.setText("jLabel6");

        pcbL12C03.setText("jLabel6");

        pcbL12C04.setText("jLabel6");

        pcbL09C08.setText("jLabel6");

        pcbL12C05.setText("jLabel6");

        pcbL12C06.setText("jLabel6");

        pcbL12C07.setText("jLabel6");

        pcbL12C08.setText("jLabel6");

        pcbL12C09.setText("jLabel6");

        pcbL09C09.setText("jLabel6");

        menuParametres.setText("Paramètres");
        menuParametres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuParametresActionPerformed(evt);
            }
        });

        menuVoir.setText("Voir");
        menuVoir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuVoirActionPerformed(evt);
            }
        });
        menuParametres.add(menuVoir);

        paramsProg.setText("Programmateur");
        paramsProg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paramsProgActionPerformed(evt);
            }
        });
        menuParametres.add(paramsProg);

        menuCreer.setText("Créer");
        menuCreer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCreerActionPerformed(evt);
            }
        });
        menuParametres.add(menuCreer);
        menuParametres.add(jSeparator1);

        btnFermer.setText("Fermer");
        btnFermer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFermerActionPerformed(evt);
            }
        });
        menuParametres.add(btnFermer);

        jMenuBar1.add(menuParametres);

        menuConnexion.setText("Communication");
        menuConnexion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuConnexionActionPerformed(evt);
            }
        });

        menuPort.setText("Ports");
        menuPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPortActionPerformed(evt);
            }
        });
        menuConnexion.add(menuPort);

        menuBaud.setText("Baud");

        groupBaud.add(baud9600);
        baud9600.setSelected(true);
        baud9600.setText("9600");
        menuBaud.add(baud9600);

        groupBaud.add(baud19200);
        baud19200.setText("19200");
        menuBaud.add(baud19200);

        groupBaud.add(baud38400);
        baud38400.setText("38400");
        menuBaud.add(baud38400);

        groupBaud.add(baud115200);
        baud115200.setText("115200");
        menuBaud.add(baud115200);

        menuConnexion.add(menuBaud);

        menuBits.setText("Bits");

        groupBits.add(bits6);
        bits6.setSelected(true);
        bits6.setText("6");
        menuBits.add(bits6);

        groupBits.add(bits7);
        bits7.setText("7");
        menuBits.add(bits7);

        groupBits.add(bits8);
        bits8.setText("8");
        menuBits.add(bits8);

        groupBits.add(bits9);
        bits9.setText("9");
        menuBits.add(bits9);

        menuConnexion.add(menuBits);

        menuStop.setText("Stop");

        groupStop.add(stop1);
        stop1.setSelected(true);
        stop1.setText("1");
        menuStop.add(stop1);

        groupStop.add(stop2);
        stop2.setText("2");
        menuStop.add(stop2);

        menuConnexion.add(menuStop);

        menuParity.setText("Parity");

        groupParity.add(parityNone);
        parityNone.setSelected(true);
        parityNone.setText("None");
        menuParity.add(parityNone);

        groupParity.add(parityOdd);
        parityOdd.setText("Odd");
        menuParity.add(parityOdd);

        groupParity.add(parityEven);
        parityEven.setText("Even");
        menuParity.add(parityEven);

        menuConnexion.add(menuParity);
        menuConnexion.add(jSeparator2);

        btnConnexion.setText("Connexion");
        btnConnexion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnexionActionPerformed(evt);
            }
        });
        menuConnexion.add(btnConnexion);
        menuConnexion.add(jSeparator3);

        btnDeconnexion.setText("Déconnexion");
        btnDeconnexion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeconnexionActionPerformed(evt);
            }
        });
        menuConnexion.add(btnDeconnexion);

        jMenuBar1.add(menuConnexion);

        menuAide.setText("Autres");
        menuAide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAideActionPerformed(evt);
            }
        });

        voirAide.setText("Aide");
        voirAide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                voirAideActionPerformed(evt);
            }
        });
        menuAide.add(voirAide);

        jMenuBar1.add(menuAide);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(nomProduit, javax.swing.GroupLayout.DEFAULT_SIZE, 893, Short.MAX_VALUE)
                    .addComponent(emplacementBinaire, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 148, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(263, 263, 263)
                        .addComponent(btnEffacer, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(89, 89, 89)
                        .addComponent(btnProg, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(88, 88, 88)
                        .addComponent(btnACQ, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(version)
                        .addGap(16, 16, 16))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(statutPGRM, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(titre, javax.swing.GroupLayout.PREFERRED_SIZE, 531, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(232, 232, 232)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(StatutRS232Lab, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(statutRs232, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(statutPRGLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(36, 36, 36))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(progBarre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(console, javax.swing.GroupLayout.PREFERRED_SIZE, 893, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(232, 232, 232)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(pcbL12C01)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL12C02)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(pcbL12C03)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL12C04)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL12C05)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL12C06)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(pcbL12C07)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL12C08)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL12C09)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL12C10)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(pcbL12C11)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL12C12))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(pcbL09C01)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL09C02)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(pcbL09C03)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL09C04)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL09C05)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL09C06)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(pcbL09C07)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL09C08)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL09C09)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL09C10)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(pcbL09C11)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL09C12))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(pcbL11C01)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL11C02)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(pcbL11C03)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL11C04)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL11C05)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL11C06)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(pcbL11C07)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL11C08)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL11C09)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL11C10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(pcbL11C11)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL11C12))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(pcbL10C01)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL10C02)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL10C03)
                                        .addGap(10, 10, 10)
                                        .addComponent(pcbL10C04)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL10C05)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL10C06)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(pcbL10C07)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL10C08)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL10C09)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL10C10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(pcbL10C11)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL10C12))))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(pcbL01C01)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL01C02)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(pcbL01C03)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL01C04)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL01C05)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL01C06)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(pcbL01C07)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL01C08)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL01C09)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL01C10)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(pcbL01C11)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL01C12))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(pcbL03C01)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL03C02)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(pcbL03C03)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL03C04)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL03C05)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL03C06)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(pcbL03C07)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL03C08)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL03C09)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL03C10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(pcbL03C11)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL03C12))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(pcbL02C01)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL02C02)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(pcbL02C03)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL02C04)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL02C05)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL02C06)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(pcbL02C07)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL02C08)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL02C09)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL02C10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(pcbL02C11)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL02C12)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(pcbL08C01)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL08C02)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(pcbL08C03)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL08C04)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL08C05)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL08C06)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(pcbL08C07)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL08C08)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL08C09)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL08C10)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(pcbL08C11)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL08C12))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(pcbL05C01)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL05C02)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(pcbL05C03)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL05C04)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL05C05)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL05C06)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(pcbL05C07)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL05C08)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL05C09)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL05C10)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(pcbL05C11)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL05C12))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(pcbL07C01)
                                                .addGap(18, 18, 18)
                                                .addComponent(pcbL07C02)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(pcbL07C03)
                                                .addGap(18, 18, 18)
                                                .addComponent(pcbL07C04)
                                                .addGap(18, 18, 18)
                                                .addComponent(pcbL07C05)
                                                .addGap(18, 18, 18)
                                                .addComponent(pcbL07C06)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(pcbL07C07)
                                                .addGap(18, 18, 18)
                                                .addComponent(pcbL07C08)
                                                .addGap(18, 18, 18)
                                                .addComponent(pcbL07C09)
                                                .addGap(18, 18, 18)
                                                .addComponent(pcbL07C10)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(pcbL07C11)
                                                .addGap(18, 18, 18)
                                                .addComponent(pcbL07C12))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(pcbL06C01)
                                                .addGap(18, 18, 18)
                                                .addComponent(pcbL06C02)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(pcbL06C03)
                                                .addGap(18, 18, 18)
                                                .addComponent(pcbL06C04)
                                                .addGap(18, 18, 18)
                                                .addComponent(pcbL06C05)
                                                .addGap(18, 18, 18)
                                                .addComponent(pcbL06C06)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(pcbL06C07)
                                                .addGap(18, 18, 18)
                                                .addComponent(pcbL06C08)
                                                .addGap(18, 18, 18)
                                                .addComponent(pcbL06C09)
                                                .addGap(18, 18, 18)
                                                .addComponent(pcbL06C10)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(pcbL06C11)
                                                .addGap(18, 18, 18)
                                                .addComponent(pcbL06C12))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(pcbL04C01)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL04C02)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(pcbL04C03)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL04C04)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL04C05)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL04C06)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(pcbL04C07)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL04C08)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL04C09)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL04C10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(pcbL04C11)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL04C12)))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(StatutRS232Lab)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(statutRs232, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(titre, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statutPRGLabel)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(statutPGRM, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(nomProduit, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(emplacementBinaire, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pcbL01C01, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL01C02, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL01C03, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL01C04, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL01C05, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL01C06, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL01C07, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL01C08, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL01C09, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL01C10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL01C11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL01C12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pcbL02C01, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL02C02, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL02C03, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL02C04, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL02C05, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL02C06, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL02C07, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL02C08, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL02C09, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL02C10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL02C11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL02C12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pcbL03C01, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL03C02, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL03C03, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL03C04, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL03C05, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL03C06, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL03C07, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL03C08, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL03C09, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL03C10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL03C11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL03C12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pcbL04C01, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL04C02, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL04C03, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL04C04, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL04C05, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL04C06, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL04C07, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL04C08, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL04C09, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL04C10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL04C11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL04C12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pcbL05C01, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL05C02, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL05C03, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL05C04, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL05C05, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL05C06, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL05C07, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL05C08, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL05C09, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL05C10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL05C11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL05C12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pcbL06C01, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL06C02, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL06C03, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL06C04, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL06C05, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL06C06, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL06C07, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL06C08, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL06C09, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL06C10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL06C11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL06C12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pcbL07C01, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL07C02, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL07C03, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL07C04, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL07C05, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL07C06, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL07C07, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL07C08, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL07C09, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL07C10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL07C11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL07C12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pcbL08C01, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL08C02, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL08C03, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL08C04, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL08C05, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL08C06, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL08C07, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL08C08, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL08C09, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL08C10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL08C11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL08C12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pcbL09C01, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL09C02, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL09C03, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL09C04, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL09C05, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL09C06, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL09C07, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL09C08, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL09C09, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL09C10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL09C11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL09C12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pcbL10C01, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL10C02, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL10C03, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL10C04, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL10C05, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL10C06, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL10C07, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL10C08, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL10C09, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL10C10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL10C11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL10C12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pcbL11C01, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL11C02, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL11C03, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL11C04, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL11C05, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL11C06, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL11C07, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL11C08, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL11C09, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL11C10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL11C11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL11C12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pcbL12C01, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL12C02, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL12C03, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL12C04, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL12C05, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL12C06, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL12C07, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL12C08, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL12C09, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL12C10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL12C11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcbL12C12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(progBarre, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(console, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEffacer, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnProg, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnACQ, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(version, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnProgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProgActionPerformed

        if (!confirmationParams) {

            boolean confirmation = confirmeParams();
            if (!confirmation) {

                return;

            } else {

                confirmationParams = true;
            }
        }

        console.setText("Programmation en cours");
        programmationActive = true;
        progBarre.setVisible(true);

        Thread t = new Thread() {
            public void run() {

                try {
                    int comm = connecteur.program(hexLocationsParamsProperties, envVariable, programmerPathParamsProperties, programmerParamsProperties, deviceEnTest, binaireLocation, intNombreDeVoiesCarteEnTest);
                    System.out.println("Retour programmation. Code reçu: " + comm);

                    switch (comm) {

                        case 1:

                            console.setText("Programmation terminée");

                            break;

                        /*
                        case -1:

                            console.setText("La connexion au programmateur a échoué!");
                            break;

                        case -2:

                            console.setText("Cible non trouvée!");
                            break;
                         */
                    }

                } catch (IOException ex) {
                    Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        t.start();


    }//GEN-LAST:event_btnProgActionPerformed

    private void btnEffacerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEffacerActionPerformed

        connecteur.getFileSize(".\\logs\\logs.txt");  // Pour test
        /*
        if (!testActif) {
            System.out.println("fonction Effacement");
            if (!confirmationParams) {

                boolean confirmation = confirmeParams();

                if (!confirmation) {

                    System.out.println("return");
                    return;
                }
            }

            console.setText("Effacement en cours");
            voyant.setBackground(Color.YELLOW);

            Thread t = new Thread() {
                public void run() {

                    connecteur.erase(envVariable, filePath);
                }
            };
            t.start();

        } else {

            int comm = connecteur.envoyerData(Constants.KO);
            if (comm == -1) {

                alerteRS232();

            }
            console.setText("Résultat non conforme. Test terminé!");
            voyant.setBackground(Color.RED);

        }
         */
    }//GEN-LAST:event_btnEffacerActionPerformed

    private void btnSelectLocationProgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectLocationProgActionPerformed


    }//GEN-LAST:event_btnSelectLocationProgActionPerformed

    private void btnSelectBinaryLocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectBinaryLocActionPerformed


    }//GEN-LAST:event_btnSelectBinaryLocActionPerformed

    private void btnFermerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFermerActionPerformed

        System.exit(0);
    }//GEN-LAST:event_btnFermerActionPerformed

    private void paramsProgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paramsProgActionPerformed

        programmerLoc.showOpenDialog(this);
        programmerLocation = programmerLoc.getSelectedFile();
        if (programmerLoc.getSelectedFile() != null) {

            console.setText("Repertoire programmateur: " + programmerLocation.getPath());
            System.out.println("Repertoire programmateur: " + programmerLocation.getPath());
            programmerPathParamsProperties = programmerLocation.getPath();
            initializer.update("programmerDirectory", programmerPathParamsProperties);

        }

        testParamsProg();
    }//GEN-LAST:event_paramsProgActionPerformed

    private void menuCreerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCreerActionPerformed

        paramsWin.setVisible(true);
        EnvVarBox.setSelected(envVariable);
        activerFonctionAjouter(true);
        if (filePath != null) {
            progLocLabel.setText("Repertoire programmateur: " + filePath);
        } else {

            progLocLabel.setText("Repertoire programmateur non défini!");
        }
        messageCreation.setText("Compléter le formulaire ci-dessous pour ajouter un produit à programmer!");

    }//GEN-LAST:event_menuCreerActionPerformed

    private void menuVoirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuVoirActionPerformed

        paramsWin.setVisible(true);
        activerFonctionAjouter(false);
        EnvVarBox.setSelected(envVariable);

        if (comboListeProduits.getSelectedIndex() == 0) {

            hexLocalisation.setText("Aucun produit sélectionné.");

        }

        if (programmerPathParamsProperties != null || !programmerPathParamsProperties.equals("na")) {

            progLocLabel.setText("Repertoire programmateur: " + programmerPathParamsProperties);

        } else {

            progLocLabel.setText("Repertoire programmateur non défini!");
        }

    }//GEN-LAST:event_menuVoirActionPerformed

    private void menuAideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAideActionPerformed


    }//GEN-LAST:event_menuAideActionPerformed

    private void btnFermerAideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFermerAideActionPerformed

        aide.setVisible(false);
    }//GEN-LAST:event_btnFermerAideActionPerformed

    private void voirAideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_voirAideActionPerformed

        aide.setVisible(true);
    }//GEN-LAST:event_voirAideActionPerformed

    private void menuParametresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuParametresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menuParametresActionPerformed

    private void btnConnexionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnexionActionPerformed

        int i = connecteur.makeConnection(Connecteur.portName, baudeRate, numDatabits, parity, stopBits);
        if (i == 99) {

            console.setForeground(Color.BLUE);
            console.setText("Connexion réussie");
            setStatusRS232(true);
            btnConnexion.setEnabled(false);
            btnDeconnexion.setEnabled(true);
            connexionRS232Active = true;
            //activerBtnAttenteLancement();
            //activerBtnTester(true);
            activerBtnProgrammer(true);

        } else {

            console.setForeground(Color.red);
            console.setText("Tentative de connexion échouée");
            setStatusRS232(false);

        }

        setEnabledMenusConfiguration();

    }//GEN-LAST:event_btnConnexionActionPerformed

    private void btnDeconnexionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeconnexionActionPerformed

        int i = connecteur.disconnect();
        if (i == 0) {
            console.setForeground(Color.BLUE);
            console.setText("Déconnexion réussie");
            setStatusRS232(false);
            btnConnexion.setEnabled(true);
            btnDeconnexion.setEnabled(false);
            connexionRS232Active = false;
            //inhibBtn();
            activerBtnProgrammer(false);

        }
    }//GEN-LAST:event_btnDeconnexionActionPerformed

    private void menuConnexionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuConnexionActionPerformed

        rechercherPortsComms();

    }//GEN-LAST:event_menuConnexionActionPerformed

    private void menuPortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPortActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menuPortActionPerformed

    private void btnACQActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnACQActionPerformed

        System.out.println("Acquittement");
        int comm = connecteur.envoyerData(Constants.AQC);

        if (comm == -1) {

            alerteRS232();

        }

        testActif = false;
        console.setText("RESULTAT ACQUITTE - PRET POUR NOUVEAU TEST!");
        testParamsProg();

        progBarre.setValue(0);
        progBarre.setString("En attente lancement de programmation");
        progBarre.setStringPainted(true);


    }//GEN-LAST:event_btnACQActionPerformed

    private void nombreVoiesNouvelleCarteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nombreVoiesNouvelleCarteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nombreVoiesNouvelleCarteActionPerformed

    private void messageBinaireSelectionneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_messageBinaireSelectionneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_messageBinaireSelectionneActionPerformed

    private void btnEnregistrerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnregistrerActionPerformed

        if (nomNouvelleCarte.getText().equals("")) {

            messageCreation.setText("Le nom de la nouvelle carte doit être défini!");
            montrerError("Le nom de la nouvelle carte doit être défini", "Formulaire imcomplet");

            return;
        } else {

            nomNouveauBinaire = nomNouvelleCarte.getText();
        }

        if (localisationNouveauBinaire == null) {

            messageCreation.setText("Indiquer l'accès au bianire");
            montrerError("L'accès au binaire doit être défini", "Formulaire imcomplet");
            return;
        }

        if (nombreVoiesNouvelleCarte.getText().equals("")) {

            montrerError("Le nombre de voies doit être défini!", "Formulaire imcomplet");
            return;
        } else {

            try {

                nombreDeVoiesNouvelleCarte = nombreVoiesNouvelleCarte.getText();
                intNombreDeVoiesNouvelleCarte = Integer.parseInt(nombreDeVoiesCarteEnTest);

            } catch (Exception e) {

                montrerError("Le nombre de voies doit être compris entre 1 et " + limCommutateur, "Formulaire imcomplet");
                return;

            }

        }

        if (nouveauMicroController.getText().equals("")) {

            messageCreation.setText("Le nom du mirocontrôleur doit être défini!");
            montrerError("Le nom du mirocontrôleur doit être défini!", "Formulaire imcomplet");

            return;
        } else {

            nouveauDevice = nouveauMicroController.getText();
        }

        enregistrerNouvelleCarte(localisationNouveauBinaire, nomNouveauBinaire, nombreDeVoiesNouvelleCarte, nouveauDevice);
        montrerError("La nouvelle carte à été enregistrée", "Enregistrement effectué");

        paramsWin.setVisible(false);

    }//GEN-LAST:event_btnEnregistrerActionPerformed

    private void btnSelectionBinaireAjouterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectionBinaireAjouterActionPerformed

        programmerLoc.showOpenDialog(this);
        nouveauBinaire = programmerLoc.getSelectedFile();
        if (programmerLoc.getSelectedFile() != null) {

            console.setText("Repertoire binaire: " + nouveauBinaire.getPath());
            localisationNouveauBinaire = nouveauBinaire.getPath();
            System.out.println("Binaire sélectionné: " + localisationNouveauBinaire);
            messageBinaireSelectionne.setText("Binaire sélectionné: " + localisationNouveauBinaire);

        } else {

            System.out.println("Aucun binaire sélectionné!");
        }

        //testParamsProg();
    }//GEN-LAST:event_btnSelectionBinaireAjouterActionPerformed

    private void nomNouvelleCarteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomNouvelleCarteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nomNouvelleCarteActionPerformed

    private void btnAjouterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAjouterActionPerformed

        activerFonctionAjouter(true);
    }//GEN-LAST:event_btnAjouterActionPerformed

    private void comboListeProduitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboListeProduitsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboListeProduitsActionPerformed

    private void comboListeProduitsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboListeProduitsItemStateChanged
        System.out.println("item selected: " + comboListeProduits.getSelectedIndex());

        if (comboListeProduits.getSelectedIndex() != 0) {

            hexLocalisation.setText(ListeBinairesEnregistres.get(comboListeProduits.getSelectedIndex() - 1));
            nombreVoies.setText(listesVoies.get(comboListeProduits.getSelectedIndex() - 1));
            nombreDeVoiesCarteEnTest = listesVoies.get(comboListeProduits.getSelectedIndex() - 1);
            deviceEnTest = listeDevicesEnregistres.get(comboListeProduits.getSelectedIndex() - 1);

        } else {

            hexLocalisation.setText("Aucun produit sélectionné!");
            nombreVoies.setText("");
            nombreDeVoiesCarteEnTest = null;
            deviceEnTest = null;
            produitAprogrammer = null;
            testParamsProg();
        }
    }//GEN-LAST:event_comboListeProduitsItemStateChanged

    private void EnvVarBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EnvVarBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EnvVarBoxActionPerformed

    private void EnvVarBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_EnvVarBoxStateChanged

        if (EnvVarBox.isSelected()) {

            progLocLabel.setText("Variable d'environnement définie!");

        } else {

            progLocLabel.setText("Vérifier variables d'environnement! JAVA doit être dans le path");

        }
    }//GEN-LAST:event_EnvVarBoxStateChanged

    private void btnFermerParamsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFermerParamsActionPerformed

        paramsWin.setSize(1300, 600);
        paramsWin.setVisible(false);

        if (EnvVarBox.isSelected()) {

            envVariable = true;

        } else {

            envVariable = false;
        }

        selectedProduct = comboListeProduits.getSelectedIndex();

        if (selectedProduct != 0) {

            produitAprogrammer = listesProduits.get(selectedProduct);
            nomProduit.setText(produitAprogrammer + " - Microcontôleur: " + deviceEnTest + " - Nombre de voie: " + nombreDeVoiesCarteEnTest + " - Programmateur: " + programmerParamsProperties);
            binaireLocation = ListeBinairesEnregistres.get(selectedProduct - 1);
            System.out.println("localistaion binaire: " + binaireLocation);
            emplacementBinaire.setText(binaireLocation);
            intNombreDeVoiesCarteEnTest = Integer.parseInt(nombreDeVoiesCarteEnTest);
            System.out.println("nombre de voies carte en test (int): " + intNombreDeVoiesCarteEnTest);
        } else {

            nomProduit.setText("Aucun produit sélectionné!");
            emplacementBinaire.setText("");
            nombreDeVoiesCarteEnTest = null;
            deviceEnTest = null;
            produitAprogrammer = null;

        }

        testParamsProg();
    }//GEN-LAST:event_btnFermerParamsActionPerformed

    private void nouveauMicroControllerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nouveauMicroControllerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nouveauMicroControllerActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Interface().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox EnvVarBox;
    private javax.swing.JLabel LabNombreVoies;
    private javax.swing.JLabel LabelNbreDeVoiesNouvelleCarte;
    private javax.swing.JLabel LabelmicroController;
    private javax.swing.JLabel LabfichierBinaire;
    private javax.swing.JLabel StatutRS232Lab;
    private javax.swing.JFrame aide;
    private javax.swing.JRadioButtonMenuItem baud115200;
    private javax.swing.JRadioButtonMenuItem baud19200;
    private javax.swing.JRadioButtonMenuItem baud38400;
    private javax.swing.JRadioButtonMenuItem baud9600;
    private javax.swing.JFileChooser binaryLoc;
    private javax.swing.JRadioButtonMenuItem bits6;
    private javax.swing.JRadioButtonMenuItem bits7;
    private javax.swing.JRadioButtonMenuItem bits8;
    private javax.swing.JRadioButtonMenuItem bits9;
    private javax.swing.JButton btnACQ;
    private javax.swing.JButton btnAjouter;
    private javax.swing.JMenuItem btnConnexion;
    private javax.swing.JMenuItem btnDeconnexion;
    private javax.swing.JButton btnEffacer;
    private javax.swing.JButton btnEnregistrer;
    private javax.swing.JMenuItem btnFermer;
    private javax.swing.JButton btnFermerAide;
    private javax.swing.JButton btnFermerParams;
    private javax.swing.JButton btnProg;
    private javax.swing.JButton btnSelectBinaryLoc;
    private javax.swing.JButton btnSelectLocationProg;
    private javax.swing.JButton btnSelectionBinaireAjouter;
    private javax.swing.JComboBox<String> comboListeProduits;
    private javax.swing.JLabel console;
    private javax.swing.JLabel emplacementBinaire;
    private javax.swing.ButtonGroup groupBaud;
    private javax.swing.ButtonGroup groupBits;
    private javax.swing.ButtonGroup groupParity;
    private javax.swing.ButtonGroup groupPorts;
    private javax.swing.ButtonGroup groupStop;
    private javax.swing.JLabel hexLocalisation;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel labelAjoutCarte;
    private javax.swing.JLabel labelBinaireSelectionne;
    private javax.swing.JLabel listeProduits;
    private javax.swing.JMenu menuAide;
    private javax.swing.JMenu menuBaud;
    private javax.swing.JMenu menuBits;
    private javax.swing.JMenu menuConnexion;
    private javax.swing.JMenuItem menuCreer;
    private javax.swing.JMenu menuParametres;
    private javax.swing.JMenu menuParity;
    private javax.swing.JMenu menuPort;
    private javax.swing.JMenu menuStop;
    private javax.swing.JMenuItem menuVoir;
    private javax.swing.JTextField messageBinaireSelectionne;
    private javax.swing.JLabel messageCreation;
    private javax.swing.JTextField nomNouvelleCarte;
    private javax.swing.JLabel nomProduit;
    private javax.swing.JLabel nombreVoies;
    private javax.swing.JTextField nombreVoiesNouvelleCarte;
    private javax.swing.JTextField nouveauMicroController;
    private javax.swing.JMenuItem paramsProg;
    private javax.swing.JFrame paramsWin;
    private javax.swing.JRadioButtonMenuItem parityEven;
    private javax.swing.JRadioButtonMenuItem parityNone;
    private javax.swing.JRadioButtonMenuItem parityOdd;
    private javax.swing.JLabel pcbL01C01;
    private javax.swing.JLabel pcbL01C02;
    private javax.swing.JLabel pcbL01C03;
    private javax.swing.JLabel pcbL01C04;
    private javax.swing.JLabel pcbL01C05;
    private javax.swing.JLabel pcbL01C06;
    private javax.swing.JLabel pcbL01C07;
    private javax.swing.JLabel pcbL01C08;
    private javax.swing.JLabel pcbL01C09;
    private javax.swing.JLabel pcbL01C10;
    private javax.swing.JLabel pcbL01C11;
    private javax.swing.JLabel pcbL01C12;
    private javax.swing.JLabel pcbL02C01;
    private javax.swing.JLabel pcbL02C02;
    private javax.swing.JLabel pcbL02C03;
    private javax.swing.JLabel pcbL02C04;
    private javax.swing.JLabel pcbL02C05;
    private javax.swing.JLabel pcbL02C06;
    private javax.swing.JLabel pcbL02C07;
    private javax.swing.JLabel pcbL02C08;
    private javax.swing.JLabel pcbL02C09;
    private javax.swing.JLabel pcbL02C10;
    private javax.swing.JLabel pcbL02C11;
    private javax.swing.JLabel pcbL02C12;
    private javax.swing.JLabel pcbL03C01;
    private javax.swing.JLabel pcbL03C02;
    private javax.swing.JLabel pcbL03C03;
    private javax.swing.JLabel pcbL03C04;
    private javax.swing.JLabel pcbL03C05;
    private javax.swing.JLabel pcbL03C06;
    private javax.swing.JLabel pcbL03C07;
    private javax.swing.JLabel pcbL03C08;
    private javax.swing.JLabel pcbL03C09;
    private javax.swing.JLabel pcbL03C10;
    private javax.swing.JLabel pcbL03C11;
    private javax.swing.JLabel pcbL03C12;
    private javax.swing.JLabel pcbL04C01;
    private javax.swing.JLabel pcbL04C02;
    private javax.swing.JLabel pcbL04C03;
    private javax.swing.JLabel pcbL04C04;
    private javax.swing.JLabel pcbL04C05;
    private javax.swing.JLabel pcbL04C06;
    private javax.swing.JLabel pcbL04C07;
    private javax.swing.JLabel pcbL04C08;
    private javax.swing.JLabel pcbL04C09;
    private javax.swing.JLabel pcbL04C10;
    private javax.swing.JLabel pcbL04C11;
    private javax.swing.JLabel pcbL04C12;
    private javax.swing.JLabel pcbL05C01;
    private javax.swing.JLabel pcbL05C02;
    private javax.swing.JLabel pcbL05C03;
    private javax.swing.JLabel pcbL05C04;
    private javax.swing.JLabel pcbL05C05;
    private javax.swing.JLabel pcbL05C06;
    private javax.swing.JLabel pcbL05C07;
    private javax.swing.JLabel pcbL05C08;
    private javax.swing.JLabel pcbL05C09;
    private javax.swing.JLabel pcbL05C10;
    private javax.swing.JLabel pcbL05C11;
    private javax.swing.JLabel pcbL05C12;
    private javax.swing.JLabel pcbL06C01;
    private javax.swing.JLabel pcbL06C02;
    private javax.swing.JLabel pcbL06C03;
    private javax.swing.JLabel pcbL06C04;
    private javax.swing.JLabel pcbL06C05;
    private javax.swing.JLabel pcbL06C06;
    private javax.swing.JLabel pcbL06C07;
    private javax.swing.JLabel pcbL06C08;
    private javax.swing.JLabel pcbL06C09;
    private javax.swing.JLabel pcbL06C10;
    private javax.swing.JLabel pcbL06C11;
    private javax.swing.JLabel pcbL06C12;
    private javax.swing.JLabel pcbL07C01;
    private javax.swing.JLabel pcbL07C02;
    private javax.swing.JLabel pcbL07C03;
    private javax.swing.JLabel pcbL07C04;
    private javax.swing.JLabel pcbL07C05;
    private javax.swing.JLabel pcbL07C06;
    private javax.swing.JLabel pcbL07C07;
    private javax.swing.JLabel pcbL07C08;
    private javax.swing.JLabel pcbL07C09;
    private javax.swing.JLabel pcbL07C10;
    private javax.swing.JLabel pcbL07C11;
    private javax.swing.JLabel pcbL07C12;
    private javax.swing.JLabel pcbL08C01;
    private javax.swing.JLabel pcbL08C02;
    private javax.swing.JLabel pcbL08C03;
    private javax.swing.JLabel pcbL08C04;
    private javax.swing.JLabel pcbL08C05;
    private javax.swing.JLabel pcbL08C06;
    private javax.swing.JLabel pcbL08C07;
    private javax.swing.JLabel pcbL08C08;
    private javax.swing.JLabel pcbL08C09;
    private javax.swing.JLabel pcbL08C10;
    private javax.swing.JLabel pcbL08C11;
    private javax.swing.JLabel pcbL08C12;
    private javax.swing.JLabel pcbL09C01;
    private javax.swing.JLabel pcbL09C02;
    private javax.swing.JLabel pcbL09C03;
    private javax.swing.JLabel pcbL09C04;
    private javax.swing.JLabel pcbL09C05;
    private javax.swing.JLabel pcbL09C06;
    private javax.swing.JLabel pcbL09C07;
    private javax.swing.JLabel pcbL09C08;
    private javax.swing.JLabel pcbL09C09;
    private javax.swing.JLabel pcbL09C10;
    private javax.swing.JLabel pcbL09C11;
    private javax.swing.JLabel pcbL09C12;
    private javax.swing.JLabel pcbL10C01;
    private javax.swing.JLabel pcbL10C02;
    private javax.swing.JLabel pcbL10C03;
    private javax.swing.JLabel pcbL10C04;
    private javax.swing.JLabel pcbL10C05;
    private javax.swing.JLabel pcbL10C06;
    private javax.swing.JLabel pcbL10C07;
    private javax.swing.JLabel pcbL10C08;
    private javax.swing.JLabel pcbL10C09;
    private javax.swing.JLabel pcbL10C10;
    private javax.swing.JLabel pcbL10C11;
    private javax.swing.JLabel pcbL10C12;
    private javax.swing.JLabel pcbL11C01;
    private javax.swing.JLabel pcbL11C02;
    private javax.swing.JLabel pcbL11C03;
    private javax.swing.JLabel pcbL11C04;
    private javax.swing.JLabel pcbL11C05;
    private javax.swing.JLabel pcbL11C06;
    private javax.swing.JLabel pcbL11C07;
    private javax.swing.JLabel pcbL11C08;
    private javax.swing.JLabel pcbL11C09;
    private javax.swing.JLabel pcbL11C10;
    private javax.swing.JLabel pcbL11C11;
    private javax.swing.JLabel pcbL11C12;
    private javax.swing.JLabel pcbL12C01;
    private javax.swing.JLabel pcbL12C02;
    private javax.swing.JLabel pcbL12C03;
    private javax.swing.JLabel pcbL12C04;
    private javax.swing.JLabel pcbL12C05;
    private javax.swing.JLabel pcbL12C06;
    private javax.swing.JLabel pcbL12C07;
    private javax.swing.JLabel pcbL12C08;
    private javax.swing.JLabel pcbL12C09;
    private javax.swing.JLabel pcbL12C10;
    private javax.swing.JLabel pcbL12C11;
    private javax.swing.JLabel pcbL12C12;
    private javax.swing.JProgressBar progBarre;
    private javax.swing.JLabel progLocLabel;
    private javax.swing.JFileChooser programmerLoc;
    private javax.swing.JLabel statutPGRM;
    private javax.swing.JLabel statutPRGLabel;
    private javax.swing.JLabel statutRs232;
    private javax.swing.JRadioButtonMenuItem stop1;
    private javax.swing.JRadioButtonMenuItem stop2;
    private javax.swing.JLabel titre;
    private javax.swing.JLabel titreLabProg;
    private javax.swing.JLabel titreParamsWin;
    private javax.swing.JLabel version;
    private javax.swing.JMenuItem voirAide;
    // End of variables declaration//GEN-END:variables

    private void testParamsProg() {

        System.out.println("produit à programmer: " + produitAprogrammer);

        if (produitAprogrammer == null) {

            console.setText("Sélectionner un produit avant de commencer!");

            activerLedPRGM(false);
            return;

        }

        if (!envVariable) {

            console.setText("Vérifier que JAVA est ajouté aux variables d'environnement");

            activerLedPRGM(false);
            return;

        }

        if (programmerPathParamsProperties == null || programmerPathParamsProperties.equals("na")) {

            console.setText("Le programmateur n'est pas localisé");

            activerLedPRGM(false);
            return;

        }

        console.setText("Paramètres de programmation définis. Vous pouvez commencer!");
        activerLedPRGM(true);

        //initializer.update("binaryLocations", hexLocationsParamsProperties);
        //initializer.update("programmerDirectory", programmerPathParamsProperties);
        //initialisationParams();
        if (envVariable) {

            initializer.update("varEnv", "true");

        } else {

            initializer.update("varEnv", "false");
        }

        if (connexionRS232Active) {

            statutRs232.setBackground(Color.GREEN);
            activerBtnProgrammer(true);

        } else {

            statutRs232.setBackground(Color.RED);
            activerBtnProgrammer(true); // forcer pour besoin de test
            // inhibBtn();               // forcer pour besoin de test

        }

    }

    public void montrerError(String message, String titre) {

        JOptionPane.showMessageDialog(this, message, titre, JOptionPane.ERROR_MESSAGE);
    }

    public boolean confirmeParams() {

        int response = JOptionPane.showConfirmDialog(this, "Confirmez-vous ces paramètres?", "Paramètres de programmation définis", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.NO_OPTION) {
            System.out.println("No button clicked");
            return false;
        }
        if (response == JOptionPane.YES_OPTION) {

            return true;

        }
        return false;

    }

    private Connecteur getConnecteur() {

        if (this.connecteur == null) {
            this.connecteur = new Connecteur();
            this.connecteur.addObserver(this);
        }
        return this.connecteur;

    }

    @Override
    public void update(Observable o, Object arg) {

        /*
        String inputLine = (String) arg;
        System.out.println(inputLine);
        processRapport(inputLine);
         */
        if (arg instanceof Integer) {

            progBarre.setValue((100 / intNombreDeVoiesCarteEnTest) * (Integer) arg);
            console.setText("Numéro de carte: " + (Integer) arg);

        }
        /*
        if (arg instanceof Integer) {

            if ((Integer) arg == Constants.PROG_SUCCESS) {

                //voyant.setBackground(Color.GREEN);
                progBarre.setValue(100);
                console.setText("Programmation terminée!");
            }

            if ((Integer) arg == Constants.ERASE_SUCCESS) {

                voyant.setBackground(Color.GREEN);
                progBarre.setValue(100);
                console.setText("Effacement terminée!");
            }

            if ((Integer) arg == Constants.PROG_START) {

                voyant.setBackground(Color.YELLOW);
                progBarre.setStringPainted(true);
                progBarre.setString("Programmation en cours...");
                progBarre.setValue(0);
                console.setText("Programmation en cours");
            }

            if ((Integer) arg == Constants.PROG_SUCCESS_ETAPE1) {

                voyant.setBackground(Color.YELLOW);
                progBarre.setStringPainted(true);
                progBarre.setString("Programmation en cours...");
                progBarre.setValue(15);
                console.setText("Programmation: étape 1 terminée!");
            }

            if ((Integer) arg == Constants.PROG_UNSUCCESS_ETAPE1) {

                voyant.setBackground(Color.RED);
                progBarre.setString("Echec programmation!");
                progBarre.setStringPainted(true);
                console.setText("Programmation: échec étape 1!");
                montrerError("Vérifier positionnement carte!", "Erreur de programmation");
                activerBtnAttenteACQ();
            }

        } else {

        }
         */

    }

    private void setEnabledMenusConfiguration() {

    }

    private void setStatusRS232(boolean statut) {

        if (statut) {

            statutRs232.setForeground(Color.GREEN);
            statutRs232.setBackground(Color.GREEN);
        } else {
            statutRs232.setForeground(Color.RED);
            statutRs232.setBackground(Color.RED);
        }

    }

    private void processRapport(String inputLine) {

        if (inputLine.trim().startsWith("-> INIT")) {

            System.out.println("reset system");
            messageConsole(inputLine.trim());
            resetSystem();

        }

        // traitement des résultats aux étapes de test
        if (inputLine.trim().startsWith("-> COMMUTATION")) {

            String[] tab = inputLine.trim().split(":");
            int etape = Integer.parseInt(tab[1]);

        }

    }

    private void activerBtnReponseOp(boolean active) {

        if (active) {

            btnProg.setText("OK");
            btnProg.setBackground(new Color(163, 194, 240));
            btnProg.setEnabled(true);

            btnEffacer.setText("NON OK");
            btnEffacer.setBackground(new Color(163, 194, 240));
            btnEffacer.setEnabled(true);

        } else {

            btnProg.setText("OK");
            btnProg.setBackground(new Color(163, 194, 240));
            btnProg.setEnabled(false);

            btnEffacer.setText("NON OK");
            btnEffacer.setBackground(new Color(163, 194, 240));
            btnEffacer.setEnabled(false);

        }

    }

    private void activerBtnAcquittement(boolean active) {

        if (active) {

            btnProg.setEnabled(false);
            btnProg.setBackground(Color.GRAY);
            btnEffacer.setEnabled(false);
            btnEffacer.setBackground(Color.GRAY);

        } else {

        }

    }

    private void messageConsole(String message) {

        console.setText(message);

    }

    private void activerBtnProgrammation(boolean active) {

        if (active) {

            btnProg.setText("PROGRAMMER");
            btnProg.setEnabled(true);
            btnProg.setBackground(new Color(163, 194, 240));

            btnEffacer.setText("EFFACER");
            btnEffacer.setEnabled(true);
            btnEffacer.setBackground(new Color(163, 194, 240));
            testActif = false;
            auto = true;

        } else {

            btnProg.setText("PROGRAMMER");
            btnProg.setEnabled(false);
            btnProg.setBackground(Color.GRAY);

            btnEffacer.setText("EFFACER");
            btnEffacer.setEnabled(false);
            btnEffacer.setBackground(Color.GRAY);
            testActif = false;
            auto = false;

        }
    }

    private void waitForErasing(boolean active) {

        if (active) {

            console.setText("Effacement demandé");

            System.out.println("Affichage yellow");

        } else {

            console.setText("Effacement terminé");

            System.out.println("Affichage green");

        }

    }

    private void waitForProgramming(boolean active) {

        if (active) {

            console.setText("Programation en cours");

        } else {

            console.setText("Programation terminé");

        }

    }

    private void alerteRS232() {

        int i = connecteur.disconnect();
        if (i == 0) {

            console.setForeground(Color.BLUE);
            console.setText("La connexion a été perdue! Vous devez la rétablir.");
            setStatusRS232(false);
            btnConnexion.setEnabled(true);
            btnDeconnexion.setEnabled(false);
            connexionRS232Active = false;

            setEnabledMenusConfiguration();
            testActif = false;
            auto = false;
            // activerBtnProgrammation(false);
            inhibBtn();

        }

        montrerError("La connexion RS-232 a été perdue!\n Vérifer la connexion RS-232 et appuyer sur OK\n L'application va redémarrer", "Défaut de connexion");
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("java -jar keypadProgrammerModule2.jar");
            System.exit(0);

        } catch (IOException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void rechercherPortsComms() {

        menuPort.removeAll();

        listePorts.clear();
        listePortString.clear();
        listePortString = connecteur.getListPorts();

        for (String p : listePortString) {

            JRadioButtonMenuItem m = new JRadioButtonMenuItem(p);
            groupPorts.add(m);
            m.addActionListener(new PortSupplier());
            menuPort.add(m);
        }

    }

    // Fonction d'activation des boutons selon phase en cours
    void activerBtnAttenteLancement() {

        activerBtnLancer(true);
        activerBtnACQ(false);
        activerBtnEffacer(false);
        activerBtnOK(false);
        activerBtnNOK(false);
        activerBtnTester(false);
        activerBtnProgrammer(false);

    }

    void activerBtnAttenteACQ() {

        activerBtnLancer(false);
        activerBtnACQ(true);
        activerBtnEffacer(false);
        activerBtnOK(false);
        activerBtnNOK(false);
        activerBtnTester(false);
        activerBtnProgrammer(false);

    }

    void activerBtnTestEnCours() {

        activerBtnLancer(false);
        activerBtnACQ(false);
        activerBtnEffacer(false);
        activerBtnOK(true);
        activerBtnNOK(true);
        activerBtnTester(false);
        activerBtnProgrammer(false);

    }

    void inhibBtn() {

        activerBtnLancer(false);
        activerBtnACQ(false);
        activerBtnEffacer(true);
        activerBtnOK(false);
        activerBtnNOK(false);
        activerBtnTester(false);
        activerBtnProgrammer(false);

    }

    // Fonctions d'activation des boutons
    void activerBtnLancer(boolean active) {

    }

    void activerBtnOK(boolean active) {

    }

    void activerBtnNOK(boolean active) {

    }

    void activerBtnTester(boolean active) {

    }

    void activerBtnACQ(boolean active) {

        btnACQ.setOpaque(true);
        if (active) {

            btnACQ.setEnabled(true);
            btnACQ.setBackground(new Color(163, 194, 240));

        } else {

            btnACQ.setEnabled(false);
            btnACQ.setBackground(Color.GRAY);

        }
    }

    void activerBtnProgrammer(boolean active) {

        btnProg.setOpaque(true);
        if (active) {

            btnProg.setEnabled(true);
            btnProg.setBackground(new Color(163, 194, 240));

        } else {

            btnProg.setEnabled(false);
            btnProg.setBackground(Color.GRAY);

        }
    }

    void activerBtnEffacer(boolean active) {

        btnEffacer.setOpaque(true);
        if (active) {

            btnEffacer.setEnabled(true);
            btnEffacer.setBackground(new Color(163, 194, 240));

        } else {

            btnEffacer.setEnabled(false);
            btnEffacer.setBackground(Color.GRAY);

        }
    }

    private void clignottementVoyant() {

        while (AttenteReponseOperateur) {

            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void resetSystem() {

        System.out.println("reset system");

        testActif = false;
        console.setText("Système réinitialisé");
        testParamsProg();

        progBarre.setValue(0);
        progBarre.setString("En attente lancement de programmation");
        progBarre.setStringPainted(true);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }
        int comm = connecteur.envoyerData(Constants.START);
        if (comm == -1) {

            alerteRS232();
            return;

        }
        testActif = true;
        auto = true;

        // activerBtnReponseOp(testActif);
        activerBtnTestEnCours();
    }

    private void activerFonctionAjouter(boolean active) {

        if (active) {
            paramsWin.setSize(1300, 2600);
        } else {
            paramsWin.setSize(1300, 600);
        }

        btnAjouter.setVisible(!active);
        messageCreation.setVisible(active);
        labelAjoutCarte.setVisible(active);
        nomNouvelleCarte.setVisible(active);
        LabelNbreDeVoiesNouvelleCarte.setVisible(active);
        nombreVoiesNouvelleCarte.setVisible(active);
        btnSelectionBinaireAjouter.setVisible(active);
        btnEnregistrer.setVisible(active);
        messageBinaireSelectionne.setVisible(active);
        labelBinaireSelectionne.setVisible(active);
        LabelmicroController.setVisible(active);
        nouveauMicroController.setVisible(active);
        if (active) {
            messageCreation.setText("Compléter le formulaire ci-dessous");
        }
    }

    private ArrayList<String> extraireProduits(String listeProduits) {

        String[] liste = listeProduits.split(";");
        ArrayList<String> arrList = new ArrayList<String>();
        arrList.add("---");
        for (int i = 0; i < liste.length; i++) {

            System.out.println(liste[i]);
            arrList.add(liste[i]);
        }
        return arrList;

    }

    private ArrayList<String> extraireLocalisationBinaires(String hexLocations) {

        String[] liste = hexLocations.split(";");
        ArrayList<String> arrList = new ArrayList<String>();
        for (int i = 0; i < liste.length; i++) {

            System.out.println(liste[i]);
            arrList.add(liste[i]);
        }
        return arrList;

    }

    private ArrayList<String> extraireVoies(String listeVoies) {

        String[] liste = listeVoies.split(";");
        ArrayList<String> arrList = new ArrayList<String>();
        for (int i = 0; i < liste.length; i++) {

            System.out.println(liste[i]);
            arrList.add(liste[i]);
        }
        return arrList;

    }

    private void enregistrerNouvelleCarte(String localisationNouveauBinaire, String nomNouveauBinaire, String nombreDeVoiesNouvelleCarte, String nouveauDevice) {

        comboListeProduits.addItem(nomNouveauBinaire);
        ListeBinairesEnregistres.add(localisationNouveauBinaire);
        listesProduits.add(nomNouveauBinaire);
        listesVoies.add(nombreDeVoiesNouvelleCarte);
        listeDevicesEnregistres.add(nouveauDevice);

        nomNouvelleCarte.setText("");
        messageBinaireSelectionne.setText("");
        nouveauMicroController.setText("");
        nombreVoiesNouvelleCarte.setText("");

        initialisation.setBinaryLocations(initialisation.getBinaryLocations() + ";" + localisationNouveauBinaire);
        initialisation.setProductNames(initialisation.getProductNames() + ";" + nomNouveauBinaire);
        initialisation.setNombreVoies(initialisation.getNombreVoies() + ";" + nombreDeVoiesNouvelleCarte);
        initialisation.setDevice(initialisation.getDevice() + ";" + nouveauDevice);

        initializer.update("binaryLocations", initialisation.getBinaryLocations());
        initializer.update("productNames", initialisation.getProductNames());
        initializer.update("voies", initialisation.getNombreVoies());
        initializer.update("device", initialisation.getDevice());

        /*
        localisationNouveauBinaire = null;
        nomNouveauBinaire = null;
        nombreDeVoiesNouvelleCarte = null;
        nouveauDevice = null;
         */
    }

    private ArrayList<String> extraireDevices(String devices) {

        String[] liste = devices.split(";");
        ArrayList<String> arrList = new ArrayList<String>();
        for (int i = 0; i < liste.length; i++) {

            System.out.println(liste[i]);
            arrList.add(liste[i]);
        }
        return arrList;
    }

    private void activerLedPRGM(boolean active) {

        if (!active) {

            statutPGRM.setBackground(Color.RED);
            statutPGRM.setForeground(Color.RED);
        } else {

            statutPGRM.setBackground(Color.GREEN);
            statutPGRM.setForeground(Color.GREEN);
        }
    }

    private void activerLedRS232(boolean active) {

        if (!active) {

            statutPGRM.setBackground(Color.RED);
            statutPGRM.setForeground(Color.RED);
        } else {

            statutPGRM.setBackground(Color.GREEN);
            statutPGRM.setForeground(Color.GREEN);
        }
    }

    void initialisationParams() throws IOException {

        initialisation = initializer.getInit();

        //Recherche nombre de voie du commutateur
        if (initialisation.getCommutateur().equals("na")) {

            System.out.println("Commutateur = " + initialisation.getCommutateur());

        } else {

            System.out.println("Commutateur = " + initialisation.getCommutateur());
            nombreVoiesCommutateurParamsProperties = initialisation.getCommutateur();
            limCommutateur = Integer.parseInt(nombreVoiesCommutateurParamsProperties);
        }

        // Recherche du fichier binaire
        if (initialisation.getBinaryLocations().equals("na")) {

            System.out.println("BinaryLocation = " + initialisation.getBinaryLocations());

        } else {

            System.out.println("BinaryLocation = " + initialisation.getBinaryLocations());
            hexLocationsParamsProperties = initialisation.getBinaryLocations();
            ListeBinairesEnregistres = extraireLocalisationBinaires(hexLocationsParamsProperties);
        }

        // Recherche nom du produit
        if (initialisation.getProductNames().equals("na")) {

            System.out.println("liste noms de produits = " + initialisation.getProductNames());
            nomProduit.setText("Aucun produit crée");

        } else {

            System.out.println("liste noms de produits  = " + initialisation.getProductNames());
            listeProduitsConnusParamsProperties = initialisation.getProductNames();
            //produits = extraireProduits(listeProduitsConnus);
            listesProduits = extraireProduits(listeProduitsConnusParamsProperties);
            //comboListeProduits.addItem("---");
            for (int i = 0; i < listesProduits.size(); i++) {

                //comboListeProduits.addItem(produits[i]);
                comboListeProduits.addItem(listesProduits.get(i));

            }
            comboListeProduits.setSelectedIndex(0);
            nomProduit.setText("Veuillez sélectionner un produit!");

        }

        // Recherche nombre de voies à programmer (nombre de carte par panneau)
        if (initialisation.getNombreVoies().equals("na")) {

            System.out.println("liste nombre de voies = " + initialisation.getNombreVoies());
            nombreVoies.setText("Aucune voie définie");

        } else {

            System.out.println("liste du nombre de voies  = " + initialisation.getNombreVoies());
            nombreDeVoiesEnregistresParamsProperties = initialisation.getNombreVoies();
            listesVoies = extraireVoies(nombreDeVoiesEnregistresParamsProperties);

        }

        // Recherche nom du microcontrôleur à programmer
        if (initialisation.getDevice().equals("na")) {

            System.out.println("liste des devices lues = " + initialisation.getDevice());
            nombreVoies.setText("Aucun device enregistré");

        } else {

            System.out.println("liste des devices lus  = " + initialisation.getDevice());
            devicesParamsProperties = initialisation.getDevice();
            listeDevicesEnregistres = extraireDevices(devicesParamsProperties);

        }

        // Recherche type de programmateur (code programmateur) 
        if (initialisation.getProgrammer().equals("na")) {

            System.out.println("liste des devices lues = " + initialisation.getProgrammer());
            nombreVoies.setText("Aucun programmateur enregistré");

        } else {

            System.out.println("Programmateur enregistré  = " + initialisation.getProgrammer());
            programmerParamsProperties = initialisation.getProgrammer();

        }

        // Recherche repertoire d'installation de la plateforme Microchip
        if (initialisation.getProgrammerDirectory().equals("na")) {

            System.out.println("Directory programmer = " + initialisation.getProgrammerDirectory());
            nombreVoies.setText("Aucune localisation programmateur enregistré");

        } else {

            System.out.println("Directory programmer = " + initialisation.getProgrammerDirectory());
            programmerPathParamsProperties = initialisation.getProgrammerDirectory();

        }

        // Recherche variable d'environnement pour la commande Java
        if (initialisation.getVarEnv().equals("na")) {

            System.out.println("varEnv = " + initialisation.getVarEnv());

        } else {

            System.out.println("varEnv = " + initialisation.getVarEnv());
            if (initialisation.getVarEnv().equals("true")) {

                envVariable = true;
            }

            if (initialisation.getVarEnv().equals("false")) {

                envVariable = false;
            }

        }

    }

}
