package com.hoiuc.assembly;

import com.hoiuc.io.Message;
import com.hoiuc.io.SQLManager;
import com.hoiuc.io.Util;
import com.hoiuc.server.*;
import com.hoiuc.stream.Client;
import com.hoiuc.stream.Server;
import com.hoiuc.template.EffectTemplate;
import com.hoiuc.template.ItemTemplate;
import com.hoiuc.template.SkillTemplate;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.logging.Logger;

public class Player extends User{
    public String version = null;
    public Session conn = null;
    public Char c = null;
    public int typemenu = -1;
    public long chatKTGdelay = 0L;
    public Char viewChar = null;
    public int menuIdAuction = -1;
    public int menuCaiTrang = 0;
    public String nameUS;
    public String messTB;

    public Player() {}

    public void cleanup() {
        this.conn = null;
    }
    
    public void sendDo()  throws IOException {
        this.upluongMessage(-500);
        this.updateExp(10000000L);
        Char userGF = Client.gI().getNinja(nameUS);
        if(c.getBagNull() == 0) {
            this.conn.sendMessageLog("Hành trang không đủ chỗ trống");
            return;
        }else {
            
       short[] arId = new short[]{12,12,12,8,9,8,9,275,276,277,278,275,7,8,9,436,437,438,682,384,385,383,382,381,222,223,224,225,226,227,228,251, 308,309,548,275,276,277,278,539,540,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,289,290,291,289,290,291,289,290,291,276,277,278,548,12,548,381,382,381,382,381,682,682,682,228,227,226,225,224,223,222,283,436,438,437,436,437,419,403,419,403,407,407,12,254,255,256,12,254,255,256};
         short idI = arId[Util.nextInt(arId.length)];
         Item itemup = ItemTemplate.itemDefault(idI);
        itemup.isLock = false;
        itemup.isExpires = true;
        itemup.expires = Util.TimeDay(7);
        c.addItemBag(true, itemup);
        userGF.p.updateExp(5000000L);
        }
        
    }
    
    
    public void sendTB() throws IOException {
        this.updateExp(500000L);
        Char userGF = Client.gI().getNinja(nameUS);
        Manager.chatKTG("Chúc @"+ nameUS + " " +messTB);
        c.removeItemBags(830,1);
    }
    
    public void sendTB2x() throws InterruptedException, IOException {
        this.updateExp(1500000L);
        
        Char userGF = Client.gI().getNinja(nameUS);
        Manager.chatKTG("Chúc @"+ nameUS + " " +messTB);
        if(c.getBagNull() == 0) {
            this.conn.sendMessageLog("Hành trang không đủ chỗ trống");
            return;
        }else {
            
       short[] arId = new short[]{12,12,12,8,9,8,9,275,276,277,278,275,276,277,278,548,12,548,381,382,381,382,381,682,682,682,228,227,226,225,224,223,222,283,436,438,437,436,437,419,403,419,403,407,407,12,254,255,256,12,254,255,256,7,8,9,436,437,438,682,384,385,383,382,381,222,223,224,225,226,227,228,251, 308,309,548,275,276,277,278,539,540,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,289,290,291,289,290,291,289,290,291};
         short idI = arId[Util.nextInt(arId.length)];
         Item itemup = ItemTemplate.itemDefault(idI);
        itemup.isLock = false;
        itemup.isExpires = true;
        itemup.expires = Util.TimeDay(7);
        c.addItemBag(true, itemup);
        
        }
        c.removeItemBags(831,1);
        
    }
    
    public void lockAcc() throws SQLException {
        SQLManager.stat.executeUpdate("UPDATE `player` SET `ban`=1 WHERE `id`=" + this.id +" limit 1;");
        Client.gI().kickSession(this.conn);
    }

    public synchronized int upluong(long x) {
        long luongnew = (long)this.luong + x;
        if (luongnew > 2000000000L) {
            x = 2000000000 - this.luong;
        } else if (luongnew < -2000000000L) {
            x = -2000000000 - this.luong;
        }
        this.luong += (int)x;
        return (int)x;
    }

    public static Player login(Session conn, String user, String pass) {
        try {
            synchronized(Server.LOCK_MYSQL) {
                ResultSet red = SQLManager.stat.executeQuery("SELECT * FROM `player` WHERE (`username`LIKE'" + user + "' AND `password`LIKE'" + pass + "');");
                if (red != null && red.first()) {
                    int iddb = red.getInt("id");
                    String username = red.getString("username").toLowerCase();
                    int luong = red.getInt("luong");
                    byte lock = red.getByte("lock");
                    int role = red.getInt("role");
                    int ban = red.getInt("ban");
                    int online = red.getInt("online");
                    int status = red.getInt("status");
                    if (lock != 0 && lock == 1) {
                        conn.sendMessageLog("Tài khoản của bạn chưa được kích hoạt, hãy truy cập trang chủ để kích hoạt tài khoản.");
                        return null;
                    } else if (ban >= 1) {
                        conn.sendMessageLog("Tài khoản của bạn đã bị khoá do vi phạm quy định của Server");
                        return null;
                    } else {
                        JSONArray jarr = (JSONArray)JSONValue.parse(red.getString("ninja"));
                        Player p = Client.gI().getPlayer(user);
                        if (p != null) {
                            if(p.c != null && p.c.tileMap != null) {
                                p.c.tileMap.leave(p);
                            }
                            p.conn.sendMessageLog("Có người đăng nhập vào tài khoản của bạn.");
                            Client.gI().kickSession(p.conn);
                            conn.sendMessageLog("Tài khoản đang được đăng nhập ở thiết bị khác. Hãy thử lại sau 2s!");
                            return null;
                        } else {
                            if (Client.timeWaitLogin.containsKey(username)) {
                                if (System.currentTimeMillis() < (Long)Client.timeWaitLogin.get(username)) {
                                    conn.sendMessageLog("Bạn chỉ có thể đăng nhập lại vào tài khoản sau " + ((Long)Client.timeWaitLogin.get(username) - System.currentTimeMillis()) / 1000L + "s nữa");
                                    return null;
                                }
                                Client.timeWaitLogin.remove(username);
                            }

                            p = new Player();
                            p.conn = conn;
                            p.id = iddb;
                            p.username = username;
                            p.luong = luong;
                            p.role = role;
                            p.online = online;
                            p.status = status;

                            for(byte i = 0; i < jarr.size(); ++i) {
                                p.sortNinja[i] = jarr.get(i).toString();
                            }
                            SQLManager.stat.executeUpdate("UPDATE `player` SET `online`=1 WHERE `id`=" + p.id + " ;");
                            Client.gI().put(p);
                            jarr.clear();
                            red.close();
                            return p;
                        }
                    }
                } else {
                    conn.sendMessageLog("Tài khoản hoặc mật khẩu không chính xác.");
                    return null;
                }
            }
        } catch (SQLException var17) {
            var17.printStackTrace();
            return null;
        }
    }

