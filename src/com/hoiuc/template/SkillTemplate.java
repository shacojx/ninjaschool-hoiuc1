package com.hoiuc.template;

import com.hoiuc.assembly.Skill;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class SkillTemplate {
    public int id;
    public byte nclass;
    public String name;
    public byte maxPoint;
    public byte type;
    public short iconId;
    public String desc;
    public ArrayList<SkillOptionTemplate> templates;
    public static ArrayList<SkillTemplate> entrys = new ArrayList<SkillTemplate>();

    public SkillTemplate() {
        this.name = null;
        this.templates = new ArrayList<SkillOptionTemplate>();
    }

    public static SkillOptionTemplate Templates(byte id, byte point) {
        for (SkillTemplate temp : SkillTemplate.entrys) {
            if (temp.id == id) {
                for (SkillOptionTemplate data : temp.templates) {
                    if (data.point == point) {
                        return data;
                    }
                }
            }
        }
        return null;
    }

    public static SkillTemplate Templates(int id) {
        for (SkillTemplate temp : SkillTemplate.entrys) {
            if (temp.id == id) {
                return temp;
            }
        }
        return null;
    }

    public static JSONObject ObjectSkill(Skill skill) {
        JSONObject put = new JSONObject();
        put.put((Object)"id", (Object)skill.id);
        put.put((Object)"point", (Object)skill.point);
        return put;
    }

}
