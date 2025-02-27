/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keypadprogrammer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author Michel
 */
public class ProcessManager {

    private ProcessBuilder processBuilder = new ProcessBuilder();
    private boolean listed = false;
    private File outputFile0 = new File(".\\processes0.process");
    private String processId = "0";

    public boolean isListed() {
        return listed;
    }

    public void setListed(boolean listed) {
        this.listed = listed;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }
    
    

    public void getJavaProcesses() throws IOException {

        // Définir la commande PowerShell
        //String[] command = {"powershell.exe", "-Command", "Get-Process -ProcessName java"};
        String[] command = {"powershell.exe", "-Command", "Get-Process -ProcessName java | Format-List *"};

        // Définir le fichier 0 de sortie
        //File outputFile0 = new File(".\\processes0.process");
        // Définir le fichier 1 de sortie
        //File outputFile1 = new File(".\\processes1.process");
        // Créer un ProcessBuilder
        if (!listed) {

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectOutput(outputFile0);
            // Rediriger la sortie vers le fichier

            try {
                // Démarrer le processus
                Process process = processBuilder.start();

                // Attendre la fin du processus
                process.waitFor();
                System.out.println("La sortie a été redirigée vers " + outputFile0.getAbsolutePath());

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            String[] command2 = {"powershell.exe", "exit"};
            processBuilder = new ProcessBuilder(command2);
            extractProcessId();

        }

    }

    public void deleteFiles() throws IOException {

        Files.deleteIfExists(Paths.get(".\\processes0.process"));
        resetFlags();
    }

    public void extractProcessId() {

        String iDline = "";
        try {

            //System.out.println("le fichier est disponible");
            // Création d'un fileReader pour lire le fichier
            FileReader fileReader = new FileReader(".\\processes0.process");

            // Création d'un bufferedReader qui utilise le fileReader
            BufferedReader reader = new BufferedReader(fileReader);
            String line = reader.readLine();

            boolean lineIdFound = false;

            while (line != null && !lineIdFound) {

                // lecture de la prochaine ligne
                //System.out.println(line);
                if (line.contains("Id") && !line.contains("SessionId")) {

                    System.out.println(line);
                    iDline = line;
                }

                if (line.contains("Zulu Platform x64 Architecture")) {

                    System.out.println(line);
                    lineIdFound = true;
                }

                line = reader.readLine();

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            //System.out.println("Fichier en cours d'écriture");
        }

        listed = true;
        System.out.println("line Id: " + iDline);
        int index = iDline.indexOf(':');
        processId = iDline.substring(index + 2);
        System.out.println("Id: " + processId);

    }

    public void killProcess() throws IOException, InterruptedException {

        String[] command = {"powershell.exe", "-Command", "Stop-Process -Id " + processId};
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        process.waitFor();
        String[] command2 = {"powershell.exe", "exit"};
        processBuilder = new ProcessBuilder(command2);
        Files.deleteIfExists(Paths.get(".\\processes0.process"));
        resetFlags();

    }

    public void resetFlags() {

        listed = false;
        processId = "0";
    }

}
