package com.hoiuc.assembly;

import cache.MobCache;
import com.hoiuc.io.Util;
import com.hoiuc.server.Service;
import com.hoiuc.server.Session;
import com.hoiuc.stream.ChienTruong;
import com.hoiuc.stream.Client;
import com.hoiuc.stream.Server;
import com.hoiuc.template.MobTemplate;

import java.util.ArrayList;
import java.util.HashMap;

public class Mob {
    public boolean isFire;
    public boolean isIce;
    public boolean isWind;
    public long timeFire;
    public long timeIce;
    public long timeWind;
    public int id;
    public byte sys;
    public int hp;
    public int level;
    public int hpmax;
    public short x;
    public short y;
    public byte status;
    public int lvboss;
    public int dameFire;
    public long timeDameFire;
    public boolean isboss;
    public boolean isDie;
    public boolean isRefresh;
    public long xpup;
    public long timeRefresh;
    public long timeFight;
    public long timeDisable;
    public long timeDontMove;
    public boolean isDisable;
    public boolean isDontMove;
    public TileMap tileMap;
    public int idCharSkill25;
    public MobTemplate templates;
    private HashMap<Integer, Integer> nFight;
    private ArrayList<Character> sortFight;
    public static MobCache[] arrMobTemplate;

    private static int[] arrMobLangCoId = new int[] {148, 146, 147, 148, 149, 151, 152, 154, 155, 156, 157, 159 };
    private static int[] arrMobChienTruongId = new int[] {90, 91, 92, 93, 94, 95, 96, 97, 98, 99 };

    public Mob(int id, int idtemplate, int level, TileMap tileMap) {
        this.isRefresh = true;
        this.id = id;
        this.templates = MobTemplate.entrys.get(idtemplate);
        this.level = level;
        int hp = this.templates.hp;
        this.hpmax = hp;
        this.hp = hp;
        this.xpup = 10000L;
        this.isDie = false;
        this.dameFire = 0;
        this.timeDameFire = -1L;
        this.timeFire = -1L;
        this.nFight = new HashMap<Integer, Integer>();
        this.sortFight = new ArrayList<Character>();
        this.timeDisable = -1L;
        this.timeDontMove = -1L;
        this.isDisable = false;
        this.isDontMove = false;
        this.tileMap = tileMap;
        this.idCharSkill25 = -1;
    }

    public void setSkill25() {
        this.timeDameFire = -1L;
        this.dameFire = 0;
        this.timeFire = -1L;
        this.idCharSkill25 = -1;
    }

    public boolean checkMobLangCo() {
        int i;
        for(i = 0; i < Mob.arrMobLangCoId.length; i++) {
            if(this.templates.id == Mob.arrMobLangCoId[i]) {
                return true;
            }
        }
        return false;
    }

    public boolean checkMobChienTruong() {
        if(this.templates.id >= 90 && this.templates.id <= 99) {
            return true;
        }
        return false;
    }

    public void updateHP(int num, int _charId, boolean liveAttack) {
        this.hp += num;
        Char _char = this.tileMap.getNinja(_charId);
        if(!liveAttack) {
            if(_char != null) {
                this.Fight(_char.p.conn.id, Math.abs(num));
            }
        }
        if (this.hp <= 0) {
            this.hp = 0;
            this.status = 0;
            this.isDie = true;
            if (this.isRefresh) {
                this.timeRefresh = System.currentTimeMillis() + 7500L;
            }
            if(this.isRefresh && this.checkMobLangCo()) {
                this.timeRefresh = System.currentTimeMillis() + 20000L;
            }else if(this.isRefresh && this.checkMobChienTruong()) {
                this.timeRefresh = System.currentTimeMillis() + 30000L;
            }else if(this.isRefresh && this.tileMap.map.getXHD() == 9) {
                this.timeRefresh = System.currentTimeMillis() + 20000L;
            }
            if (this.isboss) {
                if (this.templates.id != 198 && this.templates.id != 199 && this.templates.id != 200) {
                    this.isRefresh = false;
                    this.timeRefresh = -1L;
                }
                else {
                    this.timeRefresh = System.currentTimeMillis() + 60000L;
                }
            }
            if(_char != null) {
                synchronized (this) {
                    this.handleAfterCharFight(_char);
                }
            }
        }
    }

    public void ClearFight() {
        this.nFight.clear();
    }

