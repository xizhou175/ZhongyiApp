package com.hfad.zhongyi;

import java.util.HashMap;
import java.util.HashSet;

class Page {
    private String description;
    HashMap<Integer, String> id2symptom = new HashMap<>();
    HashSet<Integer> chosen = new HashSet<>();

    Page(String des){

        if(des.equals("head")){
            String[] symptoms = {"头痛", "头扁", "头胀", "头晕", "头", "头硬"};
            int id = R.id.head_s1;
            for(int i = 0; i < symptoms.length; i++){
                id2symptom.put(id, symptoms[i]);
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
        new Page("head")
    };

}
