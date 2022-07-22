package com.hoiuc.server;

import com.hoiuc.assembly.Char;
import com.hoiuc.assembly.Player;
import com.hoiuc.io.SQLManager;
import com.hoiuc.io.Util;
import com.hoiuc.stream.Client;
import com.hoiuc.stream.Server;
import com.hoiuc.stream.thiendiabang.ThienDiaData;
import org.json.simple.JSONArray;

import java.sql.ResultSet;
import java.time.Instant;
import java.util.*;

public class ThienDiaBangManager {
    public static boolean register = true;
    public static ThienDiaBangManager[] thienDiaBangManager = new ThienDiaBangManager[2];
    public static HashMap<String, ThienDiaData> thienBangList = new HashMap<>();
    public static HashMap<String, ThienDiaData> diaBangList = new HashMap<>();
    public static int rankDiaBang = 1;
    public static int rankThienBang = 1;

    private int id;
    private String week;
    private int type;

    public ThienDiaBangManager() {
    }

    public ThienDiaBangManager(int id, String week, int type) {
        this.id =id;
        this.type =type;
        this.week = week;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int id) {
        this.type = type;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public static void giveGifts() {
        try {
            synchronized (Client.gI().conns) {
                int i;
                Player player;
                for (i=0; i<Client.gI().conns.size(); i++) {
                    if(Client.gI().conns.get(i) != null && Client.gI().conns.get(i).player != null) {
                        player = Client.gI().conns.get(i).player;
                        player.flush();
                        if (player.c != null) {
                            player.c.rankTDB = 0;
                            player.c.isGiftTDB = 0;
                        }
                    }
                }
            }
            for(ThienDiaData value : ThienDiaBangManager.getListDiaBang()) {
                if(value != null) {
                    SQLManager.stat.executeUpdate("UPDATE `ninja` SET `rankTDB`="+value.getRank()+", `isGiftTDB` = 1 WHERE `name` = '"+value.getName()+"' LIMIT 1;");
                    Char n = Client.gI().getNinja(value.getName());
                    if(n != null) {
                        n.rankTDB = value.getRank();
                        n.isGiftTDB = 1;
                    }
                }
            }
            for(ThienDiaData value : ThienDiaBangManager.getListThienBang()) {
                if(value != null) {
                    SQLManager.stat.executeUpdate("UPDATE `ninja` SET `rankTDB`="+value.getRank()+", `isGiftTDB` = 1 WHERE `name` = '"+value.getName()+"' LIMIT 1;");
                    Char n = Client.gI().getNinja(value.getName());
                    if(n != null) {
                        n.rankTDB = value.getRank();
                        n.isGiftTDB = 1;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ThienDiaBangManager.diaBangList.clear();
        ThienDiaBangManager.thienBangList.clear();
    }

    public static ArrayList<ThienDiaData> getListThienBang() {
        synchronized (ThienDiaBangManager.thienBangList) {
            ArrayList<ThienDiaData> listSortThienBang = new ArrayList<ThienDiaData>(ThienDiaBangManager.thienBangList.values());
            return ThienDiaBangManager.getListSort(listSortThienBang);
        }
    }

    public static ArrayList<ThienDiaData> getListDiaBang() {
        synchronized (ThienDiaBangManager.diaBangList) {
            ArrayList<ThienDiaData> listSortThienBang = new ArrayList<ThienDiaData>(ThienDiaBangManager.diaBangList.values());
            return ThienDiaBangManager.getListSort(listSortThienBang);
        }
    }

    public static ArrayList<ThienDiaData> getListSort (ArrayList<ThienDiaData> list) {
        Collections.sort(list, new Comparator<ThienDiaData>() {
            @Override
            public int compare(ThienDiaData o1, ThienDiaData o2) {
                return o2.getRank() - o1.getRank();
            }
        });
        return list;
    }

    public static ArrayList<ThienDiaData> getListSortAsc (ArrayList<ThienDiaData> list) {
        Collections.sort(list, new Comparator<ThienDiaData>() {
            @Override
            public int compare(ThienDiaData o1, ThienDiaData o2) {
                return o1.getRank() - o2.getRank();
            }
        });
        return list;
    }

    public synchronized static void resetThienDiaBang() {
        try {
            ThienDiaBangManager.close();
            ThienDiaBangManager.rankDiaBang = 1;
            ThienDiaBangManager.rankThienBang = 1;
            SQLManager.stat.executeUpdate("UPDATE `ninja` SET `rankTDB`=0, `isGiftTDB`=0;");
            ThienDiaBangManager.giveGifts();
            String week = Util.toDateString(Date.from(Instant.now()));
            SQLManager.stat.executeUpdate("INSERT INTO thiendia(`week`,`type`) VALUES ('" + week + "',1);");
            SQLManager.stat.executeUpdate("INSERT INTO thiendia(`week`,`type`) VALUES ('" + week + "',2);");
            ResultSet res = SQLManager.stat.executeQuery("SELECT * FROM `thiendia` ORDER BY `week` DESC LIMIT 2;");
            while (res.next()) {
                int id = Integer.parseInt(res.getString("id"));
                week = res.getString("week");
                int type = Integer.parseInt(res.getString("type"));
                if(type == 1) {
                    ThienDiaBangManager.thienDiaBangManager[0] = new ThienDiaBangManager(id, week, type);
                } else if(type == 2) {
                    ThienDiaBangManager.thienDiaBangManager[1] = new ThienDiaBangManager(id, week, type);
                }
            }
            if(res != null) {
                res.close();
            }
            ThienDiaBangManager.register = true;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void flush() {
        try {
            synchronized(Server.LOCK_MYSQL) {
                JSONArray jarr = new JSONArray();
                String sqlSET = "`type` = "+this.getType()+",`week`='" + this.getWeek() + "'";
                ThienDiaData value;
                if(this.getType() == 1) {
                    for(Map.Entry<String, ThienDiaData> entry : ThienDiaBangManager.diaBangList.entrySet()) {
                        value = entry.getValue();
                        if(value != null) {
                            JSONArray jarr2  = new JSONArray();
                            jarr2.add(value.getName());
                            jarr2.add(1);
                            jarr2.add(value.getRank());
                            jarr.add(jarr2);
                        }

                    }
                } else if(this.getType() == 2) {
                    for(Map.Entry<String, ThienDiaData> entry : ThienDiaBangManager.thienBangList.entrySet()) {
                        value = entry.getValue();
                        if(value != null) {
                            JSONArray jarr2  = new JSONArray();
                            jarr2.add(value.getName());
                            jarr2.add(1);
                            jarr2.add(value.getRank());
                            jarr.add(jarr2);
                        }
                    }
                }

                sqlSET = sqlSET + ",`data`='" + jarr.toJSONString() + "'";
                jarr.clear();

                SQLManager.stat.executeUpdate("UPDATE `thiendia` SET " + sqlSET + " WHERE `id`=" + this.getId() + ";");
                if (jarr != null && !jarr.isEmpty()) {
                    jarr.clear();
                }
            }
        }
        catch (Exception var10) {
            var10.printStackTrace();
        }
    }

    public void autoFlush() {
        try {
            synchronized(Server.LOCK_MYSQL) {
                JSONArray jarr = new JSONArray();
                String sqlSET = "`type` = "+this.getType()+",`week`='" + this.getWeek() + "'";
                ThienDiaData value;
                if(this.getType() == 1) {
                    for(Map.Entry<String, ThienDiaData> entry : ThienDiaBangManager.diaBangList.entrySet()) {
                        value = entry.getValue();
                        if(value != null) {
                            JSONArray jarr2  = new JSONArray();
                            jarr2.add(value.getName());
                            jarr2.add(value.getType());
                            jarr2.add(value.getRank());
                            jarr.add(jarr2);
                        }

                    }
                } else if(this.getType() == 2) {
                    for(Map.Entry<String, ThienDiaData> entry : ThienDiaBangManager.thienBangList.entrySet()) {
                        value = entry.getValue();
                        if(value != null) {
                            JSONArray jarr2  = new JSONArray();
                            jarr2.add(value.getName());
                            jarr2.add(value.getType());
                            jarr2.add(value.getRank());
                            jarr.add(jarr2);
                        }
                    }
                }

                sqlSET = sqlSET + ",`data`='" + jarr.toJSONString() + "'";
                jarr.clear();

                SQLManager.stat.executeUpdate("UPDATE `thiendia` SET " + sqlSET + " WHERE `id`=" + this.getId() + ";");
                if (jarr != null && !jarr.isEmpty()) {
                    jarr.clear();
                }
            }
        }
        catch (Exception var10) {
            var10.printStackTrace();
        }
    }

    public static void close() {
        for(int i = 0; i < ThienDiaBangManager.thienDiaBangManager.length; ++i) {
            if (ThienDiaBangManager.thienDiaBangManager[i] != null) {
                ThienDiaBangManager.thienDiaBangManager[i].flush();
            }
        }
        System.out.println("Flush/ Close Thiên Địa Bảng");
    }
}
