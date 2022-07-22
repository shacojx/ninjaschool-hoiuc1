package com.hoiuc.assembly;

import java.util.ArrayList;

public class CheckRHB {
    public String name;
    public String item;
    public String time;

    public static ArrayList<CheckRHB> checkRHBArrayList = new ArrayList<>();

    public CheckRHB(String name, String item, String time) {
        this.name = name;
        this.item = item;
        this.time = time;
    }
}
