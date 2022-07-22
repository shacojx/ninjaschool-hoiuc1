package com.hoiuc.stream;

import com.hoiuc.assembly.Char;
import com.hoiuc.assembly.Map;
import com.hoiuc.server.Manager;
import com.hoiuc.server.Rank;

import java.util.ArrayList;
import java.util.HashMap;

public class BossTuanLoc {
    public int mapTuanLociId;
    public Map[] map;
    public long time;
    public boolean finish;
    public ArrayList<Char> ninjas = new ArrayList();
    private static int idbase;
    private boolean rest;
    public static HashMap<Integer, BossTuanLoc> mapTuanLocs = new HashMap();

    public BossTuanLoc(int a) {
        this.mapTuanLociId = idbase++;
        this.time = System.currentTimeMillis() + 1800000L;
        this.rest = false;
        this.finish = false;
        this.map = new Map[1];
        this.initMap(a);
        for(byte i = 0; i < this.map.length; ++i) {
            this.map[i].timeMap = this.time;
            this.map[i].start();
        }

        BossTuanLoc.mapTuanLocs.put(this.mapTuanLociId, this);
    }

    private void initMap(int a) {
        this.map[0] = new Map(74, null, null, null, this, null, null);
    }

    public void rest() {
        if (!this.rest) {
            this.rest = true;
            try {
                synchronized (this) {
                    Map ma;
                    Char _char;
                    while (this.ninjas.size() > 0) {
                        _char = this.ninjas.get(0);
                        if(_char != null) {
                            _char.tileMap.leave(_char.p);
                            _char.p.restCave();
                            ma = Manager.getMapid(_char.mapLTD);
                            byte k;
                            for (k = 0; k < ma.area.length; ++k) {
                                if (ma.area[k].numplayers < ma.template.maxplayers) {
                                    ma.area[k].EnterMap0(_char);
                                    break;
                                }
                            }
                        }
                    }
                }
                this.map[0].close();
                this.map[0] = null;
                synchronized (BossTuanLoc.mapTuanLocs) {
                    if(BossTuanLoc.mapTuanLocs.containsKey(this.mapTuanLociId)){
                        BossTuanLoc.mapTuanLocs.remove(this.mapTuanLociId);
                    }
                }
            } catch (Exception e) {
                this.map[0].close();
                this.map[0] = null;
                synchronized (BossTuanLoc.mapTuanLocs) {
                    if(BossTuanLoc.mapTuanLocs.containsKey(this.mapTuanLociId)){
                        BossTuanLoc.mapTuanLocs.remove(this.mapTuanLociId);
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
                this.map[0].timeMap = this.time;
                synchronized (this.ninjas) {
                    for(int i = 0; i < this.ninjas.size(); ++i) {
                        if (this.ninjas.get(i) != null) {
                            (this.ninjas.get(i)).p.setTimeMap((int)(this.time - System.currentTimeMillis()) / 1000);
                            this.ninjas.get(i).p.sendAddchatYellow("Đã tiêu diệt boss Tuần Lộc thành công. Bạn có 30 giây để thu thập vật phẩm.");
                            this.ninjas.get(i).flush();
                        }
                    }
                }
                Rank.updateBossTL();
            }
        }
    }
}
