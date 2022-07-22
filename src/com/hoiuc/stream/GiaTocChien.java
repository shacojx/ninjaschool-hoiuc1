package com.hoiuc.stream;

import com.hoiuc.assembly.Char;
import com.hoiuc.assembly.ClanManager;
import com.hoiuc.assembly.Map;
import com.hoiuc.assembly.TileMap;
import com.hoiuc.server.Manager;
import com.hoiuc.server.Service;

import java.util.ArrayList;
import java.util.HashMap;

public class GiaTocChien {
    private static int idbase;
    public int gtcID;
    public Map[] map;
    public long time;
    public ArrayList<Char> gt1;
    public ArrayList<Char> gt2;
    public boolean rest;
    public boolean start;
    public boolean isDatCuoc;
    public long tienCuoc1;
    public long tienCuoc2;
    public ClanManager clan1;
    public ClanManager clan2;
    public ClanManager clanWin;
    public int pointClan1;
    public int pointClan2;
    public static HashMap<Integer, GiaTocChien> gtcs = new HashMap();

    public GiaTocChien() {
        this.gtcID = idbase++;
        this.time = System.currentTimeMillis() + 10000L;
        this.map = new Map[8];
        this.start = false;
        this.isDatCuoc = true;
        this.gt1 = new ArrayList<>();
        this.gt2 = new ArrayList<>();
        this.rest = false;
        this.tienCuoc1 = 0;
        this.tienCuoc2 = 0;
        this.clan1 = null;
        this.clan2 = null;
        this.clanWin = null;
        this.pointClan1 = 0;
        this.pointClan2 = 0;
        this.initMap();

        for(byte i = 0; i < this.map.length; ++i) {
            this.map[i].timeMap = this.time;
            this.map[i].start();
        }
        gtcs.put(this.gtcID, this);
    }

    private void initMap() {
        this.map[0] = new Map(117, null, null, null, null ,this, null);
        this.map[1] = new Map(118, null, null, null, null ,this, null);
        this.map[2] = new Map(119, null, null, null, null ,this, null);
        this.map[3] = new Map(120, null, null, null, null ,this, null);
        this.map[4] = new Map(121, null, null, null, null ,this, null);
        this.map[5] = new Map(122, null, null, null, null ,this, null);
        this.map[6] = new Map(123, null, null, null, null ,this, null);
        this.map[7] = new Map(124, null, null, null, null ,this, null);
    }

