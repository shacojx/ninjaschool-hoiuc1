package com.hoiuc.server;

import com.hoiuc.assembly.ClanManager;
import com.hoiuc.io.SQLManager;
import com.hoiuc.io.Util;
import com.hoiuc.stream.Server;
import com.hoiuc.template.ItemTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;

public class Rank {
    public static ArrayList<Entry>[] bangXH = new ArrayList[4];
    public static Timer t = new Timer(true);

    public static ArrayList<Entry2> bxhCaoThu = new ArrayList<>();
    public static ArrayList<Entry3> bxhBossTuanLoc = new ArrayList<>();
    public static ArrayList<Entry4> bxhBossChuot = new ArrayList<>();

    public static void updateCaoThu() {
        Rank.bxhCaoThu.clear();
        ResultSet red = null;
        try {
            synchronized (Rank.bxhCaoThu) {
                int i = 1;
                red = SQLManager.stat.executeQuery("SELECT `name`,`class`,`level`,`time` FROM `xep_hang_level` WHERE `level` = "+Manager.max_level_up+" ORDER BY `id` ASC LIMIT 20;");
                String name;
                int level;
                String nClass;
                String time;
                Entry2 bXHCaoThu;
                if(red != null) {
                    while (red.next()) {
                        name = red.getString("name");
                        level = red.getInt("level");
                        nClass = red.getString("class");
                        time = red.getString("time");
                        bXHCaoThu = new Entry2();
                        bXHCaoThu.name = name;
                        bXHCaoThu.index = i;
                        bXHCaoThu.level = level;
                        bXHCaoThu.nClass = nClass;
                        bXHCaoThu.time = time;
                        bxhCaoThu.add(bXHCaoThu);
                        i++;
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(red != null) {
                try {
                    red.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static void updateBossChuot() {
        ResultSet red = null;
        try {
            Rank.bxhBossChuot.clear();
            synchronized (Rank.bxhBossChuot) {
                int i = 1;
                red = SQLManager.stat.executeQuery("SELECT `name`,`pointBossChuot` FROM `ninja` WHERE (`pointBossChuot` > 0) ORDER BY `pointBossChuot` DESC LIMIT 10;");
                if(red != null) {
                    Entry4 bXHBChuot;
                    String name;
                    int point;
                    while (red.next()) {
                        name = red.getString("name");
                        point = Integer.parseInt(red.getString("pointBossChuot"));
                        bXHBChuot = new Entry4();
                        bXHBChuot.name = name;
                        bXHBChuot.index = i;
                        bXHBChuot.point1 = point;
                        bxhBossChuot.add(bXHBChuot);
                        ++i;
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(red != null) {
                try {
                    red.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void updateBossTL() {
        ResultSet red = null;
        try {
            Rank.bxhBossTuanLoc.clear();
            synchronized (Rank.bxhBossTuanLoc) {
                int i = 1;
                red = SQLManager.stat.executeQuery("SELECT `name`,`pointBossTL` FROM `ninja` WHERE (`pointBossTL` > 0) ORDER BY `pointBossTL` DESC LIMIT 10;");
                if(red != null) {
                    Entry3 bXHBTL;
                    String name;
                    int point;
                    while (red.next()) {
                        name = red.getString("name");
                        point = Integer.parseInt(red.getString("pointBossTL"));
                        bXHBTL = new Entry3();
                        bXHBTL.name = name;
                        bXHBTL.index = i;
                        bXHBTL.point = point;
                        bxhBossTuanLoc.add(bXHBTL);
                        ++i;
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(red != null) {
                try {
                    red.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void init() {
        Rank.updateCaoThu();
        Rank.updateBossTL();
        for (int i = 0; i < Rank.bangXH.length; ++i) {
            Rank.bangXH[i] = new ArrayList<Entry>();
        }
        System.out.println("load BXH");
        for (int i = 0; i < Rank.bangXH.length; ++i) {
            initBXH(i);
        }
    }

    public static void initBXH(int type) {
        Rank.bangXH[type].clear();
        ArrayList<Entry> bxh = Rank.bangXH[type];
        switch (type) {
            case 0: {
                ResultSet red = null;
                try {
                    int i = 1;
                    red = SQLManager.stat.executeQuery("SELECT `name`,`yen`,`level` FROM `ninja` WHERE (`yen` > 0) ORDER BY `yen` DESC LIMIT 10;");
                    String name;
                    int coin;
                    int level;
                    Entry bXHE;
                    if(red != null) {
                        while (red.next()) {
                            name = red.getString("name");
                            coin = red.getInt("yen");
                            level = red.getInt("level");
                            bXHE = new Entry();
                            bXHE.nXH = new long[2];
                            bXHE.name = name;
                            bXHE.index = i;
                            bXHE.nXH[0] = coin;
                            bXHE.nXH[1] = level;
                            bxh.add(bXHE);
                            i++;
                        }
                    }
                }
                catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if(red != null) {
                        try {
                            red.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            }
            case 1: {
                ResultSet red = null;
                try {
                    int i = 1;
                    red = SQLManager.stat.executeQuery("SELECT `name`,`exp`,`level`,`class` FROM `ninja` WHERE (`exp` > 0) ORDER BY `exp` DESC LIMIT 20;");
                    String name;
                    long exp;
                    int level2;
                    int nClass;
                    Entry bXHE2;
                    while (red.next()) {
                        name = red.getString("name");
                        exp = red.getLong("exp");
                        level2 = red.getInt("level");
                        nClass = red.getInt("class");
                        bXHE2 = new Entry();
                        bXHE2.nXH = new long[3];
                        bXHE2.name = name;
                        bXHE2.index = i;
                        bXHE2.nXH[0] = exp;
                        bXHE2.nXH[1] = level2;
                        bXHE2.nXH[2] = nClass;
                        bxh.add(bXHE2);
                        i++;
                    }
                }
                catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if(red != null) {
                        try {
                            red.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            }
            case 2: {
                ResultSet red= null;
                try {
                    int i = 1;
                    red = SQLManager.stat.executeQuery("SELECT `name`,`level` FROM `clan` WHERE (`level` > 0) ORDER BY `level` DESC LIMIT 10;");
                    String name;
                    int level3;
                    Entry bXHE3;
                    while (red.next()) {
                        name = red.getString("name");
                        level3 = red.getInt("level");
                        bXHE3 = new Entry();
                        bXHE3.nXH = new long[1];
                        bXHE3.name = name;
                        bXHE3.index = i;
                        bXHE3.nXH[0] = level3;
                        bxh.add(bXHE3);
                        i++;
                    }
                }
                catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if(red != null) {
                        try {
                            red.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            }
            case 3: {
                ResultSet red = null;
                try {
                    int i = 1;
                    red = SQLManager.stat.executeQuery("SELECT `name`,`bagCaveMax`,`itemIDCaveMax` FROM `ninja` WHERE (`bagCaveMax` > 0) ORDER BY `bagCaveMax` DESC LIMIT 10;");
                    String name;
                    int cave;
                    short id;
                    Entry bXHE;
                    while (red.next()) {
                        name = red.getString("name");
                        cave = red.getInt("bagCaveMax");
                        id = red.getShort("itemIDCaveMax");
                        bXHE = new Entry();
                        bXHE.nXH = new long[2];
                        bXHE.name = name;
                        bXHE.index = i;
                        bXHE.nXH[0] = cave;
                        bXHE.nXH[1] = id;
                        bxh.add(bXHE);
                        ++i;
                    }
                }
                catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if(red != null) {
                        try {
                            red.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            }
        }
    }

    public static Entry[] getBangXH(int type) {
        ArrayList<Entry> bxh = Rank.bangXH[type];
        Entry[] bxhA = new Entry[bxh.size()];
        for (int i = 0; i < bxhA.length; ++i) {
            bxhA[i] = bxh.get(i);
        }
        return bxhA;
    }

    public static String getStringBXH(int type) {
        String str = "";
        switch (type) {
            case 0: {
                if (Rank.bangXH[type].isEmpty()) {
                    str = "Chưa có thông tin";
                    break;
                }
                for (Entry bxh : Rank.bangXH[type]) {
                    str = str + bxh.index + ". " + bxh.name + ": " + Util.getFormatNumber(bxh.nXH[0]) + " yên - cấp: " + bxh.nXH[1] + "\n";
                }
                break;
            }
            case 1: {
                if (Rank.bangXH[type].isEmpty()) {
                    str = "Chưa có thông tin";
                    break;
                }
                if(Rank.bxhCaoThu.size() < 1) {
                    for (Entry bxh : Rank.bangXH[type]) {
                        str = str + bxh.index + ". " + bxh.name + ": " + Util.getFormatNumber(bxh.nXH[0]) + " kinh nghiệm - cấp: " + bxh.nXH[1] + " ("+ Server.manager.NinjaS[(int)bxh.nXH[2]]+")\n";
                    }
                } else {
                    for (Entry2 bxh : Rank.bxhCaoThu) {
                        str = str + bxh.index + ". " + bxh.name + " ("+ bxh.nClass +") đã đạt cấp độ " + bxh.level + " vào lúc " + bxh.time + ".\n";
                    }
                }
                break;
            }
            case 2: {
                if (Rank.bangXH[type].isEmpty()) {
                    str = "Chưa có thông tin";
                    break;
                }
                for (Entry bxh : Rank.bangXH[type]) {
                    ClanManager clan = ClanManager.getClanName(bxh.name);
                    if (clan != null) {
                        str = str + bxh.index + ". Gia tộc " + bxh.name + " trình độ cấp " + bxh.nXH[0] + " do " + clan.getmain_name() + " làm tộc trưởng, thành viên " + clan.members.size() + "/" + clan.getMemMax() + "\n";
                    }
                    else {
                        str = str + bxh.index + ". Gia tộc " + bxh.name + " trình độ cấp " + bxh.nXH[0] + " đã bị giải tán\n";
                    }
                }
                break;
            }
            case 3: {
                if (Rank.bangXH[type].isEmpty()) {
                    str = "Chưa có thông tin";
                    break;
                }
                for (Entry bxh : Rank.bangXH[type]) {
                    str = str + bxh.index + ". " + bxh.name + " nhận được" + Util.getFormatNumber(bxh.nXH[0]) + " " + ItemTemplate.ItemTemplateId((int)bxh.nXH[1]).name + "\n";
                }
                break;
            }
        }
        return str;
    }

    public static class Entry
    {
        int index;
        String name;
        long[] nXH;
    }

    public static class Entry2
    {
        int index;
        String name;
        int level;
        String nClass;
        String time;
    }

    public static class Entry3
    {
        int index;
        String name;
        int point;
    }
    
    public static class Entry4
    {
        int index;
        String name;
        int point1;
    }
}
