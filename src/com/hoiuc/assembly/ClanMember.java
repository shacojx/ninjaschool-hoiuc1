package com.hoiuc.assembly;

public class ClanMember {
    public String clanName = "";
    public byte typeclan = -1;
    public int charID;
    public String cName = "";
    public int clevel;
    public byte nClass;
    public int pointClan = 0;
    public int pointClanWeek = 0;

    public ClanMember(String clanN, byte typeclan, Char n) {
        this.clanName = clanN;
        this.typeclan = typeclan;
        this.charID = n.id;
        this.cName = n.name;
        this.nClass = n.nclass;
        this.clevel = n.level;
    }

    public ClanMember(String clanN, Char n) {
        this.clanName = clanN;
        this.charID = n.id;
        this.cName = n.name;
        this.nClass = n.nclass;
        this.clevel = n.level;
    }

    public ClanMember() {
    }
}
