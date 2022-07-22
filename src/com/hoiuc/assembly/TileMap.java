package com.hoiuc.assembly;

import com.hoiuc.io.Message;
import com.hoiuc.io.Util;
import com.hoiuc.server.GameCanvas;
import com.hoiuc.server.GameSrc;
import com.hoiuc.server.Manager;
import com.hoiuc.server.Service;
import com.hoiuc.stream.ChienTruong;
import com.hoiuc.stream.Client;
import com.hoiuc.stream.Server;
import com.hoiuc.template.ItemTemplate;
import com.hoiuc.template.SkillOptionTemplate;
import com.hoiuc.template.SkillTemplate;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class TileMap {
    public Map map;
    public byte id;
    public byte numplayers = 0;
    public int numParty = 0;
    public int numTA = 0;
    public int numTL = 0;
    public int numMobDie = 0;
    public ArrayList<Player> players = new ArrayList();
    public ArrayList<Mob> mobs = new ArrayList();
    public ArrayList<ItemMap> itemMap = new ArrayList();
    public ArrayList<Party> aParty = new ArrayList();
    public ArrayList<BuNhin> buNhins = new ArrayList();
    public Object LOCK = new Object();
    public long Delay = 0L;
    private short MOVE_LIMIT = 80;
    private short RESET_LIMIT = 90;

    public TileMap(Map map, byte id) {
        this.map = map;
        this.id = id;
    }

    public void sendMessage(Message m) {
        try {
            for(int i = this.players.size() - 1; i >= 0; --i) {
                if (this.players.get(i) != null && (this.players.get(i)).conn != null) {
                    (this.players.get(i)).conn.sendMessage(m);
                }
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

    public void sendMyMessage(Player p, Message m) {
        try {
            int i;
            Player player;
            for(i = this.players.size() - 1; i >= 0; --i) {
                player = this.players.get(i);
                if(player != null) {
                    if (p.id != player.id && player.conn != null) {
                        player.conn.sendMessage(m);
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

    public synchronized void sendToMap(Message ms) {
        int i;
        Player player;
        for(i = this.players.size() - 1; i >= 0; --i) {
            player = this.players.get(i);
            if(player != null) {
                player.conn.sendMessage(ms);
            }

        }
        if(ms != null) {
            ms.cleanup();
        }

    }

    public Mob getMob(int id) {
        short i;
          for(i = 0; i < this.mobs.size(); i++) {
            if (this.mobs.get(i) != null && (this.mobs.get(i)).id == id) {
                return this.mobs.get(i);
            }
        }
        return null;
    }

    public Char getNinja(int id) {
        synchronized(this) {
            int i;
            for(i = this.players.size() -1;i>=0; i--) {
                if(this.players.get(i) != null && this.players.get(i).c != null && (this.players.get(i)).c.id == id) {
                    return (this.players.get(i)).c;
                }
            }
            return null;
        }
    }

    public short getItemMapNotId() {
        short itemmapid = 0;
        while(true) {
            boolean isset = false;
            for(int i = this.itemMap.size() - 1; i >= 0; --i) {
                if (this.itemMap.get(i) != null && (this.itemMap.get(i)).itemMapId == itemmapid) {
                    isset = true;
                }
            }
            if (!isset) {
                return itemmapid;
            }
            itemmapid++;
        }
    }

    public void leave(Player p) {
        synchronized(this) {
            if (p.c.buNhin != null) {
                p.c.buNhin = null;
            }
            if (p.c.isCuuSat) {
                this.clearCuuSat(p.c);
            }
            if (p.c.isTest) {
                this.endTest(p.c, 0);
                Char player = this.getNinja(p.c.testCharID);
                if (player != null) {
                    player.testCharID = -9999;
                    player.isTest = false;
                }

                p.c.testCharID = -9999;
                p.c.isTest = false;
            }

            if (this.map.cave != null && this.map.cave.ninjas.contains(p.c)) {
                this.map.cave.ninjas.remove(p.c);
            }
            else if (this.map.bossTuanLoc != null && this.map.bossTuanLoc.ninjas.contains(p.c)) {
                this.map.bossTuanLoc.ninjas.remove(p.c);
            }
            else if (this.map.lanhDiaGiaToc != null && this.map.lanhDiaGiaToc.ninjas.contains(p.c)) {
                this.map.lanhDiaGiaToc.ninjas.remove(p.c);
            }
            else if (this.map.giaTocChien != null) {
                if(this.map.giaTocChien.gt1.contains(p.c)) {
                    this.map.giaTocChien.gt1.remove(p.c);
                    if(this.map.id == 117 && this.map.giaTocChien.gt1.size() < 1 && this.map.giaTocChien.isDatCuoc) {
                        this.map.giaTocChien.leave();
                    }
                } else if(this.map.giaTocChien.gt2.contains(p.c)){
                    this.map.giaTocChien.gt2.remove(p.c);
                    if(this.map.id == 117 && this.map.giaTocChien.gt2.size() < 1 && this.map.giaTocChien.isDatCuoc) {
                        this.map.giaTocChien.leave();
                    }
                }

            }
            else if (this.map.dun != null) {
//                if ((this.map.dun.c1 == p.c || this.map.dun.c2 == p.c) && p.c.mapid == 133 && this.map.dun.isMap133 && !this.map.dun.isStart) {
//                    this.map.dun.isMap133 = false;
//                }
                if(this.map.dun.c1 != null && this.map.dun.c1.id == p.c.id && this.map.id == 133 && !this.map.dun.isStart) {
                    this.map.dun.c1 = null;
                    this.map.dun.team1.remove(p.c);
                    this.map.dun.check1();
                } else if(this.map.dun.c2 != null && this.map.dun.c2.id == p.c.id && this.map.id == 133 && !this.map.dun.isStart) {
                    this.map.dun.c2 = null;
                    this.map.dun.team2.remove(p.c);
                    this.map.dun.check1();
                }

                if (!this.map.dun.isStart && !p.c.isInDun && this.map.dun.team1.contains(p.c)) {
                    this.map.dun.team1.remove(p.c);
                }

                if (!this.map.dun.isStart && !p.c.isInDun && this.map.dun.team2.contains(p.c)) {
                    this.map.dun.team2.remove(p.c);
                }

                if (p.c.mapid == 111 && this.map.dun.team1.contains(p.c)) {
                    if (this.map.dun.c1 == p.c) {
                        this.map.dun.c1 = null;
                    }
                    p.c.typepk = 0;
                    Service.ChangTypePkId(p.c, (byte)0);
                    this.map.dun.team1.remove(p.c);
                }

                if (p.c.mapid == 111 && this.map.dun.team2.contains(p.c)) {
                    if (this.map.dun.c2 == p.c) {
                        this.map.dun.c2 = null;
                    }
                    p.c.typepk = 0;
                    Service.ChangTypePkId(p.c, (byte)0);
                    this.map.dun.team2.remove(p.c);
                }

                if (p.c.mapid == 111 && this.map.dun.viewer.contains(p.c)) {
                    this.map.dun.viewer.remove(p.c);
                }
            }
            else if (ChienTruong.chienTruong != null && p.c.tileMap.map.mapChienTruong()) {
                switch (p.c.pheCT) {
                    case 0: {
                        if(ChienTruong.chienTruong.bachGia.contains(p.c)) {
                            ChienTruong.chienTruong.bachGia.remove(p.c);
                        }
                        break;
                    }
                    case 1: {
                        if(ChienTruong.chienTruong.hacGia.contains(p.c)) {
                            ChienTruong.chienTruong.hacGia.remove(p.c);
                        }
                        break;
                    }
                }
            }

            if (this.players.contains(p)) {
                this.players.remove(p);
                this.removeMessage(p.c.id);
                if (p.c.clone != null) {
                    this.removeMessage(p.c.clone.id);
                }
                this.numplayers--;
                if (p.c.party != null && p.c.party.charID == p.c.id && this.aParty.contains(p.c.party)) {
                    this.numParty--;
                    this.aParty.remove(p.c.party);
                }
            }

//            if (this.mobs != null && this.mobs.size() > 0) {
//                synchronized(this.mobs) {
//                    for(int i = 0; i < this.mobs.size(); i++) {
//                        if(this.mobs.get(i) != null) {
//                            (this.mobs.get(i)).removeFight(p.conn.id);
//                        }
//                    }
//                }
//            }

        }
    }

    public void sendCoat(Body b, Player pdo) {
        if(b != null && b.c != null && pdo != null && pdo.c != null && pdo.conn != null) {
            Message m = null;
            try {
                if (b.ItemBody[12] != null) {
                    m = new Message(-30);
                    m.writer().writeByte(-56);
                    m.writer().writeInt(b.id);
                    m.writer().writeInt(b.hp);
                    m.writer().writeInt(b.getMaxHP());
                    m.writer().writeShort(b.ItemBody[12].id);
                    m.writer().flush();
                    pdo.conn.sendMessage(m);
                }
            } catch (Exception var4) {
                var4.printStackTrace();
            } finally {
                if(m != null) {
                    m.cleanup();
                }
            }
        }
    }

    public void sendGlove(Body b, Player pdo) {
        if(b != null && b.c != null && pdo != null && pdo.c != null && pdo.conn != null) {
            Message m = null;
            try {
                if (b.ItemBody[13] != null) {
                    m = new Message(-30);
                    m.writer().writeByte(-55);
                    m.writer().writeInt(b.id);
                    m.writer().writeInt(b.hp);
                    m.writer().writeInt(b.getMaxHP());
                    m.writer().writeShort(b.ItemBody[13].id);
                    m.writer().flush();
                    pdo.conn.sendMessage(m);
                }
            } catch (Exception var4) {
                var4.printStackTrace();
            } finally {
                if(m != null) {
                    m.cleanup();
                }
            }
        }

    }

    public void sendMounts(Body b, Player pdo) {
        if(b != null && b.c != null && pdo != null && pdo.c != null && pdo.c.clone != null && pdo.conn != null) {
            Message m = null;
            try {
                m = new Message(-30);
                m.writer().writeByte(-54);
                m.writer().writeInt(b.id);
                Item item;
                byte i;
                for(i = 0; i < b.ItemMounts.length; i++) {
                    if (b.ItemMounts[i] != null) {
                        item = b.ItemMounts[i];
                        m.writer().writeShort(item.id);
                        m.writer().writeByte(item.upgrade);
                        m.writer().writeLong(item.expires);
                        m.writer().writeByte(item.sys);
                        m.writer().writeByte(item.options.size());
                        for(Option op : item.options) {
                            m.writer().writeByte(op.id);
                            m.writer().writeInt(op.param);
                        }
                    } else {
                        m.writer().writeShort(-1);
                    }
                }
                m.writer().flush();
                pdo.conn.sendMessage(m);
            } catch (Exception var8) {
                var8.printStackTrace();
            } finally {
                if(m != null) {
                    m.cleanup();
                }
            }
        }

    }

    public void VGo(Player p, Message m) {
        try {
            if(m != null && m.reader().available() > 0) {
                m.cleanup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte i;
        short var10001;
        int mapid;
        Map ma;
        byte j;
        byte n;
        for(i = 0; i < this.map.template.vgo.length; i++) {
            if (p.c.get().x + 100 >= this.map.template.vgo[i].minX) {
                var10001 = this.map.template.vgo[i].maxX;
                if (p.c.get().x <= var10001 + 100 && p.c.get().y + 100 >= this.map.template.vgo[i].minY) {
                    var10001 = this.map.template.vgo[i].maxY;
                    if (p.c.get().y <= var10001 + 100) {
                        this.leave(p);
                        if (this.map.id == 138) {
                            mapid = Map.arrLangCo[Util.nextInt(Map.arrLangCo.length)];
                        } else {
                            mapid = this.map.template.vgo[i].mapid;
                        }

                        ma = Manager.getMapid(mapid);
                        if (this.map.cave != null) {
                            for(j = 0; j < this.map.cave.map.length; j++) {
                                if (this.map.cave.map[j].id == mapid) {
                                    ma = this.map.cave.map[j];
                                    break;
                                }
                            }
                        }
                        else if (this.map.lanhDiaGiaToc != null) {
                            for(j = 0; j < this.map.lanhDiaGiaToc.map.length; j++) {
                                if (this.map.lanhDiaGiaToc.map[j].id == mapid) {
                                    ma = this.map.lanhDiaGiaToc.map[j];
                                    break;
                                }
                            }
                        } else if (this.map.chienTruong != null) {
                            for(j = 0; j < this.map.chienTruong.map.length; j++) {
                                if (this.map.chienTruong.map[j].id == mapid) {
                                    ma = this.map.chienTruong.map[j];
                                    break;
                                }
                            }
                        } else if (this.map.giaTocChien != null) {
                            for(j = 0; j < this.map.giaTocChien.map.length; j++) {
                                if (this.map.giaTocChien.map[j].id == mapid) {
                                    ma = this.map.giaTocChien.map[j];
                                    break;
                                }
                            }
                        }

                        for(j = 0; j < ma.template.vgo.length; j++) {
                            if (ma.template.vgo[j].mapid == this.map.id) {
                                p.c.get().x = ma.template.vgo[j].goX;
                                p.c.get().y = ma.template.vgo[j].goY;
                                break;
                            }
                        }

                        byte errornext = -1;

                        for(n = 0; n < p.c.get().ItemMounts.length; n++) {
                            if (p.c.get().ItemMounts[n] != null && p.c.get().ItemMounts[n].isExpires && p.c.get().ItemMounts[n].expires < System.currentTimeMillis()) {
                                errornext = 1;
                                break;
                            }
                        }

                        if (this.map.cave != null && this.map.getXHD() < 9 && this.map.cave.map.length > this.map.cave.level && this.map.cave.map[this.map.cave.level].id < mapid) {
                            errornext = 2;
                        } else if(this.map.mapChienTruong()) {
                            if((mapid == 104 && p.c.pheCT == 0) || (mapid == 98 && p.c.pheCT == 1)) {
                                errornext = 3;
                            } else if((mapid == 99 || mapid == 103) && !ChienTruong.start) {
                                errornext = 4;
                            }
                        } else if(this.map.mapLDGT() && this.map.lanhDiaGiaToc != null) {
                            if((mapid == 81 || mapid == 82 || mapid == 83) && !this.map.lanhDiaGiaToc.cua1) {
                                errornext = 5;
                            }
                            if((mapid == 84 || mapid == 85 || mapid == 86) && (!this.map.lanhDiaGiaToc.cua2_81 || !this.map.lanhDiaGiaToc.cua2_82 || !this.map.lanhDiaGiaToc.cua2_83))
                            {
                                errornext = 5;
                            }
                            if((mapid == 87 || mapid == 88 || mapid == 89) && (!this.map.lanhDiaGiaToc.cua3_84 || !this.map.lanhDiaGiaToc.cua3_85 || !this.map.lanhDiaGiaToc.cua3_86))
                            {
                                errornext = 5;
                            }
                            if(mapid == 90 && (!this.map.lanhDiaGiaToc.cua4_87 || !this.map.lanhDiaGiaToc.cua4_88 || !this.map.lanhDiaGiaToc.cua4_89))
                            {
                                errornext = 5;
                            }
                        } else if(this.map.mapGTC() && this.map.giaTocChien != null) {
                            if((mapid == 119 && p.c.clan.clanName == this.map.giaTocChien.clan1.name) || (mapid == 118 && p.c.clan.clanName == this.map.giaTocChien.clan2.name)) {
                                errornext = 3;
                            } else if((mapid == 120 || mapid == 124) && !this.map.giaTocChien.start) {
                                errornext = 6;
                            }
                        }

                        if (errornext == -1) {
                            byte k;
                            for(k = 0; k < ma.area.length; k++) {
                                if (ma.area[k].numplayers < ma.template.maxplayers) {
                                    if (this.map.id == 138) {
                                        ma.area[k].EnterMap0(p.c);
                                    } else {
                                        p.c.mapid = mapid;
                                        p.c.x = this.map.template.vgo[i].goX;
                                        p.c.y = this.map.template.vgo[i].goY;
                                        if (p.c.clone != null) {
                                            p.c.clone.x = p.c.x;
                                            p.c.clone.y = p.c.y;
                                        }
                                        ma.area[k].Enter(p);
                                    }
                                    return;
                                }
                                if (k == ma.area.length - 1) {
                                    errornext = 0;
                                    break;
                                }
                            }
                        }

                        this.Enter(p);
                        switch(errornext) {
                            case 0: {
                                p.conn.sendMessageLog("Bản đồ quá tải.");
                                return;
                            }
                            case 1: {
                                p.conn.sendMessageLog("Trang bị thú cưỡi đã hết hạn. Vui lòng tháo ra để di chuyển");
                                return;
                            }
                            case 2: {
                                p.conn.sendMessageLog("Cửa " + ma.template.name + " vẫn chưa mở.");
                                return;
                            }
                            case 3: {
                                p.conn.sendMessageLog("Bạn không thể di chuyển tới Căn cứ địa của đối phương.");
                                return;
                            }
                            case 4: {
                                p.conn.sendMessageLog("Đang trong thời gian báo danh. Chiến trường lv30 sẽ được bắt đầu vào 19h30' - 20h30'. Chiến trường lv50 sẽ được mở vào 21h30' - 22h30'.");
                                return;
                            }
                            case 5: {
                                p.conn.sendMessageLog("Cửa chưa được mở.");
                                return;
                            }
                            case 6: {
                                p.conn.sendMessageLog("Trận đấu chưa được bắt đầu.");
                                return;
                            }
                            default: {
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public void EnterMap0(Char n) {
        n.x = this.map.template.x0;
        n.y = this.map.template.y0;
        if(n.clone != null) {
            n.clone.x = n.x;
            n.clone.y = n.y;
        }
        n.mapid = this.map.id;
        this.Enter(n.p);
    }

    public void EnterMap0WithXY(Char n, short x, short y) {
        if (x != -1) {
            n.x = x;
            if(n.clone != null) {
                n.clone.x =n.x;
            }
        } else {
            n.x = this.map.template.x0;
            if(n.clone != null) {
                n.clone.x =n.x;
            }
        }

        if (y != -1) {
            n.y = y;
            if(n.clone != null) {
                n.clone.y = n.y;
            }
        } else {
            n.y = this.map.template.y0;
            if(n.clone != null) {
                n.clone.y = n.y;
            }
        }
        n.mapid = this.map.id;
        this.Enter(n.p);
    }

    public void Enter(Player p) {
        try {
            synchronized(this) {
                this.players.add(p);
                p.c.tileMap = this;
                p.c.tdbTileMap = null;
                p.c.mapid = this.map.id;
                if (p.c.party != null && p.c.party.charID == p.c.id) {
                    this.aParty.add(p.c.party);
                    this.numParty++;
                }
                this.numplayers++;
                p.c.mobAtk = -1;
                p.c.eff5buff = System.currentTimeMillis() + 5000L;
                if (this.map.cave != null) {
                    this.map.cave.ninjas.add(p.c);
                } else if (this.map.bossTuanLoc != null) {
                    this.map.bossTuanLoc.ninjas.add(p.c);
                } else if (this.map.lanhDiaGiaToc != null) {
                    this.map.lanhDiaGiaToc.ninjas.add(p.c);
                } else if(this.map.giaTocChien != null) {
                    if(this.map.giaTocChien.clan1.name.equals(p.c.clan.clanName)) {
                        this.map.giaTocChien.gt1.add(p.c);
                    } else {
                        this.map.giaTocChien.gt2.add(p.c);
                    }
                }

                if(ChienTruong.start && ChienTruong.chienTruong != null && this.map.mapChienTruong()) {
                    switch (p.c.pheCT) {
                        case 0: {
                            ChienTruong.chienTruong.bachGia.add(p.c);
                            break;
                        }
                        case 1: {
                            ChienTruong.chienTruong.hacGia.add(p.c);
                            break;
                        }
                    }
                }

                if (this.map.timeMap != -1L) {
                    if (this.map.cave != null) {
                        p.setTimeMap((int)(this.map.cave.time - System.currentTimeMillis()) / 1000);
                    } else if (this.map.dun != null) {
                        p.setTimeMap((int)(this.map.dun.time - System.currentTimeMillis()) / 1000);
                    } else if (this.map.bossTuanLoc != null) {
                        p.setTimeMap((int)(this.map.bossTuanLoc.time - System.currentTimeMillis()) / 1000);
                    } else if (this.map.lanhDiaGiaToc != null) {
                        p.setTimeMap((int)(this.map.lanhDiaGiaToc.time - System.currentTimeMillis()) / 1000);
                    } else if(this.map.giaTocChien != null) {
                        p.setTimeMap((int)(this.map.giaTocChien.time - System.currentTimeMillis()) / 1000);
                    }
                }

                Message m = new Message(57);
                m.writer().flush();
                p.conn.sendMessage(m);
                m.cleanup();
                m = new Message(-18);
                int mapId = this.map.id;
                if(this.map.id == 118) {
                    mapId = 98;
                } else if(this.map.id == 119) {
                    mapId = 104;
                }
                m.writer().writeByte(mapId);
                m.writer().writeByte(this.map.template.tileID);
                m.writer().writeByte(this.map.template.bgID);
                m.writer().writeByte(this.map.template.typeMap);
                m.writer().writeUTF(this.map.template.name);
                m.writer().writeByte(this.id);
                m.writer().writeShort(p.c.get().x);
                m.writer().writeShort(p.c.get().y);
                m.writer().writeByte(this.map.template.vgo.length);

                int i;
                for(i = 0; i < this.map.template.vgo.length; i++) {
                    m.writer().writeShort(this.map.template.vgo[i].minX);
                    m.writer().writeShort(this.map.template.vgo[i].minY);
                    m.writer().writeShort(this.map.template.vgo[i].maxX);
                    m.writer().writeShort(this.map.template.vgo[i].maxY);
                }

                m.writer().writeByte(this.mobs.size());

                Mob mob;
                int j;
                for(j = 0; j < this.mobs.size(); j++) {
                    if(this.mobs.get(j) != null) {
                        mob = this.mobs.get(j);
                        m.writer().writeBoolean(mob.isDisable());
                        m.writer().writeBoolean(mob.isDonteMove());
                        m.writer().writeBoolean(mob.isFire);
                        m.writer().writeBoolean(mob.isIce);
                        m.writer().writeBoolean(mob.isWind);
                        m.writer().writeByte(mob.templates.id);
                        m.writer().writeByte(mob.sys);
                        m.writer().writeInt(mob.hp);
                        m.writer().writeByte(mob.level);
                        m.writer().writeInt(mob.hpmax);
                        m.writer().writeShort(mob.x);
                        m.writer().writeShort(mob.y);
                        m.writer().writeByte(mob.status);
                        m.writer().writeByte(mob.lvboss);
                        m.writer().writeBoolean(mob.isboss);
                    }
                }

                m.writer().writeByte(this.buNhins.size());

                for(i = this.buNhins.size() - 1; i >= 0; i--) {
                    if(this.buNhins.get(i) != null) {
                        m.writer().writeUTF((this.buNhins.get(i)).name);
                        m.writer().writeShort((this.buNhins.get(i)).x);
                        m.writer().writeShort((this.buNhins.get(i)).y);
                    }
                }

                if(Server.manager.event == 3) {
                    m.writer().writeByte(this.map.template.npc.length);
                    int var8 = this.map.template.npc.length;
                    Npc npc;
                    for (int var9 = 0; var9 < var8; var9++) {
                        npc = this.map.template.npc[var9];
                        m.writer().writeByte(npc.type);
                        m.writer().writeShort(npc.x);
                        m.writer().writeShort(npc.y);
                        m.writer().writeByte(npc.id);
                    }
                } else {
                    int var8 = this.map.template.npc.length;
                    Npc npc;
                    int length = this.map.template.npc.length;
                    for (int var9 = 0; var9 < var8; var9++) {
                        npc = this.map.template.npc[var9];
                        if(npc.id == 34) {
                            length = length - 1;
                        }
                    }
                    m.writer().writeByte(length);

                    int var9;
                    for (var9 = 0; var9 < var8; var9++) {
                        npc = this.map.template.npc[var9];
                        if(npc.id != 34) {
                            m.writer().writeByte(npc.type);
                            m.writer().writeShort(npc.x);
                            m.writer().writeShort(npc.y);
                            m.writer().writeByte(npc.id);
                        }
                    }
                }

                m.writer().writeByte(this.itemMap.size());

                int k;
                ItemMap im;
                for(k = this.itemMap.size() - 1; k>= 0;k--) {
                    im = this.itemMap.get(k);
                    if(im != null) {
                        m.writer().writeShort(im.itemMapId);
                        m.writer().writeShort(im.item.id);
                        m.writer().writeShort(im.x);
                        m.writer().writeShort(im.y);
                    }
                }

                m.writer().writeUTF(this.map.template.name);
                m.writer().writeByte(0);
                m.writer().flush();
                p.conn.sendMessage(m);
                m.cleanup();

                Player player;
                for(k = this.players.size() - 1; k>= 0; k--) {
                    player = this.players.get(k);
                    if(player != null ) {
                        if(player.c != null) {
                            if (player.id != p.id) {
                                this.sendCharInfo(player, p);
                                this.sendCoat(player.c.get(), p);
                                this.sendGlove(player.c.get(), p);

                                this.sendCharInfo(p, player);
                                this.sendCoat(p.c.get(), player);
                                this.sendGlove(p.c.get(), player);

                                if (!p.c.isNhanban && p.c.clone != null && p.c.timeRemoveClone != -1L && p.c.timeRemoveClone > System.currentTimeMillis()) {
                                    Service.sendclonechar(p, player);
                                }
                            }
                            if (player.c.clone != null && !player.c.isNhanban && !player.c.clone.isDie) {
                                Service.sendclonechar(player, p);
                            }
                            this.sendMounts(player.c.get(), p);
                            this.sendMounts(p.c.get(), player);
                        }
                    }
                }

                if(ChienTruong.finish) {
                    p.c.pheCT = -1;
                }

                if(Util.compare_Day(Date.from(Instant.now()), p.c.newlogin)) {
                    p.menuCaiTrang = 0;
                    p.menuIdAuction = -1;
                    p.c.pointCave = 0;
                    p.c.nCave = 1;
                    p.c.useCave = 2;
                    p.c.ddClan = false;
                    p.c.newlogin = Date.from(Instant.now());
                    switch (Util.getDay(p.c.newlogin)) {
                        case "Saturday": {
                            p.c.countTDB = 5;
                            break;
                        }
                        case "Sunday": {
                            p.c.countTDB = 5;
                            break;
                        }
                        default: {
                            p.c.countTDB = 999999;
                            break;
                        }
                    }
                    p.c.isDiemDanh = 0;
                    p.c.countWin = 0;
                    p.c.isQuaHangDong = 0;
                    p.c.countHangDong = 0;
                    p.c.countBuyX3 = 6;
                    p.c.useTaThuLenh = 1;
                    p.c.useDanhVongPhu = 6;
                    p.c.isTaskHangNgay = 0;
                    p.c.isTaskTaThu = 0;
                    p.c.isTaskDanhVong = 0;
                    p.c.countTaskHangNgay = 0;
                    p.c.countTaskTaThu = 0;
                    p.c.countTaskDanhVong = 20;
                    p.c.taskHangNgay = new int[]{-1, -1, -1, -1, -1, 0, 0};
                    p.c.taskTaThu = new int[]{-1, -1, -1, -1, -1, 0, 0};
                    p.c.taskDanhVong = new int[]{-1, -1, -1, 0, 20};

                    p.c.pheCT = -1;
                    p.c.pointCT = 0;

                    p.c.isNhanQuaNoel = 1;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void move(int id, short x, short y) {
        Message m = null;
        try {
            m = new Message(1);
            m.writer().writeInt(id);
            m.writer().writeShort(x);
            m.writer().writeShort(y);
            m.writer().flush();
            this.sendMessage(m);
        } catch (IOException var5) {
            var5.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void removeItemMapMessage(short itemmapid) {
        Message m = null;
        try {
            m = new Message(-15);
            m.writer().writeShort(itemmapid);
            m.writer().flush();
            this.sendMessage(m);
        } catch (IOException var5) {
            var5.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void removeBuNhin(short id) {
        Message m = null;
        try {
            if(this.buNhins.get(id) != null) {
                this.buNhins.remove(id);
                m = new Message((byte)77);
                m.writer().writeShort(id);
                m.writer().flush();
                this.sendMessage(m);
            }
        } catch (IOException var3) {
            var3.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void refreshMob(int mobid) {
        Message m = null;
        try {
            synchronized(this) {
                Mob mob = this.getMob(mobid);
                if(mob != null) {
                    mob.ClearFight();
                    mob.sys = (byte)Util.nextInt(1, 3);
                    if (this.map.cave == null && mob.lvboss != 3 && !mob.isboss && this.map.lanhDiaGiaToc == null) {
                        if (mob.lvboss > 0) {
                            mob.lvboss = 0;
                        }

                        if (mob.level >= 10 && 2 > Util.nextInt(100) && this.numTA < 3 && this.numTL < 1 && !this.map.mapChienTruong()) {
                            mob.lvboss = Util.nextInt(1, 2);
                        }
                    }

                    int n4;
                    if (this.map.cave != null && this.map.cave.finsh > 0 && this.map.getXHD() == 6) {
                        n4 = mob.templates.hp * (10 * this.map.cave.finsh + 100) / 100;
                        mob.hpmax = n4;
                        mob.hp = n4;
                    }
                    else {
                        n4 = mob.templates.hp;
                        mob.hpmax = n4;
                        mob.hp = n4;
                    }

                    if (mob.lvboss == 3) {
                        n4 = mob.hpmax * 200;
                        mob.hpmax = n4;
                        mob.hp = n4;
                    } else if (mob.lvboss == 2) {
                        this.numTL++;
                        n4 = mob.hpmax * 100;
                        mob.hpmax = n4;
                        mob.hp = n4;
                    } else if (mob.lvboss == 1) {
                        this.numTA++;
                        n4 = mob.hpmax * 10;
                        mob.hpmax = n4;
                        mob.hp = n4;
                    }

                    mob.setSkill25();

                    mob.status = 5;
                    mob.isDie = false;
                    mob.timeRefresh = 0L;
                    m = new Message(-5);
                    m.writer().writeByte(mob.id);
                    m.writer().writeByte(mob.sys);
                    m.writer().writeByte(mob.lvboss);
                    m.writer().writeInt(mob.hpmax);
                    m.writer().flush();
                    this.sendMessage(m);
                }

            }
        } catch (IOException var7) {
            var7.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    private void attachedMob(int dame, int mobid, boolean fatal) {
        Message m = null;
        try {
            Mob mob = this.getMob(mobid);
            if (mob != null) {
                m = new Message(-1);
                m.writer().writeByte(mobid);
                m.writer().writeInt(mob.hp);
                m.writer().writeInt(dame);
                m.writer().writeBoolean(fatal);
                m.writer().writeByte(mob.lvboss);
                m.writer().writeInt(mob.hpmax);
                m.writer().flush();
                this.sendMessage(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    private void MobStartDie(int dame, int mobid, boolean fatal) {
        Message m = null;
        try {
            m = new Message(-4);
            m.writer().writeByte(mobid);
            m.writer().writeInt(dame);
            m.writer().writeBoolean(fatal);
            m.writer().flush();
            this.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    private void sendXYPlayer(Player p) {
        Message m = null;
        try {
            m = new Message(52);
            m.writer().writeShort(p.c.get().x);
            m.writer().writeShort(p.c.get().y);
            m.writer().flush();
            p.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    private void setXYPlayers(short x, short y, Player p1, Player p2) {
        if(p1 != null && p2 != null && p1.c != null && p2.c != null) {
            p2.c.get().x = x;
            p1.c.get().x = x;
            p2.c.get().y = y;
            p1.c.get().y = y;
            Message m = null;
            try {
                m = new Message(64);
                m.writer().writeInt(p1.c.get().id);
                m.writer().writeShort(p1.c.get().x);
                m.writer().writeShort(p1.c.get().y);
                m.writer().writeInt(p2.c.get().id);
                m.writer().flush();
                this.sendMessage(m);
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if(m != null) {
                    m.cleanup();
                }
            }
        }

    }

    public void removeMessage(int id) {
        Message m = null;
        try {
            m = new Message(2);
            m.writer().writeInt(id);
            m.writer().flush();
            this.sendMessage(m);
        } catch (IOException var3) {
            var3.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void sendCharInfo(Player p, Player p2) {
        if(p!= null && p.c != null && p2 != null && p2.c != null) {
            Message m = null;
            try {
                m = new Message(3);
                m.writer().writeInt(p.c.get().id);
                m.writer().writeUTF(p.c.clan.clanName);
                if (!p.c.clan.clanName.isEmpty()) {
                    m.writer().writeByte(p.c.clan.typeclan);
                }
                m.writer().writeBoolean(false);
                m.writer().writeByte(p.c.get().typepk);
                m.writer().writeByte(p.c.get().nclass);
                m.writer().writeByte(p.c.gender);
                m.writer().writeShort(p.c.get().partHead());
                m.writer().writeUTF(p.c.name);
                m.writer().writeInt(p.c.get().hp);
                m.writer().writeInt(p.c.get().getMaxHP());
                m.writer().writeByte(p.c.get().level);
                m.writer().writeShort(p.c.get().Weapon());
                m.writer().writeShort(p.c.get().Body());
                m.writer().writeShort(p.c.get().Leg());
                m.writer().writeByte(-1);
                m.writer().writeShort(p.c.get().x);
                m.writer().writeShort(p.c.get().y);
                m.writer().writeShort(p.c.get().eff5buffHP());
                m.writer().writeShort(p.c.get().eff5buffMP());
                m.writer().writeByte(0);
                m.writer().writeBoolean(p.c.isHuman);
                m.writer().writeBoolean(p.c.isNhanban);
                m.writer().writeShort(p.c.get().partHead());
                m.writer().writeShort(p.c.get().Weapon());
                m.writer().writeShort(p.c.get().Body());
                m.writer().writeShort(p.c.get().Leg());

                m.writer().writeShort(p.c.get().ID_HAIR);
                m.writer().writeShort(p.c.get().ID_Body);
                m.writer().writeShort(p.c.get().ID_LEG);
                m.writer().writeShort(p.c.get().ID_WEA_PONE);
                m.writer().writeShort(p.c.get().ID_PP);
                m.writer().writeShort(p.c.get().ID_NAME);
                m.writer().writeShort(p.c.get().ID_HORSE);
                m.writer().writeShort(p.c.get().ID_RANK);
                m.writer().writeShort(p.c.get().ID_MAT_NA);
                m.writer().writeShort(p.c.get().ID_Bien_Hinh);

                m.writer().flush();
                p2.conn.sendMessage(m);
                m.cleanup();

                if (p.c.get().mobMe != null) {
                    m = new Message(-30);
                    m.writer().writeByte(-68);
                    m.writer().writeInt(p.c.get().id);
                    m.writer().writeByte(p.c.get().mobMe.templates.id);
                    m.writer().writeByte(p.c.get().mobMe.isboss ? 1 : 0);
                    m.writer().flush();
                    p2.conn.sendMessage(m);
                    m.cleanup();
                }
            } catch (Exception var4) {
                var4.printStackTrace();
            } finally {
                if(m != null) {
                    m.cleanup();
                }
            }
        }

    }

    public void handleAfterAttackNinja(Player p, Char[] arNinja) {
        int i;
        Char fightChar;
        for(i = 0; i < arNinja.length; ++i) {
            fightChar = arNinja[i];
            if (fightChar != null) {
                if (fightChar.get().percentIce() >= Util.nextInt(1, 100)) {
                    p.removeEffect(15);
                    p.removeEffect(16);
                    if (p.c.getEffId(20) != null) {
                        if (p.c.get().nclass == 6) {
                            p.setEffect(6, 0, -1, 0);
                        } else {
                            p.setEffect(6, 0, fightChar.get().timeIce() - 1000 - p.c.get().getPramSkill(38) * 100, 0);
                        }
                    } else {
                        p.setEffect(6, 0, fightChar.get().timeIce() - p.c.get().getPramSkill(38) * 100, 0);
                    }
                } else {
                    int dame = Util.nextInt(p.c.get().dameMin(), p.c.get().dameMax());
                    switch(fightChar.c.get().Sys()) {
                        case 1: {
                            dame += dame * p.c.get().getPramSkill(54) / 100;
                            break;
                        }
                        case 2: {
                            dame += dame * p.c.get().getPramSkill(55) / 100;
                            break;
                        }
                        case 3: {
                            dame += dame * p.c.get().getPramSkill(56) / 100;
                            break;
                        }
                    }
                    dame += p.c.get().getPramItem(103);

                    int oldhp;
                    switch(p.c.get().Sys()) {
                        case 1: {
                            oldhp = fightChar.c.get().ResFire() * 11 / 100;
                            oldhp += 795;
                            dame /= oldhp;
                            dame *= 100;
                            dame += p.c.get().getPramItem(51);
                            dame -= fightChar.c.get().getPramItem(48);
                            break;
                        }
                        case 2: {
                            oldhp = fightChar.c.get().ResIce() * 11 / 100;
                            oldhp += 795;
                            dame /= oldhp;
                            dame *= 100;
                            dame += p.c.get().getPramItem(52);
                            dame -= fightChar.c.get().getPramItem(49);
                            break;
                        }
                        case 3: {
                            oldhp = fightChar.c.get().ResWind() * 11 / 100;
                            oldhp += 795;
                            dame /= oldhp;
                            dame *= 100;
                            dame += p.c.get().getPramItem(53);
                            dame -= fightChar.c.get().getPramItem(50);
                            break;
                        }
                    }

                    dame -= fightChar.c.get().dameDown();
                    if (p.c.get().Fatal() > Util.nextInt(1000)) {
                        dame = dame * 2 + dame * (p.c.get().percentFantalDame() - fightChar.c.get().percentFantalDameDown()) / 100;
                        dame += p.c.get().FantalDame();

                    }

                    if (fightChar.c.get().getEffId(5) != null) {
                        dame *= 2;
                    }

                    oldhp = fightChar.hp;
                    if (dame <= 0) {
                        dame = 1;
                    }

                    int miss = p.c.get().Exactly() * 10000 / fightChar.Miss();
                    miss -= fightChar.get().getPramSkill(31) * 100;
                    if (miss < Util.nextInt(10000)) {
                        dame = 0;
                    } else {
                        if (p.c.percentFire2() >= Util.nextInt(1, 100)) {
                            if (fightChar.c.getEffId(20) != null) {
                                if (fightChar.c.get().nclass == 6) {
                                    fightChar.p.setEffect(5, 0, -1, 0);
                                } else {
                                    fightChar.p.setEffect(5, 0, -1, 0);
                                }
                            } else {
                                fightChar.p.setEffect(5, 0, 2000 - fightChar.c.get().getPramSkill(37) * 100, 0);
                            }
                        }

                        if (p.c.percentFire4() >= Util.nextInt(1, 100)) {
                            if (fightChar.c.getEffId(20) != null) {
                                if (fightChar.c.get().nclass == 6) {
                                    fightChar.p.setEffect(5, 0, 1000, 0);
                                } else {
                                    fightChar.p.setEffect(5, 0, 2000 - fightChar.c.get().getPramSkill(37) * 100, 0);
                                }
                            } else {
                                fightChar.p.setEffect(5, 0, 4000 - fightChar.c.get().getPramSkill(37) * 100, 0);
                            }
                        }

                        if (p.c.percentIce1_5() >= Util.nextInt(1, 100)) {
                            if (fightChar.c.getEffId(20) != null) {
                                if (fightChar.c.get().nclass == 6) {
                                    fightChar.p.setEffect(6, 0, -1, 0);
                                } else {
                                    fightChar.p.setEffect(6, 0, 500 - fightChar.c.get().getPramSkill(38) * 100, 0);
                                }
                            } else {
                                fightChar.p.setEffect(6, 0, 1500 - fightChar.c.get().getPramSkill(38) * 100, 0);
                            }
                        }

                        if (p.c.percentIce3() >= Util.nextInt(1, 100)) {
                            if (fightChar.c.getEffId(20) != null) {
                                if (fightChar.c.get().nclass == 6) {
                                    fightChar.p.setEffect(6, 0, 1000, 0);
                                } else {
                                    fightChar.p.setEffect(6, 0, 2000 - fightChar.c.get().getPramSkill(38) * 100, 0);
                                }
                            } else {
                                fightChar.p.setEffect(6, 0, 3000 - fightChar.c.get().getPramSkill(38) * 100, 0);
                            }
                        }

                        if (p.c.percentWind1() >= Util.nextInt(1, 100)) {
                            if (fightChar.c.getEffId(20) != null) {
                                if (fightChar.c.get().nclass == 6) {
                                    fightChar.p.setEffect(7, 0, -1, 0);
                                } else {
                                    fightChar.p.setEffect(7, 0, 500 - fightChar.c.get().getPramSkill(39) * 100, 0);
                                }
                            } else {
                                fightChar.p.setEffect(7, 0, 1000 - fightChar.c.get().getPramSkill(39) * 100, 0);
                            }
                        }

                        if (p.c.percentWind2() >= Util.nextInt(1, 100)) {
                            if (fightChar.c.getEffId(20) != null) {
                                if (fightChar.c.get().nclass == 6) {
                                    fightChar.p.setEffect(7, 0, 1000, 0);
                                } else {
                                    fightChar.p.setEffect(7, 0, 1500 - fightChar.c.get().getPramSkill(39) * 100, 0);
                                }
                            } else {
                                fightChar.p.setEffect(7, 0, 2000 - fightChar.c.get().getPramSkill(39) * 100, 0);
                            }
                        }
                    }

                    int j;
                    for(j = p.c.veff.size() - 1; j >= 0; --j) {
                        if ((p.c.veff.get(j)).template.type == 11) {
                            dame *= (p.c.get().getPramSkill(61) + 100) / 100;
                        }
                    }

                    int odhp = p.c.hp;
                    if (fightChar.c.get().ReactDame() > Util.nextInt(1000)) {
                        p.c.upHP(-dame / 10);
                        this.attached(odhp - p.c.hp, p.c.id);
                    }

                    fightChar.upHP(-dame);
                    this.attached(oldhp - fightChar.hp, fightChar.id);
                    if (fightChar.isDie) {
                        if (p.c.get().typepk == 1 || p.c.get().typepk == 3 || p.c.isCuuSat) {
                            if (p.c.isCuuSat) {
                                p.c.get().updatePk(2);
                            } else {
                                p.c.get().updatePk(1);
                            }

                            if (p.c.isTaskDanhVong == 1 && p.c.taskDanhVong[0] == 5) {
                                p.c.taskDanhVong[1]++;
                                if(p.c.taskDanhVong[1] == p.c.taskDanhVong[2]) {
                                    p.sendAddchatYellow("Bạn đã hoàn thành nhiệm vụ danh vọng.");
                                }
                            }
                        }

                        if(p.c.tileMap.map.mapChienTruong()) {
                            p.c.pointCT += 3;
                            if(p.c.pointCT > 14000) {
                                p.c.pointCT = 14000;
                            }
                            Service.updatePointCT(p.c, 3);
                            p.sendAddchatYellow("Bạn vừa đánh trọng thương " + fightChar.name);
                        } else if(p.c.tileMap.map.mapGTC()) {
                            p.c.pointGTC += 3;
                            if(p.c.pointGTC > 14000) {
                                p.c.pointGTC = 14000;
                            }
                            Service.sendPointGTC(p.c, 3);
                            p.c.p.sendAddchatYellow("Bạn vừa đánh trọng thương " + fightChar.name);
                        }

                        if (fightChar.pk > 0) {
                            if (fightChar.pk > 3) {
                                long expTEMP = Level.getMaxExp(fightChar.level);
                                Level levelTEMP = Level.getLevel(fightChar.level);
                                if (fightChar.exp > expTEMP) {
                                    fightChar.expdown = 0L;
                                    fightChar.exp -= levelTEMP.exps * (long)(5 + fightChar.pk) / 100L;
                                    if (fightChar.exp < expTEMP) {
                                        fightChar.exp = expTEMP;
                                    }
                                } else {
                                    fightChar.exp = Level.getMaxExp(arNinja[i].level);
                                    fightChar.expdown += levelTEMP.exps * (long)(5 + fightChar.pk) / 100L;
                                    if (fightChar.expdown > levelTEMP.exps * 50L / 100L) {
                                        fightChar.expdown = levelTEMP.exps * 50L / 100L;
                                    }
                                }
                            }
                            fightChar.updatePk(-1);
                        }
                        fightChar.type = 14;
                        this.sendDie(fightChar);
                    }
                }
            }
        }
        p.removeEffect(15);
        p.removeEffect(16);
        p.c.setTimeKickSession();
    }

    public long handleAfterAttackMob(Mob mob3, Char _char, long xpup) {
        if (mob3 != null && _char != null) {
            int dame = Util.nextInt(_char.get().dameMin(), _char.get().dameMax());
            if (this.map.cave == null && mob3.isboss && mob3.templates.id != 230) {
                if(_char.isNhanban && Math.abs(_char.clone.level - mob3.level) > 20) {
                    dame = 1;
                } else if(_char.isHuman && Math.abs(_char.level - mob3.level) > 20) {
                    dame = 1;
                }
            }
           
            switch (mob3.sys) {
                case 1: {
                    dame += dame * _char.c.get().getPramItem(54) / 100;
                    break;
                }
                case 2: {
                    dame += dame * _char.c.get().getPramItem(55) / 100;
                    break;
                }
                case 3: {
                    dame += dame * _char.c.get().getPramItem(56) / 100;
                    break;
                }
            }

            int oldhp = mob3.hp;
            int fatal = _char.get().Fatal();
            if (fatal > 800) {
                fatal = 800;
            }
            boolean isfatal = fatal > Util.nextInt(1, 1000);
            if (isfatal) {
                dame *= 2;
                dame = dame * (100 + _char.get().percentFantalDame() / 2) / 100;
                dame += _char.get().FantalDame();
            }
            if (dame <= 0) {
                dame = 1;
            }
            if (mob3.isFire) {
                dame *= 2;
            }

            dame += _char.get().getPramItem(102);

            if (_char.isNhanban) {
                dame = dame * _char.clone.percendame / 100;
            }
            if(this.map.lanhDiaGiaToc != null && this.map.mapLDGT()) {
                if(this.map.id == 82) {
                    if(Util.nextInt(10) < 3) {
                        dame = 1;
                    }
                } else if (this.map.id == 83) {
                    if(Util.nextInt(10) < 3) {
                        this.MobAtkMessage(mob3.id, _char, dame/3, 0, (short)-1, (byte)-1, (byte)-1);
                    }
                }
            }
            int xpnew;
            Effect eff;
            for(xpnew = _char.veff.size() - 1; xpnew >= 0; --xpnew) {
                eff = _char.veff.get(xpnew);
                if (eff != null && eff.template.type == 11) {
                    dame *= (_char.get().getPramSkill(61) + 100) / 100;
                }
            }
            if (this.map.cave != null || (mob3.level > 1 && Math.abs(mob3.level - _char.get().level) <= 10)) {
                if(mob3.level >= 50 && mob3.level < 60) {
                    xpnew = dame * 8 / 10;
                }
                else if(mob3.level >= 60 && mob3.level < 70) {
                    xpnew = dame * 6 / 10;
                }
                else {
                    xpnew = dame * 4 / 10 + mob3.level * Util.nextInt(7, 15);
                }

                if(mob3.level > _char.level) {
                    xpnew += (mob3.level-_char.level) * Util.nextInt(100,1000);
                }

                if (_char.get().getEffType((byte)18) != null) {
                    xpnew *= _char.get().getEffType((byte)18).param;
                }

                if (mob3.lvboss == 1) {
                    xpnew *= 3;
                } else if (mob3.lvboss == 2) {
                    xpnew *= 8;
                } else if (mob3.lvboss == 3) {
                    xpnew /= 2;
                }

                if (this.map.LangCo()) {
                    xpnew = xpnew * 110 / 100;
                } else if (this.map.VDMQ()) {
                    xpnew *= 2;
                }
                xpup += (long)xpnew;
            }

            if(!mob3.isDie && mob3.hp > 0) {
                mob3.updateHP(-dame, _char.id, true);
            }

            if (dame > 0) {
                mob3.Fight(_char.p.conn.id, dame);
            }

            if(mob3.idCharSkill25 == -1 && _char.get().getEffId(9) != null) {
                this.FireMobSkill25(mob3.id, _char.id, _char.get().getEffId(9).param*_char.get().dameSide()/100);
            }

            if (!mob3.isFire) {
                if (_char.get().percentFire2() >= Util.nextInt(1, 100)) {
                    this.FireMobMessage(mob3.id, 0);
                }

                if (_char.get().percentFire4() >= Util.nextInt(1, 100)) {
                    this.FireMobMessage(mob3.id, 1);
                }
            }

            if (!mob3.isIce) {
                if(mob3.isboss) {
                    if (_char.get().percentIce1_5() >= Util.nextInt(1, 150)) {
                        this.IceMobMessage(mob3.id, 0);
                    }
                    if (_char.get().percentIce3() >= Util.nextInt(1, 150)) {
                        this.IceMobMessage(mob3.id, 1);
                    }
                } else {
                    if (_char.get().percentIce1_5() >= Util.nextInt(1, 100)) {
                        this.IceMobMessage(mob3.id, 0);
                    }
                    if (_char.get().percentIce3() >= Util.nextInt(1, 100)) {
                        this.IceMobMessage(mob3.id, 1);
                    }
                }

            }

            if (!mob3.isWind) {
                if (_char.get().percentWind1() >= Util.nextInt(1, 100)) {
                    this.WindMobMessage(mob3.id, 0);
                }

                if (_char.get().percentWind2() >= Util.nextInt(1, 100)) {
                    this.WindMobMessage(mob3.id, 1);
                }
            }
            
            if(mob3.templates.id == 231 && mob3.isDie) {
                for (byte j = 0; j < _char.c.get().ItemBody.length; j++) {
                        Item item = _char.c.get().ItemBody[j];
                         if( item != null && item.id == 745) {
                            _char.c.pointBossChuot++;
                            }
                        }
            }

            int i;
            int master;
            if (mob3.isDie) {
                this.MobStartDie(oldhp - mob3.hp, mob3.id, isfatal);
            }
            else {
                this.attachedMob(oldhp - mob3.hp, mob3.id, isfatal);
                for(master = _char.veff.size() - 1; master >= 0; --master) {
                    eff = _char.veff.get(master);
                    if (eff.template.type == 5) {
                        i = Util.nextInt(_char.get().dameMin(), _char.get().dameMax());
                        i *= _char.getPramSkill(51);
                        i /= 100;
                        int old = mob3.hp;
                        if (this.Delay < System.currentTimeMillis()) {
                            this.Delay = System.currentTimeMillis() + 1500L;
                            if(!mob3.isDie && mob3.hp > 0) {
                                mob3.updateHP(-i, _char.id, true);
                            }
                            this.attachedMob(old - mob3.hp, mob3.id, false);
                        }
                    }
                }
            }
        }

        return xpup;
    }

    public void handleAfterCloneAttackMob(Mob mob3, CloneCharacter _char) throws IOException {
        if (mob3 != null && _char != null) {
            int dame = Util.nextInt(_char.dameMin(), _char.dameMax());
            if (this.map.cave == null && mob3.isboss && Math.abs(_char.c.level - mob3.level) > 20 && mob3.templates.id != 230) {
                dame = 1;
            }
            switch (mob3.sys) {
                case 1: {
                    dame += dame * _char.getPramItem(54) / 100;
                    break;
                }
                case 2: {
                    dame += dame * _char.getPramItem(55) / 100;
                    break;
                }
                case 3: {
                    dame += dame * _char.getPramItem(56) / 100;
                    break;
                }
            }

            dame += _char.getPramItem(102);

            int oldhp = mob3.hp;
            int fatal = _char.Fatal();
            if (fatal > 800) {
                fatal = 800;
            }

            boolean isfatal = fatal > Util.nextInt(1, 1000);
            if (isfatal) {
                dame *= 2;
                dame = dame * (100 + _char.percentFantalDame() / 2) / 100;
                dame += _char.FantalDame();
            }

            if (dame <= 0) {
                dame = 1;
            }

            if (mob3.isFire) {
                dame *= 2;
            }
            dame += _char.getPramItem(102);
            if(!mob3.isDie && mob3.hp > 0) {
                mob3.updateHP(-dame, _char.c.id, true);
            }
            if (dame > 0) {
                mob3.Fight(_char.c.p.conn.id, dame);
            }

            if(mob3.idCharSkill25 == -1 && _char.getEffId(9) != null) {
                this.FireMobSkill25(mob3.id, _char.c.id, _char.getEffId(9).param*_char.dameSide()/100);
            }

            if (!mob3.isFire) {
                if (_char.percentFire2() >= Util.nextInt(1, 100)) {
                    this.FireMobMessage(mob3.id, 0);
                }

                if (_char.percentFire4() >= Util.nextInt(1, 100)) {
                    this.FireMobMessage(mob3.id, 1);
                }
            }

            if (!mob3.isIce) {
                if(mob3.isboss) {
                    if (_char.percentIce1_5() >= Util.nextInt(1, 150)) {
                        this.IceMobMessage(mob3.id, 0);
                    }
                    if (_char.percentIce3() >= Util.nextInt(1, 150)) {
                        this.IceMobMessage(mob3.id, 1);
                    }
                } else {
                    if (_char.percentIce1_5() >= Util.nextInt(1, 100)) {
                        this.IceMobMessage(mob3.id, 0);
                    }
                    if (_char.percentIce3() >= Util.nextInt(1, 100)) {
                        this.IceMobMessage(mob3.id, 1);
                    }
                }
            }

            if (!mob3.isWind) {
                if (_char.percentWind1() >= Util.nextInt(1, 100)) {
                    this.WindMobMessage(mob3.id, 0);
                }

                if (_char.percentWind2() >= Util.nextInt(1, 100)) {
                    this.WindMobMessage(mob3.id, 1);
                }
            }

            if (mob3.isDie) {
                this.MobStartDie(oldhp - mob3.hp, mob3.id, isfatal);
            } else {
                this.attachedMob(oldhp - mob3.hp, mob3.id, isfatal);
                int j;
                Effect eff;
                for(j = _char.veff.size() - 1; j >= 0; --j) {
                    eff = _char.veff.get(j);
                    if (eff.template.type == 5) {
                        int damage = Util.nextInt(_char.dameMin(), _char.dameMax());
                        damage *= _char.getPramSkill(51);
                        damage /= 100;
                        int old = mob3.hp;
                        if (this.Delay < System.currentTimeMillis()) {
                            this.Delay = System.currentTimeMillis() + 1500L;
                            if(!mob3.isDie && mob3.hp > 0) {
                                mob3.updateHP(-damage, _char.c.id, true);
                            }
                            this.attachedMob(old - mob3.hp, mob3.id, false);
                        }
                    }
                }
            }
        }

    }

    public synchronized void PlayerAttack(Char _char, Mob[] arrMob, Char[] arrChar) throws IOException {
        if (_char.skill != null) {
            Skill skill = _char.get().getSkill(_char.get().CSkill);
            SkillOptionTemplate data = SkillTemplate.Templates(skill.id, skill.point);
            skill.coolDown = System.currentTimeMillis() + (long)data.coolDown;
            _char.get().upMP(-data.manaUse);
            if (arrMob != null && arrChar != null) {
                try {
                    int i;
                    Player player;
                    for(i = this.players.size() - 1; i>= 0; i--) {
                        player = this.players.get(i);
                        if (player.c != null && player.conn != null && player.c.id != _char.id) {
                            Service.PlayerAttack(player.c, _char.id, skill.id, arrMob, arrChar);
                        }
                    }
                } catch (Exception var12) {
                    var12.printStackTrace();
                }
            }

            byte i;
            int miss;
            if (arrMob != null) {
                long xpup = 0L;

                for(i = 0; i < arrMob.length; ++i) {
                    xpup = this.handleAfterAttackMob(arrMob[i], _char, xpup);
                }

                if (xpup > 0L && xpup > 0L) {
                    if (_char.level >= 90 && _char.clone.islive) {
                        Skill ski = null;
                        if (_char.level >= 90 && _char.clone.islive) {
                            for(byte sk = 67; sk <= 72; ++sk) {
                                ski = _char.get().getSkill(sk);
                                if (ski != null) {
                                    if (ski.point < 10) {
                                        _char.expSkillClone += xpup;
                                        if (_char.expSkillClone >= GameSrc.upExpSkillClone[ski.point - 1] * 1000L) {
                                            _char.expSkillClone = 0L;
                                            ++ski.point;
                                            _char.p.loadSkill();
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }

                    if (Manager.up_exp > 1) {
                        xpup *= (long)Manager.up_exp;
                    }

                    if (this.map.cave != null) {
                        this.map.cave.updateXP(xpup * 2L);
                    } else {
                        if (_char.isNhanban) {
                            xpup /= 4L;
                        }
                        _char.p.updateExp(xpup);
                        if (_char.get().party != null) {
                            Player pl;
                            for(miss = this.players.size() - 1; miss >= 0; miss--) {
                                pl = this.players.get(miss);
                                if (pl != null && pl.c != null && pl.c.id != _char.id && pl.c.party == _char.party && Math.abs(pl.c.level - _char.level) <= 10) {
                                    pl.updateExp(xpup * 13L / 100L);
                                }
                            }
                        }
                    }
                }
            }

            if (arrChar != null) {
                this.handleAfterAttackNinja(_char.p, arrChar);
            }
        }

    }

    public ItemMap LeaveItem(short id, short x, short y, byte type, boolean isBoss) {
        Message m = null;
        try {
            if (this.itemMap.size() > 100) {
                this.removeItemMapMessage((this.itemMap.remove(0)).itemMapId);
            }

            ItemTemplate data = ItemTemplate.ItemTemplateId(id);
            Item item;
            if (data.type < 10) {
                if (data.type == 1) {
                    item = ItemTemplate.itemDefault(id);
                    item.sys = GameSrc.SysClass(data.nclass);
                } else {
                    byte sys = (byte)Util.nextInt(1, 3);
                    item = ItemTemplate.itemDefault(id, sys);
                }
            } else {
                item = ItemTemplate.itemDefault(id);
            }

            ItemMap im = new ItemMap();
            im.itemMapId = this.getItemMapNotId();
            if (isBoss) {
                im.x = (short)Util.nextInt(x - 120, x + 120);
                im.removedelay = 90000L + System.currentTimeMillis();
            } else {
                im.x = (short)Util.nextInt(x - 30, x + 30);
                if(this.map.mapLDGT()) {
                    im.removedelay = 90000L + System.currentTimeMillis();
                }
            }

            if (type != 5 && type != 4) {
                im.y = y;
            } else {
                im.y = this.map.touchY(x, y);
            }

            im.item = item;
            this.itemMap.add(im);
            m = new Message(6);
            m.writer().writeShort(im.itemMapId);
            m.writer().writeShort(item.id);
            m.writer().writeShort(im.x);
            m.writer().writeShort(im.y);
            m.writer().flush();
            this.sendMessage(m);
            m.cleanup();
            return im;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void FightMob(Player p, Message m) {
        try {
            if (p.c.get().CSkill == -1 && p.c.get().skill.size() > 0) {
                p.c.get().CSkill = (p.c.get().skill.get(0)).id;
            }

//            if (p.c.clone != null && p.c.clone.islive && p.c.clone.CSkill == -1 && p.c.clone.skill.size() > 0) {
//                p.c.clone.CSkill = (p.c.clone.skill.get(0)).id;
//            }

            Skill skill = p.c.get().getSkill(p.c.get().CSkill);
            if (skill != null) {
                int mobId = m.reader().readUnsignedByte();
                synchronized(this) {
                    Mob mob = this.getMob(mobId);
                    Mob[] arMob = new Mob[10];
                    arMob[0] = mob;
                    if (p.c.get().getPramItem(130) > 0 && Util.nextInt(1,50) == 50){
                         GameCanvas.addEffect(p.conn, (byte) 1, arMob[0].id, (byte) 64, 10, 1 , true);
                        int dame =  mob.hp / 100 * 20 ;
                         this.attachedMob(dame,arMob[0].id,false);
                    }
               //     }
                    //skill phượng hoàng
                    if (p.c.get().getPramItem(131) > 0 && Util.nextInt(1,50) == 50) {
                        for (int f = 0 ; f < mobs.size(); f++) {
                           GameCanvas.addEffect(p.conn, (byte) 1, mobs.get(f).id, (byte) 65, 10, 1, true);
                           int dame = mob.hp / 100 * 30 ;
                         this.attachedMob(dame,mobs.get(f).id,false);
                        }
                    }
                    if (mob != null && !mob.isDie) {
                        if (p.c.get().ItemBody[1] == null) {
                            p.sendAddchatYellow("Vũ khí không thích hợp");
                        } else {
                            SkillOptionTemplate data = SkillTemplate.Templates(skill.id, skill.point);
                            if (p.c.get().mp < data.manaUse) {
                                p.getMp();
                            }
                            else if (skill.coolDown <= System.currentTimeMillis() && Math.abs(p.c.get().x - mob.x) <= 150 && Math.abs(p.c.get().y - mob.y) <= 150 && p.c.get().getEffId(6) == null && p.c.get().getEffId(7) == null) {

                                p.c.setTimeKickSession();
                                skill.coolDown = System.currentTimeMillis() + (long)data.coolDown;
                                p.c.mobAtk = mob.id;

                                p.c.get().upMP(-data.manaUse);
                                if (skill.id == 42) {
                                    p.c.get().x = mob.x;
                                    p.c.get().y = mob.y;
                                    this.sendXYPlayer(p);
                                }
                                if (skill.id == 24) {
                                    if (mob.lvboss == 0 && !mob.isboss) {
                                        mob.setDonteMove(true, System.currentTimeMillis() + (long)(p.c.getPramSkill(55) * 1000));
                                        this.sendDontMoveMob(mob);
                                    }
                                }
                                else if (p.c.isSkill25Dao) {
                                    if (mob.lvboss == 0 && !mob.isboss) {
                                        mob.setDisable(true, System.currentTimeMillis() + (long)(p.c.getPramSkill(48) * 1000));
                                        this.sendDisableMob(mob);
                                    }
                                    p.c.isSkill25Dao = false;
                                }
                                else if (p.c.isSkill25Kiem) {
                                    if (mob.lvboss == 0 && !mob.isboss && Math.abs(mob.level - p.c.level) <= 7) {
                                        short itemmapid = this.getItemMapNotId();
                                        Item itemMap = ItemTemplate.itemDefault(218);
                                        ItemMap item = new ItemMap();
                                        item.item = itemMap;
                                        item.master = p.c.id;
                                        item.x = mob.x;
                                        item.y = mob.y;
                                        item.itemMapId = itemmapid;
                                        this.itemMap.add(item);
                                        Message m2 = new Message(78);
                                        m2.writer().writeByte(mob.id);
                                        m2.writer().writeShort(item.itemMapId);
                                        m2.writer().writeShort(item.item.id);
                                        m2.writer().writeShort(item.x);
                                        m2.writer().writeShort(item.y);
                                        m2.writer().flush();
                                        this.sendMessage(m2);
                                        m2.cleanup();
                                    } else {
                                        p.sendAddchatYellow("Không thể sử dụng chiêu thức này lên quái có level chênh lệch quá nhiều");
                                    }
                                    p.c.isSkill25Kiem = false;
                                }
                                else {

                                    int size = m.reader().available();
                                    byte n = 1;
                                    Mob mob2;
                                    int i;
                                    for(i = 0; i < size; ++i) {
                                        mob2 = this.getMob(m.reader().readUnsignedByte());
                                        if (!mob2.isDie && mob.id != mob2.id) {
                                            if (data.maxFight <= n) {
                                                break;
                                            }
                                            arMob[n] = mob2;
                                            ++n;
                                        }
                                    }

                                    Player pl;
                                    int j;
                                    for(j = this.players.size() - 1; j>=0; j--) {
                                        pl = this.players.get(j);
                                        if(pl != null) {
                                            Service.PlayerAttack(pl, arMob, p.c.get());
                                            if (p.c.clone != null && p.c.clone.islive && p.c.clone != null) {
                                                Service.PlayerAttack(pl, arMob, p.c.clone);
                                            }
                                        }

                                    }

                                    long xpup = 0L;

                                    byte k;
                                    for(k = 0; k < arMob.length; ++k) {

                                        xpup = this.handleAfterAttackMob(arMob[k], p.c, xpup);
                                        if (p.c.clone.islive && p.c.clone != null && !p.c.isNhanban) {
                                            this.handleAfterCloneAttackMob(arMob[k], p.c.clone);
                                        }
                                    }

                                    p.removeEffect(15);
                                    p.removeEffect(16);
                                    if (xpup > 0L) {
                                        int i2;
                                        if (p.c.level >= 90 && p.c.clone.islive && p.c.clone != null) {
                                            Skill ski = p.c.get().getSkill90();
                                            if (ski != null) {
                                                if (ski.point < 10) {
                                                    p.c.expSkillClone += xpup;
                                                    if (p.c.expSkillClone >= GameSrc.upExpSkillClone[ski.point - 1] * 1000L) {
                                                        p.c.expSkillClone = 0L;
                                                        ++ski.point;
                                                        p.loadSkill();
                                                    }
                                                }
                                            }
                                        }

                                        if (Manager.up_exp > 1) {
                                            xpup *= (long)Manager.up_exp;
                                        }

                                        if(p.c.get().level >= 10 && p.c.get().level < 70 && p.c.isHuman) {
                                            xpup *= 2;
                                        }

                                        if (this.map.cave != null) {
                                            this.map.cave.updateXP(xpup + xpup / 2L);
                                        } else {
                                            if (p.c.isNhanban) {
                                                xpup /= 4L;
                                            }
                                            p.updateExp(xpup);
                                            if (p.c.get().party != null) {
                                                Player player;
                                                for(i2 = this.players.size() - 1; i2 >= 0; i2--) {
                                                    player = this.players.get(i2);
                                                    if (player != null && player.c.id != p.c.id && player.c.party == p.c.party && Math.abs(player.c.level - p.c.level) <= 10) {
                                                        player.updateExp(xpup * 13L / 100L);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    m.cleanup();
                                }
                            }
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

    public void FightNinja(Player p, Message m) throws IOException {
        if (m.reader().available() > 0) {
            int idP = m.reader().readInt();
            Char c = this.getNinja(idP);
            if (c != null) {
                if ((!GameSrc.mapNotPK(this.map.id) || p.c.isTest || c.isTest || p.c.testCharID == c.id) && p.c.get().getEffId(14) == null && p.c.get().getEffId(6) == null && p.c.get().getEffId(7) == null) {
                    synchronized(this) {
                        if (p.c.get().ItemBody[1] != null && c.get() != null && (p.c.get().typepk == 1 && c.get().typepk == 1 || p.c.get().typepk == 3 || c.get().typepk == 3 || c.get().typepk == 4 || p.c.get().typepk == 4 || c.get().typepk == 5 || p.c.get().typepk == 5 || p.c.isTest && c.isTest && p.c.testCharID == c.id || c.isCuuSat && p.c.id == c.KillCharId || p.c.isCuuSat && p.c.KillCharId == c.id)) {
                            label425: {
                                if (p.c.get().CSkill == -1 && p.c.get().skill.size() > 0) {
                                    p.c.get().CSkill = (short)((Skill)p.c.get().skill.get(0)).id;
                                }

                                Skill skill = p.c.get().getSkill(p.c.get().CSkill);
                                if (skill != null && !c.get().isDie && c.get().getEffId(15) == null && c.get().getEffId(16) == null) {
                                    Char[] arNinja = new Char[10];
                                    arNinja[0] = c;
                                    SkillOptionTemplate temp = SkillTemplate.Templates(skill.id, skill.point);
                                    if (skill.coolDown <= System.currentTimeMillis() && Math.abs(p.c.get().x - c.get().x) <= temp.dx && Math.abs(p.c.get().y - c.get().y) <= temp.dy && p.c.get().mp >= temp.manaUse) {
                                        p.c.get().upMP(-temp.manaUse);
                                        skill.coolDown = System.currentTimeMillis() + (long)temp.coolDown;
                                        if (skill.id == 24) {
                                            c.p.setEffect(18, 0, p.c.get().getPramSkill(55) * 1000, 0);
                                            return;
                                        }

                                        if (skill.id == 42) {
                                            this.setXYPlayers(c.get().x, c.get().y, p, c.p);
                                            c.p.setEffect(18, 0, p.c.get().getPramSkill(62), 0);
                                        }

                                        byte n = 1;

                                        try {
                                            Char nj2;
                                            int idn;
                                            while(m.reader().available() > 0) {
                                                idn = m.reader().readInt();
                                                nj2 = this.getNinja(idn);
                                                if (nj2 != null && !nj2.isDie && nj2.getEffId(15) == null && c.get().id != p.c.get().id && nj2.id != p.c.get().id && Math.abs(c.get().x - nj2.x) <= temp.dx && Math.abs(c.get().y - nj2.y) <= temp.dy) {
                                                    if (temp.maxFight <= n) {
                                                        break;
                                                    }

                                                    if (nj2.typepk == 3 || p.c.get().typepk == 3 || p.c.get().typepk == 1 && nj2.typepk == 1 || nj2.typepk == 4 || p.c.get().typepk == 4 || nj2.typepk == 5 || p.c.get().typepk == 5 || p.c.isTest && nj2.isTest && p.c.testCharID == nj2.id || nj2.isCuuSat && p.c.id == nj2.KillCharId || p.c.isCuuSat && p.c.KillCharId == nj2.id) {
                                                        arNinja[n] = nj2;
                                                    }
                                                    n++;
                                                }
                                            }
                                        } catch (IOException var17) {
                                            var17.printStackTrace();
                                        }

                                        m.cleanup();
                                        m = new Message(61);
                                        m.writer().writeInt(p.c.get().id);
                                        m.writer().writeByte(skill.id);

                                        byte i;
                                        for(i = 0; i < arNinja.length; ++i) {
                                            if (arNinja[i] != null) {
                                                m.writer().writeInt(arNinja[i].id);
                                            }
                                        }

                                        m.writer().flush();
                                        this.sendMyMessage(p, m);
                                        m.cleanup();

                                        this.handleAfterAttackNinja(p, arNinja);
                                        break label425;
                                    }
                                    return;
                                }
                                return;
                            }
                        }
                    }
                    m.cleanup();
                }
            }
        }
    }

    private void checkTest(Char c) {
        if (c.isTest) {
            Char player = this.getNinja(c.testCharID);
            if (player != null) {
                if (player.hp <= 0) {
                    this.endTest(c, 0);
                } else {
                    this.endTest(c, 10);
                }
            }
        }

    }

    private void endTest(Char c, int num) {
        if (num > 0) {
            c.hp = num;
        }
        if (c.isTest) {
            try {
                int i;
                for(i = this.players.size() - 1; i >= 0; i--) {
                    if (this.players.get(i) != null && (this.players.get(i)).conn != null) {
                        Service.TestEnd((this.players.get(i)).c, c.id, c.testCharID, num);
                    }
                }
            } catch (Exception var4) {
                var4.printStackTrace();
            }

        }
    }

    private void clearCuuSat(Char c) {
        if (c.KillCharId != -9999) {
            c.KillCharId = -9999;
            c.isCuuSat = false;
            Service.ClearCuuSat(c, c.id);
            Char player = this.getNinja(c.KillCharId);
            if (player != null) {
                Service.ClearCuuSat(player, c.id);
            }
        }

        int i;
        Player player;
        for(i = this.players.size() - 1; i >= 0; i--) {
            player = this.players.get(i);
            if (player != null && player.c != null && player.conn != null) {
                if (player.c.KillCharId == c.id) {
                    player.c.KillCharId = -9999;
                }
                Service.ClearCuuSat(player.c, player.c.id);
            }
        }

    }

    public void sendDie(Char c)  {
        Message m = null;
        try {
            this.clearCuuSat(c);
            this.checkTest(c);
            if (c.get().exp > Level.getMaxExp(c.get().level)) {
                m = new Message(-11);
                m.writer().writeByte(c.get().pk);
                m.writer().writeShort(c.get().x);
                m.writer().writeShort(c.get().y);
                m.writer().writeLong(c.get().exp);
                m.writer().flush();
                c.p.conn.sendMessage(m);
                m.cleanup();
            } else {
                c.get().exp = Level.getMaxExp(c.get().level);
                m = new Message(72);
                m.writer().writeByte(c.get().pk);
                m.writer().writeShort(c.get().x);
                m.writer().writeShort(c.get().y);
                m.writer().writeLong(c.get().expdown);
                m.writer().flush();
                c.p.conn.sendMessage(m);
                m.cleanup();
            }

            m = new Message(0);
            m.writer().writeInt(c.get().id);
            m.writer().writeByte(c.get().pk);
            m.writer().writeShort(c.get().x);
            m.writer().writeShort(c.get().y);
            m.writer().flush();
            this.sendMyMessage(c.p, m);
            m.cleanup();
            m = new Message(-9);
            m.writer().writeInt(c.get().id);
            m.writer().writeByte(c.get().pk);
            m.writer().writeShort(c.get().x);
            m.writer().writeShort(c.get().y);
            m.writer().flush();
            this.sendMyMessage(c.p, m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void DieReturn(Player p) {
        Message m = null;
        try {
            if (p.c.isInDun && p.c.dunId != -1) {
                p.restCave();
            }
            this.leave(p);
            p.c.get().isDie = false;
            Map ma;
            if (this.map.cave != null) {
                ma = this.map.cave.map[0];
            } else if (this.map.dun != null) {
                ma = Manager.getMapid(p.c.mapKanata);
            } else if(this.map.bossTuanLoc != null){
                ma = this.map.bossTuanLoc.map[0];
            }
            else if (this.map.lanhDiaGiaToc != null) {
                ma = this.map.lanhDiaGiaToc.map[0];
            }
            else if (this.map.giaTocChien != null) {
                if(this.map.giaTocChien.gt1.contains(p.c)) {
                    ma = this.map.giaTocChien.map[1];
                } else {
                    ma = this.map.giaTocChien.map[2];
                }
            }
            else if (ChienTruong.chienTruong != null) {
                switch (p.c.pheCT) {
                    case 0: {
                        ma = ChienTruong.chienTruong.map[0];
                        break;
                    }
                    case 1: {
                        ma = ChienTruong.chienTruong.map[6];
                        break;
                    }
                    default: {
                        p.c.typepk = 0;
                        Service.ChangTypePkId(p.c, (byte)0);
                        ma = Manager.getMapid(p.c.mapLTD);
                        break;
                    }
                }
            }
            else {
                ma = Manager.getMapid(p.c.mapLTD);
            }

            TileMap[] var3 = ma.area;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; var5++) {
                TileMap area = var3[var5];
                if (area.numplayers < ma.template.maxplayers) {
                    area.EnterMap0(p.c);
                    p.c.get().hp = p.c.get().getMaxHP();
                    p.c.get().mp = p.c.get().getMaxMP();
                    m = new Message(-30);
                    m.writer().writeByte(-123);
                    m.writer().writeInt(p.c.xu);
                    m.writer().writeInt(p.c.yen);
                    m.writer().writeInt(p.luong);
                    m.writer().writeInt(p.c.get().getMaxHP());
                    m.writer().writeInt(p.c.get().getMaxMP());
                    m.writer().writeByte(0);
                    m.writer().flush();
                    p.conn.sendMessage(m);
                    m.cleanup();
                    m = new Message(57);
                    m.writer().flush();
                    p.conn.sendMessage(m);
                    m.cleanup();
                    return;
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

    private void attached(int dame, int nid) {
        Message m = null;
        try {
            Char n = this.getNinja(nid);
            if(n != null) {
                m = new Message(62);
                m.writer().writeInt(nid);
                m.writer().writeInt(n.hp);
                m.writer().writeInt(dame);
                m.writer().writeInt(n.mp);
                m.writer().writeInt(0);
                m.writer().flush();
                this.sendMessage(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    private void FireMobSkill25(int mobid, int idCharSkill25, int dame) {
        Mob mob = this.getMob(mobid);
        if(mob != null) {
            mob.timeFire = System.currentTimeMillis() + 30000L;
            mob.dameFire = dame;
            mob.timeDameFire = System.currentTimeMillis() + 1500L;
            mob.idCharSkill25 = idCharSkill25;
        }
    }

    private void FireMobMessage(int mobid, int type) {
        Message m = null;
        try {
            Mob mob = this.getMob(mobid);
            if(mob != null) {
                switch(type) {
                    case -1: {
                        mob.isFire = false;
                        break;
                    }
                    case 0: {
                        mob.isFire = true;
                        mob.timeFire = System.currentTimeMillis() + 2000L;
                        break;
                    }
                    case 1: {
                        mob.isFire = true;
                        mob.timeFire = System.currentTimeMillis() + 4000L;
                        break;
                    }
                    case 3: {
                        mob.isFire = true;
                        mob.timeFire = System.currentTimeMillis() + 430000L;

                        break;
                    }
                }
                m = new Message(89);
                m.writer().writeByte(mobid);
                m.writer().writeBoolean(mob.isFire);
                m.writer().flush();
                this.sendMessage(m);
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    private void FireHideMobMessage(int mobid) {
        Message m = null;
        try {
            Mob mob = this.getMob(mobid);
            if(mob != null) {
                m = new Message(-30);
                m.writer().writeByte(-73);
                m.writer().writeByte(mobid);
                m.writer().flush();
                this.sendMessage(m);
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    private void IceMobMessage(int mobid, int type) {
        Message m = null;
        try {
            Mob mob = this.getMob(mobid);
            if(mob != null) {
                switch(type) {
                    case -1:
                        mob.isIce = false;
                        break;
                    case 0:
                        mob.isIce = true;
                        mob.timeIce = System.currentTimeMillis() + 1500L;
                        break;
                    case 1:
                        mob.isIce = true;
                        mob.timeIce = System.currentTimeMillis() + 4000L;
                        break;
                    case 2:
                        mob.isIce = true;
                        mob.timeIce = System.currentTimeMillis() + 2000L;
                }
                m = new Message(90);
                m.writer().writeByte(mobid);
                m.writer().writeBoolean(mob.isIce);
                m.writer().flush();
                this.sendMessage(m);
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    private void WindMobMessage(int mobid, int type) {
        Message m = null;
        try {
            Mob mob = this.getMob(mobid);
            if(mob != null) {
                switch(type) {
                    case -1:
                        mob.isWind = false;
                        break;
                    case 0:
                        mob.isWind = true;
                        mob.timeWind = System.currentTimeMillis() + 1000L;
                        break;
                    case 1:
                        mob.isWind = true;
                        mob.timeWind = System.currentTimeMillis() + 2000L;
                }
                m = new Message(91);
                m.writer().writeByte(mobid);
                m.writer().writeBoolean(mob.isWind);
                m.writer().flush();
                this.sendMessage(m);
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void loadMobAttached(int mobid) {
        synchronized(this) {
            try {
                Mob mob = this.getMob(mobid);
                if (mob != null && !mob.isIce && !mob.isWind && !mob.isDisable()) {
                    long tFight = System.currentTimeMillis() + 1200L;
                    if (mob.isboss) {
                        tFight = System.currentTimeMillis() + 500L;
                    }
                    mob.timeFight = tFight;

                    int dame = 0;
                    if (mob.isboss) {
                        dame = Util.nextInt(7500,8500);
                        if(mob.level > 60) {
                            dame = Util.nextInt(8000,9500);
                        }
                        else if(mob.level > 70) {
                            dame = Util.nextInt(8000,10000);
                        } else if(mob.level > 80) {
                            dame = Util.nextInt(8500,11000);
                        }
                        if(this.map.getXHD() != -1) {
                            dame = Util.nextInt(6000,8000);
                        }
                        else if(this.map.mapBossTuanLoc()) {
                            dame = Util.nextInt(5000,6500);
                        }
                    } else if (this.map.LangCo()) {
                        switch(mob.lvboss) {
                            case 0:
                                dame = 3000;
                                break;
                            case 1:
                                dame = 4500;
                                break;
                            case 2:
                                dame = 6500;
                        }
                    } else {
                        dame = mob.level * mob.level / 5;
                        if (this.map.cave != null && this.map.cave.finsh > 0 && this.map.getXHD() == 6) {
                            dame = dame * (10 * this.map.cave.finsh + 100) / 100;
                        }

                        if (mob.level < 60) {
                            switch(mob.lvboss) {
                                case 1:
                                    dame *= 2;
                                    break;
                                case 2:
                                    dame *= 4;
                                    break;
                                case 3:
                                    dame *= 2;
                            }
                        }
                        else {
                            switch(mob.lvboss) {
                                case 1:
                                    dame *= 3;
                                    break;
                                case 2:
                                    dame *= 5;

                                    break;
                                case 3:
                                    dame *= 3;
                            }
                        }
                    }
                    BuNhin buNhin;
                    Player player;
                    short i;
                    int indexBuNhin;
                    for(i = 0; i < this.players.size(); i++) {
                        if(this.players.get(i) != null && this.players.get(i).c != null) {
                            player = this.players.get(i);
                            if(((mob.templates.id == 97 || mob.templates.id == 98) && player.c.pheCT == 0) || ((mob.templates.id == 96 || mob.templates.id == 99) && player.c.pheCT == 1)) {
                                continue;
                            }
                            if (player.c.buNhin != null && this.buNhins.contains(player.c.buNhin) && Math.abs(player.c.buNhin.x - mob.x) <= 150 && Math.abs(player.c.buNhin.y - mob.y) <= 60) {
                                indexBuNhin = this.buNhins.indexOf(player.c.buNhin);
                                buNhin = this.buNhins.get(indexBuNhin);
                                if(buNhin != null) {
                                    if(buNhin.miss < Util.nextInt(10000)) {
                                        buNhin.setHp(-dame);
                                    }
                                    Message m = null;
                                    try {
                                        m = new Message(76);
                                        m.writer().writeByte(mob.id);
                                        m.writer().writeShort((short)indexBuNhin);
                                        m.writer().writeShort(-1);
                                        m.writer().writeByte(-1);
                                        m.writer().writeByte(-1);
                                        m.writer().flush();
                                        this.sendMessage(m);
                                    } catch (Exception var17) {
                                        var17.printStackTrace();
                                    } finally {
                                        if(m != null) {
                                            m.cleanup();
                                        }
                                    }
                                }
                                if(!mob.isboss && mob.lvboss == 0) {
                                    break;
                                }
                            }
                            else if (!player.c.get().isDie && player.c.get().getEffId(15) == null && player.c.get().getEffId(16) == null) {
                                short dx = 80;
                                short dy = 2;
                                if (mob.templates.type > 3) {
                                    dy = 80;
                                }
                                if (mob.isboss) {
                                    dx = 110;
                                }
                                if (mob.isFight(player.conn.id)) {
                                    dx = 200;
                                    dy = 160;
                                }
                                if (Math.abs(player.c.get().x - mob.x) < dx && Math.abs(player.c.get().y - mob.y) < dy) {
                                    if (mob.isboss) {
                                        if(player.c.get().percentIce() >= Util.nextInt(1, 150)) {
                                            this.IceMobMessage(mob.id, 2);
                                        } else {
                                            switch(mob.sys) {
                                                case 1:
                                                    dame -= player.c.get().ResFire();
                                                    break;
                                                case 2:
                                                    dame -= player.c.get().ResIce();
                                                    break;
                                                case 3:
                                                    dame -= player.c.get().ResWind();
                                                    break;
                                            }
                                            dame -= player.c.get().dameDown();
                                            dame = Util.nextInt(dame * 90 / 100, dame);
                                            if (dame <= 0) {
                                                dame = 1;
                                            }
                                            int miss = player.c.get().Miss();
                                            if (miss > Util.nextInt(8000)) {
                                                dame = 0;
                                            }
                                            int mpdown = 0;
                                            if (player.c.get().hp * 100 / player.c.get().getMaxHP() > 10) {
                                                Effect eff = player.c.get().getEffId(10);
                                                if (eff != null) {
                                                    int mpold = player.c.get().mp;
                                                    player.c.get().upMP(-(dame * eff.param / 100));
                                                    dame -= mpdown = mpold - player.c.get().mp;
                                                }
                                                eff = player.c.get().getEffId(5);
                                                if (eff != null) {
                                                    dame *= 2;
                                                }
                                            }
                                            if(this.map.lanhDiaGiaToc != null && this.map.mapLDGT()) {
                                                if(mob.templates.id != 82) {
                                                    switch (this.map.id) {
                                                        case 84: {
                                                            if(Util.nextInt(10) < 3) {
                                                                player.setEffect(5, 0, 1000, 0);
                                                            }
                                                            break;
                                                        }
                                                        case 85: {
                                                            if(Util.nextInt(10) < 3) {
                                                                player.setEffect(7, 0, 1000, 0);
                                                            }
                                                            break;
                                                        }
                                                        case 86: {
                                                            if(Util.nextInt(10) < 3) {
                                                                player.setEffect(6, 0, 1000, 0);
                                                            }
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    int per1 = Util.nextInt(1,3);
                                                    switch (per1) {
                                                        case 1: {
                                                            if(Util.nextInt(10) < 4) {
                                                                player.setEffect(5, 0, 2000, 0);
                                                            }
                                                            break;
                                                        }
                                                        case 2: {
                                                            if(Util.nextInt(10) < 4) {
                                                                player.setEffect(7, 0, 2000, 0);
                                                            }
                                                            break;
                                                        }
                                                        case 3: {
                                                            if(Util.nextInt(10) < 4) {
                                                                player.setEffect(6, 0, 2000, 0);
                                                            }
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            player.c.get().upHP(-dame);
                                            this.MobAtkMessage(mob.id, player.c, dame, mpdown, (short)-1, (byte)-1, (byte)-1);
                                            if(!mob.isboss && mob.lvboss == 0) {
                                                break;
                                            }
                                        }
                                    } else {
                                        if(player.c.get().percentIce() >= Util.nextInt(1, 100)) {
                                            this.IceMobMessage(mob.id, 2);
                                        } else {
                                            switch(mob.sys) {
                                                case 1:
                                                    dame -= player.c.get().ResFire();
                                                    break;
                                                case 2:
                                                    dame -= player.c.get().ResIce();
                                                    break;
                                                case 3:
                                                    dame -= player.c.get().ResWind();
                                                    break;
                                            }
                                            dame -= player.c.get().dameDown();
                                            dame = Util.nextInt(dame * 90 / 100, dame);
                                            if (dame <= 0) {
                                                dame = 1;
                                            }
                                            int miss = player.c.get().Miss();
                                            if (miss > Util.nextInt(8000)) {
                                                dame = 0;
                                            }
                                            int mpdown = 0;
                                            if (player.c.get().hp * 100 / player.c.get().getMaxHP() > 10) {
                                                Effect eff = player.c.get().getEffId(10);
                                                if (eff != null) {
                                                    int mpold = player.c.get().mp;
                                                    player.c.get().upMP(-(dame * eff.param / 100));
                                                    dame -= mpdown = mpold - player.c.get().mp;
                                                }
                                                eff = player.c.get().getEffId(5);
                                                if (eff != null) {
                                                    dame *= 2;
                                                }
                                            }
                                            if(this.map.lanhDiaGiaToc != null && this.map.mapLDGT()) {
                                                if(mob.templates.id != 82) {
                                                    switch (this.map.id) {
                                                        case 84: {
                                                            if(Util.nextInt(10) < 3) {
                                                                player.setEffect(5, 0, 1000, 0);
                                                            }
                                                            break;
                                                        }
                                                        case 85: {
                                                            if(Util.nextInt(10) < 3) {
                                                                player.setEffect(7, 0, 1000, 0);
                                                            }
                                                            break;
                                                        }
                                                        case 86: {
                                                            if(Util.nextInt(10) < 3) {
                                                                player.setEffect(6, 0, 1000, 0);
                                                            }
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    int per1 = Util.nextInt(1,3);
                                                    switch (per1) {
                                                        case 1: {
                                                            if(Util.nextInt(10) < 4) {
                                                                player.setEffect(5, 0, 2000, 0);
                                                            }
                                                            break;
                                                        }
                                                        case 2: {
                                                            if(Util.nextInt(10) < 4) {
                                                                player.setEffect(7, 0, 2000, 0);
                                                            }
                                                            break;
                                                        }
                                                        case 3: {
                                                            if(Util.nextInt(10) < 4) {
                                                                player.setEffect(6, 0, 2000, 0);
                                                            }
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            player.c.get().upHP(-dame);
                                            this.MobAtkMessage(mob.id, player.c, dame, mpdown, (short)-1, (byte)-1, (byte)-1);
                                            if(!mob.isboss && mob.lvboss == 0) {
                                                break;
                                            }
                                        }

                                    }
                                }

                            }
                        }
                        else {
                            continue;
                        }
                    }
                }
            } catch (Exception var18) {
                var18.printStackTrace();
                return;
            }

        }
    }

    private void MobAtkMessage(int mobid, Char n, int dame, int mpdown, short idskill_atk, byte typeatk, byte typetool) {
        Message m = null;
        try {
            m = new Message(-3);
            m.writer().writeByte(mobid);
            m.writer().writeInt(dame);
            m.writer().writeInt(mpdown);
            m.writer().writeShort(idskill_atk);
            m.writer().writeByte(typeatk);
            m.writer().writeByte(typetool);
            m.writer().flush();
            n.p.conn.sendMessage(m);
            m.cleanup();
            m = new Message(-2);
            m.writer().writeByte(mobid);
            m.writer().writeInt(n.id);
            m.writer().writeInt(n.hp);
            m.writer().writeInt(mpdown);
            m.writer().writeShort(idskill_atk);
            m.writer().writeByte(typeatk);
            m.writer().writeByte(typetool);
            m.writer().flush();
            this.sendMyMessage(n.p, m);
            if (n.isDie && !this.map.LangCo()) {
                this.sendDie(n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    private void loadMobMeAtk(Char n) {
        if(n != null && n.mobMe != null) {
            n.mobMe.timeFight = System.currentTimeMillis() + 3000L;
            try {
                if (n.mobAtk != -1 && n.mobMe.templates.id >= 211 && n.mobMe.templates.id <= 217) {
                    Mob mob = this.getMob(n.mobAtk);
                    if (mob != null && !mob.isDie) {
                        int dame = n.dameMax() * 20 / 100;
                        this.MobMeAtkMessage(n, mob.id, dame, (short)40, (byte)1, (byte)1, (byte)0);
                        if(!mob.isDie && mob.hp > 0) {
                            mob.updateHP(-dame, n.id, false);
                        }
                        this.attachedMob(dame, mob.id, false);
                    }
                }
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }
    }

    private void MobMeAtkMessage(Char n, int idatk, int dame, short idskill_atk, byte typeatk, byte typetool, byte type) {
        Message m = null;
        try {
            m = new Message(87);
            m.writer().writeInt(n.id);
            m.writer().writeByte(idatk);
            m.writer().writeShort(idskill_atk);
            m.writer().writeByte(typeatk);
            m.writer().writeByte(typetool);
            m.writer().writeByte(type);
            if (type == 1) {
                m.writer().writeInt(idatk);
            }
            m.writer().flush();
            n.p.conn.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void updateMob() {
        int i;
        Mob mob;
        for(i = this.mobs.size() - 1; i >= 0; i--) {
            mob = this.mobs.get(i);
            if(mob != null) {
                if (mob.timeDisable > 0L && System.currentTimeMillis() >= mob.timeDisable) {
                    mob.setDisable(false, -1L);
                    this.sendDisableMob(mob);
                }

                if (mob.timeDontMove > 0L && System.currentTimeMillis() >= mob.timeDontMove) {
                    mob.setDonteMove(false, -1L);
                    this.sendDontMoveMob(mob);
                }

                if (mob.timeRefresh > 0L && System.currentTimeMillis() >= mob.timeRefresh && mob.isRefresh) {
                    this.refreshMob(mob.id);
                }

                if (mob.isFire && System.currentTimeMillis() >= mob.timeFire) {
                    this.FireMobMessage(mob.id, -1);
                }

                if (mob.isIce && System.currentTimeMillis() >= mob.timeIce) {
                    this.IceMobMessage(mob.id, -1);
                }

                if (mob.isWind && System.currentTimeMillis() >= mob.timeWind) {
                    this.WindMobMessage(mob.id, -1);
                }

                if (mob.idCharSkill25 != -1 && System.currentTimeMillis() > mob.timeDameFire) {
                    if(System.currentTimeMillis() < mob.timeFire) {
                        mob.timeDameFire = System.currentTimeMillis() + 1300L;
                        int oldhp = mob.hp;
                        if(!mob.isDie && mob.hp > 0) {
                            mob.updateHP(-mob.dameFire, mob.idCharSkill25, false);
                        }
                        this.attachedMob(oldhp - mob.hp, mob.id, false);
                        this.FireHideMobMessage(mob.id);
                    } else {
                        mob.setSkill25();
                    }
                }

                if (!mob.isDie && mob.status != 0 && mob.level != 1 && System.currentTimeMillis() >= mob.timeFight) {
                    this.loadMobAttached(mob.id);
                }
            }
        }
    }

    public void updatePlayer() {
        int i;
        Player p;
        for(i = this.players.size() - 1; i >= 0; i--) {
            p = this.players.get(i);
            if (p != null && p.c != null) {
                p.c.timeKickSession -= 100L;
                if (p.c.timeKickSession <= System.currentTimeMillis()) {
                }
                else {

                    if(this.map.LangCo() && p.c.pk > 0) {
                        p.c.tileMap.leave(p);
                        Map ma = Manager.getMapid(p.c.mapLTD);
                        byte k;
                        for (k = 0; k < ma.area.length; k++) {
                            if (ma.area[k].numplayers < ma.template.maxplayers) {
                                ma.area[k].EnterMap0(p.c);
                                return;
                            }
                        }
                    }

                    short s;
                    PartyPlease var20;
                    if (p.c.aPartyInvate != null && p.c.aPartyInvate.size() > 0) {
                        synchronized(p.c.aPartyInvate) {
                            if (p.c.party != null) {
                                p.c.aPartyInvate.clear();
                            } else {
                                for(s = 0; s < p.c.aPartyInvate.size(); s++) {
                                    var20 = p.c.aPartyInvate.get(s);
                                    if (var20 != null) {
                                        var20.timeLive -= 500;
                                        if ((p.c.aPartyInvate.get(s)).timeLive <= 0) {
                                            p.c.aPartyInvate.remove(s);
                                            s--;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (p.c.aPartyInvite != null && p.c.aPartyInvite.size() > 0) {
                        synchronized(p.c.aPartyInvite) {
                            if (p.c.party == null) {
                                p.c.aPartyInvite.clear();
                            } else {
                                for(s = 0; s < p.c.aPartyInvite.size(); s++) {
                                    if (p.c.aPartyInvite.get(s) != null) {
                                        var20 = p.c.aPartyInvite.get(s);
                                        var20.timeLive -= 500;
                                        if ((p.c.aPartyInvite.get(s)).timeLive <= 0) {
                                            p.c.aPartyInvite.remove(s);
                                            s--;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    int k;
                    Effect effect;
                    for(k = p.c.get().veff.size() - 1; k >= 0; k--) {
                        effect = p.c.get().veff.get(k);
                        if (System.currentTimeMillis() >= effect.timeRemove) {
                            p.removeEffect(effect.template.id);
                            k--;
                        } else if (effect.template.type != 0 && effect.template.type != 12) {
                            if (effect.template.type != 4 && effect.template.type != 17) {
                                if (effect.template.type == 13) {
                                    p.c.get().upHP(-(p.c.get().getMaxHP() * 3 / 100));
                                    if (p.c.get().isDie) {
                                        p.c.get().upDie();
                                    }
                                }
                            } else {
                                p.c.get().upHP(effect.param);
                            }
                        } else {
                            p.c.get().upHP(effect.param);
                            p.c.get().upMP(effect.param);
                        }
                    }

                    if ((p.c.eff5buffHP() > 0 || p.c.get().eff5buffMP() > 0) && p.c.eff5buff <= System.currentTimeMillis()) {
                        p.c.eff5buff = System.currentTimeMillis() + 5000L;
                        p.c.get().upHP(p.c.get().eff5buffHP());
                        p.c.get().upMP(p.c.get().eff5buffMP());
                    }

                    byte l;
                    if (p.c.get().fullTL() >= 9 && System.currentTimeMillis() > p.c.delayEffect) {
                        p.c.delayEffect = System.currentTimeMillis() + 5000L;
                        l = 0;
                        switch(GameSrc.SysClass(p.c.nclass)) {
                            case 1:
                                l = 9;
                                break;
                            case 2:
                                l = 3;
                                break;
                            case 3:
                                l = 6;
                         }
                        if (p.c.fullTL() >= 9) {
                            l += 1;
                        }
                        if (p.c.fullTL() >= 7) {
                            l += 0;
                        }
                        for(k = this.players.size() - 1; k>=0; k--) {
                            GameCanvas.addEffect((this.players.get(k)).conn, (byte)0, p.c.get().id, (short)l, 1, 1, false);
                        }
                    }

                    if (p.c.get().getNgocEff() != -1 && System.currentTimeMillis() > p.c.delayEffect) {
                        p.c.delayEffect = System.currentTimeMillis() + 5000L;
                        for(k = this.players.size() - 1; k>=0; k--) {
                            GameCanvas.addEffect((this.players.get(k)).conn, (byte)0, p.c.get().id, (short)p.c.get().ngocEff, 1, 1, false);
                        }
                    }

                    if (p.c.get().ItemBody[18] != null && System.currentTimeMillis() > p.c.delayEffect) {
                        p.c.delayEffect = System.currentTimeMillis() + 3000L;
                        for(k = this.players.size() - 1; k>=0; k--) {
                            GameCanvas.addEffect((this.players.get(k)).conn, (byte)0, p.c.get().id, p.c.gender == 1 ? (short)34 : (short)33, 1, 1, false);
                        }
                    }
                //    if (p.c.id == 1 && System.currentTimeMillis() > p.c.delayEffect) {
              //         p.c.delayEffect = System.currentTimeMillis() + 3000L;
              //           for(k = this.players.size() - 1; k>=0; k--) {
                //            GameCanvas.addEffect((this.players.get(k)).conn, (byte)0, p.c.get().id, (short)64, 0, 0, false);
               //             GameCanvas.addEffect((this.players.get(k)).conn, (byte)0, p.c.get().id, (short)65, 0, 0, false);
              //         }   
                //        }
                   for (byte j = 0; j < p.c.get().ItemBody.length; j++) {
                            Item item = p.c.get().ItemBody[j];
                            if (item != null && item.id == 825) {
                             for(k = this.players.size() - 1; k>=0; k--) {;
                            GameCanvas.addEffect((this.players.get(k)).conn, (byte)0, p.c.get().id, (short)61, 0, 0, false);
                        }   
                        }
                   }
                   for (byte j = 0; j < p.c.get().ItemBody.length; j++) {
                            Item item = p.c.get().ItemBody[j];
                            if (item != null && item.id == 826) {
                             for(k = this.players.size() - 1; k>=0; k--) {;
                            GameCanvas.addEffect((this.players.get(k)).conn, (byte)0, p.c.get().id, (short)62, 0, 0, false);
                        }   
                        }
                    }
                  
                    if(p.c.isGiftTDB == 0) {
                        switch (p.c.rankTDB) {
                            case 1: {
                                for(k = this.players.size() - 1; k>=0; k--) {
                                    GameCanvas.addEffect((this.players.get(k)).conn, (byte)0, p.c.get().id, (short)35, 1, 1, false);
                                    GameCanvas.addEffect((this.players.get(k)).conn, (byte)0, p.c.get().id, (short)22, 1, 1, false);
                                    GameCanvas.addEffect((this.players.get(k)).conn, (byte)0, p.c.get().id, (short)23, 1, 1, false);
                                    GameCanvas.addEffect((this.players.get(k)).conn, (byte)0, p.c.get().id, (short)24, 1, 1, false);
                                }
                                break;
                            }
                            case 2: {
                                for(k = this.players.size() - 1; k>=0; k--) {
                                    GameCanvas.addEffect((this.players.get(k)).conn, (byte)0, p.c.get().id, (short)22, 1, 1, false);
                                    GameCanvas.addEffect((this.players.get(k)).conn, (byte)0, p.c.get().id, (short)23, 1, 1, false);
                                    GameCanvas.addEffect((this.players.get(k)).conn, (byte)0, p.c.get().id, (short)24, 1, 1, false);
                                }
                                break;
                            }
                            case 3: {
                                for(k = this.players.size() - 1; k>=0; k--) {
                                    GameCanvas.addEffect((this.players.get(k)).conn, (byte)0, p.c.get().id, (short)22, 1, 1, false);
                                    GameCanvas.addEffect((this.players.get(k)).conn, (byte)0, p.c.get().id, (short)23, 1, 1, false);
                                }
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                    }

                    if (p.c.get().isGoiRong) {
                        for(k = 0; k < this.players.size(); k++) {
                            GameCanvas.addEffect((this.players.get(k)).conn, (byte)0, p.c.get().id, (short)21, 1, 1, false);
                        }
                        p.c.get().timeEffGoiRong -= 500L;
                        if (p.c.get().timeEffGoiRong <= 0L) {
                            p.c.get().isGoiRong = false;
                        }
                    }

                    if (p.c.buNhin != null && p.c.buNhin.timeRemove <= System.currentTimeMillis()) {
                        p.c.buNhin = null;
                    }

                    if (p.c.get().mobMe != null && p.c.get().mobMe.timeFight <= System.currentTimeMillis()) {
                        this.loadMobMeAtk(p.c);
                    }

                    for(l = 0; l < p.c.ItemBag.length; l++) {
                        if (p.c.ItemBag[l] != null && p.c.ItemBag[l].isExpires && System.currentTimeMillis() >= p.c.ItemBag[l].expires) {
                            p.c.removeItemBag(l, p.c.ItemBag[l].quantity);
                        }
                    }

                    for(l = 0; l < p.c.get().ItemBody.length; l++) {
                        if (p.c.get().ItemBody[l] != null && p.c.get().ItemBody[l].isExpires && System.currentTimeMillis() >= p.c.get().ItemBody[l].expires) {
                            p.c.removeItemBody(l);
                        }
                    }

                    if(p.c.isHuman && p.c.clone != null) {
                        for(l = 0; l < p.c.clone.ItemBody.length; l++) {
                            if (p.c.clone.ItemBody[l] != null && p.c.clone.ItemBody[l].isExpires && System.currentTimeMillis() >= p.c.clone.ItemBody[l].expires) {
                                p.c.clone.removeItemBody(l);
                            }
                        }
                    }

                    for(l = 0; l < p.c.ItemBox.length; l++) {
                        if (p.c.ItemBox[l] != null && p.c.ItemBox[l].isExpires && System.currentTimeMillis() >= p.c.ItemBox[l].expires) {
                            p.c.removeItemBox(l);
                        }
                    }

                    if (this.map.LangCo() && !p.c.isTest && (p.c.isDie || p.c.expdown > 0L)) {
                        this.DieReturn(p);
                    }

                    if (p.c.get().isDie && p.c.isTest) {
                        p.liveFromDead();
                        Char player = this.getNinja(p.c.testCharID);
                        if (player != null) {
                            player.testCharID = -9999;
                            player.isTest = false;
                        }

                        p.c.testCharID = -9999;
                        p.c.isTest = false;
                    }

                    if (System.currentTimeMillis() > p.c.deleyRequestClan) {
                        p.c.requestclan = -1;
                    }

                    if (p.c.clone != null && p.c.clone.islive && p.c.get().isHuman){
                        if (Math.abs(p.c.x - p.c.clone.x) > 80 || Math.abs(p.c.y - p.c.clone.y) > 30) {
                            p.c.clone.move((short)Util.nextInt(p.c.x - 35, p.c.x + 35), p.c.y);
                        }
                        if(p.c.clone.nclass == 6) {
                            for(short idSk : p.c.clone.getWinBuffSkills()) {
                                UseSkill.useSkillCloneBuff(p.c.clone, idSk);
                            }
                        }
                    }

                    if (p.c.clone != null && (!p.c.clone.islive || System.currentTimeMillis() > p.c.timeRemoveClone)) {
                        p.c.clone.off();
                    }

                    if (p.c.get().isDie) {
                        p.exitNhanBan(false);
                    }
                }
            }
        }
    }

    public void updateBuNhin() {
        int i;
        BuNhin buNhin;
        Char _char;
        for(i = this.buNhins.size() - 1; i >= 0; i--) {
            buNhin = this.buNhins.get(i);
            if (buNhin != null) {
                if ((buNhin.timeRemove > 0L && System.currentTimeMillis() >= buNhin.timeRemove) || buNhin.hp <= 0) {
                    _char = Client.gI().getNinja(buNhin.idChar);
                    if (_char != null) {
                        _char.buNhin = null;
                    }
                    this.removeBuNhin((short)i);
                }
            }
        }
    }

    public void updateItemMap() {
        int i;
        ItemMap itemMap;
        for(i = this.itemMap.size() - 1; i>= 0; i--) {
            itemMap = this.itemMap.get(i);
            if(itemMap != null) {
                if (System.currentTimeMillis() >= itemMap.removedelay) {
                    this.removeItemMapMessage(itemMap.itemMapId);
                    this.itemMap.remove(i);
                    i--;
                } else if (itemMap.removedelay - System.currentTimeMillis() < 5000L && itemMap.master != -1 && itemMap.item.id != 218) {
                    itemMap.master = -1;
                }
            }
        }
    }

    public void updateMap() {
        if (this.map.cave != null) {
            if (this.map.cave != null && System.currentTimeMillis() > this.map.cave.time) {
                this.map.cave.rest();
            }

            if (this.map.cave != null && this.map.cave.level == this.map.cave.map.length) {
                this.map.cave.finish();
            }
        } else if (this.map.dun != null) {
            if (this.map.dun != null && this.map.dun.isStart && System.currentTimeMillis() > this.map.dun.time) {
                this.map.dun.finish();
            }

            if (this.map.dun != null && System.currentTimeMillis() > this.map.dun.time) {
                this.map.dun.rest();
            }
            if(this.map.dun != null && (this.map.dun.team2.size() < 1 || this.map.dun.team1.size() < 1) && !this.map.dun.isMap133) {
                this.map.dun.check2();
            }

            if (this.map.dun != null && this.map.dun.isStart && !this.map.dun.rest) {
                this.map.dun.check2();
            }
        } else if (this.map.bossTuanLoc != null) {
            if(this.map.bossTuanLoc != null && (System.currentTimeMillis() > this.map.bossTuanLoc.time || this.map.bossTuanLoc.ninjas.size() < 1)) {
                this.map.bossTuanLoc.rest();
            }
            if(this.map.bossTuanLoc != null && this.mobs.size() < 1 && !this.map.bossTuanLoc.finish) {
                this.map.bossTuanLoc.finish();
            }

        } else if (this.map.lanhDiaGiaToc != null) {
            if (this.map.lanhDiaGiaToc != null && System.currentTimeMillis() > this.map.lanhDiaGiaToc.time) {
                this.map.lanhDiaGiaToc.rest();
            }
        } else if(this.map.giaTocChien != null) {
            if (this.map.giaTocChien != null && System.currentTimeMillis() > this.map.giaTocChien.time && !this.map.giaTocChien.isDatCuoc && !this.map.giaTocChien.start && !this.map.giaTocChien.rest) {
                this.map.giaTocChien.start();
            }
            if (this.map.giaTocChien != null && System.currentTimeMillis() > this.map.giaTocChien.time && !this.map.giaTocChien.rest) {
                if((this.map.giaTocChien.isDatCuoc && !this.map.giaTocChien.start) || (!this.map.giaTocChien.isDatCuoc && this.map.giaTocChien.start)) {
                    this.map.giaTocChien.rest();
                }
            }
        }
    }

    public void update() {
        synchronized(this) {
            try {
                this.updateMob();
                this.updatePlayer();
                this.updateBuNhin();
                this.updateItemMap();
                this.updateMap();
            } catch (Exception var14) {
                var14.printStackTrace();
            }

        }
    }

    protected Party getParty(int partyId) {
        try {
            for(short i = 0; i < this.numParty; i++) {
                if (this.aParty.get(i) != null && (this.aParty.get(i)).partyId == partyId) {
                    return this.aParty.get(i);
                }
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return null;
    }

    public void addParty(Party party) {
        synchronized(this.LOCK) {
            if (!this.aParty.contains(party)) {
                this.numParty++;
                this.aParty.add(party);
            }
        }
    }

    public void removeParty(int partyId) {
        try {
            synchronized(this.LOCK) {
                short i;
                for(i = 0; i < this.numParty; i++) {
                    if (this.aParty.get(i) != null && (this.aParty.get(i)).partyId == partyId) {
                        --this.numParty;
                        this.aParty.remove(i);
                        return;
                    }
                }
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public int getNumPlayerParty(int partyId) {
        synchronized(this.LOCK) {
            int num = 0;
            try {
                short i;
                Player pl;
                for(i = 0; i < this.numplayers; i++) {
                    pl = this.players.get(i);
                    if (pl != null && pl.c != null && pl.c.party != null && pl.c.party.partyId == partyId) {
                        num++;
                    }
                }
            } catch (Exception var6) {
                var6.printStackTrace();
            }
            return num;
        }
    }

    public void sendDisableMob(Mob mob) {
        Message m2 = null;
        try {
            if (mob != null) {
                m2 = new Message(85);
                m2.writer().writeByte(mob.id);
                m2.writer().writeBoolean(mob.isDisable());
                m2.writer().flush();
                this.sendMessage(m2);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        } finally {
            if(m2 != null) {
                m2.cleanup();
            }
        }

    }

    public void sendDontMoveMob(Mob mob) {
        Message m2 = null;
        try {
            if(mob != null) {
                m2 = new Message(86);
                m2.writer().writeByte(mob.id);
                m2.writer().writeBoolean(mob.isDonteMove());
                m2.writer().flush();
                this.sendMessage(m2);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        } finally {
            if(m2 != null) {
                m2.cleanup();
            }
        }

    }
    

    public void pickItem(Player p, Message m) {
        try {
            synchronized (this.itemMap) {
                short itemmapid = m.reader().readShort();
                m.cleanup();
                short i;
                ItemMap itemmap;
                Item item;
                ItemTemplate data;
                for (i = 0; i < this.itemMap.size(); i++) {
                    itemmap = this.itemMap.get(i);
                    if (itemmap != null) {
                        if (this.itemMap.get(i).itemMapId == itemmapid) {
                            item = itemmap.item;
                            data = ItemTemplate.ItemTemplateId(item.id);
                            if (itemmap.master != -1 && itemmap.master != p.c.id) {
                                p.sendAddchatYellow("Vật phẩm của người khác.");
                                return;
                            }
                            if (Math.abs(itemmap.x - p.c.get().x) > 50 || Math.abs(itemmap.y - p.c.get().y) > 30) {
                                p.sendAddchatYellow("Khoảng cách quá xa.");
                                return;
                            }
                            if (data.type == 21 || data.type == 19 || p.c.getBagNull() > 0 || (p.c.getIndexBagid(item.id, item.isLock) != -1 && data.isUpToUp)) {
                                if(this.itemMap.contains(itemmap)) {
                                    this.itemMap.remove(itemmap);
                                }
                                m = new Message(-13);
                                m.writer().writeShort(itemmap.itemMapId);
                                m.writer().writeInt(p.c.get().id);
                                m.writer().flush();
                                this.sendMyMessage(p, m);
                                m.cleanup();
                                m = new Message(-14);
                                m.writer().writeShort(itemmap.itemMapId);
                                if (ItemTemplate.ItemTemplateId(item.id).type == 19) {
                                    p.c.upyen(1);
                                    m.writer().writeShort(1);
                                    int yenup = 0;
                                    switch (itemmap.checkMob) {
                                        case 0: {
                                            yenup = 500000;
                                            break;
                                        }
                                        case 1: {
                                            yenup = 1000000;
                                            break;
                                        }
                                        case 2: {
                                            yenup = 3000000;
                                            break;
                                        }
                                        case 4: {
                                            yenup = 10000000;
                                            break;
                                        }
                                    }
                                    if (p.c.get().ItemBody[11] != null && p.c.get().ItemBody[11].id == 774) {
                                        yenup += yenup * 50 / 100;
                                    }
                                    p.c.upyenMessage(yenup);
                                    p.sendAddchatYellow("Bạn nhận được " + Util.getFormatNumber(yenup) + " yên");
                                } else if (ItemTemplate.ItemTemplateId(item.id).type == 21) {
                                    p.c.get().upHP(p.c.getPramSkill(50));
                                }
                                m.writer().flush();
                                p.conn.sendMessage(m);
                                m.cleanup();
                                if (ItemTemplate.ItemTemplateId(item.id).type != 19 && ItemTemplate.ItemTemplateId(item.id).type != 21) {
                                    p.c.addItemBag(true, itemmap.item);
                                    if (item.id == 260 && this.map.lanhDiaGiaToc != null) {
                                        for (int ii = 0; ii < this.map.lanhDiaGiaToc.ninjas.size(); ii++) {
                                            if (this.map.lanhDiaGiaToc.ninjas.get(ii) != null && this.map.lanhDiaGiaToc.ninjas.get(ii).id != p.c.id) {
                                                this.map.lanhDiaGiaToc.ninjas.get(ii).p.sendAddchatYellow(p.c.name + " đã nhặt được chìa Khoá lãnh tại gia tộc.");
                                            }
                                        }
                                    }
                                    break;
                                }
                                break;
                            }
                            else {
                                p.conn.sendMessageLog("Hành trang không đủ chỗ trống.");
                                return;
                            }
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

    public void close() {
    }

}
