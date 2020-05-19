package it.uniba.di.sms1920.everit.restaurateur.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {

    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> pizze = new ArrayList<String>();
        pizze.add("Margherita");
        pizze.add("Diavola");
        pizze.add("4 stagioni");


        List<String> bevande = new ArrayList<String>();
        bevande.add("Coca Cola");
        bevande.add("Sprite");

        List<String> dolci = new ArrayList<String>();
        dolci.add("Babà");
        dolci.add("Frappè");

        expandableListDetail.put("PIZZE", pizze);
        expandableListDetail.put("BIBITE", bevande);
        expandableListDetail.put("DOLCI", dolci);
        return expandableListDetail;
    }



}