    public void rest() {
        if (!this.rest) {
            this.rest = true;
            try {
                synchronized (this) {
                    if(this.isDatCuoc) {
                        if(tienCuoc1 > 0) {
                            clan1.coin += tienCuoc1;
                            clan1.flush();
                        }
                        if(tienCuoc2 > 0) {
                            clan2.coin += tienCuoc2;
                            clan2.flush();
                        }
                    }
                    if(this.start) {
                        if(this.pointClan1 == this.pointClan2) {
                            this.clan1.coin += this.tienCuoc1*95/100;
                            this.clan2.coin += this.tienCuoc2*95/100;
                            this.clan1.flush();
                            this.clan2.flush();
                        } else {
                            if (this.pointClan1 > this.pointClan2) {
                                this.clanWin = this.clan1;
                            } else if (this.pointClan1 < this.pointClan2) {
                                this.clanWin = this.clan2;
                            }
                            this.clanWin.coin += this.tienCuoc1 * 2 - this.tienCuoc1*2 / 10;
                            this.clanWin.flush();
                        }
                    }

                    this.clan1.gtcID = -1;
                    this.clan2.gtcID = -1;
                    this.clan1.gtcClanName = null;
                    this.clan2.gtcClanName = null;

                    Map ma;
                    Char _char;
                    while (this.gt1.size() > 0) {
                        _char = this.gt1.remove(0);
                        if(_char != null) {
                            if(this.start) {
                                if(this.clanWin == null) {
                                    _char.p.sendAddchatYellow("Gia tộc chiến đã kết thúc. Hai gia tộc có kết quả hoà.");
                                } else {
                                    _char.p.sendAddchatYellow("Gia tộc chiến đã kết thúc. Gia tộc " + this.clanWin.name + " đã giành chiến thắng.");
                                }

                            } else {
                                _char.p.sendAddchatYellow("Gia tộc chiến đã kết thúc.");
                            }
                            _char.pointGTC = 0;
                            _char.gtcId = -1;
                            _char.typepk = 0;
                            Service.ChangTypePkId(_char, (byte)0);

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
                    while (this.gt2.size() > 0) {
                        _char = this.gt2.remove(0);
                        if(_char != null) {
                            if(this.start) {
                                if(this.clanWin == null) {
                                    _char.p.sendAddchatYellow("Gia tộc chiến đã kết thúc. Hai gia tộc có kết quả hoà.");
                                } else {
                                    _char.p.sendAddchatYellow("Gia tộc chiến đã kết thúc. Gia tộc " + this.clanWin.name + " đã giành chiến thắng.");
                                }
                            } else {
                                _char.p.sendAddchatYellow("Gia tộc chiến đã kết thúc.");
                            }
                            _char.pointGTC = 0;
                            _char.gtcId = -1;
                            _char.typepk = 0;
                            Service.ChangTypePkId(_char, (byte)0);
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

                byte i;
                for (i = 0; i < this.map.length; ++i) {
                    this.map[i].close();
                    this.map[i] = null;
                }
                synchronized (GiaTocChien.gtcs) {
                    if(GiaTocChien.gtcs.containsKey(this.gtcID)){
                        GiaTocChien.gtcs.remove(this.gtcID);
                    }
                }
            }
            catch (Exception e) {
                byte i;
                for (i = 0; i < this.map.length; ++i) {
                    if(this.map[i] != null) {
                        this.map[i].close();
                        this.map[i] = null;
                    }

                }
                synchronized (GiaTocChien.gtcs) {
                    if(GiaTocChien.gtcs.containsKey(this.gtcID)){
                        GiaTocChien.gtcs.remove(this.gtcID);
                    }
                }
            }
        }
    }

    public void start() {
        if(!this.start) {
            this.start = true;
            synchronized (this) {
                //this.time = System.currentTimeMillis() + 3600000L;
                this.time = System.currentTimeMillis() + 20000L;
                int i;
                for(i = 0; i < this.map.length; i++) {
                    this.map[i].timeMap = this.time;
                }
                Char _char;
                synchronized (this.gt1) {
                    for(i = 0; i < this.gt1.size(); ++i) {
                        _char = this.gt1.get(i);
                        if (_char != null) {
                            _char.p.setTimeMap((int)(this.time - System.currentTimeMillis()) / 1000);
                            _char.p.sendAddchatYellow("Gia tộc chiến đã bắt đầu.");
                        }
                    }
                }
                synchronized (this.gt2) {
                    for(i = 0; i < this.gt2.size(); ++i) {
                        _char = this.gt2.get(i);
                        if (_char != null) {
                            _char.p.setTimeMap((int)(this.time - System.currentTimeMillis()) / 1000);
                            _char.p.sendAddchatYellow("Gia tộc chiến đã bắt đầu.");
                        }
                    }
                }
            }
        }
    }

    public void invite() {
        if(this.isDatCuoc) {
            synchronized (this) {
                this.isDatCuoc = false;
                this.time = System.currentTimeMillis() + 10000L;
                int i;
                for (i = 0; i < this.map.length; i++) {
                    this.map[i].timeMap = this.time;
                }
                Char _char;
                synchronized (this.gt1) {
                    for (i = this.gt1.size() - 1;i>=0; i--) {
                        _char = this.gt1.get(i);
                        if (_char != null) {
                            _char.p.setTimeMap((int) (this.time - System.currentTimeMillis()) / 1000);
                            _char.gtcId = this.gtcID;
                            _char.pointGTC = 0;
                            _char.typepk = 4;
                            Service.ChangTypePkId(_char, (byte) 4);
                            Service.sendPointGTC(_char, 0);
                            for (TileMap area : this.map[1].area) {
                                if (area.numplayers < this.map[1].template.maxplayers) {
                                    _char.tileMap.leave(_char.p);
                                    area.EnterMap0(_char);
                                    break;
                                }
                            }
                            _char.p.sendAddchatYellow("Bạn có 5 phút để triệu tập thành viên tham gia Gia tộc chiến.");
                        }
                    }
                }
                synchronized (this.gt2) {
                    for (i = this.gt2.size() - 1;i>=0; i--) {
                        _char = this.gt2.get(i);
                        if (_char != null) {
                            _char.p.setTimeMap((int) (this.time - System.currentTimeMillis()) / 1000);
                            _char.gtcId = this.gtcID;
                            _char.pointGTC = 0;
                            _char.typepk = 5;
                            Service.ChangTypePkId(_char, (byte) 5);
                            Service.sendPointGTC(_char, 0);
                            for (TileMap area : this.map[2].area) {
                                if (area.numplayers < this.map[2].template.maxplayers) {
                                    _char.tileMap.leave(_char.p);
                                    area.EnterMap0(_char);
                                    break;
                                }
                            }
                            _char.p.sendAddchatYellow("Bạn có 5 phút để triệu tập thành viên tham gia Gia tộc chiến.");
                        }
                    }
                }
            }
        }
    }

    public void join() {
        synchronized (this) {
            this.time = System.currentTimeMillis() + 300000L;
            int i;
            for(i = 0; i < this.map.length; i++) {
                this.map[i].timeMap = this.time;
            }
            synchronized (this.gt1) {
                for(i = this.gt1.size() - 1;i>=0; i--) {
                    if (this.gt1.get(i) != null) {
                        (this.gt1.get(i)).p.setTimeMap((int)(this.time - System.currentTimeMillis()) / 1000);
                    }
                }
            }
            synchronized (this.gt2) {
                for(i = this.gt2.size() - 1;i>=0; i--) {
                    if (this.gt2.get(i) != null) {
                        (this.gt2.get(i)).p.setTimeMap((int)(this.time - System.currentTimeMillis()) / 1000);
                    }
                }
            }
        }
    }

    public void leave() {
        if(this.isDatCuoc) {
            synchronized (this) {
                int i;
                if(this.gt1.size() > 0) {
                    for (i = this.gt1.size() - 1;i>=0; i--) {
                        if(this.gt1.get(i) != null) {
                            this.gt1.get(i).p.sendAddchatYellow("Gia tộc đối phương đã huỷ bỏ cuộc so găng.");
                        }
                    }
                }
                if(this.gt2.size() > 0) {
                    for (i = this.gt2.size() - 1;i>=0; i--) {
                        if(this.gt2.get(i) != null) {
                            this.gt2.get(i).p.sendAddchatYellow("Gia tộc đối phương đã huỷ bỏ cuộc so găng.");
                        }
                    }
                }
                if(tienCuoc1 > 0) {
                    clan1.coin += tienCuoc1;
                    clan1.flush();
                }
                if(tienCuoc2 > 0) {
                    clan2.coin += tienCuoc2;
                    clan2.flush();
                }
                this.isDatCuoc = false;
                this.rest();
            }
        }
    }
}