    public int sortNinjaFight() {
        int idN = -1;
        int dameMax = 0;
        int dame;
        Session conn;
        for (int value : this.nFight.keySet()) {
            dame = this.nFight.get(value);
            conn = Client.gI().getConn(value);
            if (conn != null && conn.player != null && conn.player.c != null) {
                if (dame <= dameMax) {
                    continue;
                }
                dameMax = this.nFight.get(value);
                idN = conn.player.c.id;
            }
        }
        return idN;
    }

    public void Fight(int id, int dame) {
        if (!this.nFight.containsKey(id)) {
            this.nFight.put(id, dame);
        }
        else {
            int damenew = this.nFight.get(id);
            damenew += dame;
            this.nFight.replace(id, damenew);
        }
    }

    public void removeFight(int id) {
        if (this.nFight.containsKey(id)) {
            this.nFight.remove(id);
        }
    }

    public boolean isFight(int id) {
        return this.nFight.containsKey(id);
    }

    public void setDisable(boolean isDisable, long timeDisable) {
        this.isDisable = isDisable;
        this.timeDisable = timeDisable;
    }

    public void setDonteMove(boolean isDontMove, long timeDontMove) {
        this.isDontMove = isDontMove;
        this.timeDontMove = timeDontMove;
    }

    public boolean isDisable() {
        return this.isDisable;
    }

    public boolean isDonteMove() {
        return this.isDontMove;
    }

