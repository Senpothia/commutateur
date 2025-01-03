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
    private String binaryLocations;
    private String productNames;
    private String nombreVoies;
    private String commutateur;
    private String device;
    private String programmer;

    public Initialisation(String programmerDirectory, String varEnv, String binaryLocations, String productNames, String nombreVoies, String commutateur, String device, String programmer) {
        this.programmerDirectory = programmerDirectory;
        this.varEnv = varEnv;
        this.binaryLocations = binaryLocations;
        this.productNames = productNames;
        this.nombreVoies = nombreVoies;
        this.commutateur = commutateur;
        this.device = device;
        this.programmer = programmer;
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

    public String getBinaryLocations() {
        return binaryLocations;
    }

    public void setBinaryLocations(String binaryLocations) {
        this.binaryLocations = binaryLocations;
    }

    public String getProductNames() {
        return productNames;
    }

    public void setProductNames(String productNames) {
        this.productNames = productNames;
    }

    public String getNombreVoies() {
        return nombreVoies;
    }

    public void setNombreVoies(String nombreVoies) {
        this.nombreVoies = nombreVoies;
    }

    public String getCommutateur() {
        return commutateur;
    }

    public void setCommutateur(String commutateur) {
        this.commutateur = commutateur;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getProgrammer() {
        return programmer;
    }

    public void setProgrammer(String programmer) {
        this.programmer = programmer;
    }

    

}
