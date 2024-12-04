/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keypadprogrammer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Michel
 */
public class Initializer {

    public Initialisation getInit() throws FileNotFoundException, IOException {

        Properties progProperpies = new Properties();
        FileReader reader = new FileReader(".\\params.properties");
        progProperpies.load(reader);

        String programmerDirectory = progProperpies.getProperty("programmerDirectory");
        String binaryLocations = progProperpies.getProperty("binaryLocations");
        String varEnv = progProperpies.getProperty("varEnv");
        String productNames = progProperpies.getProperty("productNames");
        
        Initialisation init = new Initialisation(programmerDirectory, varEnv, binaryLocations, productNames);

        return init;
    }

    public void update(String key, String value) {

        try {

            Properties progProperpies = new Properties();
            FileInputStream configStream = new FileInputStream(".\\params.properties");
            progProperpies.load(configStream);
            configStream.close();

            //modifies existing or adds new property
            progProperpies.setProperty(key, value);

            FileOutputStream output = new FileOutputStream(".\\params.properties");
            progProperpies.store(output, "GALEO TESTER - Properties");
            output.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
