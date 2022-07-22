package com.hoiuc.assembly;

import com.hoiuc.io.Message;
import com.hoiuc.io.SQLManager;
import com.hoiuc.io.Util;
import com.hoiuc.server.Manager;
import com.hoiuc.server.Service;
import com.hoiuc.stream.Server;
import com.hoiuc.template.ItemTemplate;
import com.hoiuc.template.SkillTemplate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class CloneCharacter extends Body {
    public Char c = null;
    public int percendame = 0;
    public boolean islive = false;

    public CloneCharacter (Char n) {
        try {
            this.seNinja(n);
            this.c = n;
            this.id = -n.id - 100000;
            this.ItemBody = new Item[32];
            this.ItemMounts = new Item[5];
            this.KSkill = new byte[3];
            this.OSkill = new byte[5];
            byte i;
            for(i = 0; i < this.KSkill.length; ++i) {
                this.KSkill[i] = -1;
            }
            for(i = 0; i < this.OSkill.length; ++i) {
                this.OSkill[i] = -1;
            }
            this.isHuman = false;
            this.isNhanban = true;
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static CloneCharacter getClone(Char n) {
        try {
            synchronized(Server.LOCK_MYSQL) {
                ResultSet red = SQLManager.stat.executeQuery("SELECT * FROM `clone_ninja` WHERE `name`LIKE'" + n.name + "';");
                CloneCharacter cl;
                if (red != null && red.first()) {
                    cl = new CloneCharacter(n);
                    cl.id = red.getInt("id");
                    cl.speed = red.getByte("speed");
                    cl.nclass = red.getByte("class");
                    cl.ppoint = red.getShort("ppoint");
                    cl.potential0 = red.getShort("potential0");
                    cl.potential1 = red.getShort("potential1");
                    cl.potential2 = red.getInt("potential2");
                    cl.potential3 = red.getInt("potential3");
                    cl.spoint = red.getShort("spoint");
                    JSONArray jar = (JSONArray) JSONValue.parse(red.getString("skill"));
                    JSONObject job = null;
                    JSONObject job2 = null;
                    if (jar != null) {
                        for(byte b = 0; b < jar.size(); ++b) {
                            job = (JSONObject)jar.get(b);
                            Skill skill = new Skill();
                            skill.id = Byte.parseByte(job.get("id").toString());
                            skill.point = Byte.parseByte(job.get("point").toString());
                            cl.skill.add(skill);
                            job.clear();
                        }
                    }

                    JSONArray jarr2 = (JSONArray)JSONValue.parse(red.getString("KSkill"));
                    cl.KSkill = new byte[jarr2.size()];

                    byte j;
                    for(j = 0; j < cl.KSkill.length; ++j) {
                        cl.KSkill[j] = Byte.parseByte(jarr2.get(j).toString());
                    }

                    jarr2 = (JSONArray)JSONValue.parse(red.getString("OSkill"));
                    cl.OSkill = new byte[jarr2.size()];

                    for(j = 0; j < cl.OSkill.length; ++j) {
                        cl.OSkill[j] = Byte.parseByte(jarr2.get(j).toString());
                    }

                    cl.CSkill = (short)Byte.parseByte(red.getString("CSkill"));
                    cl.level = red.getShort("level");
                    cl.exp = red.getLong("exp");
                    cl.expdown = red.getLong("expdown");
                    cl.pk = red.getByte("pk");
                    cl.ItemBody = new Item[32];
                    jar = (JSONArray)JSONValue.parse(red.getString("ItemBody"));
                    byte index;
                    if (jar != null) {
                        for(j = 0; j < jar.size(); ++j) {
                            job2 = (JSONObject)jar.get(j);
                            index = Byte.parseByte(job2.get("index").toString());
                            cl.ItemBody[index] = ItemTemplate.parseItem(jar.get(j).toString());
                            job2.clear();
                        }
                    }

                    cl.ItemMounts = new Item[5];
                    jar = (JSONArray)JSONValue.parse(red.getString("ItemMounts"));
                    if (jar != null) {
                        for(j = 0; j < jar.size(); ++j) {
                            job2 = (JSONObject)jar.get(j);
                            index = Byte.parseByte(job2.get("index").toString());
                            cl.ItemMounts[index] = ItemTemplate.parseItem(jar.get(j).toString());
                            job2.clear();
                        }
                    }

                    jar = (JSONArray)JSONValue.parse(red.getString("effect"));
                    JSONArray jar2 = null;

                    for(j = 0; j < jar.size(); ++j) {
                        jar2 = (JSONArray)jar.get(j);
                        if (jar2 != null) {
                            int effid = Integer.parseInt(jar2.get(0).toString());
                            byte efftype = Byte.parseByte(jar2.get(1).toString());
                            long efftime = Long.parseLong(jar2.get(2).toString());
                            int param = Integer.parseInt(jar2.get(3).toString());
                            Effect eff = new Effect(effid, param);
                            eff.timeStart = 0;
                            eff.timeRemove = efftime;
                            eff.timeLength = (int)(efftime - System.currentTimeMillis());
                            eff = new Effect(effid, 0, (int)efftime, param);
                            cl.veff.add(eff);
                        }
                        jar2.clear();
                    }
                    jar = (JSONArray)JSONValue.parse(red.getString("thoi-trang"));
                    cl.ID_HAIR = Short.parseShort(jar.get(0).toString());
                    cl.ID_Body = Short.parseShort(jar.get(1).toString());
                    cl.ID_LEG = Short.parseShort(jar.get(2).toString());
                    cl.ID_WEA_PONE = Short.parseShort(jar.get(3).toString());
                    cl.ID_PP = Short.parseShort(jar.get(4).toString());
                    cl.ID_NAME = Short.parseShort(jar.get(5).toString());
                    cl.ID_HORSE = Short.parseShort(jar.get(6).toString());
                    cl.ID_RANK = Short.parseShort(jar.get(7).toString());
                    cl.ID_MAT_NA = Short.parseShort(jar.get(8).toString());
                    cl.ID_Bien_Hinh = Short.parseShort(jar.get(9).toString());

                    jar = (JSONArray)JSONValue.parse(red.getString("info"));
                    cl.useTiemNang = Integer.parseInt(jar.get(0).toString());
                    cl.useKyNang = Integer.parseInt(jar.get(1).toString());
                    cl.useBanhPhongLoi = Integer.parseInt(jar.get(2).toString());
                    cl.useBanhBangHoa= Integer.parseInt(jar.get(3).toString());
                    cl.countTayKyNang = Integer.parseInt(jar.get(4).toString());
                    cl.countTayTiemNang = Integer.parseInt(jar.get(5).toString());

                    if (jar != null && !jar.isEmpty()) {
                        jar.clear();
                    }

                    if (job != null && !job.isEmpty()) {
                        job.clear();
                    }

                    if (job2 != null && !job2.isEmpty()) {
                        job2.clear();
                    }

                    if (jarr2 != null && !jarr2.isEmpty()) {
                        jarr2.clear();
                    }

                    if (jar2 != null && !jar2.isEmpty()) {
                        jar2.clear();
                    }

                    red.close();
                    return cl;
                } else {
                    red.close();
                    SQLManager.stat.executeUpdate("INSERT INTO clone_ninja(`id`,`name`,`ItemBody`,`ItemMounts`) VALUES (" + (-10000000 - n.id) + ",'" + n.name + "','[]','[]');");
                    cl = new CloneCharacter(n);
                    cl.id = -10000000 - n.id;
                    cl.speed = 8;
                    cl.setLevel_Exp(cl.exp = Level.getMaxExp(10), true);
                    cl.ItemBody[1] = ItemTemplate.itemDefault(194,true);
                    Skill skill2 = new Skill();
                    cl.skill.add(skill2);
                    return cl;
                }
            }
        } catch (Exception var22) {
            Util.Debug("Error cloneChar.java(180): " + var22.toString());
            return null;
        }
    }

    public void refresh() {
        synchronized(this) {
            this.hp = this.getMaxHP();
            this.mp = this.getMaxMP();
            this.x = (short) Util.nextInt(this.c.x - 30, this.c.x + 30);
            this.y = this.c.y;
            this.isDie = false;
        }
    }

    public void move(short x, short y) {
        synchronized(this) {
            this.x = x;
            this.y = y;
            if(this.c.tileMap != null) {
                this.c.tileMap.move(this.id, x, y);
            } else if(this.c.tdbTileMap != null) {
                this.c.tdbTileMap.move(this.id, x, y);
            }

        }
    }

    public void off() {
        synchronized(this) {
            this.c.timeRemoveClone = -1L;
            this.islive = false;
            this.isDie = true;
            if(this.c.tileMap != null) {
                this.c.tileMap.removeMessage(this.id);
            } else if(this.c.tdbTileMap != null) {
                this.c.tdbTileMap.removeMessage(this.id);
            }

        }
    }

    public void open(long time, int percentdame) {
        synchronized(this) {
            if (!this.isDie && this.c.tileMap != null) {
                this.c.tileMap.removeMessage(this.id);
            } else if (!this.isDie && this.c.tdbTileMap != null) {
                this.c.tdbTileMap.removeMessage(this.id);
            }
            this.c.timeRemoveClone = time;
            this.percendame = percentdame;
            this.islive = true;
            this.refresh();
            int i;
            if (this.c.tileMap != null) {
                for(i = this.c.tileMap.players.size() - 1; i >=0; i--) {
                    if(this.c.tileMap.players.get(i) != null) {
                        Service.sendclonechar(this.c.p, this.c.tileMap.players.get(i));
                    }
                }
            } else if (this.c.tdbTileMap != null) {
                for(i = this.c.tdbTileMap.players.size() - 1; i >=0; i--) {
                    if(this.c.tdbTileMap.players.get(i) != null && this.c.tdbTileMap.players.get(i).conn != null) {
                        Service.sendclonechar(this.c.p, this.c.tdbTileMap.players.get(i));
                    }
                }
            }

        }
    }

    public void setXPLoadSkill(long exp) {
        Message m = null;
        this.exp = exp;
        try {
            m = new Message(-30);
            m.writer().writeByte(-124);
            m.writer().writeByte(this.speed);
            m.writer().writeInt(this.getMaxHP());
            m.writer().writeInt(this.getMaxMP());
            m.writer().writeLong(this.exp);
            m.writer().writeShort(this.spoint);
            m.writer().writeShort(this.ppoint);
            m.writer().writeShort(this.potential0);
            m.writer().writeShort(this.potential1);
            m.writer().writeInt(this.potential2);
            m.writer().writeInt(this.potential3);
            m.writer().flush();
            this.c.p.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void close() {

    }

    public void flush() {
        JSONArray jarr = new JSONArray();

        try {
            synchronized(Server.LOCK_MYSQL) {
                if(this.level >= Manager.max_level_up) {
                    this.level = Manager.max_level_up;
                }
                this.level = (int)Level.getLevelExp(this.exp)[0];
                String sqlSET = "`class`=" + this.nclass + ",`ppoint`=" + this.ppoint + ",`potential0`=" + this.potential0 + ",`potential1`=" + this.potential1 + ",`potential2`=" + this.potential2 + ",`potential3`=" + this.potential3 + ",`spoint`=" + this.spoint + ",`level`=" + this.level + ",`exp`=" + this.exp + ",`expdown`=" + this.expdown + ",`pk`=" + this.pk + "";
                jarr.clear();
                Iterator var4 = this.skill.iterator();

                while(var4.hasNext()) {
                    Skill skill = (Skill)var4.next();
                    jarr.add(SkillTemplate.ObjectSkill(skill));
                }

                sqlSET = sqlSET + ",`skill`='" + jarr.toJSONString() + "'";
                jarr.clear();
                byte[] var11 = this.KSkill;
                int var13 = var11.length;

                int var6;
                byte oid;
                for(var6 = 0; var6 < var13; ++var6) {
                    oid = var11[var6];
                    jarr.add(oid);
                }

                sqlSET = sqlSET + ",`KSkill`='" + jarr.toJSONString() + "'";
                jarr.clear();
                var11 = this.OSkill;
                var13 = var11.length;

                for(var6 = 0; var6 < var13; ++var6) {
                    oid = var11[var6];
                    jarr.add(oid);
                }

                sqlSET = sqlSET + ",`OSkill`='" + jarr.toJSONString() + "',`CSkill`=" + this.CSkill + "";
                jarr.clear();

                byte j;
                for(j = 0; j < this.ItemBody.length; ++j) {
                    if (this.ItemBody[j] != null) {
                        jarr.add(ItemTemplate.ObjectItem(this.ItemBody[j], j));
                    }
                }

                sqlSET = sqlSET + ",`ItemBody`='" + jarr.toJSONString() + "'";
                jarr.clear();

                for(j = 0; j < this.ItemMounts.length; ++j) {
                    if (this.ItemMounts[j] != null) {
                        jarr.add(ItemTemplate.ObjectItem(this.ItemMounts[j], j));
                    }
                }

                sqlSET = sqlSET + ",`ItemMounts`='" + jarr.toJSONString() + "'";
                jarr.clear();
                JSONArray jarr2 = new JSONArray();

                byte i;
                Effect effect;
                for(i = 0; i < this.veff.size(); ++i) {
                    effect = this.veff.get(i);
                    if (effect.template.type == 0 || effect.template.type == 18 || effect.template.type == 25) {
                        jarr2 = new JSONArray();
                        jarr2.add((this.veff.get(i)).template.id);
                        if (effect.template.id != 36 && effect.template.id != 42 && effect.template.id != 37 && effect.template.id != 38 && effect.template.id != 39) {
                            jarr2.add(0);
                            jarr2.add(effect.timeRemove - System.currentTimeMillis());
                        } else {
                            jarr2.add(1);
                            jarr2.add(effect.timeRemove);
                        }
                        jarr2.add(effect.param);
                        jarr.add(jarr2);
                    }
                }

                sqlSET = sqlSET + ",`effect`='" + jarr.toJSONString() + "'";
                jarr.clear();
                jarr.add(this.ID_HAIR);
                jarr.add(this.ID_Body);
                jarr.add(this.ID_LEG);
                jarr.add(this.ID_WEA_PONE);
                jarr.add(this.ID_PP);
                jarr.add(this.ID_NAME);
                jarr.add(this.ID_HORSE);
                jarr.add(this.ID_RANK);
                jarr.add(this.ID_MAT_NA);
                jarr.add(this.ID_Bien_Hinh);
                sqlSET = sqlSET + ",`thoi-trang`='" + jarr.toJSONString() + "'";

                jarr.clear();
                jarr.add(this.useTiemNang);
                jarr.add(this.useKyNang);
                jarr.add(this.useBanhPhongLoi);
                jarr.add(this.useBanhBangHoa);
                jarr.add(this.countTayKyNang);
                jarr.add(this.countTayTiemNang);
                sqlSET = sqlSET + ",`info`='" + jarr.toJSONString() + "'";

                SQLManager.stat.executeUpdate("UPDATE `clone_ninja` SET " + sqlSET + " WHERE `id`=" + this.id + " LIMIT 1;");
                if (jarr != null && !jarr.isEmpty()) {
                    jarr.clear();
                }

                if (jarr2 != null && !jarr2.isEmpty()) {
                    jarr2.clear();
                }
            }
        } catch (SQLException var10) {
            var10.printStackTrace();
        }
    }

    public void removeItemBody(byte index) {
        Message m = null;
        try {
            this.ItemBody[index] = null;
            if (index == 10) {
                this.c.p.mobMeCloneMessage(0, (byte)0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public short[] getWinBuffSkills() {
        short[] skills = new short[]{-1, -1, -1};
        int idx = 0;
        List<Skill> skillArrayList = this.skill;
        int i = 0;
        for(int skillArrayListSize = skillArrayList.size(); i < skillArrayListSize; ++i) {
            Skill skill1 = (Skill)skillArrayList.get(i);
            if (skill1.id == 51 || skill1.id == 52 || skill1.id == 47) {
                skills[idx++] = skill1.id;
            }
        }
        return skills;
    }
}
