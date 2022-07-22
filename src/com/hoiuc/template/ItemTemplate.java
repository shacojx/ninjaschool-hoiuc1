package com.hoiuc.template;

import com.hoiuc.assembly.*;
import com.hoiuc.io.Message;

import com.hoiuc.io.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.*;
import java.util.Map;

public class ItemTemplate {
    public short id;
    public byte type;
    public byte nclass;
    public byte skill;
    public byte gender;
    public String name;
    public String description;
    public byte level;
    public short iconID;
    public short part;
    public boolean isUpToUp;
    public boolean isExpires;
    public long seconds_expires;
    public int saleCoinLock;
    public ArrayList<Option> itemoption;
    public ArrayList<Option> option1;
    public ArrayList<Option> option2;
    public ArrayList<Option> option3;
    public static ArrayList<ItemTemplate> entrys = new ArrayList<ItemTemplate>();

    public static final Map<Integer, Integer> PARAMS = new HashMap();

    public static final Option VU_KHI_OPTION = new Option(106, 0);
    public static final Option TRANG_BI_OPTION = new Option(107, 0);
    public static final Option TRANG_SUC_OPTION = new Option(108, 0);
    public static final int VU_KHI_OPTION_ID= 106;
    public static final int TRANG_BI_OPTION_ID = 107;
    public static final int TRANG_SUC_OPTION_ID = 108;
    public static final int EXP_ID = 104;
    public static final int GIA_KHAM_OPTION_ID = 123;

    public static int HUYEN_TINH_NGOC = 652;
    public static int HUYET_NGOC = 653;
    public static int LAM_TINH_NGOC = 654;
    public static int LUC_NGOC = 655;
    public static int MAX_ST_NGUOI = 200;
    public static int MAX_ST_QUAI = 500;
    public static int HP_TOI_DA = 50;
    public static int MP_TOI_DA = 50;
    public static int CHI_MANG = 5;
    public static int KHANG_ST_CHI_MANG = 5;
    public static int MOI_GIAY_HOI_PHUC_HP = 5;
    public static int MOI_GIAY_HOI_PHUC_MP = 5;
    public static int MAX_TAN_CONG = 100;
    public static int GIAM_TRU_ST = 10;
    public static int PHAN_DON = 10;
    public static int ST_CHI_MANG = 500;
    public static int CHINH_XAC = 10;
    public static int NE_DON = 10;
    public static int KHANG_TAT_CA = 10;

    public static final int TAN_CONG_ID = 73;
    public static final int ST_LEN_QUAI_ID = 102;
    public static final int ST_LEN_NGUOI_ID = 103;
    public static final int ST_CHI_MANG_ID = 105;
    public static final int CHI_MANG_ID = 114;
    public static final int NE_DON_ID = 115;
    public static final int CHINH_XAC_ID = 116;
    public static final int MP_TOI_DA_ID = 117;
    public static final int KHANG_TAT_CA_ID = 118;
    public static final int MOI_GIAY_HOI_PHUC_MP_ID = 119;
    public static final int MOI_GIAY_HOI_PHUC_HP_ID = 120;
    public static final int KHANG_SAT_THUONG_CHI_MANG_ID = 121;
    public static final int GIAM_TRU_ST_ID = 124;
    public static final int HP_TOI_DA_ID = 125;
    public static final int PHAN_DON_ID = 126;

    public static final int[] checkPartHead = new int[]{226, 223, 258, 264, 267};

    public static final int[] check_id = new int[]{73, 102, 103, 105, 114, 115, 116, 117, 118, 119, 120, 121, 124, 125, 126};

    public static short[] idNewItems = new short[]{798, 801, 802, 803, 795, 796, 804, 805, 799, 800, 813, 814, 815, 816, 817,828};

    public static short[][] idNewItemMounts = new short[][]{
            new short[]{798, 801, 802, 803, 828},
            new short[]{36, 47, 48, 49, 63},
    };
    public static short[][] idNewItemCaiTrang = new short[][]{
            new short[]{795, 796, 804, 805},
            new short[]{37, 40, 55, 58},
            new short[]{38, 41, 56, 59},
            new short[]{39, 42, 57, 60}
    };
    public static short[][] idNewItemWP = new short[][]{
            new short[]{799, 800},
            new short[]{44, 46},
    };
    public static short[][] idNewItemMatNa = new short[][]{
            new short[]{813, 814, 815, 816, 817},
            new short[]{50, 51, 52, 53, 54},
    };
    public static short[][] idNewItemYoroi= new short[][]{
            new short[]{797},
            new short[]{43},
    };
    public static short[][] idNewItemBienHinh= new short[][]{
            new short[]{825, 826},
            new short[]{61, 62},
    };

