package com.hoiuc.stream.thiendiabang;

public class ThienDiaData {
    private String name;
    private int rank;
    private int type;

    public ThienDiaData(){}

    public ThienDiaData(String name, int rank, int type) {
        this.name = name;
        this.rank = rank;
        this.type = type;
    }

    public String getName() {
        return this.name != null ? this.name : "";
    }

    public int getRank() {
        return this.rank;
    }

    public int getType() {
        return this.type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setType(int type) {
        this.type = type;
    }
}
