package com.hoiuc.assembly;

import com.hoiuc.io.Message;
import com.hoiuc.io.SQLManager;
import com.hoiuc.io.Util;
import com.hoiuc.server.Manager;
import com.hoiuc.stream.Client;
import com.hoiuc.stream.Server;
import com.hoiuc.stream.thiendiabang.ThienDiaBangTileMap;
import com.hoiuc.template.ItemTemplate;
import com.hoiuc.template.SkillTemplate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Char extends Body {
    public Player p = null;
    public TileMap tileMap = null;
    public ThienDiaBangTileMap tdbTileMap = null;
    public String name = null;
    public ClanMember clan = null;
    public boolean isBot = false;
    public byte taskId = 0;
    public byte gender = -1;
    public int xu = 0;
    public int xuBox = 0;
    public int yen = 0;
    public byte maxluggage = 30;
    public byte levelBag = 0;
    public Item[] ItemShinwa = null;
    public Item[] ItemBag = null;
    public Item[] ItemBox = null;
    public Item[] ItemBST = null;
    public Item[] ItemCaiTrang = null;
    public String friend = "[]";
    public int mapType = 0;
    public int mapLTD = 22;
    public int mapid = 22;
    public int mapKanata = 22;
    public int mobAtk = -1;
    public long eff5buff = 0L;
    public byte type = 0;
    public boolean isTrade = false;
    public int rqTradeId;
    public int tradeId;
    public int tradeCoin = 0;
    public long tradeDelay = 0L;
    public byte tradeLock = -1;
    public ArrayList<Byte> tradeIdItem = new ArrayList();
    public byte denbu = 0;
    public Date newlogin = null;
    public boolean ddClan = false;
    public int caveID = -1;
    public int nCave = 1;
    public String sTimeCave = null;
    public int pointCave = 0;
    public int useCave = 2;
    public int bagCaveMax = 0;
    public short itemIDCaveMax = -1;
    public int requestclan = -1;
    public long deleyRequestClan = 0L;
    public long delayEffect = 0L;
    public long timeRemoveClone = -1L;
    public long timeRemoveCloneSave = 0L;
    public int typemenu;
    public int saveBXH = 1;
    public CloneCharacter clone = null;
    public long timeKickSession = System.currentTimeMillis() + 350000L;
    public long expSkillClone = 0L;
    public int isDiemDanh = 0;
    public int isQuaHangDong = 0;
    public int countHangDong = 0;
    public int[] checkLevel = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
    public int testCharID = -9999;
    public boolean isTest = false;
    public int KillCharId = -9999;
    public boolean isCuuSat = false;
    public int isHangDong6x = 0;
    public int useTaThuLenh = 1;
    public int useDanhVongPhu = 6;
    public int isTaskHangNgay = 0;
    public int isTaskTaThu = 0;
    public int isTaskDanhVong = 0;
    public int[] taskHangNgay = new int[]{-1, -1, -1, -1, -1, 0, 0};
    public int[] taskTaThu = new int[]{-1, -1, -1, -1, -1, 0, 0};
    public int[] taskDanhVong = new int[]{-1, -1, -1, 0, 20};
    public int pointUydanh = 0;
    public int pointNon = 0;
    public int pointVukhi = 0;
    public int pointAo = 0;
    public int pointLien = 0;
    public int pointGangtay = 0;
    public int pointNhan = 0;
    public int pointQuan = 0;
    public int pointNgocboi = 0;
    public int pointGiay = 0;
    public int pointPhu = 0;
    public int countTaskHangNgay = 0;
    public int countTaskTaThu = 0;
    public int countTaskDanhVong = 20;

    public int pointTinhTu = 0;

    public int countBuyX3 = 6;
    public BuNhin buNhin = null;
    public int dunId = -1;
    public boolean isInDun = false;
    public boolean isSkill25Kiem = false;
    public boolean isSkill25Dao = false;

    public int pheCT = -1;
    public int pointCT = 0;
    public int isTakePoint = 0;

    public int gtcId = -1;
    public int pointGTC = 0;

    public int isNhanQuaNoel = 1;
    public int pointNoel = 0;
    public int pointBossTL = 0;

    public long yenTN = 0;
    public long xuTN = 0;
    public long luongTN = 0;
    public long expTN = 0;

    public int ldgtID = -1;
    public int tempLdgtID = -1;

    public int countTDB = 1;
    public int rankTDB = 0;
    public int isGiftTDB = 0;
    public long delayJoinTDB = -1L;
    public int countWin = 0;
    public int pointBossChuot = 0;
    public int nhanTP = 0; //nhan diem Trang Phao
    public int countPhao = 0;

    //public HashMap<Short, Skill> idSkillBot = new HashMap<>();
    public ArrayList<Short> idSkillBot = new ArrayList<>();

    public Char() {
        this.seNinja(this);
    }

    public void setTimeKickSession() {
        this.timeKickSession = System.currentTimeMillis() + 350000L;
    }

    public Body get() {
        Body b = this;
        if (this.isNhanban) {
            b = this.clone;
        }
        return (Body)b;
    }

    public byte getBagNull() {
        byte num = 0;
        byte i;
        for(i = 0; i < this.ItemBag.length; ++i) {
            if (this.ItemBag[i] == null) {
                ++num;
            }
        }
        return num;
    }

    public int getPointDanhVong(int type) {
        switch(type) {
            case 0:
                return this.pointNon;
            case 1:
                return this.pointVukhi;
            case 2:
                return this.pointAo;
            case 3:
                return this.pointLien;
            case 4:
                return this.pointGangtay;
            case 5:
                return this.pointNhan;
            case 6:
                return this.pointQuan;
            case 7:
                return this.pointNgocboi;
            case 8:
                return this.pointGiay;
            case 9:
                return this.pointPhu;
            default:
                return 0;
        }
    }

    public boolean avgPointDanhVong(int point) {
        int avg = (this.pointNon + this.pointVukhi + this.pointAo + this.pointLien + this.pointGangtay + this.pointNhan + this.pointQuan + this.pointNgocboi + this.pointGiay + this.pointPhu) / 10;
        return point > avg;
    }

    public void plusPointDanhVong(int type, int point) {
        switch(type) {
            case 0:
                this.pointNon += point;
                break;
            case 1:
                this.pointVukhi += point;
                break;
            case 2:
                this.pointAo += point;
                break;
            case 3:
                this.pointLien += point;
                break;
            case 4:
                this.pointGangtay += point;
                break;
            case 5:
                this.pointNhan += point;
                break;
            case 6:
                this.pointQuan += point;
                break;
            case 7:
                this.pointNgocboi += point;
                break;
            case 8:
                this.pointGiay += point;
                break;
            case 9:
                this.pointPhu += point;
        }

    }

    public boolean checkPointDanhVong(int type) {
        return this.pointNon >= 100 * type && this.pointAo >= 100 * type && this.pointGiay >= 100 * type && this.pointGangtay >= 100 * type && this.pointLien >= 100 * type && this.pointNgocboi >= 100 * type && this.pointNhan >= 100 * type && this.pointPhu >= 100 * type && this.pointQuan >= 100 * type && this.pointVukhi >= 100 * type;
    }

    public byte getBoxNull() {
        byte num = 0;
        byte i;
        for(i = 0; i < this.ItemBox.length; ++i) {
            if (this.ItemBox[i] == null) {
                ++num;
            }
        }
        return num;
    }

    public Item getIndexBag(int index) {
        return index < this.ItemBag.length && index >= 0 ? this.ItemBag[index] : null;
    }

    public Item getIndexBox(int index) {
        return index < this.ItemBox.length && index >= 0 ? this.ItemBox[index] : null;
    }

    public Item getIndexBST(int index) {
        return index < this.ItemBST.length && index >= 0 ? this.ItemBST[index] : null;
    }

    public Item getIndexCaiTrang(int index) {
        return index < this.ItemCaiTrang.length && index >= 0 ? this.ItemCaiTrang[index] : null;
    }

    public int quantityItemyTotal(int id) {
        int quantity = 0;
        byte i;
        Item item;
        for(i = 0; i < this.ItemBag.length; ++i) {
            item = this.ItemBag[i];
            if (item != null && item.id == id) {
                quantity += item.quantity;
            }
        }

        return quantity;
    }

    public Item getItemIdBag(int id) {
        byte i;
        Item item;
        for(i = 0; i < this.ItemBag.length; ++i) {
            item = this.ItemBag[i];
            if (item != null && item.id == id) {
                return item;
            }
        }

        return null;
    }

    public byte getIndexBagid(int id, boolean lock) {
        byte i;
        Item item;
        for(i = 0; i < this.ItemBag.length; ++i) {
            item = this.ItemBag[i];
            if (item != null && item.id == id && item.isLock == lock) {
                return i;
            }
        }

        return -1;
    }

    public byte getIndexBoxid(int id, boolean lock) {
        byte i;
        Item item;
        for(i = 0; i < this.ItemBox.length; ++i) {
            item = this.ItemBox[i];
            if (item != null && item.id == id && item.isLock == lock) {
                return i;
            }
        }

        return -1;
    }

    protected byte getIndexBagItem(int id, boolean lock) {
        byte i;
        Item item;
        for(i = 0; i < this.ItemBag.length; ++i) {
            item = this.ItemBag[i];
            if (item != null && item.id == id && item.isLock == lock) {
                return i;
            }
        }

        return -1;
    }

    public byte getIndexBagNotItem() {
        byte i;
        Item item;
        for(i = 0; i < this.ItemBag.length; ++i) {
            item = this.ItemBag[i];
            if (item == null) {
                return i;
            }
        }

        return -1;
    }

    public byte getIndexBoxNotItem() {
        byte i;
        Item item;
        for(i = 0; i < this.ItemBox.length; ++i) {
            item = this.ItemBox[i];
            if (item == null) {
                return i;
            }
        }

        return -1;
    }

    public void setXPLoadSkill(long exp) {
        Message m = null;
        try {
            this.get().exp = exp;
            m = new Message(-30);
            m.writer().writeByte(-124);
            m.writer().writeByte(this.get().speed);
            m.writer().writeInt(this.get().getMaxHP());
            m.writer().writeInt(this.get().getMaxMP());
            m.writer().writeLong(this.get().exp);
            m.writer().writeShort(this.get().spoint);
            m.writer().writeShort(this.get().ppoint);
            m.writer().writeShort(this.get().potential0);
            m.writer().writeShort(this.get().potential1);
            m.writer().writeInt(this.get().potential2);
            m.writer().writeInt(this.get().potential3);
            m.writer().flush();
            this.p.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public boolean addItemBag(Boolean uptoup, Item itemup) {
        try {
            byte index = this.getIndexBagid(itemup.id, itemup.isLock);
            if (uptoup && !itemup.isExpires && ItemTemplate.ItemTemplateId(itemup.id).isUpToUp && index != -1) {
                Item item = this.ItemBag[index];
                item.quantity += itemup.quantity;
                Message message = new Message(9);
                message.writer().writeByte(index);
                message.writer().writeShort(itemup.quantity);
                message.writer().flush();
                this.p.conn.sendMessage(message);
                message.cleanup();
                return true;
            } else {
                index = this.getIndexBagNotItem();
                if (index == -1) {
                    this.p.conn.sendMessageLog("Hành trang không đủ chỗ trống!");
                    return false;
                } else {
                    this.ItemBag[index] = itemup;
                    Message m = new Message(8);
                    m.writer().writeByte(index);
                    m.writer().writeShort(itemup.id);
                    m.writer().writeBoolean(itemup.isLock);
                    if (ItemTemplate.isTypeBody(itemup.id) || ItemTemplate.isTypeNgocKham(itemup.id)) {
                        m.writer().writeByte(itemup.upgrade);
                    }

                    m.writer().writeBoolean(itemup.isExpires);
                    m.writer().writeShort(itemup.quantity);
                    m.writer().flush();
                    this.p.conn.sendMessage(m);
                    m.cleanup();
                    return true;
                }
            }
        } catch (IOException var6) {
            var6.printStackTrace();
            return false;
        }
    }

    public void removeItemBags(int id, int quantity) {
        int num = 0;

        Item item;
        byte i;
        for(i = 0; i < this.ItemBag.length; ++i) {
            item = this.ItemBag[i];
            if (item != null && item.id == id) {
                if (num + item.quantity >= quantity) {
                    this.removeItemBag(i, quantity - num);
                    break;
                }

                num += item.quantity;
                this.removeItemBag(i, item.quantity);
            }
        }
    }

    public synchronized void removeItemBag(byte index, int quantity) {
        Item item = this.getIndexBag(index);
        if (item != null) {
            try {
                item.quantity -= quantity;
                Message m = new Message(18);
                m.writer().writeByte(index);
                m.writer().writeShort(quantity);
                m.writer().flush();
                this.p.conn.sendMessage(m);
                m.cleanup();
                if (item.quantity <= 0) {
                    this.ItemBag[index] = null;
                }
            } catch (IOException var5) {
                var5.printStackTrace();
                item = null;
            }
        }
    }

    public synchronized void removeItemBag(byte index) {
        Item item = this.getIndexBag(index);
        if (item != null) {
            try {
                Message m = new Message(18);
                m.writer().writeByte(index);
                m.writer().writeShort(item.quantity);
                m.writer().flush();
                this.p.conn.sendMessage(m);
                m.cleanup();
                this.ItemBag[index] = null;
            } catch (IOException var4) {
                var4.printStackTrace();
                item = null;
                return;
            }
        }
    }

    public void removeItemBody(byte index) {
        Message m = null;
        try {
            this.get().ItemBody[index] = null;
            if (index == 10) {
                this.p.mobMeMessage(0, (byte)0);
            }
            m = new Message(-30);
            m.writer().writeByte(-80);
            m.writer().writeByte(index);
            m.writer().flush();
            this.p.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace( );
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void removeItemBox(byte index) {
        Message m = null;
        this.ItemBox[index] = null;
        try {
            m = new Message(-30);
            m.writer().writeByte(-75);
            m.writer().writeByte(index);
            m.writer().flush();
            this.p.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public synchronized int upxu(long x) {
        long xunew = (long)this.xu + x;
        if (xunew > 2000000000L) {
            x = (long)(2000000000 - this.xu);
        } else if (xunew < -2000000000L) {
            x = (long)(-2000000000 - this.xu);
        }

        this.xu += (int)x;
        return (int)x;
    }

    public synchronized int upyen(long x) {
        long yennew = (long)this.yen + x;
        if (yennew > 2000000000L) {
            x = (long)(2000000000 - this.yen);
        } else if (yennew < -2000000000L) {
            x = (long)(-2000000000 - this.yen);
        }

        this.yen += (int)x;
        return (int)x;
    }

    public void upxuMessage(long x) {
        try {
            Message m = new Message(95);
            m.writer().writeInt(this.upxu(x));
            m.writer().flush();
            this.p.conn.sendMessage(m);
            m.cleanup();
        } catch (IOException var4) {
            var4.printStackTrace();
        }
    }

    public void upyenMessage(long x) {
        try {
            Message m = new Message(-8);
            m.writer().writeInt(this.upyen(x));
            m.writer().flush();
            this.p.conn.sendMessage(m);
            m.cleanup();
        } catch (IOException var4) {
            var4.printStackTrace();
        }
    }

    public static Char setup(Player p, String name) {
        try {
            synchronized(Server.LOCK_MYSQL) {
                ResultSet red = SQLManager.stat.executeQuery("SELECT * FROM `ninja` WHERE `name`LIKE'" + name + "';");
                if (red != null && red.first()) {
                    Char nja = new Char();
                    nja.p = p;
                    nja.id = red.getInt("id");
                    nja.name = red.getString("name");
                    nja.gender = red.getByte("gender");
                    nja.head = red.getByte("head");
                    nja.caiTrang = red.getByte("caiTrang");
                    nja.speed = red.getByte("speed");
                    nja.nclass = red.getByte("class");
                    nja.ppoint = red.getShort("ppoint");
                    nja.potential0 = red.getShort("potential0");
                    nja.potential1 = red.getShort("potential1");
                    nja.potential2 = red.getInt("potential2");
                    nja.potential3 = red.getInt("potential3");
                    nja.spoint = red.getShort("spoint");
                    nja.countBuyX3 = red.getInt("buyX3");
                    nja.pointBossTL = red.getInt("pointBossTL");
                    nja.ldgtID = red.getInt("ldgtID");
                    nja.isHangDong6x = red.getInt("isHangDong6x");
                    nja.timeRemoveCloneSave = red.getLong("timeRemoveClone");
                    nja.rankTDB = red.getInt("rankTDB");
                    nja.isGiftTDB = red.getInt("isGiftTDB");
                    nja.countTDB = red.getInt("countTDB");
                    nja.countWin = red.getInt("countWin");
                    nja.nhanTP = red.getInt("nhanTP");
                    nja.pointBossChuot = red.getInt("pointBossChuot");
                    nja.countPhao = red.getInt("countPhao");
                    JSONArray jar = (JSONArray)JSONValue.parse(red.getString("skill"));
                    JSONObject job;
                    Skill skill;
                    byte index;
                    if (jar != null) {
                        job = null;
                        for(index = 0; index < jar.size(); ++index) {
                            job = (JSONObject)jar.get(index);
                            skill = new Skill();
                            skill.id = Byte.parseByte(job.get("id").toString());
                            skill.point = Byte.parseByte(job.get("point").toString());
                            nja.skill.add(skill);
                            job.clear();
                        }
                    }

                    JSONArray jarr2 = (JSONArray)JSONValue.parse(red.getString("KSkill"));
                    nja.KSkill = new byte[jarr2.size()];
                    byte j;
                    for(j = 0; j < nja.KSkill.length; ++j) {
                        nja.KSkill[j] = Byte.parseByte(jarr2.get(j).toString());
                    }

                    jarr2 = (JSONArray)JSONValue.parse(red.getString("OSkill"));
                    nja.OSkill = new byte[jarr2.size()];
                    for(j = 0; j < nja.OSkill.length; ++j) {
                        nja.OSkill[j] = Byte.parseByte(jarr2.get(j).toString());
                    }

                    jarr2 = (JSONArray)JSONValue.parseWithException(red.getString("friends"));
                    short s;
                    JSONArray jarr3;
                    Friend friend;
                    for (s = 0; s < jarr2.size(); s = (short)(s + 1)) {
                        jarr3 = (JSONArray)jarr2.get(s);
                        friend = new Friend(jarr3.get(0).toString(), Byte.parseByte(jarr3.get(1).toString()));
                        nja.vFriend.addElement(friend);
                    }

                    nja.CSkill = (short)Byte.parseByte(red.getString("CSkill"));
                    nja.level = red.getShort("level");
                    nja.saveBXH = red.getShort("saveBXH");
                    nja.exp = red.getLong("exp");
                    nja.expdown = red.getLong("expdown");
                    nja.expSkillClone = red.getLong("expSkillClone");
                    nja.pk = red.getByte("pk");
                    nja.xu = red.getInt("xu");
                    nja.xuBox = red.getInt("xuBox");
                    nja.yen = red.getInt("yen");
                    nja.maxluggage = red.getByte("maxluggage");
                    nja.levelBag = red.getByte("levelBag");
                    JSONObject job2 = null;
                    nja.ItemBag = new Item[nja.maxluggage];
                    jar = (JSONArray)JSONValue.parse(red.getString("ItemBag"));

                    if (jar != null) {
                        for(j = 0; j < jar.size(); ++j) {
                            job2 = (JSONObject)jar.get(j);
                            index = Byte.parseByte(job2.get("index").toString());
                            nja.ItemBag[index] = ItemTemplate.parseItem(jar.get(j).toString());
                            job2.clear();
                        }
                    }

                    nja.ItemBox = new Item[30];
                    jar = (JSONArray)JSONValue.parse(red.getString("ItemBox"));
                    if (jar != null) {
                        for(j = 0; j < jar.size(); ++j) {
                            job2 = (JSONObject)jar.get(j);
                            index = Byte.parseByte(job2.get("index").toString());
                            nja.ItemBox[index] = ItemTemplate.parseItem(jar.get(j).toString());
                            job2.clear();
                        }
                    }

                    nja.ItemBST = new Item[9];
                    jar = (JSONArray)JSONValue.parse(red.getString("ItemBST"));
                    if (jar != null) {
                        for(j = 0; j < jar.size(); ++j) {
                            job2 = (JSONObject)jar.get(j);
                            index = Byte.parseByte(job2.get("index").toString());
                            nja.ItemBST[index] = ItemTemplate.parseItem(jar.get(j).toString());
                            job2.clear();
                        }
                    }

                    nja.ItemCaiTrang = new Item[18];
                    jar = (JSONArray)JSONValue.parse(red.getString("ItemCaiTrang"));
                    if (jar != null) {
                        for(j = 0; j < jar.size(); ++j) {
                            job2 = (JSONObject)jar.get(j);
                            index = Byte.parseByte(job2.get("index").toString());
                            nja.ItemCaiTrang[index] = ItemTemplate.parseItem(jar.get(j).toString());
                            job2.clear();
                        }
                    }

                    nja.get().ItemBody = new Item[32];
                    jar = (JSONArray)JSONValue.parse(red.getString("ItemBody"));
                    if (jar != null) {
                        for(j = 0; j < jar.size(); ++j) {
                            job2 = (JSONObject)jar.get(j);
                            index = Byte.parseByte(job2.get("index").toString());
                            nja.get().ItemBody[index] = ItemTemplate.parseItem(jar.get(j).toString());
                            job2.clear();
                        }
                    }

                    nja.ItemMounts = new Item[5];
                    jar = (JSONArray)JSONValue.parse(red.getString("ItemMounts"));
                    if (jar != null) {
                        for(j = 0; j < jar.size(); ++j) {
                            job2 = (JSONObject)jar.get(j);
                            index = Byte.parseByte(job2.get("index").toString());
                            nja.ItemMounts[index] = ItemTemplate.parseItem(jar.get(j).toString());
                            job2.clear();
                        }
                    }

                    jar = (JSONArray)JSONValue.parse(red.getString("site"));
                    nja.mapid = Util.UnsignedByte((byte)Integer.parseInt(jar.get(0).toString()));
                    nja.x = Short.parseShort(jar.get(1).toString());
                    nja.y = Short.parseShort(jar.get(2).toString());
                    nja.mapLTD = Short.parseShort(jar.get(3).toString());
                    Map ma = Manager.getMapid(nja.mapLTD);
                    if(ma.getXHD() != -1 || ma.LangCo() || ma.mapBossTuanLoc() || ma.mapLDGT() || ma.mapGTC() || ma.id == 111 || ma.id == 113) {
                        nja.mapLTD = 22;
                    }

                    jar = (JSONArray)JSONValue.parse(red.getString("effect"));
                    JSONArray jar2;
                    for(j = 0; j < jar.size(); ++j) {
                        jar2 = (JSONArray)jar.get(j);
                        if (jar2 != null) {
                            int effid = Integer.parseInt(jar2.get(0).toString());
                            byte efftype = Byte.parseByte(jar2.get(1).toString());
                            long efftime = Long.parseLong(jar2.get(2).toString());
                            int param = Integer.parseInt(jar2.get(3).toString());
                            Effect eff = new Effect(effid, param);
                            eff.timeStart = 0;
                            if (effid == 36 || effid == 42 || effid == 37 || effid == 38 || effid == 39) {
                                efftime -= System.currentTimeMillis();
                            }
                            eff.timeRemove = efftime;
                            eff.timeLength = (int)(eff.timeRemove - System.currentTimeMillis());
                            eff = new Effect(effid, 0, (int)efftime, param);
                            nja.veff.add(eff);
                        }
                        jar2.clear();
                    }

                    jar = (JSONArray)JSONValue.parse(red.getString("info"));
                    nja.isDiemDanh = Integer.parseInt(jar.get(0).toString());
                    nja.countHangDong = Integer.parseInt(jar.get(1).toString());
                    jarr2 = (JSONArray)JSONValue.parse(jar.get(2).toString());

                    for(j = 0; j < jarr2.size(); ++j) {
                        nja.checkLevel[j] = Integer.parseInt(jarr2.get(j).toString());
                    }

                    nja.isQuaHangDong = Integer.parseInt(jar.get(3).toString());
                    nja.get().countTayTiemNang = Integer.parseInt(jar.get(4).toString());
                    nja.get().countTayKyNang = Integer.parseInt(jar.get(5).toString());
                    jar = (JSONArray)JSONValue.parse(red.getString("taskHangNgay"));
                    nja.taskHangNgay[0] = Integer.parseInt(jar.get(0).toString());
                    nja.taskHangNgay[1] = Integer.parseInt(jar.get(1).toString());
                    nja.taskHangNgay[2] = Integer.parseInt(jar.get(2).toString());
                    nja.taskHangNgay[3] = Integer.parseInt(jar.get(3).toString());
                    nja.taskHangNgay[4] = Integer.parseInt(jar.get(4).toString());
                    nja.taskHangNgay[5] = Integer.parseInt(jar.get(5).toString());
                    nja.taskHangNgay[6] = Integer.parseInt(jar.get(6).toString());
                    nja.isTaskHangNgay = nja.taskHangNgay[5];
                    nja.countTaskHangNgay = nja.taskHangNgay[6];
                    jar = (JSONArray)JSONValue.parse(red.getString("taskTaThu"));
                    nja.taskTaThu[0] = Integer.parseInt(jar.get(0).toString());
                    nja.taskTaThu[1] = Integer.parseInt(jar.get(1).toString());
                    nja.taskTaThu[2] = Integer.parseInt(jar.get(2).toString());
                    nja.taskTaThu[3] = Integer.parseInt(jar.get(3).toString());
                    nja.taskTaThu[4] = Integer.parseInt(jar.get(4).toString());
                    nja.taskTaThu[5] = Integer.parseInt(jar.get(5).toString());
                    nja.taskTaThu[6] = Integer.parseInt(jar.get(6).toString());
                    nja.isTaskTaThu = nja.taskTaThu[5];
                    nja.countTaskTaThu = nja.taskTaThu[6];

                    jar = (JSONArray)JSONValue.parse(red.getString("taskDanhVong"));
                    nja.taskDanhVong[0] = Integer.parseInt(jar.get(0).toString());
                    nja.taskDanhVong[1] = Integer.parseInt(jar.get(1).toString());
                    nja.taskDanhVong[2] = Integer.parseInt(jar.get(2).toString());
                    nja.taskDanhVong[3] = Integer.parseInt(jar.get(3).toString());
                    nja.taskDanhVong[4] = Integer.parseInt(jar.get(4).toString());
                    nja.useDanhVongPhu = Integer.parseInt(jar.get(5).toString());
                    nja.isTaskDanhVong = nja.taskDanhVong[3];
                    nja.countTaskDanhVong = nja.taskDanhVong[4];

                    jar = (JSONArray)JSONValue.parse(red.getString("countUseItem"));
                    nja.useTaThuLenh = Integer.parseInt(jar.get(0).toString());
                    nja.get().useKyNang = Integer.parseInt(jar.get(1).toString());
                    nja.get().useTiemNang = Integer.parseInt(jar.get(2).toString());
                    nja.get().useBanhPhongLoi = Integer.parseInt(jar.get(3).toString());
                    nja.get().useBanhBangHoa = Integer.parseInt(jar.get(4).toString());

                    jar = (JSONArray)JSONValue.parse(red.getString("char_info"));
                    nja.pointUydanh = Integer.parseInt(jar.get(0).toString());
                    nja.pointNon = Integer.parseInt(jar.get(1).toString());
                    nja.pointVukhi = Integer.parseInt(jar.get(2).toString());
                    nja.pointAo = Integer.parseInt(jar.get(3).toString());
                    nja.pointLien = Integer.parseInt(jar.get(4).toString());
                    nja.pointGangtay = Integer.parseInt(jar.get(5).toString());
                    nja.pointNhan = Integer.parseInt(jar.get(6).toString());
                    nja.pointQuan = Integer.parseInt(jar.get(7).toString());
                    nja.pointNgocboi = Integer.parseInt(jar.get(8).toString());
                    nja.pointGiay = Integer.parseInt(jar.get(9).toString());
                    nja.pointPhu = Integer.parseInt(jar.get(10).toString());
                    nja.pointTinhTu = Integer.parseInt(jar.get(11).toString());

                    jar = (JSONArray)JSONValue.parse(red.getString("chien_truong"));
                    nja.pheCT = Integer.parseInt(jar.get(0).toString());
                    nja.pointCT = Integer.parseInt(jar.get(1).toString());

                    nja.isTakePoint = red.getInt("maxPointCT");

                    jar = (JSONArray)JSONValue.parse(red.getString("sk_noel"));
                    nja.isNhanQuaNoel = Integer.parseInt(jar.get(0).toString());
                    nja.pointNoel = Integer.parseInt(jar.get(1).toString());

                    jar = (JSONArray)JSONValue.parse(red.getString("trai_nghiem"));
                    nja.yenTN = Long.parseLong(jar.get(0).toString());
                    nja.xuTN = Long.parseLong(jar.get(1).toString());
                    nja.luongTN = Long.parseLong(jar.get(2).toString());
                    nja.expTN = Long.parseLong(jar.get(3).toString());

                    jar = (JSONArray)JSONValue.parse(red.getString("thoi-trang"));
                    nja.ID_HAIR = Short.parseShort(jar.get(0).toString());
                    nja.ID_Body = Short.parseShort(jar.get(1).toString());
                    nja.ID_LEG = Short.parseShort(jar.get(2).toString());
                    nja.ID_WEA_PONE = Short.parseShort(jar.get(3).toString());
                    nja.ID_PP = Short.parseShort(jar.get(4).toString());
                    nja.ID_NAME = Short.parseShort(jar.get(5).toString());
                    nja.ID_HORSE = Short.parseShort(jar.get(6).toString());
                    nja.ID_RANK = Short.parseShort(jar.get(7).toString());
                    nja.ID_MAT_NA = Short.parseShort(jar.get(8).toString());
                    nja.ID_Bien_Hinh = Short.parseShort(jar.get(9).toString());

                    jar = (JSONArray)JSONValue.parse(red.getString("clan"));
                    if (jar != null && jar.size() == 2) {
                        String clanName = jar.get(0).toString();
                        ClanManager clan = ClanManager.getClanName(clanName);
                        if (clan != null && clan.getMem(name) != null) {
                            nja.clan = clan.getMem(name);
                            nja.clan.nClass = nja.nclass;
                            nja.clan.clevel = nja.level;
                        } else {
                            nja.clan = new ClanMember("", nja);
                        }

                        nja.clan.pointClan = Integer.parseInt(jar.get(1).toString());
                    } else {
                        nja.clan = new ClanMember("", nja);
                    }

                    nja.denbu = red.getByte("denbu");
                    if (!red.getString("newlogin").equals("")) {
                        if(Util.getDate(red.getString("newlogin")) == null) {
                            return null;
                        }
                        nja.newlogin = Util.getDate(red.getString("newlogin"));
                        nja.ddClan = red.getBoolean("ddClan");
                        nja.caveID = red.getInt("caveID");
                        nja.nCave = red.getInt("nCave");
                        nja.pointCave = red.getInt("pointCave");
                        nja.useCave = red.getInt("useCave");
                        nja.bagCaveMax = red.getInt("bagCaveMax");
                        nja.itemIDCaveMax = red.getShort("itemIDCaveMax");
                        nja.exptype = red.getByte("exptype");
                        nja.isHuman = true;
                        nja.isNhanban = false;
                        red.close();
                        return nja;
                    } else {
                        Client.gI().kickSession(nja.p.conn);
                        return null;
                    }
                } else {
                    return null;
                }
            }
        } catch (Exception var23) {
            var23.printStackTrace();
            return null;
        }
    }

    public static Char getChar(String name) {
        try {
            Char nja = new Char();
            synchronized(Server.LOCK_MYSQL) {
                    ResultSet red = SQLManager.stat.executeQuery("SELECT * FROM `ninja` WHERE `name`LIKE'" + name + "';");
                    if (red != null && red.first()) {
                        nja.id = red.getInt("id");
                        nja.name = red.getString("name");
                        nja.gender = red.getByte("gender");
                        nja.head = red.getByte("head");
                        nja.speed = red.getByte("speed");
                        nja.nclass = red.getByte("class");
                        nja.ppoint = red.getShort("ppoint");
                        nja.potential0 = red.getShort("potential0");
                        nja.potential1 = red.getShort("potential1");
                        nja.potential2 = red.getInt("potential2");
                        nja.potential3 = red.getInt("potential3");
                        nja.spoint = red.getShort("spoint");
                        JSONArray jar = (JSONArray)JSONValue.parse(red.getString("skill"));
                        JSONObject job;
                        Skill skill;
                        byte index;
                        if (jar != null) {
                            job = null;
                            for(index = 0; index < jar.size(); ++index) {
                                job = (JSONObject)jar.get(index);
                                skill = new Skill();
                                skill.id = Byte.parseByte(job.get("id").toString());
                                skill.point = Byte.parseByte(job.get("point").toString());
                                nja.get().skill.add(skill);
                                job.clear();
                            }
                        }
                        for(Skill skl : nja.get().skill) {
                            if(skl.id != 67 && skl.id != 68 && skl.id != 69 && skl.id != 70 && skl.id != 71 && skl.id != 72) {
                                SkillTemplate data = SkillTemplate.Templates(skl.id);
                                if(data.type == 2) {
                                    nja.idSkillBot.add((short)skl.id);
                                }
                            }
                        }

                        JSONArray jarr2 = (JSONArray)JSONValue.parse(red.getString("KSkill"));
                        nja.get().KSkill = new byte[jarr2.size()];
                        byte j;
                        for(j = 0; j < nja.get().KSkill.length; ++j) {
                            nja.get().KSkill[j] = Byte.parseByte(jarr2.get(j).toString());
                        }

                        jarr2 = (JSONArray)JSONValue.parse(red.getString("OSkill"));
                        nja.get().OSkill = new byte[jarr2.size()];
                        for(j = 0; j < nja.get().OSkill.length; ++j) {
                            nja.get().OSkill[j] = Byte.parseByte(jarr2.get(j).toString());
                        }

                        nja.CSkill = Byte.parseByte(red.getString("CSkill"));
                        nja.level = red.getShort("level");
                        nja.exp = red.getLong("exp");
                        nja.expdown = red.getLong("expdown");
                        nja.expSkillClone = red.getLong("expSkillClone");
                        nja.pk = red.getByte("pk");
                        JSONObject job2 = null;

                        nja.get().ItemBody = new Item[32];
                        jar = (JSONArray)JSONValue.parse(red.getString("ItemBody"));
                        if (jar != null) {
                            for(j = 0; j < jar.size(); ++j) {
                                job2 = (JSONObject)jar.get(j);
                                index = Byte.parseByte(job2.get("index").toString());
                                nja.get().ItemBody[index] = ItemTemplate.parseItem(jar.get(j).toString());
                                job2.clear();
                            }
                        }

                        nja.get().ItemMounts = new Item[5];
                        jar = (JSONArray)JSONValue.parse(red.getString("ItemMounts"));
                        if (jar != null) {
                            for(j = 0; j < jar.size(); ++j) {
                                job2 = (JSONObject)jar.get(j);
                                index = Byte.parseByte(job2.get("index").toString());
                                nja.get().ItemMounts[index] = ItemTemplate.parseItem(jar.get(j).toString());
                                job2.clear();
                            }
                        }

                        jar = (JSONArray)JSONValue.parse(red.getString("info"));
                        nja.isDiemDanh = Integer.parseInt(jar.get(0).toString());
                        nja.countHangDong = Integer.parseInt(jar.get(1).toString());
                        jarr2 = (JSONArray)JSONValue.parse(jar.get(2).toString());
                        for(j = 0; j < jarr2.size(); ++j) {
                            nja.checkLevel[j] = Integer.parseInt(jarr2.get(j).toString());
                        }
                        nja.isQuaHangDong = Integer.parseInt(jar.get(3).toString());
                        nja.get().countTayTiemNang = Integer.parseInt(jar.get(4).toString());
                        nja.get().countTayKyNang = Integer.parseInt(jar.get(5).toString());
                        jar = (JSONArray)JSONValue.parse(red.getString("taskHangNgay"));
                        nja.taskHangNgay[0] = Integer.parseInt(jar.get(0).toString());
                        nja.taskHangNgay[1] = Integer.parseInt(jar.get(1).toString());
                        nja.taskHangNgay[2] = Integer.parseInt(jar.get(2).toString());
                        nja.taskHangNgay[3] = Integer.parseInt(jar.get(3).toString());
                        nja.taskHangNgay[4] = Integer.parseInt(jar.get(4).toString());
                        nja.taskHangNgay[5] = Integer.parseInt(jar.get(5).toString());
                        nja.taskHangNgay[6] = Integer.parseInt(jar.get(6).toString());
                        nja.isTaskHangNgay = nja.taskHangNgay[5];
                        nja.countTaskHangNgay = nja.taskHangNgay[6];
                        jar = (JSONArray)JSONValue.parse(red.getString("taskTaThu"));
                        nja.taskTaThu[0] = Integer.parseInt(jar.get(0).toString());
                        nja.taskTaThu[1] = Integer.parseInt(jar.get(1).toString());
                        nja.taskTaThu[2] = Integer.parseInt(jar.get(2).toString());
                        nja.taskTaThu[3] = Integer.parseInt(jar.get(3).toString());
                        nja.taskTaThu[4] = Integer.parseInt(jar.get(4).toString());
                        nja.taskTaThu[5] = Integer.parseInt(jar.get(5).toString());
                        nja.taskTaThu[6] = Integer.parseInt(jar.get(6).toString());
                        nja.isTaskTaThu = nja.taskTaThu[5];
                        nja.countTaskTaThu = nja.taskTaThu[6];

                        jar = (JSONArray)JSONValue.parse(red.getString("countUseItem"));
                        nja.useTaThuLenh = Integer.parseInt(jar.get(0).toString());
                        nja.get().useKyNang = Integer.parseInt(jar.get(1).toString());
                        nja.get().useTiemNang = Integer.parseInt(jar.get(2).toString());
                        nja.get().useBanhPhongLoi = Integer.parseInt(jar.get(3).toString());
                        nja.get().useBanhBangHoa = Integer.parseInt(jar.get(4).toString());

                        jar = (JSONArray)JSONValue.parse(red.getString("char_info"));
                        nja.pointUydanh = Integer.parseInt(jar.get(0).toString());
                        nja.pointNon = Integer.parseInt(jar.get(1).toString());
                        nja.pointVukhi = Integer.parseInt(jar.get(2).toString());
                        nja.pointAo = Integer.parseInt(jar.get(3).toString());
                        nja.pointLien = Integer.parseInt(jar.get(4).toString());
                        nja.pointGangtay = Integer.parseInt(jar.get(5).toString());
                        nja.pointNhan = Integer.parseInt(jar.get(6).toString());
                        nja.pointQuan = Integer.parseInt(jar.get(7).toString());
                        nja.pointNgocboi = Integer.parseInt(jar.get(8).toString());
                        nja.pointGiay = Integer.parseInt(jar.get(9).toString());
                        nja.pointPhu = Integer.parseInt(jar.get(10).toString());
                        nja.pointTinhTu = Integer.parseInt(jar.get(11).toString());

                        jar = (JSONArray)JSONValue.parse(red.getString("thoi-trang"));
                        nja.ID_HAIR = Short.parseShort(jar.get(0).toString());
                        nja.ID_Body = Short.parseShort(jar.get(1).toString());
                        nja.ID_LEG = Short.parseShort(jar.get(2).toString());
                        nja.ID_WEA_PONE = Short.parseShort(jar.get(3).toString());
                        nja.ID_PP = Short.parseShort(jar.get(4).toString());
                        nja.ID_NAME = Short.parseShort(jar.get(5).toString());
                        nja.ID_HORSE = Short.parseShort(jar.get(6).toString());
                        nja.ID_RANK = Short.parseShort(jar.get(7).toString());
                        nja.ID_MAT_NA = Short.parseShort(jar.get(8).toString());
                        nja.ID_Bien_Hinh = Short.parseShort(jar.get(9).toString());

                        jar = (JSONArray)JSONValue.parse(red.getString("clan"));
                        if (jar != null && jar.size() == 2) {
                            String clanName = jar.get(0).toString();
                            ClanManager clan = ClanManager.getClanName(clanName);
                            if (clan != null && clan.getMem(name) != null) {
                                nja.clan = clan.getMem(name);
                                nja.clan.nClass = nja.nclass;
                                nja.clan.clevel = nja.level;
                            } else {
                                nja.clan = new ClanMember("", nja);
                            }
                            nja.clan.pointClan = Integer.parseInt(jar.get(1).toString());
                        } else {
                            nja.clan = new ClanMember("", nja);
                        }
                        return nja;
                    } else {
                        return null;
                    }
                }
        } catch (Exception var23) {
            var23.printStackTrace();
            return null;
        }
    }

    public short getCSkill() {
        if (this.isBot) {
            final int randomIndex = Util.nextInt(0, this.getSkills().size());
            return this.getSkills().get(randomIndex % this.getSkills().size()).id;
        }
        return super.getCSkill();
    }

    public void flush() {
        JSONArray jarr = new JSONArray();
        try {
            synchronized(Server.LOCK_MYSQL) {
                if (this.get().level >= Manager.max_level_up) {
                    this.get().level = Manager.max_level_up;
                }
                this.get().level = (int)Level.getLevelExp(this.get().exp)[0];
                if (this.mapid == 133 || this.mapid == 149 || this.mapid == 111 || this.mapid == 129 || Map.mapHD(this.mapid) || (this.mapid >= 80 && this.mapid <= 90)) {
                    this.mapid = this.mapKanata;
                    switch(this.mapKanata) {
                        case 1:
                            this.x = 613;
                            this.y = 288;
                            break;
                        case 27:
                            this.x = 1764;
                            this.y = 360;
                            break;
                        case 72:
                            this.x = 852;
                            this.y = 216;
                    }
                } else if(this.mapid == 74) {
                    this.mapid = this.mapLTD;
                }

                jarr.add(this.mapid);
                jarr.add(this.x);
                jarr.add(this.y);
                jarr.add(this.mapLTD);
                String sqlSET = "`head`=" + this.head +",`caiTrang`=" + this.caiTrang +",`rankTDB`=" + this.rankTDB +",`countWin`=" + this.countWin +",`countPhao`=" + this.countPhao +",`nhanTP`=" + this.nhanTP +",`countTDB`=" + this.countTDB + ",`isGiftTDB`=" + this.isGiftTDB + ", `taskId`=" + this.taskId + ",`class`=" + this.get().nclass + ",`ppoint`=" + this.get().ppoint + ",`potential0`=" + this.get().potential0 + ",`potential1`=" + this.get().potential1 + ",`potential2`=" + this.get().potential2 + ",`potential3`=" + this.get().potential3 + ",`spoint`=" + this.get().spoint + ",`level`=" + this.get().level + ",`exp`=" + this.exp + ",`expdown`=" + this.expdown + ",`expSkillClone`=" + this.expSkillClone + ",`pk`=" + this.pk + ",`xu`=" + this.xu + ",`yen`=" + this.yen + ",`maxluggage`=" + this.maxluggage + ",`levelBag`=" + this.levelBag + ",`site`='" + jarr.toJSONString() + "', `buyX3` =" + this.countBuyX3;
                jarr.clear();
                Iterator var4 = this.skill.iterator();

                Skill skill;
                while(var4.hasNext()) {
                    skill = (Skill)var4.next();
                    jarr.add(SkillTemplate.ObjectSkill(skill));
                }
                sqlSET = sqlSET + ",`skill`='" + jarr.toJSONString() + "'";
                jarr.clear();

                jarr = new JSONArray();
                short s1;
                JSONArray friend;
                for (s1 = 0; s1 < this.vFriend.size(); s1 = (short)(s1 + 1)) {
                    friend = new JSONArray();
                    friend.add((this.vFriend.get(s1)).friendName);
                    friend.add(Byte.valueOf((this.vFriend.get(s1)).type));
                    jarr.add(friend);
                }
                sqlSET = sqlSET + ",`friends`='" + jarr.toJSONString() + "'";
                jarr.clear();

                byte[] var11 = this.KSkill;
                int var13 = var11.length;
                int var6;
                byte oid;
                for(var6 = 0; var6 < var13; ++var6) {
                    oid = var11[var6];
                    jarr.add(oid);
                }

                sqlSET = sqlSET + ",`KSkill`='" + jarr.toJSONString() + "'";
                jarr.clear();
                var11 = this.OSkill;
                var13 = var11.length;

                for(var6 = 0; var6 < var13; ++var6) {
                    oid = var11[var6];
                    jarr.add(oid);
                }

                sqlSET = sqlSET + ",`OSkill`='" + jarr.toJSONString() + "',`CSkill`=" + this.CSkill + "";
                jarr.clear();

                byte j;
                for(j = 0; j < this.ItemBag.length; ++j) {
                    if (this.ItemBag[j] != null) {
                        jarr.add(ItemTemplate.ObjectItem(this.ItemBag[j], j));
                    }
                }
                sqlSET = sqlSET + ",`ItemBag`='" + jarr.toJSONString() + "'";
                jarr.clear();

                for(j = 0; j < this.ItemBST.length; ++j) {
                    if (this.ItemBST[j] != null) {
                        jarr.add(ItemTemplate.ObjectItem(this.ItemBST[j], j));
                    }
                }
                sqlSET = sqlSET + ",`ItemBST`='" + jarr.toJSONString() + "'";
                jarr.clear();

                for(j = 0; j < this.ItemCaiTrang.length; ++j) {
                    if (this.ItemCaiTrang[j] != null) {
                        jarr.add(ItemTemplate.ObjectItem(this.ItemCaiTrang[j], j));
                    }
                }
                sqlSET = sqlSET + ",`ItemCaiTrang`='" + jarr.toJSONString() + "'";
                jarr.clear();

                for(j = 0; j < this.ItemBox.length; ++j) {
                    if (this.ItemBox[j] != null) {
                        jarr.add(ItemTemplate.ObjectItem(this.ItemBox[j], j));
                    }
                }
                sqlSET = sqlSET + ",`xuBox`=" + this.xuBox + ",`ItemBox`='" + jarr.toJSONString() + "'";
                jarr.clear();

                for(j = 0; j < this.get().ItemBody.length; ++j) {
                    if (this.get().ItemBody[j] != null) {
                        jarr.add(ItemTemplate.ObjectItem(this.get().ItemBody[j], j));
                    }
                }

                sqlSET = sqlSET + ",`ItemBody`='" + jarr.toJSONString() + "'";
                jarr.clear();

                for(j = 0; j < this.ItemMounts.length; ++j) {
                    if (this.ItemMounts[j] != null) {
                        jarr.add(ItemTemplate.ObjectItem(this.ItemMounts[j], j));
                    }
                }

                sqlSET = sqlSET + ",`ItemMounts`='" + jarr.toJSONString() + "'";
                jarr.clear();

                JSONArray jarr2;
                byte i;
                for(i = 0; i < this.veff.size(); ++i) {
                    if (((Effect)this.veff.get(i)).template.type == 0 || ((Effect)this.veff.get(i)).template.type == 18 || ((Effect)this.veff.get(i)).template.type == 25) {
                        jarr2 = new JSONArray();
                        jarr2.add(((Effect)this.veff.get(i)).template.id);
                        if (((Effect)this.veff.get(i)).template.id != 36 && ((Effect)this.veff.get(i)).template.id != 42 && ((Effect)this.veff.get(i)).template.id != 37 && ((Effect)this.veff.get(i)).template.id != 38 && ((Effect)this.veff.get(i)).template.id != 39) {
                            jarr2.add(0);
                            jarr2.add(((Effect)this.veff.get(i)).timeRemove - System.currentTimeMillis());
                        } else {
                            jarr2.add(1);
                            jarr2.add(((Effect)this.veff.get(i)).timeRemove);
                        }

                        jarr2.add(((Effect)this.veff.get(i)).param);
                        jarr.add(jarr2);
                    }
                }
                sqlSET = sqlSET + ",`effect`='" + jarr.toJSONString() + "'";

                jarr.clear();
                jarr.add(this.taskHangNgay[0]);
                jarr.add(this.taskHangNgay[1]);
                jarr.add(this.taskHangNgay[2]);
                jarr.add(this.taskHangNgay[3]);
                jarr.add(this.taskHangNgay[4]);
                jarr.add(this.isTaskHangNgay);
                jarr.add(this.countTaskHangNgay);
                sqlSET = sqlSET + ",`taskHangNgay`='" + jarr.toJSONString() + "'";

                jarr.clear();
                jarr.add(this.taskTaThu[0]);
                jarr.add(this.taskTaThu[1]);
                jarr.add(this.taskTaThu[2]);
                jarr.add(this.taskTaThu[3]);
                jarr.add(this.taskTaThu[4]);
                jarr.add(this.isTaskTaThu);
                jarr.add(this.countTaskTaThu);
                sqlSET = sqlSET + ",`taskTaThu`='" + jarr.toJSONString() + "'";
                jarr.clear();
                jarr.add(this.taskDanhVong[0]);
                jarr.add(this.taskDanhVong[1]);
                jarr.add(this.taskDanhVong[2]);
                jarr.add(this.isTaskDanhVong);
                jarr.add(this.countTaskDanhVong);
                jarr.add(this.useDanhVongPhu);
                sqlSET = sqlSET + ",`taskDanhVong`='" + jarr.toJSONString() + "'";
                jarr.clear();
                jarr.add(this.isDiemDanh);
                jarr.add(this.countHangDong);
                jarr2 = new JSONArray();

                for(i = 0; i < this.checkLevel.length; ++i) {
                    jarr2.add(this.checkLevel[i]);
                }

                jarr.add(jarr2);
                jarr.add(this.isQuaHangDong);
                jarr.add(this.get().countTayTiemNang);
                jarr.add(this.get().countTayKyNang);
                sqlSET = sqlSET + ",`info`='" + jarr.toJSONString() + "'";
                jarr.clear();
                jarr.add(this.useTaThuLenh);
                jarr.add(this.get().useKyNang);
                jarr.add(this.get().useTiemNang);
                jarr.add(this.get().useBanhPhongLoi);
                jarr.add(this.get().useBanhBangHoa);
                sqlSET = sqlSET + ",`countUseItem`='" + jarr.toJSONString() + "'";
                jarr.clear();
                jarr.add(this.pointUydanh);
                jarr.add(this.pointNon);
                jarr.add(this.pointVukhi);
                jarr.add(this.pointAo);
                jarr.add(this.pointLien);
                jarr.add(this.pointGangtay);
                jarr.add(this.pointNhan);
                jarr.add(this.pointQuan);
                jarr.add(this.pointNgocboi);
                jarr.add(this.pointGiay);
                jarr.add(this.pointPhu);
                jarr.add(this.pointTinhTu);
                sqlSET = sqlSET + ",`char_info`='" + jarr.toJSONString() + "'";

                jarr.clear();
                jarr.add(this.pheCT);
                jarr.add(this.pointCT);
                sqlSET = sqlSET + ",`chien_truong`='" + jarr.toJSONString() + "'";

                jarr.clear();
                jarr.add(this.isNhanQuaNoel);
                jarr.add(this.pointNoel);
                sqlSET = sqlSET + ",`sk_noel`='" + jarr.toJSONString() + "'";

                jarr.clear();
                jarr.add(this.yenTN);
                jarr.add(this.xuTN);
                jarr.add(this.luongTN);
                jarr.add(this.expTN);
                sqlSET = sqlSET + ",`trai_nghiem`='" + jarr.toJSONString() + "'";

                jarr.clear();
                jarr.add(this.ID_HAIR);
                jarr.add(this.ID_Body);
                jarr.add(this.ID_LEG);
                jarr.add(this.ID_WEA_PONE);
                jarr.add(this.ID_PP);
                jarr.add(this.ID_NAME);
                jarr.add(this.ID_HORSE);
                jarr.add(this.ID_RANK);
                jarr.add(this.ID_MAT_NA);
                jarr.add(this.ID_Bien_Hinh);
                sqlSET = sqlSET + ",`thoi-trang`='" + jarr.toJSONString() + "'";

                jarr.clear();
                jarr.add(this.clan.clanName);
                jarr.add(this.clan.pointClan);
                sqlSET = sqlSET + ",`timeRemoveClone` = "+this.timeRemoveClone+  ",`isHangDong6x` = "+this.isHangDong6x+ ",`ldgtID` = "+this.ldgtID+",`pointBossTL` = "+this.pointBossTL+ ",`maxPointCT` = "+this.isTakePoint+",`clan`='" + jarr.toJSONString() + "',`denbu`=" + this.denbu + ",`newlogin`='" + Util.toDateString(this.newlogin) + "',`ddClan`=" + this.ddClan + ",`caveID`=" + this.caveID + ",`nCave`=" + this.nCave + ",`pointCave`=" + this.pointCave + ",`useCave`=" + this.useCave + ",`bagCaveMax`=" + this.bagCaveMax + ",`itemIDCaveMax`=" + this.itemIDCaveMax + ",`saveBXH`=" + this.saveBXH + ",`exptype`=" + this.exptype + "";
                SQLManager.stat.executeUpdate("UPDATE `ninja` SET " + sqlSET + " WHERE `id`=" + this.id + " LIMIT 1;");
                if (jarr != null && !jarr.isEmpty()) {
                    jarr.clear();
                }
                if (jarr2 != null && !jarr2.isEmpty()) {
                    jarr2.clear();
                }
            }
        } catch (Exception var10) {
            Util.Debug("Error update database: " + var10.toString());
        }

    }

    public void close() {
    }

    public PartyPlease findPartyInvite(int charId) {
        try {
            int i;
            PartyPlease Invite;
            for(i = this.aPartyInvite.size() - 1; i >= 0; --i) {
                Invite = (PartyPlease)this.aPartyInvite.get(i);
                if (Invite.charID == charId) {
                    return Invite;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public void removePartyInvite(int charId) {
        try {
            int i;
            PartyPlease Invite;
            for(i = this.aPartyInvite.size() - 1; i >= 0; --i) {
                Invite = (PartyPlease)this.aPartyInvite.get(i);
                if (Invite.charID == charId) {
                    this.aPartyInvite.remove(i);
                    return;
                }
            }
            return;
        } catch (Exception e) {
            return;
        }

    }

    public PartyPlease findPartyInvate(int charId) {
        try {
            int i;
            PartyPlease Invate;
            for(i = this.aPartyInvate.size() - 1; i >= 0; --i) {
                Invate = (PartyPlease)this.aPartyInvate.get(i);
                if (Invate.charID == charId) {
                    return Invate;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public void removePartyInvate(int charId) {
        try {
            int i;
            PartyPlease Invate;
            for(i = this.aPartyInvate.size() - 1; i >= 0; --i) {
                Invate = this.aPartyInvate.get(i);
                if (Invate.charID == charId) {
                    this.aPartyInvate.remove(i);
                    return;
                }
            }
            return;
        } catch (Exception e) {
            return;
        }

    }
}
