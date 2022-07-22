package com.hoiuc.server;

import com.hoiuc.assembly.*;
import com.hoiuc.io.Message;
import com.hoiuc.io.Util;
import com.hoiuc.template.ItemTemplate;
import com.hoiuc.template.MobTemplate;
import com.hoiuc.template.SkillTemplate;

import java.io.*;
import java.util.HashMap;

public class GameSrc {
    static int[] crystals;
    static int[] upClothe;
    static int[] upAdorn;
    static int[] upWeapon;
    static int[] coinUpCrystals;
    static int[] coinUpClothes;
    static int[] coinUpAdorns;
    static int[] coinUpWeapons;
    static int[] goldUps;
    static int[] maxPercents;
    private static short[] ArridLuck;
    //private static int[] ArryenLuck;
    public static byte[] ArrdayLuck;

    public static long[] upExpSkillClone = new long[] {500000, 1000000, 3000000, 5000000, 10000000, 30000000, 50000000, 100000000, 300000000};

    public static int[] arrNgocRong = new int[] {222, 223, 224, 225, 226, 227, 228};

    public static int[] coinUpMat = new int[] {250000, 500000, 1250000, 2000000, 4000000, 10000000, 20000000, 35000000, 50000000, 100000000};
    public static int[] goldUpMat = new int[] {10, 40, 60, 85, 120, 150, 190, 235, 285, 350};
    public static int[] percentUpMat = new int[] {100, 50, 35, 25, 20, 15, 10, 7, 5, 2};

    public static int[] arrModIdTaThu30 = new int[]{30, 33, 35, 37};
    public static int[] arrModIdTaThu40 = new int[]{40, 43, 45, 47, 49};
    public static int[] arrModIdTaThu50 = new int[]{51, 53, 57, 59};
    public static int[] arrModIdTaThu60 = new int[]{61, 65, 67, 63};
    public static int[] arrModIdTaThu70 = new int[]{129, 132, 135};
    public static int[] arrModIdTaThu70_2 = new int[]{71, 74, 77};
    public static int[] arrModIdTaThu80 = new int[]{130,137};
    public static int[] arrModIdTaThu80_2 = new int[]{80,88};
    public static int[] arrModIdTaThu100 = new int[]{133};

    private static final HashMap<Integer, Integer> xuGotNgoc = new HashMap();
    public static final HashMap<Integer, Integer> exps = new HashMap();
    private static int[][] arrNgocKhamEXP = new int[][]{
            new int[2],
            new int[]{200, 10},
            new int[]{500, 20},
            new int[]{1000, 50},
            new int[]{2000, 100},
            new int[]{5000, 200},
            new int[]{10000, 500},
            new int[]{20000, 1000},
            new int[]{50000, 2000},
            new int[]{100000, 5000},
            new int[]{100000, 10000}
    };
    private static int[][] arrLuyenNgocEXP = new int[][]{
            new int[2],
            new int[]{200, 0},
            new int[]{500, 200},
            new int[]{1000, 500},
            new int[]{2000, 1000},
            new int[]{5000, 2000},
            new int[]{10000, 5000},
            new int[]{20000, 10000},
            new int[]{50000, 20000},
            new int[]{100000, 50000},
            new int[]{110000, 100000}
    };
    public static int[] coinGotngoc = new int[]{0, 5000, 40000, 135000, 320000, 625000, 1080000, 1715000, 2560000, 3645000, 5000000};

    public static boolean mapNotPK(int mapId) {
        return mapId == 1 || mapId == 10 || mapId == 17 || mapId == 22 || mapId == 27 || mapId == 32 || mapId == 38 || mapId == 43 || mapId == 48 || mapId == 72 || mapId == 109 || mapId == 121 || mapId == 122 || mapId == 123; //138
    }

    public static byte KeepUpgrade(int upgrade) {
        if (upgrade >= 14) {
            return 14;
        }
        if (upgrade >= 12) {
            return 12;
        }
        if (upgrade >= 8) {
            return 8;
        }
        if (upgrade >= 4) {
            return 4;
        }
        return (byte)upgrade;
    }

    public static byte SysClass(byte nclass) {
        switch (nclass) {
            case 1:
            case 2: {
                return 1;
            }
            case 3:
            case 4: {
                return 2;
            }
            case 5:
            case 6: {
                return 3;
            }
            default: {
                if (nclass == 6 || nclass == 5) {
                    return 3;
                }
                if (nclass == 4 || nclass == 3) {
                    return 2;
                }
                if (nclass == 2 || nclass == 1) {
                    return 1;
                }
                return 0;
            }
        }
    }

    public static byte SideClass(byte nclass) {
        if (nclass == 6 || nclass == 4 || nclass == 2) {
            return 1;
        }
        return 0;
    }

    public static void SendFile(Session session, int cmd, String url) throws IOException {
        byte[] ab = loadFile(url).toByteArray();
        Message msg = new Message(cmd);
        msg.writer().write(ab);
        msg.writer().flush();
        session.sendMessage(msg);
        msg.cleanup();
    }

    public static void ItemStands(Player p) throws IOException {
        Message m = new Message(-28);
        m.writer().writeByte(-83);
        m.writer().writeByte(10);
        m.writer().writeByte(12);
        m.writer().writeByte(12);
        m.writer().writeByte(13);
        m.writer().flush();
        p.conn.sendMessage(m);
        m.cleanup();
    }

