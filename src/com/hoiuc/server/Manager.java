package com.hoiuc.server;

import com.hoiuc.assembly.*;
import com.hoiuc.assembly.Map;
import com.hoiuc.io.Message;
import com.hoiuc.io.SQLManager;
import com.hoiuc.io.Util;
import com.hoiuc.stream.Client;
import com.hoiuc.stream.Server;
import com.hoiuc.stream.thiendiabang.ThienDiaData;
import com.hoiuc.template.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import cache.ItemCache;
import cache.ItemOptionCache;
import cache.MapCache;
import cache.Part;
import cache.NpcCache;
import cache.MobCache;
import cache.PartImage;
import static com.hoiuc.server.Service.messageNotMap;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.*;

public class Manager {
    public static int baseWhiteListId = 0;
    public static HashMap<Integer, String> whiteList;
    public int os;
    public int post;
    public String host;
    public static String mysql_part;
    public static String backup_part;
    public static String mysql_host;
    public static int mysql_port;
    public static String mysql_database_data;
    public static String mysql_database_ninja;
    public static String mysql_user;
    public static String mysql_pass;
    public static int max_level_up;
    public static int up_exp;
    public static int hoursUpdate = 1;
    public static int nhanquatdb;
    public static boolean isClearSession = false;
    public static boolean isClearCloneLogin = false;
    public static boolean isSaveData = false;
    public static Alert alert = new Alert();
    public static byte vsData;
    public static byte vsMap;
    public static byte vsSkill;
    public static byte vsItem;
    private byte[][] tasks;
    private byte[][] maptasks;
    Lucky[] rotationluck;
    public byte event;
    public String[] NinjaS;
    public static ArrayList<NpcTemplate> npcs;
    public static SkillOptionTemplate[] sOptionTemplates;
    //public static NpcTemplate[] npcs;
    
    public static MapTemplate[] mapTemplates;
    public static ArrayList<Part> parts;
    public static MapCache[] mapCache;
    public static ItemOptionCache[] iOptionTemplates;
    public static ItemCache[] itemTemplates;
    public static int[] idMapLoad = new int[]{4, 5, 7, 8, 9, 11, 12, 13, 14, 15, 16, 18, 19, 24, 28, 29, 30, 31, 33, 34, 35, 36, 37, 39, 40, 41, 42, 46, 47, 48, 49, 50, 51, 52, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68};

    public Manager() {
         loadConfigFile();
        npcs = new ArrayList();
        this.rotationluck = new Lucky[2];
        this.event = 0;
        this.NinjaS = new String[]{"Chưa vào lớp", "Ninja Kiếm", "Ninja Phi Tiêu", "Ninja Kunai", "Ninja Cung", "Ninja Đao", "Ninja Quạt"};
        this.rotationluck[0] = new Lucky("Vòng xoay vip", (byte)0, (short)120, 1000000, 50000000, 1000000000);
        this.rotationluck[1] = new Lucky("Vòng xoay thường", (byte)1, (short)120, 10000, 100000, 500000000);
        this.rotationluck[0].start();
        this.rotationluck[1].start();
        LoadCache();
       
    }

    public static Map getMapid(int id) {
        if(Server.maps != null) {
            synchronized (Server.maps) {
                Map map;
                short i;
                for (i = 0; i < Server.maps.length; ++i) {
                    map = Server.maps[i];
                    if (map != null && map.id == id) {
                        return map;
                    }
                }
                return null;
            }
        }
        return null;
    }
    
