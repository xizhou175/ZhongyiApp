package com.hfad.zhongyi;

public class PersonalInfo {
    private String gender = "";
    private Integer age = 0;

    PersonalInfo(String gender, Integer age){
        this.gender = gender;
        this.age = age;
    }

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
}

class Patient{
    static PersonalInfo personalInfo = new PersonalInfo("", 0);
}
