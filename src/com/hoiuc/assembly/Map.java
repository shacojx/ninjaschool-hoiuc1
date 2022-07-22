package com.hoiuc.assembly;

import com.hoiuc.io.Util;
import com.hoiuc.server.GameSrc;
import com.hoiuc.stream.*;
import com.hoiuc.template.MapTemplate;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class Map {
    public static int[] arrLang = new int[] { 10, 17, 22, 32, 38, 43, 48 };
    public static int[] arrTruong = new int[] { 1, 27, 72 };
    public static int[] arrLangCo = new int[] { 134, 135, 136, 137};
    public static int[] idMapThuong = new int[]{4, 5, 7, 8, 9, 11, 12, 13, 14, 15, 16, 18, 19, 24, 28, 29, 30, 31, 33, 34, 35, 36, 37, 39, 40, 41, 42, 46, 47, 48, 49, 50, 51, 52, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68};
    public int id;
    public int numBossTL;
    public MapTemplate template;
    public TileMap[] area;
    public Cave cave;
    public Dun dun;
    public ChienTruong chienTruong;
    public BossTuanLoc bossTuanLoc;
    public GiaTocChien giaTocChien;
    public LanhDiaGiaToc lanhDiaGiaToc;
    public long timeMap;
    private boolean runing;
    private Object LOCK;
    private Thread threadUpdate;

    private class RunPlace implements Runnable {
        public RunPlace(){};
        public void run() {
            long l1;
            long l2;
            while (Map.this.runing) {
                try {
                    l1 = System.currentTimeMillis();
                    Map.this.update();
                    l2 = System.currentTimeMillis() - l1;
                    Thread.sleep(Math.abs(500L - l2));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return;
        }
    }

    public Map(int id,  Cave cave, Dun dun, ChienTruong chienTruong, BossTuanLoc bossTuanLoc, GiaTocChien giaTocChien, LanhDiaGiaToc lanhDiaGiaToc) {
        this.LOCK = new Object();
        this.numBossTL = 0;
        this.timeMap = -1L;
        this.id = id;
        this.template = MapTemplate.arrTemplate[id];
        this.area = new TileMap[MapTemplate.arrTemplate[id].numarea];
        for (byte i = 0; i < this.template.numarea; ++i) {
            this.area[i] = new TileMap(this, i);
        }
        this.cave = cave;
        this.dun = dun;
        this.chienTruong = chienTruong;
        this.bossTuanLoc = bossTuanLoc;
        this.giaTocChien = giaTocChien;
        this.lanhDiaGiaToc = lanhDiaGiaToc;
        this.loadMapFromResource();
        this.loadMap(this.template.tileID);
        this.initMob();
        this.threadUpdate = new Thread(new RunPlace());
    }
    
    public void refreshBossTet(int area) {
        for (int i = 15; i < this.area.length; i++) { //mac dinh 15
            if (i >= 30)
                break;
            if (i == area ) {
                TileMap tileMap = this.area[i];
                for (short j = 0; j < tileMap.mobs.size(); j++) {
                    Mob mob = tileMap.mobs.get(j);
                    if (mob.status == 0 && mob.isboss ) {
                        tileMap.refreshMob(mob.id);
                         System.out.println("Khởi tạo boss: " + mob.templates.name);
                    }
                }
                break;
            }
        }
    }

    public void initMob() {
        if(this.id >= 99 && this.id <= 103) {
            this.initMobChienTruong();
        } else {
            byte j;
            for (j = 0; j < this.area.length; ++j) {
                this.area[j].mobs.clear();
                int k = 0;
                int n;
                Mob m;
                short i;
                for (i = 0; i < this.template.arMobid.length; ++i) {
                    m = new Mob(k, this.template.arMobid[i], this.template.arrMoblevel[i], this.area[j]);
                    m.x = this.template.arrMobx[i];
                    m.y = this.template.arrMoby[i];
                    m.status = this.template.arrMobstatus[i];
                    m.lvboss = this.template.arrLevelboss[i];
                    m.sys = (byte) Util.nextInt(1, 3);
                    if (m.lvboss == 3) {
                        if (j % 5 == 0) {
                            n = m.hpmax * 200;
                            m.hpmax = n;
                            m.hp = n;
                        } else {
                            m.lvboss = 0;
                        }
                    } else if (m.lvboss == 2) {
                        n = m.hpmax * 100;
                        m.hpmax = n;
                        m.hp = n;
                    } else if (m.lvboss == 1) {
                        n = m.hpmax * 10;
                        m.hpmax = n;
                        m.hp = n;
                    }
                    m.isboss = this.template.arrisboss[i];
                    this.area[j].mobs.add(m);
                    k++;
                }
            }
        }
    }

    public void initMobChienTruong() {
        byte j;
        for (j = 0; j < this.area.length; j++) {
            this.area[j].mobs.clear();
            int k = 0;
            Mob m;
            short i;

            for (i = 0; i < this.template.arMobid.length; ++i) {
                m = new Mob(k, this.template.arMobid[i], this.template.arrMoblevel[i], this.area[j]);
                m.x = this.template.arrMobx[i];
                m.y = this.template.arrMoby[i];
                m.status = this.template.arrMobstatus[i];
                m.lvboss = this.template.arrLevelboss[i];
                m.sys = (byte) Util.nextInt(1, 3);
                m.isboss = false;
                if(m.templates.id == 98 || m.templates.id == 99) {
                    m.hpmax = 150000000;
                    m.hp = 150000000;
                }
                if(j == 0) {
                    this.area[j].mobs.add(m);
                }
                k++;
            }
        }
    }

    public void refreshBoss( int area) {
        int i;
        short j;
        TileMap tileMap;
        for (i = 15; i < this.area.length; ++i) {
            if (i >= 29) {
                break;
            }
            if (i == area) {
                tileMap = this.area[i];
                Mob mob;
                for (j = 0; j < tileMap.mobs.size(); ++j) {
                    mob = tileMap.mobs.get(j);
                    if (mob != null && mob.status == 0 && mob.isboss) {
                        tileMap.refreshMob(mob.id);
                        System.out.println("Khởi tạo boss: " + mob.templates.name);
                    }
                }
                break;
            }
        }
    }

    public boolean mapGTC() {
        if(this.id >= 117 && this.id <= 124) {
            return true;
        }
        return false;
    }

    public boolean mapLDGT() {
        if(this.id >= 80 && this.id <= 90) {
            return true;
        }
        return false;
    }

    public boolean mapThuong() {
        int i;
        for(i = 0; i < this.idMapThuong.length; i++) {
            if(this.id == this.idMapThuong[i]) {
                return true;
            }
        }
        return false;
    }

    public boolean mapBossTuanLoc() {
        if(this.id == 74) {
            return true;
        }
        return false;
    }

    public boolean mapChienTruong() {
        if(this.id == 98 || this.id == 99 || this.id == 100 || this.id == 101 ||this.id == 102 || this.id == 103 || this.id == 104) {
            return true;
        }
        return false;
    }

    public static boolean mapHD(int idMap) {
        if(idMap == 157 || idMap == 158 || idMap == 159 || idMap == 125
                || idMap == 126 || idMap == 127 || idMap == 128
                || idMap == 114 || idMap == 115 || idMap == 116
                || idMap == 105 || idMap == 106 || idMap == 107
                || idMap == 108 || idMap == 109 || idMap == 94
                || idMap == 95 || idMap == 96 || idMap == 97
                || idMap == 91 || idMap == 92 || idMap == 93) {
            return true;
        }
        return false;
    }

    public int getXHD() {
        if (this.id == 157 || this.id == 158 || this.id == 159) {
            return 9;
        }
        if (this.id == 125 || this.id == 126 || this.id == 127 || this.id == 128) {
            return 7;
        }
        if (this.id == 114 || this.id == 115 || this.id == 116) {
            return 6;
        }
        if (this.id == 105 || this.id == 106 || this.id == 107 || this.id == 108 || this.id == 109) {
            return 5;
        }
        if (this.id == 94 || this.id == 95 || this.id == 96 || this.id == 97) {
            return 4;
        }
        if (this.id == 91 || this.id == 92 || this.id == 93) {
            return 3;
        }
        return -1;
    }

    public boolean LangCo() {
        return this.id >= 132 && this.id <= 138;
    }

    public boolean VDMQ() {
        return this.id >= 139 && this.id <= 148;
    }

    private void update() {
        byte i;
        for (i = 0; i < this.area.length; ++i) {
            if(this.area[i] != null) {
                this.area[i].update();
            }
        }
    }

    public void start() {
        if (this.runing) {
            this.close();
        }
        this.runing = true;
        this.threadUpdate.start();
    }

    public void close() {
        this.runing = false;
        byte i;
        for (i = 0; i < this.area.length; ++i) {
            if(this.area[i] != null) {
                this.area[i].close();
                this.area[i] = null;
            }
        }
        this.threadUpdate = null;
        this.template = null;
        this.cave = null;
        this.dun = null;
        this.chienTruong = null;
        this.bossTuanLoc = null;
        this.giaTocChien = null;
        this.lanhDiaGiaToc = null;
        this.LOCK = null;
    }

    public void loadMapFromResource() {
//        if (this.id == 0 || this.id == 56 || (this.id > 72 && this.id < 125) || (this.id > 125 && this.id < 133) ||
//                (this.id > 133 && this.id < 139) || this.id > 148) {
//            return;
//        }
        ByteArrayInputStream bai = null;
        DataInputStream dis = null;
        try {
            byte[] ab = GameSrc.loadFile("res/Data/Map/" + this.id).toByteArray();
            bai = new ByteArrayInputStream(ab);
            dis = new DataInputStream(bai);
            this.template.tmw = this.ushort((short)dis.read());
            this.template.tmh = this.ushort((short)dis.read());
            this.template.maps = new char[dis.available()];
            int i;
            for (i = 0; i < this.template.tmw * this.template.tmh; i++)
                this.template.maps[i] = (char)dis.readByte();
            this.template.types = new int[this.template.maps.length];
            if(dis != null) {
                dis.close();
            }
            if(bai != null) {
                bai.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMap(int tileId) {
        //this.template.types = new int[this.template.tmw * this.template.tmh];
        this.template.pxh = (short)(this.template.tmh * 24);
        this.template.pxw = (short)(this.template.tmw * 24);
        try {
            int i;
            for (i = 0; i < this.template.tmh * this.template.tmw; ++i) {
                if (tileId == 4) {
                    if(this.template.maps[i] == 1 || this.template.maps[i] == 2 || this.template.maps[i] == 3 || this.template.maps[i] == 4 || this.template.maps[i] == 5 || this.template.maps[i] == 6 || this.template.maps[i] == 9 || this.template.maps[i] == 10 || this.template.maps[i] == 79 || this.template.maps[i] == 80 || this.template.maps[i] == 13 || this.template.maps[i] == 14 || this.template.maps[i] == 43 || this.template.maps[i] == 44 || this.template.maps[i] == 45 || this.template.maps[i] == 50){
                        this.template.types[i] |= MapTemplate.T_TOP;
                    }
                    if(this.template.maps[i] == 9 || this.template.maps[i] == 11) {
                        this.template.types[i] |= MapTemplate.T_LEFT;
                    }
                    if(this.template.maps[i] == 10 || this.template.maps[i] == 12) {
                        this.template.types[i] |= MapTemplate.T_RIGHT;
                    }
                    if(this.template.maps[i] == 13 || this.template.maps[i] == 14) {
                        this.template.types[i] |= MapTemplate.T_BRIDGE;
                    }
                    if(this.template.maps[i] == 76 || this.template.maps[i] == 77){
                        this.template.types[i] |= MapTemplate.T_WATERFLOW;
                        if(this.template.maps[i] == 78) {
                            this.template.types[i] |= MapTemplate.T_SOLIDGROUND;
                        }
                    }
                }
                else if (tileId == 1) {
                    if(this.template.maps[i] == 1 || this.template.maps[i] == 2 || this.template.maps[i] == 3 || this.template.maps[i] == 4 || this.template.maps[i] == 5 || this.template.maps[i] == 6 || this.template.maps[i] == 7 || this.template.maps[i] == 36 || this.template.maps[i] == 37 || this.template.maps[i] == 54 || this.template.maps[i] == 91 || this.template.maps[i] == 92 || this.template.maps[i] == 93 || this.template.maps[i] == 94 || this.template.maps[i] == 73 || this.template.maps[i] == 74 || this.template.maps[i] == 97 || this.template.maps[i] == 98 || this.template.maps[i] == 116 || this.template.maps[i] == 117 || this.template.maps[i] == 118 || this.template.maps[i] == 120 || this.template.maps[i] == 61) {
                        this.template.types[i] |= MapTemplate.T_TOP;
                    }
                    if(this.template.maps[i] == 2 || this.template.maps[i] == 3 || this.template.maps[i] == 4 || this.template.maps[i] == 5 || this.template.maps[i] == 6 || this.template.maps[i] == 20 || this.template.maps[i] == 21 || this.template.maps[i] == 22 || this.template.maps[i] == 23 || this.template.maps[i] == 36 || this.template.maps[i] == 37 || this.template.maps[i] == 38 || this.template.maps[i] == 39 || this.template.maps[i] == 61) {
                        this.template.types[i] |= MapTemplate.T_SOLIDGROUND;
                    }
                    if(this.template.maps[i] == 8 || this.template.maps[i] == 9 || this.template.maps[i] == 10 || this.template.maps[i] == 12 || this.template.maps[i] == 13 || this.template.maps[i] == 14 || this.template.maps[i] == 30) {
                        this.template.types[i] |= MapTemplate.T_TREE;
                    }
                    if(this.template.maps[i] == 17) {
                        this.template.types[i] |= MapTemplate.T_WATERFALL;
                    }
                    if(this.template.maps[i] == 18) {
                        this.template.types[i] |= MapTemplate.T_TOPFALL;
                    }
                    if(this.template.maps[i] == 37 || this.template.maps[i] == 38 || this.template.maps[i] == 61) {
                        this.template.types[i] |= MapTemplate.T_LEFT;
                    }
                    if(this.template.maps[i] == 36 || this.template.maps[i] == 39 || this.template.maps[i] == 61) {
                        this.template.types[i] |= MapTemplate.T_RIGHT;
                    }
                    if(this.template.maps[i] == 19) {
                        this.template.types[i] |= MapTemplate.T_WATERFLOW;
                        if ((this.template.types[i - this.template.tmw] & MapTemplate.T_SOLIDGROUND) == MapTemplate.T_SOLIDGROUND) {
                            this.template.types[i] |= MapTemplate.T_SOLIDGROUND;
                        }
                    }
                    if(this.template.maps[i] == 35) {
                        this.template.types[i] |= MapTemplate.T_UNDERWATER;
                    }
                    if(this.template.maps[i] == 7) {
                        this.template.types[i] |= MapTemplate.T_BRIDGE;
                    }
                    if(this.template.maps[i] == 32 || this.template.maps[i] == 33 || this.template.maps[i] == 34) {
                        this.template.types[i] |= MapTemplate.T_OUTSIDE;
                    }
                }
                else if (tileId == 2) {
                    if(this.template.maps[i] == 1 || this.template.maps[i] == 2 || this.template.maps[i] == 3 || this.template.maps[i] == 4 || this.template.maps[i] == 5 || this.template.maps[i] == 6 || this.template.maps[i] == 7 || this.template.maps[i] == 36 || this.template.maps[i] == 37 || this.template.maps[i] == 54 || this.template.maps[i] == 61 || this.template.maps[i] == 73 || this.template.maps[i] == 76 || this.template.maps[i] == 77 || this.template.maps[i] == 78 || this.template.maps[i] == 79 || this.template.maps[i] == 82 || this.template.maps[i] == 83 || this.template.maps[i] == 98 || this.template.maps[i] == 99 || this.template.maps[i] == 100 || this.template.maps[i] == 102 || this.template.maps[i] == 103 || this.template.maps[i] == 108 || this.template.maps[i] == 109 || this.template.maps[i] == 110 || this.template.maps[i] == 112 || this.template.maps[i] == 113 || this.template.maps[i] == 116 || this.template.maps[i] == 117 || this.template.maps[i] == 125 || this.template.maps[i] == 126 || this.template.maps[i] == 127 || this.template.maps[i] == 129 || this.template.maps[i] == 130) {
                        this.template.types[i] |= MapTemplate.T_TOP;
                    }
                    if(this.template.maps[i] == 1 || this.template.maps[i] == 3 || this.template.maps[i] == 4 || this.template.maps[i] == 5 || this.template.maps[i] == 6 || this.template.maps[i] == 20 || this.template.maps[i] == 21 || this.template.maps[i] == 22 || this.template.maps[i] == 23 || this.template.maps[i] == 36 || this.template.maps[i] == 37 || this.template.maps[i] == 38 || this.template.maps[i] == 39 || this.template.maps[i] == 55 || this.template.maps[i] == 109 || this.template.maps[i] == 111 || this.template.maps[i] == 112 || this.template.maps[i] == 113 || this.template.maps[i] == 114 || this.template.maps[i] == 115 || this.template.maps[i] == 116 || this.template.maps[i] == 127 || this.template.maps[i] == 129 || this.template.maps[i] == 130) {
                        this.template.types[i] |= MapTemplate.T_SOLIDGROUND;
                    }
                    if(this.template.maps[i] == 8 || this.template.maps[i] == 9 || this.template.maps[i] == 10 || this.template.maps[i] == 12 || this.template.maps[i] == 13 || this.template.maps[i] == 14 || this.template.maps[i] == 30 || this.template.maps[i] == 135) {
                        this.template.types[i] |= MapTemplate.T_TREE;
                    }
                    if(this.template.maps[i] == 17) {
                        this.template.types[i] |= MapTemplate.T_WATERFALL;
                    }
                    if(this.template.maps[i] == 18) {
                        this.template.types[i] |= MapTemplate.T_TOPFALL;
                    }
                    if(this.template.maps[i] == 61 || this.template.maps[i] == 37 || this.template.maps[i] == 38 || this.template.maps[i] == 127 || this.template.maps[i] == 130 || this.template.maps[i] == 131) {
                        this.template.types[i] |= MapTemplate.T_LEFT;
                    }
                    if(this.template.maps[i] == 61 || this.template.maps[i] == 36 || this.template.maps[i] == 39 || this.template.maps[i] == 127 || this.template.maps[i] == 129 || this.template.maps[i] == 132) {
                        this.template.types[i] |= MapTemplate.T_RIGHT;
                    }
                    if(this.template.maps[i] == 19) {
                        this.template.types[i] |= MapTemplate.T_WATERFLOW;
                        if ((this.template.types[i - this.template.tmw] & MapTemplate.T_SOLIDGROUND) == MapTemplate.T_SOLIDGROUND) {
                            this.template.types[i] |= MapTemplate.T_SOLIDGROUND;
                        }
                    }
                    if(this.template.maps[i] == 134) {
                        this.template.types[i] |= MapTemplate.T_WATERFLOW;
                        if ((this.template.types[i - this.template.tmw] & MapTemplate.T_SOLIDGROUND) == MapTemplate.T_SOLIDGROUND) {
                            this.template.types[i] |= MapTemplate.T_SOLIDGROUND;
                        }
                    }
                    if(this.template.maps[i] == 35) {
                        this.template.types[i] |= MapTemplate.T_UNDERWATER;
                    }
                    if(this.template.maps[i] == 7) {
                        this.template.types[i] |= MapTemplate.T_BRIDGE;
                    }
                    if(this.template.maps[i] == 32 || this.template.maps[i] == 33 || this.template.maps[i] == 34){
                        this.template.types[i] |= MapTemplate.T_OUTSIDE;
                    }
                    if(this.template.maps[i] == 61 || this.template.maps[i] == 127) {
                        this.template.types[i] |= MapTemplate.T_BOTTOM;
                    }
                }
                else if (tileId == 3) {
                    if(this.template.maps[i] == 1 || this.template.maps[i] == 2 || this.template.maps[i] == 3 || this.template.maps[i] == 4 || this.template.maps[i] == 5 || this.template.maps[i] == 6 || this.template.maps[i] == 7 || this.template.maps[i] == 11 || this.template.maps[i] == 14 || this.template.maps[i] == 17 || this.template.maps[i] == 43 || this.template.maps[i] == 51 || this.template.maps[i] == 63 || this.template.maps[i] == 65 || this.template.maps[i] == 67 || this.template.maps[i] == 68 || this.template.maps[i] == 71 || this.template.maps[i] == 72 || this.template.maps[i] == 83 || this.template.maps[i] == 84 || this.template.maps[i] == 85 || this.template.maps[i] == 87 || this.template.maps[i] == 91 || this.template.maps[i] == 94 || this.template.maps[i] == 97 || this.template.maps[i] == 98 || this.template.maps[i] == 106 || this.template.maps[i] == 107 || this.template.maps[i] == 111 || this.template.maps[i] == 113 || this.template.maps[i] == 117 || this.template.maps[i] == 118 || this.template.maps[i] == 119 || this.template.maps[i] == 125 || this.template.maps[i] == 126 || this.template.maps[i] == 129 || this.template.maps[i] == 130 || this.template.maps[i] == 131 || this.template.maps[i] == 133 || this.template.maps[i] == 136 || this.template.maps[i] == 138 || this.template.maps[i] == 139 || this.template.maps[i] == 142) {
                        this.template.types[i] |= MapTemplate.T_TOP;
                    }
                    if (this.template.maps[i] == 124 || this.template.maps[i] == 116 || this.template.maps[i] == 123 || this.template.maps[i] == 44 || this.template.maps[i] == 12 || this.template.maps[i] == 15 || this.template.maps[i] == 15 || this.template.maps[i] == 45 || this.template.maps[i] == 10 || this.template.maps[i] == 9) {
                        this.template.types[i] |= MapTemplate.T_SOLIDGROUND;
                    }
                    if (this.template.maps[i] == 23) {
                        this.template.types[i] |= MapTemplate.T_WATERFALL;
                    }
                    if (this.template.maps[i] == 24) {
                        this.template.types[i] |= MapTemplate.T_TOPFALL;
                    }
                    if (this.template.maps[i] == 6 || this.template.maps[i] == 15 || this.template.maps[i] == 51 || this.template.maps[i] == 95 || this.template.maps[i] == 97 || this.template.maps[i] == 106 || this.template.maps[i] == 111 || this.template.maps[i] == 123 || this.template.maps[i] == 125 || this.template.maps[i] == 138 || this.template.maps[i] == 140) {
                        this.template.types[i] |= MapTemplate.T_LEFT;
                    }
                    if (this.template.maps[i] == 7 || this.template.maps[i] == 16 || this.template.maps[i] == 51 || this.template.maps[i] == 96 || this.template.maps[i] == 98 || this.template.maps[i] == 107 || this.template.maps[i] == 111 || this.template.maps[i] == 124 || this.template.maps[i] == 126 || this.template.maps[i] == 139 || this.template.maps[i] == 141) {
                        this.template.types[i] |= MapTemplate.T_RIGHT;
                    }
                    if (this.template.maps[i] == 25) {
                        this.template.types[i] |= MapTemplate.T_WATERFLOW;
                        if ((this.template.types[i - this.template.tmw] & MapTemplate.T_SOLIDGROUND) == MapTemplate.T_SOLIDGROUND) {
                            this.template.types[i] |= MapTemplate.T_SOLIDGROUND;
                        }
                    }
                    if (this.template.maps[i] == 34) {
                        this.template.types[i] |= MapTemplate.T_UNDERWATER;
                    }
                    if (this.template.maps[i] == 17) {
                        this.template.types[i] |= MapTemplate.T_BRIDGE;
                    }
                    if (this.template.maps[i] == 33 || this.template.maps[i] == 103 || this.template.maps[i] == 104 || this.template.maps[i] == 105 || this.template.maps[i] == 26 || this.template.maps[i] == 33) {
                        this.template.types[i] |= MapTemplate.T_OUTSIDE;
                    }
                    if (this.template.maps[i] == 51 || this.template.maps[i] == 111 || this.template.maps[i] == 68) {
                        this.template.types[i] |= MapTemplate.T_BOTTOM;
                    }
                    if (this.template.maps[i] == 82 || this.template.maps[i] == 110 || this.template.maps[i] == 143) {
                        this.template.types[i] |= MapTemplate.T_DIE;
                    }
                    if (this.template.maps[i] == 113) {
                        this.template.types[i] |= MapTemplate.T_BANG;
                    }
                    if (this.template.maps[i] == 142) {
                        this.template.types[i] |= 0x8000;
                    }
                    if (this.template.maps[i] == 40 || this.template.maps[i] == 41) {
                        this.template.types[i] |= MapTemplate.T_JUM8;
                    }
                    if (this.template.maps[i] == 110) {
                        this.template.types[i] |= MapTemplate.T_NT0;
                    }
                    if (this.template.maps[i] == 143) {
                        this.template.types[i] |= MapTemplate.T_NT1;
                    }
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public short touchY(short x, short y) {
        short yOld = y;
        while (y < this.template.pxh) {
            if (tileTypeAt(x, y, 2))
                return y;
            y = (short)(y + 1);
        }
        if((short)this.template.pxh != 0) {
            return (short)this.template.pxh;
        }
        return (short)(yOld+24);
    }

    public boolean tileTypeAt(int px, int py, int t) {
        boolean result;
        try {
            result = ((this.template.types[py / 24 * this.template.tmw + px / 24] & t) == t);
        } catch (Exception ex) {
            result = false;
            ex.printStackTrace();
        }
        return result;
    }

    public int ushort(short s) {
        return s & 0xFFFF;
    }
}
