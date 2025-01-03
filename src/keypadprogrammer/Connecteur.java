/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keypadprogrammer;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.io.File;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michel
 */
public class Connecteur extends Observable {

    public static String portName = null;
    private SerialPort[] ports = null;
    public SerialPort portComm;
    private int baudeRate = 9600;
    private int numDatabits = 8;
    private int parity = 0;
    private int stopBits = 1;
    private int newReadTimeout = 1000;
    private int newWriteTimeout = 0;
    private ProgController progController = new ProgController();

    private OutputStream outputStream;

    private String inputLine;

    public static String getPortName() {
        return portName;
    }

    public static void setPortName(String portName) {
        Connecteur.portName = portName;
    }

    public int getBaudeRate() {
        return baudeRate;
    }

    public void setBaudeRate(int baudeRate) {
        this.baudeRate = baudeRate;
    }

    public int getNumDatabits() {
        return numDatabits;
    }

    public void setNumDatabits(int numDatabits) {
        this.numDatabits = numDatabits;
    }

    public int getParity() {
        return parity;
    }

    public void setParity(int parity) {
        this.parity = parity;
    }

    public int getStopBits() {
        return stopBits;
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
    }

    public int makeConnection(String portName, int baudeRate, int numDataBits, int parity, int stopBits) {

        try {

            if (portName == null) {

                System.out.println("makeConnection() - Port non sélectionné");
                return 0;
            }

            for (SerialPort p : ports) {

                //System.out.println("Interface.makeConnection() - getSystemPortName: " + p.getSystemPortName() + " // " + portName);
                if (p.getSystemPortName().equals(portName)) {

                    portComm = p;
                }
            }

            portComm.setBaudRate(baudeRate);
            portComm.setNumDataBits(numDatabits);
            portComm.setParity(parity);
            portComm.setNumStopBits(stopBits);
            portComm.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, newReadTimeout, newWriteTimeout);
            portComm.openPort();

            if (portComm.isOpen()) {

                System.out.println("Connexion réussie!");
                envoyerData(Constants.RESET_HARDWARE);
                // return 99;

            } else {

                System.out.println("Connexion échouée!");
                return -1;
            }

        } catch (Exception e) {

            System.out.println("Connexion échouée!");
            return -2;
        }

