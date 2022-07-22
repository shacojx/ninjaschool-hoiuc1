package com.hoiuc.assembly;

import com.hoiuc.server.GameSrc;
import com.hoiuc.server.Manager;
import com.hoiuc.template.ItemTemplate;
import com.hoiuc.template.SkillOptionTemplate;
import com.hoiuc.template.SkillTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Body {
    public Char c;
    public int id;
    public byte head;
    public byte caiTrang;
    protected byte speed;
    public byte nclass;
    public int level;
    public long exp;
    public long expdown;
    public byte pk;
    public byte typepk;
    public short ppoint;
    public short potential0;
    public short potential1;
    public int potential2;
    public int potential3;
    public short spoint;
    public boolean isDie;
    public ArrayList<Skill> skill;
    public byte[] KSkill;
    public byte[] OSkill;
    public short CSkill;
    public Item[] ItemBody;
    public Item[] ItemMounts;
    public boolean isHuman;
    public boolean isNhanban;
    public long CSkilldelay;
    public Mob mobMe;
    public short x;
    public short y;
    public short yDun;
    public int hp;
    public int mp;
    public byte exptype;
    public ArrayList<Effect> veff;

    public boolean isGoiRong;
    public long timeEffGoiRong;
    public long timeUseItem;

    public int useTiemNang;
    public int useKyNang;
    public int useBanhPhongLoi;
    public int useBanhBangHoa;
    public int countTayKyNang;
    public int countTayTiemNang;

    public short ID_Body = -1;
    public short ID_PP = -1;
    public short ID_HAIR = -1;
    public short ID_LEG = -1;
    public short ID_HORSE = -1;
    public short ID_NAME = -1;
    public short ID_RANK = -1;
    public short ID_MAT_NA = -1;
    public short ID_Bien_Hinh = -1;
    public short ID_WEA_PONE = -1;

    public static int MIN_EFF0 = 30;
    public static int MIN_EFF1 = 40;
    public static int MIN_EFF2 = 50;
    public static int MIN_EFF3 = 60;
    public static int MIN_EFF4 = 70;
    public static int MIN_EFF5 = 80;
    public static int MIN_EFF6 = 90;
    public static int MIN_EFF7 = 100;
    public static int MIN_EFF8 = 110;
    public int ngocEff = -1;

    public Party party;
    public ArrayList<PartyPlease> aPartyInvite;
    public ArrayList<PartyPlease> aPartyInvate;
    public Vector<Friend> vFriend;

    public Body() {
        this.id = 0;
        this.head = -1;
        this.caiTrang = -1;
        this.speed = 4;
        this.nclass = 0;
        this.level = 1;
        this.exp = 1;
        this.expdown = 0L;
        this.pk = 0;
        this.typepk = 0;
        this.ppoint = 0;
        this.potential0 = 15;
        this.potential1 = 5;
        this.potential2 = 5;
        this.potential3 = 5;
        this.spoint = 0;
        this.isDie = false;
        this.skill = new ArrayList<Skill>();
        this.KSkill = null;
        this.OSkill = null;
        this.CSkill = -1;
        this.ItemBody = null;
        this.ItemMounts = null;
        this.CSkilldelay = 0L;
        this.mobMe = null;
        this.x = 0;
        this.y = 0;
        this.yDun = 0;
        this.hp = 0;
        this.mp = 0;
        this.exptype = 1;
        this.veff = new ArrayList<Effect>();
        this.party = null;
        this.aPartyInvite = new ArrayList<>();
        this.aPartyInvate = new ArrayList<>();
        this.isGoiRong = false;
        this.timeEffGoiRong = 6000L;
        this.timeUseItem = 0L;
        this.useTiemNang = 8;
        this.useKyNang = 8;
        this.useBanhPhongLoi = 10;
        this.useBanhBangHoa = 10;
        this.countTayKyNang = 1;
        this.countTayTiemNang = 1;
        this.vFriend = new Vector<>();
    }

    public void seNinja(Char c) {
        this.c = c;
    }

    public short partHead() {
        if(this.caiTrang != -1) {
            return ItemTemplate.ItemTemplateId(this.c.ItemCaiTrang[this.caiTrang].id).part;
        }
        if (this.ItemBody[11] == null) {
            return this.c.head;
        }
        return ItemTemplate.ItemTemplateId(this.ItemBody[11].id).part;
    }

    public short Weapon() {
        if (this.ItemBody[1] != null) {
            return ItemTemplate.ItemTemplateId(this.ItemBody[1].id).part;
        }
        return -1;
    }

    public short Body() {
        if (ItemTemplate.isPartHead(this.partHead())) {
            return (short)(this.partHead()+1);
        }
        if (this.ItemBody[2] != null) {
            return ItemTemplate.ItemTemplateId(this.ItemBody[2].id).part;
        }
        return -1;
    }

    public short Leg() {
        if (ItemTemplate.isPartHead(this.partHead())) {
            return (short)(this.partHead()+2);
        }
        if (this.ItemBody[6] != null) {
            return ItemTemplate.ItemTemplateId(this.ItemBody[6].id).part;
        }
        return -1;
    }

    public void updatePk(int num) {
        this.pk += (byte) num;
        if (this.pk < 0) {
            this.pk = 0;
        } else if (this.pk > 20) {
            this.pk = 20;
        }
    }

    public int getMaxHP() {
        int hpmax = this.getPotential(2) * 10;
        hpmax += hpmax * (this.getPramItem(31) + this.getPramItem(61) + this.getPramSkill(17)+ 100) / 100;
        hpmax += this.getPramItem(6);
        hpmax += this.getPramItem(32);
        hpmax += this.getPramItem(77);
        hpmax += this.getPramItem(82);
        hpmax += this.getPramItem(125);
        Effect eff = this.c.get().getEffId(27);
        if (eff != null) {
            hpmax += eff.param;
        }
        if (this.hp > hpmax) {
            this.hp = hpmax;
        }
        return hpmax;
    }

    public synchronized void upHP(int hpup) {
        if (this.isDie) {
            return;
        }
        this.hp += hpup;
        if (this.hp > this.getMaxHP()) {
            this.hp = this.getMaxHP();
        }
        if (this.hp <= 0) {
            this.isDie = true;
            this.hp = 0;
        }
    }

    public int getMaxMP() {
        int mpmax = this.getPotential(3) * 10;
        mpmax += mpmax * (this.getPramItem(28) + this.getPramItem(60) + this.getPramSkill(18) + 100) / 100;
        mpmax += this.getPramItem(7);
        mpmax += this.getPramItem(19);
        mpmax += this.getPramItem(29);
        mpmax += this.getPramItem(83);
        mpmax += this.getPramItem(117);
        if (this.mp > mpmax) {
            this.mp = mpmax;
        }
        return mpmax;
    }

    public synchronized void upMP(int mpup) {
        if (this.isDie) {
            return;
        }
        this.mp += mpup;
        if (this.mp > this.getMaxMP()) {
            this.mp = this.getMaxMP();
        } else if (this.mp < 0) {
            this.mp = 0;
        }
    }

    public int eff5buffHP() {
        return this.getPramItem(30) + this.getPramItem(120);
    }

    public int eff5buffMP() {
        return this.getPramItem(27) + this.getPramItem(119);
    }

    public void setSpeed(byte speed) {
        this.speed = speed;
    }

    public int speed() {
        int sp = this.speed;
        sp = sp * (100 + this.getPramItem(16)) / 100;
        sp += this.getPramItem(93);
        return sp;
    }

    public int dameSide() {
        int percent = this.getPramSkill(11) + this.getPramItem(94);
        Effect eff = this.c.get().getEffId(25);
        if (eff != null) {
            percent += eff.param;
        }
        eff = this.c.get().getEffId(17);
        if (eff != null) {
            percent += eff.param;
        }
        eff = this.c.get().getEffId(19);
        if (eff != null) {
            percent += eff.param * 10 / 3;
        }
        int si;
        if (this.Side() == 1) {
            si = this.getPotential(3);
            si += si * (this.getPramSkill(1) + this.getPramItem(9) + percent) / 100;
            si += this.getPramItem(1);
            si += this.getPramItem(0);
        } else {
            si = this.getPotential(0);
            si += si * (this.getPramSkill(0) + this.getPramItem(8) + percent) / 100;
            si += this.getPramItem(0);
            si += this.getPramItem(1);
        }
        si += this.getPramItem(38);//tan cong co ban vu khi
        eff = this.c.get().getEffId(17);
        if (eff != null) {
            si += this.getPramItem(38) * eff.param / 100;//buff tan cong co ban phai cung
        }
        eff = this.c.get().getEffId(19);
        if (eff != null) {
            si += this.getPramItem(38) * eff.param * 10 / 300;//buff tan cong co ban phai quat
        }
        si += this.getPramItem(38) * this.getPramSkill(11) / 100;// buff tan cong co ban cua skill chu dong
        eff = this.c.get().getEffId(25);
        if (eff != null) {
            si += this.getPramItem(38) * eff.param / 100;//buff tan cong co ban cua long luc dan
        }
        return si;
    }

    public int dameSys() {
        int ds = 0;
        if (this.Sys() == 1) {
            ds = this.getPramSkill(2);
            ds += this.getPramItem(88);
            ds += this.getPramItem(89);
            ds += this.getPramItem(90);
            if (this.Side() == 1) {
                ds += this.getPramSkill(8);
                ds += this.getPramItem(21);
                ds += this.getPramItem(22);
                ds += this.getPramItem(23);
                ds += this.getPramItem(24);
                ds += this.getPramItem(25);
                ds += this.getPramItem(26);
            } else {
                ds += this.getPramSkill(5);
                ds += this.getPramItem(21);
                ds += this.getPramItem(22);
                ds += this.getPramItem(23);
                ds += this.getPramItem(24);
                ds += this.getPramItem(25);
                ds += this.getPramItem(26);
            }
        } else if (this.Sys() == 2) {
            ds = this.getPramSkill(3);
            ds += this.getPramItem(88);
            ds += this.getPramItem(89);
            ds += this.getPramItem(90);
            if (this.Side() == 1) {
                ds += this.getPramSkill(9);
                ds += this.getPramItem(21);
                ds += this.getPramItem(22);
                ds += this.getPramItem(23);
                ds += this.getPramItem(24);
                ds += this.getPramItem(25);
                ds += this.getPramItem(26);
            } else {
                ds += this.getPramSkill(6);
                ds += this.getPramItem(21);
                ds += this.getPramItem(22);
                ds += this.getPramItem(23);
                ds += this.getPramItem(24);
                ds += this.getPramItem(25);
                ds += this.getPramItem(26);
            }
        } else if (this.Sys() == 3) {
            ds = this.getPramSkill(4);
            ds += this.getPramItem(88);
            ds += this.getPramItem(89);
            ds += this.getPramItem(90);
            if (this.Side() == 1) {
                ds += this.getPramSkill(10);
                ds += this.getPramItem(21);
                ds += this.getPramItem(22);
                ds += this.getPramItem(23);
                ds += this.getPramItem(24);
                ds += this.getPramItem(25);
                ds += this.getPramItem(26);
            } else {
                ds += this.getPramSkill(7);
                ds += this.getPramItem(21);
                ds += this.getPramItem(22);
                ds += this.getPramItem(23);
                ds += this.getPramItem(24);
                ds += this.getPramItem(25);
                ds += this.getPramItem(26);
            }
        }
        return ds;
    }

    public int dameMax() {
        int dame = this.dameSide();
        dame += this.dameSys();
        dame += this.getPramItem(73);
        dame += this.getPramItem(76);
        dame += this.getPramItem(87);
        if (dame < 0) {
            dame = 0;
        }
        return dame;
    }

    public int dameMin() {
        return this.dameMax() * 90 / 100;
    }

    public int dameDown() {
        int dwn = this.getPramItem(47);//giam sat thuong cua itembody
        dwn += this.getPramItem(74);//giam sat thuong cua thu cuoi
        dwn += this.getPramItem(80);//giam sat thuong cua tinh luyen
        dwn += this.getPramItem(124);//giam sat thuong cua ngoc
        return dwn;
    }

    public int ResFire() {
        int bear = this.getPramItem(2);
        bear += this.getPramItem(11);
        bear += this.getPramItem(33);
        bear += this.getPramItem(70);
        bear += this.getPramItem(96);
        bear += this.getPramSkill(19);
        bear += this.getPramSkill(20);
        if (this.c.get().getEffId(19) != null) {
            bear += this.c.get().getEffId(19).param;
        }
        if (this.c.get().getEffId(26) != null) {
            bear += this.c.get().getEffId(26).param;
        }
        bear += this.getPramItem(20);
        bear += this.getPramItem(36);
        bear += this.getPramItem(81);
        bear += this.getPramItem(118);
        bear += (bear * this.getPramItem(127)/100);
        return bear;
    }

    public int ResIce() {
        int bear = this.getPramItem(3);
        bear += this.getPramItem(12);
        bear += this.getPramItem(34);
        bear += this.getPramItem(71);
        bear += this.getPramItem(95);
        bear += this.getPramSkill(19);
        bear += this.getPramSkill(21);
        if (this.c.get().getEffId(19) != null) {
            bear += this.c.get().getEffId(19).param;
        }
        if (this.c.get().getEffId(26) != null) {
            bear += this.c.get().getEffId(26).param;
        }
        bear += this.getPramItem(20);
        bear += this.getPramItem(36);
        bear += this.getPramItem(81);
        bear += this.getPramItem(118);
        bear += (bear * this.getPramItem(128)/100);
        return bear;
    }

    public int ResWind() {
        int bear = this.getPramItem(4);
        bear += this.getPramItem(13);
        bear += this.getPramItem(35);
        bear += this.getPramItem(72);
        bear += this.getPramItem(97);
        bear += this.getPramSkill(19);
        bear += this.getPramSkill(22);
        if (this.c.get().getEffId(19) != null) {
            bear += this.c.get().getEffId(19).param;
        }
        if (this.c.get().getEffId(26) != null) {
            bear += this.c.get().getEffId(26).param;
        }
        bear += this.getPramItem(20);
        bear += this.getPramItem(36);
        bear += this.getPramItem(81);
        bear += this.getPramItem(118);
        bear += (bear * this.getPramItem(129)/100);
        return bear;
    }

    public int Exactly() {
        int exa = this.getPotential(1);
        exa += this.getPramItem(10);
        exa += this.getPramItem(18);
        exa += this.getPramItem(75);
        exa += this.getPramItem(86);
        exa += this.getPramItem(116);
        exa += this.getPramSkill(12);
        if (this.c.get().getEffId(24) != null) {
            exa += this.c.get().getEffId(24).param;
        }
        if (this.c.get().getEffId(23) != null) {
            exa += this.c.get().getEffId(23).param;
        }
        return exa;
    }

    public int Miss() {
        int miss = 0;
        if (this.c.get().getEffId(11) != null) {
            miss += this.c.get().getEffId(11).param;
        }
        miss += this.getPotential(1) * 150 / 100;
        miss += this.getPramItem(5);
        miss += this.getPramItem(17);
        miss += this.getPramItem(62);
        miss += this.getPramItem(68);
        miss += this.getPramItem(78);
        miss += this.getPramItem(84);
        miss += this.getPramItem(115);
        miss += this.getPramSkill(13);
        return miss;
    }

    public int Fatal() {
        int ft = 0;
        ft += this.getPramItem(14);
        ft += this.getPramItem(37);
        ft += this.getPramItem(69);
        ft += this.getPramItem(92);
        ft += this.getPramItem(114);
        ft += this.getPramSkill(14);
        return ft;
    }

    public int percentFantalDameDown() {
        int pfdd = 0;
        pfdd += this.getPramItem(46);
        pfdd += this.getPramItem(79);
        pfdd += this.getPramItem(121);
        return pfdd;
    }

    public int FantalDame() {
        return this.getPramItem(105);
    }

    public int percentFantalDame() {
        int pfd = 0;
        pfd += this.getPramItem(39);
        pfd += this.getPramItem(67);
        pfd += this.getPramSkill(65);
        return pfd;
    }

    public int ReactDame() {
        int rd = 0;
        rd += this.getPramItem(15);
        rd += this.getPramItem(91);
        rd += this.getPramItem(126);
        rd += this.getPramSkill(15);
        return rd;
    }

    public int sysUp() {
        return 0;
    }

    public int sysDown() {
        return 0;
    }

    public int percentFire2() {
        if (this.isNhanban) {
            return this.getPramSkillClone(24);
        }
        return this.getPramSkill(24);
    }

    public int percentFire4() {
        if (this.isNhanban) {
            return this.getPramSkillClone(34);
        }
        return this.getPramSkill(34);
    }

    public int percentIce1_5() {
        if (this.isNhanban) {
            return this.getPramSkillClone(25);
        }
        return this.getPramSkill(25);

    }

    public int percentWind1() {
        if (this.isNhanban) {
            return this.getPramSkillClone(26);
        }
        return this.getPramSkill(26);
    }

    public int percentWind2() {
        if (this.isNhanban) {
            return this.getPramSkillClone(36);
        }
        return this.getPramSkill(36);
    }

    public int getPotential(int i) {
        int potential = 0;
        switch (i) {
            case 0: {
                potential = this.potential0;
                break;
            }
            case 1: {
                potential = this.potential1;
                break;
            }
            case 2: {
                potential = this.potential2;
                break;
            }
            case 3: {
                potential = this.potential3;
                break;
            }
        }
        potential = potential * (100 + this.getPramItem(58)) / 100;
        potential += this.getPramItem(57);
        return potential;
    }

    public int getPramItem(int id) {
        try {
            if (this.c.get() == null) {
                return 0;
            }
            int param = 0;

            for (Item body : this.c.get().ItemBody) {
                if (body != null) {
                    for (Option option : body.options) {
                        if (option.id == id && !ItemTemplate.isUpgradeHide(option.id, body.upgrade)) {
                            param += option.param;
                        }
                    }
                    if (body.ngocs != null && body.ngocs.size() > 0 && ItemTemplate.isCheckId(id)) {
                        int idNgocOption = -1;
                        if (body.getData().type == 1) {
                            idNgocOption = ItemTemplate.VU_KHI_OPTION_ID;
                        } else if (body.getData().isTrangSuc()) {
                            idNgocOption = ItemTemplate.TRANG_SUC_OPTION_ID;
                        } else if (body.getData().isTrangPhuc()) {
                            idNgocOption = ItemTemplate.TRANG_BI_OPTION_ID;
                        }
                        if (idNgocOption != -1) {
                            for (Item ngoc : body.ngocs) {
                                if (ngoc != null) {
                                    int index = -1;
                                    for (Option opt : ngoc.options) {
                                        if (opt.id == idNgocOption) {
                                            index = ngoc.options.indexOf(opt);
                                            break;
                                        }
                                    }
                                    if (index != -1) {
                                        if (index + 1 < ngoc.options.size() && (ngoc.options.get(index + 1)).id == id) {
                                            param += (ngoc.options.get(index + 1)).param;
                                        } else if (index + 2 < ngoc.options.size() && (ngoc.options.get(index + 2)).id == id) {
                                            param += (ngoc.options.get(index + 2)).param;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            for (Item mounts : this.c.get().ItemMounts) {
                if (mounts != null) {
                    for (Option option : mounts.options) {
                        if (option.id == id) {
                            param += option.param;
                        }
                    }
                }
            }
            if(this.caiTrang != -1 && this.isHuman) {
                for (Option option : this.c.ItemCaiTrang[this.caiTrang].options) {
                    if (option.id == id) {
                        param += option.param;
                    }
                }
            }
            return param;
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean checkIdSkill90(int id) {
        for(int i2 = 67; i2 <= 72; ++i2) {
            if(id == i2) {
                return true;
            }
        }
        return false;
    }

    public int getIdSkill90() {
        Skill ski;
        for(int i2 = 67; i2 <= 72; ++i2) {
            ski = this.getSkill(i2);
            if(ski != null) {
                return i2;
            }
        }
        return 71;
    }

    public Skill getSkill90() {
        Skill ski;
        for(int i2 = 67; i2 <= 72; ++i2) {
            ski = this.getSkill(i2);
            if(ski != null) {
                return ski;
            }
        }
        return null;
    }

    public int getPramSkill(int id) {
        try {
            if (this.c.get() == null) {
                return 0;
            }
            int param = 0;
            SkillTemplate data;
            SkillOptionTemplate temp;
            for (Skill sk : this.c.get().skill) {
                data = SkillTemplate.Templates(sk.id);
                if (data.type == 0 || data.type == 2 || sk.id == this.CSkill || data.type == 4 || data.type == 3) {
                    temp = SkillTemplate.Templates(sk.id, sk.point);
                    for (Option op : temp.options) {
                        if (op.id == id) {
                            param += op.param;
                            break;
                        }
                    }
                }
            }
            return param;
        } catch (Exception e) {
            return 0;
        }
    }

    public int getPramSkillClone(int id) {
        try {
            if (this.c.clone == null) {
                return 0;
            }
            int param = 0;
            SkillTemplate data;
            SkillOptionTemplate temp;
            for (Skill sk : this.c.clone.skill) {
                data = SkillTemplate.Templates(sk.id);
                if (data.type == 0 || data.type == 2 || sk.id == this.CSkill || data.type == 4 || data.type == 3) {
                    temp = SkillTemplate.Templates(sk.id, sk.point);
                    for (Option op : temp.options) {
                        if (op.id == id) {
                            param += op.param;
                        }
                    }
                }
            }
            return param;
        } catch (Exception e) {
            return 0;
        }
    }

    public Effect getEffId(int effid) {
        try {
            byte i;
            Effect eff;
            for (i = 0; i < this.veff.size(); ++i) {
                eff = this.veff.get(i);
                if (eff != null && eff.template != null && effid == eff.template.id) {
                    return eff;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public Effect getEffType(byte efftype) {
        try {
            byte i;
            Effect eff;
            for (i = 0; i < this.veff.size(); ++i) {
                eff = this.veff.get(i);
                if (eff != null && eff.template != null && efftype == eff.template.type) {
                    return eff;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public Skill getSkill(int id) {
        for (Skill skl : this.skill) {
            if (skl.id == id) {
                return skl;
            }
        }
        return null;
    }

    public short getCSkill() {
        if (this.nclass == 0) {
            return 0;
        }
        return this.CSkill;
    }

    public List<Skill> getSkills() {
        if (this.nclass == 0 && this.skill.size() == 0) {
            this.skill.add(new Skill(0));
        }
        return this.skill;
    }

    public void setLevel_Exp(long exp, boolean value) {
        long[] levelExp = Level.getLevelExp(exp);
        if (value && this.level < Manager.max_level_up + 1) {
            this.level = (int) levelExp[0];
        }
    }

    public void upDie() {
        synchronized (this) {
            this.hp = 0;
            this.isDie = true;
            try {
                if (!this.c.isNhanban && this.c != null && this.c.tileMap != null) {
                    this.c.tileMap.sendDie(this.c);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public int fullTL() {
        int tl = 0;
        boolean ad = false;
        byte i;
        Item item;
        int tl2;
        short j;
        Option op;
        for (i = 0; i < 10; ++i) {
            tl2 = 0;
            item = this.ItemBody[i];
            if (item == null) {
                return 0;
            }
            for (j = 0; j < item.options.size(); ++j) {
                op = item.options.get(j);
                if (op.id == 85) {
                    tl2 = op.param;
                    break;
                }
                if (j == item.options.size() - 1) {
                    return 0;
                }
            }
            if (!ad) {
                tl = tl2;
                ad = true;
            }
            if (tl > tl2) {
                tl = tl2;
            }
        }
        return tl;
    }

    public byte Sys() {
        return GameSrc.SysClass(this.nclass);
    }

    public byte Side() {
        return GameSrc.SideClass(this.nclass);
    }

    public int percentIce() {
        return this.getPramSkill(69);
    }

    public int timeIce() {
        return this.getPramSkill(70);
    }

    public int percentIce3() {
        return this.getPramSkill(35);
    }

    public int getNgocEff() {
        if (this.ngocEff != -1) {
            return this.ngocEff;
        } else {
            int countPoint = 0;
            Item item;
            int var4;
            for (var4 = 0; var4 < this.ItemBody.length; ++var4) {
                item = this.ItemBody[var4];
                if (item != null && item.ngocs != null) {
                    int i;
                    for (i = 0; i < item.ngocs.size(); ++i) {
                        countPoint += ((Item) item.ngocs.get(i)).getUpgrade();
                    }
                }
            }
            if (countPoint >= MIN_EFF0 && countPoint < MIN_EFF1) {
                this.ngocEff = 0;
                return this.ngocEff;
            } else if (countPoint >= MIN_EFF1 && countPoint < MIN_EFF2) {
                this.ngocEff = 1;
                return 1;
            } else if (countPoint >= MIN_EFF2 && countPoint < MIN_EFF3) {
                this.ngocEff = 2;
                return 2;
            } else if (countPoint >= MIN_EFF3 && countPoint < MIN_EFF4) {
                switch (GameSrc.SysClass(this.nclass)) {
                    case 1: {
                        this.ngocEff = 9;
                        return 9;
                    }
                    case 2: {
                        this.ngocEff = 3;
                        return 3;
                    }
                    case 3: {
                        this.ngocEff = 6;
                        return 6;
                    }
                }
                return -1;
            } else if (countPoint >= MIN_EFF4 && countPoint < MIN_EFF5) {
                switch (GameSrc.SysClass(this.nclass)) {
                    case 1: {
                        this.ngocEff = 4;
                        return 4;
                    }
                    case 2: {
                        this.ngocEff = 7;
                        return 7;
                    }
                    case 3: {
                        this.ngocEff = 10;
                        return 10;
                    }
                }
                return -1;
            } else if (countPoint >= MIN_EFF5 && countPoint < MIN_EFF6) {
                switch (GameSrc.SysClass(this.nclass)) {
                    case 1: {
                        this.ngocEff = 5;
                        return 5;
                    }
                    case 2: {
                        this.ngocEff = 8;
                        return 8;
                    }
                    case 3: {
                        this.ngocEff = 11;
                        return 11;
                    }
                }
                return -1;
            } else if (countPoint >= MIN_EFF6 && countPoint < MIN_EFF7) {
                this.ngocEff = 35;
                return 35;
            } else if (countPoint >= MIN_EFF7 && countPoint < MIN_EFF8) {
                this.ngocEff = 27;
                return 27;
            } else if (countPoint >= MIN_EFF8) {
                this.ngocEff = 25;
                return 25;
            } else {
                return -1;
            }
        }
    }
}
