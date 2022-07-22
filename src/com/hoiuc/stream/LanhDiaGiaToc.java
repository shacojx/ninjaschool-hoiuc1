package com.hoiuc.stream;

import com.hoiuc.assembly.*;
import com.hoiuc.io.Util;
import com.hoiuc.server.Manager;
import com.hoiuc.template.ItemTemplate;

import java.util.ArrayList;
import java.util.HashMap;

public class LanhDiaGiaToc {
    private static int idbase;
    public static HashMap<Integer, LanhDiaGiaToc> ldgts = new HashMap();

    public int ldgtID;
    public Map[] map;
    public long time;
    public boolean finish;
    public ArrayList<Char> ninjas;
    public boolean rest;
    public boolean start;
    public boolean cua1;
    public boolean cua2_81;
    public boolean cua2_82;
    public boolean cua2_83;
    public boolean cua3_84;
    public boolean cua3_85;
    public boolean cua3_86;
    public boolean cua4_87;
    public boolean cua4_88;
    public boolean cua4_89;
    public ClanManager clanManager;


    public LanhDiaGiaToc() {
        this.ldgtID = idbase++;
        this.time = System.currentTimeMillis() + 600000L;
        this.finish = false;
        this.ninjas = new ArrayList();
        this.rest = false;
        this.start = false;
        this.cua2_81 = false;
        this.cua2_82 = false;
        this.cua2_83 = false;
        this.cua3_84 = false;
        this.cua3_85 = false;
        this.cua3_86 = false;
        this.cua4_87 = false;
        this.cua4_88 = false;
        this.cua4_89 = false;
        this.map = new Map[11];
        this.clanManager = null;
        this.initMap();

        for(byte i = 0; i < this.map.length; i++) {
            this.map[i].timeMap = this.time;
            this.map[i].start();
        }

        LanhDiaGiaToc.ldgts.put(this.ldgtID, this);
    }

    private void initMap() {
        this.map[0] = new Map(80, null, null, null, null, null, this);
        this.map[1] = new Map(81, null, null, null, null, null, this);
        this.map[2] = new Map(82, null, null, null, null, null, this);
        this.map[3] = new Map(83, null, null, null, null, null, this);
        this.map[4] = new Map(84, null, null, null, null, null, this);
        this.map[5] = new Map(85, null, null, null, null, null, this);
        this.map[6] = new Map(86, null, null, null, null, null, this);
        this.map[7] = new Map(87, null, null, null, null, null, this);
        this.map[8] = new Map(88, null, null, null, null, null, this);
        this.map[9] = new Map(89, null, null, null, null, null, this);
        this.map[10] = new Map(90, null, null, null, null, null, this);
    }

    public void updateXP(long xp) {
        synchronized(this) {
            for(short i = 0; i < this.ninjas.size(); i++) {
                try {
                    ((Char)this.ninjas.get(i)).p.updateExp(xp);
                } catch (Exception var7) {
                    var7.printStackTrace();
                }
            }
        }
    }

    public void rest() {
        if (!this.rest) {
            this.rest = true;
            try {
                synchronized (this) {
                    Map ma;
                    Char _char;
                    while (this.ninjas.size() > 0) {
                        _char = this.ninjas.remove(0);
                        if(_char != null) {
                            if(_char.get().getEffId(23) != null) {
                                _char.p.removeEffect(23);
                            }
                            _char.ldgtID = -1;
                            _char.tileMap.leave(_char.p);
                            _char.p.restCave();
                            ma = Manager.getMapid(_char.mapKanata);
                            byte k;
                            for (k = 0; k < ma.area.length; k++) {
                                if (ma.area[k].numplayers < ma.template.maxplayers) {
                                    ma.area[k].EnterMap0(_char);
                                    break;
                                }
                            }
                        }
                    }
                }

                if(this.clanManager != null) {
                    this.clanManager.ldgtID = -1;
                }

                byte i;
                for (i = 0; i < this.map.length; i++) {
                    this.map[i].close();
                    this.map[i] = null;
                }
                synchronized (LanhDiaGiaToc.ldgts) {
                    if(LanhDiaGiaToc.ldgts.containsKey(this.ldgtID)){
                        LanhDiaGiaToc.ldgts.remove(this.ldgtID);
                    }
                }
            } catch (Exception e) {
                byte i;
                for (i = 0; i < this.map.length; i++) {
                    if(this.map[i] != null) {
                        this.map[i].close();
                        this.map[i] = null;
                    }

                }
                synchronized (LanhDiaGiaToc.ldgts) {
                    if(LanhDiaGiaToc.ldgts.containsKey(this.ldgtID)){
                        LanhDiaGiaToc.ldgts.remove(this.ldgtID);
                    }
                }
            }
        }
    }

