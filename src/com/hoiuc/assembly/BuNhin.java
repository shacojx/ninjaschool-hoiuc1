package com.hoiuc.assembly;

public class BuNhin {
    public short x;
    public short y;
    public String name;
    public long timeRemove;
    public int idChar;
    public int hp;
    public int miss;

    public BuNhin(String name, short x, short y, long timeRemove, int idChar, int hpChar, int missChar) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.timeRemove = timeRemove;
        this.idChar = idChar;
        this.hp = hpChar;
        this.miss = missChar;
    }

    public void setHp(int hp) {
        this.hp += hp;
    }
}
