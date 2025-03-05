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
    private int sequenceInterrompue = 1;

    private OutputStream outputStream;

    private String inputLine;

    private boolean echo = false;

    private ProcessBuilder processBuilder = new ProcessBuilder();

    private ProcessManager processManager = new ProcessManager();

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

    public int getSequenceInterrompue() {
        return sequenceInterrompue;
    }

    public void setSequenceInterrompue(int sequenceInterrompue) {
        this.sequenceInterrompue = sequenceInterrompue;
    }

    public boolean isEcho() {
        return echo;
    }

    public void setEcho(boolean echo) {
        this.echo = echo;
    }

    public int makeConnection(String portName, int baudeRate, int numDataBits, int parity, int stopBits) {

        try {

            if (portName == null) {

                // System.out.println("makeConnection() - Port non sélectionné");
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

                // System.out.println("Connexion réussie!");
                envoyerData(Constants.RESET_HARDWARE);
                // return 99;

            } else {

                // System.out.println("Connexion échouée!");
                return -1;
            }

        } catch (Exception e) {

            //System.out.println("Connexion échouée!");
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

                    //System.out.println("Received -> " + numRead + "bits lus - " + inputLine);
                    /*
                    if (inputLine.startsWith("->GR")) {
                        System.out.println("reception echo - connecteur");

                        String[] tab = inputLine.trim().split(":");

                        if (tab[2].equals("ON") || tab[2].equals("OFF")) {
                            echo = true;
                            System.out.println("echo = true - connecteur");
                        }
                    }
                     */
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
            if (!waitForEcho()) {

                return -9;
            }
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
        if (this.getInputLine().startsWith("->GR")) {
            System.out.println("reception echo - connecteur");

            String[] tab = this.getInputLine().trim().split(":");

            if (tab[2].equals("ON") || tab[2].equals("OFF")) {
                echo = true;
                System.out.println("echo = true - connecteur - notification");
            }
        }

    }

    void resetTestBoard() {

    }

    void flushBuffer() {

        portComm.flushIOBuffers();
    }

    public void programmationCompleted(String operation) {

        this.setChanged();
        this.notifyObservers(operation);

    }

    public void erase(boolean envVariable, String programmerLocation) {

    }

    void tempo(long duree) {

        try {

            Thread.sleep(duree);

        } catch (InterruptedException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int program(String hexLocation, boolean envVariable, String programmerPath, String programmer, String device, String binaryLocation, int nombreDeVoiesCarteEnTest, String programmerPathTempDir) throws IOException, InterruptedException {

        char count = 48;
        for (int j = 1; j < sequenceInterrompue; j++) {

            count++;
        }

        for (int i = sequenceInterrompue; i < nombreDeVoiesCarteEnTest + 1; i++) {

            count++;
            envoyerData(Character.toString(count));
            if (!lecturePort()) {

                System.out.println("->ECHO:DEFAULT");
                programmationCompleted("->ECHO:DEFAULT");
                //echo = false;
                return -9;
            } else {

                System.out.println("->ECHO:OK");

            }
            //tempo(10000);  // pour tests
            //System.out.println("Début programmation");
            cleanDirectory(programmerPathTempDir);
            cleanDirectory2(".\\logs\\logs.txt");
            tempo(250);
            programmationCompleted("->START:99:" + i);
            //ProcessBuilder processBuilder = new ProcessBuilder();
            //processBuilder.command("cmd.exe", "/c", "java -jar " + programmerPath + " /" + programmer + " /" + device + " /F" + binaryLocation + " /M /W /OY2013 >.\\logs\\logs.txt");
            processBuilder.command("cmd.exe", "/c", "java -jar " + programmerPath + " /" + programmer + " /" + device + " /F" + binaryLocation + " /M /OY2013 >.\\logs\\logs.txt");
            Process process = processBuilder.start();

            tempo(200);

            //System.out.println("Fin programmation");
            //System.out.println("Début vérification");
            int control = progController.find(".\\logs\\logs.txt", Constants.ERREURS_LOG1, Constants.REQUIS_LOG1);
            if (control == -1) {

                //System.out.println("tentative 2");
                programmationCompleted("->PROG:" + i + ":-54");
                processBuilder.command("cmd.exe", "/c", "java -jar " + programmerPath + " /" + programmer + " /" + device + " /F" + binaryLocation + " /M /W /OY2013 >.\\logs\\logs.txt");
                control = progController.find(".\\logs\\logs.txt", Constants.ERREURS_LOG1, Constants.REQUIS_LOG1);
                if (control == -1) {

                    control = -55;
                    programmationCompleted("->PROG:" + i + ":-55");
                    return 1;
                }

            }

            if (control == -4) {

                //System.out.println("tentative 2");
                programmationCompleted("->PROG:" + i + ":-54");
                processBuilder.command("cmd.exe", "/c", "java -jar " + programmerPath + " /" + programmer + " /" + device + " /F" + binaryLocation + " /M /W /OY2013 >.\\logs\\logs.txt");
                control = progController.find(".\\logs\\logs.txt", Constants.ERREURS_LOG1, Constants.REQUIS_LOG1);

                if (control == -4) {

                    control = -66;
                    programmationCompleted("->PROG:" + i + ":-66");
                    return 1;
                }
            }

            if (control == -33) {

                System.out.println("Interruption processus -  sequence: " + i);
                processManager.killProcess();
                sequenceInterrompue = i;
                programmationCompleted("->PROG:" + i + ":-33");
                return -33;

            }
            //System.out.println("code controle: " + control);
            programmationCompleted("->PROG:" + i + ":" + control);

            if (i == 1) {

                processManager.getJavaProcesses();
            }

        }

        sequenceInterrompue = 1;
        return 1;

    }

    public void cleanDirectory(String programmerPathTempDir) throws IOException {

        boolean deleteIfExists1 = Files.deleteIfExists(Paths.get(programmerPathTempDir + "2013.ini"));
        boolean deleteIfExists2 = Files.deleteIfExists(Paths.get(programmerPathTempDir + "2013.lock"));
        //boolean deleteIfExists1 = Files.deleteIfExists(Paths.get("C:\\Users\\Michel\\.mchp_ipe\\2013.ini"));
        //boolean deleteIfExists2 = Files.deleteIfExists(Paths.get("C:\\Users\\Michel\\.mchp_ipe\\2013.lock"));

    }

    public void cleanDirectory2(String logFile) {

        //System.out.println("Suppression fichier de log");
        Path path = Paths.get(logFile);
        try {
            boolean deleteIfExists1 = Files.deleteIfExists(path);
        } catch (IOException ex) {

            //System.out.println("Problème suppression fichier de log");
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

    private boolean waitForEcho() {

        int i = 0;
        while (!echo) {

            i++;
            if (i > 100000000) {
                System.out.println("hors delais");
                return false;
            }

        }

        return true;
    }

    private boolean lecturePort() {

        int k = 0;
        byte[] readBuffer = new byte[100];
        while (portComm.readBytes(readBuffer, readBuffer.length) == 0 && k < 1000) {

            k++;
            return false;

        }
        int numRead = portComm.readBytes(readBuffer, readBuffer.length);
        byte[] lecture = new byte[numRead];
        for (int i = 0; i < numRead; i++) {

            lecture[i] = readBuffer[i];
        }
        inputLine = new String(lecture, StandardCharsets.UTF_8);
        return true;

    }

}
