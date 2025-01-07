/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keypadprogrammer;

import java.util.ArrayList;
import javax.swing.JLabel;

public class Ligne {

    private ArrayList<Voyant> voyants;
    private boolean visible;

    public Ligne(ArrayList<Voyant> voyants, Boolean visible) {

        this.voyants = voyants;
        this.visible = visible;
    }

    public Ligne(ArrayList<JLabel> indicateurs, boolean visible) {

        ArrayList<Voyant> listeVoyants = new ArrayList<Voyant>();
        for (JLabel i : indicateurs) {

            Voyant v = new Voyant(i, 0);
            listeVoyants.add(v);
        }
        this.voyants = listeVoyants;

        this.visible = visible;

    }

    Ligne() {

    }

    public void supprimer() {

        for (Voyant v : voyants) {

            v.supprimer();
        }

    }

    public ArrayList<Voyant> getVoyants() {
        return voyants;
    }

    public void setVoyants(ArrayList<Voyant> voyants) {
        this.voyants = voyants;
    }

    public void initialiser(int colonnes) {

        int position = (12 - colonnes) / 2;
        for (Voyant v : voyants) {

            if (voyants.indexOf(v) > position && voyants.indexOf(v) < position + colonnes + 1) {
                v.processing(false);
            } else {

                v.masquer();
            }

        }

    }

    public void afficher() {

        for (Voyant v : voyants) {
            v.processing(false);

        }

    }
}