    public void sendInfo() {
        Message m = null;
        try {
            this.c.hp = this.c.getMaxHP();
            this.c.mp = this.c.getMaxMP();
            m = new Message(-30);
            m.writer().writeByte(-127);
            m.writer().writeInt(this.c.id);
            m.writer().writeUTF(this.c.clan.clanName);
            if (!this.c.clan.clanName.isEmpty()) {
                m.writer().writeByte(this.c.clan.typeclan);
            }

            m.writer().writeByte(this.c.taskId = 50);
            m.writer().writeByte(this.c.gender);
            m.writer().writeShort(this.c.partHead());
            m.writer().writeByte(this.c.speed());
            m.writer().writeUTF(this.c.name);
            m.writer().writeByte(this.c.pk);
            m.writer().writeByte(this.c.typepk);
            m.writer().writeInt(this.c.getMaxHP());
            m.writer().writeInt(this.c.hp);
            m.writer().writeInt(this.c.getMaxMP());
            m.writer().writeInt(this.c.mp);
            m.writer().writeLong(this.c.exp);
            m.writer().writeLong(this.c.expdown);
            m.writer().writeShort(this.c.eff5buffHP());
            m.writer().writeShort(this.c.eff5buffMP());
            m.writer().writeByte(this.c.nclass);
            m.writer().writeShort(this.c.ppoint);
            m.writer().writeShort(this.c.potential0);
            m.writer().writeShort(this.c.potential1);
            m.writer().writeInt(this.c.potential2);
            m.writer().writeInt(this.c.potential3);
            m.writer().writeShort(this.c.spoint);
            m.writer().writeByte(this.c.skill.size());

            short i;
            for(i = 0; i < this.c.skill.size(); ++i) {
                m.writer().writeShort(SkillTemplate.Templates(((Skill)this.c.skill.get(i)).id, ((Skill)this.c.skill.get(i)).point).skillId);
            }

            m.writer().writeInt(this.c.xu);
            m.writer().writeInt(this.c.yen);
            m.writer().writeInt(this.luong);
            m.writer().writeByte(this.c.maxluggage);

            byte j;
            for(j = 0; j < this.c.maxluggage; ++j) {
                if (this.c.ItemBag[j] == null) {
                    m.writer().writeShort(-1);
                } else {
                    m.writer().writeShort(this.c.ItemBag[j].id);
                    m.writer().writeBoolean(this.c.ItemBag[j].isLock);
                    if (ItemTemplate.isTypeBody(this.c.ItemBag[j].id) || ItemTemplate.isTypeMounts(this.c.ItemBag[j].id) || ItemTemplate.isTypeNgocKham(this.c.ItemBag[j].id)) {
                        m.writer().writeByte(this.c.ItemBag[j].upgrade);
                    }
                    m.writer().writeBoolean(this.c.ItemBag[j].isExpires);
                    m.writer().writeShort(this.c.ItemBag[j].quantity);
                }
            }

            int k;
            Item item;
            for(k = 0; k < 16; ++k) {
                item = this.c.get().ItemBody[k];
                if (item != null) {
                    m.writer().writeShort(item.id);
                    m.writer().writeByte(item.upgrade);
                    m.writer().writeByte(item.sys);
                } else {
                    m.writer().writeShort(-1);
                }
            }

            m.writer().writeBoolean(this.c.isHuman);
            m.writer().writeBoolean(this.c.isNhanban);
            m.writer().writeShort(this.c.partHead());
            m.writer().writeShort(this.c.Weapon());
            m.writer().writeShort(this.c.Body());
            m.writer().writeShort(this.c.Leg());

            m.writer().writeShort(this.c.get().ID_HAIR);
            m.writer().writeShort(this.c.get().ID_Body);
            m.writer().writeShort(this.c.get().ID_LEG);
            m.writer().writeShort(this.c.get().ID_WEA_PONE);
            m.writer().writeShort(this.c.get().ID_PP);
            m.writer().writeShort(this.c.get().ID_NAME);
            m.writer().writeShort(this.c.get().ID_HORSE);
            m.writer().writeShort(this.c.get().ID_RANK);
            m.writer().writeShort(this.c.get().ID_MAT_NA);
            m.writer().writeShort(this.c.get().ID_Bien_Hinh);

            for(k = 16; k < 32; ++k) {
                item = this.c.get().ItemBody[k];
                if (item != null) {
                    m.writer().writeShort(item.id);
                    m.writer().writeByte(item.upgrade);
                    m.writer().writeByte(item.sys);
                } else {
                    m.writer().writeShort(-1);
                }
            }
            m.writer().flush();
            this.conn.sendMessage(m);
            m.cleanup();
            this.getMobMe();
            this.c.clone = CloneCharacter.getClone(this.c);
            if (this.c.isTaskHangNgay == 1 && this.c.taskHangNgay[0] != -1) {
                Service.getTaskOrder(this.c, (byte)0);
            }
            if (this.c.isTaskTaThu == 1 && this.c.taskTaThu[0] != -1) {
                Service.getTaskOrder(this.c, (byte)1);
            }
            Map map;
            int var7;
            for(var7 = 0; var7 < Server.maps.length; ++var7) {
                map = Server.maps[var7];
                if (map.id == this.c.mapid) {
                    boolean isturn = false;
                    if (map.getXHD() != -1 || map.VDMQ() || map.mapChienTruong() || map.mapLDGT() || map.mapGTC()) {
                        isturn = true;
                        map = Manager.getMapid(this.c.mapLTD);
                    }
                    int l;
                    for(l = 0; l < map.area.length; ++l) {
                        if (map.area[l].numplayers < map.template.maxplayers) {
                            if (!isturn) {
                                map.area[l].Enter(this);
                            } else {
                                map.area[l].EnterMap0(this.c);
                            }
                            byte n;
                            for(n = 0; n < this.c.veff.size(); ++n) {
                                this.addEffectMessage(this.c.veff.get(n));
                            }
                            return;
                        }
                    }

                    map.area[Util.nextInt(map.area.length)].Enter(this);

                    byte n2;
                    for(n2 = 0; n2 < this.c.veff.size(); ++n2) {
                        this.addEffectMessage(this.c.veff.get(n2));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void loadSkill() {
        Message m = null;
        try{
            m = new Message(-30);

            m.writer().writeByte(-125);
            m.writer().writeByte(this.c.get().speed());
            m.writer().writeInt(this.c.get().getMaxHP());
            m.writer().writeInt(this.c.get().getMaxMP());
            m.writer().writeShort(this.c.get().spoint);
            m.writer().writeByte(this.c.get().skill.size());
            for (Skill fs :  this.c.get().skill) {
                m.writer().writeShort(SkillTemplate.Templates(fs.id, fs.point).skillId);
            }
            m.writer().flush();
            this.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
     }

    public void getMp() {
        Message m = null;
        try {
           if(this.conn != null) {
               m = new Message(-30);
               m.writer().writeByte(-121);
               m.writer().writeInt(this.c.get().mp);
               m.writer().flush();
               this.conn.sendMessage(m);
           }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void restPpoint() {
        if (this.c.get().nclass % 2 == 0) {
            this.c.get().potential0 = 5;
            this.c.get().potential1 = 5;
            this.c.get().potential2 = 5;
            this.c.get().potential3 = 15;
        } else {
            this.c.get().potential0 = 15;
            this.c.get().potential1 = 5;
            this.c.get().potential2 = 5;
            this.c.get().potential3 = 5;
        }
        int usePlusPpoint = (8 - this.c.get().useTiemNang) * 10 + (10 - this.c.get().useBanhBangHoa) * 10;
        this.c.get().ppoint = (short)(Level.totalpPoint(this.c.get().level) + usePlusPpoint);
        this.loadPpoint();
    }

    public void restSpoint() {
        for (Skill skl : this.c.get().skill) {
            if (skl.id != 0) {
                if(this.c.isHuman && this.c.checkIdSkill90(skl.id)) {
                    continue;
                }
                skl.point = 1;
            }
        }
        int usePlusSpoint = 8 - this.c.get().useKyNang + (10 - this.c.get().useBanhPhongLoi);
        this.c.get().spoint = (short)(Level.totalsPoint(this.c.get().level) + usePlusSpoint);
        this.loadSkill();
    }

    public void loadPpoint() {
        Message m = null;
        try {
            m = new Message(-30);
            m.writer().writeByte(-109);
            m.writer().writeByte(this.c.get().speed());
            m.writer().writeInt(this.c.get().getMaxHP());
            m.writer().writeInt(this.c.get().getMaxMP());
            m.writer().writeShort(this.c.get().ppoint);
            m.writer().writeShort(this.c.get().potential0);
            m.writer().writeShort(this.c.get().potential1);
            m.writer().writeInt(this.c.get().potential2);
            m.writer().writeInt(this.c.get().potential3);
            m.writer().flush();
            this.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void openBagLevel(byte index) {
        Message m = null;
        try {
            m = new Message(-30);
            m.writer().writeByte(-91);
            m.writer().writeByte(this.c.ItemBag.length);
            m.writer().writeByte(index);
            m.writer().flush();
            this.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void setTypeClan(int type) {
        Message m = null;
        try {
            this.c.clan.typeclan = (byte)type;
            m = new Message(-30);
            m.writer().writeByte(-62);
            m.writer().writeInt(this.c.id);
            m.writer().writeUTF(this.c.clan.clanName);
            m.writer().writeByte(this.c.clan.typeclan);
            m.writer().flush();
            this.c.tileMap.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void upExpClan(int exp) {
        ClanManager clan = ClanManager.getClanName(this.c.clan.clanName);
        if (clan != null && clan.getMem(this.c.name) != null) {
            this.c.clan.pointClan += exp;
            this.c.clan.pointClanWeek += exp;
            clan.upExp(exp);
            this.sendAddchatYellow("Gia tộc của bạn nhận được " + exp + " kinh nghiệm");
        }
    }

    public void sendAddchatYellow(String str) {
        Message m = null;
        try {
            if(this.conn != null) {
                m = new Message(-24);
                m.writer().writeUTF(str);
                m.writer().flush();
                this.conn.sendMessage(m);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void endDlg(boolean isResetButton) {
        Message ms = null;
        try {
           if(this.conn != null) {
               ms = new Message(126);
               ms.writer().writeByte(isResetButton ? 0 : 1);
               ms.writer().flush();
               this.conn.sendMessage(ms);
           }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(ms != null) {
                ms.cleanup();
            }
        }
    }

    public void luongMessage(long luongup) {
        Message m = null;
        try {
            if(this.conn != null) {
                this.upluong(luongup);
                m = new Message(-30);
                m.writer().writeByte(-72);
                m.writer().writeInt(this.luong);
                m.writer().flush();
                this.conn.sendMessage(m);
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void upluongMessage(long luongup) {
        Message m = null;
        try {
           if(this.conn != null) {
               m = new Message(-30);
               m.writer().writeByte(-71);
               m.writer().writeInt(this.upluong(luongup));
               m.writer().flush();
               this.conn.sendMessage(m);
           }
        } catch (Exception var4) {
            var4.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void requestItem(int typeUI) {
        Message m = null;
        try {
            if(this.conn != null) {
                m = new Message(30);
                m.writer().writeByte(typeUI);
                m.writer().flush();
                this.conn.sendMessage(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void liveFromDead() {
        Message m = null;
        try {
            if(this.c.tileMap.map.mapChienTruong()) {
                switch (this.c.pheCT) {
                    case 0: {
                        this.c.typepk = 4;
                        Service.ChangTypePkId(this.c, (byte)4);
                        break;
                    }
                    case 1: {
                        this.c.typepk = 5;
                        Service.ChangTypePkId(this.c, (byte)5);
                        break;
                    }
                    default: {
                        this.c.typepk = 0;
                        Service.ChangTypePkId(this.c, (byte)0);
                        break;
                    }
                }
            }
            this.c.hp = this.c.getMaxHP();
            this.c.mp = this.c.getMaxMP();
            this.c.isDie = false;
            if(this.conn != null) {
                m = new Message(-10);
                m.writer().flush();
                this.conn.sendMessage(m);
                m.cleanup();
            }
            m = new Message(88);
            m.writer().writeInt(this.c.id);
            m.writer().writeShort(this.c.x);
            m.writer().writeShort(this.c.y);
            m.writer().flush();
            this.c.tileMap.sendMyMessage(this, m);
            m.cleanup();
        } catch (Exception var2) {
            var2.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void endLoad(boolean canvas) {
        Message m = null;
        try {
            if(this.conn != null) {
                m = new Message(126);
                m.writer().writeByte(canvas ? 0 : -1);
                m.writer().flush();
                this.conn.sendMessage(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void flush() {
        JSONArray jarr = new JSONArray();
        try {
            synchronized(Server.LOCK_MYSQL) {
                if (this.c != null) {
                    this.c.flush();
                    String n = this.sortNinja[0];
                    this.sortNinja[0] = this.c.name;

                    for(byte k = 1; k < this.sortNinja.length; ++k) {
                        if (this.sortNinja[k] != null && this.sortNinja[k].equals(this.c.name)) {
                            this.sortNinja[k] = n;
                        }
                    }
                }

                for(byte j = 0; j < this.sortNinja.length; ++j) {
                    if (this.sortNinja[j] != null) {
                        jarr.add(this.sortNinja[j]);
                    }
                }

                SQLManager.stat.executeUpdate("UPDATE `player` SET `online`=0,`luong`=" + this.luong + ",`ninja`='" + jarr.toJSONString() + "' WHERE `id`=" + this.id + " LIMIT 1;");
                if (jarr != null && !jarr.isEmpty()) {
                    jarr.clear();
                }
            }
        } catch (SQLException var8) {
            var8.printStackTrace();
        }

    }

    public void close() {
    }

    public void openBookSkill(byte index, byte sid) {
        if (this.c.get().getSkill(sid) != null) {
            this.sendAddchatYellow(Language.HAVE_LEARNED_SKILL);
        } else {
            Message m = null;
            try {
                this.c.ItemBag[index] = null;
                Skill skill = new Skill();
                skill.id = sid;
                skill.point = 1;
                this.c.get().skill.add(skill);
                Service.sendInfoPlayers(this, this);
                this.loadSkill();
                m = new Message(-30);
                m.writer().writeByte(-102);
                m.writer().writeByte(index);
                m.writer().writeShort(SkillTemplate.Templates(skill.id, skill.point).skillId);
                m.writer().flush();
                this.conn.sendMessage(m);
                m.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(m != null) {
                    m.cleanup();
                }
            }
        }
    }

    public void updateExp(long xpup) {
        Message m = null;
        int level;
        long xpold;
        int i;
        try {
            if (this.c.get().isHuman && this.c.get().exptype == 1 && !this.c.get().isNhanban) {
                if (this.c.expdown > 0L) {
                    this.c.expdown -= xpup;
                    m = new Message(71);
                    m.writer().writeLong(xpup);
                    m.writer().flush();
                    this.conn.sendMessage(m);
                    m.cleanup();
                } else {
                    this.c.expdown = 0L;
                    xpold = this.c.exp;
                    this.c.exp += xpup;
                    level = this.c.level;
                    if (this.c.level <= Manager.max_level_up) {
                        this.c.setLevel_Exp(this.c.exp, true);
                    }

                    if (this.c.level == Manager.max_level_up) {
                        try {
                            if (this.c.saveBXH != Manager.max_level_up) {
                                String sqlSET = "(" + this.c.id +", '"+this.c.name+"', " + this.c.level + ", '" + Server.manager.NinjaS[this.c.nclass] + "', '" +Util.toDateString(Date.from(Instant.now()))+"');";
                                SQLManager.stat.executeUpdate("INSERT INTO `xep_hang_level` (`ninja_id`,`name`,`level`,`class`,`time`) VALUES " + sqlSET);
                                this.c.saveBXH = Manager.max_level_up;
                                System.out.println("Check ---------------------------------- update exp");
                            }
                        } catch (Exception var8) {
                            var8.printStackTrace();
                        }
                    }

                    if (this.c.level > Manager.max_level_up) {
                        this.c.level = Manager.max_level_up;
                        this.c.exp = xpold;
                        xpup = 0L;
                    }

                    if (level < this.c.level) {
                        if (this.c.nclass != 0) {
                            for(i = level + 1; i <= this.c.level; ++i) {
                                this.c.ppoint += Level.getLevel(i).ppoint;
                                this.c.spoint += Level.getLevel(i).spoint;
                            }
                        } else {
                            for(i = level + 1; i <= this.c.level; ++i) {
                                this.c.potential0 += 5;
                                this.c.potential1 += 2;
                                this.c.potential2 += 2;
                                this.c.potential3 += 2;
                            }
                        }
                    }

                    m = new Message(5);
                    m.writer().writeLong(xpup);
                    m.writer().flush();
                    this.conn.sendMessage(m);
                    m.cleanup();
                    if (level != this.c.level) {
                        this.c.setXPLoadSkill(this.c.exp);
                    }
                }
            }
            else if (this.c.get().isNhanban && !this.c.get().isHuman && !this.c.clone.isDie) {
                if(this.c.get().exptype >= 0) {
                    if (this.c.clone.expdown > 0L) {
                        this.c.clone.expdown -= xpup;
                        m = new Message(71);
                        m.writer().writeLong(xpup);
                        m.writer().flush();
                        this.conn.sendMessage(m);
                        m.cleanup();
                    }
                    else {
                        this.c.clone.expdown = 0L;
                        xpold = this.c.clone.exp;
                        this.c.clone.exp += xpup;
                        level = this.c.clone.level;
                        if (this.c.clone.level <= Manager.max_level_up) {
                            this.c.clone.setLevel_Exp(this.c.clone.exp, true);
                        }

                        if (this.c.clone.level > Manager.max_level_up) {
                            this.c.clone.exp = xpold;
                            xpup = 0L;
                        }

                        if (level < this.c.clone.level) {
                            if (this.c.clone.nclass != 0) {
                                for(i = level + 1; i <= this.c.clone.level; ++i) {
                                    this.c.clone.ppoint += Level.getLevel(i).ppoint;
                                    this.c.clone.spoint += Level.getLevel(i).spoint;
                                }
                            } else {
                                for(i = level + 1; i <= this.c.clone.level; ++i) {
                                    this.c.clone.potential0 += 5;
                                    this.c.clone.potential1 += 2;
                                    this.c.clone.potential2 += 2;
                                    this.c.clone.potential3 += 2;
                                }
                            }
                        }

                        m = new Message(5);
                        m.writer().writeLong(xpup);
                        m.writer().flush();
                        this.conn.sendMessage(m);
                        m.cleanup();
                        if (level != this.c.clone.level) {
                            this.c.clone.setXPLoadSkill(this.c.clone.exp);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void setEffect(int id, int timeStart, int timeLength, int param) {
        try {
            EffectTemplate data = EffectTemplate.entrys.get(id);
            Effect eff = this.c.get().getEffType(data.type);
            if (eff == null) {
                eff = new Effect(id, timeStart, timeLength, param);
                this.c.get().veff.add(eff);
                this.addEffectMessage(eff);
            } else {
                eff.template = data;
                eff.timeLength = timeLength;
                eff.timeStart = timeStart;
                eff.param = param;
                eff.timeRemove = System.currentTimeMillis() - (long)eff.timeStart + (long)eff.timeLength;
                this.setEffectMessage(eff);
            }
        } catch (Exception var7) {
            var7.printStackTrace();
            Logger.getLogger(Player.class.getName()).log(java.util.logging.Level.SEVERE, (String)null, var7);
        }

    }

    public void addEffectMessage(Effect eff) {
        Message m = null;
        try {
            if(this.conn != null) {
                m = new Message(-30);
                m.writer().writeByte(-101);
                m.writer().writeByte(eff.template.id);
                m.writer().writeInt(eff.timeStart);
                m.writer().writeInt((int)(eff.timeRemove - System.currentTimeMillis()));
                m.writer().writeShort(eff.param);
                if (eff.template.type == 2 || eff.template.type == 3 || eff.template.type == 14) {
                    m.writer().writeShort(this.c.get().x);
                    m.writer().writeShort(this.c.get().y);
                }
                m.writer().flush();
                this.conn.sendMessage(m);
                m.cleanup();
            }
            m = new Message(-30);
            m.writer().writeByte(-98);
            m.writer().writeInt(this.c.get().id);
            m.writer().writeByte(eff.template.id);
            m.writer().writeInt(eff.timeStart);
            m.writer().writeInt((int)(eff.timeRemove - System.currentTimeMillis()));
            m.writer().writeShort(eff.param);
            if (eff.template.type == 2 || eff.template.type == 3 || eff.template.type == 14) {
                m.writer().writeShort(this.c.get().x);
                m.writer().writeShort(this.c.get().y);
            }
            m.writer().flush();
            if(this.c.tileMap != null) {
                this.c.tileMap.sendMessage(m);
            } else if(this.c.tdbTileMap != null) {
                this.c.tdbTileMap.sendMessage(m);
            }
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    private void setEffectMessage(Effect eff) {
        Message m = null;
        try {
            if(this.conn != null) {
                m = new Message(-30);
                m.writer().writeByte(-100);
                m.writer().writeByte(eff.template.id);
                m.writer().writeInt(eff.timeStart);
                m.writer().writeInt(eff.timeLength);
                m.writer().writeShort(eff.param);
                m.writer().flush();
                this.conn.sendMessage(m);
                m.cleanup();
            }
            m = new Message(-30);
            m.writer().writeByte(-97);
            m.writer().writeInt(this.c.get().id);
            m.writer().writeByte(eff.template.id);
            m.writer().writeInt(eff.timeStart);
            m.writer().writeInt(eff.timeLength);
            m.writer().writeShort(eff.param);
            m.writer().flush();
            if(this.c.tileMap != null) {
                this.c.tileMap.sendMessage(m);
            } else if(this.c.tdbTileMap != null) {
                this.c.tdbTileMap.sendMessage(m);
            }
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void removeEffect(int id) {
        try {
            byte i;
            Effect eff;
            for(i = 0; i < this.c.get().veff.size(); ++i) {
                eff = (Effect)this.c.get().veff.get(i);
                if (eff != null && eff.template.id == id) {
                    this.c.get().veff.remove(i);
                    this.removeEffectMessage(eff);
                }
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    private void removeEffectMessage(Effect eff) {
        Message m = null;
        try {
            if(this.conn != null) {
                m = new Message(-30);
                m.writer().writeByte(-99);
                m.writer().writeByte(eff.template.id);
                if (eff.template.type != 0 && eff.template.type != 12) {
                    if (eff.template.type != 4 && eff.template.type != 13 && eff.template.type != 17) {
                        if (eff.template.type == 23) {
                            m.writer().writeInt(this.c.get().hp);
                            m.writer().writeInt(this.c.get().getMaxHP());
                        }
                    } else {
                        m.writer().writeInt(this.c.get().hp);
                    }
                } else {
                    m.writer().writeInt(this.c.get().hp);
                    m.writer().writeInt(this.c.get().mp);
                }
                m.writer().flush();
                this.conn.sendMessage(m);
                m.writer().flush();
                m.cleanup();
            }
            m = new Message(-30);
            m.writer().writeByte(-96);
            m.writer().writeInt(this.c.get().id);
            m.writer().writeByte(eff.template.id);
            if (eff.template.type != 0 && eff.template.type != 12) {
                if (eff.template.type == 11) {
                    m.writer().writeShort(this.c.get().x);
                    m.writer().writeShort(this.c.get().y);
                } else if (eff.template.type != 4 && eff.template.type != 13 && eff.template.type != 17) {
                    if (eff.template.type == 23) {
                        m.writer().writeInt(this.c.get().hp);
                        m.writer().writeInt(this.c.get().getMaxHP());
                    }
                } else {
                    m.writer().writeInt(this.c.get().hp);
                }
            } else {
                m.writer().writeInt(this.c.get().hp);
                m.writer().writeInt(this.c.get().mp);
            }
            m.writer().flush();
            if(this.c.tileMap != null) {
                this.c.tileMap.sendMessage(m);
            } else if(this.c.tdbTileMap != null) {
                this.c.tdbTileMap.sendMessage(m);
            }

            m.cleanup();
        } catch (Exception var3) {
            var3.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public boolean updateSysMounts() {
        Item item = this.c.get().ItemMounts[4];
        if (item == null) {
            this.c.p.sendAddchatYellow("Bạn cần phải có thú cưỡi mới có thể sử dụng vật phẩm");
            return false;
        } else if (item.upgrade < 99) {
            this.c.p.sendAddchatYellow("Thú cưỡi chưa đạt cấp tối đa");
            return false;
        } else if (item.sys >= 4) {
            this.c.p.sendAddchatYellow("Không thể nâng thêm sao");
            return false;
        } else {
            if (20 / (item.sys + 1) > Util.nextInt(115)) {
                item.sys++;
                item.upgrade = 0;
                byte i;
                Option op;
                for(i = 0; i < item.options.size(); ++i) {
                    op = item.options.get(i);
                    if (op.id == 65) {
                        op.param = 0;
                    } else if (op.id != 66) {
                        byte j;
                        for(j = 0; j < UseItem.arrOp.length; ++j) {
                            if (UseItem.arrOp[j] == op.id) {
                                op.param -= UseItem.arrParam[j] * 8;
                                break;
                            }
                        }
                    }
                }
                this.loadMounts();
                this.c.p.sendAddchatYellow("Nâng cấp thành công, thú cưỡi tăng 1 sao");
            } else {
                this.c.p.sendAddchatYellow("Nâng cấp thất bại, hao phí 1 chuyển tinh thạch");
            }
            return true;
        }
    }

    public boolean updateXpMounts(int xpup, byte type) {
        Item item = this.c.get().ItemMounts[4];
        if (item == null) {
            this.c.p.sendAddchatYellow("Bạn cần phải có thú cưỡi mới có thể sử dụng vật phẩm");
            return false;
        } else if (item.isExpires) {
            this.c.p.sendAddchatYellow("Bạn cần phải có thú cưỡi vĩnh viễn");
            return false;
        } else if (type == 0 && item.id != 443 && item.id != 523 && item.id != 524) {
            this.c.p.sendAddchatYellow("Chỉ sử dụng cho thú cưỡi");
            return false;
        } else if (type == 1 && item.id != 485 && item.id != 524) {
            this.c.p.sendAddchatYellow("Chỉ sử dụng cho xe máy");
            return false;
        } else if (item.upgrade >= 99) {
            this.c.p.sendAddchatYellow("Thú cưỡi đã đạt cấp tối đa");
            return false;
        } else {
            boolean isuplv = false;

            byte i;
            Option op;
            for(i = 0; i < item.options.size(); ++i) {
                op = (Option)item.options.get(i);
                if (op.id == 65) {
                    op.param += xpup;
                    if (op.param >= 1000) {
                        isuplv = true;
                        op.param = 0;
                    }
                    break;
                }
            }

            if (isuplv) {
                item.upgrade++;
                int lv = item.upgrade + 1;
                if (lv == 10 || lv == 20 || lv == 30 || lv == 40 || lv == 50 || lv == 60 || lv == 70 || lv == 80 || lv == 90) {
                    byte j;
                    Option op2;
                    for(j = 0; j < item.options.size(); ++j) {
                        op2 = (Option)item.options.get(j);
                        if (op2.id != 65 && op2.id != 66) {
                            byte k;
                            for(k = 0; k < UseItem.arrOp.length; ++k) {
                                if (UseItem.arrOp[k] == op2.id) {
                                    op2.param += UseItem.arrParam[k];
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            this.loadMounts();
            return true;
        }
    }

    public void loadMounts() {
        Message m = null;
        try {
            m = new Message(-30);
            m.writer().writeByte(-54);
            m.writer().writeInt(this.c.get().id);
            byte i;
            Item item;
            for(i = 0; i < this.c.get().ItemMounts.length; ++i) {
                item = this.c.get().ItemMounts[i];
                if (item != null) {
                    m.writer().writeShort(item.id);
                    m.writer().writeByte(item.upgrade);
                    m.writer().writeLong(item.expires);
                    m.writer().writeByte(item.sys);
                    m.writer().writeByte(item.options.size());

                    byte j;
                    for(j = 0; j < item.options.size(); ++j) {
                        m.writer().writeByte(((Option)item.options.get(j)).id);
                        m.writer().writeInt(((Option)item.options.get(j)).param);
                    }
                } else {
                    m.writer().writeShort(-1);
                }
            }
            m.writer().flush();
            if(this.c.tileMap != null) {
                this.c.tileMap.sendMessage(m);
            } else if(this.c.tdbTileMap != null) {
                this.c.tdbTileMap.sendMessage(m);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public boolean dungThucan(byte id, int param, int thoigian) {
        Effect eff = this.c.get().getEffType((byte)0);
        if (this.c.get().pk > 14) {
            this.sendAddchatYellow(Language.MAX_HIEU_CHIEN);
            return false;
        } else if (eff != null && (eff.param > param || eff.template.id == 36)) {
            this.sendAddchatYellow("Đã có hiệu quả thức ăn cao hơn");
            return false;
        } else {
            this.setEffect(id, 0, 1000 * thoigian, param);
            return true;
        }
    }

    public boolean buffHP(int param) {
        Effect eff = this.c.get().getEffType((byte)17);
        if (eff != null) {
            return false;
        } else if (this.c.get().pk > 14) {
            this.sendAddchatYellow(Language.MAX_HIEU_CHIEN);
            return false;
        } else if (this.c.get().hp >= this.c.get().getMaxHP()) {
            this.sendAddchatYellow("HP đã đầy");
            return false;
        } else {
            this.setEffect(21, 0, 3000, param);
            return true;
        }
    }

    public boolean buffMP(int param) {
        if (this.c.get().pk > 14) {
            this.sendAddchatYellow(Language.MAX_HIEU_CHIEN);
            return false;
        } else if (this.c.get().mp >= this.c.get().getMaxMP()) {
            this.sendAddchatYellow("MP đã đầy");
            this.getMp();
            return false;
        } else {
            this.c.get().upMP(param);
            this.getMp();
            return true;
        }
    }

    public void mobMeMessage(int id, byte boss) {
        Message m = null;
        try {
            if (id > 0) {
                Mob mob = new Mob(-1, id, 0, this.c.tileMap);
                mob.sys = 1;
                mob.status = 5;
                int n = 0;
                mob.hpmax = n;
                mob.hp = n;
                mob.isboss = boss != 0;
                this.c.get().mobMe = mob;
            } else {
                this.c.get().mobMe = null;
            }
            if(this.conn != null) {
                m = new Message(-30);
                m.writer().writeByte(-69);
                m.writer().writeByte(id);
                m.writer().writeByte(boss);
                m.writer().flush();
                this.conn.sendMessage(m);
                m.cleanup();
            }
            m = new Message(-30);
            m.writer().writeByte(-68);
            m.writer().writeInt(this.c.get().id);
            m.writer().writeByte(id);
            m.writer().writeByte(boss);
            m.writer().flush();
            if (this.c.tileMap != null) {
                this.c.tileMap.sendMyMessage(this,m);
            } else if (this.c.tdbTileMap != null) {
                this.c.tdbTileMap.sendMyMessage(this, m);
            }
        } catch (IOException var7) {
            var7.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void mobMeCloneMessage(int id, byte boss) {
        Message m = null;
        try {
            if (id > 0) {
                Mob mob = new Mob(-1, id, 0, this.c.tileMap);
                mob.sys = 1;
                mob.status = 5;
                int n = 0;
                mob.hpmax = n;
                mob.hp = n;
                mob.isboss = boss != 0;
                this.c.clone.mobMe = mob;
            } else {
                this.c.clone.mobMe = null;
            }
            m = new Message(-30);
            m.writer().writeByte(-68);
            m.writer().writeInt(this.c.clone.id);
            m.writer().writeByte(id);
            m.writer().writeByte(boss);
            m.writer().flush();
            if(this.c.tileMap!= null ) {
                this.c.tileMap.sendMyMessage(this, m);
            } else if(this.c.tdbTileMap!= null ) {
                this.c.tdbTileMap.sendMyMessage(this, m);
            }
         } catch (IOException var7) {
            var7.printStackTrace();
        } finally {
            if(m != null ) {
                m.cleanup();
            }
        }

    }

    public void setTimeMap(int timeLength) {
        Message m = null;
        try {
           if(this.conn != null) {
               m = new Message(-30);
               m.writer().writeByte(-95);
               m.writer().writeInt(timeLength);
               m.writer().flush();
               this.conn.sendMessage(m);
           }
        } catch (IOException var3) {
            var3.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void setPointPB(int point) {
        Message m = null;
        try {
            m = new Message(-28);
            m.writer().writeByte(-84);
            m.writer().writeShort(point);
            m.writer().flush();
            this.conn.sendMessage(m);

        } catch (IOException var3) {
            var3.printStackTrace();
        } finally {
            if(m !=null) {
                m.cleanup();
            }
        }

    }

    public void restCave() {
        Message m = null;
        try {
            m = new Message(-16);
            m.writer().flush();
            this.conn.sendMessage(m);
        } catch (IOException var2) {
            var2.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void getMobMe() {
        if (this.c.get().ItemBody[10] != null) {
            switch(this.c.get().ItemBody[10].id) {
                case 246:
                    this.mobMeMessage(70, (byte)0);
                    break;
                case 419:
                    this.mobMeMessage(122, (byte)0);
                    break;
                case 568:
                    this.mobMeMessage(205, (byte)0);
                    break;
                case 569:
                    this.mobMeMessage(206, (byte)0);
                    break;
                case 570:
                    this.mobMeMessage(207, (byte)0);
                    break;
                case 571:
                    this.mobMeMessage(208, (byte)0);
                    break;
                case 583:
                    this.mobMeMessage(211, (byte)1);
                    break;
                case 584:
                    this.mobMeMessage(236, (byte)1);
                    break;
                case 585:
                    this.mobMeMessage(237, (byte)1);
                    break;
                case 586:
                    this.mobMeMessage(214, (byte)1);
                    break;
                case 587:
                    this.mobMeMessage(238, (byte)1);
                    break;
                case 588:
                    this.mobMeMessage(216, (byte)1);
                    break;
                case 589:
                    this.mobMeMessage(217, (byte)1);
                    break;
                case 742:
                    this.mobMeMessage(229, (byte)1);
                    break;
                case 781:
                    this.mobMeMessage(235, (byte)1);
                    break;
            }
        } else {
            this.mobMeMessage(0, (byte)0);
        }

    }

    public void getMobMeClone() {
        if (this.c.clone.ItemBody[10] != null) {
            switch(this.c.clone.ItemBody[10].id) {
                case 246:
                    this.mobMeCloneMessage(70, (byte)0);
                    break;
                case 419:
                    this.mobMeCloneMessage(122, (byte)0);
                    break;
                case 568:
                    this.mobMeCloneMessage(205, (byte)0);
                    break;
                case 569:
                    this.mobMeCloneMessage(206, (byte)0);
                    break;
                case 570:
                    this.mobMeCloneMessage(207, (byte)0);
                    break;
                case 571:
                    this.mobMeCloneMessage(208, (byte)0);
                    break;
                case 583:
                    this.mobMeCloneMessage(211, (byte)1);
                    break;
                case 584:
                    this.mobMeCloneMessage(236, (byte)1);
                    break;
                case 585:
                    this.mobMeCloneMessage(237, (byte)1);
                    break;
                case 586:
                    this.mobMeCloneMessage(214, (byte)1);
                    break;
                case 587:
                    this.mobMeCloneMessage(238, (byte)1);
                    break;
                case 588:
                    this.mobMeCloneMessage(216, (byte)1);
                    break;
                case 589:
                    this.mobMeCloneMessage(217, (byte)1);
                    break;
                case 742:
                    this.mobMeCloneMessage(229, (byte)1);
                    break;
                case 781:
                    this.mobMeCloneMessage(235, (byte)1);
                    break;
            }
        } else {
            this.mobMeCloneMessage(0, (byte)0);
        }

    }

    public void toNhanBan() {
        try {
            if (!this.c.isNhanban) {
                if (this.c.party != null) {
                    HandleController.RoiNhom(this);
                }
                byte n;
                for(n = 0; n < this.c.get().veff.size(); ++n) {
                    if(this.c.get().veff.get(n) != null) {
                        this.removeEffectMessage(this.c.get().veff.get(n));
                    }
                }
                this.c.isNhanban = true;
                this.c.isHuman = false;
                this.c.clone.islive = true;
                this.c.clone.x = this.c.x;
                this.c.clone.y = this.c.y;
                this.c.tileMap.removeMessage(this.c.clone.id);
                this.c.tileMap.removeMessage(this.c.id);
                Service.CharViewInfo(this);
                GameSrc.sendSkill(this, "KSkill");
                GameSrc.sendSkill(this, "OSkill");
                GameSrc.sendSkill(this, "CSkill");
                this.c.get().x = this.c.clone.x;
                this.c.get().y = this.c.clone.y;
                int i;
                Player player;
                for(i = this.c.tileMap.players.size() - 1; i >= 0; --i) {
                    player = this.c.tileMap.players.get(i);
                    if(player != null) {
                        if (player.id != this.id) {
                            this.c.tileMap.sendCharInfo(this, player);
                            this.c.tileMap.sendCoat(this.c.get(), player);
                            this.c.tileMap.sendGlove(this.c.get(), player);
                        }
                        this.c.tileMap.sendMounts(this.c.get(), player);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public synchronized void exitNhanBan(boolean islive) {
        try {
            if (this.c.isNhanban) {
                if (this.c.clone != null) {
                    this.c.clone.flush();
                }
                if (this.c.clone.party != null) {
                    HandleController.RoiNhom(this);
                }
                for(byte n = 0; n < this.c.get().veff.size(); ++n) {
                    if(this.c.get().veff.get(n) != null) {
                        this.removeEffectMessage(this.c.get().veff.get(n));
                    }
                }
                this.c.isNhanban = false;
                this.c.isHuman = true;
                this.c.clone.islive = islive;
                this.c.x = this.c.clone.x;
                this.c.y = this.c.clone.y;
                this.c.clone.refresh();
                this.c.tileMap.removeMessage(this.c.clone.id);
                Service.CharViewInfo(this);
                GameSrc.sendSkill(this, "KSkill");
                GameSrc.sendSkill(this, "OSkill");
                GameSrc.sendSkill(this, "CSkill");
                Player player;
                int i;
                for(i = this.c.tileMap.players.size() - 1; i >= 0; i--) {
                    player = this.c.tileMap.players.get(i);
                    if(player != null) {
                        if (player.id != this.id) {
                            this.c.tileMap.sendCharInfo(this, player);
                            this.c.tileMap.sendCoat(this.c.get(), player);
                            this.c.tileMap.sendGlove(this.c.get(), player);
                        }
                        this.c.tileMap.sendMounts(this.c.get(), player);
                        if(islive) {
                            Service.sendclonechar(this.c.p, player);
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void changeMap(int id) {
        Map ma = Manager.getMapid(id);
        TileMap[] var3 = ma.area;
        int var4 = var3.length;
        TileMap area;
        int var5;
        for(var5 = 0; var5 < var4; ++var5) {
            area = var3[var5];
            if (area.numplayers < ma.template.maxplayers) {
                this.c.tileMap.leave(this);
                area.EnterMap0(this.c);
                return;
            }
        }

    }

    public void sendRequestBattleToAnother(Char friendNinja, Char _char) {
        Message m = null;
        try {
            m = new Message(-157);
            m.writer().writeInt(_char.id);
            friendNinja.p.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void acceptClanDun(Message m) {
        try {
            if (m.reader().available() < 0) {
                return;
            }
            System.out.println( "Read ------------------------- " + m.reader().available());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void accpetDun(Message m) throws IOException {
        if (m.reader().available() > 0) {

        }
    }

    public void CuuSat(Message msg) throws IOException {
        if (msg.reader().available() > 0) {

        }
    }

    public void inviteToLDT(Message m) {
        try {
            String name = m.reader().readUTF();
            Char _char = Client.gI().getNinja(name);
            if(_char != null && _char.tileMap != null && !_char.tileMap.map.mapLDGT() && _char.tileMap.map.getXHD() == -1 && !_char.tileMap.map.mapChienTruong() && !_char.tileMap.map.mapBossTuanLoc() && !_char.isInDun) {
                Service.startYesNoDlg(_char.p, (byte) 3, this.c.name + " mời bạn vào Lãnh Địa Gia Tộc. Bạn có muốn tham gia?");
            } else {
                this.sendAddchatYellow("Người chơi đang ở vị trí không thể mời vào LDGT.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void sendInfoMeNewItem() {
        Service.CharViewInfo(this, false);
        int i;
        if(this.c.tileMap != null) {
            for (i = this.c.tileMap.players.size() - 1; i >= 0; i--) {
                if (this.c.tileMap.players.get(i) != null) {
                    this.c.tileMap.sendMounts(this.c.get(), this.c.tileMap.players.get(i));
                    if (this.id != this.c.tileMap.players.get(i).id) {
                        Service.sendInfoChar(this, this.c.tileMap.players.get(i));
                    }
                }
            }
        } else if(this.c.tdbTileMap != null) {
            for (i = this.c.tdbTileMap.players.size() - 1; i >= 0; i--) {
                if (this.c.tdbTileMap.players.get(i) != null) {
                    this.c.tdbTileMap.sendMounts(this.c.get(), this.c.tdbTileMap.players.get(i));
                }
            }
        }
    }
}
