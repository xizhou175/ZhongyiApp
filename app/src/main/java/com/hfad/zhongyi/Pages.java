package com.hfad.zhongyi;

import java.util.HashMap;
import java.util.HashSet;

class Page {
    private String description;
    HashMap<Integer, String> id2symptom = new HashMap<>();
    HashMap<String, Integer> symptom2id = new HashMap<>();
    HashSet<Integer> chosen = new HashSet<Integer>();

    Page(String des, int numOfsym){

        if(des.equals("head")){
            String[] symptoms = {"头痛", "头扁", "头胀", "头", "头晕", "头硬"};
            int id = R.id.head_s1;
            for(int i = 0; i < numOfsym; i++){
                id2symptom.put(id, symptoms[i]);
                symptom2id.put(symptoms[i], id);
                id += 1;
            }
        }
    }

    public String getDescription(){
        return this.description;
    }

    public HashSet<Integer> getChosen(){
        return this.chosen;
    }

    public HashMap<Integer, String> getId2symptom() {
        return this.id2symptom;
    }

    public HashMap<String, Integer> getSymptom2id(){
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
        new Page("head", 6)
    };

}
