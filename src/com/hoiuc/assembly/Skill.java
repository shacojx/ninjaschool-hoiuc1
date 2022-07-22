package com.hoiuc.assembly;

import com.hoiuc.template.SkillOptionTemplate;
import com.hoiuc.template.SkillTemplate;

public class Skill {
    public byte id;
    public byte point;
    public long coolDown;

    public Skill() {
    }

    public Skill(final int id) {
        this.id = (byte)id;
    }

    public SkillOptionTemplate getTemplate() {
        return SkillTemplate.Templates(this.id, this.point);
    }

    public SkillTemplate skillData() {
        return SkillTemplate.Templates(this.id);
    }
}