    public static void sendSkill(Player p, String text) {
        try {
            byte[] arrSkill = null;
            int lent = 0;
            if (text.equals("KSkill")) {
                lent = p.c.get().KSkill.length;
                arrSkill = new byte[lent];
                System.arraycopy(p.c.get().KSkill, 0, arrSkill, 0, lent);
            }
            if (text.equals("OSkill")) {
                lent = p.c.get().OSkill.length;
                arrSkill = new byte[lent];
                System.arraycopy(p.c.get().OSkill, 0, arrSkill, 0, lent);
            }
            if (text.equals("CSkill")) {
                lent = 1;
                arrSkill = new byte[lent];
                arrSkill[0] = -1;
                Skill skill = p.c.get().getSkill(p.c.get().CSkill);
                if (skill != null) {
                    SkillTemplate data = SkillTemplate.Templates(skill.id);
                    if (data.type != 2) {
                        arrSkill[0] = skill.id;
                    }
                }
                if (arrSkill[0] == -1 && p.c.get().skill.size() > 0) {
                    arrSkill[0] = p.c.get().skill.get(0).id;
                }
            }
            if (arrSkill == null) {
                return;
            }
            Message m = new Message(-30);
            m.writer().writeByte(-65);
            m.writer().writeUTF(text);
            m.writer().writeInt(lent);
            m.writer().write(arrSkill);
            m.writer().writeByte(0);
            m.writer().flush();
            p.conn.sendMessage(m);
            m.cleanup();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reciveImage(Player p, Message m) {
        try {
            int id = m.reader().readInt();
            m.cleanup();
            int zoomLv = p.conn.zoomLevel;
            if(zoomLv < 1 || zoomLv > 4){
                zoomLv = 1;
            }
            ByteArrayOutputStream a = loadFile("res/assets/icon/" + zoomLv + "/Small" + id + ".png");
            if (a != null) {
                a.flush();
                byte[] ab = a.toByteArray();
                m = new Message(-28);
                m.writer().writeByte(-115);
                m.writer().writeInt(id);
                m.writer().writeInt(ab.length);
                m.writer().write(ab);
                m.writer().flush();
                p.conn.sendMessage(m);
                m.cleanup();
                a.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void reciveImageMOB(Player p, Message m){
        try {
            int id = m.reader().readUnsignedByte();
            MobTemplate mob = MobTemplate.getMob(id);
            if (mob == null) {
                return;
            }
            Util.Debug(mob.id + " Id mob " + id);
            int zoomLv = p.conn.zoomLevel;
            if(zoomLv < 1 || zoomLv > 4){
                zoomLv = 1;
            }
            ByteArrayOutputStream a = loadFile("Monster/x"+zoomLv+"/"+id);
            if (a != null) {
                a.flush();
                byte[] ab = a.toByteArray();
                m = new Message(-28);
                m.writer().write(ab);
                m.writer().flush();
                p.conn.sendMessage(m);
            }
            a.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static ByteArrayOutputStream loadFile(String url) {
        try {
            FileInputStream openFileInput = new FileInputStream(url);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[1024];
            while (true) {
                int read = openFileInput.read(bArr);
                if (read == -1) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, read);
            }
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
            openFileInput.close();
            return byteArrayOutputStream;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveFile(String url, byte[] data) {
        try {
            File f = new File(url);
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(url);
            fos.write(data);
            fos.flush();
            fos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ItemInfo(Player p, Message m) throws IOException {
        byte type = m.reader().readByte();
        m.cleanup();
        Util.Debug("Item info type " + type);
        Item[] arrItem = null;
        switch (type) {
            case 2: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 4: {
                if(p.menuCaiTrang == 0) {
                    arrItem = p.c.ItemBox;
                }
                break;
            }
            case 6: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 7: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 8: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 9: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 14: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 15: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 16: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 17: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 18: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 19: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 20: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 21: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 22: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 23: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 24: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 25: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 26: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 27: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 28: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 29: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 32: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 34: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
        }
        if (arrItem == null) {
            return;
        }
        if (type == 4) {
            m = new Message(31);
            m.writer().writeInt(p.c.xuBox);
            m.writer().writeByte(arrItem.length);
            for (Item item : arrItem) {
                if (item != null) {
                    m.writer().writeShort(item.id);
                    m.writer().writeBoolean(item.isLock);
                    if (ItemTemplate.isTypeBody(item.id) || ItemTemplate.isTypeNgocKham(item.id)) {
                        m.writer().writeByte(item.upgrade);
                    }
                    m.writer().writeBoolean(item.isExpires);
                    m.writer().writeShort(item.quantity);
                }
                else {
                    m.writer().writeShort(-1);
                }
            }
            m.writer().flush();
            p.conn.sendMessage(m);
            m.cleanup();
        }
        else {
            m = new Message(33);
            m.writer().writeByte(type);
            m.writer().writeByte(arrItem.length);
            for (int i = 0; i < arrItem.length; ++i) {
                m.writer().writeByte(i);
                m.writer().writeShort(arrItem[i].id);
            }
            m.writer().flush();
            p.conn.sendMessage(m);
            m.cleanup();
        }
    }

    public static void buyItemStore(Player p, Message m) {
        try {
            if (p.c.isNhanban) {
                p.conn.sendMessageLog(Language.IS_THU_THAN);
                return;
            }
            byte type = m.reader().readByte();
            byte index = m.reader().readByte();
            short num = 1;
            if (m.reader().available() > 0) {
                num = m.reader().readShort();
            }
            m.cleanup();
            Item sell = ItemSell.getItemTypeIndex(type, index);
            if (num <= 0 || sell == null) {
                return;
            }
            if(sell.id == 539 && (p.c.countBuyX3 <= 0 || num > p.c.countBuyX3)) {
                p.conn.sendMessageLog("Mỗi Ngay bạn chỉ có thể mua tối đa 6 nấm X3");
                return;
            }
            int buycoin = sell.buyCoin * num;
            int buycoinlock = sell.buyCoinLock * num;
            int buycoingold = sell.buyGold * num;
            if (buycoin < 0 || buycoinlock < 0 || buycoingold < 0) {
                return;
            }
            ItemTemplate data = ItemTemplate.ItemTemplateId(sell.id);

            if (type == 34 && num > 0) {
                ClanManager clan = ClanManager.getClanName(p.c.clan.clanName);
                if (clan == null) {
                    p.conn.sendMessageLog("Bạn cần có gia tộc.");
                }
                else if (p.c.clan.typeclan < 3) {
                    p.conn.sendMessageLog("Chỉ có tộc trường hoặc tộc phó mới có thể mua.");
                }
                else if ((sell.id == 423 && clan.itemLevel < 1) || (sell.id == 424 && clan.itemLevel < 2) || (sell.id == 425 && clan.itemLevel < 3) || (sell.id == 426 && clan.itemLevel < 4) || (sell.id == 427 && clan.itemLevel < 5)) {
                    p.conn.sendMessageLog("Cần khai mở gia tộc để mua vật phẩm này");
                }
                else {
                    if (buycoin > clan.coin) {
                        p.conn.sendMessageLog("Ngân sách gia tộc không đủ.");
                        return;
                    }
                    if ((sell.id >= 423 && sell.id <= 427) || sell.id == 281) {
                        Item item = sell.clone();
                        item.quantity = num;
                        for (short i = 0; i < item.options.size(); ++i) {
                            item.options.get(i).param = Util.nextInt(item.getOptionShopMin(item.options.get(i).id, item.options.get(i).param), item.options.get(i).param);
                        }
                        clan.addItem(item);
                        clan.updateCoin(-buycoin);
                        Service.updateCost(p);
                        m = new Message(-24);
                        m.writer().writeUTF("Gia tộc nhận được " + data.name);
                        m.writer().flush();
                        clan.sendMessage(m);
                        m.cleanup();
                    }
                    else {
                        p.conn.sendMessageLog("Chưa hỗ trợ");
                    }
                }
            }
            else if ((!data.isUpToUp && p.c.getBagNull() >= num) || (data.isUpToUp && p.c.getIndexBagid(sell.id, sell.isLock) != -1) || (data.isUpToUp && p.c.getBagNull() > 0)) {
                if (p.c.xu < buycoin) {
                    p.conn.sendMessageLog("Không đủ xu");
                    return;
                }
                if (p.c.yen < buycoinlock) {
                    p.conn.sendMessageLog("Không đủ yên");
                    return;
                }
                if (p.luong < buycoingold) {
                    p.conn.sendMessageLog("Không đủ lượng");
                    return;
                }
                p.c.upxu(-buycoin);
                p.c.upyen(-buycoinlock);
                p.upluong(-buycoingold);
                Item item;
                int j;
                for (j = 0; j < num; ++j) {
                    item = new Item();
                    item.id = sell.id;
                    if(sell.id == 539) {
                        p.c.countBuyX3--;
                    }
                    if (sell.isLock) {
                        item.isLock = true;
                    }
                    item.sys = sell.sys;
                    if (sell.isExpires) {
                        item.isExpires = true;
                        item.expires = Util.TimeMillis(sell.expires);
                    }
                    item.saleCoinLock = sell.saleCoinLock;
                    int idOp;
                    int par;
                    Option option;
                    for (Option Option : sell.options) {
                        idOp = Option.id;
                        par = Util.nextInt(item.getOptionShopMin(idOp, Option.param), Option.param);
                        option = new Option(idOp, par);
                        item.options.add(option);
                    }
                    if (data.isUpToUp) {
                        item.quantity = num;
                        p.c.addItemBag(true, item);
                        break;
                    }
                    p.c.addItemBag(false, item);
                }
                Service.updateCost(p);
            }
            else {
                p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void doConvertUpgrade(Player p, Message m) {
        try {
            byte index1 = m.reader().readByte();
            byte index2 = m.reader().readByte();
            byte index3 = m.reader().readByte();
            m.cleanup();
            Item item1 = p.c.getIndexBag(index1);
            Item item2 = p.c.getIndexBag(index2);
            Item item3 = p.c.getIndexBag(index3);
            if (item1 != null && item2 != null && item3 != null) {
                if (!ItemTemplate.isTypeBody(item1.id) || !ItemTemplate.isTypeBody(item2.id) || (item3.id != 269 && item3.id != 270 && item3.id != 271)) {
                    p.conn.sendMessageLog("Chỉ được dùng trang bị và chuyển hoá");
                    return;
                }
                ItemTemplate data1 = ItemTemplate.ItemTemplateId(item1.id);
                ItemTemplate data2 = ItemTemplate.ItemTemplateId(item2.id);
                if (item1.upgrade == 0 || item2.upgrade > 0 || (item3.id == 269 && item1.upgrade > 10) || (item3.id == 270 && item1.upgrade > 13)) {
                    p.conn.sendMessageLog("Vậy phẩm chuyển hoá không hợp lệ");
                    return;
                }
                if (data1.level > data2.level || data1.type != data2.type) {
                    p.conn.sendMessageLog("Chỉ có thể chuyển hoá trang bị cùng cấp và cùng loại trở lên");
                    return;
                }
                item1.isLock = true;
                item2.isLock = true;
                byte upgrade = item1.upgrade;
                item1.upgradeNext((byte)(-item1.upgrade));
                item2.upgradeNext(upgrade);
                m = new Message(-28);
                m.writer().writeByte(-88);
                m.writer().writeByte(index1);
                m.writer().writeByte(item1.upgrade);
                m.writer().writeByte(index2);
                m.writer().writeByte(item2.upgrade);
                m.writer().flush();
                p.conn.sendMessage(m);
                m.cleanup();
                p.c.removeItemBag(index3, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void crystalCollect(Player p, Message m, boolean isCoin) {
        try {
            if (p.c.isNhanban) {
                p.conn.sendMessageLog(Language.NOT_FOR_PHAN_THAN);
                return;
            }
            if (m.reader().available() > 28) {
                return;
            }
            if (p.c.getBagNull() == 0) {
                p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                return;
            }
            int crys = 0;
            byte[] arrItem = new byte[m.reader().available()];
            byte i;
            byte index;
            Item item;
            ItemTemplate data;
            for (i = 0; i < arrItem.length; ++i) {
                arrItem[i] = -1;
                index = m.reader().readByte();
                item = p.c.getIndexBag(index);
                if (item != null) {
                    data = ItemTemplate.ItemTemplateId(item.id);
                    if (data.type != 26 || item.id >= 12) {
                        p.conn.sendMessageLog("Chỉ có thể dùng đá dưới 12 để nâng cấp");
                        return;
                    }
                    arrItem[i] = index;
                    crys += GameSrc.crystals[item.id];
                }
            }
            m.cleanup();
            short id = 0;
            byte j;
            for (j = 0; j < GameSrc.crystals.length; ++j) {
                if (crys > GameSrc.crystals[j]) {
                    id = (short)(j + 1);
                }
            }
            if (id > 11) {
                id = 11;
            }
            int percen = crys * 100 / GameSrc.crystals[id];
            if (percen < 45) {
                p.conn.sendMessageLog("Tỷ lệ phải từ 45% trở lên");
                return;
            }
            if (isCoin) {
                if (GameSrc.coinUpCrystals[id] > p.c.xu) {
                    return;
                }
                p.c.upxu(-GameSrc.coinUpCrystals[id]);
            }
            else {
                if (GameSrc.coinUpCrystals[id] > p.c.xu + p.c.yen) {
                    return;
                }
                if (p.c.yen >= GameSrc.coinUpCrystals[id]) {
                    p.c.upyen(-GameSrc.coinUpCrystals[id]);
                }
                else {
                    int coin = GameSrc.coinUpCrystals[id] - p.c.yen;
                    p.c.upyen(-p.c.yen);
                    p.c.upxu(-coin);
                }
            }
            boolean suc = false;
            Item item2 = new Item();
            if (Util.nextInt(1, 100) <= percen) {
                suc = true;
                item2.id = id;
                if(item2.id == 10 && p.c.isTaskDanhVong == 1 && p.c.taskDanhVong[0] == 3) {
                    p.c.taskDanhVong[1]++;
                    if(p.c.taskDanhVong[1] == p.c.taskDanhVong[2]) {
                        p.sendAddchatYellow("Bạn đã hoàn thành nhiệm vụ danh vọng.");
                    }
                }
            }
            else {
                item2.id = (short)(id - 1);
            }
            item2.isLock = false;
            byte k;
            for (k = 0; k < arrItem.length; ++k) {
                if (arrItem[k] != -1) {
                    if(!isCoin || (p.c.ItemBag[arrItem[k]]!= null && p.c.ItemBag[arrItem[k]].isLock)) {
                        item2.isLock = true;
                    }
                    p.c.ItemBag[arrItem[k]] = null;
                }
            }
            byte index2 = p.c.getIndexBagNotItem();
            p.c.ItemBag[index2] = item2;

            m = new Message(isCoin ? 19 : 20);
            m.writer().writeByte(suc ? 1 : 0);
            m.writer().writeByte(index2);
            m.writer().writeShort(item2.id);
            m.writer().writeBoolean(item2.isLock);
            m.writer().writeBoolean(item2.isExpires);
            if (isCoin) {
                m.writer().writeInt(p.c.xu);
            }
            else {
                m.writer().writeInt(p.c.yen);
                m.writer().writeInt(p.c.xu);
            }
            m.writer().flush();
            p.conn.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void UpGrade(Player p, Message m) {
        try {
            if (p.c.isNhanban) {
                p.conn.sendMessageLog(Language.NOT_FOR_PHAN_THAN);
                return;
            }
            byte type = m.reader().readByte();

            byte index = m.reader().readByte();
            Item item = p.c.getIndexBag(index);
            if (item == null || m.reader().available() > 18) {
                return;
            }
            if (item.upgrade >= item.getUpMax()) {
                p.conn.sendMessageLog("Trang bị đã đạt cấp tối đa");
                return;
            }
            byte[] arrItem = new byte[m.reader().available()];
            int crys = 0;
            boolean keep = false;
            boolean da = false;
            byte i;
            byte index2;
            Item item2;
            ItemTemplate data;
            for (i = 0; i < arrItem.length; ++i) {
                arrItem[i] = -1;
                index2 = m.reader().readByte();
                item2 = p.c.getIndexBag(index2);
                if (item2 != null) {
                    data = ItemTemplate.ItemTemplateId(item2.id);
                    if (data.type == 26) {
                        arrItem[i] = index2;
                        crys += GameSrc.crystals[item2.id];
                        da = true;
                    }
                    else {
                        if (data.type != 28) {
                            p.conn.sendMessageLog("Chỉ có thể chọn đá và bảo hiểm");
                            return;
                        }
                        arrItem[i] = index2;
                        if (item2.id == 242 && item.upgrade < 8) {
                            keep = true;
                        }
                        else if (item2.id == 284 && item.upgrade < 12) {
                            keep = true;
                        }
                        else if (item2.id == 285 && item.upgrade < 14) {
                            keep = true;
                        }
                        else {
                            if (item2.id != 475) {
                                p.conn.sendMessageLog("Bảo hiểm không phù hợp");
                                return;
                            }
                            keep = true;
                        }
                    }
                }
            }
            m.cleanup();
            data = ItemTemplate.ItemTemplateId(item.id);
            int gold = 0;
            if (arrItem.length == 0 || data.type > 10) {
                return;
            }
            if (!da) {
                p.conn.sendMessageLog("Hãy chọn thêm đá");
                return;
            }
            int coins;
            int percen;
            if (data.type == 1) {
                coins = GameSrc.coinUpWeapons[item.upgrade];
                percen = crys * 100 / GameSrc.upWeapon[item.upgrade];
                if (percen > GameSrc.maxPercents[item.upgrade]) {
                    percen = GameSrc.maxPercents[item.upgrade];
                }
            }
            else if (data.type % 2 == 0) {
                coins = GameSrc.coinUpClothes[item.upgrade];
                percen = crys * 100 / GameSrc.upClothe[item.upgrade];
                if (percen > GameSrc.maxPercents[item.upgrade]) {
                    percen = GameSrc.maxPercents[item.upgrade];
                }
            }
            else {
                coins = GameSrc.coinUpAdorns[item.upgrade];
                percen = crys * 100 / GameSrc.upAdorn[item.upgrade];
                if (percen > GameSrc.maxPercents[item.upgrade]) {
                    percen = GameSrc.maxPercents[item.upgrade];
                }
            }
            if (type == 1) {
                percen += percen/2;
                gold = GameSrc.goldUps[item.upgrade];
            }
            if (coins/1000 > (p.c.yen + p.c.xu)/1000 || gold > p.luong) {
                return;
            }
            byte j;
            for (j = 0; j < arrItem.length; ++j) {
                if (arrItem[j] != -1) {
                    p.c.ItemBag[arrItem[j]] = null;
                }
            }
            p.upluong(-gold);
            if (coins <= p.c.yen) {
                p.c.upyen(-coins);
            }
            else if (coins >= p.c.yen) {
                int coin = coins - p.c.yen;
                p.c.upyen(-p.c.yen);
                p.c.upxu(-coin);
            }
            boolean suc = false;
            if(item.upgrade <= 8) {
                suc = Util.nextInt(1, 100) <= percen;
            } else {
                suc = Util.nextInt(1, 150) <= percen;
            }

            item.isLock = true;
            Util.Debug("type " + type + " index " + index + " percen " + percen);
            if (suc) {
                item.upgradeNext((byte)1);
            }
            else if (!keep) {
                item.upgradeNext((byte)(-(item.upgrade - KeepUpgrade(item.upgrade))));
            }
            m = new Message(21);
            m.writer().writeByte(suc ? 1 : 0);
            m.writer().writeInt(p.luong);
            m.writer().writeInt(p.c.xu);
            m.writer().writeInt(p.c.yen);
            m.writer().writeByte(item.upgrade);
            m.writer().flush();
            p.conn.sendMessage(m);
            m.cleanup();
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void Split(Player p, Message m) {
        try {
            if (p.c.isNhanban) {
                p.conn.sendMessageLog(Language.NOT_FOR_PHAN_THAN);
                return;
            }
            byte index = m.reader().readByte();
            m.cleanup();
            Item item = p.c.getIndexBag(index);
            if (item == null || item.upgrade <= 0) {
                return;
            }
            ItemTemplate data = ItemTemplate.ItemTemplateId(item.id);
            if (data.type > 10) {
                p.conn.sendMessageLog("Không thể phân tách vật phẩm này");
                return;
            }
            int num = 0;
            if (data.type == 1) {
                for (byte i = 0; i < item.upgrade; ++i) {
                    num += GameSrc.upWeapon[i];
                }
            }
            else if (data.type % 2 == 0) {
                for (byte i = 0; i < item.upgrade; ++i) {
                    num += GameSrc.upClothe[i];
                }
            }
            else {
                for (byte i = 0; i < item.upgrade; ++i) {
                    num += GameSrc.upAdorn[i];
                }
            }
            num /= 2;
            int num2 = 0;
            Item[] arrItem = new Item[24];
            for (int n = GameSrc.crystals.length - 1; n >= 0; --n) {
                if (num >= GameSrc.crystals[n]) {
                    arrItem[num2] = new Item();
                    arrItem[num2].id = (short)n;
                    arrItem[num2].isLock = item.isLock;
                    num -= GameSrc.crystals[n];
                    n++;
                    num2++;
                }
            }
            if (num2 > p.c.getBagNull()) {
                p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                return;
            }
            byte[] arrIndex = new byte[arrItem.length];
            for (byte j = 0; j < arrItem.length; ++j) {
                if (arrItem[j] != null) {
                    byte index2 = p.c.getIndexBagNotItem();
                    p.c.ItemBag[index2] = arrItem[j];
                    arrIndex[j] = index2;
                }
            }
            item.upgradeNext((byte)(-item.upgrade));
            m = new Message(22);
            m.writer().writeByte(num2);
            for (byte j = 0; j < num2; ++j) {
                if (arrItem[j] != null) {
                    m.writer().writeByte(arrIndex[j]);
                    m.writer().writeShort(arrItem[j].id);
                }
            }
            m.writer().flush();
            p.conn.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void LuckValue(Player p, Message m) {
        try {
            byte index = m.reader().readByte();
            m.cleanup();
            if (index < 0 || index > 8) {
                index = 0;
            }
            if (p.c.getBagNull() == 0) {
                p.conn.sendMessageLog("Hành trang không đủ chỗ trống.");
                return;
            }
            if (p.c.quantityItemyTotal(340) == 0) {
                p.conn.sendMessageLog("Cần có phiếu may mắn (mua tại NPC Goosho).");
                return;
            }

            if(p.c.isTaskDanhVong == 1 && p.c.taskDanhVong[0] == 4) {
                p.c.taskDanhVong[1]++;
                if(p.c.taskDanhVong[1] == p.c.taskDanhVong[2]) {
                    p.sendAddchatYellow("Bạn đã hoàn thành nhiệm vụ danh vọng.");
                }
            }

            p.c.removeItemBags(340, 1);
            short id = GameSrc.ArridLuck[Util.nextInt(GameSrc.ArridLuck.length)];
            switch (id) {
                case -1: {
                    if(p.c.gender == 1) {
                        id = 741;
                    } else {
                        id = 768;
                    }
                    break;
                }
                case -2: {
                    if(p.c.gender == 1) {
                        id = (short)Util.nextInt(733,734);
                    } else {
                        id = (short)Util.nextInt(760,761);
                    }
                    break;
                }
                case -3: {
                    if(p.c.gender == 1) {
                        id = (short)Util.nextInt(737,738);
                    } else {
                        id = (short)Util.nextInt(764,765);
                    }
                    break;
                }
                case -4: {
                    if(p.c.gender == 1) {
                        id = (short)Util.nextInt(739,740);
                    } else {
                        id = (short)Util.nextInt(766,767);
                    }
                    break;
                }
            }

            ItemTemplate data = ItemTemplate.ItemTemplateId(id);
            Item item;
            if (data.type < 10) {
                if (data.type == 1) {
                    item = ItemTemplate.itemDefault(id);
                    item.sys = SysClass(data.nclass);
                }
                else {
                    byte sys = (byte)Util.nextInt(1, 3);
                    item = ItemTemplate.itemDefault(id, sys);
                }
            }
            else {
                item = ItemTemplate.itemDefault(id);
            }
            if (id == 523 || id == 419) {
                item.isExpires = true;
                item.expires = Util.TimeDay(GameSrc.ArrdayLuck[Util.nextInt(GameSrc.ArrdayLuck.length)]);
            }
            if (data.type != 19) {
                if(item.id == 733 || item.id == 734 || item.id == 760 || item.id == 761 || (item.id >= 737 && item.id <= 741) || (item.id >= 764 && item.id <= 768) ) {
                    item.quantity = 1;
                    item.isLock = true;
                    item.isExpires = false;
                    item.expires = -1L;
                }
                p.c.addItemBag(true, item);
            }
//        if ( id == 8 || id == 9 || id == 11 || id == 343 || id == 344 || id == 345 || id == 346 || id == 403 || id == 404 || id == 405 || id == 406 || id == 407 || id == 408 || id == 419) {
//            Manager manager = GameSrc.server.manager;
//            Manager.chatKTG(p.c.name + " tham gia lật bài may mắn nhận được" + ((item.quantity > 1) ? (" " + item.quantity + " ") : " ") + data.name);
//        }
            m = new Message(-28);
            m.writer().writeByte(-72);
            byte i;
            for (i = 0; i < 9; ++i) {
                if (i == index) {
                    m.writer().writeShort(id);
                }
                else {
                    m.writer().writeShort(GameSrc.ArridLuck[Util.nextInt(GameSrc.ArridLuck.length)]);
                }
            }
            m.writer().flush();
            p.conn.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void LuyenThach(Player p, Message m) throws IOException {
        byte[] arrItem = new byte[m.reader().available()];

        Item item = null;
        int checkTTS = 0;
        int checkTTT = 0;

        p.endDlg(true);

        if(arrItem.length == 4) {
            byte i;
            byte index2;
            for(i=0; i<arrItem.length; i++) {
                index2 = m.reader().readByte();
                item = p.c.getIndexBag(index2);
                if(item.id == 455) {
                    checkTTS++;
                    checkTTT=0;
                } else if(item.id == 456) {
                    checkTTT++;
                    checkTTS=0;
                }
                p.c.removeItemBag(index2, 1);
            }
            if(checkTTS>0) {
                p.c.addItemBag(false, ItemTemplate.itemDefault(456));
            } else if(checkTTT>0){
                p.c.addItemBag(false, ItemTemplate.itemDefault(457));
            }
            return;

        } else if (arrItem.length == 9) {
            byte i;
            byte index2;
            for(i=0; i<arrItem.length; i++) {
                index2 = m.reader().readByte();
                if(i==0) {
                    item = p.c.getIndexBag(index2);
                }
                p.c.removeItemBag(index2, 1);
            }

            if(item.id == 455) {
                p.c.addItemBag(false, ItemTemplate.itemDefault(456));
            } else if(item.id == 456){
                p.c.addItemBag(false, ItemTemplate.itemDefault(457));
            }
            return;
        }
        m.cleanup();

    }

    public static void TinhLuyen(Player p, Message m) {
        try {
            byte index = m.reader().readByte();
            Item it = p.c.getIndexBag(index);
            if (it == null) {
                return;
            }
            ItemTemplate data = ItemTemplate.ItemTemplateId(it.id);
            int tl = -1;
            byte i;
            for (i = 0; i < it.options.size(); ++i) {
                if (it.options.get(i).id == 85) {
                    tl = it.options.get(i).param;
                    if (tl >= 9) {
                        p.conn.sendMessageLog("Vật phẩm đã đạt cấp tinh luyện tối đa.");
                        return;
                    }
                }
            }
            if (tl == -1) {
                p.conn.sendMessageLog("Vật phẩm không dùng cho tinh luyện.");
                return;
            }
            int ttts = 0;
            int tttt = 0;
            int tttc = 0;
            byte[] arit = new byte[m.reader().available()];
            byte j;
            byte ind;
            Item item;
            for (j = 0; j < arit.length; ++j) {
                ind = m.reader().readByte();
                item = p.c.getIndexBag(ind);
                if (item == null) {
                    return;
                }
                if (item.id != 455 && item.id != 456 && item.id != 457) {
                    p.conn.sendMessageLog("Vật phẩm không dùng cho tinh luyện.");
                    return;
                }
                arit[j] = ind;
                if (item.id == 455) {
                    ++ttts;
                }
                else if (item.id == 456) {
                    ++tttt;
                }
                else if (item.id == 457) {
                    ++tttc;
                }
            }
            m.cleanup();
            int percent = 0;
            int yen = 0;
            switch (tl) {
                case 0: {
                    percent = 60;
                    yen = 150000;
                    if (ttts != 3 || tttt != 0 || tttc != 0) {
                        p.conn.sendMessageLog("Tinh luyện cần 3 tử tinh thạch sơ.");
                        return;
                    }
                    break;
                }
                case 1: {
                    percent = 45;
                    yen = 247500;
                    if (ttts != 5 || tttt != 0 || tttc != 0) {
                        p.conn.sendMessageLog("Tinh luyện 2 cần dùng 5 tử tinh thạch sơ");
                        return;
                    }
                    break;
                }
                case 2: {
                    percent = 34;
                    yen = 408375;
                    if (ttts != 9 || tttt != 0 || tttc != 0) {
                        p.conn.sendMessageLog("Tinh luyện 3 cần dùng 9 tử tinh thạch sơ");
                        return;
                    }
                    break;
                }
                case 3: {
                    percent = 26;
                    yen = 673819;
                    if (ttts != 0 || tttt != 4 || tttc != 0) {
                        p.conn.sendMessageLog("Tinh luyện 4 cần dùng 4 tử tinh thạch trung");
                        return;
                    }
                    break;
                }
                case 4: {
                    percent = 20;
                    yen = 1111801;
                    if (ttts != 0 || tttt != 7 || tttc != 0) {
                        p.conn.sendMessageLog("Tinh luyện 5 cần dùng 7 tử tinh thạch trung");
                        return;
                    }
                    break;
                }
                case 5: {
                    percent = 15;
                    yen = 2056832;
                    if (ttts != 0 || tttt != 10 || tttc != 0) {
                        p.conn.sendMessageLog("Tinh luyện 6 cần dùng 10 tử tinh thạch trung");
                        return;
                    }
                    break;
                }
                case 6: {
                    percent = 11;
                    yen = 4010922;
                    if (ttts != 0 || tttt != 0 || tttc != 5) {
                        p.conn.sendMessageLog("Tinh luyện 7 cần dùng 5 tử tinh thạch cao");
                        return;
                    }
                    break;
                }
                case 7: {
                    percent = 8;
                    yen = 7420021;
                    if (ttts != 0 || tttt != 0 || tttc != 7) {
                        p.conn.sendMessageLog("Tinh luyện 8 cần dùng 7 tử tinh thạch cao");
                        return;
                    }
                    break;
                }
                case 8: {
                    percent = 6;
                    yen = 12243035;
                    if (ttts != 0 || tttt != 0 || tttc != 9) {
                        p.conn.sendMessageLog("Tinh luyện 9 cần dùng 9 tử tinh thạch cao");
                        return;
                    }
                    break;
                }
                default: {
                    return;
                }
            }
            if (yen > p.c.yen) {
                p.conn.sendMessageLog("Không đủ yên để Tinh luyện");
                return;
            }
            p.endLoad(true);
            p.c.upyenMessage(-yen);

            if (percent >= Util.nextInt(140)) {
                Option option;
                for (j = 0; j < it.options.size(); ++j) {
                    option = it.options.get(j);
                    option.param += ItemTemplate.ThinhLuyenParam(it.options.get(j).id, tl);
                }
                Service.requestItemInfoMessage(p, it, index, 3);
                p.sendAddchatYellow("Tinh luyện thành công!");
            }
            else {
                p.sendAddchatYellow("Tinh luyện thất bại!");
            }
            for (j = 0; j < arit.length; ++j) {
                p.c.removeItemBag(arit[j], 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void DichChuyen(Player p, Message m) throws IOException {
        byte index = m.reader().readByte();
        Item item = p.c.getIndexBag(index);
        if (item != null && ItemTemplate.isTypeBody(item.id) && item.upgrade > 11) {
            byte i;
            for (i = 0; i < item.options.size(); ++i) {
                if (item.options.get(i).id == 85) {
                    p.conn.sendMessageLog("Vật phẩm đã được dịch chuyển.");
                    return;
                }
            }
            byte[] arrIndex = new byte[20];
            byte index2;
            Item item2;
            for (i = 0; i < arrIndex.length; ++i) {
                index2 = m.reader().readByte();
                item2 = p.c.getIndexBag(index2);
                if (item2 == null || item2.id != 454) {
                    return;
                }
                arrIndex[i] = index2;
            }
            m.cleanup();
            p.endLoad(true);
            ItemTemplate data = ItemTemplate.ItemTemplateId(item.id);
            item.options.add(new Option(85, 0));
            switch (data.type) {
                case 0: {
                    if (item.sys == 1) {
                        item.options.add(new Option(96, 10));
                    }
                    else if (item.sys == 2) {
                        item.options.add(new Option(95, 10));
                    }
                    else if (item.sys == 3) {
                        item.options.add(new Option(97, 10));
                    }
                    item.options.add(new Option(79, 5));
                    break;
                }
                case 1: {
                    item.options.add(new Option(87, Util.nextInt(250, 400)));
                    item.options.add(new Option(87 + item.sys, Util.nextInt(350, 600)));
                    break;
                }
                case 2: {
                    item.options.add(new Option(80, Util.nextInt(20, 50)));
                    item.options.add(new Option(91, Util.nextInt(9, 11)));
                    break;
                }
                case 3: {
                    item.options.add(new Option(81, 5));
                    item.options.add(new Option(79, 5));
                    break;
                }
                case 4: {
                    item.options.add(new Option(86, Util.nextInt(80, 124)));
                    item.options.add(new Option(94, Util.nextInt(80, 124)));
                    break;
                }
                case 5: {
                    if (item.sys == 1) {
                        item.options.add(new Option(96, 5));
                    }
                    else if (item.sys == 2) {
                        item.options.add(new Option(95, 5));
                    }
                    else if (item.sys == 3) {
                        item.options.add(new Option(97, 5));
                    }
                    item.options.add(new Option(92, Util.nextInt(9, 11)));
                    break;
                }
                case 6: {
                    item.options.add(new Option(83, Util.nextInt(350, 600)));
                    item.options.add(new Option(82, Util.nextInt(350, 600)));
                    break;
                }
                case 7: {
                    if (item.sys == 1) {
                        item.options.add(new Option(96, 5));
                    }
                    else if (item.sys == 2) {
                        item.options.add(new Option(95, 5));
                    }
                    else if (item.sys == 3) {
                        item.options.add(new Option(97, 5));
                    }
                    item.options.add(new Option(87 + item.sys, Util.nextInt(350, 600)));
                    break;
                }
                case 8: {
                    item.options.add(new Option(82, Util.nextInt(350, 600)));
                    item.options.add(new Option(84, Util.nextInt(90, 100)));
                    break;
                }
                case 9: {
                    item.options.add(new Option(84, Util.nextInt(90, 100)));
                    item.options.add(new Option(83, Util.nextInt(350, 600)));
                    break;
                }
                default: {
                    return;
                }
            }
            for (i = 0; i < arrIndex.length; ++i) {
                p.c.removeItemBag(arrIndex[i], 1);
            }
            p.sendAddchatYellow("Đã dịch chuyển trang bị.");
            Service.requestItemInfoMessage(p, item, index, 3);
        }
        Util.Debug(index + " " + item.id);
    }

    public static void luyenNgoc(Player p, Message m) throws IOException {
        byte index = m.reader().readByte();
        switch ((int) index) {
            //khảm
            case 0: {
                byte indexItem = m.reader().readByte();
                Item item = p.c.getIndexBag(indexItem);
                int exp = 0;
                if (item != null) {
                    ItemTemplate data = ItemTemplate.ItemTemplateId(item.id);
                    int bach = data.type;
                    byte[] arrItem = new byte[m.reader().available()];
                    byte indexItemSub = m.reader().readByte();
                    arrItem[0] = indexItemSub;
                    Item itemSub = p.c.getIndexBag(indexItemSub);
                    if (checkTonTaiNgoc(itemSub, item)) {
                        p.conn.sendMessageLog("Không thể khảm cùng 1 loại ngọc trên 1 vật phẩm");
                        return;
                    }
                    if(data.level > p.c.level) {
                        p.conn.sendMessageLog("Level của bạn chưa đủ để khảm ngọc này");
                        return;
                    }
                    int money = 0;
                    for (int i = 0; i < itemSub.options.size(); i++) {
                        if (itemSub.options.get(i).id == 123) {
                            money = itemSub.options.get(i).param;
                        }
                    }
                    int type = 3;
                    int money2 = p.c.yen - money;
                    if (money <= p.c.yen) {
                        p.c.upyen(-money);
                    } else if (money >= p.c.yen) {
                        int coin = money - p.c.yen;
                        if (coin > p.c.xu) {
                            p.conn.sendMessageLog("Không đủ xu và yên để khảm ngọc.");
                            return;
                        }
                        p.c.upyen(-p.c.yen);
                        p.c.upxu(-coin);
                    }
                    Util.Debug("money2" + money2);
                    int loai = 0;
                    int loai1 = 0;
                    int loai2 = 0;
                    int indextemp = 0;
                    int yenThaoNgoc = 0;
                    int isKhamNgoc = 0;
                    for (byte i = 1; i < arrItem.length; i++) {
                        arrItem[i] = m.reader().readByte();
                    }
                    for (byte i = 0; i < arrItem.length; i++) {
                        p.c.removeItemBag(arrItem[i], 1);
                    }
                    for (int i = 0; i < item.options.size(); i++) {
                        if (item.options.get(i).id == 122) {
                            yenThaoNgoc = item.options.get(i).param;
                            item.options.remove(i);
                            isKhamNgoc++;
                        }
                    }

                    switch (data.type) {
                        case 0: {
                            loai = 107;
                            loai1 = 106;
                            loai2 = 108;
                            break;
                        }
                        case 1: {
                            loai = 106;
                            loai1 = 107;
                            loai2 = 108;
                            break;
                        }
                        case 2: {
                            loai = 107;
                            loai1 = 106;
                            loai2 = 108;
                            break;
                        }
                        case 3: {
                            loai = 108;
                            loai1 = 106;
                            loai2 = 107;
                            break;
                        }
                        case 4: {
                            loai = 107;
                            loai1 = 106;
                            loai2 = 108;
                            break;
                        }
                        case 5: {
                            loai = 108;
                            loai1 = 106;
                            loai2 = 107;
                            break;
                        }
                        case 6: {
                            loai = 107;
                            loai1 = 106;
                            loai2 = 108;
                            break;
                        }
                        case 7: {
                            loai = 108;
                            loai1 = 106;
                            loai2 = 107;
                            break;
                        }
                        case 8: {
                            loai = 107;
                            loai1 = 106;
                            loai2 = 108;
                            break;
                        }
                        case 9: {
                            loai = 108;
                            loai1 = 106;
                            loai2 = 107;
                            break;
                        }
                    }
                    switch ((int) itemSub.id) {
                        case 655: {
                            indextemp = 0;
                            int[] temp = new int[]{106, 107, 108};
                            for (int j = 0; j < temp.length; j++) {
                                if (temp[j] == 106) {
                                    for (int i = 0; i < itemSub.options.size(); i++) {
                                        if (itemSub.options.get(i).id == loai && itemSub.options.get(i).id == temp[j]) {
                                            item.options.add(new Option(112, 0));
                                            item.options.add(new Option(itemSub.options.get(i + 1).id, itemSub.options.get(i + 1).param));
                                            item.options.add(new Option(itemSub.options.get(i + 2).id, itemSub.options.get(i + 2).param));
                                        } else if (itemSub.options.get(i).id == temp[j]) {
                                            item.options.add(new Option(indextemp - 1, Integer.parseInt(itemSub.upgrade + "" + itemSub.options.get(i).id)));
                                            item.options.add(new Option(indextemp - 2, Integer.parseInt(itemSub.options.get(i + 1).param + "" + itemSub.options.get(i + 1).id)));
                                            item.options.add(new Option(indextemp - 3, Integer.parseInt(itemSub.options.get(i + 2).param + "" + itemSub.options.get(i + 2).id)));
                                        }
                                    }
                                }
                                if (temp[j] == 107) {
                                    for (int i = 0; i < itemSub.options.size(); i++) {
                                        if (itemSub.options.get(i).id == loai && itemSub.options.get(i).id == temp[j]) {
                                            item.options.add(new Option(112, 0));
                                            item.options.add(new Option(itemSub.options.get(i + 1).id, itemSub.options.get(i + 1).param));
                                            item.options.add(new Option(itemSub.options.get(i + 2).id, itemSub.options.get(i + 2).param));
                                        } else if (itemSub.options.get(i).id == temp[j]) {
                                            item.options.add(new Option(indextemp - 4, Integer.parseInt(itemSub.upgrade + "" + itemSub.options.get(i).id)));
                                            item.options.add(new Option(indextemp - 5, Integer.parseInt(itemSub.options.get(i + 1).param + "" + itemSub.options.get(i + 1).id)));
                                            item.options.add(new Option(indextemp - 6, Integer.parseInt(itemSub.options.get(i + 2).param + "" + itemSub.options.get(i + 2).id)));
                                        }
                                    }
                                }
                                if (temp[j] == 108) {
                                    for (int i = 0; i < itemSub.options.size(); i++) {
                                        if (itemSub.options.get(i).id == loai && itemSub.options.get(i).id == temp[j]) {
                                            item.options.add(new Option(112, 0));
                                            item.options.add(new Option(itemSub.options.get(i + 1).id, itemSub.options.get(i + 1).param));
                                            item.options.add(new Option(itemSub.options.get(i + 2).id, itemSub.options.get(i + 2).param));
                                        } else if (itemSub.options.get(i).id == temp[j]) {
                                            item.options.add(new Option(indextemp - 7, Integer.parseInt(itemSub.upgrade + "" + itemSub.options.get(i).id)));
                                            item.options.add(new Option(indextemp - 8, Integer.parseInt(itemSub.options.get(i + 1).param + "" + itemSub.options.get(i + 1).id)));
                                            item.options.add(new Option(indextemp - 9, Integer.parseInt(itemSub.options.get(i + 2).param + "" + itemSub.options.get(i + 2).id)));
                                        }
                                    }
                                }
                            }

                            for (int i = 0; i < itemSub.options.size(); i++) {
                                if (itemSub.options.get(i).id == 104) {
                                    item.options.add(new Option(indextemp - 10, itemSub.options.get(i).param));
                                } else if (itemSub.options.get(i).id == 123) {
                                    item.options.add(new Option(indextemp - 11, itemSub.options.get(i).param));
                                    yenThaoNgoc += itemSub.options.get(i).param * 2;
                                }
                            }
                            item.options.add(new Option(indextemp - 12, itemSub.upgrade));
                            item.options.add(new Option(indextemp - 13, itemSub.saleCoinLock));
                            break;
                        }
                        case 654: {
                            indextemp = -15;
                            int[] temp1 = new int[]{106, 107, 108};
                            for (int j = 0; j < temp1.length; j++) {
                                if (temp1[j] == 106) {
                                    for (int i = 0; i < itemSub.options.size(); i++) {
                                        if (itemSub.options.get(i).id == loai && itemSub.options.get(i).id == temp1[j]) {
                                            item.options.add(new Option(111, 0));
                                            item.options.add(new Option(itemSub.options.get(i + 1).id, itemSub.options.get(i + 1).param));
                                            item.options.add(new Option(itemSub.options.get(i + 2).id, itemSub.options.get(i + 2).param));
                                        } else if (itemSub.options.get(i).id == temp1[j]) {
                                            item.options.add(new Option(indextemp - 1, Integer.parseInt(itemSub.upgrade + "" + itemSub.options.get(i).id)));
                                            item.options.add(new Option(indextemp - 2, Integer.parseInt(itemSub.options.get(i + 1).param + "" + itemSub.options.get(i + 1).id)));
                                            item.options.add(new Option(indextemp - 3, Integer.parseInt(itemSub.options.get(i + 2).param + "" + itemSub.options.get(i + 2).id)));
                                        }
                                    }
                                }
                                if (temp1[j] == 107) {
                                    for (int i = 0; i < itemSub.options.size(); i++) {
                                        if (itemSub.options.get(i).id == loai && itemSub.options.get(i).id == temp1[j]) {
                                            item.options.add(new Option(111, 0));
                                            item.options.add(new Option(itemSub.options.get(i + 1).id, itemSub.options.get(i + 1).param));
                                            item.options.add(new Option(itemSub.options.get(i + 2).id, itemSub.options.get(i + 2).param));
                                        } else if (itemSub.options.get(i).id == temp1[j]) {
                                            item.options.add(new Option(indextemp - 4, Integer.parseInt(itemSub.upgrade + "" + itemSub.options.get(i).id)));
                                            item.options.add(new Option(indextemp - 5, Integer.parseInt(itemSub.options.get(i + 1).param + "" + itemSub.options.get(i + 1).id)));
                                            item.options.add(new Option(indextemp - 6, Integer.parseInt(itemSub.options.get(i + 2).param + "" + itemSub.options.get(i + 2).id)));
                                        }
                                    }
                                }
                                if (temp1[j] == 108) {
                                    for (int i = 0; i < itemSub.options.size(); i++) {
                                        if (itemSub.options.get(i).id == loai && itemSub.options.get(i).id == temp1[j]) {
                                            item.options.add(new Option(111, 0));
                                            item.options.add(new Option(itemSub.options.get(i + 1).id, itemSub.options.get(i + 1).param));
                                            item.options.add(new Option(itemSub.options.get(i + 2).id, itemSub.options.get(i + 2).param));
                                        } else if (itemSub.options.get(i).id == temp1[j]) {
                                            item.options.add(new Option(indextemp - 7, Integer.parseInt(itemSub.upgrade + "" + itemSub.options.get(i).id)));
                                            item.options.add(new Option(indextemp - 8, Integer.parseInt(itemSub.options.get(i + 1).param + "" + itemSub.options.get(i + 1).id)));
                                            item.options.add(new Option(indextemp - 9, Integer.parseInt(itemSub.options.get(i + 2).param + "" + itemSub.options.get(i + 2).id)));
                                        }
                                    }
                                }
                            }

                            for (int i = 0; i < itemSub.options.size(); i++) {
                                if (itemSub.options.get(i).id == 104) {
                                    item.options.add(new Option(indextemp - 10, itemSub.options.get(i).param));
                                } else if (itemSub.options.get(i).id == 123) {
                                    item.options.add(new Option(indextemp - 11, itemSub.options.get(i).param));
                                    yenThaoNgoc += itemSub.options.get(i).param * 2;
                                }
                            }
                            item.options.add(new Option(indextemp - 12, itemSub.upgrade));
                            item.options.add(new Option(indextemp - 13, itemSub.saleCoinLock));
                            break;
                        }
                        case 653: {
                            indextemp = -30;
                            int[] temp2 = new int[]{106, 107, 108};
                            for (int j = 0; j < temp2.length; j++) {
                                if (temp2[j] == 106) {
                                    for (int i = 0; i < itemSub.options.size(); i++) {
                                        if (itemSub.options.get(i).id == loai && itemSub.options.get(i).id == temp2[j]) {
                                            item.options.add(new Option(110, 0));
                                            item.options.add(new Option(itemSub.options.get(i + 1).id, itemSub.options.get(i + 1).param));
                                            item.options.add(new Option(itemSub.options.get(i + 2).id, itemSub.options.get(i + 2).param));
                                        } else if (itemSub.options.get(i).id == temp2[j]) {
                                            item.options.add(new Option(indextemp - 1, Integer.parseInt(itemSub.upgrade + "" + itemSub.options.get(i).id)));
                                            item.options.add(new Option(indextemp - 2, Integer.parseInt(itemSub.options.get(i + 1).param + "" + itemSub.options.get(i + 1).id)));
                                            item.options.add(new Option(indextemp - 3, Integer.parseInt(itemSub.options.get(i + 2).param + "" + itemSub.options.get(i + 2).id)));
                                        }
                                    }
                                }
                                if (temp2[j] == 107) {
                                    for (int i = 0; i < itemSub.options.size(); i++) {
                                        if (itemSub.options.get(i).id == loai && itemSub.options.get(i).id == temp2[j]) {
                                            item.options.add(new Option(110, 0));
                                            item.options.add(new Option(itemSub.options.get(i + 1).id, itemSub.options.get(i + 1).param));
                                            item.options.add(new Option(itemSub.options.get(i + 2).id, itemSub.options.get(i + 2).param));
                                        } else if (itemSub.options.get(i).id == temp2[j]) {
                                            item.options.add(new Option(indextemp - 4, Integer.parseInt(itemSub.upgrade + "" + itemSub.options.get(i).id)));
                                            item.options.add(new Option(indextemp - 5, Integer.parseInt(itemSub.options.get(i + 1).param + "" + itemSub.options.get(i + 1).id)));
                                            item.options.add(new Option(indextemp - 6, Integer.parseInt(itemSub.options.get(i + 2).param + "" + itemSub.options.get(i + 2).id)));
                                        }
                                    }
                                }
                                if (temp2[j] == 108) {
                                    for (int i = 0; i < itemSub.options.size(); i++) {
                                        if (itemSub.options.get(i).id == loai && itemSub.options.get(i).id == temp2[j]) {
                                            item.options.add(new Option(110, 0));
                                            item.options.add(new Option(itemSub.options.get(i + 1).id, itemSub.options.get(i + 1).param));
                                            item.options.add(new Option(itemSub.options.get(i + 2).id, itemSub.options.get(i + 2).param));
                                        } else if (itemSub.options.get(i).id == temp2[j]) {
                                            item.options.add(new Option(indextemp - 7, Integer.parseInt(itemSub.upgrade + "" + itemSub.options.get(i).id)));
                                            item.options.add(new Option(indextemp - 8, Integer.parseInt(itemSub.options.get(i + 1).param + "" + itemSub.options.get(i + 1).id)));
                                            item.options.add(new Option(indextemp - 9, Integer.parseInt(itemSub.options.get(i + 2).param + "" + itemSub.options.get(i + 2).id)));
                                        }
                                    }
                                }
                            }

                            for (int i = 0; i < itemSub.options.size(); i++) {
                                if (itemSub.options.get(i).id == 104) {
                                    item.options.add(new Option(indextemp - 10, itemSub.options.get(i).param));
                                } else if (itemSub.options.get(i).id == 123) {
                                    item.options.add(new Option(indextemp - 11, itemSub.options.get(i).param));
                                    yenThaoNgoc += itemSub.options.get(i).param * 2;
                                }
                            }
                            item.options.add(new Option(indextemp - 12, itemSub.upgrade));
                            item.options.add(new Option(indextemp - 13, itemSub.saleCoinLock));
                            break;
                        }
                        case 652: {
                            indextemp = -45;
                            int[] temp3 = new int[]{106, 107, 108};
                            for (int j = 0; j < temp3.length; j++) {
                                if (temp3[j] == 106) {
                                    for (int i = 0; i < itemSub.options.size(); i++) {
                                        if (itemSub.options.get(i).id == loai && itemSub.options.get(i).id == temp3[j]) {
                                            item.options.add(new Option(109, 0));
                                            item.options.add(new Option(itemSub.options.get(i + 1).id, itemSub.options.get(i + 1).param));
                                            item.options.add(new Option(itemSub.options.get(i + 2).id, itemSub.options.get(i + 2).param));
                                        } else if (itemSub.options.get(i).id == temp3[j]) {
                                            item.options.add(new Option(indextemp - 1, Integer.parseInt(itemSub.upgrade + "" + itemSub.options.get(i).id)));
                                            item.options.add(new Option(indextemp - 2, Integer.parseInt(itemSub.options.get(i + 1).param + "" + itemSub.options.get(i + 1).id)));
                                            item.options.add(new Option(indextemp - 3, Integer.parseInt(itemSub.options.get(i + 2).param + "" + itemSub.options.get(i + 2).id)));
                                        }
                                    }
                                }
                                if (temp3[j] == 107) {
                                    for (int i = 0; i < itemSub.options.size(); i++) {
                                        if (itemSub.options.get(i).id == loai && itemSub.options.get(i).id == temp3[j]) {
                                            item.options.add(new Option(109, 0));
                                            item.options.add(new Option(itemSub.options.get(i + 1).id, itemSub.options.get(i + 1).param));
                                            item.options.add(new Option(itemSub.options.get(i + 2).id, itemSub.options.get(i + 2).param));
                                        } else if (itemSub.options.get(i).id == temp3[j]) {
                                            item.options.add(new Option(indextemp - 4, Integer.parseInt(itemSub.upgrade + "" + itemSub.options.get(i).id)));
                                            item.options.add(new Option(indextemp - 5, Integer.parseInt(itemSub.options.get(i + 1).param + "" + itemSub.options.get(i + 1).id)));
                                            item.options.add(new Option(indextemp - 6, Integer.parseInt(itemSub.options.get(i + 2).param + "" + itemSub.options.get(i + 2).id)));
                                        }
                                    }
                                }
                                if (temp3[j] == 108) {
                                    for (int i = 0; i < itemSub.options.size(); i++) {
                                        if (itemSub.options.get(i).id == loai && itemSub.options.get(i).id == temp3[j]) {
                                            item.options.add(new Option(109, 0));
                                            item.options.add(new Option(itemSub.options.get(i + 1).id, itemSub.options.get(i + 1).param));
                                            item.options.add(new Option(itemSub.options.get(i + 2).id, itemSub.options.get(i + 2).param));
                                        } else if (itemSub.options.get(i).id == temp3[j]) {
                                            item.options.add(new Option(indextemp - 7, Integer.parseInt(itemSub.upgrade + "" + itemSub.options.get(i).id)));
                                            item.options.add(new Option(indextemp - 8, Integer.parseInt(itemSub.options.get(i + 1).param + "" + itemSub.options.get(i + 1).id)));
                                            item.options.add(new Option(indextemp - 9, Integer.parseInt(itemSub.options.get(i + 2).param + "" + itemSub.options.get(i + 2).id)));
                                        }
                                    }
                                }
                            }

                            for (int i = 0; i < itemSub.options.size(); i++) {
                                if (itemSub.options.get(i).id == 104) {
                                    item.options.add(new Option(indextemp - 10, itemSub.options.get(i).param));
                                } else if (itemSub.options.get(i).id == 123) {
                                    item.options.add(new Option(indextemp - 11, itemSub.options.get(i).param));
                                    yenThaoNgoc += itemSub.options.get(i).param * 2;
                                }
                            }
                            item.options.add(new Option(indextemp - 12, itemSub.upgrade));
                            item.options.add(new Option(indextemp - 13, itemSub.saleCoinLock));

                            break;
                        }
                        default: {
                            break;
                        }

                    }
                    item.options.add(new Option(122, yenThaoNgoc));
                    m.cleanup();
                    m = new Message(21);
                    m.writer().writeByte(5);
                    m.writer().writeInt(p.luong);
                    m.writer().writeInt(p.c.xu);
                    m.writer().writeInt(money2);
                    m.writer().writeByte(item.upgrade);
                    m.writer().flush();
                    p.conn.sendMessage(m);
                    m.cleanup();
                    m = new Message(42);
                    m.writer().writeByte(3);
                    m.writer().writeByte(indexItem);
                    m.writer().writeLong(item.expires);
                    if (ItemTemplate.isTypeUIME(type)) {
                        m.writer().writeInt(item.saleCoinLock);
                    }
                    if (ItemTemplate.isTypeUIShop(type) || ItemTemplate.isTypeUIShopLock(type) || ItemTemplate.isTypeMounts(type) || ItemTemplate.isTypeUIStore(type) || ItemTemplate.isTypeUIBook(type) || ItemTemplate.isTypeUIFashion(type) || ItemTemplate.isTypeUIClanShop(type)) {
                        m.writer().writeInt(item.buyCoin);
                        m.writer().writeInt(item.buyCoinLock);
                        m.writer().writeInt(item.buyGold);
                    }
                    if (ItemTemplate.isTypeBody(item.id) || ItemTemplate.isTypeMounts(item.id) || ItemTemplate.isTypeNgocKham(item.id)) {
                        m.writer().writeByte(item.sys);//thuoc tinh
                        if (item.options != null) {
                            for (Option Option : item.options) {
                                if (Option.id > 0) {
                                    m.writer().writeByte(Option.id);
                                    m.writer().writeInt(Option.param);
                                }
                            }
                        }
                    }
                    m.writer().flush();
                    p.conn.sendMessage(m);
                    m.cleanup();
                }
                break;
            }
            //luyện
            case 1: {
                byte indexItemLuyenNgoc = m.reader().readByte();
                Item itemLuyenNgoc = p.c.getIndexBag(indexItemLuyenNgoc);
                int expLuyenNgoc = 0;
                int totalExp = 0;
                if (itemLuyenNgoc != null) {
                    if (itemLuyenNgoc.upgrade >= 10) {
                        p.conn.sendMessageLog("Ngọc đã đạt giới hạn tối đa");
                        return;
                    }
                    ItemTemplate dataItemLN = ItemTemplate.ItemTemplateId(itemLuyenNgoc.id);
                    if(dataItemLN.level > p.c.level ) {
                        p.conn.sendMessageLog("Level của bạn chưa đủ để luyện ngọc này");
                        return;
                    }
                    for (byte i = 0; i < itemLuyenNgoc.options.size(); i++) {
                        if (itemLuyenNgoc.options.get(i).id == 104) {
                            expLuyenNgoc = itemLuyenNgoc.options.get(i).param;
                        }
                    }
                    byte[] arrIndex = new byte[m.reader().available()];
                    int exp2 = 0;
                    for (byte i = 0; i < arrIndex.length; i++) {
                        byte index2 = m.reader().readByte();
                        Item item2 = p.c.getIndexBag(index2);
                        if (item2 != null) {
                            exp2 += arrNgocKhamEXP[item2.upgrade][1];
                        }
                        arrIndex[i] = index2;
                    }
                    totalExp = expLuyenNgoc + exp2;
                    for (byte i = 0; i < arrIndex.length; i++) {
                        p.c.removeItemBag(arrIndex[i], 1);
                    }
                }
                int upgrade = itemLuyenNgoc.upgrade;
                int isupgrade = 0;
                int chenhlech = 0;
                for (byte i = 1; i < arrLuyenNgocEXP.length; i++) {
                    if (totalExp > arrLuyenNgocEXP[i][1] && totalExp < arrLuyenNgocEXP[i][0]) {
                        if (upgrade < i) {
                            chenhlech = i - upgrade;
                            upgrade = i;
                            isupgrade++;

                            totalExp = totalExp - arrLuyenNgocEXP[i][1];
                        } else {
                            totalExp = totalExp;
                        }
                        break;
                    }
                }

                itemLuyenNgoc.upgrade = (byte) upgrade;
                int type = 3;
                m.cleanup();
                m = new Message(124);
                m.writer().writeByte(1);
                m.writer().writeInt(p.luong);
                m.writer().writeInt(p.c.xu);
                m.writer().writeInt(p.c.yen);
                m.writer().writeByte(upgrade);
                m.writer().flush();
                p.conn.sendMessage(m);
                m.cleanup();
                m = new Message(42);
                m.writer().writeByte(3);
                m.writer().writeByte(indexItemLuyenNgoc);
                m.writer().writeLong(itemLuyenNgoc.expires);
                if (ItemTemplate.isTypeUIME(type)) {
                    m.writer().writeInt(itemLuyenNgoc.saleCoinLock);
                }
                if (ItemTemplate.isTypeUIShop(type) || ItemTemplate.isTypeUIShopLock(type) || ItemTemplate.isTypeMounts(type) || ItemTemplate.isTypeUIStore(type) || ItemTemplate.isTypeUIBook(type) || ItemTemplate.isTypeUIFashion(type) || ItemTemplate.isTypeUIClanShop(type)) {
                    m.writer().writeInt(itemLuyenNgoc.buyCoin);
                    m.writer().writeInt(itemLuyenNgoc.buyCoinLock);
                    m.writer().writeInt(itemLuyenNgoc.buyGold);
                }
                if (ItemTemplate.isTypeBody(itemLuyenNgoc.id) || ItemTemplate.isTypeMounts(itemLuyenNgoc.id) || ItemTemplate.isTypeNgocKham(itemLuyenNgoc.id)) {
                    m.writer().writeByte(itemLuyenNgoc.sys);//thuoc tinh
                    int i = 0;
                    if (itemLuyenNgoc.options != null) {
                        for (Option Option : itemLuyenNgoc.options) {
                            m.writer().writeByte(Option.id);
                            if (Option.id == 104) {
                                m.writer().writeInt(totalExp);
                                itemLuyenNgoc.options.get(i).param = totalExp;
                            } else {
                                if (isupgrade == 1) {
                                    if (Option.id != 106 || Option.id != 107 || Option.id != 108 || Option.id != 104 || Option.id != 123) {
                                        int value = itemLuyenNgoc.options.get(i).param;
                                        if (value > 0 && value < 50) {
                                            value += Util.nextInt(value/4,value/3) ;
                                        } else if (value >= 50 && value < 300) {
                                            value += Util.nextInt(value/3,value/2) ;
                                        } else if (value >= 300) {
                                            value += Util.nextInt(value/2,value) ;
                                        } else if(value >= -50 && value < 0) {
                                            value -= Util.nextInt(20);
                                        } else if(value < -50) {
                                            value -= Util.nextInt(50,100);
                                        }
                                        itemLuyenNgoc.options.get(i).param = value;
                                        m.writer().writeInt(value);
                                    } else if (Option.id == 123) {
                                        int value = itemLuyenNgoc.options.get(i).param *2* chenhlech;
                                        itemLuyenNgoc.options.get(i).param = value;
                                        m.writer().writeInt(value);
                                    } else {
                                        m.writer().writeInt(Option.param);
                                    }
                                } else {
                                    m.writer().writeInt(Option.param);
                                }
                            }
                            i++;

                        }
                    }
                }
                m.writer().flush();
                p.conn.sendMessage(m);
                m.cleanup();
                p.sendAddchatYellow("Luyện ngọc thành công");
                break;
            }
            //Gọt
            case 2: {
                byte indexItemGotNgoc = m.reader().readByte();
                Item itemGotNgoc = p.c.getIndexBag(indexItemGotNgoc);
                int expGotNgoc = 0;
                ItemTemplate dataItemLN = ItemTemplate.ItemTemplateId(itemGotNgoc.id);
                if(dataItemLN.level > p.c.level ) {
                    p.conn.sendMessageLog("Level của bạn chưa đủ để gọt ngọc này");
                    return;
                }

                int money2 = p.c.xu - coinGotngoc[itemGotNgoc.upgrade];
                if (coinGotngoc[itemGotNgoc.upgrade] <= p.c.xu) {
                    p.c.upxu(-coinGotngoc[itemGotNgoc.upgrade]);
                } //
                else if (coinGotngoc[itemGotNgoc.upgrade] >= p.c.xu) {
                    int coin = coinGotngoc[itemGotNgoc.upgrade] - p.c.xu;
                    if (coin > p.c.yen) {
                        p.conn.sendMessageLog("Không đủ xu và yên để gọt ngọc");
                        return;
                    }
                    p.c.upxu(-p.c.xu);
                    p.c.upyen(-coin);
                }

                int typeGotNgoc = 3;
                m.cleanup();
                m = new Message(124);
                m.writer().writeByte(2);
                m.writer().writeInt(p.luong);
                m.writer().writeInt(p.c.xu);
                m.writer().writeInt(p.c.yen);
                m.writer().writeByte(itemGotNgoc.upgrade);
                m.writer().flush();
                p.conn.sendMessage(m);
                m.cleanup();
                m = new Message(42);
                m.writer().writeByte(3);
                m.writer().writeByte(indexItemGotNgoc);
                m.writer().writeLong(itemGotNgoc.expires);
                if (ItemTemplate.isTypeUIME(typeGotNgoc)) {
                    m.writer().writeInt(itemGotNgoc.saleCoinLock);
                }
                if (ItemTemplate.isTypeUIShop(typeGotNgoc) || ItemTemplate.isTypeUIShopLock(typeGotNgoc) || ItemTemplate.isTypeMounts(typeGotNgoc) || ItemTemplate.isTypeUIStore(typeGotNgoc) || ItemTemplate.isTypeUIBook(typeGotNgoc) || ItemTemplate.isTypeUIFashion(typeGotNgoc) || ItemTemplate.isTypeUIClanShop(typeGotNgoc)) {
                    m.writer().writeInt(itemGotNgoc.buyCoin);
                    m.writer().writeInt(itemGotNgoc.buyCoinLock);
                    m.writer().writeInt(itemGotNgoc.buyGold);
                }
                if (ItemTemplate.isTypeBody(itemGotNgoc.id) || ItemTemplate.isTypeMounts(itemGotNgoc.id) || ItemTemplate.isTypeNgocKham(itemGotNgoc.id)) {
                    m.writer().writeByte(itemGotNgoc.sys);//thuoc tinh
                    int i = 0;
                    if (itemGotNgoc.options != null) {
                        for (Option Option : itemGotNgoc.options) {
                            m.writer().writeByte(Option.id);
                            if (Option.id == 104) {
                                m.writer().writeInt(Option.param);
                            } else {
                                if (Option.id == 73 || Option.id == 105 || Option.id == 114 || Option.id == 115 || Option.id == 116 || Option.id == 117 || Option.id == 118 || Option.id == 120 || Option.id == 125 || Option.id == 126) {
                                    if (itemGotNgoc.options.get(i).param < -1) {
                                        int value = itemGotNgoc.options.get(i).param;
                                        if(value > -20) {
                                            value += Util.nextInt(1,10);
                                        }
                                        else if (value <= -20 && value > -100) {
                                            value += Util.nextInt(10,20);
                                        } else if (value <= -100 && value > -200) {
                                            value += Util.nextInt(20,40);
                                        } else if (value <= -200) {
                                            value += Util.nextInt(30,50);
                                        }
                                        if(value >= 0) {
                                            value = -1;
                                        }
                                        itemGotNgoc.options.get(i).param = value;
                                        m.writer().writeInt(value);
                                    } else {
                                        m.writer().writeInt(Option.param);
                                    }
                                } else {
                                    m.writer().writeInt(Option.param);
                                }
                            }
                            i++;
                        }
                    }
                }
                m.writer().flush();
                p.conn.sendMessage(m);
                m.cleanup();
                p.conn.sendMessageLog("Ngọc đã được gọt");
                break;
            }
            //Tháo
            case 3: {
                byte indexItemThaoNgoc = m.reader().readByte();
                Item itemThaoNgoc = p.c.getIndexBag(indexItemThaoNgoc);
                ItemTemplate data = ItemTemplate.ItemTemplateId(itemThaoNgoc.id);
                int expThaoNgoc = 0;
                Item itemNgoc = new Item();
                itemNgoc.isLock = true;
                int loai = 0;
                int loai1 = 0;
                int loai2 = 0;
                int indextemp = 0;
                switch (data.type) {
                    case 0:
                        loai = 107;
                        loai1 = 106;
                        loai2 = 108;
                        break;
                    case 1:
                        loai = 106;
                        loai1 = 107;
                        loai2 = 108;
                        break;
                    case 2:
                        loai = 107;
                        loai1 = 106;
                        loai2 = 108;
                        break;
                    case 3:
                        loai = 108;
                        loai1 = 106;
                        loai2 = 107;
                        break;
                    case 4:
                        loai = 107;
                        loai1 = 106;
                        loai2 = 108;
                        break;
                    case 5:
                        loai = 108;
                        loai1 = 106;
                        loai2 = 107;
                        break;
                    case 6:
                        loai = 107;
                        loai1 = 106;
                        loai2 = 108;
                        break;
                    case 7:
                        loai = 108;
                        loai1 = 106;
                        loai2 = 107;
                        break;
                    case 8:
                        loai = 107;
                        loai1 = 106;
                        loai2 = 108;
                        break;
                    case 9:
                        loai = 108;
                        loai1 = 106;
                        loai2 = 107;
                        break;
                }
                int[] temp = new int[]{106, 107, 108};
                int a = 0;
                for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                    if (itemThaoNgoc.options.get(i).id == 109 || itemThaoNgoc.options.get(i).id == 110 || itemThaoNgoc.options.get(i).id == 111 || itemThaoNgoc.options.get(i).id == 112) {
                        a++;
                    }
                }
                if (a == 0) {
                    p.conn.sendMessageLog("Vật phẩm này chưa được khảm ngọc.");
                    return;
                }
                int yenThaoNgoc = 0;
                for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                    if (itemThaoNgoc.options.get(i).id == 122) {
                        yenThaoNgoc = itemThaoNgoc.options.get(i).param;
//                        itemThaoNgoc.options.remove(i);
                    }
                }
                if (yenThaoNgoc <= p.c.yen) {
                    p.c.upyen(-yenThaoNgoc);
                }
                else if (yenThaoNgoc >= p.c.yen) {
                    int coin = yenThaoNgoc - p.c.yen;
                    if (coin > p.c.xu) {
                        p.conn.sendMessageLog("Không đủ xu và yên để tháo ngọc");
                        return;
                    }
                    p.c.upyen(-p.c.yen);
                    p.c.upxu(-coin);
                }
                int[] tempa = new int[a];
                a = 0;
                for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                    if (itemThaoNgoc.options.get(i).id == 109 || itemThaoNgoc.options.get(i).id == 110 || itemThaoNgoc.options.get(i).id == 111 || itemThaoNgoc.options.get(i).id == 112) {
                        tempa[a] = itemThaoNgoc.options.get(i).id;
                        a++;
                    }
                }
                int a2 = 0;
                for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                    if (itemThaoNgoc.options.get(i).id == 109 || itemThaoNgoc.options.get(i).id == 110 || itemThaoNgoc.options.get(i).id == 111 || itemThaoNgoc.options.get(i).id == 112 || itemThaoNgoc.options.get(i).id < 0) {
                        a2++;
                    }
                }
                int[] tempIndex = new int[a2];
                int[] tempIndexNagative = new int[a2];
                a2 = 0;
                for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                    if (itemThaoNgoc.options.get(i).id == 109 || itemThaoNgoc.options.get(i).id == 110 || itemThaoNgoc.options.get(i).id == 111 || itemThaoNgoc.options.get(i).id == 112 || itemThaoNgoc.options.get(i).id < 0) {
                        tempIndex[a2] = i;
                        tempIndexNagative[a2] = itemThaoNgoc.options.get(i).id;
                        a2++;
                    }
                }
                for (int ai = 0; ai < tempa.length; ai++) {
                    itemNgoc = new Item();
                    itemNgoc.isLock = false;
                    switch (tempa[ai]) {
                        case 112: {
                            indextemp = 0;
                            for (int j = 0; j < temp.length; j++) {
                                if (temp[j] == 106 && temp[j] == loai) {
                                    for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                        if (itemThaoNgoc.options.get(i).id == 112) {
                                            itemNgoc.options.add(new Option(loai, 0));
                                            itemNgoc.options.add(new Option(itemThaoNgoc.options.get(i + 1).id, itemThaoNgoc.options.get(i + 1).param));
                                            itemNgoc.options.add(new Option(itemThaoNgoc.options.get(i + 2).id, itemThaoNgoc.options.get(i + 2).param));
                                        }
                                    }
                                } else if (temp[j] == 106) {
                                    for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                        if (itemThaoNgoc.options.get(i).id == indextemp - 1) {
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i).param).substring(String.valueOf(itemThaoNgoc.options.get(i).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i).param).length() - 3))));
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 1).param).substring(String.valueOf(itemThaoNgoc.options.get(i + 1).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i + 1).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 1).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i + 1).param).length() - 3))));
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 2).param).substring(String.valueOf(itemThaoNgoc.options.get(i + 2).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i + 2).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 2).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i + 2).param).length() - 3))));
                                        }
                                    }
                                }
                                if (temp[j] == 107 && temp[j] == loai) {
                                    for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                        if (itemThaoNgoc.options.get(i).id == 112) {
                                            itemNgoc.options.add(new Option(loai, 0));
                                            itemNgoc.options.add(new Option(itemThaoNgoc.options.get(i + 1).id, itemThaoNgoc.options.get(i + 1).param));
                                            itemNgoc.options.add(new Option(itemThaoNgoc.options.get(i + 2).id, itemThaoNgoc.options.get(i + 2).param));
                                        }
                                    }
                                } else if (temp[j] == 107) {
                                    for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                        if (itemThaoNgoc.options.get(i).id == indextemp - 4) {
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i).param).substring(String.valueOf(itemThaoNgoc.options.get(i).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i).param).length() - 3))));
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 1).param).substring(String.valueOf(itemThaoNgoc.options.get(i + 1).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i + 1).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 1).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i + 1).param).length() - 3))));
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 2).param).substring(String.valueOf(itemThaoNgoc.options.get(i + 2).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i + 2).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 2).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i + 2).param).length() - 3))));
                                        }
                                    }
                                }
                                if (temp[j] == 108 && temp[j] == loai) {
                                    for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                        if (itemThaoNgoc.options.get(i).id == 112) {
                                            itemNgoc.options.add(new Option(loai, 0));
                                            itemNgoc.options.add(new Option(itemThaoNgoc.options.get(i + 1).id, itemThaoNgoc.options.get(i + 1).param));
                                            itemNgoc.options.add(new Option(itemThaoNgoc.options.get(i + 2).id, itemThaoNgoc.options.get(i + 2).param));
                                        }
                                    }
                                } else if (temp[j] == 108) {
                                    for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                        if (itemThaoNgoc.options.get(i).id == indextemp - 7) {
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i).param).substring(String.valueOf(itemThaoNgoc.options.get(i).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i).param).length() - 3))));
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 1).param).substring(String.valueOf(itemThaoNgoc.options.get(i + 1).param).length() - 2, String.valueOf(itemThaoNgoc.options.get(i + 1).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 1).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i + 1).param).length() - 2))));
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 2).param).substring(String.valueOf(itemThaoNgoc.options.get(i + 2).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i + 2).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 2).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i + 2).param).length() - 3))));
                                        }
                                    }
                                }
                            }
                            for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                if (itemThaoNgoc.options.get(i).id == indextemp - 10) {
                                    itemNgoc.options.add(new Option(104, itemThaoNgoc.options.get(i).param));
                                } else if (itemThaoNgoc.options.get(i).id == indextemp - 11) {
                                    itemNgoc.options.add(new Option(123, itemThaoNgoc.options.get(i).param));
                                } else if (itemThaoNgoc.options.get(i).id == indextemp - 12) {
                                    itemNgoc.upgrade = (byte) itemThaoNgoc.options.get(i).param;
                                } else if (itemThaoNgoc.options.get(i).id == indextemp - 13) {
                                    itemNgoc.saleCoinLock = itemThaoNgoc.options.get(i).param;
                                }
                            }
                            itemNgoc.id = 655;
                            p.c.addItemBag(false, itemNgoc);
                            break;
                        }
                        case 111: {
                            indextemp = -15;
                            for (int j = 0; j < temp.length; j++) {
                                if (temp[j] == 106 && temp[j] == loai) {
                                    for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                        if (itemThaoNgoc.options.get(i).id == 111) {
                                            itemNgoc.options.add(new Option(loai, 0));
                                            itemNgoc.options.add(new Option(itemThaoNgoc.options.get(i + 1).id, itemThaoNgoc.options.get(i + 1).param));
                                            itemNgoc.options.add(new Option(itemThaoNgoc.options.get(i + 2).id, itemThaoNgoc.options.get(i + 2).param));
                                        }
                                    }
                                } else if (temp[j] == 106) {
                                    for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                        if (itemThaoNgoc.options.get(i).id == indextemp - 1) {
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i).param).substring(String.valueOf(itemThaoNgoc.options.get(i).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i).param).length() - 3))));
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 1).param).substring(String.valueOf(itemThaoNgoc.options.get(i + 1).param).length() - 2, String.valueOf(itemThaoNgoc.options.get(i + 1).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 1).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i + 1).param).length() - 2))));
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 2).param).substring(String.valueOf(itemThaoNgoc.options.get(i + 2).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i + 2).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 2).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i + 2).param).length() - 3))));
                                        }
                                    }
                                }
                                if (temp[j] == 107 && temp[j] == loai) {
                                    for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                        if (itemThaoNgoc.options.get(i).id == 111) {
                                            itemNgoc.options.add(new Option(loai, 0));
                                            itemNgoc.options.add(new Option(itemThaoNgoc.options.get(i + 1).id, itemThaoNgoc.options.get(i + 1).param));
                                            itemNgoc.options.add(new Option(itemThaoNgoc.options.get(i + 2).id, itemThaoNgoc.options.get(i + 2).param));
                                        }
                                    }
                                } else if (temp[j] == 107) {
                                    for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                        if (itemThaoNgoc.options.get(i).id == indextemp - 4) {
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i).param).substring(String.valueOf(itemThaoNgoc.options.get(i).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i).param).length() - 3))));
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 1).param).substring(String.valueOf(itemThaoNgoc.options.get(i + 1).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i + 1).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 1).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i + 1).param).length() - 3))));
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 2).param).substring(String.valueOf(itemThaoNgoc.options.get(i + 2).param).length() - 2, String.valueOf(itemThaoNgoc.options.get(i + 2).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 2).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i + 2).param).length() - 2))));
                                        }
                                    }
                                }
                                if (temp[j] == 108 && temp[j] == loai) {
                                    for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                        if (itemThaoNgoc.options.get(i).id == 111) {
                                            itemNgoc.options.add(new Option(loai, 0));
                                            itemNgoc.options.add(new Option(itemThaoNgoc.options.get(i + 1).id, itemThaoNgoc.options.get(i + 1).param));
                                            itemNgoc.options.add(new Option(itemThaoNgoc.options.get(i + 2).id, itemThaoNgoc.options.get(i + 2).param));
                                        }
                                    }
                                } else if (temp[j] == 108) {
                                    for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                        if (itemThaoNgoc.options.get(i).id == indextemp - 7) {
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i).param).substring(String.valueOf(itemThaoNgoc.options.get(i).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i).param).length() - 3))));
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 1).param).substring(String.valueOf(itemThaoNgoc.options.get(i + 1).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i + 1).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 1).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i + 1).param).length() - 3))));
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 2).param).substring(String.valueOf(itemThaoNgoc.options.get(i + 2).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i + 2).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 2).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i + 2).param).length() - 3))));
                                        }
                                    }
                                }
                            }
                            for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                if (itemThaoNgoc.options.get(i).id == indextemp - 10) {
                                    itemNgoc.options.add(new Option(104, itemThaoNgoc.options.get(i).param));
                                } else if (itemThaoNgoc.options.get(i).id == indextemp - 11) {
                                    itemNgoc.options.add(new Option(123, itemThaoNgoc.options.get(i).param));
                                } else if (itemThaoNgoc.options.get(i).id == indextemp - 12) {
                                    itemNgoc.upgrade = (byte) itemThaoNgoc.options.get(i).param;
                                } else if (itemThaoNgoc.options.get(i).id == indextemp - 13) {
                                    itemNgoc.saleCoinLock = itemThaoNgoc.options.get(i).param;
                                }
                            }
                            itemNgoc.id = 654;
                            p.c.addItemBag(false, itemNgoc);
                            break;
                        }
                        case 110: {
                            indextemp = -30;
                            for (int j = 0; j < temp.length; j++) {
                                if (temp[j] == 106 && temp[j] == loai) {
                                    for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                        if (itemThaoNgoc.options.get(i).id == 110) {
                                            itemNgoc.options.add(new Option(loai, 0));
                                            itemNgoc.options.add(new Option(itemThaoNgoc.options.get(i + 1).id, itemThaoNgoc.options.get(i + 1).param));
                                            itemNgoc.options.add(new Option(itemThaoNgoc.options.get(i + 2).id, itemThaoNgoc.options.get(i + 2).param));
                                        }
                                    }
                                } else if (temp[j] == 106) {
                                    for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                        if (itemThaoNgoc.options.get(i).id == indextemp - 1) {
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i).param).substring(String.valueOf(itemThaoNgoc.options.get(i).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i).param).length() - 3))));
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 1).param).substring(String.valueOf(itemThaoNgoc.options.get(i + 1).param).length() - 2, String.valueOf(itemThaoNgoc.options.get(i + 1).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 1).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i + 1).param).length() - 2))));
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 2).param).substring(String.valueOf(itemThaoNgoc.options.get(i + 2).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i + 2).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 2).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i + 2).param).length() - 3))));
                                        }
                                    }
                                }
                                if (temp[j] == 107 && temp[j] == loai) {
                                    for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                        if (itemThaoNgoc.options.get(i).id == 110) {
                                            itemNgoc.options.add(new Option(loai, 0));
                                            itemNgoc.options.add(new Option(itemThaoNgoc.options.get(i + 1).id, itemThaoNgoc.options.get(i + 1).param));
                                            itemNgoc.options.add(new Option(itemThaoNgoc.options.get(i + 2).id, itemThaoNgoc.options.get(i + 2).param));
                                        }
                                    }
                                } else if (temp[j] == 107) {
                                    for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                        if (itemThaoNgoc.options.get(i).id == indextemp - 4) {
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i).param).substring(String.valueOf(itemThaoNgoc.options.get(i).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i).param).length() - 3))));
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 1).param).substring(String.valueOf(itemThaoNgoc.options.get(i + 1).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i + 1).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 1).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i + 1).param).length() - 3))));
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 2).param).substring(String.valueOf(itemThaoNgoc.options.get(i + 2).param).length() - 2, String.valueOf(itemThaoNgoc.options.get(i + 2).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 2).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i + 2).param).length() - 2))));
                                        }
                                    }
                                }
                                if (temp[j] == 108 && temp[j] == loai) {
                                    for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                        if (itemThaoNgoc.options.get(i).id == 110) {
                                            itemNgoc.options.add(new Option(loai, 0));
                                            itemNgoc.options.add(new Option(itemThaoNgoc.options.get(i + 1).id, itemThaoNgoc.options.get(i + 1).param));
                                            itemNgoc.options.add(new Option(itemThaoNgoc.options.get(i + 2).id, itemThaoNgoc.options.get(i + 2).param));
                                        }
                                    }
                                } else if (temp[j] == 108) {
                                    for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                        if (itemThaoNgoc.options.get(i).id == indextemp - 7) {
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i).param).substring(String.valueOf(itemThaoNgoc.options.get(i).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i).param).length() - 3))));
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 1).param).substring(String.valueOf(itemThaoNgoc.options.get(i + 1).param).length() - 2, String.valueOf(itemThaoNgoc.options.get(i + 1).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 1).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i + 1).param).length() - 2))));
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 2).param).substring(String.valueOf(itemThaoNgoc.options.get(i + 2).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i + 2).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 2).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i + 2).param).length() - 3))));
                                        }
                                    }
                                }
                            }
                            for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                if (itemThaoNgoc.options.get(i).id == indextemp - 10) {
                                    itemNgoc.options.add(new Option(104, itemThaoNgoc.options.get(i).param));
                                } else if (itemThaoNgoc.options.get(i).id == indextemp - 11) {
                                    itemNgoc.options.add(new Option(123, itemThaoNgoc.options.get(i).param));
                                } else if (itemThaoNgoc.options.get(i).id == indextemp - 12) {
                                    itemNgoc.upgrade = (byte) itemThaoNgoc.options.get(i).param;
                                } else if (itemThaoNgoc.options.get(i).id == indextemp - 13) {
                                    itemNgoc.saleCoinLock = itemThaoNgoc.options.get(i).param;
                                }
                            }
                            itemNgoc.id = 653;
                            p.c.addItemBag(false, itemNgoc);
                            break;
                        }
                        case 109: {
                            indextemp = -45;
                            for (int j = 0; j < temp.length; j++) {
                                if (temp[j] == 106 && temp[j] == loai) {
                                    for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                        if (itemThaoNgoc.options.get(i).id == 109) {
                                            itemNgoc.options.add(new Option(loai, 0));
                                            itemNgoc.options.add(new Option(itemThaoNgoc.options.get(i + 1).id, itemThaoNgoc.options.get(i + 1).param));
                                            itemNgoc.options.add(new Option(itemThaoNgoc.options.get(i + 2).id, itemThaoNgoc.options.get(i + 2).param));
                                        }
                                    }
                                } else if (temp[j] == 106) {
                                    for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                        if (itemThaoNgoc.options.get(i).id == indextemp - 1) {
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i).param).substring(String.valueOf(itemThaoNgoc.options.get(i).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i).param).length() - 3))));
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 1).param).substring(String.valueOf(itemThaoNgoc.options.get(i + 1).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i + 1).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 1).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i + 1).param).length() - 3))));
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 2).param).substring(String.valueOf(itemThaoNgoc.options.get(i + 2).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i + 2).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 2).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i + 2).param).length() - 3))));
                                        }
                                    }
                                }
                                if (temp[j] == 107 && temp[j] == loai) {
                                    for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                        if (itemThaoNgoc.options.get(i).id == 109) {
                                            itemNgoc.options.add(new Option(loai, 0));
                                            itemNgoc.options.add(new Option(itemThaoNgoc.options.get(i + 1).id, itemThaoNgoc.options.get(i + 1).param));
                                            itemNgoc.options.add(new Option(itemThaoNgoc.options.get(i + 2).id, itemThaoNgoc.options.get(i + 2).param));
                                        }
                                    }
                                } else if (temp[j] == 107) {
                                    for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                        if (itemThaoNgoc.options.get(i).id == indextemp - 4) {
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i).param).substring(String.valueOf(itemThaoNgoc.options.get(i).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i).param).length() - 3))));
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 1).param).substring(String.valueOf(itemThaoNgoc.options.get(i + 1).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i + 1).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 1).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i + 1).param).length() - 3))));
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 2).param).substring(String.valueOf(itemThaoNgoc.options.get(i + 2).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i + 2).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 2).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i + 2).param).length() - 3))));
                                        }
                                    }
                                }
                                if (temp[j] == 108 && temp[j] == loai) {
                                    for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                        if (itemThaoNgoc.options.get(i).id == 109) {
                                            itemNgoc.options.add(new Option(loai, 0));
                                            itemNgoc.options.add(new Option(itemThaoNgoc.options.get(i + 1).id, itemThaoNgoc.options.get(i + 1).param));
                                            itemNgoc.options.add(new Option(itemThaoNgoc.options.get(i + 2).id, itemThaoNgoc.options.get(i + 2).param));
                                        }
                                    }
                                } else if (temp[j] == 108) {
                                    for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                        if (itemThaoNgoc.options.get(i).id == indextemp - 7) {
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i).param).substring(String.valueOf(itemThaoNgoc.options.get(i).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i).param).length() - 3))));
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 1).param).substring(String.valueOf(itemThaoNgoc.options.get(i + 1).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i + 1).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 1).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i + 1).param).length() - 3))));
                                            itemNgoc.options.add(new Option(Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 2).param).substring(String.valueOf(itemThaoNgoc.options.get(i + 2).param).length() - 3, String.valueOf(itemThaoNgoc.options.get(i + 2).param).length())), Integer.valueOf(String.valueOf(itemThaoNgoc.options.get(i + 2).param).substring(0, String.valueOf(itemThaoNgoc.options.get(i + 2).param).length() - 3))));
                                        }
                                    }
                                }
                            }
                            for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                                if (itemThaoNgoc.options.get(i).id == indextemp - 10) {
                                    itemNgoc.options.add(new Option(104, itemThaoNgoc.options.get(i).param));
                                } else if (itemThaoNgoc.options.get(i).id == indextemp - 11) {
                                    itemNgoc.options.add(new Option(123, itemThaoNgoc.options.get(i).param));
                                } else if (itemThaoNgoc.options.get(i).id == indextemp - 12) {
                                    itemNgoc.upgrade = (byte) itemThaoNgoc.options.get(i).param;
                                } else if (itemThaoNgoc.options.get(i).id == indextemp - 13) {
                                    itemNgoc.saleCoinLock = itemThaoNgoc.options.get(i).param;
                                }
                            }
                            itemNgoc.id = 652;
                            p.c.addItemBag(false, itemNgoc);
                            break;
                        }
                    }
                }
                for (int i = 0; i < itemThaoNgoc.options.size(); i++) {
                    if (itemThaoNgoc.options.get(i).id == 122) {
//                        yenThaoNgoc = itemThaoNgoc.options.get(i).param;
                        itemThaoNgoc.options.remove(i);
                    }
                }
                for (int i = tempIndex.length - 1; i >= 0; i--) {
                    if (tempIndexNagative[i] == 109 || tempIndexNagative[i] == 110 || tempIndexNagative[i] == 111 || tempIndexNagative[i] == 112) {
                        itemThaoNgoc.options.remove(tempIndex[i] + 2);
                        itemThaoNgoc.options.remove(tempIndex[i] + 1);
                        itemThaoNgoc.options.remove(tempIndex[i]);
                    } else {
                        itemThaoNgoc.options.remove(tempIndex[i]);
                    }
                }

                int typeThaoNgoc = 3;
                m.cleanup();
                m = new Message(124);
                m.writer().writeByte(3);
                m.writer().writeInt(p.luong);
                m.writer().writeInt(p.c.xu);
                m.writer().writeInt(p.c.yen);
                m.writer().writeByte(itemThaoNgoc.upgrade);
                m.writer().flush();
                p.conn.sendMessage(m);
                m.cleanup();
                m = new Message(42);
                m.writer().writeByte(3);
                m.writer().writeByte(indexItemThaoNgoc);
                m.writer().writeLong(itemThaoNgoc.expires);
                if (ItemTemplate.isTypeUIME(typeThaoNgoc)) {
                    m.writer().writeInt(itemThaoNgoc.saleCoinLock);
                }
                if (ItemTemplate.isTypeUIShop(typeThaoNgoc) || ItemTemplate.isTypeUIShopLock(typeThaoNgoc) || ItemTemplate.isTypeMounts(typeThaoNgoc) || ItemTemplate.isTypeUIStore(typeThaoNgoc) || ItemTemplate.isTypeUIBook(typeThaoNgoc) || ItemTemplate.isTypeUIFashion(typeThaoNgoc) || ItemTemplate.isTypeUIClanShop(typeThaoNgoc)) {
                    m.writer().writeInt(itemThaoNgoc.buyCoin);
                    m.writer().writeInt(itemThaoNgoc.buyCoinLock);
                    m.writer().writeInt(itemThaoNgoc.buyGold);
                }
                if (ItemTemplate.isTypeBody(itemThaoNgoc.id) || ItemTemplate.isTypeMounts(itemThaoNgoc.id) || ItemTemplate.isTypeNgocKham(itemThaoNgoc.id)) {
                    m.writer().writeByte(itemThaoNgoc.sys);//thuoc tinh
                    int i = 0;
                    if (itemThaoNgoc.options != null) {
                        for (Option Option : itemThaoNgoc.options) {
                            m.writer().writeByte(Option.id);
                            m.writer().writeInt(Option.param);
                            i++;

                        }
                    }
                }
                m.writer().flush();
                p.conn.sendMessage(m);
                m.cleanup();
                    p.conn.sendMessageLog("Tháo ngọc thành công");
                break;
            }
            default: {
                break;
            }

        }
    }

    private static boolean checkTonTaiNgoc(Item itemsub, Item item) {
        switch (itemsub.id) {
            case 655: {
                for (int i = 0; i < item.options.size(); i++) {
                    if (item.options.get(i).id == 112) {
                        return true;
                    }
                }
                break;
            }
            case 654: {
                for (int i = 0; i < item.options.size(); i++) {
                    if (item.options.get(i).id == 111) {
                        return true;
                    }
                }
                break;
            }
            case 653: {
                for (int i = 0; i < item.options.size(); i++) {
                    if (item.options.get(i).id == 110) {
                        return true;
                    }

                }
                break;
            }
            case 652: {
                for (int i = 0; i < item.options.size(); i++) {
                    if (item.options.get(i).id == 109) {
                        return true;
                    }
                }
                break;
            }
            default: {
                return false;
            }
        }
        return false;

    }

    public static void ngocFeature(Player p, Message m) {
        try {
            if (p.c.get().isNhanban) {
                p.sendAddchatYellow("Tính năng ko dành cho phân thân");
                return;
            }
            byte type = m.reader().readByte();
            byte indexUI = m.reader().readByte();
            int yen;
            switch (type) {
                //Khảm
                case 0: {
                    byte ngocIndex = m.reader().readByte();
                    Item ngocItem = p.c.ItemBag[ngocIndex];
                    Item item = p.c.ItemBag[indexUI];
                    if(item.ngocs != null) {
                        for (Item itemN:item.ngocs) {
                            if(itemN.id == ngocItem.id) {
                                p.conn.sendMessageLog("Trang bị đã được khảm loại ngọc này trước đó rồi.");
                                return;
                            }
                        }
                    }
                    if (!item.getData().isTrangSuc() && !item.getData().isTrangPhuc() && !item.getData().isVuKhi()) {
                        p.conn.sendMessageLog("Trang bị không hỗ trợ");
                        return;
                    }
                    p.endLoad(true);
                    yen = 0;
                    for (Option op:ngocItem.options) {
                        if(op.id == 123) {
                            yen = op.param;
                            break;
                        }
                    }
                    if (p.c.yen < yen || yen <= 0) {
                        p.sendAddchatYellow("Không đủ yên để khảm");
                        return;
                    }
                    p.c.upyenMessage(-yen);
                    ItemTemplate data2 = item.getData();
                    int crys = 0;

                    byte index;
                    Item tone;
                    while (m.reader().available() > 0) {
                        index = m.reader().readByte();
                        tone = p.c.ItemBag[index];
                        p.c.removeItemBag(index);
                        crys += crystals[tone.id];
                    }

                    int coins;
                    int percent;
                    if (data2.type == 1) {
                        coins = coinUpWeapons[ngocItem.getUpgrade()];
                        percent = crys * 100 / upWeapon[ngocItem.getUpgrade()];
                        if (percent > maxPercents[ngocItem.getUpgrade()]) {
                            percent = maxPercents[ngocItem.getUpgrade()];
                        }
                    } else if (data2.type % 2 == 0) {
                        coins = coinUpClothes[ngocItem.getUpgrade()];
                        percent = crys * 100 / upClothe[ngocItem.getUpgrade()];
                        if (percent > maxPercents[ngocItem.getUpgrade()]) {
                            percent = maxPercents[ngocItem.getUpgrade()];
                        }
                    } else {
                        coins = coinUpAdorns[ngocItem.getUpgrade()];
                        percent = crys * 100 / upAdorn[ngocItem.getUpgrade()];
                        if (percent > maxPercents[ngocItem.getUpgrade()]) {
                            percent = maxPercents[ngocItem.getUpgrade()];
                        }
                    }

                    if (coins <= p.c.yen) {
                        p.c.upyen(-coins);
                    } else {
                        int coin = coins - p.c.yen;
                        p.c.upyen(-p.c.yen);
                        p.c.upxu(-coin);
                    }
                    boolean suc = Util.nextInt(1, 100) <= percent;
                    m.cleanup();
                    item.setLock(true);
                    ngocItem.setLock(true);
                    if (suc) {
                        item.ngocs.add(ngocItem);
                        p.c.removeItemBag(ngocIndex);
                        p.sendAddchatYellow("Khảm ngọc thành công");
                    } else {
                        p.sendAddchatYellow("Khảm ngọc thất bại");
                    }

                    m = new Message(21);
                    m.writer().writeByte(suc ? 1 : 0);
                    m.writer().writeInt(p.luong);
                    m.writer().writeInt(p.c.xu);
                    m.writer().writeInt(p.c.yen);
                    m.writer().writeByte(item.getUpgrade());
                    m.writer().flush();
                    p.conn.sendMessage(m);
                    m.cleanup();
                    Service.requestItemInfoMessage(p,item, indexUI, 3);
                    break;
                }
                //Luyện
                case 1: {
                    int exp = 0;
                    Item ngocItem = p.c.ItemBag[indexUI];
                    if(ngocItem != null) {
                        int i2 = -1;
                        for(Option op : ngocItem.options) {
                            if(op.id == 104) {
                                i2 = ngocItem.options.indexOf(op);
                                break;
                            }
                        }
                        if(i2 != -1) {
                            byte[] arrIndex = new byte[m.reader().available()];
                            byte index2;
                            Item item2;
                            byte i;
                            for (i = 0; i < arrIndex.length; i++) {
                                index2 = m.reader().readByte();
                                item2 = p.c.getIndexBag(index2);
                                if (item2.getUpgrade() == 1) {
                                    exp += 10;
                                } else if (item2.getUpgrade() == 2) {
                                    exp += 20;
                                } else if (item2.getUpgrade() == 3) {
                                    exp += 50;
                                } else if (item2.getUpgrade() == 4) {
                                    exp += 100;
                                } else if (item2.getUpgrade() == 5) {
                                    exp += 200;
                                } else if (item2.getUpgrade() == 6) {
                                    exp += 500;
                                } else if (item2.getUpgrade() == 7) {
                                    exp += 1000;
                                } else if (item2.getUpgrade() == 8) {
                                    exp += 2000;
                                } else if (item2.getUpgrade() == 9) {
                                    exp += 5000;
                                } else if (item2.getUpgrade() == 10) {
                                    exp += 10000;
                                }
                                arrIndex[i] = index2;
                            }

                            for (i = 0; i < arrIndex.length; i++) {
                                p.c.removeItemBag(arrIndex[i], 1);
                            }

                            yen = getNextUpgrade((ngocItem.options.get(i2)).param);
                            ngocItem.options.get(i2).param += exp;
                            int nextUpgrade = getNextUpgrade((ngocItem.options.get(i2)).param);
                            upgradeNgoc(ngocItem, yen, nextUpgrade);
                            ngocItem.setUpgrade((byte) nextUpgrade);

                            m.cleanup();
                            m = new Message(124);
                            m.writer().writeByte(1);
                            m.writer().writeInt(p.luong);
                            m.writer().writeInt(p.c.xu);
                            m.writer().writeInt(p.c.yen);
                            m.writer().writeByte(ngocItem.getUpgrade());
                            m.writer().flush();
                            p.conn.sendMessage(m);
                            m.cleanup();
                            m = new Message(42);
                            m.writer().writeByte(3);
                            m.writer().writeByte(indexUI);
                            m.writer().writeLong(ngocItem.expires);
                            if (ItemTemplate.isTypeUIME(3)) {
                                m.writer().writeInt(ngocItem.saleCoinLock);
                            }
                            if (ItemTemplate.isTypeUIShop(3) || ItemTemplate.isTypeUIShopLock(3) || ItemTemplate.isTypeMounts(3) || ItemTemplate.isTypeUIStore(3) || ItemTemplate.isTypeUIBook(3) || ItemTemplate.isTypeUIFashion(3) || ItemTemplate.isTypeUIClanShop(3)) {
                                m.writer().writeInt(ngocItem.buyCoin);
                                m.writer().writeInt(ngocItem.buyCoinLock);
                                m.writer().writeInt(ngocItem.buyGold);
                            }
                            if (ItemTemplate.isTypeBody(ngocItem.id) || ItemTemplate.isTypeMounts(ngocItem.id) || ItemTemplate.isTypeNgocKham(ngocItem.id)) {
                                m.writer().writeByte(ngocItem.sys);//thuoc tinh
                                if (ngocItem.options != null) {
                                    for (Option Option : ngocItem.options) {
                                        m.writer().writeByte(Option.id);
                                        m.writer().writeInt(Option.param);
                                    }
                                }
                            }
                            m.writer().flush();
                            p.conn.sendMessage(m);
                            m.cleanup();
                            p.sendAddchatYellow("Luyện ngọc thành công.");
                        }
                    }
                    break;
                }
                //gọt
                case 2: {
                    Item item = p.c.ItemBag[indexUI];
                    if (item == null) {
                        return;
                    }
                    if (p.c.xu < xuGotNgoc.get(Integer.valueOf(item.getUpgrade()))) {
                        p.conn.sendMessageLog("Không đủ xu để gọt");
                        return;
                    }
                    p.c.upxuMessage((-(Integer) xuGotNgoc.get(Integer.valueOf(item.getUpgrade()))));
                    for(Option option : item.options) {
                        if(option != null && option.param < -1) {
                            option.param += Util.nextInt(ItemTemplate.PARAMS.get(option.id) * 20 / 100, ItemTemplate.PARAMS.get(option.id) * 40 / 100);
                            if (option.param >= 0) {
                                option.param = -1;
                            }
                        }
                    }
                    int typeGotNgoc = 3;
                    m.cleanup();
                    m = new Message(124);
                    m.writer().writeByte(2);
                    m.writer().writeInt(p.luong);
                    m.writer().writeInt(p.c.xu);
                    m.writer().writeInt(p.c.yen);
                    m.writer().writeByte(item.upgrade);
                    m.writer().flush();
                    p.conn.sendMessage(m);
                    m.cleanup();
                    m = new Message(42);
                    m.writer().writeByte(3);
                    m.writer().writeByte(indexUI);
                    m.writer().writeLong(item.expires);
                    if (ItemTemplate.isTypeUIME(typeGotNgoc)) {
                        m.writer().writeInt(item.saleCoinLock);
                    }
                    if (ItemTemplate.isTypeUIShop(typeGotNgoc) || ItemTemplate.isTypeUIShopLock(typeGotNgoc) || ItemTemplate.isTypeMounts(typeGotNgoc) || ItemTemplate.isTypeUIStore(typeGotNgoc) || ItemTemplate.isTypeUIBook(typeGotNgoc) || ItemTemplate.isTypeUIFashion(typeGotNgoc) || ItemTemplate.isTypeUIClanShop(typeGotNgoc)) {
                        m.writer().writeInt(item.buyCoin);
                        m.writer().writeInt(item.buyCoinLock);
                        m.writer().writeInt(item.buyGold);
                    }
                    if (ItemTemplate.isTypeBody(item.id) || ItemTemplate.isTypeMounts(item.id) || ItemTemplate.isTypeNgocKham(item.id)) {
                        m.writer().writeByte(item.sys);//thuoc tinh
                        if (item.options != null) {
                            for (Option Option : item.options) {
                                m.writer().writeByte(Option.id);
                                m.writer().writeInt(Option.param);
                            }
                        }
                    }
                    m.writer().flush();
                    p.conn.sendMessage(m);
                    m.cleanup();
                    p.sendAddchatYellow("Ngọc đã được gọt");
                    break;
                }
                case 3: {
                    Item item = p.c.ItemBag[indexUI];
                    if(item != null) {
                        if(p.c.getBagNull() < item.ngocs.size()) {
                            p.conn.sendMessageLog(Language.NOT_ENOUGH_BAG);
                            return;
                        }
                        p.endLoad(true);
                        Item itN;
                        while (item.ngocs.size() > 0) {
                            itN = item.ngocs.remove(0);
                            if(itN != null) {
                                itN.isLock = false;
                                p.c.addItemBag(false, itN);
                            }
                        }
                        Service.requestItemInfoMessage(p, item, indexUI, 3);
                        p.sendAddchatYellow("Tháo ngọc thành công");
                    }
                    break;
                }
                default: {
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void upgradeNgoc(Item mainItem, int oldUpGrad, int nextUpgrade) {
        try {
            int j;
            for(j = oldUpGrad; j < nextUpgrade; j++) {
                for(Option op : mainItem.options) {
                    if (ItemTemplate.PARAMS.containsKey(op.id)) {
                        op.param += op.param / Math.abs(op.param) * Util.nextInt((int)(0.6D * (double)ItemTemplate.PARAMS.get(op.id)), (int)(0.9D * (double)ItemTemplate.PARAMS.get(op.id)));
                    }
                    else if(op.id == 123) {
                        op.param += 400000;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getNextUpgrade(int xExp) {
        if (xExp > 200 && xExp <= 500) {
            return 2;
        } else if (xExp > 500 && xExp <= 1000) {
            return 3;
        } else if (xExp > 1000 && xExp <= 2000) {
            return 4;
        } else if (xExp > 2000 && xExp <= 5000) {
            return 5;
        } else if (xExp > 5000 && xExp <= 10000) {
            return 6;
        } else if (xExp > 10000 && xExp <= 20000) {
            return 7;
        } else if (xExp > 20000 && xExp <= 50000) {
            return 8;
        } else if (xExp > 50000 && xExp <= 100000) {
            return 9;
        } else {
            return xExp > 100000 ? 10 : 1;
        }
    }


    public static void HuyNhiemVuDanhVong(Player p) {
        p.c.isTaskDanhVong = 0;
        p.c.countTaskDanhVong++;
        p.c.taskDanhVong = new int[]{-1, -1, -1, 0, p.c.countTaskDanhVong};
        Service.chatNPC(p, (short)2, "Con đã huỷ nhiệm vụ lần này.");
    }

    private static void handleUpgradeMat(Player p, Item item,int type) {
        try {
            int upPer = GameSrc.percentUpMat[item.upgrade];
            if(type == 1) {
                upPer *= 2;
            }
            if(Util.nextInt(110) < upPer) {
                p.c.removeItemBody((byte)14);
                Item itemup = ItemTemplate.itemDefault(685+item.upgrade, true);
                itemup.quantity = 1;
                itemup.upgrade = (byte)(item.upgrade+1);
                itemup.isLock = true;

                Option op = new Option(6, 1000*itemup.upgrade);
                itemup.options.add(op);
                op = new Option(87, 500+(250*item.upgrade));
                itemup.options.add(op);

                if(itemup.upgrade >= 3) {
                    op = new Option(79, 25);
                    itemup.options.add(op);
                }
                if(itemup.upgrade >= 6) {
                    op = new Option(64, 0);
                    itemup.options.add(op);
                }
                if(itemup.upgrade == 10) {
                    op = new Option(113, 5000);
                    itemup.options.add(op);
                }
                p.c.addItemBag( false, itemup);
            } else {
                p.sendAddchatYellow("Nâng cấp mắt thất bại!");
            }

            if(p.c.yen < GameSrc.coinUpMat[item.upgrade]) {
                p.c.xu -= (GameSrc.coinUpMat[item.upgrade] - p.c.yen);
                p.c.yen = 0;
            } else {
                p.c.yen -= GameSrc.coinUpMat[item.upgrade];
            }
            if(type == 1) {
                p.luong -= GameSrc.goldUpMat[item.upgrade];
            }
            p.c.removeItemBags(694+item.upgrade, 10);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void NangMat(Player p, Item item, int type) throws IOException {
        if(item.upgrade >= 10) {
            p.conn.sendMessageLog("Mắt đã nâng cấp tối đa");
            return;
        }
        if(p.c.quantityItemyTotal(694+item.upgrade) < 10) {
            ItemTemplate data = ItemTemplate.ItemTemplateId(694+item.upgrade);
            p.conn.sendMessageLog("Bạn không đủ 10 viên "+data.name+" để nâng cấp");
            return;
        }
        if((p.c.yen + p.c.xu) < GameSrc.coinUpMat[item.upgrade]) {
            p.conn.sendMessageLog("Bạn không đủ yên và xu để nâng cấp mắt");
            return;
        }
        if(type == 1 && p.luong < GameSrc.goldUpMat[item.upgrade]) {
            p.conn.sendMessageLog("Bạn không đủ lượng để nâng cấp mắt");
            return;
        }

        GameSrc.handleUpgradeMat(p, item, type);

        Message m = new Message(13);
        m.writer().writeInt(p.c.xu);//xu
        m.writer().writeInt(p.c.yen);//yen
        m.writer().writeInt(p.luong);//luong
        m.writer().flush();
        p.conn.sendMessage(m);
        m.cleanup();
    }

    public static void requestMapTemplate(Player user, Message m) {
        try {
            int templateId = m.reader().readUnsignedByte();
            m.cleanup();
            m = new Message(-28);
            String url = "res/map/" + templateId;
            if (templateId == 139) {
                url = "res/map_file_msg/map_back.bin";
            } else {
                m.writer().writeByte(-109);
            }
            byte[] data = loadFile(url).toByteArray();
            m.writer().write(data);
            user.conn.sendMessage(m);
        } catch (Exception var6) {
            var6.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    static {
        crystals = new int[] { 1, 4, 16, 64, 256, 1024, 4096, 16384, 65536, 262144, 1048576, 3096576 };
        upClothe = new int[] { 4, 9, 33, 132, 177, 256, 656, 2880, 3968, 6016, 13440, 54144, 71680, 108544, 225280, 1032192 };
        upAdorn = new int[] { 6, 14, 50, 256, 320, 512, 1024, 5120, 6016, 9088, 19904, 86016, 108544, 166912, 360448, 1589248 };
        upWeapon = new int[] { 18, 42, 132, 627, 864, 1360, 2816, 13824, 17792, 26880, 54016, 267264, 315392, 489472, 1032192, 4587520 };
        coinUpCrystals = new int[] { 10, 40, 160, 640, 2560, 10240, 40960, 163840, 655360, 1310720, 3932160, 11796480 };
        coinUpClothes = new int[] { 120, 270, 990, 3960, 5310, 7680, 19680, 86400, 119040, 180480, 403200, 1624320, 2150400, 3256320, 6758400, 10137600 };
        coinUpAdorns = new int[] { 180, 420, 1500, 7680, 9600, 15360, 30720, 153600, 180480, 272640, 597120, 2580480, 3256320, 5007360, 10813440, 16220160 };
        coinUpWeapons = new int[] { 540, 1260, 3960, 18810, 25920, 40800, 84480, 414720, 533760, 806400, 1620480, 8017920, 9461760, 14684160, 22026240, 33039360 };
        goldUps = new int[] { 1, 2, 3, 4, 5, 10, 15, 20, 50, 100, 150, 200, 300, 400, 500, 600 };
        maxPercents = new int[] { 80, 75, 70, 65, 60, 55, 50, 45, 40, 35, 30, 25, 20, 15, 10, 5 };
        ArridLuck = new short[] {280, 742,268,242,-1,242,242,-2,-3,-4,409,409,-1,-1,-1,-1,-1,-1,-1,409,409,409,410,-1,410,410,410,-1,-1,-1,-1,-1,-1,-1,410,567,567,567,283,-2,-3,-4,283,283,283,-1, 4,4,4,-1,4,4,4,-2,-3,-4,4,4,4,5, 5,-1,-1,-1,-1,-1,-1,-1, 5, 5, 5, 5,5,5,5,-1,5,6, 6, 6,-1,-1,-1,-1,-1,-1, 6, 6, 6,6,-1,-1,-1,6,6,6,6,-1,6,6,6,-1,-1,-1,-1,-1,-1, 7, 7, 7,-1,-1, 7, 7, 7,7,7,-1,-1,-1, 8,8,8,8,9,9,-1,-1,-1, 567, 317, 318,-1,-1,-1,-1,-1,-1, 319, 320, 321, 322, 323, 324, 325,-1,-1,-1,-1,-1,-1,-1, 326, 327, 328, 329,-2,-3,-4,-1,-1,-1, 330, 331, 332,-1,-1, 333, 334, 335, 336,-1,-1,-1,-1,-1,-1,-1,-1, 369, 370, 371, 372, 373, 374, 419,436,436,436,436,-1,-1,-1,-1,-1,-1,-1,-1,437,437,438, 443, 523, 523, 485,-1,-1,-1,-1,-1,-1,-1,-1, 492, 493, 494,-1, 495, 496, 497, 498, 499,-1,-1,-1,-1,-1,-1,-1, 500, 501, 502, 503, 504, 505, 506, 507, 508,-1,-1,-1,-1,-1,-1, 509, 510,511, 742, 735,762 };
        //ArryenLuck = new int[] { 10000, 20000, 30000, 50000, 100000, 200000, 500000, 1000000, 5000000 };
        ArrdayLuck = new byte[] { 3, 7, 15 };
        xuGotNgoc.put(1, 5000);
        xuGotNgoc.put(2, 40000);
        xuGotNgoc.put(3, 135000);
        xuGotNgoc.put(4, 330000);
        xuGotNgoc.put(5, 625000);
        xuGotNgoc.put(6, 1080000);
        xuGotNgoc.put(7, 1715000);
        xuGotNgoc.put(8, 2560000);
        xuGotNgoc.put(9, 3645000);
        xuGotNgoc.put(10, 5000000);
        exps.put(1, 0);
        exps.put(2, 210);
        exps.put(3, 510);
        exps.put(4, 1010);
        exps.put(5, 2010);
        exps.put(6, 5010);
        exps.put(7, 10010);
        exps.put(8, 20010);
        exps.put(9, 50010);
        exps.put(10, 100010);
    }

}
