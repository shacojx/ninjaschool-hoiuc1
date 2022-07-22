package com.hoiuc.stream.thiendiabang;

import com.hoiuc.assembly.Char;
import com.hoiuc.server.GameSrc;
import com.hoiuc.template.MapTemplate;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class ThienDiaBangMap {
    public int id;
    public MapTemplate template;
    public ThienDiaBangTileMap[] area;
    public ThienDiaBang thienDiaBang;
    public long timeMap;
    private boolean runing;
    private Object LOCK;
    private Thread threadUpdate;

    private class RunPlace implements Runnable {
        public RunPlace(){};
        public void run() {
            long l1;
            long l2;
            while (ThienDiaBangMap.this.runing) {
                try {
                    l1 = System.currentTimeMillis();
                    ThienDiaBangMap.this.update();
                    l2 = System.currentTimeMillis() - l1;
                    Thread.sleep(Math.abs(500L - l2));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return;
        }
    }

    public ThienDiaBangMap(int id, ThienDiaBang thienDiaBang, Char ninjaReal, Char ninjaBot) {
        this.LOCK = new Object();
        this.timeMap = -1L;
        this.id = id;
        this.template = MapTemplate.arrTemplate[id];
        this.area = new ThienDiaBangTileMap[MapTemplate.arrTemplate[id].numarea];
        for (byte i = 0; i < this.template.numarea; ++i) {
            this.area[i] = new ThienDiaBangTileMap(this, i, ninjaReal, ninjaBot);
        }
        this.thienDiaBang = thienDiaBang;
        this.loadMapFromResource();
        this.loadMap(this.template.tileID);
        this.threadUpdate = new Thread(new ThienDiaBangMap.RunPlace());
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
        this.thienDiaBang = null;
        this.LOCK = null;
    }

    public void loadMapFromResource() {

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

    public int ushort(short s) {
        return s & 0xFFFF;
    }
}
