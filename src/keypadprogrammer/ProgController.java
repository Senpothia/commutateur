/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keypadprogrammer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michel
 */
public class ProgController {

    public int createLogFolder(String path) {

        try {
            Path location = Paths.get(path);

            //java.nio.file.Files;
            Files.createDirectories(location);
            return 1;

        } catch (IOException ex) {
            Logger.getLogger(ProgController.class.getName()).log(Level.SEVERE, null, ex);
            return 0;

        }

    }

    public int find(String logfile, String[] erreurs, String[] requis) {

        int codeControl = 0;
        /*
        for (int i = 0; i < erreurs.length; i++) {

                System.out.println(erreurs[i]);
            }
         */
        try {
            // Création d'un fileReader pour lire le fichier
            FileReader fileReader = new FileReader(logfile);

            // Création d'un bufferedReader qui utilise le fileReader
            BufferedReader reader = new BufferedReader(fileReader);

            // une fonction à essayer pouvant générer une erreur
            String line = reader.readLine();

            while (line != null) {

                // lecture de la prochaine ligne
                line = reader.readLine();
                //System.out.println(line);

                if (erreurs != null && line != null) {

                    for (int i = 0; i < erreurs.length; i++) {
                       // System.out.println("controle des erreurs  -  " + erreurs[i]);
                        if (line.contains(erreurs[i])) {

                            codeControl = -1;
                        }
                    }

                }

                if (requis != null && line != null) {

                    for (int i = 0; i < requis.length; i++) {
                        //System.out.println("controle des requis  -  " + requis[i]);
                        if (line.contains(requis[i])) {

                            codeControl = 1;
                        }
                    }

                }

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return codeControl;
    }

}
