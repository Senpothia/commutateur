/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keypadprogrammer;

import java.awt.Color;
import javax.swing.JLabel;

public class Voyant {

    private JLabel indicateur;
    private int statut;  // ; 0=masqué; 1=attente; 2=processing; 3=ok; 4=nok

    public Voyant(JLabel indicateur, int statut) {

        this.indicateur = indicateur;
        this.statut = statut;
    }

    public JLabel getIndicateur() {
        return indicateur;
    }

    public void setIndicateur(JLabel indicateur) {
        this.indicateur = indicateur;
    }

    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }

    public void masquer() {

        indicateur.setBackground(new Color(50, 131, 168));
        indicateur.setForeground(new Color(50, 131, 168));
        indicateur.setOpaque(true);
        statut = 0;
    }

    public void ok(Boolean ok) {

        if (!ok) {

            indicateur.setBackground(Color.RED);
            indicateur.setForeground(Color.RED);
            statut = 3;
        } else {

            indicateur.setBackground(Color.GREEN);
            indicateur.setForeground(Color.GREEN);
            statut = 4;
        }

        indicateur.setOpaque(true);
    }

    public void processing(Boolean processing) {

        if (!processing) {

            indicateur.setBackground(Color.GRAY);
            indicateur.setForeground(Color.GRAY);
            statut = 1;

        } else {

            indicateur.setBackground(Color.YELLOW);
            indicateur.setForeground(Color.YELLOW);
            statut = 2;
        }
        indicateur.setOpaque(true);
    }

    void supprimer() {

        indicateur.setVisible(false);
    }

}
