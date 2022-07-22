package com.hoiuc.server;

import cache.Part;
import cache.PartImage;
import com.hoiuc.assembly.*;
import com.hoiuc.assembly.Char;
import com.hoiuc.io.Message;
import com.hoiuc.io.Util;
import com.hoiuc.stream.ChienTruong;
import com.hoiuc.stream.Client;
import com.hoiuc.stream.Dun;
import com.hoiuc.stream.Server;
import com.hoiuc.stream.thiendiabang.ThienDiaData;
import com.hoiuc.template.ItemTemplate;
import com.hoiuc.template.MapTemplate;
import com.hoiuc.template.MobTemplate;
import com.hoiuc.template.SkillTemplate;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Service {
    protected static Message messageSubCommand(byte command) throws Exception {
        Message message = new Message(-30);
        message.writer().writeByte(command);
        return message;
    }

    protected static Message messageNotLogin(byte command) throws Exception {
        Message message = new Message(-29);
        message.writer().writeByte(command);
        return message;
    }

    protected static Message messageNotMap(byte command) throws Exception {
        Message message = new Message(-28);
        message.writer().writeByte(command);
        return message;
    }

    public static void sendMapInfo(Player player, Map map) {
        Message m = null;
        try {
            m = messageNotMap((byte)-109);
            m.writer().writeByte(map.template.tmw);
            m.writer().writeByte(map.template.tmh);
            for(int i = 0; i < map.template.maps.length; i++) {
                m.writer().writeByte(map.template.maps[i]);
            }
            m.writer().flush();
            player.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }
    
    private static byte[][] cache = new byte[5][];
    
    public static void sendMap(Player p) throws IOException, Exception {
        if (cache[1] == null) {
            cache[1] = getFile("cache/map");
        }
        Message ms = messageNotMap((byte) -121);
        DataOutputStream ds = ms.writer();
        ds.write(cache[1]);
        ds.flush();
        p.conn.sendMessage(ms);
        ms.cleanup();
    }
    
    public static void sendSkill(Player p) throws IOException, Exception {
        if (cache[3] == null) {
            cache[3] = getFile("cache/skill");
        }
        Message ms = messageNotMap((byte) -119);
        DataOutputStream ds = ms.writer();
        ds.write(cache[3]);
        ds.flush();
        p.conn.sendMessage(ms);
        ms.cleanup();
    }
    
    public static void sendItem(Player p) throws IOException, Exception {
        if (cache[4] == null) {
            cache[4] = getFile("cache/item");
        }
        Message ms = messageNotMap((byte) -119);
        DataOutputStream ds = ms.writer();
        ds.write(cache[4]);
        ds.flush();
        p.conn.sendMessage(ms);
        ms.cleanup();
    }
    
    public static void getPackMessage(Player p) throws IOException {
        Message msg = null;
        try {
            msg = messageNotMap((byte)(-123));
            msg.writer().writeByte(Manager.vsData);
            msg.writer().writeByte(Manager.vsMap);
            msg.writer().writeByte(Manager.vsSkill);
            msg.writer().writeByte(Manager.vsItem);
            final byte[] ab = GameSrc.loadFile("cache/request").toByteArray();
            msg.writer().write(ab);
            p.conn.sendMessage(msg);
            msg.cleanup();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }
    
    public static byte[] getFile(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            byte[] ab = new byte[fis.available()];
            fis.read(ab, 0, ab.length);
            fis.close();
            return ab;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    
    public static void createCacheItem(){
        try{ 
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bas);       
            dos.writeByte(Manager.vsItem);
            dos.writeByte(Manager.iOptionTemplates.length);
            for (short i = 0; i < Manager.iOptionTemplates.length; ++i) {
                dos.writeUTF(Manager.iOptionTemplates[i].name);
                dos.writeByte(Manager.iOptionTemplates[i].type);
            }
            dos.writeShort(Manager.itemTemplates.length);
            for (short j = 0; j < Manager.itemTemplates.length; ++j) {
                dos.writeByte(Manager.itemTemplates[j].type);
                dos.writeByte(Manager.itemTemplates[j].gender);
                dos.writeUTF(Manager.itemTemplates[j].name);
                dos.writeUTF(Manager.itemTemplates[j].description);
                dos.writeByte(Manager.itemTemplates[j].level);
                dos.writeShort(Manager.itemTemplates[j].iconID);
                dos.writeShort(Manager.itemTemplates[j].part);
                dos.writeBoolean(Manager.itemTemplates[j].isUpToUp);
            }
            byte[]ab = bas.toByteArray();
            saveFile("cache/item", ab);
            dos.close();
            bas.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void createCachePart() {
        try {
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bas);
            ByteArrayOutputStream parts = new ByteArrayOutputStream();
            DataOutputStream ds = new DataOutputStream(parts);
            ds = new DataOutputStream(parts);
            ds.writeShort(Manager.parts.size());
            for (Part p : Manager.parts) {
                ds.writeByte(p.type);
                for (PartImage pi : p.pi) {
                    ds.writeShort(pi.id);
                    ds.writeByte(pi.dx);
                    ds.writeByte(pi.dy);
                }
            }
            ds.flush();
            dos.writeInt(parts.toByteArray().length);
            dos.write(parts.toByteArray());           
            byte[]ab = bas.toByteArray();
            saveFile("cache/nj_part", ab);
            ds.close();
            parts.close();
            dos.close();
            bas.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void createCacheMap() {
        try {
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bas);
            dos.writeByte(Manager.vsMap);
            dos.writeByte(Manager.mapCache.length);
            for (short i = 0; i < Manager.mapCache.length; ++i) {
                dos.writeUTF(Manager.mapCache[i].mapName);
            }
            dos.writeByte(Npc.arrNpcTemplate.length);
            for (byte j = 0; j < Npc.arrNpcTemplate.length; ++j) {
                dos.writeUTF(Npc.arrNpcTemplate[j].name);
                dos.writeShort(Npc.arrNpcTemplate[j].headId);
                dos.writeShort(Npc.arrNpcTemplate[j].bodyId);
                dos.writeShort(Npc.arrNpcTemplate[j].legId);
                dos.writeByte(Npc.arrNpcTemplate[j].menu.length);
                for (short k = 0; k < Npc.arrNpcTemplate[j].menu.length; ++k) {
                    dos.writeByte(Npc.arrNpcTemplate[j].menu[k].length);
                    for (short m = 0; m < Npc.arrNpcTemplate[j].menu[k].length; ++m) {
                        dos.writeUTF(Npc.arrNpcTemplate[j].menu[k][m]);
                    }
                }
            }
            dos.writeByte(Mob.arrMobTemplate.length);
            for (short l = 0; l < Mob.arrMobTemplate.length; ++l) {
                dos.writeByte(Mob.arrMobTemplate[l].type);
                dos.writeUTF(Mob.arrMobTemplate[l].name);
                dos.writeInt(Mob.arrMobTemplate[l].hp);
                dos.writeByte(Mob.arrMobTemplate[l].rangeMove);
                dos.writeByte(Mob.arrMobTemplate[l].speed);
            }
            byte[]ab = bas.toByteArray();
            saveFile("cache/map", ab);
            dos.close();
            bas.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void saveFile(final String url, final byte[] ab) {
        try {
            final File f = new File(url);
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            final FileOutputStream fos = new FileOutputStream(url);
            fos.write(ab);
            fos.flush();
            fos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void restPoint(Char ninja) {
        if (ninja == null) {
            return;
        }
        Message msg = null;
        try {
            if(ninja.p.conn != null) {
                msg = new Message((byte)52);
                msg.writer().writeShort(ninja.x);
                msg.writer().writeShort(ninja.y);
                ninja.p.conn.sendMessage(msg);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void sendInfoChar(Player player, Player _p) {
        Message m = null;
        try {
            //m = messageSubCommand((byte)-120);
            m = new Message((byte)116);
            m.writer().writeInt(player.c.get().id);
            m.writer().writeUTF(player.c.clan.clanName);
            if (!player.c.clan.clanName.isEmpty()) {
                m.writer().writeByte(player.c.clan.typeclan);
            }
            m.writer().writeBoolean(false);
            m.writer().writeByte(player.c.get().typepk);
            m.writer().writeByte(player.c.get().nclass);
            m.writer().writeByte(player.c.gender);
            m.writer().writeShort(player.c.get().partHead());
            m.writer().writeUTF(player.c.name);
            m.writer().writeInt(player.c.get().hp);
            m.writer().writeInt(player.c.get().getMaxHP());
            m.writer().writeByte(player.c.get().level);
            m.writer().writeShort(player.c.get().Weapon());
            m.writer().writeShort(player.c.get().Body());
            m.writer().writeShort(player.c.get().Leg());
            m.writer().writeByte(-1);
            m.writer().writeShort(player.c.get().x);
            m.writer().writeShort(player.c.get().y);
            m.writer().writeShort(player.c.get().eff5buffHP());
            m.writer().writeShort(player.c.get().eff5buffMP());
            m.writer().writeByte(0);
            m.writer().writeBoolean(player.c.isHuman);
            m.writer().writeBoolean(player.c.isNhanban);
            m.writer().writeShort(player.c.get().partHead());
            m.writer().writeShort(player.c.get().Weapon());
            m.writer().writeShort(player.c.get().Body());
            m.writer().writeShort(player.c.get().Leg());

            m.writer().writeShort(player.c.get().ID_HAIR);
            m.writer().writeShort(player.c.get().ID_Body);
            m.writer().writeShort(player.c.get().ID_LEG);
            m.writer().writeShort(player.c.get().ID_WEA_PONE);
            m.writer().writeShort(player.c.get().ID_PP);
            m.writer().writeShort(player.c.get().ID_NAME);
            m.writer().writeShort(player.c.get().ID_HORSE);
            m.writer().writeShort(player.c.get().ID_RANK);
            m.writer().writeShort(player.c.get().ID_MAT_NA);
            m.writer().writeShort(player.c.get().ID_Bien_Hinh);
            m.writer().flush();
            _p.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void addItemToBagNinja(Char c, int id,boolean isUptoup, boolean isLock, int quantity, boolean isExpires, long expires) {
        Item it = ItemTemplate.itemDefault(id);
        it.isLock = isLock;
        it.quantity = quantity;
        it.isExpires = isExpires;
        if(expires != -1) {
            it.expires = System.currentTimeMillis() + expires;
        }
        c.addItemBag(isUptoup, it);
    }

    public static void startYesNoDlg(Player p, byte id, String str) {
        Message msg = null;

        try {
            msg = new Message((byte)107);
            msg.writer().writeByte(id);
            msg.writer().writeUTF(str);
            msg.writer().flush();
            p.conn.sendMessage(msg);
        } catch (Exception var8) {
            var8.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }

        }

    }

    public static void evaluateCave(Char nj) {
        Message msg = null;

        try {
            int luckyCase;
            if (nj.isHangDong6x == 1) {
                luckyCase = nj.pointCave / 20;
            } else {
                luckyCase = nj.pointCave / 10;
            }

            msg = messageNotMap((byte)-83);
            msg.writer().writeShort(nj.pointCave);
            msg.writer().writeShort(0);
            msg.writer().writeByte(0);
            msg.writer().writeShort(luckyCase);
            msg.writer().flush();
            nj.p.conn.sendMessage(msg);
        } catch (Exception var6) {
            var6.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }

        }

    }

    public static void evaluateCT(Char nj) {
        Message msg = null;

        try {
            boolean check = false;
            if(nj.isTakePoint > 0 && ChienTruong.bxhCT.containsKey(nj) && ChienTruong.finish) {
                check = true;
            }
            msg = messageNotMap((byte)-80);
            if(ChienTruong.review != null) {
                msg.writer().writeUTF(ChienTruong.review);
            } else {
                msg.writer().writeUTF("Chưa có thông tin!");
            }

            msg.writer().writeBoolean(check);
            msg.writer().flush();
            nj.p.conn.sendMessage(msg);
        } catch (Exception var6) {
            var6.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }

    }

    public static String checkPhe(int a) {
        if(a == 0) {
            return "(Bạch)";
        }
        return "(Hắc)";
    }

    public static String checkPoint(int a) {
        if(a < 200) {
            return "Học giả";
        } else if (a >= 200 && a < 600) {
            return "Hạ nhẫn";
        } else if (a >= 600 && a < 1500) {
            return "Trung nhẫn";
        } else if (a >= 1500 && a < 4000) {
            return "Thượng nhẫn";
        } else if (a >= 4000) {
            return "Nhẫn giả";
        }
        return "";
    }

    public static void updateCT() {
        ChienTruong.review = "Bạch giả: " + ChienTruong.pointBachGia + "\nHắc giả: " + ChienTruong.pointHacGia + "\n";
        if(ChienTruong.finish) {
            if(ChienTruong.pheWin == -1) {
                if(ChienTruong.pointBachGia > ChienTruong.pointHacGia) {
                    ChienTruong.pheWin = 0;
                } else if (ChienTruong.pointBachGia < ChienTruong.pointHacGia) {
                    ChienTruong.pheWin = 1;
                } else {
                    ChienTruong.pheWin = Util.nextInt(0,1);
                }
            }
            if(ChienTruong.pheWin == 0) {
                ChienTruong.review += "Phe Bạch Giả đã giành chiến thắng trong chiến trường lần này.\n";
            } else if (ChienTruong.pheWin == 1) {
                ChienTruong.review += "Phe Hắc Giả đã giành chiến thắng trong chiến trường lần này.\n";
            }
        }
        Char key;
        Integer value;
        int i = 1;
        for(java.util.Map.Entry<Char, Integer> entry : ChienTruong.bxhCT.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            ChienTruong.review += i+" . " + key.name + ": " + value + " điểm " + Service.checkPhe(key.pheCT) +"\n";
            if(ChienTruong.start) {
                ChienTruong.review += "Danh hiệu: " + Service.checkPoint(value) + "\n";
            }
            i++;
            if(i > 10 && ChienTruong.finish) {
                break;
            } else if(ChienTruong.start && i > 40) {
                break;
            }
        }
    }

    public static void sendPointGTC(Char nj, int point) {
        Message msg = null;
        try {
            if(nj.tileMap.map.giaTocChien != null) {
                if(nj.tileMap.map.giaTocChien.gt1.contains(nj)) {
                    nj.tileMap.map.giaTocChien.pointClan1 += point;
                } else {
                    nj.tileMap.map.giaTocChien.pointClan2 += point;
                }
            }
            msg = messageNotMap((byte)-81);
            msg.writer().writeShort(nj.pointGTC);
            msg.writer().flush();
            nj.p.conn.sendMessage(msg);
        } catch (Exception var6) {
            var6.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }

        }
    }

    public static void updatePointCT(Char nj, int point) {
        Message msg = null;
        try {
            if(ChienTruong.bxhCT.containsKey(nj)) {
                ChienTruong.bxhCT.replace(nj, nj.pointCT);
            }
            if(nj.pheCT == 0) {
                ChienTruong.pointBachGia += point;
            } else if(nj.pheCT == 1) {
                ChienTruong.pointHacGia += point;
            }
            msg = messageNotMap((byte)-81);
            msg.writer().writeShort(nj.pointCT);
            msg.writer().flush();
            nj.p.conn.sendMessage(msg);
        } catch (Exception var6) {
            var6.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }

        }

    }

    public static void inviteInClanDun(Char nj, String name) {
        Message msg = null;
        try {
            //msg = messageNotMap((byte)-87);
            msg = messageSubCommand((byte)-87);;
            //msg.writer().writeUTF(name);
            msg.writer().flush();
            nj.p.conn.sendMessage(msg);
        } catch (Exception var6) {
            var6.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void sendclonechar(Player p, Player top) {
        try {
            Message m = new Message(3);
            m.writer().writeInt(p.c.clone.id);
            m.writer().writeUTF("");
            m.writer().writeBoolean(false);
            m.writer().writeByte(p.c.clone.typepk);
            m.writer().writeByte(p.c.clone.nclass);
            m.writer().writeByte(p.c.clone.c.gender);
            m.writer().writeShort(p.c.clone.partHead());
            m.writer().writeUTF(p.c.clone.c.name);
            m.writer().writeInt(p.c.clone.hp);
            m.writer().writeInt(p.c.clone.getMaxHP());
            m.writer().writeByte(p.c.clone.level);
            m.writer().writeShort(p.c.clone.Weapon());
            m.writer().writeShort(p.c.clone.Body());
            m.writer().writeShort(p.c.clone.Leg());
            m.writer().writeByte(-1);
            m.writer().writeShort(p.c.clone.x);
            m.writer().writeShort(p.c.clone.y);
            m.writer().writeShort(p.c.eff5buffHP());
            m.writer().writeShort(p.c.eff5buffMP());
            m.writer().writeByte(0);
            m.writer().writeBoolean(p.c.clone.isHuman);
            m.writer().writeBoolean(p.c.clone.isNhanban);
            m.writer().writeShort(-1);
            m.writer().writeShort(-1);
            m.writer().writeShort(-1);
            m.writer().writeShort(-1);
 m.writer().writeShort(p.c.clone.ID_HAIR);
            m.writer().writeShort(p.c.clone.ID_Body);
            m.writer().writeShort(p.c.clone.ID_LEG);
            m.writer().writeShort(p.c.clone.ID_WEA_PONE);
            m.writer().writeShort(p.c.clone.ID_PP);
            m.writer().writeShort(p.c.clone.ID_NAME);
            m.writer().writeShort(p.c.clone.ID_HORSE);
            m.writer().writeShort(p.c.clone.ID_RANK);
            m.writer().writeShort(p.c.clone.ID_MAT_NA);
            m.writer().writeShort(p.c.clone.ID_Bien_Hinh);
            m.writer().flush();
            top.conn.sendMessage(m);
            m.cleanup();
            p.getMobMeClone();
            if (p.c.clone.mobMe != null) {
                m = new Message(-30);
                m.writer().writeByte(-68);
                m.writer().writeInt(p.c.clone.id);
                m.writer().writeByte(p.c.clone.mobMe.templates.id);
                m.writer().writeByte(p.c.clone.mobMe.isboss ? 1 : 0);
                m.writer().flush();
                top.conn.sendMessage(m);
                m.cleanup();
            }
            if(p.c.tileMap != null) {
                p.c.tileMap.sendCoat(p.c.clone, top);
                p.c.tileMap.sendGlove(p.c.clone, top);
                p.c.tileMap.sendMounts(p.c.clone, top);
            } else if(p.c.tdbTileMap != null) {
                p.c.tdbTileMap.sendCoat(p.c.clone, top);
                p.c.tdbTileMap.sendGlove(p.c.clone, top);
                p.c.tdbTileMap.sendMounts(p.c.clone, top);
            }

        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    public static void setHPMob(Char nj, int mobid, int hp) {
        Message msg = null;

        try {
            msg = new Message(51);
            msg.writer().writeByte(mobid);
            msg.writer().writeInt(0);
            msg.writer().flush();
            nj.p.conn.sendMessage(msg);
        } catch (Exception var8) {
            var8.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }

    }

    public static void CharViewInfo(Player p) {
        Service.CharViewInfo(p, true);
    }

    public static void CharViewInfo(Player p, boolean sendEff) {
        Message msg = null;
        try {
            Char c = p.c;
            msg = messageSubCommand((byte)115);
            msg.writer().writeInt(c.get().id);
            msg.writer().writeUTF(c.clan.clanName);
            if (!c.clan.clanName.isEmpty()) {
                msg.writer().writeByte(c.clan.typeclan);
            }

            msg.writer().writeByte(c.taskId = 50);
            msg.writer().writeByte(c.gender);
            msg.writer().writeShort(c.get().partHead());
            msg.writer().writeByte(c.get().speed());
            msg.writer().writeUTF(c.name);
            msg.writer().writeByte(c.get().pk);
            msg.writer().writeByte(c.get().typepk);
            msg.writer().writeInt(c.get().getMaxHP());
            msg.writer().writeInt(c.get().hp);
            msg.writer().writeInt(c.get().getMaxMP());
            msg.writer().writeInt(c.get().mp);
            msg.writer().writeLong(c.get().exp);
            msg.writer().writeLong(c.get().expdown);
            msg.writer().writeShort(c.get().eff5buffHP());
            msg.writer().writeShort(c.get().eff5buffMP());
            msg.writer().writeByte(c.get().nclass);
            msg.writer().writeShort(c.get().ppoint);
            msg.writer().writeShort(c.get().potential0);
            msg.writer().writeShort(c.get().potential1);
            msg.writer().writeInt(c.get().potential2);
            msg.writer().writeInt(c.get().potential3);
            msg.writer().writeShort(c.get().spoint);
            msg.writer().writeByte(c.get().skill.size());

            for(short i = 0; i < c.get().skill.size(); ++i) {
                msg.writer().writeShort(SkillTemplate.Templates(((Skill)c.get().skill.get(i)).id, ((Skill)c.get().skill.get(i)).point).skillId);
            }

            msg.writer().writeInt(c.xu);
            msg.writer().writeInt(c.yen);
            msg.writer().writeInt(p.luong);
            msg.writer().writeByte(c.maxluggage);

            byte j;
            for(j = 0; j < c.maxluggage; ++j) {
                if (c.ItemBag[j] == null) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(c.ItemBag[j].id);
                    msg.writer().writeBoolean(c.ItemBag[j].isLock);
                    if (ItemTemplate.isTypeBody(c.ItemBag[j].id) || ItemTemplate.isTypeMounts(c.ItemBag[j].id) || ItemTemplate.isTypeNgocKham(c.ItemBag[j].id)) {
                        msg.writer().writeByte(c.ItemBag[j].upgrade);
                    }
                    msg.writer().writeBoolean(c.ItemBag[j].isExpires);
                    msg.writer().writeShort(c.ItemBag[j].quantity);
                }
            }

            Item itemB;
            for(j = 0; j < 16; ++j) {
                itemB = c.get().ItemBody[j];
                if (itemB != null) {
                    msg.writer().writeShort(itemB.id);
                    msg.writer().writeByte(itemB.upgrade);
                    msg.writer().writeByte(itemB.sys);
                } else {
                    msg.writer().writeShort(-1);
                }
            }

            msg.writer().writeBoolean(c.get().isHuman);
            msg.writer().writeBoolean(c.get().isNhanban);
            msg.writer().writeShort(c.get().partHead());
            msg.writer().writeShort(c.get().Weapon());
            msg.writer().writeShort(c.get().Body());
            msg.writer().writeShort(c.get().Leg());

            msg.writer().writeShort(c.get().ID_HAIR);
            msg.writer().writeShort(c.get().ID_Body);
            msg.writer().writeShort(c.get().ID_LEG);
            msg.writer().writeShort(c.get().ID_WEA_PONE);
            msg.writer().writeShort(c.get().ID_PP);
            msg.writer().writeShort(c.get().ID_NAME);
            msg.writer().writeShort(c.get().ID_HORSE);
            msg.writer().writeShort(c.get().ID_RANK);
            msg.writer().writeShort(c.get().ID_MAT_NA);
            msg.writer().writeShort(c.get().ID_Bien_Hinh);

            for(j = 16; j < 32; ++j) {
                itemB = c.get().ItemBody[j];
                if (itemB != null) {
                    msg.writer().writeShort(itemB.id);
                    msg.writer().writeByte(itemB.upgrade);
                    msg.writer().writeByte(itemB.sys);
                } else {
                    msg.writer().writeShort(-1);
                }
            }
            msg.writer().flush();
            p.conn.sendMessage(msg);
            msg.cleanup();
            p.getMobMe();
            if(sendEff) {
                for(j = 0; j < c.get().veff.size(); ++j) {
                    p.addEffectMessage(c.get().veff.get(j));
                }
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }

        }
    }

    public static void Mobstart(Player p, int mobid, int dame, boolean flag) {
        Message msg = null;
        try {
            msg = new Message(-4);
            msg.writer().writeByte(mobid);
            msg.writer().writeInt(dame);
            msg.writer().writeBoolean(flag);
            msg.writer().flush();
            p.conn.sendMessage(msg);
        } catch (Exception var9) {
            var9.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }

        }
    }

    public static void Mobstart(Player p, int mobid, int hp, int dame, boolean flag, int levelboss, int hpmax) {
        Message msg = null;

        try {
            msg = new Message(-1);
            msg.writer().writeByte(mobid);
            msg.writer().writeInt(hp);
            msg.writer().writeInt(dame);
            msg.writer().writeBoolean(flag);
            msg.writer().writeByte(levelboss);
            msg.writer().writeInt(hpmax);
            msg.writer().flush();
            p.conn.sendMessage(msg);
        } catch (Exception var12) {
            var12.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }

        }

    }

    public static void PlayerAttack(Player p, Mob[] mob, Body b) {
        Message msg = null;

        try {
            msg = new Message(60);
            msg.writer().writeInt(b.id);
            msg.writer().writeByte(b.CSkill);

            for(byte i = 0; i < mob.length; ++i) {
                if (mob[i] != null) {
                    msg.writer().writeByte(mob[i].id);
                }
            }

            msg.writer().flush();
            p.conn.sendMessage(msg);
        } catch (Exception var8) {
            var8.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }

        }

    }

    public static void PlayerAttack(Char c, int charID, byte skill, Mob[] arrMob, Char[] arrChar) {
        Message msg = null;

        try {
            msg = new Message((byte)4);
            msg.writer().writeInt(charID);
            msg.writer().writeByte(skill);
            byte num = 0;

            byte i;
            for(i = 0; i < arrMob.length; ++i) {
                if (arrMob[i] != null) {
                    num++;
                }
            }

            msg.writer().writeByte(num);

            for(i = 0; i < arrMob.length; ++i) {
                if (arrMob[i] != null) {
                    msg.writer().writeByte(arrMob[i].id);
                }
            }

            for(i = 0; i < arrChar.length; ++i) {
                if (arrChar[i] != null) {
                    msg.writer().writeInt(arrChar[i].id);
                }
            }
            msg.writer().flush();
            c.p.conn.sendMessage(msg);
        } catch (Exception var11) {
            var11.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }

        }

    }

    public static void TestInvite(Char c, int charId) {
        Message msg = null;

        try {
            msg = new Message((byte)65);
            msg.writer().writeInt(charId);
            msg.writer().flush();
            c.p.conn.sendMessage(msg);
        } catch (Exception var7) {
            var7.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }

        }

    }

    public static void TestAccept(Char c, int playerId, int playerId2) {
        Message msg = null;

        try {
            msg = new Message((byte)66);
            msg.writer().writeInt(playerId);
            msg.writer().writeInt(playerId2);
            msg.writer().flush();
            c.p.conn.sendMessage(msg);
        } catch (Exception var8) {
            var8.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }

        }

    }

    public static void TestEnd(Char c, int playerId, int playerId2, int num) {
        Message msg = null;

        try {
            msg = new Message((byte)67);
            msg.writer().writeInt(playerId);
            msg.writer().writeInt(playerId2);
            if (num > 0) {
                msg.writer().writeInt(num);
            }
            msg.writer().flush();
            c.p.conn.sendMessage(msg);
        } catch (Exception var9) {
            var9.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }

        }

    }

    public static void AddCuuSat(Char c, int charId) {
        Message msg = null;

        try {
            msg = new Message((byte)68);
            msg.writer().writeInt(charId);
            msg.writer().flush();
            c.p.conn.sendMessage(msg);
        } catch (Exception var7) {
            var7.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }

        }

    }

    public static void MeCuuSat(Char c, int charId) {
        Message msg = null;

        try {
            msg = new Message((byte)69);
            msg.writer().writeInt(charId);
            msg.writer().flush();
            c.p.conn.sendMessage(msg);
        } catch (Exception var7) {
            var7.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }

        }

    }

    public static void ClearCuuSat(Char c, int charId) {
        Message msg = null;

        try {
            msg = new Message((byte)70);
            if (c.id != charId) {
                msg.writer().writeInt(charId);
            }
            msg.writer().flush();
            c.p.conn.sendMessage(msg);
        } catch (Exception var7) {
            var7.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }

        }

    }

    public static void ChangTypePkId(Char c, byte type) {
        Message m = null;
        try {
            m = new Message(-30);
            m.writer().writeByte(-92);
            m.writer().writeInt(c.id);
            m.writer().writeByte(type);
            m.writer().flush();
            c.p.conn.sendMessage(m);
        } catch (Exception var3) {
            var3.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public static MobTemplate getMobIdByLevel(int level) {

        synchronized(Server.maps) {
            Map map;
            short i;
            int j;
            MobTemplate mobData = null;

            for (i = 0; i < Server.maps.length; ++i) {
                map = Server.maps[i];
                if (map != null && map.mapThuong()) {
                    for (j = 0; j < map.template.arMobid.length; j++) {
                        if(level >= 100 && map.template.arrMoblevel[j] >= 95+ Util.nextInt(3,5) && !map.template.arrisboss[j]) {
                            mobData = MobTemplate.getMob(map.template.arMobid[j]);
                        } else if(map.template.arrMoblevel[j] >= 10 && Math.abs(map.template.arrMoblevel[j] - level) <= 5 && !map.template.arrisboss[j]) {
                            mobData = MobTemplate.getMob(map.template.arMobid[j]);
                        }
                        if(mobData != null) {
                            return mobData;
                        }
                    }
                }
            }

            return null;
        }
    }

    public static MapTemplate getMobMapId(int id) {

        synchronized(Server.maps) {
            Map map;
            short i;
            int j;
            for (i = 0; i < Server.maps.length; ++i) {
                map = Server.maps[i];
                if (map != null ) {
                    for (j = 0; j < map.template.arMobid.length; j++) {
                        if(map.template.arMobid[j] == id) {
                            return map.template;
                        }
                    }
                }
            }
            return null;
        }
    }

    public static MobTemplate getMobIdTaThu(int level) {
        synchronized (Server.maps) {
            MobTemplate mobData = null;
            int i;
            if(level>=30 && level<40) {
                for(i = 0; i < GameSrc.arrModIdTaThu30.length; i++) {
                    if(Math.abs(GameSrc.arrModIdTaThu30[i]-level) <= 2) {
                        mobData = MobTemplate.getMob(GameSrc.arrModIdTaThu30[i]);
                    }
                }
            }else if(level>=40 && level<50) {
                for(i = 0; i < GameSrc.arrModIdTaThu40.length; i++) {
                    if(Math.abs(GameSrc.arrModIdTaThu40[i]-level) <= 2) {
                        mobData = MobTemplate.getMob(GameSrc.arrModIdTaThu40[i]);
                    }
                }
            }else if(level>=50 && level<60) {
                for(i = 0; i < GameSrc.arrModIdTaThu50.length; i++) {
                    if(Math.abs(GameSrc.arrModIdTaThu50[i]-level) <= 2) {
                        mobData = MobTemplate.getMob(GameSrc.arrModIdTaThu50[i]);
                    }
                }
            }else if(level>=60 && level <70) {
                for(i = 0; i < GameSrc.arrModIdTaThu60.length; i++) {
                    if(Math.abs(GameSrc.arrModIdTaThu60[i]-level) <= 2) {
                        mobData = MobTemplate.getMob(GameSrc.arrModIdTaThu60[i]);
                    }
                }
            }else if(level>=70 && level <80) {
                for(i = 0; i < GameSrc.arrModIdTaThu70_2.length; i++) {
                    if(Math.abs(GameSrc.arrModIdTaThu70_2[i]-level) <= 3) {
                        mobData = MobTemplate.getMob(GameSrc.arrModIdTaThu70[i]);
                    }
                }
            }else if(level>=80 && level<97) {
                for(i = 0; i < GameSrc.arrModIdTaThu80_2.length; i++) {
                    if(Math.abs(GameSrc.arrModIdTaThu80_2[i]-level) <= 8) {
                        mobData = MobTemplate.getMob(GameSrc.arrModIdTaThu80[i]);
                    }
                }
            }else if(level>=97) {
                mobData = MobTemplate.getMob(GameSrc.arrModIdTaThu100[0]);
            }

            if(mobData != null) {
                return mobData;
            }
            return null;
        }
    }

    public static MapTemplate getMobMapIdTaThu(int id) {
        synchronized (Server.maps) {
            Map map;
            short i;
            int j;
            for (i = 0; i < Server.maps.length; ++i) {
                map = Server.maps[i];
                if (map != null ) {
                    for (j = 0; j < map.template.arMobid.length; j++) {
                        if(map.template.arMobid[j] == id && map.template.arrLevelboss[j] == 3) {
                            return map.template;
                        }
                    }
                }
            }
            return null;
        }
    }

    public static void getTaskOrder(Char c, byte taskId) {
        try {
            Message m = new Message(96);
            m.writer().writeByte(taskId);
            if (taskId == 0) {
                m.writer().writeInt(c.taskHangNgay[1]);
                m.writer().writeInt(c.taskHangNgay[2]);
                m.writer().writeUTF("Nhiệm vụ hàng Ngay:");
                m.writer().writeUTF("Hãy hoàn thành nhiệm vụ, sau đó trở về gặp NPC Rikudou để nhận thưởng.");
                m.writer().writeByte(c.taskHangNgay[3]);
                m.writer().writeByte(c.taskHangNgay[4]);
            } else if (taskId == 1) {
                m.writer().writeInt(c.taskTaThu[1]);
                m.writer().writeInt(c.taskTaThu[2]);
                m.writer().writeUTF("Nhiệm vụ tà thú:");
                m.writer().writeUTF("Hãy hoàn thành nhiệm vụ, sau đó trở về gặp NPC Rikudou để nhận thưởng.");
                m.writer().writeByte(c.taskTaThu[3]);
                m.writer().writeByte(c.taskTaThu[4]);
            }

            m.writer().flush();
            c.p.conn.sendMessage(m);
            m.cleanup();
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    public static void updateTaskOrder(Char c, byte taskId) {
        try {
            Message m = new Message(97);
            m.writer().writeByte(taskId);
            if (taskId == 0) {
                m.writer().writeInt(c.taskHangNgay[1]);
            } else if (taskId == 1) {
                m.writer().writeInt(c.taskTaThu[1]);
            }

            m.writer().flush();
            c.p.conn.sendMessage(m);
            m.cleanup();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static void clearTaskOrder(Char c, byte taskId) {
        try {
            Message m = new Message(98);
            m.writer().writeByte(taskId);
            m.writer().flush();
            c.p.conn.sendMessage(m);
            m.cleanup();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static void sendItemToAuction(Char c, byte index, int xu) {
        Message m = null;
        try {
            m = new Message(102);
            m.writer().writeByte(index);
            m.writer().writeInt(xu);
            m.writer().flush();
            c.p.conn.sendMessage(m);
        } catch (Exception var2) {
            var2.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public static void viewItemToAuction(Player player, int index, Item item) {
        Message m = null;
        try {
            m = new Message(104);
            m.writer().writeInt(index);
            m.writer().writeInt(item.saleCoinLock);
            if(ItemTemplate.isTypeBody(item.id) || ItemTemplate.isTypeNgocKham(item.id)) {
                m.writer().writeByte(item.upgrade);
                m.writer().writeByte(item.sys);
                for(Option op : item.options) {
                    m.writer().writeByte(op.id);
                    m.writer().writeInt(op.param);
                }
            }
            m.writer().flush();
            player.conn.sendMessage(m);
        } catch (Exception var2) {
            var2.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public static void sendWait(Char c, String str) {
        try {
            Message m = messageSubCommand((byte)-74);
            m.writer().writeUTF(str);
            m.writer().flush();
            c.p.conn.sendMessage(m);
            m.cleanup();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static void openMenuBox(Player p) {
        Message m = null;
        try {
            p.menuCaiTrang = 0;
            p.requestItem(4);
            m = new Message(31);
            m.writer().writeInt(p.c.xuBox);
            m.writer().writeByte(p.c.ItemBox.length);
            for (Item item : p.c.ItemBox) {
                if (item != null) {
                    m.writer().writeShort(item.id);
                    m.writer().writeBoolean(item.isLock);
                    if (ItemTemplate.isTypeBody(item.id) || ItemTemplate.isTypeNgocKham(item.id)) {
                        m.writer().writeByte(item.upgrade);
                    }
                    m.writer().writeBoolean(item.isExpires);
                    m.writer().writeShort(item.quantity);
                }
                else {
                    m.writer().writeShort(-1);
                }
            }
            m.writer().flush();
            p.conn.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }


    }

    public static void openMenuBST(Player p) {
        Message m = null;
        try {
            p.menuCaiTrang = 1;
            Service.sendTileAction(p, (byte)4, "Bộ Sưu tập", "Sử dụng");
            m = new Message(31);
            m.writer().writeInt(p.c.xuBox);
            m.writer().writeByte(p.c.ItemBST.length);
            for (Item item : p.c.ItemBST) {
                if (item != null) {
                    m.writer().writeShort(item.id);
                    m.writer().writeBoolean(item.isLock);
                    if (ItemTemplate.isTypeBody(item.id) || ItemTemplate.isTypeNgocKham(item.id)) {
                        m.writer().writeByte(item.upgrade);
                    }
                    m.writer().writeBoolean(item.isExpires);
                    m.writer().writeShort(item.quantity);
                }
                else {
                    m.writer().writeShort(-1);
                }
            }
            m.writer().flush();
            p.conn.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }


    }

    public static void openMenuCaiTrang(Player p) {
        Message m = null;
        try {
            p.menuCaiTrang = 2;
            Service.sendTileAction(p, (byte)4, "Cải trang", "Sử dụng");
            m = new Message(31);
            m.writer().writeInt(p.c.xuBox);
            m.writer().writeByte(p.c.ItemCaiTrang.length);
            for (Item itemCT : p.c.ItemCaiTrang) {
                if (itemCT != null) {
                    m.writer().writeShort(itemCT.id);
                    m.writer().writeBoolean(itemCT.isLock);
                    if (ItemTemplate.isTypeBody(itemCT.id) || ItemTemplate.isTypeNgocKham(itemCT.id)) {
                        m.writer().writeByte(itemCT.upgrade);
                    }
                    m.writer().writeBoolean(itemCT.isExpires);
                    m.writer().writeShort(itemCT.quantity);
                }
                else {
                    m.writer().writeShort(-1);
                }
            }
            m.writer().flush();
            p.conn.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }


    }

    public static void sendTileAction(Player player,byte typeUI, String title, String action) {
        Message m = null;
        try {
            if(player.conn != null) {
                m = new Message(30);
                m.writer().writeByte(typeUI);
                m.writer().writeUTF(title);
                m.writer().writeUTF(action);
                m.writer().flush();
                player.conn.sendMessage(m);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public static void sendPleaseClan(Char c, int id) {
        try {
            Message m = messageSubCommand((byte)-61);
            m.writer().writeInt(id);
            m.writer().flush();
            c.p.conn.sendMessage(m);
            m.cleanup();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static void addFriend(Char _char, String friendName, byte type) {
        Message msg = null;
        try {
            msg = new Message((byte)84);
            msg.writer().writeUTF(friendName);
            msg.writer().writeByte(type);
            _char.p.conn.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null)
                msg.cleanup();
        }
    }

    public static void removeFriend(Char _char, String name) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte)-83);
            msg.writer().writeUTF(name);
            _char.p.conn.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null)
                msg.cleanup();
        }
    }

    public static void removeEnemies(Char _char, String name) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte)-82);
            msg.writer().writeUTF(name);
            _char.p.conn.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null)
                msg.cleanup();
        }
    }

    public static void requestFriend(Char _char) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte)-85);
            Friend friend;
            Char player;
            for (short i = 0; i < _char.vFriend.size(); i = (short)(i + 1)) {
                friend = _char.vFriend.get(i);
                if (friend != null && friend.type != 2) {
                    msg.writer().writeUTF(friend.friendName);
                    byte type = friend.type;
                    if (friend.type == 1) {
                        player = Client.gI().getNinja(friend.friendName);
                        if (player != null)
                            type = 3;
                    }
                    msg.writer().writeByte(type);
                }
            }
            _char.p.conn.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null)
                msg.cleanup();
        }
    }

    public static void requestEnemies(Char _char) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte)-84);
            Friend friend;
            Char player;
            for (short i = 0; i < _char.vFriend.size(); i = (short)(i + 1)) {
                friend = _char.vFriend.get(i);
                if (friend != null && friend.type == 2) {
                    msg.writer().writeUTF(friend.friendName);
                    byte type = friend.type;
                    player = Client.gI().getNinja(friend.friendName);
                    if (player != null)
                        type = 3;
                    msg.writer().writeByte(type);
                }
            }
            _char.p.conn.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null)
                msg.cleanup();
        }
    }

    protected static void FriendInvate(Char _char, String charName) {
        Message msg = null;
        try {
            msg = new Message((byte)59);
            msg.writer().writeUTF(charName);
            _char.p.conn.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null)
                msg.cleanup();
        }
    }

    public static void ServerMessage(Char c, String text) {
        Message msg = null;
        try {
            if (text.length() > 0) {
                msg = new Message((byte)-24);
                msg.writer().writeUTF(text);
                msg.writer().flush();
                c.p.conn.sendMessage(msg);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        } finally {
            if(msg != null) {
                msg.cleanup();
            }
        }

    }

    public static void inviteParty(Char c, String name, int num) {
        try {
            Message msg = new Message((byte)79);
            msg.writer().writeInt(num);
            msg.writer().writeUTF(name);
            msg.writer().flush();
            c.p.conn.sendMessage(msg);
            msg.cleanup();
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public static void pleaseInputParty(Char c, String name) {
        Message msg = null;
        try {
            msg = new Message((byte)23);
            msg.writer().writeUTF(name);
            msg.writer().flush();
            c.p.conn.sendMessage(msg);

        } catch (Exception var3) {
            var3.printStackTrace();
        } finally {
            if(msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void PlayerInParty(Char c, Party party) {
        Message msg = null;
        try {
            msg = new Message((byte)82);
            msg.writer().writeBoolean(party.isLock);
            byte i;
            for(i = 0; i < party.numPlayer; ++i) {
                msg.writer().writeInt((party.aChar.get(i)).id);
                msg.writer().writeByte((party.aChar.get(i)).nclass);
                msg.writer().writeUTF((party.aChar.get(i)).name);
            }
            msg.writer().flush();
            c.p.conn.sendMessage(msg);
        } catch (Exception var4) {
            var4.printStackTrace();
        } finally {
            if(msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void OutParty(Char c) {
        try {
            Message msg = new Message((byte)83);
            msg.writer().flush();
            c.p.conn.sendMessage(msg);
            msg.cleanup();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public static void findParty(Char c) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte)-77);
            for(short i = 0; i < c.tileMap.numParty; ++i) {
                if (c.tileMap.aParty.get(i) != null) {
                    Char player = ((Party)c.tileMap.aParty.get(i)).findChar(((Party)c.tileMap.aParty.get(i)).charID);
                    if (player != null) {
                        msg.writer().writeByte(player.nclass);
                        msg.writer().writeByte(player.level);
                        msg.writer().writeUTF(player.name);
                        msg.writer().writeByte(((Party)c.tileMap.aParty.get(i)).numPlayer);
                    }
                }
            }
            msg.writer().flush();
            c.p.conn.sendMessage(msg);
        } catch (Exception var4) {
            var4.printStackTrace();
        } finally {
            if(msg != null) {
                msg.cleanup();
            }
        }

    }

    public static void lockParty(Char c, boolean isLock) {
        try {
            Message msg = messageSubCommand((byte)-76);
            msg.writer().writeBoolean(isLock);
            msg.writer().flush();
            c.p.conn.sendMessage(msg);
            msg.cleanup();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static void sendLoiDaiList(Char c) {
        try {
            Message m = new Message(100);
            m.writer().writeByte((byte)Dun.duns.size());

            for(int i = 0; i < Dun.duns.size(); ++i) {
                if (Dun.duns.get(i) != null) {
                    m.writer().writeByte((byte)((Dun)Dun.duns.get(i)).dunID);
                    m.writer().writeUTF(((Dun)Dun.duns.get(i)).name1 + " (" + ((Dun)Dun.duns.get(i)).lv1 + ")");
                    m.writer().writeUTF(((Dun)Dun.duns.get(i)).name2 + " (" + ((Dun)Dun.duns.get(i)).lv2 + ")");
                }
            }

            m.writer().flush();
            c.p.conn.sendMessage(m);
            m.cleanup();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static void sendInputDialog(Player user, short typeInput, String content) {
        try {
            Message message = new Message(92);
            message.writer().writeUTF(content);
            message.writer().writeShort(typeInput);
            message.writer().flush();
            user.conn.sendMessage(message);
            message.cleanup();
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public static void KhoaTaiKhoan(Player p) {
        sendInputDialog(p, (short)9999, "Nhập tên người chơi muốn khoá");
    }

    public static void AutoSaveData() {
        Player player = null;
        ClanManager clan = null;
        int i;
        synchronized(Client.gI().conns) {
            for(i = 0; i < Client.gI().conns.size(); ++i) {
                if (Client.gI().conns.get(i) != null && (Client.gI().conns.get(i)).player != null) {
                    player = (Client.gI().conns.get(i)).player;
                    player.flush();
                    if (player.c != null) {
                        player.c.flush();
                        if (player.c.clone != null) {
                            player.c.clone.flush();
                        }
                    }
                }
            }
        }

        synchronized(ClanManager.entrys) {
            for(i = 0; i < ClanManager.entrys.size(); ++i) {
                if (ClanManager.entrys.get(i) != null) {
                    clan = (ClanManager)ClanManager.entrys.get(i);
                    clan.flush();
                }
            }
        }

        Rank.init();
    }


    public static void ShowAdmin(Player p) throws IOException {
        p.typemenu = 9999;
        Menu.doMenuArray(p, new String[]{"Bảo trì Server", "Khoá tài khoản người chơi", "Cập nhật BXH cao thủ", "Xem info map, vị trí", "Cộng Xu", "Cộng Lượng", "Cộng Yên", "Tăng level", "Tăng điểm tiềm năng", "Tăng kỹ năng", "Cập nhật DATA", "Đăng thông báo", "Kiểm tra số người chơi", "Clear Clone Login", "Clear Session", "Thay đổi tăng exp", "CHECK RHB", "Cập nhật Thông báo", "Rương Huyền bí", "Rương Bạch ngân", "Bật/Tắt nhận quà"});
    }

    public static void mess_30_106(Player player){
        Message msg = null;
        try {
            msg = messageSubCommand((byte)-106);;
            msg.writer().flush();
            player.conn.sendMessage(msg);
        } catch (Exception var6) {
            var6.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void mess_30_105(Player player, int xu){
        Message msg = null;
        try {
            msg = messageSubCommand((byte)-105);;
            msg.writer().writeInt(xu);
            msg.writer().flush();
            player.conn.sendMessage(msg);
        } catch (Exception var6) {
            var6.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void mess_30_104(Player player, int xu){
        Message msg = null;
        try {
            msg = messageSubCommand((byte)-104);;
            msg.writer().writeInt(xu);
            msg.writer().flush();
            player.conn.sendMessage(msg);
        } catch (Exception var6) {
            var6.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void mess_30_63(Player player, Player p){
        Message msg = null;
        try {
            msg = messageSubCommand((byte)-63);;
            msg.writer().writeInt(player.c.get().id);
            msg.writer().writeUTF(player.c.clan.clanName);
            msg.writer().flush();
            p.conn.sendMessage(msg);
        } catch (Exception var6) {
            var6.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void mess_28_95(Player player, String a){
        Message msg = null;
        try {
            msg = messageNotMap((byte)-95);;
            msg.writer().writeUTF(a);
            msg.writer().flush();
            player.conn.sendMessage(msg);
        } catch (Exception var6) {
            var6.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void serverChat(String name, String s) {
        Message m = null;
        try {
            m = new Message(-21);
            m.writer().writeUTF(name);
            m.writer().writeUTF(s);
            m.writer().flush();
            Client.gI().NinjaMessage(m);
            m.cleanup();
        } catch (Exception var3) {
            var3.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void publicChat(Player p, String s) {
        Message m = null;
        try {
            m = new Message(-23);
            m.writer().writeUTF(s);
            m.writer().flush();
            p.conn.sendMessage(m);
        } catch (Exception var3) {
            var3.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void leaveItemBackground(Player player,ItemMap item, int index) {
        Message m= null;
        try {
            m = new Message(-12);
            m.writer().writeByte(index);
            m.writer().writeShort(item.itemMapId);
            m.writer().writeShort(item.x);
            m.writer().writeShort(item.y);
            m.writer().flush();
            player.conn.sendMessage(m);
        } catch (Exception var3) {
            var3.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void updateCost(Player player) {
        Message m= null;
        try {
            m = new Message(13);
            m.writer().writeInt(player.c.xu);
            m.writer().writeInt(player.c.yen);
            m.writer().writeInt(player.luong);
            m.writer().flush();
            player.conn.sendMessage(m);
        } catch (Exception var3) {
            var3.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void requestItemInfoMessage(Player player, Item item, int index, int typeUI) {
        Message m= null;
        try {
            m = new Message(42);
            m.writer().writeByte(typeUI);
            m.writer().writeByte(index);
            m.writer().writeLong(item.expires);
            if (ItemTemplate.isTypeUIME(typeUI)) {
                m.writer().writeInt(item.saleCoinLock);
            }
            if (ItemTemplate.isTypeUIShop(typeUI) || ItemTemplate.isTypeUIShopLock(typeUI) || ItemTemplate.isTypeMounts(typeUI) || ItemTemplate.isTypeUIStore(typeUI) || ItemTemplate.isTypeUIBook(typeUI) || ItemTemplate.isTypeUIFashion(typeUI) || ItemTemplate.isTypeUIClanShop(typeUI)) {
                m.writer().writeInt(item.buyCoin);
                m.writer().writeInt(item.buyCoinLock);
                m.writer().writeInt(item.buyGold);
            }
            if (ItemTemplate.isTypeBody(item.id) || ItemTemplate.isTypeMounts(item.id) || ItemTemplate.isTypeNgocKham(item.id)) {
                m.writer().writeByte(item.sys);
                if (item.options != null) {
                    for(Option opt : item.options) {
                        m.writer().writeByte(opt.id);
                        m.writer().writeInt(opt.param);
                    }
                }
                if (item.ngocs != null && item.ngocs.size() > 0) {
                    int idNgoc = -1;
                    if (item.getData().type == 1) {
                        idNgoc = ItemTemplate.VU_KHI_OPTION_ID;
                    } else if (item.getData().isTrangSuc()) {
                        idNgoc = ItemTemplate.TRANG_SUC_OPTION_ID;
                    } else if (item.getData().isTrangPhuc()) {
                        idNgoc = ItemTemplate.TRANG_BI_OPTION_ID;
                    }

                    if (idNgoc != -1) {
                        for(Item itemNgoc : item.ngocs) {
                            for(Option op : itemNgoc.options) {
                                if(op.id == idNgoc) {
                                    int indx = itemNgoc.options.indexOf(op);
                                    if(indx != -1) {
                                        if (itemNgoc.id == ItemTemplate.HUYEN_TINH_NGOC) {
                                            m.writer().writeByte(109);
                                        } else if (itemNgoc.id == ItemTemplate.HUYET_NGOC) {
                                            m.writer().writeByte(110);
                                        } else if (itemNgoc.id == ItemTemplate.LAM_TINH_NGOC) {
                                            m.writer().writeByte(111);
                                        } else if (itemNgoc.id == ItemTemplate.LUC_NGOC) {
                                            m.writer().writeByte(112);
                                        }
                                        Option op1 = itemNgoc.options.get(indx + 1);
                                        Option op2 = itemNgoc.options.get(indx + 2);
                                        m.writer().writeInt(0);
                                        m.writer().writeByte(op1.id);
                                        m.writer().writeInt(op1.param);
                                        m.writer().writeByte(op2.id);
                                        m.writer().writeInt(op2.param);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (item.id == 233 || item.id == 234 || item.id == 235) {
                ByteArrayOutputStream a = GameSrc.loadFile("res/icon/1/diado.png");
                if (a != null) {
                    byte[] ab = a.toByteArray();
                    m.writer().writeInt(ab.length);
                    m.writer().write(ab);
                }
            }
            m.writer().flush();
            player.conn.sendMessage(m);
        } catch (Exception var3) {
            var3.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void chatNPC(Player p, Short idnpc, String chat) {
        Message m = null;
        try {
            m = new Message(38);
            m.writer().writeShort(idnpc);
            m.writer().writeUTF(chat);
            m.writer().flush();
            p.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void sendInfoPlayers(Player player, Player p) {
        Message m = null;
        try {
            m = new Message(93);
            m.writer().writeInt(p.c.get().id);
            m.writer().writeUTF(p.c.name);
            m.writer().writeShort(p.c.get().partHead());
            m.writer().writeByte(p.c.gender);
            m.writer().writeByte(p.c.get().nclass);
            m.writer().writeByte(p.c.get().pk);
            m.writer().writeInt(p.c.get().hp);
            m.writer().writeInt(p.c.get().getMaxHP());
            m.writer().writeInt(p.c.get().mp);
            m.writer().writeInt(p.c.get().getMaxMP());
            m.writer().writeByte(p.c.get().speed());
            m.writer().writeShort(p.c.get().ResFire());
            m.writer().writeShort(p.c.get().ResIce());
            m.writer().writeShort(p.c.get().ResWind());
            m.writer().writeInt(p.c.get().dameMax());
            m.writer().writeInt(p.c.get().dameDown());
            m.writer().writeShort(p.c.get().Exactly());
            m.writer().writeShort(p.c.get().Miss());
            m.writer().writeShort(p.c.get().Fatal());
            m.writer().writeShort(p.c.get().ReactDame());
            m.writer().writeShort(p.c.get().sysUp());
            m.writer().writeShort(p.c.get().sysDown());
            m.writer().writeByte(p.c.get().level);
            m.writer().writeShort(p.c.pointUydanh);
            m.writer().writeUTF(p.c.clan.clanName);
            if (!p.c.clan.clanName.isEmpty()) {
                m.writer().writeByte(p.c.clan.typeclan);
            }
            m.writer().writeShort(p.c.pointUydanh);
            m.writer().writeShort(p.c.pointNon);
            m.writer().writeShort(p.c.pointAo);
            m.writer().writeShort(p.c.pointGangtay);
            m.writer().writeShort(p.c.pointQuan);
            m.writer().writeShort(p.c.pointGiay);
            m.writer().writeShort(p.c.pointVukhi);
            m.writer().writeShort(p.c.pointLien);
            m.writer().writeShort(p.c.pointNhan);
            m.writer().writeShort(p.c.pointNgocboi);
            m.writer().writeShort(p.c.pointPhu);
            m.writer().writeByte(20 - p.c.countTaskHangNgay);
            m.writer().writeByte(2 - p.c.countTaskTaThu);
            m.writer().writeByte(p.c.nCave);
            m.writer().writeByte(8 - p.c.get().useTiemNang);
            m.writer().writeByte(8 - p.c.get().useKyNang);
            Item[] var3 = p.c.get().ItemBody;
            Item body;
            int lengItemBody = 16;
            if(player.conn.version >= 180) {
                lengItemBody = 32;
            }
            int var4;
            for(var4 = 0; var4 < lengItemBody; var4++) {
                body = var3[var4];
                if (body != null) {
                    m.writer().writeShort(body.id);
                    m.writer().writeByte(body.upgrade);
                    m.writer().writeByte(body.sys);
                } else {
                    if(player.conn.version >= 180) {
                        m.writer().writeShort(-1);
                    }
                }
            }
            m.writer().flush();
            player.conn.sendMessage(m);
            m.cleanup();
            m = new Message(101);
            m.writer().writeInt(p.c.pointTinhTu);
            m.writer().writeByte(10 - p.c.get().useBanhPhongLoi);
            m.writer().writeByte(10 - p.c.get().useBanhBangHoa);
            m.writer().flush();
            player.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void chatKTG(String chat) {
        Message m = null;
        try {
            m = new Message(-25);
            m.writer().writeUTF(chat);
            m.writer().flush();
            Client.gI().NinjaMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m!= null) {
                m.cleanup();
            }
        }
    }

    public static void SendChinhPhuc(Player p, ArrayList<ThienDiaData> list) {
        Message msg = null;
        try {
            if(p.conn != null && list != null) {
                msg = new Message(121);
                msg.writer().writeByte(list.size());
                for (int i = list.size() - 1; i >= 0; i--) {
                    if(list.get(i) != null) {
                        msg.writer().writeUTF(list.get(i).getName());
                        msg.writer().writeInt(list.get(i).getRank());
                        msg.writer().writeUTF((list.get(i).getType() == 1 && !list.get(i).getName().equals(p.c.name) ) ? "Có thể thách đấu" : "Không thể thách đấu");
                    }
                }
                msg.writer().flush();
                p.conn.sendMessage(msg);
            }
        } catch (Exception var9) {
            var9.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }

        }

    }
}