    private void LoadCache() {
        SQLManager.create(this.mysql_host, this.mysql_port, this.mysql_database_data, this.mysql_user, this.mysql_pass);
        parts = new ArrayList<>();
        int i = 0;
        ResultSet res;
        System.out.println("Load Map TemPlate..");
            try {
                res = SQLManager.stat.executeQuery("SELECT * FROM `map`;");
                if (res.last()) {
                    mapCache = new MapCache[res.getRow()];
                    res.beforeFirst();
                }
                i = 0;
                while (res.next()) {
                    final MapCache mapTemplate = new MapCache();
                    mapTemplate.mapName = res.getString("name");
                    mapCache[i] = mapTemplate;
                    ++i;
                }
                res.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        System.out.println("Load Map NpcTemplate..");
            try {
                res = SQLManager.stat.executeQuery("SELECT * FROM `npc`;");
                if (res.last()) {
                    Npc.arrNpcTemplate = new NpcCache[res.getRow()];
                    res.beforeFirst();
                }
                i = 0;
                while (res.next()) {
                    final NpcCache npcTemplate = new NpcCache();
                    npcTemplate.name = res.getString("name");
                    npcTemplate.headId = res.getShort("headId");
                    npcTemplate.bodyId = res.getShort("bodyId");
                    npcTemplate.legId = res.getShort("legId");
                    final JSONArray jarr = (JSONArray)JSONValue.parse(res.getString("menu"));
                    npcTemplate.menu = new String[jarr.size()][];
                    for (int j = 0; j < npcTemplate.menu.length; ++j) {
                        final JSONArray jarr2 = (JSONArray)jarr.get(j);
                        npcTemplate.menu[j] = new String[jarr2.size()];
                        for (int k2 = 0; k2 < npcTemplate.menu[j].length; ++k2) {
                            npcTemplate.menu[j][k2] = jarr2.get(k2).toString();
                        }
                    }
                    Npc.arrNpcTemplate[i] = npcTemplate;
                    ++i;
                }
                res.close();
            }
            catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
            }
            System.out.println("Load Map MobTemplate..");
            try {
                res = SQLManager.stat.executeQuery("SELECT * FROM `Mob`;");
                if (res.last()) {
                    Mob.arrMobTemplate = new MobCache[res.getRow()];
                    res.beforeFirst();
                }
                i = 0;
                while (res.next()) {
                    final MobCache mobTemplate = new MobCache();
                    mobTemplate.type = res.getByte("type");
                    mobTemplate.name = res.getString("name");
                    mobTemplate.hp = res.getInt("hp");
                    mobTemplate.rangeMove = res.getByte("rangeMove");
                    mobTemplate.speed = res.getByte("speed");
                    Mob.arrMobTemplate[i] = mobTemplate;
                    ++i;
                }
                res.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        System.out.println("Load ItemOptionTemplate..");
            try {
                res = SQLManager.stat.executeQuery("SELECT * FROM `optionitem`;");
                if (res.last()) {
                    iOptionTemplates = new ItemOptionCache[res.getRow()];
                    res.beforeFirst();
                }
                i = 0;
                while (res.next()) {
                    final ItemOptionCache iotemplate = new ItemOptionCache();
                    iotemplate.id = res.getInt("id");
                    iotemplate.name = res.getString("name");
                    iotemplate.type = res.getByte("type");
                    iOptionTemplates[i] = iotemplate;
                    ++i;
                }
                res.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        System.out.println("Load ItemTemplate..");
            try {
                res = SQLManager.stat.executeQuery("SELECT * FROM `item`;");
                if (res.last()) {
                    itemTemplates = new ItemCache[res.getRow()];
                    res.beforeFirst();
                }
                i = 0;
                while (res.next()) {
                    final ItemCache itemTemplate = new ItemCache();
                    itemTemplate.id = res.getShort("id");
                    itemTemplate.type = res.getByte("type");
                    itemTemplate.gender = res.getByte("gender");
                    itemTemplate.name = res.getString("name");
                    itemTemplate.description = res.getString("description");
                    itemTemplate.level = res.getInt("level");
                    itemTemplate.iconID = res.getShort("iconID");
                    itemTemplate.part = res.getShort("part");
                    itemTemplate.isUpToUp = res.getBoolean("uptoup");
                    itemTemplates[i] = itemTemplate;
                    ++i;
                }
                res.close();
            }catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
         /*System.out.println("Load PartNew...");
             try {
                 res = SQLManager.stat.executeQuery("SELECT * FROM `nj_part`;");
                 while (res.next()) {
                 byte type = res.getByte("type");
                 JSONArray jA = (JSONArray) JSONValue.parse(res.getString("part"));
                 Part part = new Part(type);
                 for (int k = 0; k < part.pi.length; k++) {
                     JSONObject o = (JSONObject) jA.get(k);
                     part.pi[k] = new PartImage();
                     part.pi[k].id = ((Long) o.get("id")).shortValue();
                     part.pi[k].dx = ((Long) o.get("dx")).byteValue();
                     part.pi[k].dy = ((Long) o.get("dy")).byteValue();
                 }
                 parts.add(part);
                 }

             }
             catch (Exception e) {
                 e.printStackTrace();
                 System.exit(0);
             }*/
        //Service.createCachePart();
        Service.createCacheItem();
        Service.createCacheMap();
        SQLManager.close();
        SQLManager.create(this.mysql_host, this.mysql_port, this.mysql_database_data, this.mysql_user, this.mysql_pass);
        loadDataBase();
    }

    private void loadConfigFile() {
        byte[] ab = GameSrc.loadFile("ninja.conf").toByteArray();
        if (ab == null) {
            System.out.println("Config file not found!");
            System.exit(0);
        }

        String data = new String(ab);
        HashMap<String, String> configMap = new HashMap();
        StringBuilder sbd = new StringBuilder();
        boolean bo = false;

        for(int i = 0; i <= data.length(); ++i) {
            char es;
            if (i != data.length() && (es = data.charAt(i)) != '\n') {
                if (es == '#') {
                    bo = true;
                }

                if (!bo) {
                    sbd.append(es);
                }
            } else {
                bo = false;
                String sbf = sbd.toString().trim();
                if (sbf != null && !sbf.equals("") && sbf.charAt(0) != '#') {
                    int j = sbf.indexOf(58);
                    if (j > 0) {
                        String key = sbf.substring(0, j).trim();
                        String value = sbf.substring(j + 1).trim();
                        configMap.put(key, value);
                        System.out.println("config: " + key + "-" + value);
                    }
                }
                sbd.setLength(0);
            }
        }

        if (configMap.containsKey("debug")) {
            Util.setDebug(Boolean.parseBoolean((String)configMap.get("debug")));
        } else {
            Util.setDebug(false);
        }

        if (configMap.containsKey("os")) {
            this.os = Integer.parseInt(configMap.get("os"));
        } else {
            this.os = 1;
        }

        if (configMap.containsKey("host")) {
            this.host = (String)configMap.get("host");
        } else {
            this.host = "localhost";
        }

        if (configMap.containsKey("post")) {
            this.post = Integer.parseInt((String)configMap.get("post"));
        } else {
            this.post = 14444;
        }

        if (configMap.containsKey("mysql-part")) {
            this.mysql_part = (String)configMap.get("mysql-part");
        } else {
            this.mysql_part = "C:\\";
        }

        if (configMap.containsKey("backup-part")) {
            this.backup_part = (String)configMap.get("backup-part");
        } else {
            this.backup_part = "C:\\";
        }

        if (configMap.containsKey("mysql-host")) {
            this.mysql_host = (String)configMap.get("mysql-host");
        } else {
            this.mysql_host = "localhost";
        }

        if (configMap.containsKey("mysql-port")) {
            this.mysql_port = Integer.parseInt((String)configMap.get("mysql-port"));
        } else {
            this.mysql_port = 3306;
        }

        if (configMap.containsKey("mysql-user")) {
            this.mysql_user = (String)configMap.get("mysql-user");
        } else {
            this.mysql_user = "root";
        }

        if (configMap.containsKey("mysql-password")) {
            this.mysql_pass = (String)configMap.get("mysql-password");
        } else {
            this.mysql_pass = "";
        }

        if (configMap.containsKey("mysql-database_data")) {
            this.mysql_database_data = (String)configMap.get("mysql-database_data");
        } else {
            this.mysql_database_data = "nja_data";
        }

        if (configMap.containsKey("mysql-database_ninja")) {
            this.mysql_database_ninja = (String)configMap.get("mysql-database_ninja");
        } else {
            this.mysql_database_ninja = "nja_account";
        }

        if (configMap.containsKey("version-Data")) {
            this.vsData = Byte.parseByte((String)configMap.get("version-Data"));
        } else {
            this.vsData = 23;
        }

        if (configMap.containsKey("version-Map")) {
             vsMap = Byte.parseByte(String.valueOf(Util.nextInt(28,35)));
        } else {
            this.vsMap = 28;
        }

        if (configMap.containsKey("version-Skill")) {
            this.vsSkill = Byte.parseByte((String)configMap.get("version-Skill"));
        } else {
            this.vsSkill = 10;
        }

        if (configMap.containsKey("version-Item")) {
            vsItem = Byte.parseByte(String.valueOf(Util.nextInt(32,45)));
        } else {
            this.vsItem = 32;
        }

        if (configMap.containsKey("version-Event")) {
            event = Byte.parseByte(configMap.get("version-Event"));
        } else {
            event = 4;
        }

        if (configMap.containsKey("up-exp")) {
            up_exp = Integer.parseInt((String)configMap.get("up-exp"));
        } else {
            up_exp = 1;
        }

        if (configMap.containsKey("max-level-up")) {
            max_level_up = Integer.parseInt((String)configMap.get("max-level-up"));
        } else {
            max_level_up = 132;
        }
        if (configMap.containsKey("nhanquatdb")) {
            this.nhanquatdb = Byte.parseByte((String)configMap.get("nhanquatdb"));
        } else {
            this.nhanquatdb = 0;
        }
    }

    private void loadDataBase() {
        SQLManager.create(this.mysql_host, this.mysql_port, this.mysql_database_data, this.mysql_user, this.mysql_pass);
        int i = 0;

        try {
            ResultSet res = SQLManager.stat.executeQuery("SELECT * FROM `tasks`;");
            if (res.last()) {
                this.tasks = new byte[res.getRow()][];
                this.maptasks = new byte[this.tasks.length][];
                res.beforeFirst();
            }

            JSONArray Option;
            byte j;
            while(res.next()) {
                JSONArray jarr = (JSONArray) JSONValue.parse(res.getString("tasks"));
                Option = (JSONArray)JSONValue.parse(res.getString("maptasks"));
                this.tasks[i] = new byte[jarr.size()];
                this.maptasks[i] = new byte[this.tasks.length];

                for(j = 0; j < jarr.size(); ++j) {
                    this.tasks[i][j] = Byte.parseByte(jarr.get(j).toString());
                    this.maptasks[i][j] = Byte.parseByte(Option.get(j).toString());
                }

                ++i;
            }

            res.close();
            i = 0;

            for(res = SQLManager.stat.executeQuery("SELECT * FROM `level`;"); res.next(); ++i) {
                Level level = new Level();
                level.level = Integer.parseInt(res.getString("level"));
                level.exps = Long.parseLong(res.getString("exps"));
                level.ppoint = Short.parseShort(res.getString("ppoint"));
                level.spoint = Short.parseShort(res.getString("spoint"));
                Level.entrys.add(level);
            }

            res.close();
            i = 0;

            for(res = SQLManager.stat.executeQuery("SELECT * FROM `effect`;"); res.next(); ++i) {
                EffectTemplate eff = new EffectTemplate();
                eff.id = Byte.parseByte(res.getString("id"));
                eff.type = Byte.parseByte(res.getString("type"));
                eff.name = res.getString("name");
                eff.iconId = Short.parseShort(res.getString("iconId"));
                EffectTemplate.entrys.add(eff);
            }
            res.close();

            i = 0;
            int k;
            for(res = SQLManager.stat.executeQuery("SELECT * FROM `mob`;"); res.next(); ++i) {
                MobTemplate md = new MobTemplate();
                md.id = Integer.parseInt(res.getString("id"));
                md.type = Byte.parseByte(res.getString("type"));
                md.name = res.getString("name");
                md.hp = Integer.parseInt(res.getString("hp"));
                md.rangeMove = Byte.parseByte(res.getString("rangeMove"));
                md.speed = Byte.parseByte(res.getString("speed"));
                Option = (JSONArray)JSONValue.parse(res.getString("item"));
                md.arrIdItem = new short[Option.size()];

                for(k = 0; k < Option.size(); ++k) {
                    md.arrIdItem[k] = Short.parseShort(Option.get(k).toString());
                }

                MobTemplate.entrys.add(md);
            }
            res.close();

            

            i = 0;
            res = SQLManager.stat.executeQuery("SELECT * FROM `map`;");
            if (res.last()) {
                MapTemplate.arrTemplate = new MapTemplate[res.getRow()];
                res.beforeFirst();
            }

            while(res.next()) {
                MapTemplate temp = new MapTemplate();
                temp.id = res.getInt("id");
                temp.tileID = res.getByte("tile");
                temp.bgID = res.getByte("backid");
                temp.name = res.getString("name");
                temp.typeMap = res.getByte("type");
                temp.maxplayers = res.getByte("maxplayer");
                temp.numarea = res.getByte("numzone");
                temp.x0 = res.getShort("x0");
                temp.y0 = res.getShort("y0");
                Option = (JSONArray)JSONValue.parse(res.getString("Vgo"));
                temp.vgo = new Waypoint[Option.size()];

                JSONArray jar2;
                Waypoint vg;
                for(j = 0; j < Option.size(); ++j) {
                    temp.vgo[j] = new Waypoint();
                    jar2 = (JSONArray)JSONValue.parse(Option.get(j).toString());
                    vg = temp.vgo[j];
                    vg.minX = Short.parseShort(jar2.get(0).toString());
                    vg.minY = Short.parseShort(jar2.get(1).toString());
                    vg.maxX = Short.parseShort(jar2.get(2).toString());
                    vg.maxY = Short.parseShort(jar2.get(3).toString());
                    vg.mapid = Short.parseShort(jar2.get(4).toString());
                    vg.goX = Short.parseShort(jar2.get(5).toString());
                    vg.goY = Short.parseShort(jar2.get(6).toString());
                }

                Option = (JSONArray)JSONValue.parse(res.getString("Mob"));
                temp.arMobid = new short[Option.size()];
                temp.arrMobx = new short[Option.size()];
                temp.arrMoby = new short[Option.size()];
                temp.arrMobstatus = new byte[Option.size()];
                temp.arrMoblevel = new int[Option.size()];
                temp.arrLevelboss = new byte[Option.size()];
                temp.arrisboss = new boolean[Option.size()];

                short l;
                for(l = 0; l < Option.size(); ++l) {
                    jar2 = (JSONArray)Option.get(l);
                    temp.arMobid[l] = Short.parseShort(jar2.get(0).toString());
                    temp.arrMoblevel[l] = Integer.parseInt(jar2.get(1).toString());
                    temp.arrMobx[l] = Short.parseShort(jar2.get(2).toString());
                    temp.arrMoby[l] = Short.parseShort(jar2.get(3).toString());
                    temp.arrMobstatus[l] = Byte.parseByte(jar2.get(4).toString());
                    temp.arrLevelboss[l] = Byte.parseByte(jar2.get(5).toString());
                    temp.arrisboss[l] = Boolean.parseBoolean(jar2.get(6).toString());
                }

                Option = (JSONArray)JSONValue.parse(res.getString("NPC"));
                temp.npc = new Npc[Option.size()];

                for(j = 0; j < Option.size(); ++j) {
                    temp.npc[j] = new Npc();
                    jar2 = (JSONArray)JSONValue.parse(Option.get(j).toString());
                    Npc npc = temp.npc[j];
                    npc.type = Byte.parseByte(jar2.get(0).toString());
                    npc.x = Short.parseShort(jar2.get(1).toString());
                    npc.y = Short.parseShort(jar2.get(2).toString());
                    npc.id = Byte.parseByte(jar2.get(3).toString());
                }
                MapTemplate.arrTemplate[i] = temp;
                i++;
            }

            res.close();
            res = SQLManager.stat.executeQuery("SELECT * FROM `optionitem`;");

            while(res.next()) {
                ItemOptionTemplate item2 = new ItemOptionTemplate();
                item2.id = res.getInt("id");
                item2.name = res.getString("name");
                item2.type = res.getByte("type");
                ItemTemplate.put(item2.id, item2);
            }
            res.close();

            i = 0;
            JSONObject job;
            for(res = SQLManager.stat.executeQuery("SELECT * FROM `item`;"); res.next(); ++i) {
                ItemTemplate item = new ItemTemplate();
                item.id = Short.parseShort(res.getString("id"));
                item.type = Byte.parseByte(res.getString("type"));
                item.nclass = Byte.parseByte(res.getString("class"));
                item.skill = Byte.parseByte(res.getString("skill"));
                item.gender = Byte.parseByte(res.getString("gender"));
                item.name = res.getString("name");
                item.description = res.getString("description");
                item.level = Byte.parseByte(res.getString("level"));
                item.iconID = Short.parseShort(res.getString("iconID"));
                item.part = Short.parseShort(res.getString("part"));
                item.isUpToUp = Byte.parseByte(res.getString("uptoup")) == 1;
                item.isExpires = Byte.parseByte(res.getString("isExpires")) == 1;
                item.seconds_expires = Long.parseLong(res.getString("secondsExpires"));
                item.saleCoinLock = Integer.parseInt(res.getString("saleCoinLock"));
                item.itemoption = new ArrayList();
                Option = (JSONArray)JSONValue.parse(res.getString("ItemOption"));

                com.hoiuc.assembly.Option option;
                for(k = 0; k < Option.size(); ++k) {
                    job = (JSONObject)Option.get(k);
                    option = new Option(Integer.parseInt(job.get("id").toString()), Integer.parseInt(job.get("param").toString()));
                    item.itemoption.add(option);
                }

                item.option1 = new ArrayList();
                Option = (JSONArray)JSONValue.parse(res.getString("Option1"));

                for(k = 0; k < Option.size(); ++k) {
                    job = (JSONObject)Option.get(k);
                    option = new Option(Integer.parseInt(job.get("id").toString()), Integer.parseInt(job.get("param").toString()));
                    item.option1.add(option);
                }

                item.option2 = new ArrayList();
                Option = (JSONArray)JSONValue.parse(res.getString("Option2"));

                for(k = 0; k < Option.size(); ++k) {
                    job = (JSONObject)Option.get(k);
                    option = new Option(Integer.parseInt(job.get("id").toString()), Integer.parseInt(job.get("param").toString()));
                    item.option2.add(option);
                }

                item.option3 = new ArrayList();
                Option = (JSONArray)JSONValue.parse(res.getString("Option3"));

                for(k = 0; k < Option.size(); ++k) {
                    job = (JSONObject)Option.get(k);
                    option = new Option(Integer.parseInt(job.get("id").toString()), Integer.parseInt(job.get("param").toString()));
                    item.option3.add(option);
                }

                ItemTemplate.entrys.add(item);
            }
            res.close();

            i = 0;
            for(res = SQLManager.stat.executeQuery("SELECT * FROM `skill`;"); res.next(); ++i) {
                SkillTemplate skill = new SkillTemplate();
                skill.id = Short.parseShort(res.getString("id"));
                skill.nclass = Byte.parseByte(res.getString("class"));
                skill.name = res.getString("name");
                skill.maxPoint = Byte.parseByte(res.getString("maxPoint"));
                skill.type = Byte.parseByte(res.getString("type"));
                skill.iconId = Short.parseShort(res.getString("iconId"));
                skill.desc = res.getString("desc");
                Option = (JSONArray)JSONValue.parse(res.getString("SkillTemplates"));
                Iterator var33 = Option.iterator();

                while(var33.hasNext()) {
                    Object template = var33.next();
                    JSONObject job2 = (JSONObject)template;
                    SkillOptionTemplate temp2 = new SkillOptionTemplate();
                    temp2.skillId = Short.parseShort(job2.get("skillId").toString());
                    temp2.point = Byte.parseByte(job2.get("point").toString());
                    temp2.level = Integer.parseInt(job2.get("level").toString());
                    temp2.manaUse = Short.parseShort(job2.get("manaUse").toString());
                    temp2.coolDown = Integer.parseInt(job2.get("coolDown").toString());
                    temp2.dx = Short.parseShort(job2.get("dx").toString());
                    temp2.dy = Short.parseShort(job2.get("dy").toString());
                    temp2.maxFight = Byte.parseByte(job2.get("maxFight").toString());
                    JSONArray Option2 = (JSONArray)JSONValue.parse(job2.get("options").toString());
                    Iterator var12 = Option2.iterator();

                    while(var12.hasNext()) {
                        Object option2 = var12.next();
                        JSONObject job3 = (JSONObject)option2;
                        Option op = new Option(Integer.parseInt(job3.get("id").toString()), Integer.parseInt(job3.get("param").toString()));
                        temp2.options.add(op);
                    }

                    skill.templates.add(temp2);
                }

                SkillTemplate.entrys.add(skill);
            }
            res.close();

            i = 0;
            for(res = SQLManager.stat.executeQuery("SELECT * FROM `ItemSell`;"); res.next(); ++i) {
                ItemSell sell = new ItemSell();
                sell.id = Integer.parseInt(res.getString("id"));
                sell.type = Byte.parseByte(res.getString("type"));
                Option = (JSONArray)JSONValue.parse(res.getString("ListItem"));
                if (Option != null) {
                    sell.item = new Item[Option.size()];

                    for(j = 0; j < Option.size(); ++j) {
                        job = (JSONObject)Option.get(j);
                        Item item2 = ItemTemplate.parseItem(Option.get(j).toString());
                        item2.buyCoin = Integer.parseInt(job.get("buyCoin").toString());
                        item2.buyCoinLock = Integer.parseInt(job.get("buyCoinLock").toString());
                        item2.buyGold = Integer.parseInt(job.get("buyGold").toString());
                        sell.item[j] = item2;
                    }
                }
                ItemSell.entrys.add(sell);
            }
            res.close();
        } catch (Exception var14) {
            System.out.println("Error i:" + i);
            var14.printStackTrace();
            System.exit(0);
        }

        SQLManager.close();
        SQLManager.create(this.mysql_host, this.mysql_port, this.mysql_database_ninja, this.mysql_user, this.mysql_pass);
        this.loadGame();
    }

    private void loadGame() {
        try {
            ResultSet res = SQLManager.stat.executeQuery("SELECT * FROM `clan`;");
            while(res.next()) {
                ClanManager clan = new ClanManager();
                clan.id = Integer.parseInt(res.getString("id"));
                clan.name = res.getString("name");
                clan.exp = res.getInt("exp");
                clan.level = res.getInt("level");
                clan.itemLevel = res.getInt("itemLevel");
                clan.coin = res.getInt("coin");
                clan.reg_date = res.getString("reg_date");
                clan.log = res.getString("log");
                clan.alert = res.getString("alert");
                clan.use_card = res.getByte("use_card");
                clan.openDun = res.getByte("openDun");
                clan.debt = res.getByte("debt");
                JSONArray jar = (JSONArray)JSONValue.parse(res.getString("members"));
                if (jar != null) {
                    for(short j = 0; j < jar.size(); ++j) {
                        JSONArray jar2 = (JSONArray)jar.get(j);
                        ClanMember mem = new ClanMember();
                        mem.charID = Integer.parseInt(jar2.get(0).toString());
                        mem.cName = jar2.get(1).toString();
                        mem.clanName = jar2.get(2).toString();
                        mem.typeclan = Byte.parseByte(jar2.get(3).toString());
                        mem.clevel = Integer.parseInt(jar2.get(4).toString());
                        mem.nClass = Byte.parseByte(jar2.get(5).toString());
                        mem.pointClan = Integer.parseInt(jar2.get(6).toString());
                        mem.pointClanWeek = Integer.parseInt(jar2.get(7).toString());
                        clan.members.add(mem);
                    }
                }

                jar = (JSONArray)JSONValue.parse(res.getString("items"));
                if (jar != null) {
                    for(byte k = 0; k < jar.size(); ++k) {
                        clan.items.add(ItemTemplate.parseItem(jar.get(k).toString()));
                    }
                }
                clan.week = res.getString("week");
                ClanManager.entrys.add(clan);
            }
            res.close();

            res = SQLManager.stat.executeQuery("SELECT * FROM `alert`;");
            String alert;
            while(res.next()) {
                alert = res.getString("content");
                Manager.alert.setAlert(alert);
            }
            res.close();

            res = SQLManager.stat.executeQuery("SELECT * FROM `thiendia` ORDER BY `id` DESC LIMIT 2;");
            int count = 0;
            while (res.next()) {
                int id = Integer.parseInt(res.getString("id"));
                String week = res.getString("week");
                int type = Integer.parseInt(res.getString("type"));
                if(type == 1) {
                    ThienDiaBangManager.thienDiaBangManager[0] = new ThienDiaBangManager(id, week, type);
                } else if(type == 2) {
                    ThienDiaBangManager.thienDiaBangManager[1] = new ThienDiaBangManager(id, week, type);
                }
                JSONArray jar = (JSONArray)JSONValue.parse(res.getString("data"));
                if (jar != null) {
                    for(short j = 0; j < jar.size(); ++j) {
                        JSONArray jar2 = (JSONArray)jar.get(j);
                        String nameData = jar2.get(0).toString();
                        int typeData = Integer.parseInt(jar2.get(1).toString());
                        int rankData = Integer.parseInt(jar2.get(2).toString());
                        if(type == 1) {
                            ThienDiaBangManager.diaBangList.put(nameData, new ThienDiaData(nameData, rankData, typeData));
                        } else if(type == 2) {
                            ThienDiaBangManager.thienBangList.put(nameData, new ThienDiaData(nameData, rankData, typeData));
                        }
                    }
                }
                count++;
            }
            if(count == 0) {
                String week = Util.toDateString(Date.from(Instant.now()));
                SQLManager.stat.executeUpdate("INSERT INTO thiendia(`id`,`week`,`type`) VALUES (1,'" + week + "',1);");
                SQLManager.stat.executeUpdate("INSERT INTO thiendia(`id`,`week`,`type`) VALUES (2,'" + week + "',2);");
                ThienDiaBangManager.thienDiaBangManager[0] = new ThienDiaBangManager(1, week, 1);
                ThienDiaBangManager.thienDiaBangManager[1] = new ThienDiaBangManager(2, week, 2);
            }
            ThienDiaBangManager.rankDiaBang = ThienDiaBangManager.diaBangList.size() + 1;
            ThienDiaBangManager.rankThienBang = ThienDiaBangManager.thienBangList.size() + 1;
            res.close();

            res = SQLManager.stat.executeQuery("SELECT * FROM `shinwa`;");
            while(res.next()) {
                int id = Integer.parseInt(res.getString("id"));
                JSONArray jar = (JSONArray)JSONValue.parse(res.getString("data"));
                if (jar != null) {
                    List<ShinwaTemplate> list = new ArrayList<>();
                    for(short j = 0; j < jar.size(); ++j) {
                        JSONArray jar2 = (JSONArray)jar.get(j);
                        Item item = ItemTemplate.parseItem(jar2.get(0).toString());
                        long timeStart = Long.parseLong(jar2.get(1).toString());
                        String seller = jar2.get(2).toString();
                        long price = Long.parseLong(jar2.get(3).toString());
                        list.add(new ShinwaTemplate(item, timeStart, seller, price));
                    }
                    ShinwaManager.entrys.put(id, list);
                }
            }
            res.close();

            SQLManager.stat.executeUpdate("UPDATE `ninja` SET `caveID`=-1;");
        } catch (Exception var8) {
            var8.printStackTrace();
            System.exit(0);
        }

    }

    public static void getPackMessage(Player p) throws IOException {
        Message msg = null;
        try {
            msg = messageNotMap((byte)(-123));
            msg.writer().writeByte(Manager.vsData);
            msg.writer().writeByte(Manager.vsMap);
            msg.writer().writeByte(Manager.vsSkill);
            msg.writer().writeByte(Manager.vsItem);
            final byte[] ab = GameSrc.loadFile("cache/request").toByteArray();
            msg.writer().write(ab);
            p.conn.sendMessage(msg);
            msg.cleanup();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public void sendData(Player p) {
        Message m = null;
        try {
            m = new Message(-28);
            m.writer().writeByte(-122);
            m.writer().writeByte(this.vsData);
            byte[] ab = GameSrc.loadFile("res/cache/data/nj_arrow").toByteArray();
            m.writer().writeInt(ab.length);
            m.writer().write(ab);
            ab = GameSrc.loadFile("res/cache/data/nj_effect").toByteArray();
            m.writer().writeInt(ab.length);
            m.writer().write(ab);
            ab = GameSrc.loadFile("res/cache/data/nj_image").toByteArray();
            m.writer().writeInt(ab.length);
            m.writer().write(ab);
            ab = GameSrc.loadFile("res/cache/data/nj_part").toByteArray();
            m.writer().writeInt(ab.length);
            m.writer().write(ab);
            ab = GameSrc.loadFile("res/cache/data/nj_skill").toByteArray();
            m.writer().writeInt(ab.length);
            m.writer().write(ab);
            m.writer().writeByte(this.tasks.length);
            byte i;
            for(i = 0; i < this.tasks.length; ++i) {
                m.writer().writeByte(this.tasks[i].length);
                for(byte j = 0; j < this.tasks[i].length; ++j) {
                    m.writer().writeByte(this.tasks[i][j]);
                    m.writer().writeByte(this.maptasks[i][j]);
                }
            }
            m.writer().writeByte(Level.entrys.size());
            Iterator var6 = Level.entrys.iterator();
            Level entry;
            while(var6.hasNext()) {
                entry = (Level)var6.next();
                m.writer().writeLong(entry.exps);
            }
            m.writer().writeByte(GameSrc.crystals.length);
            for(i = 0; i < GameSrc.crystals.length; ++i) {
                m.writer().writeInt(GameSrc.crystals[i]);
            }
            m.writer().writeByte(GameSrc.upClothe.length);
            for(i = 0; i < GameSrc.upClothe.length; ++i) {
                m.writer().writeInt(GameSrc.upClothe[i]);
            }
            m.writer().writeByte(GameSrc.upAdorn.length);
            for(i = 0; i < GameSrc.upAdorn.length; ++i) {
                m.writer().writeInt(GameSrc.upAdorn[i]);
            }
            m.writer().writeByte(GameSrc.upWeapon.length);
            for(i = 0; i < GameSrc.upWeapon.length; ++i) {
                m.writer().writeInt(GameSrc.upWeapon[i]);
            }
            m.writer().writeByte(GameSrc.coinUpCrystals.length);
            for(i = 0; i < GameSrc.coinUpCrystals.length; ++i) {
                m.writer().writeInt(GameSrc.coinUpCrystals[i]);
            }
            m.writer().writeByte(GameSrc.coinUpClothes.length);
            for(i = 0; i < GameSrc.coinUpClothes.length; ++i) {
                m.writer().writeInt(GameSrc.coinUpClothes[i]);
            }
            m.writer().writeByte(GameSrc.coinUpAdorns.length);
            for(i = 0; i < GameSrc.coinUpAdorns.length; ++i) {
                m.writer().writeInt(GameSrc.coinUpAdorns[i]);
            }
            m.writer().writeByte(GameSrc.coinUpWeapons.length);
            for(i = 0; i < GameSrc.coinUpWeapons.length; ++i) {
                m.writer().writeInt(GameSrc.coinUpWeapons[i]);
            }
            m.writer().writeByte(GameSrc.goldUps.length);
            for(i = 0; i < GameSrc.goldUps.length; ++i) {
                m.writer().writeInt(GameSrc.goldUps[i]);
            }
            m.writer().writeByte(GameSrc.maxPercents.length);
            for(i = 0; i < GameSrc.maxPercents.length; ++i) {
                m.writer().writeInt(GameSrc.maxPercents[i]);
            }
            m.writer().writeByte(EffectTemplate.entrys.size());
            for(i = 0; i < EffectTemplate.entrys.size(); ++i) {
                m.writer().writeByte((EffectTemplate.entrys.get(i)).id);
                m.writer().writeByte((EffectTemplate.entrys.get(i)).type);
                m.writer().writeUTF((EffectTemplate.entrys.get(i)).name);
                m.writer().writeShort((EffectTemplate.entrys.get(i)).iconId);
            }
            m.writer().flush();
            p.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void chatKTG(String chat) {
        Message m = null;
        try {
            m = new Message(-25);
            m.writer().writeUTF(chat);
            m.writer().flush();
            Client.gI().NinjaMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m !=null) {
                m.cleanup();
            }
        }

    }

    public void Infochat(String chat) {
        Message m = null;
        try {
            m = new Message(-24);
            m.writer().writeUTF(chat);
            m.writer().flush();
            Client.gI().NinjaMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    protected void stop() {
    }

    protected void chatKTG(Player p, Message m) {
        try {
            String chat = m.reader().readUTF();
            m.cleanup();
            if (p.chatKTGdelay > System.currentTimeMillis()) {
                p.conn.sendMessageLog("Chờ sau " + (p.chatKTGdelay - System.currentTimeMillis()) / 1000L + "s.");
            } else {
                p.chatKTGdelay = System.currentTimeMillis() + 5000L;
                if (p.luong < 10) {
                    p.conn.sendMessageLog("Bạn không đủ 10 lượng trên người.");
                } else {
                    p.luongMessage(-10L);
                    serverChat(p.c.name, chat);
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

    public static void serverChat(String name, String s) {
        Message m = null;
        try {
            m = new Message(-21);
            m.writer().writeUTF(name);
            m.writer().writeUTF(s);
            m.writer().flush();
            Client.gI().NinjaMessage(m);

        } catch (Exception var3) {
            var3.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void sendTB(Player p, String title, String s) {
        Message m = null;
        try {
            m = new Message(53);
            m.writer().writeUTF(title);
            m.writer().writeUTF(s);
            m.writer().flush();
            p.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void close() {
        for (int i = 0; i < this.rotationluck.length; ++i) {
            this.rotationluck[i].close();
            this.rotationluck[i] = null;
        }
        this.rotationluck = null;
        for (int i = 0; i < Server.maps.length; ++i) {
            Server.maps[i].close();
            Server.maps[i] = null;
        }
        Server.maps = null;
        ClanManager.close();
    }

   public void sendSkill(Player p) {
        Message m = null;
        try {
            m = new Message(-28);
            m.writer().writeByte(-120);
            m.writer().write(Server.cache[2].toByteArray());
            m.writer().flush();
            p.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void sendItem(Player p) {
        Message m = null;
        try {
            m = new Message(-28);
            m.writer().writeByte(-119);
            m.writer().write(Server.cache[3].toByteArray());
            m.writer().flush();
            p.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }
}
