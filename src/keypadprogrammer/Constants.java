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
    static String KO = "3";
    static String PROG = "7";
    static String ERASE = "8";
    static String END_PROG = "9";
    static String END_ERASE = "5";
    static String ERR_PROG = "6";
    static String RESET_MODULE_BLE = "z";

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
    static Integer PROG_SUCCESS_ETAPE2 = 12;
    static Integer PROG_SUCCESS_ETAPE3 = 13;
    static Integer PROG_SUCCESS_ETAPE4 = 14;

    static Integer PROG_UNSUCCESS_ETAPE1 = 91;
    static Integer PROG_UNSUCCESS_ETAPE2 = 92;
    static Integer PROG_UNSUCCESS_ETAPE3 = 93;
    static Integer PROG_UNSUCCESS_ETAPE4 = 94;

    static Integer ERASE_SUCCESS = 50;
    static Integer ERASE_UNSUCCESS = 55;

    // Signalisation résultats des étapes de test
    //********************************************************************************************************
    // ERREURS TRACE1
    static String E1_LOG1 = "Error: No STM32 target found! ";
    static String E2_LOG1 = "Error: FUS_STATE_ERR_UNKNOWN: Unknown error";
    static String E3_LOG1 = "Error: Fus is not yet running, try again";

    static String[] ERREURS_LOG1 = {E1_LOG1};

    // REQUIS TRACE1
    //********************************************************************************************************
    // ERREURS TRACE2
    // REQUIS TRACE2
    static String R1_LOG2 = "Success with Time elapsed during Reconnect:";
    static String R2_LOG2 = "Firmware Upgrade Success";

    static String[] REQUIS_LOG2 = {R1_LOG2, R2_LOG2};

    //********************************************************************************************************
    // ERREURS TRACE3
    static String E1_LOG3 = "Error: FUS_STATE_ERR_UNKNOWN";

    static String[] ERREURS_LOG3 = {E1_LOG3};

    // REQUIS TRACE3
    static String R1_LOG3 = "Success with Time elapsed during Reconnect:";
    static String R2_LOG3 = "FusStartWS activated successfully";
    static String R3_LOG3 = "startwirelessStack command execution finished";

    static String[] REQUIS_LOG3 = {R1_LOG3, R2_LOG3, R3_LOG3};

    //********************************************************************************************************
    // ERREURS TRACE4
    // REQUIS TRACE4
    static String R1_LOG4 = "File download complete";
    static String R2_LOG4 = "Software reset is performed";

    static String[] REQUIS_LOG4 = {R1_LOG4, R2_LOG4};

    //********************************************************************************************************
    static void tempo(long duree) {

        try {

            Thread.sleep(duree);

        } catch (InterruptedException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
