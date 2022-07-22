package com.hoiuc.stream;

import com.hoiuc.assembly.Char;
import com.hoiuc.assembly.Map;
import com.hoiuc.server.Manager;

import java.util.ArrayList;
import java.util.HashMap;

public class Cave {
    public int caveID;
    public Map[] map;
    public long time;
    public int level = 0;
    public byte finsh = 0;
    public int x = -1;
    public ArrayList<Char> ninjas = new ArrayList();
    private static int idbase;
    private boolean rest = false;
    public static HashMap<Integer, Cave> caves = new HashMap();

    public Cave(int x) {
        this.x = x;
        this.caveID = idbase++;
        this.time = System.currentTimeMillis() + 3600000L;
        if (x == 3) {
            this.map = new Map[3];
        } else if (x == 4) {
            this.map = new Map[4];
        } else if (x == 5) {
            this.map = new Map[5];
        } else if (x == 6) {
            this.map = new Map[3];
        } else if (x == 7) {
            this.map = new Map[4];
        } else if (x == 9) {
            this.map = new Map[3];
        }

        this.initMap(x);

        for(byte i = 0; i < this.map.length; i++) {
            this.map[i].timeMap = this.time;
            this.map[i].start();
        }

        caves.put(this.caveID, this);
    }

    private void initMap(int x) {
        switch(x) {
            case 3:
                this.map[0] = new Map(91, this, null, null, null, null, null);
                this.map[1] = new Map(92, this, null, null, null, null, null);
                this.map[2] = new Map(93, this, null, null, null, null, null);
                break;
            case 4:
                this.map[0] = new Map(94, this, null, null, null, null, null);
                this.map[1] = new Map(95, this, null, null, null, null, null);
                this.map[2] = new Map(96, this, null, null, null, null, null);
                this.map[3] = new Map(97, this, null, null, null, null, null);
                break;
            case 5:
                this.map[0] = new Map(105, this, null, null, null, null, null);
                this.map[1] = new Map(106, this, null, null, null, null, null);
                this.map[2] = new Map(107, this, null, null, null, null, null);
                this.map[3] = new Map(108, this, null, null, null, null, null);
                this.map[4] = new Map(109, this, null, null, null, null, null);
                break;
            case 6:
                this.map[0] = new Map(114, this, null, null, null, null, null);
                this.map[1] = new Map(115, this, null, null, null, null, null);
                this.map[2] = new Map(116, this, null, null, null, null, null);
                break;
            case 7:
                this.map[0] = new Map(125, this, null, null, null, null, null);
                this.map[1] = new Map(126, this, null, null, null, null, null);
                this.map[2] = new Map(127, this, null, null, null, null, null);
                this.map[3] = new Map(128, this, null, null, null, null, null);
                break;
            case 9:
                this.map[0] = new Map(157, this, null, null, null, null, null);
                this.map[1] = new Map(158, this, null, null, null, null, null);
                this.map[2] = new Map(159, this, null, null, null, null, null);
                break;
        }

    }

    public void updateXP(long xp) {
        synchronized(this) {
            for(short i = 0; i < this.ninjas.size(); i++) {
                if(this.ninjas.get(i) != null){
                    (this.ninjas.get(i)).p.updateExp(xp);
                }
            }

        }
    }

    public void updatePoint(int point) {
        synchronized(this) {
            for(short i = 0; i < this.ninjas.size(); i++) {
                if (this.ninjas.get(i) != null) {
                    this.ninjas.get(i).pointCave += point;
                    ((Char)this.ninjas.get(i)).p.setPointPB(((Char)this.ninjas.get(i)).pointCave);
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
                            if(_char.party != null && _char.party.cave != null && _char.party.cave.caveID == this.caveID) {
                                _char.party.cave = null;
                            }
                            _char.caveID = -1;
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
                byte i;
                for (i = 0; i < this.map.length; i++) {
                    this.map[i].close();
                    this.map[i] = null;
                }
                synchronized (Cave.caves) {
                    if(Cave.caves.containsKey(this.caveID)){
                        Cave.caves.remove(this.caveID);
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
                synchronized (Cave.caves) {
                    if(Cave.caves.containsKey(this.caveID)){
                        Cave.caves.remove(this.caveID);
                    }
                }
            }
        }
    }

    public void finish() {
        synchronized(this) {
            this.level++;
            byte i;
            if (this.x != 6) {
                this.time = System.currentTimeMillis() + 10000L;
                for(i = 0; i < this.map.length; i++) {
                    this.map[i].timeMap = this.time;
                }
            }

            if (this.x != 6 || this.finsh == 0) {
                this.finsh++;
                Char _char;
                for(i = 0; i < this.ninjas.size(); i++) {
                    _char = this.ninjas.get(i);
                    if(_char != null && _char.p != null && _char.p.conn != null) {
                        _char.p.setTimeMap((int)(this.time - System.currentTimeMillis()) / 1000);
                        _char.p.sendAddchatYellow("Hoàn thành hang động");
                        if (_char.party != null && _char.party.cave != null) {
                            _char.party.cave = null;
                        }
                        if (!_char.clan.clanName.isEmpty()) {
                            _char.p.upExpClan(10);
                        }
                    }
                }
            }

        }
    }

    public void openMap() {
        synchronized(this) {
            this.level++;
            if (this.level < this.map.length) {
                for(byte i = 0; i < this.ninjas.size(); ++i) {
                    if(this.ninjas.get(i) != null) {
                        this.ninjas.get(i).p.sendAddchatYellow(this.map[this.level].template.name + " đã được mở");
                    }
                }
            }

        }
    }
}