    public void finish() {
        synchronized (this) {
            if(!this.finish) {
                this.finish = true;
                this.time = System.currentTimeMillis() + 30000L;
                int i;
                for(i = 0; i < this.map.length; i++) {
                    this.map[i].timeMap = this.time;
                }
                synchronized (this.ninjas) {
                    Char _char;
                    for(i = 0; i < this.ninjas.size(); ++i) {
                        _char = this.ninjas.get(i);
                        if (_char != null) {
                            _char.p.setTimeMap((int)(this.time - System.currentTimeMillis()) / 1000);

                            _char.p.upluongMessage(10*clanManager.level*((int)clanManager.level/10));
                            _char.p.sendAddchatYellow("Đã hoàn thành Lãnh Địa Gia Tộc.");
                        }
                    }
                }
            }
        }
    }

    public void openMap(int type, Player p) {
        synchronized(this) {

            switch (type) {
                case 1: {
                    if(this.cua1) {
                        p.sendAddchatYellow("Trụ cơ quan này đã được cắm khoá, không thể cắm thêm.");
                        return;
                    }
                    p.c.removeItemBags(260, 1);
                    this.start = true;
                    this.cua1 = true;
                    this.time = System.currentTimeMillis() + 5400000L;
                    int i;
                    Char _char;
                    for(i = 0; i < this.map.length; i++) {
                        this.map[i].timeMap = this.time;
                    }
                    for(i = 0; i < this.ninjas.size(); i++) {
                        if(this.ninjas.get(i) != null) {
                            _char = this.ninjas.get(i);
                            _char.p.setTimeMap((int)(this.time - System.currentTimeMillis()) / 1000);
                            _char.p.sendAddchatYellow(p.c.name + " đã mở Cửa Siêu Tốc, Cửa Né Tránh, Cửa Phản Đòn");
                        }
                    }
                    break;
                }
                case 2: {
                    if(this.cua2_81) {
                        p.sendAddchatYellow("Trụ cơ quan này đã được cắm khoá, không thể cắm thêm.");
                        return;
                    }
                    p.c.removeItemBags(260, 1);
                    this.cua2_81 = true;
                    int i;
                    for(i = 0; i < this.ninjas.size(); i++) {
                        if(this.ninjas.get(i) != null) {
                            this.ninjas.get(i).p.sendAddchatYellow(p.c.name + " đã cắm chìa khoá tại Cửa Siêu Tốc");
                        }
                    }
                    break;
                }
                case 3: {
                    if(this.cua2_82) {
                        p.sendAddchatYellow("Trụ cơ quan này đã được cắm khoá, không thể cắm thêm.");
                        return;
                    }
                    p.c.removeItemBags(260, 1);
                    this.cua2_82 = true;
                    int i;
                    for(i = 0; i < this.ninjas.size(); i++) {
                        if(this.ninjas.get(i) != null) {
                            this.ninjas.get(i).p.sendAddchatYellow(p.c.name + " đã cắm chìa khoá tại Cửa Né Tránh");
                        }
                    }
                    break;
                }
                case 4: {
                    if(this.cua2_83) {
                        p.sendAddchatYellow("Trụ cơ quan này đã được cắm khoá, không thể cắm thêm.");
                        return;
                    }
                    p.c.removeItemBags(260, 1);
                    this.cua2_83 = true;
                    int i;
                    for(i = 0; i < this.ninjas.size(); i++) {
                        if(this.ninjas.get(i) != null) {
                            this.ninjas.get(i).p.sendAddchatYellow(p.c.name + " đã cắm chìa khoá tại Cửa Phản Đòn");
                        }
                    }
                    break;
                }
                case 5: {
                    if(this.cua3_84) {
                        p.sendAddchatYellow("Trụ cơ quan này đã được cắm khoá, không thể cắm thêm.");
                        return;
                    }
                    p.c.removeItemBags(260, 1);
                    this.cua3_84 = true;
                    int i;
                    for(i = 0; i < this.ninjas.size(); i++) {
                        if(this.ninjas.get(i) != null) {
                            this.ninjas.get(i).p.sendAddchatYellow(p.c.name + " đã cắm chìa khoá tại Cửa Hoả");
                        }
                    }
                    break;
                }
                case 6: {
                    if(this.cua3_85) {
                        p.sendAddchatYellow("Trụ cơ quan này đã được cắm khoá, không thể cắm thêm.");
                        return;
                    }
                    p.c.removeItemBags(260, 1);
                    this.cua3_85 = true;
                    int i;
                    for(i = 0; i < this.ninjas.size(); i++) {
                        if(this.ninjas.get(i) != null) {
                            this.ninjas.get(i).p.sendAddchatYellow(p.c.name + " đã cắm chìa khoá tại Cửa Phong");
                        }
                    }
                    break;
                }
                case 7: {
                    if(this.cua3_86) {
                        p.sendAddchatYellow("Trụ cơ quan này đã được cắm khoá, không thể cắm thêm.");
                        return;
                    }
                    p.c.removeItemBags(260, 1);
                    this.cua3_86 = true;
                    int i;
                    for(i = 0; i < this.ninjas.size(); i++) {
                        if(this.ninjas.get(i) != null) {
                            this.ninjas.get(i).p.sendAddchatYellow(p.c.name + " đã cắm chìa khoá tại Cửa Băng");
                        }
                    }
                    break;
                }
                case 8: {
                    if(this.cua4_87) {
                        p.sendAddchatYellow("Trụ cơ quan này đã được cắm khoá, không thể cắm thêm.");
                        return;
                    }
                    p.c.removeItemBags(260, 1);
                    this.cua4_87 = true;
                    int i;
                    for(i = 0; i < this.ninjas.size(); i++) {
                        if(this.ninjas.get(i) != null) {
                            this.ninjas.get(i).p.sendAddchatYellow(p.c.name + " đã cắm chìa khoá tại Cửa Sa Mạc");
                        }
                    }
                    break;
                }
                case 9: {
                    if(this.cua4_88) {
                        p.sendAddchatYellow("Trụ cơ quan này đã được cắm khoá, không thể cắm thêm.");
                        return;
                    }
                    p.c.removeItemBags(260, 1);
                    this.cua4_88 = true;
                    int i;
                    for(i = 0; i < this.ninjas.size(); i++) {
                        if(this.ninjas.get(i) != null) {
                            this.ninjas.get(i).p.sendAddchatYellow(p.c.name + " đã cắm chìa khoá tại Cửa Đồi Núi");
                        }
                    }
                    break;
                }
                case 10: {
                    if(this.cua4_89) {
                        p.sendAddchatYellow("Trụ cơ quan này đã được cắm khoá, không thể cắm thêm.");
                        return;
                    }
                    p.c.removeItemBags(260, 1);
                    this.cua4_89 = true;
                    int i;
                    for(i = 0; i < this.ninjas.size(); i++) {
                        if(this.ninjas.get(i) != null) {
                            this.ninjas.get(i).p.sendAddchatYellow(p.c.name + " đã cắm chìa khoá tại Cửa Đầm Lầy");
                        }
                    }
                    break;
                }
            }

        }
    }

    public void plusPoint(int quantity) {
        synchronized (this) {
            Item itemup = ItemTemplate.itemDefault(262);
            itemup.quantity = 1;
            if(quantity == 2) {
                itemup.quantity = Util.nextInt(10,20);
            } else if (quantity == 3) {
                itemup.quantity = Util.nextInt(30,50);
            }
            itemup.isExpires = false;
            itemup.isLock = true;
            int i;
            Char _char;
            for(i = 0; i < this.ninjas.size() ; i++) {
                if(this.ninjas.get(i) != null) {
                    _char = this.ninjas.get(i);
                    if(_char != null && _char.getBagNull() > 0) {
                        _char.addItemBag(true, itemup);
                        _char.p.sendAddchatYellow("Bạn nhận được "+itemup.quantity+" Đồng Tiền Gia Tộc.");
                    }
                }
            }
        }
    }
}
