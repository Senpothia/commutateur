/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keypadprogrammer;

import java.io.IOException;

/**
 *
 * @author Michel
 */
public class ProcessManager {
    
    
    private ProcessBuilder processBuilder = new ProcessBuilder();

    public ProcessManager() {
    }

    public ProcessBuilder getProcessBuilder() {
        return processBuilder;
    }

    public void setProcessBuilder(ProcessBuilder processBuilder) {
        this.processBuilder = processBuilder;
    }

    public void getProcessByName(String name, String fileName) throws IOException {

        processBuilder.command("cmd.exe", "/c", "Get-Process -ProcessName java >.\\get2.txt");
        Process process = processBuilder.start();

    }

}