        portComm.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                    return;
                }

                try {

                    byte[] readBuffer = new byte[100];

                    int numRead = portComm.readBytes(readBuffer,
                            readBuffer.length);
                    byte[] lecture = new byte[numRead];
                    for (int i = 0; i < numRead; i++) {

                        lecture[i] = readBuffer[i];
                    }
                    inputLine = new String(lecture, StandardCharsets.UTF_8);

                    System.out.println("Received -> " + numRead + "bits lus - " + inputLine);
                    notifierResultat();

                } catch (Exception e) {   // Traitement des exceptions

                    System.err.println(e.toString());
                }
            }
        });

        return 99;

    }

    public int disconnect() {

        if (portComm != null) {
            portComm.closePort();
        }
        return 0;

    }

    public List<String> getListPorts() {

        List<String> portNames = new ArrayList<>();
        ports = SerialPort.getCommPorts();
        for (SerialPort p : ports) {

            portNames.add(p.getSystemPortName());
        }

        return portNames;

    }

    public int envoyerData(String dataToSend) {

        outputStream = portComm.getOutputStream();

        try {

            //    System.out.println("Interface.envoyerData(), données: " + dataToSend);
            outputStream.write(dataToSend.getBytes());
            return 1;

        } catch (IOException e) {

            return -1;

        }

    }

    public String getInputLine() {
        return inputLine;
    }

    public void setInputLine(String inputLine) {
        this.inputLine = inputLine;
    }

    public void notifierResultat() {

        this.setChanged();
        this.notifyObservers(this.getInputLine());

    }

    void resetTestBoard() {

    }

    void flushBuffer() {

        portComm.flushIOBuffers();
    }

    public void programmationCompleted(Integer operation) {

        this.setChanged();
        this.notifyObservers(operation);

    }

    public void erase(boolean envVariable, String programmerLocation) {

        envoyerData(Constants.ERASE);
        tempo(1000);

        Runtime runtime = Runtime.getRuntime();

        try {

            if (envVariable) {

                //Process process = runtime.exec("STM32_Programmer_CLI.exe -c port=SWD sn=002800323532511431333430 -e all");
                Process process = runtime.exec("STM32_Programmer_CLI.exe -c port=SWD -e all");
            } else {

            }

        } catch (IOException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }

        tempo(5000);

        programmationCompleted(Constants.ERASE_SUCCESS);
        envoyerData(Constants.END_ERASE);

    }

    void tempo(long duree) {

        try {

            Thread.sleep(duree);

        } catch (InterruptedException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int program(String hexLocation, boolean envVariable, String programmerPath, String programmer, String device, String binaryLocation, int nombreDeVoiesCarteEnTest) throws IOException {

        for (int i = 1; i < nombreDeVoiesCarteEnTest + 1; i++) {

            /*
            // Fonctionnel sans le fichier de log
            Runtime runtime = Runtime.getRuntime();
            String commande1 = "java -jar C:\\Users\\Michel\\mplab_platform\\mplab_ipe\\ipecmdboost.jar /TPICD4 /P16F1507 /Fc:\\Users\\Michel\\Desktop\\profil.hex /M /W /OY2013 >C:\\Users\\Michel\\Desktop\\logs.txt"; 
            Process programming = runtime.exec(commande1);
            tempo(5000);  // 5000-> valeur validée
            System.out.println("Fin programmation");
            /*
            // test fonctionnel
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("notepad.exe C:\\Users\\Michel\\Desktop\\bonjour.txt");
             */
            //boolean deleteIfExists1 = Files.deleteIfExists(Paths.get("C:\\Users\\Michel\\.mchp_ipe\\2013.ini"));
            //boolean deleteIfExists2 = Files.deleteIfExists(Paths.get("C:\\Users\\Michel\\.mchp_ipe\\2013.lock"));
            //tempo(500);
            System.out.println("Début programmation");
            cleanDirectory();
            //cleanDirectory("C:\\Users\\Michel\\Desktop\\logs\\logs.txt");
            cleanDirectory(".\\logs\\logs.txt");
            tempo(250);

            // Fonctionnelle
            ProcessBuilder processBuilder = new ProcessBuilder();
            //processBuilder.command("C:\\Users\\Michel\\Desktop\\test.bat");
            //processBuilder.command("cmd.exe", "/c", "java -jar C:\\Users\\Michel\\mplab_platform\\mplab_ipe\\ipecmdboost.jar /TPICD4 /P16F1507 /Fc:\\Users\\Michel\\Desktop\\profil.hex /M /W /OY2013 >C:\\Users\\Michel\\Desktop\\logs.txt");
            // processBuilder.command("cmd.exe", "/c", "java -jar C:\\Users\\Michel\\mplab_platform\\mplab_ipe\\ipecmdboost.jar /TPICD4 /P16F1507 /Fc:\\Users\\Michel\\Desktop\\profil.hex /M /W /OY2013 >.\\logs\\logs.txt");
            //processBuilder.command("cmd.exe", "/c", "java -jar " + programmerPath + " /TPICD4 /P16F1507 /Fc:\\Users\\Michel\\Desktop\\profil.hex /M /W /OY2013 >.\\logs\\logs.txt");
            //processBuilder.command("cmd.exe", "/c", "java -jar " + programmerPath + " /" + programmer + " /" + device + " /Fc:\\Users\\Michel\\Desktop\\profil.hex /M /W /OY2013 >.\\logs\\logs.txt");

            processBuilder.command("cmd.exe", "/c", "java -jar " + programmerPath + " /" + programmer + " /" + device + " /F" + binaryLocation + " /M /W /OY2013 >.\\logs\\logs.txt");
            // processBuilder.command("cmd.exe", "/c", "java -jar " + programmerPath + " /" + programmer + " /" + device + " /F" + binaryLocation + " /M /W /OY2013 >C:\\Users\\Michel\\Desktop\\logs\\logs.txt");

            Process process = processBuilder.start();

            tempo(200);  // 5000-> valeur validée
            System.out.println("Fin programmation");
            System.out.println("Début vérification");

            int control = progController.find(".\\logs\\logs.txt", Constants.ERREURS_LOG1, Constants.REQUIS_LOG1);
            System.out.println("code controle: " + control);
            programmationCompleted(i);
        }
        return 1;

        /*
            if (control == 1) {

                programmationCompleted(Constants.PROG_SUCCESS_ETAPE1);
                System.out.println("programmation terminée avec succès");
                return 1;
            } else {

                programmationCompleted(Constants.PROG_UNSUCCESS_ETAPE1);
                System.out.println("retour code erreur programmation");
                return -2;

            }

         */
    }

    public void cleanDirectory() throws IOException {

        boolean deleteIfExists1 = Files.deleteIfExists(Paths.get("C:\\Users\\Michel\\.mchp_ipe\\2013.ini"));
        boolean deleteIfExists2 = Files.deleteIfExists(Paths.get("C:\\Users\\Michel\\.mchp_ipe\\2013.lock"));

    }

    public void cleanDirectory(String logFile) {

        System.out.println("Suppression fichier de log");
        Path path = Paths.get(logFile);
        try {
            boolean deleteIfExists1 = Files.deleteIfExists(path);
        } catch (IOException ex) {

            System.out.println("Problème suppression fichier de log");
        }

    }

    void getFileSize(String logFile) {

        double size0 = 0;
        double size1 = 0;
        Boolean end = false;
        int counter = 0;
        int lim = 1000;

        while (!end) {

            File file = new File(logFile);
            size1 = (double) file.length();
            System.out.println(size1 / 1024 + "  kb");
            size0 = size1;
            if (size0 == size1 && size1 != 0) {

                counter++;
                if (counter > lim) {
                    end = true;

                }
            }
        }

        System.out.println("Mesure terminée!");

    }

}
