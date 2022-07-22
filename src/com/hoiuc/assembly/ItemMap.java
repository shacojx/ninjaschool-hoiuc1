package com.hoiuc.assembly;

public class ItemMap {
    public short x;
    public short y;
    public short itemMapId;
    public long removedelay = 25000L + System.currentTimeMillis();
    public int master = -1;
    public Item item;
    public int checkMob = 0;
}