    public void handleAfterCharFight(Char _char) {
        if (this.level > 1) {
            this.tileMap.numMobDie++;
        }
        if (this.templates.id == 0) {
            if (_char.isTaskDanhVong == 1 && _char.taskDanhVong[0] == 6) {
                _char.taskDanhVong[1]++;
                if(_char.c.taskDanhVong[1] == _char.c.taskDanhVong[2]) {
                    _char.p.sendAddchatYellow("Bạn đã hoàn thành nhiệm vụ danh vọng.");
                }
            }
        }
        if(this.templates.id == 230 && this.tileMap.map.bossTuanLoc != null) {
            this.isRefresh = false;
            ItemLeave.leaveItemBOSSTuanLoc(this.tileMap, this, -1);
            _char.pointNoel += 5;
            _char.pointBossTL++;
            this.tileMap.mobs.clear();
        }
        else if(this.tileMap.map.mapLDGT()) {
            if(this.lvboss == 0 && this.templates.id != 81) {
                this.isRefresh = false;
                switch (this.tileMap.map.id) {
                    case 81:
                    case 82:
                    case 83:
                    case 84:
                    case 85:
                    case 86: {
                        if(this.tileMap.mobs.size() - this.tileMap.numMobDie == 1) {
                            this.tileMap.refreshMob(this.tileMap.mobs.size()-1);
                        }
                        break;
                    }
                    case 87:
                    case 88:
                    case 89: {
                        if(this.tileMap.mobs.size() - this.tileMap.numMobDie == 5) {
                            this.tileMap.refreshMob(this.tileMap.mobs.size()-5);
                        }
                        break;
                    }
                }
                this.tileMap.map.lanhDiaGiaToc.plusPoint(1);
                this.tileMap.map.lanhDiaGiaToc.clanManager.upExp(50);
            } else if(this.lvboss == 1 && this.templates.id != 81) {
                this.isRefresh = false;
                ItemLeave.leaveChiaKhoa(this.tileMap, this, -1);
                this.tileMap.map.lanhDiaGiaToc.plusPoint(2);
                this.tileMap.map.lanhDiaGiaToc.clanManager.upExp(100);
                if(this.tileMap.map.id >= 87 && this.tileMap.map.id <= 89) {
                    int i2;
                    for(i2 = this.tileMap.mobs.size()-4; i2 < this.tileMap.mobs.size()-1; i2++) {
                        this.tileMap.refreshMob(i2);
                    }
                }
            } else if(this.lvboss == 0 && this.templates.id == 81) {
                this.isRefresh = true;
                ItemLeave.leaveLDGT(this.tileMap, this, -1);
            } else if(this.lvboss == 2 && this.templates.id == 82) {
                this.isRefresh = false;
                ItemLeave.leaveYen(this.tileMap, this, -1);
                ItemLeave.leaveYen(this.tileMap, this, -1);
                ItemLeave.leaveYen(this.tileMap, this, -1);
                ItemLeave.leaveYen(this.tileMap, this, -1);
                ItemLeave.leaveYen(this.tileMap, this, -1);
                ItemLeave.leaveLDGT(this.tileMap, this, -1);
                this.tileMap.map.lanhDiaGiaToc.finish();
                this.tileMap.map.lanhDiaGiaToc.clanManager.upExp(300);
                this.tileMap.map.lanhDiaGiaToc.plusPoint(3);
            }
        }
        else if((this.templates.id == 98 || this.templates.id == 99) && ChienTruong.chienTruong != null && this.tileMap.map.mapChienTruong()) {
            if(this.templates.id == 98) {
                ChienTruong.pheWin = 1;
            } else if (this.templates.id == 99) {
                ChienTruong.pheWin = 0;
            }
            ChienTruong.chienTruong.finish();
        }
        else if (this.level > 1) {
            if (this.tileMap.map.cave != null) {
                if (this.isboss) {
                    this.tileMap.map.cave.updatePoint(50);
                } else if (this.lvboss == 2) {
                    this.tileMap.map.cave.updatePoint(20);
                } else if (this.lvboss == 1) {
                    this.tileMap.map.cave.updatePoint(10);
                } else {
                    this.tileMap.map.cave.updatePoint(1);
                }
            }
            else if(ChienTruong.chienTruong != null && this.tileMap.map.mapChienTruong()) {
                _char.pointCT++;
                if(_char.pointCT > 14000) {
                    _char.pointCT = 14000;
                }
                Service.updatePointCT(_char, 1);
            }
            else if(this.tileMap.map.giaTocChien != null && this.tileMap.map.mapGTC()) {
                _char.pointGTC++;
                if(_char.pointGTC > 14000) {
                    _char.pointGTC = 14000;
                }
                Service.sendPointGTC(_char, 1);
            }

            if (_char.isTaskHangNgay == 1 && this.templates.id == _char.taskHangNgay[3] && _char.taskHangNgay[0] == 0 && _char.taskHangNgay[1] < _char.taskHangNgay[2]) {
                _char.taskHangNgay[1]++;
                Service.updateTaskOrder(_char, (byte)0);
            }
            if (_char.isTaskTaThu == 1 && this.templates.id == _char.taskTaThu[3] && this.lvboss == 3 && _char.taskTaThu[0] == 1 && _char.taskTaThu[1] < _char.taskTaThu[2]) {
                _char.taskTaThu[1]++;
                Service.updateTaskOrder(_char, (byte)1);
            }
            if (_char.isTaskDanhVong == 1 && _char.taskDanhVong[0] == 7 && Math.abs(this.level - _char.get().level) <= 10) {
                _char.taskDanhVong[1]++;
                if(_char.c.taskDanhVong[1] == _char.c.taskDanhVong[2]) {
                    _char.p.sendAddchatYellow("Bạn đã hoàn thành nhiệm vụ danh vọng.");
                }
            }
            int master = this.sortNinjaFight();
            if (this.lvboss == 1) {
                this.tileMap.numTA--;
                if (_char.isTaskDanhVong == 1 && _char.taskDanhVong[0] == 8 && Math.abs(this.level - _char.get().level) <= 10) {
                    _char.taskDanhVong[1]++;
                    if(_char.c.taskDanhVong[1] == _char.c.taskDanhVong[2]) {
                        _char.p.sendAddchatYellow("Bạn đã hoàn thành nhiệm vụ danh vọng.");
                    }
                }
            } else if (this.lvboss == 2) {
                this.tileMap.numTL--;
                if (_char.isTaskDanhVong == 1 && _char.taskDanhVong[0] == 9 && Math.abs(this.level - _char.get().level) <= 10) {
                    _char.taskDanhVong[1]++;
                    if(_char.c.taskDanhVong[1] == _char.c.taskDanhVong[2]) {
                        _char.p.sendAddchatYellow("Bạn đã hoàn thành nhiệm vụ danh vọng.");
                    }
                }
            }

            if (Math.abs(this.level - _char.get().level) <= 10 || this.tileMap.map.LangCo() || this.tileMap.map.mapChienTruong()) {
                if (this.lvboss == 1) {
                    ItemLeave.leaveYen(this.tileMap, this, master);
                }else if (this.lvboss == 2) {
                    ItemLeave.leaveYen(this.tileMap, this, master);
                }
                if (Server.manager.event != 0) {
                    ItemLeave.leaveItemSuKien(this.tileMap, this, master);
                }
                switch (Util.nextInt(1, 2)) {
                    case 1: {
                        if(this.lvboss == 0 && Util.nextInt(10) < 1) {
                            ItemLeave.leaveYen(this.tileMap, this, master);
                        }
                        break;
                    }
                    case 2: {
                        if(Util.nextInt(10) < 6) {
                            ItemLeave.leaveItemOrther(this.tileMap, this, master);
                        }
                        break;
                    }
                }
            }

            if (this.tileMap.map.VDMQ() && (_char.get().getEffId(40) != null || _char.get().getEffId(41) != null) && Math.abs(this.level - _char.get().level) <= 10) {
                ItemLeave.randomLeave(this.tileMap, this, master, Util.nextInt(1, 2), 0);
            } else if (this.tileMap.map.LangCo()) {
                ItemLeave.randomLeave(this.tileMap, this, master, Util.nextInt(1, 3), 1);
                if(this.lvboss == 2) {
                    ItemLeave.leaveTTTT(this.tileMap, this, master);
                }
            }

            if (this.isboss) {
                if (this.tileMap.map.cave == null) {
                    Service.chatKTG(_char.name + " đã tiêu diệt " + this.templates.name);
                    int i;
                    for(i = 0; i < 10; i++) {
                        ItemLeave.leaveYen(this.tileMap, this, master);
                    }
                    ItemLeave.leaveItemBOSS(this.tileMap, this, master);
                }
                else if (this.tileMap.map.cave != null && this.tileMap.map.getXHD() == 9 && ((this.tileMap.map.id == 157 && this.tileMap.map.cave.level == 0) || (this.tileMap.map.id == 158 && this.tileMap.map.cave.level == 1) || (this.tileMap.map.id == 159 && this.tileMap.map.cave.level == 2)) && Util.nextInt(3) < 3) {
                    ItemLeave.leaveYen(this.tileMap, this, master);
                    ItemLeave.leaveYen(this.tileMap, this, master);
                    this.tileMap.map.cave.updatePoint(this.tileMap.mobs.size());
                    short k2;
                    Mob var12;
                    for (k2 = 0; k2 < this.tileMap.mobs.size(); k2++) {
                        if(this.tileMap.mobs.get(k2) == null) {
                            continue;
                        }
                        var12 = this.tileMap.mobs.get(k2);
                        if (!var12.isDie) {
                            var12.updateHP(-var12.hpmax, _char.id, true);
                        }
                        var12.isRefresh = false;
                        short h;
                        for (h = 0; h < this.tileMap.players.size(); h++) {
                            Service.setHPMob(this.tileMap.players.get(h).c, var12.id, 0);
                        }
                    }
                    this.tileMap.map.cave.level++;
                }
            }

            if (this.tileMap.map.cave != null && this.tileMap.map.getXHD() < 9) {
                this.isRefresh = false;
                if (this.tileMap.numMobDie == this.tileMap.mobs.size()) {
                    if (this.tileMap.map.getXHD() == 5) {
                        if (this.tileMap.map.id == 105) {
                            this.tileMap.map.cave.openMap();
                            this.tileMap.map.cave.openMap();
                            this.tileMap.map.cave.openMap();
                        } else if (this.tileMap.map.id != 106 && this.tileMap.map.id != 107 && this.tileMap.map.id != 108) {
                            this.tileMap.map.cave.openMap();
                        } else {
                            this.tileMap.map.cave.finsh++;
                            if (this.tileMap.map.cave.finsh >= 3) {
                                this.tileMap.map.cave.openMap();
                            }
                        }
                    } else if (this.tileMap.map.getXHD() == 6 && this.tileMap.map.id == 116) {
                        if (this.tileMap.map.cave.finsh == 0) {
                            this.tileMap.map.cave.openMap();
                        } else {
                            this.tileMap.map.cave.finsh++;
                        }
                        this.tileMap.numMobDie = 0;
                        short l2;
                        for(l2 = 0; l2 < this.tileMap.mobs.size(); l2++) {
                            this.tileMap.refreshMob(l2);
                        }
                    } else {
                        this.tileMap.map.cave.openMap();
                    }
                }
            }
        }
    }
}
