package com.hoiuc.assembly;

import com.hoiuc.template.EffectTemplate;

public class Effect {
    public int timeStart;
    public int timeLength;
    public int param;
    public EffectTemplate template;
    public long timeRemove;

    public Effect(int id, int param) {
        this.template = EffectTemplate.entrys.get(id);
        this.param = param;
    }

    public Effect(int id, int timeStart, int timeLength, int param) {
        this.template = EffectTemplate.entrys.get(id);
        this.timeStart = timeStart;
        this.timeLength = timeLength;
        this.param = param;
        this.timeRemove = System.currentTimeMillis() - (long)timeStart + (long)timeLength;
    }
}
