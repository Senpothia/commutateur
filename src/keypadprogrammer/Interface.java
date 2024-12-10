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

    private File programmerLocation = null;
    private File binaryLocation = null;
    private File BLELocation = null;
    private File nouveauBinaire = null;
    private String filePaths = null;
    private String localisationNouveauBinaire = null;
    private String nomNouveauBinaire = null;
    private String hexLocations = null;
    private String bleLocation = null;
    private String binaireLocation = null;
    private boolean envVariable = false;
    private String produitAprogrammer = null;
    private String listeProduitsConnus = null;
    private boolean confirmationParams = false;
    private String bleCode;
    private String[] produits = null;
    private String[] localisationsBinaires = null;
    private int selectedProduct = 0;

    Connecteur connecteur = getConnecteur();            // gére la connexion RS232

    private int baudeRate = 9600;
    private int numDatabits = 8;
    private int parity = 0;
    private int stopBits = 1;
    private int newReadTimeout = 1000;
    private int newWriteTimeout = 0;

    private boolean connexionRS232Active = false;       // état de la connexion RS-232

    private boolean testActif = false;
    private boolean programmationActive = false;
    private boolean auto = true;
    private boolean AttenteReponseOperateur = false;

    public static Initializer initializer = new Initializer();  // Charge les propriétés du fichier properties contenant les paramètres de programmation
    public static Initialisation initialisation;          // Centralise les données rapportées par l'initializer

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

        this.getContentPane().setBackground(new Color(0, 102, 102));

        voyant.setBackground(new Color(204, 136, 53));
        voyant.setForeground(Color.GRAY);
        voyant.setOpaque(true);
        console.setBackground(new Color(247, 242, 208));
        console.setOpaque(true);
        console.setForeground(Color.red);
        console.setFont(new Font("Serif", Font.PLAIN, 20));
        nomProduit.setBackground(new Color(247, 242, 208));
        nomProduit.setOpaque(true);
        nomProduit.setForeground(Color.red);
        nomProduit.setFont(new Font("Serif", Font.PLAIN, 20));

        paramsWin.getContentPane().setBackground(new Color(0, 102, 102));

        messageCreation.setBackground(new Color(247, 242, 208));

        progLocLabel.setBackground(new Color(247, 242, 208));
        hexLocalisation.setOpaque(true);

        messageCreation.setOpaque(true);
        progLocLabel.setOpaque(true);
        inhibBtn();

        aide.getContentPane().setBackground(new Color(247, 242, 208));

        rechercherPortsComms();

        initialisation = initializer.getInit();
        if (initialisation.getBinaryLocations().equals("na")) {

            System.out.println("BinaryLocation = " + initialisation.getBinaryLocations());

        } else {

            System.out.println("BinaryLocation = " + initialisation.getBinaryLocations());
            hexLocations = initialisation.getBinaryLocations();
            localisationsBinaires = extraireLocalisationBinaires(hexLocations);

        }

        if (initialisation.getProductNames().equals("na")) {

            System.out.println("liste noms de produits = " + initialisation.getProductNames());
            nomProduit.setText("Aucun produit crée");

        } else {

            System.out.println("liste noms de produits  = " + initialisation.getProductNames());
            listeProduitsConnus = initialisation.getProductNames();
            produits = extraireProduits(listeProduitsConnus);
            comboListeProduits.addItem("---");
            for (int i = 0; i < produits.length; i++) {

                comboListeProduits.addItem(produits[i]);

            }
            comboListeProduits.setSelectedIndex(0);
            nomProduit.setText("Veuillez sélectionner un produit!");

        }

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

        int dirCreation = progController.createLogFolder(Constants.LOG_DIRECTORY);
        if (dirCreation != 1) {

            montrerError("Echec à la création du repertoire de logs", "Erreur d'initialisation");
            System.exit(0);

        }

        progBarre.setStringPainted(true);
        progBarre.setForeground(Color.blue);
        progBarre.setOpaque(true);
        progBarre.setVisible(true);

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
        messageCreation = new javax.swing.JLabel();
        comboListeProduits = new javax.swing.JComboBox<>();
        listeProduits = new javax.swing.JLabel();
        btnAjouter = new javax.swing.JButton();
        hexLocalisation = new javax.swing.JLabel();
        nomNouvelleCarte = new javax.swing.JTextField();
        titreLabBLE2 = new javax.swing.JLabel();
        btnSelectionBinaireAjouter = new javax.swing.JButton();
        btnEnregistrer = new javax.swing.JButton();
        messageBinaireSelectionne = new javax.swing.JTextField();
        labelBinaireSelectionne = new javax.swing.JLabel();
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
        voyant = new javax.swing.JLabel();
        statutRs232 = new javax.swing.JLabel();
        console = new javax.swing.JLabel();
        version = new javax.swing.JLabel();
        StatutRS232Lab = new javax.swing.JLabel();
        statutPRGLabel = new javax.swing.JLabel();
        statutPGRM = new javax.swing.JLabel();
        progBarre = new javax.swing.JProgressBar();
        btnACQ = new javax.swing.JButton();
        nomProduit = new javax.swing.JLabel();
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
        paramsWin.setMinimumSize(new java.awt.Dimension(1300, 900));
        paramsWin.setPreferredSize(new java.awt.Dimension(1300, 1300));

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

        messageCreation.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        messageCreation.setForeground(new java.awt.Color(255, 0, 51));
        messageCreation.setText("Jlabel");
        messageCreation.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

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

        titreLabBLE2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        titreLabBLE2.setText("Fichier binaire");

        btnSelectionBinaireAjouter.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnSelectionBinaireAjouter.setForeground(new java.awt.Color(255, 0, 0));
        btnSelectionBinaireAjouter.setText("Sélectionner le binaire");
        btnSelectionBinaireAjouter.setActionCommand("");
        btnSelectionBinaireAjouter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectionBinaireAjouterActionPerformed(evt);
            }
        });

        btnEnregistrer.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
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

        javax.swing.GroupLayout paramsWinLayout = new javax.swing.GroupLayout(paramsWin.getContentPane());
        paramsWin.getContentPane().setLayout(paramsWinLayout);
        paramsWinLayout.setHorizontalGroup(
            paramsWinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paramsWinLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paramsWinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paramsWinLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnFermerParams)
                        .addGap(49, 49, 49))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paramsWinLayout.createSequentialGroup()
                        .addGroup(paramsWinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(messageBinaireSelectionne, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nomNouvelleCarte, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(messageCreation, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(hexLocalisation, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(progLocLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, paramsWinLayout.createSequentialGroup()
                                .addGroup(paramsWinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(paramsWinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(comboListeProduits, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(paramsWinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(EnvVarBox)
                                            .addComponent(titreLabProg, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(listeProduits, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(btnAjouter)
                                    .addComponent(titreLabBLE2, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelAjoutCarte, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelBinaireSelectionne, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
            .addGroup(paramsWinLayout.createSequentialGroup()
                .addGroup(paramsWinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paramsWinLayout.createSequentialGroup()
                        .addGap(492, 492, 492)
                        .addComponent(titreParamsWin, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(paramsWinLayout.createSequentialGroup()
                        .addGap(433, 433, 433)
                        .addComponent(btnSelectionBinaireAjouter, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(paramsWinLayout.createSequentialGroup()
                        .addGap(438, 438, 438)
                        .addComponent(btnEnregistrer, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 486, Short.MAX_VALUE))
        );
        paramsWinLayout.setVerticalGroup(
            paramsWinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paramsWinLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(titreParamsWin)
                .addGap(18, 18, 18)
                .addComponent(titreLabProg)
                .addGap(18, 18, 18)
                .addComponent(EnvVarBox)
                .addGap(18, 18, 18)
                .addComponent(progLocLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(listeProduits)
                .addGap(18, 18, 18)
                .addComponent(comboListeProduits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(titreLabBLE2)
                .addGap(18, 18, 18)
                .addComponent(hexLocalisation, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addComponent(btnAjouter)
                .addGap(18, 18, 18)
                .addComponent(messageCreation, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(labelAjoutCarte)
                .addGap(18, 18, 18)
                .addComponent(nomNouvelleCarte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(btnSelectionBinaireAjouter)
                .addGap(25, 25, 25)
                .addComponent(labelBinaireSelectionne)
                .addGap(18, 18, 18)
                .addComponent(messageBinaireSelectionne, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(btnEnregistrer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 341, Short.MAX_VALUE)
                .addComponent(btnFermerParams)
                .addGap(116, 116, 116))
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

        voyant.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnEffacer, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addComponent(btnProg, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(98, 98, 98)
                .addComponent(btnACQ, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(151, 151, 151))
            .addGroup(layout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(nomProduit, javax.swing.GroupLayout.PREFERRED_SIZE, 659, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(progBarre, javax.swing.GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE)
                        .addComponent(console, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 112, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(330, 330, 330)
                .addComponent(voyant, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(statutPGRM, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(titre, javax.swing.GroupLayout.PREFERRED_SIZE, 531, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(91, 91, 91)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(StatutRS232Lab, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(statutRs232, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(statutPRGLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(36, 36, 36))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(version)
                        .addGap(26, 26, 26))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(StatutRS232Lab)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(statutRs232, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(statutPRGLabel))
                    .addComponent(titre, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statutPGRM, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(nomProduit, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(voyant, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addComponent(progBarre, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(console, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(version)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnEffacer, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnProg, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnACQ, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(74, 74, 74))))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnProgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProgActionPerformed
        if (!testActif) {

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

            voyant.setBackground(Color.YELLOW);

            Thread t = new Thread() {
                public void run() {

                    try {
                        int comm = connecteur.program(hexLocations, bleLocation, envVariable, filePaths);
                        System.out.println("Retour programmation. Code reçu: " + comm);
                        if (comm == -1) {

                            alerteRS232();

                        }

                        if (comm == -2) {

                            console.setText("Erreur de programmation");
                            voyant.setBackground(Color.red);
                            connecteur.envoyerData(Constants.ERR_PROG);
                            programmationActive = true;

                        }

                        if (comm == 1) {

                            /*
                            console.setText("Programmation terminée!");
                            voyant.setBackground(Color.GREEN);
                            connecteur.envoyerData(Constants.END_PROG);
                            programmationActive = true;
                             */
                            console.setText("Programmation terminée!");
                            // voyant.setBackground(Color.GREEN);
                            connecteur.envoyerData(Constants.END_PROG);
                            Constants.tempo(1000);
                            programmationActive = true;

                            comm = connecteur.envoyerData(Constants.AQC);
                            Constants.tempo(1000);
                            comm = connecteur.envoyerData(Constants.START);
                            activerBtnTestEnCours();

                        }

                    } catch (IOException ex) {
                        Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            t.start();

        } else {

            int comm = connecteur.envoyerData(Constants.OK);

            if (comm == -1) {

                alerteRS232();

            }

            console.setText("Réponse OK");

        }

    }//GEN-LAST:event_btnProgActionPerformed

    private void btnEffacerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEffacerActionPerformed
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

                    connecteur.erase(envVariable, filePaths);
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

    }//GEN-LAST:event_btnEffacerActionPerformed

    private void btnSelectLocationProgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectLocationProgActionPerformed


    }//GEN-LAST:event_btnSelectLocationProgActionPerformed

    private void btnSelectBinaryLocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectBinaryLocActionPerformed


    }//GEN-LAST:event_btnSelectBinaryLocActionPerformed

    private void btnFermerParamsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFermerParamsActionPerformed

        testParamsProg();
        if (EnvVarBox.isSelected()) {

            envVariable = true;
        } else {

            envVariable = false;
        }
        paramsWin.setVisible(false);
        testParamsProg();
        selectedProduct = comboListeProduits.getSelectedIndex();

        if (selectedProduct != 0) {
            nomProduit.setText(produits[selectedProduct - 1]);
            binaireLocation = localisationsBinaires[selectedProduct - 1];
            System.out.println("localistaion binaire: " + binaireLocation);
            activerBtnProgrammer(true);
            console.setText("Vous pouvez commencer à programmer");

        } else {

            nomProduit.setText("Aucun produit sélectionné!");
            activerBtnProgrammer(false);
            console.setText("Sélectionnez un produit avant de programmer");

        }
    }//GEN-LAST:event_btnFermerParamsActionPerformed

    private void btnFermerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFermerActionPerformed

        System.exit(0);
    }//GEN-LAST:event_btnFermerActionPerformed

    private void paramsProgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paramsProgActionPerformed

        programmerLoc.showOpenDialog(this);
        programmerLocation = programmerLoc.getSelectedFile();
        if (programmerLoc.getSelectedFile() != null) {

            console.setText("Repertoire programmateur: " + programmerLocation.getPath());
            filePaths = programmerLocation.getPath();

        }

        testParamsProg();
    }//GEN-LAST:event_paramsProgActionPerformed

    private void menuCreerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCreerActionPerformed

        paramsWin.setVisible(true);
        EnvVarBox.setSelected(envVariable);
        activerFonctionAjouter(true);
        if (filePaths != null) {
            progLocLabel.setText("Repertoire programmateur: " + filePaths);
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

        if (filePaths != null) {
            progLocLabel.setText("Repertoire programmateur: " + filePaths);
        } else {

            progLocLabel.setText("Repertoire programmateur non défini!");
        }

        if (bleLocation != null) {

            messageCreation.setText("Fichier binaire: " + bleLocation);
        } else {

            messageCreation.setText("Sélectionner un produit avant de commencer!");
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

    private void EnvVarBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EnvVarBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EnvVarBoxActionPerformed

    private void EnvVarBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_EnvVarBoxStateChanged

        if (EnvVarBox.isSelected()) {

            progLocLabel.setText("Variable d'environnement définie!");

        } else {

            if (filePaths == null) {

                progLocLabel.setText("Repertoire programmateur non défini!");
            } else {

                progLocLabel.setText(filePaths);
            }

        }
    }//GEN-LAST:event_EnvVarBoxStateChanged

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
            activerBtnTester(true);
            activerBtnProgrammer(auto);

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
            inhibBtn();

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
        voyantTestEnCours(false);
        progBarre.setValue(0);
        progBarre.setString("En attente lancement de programmation");
        progBarre.setStringPainted(true);


    }//GEN-LAST:event_btnACQActionPerformed

    private void comboListeProduitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboListeProduitsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboListeProduitsActionPerformed

    private void btnAjouterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAjouterActionPerformed

        activerFonctionAjouter(true);
    }//GEN-LAST:event_btnAjouterActionPerformed

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

        testParamsProg();

    }//GEN-LAST:event_btnSelectionBinaireAjouterActionPerformed

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

        enregistrerNouvelleCarte(localisationNouveauBinaire, nomNouveauBinaire);
        montrerError("La nouvelle carte à été enregistrée", "Enregistrement effectué");

        paramsWin.setVisible(false);


    }//GEN-LAST:event_btnEnregistrerActionPerformed

    private void nomNouvelleCarteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomNouvelleCarteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nomNouvelleCarteActionPerformed

    private void messageBinaireSelectionneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_messageBinaireSelectionneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_messageBinaireSelectionneActionPerformed

    private void comboListeProduitsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboListeProduitsItemStateChanged

        if (comboListeProduits.getSelectedIndex() != 0) {
            hexLocalisation.setText(localisationsBinaires[comboListeProduits.getSelectedIndex() - 1]);
        } else {

            hexLocalisation.setText("Aucun produit sélectionné!");
        }

    }//GEN-LAST:event_comboListeProduitsItemStateChanged

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
    private javax.swing.JMenuItem paramsProg;
    private javax.swing.JFrame paramsWin;
    private javax.swing.JRadioButtonMenuItem parityEven;
    private javax.swing.JRadioButtonMenuItem parityNone;
    private javax.swing.JRadioButtonMenuItem parityOdd;
    private javax.swing.JProgressBar progBarre;
    private javax.swing.JLabel progLocLabel;
    private javax.swing.JFileChooser programmerLoc;
    private javax.swing.JLabel statutPGRM;
    private javax.swing.JLabel statutPRGLabel;
    private javax.swing.JLabel statutRs232;
    private javax.swing.JRadioButtonMenuItem stop1;
    private javax.swing.JRadioButtonMenuItem stop2;
    private javax.swing.JLabel titre;
    private javax.swing.JLabel titreLabBLE2;
    private javax.swing.JLabel titreLabProg;
    private javax.swing.JLabel titreParamsWin;
    private javax.swing.JLabel version;
    private javax.swing.JMenuItem voirAide;
    private javax.swing.JLabel voyant;
    // End of variables declaration//GEN-END:variables

    private void testParamsProg() {

        if (hexLocations == null) {

            console.setText("Le fichier binaire n'est pas défini!");
            voyant.setBackground(Color.GRAY);
            statutPGRM.setBackground(Color.RED);
            statutPGRM.setForeground(Color.RED);

        } else {

            if (bleLocation == null) {

                console.setText("Sélectionner un produit avant de commencer!");
                voyant.setBackground(Color.GRAY);
                statutPGRM.setBackground(Color.RED);
                statutPGRM.setForeground(Color.RED);

            } else {

                if (!envVariable && filePaths == null) {

                    console.setText("Repertoire programmateur non défini!");
                    statutPGRM.setBackground(Color.RED);
                    statutPGRM.setForeground(Color.RED);

                } else {

                    console.setText("Paramètres de programmation définis. Vous pouvez commencer!");
                    statutPGRM.setBackground(Color.GREEN);
                    statutPGRM.setForeground(Color.GREEN);
                    initializer.update("binaryLocation", hexLocations);
                    initializer.update("bleLocation", bleLocation);
                    initializer.update("programmerDirectory", filePaths);

                    if (envVariable) {

                        initializer.update("varEnv", "true");
                    } else {

                        initializer.update("varEnv", "false");
                    }

                }

            }

        }

        if (connexionRS232Active) {

            statutRs232.setBackground(Color.GREEN);
            //activerBtnAttenteLancement();
            activerBtnTester(true);
            activerBtnProgrammer(true);
            activerBtnACQ(false);

        } else {

            statutRs232.setBackground(Color.red);
            inhibBtn();

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

            if ((Integer) arg == Constants.PROG_SUCCESS_ETAPE2) {

                voyant.setBackground(Color.YELLOW);
                progBarre.setStringPainted(true);
                progBarre.setString("Programmation en cours...");
                progBarre.setValue(70);
                console.setText("Programmation: étape 2 terminée!");
            }

            if ((Integer) arg == Constants.PROG_SUCCESS_ETAPE3) {

                voyant.setBackground(Color.YELLOW);
                progBarre.setStringPainted(true);
                progBarre.setString("Programmation en cours...");
                progBarre.setValue(90);
                console.setText("Programmation: étape 3 terminée!");
            }

            if ((Integer) arg == Constants.PROG_SUCCESS_ETAPE4) {

                //voyant.setBackground(Color.GREEN);
                progBarre.setString("Programmation terminée!");
                progBarre.setStringPainted(true);
                progBarre.setValue(100);
                console.setText("Programmation: étape 4 terminée! - Le test est en cours");

            }

            if ((Integer) arg == Constants.PROG_UNSUCCESS_ETAPE1) {

                voyant.setBackground(Color.RED);
                progBarre.setString("Echec programmation!");
                progBarre.setStringPainted(true);
                console.setText("Programmation: échec étape 1!");
                montrerError("Vérifier positionnement carte!", "Erreur de programmation");
                activerBtnAttenteACQ();
            }

            if ((Integer) arg == Constants.PROG_UNSUCCESS_ETAPE2) {

                voyant.setBackground(Color.RED);
                progBarre.setString("Echec programmation!");
                progBarre.setStringPainted(true);
                console.setText("Programmation: échec étape 2!");
                activerBtnAttenteACQ();
            }

            if ((Integer) arg == Constants.PROG_UNSUCCESS_ETAPE3) {

                voyant.setBackground(Color.RED);
                progBarre.setString("Echec programmation!");
                progBarre.setStringPainted(true);
                console.setText("Programmation: échec étape 3!");
                activerBtnAttenteACQ();
            }

            if ((Integer) arg == Constants.PROG_UNSUCCESS_ETAPE4) {

                voyant.setBackground(Color.RED);
                progBarre.setString("Echec programmation!");
                progBarre.setStringPainted(true);
                console.setText("Programmation: échec étape 4!");
                activerBtnAttenteACQ();
            }

        } else {

            String inputLine = (String) arg;
            System.out.println(inputLine);
            if (auto) {

                // console.setText(inputLine);
            }

            processRapport(inputLine);
        }

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

        if (auto) {

            if (inputLine.trim().startsWith("-> TEST CONFORME")) {

                messageConsole("TEST CONFORME -" + bleCode);
                activerBtnAttenteACQ();
                voyantTestOK(true);

            }

            if (inputLine.trim().startsWith("-> TEST MANUEL")) {

                messageConsole("TEST MANUEL EN COURS");
                auto = false;
                voyant.setBackground(Color.BLUE);

                btnProg.setEnabled(false);
                btnProg.setBackground(Color.GRAY);
                btnEffacer.setEnabled(false);
                btnEffacer.setBackground(Color.GRAY);

            }

            if (inputLine.trim().startsWith("-> FIN TEST MANUEL")) {

                System.out.println("test manuel acquitté1");
                messageConsole("FIN TEST MANUEL");
                voyant.setBackground(Color.GRAY);
                //inhibBtn();
                activerBtnTester(true);
                activerBtnProgrammer(auto);

            }

            if (inputLine.trim().startsWith("-> ERREUR:")) {

                System.out.println("Signalisation erreur!");
                messageConsole(inputLine.trim());
                activerBtnAttenteACQ();
                voyantTestOK(false);
                System.out.println("testActif  =" + testActif);

            }

            if (inputLine.trim().startsWith("-> PROGRAMMATION TERMINEE")) {

                System.out.println("programmation terminée");
                messageConsole(inputLine.trim());
                System.out.println("testActif  =" + testActif);

            }

            if (inputLine.trim().startsWith("-> EFFACEMENT TERMINE")) {

                System.out.println("effacement terminé");
                messageConsole(inputLine.trim());
                activerBtnAttenteLancement();
                voyantTestEnCours(false);
                System.out.println("testActif  =" + testActif);

            }

            if (inputLine.trim().startsWith("-> RESET")) {

                System.out.println("reset system");
                messageConsole(inputLine.trim());
                resetSystem();

            }

            // traitement des résultats aux étapes de test
            if (inputLine.trim().startsWith("-> TEST")) {

                AttenteReponseOperateur = false;
                String[] tab = inputLine.trim().split(":");
                int etape = Integer.parseInt(tab[1]);
                boolean result = false;
                System.out.println("tab[2]" + tab[2]);
                if (tab[2].equals("1")) {

                    result = true;
                } else {

                    result = false;
                }
                System.out.println("Etape: " + etape);
                if (etape != 18) {
                    console.setText("Etape: " + etape);
                }

                if (etape == 12) {

                    AttenteReponseOperateur = true;

                    if (etape == 12) {

                        console.setText("EN ATTENTE VALIDATION LEDS");
                    }

                    /*
                    if (etape > 16) {

                        console.setText("EN ATTENTE VALIDATION BLUETOOTH");
                    }
                     */
                    clignottementVoyant();

                }

                if (etape == 18) {

                    bleCode = tab[3];
                    console.setText("BLE:" + bleCode.substring(1, 18));
                    if (result) {

                        console.setText("BLE:" + bleCode.substring(1, 18) + " - PRESSEZ ACQ!");
                    } else {

                    }
                    activerBtnAttenteACQ();
                    voyantTestOK(true);

                }

            }

        } else {

            if (inputLine.trim().startsWith("-> FIN TEST MANUEL")) {

                System.out.println("test manuel acquitté2");
                messageConsole("FIN TEST MANUEL");
                voyant.setBackground(Color.GRAY);
                //activerBtnAttenteLancement();
                activerBtnTester(true);
                activerBtnProgrammer(true);

            }
        }

        if (inputLine.trim().startsWith("-> TEST ACQUITTE")) {

            messageConsole("TEST ACQUITTE");
            voyant.setBackground(Color.GRAY);
            //activerBtnAttenteLancement();
            activerBtnTester(true);
            activerBtnProgrammer(true);

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

    private void voyantTestEnCours(boolean active) {

        if (active) {
            voyant.setBackground(Color.ORANGE);
        } else {
            voyant.setBackground(Color.GRAY);
        }

    }

    private void voyantTestOK(boolean active) {

        if (active) {

            voyant.setBackground(Color.GREEN);
        } else {

            voyant.setBackground(Color.RED);
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
            voyant.setBackground(Color.YELLOW);
            System.out.println("Affichage yellow");

        } else {

            console.setText("Effacement terminé");
            voyant.setBackground(Color.GREEN);
            System.out.println("Affichage green");

        }

    }

    private void waitForProgramming(boolean active) {

        if (active) {

            console.setText("Programation en cours");
            voyant.setBackground(Color.YELLOW);
            voyant.setOpaque(true);

        } else {

            console.setText("Programation terminé");
            voyant.setBackground(Color.GREEN);
            voyant.setOpaque(true);

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
            voyantTestEnCours(false);

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
        activerBtnEffacer(false);
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
            voyant.setBackground(Color.MAGENTA);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
            }
            voyant.setBackground(Color.YELLOW);
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
        voyantTestEnCours(false);
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
        voyantTestEnCours(true);
        // activerBtnReponseOp(testActif);
        activerBtnTestEnCours();
    }

    private void activerFonctionAjouter(boolean active) {

        btnAjouter.setVisible(!active);
        messageCreation.setVisible(active);
        labelAjoutCarte.setVisible(active);
        nomNouvelleCarte.setVisible(active);

        btnSelectionBinaireAjouter.setVisible(active);
        btnEnregistrer.setVisible(active);
        messageBinaireSelectionne.setVisible(active);
        labelBinaireSelectionne.setVisible(active);
        if (active) {
            messageCreation.setText("Compléter le formulaire ci-dessous");
        }
    }

    private String[] extraireProduits(String listeProduits) {

        String[] liste = listeProduits.split(";");
        for (int i = 0; i < liste.length; i++) {

            System.out.println(liste[i]);
        }
        return liste;

    }

    private String[] extraireLocalisationBinaires(String hexLocations) {

        String[] liste = hexLocations.split(";");
        for (int i = 0; i < liste.length; i++) {

            System.out.println(liste[i]);
        }
        return liste;

    }

    private void enregistrerNouvelleCarte(String localisationNouveauBinaire1, String nomNouveauBinaire1) {

        localisationNouveauBinaire = null;
        nomNouveauBinaire = null;
        nomNouvelleCarte.setText("");
        messageBinaireSelectionne.setText("");
     
        
    }

}