    private static HashMap<Integer, ItemOptionTemplate> options = new HashMap<Integer, ItemOptionTemplate>();
    public static void put(int id, ItemOptionTemplate option) {
        ItemTemplate.options.put(id, option);
    }
    public static Collection<ItemOptionTemplate> getOptions() {
        return ItemTemplate.options.values();
    }

    public static boolean isPartHead(int id) {
        for(int entry : ItemTemplate.checkPartHead) {
            if(entry == id) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCheckId(int id) {
        for (int entry : ItemTemplate.check_id) {
            if (entry == id) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTypeBody(int id) {
        for (ItemTemplate entry : ItemTemplate.entrys) {
            if (entry.id == id) {
                return entry.type >= 0 && entry.type <= 15;
            }
        }
        return false;
    }

    public static boolean isTypeUIME(int typeUI) {
        return typeUI == 5 || typeUI == 3 || typeUI == 4 || typeUI == 39;
    }

    public static boolean isTypeUIShop(int typeUI) {
        return typeUI == 20 || typeUI == 21 || typeUI == 22 || typeUI == 23 || typeUI == 24 || typeUI == 25 || typeUI == 26 || typeUI == 27 || typeUI == 28 || typeUI == 29 || typeUI == 16 || typeUI == 17 || typeUI == 18 || typeUI == 19 || typeUI == 2 || typeUI == 6 || typeUI == 8 || typeUI == 34;
    }

    public static boolean isTypeUIShopLock(int typeUI) {
        return typeUI == 7 || typeUI == 9;
    }

    public static boolean isTypeUIStore(int typeUI) {
        return typeUI == 14;
    }

    public static boolean isTypeUIBook(int typeUI) {
        return typeUI == 15;
    }

    public static boolean isTypeUIFashion(int typeUI) {
        return typeUI == 32;
    }

    public static boolean isTypeUIClanShop(int typeUI) {
        return typeUI == 34;
    }

    public static boolean isTypeMounts(int id) {
        for (ItemTemplate entry : ItemTemplate.entrys) {
            if (entry.id == id) {
                return entry.type >= 29 && entry.type <= 33;
            }
        }
        return false;
    }

    public static boolean isTypeNgocKham(int id) {
        for (ItemTemplate entry : ItemTemplate.entrys) {
            if (entry.id == id) {
                return entry.type == 34;
            }
        }
        return false;
    }

    public static boolean checkIdNewItems(int id) {
        int i;
        for (i = 0; i < ItemTemplate.idNewItems.length; i++) {
            if(id == ItemTemplate.idNewItems[i]){
                return true;
            }
        }
        return false;
    }

    public static boolean isIdNewMounts(int id) {
        int i;
        for (i = 0; i < ItemTemplate.idNewItemMounts[0].length; i++) {
            if(id == ItemTemplate.idNewItemMounts[0][i]){
                return true;
            }
        }
        return false;
    }

    public static int checkIdNewMounts(int id) {
        int i;
        for (i = 0; i < ItemTemplate.idNewItemMounts[0].length; i++) {
            if(id == ItemTemplate.idNewItemMounts[0][i]){
                return i;
            }
        }
        return -1;
    }

    public static boolean isIdNewCaiTrang(int id) {
        int i;
        for (i = 0; i < ItemTemplate.idNewItemCaiTrang[0].length; i++) {
            if(id == ItemTemplate.idNewItemCaiTrang[0][i]){
                return true;
            }
        }
        return false;
    }

    public static int checkIdNewCaiTrang(int id) {
        int i;
        for (i = 0; i < ItemTemplate.idNewItemCaiTrang[0].length; i++) {
            if(id == ItemTemplate.idNewItemCaiTrang[0][i]){
                return i;
            }
        }
        return -1;
    }

    public static int checkIdNewMatNa(int id) {
        int i;
        for (i = 0; i < ItemTemplate.idNewItemMatNa[0].length; i++) {
            if(id == ItemTemplate.idNewItemMatNa[0][i]){
                return i;
            }
        }
        return -1;
    }

    public static int checkIdNewWP(int id) {
        int i;
        for (i = 0; i < ItemTemplate.idNewItemWP[0].length; i++) {
            if(id == ItemTemplate.idNewItemWP[0][i]){
                return i;
            }
        }
        return -1;
    }

    public static int checkIdNewYoroi(int id) {
        int i;
        for (i = 0; i < ItemTemplate.idNewItemYoroi[0].length; i++) {
            if(id == ItemTemplate.idNewItemYoroi[0][i]){
                return i;
            }
        }
        return -1;
    }
    
    public static int checkIdNewBienHinh(int id) {
        int i;
        for (i = 0; i < ItemTemplate.idNewItemBienHinh[0].length; i++) {
            if(id == ItemTemplate.idNewItemBienHinh[0][i]){
                return i;
            }
        }
        return -1;
    }

    public static int checkIdJiraiNam(int id) {
        switch (id) {
            case 0: return 746;
            case 1: return 747;
            case 2: return 712;
            case 3: return 713;
            case 4: return 748;
            case 5: return 752;
            case 6: return 751;
            case 7: return 750;
            case 8: return 749;
        }
        return -1;
    }

    public static int checkIdJiraiNu(int id) {
        switch (id) {
            case 0: return 753;
            case 1: return 754;
            case 2: return 715;
            case 3: return 716;
            case 4: return 755;
            case 5: return 759;
            case 6: return 758;
            case 7: return 757;
            case 8: return 756;
        }
        return -1;
    }

    public static int ThinhLuyenParam(int id, int tl) {
        switch (id) {
            //Tăng tấn công cho chủ: +#
            case 76: {
                return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 50) : 60) : 70) : 80) : 110) : 170) : 230) : 320) : 510; //550, 610, 680, 770, 900, 1080
            }

            //Tăng max HP cho chủ: +#
            case 77: {
                return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 40) : 60) : 80) : 100) : 140) : 220) : 300) : 420) : 590; //540, 600, 680, 780, 920, 1140, 1440, 1860, 2450
            }

            case 75: //Tăng chính xác cho chủ: +#
            case 78: { //Tăng né tránh cho chủ: +#
                return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 25) : 30) : 35) : 40) : 50) : 60) : 80) : 110) : 160; //75, 105, 140, 180
            }

            case 79: { // Kháng sát thương chí mạng: #%
                return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 1) : 1) : 2) :2) : 2) : 2) : 3) : 3) : 4; // done
            }

            case 80: { // Giảm trừ sát thương: +#
                return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 5) : 7) : 9) : 11) : 13) : 15) : 20) : 25) : 30; // done
            }

            case 84:  // Né đòn: +#
            case 86: { //Chính xác: +#
                return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 25) : 30) : 35) : 40) : 50) : 60) : 80) : 115) : 165; // done
            }

            case 85: { //Độ tinh luyện: #
                return 1;
            }
            case 82: //Hp tối đa: #
            case 83: {//Mp tối đa: +#
                return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 40) : 60) : 80) : 100) : 130) : 190) : 260) : 370) : 570; // done
            }
            case 87: //Tấn công: +#
            case 88: //Hoả công: +#
            case 89: //Băng công: +#
            case 90: { //Phong lôi: +#
                return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 50) : 60) : 85) : 115) : 125) : 300) : 350) : 405) : 510; //done
            }
            case 94: { //Tấn công: +#%
                return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 5) : 10) : 15) : 20) : 25) : 30) : 35) : 45) : 65; // done 374
            }
            case 91: //Phản đòn: #
            case 81: {//Kháng tất cả: +#
                return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 5) : 5) : 5) : 5) : 10) : 10) : 10) : 15) : 20; // done
            }

            case 92: //Chí mạng: +#
            case 95: //Kháng băng: +#
            case 96: //Kháng hoả: +#
            case 97: { //Kháng phong: +#
                return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 5) : 5) : 5) : 5) : 5) : 10) : 10) : 10) : 15; // done
            }
            default: {
                return 0;
            }
        }
    }

    public static void divedeItem(Player p, Message m) {
        try {
            byte index = m.reader().readByte();
            int quantity = m.reader().readInt();
            if (p.c.getBagNull() == 0) {
                p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                return;
            }
            Item item = p.c.getIndexBag(index);
            if (quantity > 0 && item != null && item.quantity > 1 && quantity <= item.quantity) {
                Item itemup = new Item();
                itemup.id = item.id;
                itemup.isLock = item.isLock;
                itemup.upgrade = item.upgrade;
                itemup.isExpires = item.isExpires;
                itemup.quantity = quantity;
                itemup.expires = item.expires;
                p.c.addItemBag(false, itemup);
                p.c.removeItemBag(index, quantity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static boolean isUpgradeHide(int id, byte upgrade) {
        return ((id == 27 || id == 30 || id == 60) && upgrade < 4) || ((id == 28 || id == 31 || id == 37 || id == 61) && upgrade < 8) || ((id == 29 || id == 32 || id == 38 || id == 62) && upgrade < 12) || ((id == 33 || id == 34 || id == 35 || id == 36 || id == 39) && upgrade < 14) || (((id >= 40 && id <= 46) || (id >= 48 && id <= 56)) && upgrade < 16);
    }

    public static Item itemDefault(int id) {
        //return itemDefault(id, (byte)0);
        if (id >= 652 && id <= 655) {
            return itemNgocDefault(id, 1, true);
        } else {
            try {
                return itemDefault(id, (byte)0);
            } catch (Exception var2) {
                return new ItemDefault();
            }
//            Item item;
//            if (id >= ClanThanThu.HAI_MA_1_ID && id <= ClanThanThu.DI_LONG_3_ID) {
//                item = itemDefault(id, (byte)0);
//                if (id != ClanThanThu.HAI_MA_1_ID && id != ClanThanThu.DI_LONG_1_ID) {
//                    if (id != ClanThanThu.HAI_MA_2_ID && id != ClanThanThu.DI_LONG_2_ID) {
//                        if (id == ClanThanThu.HAI_MA_3_ID || id == ClanThanThu.DI_LONG_3_ID) {
//                            item.options.add(new Option(ClanThanThu.ST_NGUOI_ID, 8000));
//                            item.options.add(new Option(ClanThanThu.ST_QUAI_ID, 30000));
//                        }
//                    } else {
//                        item.options.add(new Option(ClanThanThu.ST_NGUOI_ID, 3000));
//                        item.options.add(new Option(ClanThanThu.ST_QUAI_ID, 15000));
//                    }
//                } else {
//                    item.options.add(new Option(ClanThanThu.ST_NGUOI_ID, 1000));
//                    item.options.add(new Option(ClanThanThu.ST_QUAI_ID, 5000));
//                }
//
//                return item;
//            }
//            else if (id == ClanThanThu.HOA_LONG_ID) {
//                item = itemDefault(id, (byte)0);
//                item.options.add(new Option(ClanThanThu.ST_NGUOI_ID, 15000));
//                item.options.add(new Option(ClanThanThu.ST_QUAI_ID, 35000));
//                return item;
//            }
//            else if (id == 597) {
//                item = itemDefault(id, (byte)0);
//                item.isExpires = true;
//                item.setLock(true);
//                item.expires = Util.TimeDay(3);
//                return item;
//            }
//            else if (id == 572) {
//                item = itemDefault(id, (byte)0);
//                item.isExpires = true;
//                item.setLock(true);
//                item.expires = Util.TimeHours(5);
//                return item;
//            }
//            else {
//
//            }
        }
    }

    public static Item itemDefault(int id, boolean isLock) {
        Item item = itemDefault(id, (byte)0);
        item.isLock = isLock;
        return item;
    }

    public static Item itemDefault(int id, byte sys) {
        Item item = new Item();
        item.id = (short)id;
        item.sys = sys;
        ItemTemplate data = ItemTemplateId(id);
        if (data.isExpires) {
            item.isExpires = true;
            item.expires = Util.TimeSeconds(data.seconds_expires);
        }
        item.saleCoinLock = data.saleCoinLock;
        Option op;
        int idOp;
        int par;
        if (sys == 0) {
            for (Option option : data.itemoption) {
                idOp = option.id;
                par = option.param;
                op = new Option(idOp, par);
                item.options.add(op);
            }
        }
        else if (sys == 1) {
            for (Option option : data.option1) {
                idOp = option.id;
                par = option.param;
                op = new Option(idOp, par);
                item.options.add(op);
            }
        }
        else if (sys == 2) {
            for (Option option : data.option2) {
                idOp = option.id;
                par = option.param;
                op = new Option(idOp, par);
                item.options.add(op);
            }
        }
        else if (sys == 3) {
            for (Option option : data.option3) {
                idOp = option.id;
                par = option.param;
                op = new Option(idOp, par);
                item.options.add(op);
            }
        }
        return item;
    }

    public static Item parseItem(String str) {
        Item item = new Item();
        JSONObject job = (JSONObject) JSONValue.parse(str);
        item.id = Short.parseShort(job.get((Object)"id").toString());
        item.isLock = Boolean.parseBoolean(job.get((Object)"isLock").toString());
        item.upgrade = Byte.parseByte(job.get((Object)"upgrade").toString());
        item.isExpires = Boolean.parseBoolean(job.get((Object)"isExpires").toString());
        item.quantity = Integer.parseInt(job.get((Object)"quantity").toString());
        if (item.isExpires) {
            item.expires = Long.parseLong(job.get((Object)"expires").toString());
        }
        if (item.id == 523 && !item.isExpires) {
            item.isExpires = true;
            item.expires = Util.TimeDay(1);
        }
        item.sys = Byte.parseByte(job.get((Object)"sys").toString());
        item.saleCoinLock = Integer.parseInt(job.get((Object)"sale").toString());
        JSONArray Option = (JSONArray)JSONValue.parse(job.get((Object)"option").toString());
        for (Object Option2 : Option) {
            JSONObject job2 = (JSONObject)Option2;
            Option option = new Option(Integer.parseInt(job2.get((Object)"id").toString()), Integer.parseInt(job2.get((Object)"param").toString()));
            item.options.add(option);
        }

        if(job.get((Object)"ngocs") != null) {
            JSONArray jar = (JSONArray)JSONValue.parse(job.get((Object)"ngocs").toString());
            for (byte ii = 0; ii < jar.size(); ii++) {
                Item itemNgoc = new Item();
                JSONObject jobNgoc = (JSONObject) JSONValue.parse(jar.get(ii).toString());
                itemNgoc.id = Short.parseShort(jobNgoc.get((Object)"id").toString());
                itemNgoc.isLock = Boolean.parseBoolean(jobNgoc.get((Object) "isLock").toString());
                itemNgoc.upgrade = Byte.parseByte(jobNgoc.get((Object) "upgrade").toString());
                itemNgoc.isExpires = Boolean.parseBoolean(jobNgoc.get((Object) "isExpires").toString());
                itemNgoc.quantity = Integer.parseInt(jobNgoc.get((Object) "quantity").toString());
                if (itemNgoc.isExpires) {
                    itemNgoc.expires = Long.parseLong(jobNgoc.get((Object) "expires").toString());
                }
                if (itemNgoc.id == 523 && !itemNgoc.isExpires) {
                    itemNgoc.isExpires = true;
                    itemNgoc.expires = Util.TimeDay(1);
                }
                itemNgoc.sys = Byte.parseByte(jobNgoc.get((Object) "sys").toString());
                itemNgoc.saleCoinLock = Integer.parseInt(jobNgoc.get((Object) "sale").toString());
                JSONArray OptionNgoc = (JSONArray) JSONValue.parse(jobNgoc.get((Object) "option").toString());
                for (Object Option2 : OptionNgoc) {
                    JSONObject job2 = (JSONObject) Option2;
                    Option option = new Option(Integer.parseInt(job2.get((Object) "id").toString()), Integer.parseInt(job2.get((Object) "param").toString()));
                    itemNgoc.options.add(option);
                }
                item.ngocs.add(itemNgoc);
            }
        }
        return item;
    }

    public static JSONObject ObjectItem(Item item, int index) {
        JSONObject put = new JSONObject();
        put.put((Object)"index", (Object)index);
        put.put((Object)"id", (Object)item.id);
        put.put((Object)"isLock", (Object)item.isLock);
        put.put((Object)"upgrade", (Object)item.upgrade);
        put.put((Object)"isExpires", (Object)item.isExpires);
        put.put((Object)"quantity", (Object)item.quantity);
        if (item.isExpires) {
            put.put((Object)"expires", (Object)item.expires);
        }
        put.put((Object)"sys", (Object)item.sys);
        put.put((Object)"sale", (Object)item.saleCoinLock);
        JSONArray option = new JSONArray();
        for (Option Option : item.options) {
            JSONObject pa = new JSONObject();
//            if(item.id == ItemTemplate.HUYEN_TINH_NGOC) {
//                if(Option.id == ItemTemplate.KHANG_SAT_THUONG_CHI_MANG_ID) {
//                    Option.id = ItemTemplate.CHI_MANG_ID;
//                }
//            }
//            if(Option.id == ItemTemplate.CHI_MANG_ID) {
//                if(item.upgrade == 1) {
//                    Option.param = 4;
//                }
//                if(item.upgrade == 2) {
//                    Option.param = 7;
//                }
//                if(item.upgrade == 3) {
//                    Option.param = 9;
//                }
//            }
            pa.put((Object)"id", (Object)Option.id);
            pa.put((Object)"param", (Object)Option.param);
            option.add((Object)pa);
        }
        put.put((Object)"option", (Object)option);

        if(item.ngocs != null && item.ngocs.size() > 0) {
            JSONArray jarr = new JSONArray();
            for (Item itemNgoc:item.ngocs) {
                JSONObject put2 = new JSONObject();
                put2.put((Object)"id", (Object)itemNgoc.id);
                put2.put((Object)"isLock", (Object)itemNgoc.isLock);
                put2.put((Object)"upgrade", (Object)itemNgoc.upgrade);
                put2.put((Object)"isExpires", (Object)itemNgoc.isExpires);
                put2.put((Object)"quantity", (Object)itemNgoc.quantity);
                if (itemNgoc.isExpires) {
                    put2.put((Object)"expires", (Object)itemNgoc.expires);
                }
                put2.put((Object)"sys", (Object)itemNgoc.sys);
                put2.put((Object)"sale", (Object)itemNgoc.saleCoinLock);
                JSONArray option2 = new JSONArray();
                for (Option Option : itemNgoc.options) {
                    JSONObject pa = new JSONObject();
//                    if(itemNgoc.id == ItemTemplate.HUYEN_TINH_NGOC) {
//                        if(Option.id == ItemTemplate.KHANG_SAT_THUONG_CHI_MANG_ID) {
//                            Option.id = ItemTemplate.CHI_MANG_ID;
//                        }
//                    }
//                    if(Option.id == ItemTemplate.CHI_MANG_ID) {
//                        if(item.upgrade == 1) {
//                            Option.param = 4;
//                        }
//                        if(item.upgrade == 2) {
//                            Option.param = 7;
//                        }
//                        if(item.upgrade == 3) {
//                            Option.param = 9;
//                        }
//
                    pa.put((Object)"id", (Object)Option.id);
                    pa.put((Object)"param", (Object)Option.param);
                    option2.add((Object)pa);
                }
                put2.put((Object)"option", (Object)option2);
                jarr.add(put2);
            }
            put.put((Object)"ngocs", (Object)jarr);
        }
        return put;
    }

    public static JSONObject ObjectItem(Item item) {
        JSONObject put = new JSONObject();
        put.put((Object)"id", (Object)item.id);
        put.put((Object)"isLock", (Object)item.isLock);
        put.put((Object)"upgrade", (Object)item.upgrade);
        put.put((Object)"isExpires", (Object)item.isExpires);
        put.put((Object)"quantity", (Object)item.quantity);
        if (item.isExpires) {
            put.put((Object)"expires", (Object)item.expires);
        }
        put.put((Object)"sys", (Object)item.sys);
        put.put((Object)"sale", (Object)item.saleCoinLock);
        JSONArray option = new JSONArray();
        for (Option Option : item.options) {
            JSONObject pa = new JSONObject();
            pa.put((Object)"id", (Object)Option.id);
            pa.put((Object)"param", (Object)Option.param);
            option.add((Object)pa);
        }
        put.put((Object)"option", (Object)option);

        if(item.ngocs != null && item.ngocs.size() > 0) {
            JSONArray jarr = new JSONArray();
            for (Item itemNgoc:item.ngocs) {
                JSONObject put2 = new JSONObject();
                put2.put((Object)"id", (Object)itemNgoc.id);
                put2.put((Object)"isLock", (Object)itemNgoc.isLock);
                put2.put((Object)"upgrade", (Object)itemNgoc.upgrade);
                put2.put((Object)"isExpires", (Object)itemNgoc.isExpires);
                put2.put((Object)"quantity", (Object)itemNgoc.quantity);
                if (itemNgoc.isExpires) {
                    put2.put((Object)"expires", (Object)itemNgoc.expires);
                }
                put2.put((Object)"sys", (Object)itemNgoc.sys);
                put2.put((Object)"sale", (Object)itemNgoc.saleCoinLock);
                JSONArray option2 = new JSONArray();
                for (Option Option : itemNgoc.options) {
                    JSONObject pa = new JSONObject();
                    pa.put((Object)"id", (Object)Option.id);
                    pa.put((Object)"param", (Object)Option.param);
                    option2.add((Object)pa);
                }
                put2.put((Object)"option", (Object)option2);
                jarr.add(put2);
            }
            put.put((Object)"ngocs", (Object)jarr);
        }
        return put;
    }

    public static ItemTemplate ItemTemplateId(int id) {
        for (ItemTemplate entry : ItemTemplate.entrys) {
            if (entry.id == id) {
                return entry;
            }
        }
        return null;
    }

    public static Item itemNgocDefault(int id, int upgrade, boolean random) {
        Item item = new Item();
        item.id = (short)id;
        item.sys = 0;
        item.saleCoinLock = 5;
        ItemTemplate data = ItemTemplateId(id);
        if (data.isExpires) {
            item.isExpires = true;
            item.expires = Util.TimeSeconds(data.seconds_expires);
        }

        item.saleCoinLock = data.saleCoinLock;
        int idOp;
        int par;
        Option op;
        for(Option option : data.itemoption) {
            idOp = option.id;
            par = option.param;
            op = new Option(idOp, par);
            item.options.add(op);
        }

        if (id == HUYET_NGOC) {
            item.options.add(VU_KHI_OPTION);
            item.options.add(new Option(73, random ? Util.nextInt((int)(0.8D * (double)MAX_TAN_CONG), MAX_TAN_CONG) : MAX_TAN_CONG));
            item.options.add(new Option(114, -(random ? Util.nextInt((int)(0.8D * (double)CHI_MANG), CHI_MANG) : CHI_MANG)));
            item.options.add(TRANG_BI_OPTION);
            item.options.add(new Option(124, random ? Util.nextInt((int)(0.8D * (double)GIAM_TRU_ST), GIAM_TRU_ST) : GIAM_TRU_ST));
            item.options.add(new Option(73, -(random ? Util.nextInt((int)(0.6D * (double)MAX_TAN_CONG), MAX_TAN_CONG) : MAX_TAN_CONG)));
            item.options.add(TRANG_SUC_OPTION);
            item.options.add(new Option(115, random ? Util.nextInt((int)(0.8D * (double)NE_DON), NE_DON) : NE_DON));
            item.options.add(new Option(119, -(random ? Util.nextInt((int)(0.8D * (double)MOI_GIAY_HOI_PHUC_MP), MOI_GIAY_HOI_PHUC_MP) : MOI_GIAY_HOI_PHUC_MP)));
        }

        if (id == HUYEN_TINH_NGOC) {
            item.options.add(VU_KHI_OPTION);
            item.options.add(new Option(102, random ? Util.nextInt((int)(0.8D * (double)MAX_ST_QUAI), MAX_ST_QUAI) : MAX_ST_QUAI));
            item.options.add(new Option(115, -(random ? Util.nextInt((int)(0.8D * (double)NE_DON), NE_DON) : NE_DON)));
            item.options.add(TRANG_BI_OPTION);
            item.options.add(new Option(126, random ? Util.nextInt((int)(0.8D * (double)PHAN_DON), PHAN_DON) : PHAN_DON));
            item.options.add(new Option(105, -(random ? Util.nextInt((int)(0.6D * (double)ST_CHI_MANG), ST_CHI_MANG) : ST_CHI_MANG)));
            item.options.add(TRANG_SUC_OPTION);
            item.options.add(new Option(114, random ? Util.nextInt((int)(0.8D * (double)CHI_MANG), CHI_MANG) : CHI_MANG));
            item.options.add(new Option(118, -(random ? Util.nextInt((int)(0.8D * (double)KHANG_TAT_CA), KHANG_TAT_CA) : KHANG_TAT_CA)));
        }

        if (id == LAM_TINH_NGOC) {
            item.options.add(VU_KHI_OPTION);
            item.options.add(new Option(103, random ? Util.nextInt((int)(0.8D * (double)MAX_ST_NGUOI), MAX_ST_NGUOI) : MAX_ST_NGUOI));
            item.options.add(new Option(125, -(random ? Util.nextInt((int)(0.8D * (double)HP_TOI_DA), HP_TOI_DA) : HP_TOI_DA)));
            item.options.add(TRANG_BI_OPTION);
            item.options.add(new Option(121, random ? Util.nextInt((int)(0.8D * (double)KHANG_ST_CHI_MANG), KHANG_ST_CHI_MANG) : KHANG_ST_CHI_MANG));
            item.options.add(new Option(120, -(random ? Util.nextInt((int)(0.8D * (double)MOI_GIAY_HOI_PHUC_HP), MOI_GIAY_HOI_PHUC_HP) : MOI_GIAY_HOI_PHUC_HP)));
            item.options.add(TRANG_SUC_OPTION);
            item.options.add(new Option(116, random ? Util.nextInt((int)(0.8D * (double)CHINH_XAC), CHINH_XAC) : CHINH_XAC));
            item.options.add(new Option(126, -(random ? Util.nextInt((int)(0.8D * (double)PHAN_DON), PHAN_DON) : PHAN_DON)));
        }

        if (id == LUC_NGOC) {
            item.options.add(VU_KHI_OPTION);
            item.options.add(new Option(105, random ? Util.nextInt((int)(0.8D * (double)ST_CHI_MANG), ST_CHI_MANG) : ST_CHI_MANG));
            item.options.add(new Option(116, -(random ? Util.nextInt((int)(0.8D * (double)CHINH_XAC), CHINH_XAC) : CHINH_XAC)));
            item.options.add(TRANG_BI_OPTION);
            item.options.add(new Option(125, random ? Util.nextInt((int)(0.8D * (double)HP_TOI_DA), HP_TOI_DA) : HP_TOI_DA));
            item.options.add(new Option(117, -(random ? Util.nextInt((int)(0.8D * (double)MP_TOI_DA), MP_TOI_DA) : MP_TOI_DA)));
            item.options.add(TRANG_SUC_OPTION);
            item.options.add(new Option(117, random ? Util.nextInt((int)(0.8D * (double)MP_TOI_DA), MP_TOI_DA) : MP_TOI_DA));
            item.options.add(new Option(124, -(random ? Util.nextInt((int)(0.8D * (double)GIAM_TRU_ST), GIAM_TRU_ST) : GIAM_TRU_ST)));
        }

        item.setUpgrade((byte)upgrade);
        item.options.add(new Option(104, 0));
        item.options.add(new Option(123, 800000));
        return item;
    }

    public boolean isTrangSuc() {
        return this.type == 3 || this.type == 5 || this.type == 7 || this.type == 9;
    }

    public boolean isTrangPhuc() {
        return this.type == 0 || this.type == 2 || this.type == 4 || this.type == 6 || this.type == 8;
    }

    public boolean isYoroi() {
        return this.type == 12;
    }

    public boolean isVuKhi() {
        return this.type == 1;
    }

    public boolean isEye() {
        return this.type == 14;
    }

    public boolean isItemNhiemVu() {
        return this.type == 25 || this.type == 23 || this.type == 24;
    }

    static {
        PARAMS.put(73, MAX_TAN_CONG);
        PARAMS.put(102, MAX_ST_QUAI);
        PARAMS.put(103, MAX_ST_NGUOI);
        PARAMS.put(105, ST_CHI_MANG);
        PARAMS.put(114, CHI_MANG);
        PARAMS.put(115, NE_DON);
        PARAMS.put(116, CHINH_XAC);
        PARAMS.put(117, MP_TOI_DA);
        PARAMS.put(118, KHANG_TAT_CA);
        PARAMS.put(119, MOI_GIAY_HOI_PHUC_MP);
        PARAMS.put(120, MOI_GIAY_HOI_PHUC_HP);
        PARAMS.put(121, KHANG_ST_CHI_MANG);
        PARAMS.put(124, GIAM_TRU_ST);
        PARAMS.put(125, HP_TOI_DA);
        PARAMS.put(126, PHAN_DON);
    }

}

