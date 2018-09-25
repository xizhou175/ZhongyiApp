package com.hfad.zhongyi;

import java.util.ArrayList;

public class PersonalInfo {
    private String gender = "male";
    private Integer age;
    private String name;
    private String password;
    private String id;
    private ArrayList<String> symptoms = new ArrayList<String>();

    private int heartRate;


    public void setGender(String gender){
        this.gender = gender;
    }

    public String getGender(){
        return this.gender;
    }

    public void setAge(Integer age){
        this.age = age;
    }

    public Integer getAge(){
        return this.age;
    }

    public void setName(String name){ this.name = name; }

    public String getName() { return this.name; }

    public void setPassword(String password){ this.password = password; }

    public String getPassword() { return password; }

    public  String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int rate) {
        this.heartRate = rate;
    }

    public void addSymptom(String symptom) { symptoms.add(symptom); }

    public void removeSymptom(String symptom) { symptoms.remove(symptom); }

    public ArrayList<String> getSymptoms() { return this.symptoms; }

    @Override
    public String toString() {
        return String.join(" ", name, gender, id);
    }

}

class Patient{
    public static PersonalInfo personalInfo = new PersonalInfo();
}
