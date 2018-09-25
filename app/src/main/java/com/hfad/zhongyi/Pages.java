package com.hfad.zhongyi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.provider.Settings;
import android.util.Log;

class Page {
    private String description;
    HashMap<Integer, String> id2symptom = new HashMap<>();
    HashMap<String, Integer> symptom2id = new HashMap<>();
    HashSet<Integer> chosen = new HashSet<>();
    HashSet<String> symptoms = new HashSet<>();
    ArrayList<String> otherSymptoms = new ArrayList<String>();

    private void getSymptomsFromFile(String filename) {
        AssetManager mngr = MyApplication.getContext().getAssets();
        try {
            InputStream fstream =  mngr.open(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String line;
            int id = 0;
            while ((line = br.readLine()) != null) {
                // process the line.
                String s = line.split("\t")[0];
                // System.out.println(s);
                symptoms.add(s);
                id2symptom.put(id, s);
                symptom2id.put(s, id);
                if (id >= 6) {
                    otherSymptoms.add(s);
                }
                id++;
            }
            br.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    Page(String des){
        getSymptomsFromFile(des);
    }

    public HashSet<String> getSymptoms() {
        return this.symptoms;
    }

    public String getDescription() {
        return this.description;
    }

    public HashSet<Integer> getChosen() {
        return this.chosen;
    }

    public HashMap<Integer, String> getId2symptom() {
        return this.id2symptom;
    }

    public HashMap<String, Integer> getSymptom2id() {
        return this.symptom2id;
    }

    public void setChosen(Integer id){
        if(this.chosen.contains(id)){
            this.chosen.remove(id);
        }
        else{
            this.chosen.add(id);
        }
    }

    public ArrayList<String> getOtherSymptoms() {
        return this.otherSymptoms;
    }
}

class Pages {
    public static Page[] pages = {
            new Page("headAndface"),
            new Page("chest"),
            new Page("abdomen"),
            new Page("groin"),
            new Page("reproduction"),
            new Page("limbs"),
            new Page("body")
    };
}
