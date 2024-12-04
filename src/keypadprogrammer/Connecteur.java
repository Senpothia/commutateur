/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keypadprogrammer;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
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

    public int program(String hexLocation, String bleLocation, boolean envVariable, String programmerLocation) throws IOException {

        System.out.println("tranmission ordre relais 8");
        int com = envoyerData(Constants.PROG);
        programmationCompleted(Constants.PROG_START);
        if (com == -1) {

            return -1;
        }
        tempo(1000);
        System.out.println("Fin tempo 1s");

        if (envVariable) {

            Runtime runtime = Runtime.getRuntime();

            //  STARTFUS
            String commande1 = "STM32_Programmer_CLI.exe -c port=SWD sn=003900213532511231333430 s-startFUS -log .\\logs\\trace1.log";  // module 2
            //String commande1 = "STM32_Programmer_CLI.exe -c port=SWD sn=0035003C3532511131333430 s-startFUS -log .\\logs\\trace1.log"; // module 1
            Process startFUS = runtime.exec(commande1);
            tempo(7000);  // 5000-> valeur validée
            System.out.println("Fin startFUS");

            int control1 = progController.find(".\\logs\\trace1.log", Constants.ERREURS_LOG1, null);
            System.out.println("code controle 1: " + control1);

            if (control1 == 1 || control1 == 0) {

                programmationCompleted(Constants.PROG_SUCCESS_ETAPE1);

            } else {

                programmationCompleted(Constants.PROG_UNSUCCESS_ETAPE1);
                System.out.println("retour code erreur etape 1");
                return -2;

            }

            // UPDATE
            //programmationCompleted(Constants.PROG_SUCCESS_ETAPE1);
            //String commande2 = "STM32_Programmer_CLI.exe -c port=SWD -startFUS mode=UR -ob nSWboot0=0 nboot1=1 nboot0=1 -fwupgrade " + bleLocation + " 0x080CE000 firstinstall=0 -log .\\logs\\trace2.log";  // version avant 07/05/2024
            String commande2 = "STM32_Programmer_CLI.exe -c port=SWD sn=003900213532511231333430 -startFUS mode=UR -ob nSWboot0=0 nboot1=1 nboot0=1 -fwupgrade " + bleLocation + " 0x080CE000 firstinstall=1 -log .\\logs\\trace2.log";    // module 2 // version après 07/05/2024
            //String commande2 = "STM32_Programmer_CLI.exe -c port=SWD sn=0035003C3532511131333430 -startFUS mode=UR -ob nSWboot0=0 nboot1=1 nboot0=1 -fwupgrade " + bleLocation + " 0x080CE000 firstinstall=1 -log .\\logs\\trace2.log";  // module 1 // version après 07/05/2024
            Process upgradeBLE = runtime.exec(commande2);
            tempo(35000);  // 40000-> valeur validée
            System.out.println("Fin updateBLE");

            int control2 = progController.find(".\\logs\\trace2.log", null, Constants.REQUIS_LOG2);
            System.out.println("codeControl 2: " + control2);

            if (control2 == 1) {

                programmationCompleted(Constants.PROG_SUCCESS_ETAPE2);

            } else {

                return -2;

            }

            // STARTSTACK
            String commande3 = "STM32_Programmer_CLI.exe -c port=SWD sn=003900213532511231333430 -startwirelessstack -log .\\logs\\trace3.log";  // module 2
            //String commande3 = "STM32_Programmer_CLI.exe -c port=SWD sn=0035003C3532511131333430 -startwirelessstack -log .\\logs\\trace3.log"; // module 1
            Process startStack = runtime.exec(commande3);
            tempo(3000); // 5000-> valeur validée
            System.out.println("Fin startStack");

            int control3 = progController.find(".\\logs\\trace3.log", Constants.ERREURS_LOG3, Constants.REQUIS_LOG3);
            System.out.println("codeControl 3: " + control3);
            if (control3 == 1) {

                programmationCompleted(Constants.PROG_SUCCESS_ETAPE3);

            } else {

                return -2;

            }

            //
            // FIRMWARE
            String commande4 = "STM32_Programmer_CLI.exe -c port=SWD sn=003900213532511231333430 -w " + hexLocation + " 0x080CE000 -Rst -log .\\logs\\trace4.log"; // module 2
            //String commande4 = "STM32_Programmer_CLI.exe -c port=SWD sn=0035003C3532511131333430 -w " + hexLocation + " 0x080CE000 -Rst -log .\\logs\\trace4.log"; // module 1
            Process programFirmware = runtime.exec(commande4);
            System.out.println("Fin programmation firmware");
            envoyerData(Constants.RESET_MODULE_BLE);
            tempo(5000);

            int control4 = progController.find(".\\logs\\trace4.log", null, Constants.REQUIS_LOG4);
            System.out.println("codeControl 4: " + control4);

            if (control4 == 1) {

                programmationCompleted(Constants.PROG_SUCCESS_ETAPE4);

            } else {

                return -2;

            }

        } else {

        }

        tempo(1000); // 5000 -> valeur validée
        programmationCompleted(Constants.PROG_SUCCESS);
        envoyerData(Constants.END_PROG);
        return 1;

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

}
