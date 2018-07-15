package com.hfad.zhongyi;

import java.util.HashMap;
import java.util.HashSet;

class Page {
    private String description;
    HashMap<Integer, String> id2symptom = new HashMap<>();
    HashMap<String, Integer> symptom2id = new HashMap<>();
    HashSet<Integer> chosen = new HashSet<>();
    HashSet<String> symptoms = new HashSet<>();

    Page(String des){

        if(des.equals("head")) {
            String[] headSymptoms = {"头1", "头2", "头3", "头4", "头5"};
            description = "head symptoms";
            for(int i = 0; i < headSymptoms.length; i++){
                symptoms.add(headSymptoms[i]);
            }
            int id = 1;
            for(String key : headSymptoms){
                id2symptom.put(id, key);
                symptom2id.put(key, id);
                id += 1;
            }
        }

        else if(des.equals("chest")) {
            String[] chestSymptoms = {"胸1", "胸2", "胸3", "胸4", "胸5", "胸6", "胸7"};
            for(int i = 0; i < chestSymptoms.length; i++){
                symptoms.add(chestSymptoms[i]);
            }
            int id = 1;
            for(String key : chestSymptoms){
                id2symptom.put(id, key);
                symptom2id.put(key, id);
                id += 1;
            }
        }
        else if(des.equals("back")) {
            description = "back symptoms";
            String[] backSymptoms = {"背1", "背2", "背3", "背4"};
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
}

class Pages {
    public static Page[] pages = {
        new Page("head"),
        new Page("chest"),
        new Page("back")
    };
}
