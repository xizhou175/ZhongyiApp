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

    Page(String des){


        AssetManager mngr = MyApplication.getContext().getAssets();


        if(des.equals("head")) {

            ArrayList<String> headSymptoms = new ArrayList<String>();

            try {
                InputStream fstream =  mngr.open("headAndface");
                BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
                String line = "";
                while ((line = br.readLine()) != null) {
                    // process the line.
                    String s = line.split("\t")[0];
                    System.out.println(s);
                    headSymptoms.add(s);
                }
                br.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }

            for(int i = 0; i < headSymptoms.size(); i++){
                symptoms.add(headSymptoms.get(i));
            }
            int id = 1;
            for(String key : headSymptoms){
                id2symptom.put(id, key);
                symptom2id.put(key, id);
                if(id++ == 6) break;
            }
            for(id = 7; id <= headSymptoms.size(); id++){
                id2symptom.put(id, headSymptoms.get(id - 1));
                symptom2id.put(headSymptoms.get(id - 1), id);
                otherSymptoms.add(headSymptoms.get(id - 1));
            }
        }

        else if(des.equals("chest")) {

            ArrayList<String> chestSymptoms = new ArrayList<String>();

            try {
                InputStream fstream =  mngr.open("chest");
                BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
                String line = "";
                while ((line = br.readLine()) != null) {
                    // process the line.
                    String s = line.split("\t")[0];
                    chestSymptoms.add(s);
                }
                br.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }

            for(int i = 0; i < chestSymptoms.size(); i++){
                symptoms.add(chestSymptoms.get(i));
            }
            int id = 1;
            for(String key : chestSymptoms){
                id2symptom.put(id, key);
                symptom2id.put(key, id);
                if(id++ == 6) break;
            }
            for(id = 7; id <= chestSymptoms.size(); id++){
                id2symptom.put(id, chestSymptoms.get(id - 1));
                symptom2id.put(chestSymptoms.get(id - 1), id);
                otherSymptoms.add(chestSymptoms.get(id - 1));
            }

        }
        else if(des.equals("back")) {
            description = "back symptoms";
            String[] backSymptoms = {"背1", "背2", "背3", "背4", "背5", "背6"};
            for(int i = 0; i < backSymptoms.length; i++){
                symptoms.add(backSymptoms[i]);
            }
            int id = 1;
            for(String key : backSymptoms){
                id2symptom.put(id, key);
                symptom2id.put(key, id);
                id += 1;
            }
        }
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
        new Page("head"),
        new Page("chest"),
        new Page("back"),
        new Page("abdomen"),
    };
}
