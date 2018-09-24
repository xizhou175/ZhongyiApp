package com.hfad.zhongyi;

public class DataModel {
    private String symptom;
    private boolean selected;

    public DataModel(String symptom, boolean selected){
        this.symptom = symptom;
        this.selected = selected;
    }

    public String getSymptom() {
        return this.symptom;
    }

    public void setSymptom(String s) {
        this.symptom = s;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean select) {
        selected = select;
    }
}
