/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keypadprogrammer;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michel
 */
public class Constants {

    static String RESET_HARDWARE = "0";
    static String START = "1";
    static String AQC = "4";
    static String OK = "2";
    //static String KO = "3";
    //static String PROG = "7";
    static String ERASE = "8";
    //static String END_PROG = "9";
    static String END_ERASE = "5";
    static String ERR_PROG = "6";
   // static String RESET_MODULE_BLE = "z";

    static String RESPONSE = "@:ACQ";
    static String FIN = "@:END";
    static String ERREUR = "@:ERROR:";
    static String INTERROGATION = "@INTER:";
    static String CONFORME = "@:OK";
    static String DEFAUT = "@:KO";

    static String LOG_DIRECTORY = ".//logs";

    // Signalisation état de programmation 
    static Integer PROG_SUCCESS = 10;
    static Integer PROG_START = 77;

    static Integer PROG_SUCCESS_ETAPE1 = 11;
   
    static Integer PROG_UNSUCCESS_ETAPE1 = 91;
   
    static Integer ERASE_SUCCESS = 50;
    static Integer ERASE_UNSUCCESS = 55;

    // Signalisation résultats des étapes de test
    //********************************************************************************************************
    // ERREURS TRACE1
    static String E1_LOG1 = "Programmer not found.";
    static String E2_LOG1 = "terminated abruptly.";

    static String[] ERREURS_LOG1 = {E1_LOG1, E2_LOG1};

    // REQUIS TRACE1
    static String R1_LOG1 = "Program Succeeded.";
   
    static String[] REQUIS_LOG1 = {R1_LOG1};
   
    //********************************************************************************************************
    static void tempo(long duree) {

        try {

            Thread.sleep(duree);

        } catch (InterruptedException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
