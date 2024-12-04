/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keypadprogrammer;

/**
 *
 * @author Michel
 */
public class Initialisation {

    private String programmerDirectory;
    private String varEnv;
    private String binaryLocation;
    private String bleLocation;

    public Initialisation(String programmerDirectory, String varEnv, String binaryLocation, String bleLocation) {

        this.programmerDirectory = programmerDirectory;
        this.varEnv = varEnv;
        this.binaryLocation = binaryLocation;
        this.bleLocation = bleLocation;
    }

    public String getProgrammerDirectory() {
        return programmerDirectory;
    }

    public void setProgrammerDirectory(String programmerDirectory) {
        this.programmerDirectory = programmerDirectory;
    }

    public String getVarEnv() {
        return varEnv;
    }

    public void setVarEnv(String varEnv) {
        this.varEnv = varEnv;
    }

    public String getBinaryLocation() {
        return binaryLocation;
    }

    public void setBinaryLocation(String binaryLocation) {
        this.binaryLocation = binaryLocation;
    }

    public String getBleLocation() {
        return bleLocation;
    }

    public void setBleLocation(String bleLocation) {
        this.bleLocation = bleLocation;
    }

}
