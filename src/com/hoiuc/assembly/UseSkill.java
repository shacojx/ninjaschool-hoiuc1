package com.hoiuc.assembly;

import com.hoiuc.io.Message;
import com.hoiuc.io.Util;
import com.hoiuc.template.ItemTemplate;
import com.hoiuc.template.SkillOptionTemplate;
import com.hoiuc.template.SkillTemplate;

public class UseSkill {
    public static void useSkill(Player p, Message m) {
        try {
            if(m.reader().available() <= 0) {
                return;
            }
            short idSkill = m.reader().readShort();
            m.cleanup();
            Skill skill = p.c.get().getSkill(idSkill);
            if (skill != null && System.currentTimeMillis() > p.c.get().CSkilldelay) {
                SkillTemplate data = SkillTemplate.Templates(idSkill);
                if (data.type != 0) {
                    p.c.get().CSkilldelay = System.currentTimeMillis() + 500L;
                    switch (data.type) {
                        case 2: {
                            useSkillBuff(p, idSkill);
                            break;
                        }
                        case 3: {
                            useSkillMagic(p, idSkill);
                            break;
                        }
                        case 4: {
                            p.c.get().CSkill = 49;
                            break;
                        }
                        default: {
                            p.c.get().CSkill = idSkill;
                            break;
                        }
                    }
                }
            }
            return;
        } catch (Exception e) {
            Util.Debug("Error useSkill.java(38) : " + e.toString());
            return;
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void useSkillBot(Player p, short idSkill) {
        try {
            Skill skill = p.c.get().getSkill(idSkill);
            if (skill != null) {
                useSkillBuff(p, idSkill);
            }
        } catch (Exception e) {
            Util.Debug("Error useSkill.java(38) : " + e.toString());
            return;
        }
    }

    private static void useSkillMagic(Player p, int skilltemp) {
        Skill skill = p.c.get().getSkill(skilltemp);
        SkillOptionTemplate temp = SkillTemplate.Templates(skill.id, skill.point);
        if (p.c.get().mp < temp.manaUse) {
            p.getMp();
            return;
        }
        if (skill.coolDown > System.currentTimeMillis()) {
            return;
        }
        p.c.get().upMP(-temp.manaUse);
        skill.coolDown = System.currentTimeMillis() + temp.coolDown;
        int param = 0;
        switch (skilltemp) {
            //skill 25 kiếm
            case 4: {
                p.c.isSkill25Kiem = true;
                break;
            }
            //skill 25 đao
            case 40: {
                p.c.isSkill25Dao = true;
                break;
            }
        }
    }

    private static void useSkillBuff(Player p, int skilltemp) {
        Skill skill = p.c.get().getSkill(skilltemp);
        SkillOptionTemplate temp = SkillTemplate.Templates(skill.id, skill.point);
        if (p.c.get().mp < temp.manaUse) {
            p.getMp();
            return;
        }
        if (skill.coolDown > System.currentTimeMillis()) {
            return;
        }
        p.c.get().upMP(-temp.manaUse);
        skill.coolDown = System.currentTimeMillis() + temp.coolDown;
        int param = 0;
        switch (skilltemp) {
            case 6: {
                p.setEffect(15, 0, p.c.get().getPramSkill(53) * 1000, 0);
                break;
            }
            case 13: {
                p.setEffect(9, 0, 30000, p.c.get().getPramSkill(51));
                break;
            }
            case 15: {
                p.setEffect(16, 0, 5000, p.c.get().getPramSkill(52));
                break;
            }
            //Gốc cây
            case 22: {
                int per = Util.nextInt(0,1);
                BuNhin buNhin = new BuNhin(p.c.name,(short)(per==1?p.c.x+30:p.c.x-30),p.c.y, System.currentTimeMillis()+p.c.getPramSkill(49)*1000, p.c.id, p.c.get().getMaxHP(), p.c.get().Miss());
                if(p.c.tileMap != null) {
                    p.c.tileMap.buNhins.add(buNhin);
                } else if(p.c.tdbTileMap != null) {
                    p.c.tdbTileMap.buNhins.add(buNhin);
                }
                p.c.buNhin = buNhin;
                Message m = null;
                try {
                    m = new Message((byte)75);
                    m.writer().writeUTF(buNhin.name);
                    m.writer().writeShort(buNhin.x);
                    m.writer().writeShort(buNhin.y);
                    m.writer().flush();
                    if(p.c.tileMap != null) {
                        p.c.tileMap.sendMessage(m);
                    } else if(p.c.tdbTileMap != null) {
                        p.c.tdbTileMap.sendMessage(m);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(m != null) {
                        m.cleanup();
                    }
                }
                break;
            }
            case 31: {
                p.setEffect(10, 0, 90000, p.c.get().getPramSkill(30));
                break;
            }
            case 33: {
                p.setEffect(17, 0, 5000, p.c.get().getPramSkill(56));
                break;
            }
            case 47: {
                param = p.c.get().getPramSkill(27);
                param += param * p.c.get().getPramSkill(66) / 100;
                p.setEffect(8, 0, 5000, param);
                if (p.c.get().party != null && p.c.tileMap != null) {
                    int i;
                    Player p2;
                    Char n;
                    for (i = 0; i < p.c.tileMap.players.size(); ++i) {
                        p2 = p.c.tileMap.players.get(i);
                        if (p2.c.id != p.c.id) {
                            n = p2.c;
                            if (n.party == p.c.get().party && Math.abs(p.c.get().x - n.x) <= temp.dx && Math.abs(p.c.get().y - n.y) <= temp.dy) {
                                n.p.setEffect(8, 0, 5000, p.c.get().getPramSkill(43) + p.c.get().getPramSkill(43) * p.c.get().getPramSkill(66) / 100);
                            }
                        }
                    }
                    break;
                }
                break;
            }
            case 51: {
                param = p.c.get().getPramSkill(45);
                param += param*p.c.get().getPramSkill(66)/100;
                p.setEffect(19, 0, 90000, param);
                if (p.c.get().party != null && p.c.tileMap != null) {
                    int i;
                    Player p2;
                    Char n;
                    for (i = 0; i < p.c.tileMap.players.size(); ++i) {
                        p2 = p.c.tileMap.players.get(i);
                        if (p2.c.id != p.c.id) {
                            n = p2.c;
                            if (n.party == p.c.get().party && Math.abs(p.c.x - n.x) <= temp.dx && Math.abs(p.c.y - n.y) <= temp.dy) {
                                n.p.setEffect(19, 0, 90000, param);
                            }
                        }
                    }
                    break;
                }
                break;
            }
            case 52: {
                param = p.c.get().getPramSkill(40)*1000;
                param += param*p.c.get().getPramSkill(66)/100;
                p.setEffect(20, 0, param, 0);
                if (p.c.get().party != null && p.c.tileMap != null) {
                    int i;
                    Player p2;
                    Char n;
                    for (i = 0; i < p.c.tileMap.players.size(); ++i) {
                        p2 = p.c.tileMap.players.get(i);
                        if (p2.c.id != p.c.id) {
                            n = p2.c;
                            if (n.party == p.c.get().party && Math.abs(p.c.get().x - n.x) <= temp.dx && Math.abs(p.c.get().y - n.y) <= temp.dy) {
                                n.p.setEffect(20, 0, param, 0);
                            }
                        }
                    }
                    break;
                }
                break;
            }
            case 58: {
                p.setEffect(11, 0, p.c.get().getPramSkill(64), 20000);
                break;
            }
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72: {
                if (p.c.timeRemoveCloneSave > System.currentTimeMillis()){
                    p.c.clone.islive = true;
                    p.c.clone = CloneCharacter.getClone(p.c);
                    p.c.clone.open(p.c.timeRemoveCloneSave, p.c.getPramSkill(71));
                    return;
                }
                if (p.c.timeRemoveClone > System.currentTimeMillis()){
                    p.c.clone.islive = true;
                    p.c.clone = CloneCharacter.getClone(p.c);
                    p.c.clone.open(p.c.timeRemoveClone, p.c.getPramSkill(71));
                    return;
                }
                if (p.c.timeRemoveClone <= System.currentTimeMillis() && p.c.quantityItemyTotal(545) <= 0) {
                    p.sendAddchatYellow("Không có đủ " + ItemTemplate.ItemTemplateId(545).name);
                    return;
                }
                p.c.clone.islive = true;
                p.c.clone = CloneCharacter.getClone(p.c);
                p.c.clone.open(System.currentTimeMillis() + 60000 * p.c.getPramSkill(68), p.c.getPramSkill(71));
                if (p.c.quantityItemyTotal(545) > 0) {
                    p.c.removeItemBags(545, 1);
                    return;
                }
                break;
            }
        }
    }

    public static void useSkillCloneBuff(Body body, int skilltemp) {
        Skill skill = body.getSkill(skilltemp);
        if(skill != null) {
            SkillOptionTemplate temp = SkillTemplate.Templates(skill.id, skill.point);
            if(skill.coolDown <= System.currentTimeMillis()) {
                skill.coolDown = System.currentTimeMillis() + (long)temp.coolDown;
                Player p = body.c.p;
                int param;
                switch (skilltemp) {
                    case 51:
                        param = body.getPramSkillClone(45);
                        param += body.getPramSkillClone(66);
                        p.setEffect(19, 0, 90000, param);
                        break;
                    case 52:
                        param = body.getPramSkillClone(40)*1000;
                        param += param*body.getPramSkillClone(66)/100;
                        p.setEffect(20, 0, param, 0);
                        break;
                }
            }
        }
    }

    public static void buffLive(Player p, Message m) {
        try {
            int idP = m.reader().readInt();
            m.cleanup();
            Char nj = p.c.tileMap.getNinja(idP);
            Skill skill = p.c.get().getSkill(p.c.get().CSkill);

            if (nj != null && nj.get().isDie && nj.mapid != 133 && nj.mapid != 111) {
                SkillOptionTemplate temp = SkillTemplate.Templates(skill.id, skill.point);
                if (skill.coolDown > System.currentTimeMillis() || Math.abs(p.c.get().x - nj.x) > temp.dx || Math.abs(p.c.get().y - nj.y) > temp.dy || p.c.get().mp < temp.manaUse) {
                    return;
                }
                p.c.get().upMP(-temp.manaUse);
                skill.coolDown = System.currentTimeMillis() + temp.coolDown;
                nj.p.liveFromDead();
                nj.p.setEffect(11, 0, 5000, p.c.get().getPramSkill(28));
            }
        } catch (Exception e) {
            Util.Debug("Error useSkill.java(237) : " + e.toString());
            return;
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }
}
