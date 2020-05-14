package it.uniba.di.sms1920.everit.restaurateur.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> cricket = new ArrayList<String>();
        cricket.add("Margherita");
        cricket.add("Diavola");
        cricket.add("4 stagioni");


        List<String> football = new ArrayList<String>();
        football.add("Coca Cola");
        football.add("Sprite");

        List<String> basketball = new ArrayList<String>();
        basketball.add("Babà");
        basketball.add("Frappè");

        expandableListDetail.put("PIZZE", cricket);
        expandableListDetail.put("BIBITE", football);
        expandableListDetail.put("DOLCI", basketball);
        return expandableListDetail;
    }
}
