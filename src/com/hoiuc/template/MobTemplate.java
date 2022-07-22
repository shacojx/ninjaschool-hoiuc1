package com.hoiuc.template;

import java.util.ArrayList;

public class MobTemplate {
    public int id;
    public byte type;
    public String name;
    public int hp;
    public byte rangeMove;
    public byte speed;
    public short[] arrIdItem;
    public static ArrayList<MobTemplate> entrys = new ArrayList<MobTemplate>();

    public static MobTemplate getMob(int id) {
        for (MobTemplate mob : MobTemplate.entrys) {
            if (mob.id == id) {
                return mob;
            }
        }
        return null;
    }
}
