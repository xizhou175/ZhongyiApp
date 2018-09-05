package com.hfad.zhongyi;

public class PersonalInfo {
    private String gender = "";
    private Integer age = 0;
    private String username = "";
    private String name = "";
    private String password = "";

    PersonalInfo(String gender, Integer age, String email, String name, String password){
        this.gender = gender;
        this.age = age;
        this.name = name;
        this.password = password;
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

    public void setName(String name){ this.name = name; }

    public String getName() { return this.name; }

    public void setPassword(String password){ this.password = password; }

    public String getPassword() { return password; }

}

class Patient{
    public static PersonalInfo personalInfo = new PersonalInfo("", 0, "", "", "");
}
