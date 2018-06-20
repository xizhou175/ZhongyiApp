package com.hfad.zhongyi;

public class Pages {
    private boolean chosen[];
    private String description;

    public static Pages[] pages = {
        new Pages("head",6)
    };

    private Pages(String des, int numOfsym){
        this.description = des;
        boolean chosen_init[] = new boolean[numOfsym];
        this.chosen = chosen_init;
        for(int i = 0; i < numOfsym; i++){
            chosen[i] = false;
        }
    }

    public String getDescription(){
        return this.description;
    }

    public boolean[] getChosen() {
        return this.chosen;
    }

    public void setChosen(int id){
        if(chosen[id]) chosen[id] = false;
        else chosen[id] = true;
    }
}
