package com.hoiuc.assembly;

import com.hoiuc.io.Message;
import com.hoiuc.io.Util;
import com.hoiuc.server.GameSrc;
import com.hoiuc.server.Manager;
import com.hoiuc.server.Menu;
import com.hoiuc.server.Service;
import com.hoiuc.stream.BossTuanLoc;
import com.hoiuc.stream.Server;
import com.hoiuc.template.ItemTemplate;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

public class UseItem {
    static int[] arrOp = new int[] { 6, 7, 10, 67, 68, 69, 70, 71, 72, 73, 74 };
    static int[] arrParam = new int[] { 50, 50, 10, 5, 10, 10, 5, 5, 5, 100, 50 };
    private static byte[] arrOpenBag = new byte[] { 0, 6, 6, 12, 24};
    private static Object LOCK = new Object();

    public static short[] idItemRuongMayMan = new short[]{ 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 7, 7,7,7 , 8, 8,8, 9,9,242,242,242, 280, 284,284, 285, 436 };
    public static short[] idItemRuongTinhXao = new short[]{4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 6, 6, 6, 7, 7, 8, 8, 8, 9, 9, 9, 436, 437, 242, 242, 280, 280, 280, 283, 284, 284, 285, 436, 437, 437 };
    public static short[] idItemHopBanhThuong = new short[]{ 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6,6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7,8,8, 275, 276, 277, 278, 289, 340, 340, 383, 409, 410, 419, 436, 436, 454, 454, 457, 436, 436, 436, 437, 437, 443, 485, 524, 549, 550, 551, 568, 569, 570, 571, 577, 742 };
    public static short[] idItemHopBanhThuongHang = new short[]{4, 4, 4, 4, 4, 4, 4, 5, 5, 5,5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7,7,7,7, 8,8,8,9,9,10,10,11,11, 275, 276, 277, 278, 289, 340, 340, 384, 409, 410, 419, 436, 436, 436, 436, 436, 436, 437, 437, 438, 443, 454, 454, 457, 457, 485, 524, 539, 567, 567, 549, 550, 551, 568, 569, 570, 571,308,309 , 577, 742, 781};
    public static short[] idItemDieuGiay = new short[]{ 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 7, 7, 7, 7, 8, 9, 275, 276, 277, 278, 289, 289, 340, 340, 383, 409, 410, 436, 436, 436, 436, 436, 437, 437, 443, 485, 524, 549, 550, 551, 569, 577, 742 };
    public static short[] idItemDieuVai = new short[]{ 4, 4, 5, 5, 6, 6, 7, 7, 8, 9, 10,11, 275, 276, 277, 278, 289, 340, 340, 383, 409, 410, 419, 436, 436, 436, 436, 436, 436, 437, 437, 438, 443, 485, 524, 567, 567, 549, 550, 551, 569, 577, 742 , 781};
    public static short[] idItemRuongMaQuai = new short[]{ 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 8, 8, 8, 9, 9, 9, 280, 280, 280, 436, 437, 436, 437, 618, 619, 620, 621, 622, 623, 624, 625, 626, 627, 628, 629, 630, 631, 632, 633, 634, 635, 636, 637 };
    public static short[] idItemPhucNangNhanGia = new short[]{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,5,5,5,5,5,5,5,5,5,5,5,5,6,6,6,6,6,-1};
    public static short[] idItemBanhChocolate = new short[]{4, 4, 4, 4,4,4,4,4,4,4,4,4,4,4,4,4, 5,5,5,5,5,5,5,5,5,5, 5, 5, 5, 6, 6, 6, 7, 7, 7, 7, 7,7,7,7,7,7,7,7,7, 8, 9, 275, 276, 277, 278, 289, 289, 340, 340, 383, 409, 410, 436, 436,5,5,5,5,6,6,6,6,6,6, 7,7,7, 437, 443, 485, 524, 549, 550, 551, 549, 550, 551,569,574,5,5,5,5, 6, 6,6,6,6,6,6,6, 7, 7,7,7, 577,575,578, 742 ,673,775};
    public static short[] idItemBanhDauTay = new short[]{4, 4,4,4,4,4,4,4,4,4, 5,5,5,5,5,5,5,5,5, 5,5,5,5,5,5, 6, 6,6,6,6,6,6,6,6,6,6,6, 7, 7,7,7,7,7,7,7,7, 8, 9, 10, 275, 276, 277, 278, 289, 340, 340, 383, 409, 410, 419, 436, 436, 436,5,5,5,5,6,6,6,6, 7,7,7, 436, 437, 437, 438, 443, 485, 524, 567, 567, 549, 550, 551, 549,5,5,5,5,5, 6, 6,6, 7, 7,7,7, 550, 551,775, 569,575,5,5,5,6,6,6,6,6, 7, 7,578, 574,577, 742,673,775, 781,828};
    public static short[] idItemCayThong = new short[]{8,8,8,8,8,8,8,9,9,9,549,549,549,549,550,550,551,551,436,436,437};
    public static short[] idItemTuiQuaGiaToc = new short[]{8,8,8,8,8,8,8,9,9,9,7,7,7,7,7,7,7,7,8,8,8,8,8,8,8,8,242,242,242, 283, 436,436,437};
    public static short[] idItemHomBlackFriday = new short[]{4, 4, 4, 4,4,4,4,4,4,4,4,4,4,4,4,4, 5,5,5,5,5,5,5,5,5,5, 5, 5, 5, 6, 6, 6, 7, 7, 7, 7, 7,7,7,7,7,7,7,7,7, 8, 9, 275, 276, 277, 278, 289, 289, 340, 340, 383, 409, 410, 436, 436,5,5,5,5,6,6,6,6,6,6, 7,7,7, 437, 443, 485, 524, 549, 550, 551, 549, 550, 551,569,574,5,5,5,5, 6, 6,6,6,6,6,6,6, 7, 7,7,7, 577,575,578, 742 ,673,775,828};

    public static void uesItem(Player p, Item item, byte index) {
        Message m = null;
        try {
            long num2 = Level.getLevel(p.c.level).exps;
            boolean checkExpDown = false;
            if (p.c.expdown > num2 * 30L / 100L) {
                checkExpDown = true;
            }
            if (p.c.isNhanban) {
                num2 = Level.getLevel(p.c.clone.level).exps;
                if (p.c.clone.expdown > num2 * 30L / 100L) {
                    checkExpDown = true;
                }
            }
            if (ItemTemplate.ItemTemplateId(item.id).level > p.c.get().level) {
                p.sendAddchatYellow("Trình độ không đủ để sử dụng vật phẩm này.");
                return;
            }
            ItemTemplate data = ItemTemplate.ItemTemplateId(item.id);

            if (data.gender != 2 && data.gender != p.c.gender) {
                return;
            }
            if (data.type == 26) {
                p.sendAddchatYellow("Vật phẩm liên quan đến nâng cấp, hãy gặp Kenshinto trong làng để sử dụng.");
                return;
            }

            if (data.level > p.c.get().level || (p.c.isNhanban && data.level > p.c.clone.level)) {
                p.sendAddchatYellow("Trình độ không đủ để sử dụng vật phẩm này.");
                return;
            }

            if ((p.c.get().nclass == 0 && item.id == 547) || (data.nclass > 0 && data.nclass != p.c.get().nclass)) {
                p.sendAddchatYellow("Môn phái không phù hợp.");
                return;
            }
            if ((item.id == 420 && GameSrc.SysClass(p.c.get().nclass) != 1) || (item.id == 421 && GameSrc.SysClass(p.c.get().nclass) != 2) || (item.id == 422 && GameSrc.SysClass(p.c.get().nclass) != 3)) {
                p.sendAddchatYellow("Thuộc tính của Yoroi không phù hợp để sử dụng.");
                return;
            }
            if (p.c.isNhanban && item.id == 547) {
                p.sendAddchatYellow("Chức năng này không thể sử dụng cho phân thân.");
                return;
            }
            if (ItemTemplate.isTypeBody(item.id)) {
                item.isLock = true;
                Item itemb = null;
                if(ItemTemplate.isIdNewCaiTrang(item.id) || ItemTemplate.checkIdNewWP(item.id) != -1 || ItemTemplate.checkIdNewMatNa(item.id) != -1 || ItemTemplate.checkIdNewBienHinh(item.id) != -1) {
                    itemb = p.c.get().ItemBody[data.type+16];
                    p.c.get().ItemBody[data.type+16] = item;
                }
                else {
                    itemb = p.c.get().ItemBody[data.type];
                    p.c.get().ItemBody[data.type] = item;
                }
                p.c.ItemBag[index] = itemb;

                if (data.type == 10) {
                    p.mobMeMessage(0, (byte) 0);
                }
                if (itemb != null && (itemb.id == 569 || itemb.id == 583)) {
                    p.removeEffect(36);
                }
                switch (item.id) {
                    case 246: {
                        p.mobMeMessage(70, (byte) 0);
                        break;
                    }
                    case 419: {
                        p.mobMeMessage(122, (byte) 0);
                        break;
                    }
                    case 568: {
                        p.setEffect(38, 0, (int) (item.expires - System.currentTimeMillis()), 0);
                        p.mobMeMessage(205, (byte) 0);
                        break;
                    }
                    case 569: {
                        p.setEffect(36, 0, (int) (item.expires - System.currentTimeMillis()), p.c.get().getPramItem(99));
                        p.mobMeMessage(206, (byte) 0);
                        break;
                    }
                    case 570: {
                        p.setEffect(37, 0, (int) (item.expires - System.currentTimeMillis()), 0);
                        p.mobMeMessage(207, (byte) 0);
                        break;
                    }
                    case 571: {
                        p.setEffect(39, 0, (int) (item.expires - System.currentTimeMillis()), 0);
                        p.mobMeMessage(208, (byte) 0);
                        break;
                    }
                    case 583: {
                        p.setEffect(36, 0, (int) (item.expires - System.currentTimeMillis()), p.c.get().getPramItem(99));
                        p.mobMeMessage(211, (byte) 1);
                        break;
                    }
                    case 584: {
                        p.mobMeMessage(236, (byte) 1);
                        break;
                    }
                    case 585: {
                        p.mobMeMessage(237, (byte) 1);
                        break;
                    }
                    case 586: {
                        p.mobMeMessage(214, (byte) 1);
                        break;
                    }
                    case 587: {
                        p.mobMeMessage(238, (byte) 1);
                        break;
                    }
                    case 588: {
                        p.mobMeMessage(216, (byte) 1);
                        break;
                    }
                    case 589: {
                        p.mobMeMessage(217, (byte) 1);
                        break;
                    }
                    case 742: {
                        p.mobMeMessage(229, (byte) 1);
                        break;
                    }
                    case 781: {
                        p.mobMeMessage(235, (byte) 1);
                        break;
                    }
                }
                m = new Message(11);
                m.writer().writeByte(index);
                m.writer().writeByte(p.c.get().speed());
                m.writer().writeInt(p.c.get().getMaxHP());
                m.writer().writeInt(p.c.get().getMaxMP());
                m.writer().writeShort(p.c.get().eff5buffHP());
                m.writer().writeShort(p.c.get().eff5buffMP());
                m.writer().flush();
                p.conn.sendMessage(m);
                m.cleanup();
                Service.CharViewInfo(p, false);
            } 
            else if (ItemTemplate.isTypeMounts(item.id)) {
                byte idM = (byte) (data.type - 29);
                Item itemM = p.c.get().ItemMounts[idM];
                if (idM == 4) {
                    if (p.c.get().ItemMounts[0] != null || p.c.get().ItemMounts[1] != null || p.c.get().ItemMounts[2] != null || p.c.get().ItemMounts[3] != null) {
                        p.conn.sendMessageLog("Bạn cần phải tháo trang bị thú cưỡi đang sử dụng.");
                        return;
                    }
                    if (!item.isLock) {
                        byte i;
                        int op;
                        Option option2;
                        for (i = 0; i < 4; ++i) {
                            op = -1;
                            do {
                                op = Util.nextInt(UseItem.arrOp.length);
                                for (Option option : item.options) {
                                    if (UseItem.arrOp[op] == option.id) {
                                        op = -1;
                                        break;
                                    }
                                }
                            } while (op == -1);
                            if(op == -1) {
                                return;
                            }
                            int par = UseItem.arrParam[op];
                            if (item.isExpires) {
                                par *= 10;
                            }
                            option2 = new Option(UseItem.arrOp[op], par);
                            item.options.add(option2);
                        }
                    }
                    if (p.c.clone != null && p.c.isNhanban) {
                    if (item.id == 801) {

                        p.c.clone.ID_HORSE = 47;
                    }
                    if (item.id == 802) {
                        p.c.clone.ID_HORSE = 48;
                    }
                    if (item.id == 803) {
                        p.c.clone.ID_HORSE = 49;
                    }
                    if (item.id == 798) {
                        p.c.clone.ID_HORSE = 36;
                    }if (item.id == 828) {
                        p.c.clone.ID_HORSE = 63;
                    }
                    Service.CharViewInfo(p, false);
                    }
                }
                else if (p.c.get().ItemMounts[4] == null) {
                    p.conn.sendMessageLog("Bạn cần phải tháo trang bị thú cưỡi đang sử dụng.");
                    return;
                }
                item.isLock = true;
                p.c.ItemBag[index] = itemM;
                p.c.get().ItemMounts[idM] = item;

                m = new Message(11);
                m.writer().writeByte(index);
                m.writer().writeByte(p.c.get().speed());
                m.writer().writeInt(p.c.get().getMaxHP());
                m.writer().writeInt(p.c.get().getMaxMP());
                m.writer().writeShort(p.c.get().eff5buffHP());
                m.writer().writeShort(p.c.get().eff5buffMP());
                m.writer().flush();
                p.conn.sendMessage(m);
                m.cleanup();

                if (ItemTemplate.isTypeMounts(item.id)) {
                    Player player;
                    for(int i = p.c.tileMap.players.size() - 1; i >= 0; i--) {
                        player = p.c.tileMap.players.get(i);
                        if(player != null && player.c != null) {
                            p.c.tileMap.sendMounts(p.c.get(), player);
                        }
                    }
                }
            }             
            else if (data.skill > 0) {
                byte skill = data.skill;
                if (item.id == 547) {
                    skill += p.c.get().nclass;
                }
                p.openBookSkill(index, skill);
                return;
            }
            else {
                byte numbagnull = p.c.getBagNull();
                switch (item.id) {
                    case 13: {
                        if (p.c.pk > 10 || checkExpDown) {
                            p.sendAddchatYellow(Language.MAX_EXP_DOWN);
                            return;
                        }
                        if (p.buffHP(25)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    case 14: {
                        if (p.c.pk > 10 || checkExpDown) {
                            p.sendAddchatYellow(Language.MAX_EXP_DOWN);
                            return;
                        }
                        if (p.buffHP(90)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    case 15: {
                        if (p.c.pk > 10 || checkExpDown) {
                            p.sendAddchatYellow(Language.MAX_EXP_DOWN);
                            return;
                        }
                        if (p.buffHP(230)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    case 16: {
                        if (p.c.pk > 10 || checkExpDown) {
                            p.sendAddchatYellow(Language.MAX_EXP_DOWN);
                            return;
                        }
                        if (p.buffHP(400)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    case 17: {
                        if (p.c.pk > 10 || checkExpDown) {
                            p.sendAddchatYellow(Language.MAX_EXP_DOWN);
                            return;
                        }
                        if (p.buffHP(650)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    case 565: {
                        if (p.c.pk > 10 || checkExpDown) {
                            p.sendAddchatYellow(Language.MAX_EXP_DOWN);
                            return;
                        }
                        if (p.buffHP(1500)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    case 18: {
                        if (p.c.pk > 10 || checkExpDown) {
                            p.sendAddchatYellow(Language.MAX_EXP_DOWN);
                            return;
                        }
                        if (p.buffMP(150)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    case 19: {
                        if (p.c.pk > 10 || checkExpDown) {
                            p.sendAddchatYellow(Language.MAX_EXP_DOWN);
                            return;
                        }
                        if (p.buffMP(500)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    case 20: {
                        if (p.c.pk > 10 || checkExpDown) {
                            p.sendAddchatYellow(Language.MAX_EXP_DOWN);
                            return;
                        }
                        if (p.buffMP(1000)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    case 21: {
                        if (p.c.pk > 10 || checkExpDown) {
                            p.sendAddchatYellow(Language.MAX_EXP_DOWN);
                            return;
                        }
                        if (p.buffMP(2000)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    case 22: {
                        if (p.c.pk > 10 || checkExpDown) {
                            p.sendAddchatYellow(Language.MAX_EXP_DOWN);
                            return;
                        }
                        if (p.buffMP(3500)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    case 566: {
                        if (p.c.pk > 10 || checkExpDown) {
                            p.sendAddchatYellow(Language.MAX_EXP_DOWN);
                            return;
                        }
                        if (p.buffMP(5000)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    case 23: {
                        if (p.dungThucan((byte) 0, 3, 1800)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    case 24: {
                        if (p.dungThucan((byte) 1, 20, 1800)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    case 25: {
                        if (p.dungThucan((byte) 2, 30, 1800)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    case 26: {
                        if (p.dungThucan((byte) 3, 40, 1800)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    case 27: {
                        if (p.dungThucan((byte) 4, 50, 1800)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    case 29: {
                        if (p.dungThucan((byte) 28, 60, 1800)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    case 30: {
                        if (p.dungThucan((byte) 28, 60, 259200)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    case 249: {
                        if (p.dungThucan((byte) 3, 40, 259200)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    case 250: {
                        if (p.dungThucan((byte) 4, 50, 259200)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    case 409: {
                        if (p.dungThucan((byte) 30, 75, 86400)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    case 410: {
                        if (p.dungThucan((byte) 31, 90, 86400)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    case 567: {
                        if (p.dungThucan((byte) 35, 120, 86400)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    //Nhân sâm
                    case 28: {
//                        if (p.c.isNhanban) {
//                            p.conn.sendMessageLog(Language.NOT_FOR_PHAN_THAN);
//                            return;
//                        }
                         if (p.c.level > 130){
                             p.sendAddchatYellow("Nhân sâm chỉ sử dụng được cho level dưới 130!");
                             return;
                         }
                        long expup = (Level.getMaxExp(p.c.get().level + 1) - Level.getMaxExp(p.c.get().level)) / 10;
                        p.updateExp(expup);
                        p.sendAddchatYellow("Bạn nhận được " + expup + " kinh nghiệm.");
                        p.c.removeItemBag(index, 1);
                        break;
                    }
                    case 34:
                    case 36: {
                        Map map = Manager.getMapid(p.c.mapLTD);
                        if (map != null) {
                            byte i;
                            for (i = 0; i < map.area.length; ++i) {
                                if (map.area[i].numplayers < map.template.maxplayers) {
                                    p.c.tileMap.leave(p);
                                    map.area[i].EnterMap0(p.c);
                                    if (item.id == 34) {
                                        p.c.removeItemBag(index, 1);
                                    }
                                    return;
                                }
                            }
                            break;
                        }
                        break;
                    }
                    //phúc nang nhẫn giả
                    case 38: {
                        if (p.c.isNhanban) {
                            p.sendAddchatYellow(Language.NOT_FOR_PHAN_THAN);
                            return;
                        }
                        int per = Util.nextInt(UseItem.idItemPhucNangNhanGia.length);
                        p.c.removeItemBag(index, 1);
                        if (UseItem.idItemPhucNangNhanGia[per] == -1) {
                            long yenran = Util.nextInt(100000, 150000);
                            p.c.upyenMessage(yenran);
                            p.sendAddchatYellow("Bạn nhận được " + yenran + " yên.");
                        } else {
                            p.c.addItemBag(UseItem.idItemPhucNangNhanGia[per] == 28 ? true : false, ItemTemplate.itemDefault(UseItem.idItemPhucNangNhanGia[per]));
                        }
                        break;
                    }
                    //Hiếu chiến
                    case 257: {
                        if (p.c.get().pk > 0) {
                            p.c.get().pk -= 5;
                            if (p.c.get().pk < 0) {
                                p.c.get().pk = 0;
                            }
                            p.sendAddchatYellow("Điểm hiếu chiến của bạn còn lại là " + p.c.get().pk);
                            p.c.removeItemBag(index, 1);
                            break;
                        }
                        p.sendAddchatYellow("Bạn không có điểm hiếu chiến.");
                        break;
                    }
                    //Ngọc rồng
                    case 222:
                    case 223:
                    case 224:
                    case 225:
                    case 226:
                    case 227:
                    case 228: {
                        if (p.c.nclass == 0) {
                            p.conn.sendMessageLog("Bạn cần nhập học để sử dụng vật phẩm này.");
                            return;
                        }
                        if (p.c.quantityItemyTotal(222) < 1 || p.c.quantityItemyTotal(223) < 1 || p.c.quantityItemyTotal(224) < 1 || p.c.quantityItemyTotal(225) < 1 || p.c.quantityItemyTotal(226) < 1 || p.c.quantityItemyTotal(227) < 1 || p.c.quantityItemyTotal(228) < 1) {
                            p.conn.sendMessageLog("Bạn không có đủ 7 viên ngọc rồng 1-7 sao để gọi rồng.");
                            return;
                        }
                        if (p.c.getBagNull() < 1) {
                            p.sendAddchatYellow(Language.NOT_ENOUGH_BAG);
                            return;
                        }
                        synchronized (LOCK) {
                            try {
                                m = new Message(-30);
                                m.writer().writeByte(-58);
                                m.writer().writeInt(p.c.get().id);
                                m.writer().flush();
                                p.conn.sendMessage(m);
                                m.cleanup();

                                m = new Message(-30);
                                m.writer().writeByte(-57);
                                m.writer().flush();
                                p.c.tileMap.sendMessage(m);
                                m.cleanup();

                                LOCK.wait(400L);
                                p.c.get().isGoiRong = true;
                                LOCK.wait(400L);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        int i;
                        byte count = 0;
                        for (i = 222; i <= 228; i++) {
                            if (p.c.getIndexBagid(i, false) != -1) {
                                p.c.removeItemBag(p.c.getIndexBagid(i, false), 1);
                                count++;
                            } else {
                                p.c.removeItemBag(p.c.getIndexBagid(i, true), 1);
                            }
                        }
                        byte nClassC = p.c.get().nclass;
                        if (p.c.isNhanban) {
                            nClassC = p.c.clone.nclass;
                        }
                        p.c.addItemBag(false, ItemTemplate.itemDefault(419 + GameSrc.SysClass(nClassC), count == 7 ? false : true));
                        break;
                    }
                    //Túi vải
                    case 215:
                    case 229:
                    case 283:
                    case 829: {
                        byte level = (byte) ((item.id != 215) ? ((item.id != 229) ? ((item.id != 283)  ? 4 : 3 ) : 2) : 1);
                        if (level > p.c.levelBag + 1) {
                            p.sendAddchatYellow("Cần mở Túi vải cấp " + (p.c.levelBag + 1) + " mới có thể mở được túi vải này.");
                            return;
                        }
                        if (p.c.levelBag >= level) {
                            p.sendAddchatYellow("Bạn đã mở túi vải này rồi.");
                            return;
                        }
                        p.c.levelBag = level;
                        p.c.maxluggage += UseItem.arrOpenBag[level];
                        Item[] bag = new Item[p.c.maxluggage];
                        short j;
                        for (j = 0; j < p.c.ItemBag.length; ++j) {
                            bag[j] = p.c.ItemBag[j];
                        }
                        (p.c.ItemBag = bag)[index] = null;
                        p.openBagLevel(index);
                        break;
                    }
                    //Giấy tẩy tiềm năng
                    case 240: {
                        p.c.removeItemBag(index, 1);
                        p.c.get().countTayTiemNang++;
                        p.sendAddchatYellow("Số lần tẩy điểm tiềm năng tăng thêm 1");
                        break;
                    }
                    //giấy tẩy kỹ năng
                    case 241: {
                        p.c.removeItemBag(index, 1);
                        p.c.get().countTayKyNang++;
                        p.sendAddchatYellow("Số lần tẩy điểm kỹ năng tăng thêm 1");
                        break;
                    }
                    case 248: {
                        Effect eff = p.c.get().getEffId(22);
                        if (eff != null) {
                            long time = eff.timeRemove + 18000000L;
                            p.setEffect(22, 0, (int) (time - System.currentTimeMillis()), 2);
                        } else {
                            p.setEffect(22, 0, 18000000, 2);
                        }
                        p.c.removeItemBag(index, 1);
                        break;
                    }
                    //giấy vụn
                    case 251: {
                        if (item.quantity < 300) {
                            p.sendAddchatYellow("Bạn cần ít nhất 300 mảnh giấy vụn mới có thể sử dụng.");
                            return;
                        }
                        if (numbagnull == 0) {
                            p.conn.sendMessageLog("Hành trang không đủ chỗ trống.");
                            return;
                        }
                        p.typemenu = -125;
                        Menu.doMenuArray(p, new String[]{"Sách kỹ năng sơ", "Sách tiềm năng sơ"});
                        break;
                    }
                    //sách kỹ năng sơ
                    case 252: {
                        if (p.c.isNhanban) {
                            p.sendAddchatYellow("Phân thân không thể sử dụng vật phẩm này.");
                            return;
                        }
                        if (p.c.get().useKyNang < 1) {
                            p.sendAddchatYellow("Bạn đã hết số lần sử dụng vật phẩm này.");
                            return;
                        }
                        p.c.get().useKyNang--;
                        p.c.get().spoint += 1;
                        p.c.removeItemBag(index, 1);
                        p.loadSkill();
                        p.sendAddchatYellow("Bạn nhận được 1 điểm kỹ năng.");
                        break;
                    }
                    //sách tiềm năng sơ
                    case 253: {
                        if (p.c.isNhanban) {
                            p.sendAddchatYellow("Phân thân không thể sử dụng vật phẩm này.");
                            return;
                        }
                        if (p.c.get().useTiemNang < 1) {
                            p.sendAddchatYellow("Bạn đã hết số lần sử dụng vật phẩm này.");
                            return;
                        }
                        p.c.get().useTiemNang--;
                        p.c.get().ppoint += 10;
                        p.loadPpoint();
                        p.c.removeItemBag(index, 1);
                        p.sendAddchatYellow("Bạn nhận được 10 điểm tiềm năng.");
                        break;
                    }
                    case 254:
                    case 255:
                    case 256: {
                        if (p.c.expdown == 0) {
                            p.conn.sendMessageLog("Bạn không có kinh nhiệm âm để sử dụng vật phẩm này!");
                            return;
                        }
                        if (item.id == 254 && p.c.level >= 30) {
                            p.conn.sendMessageLog("Trình độ của bạn không phù hợp để sử dụng vật phẩm này.");
                            return;
                        }
                        if (item.id == 255 && (p.c.level < 30 || p.c.level >= 60)) {
                            p.conn.sendMessageLog("Trình độ của bạn không phù hợp để sử dụng vật phẩm này.");
                            return;
                        }
                        if (item.id == 256 && p.c.level < 60) {
                            p.conn.sendMessageLog("Trình độ của bạn không phù hợp để sử dụng vật phẩm này.");
                            return;
                        }
                        p.updateExp(p.c.expdown);
                        p.sendAddchatYellow("Kinh nghiệm âm của bạn đã được xoá.");
                        p.c.removeItemBag(index, 1);
                        break;
                    }
                    //lam thảo dược
                    case 261: {
                        if (p.c.isNhanban) {
                            p.sendAddchatYellow(Language.NOT_FOR_PHAN_THAN);
                            return;
                        }
                        if (!p.c.tileMap.map.mapLDGT()) {
                            p.sendAddchatYellow("Vật phẩm chỉ có thể được dùng trong Lãnh Địa Gia Tộc.");
                            return;
                        }
                        p.setEffect(23, 0, 300000, 2000);
                        p.c.removeItemBag(index, 1);
                        break;
                    }
                    //Túi quà gia tộc
                    case 263: {
                        if (p.c.isNhanban) {
                            p.sendAddchatYellow(Language.NOT_FOR_PHAN_THAN);
                            return;
                        }
                        int per = Util.nextInt(UseItem.idItemTuiQuaGiaToc.length);
                        p.c.addItemBag(true, ItemTemplate.itemDefault(UseItem.idItemTuiQuaGiaToc[per]));
                        p.c.removeItemBag(index, 1);
                        break;
                    }
                    case 268: {
                        if (p.c.isNhanban) {
                            p.sendAddchatYellow(Language.NOT_FOR_PHAN_THAN);
                            return;
                        }
                        if (p.c.useTaThuLenh == 0) {
                            p.conn.sendMessageLog("Số lần sử dụng lệnh bài hang động của bạn hôm nay đã hết.");
                            return;
                        }
                        p.c.useTaThuLenh--;
                        p.c.countTaskTaThu -= 2;
                        p.sendAddchatYellow("Số lần nhận nhiệm vụ tà thú tăng thêm 2 lần");
                        p.c.removeItemBag(index, 1);
                        break;
                    }
                    //Đan dược
                    case 275: {
                        p.setEffect(24, 0, 600000, 500);
                        p.c.removeItemBag(index, 1);
                        break;
                    }
                    case 276: {
                        p.setEffect(25, 0, 600000, 500);
                        p.c.removeItemBag(index, 1);
                        break;
                    }
                    case 277: {
                        p.setEffect(26, 0, 600000, 100);
                        p.c.removeItemBag(index, 1);
                        break;
                    }
                    case 278: {
                        p.setEffect(27, 0, 600000, 1000);
                        p.c.removeItemBag(index, 1);

                        m = new Message(11);
                        m.writer().writeByte(index);
                        m.writer().writeByte(p.c.get().speed());
                        m.writer().writeInt(p.c.get().getMaxHP());
                        m.writer().writeInt(p.c.get().getMaxMP());
                        m.writer().writeShort(p.c.get().eff5buffHP());
                        m.writer().writeShort(p.c.get().eff5buffMP());
                        m.writer().flush();
                        p.conn.sendMessage(m);
                        m.cleanup();

                        break;
                    }
                    case 280: {
                        if (p.c.useCave == 0) {
                            p.conn.sendMessageLog("Số lần sử dụng lệnh bài hang động của bạn hôm nay đã hết.");
                            return;
                        }
                        p.c.nCave++;
                        p.c.useCave--;
                        p.sendAddchatYellow("Số lần vào hang động tăng thêm 1 lần, hôm nay bạn chỉ cần có thể sử dụng lệnh bài " + p.c.useCave + " lần");
                        p.c.removeItemBag(index, 1);
                        break;
                    }
                    //rương may mắn
                    case 272: {
                        if (numbagnull == 0) {
                            p.conn.sendMessageLog("Hành trang không đủ chỗ trống.");
                            return;
                        }
                        p.c.removeItemBag(index, 1);
                        if (Util.nextInt(3) == 0) {
                            int num = Util.nextInt(200000, 250000);
                            p.c.upyenMessage(num);
                            p.sendAddchatYellow("Bạn nhận được " + num + " yên");
                        } else if (Util.nextInt(150) == 0) {
                            p.c.addItemBag(false, ItemTemplate.itemDefault(GameSrc.arrNgocRong[Util.nextInt(GameSrc.arrNgocRong.length)], false));
                        } else {
                            short idI = UseItem.idItemRuongMayMan[Util.nextInt(UseItem.idItemRuongMayMan.length)];
                            ItemTemplate data2 = ItemTemplate.ItemTemplateId(idI);
                            Item itemup;
                            if (data2.type < 10) {
                                if (data2.type == 1) {
                                    itemup = ItemTemplate.itemDefault(idI);
                                    itemup.sys = GameSrc.SysClass(data2.nclass);
                                } else {
                                    byte sys = (byte) Util.nextInt(1, 3);
                                    itemup = ItemTemplate.itemDefault(idI, sys);
                                }
                            } else {
                                itemup = ItemTemplate.itemDefault(idI);
                            }
                            itemup.isLock = item.isLock;
                            int idOp2;
                            for (Option Option : itemup.options) {
                                idOp2 = Option.id;
                                Option.param = Util.nextInt(item.getOptionShopMin(idOp2, Option.param), Option.param);
                            }
                            p.c.addItemBag(true, itemup);
                        }
                        break;
                    }
                    //rương tinh xảo
                    case 282: {
                        if (numbagnull == 0) {
                            p.conn.sendMessageLog("Hành trang không đủ chỗ trống.");
                            return;
                        }
                        if (Util.nextInt(3) == 0) {
                            int num = Util.nextInt(400000, 500000);
                            p.c.upyenMessage(num);
                            p.sendAddchatYellow("Bạn nhận được " + num + " yên");
                        } else if (Util.nextInt(150) == 0) {
                            p.c.addItemBag(false, ItemTemplate.itemDefault(GameSrc.arrNgocRong[Util.nextInt(GameSrc.arrNgocRong.length)], false));
                        } else if (Util.nextInt(300) == 1) {
                            p.c.addItemBag(true, ItemTemplate.itemDefault(28, false));
                        } else if (Util.nextInt(300) == 0) {
                            p.c.addItemBag(true, ItemTemplate.itemDefault(383, false));
                        } else if (Util.nextInt(130) == 99) {
                            p.c.addItemBag(false, ItemTemplate.itemDefault(539, false));
                        } else {
                            short idI = UseItem.idItemRuongTinhXao[Util.nextInt(UseItem.idItemRuongTinhXao.length)];
                            ItemTemplate data2 = ItemTemplate.ItemTemplateId(idI);
                            Item itemup;
                            if (data2.type < 10) {
                                if (data2.type == 1) {
                                    itemup = ItemTemplate.itemDefault(idI);
                                    itemup.sys = GameSrc.SysClass(data2.nclass);
                                } else {
                                    byte sys = (byte) Util.nextInt(1, 3);
                                    itemup = ItemTemplate.itemDefault(idI, sys);
                                }
                            } else {
                                itemup = ItemTemplate.itemDefault(idI);
                            }
                            itemup.isLock = item.isLock;
                            for (Option Option : itemup.options) {
                                int idOp2 = Option.id;
                                Option.param = Util.nextInt(item.getOptionShopMin(idOp2, Option.param), Option.param);
                            }
                            p.c.addItemBag(true, itemup);
                        }
                        p.c.removeItemBag(index, 1);
                        break;
                    }
                    //bánh pía, đậu xanh
                    case 298:
                    case 299:
                    case 300:
                    case 301: {
                        if (Server.manager.event != 2) {
                            p.sendAddchatYellow(Language.END_EVENT);
                            return;
                        }
                        if (p.c.level < 20) {
                            p.sendAddchatYellow("Trình độ của bạn không đủ để sử dụng vật phẩm này.");
                            return;
                        }
                        p.updateExp(300000L);
                        p.c.removeItemBag(index, 1);
                        break;
                    }
                    //Hộp bánh thường
                    case 302: {
                        if (Server.manager.event != 2) {
                            p.sendAddchatYellow(Language.END_EVENT);
                            return;
                        }
                        if (numbagnull == 0) {
                            p.conn.sendMessageLog("Hành trang không đủ chỗ trống.");
                            return;
                        }
                        if (p.c.level < 20) {
                            p.sendAddchatYellow("Trình độ của bạn không đủ để sử dụng vật phẩm này.");
                            return;
                        }
                        p.c.removeItemBag(index, 1);
                        p.updateExp(500000L);
                        if (Util.nextInt(10) < 3) {
                            p.updateExp(100000L);
                        } else {
                            short idI = UseItem.idItemHopBanhThuong[Util.nextInt(UseItem.idItemHopBanhThuong.length)];
                            Item itemup = ItemTemplate.itemDefault(idI);
                            itemup.isLock = item.isLock;
                            p.c.addItemBag(true, itemup);
                        }
                        break;
                    }
                    //Hộp bánh thượng hạng
                    case 303: {
                        if (Server.manager.event != 2) {
                            p.sendAddchatYellow(Language.END_EVENT);
                            return;
                        }
                        if (numbagnull == 0) {
                            p.conn.sendMessageLog("Hành trang không đủ chỗ trống.");
                            return;
                        }
                        if (p.c.level < 20) {
                            p.sendAddchatYellow("Trình độ của bạn không đủ để sử dụng vật phẩm này.");
                            return;
                        }
                        p.c.removeItemBag(index, 1);
                        int perRuong = Util.nextInt(200);
                        p.updateExp(1000000L);
                        if (Util.nextInt(10) < 3) {
                            p.updateExp(1500000L);
                        } else if (perRuong == 50) {
                            p.c.addItemBag(true, ItemTemplate.itemDefault(385, false));
                            Manager.chatKTG("Người chơi " + p.c.name + " sử dụng " + data.name + " đã nhận được " + ItemTemplate.ItemTemplateId(385).name);
                        } else if (perRuong <= 1) {
                            Manager.chatKTG("Người chơi " + p.c.name + " sử dụng " + data.name + " đã nhận được " + ItemTemplate.ItemTemplateId(384).name);
                            p.c.addItemBag(true, ItemTemplate.itemDefault(384, false));
                        } else {
                            short idI = UseItem.idItemHopBanhThuongHang[Util.nextInt(UseItem.idItemHopBanhThuongHang.length)];
                            Item itemup = ItemTemplate.itemDefault(idI);
                            itemup.isLock = item.isLock;
                            p.c.addItemBag(true, itemup);
                            if (itemup.id == 308 || itemup.id == 309) {
                                ItemTemplate itemD = ItemTemplate.ItemTemplateId(itemup.id);
                                Manager.chatKTG("Người chơi " + p.c.name + " sử dụng " + data.name + " đã nhận được " + itemD.name);
                            }
                        }
                        break;
                    }
                    //Bánh phong lôi
                    case 308: {
                        if(p.c.get().isNhanban) {
                            p.sendAddchatYellow(Language.NOT_FOR_PHAN_THAN);
                            return;
                        }
                        if (p.c.get().useBanhPhongLoi < 1) {
                            p.sendAddchatYellow("Bạn đã hết số lần sử dụng vật phẩm này.");
                            return;
                        }
                        p.c.get().useBanhPhongLoi--;
                        p.c.get().spoint += 1;
                        p.c.removeItemBag(index, 1);
                        p.loadSkill();
                        p.sendAddchatYellow("Bạn nhận được 1 điểm kỹ năng.");
                        break;
                    }
                    //bánh băng hoả
                    case 309: {
                        if(p.c.get().isNhanban) {
                            p.sendAddchatYellow(Language.NOT_FOR_PHAN_THAN);
                            return;
                        }
                        if (p.c.get().useBanhBangHoa < 1) {
                            p.sendAddchatYellow("Bạn đã hết số lần sử dụng vật phẩm này.");
                            return;
                        }
                        p.c.get().useBanhBangHoa--;
                        p.c.get().ppoint += 10;
                        p.loadPpoint();
                        p.c.removeItemBag(index, 1);
                        p.sendAddchatYellow("Bạn nhận được 10 điểm tiềm năng.");
                        break;
                    }
                    //Rương bát bảo, bạch ngân, huyền bí
                    case 383:
                    case 384:
                    case 385: {
                        if (numbagnull == 0) {
                            p.conn.sendMessageLog("Hành trang không đủ chỗ trống.");
                            return;
                        }
                        if (p.c.get().nclass == 0) {
                            p.conn.sendMessageLog("Hãy nhập học để sử dụng vật phẩm này.");
                            return;
                        }
                        byte sys2 = -1;
                        int idI2;
                        if (Util.nextInt(2) == 0) {
                            if (p.c.gender == 0) {
                                if (p.c.get().level < 50 && item.id != 384 && item.id != 385) {
                                    idI2 = (new short[]{171, 161, 151, 141, 131})[Util.nextInt(5)];
                                } else if (p.c.get().level < 60 && item.id != 385) {
                                    idI2 = (new short[]{137, 163, 153, 143, 133})[Util.nextInt(5)];
                                } else if (p.c.get().level < 70) {
                                    idI2 = (new short[]{330, 329, 328, 327, 326})[Util.nextInt(5)];
                                } else {
                                    idI2 = (new short[]{368, 367, 366, 365, 364})[Util.nextInt(5)];
                                }
                            } else if (p.c.get().level < 50 && item.id != 384 && item.id != 385) {
                                idI2 = (new short[]{170, 160, 102, 140, 130})[Util.nextInt(5)];
                            } else if (p.c.get().level < 60 && item.id != 385) {
                                idI2 = (new short[]{172, 162, 103, 142, 132})[Util.nextInt(5)];
                            } else if (p.c.get().level < 70) {
                                idI2 = (new short[]{325, 323, 333, 319, 317})[Util.nextInt(5)];
                            } else {
                                idI2 = (new short[]{363, 361, 359, 357, 355})[Util.nextInt(5)];
                            }
                        } else if (Util.nextInt(2) == 1) {
                            if (p.c.get().nclass == 1 || p.c.get().nclass == 2) {
                                sys2 = 1;
                            } else if (p.c.get().nclass == 3 || p.c.get().nclass == 4) {
                                sys2 = 2;
                            } else if (p.c.get().nclass == 5 || p.c.get().nclass == 6) {
                                sys2 = 3;
                            }
                            if (p.c.get().level < 50 && item.id != 384 && item.id != 385) {
                                idI2 = (new short[]{97, 117, 102, 112, 107, 122})[p.c.get().nclass - 1];
                            } else if (p.c.get().level < 60 && item.id != 385) {
                                idI2 = (new short[]{98, 118, 103, 113, 108, 123})[p.c.get().nclass - 1];
                            } else if (p.c.get().level < 70) {
                                idI2 = (new short[]{331, 332, 333, 334, 335, 336})[p.c.get().nclass - 1];
                            } else {
                                idI2 = (new short[]{369, 370, 371, 372, 373, 374})[p.c.get().nclass - 1];
                            }
                        } else if (p.c.get().level < 50 && item.id != 384 && item.id != 385) {
                            idI2 = (new short[]{192, 187, 182, 177})[Util.nextInt(4)];
                        } else if (p.c.get().level < 60 && item.id != 385) {
                            idI2 = (new short[]{193, 188, 183, 178})[Util.nextInt(4)];
                        } else if (p.c.get().level < 70) {
                            idI2 = (new short[]{324, 322, 320, 318})[Util.nextInt(4)];
                        } else {
                            idI2 = (new short[]{362, 360, 358, 356})[Util.nextInt(4)];
                        }
                        ItemTemplate data2 = ItemTemplate.ItemTemplateId(idI2);
                        Item itemup;
                        if (data2.type < 10) {
                            if (data2.type == 1) {
                                itemup = ItemTemplate.itemDefault(idI2);
                                sys2 = GameSrc.SysClass(data2.nclass);
                            } else {
                                sys2 = (byte) Util.nextInt(1, 3);
                                itemup = ItemTemplate.itemDefault(idI2, sys2);
                            }
                        } else {
                            itemup = ItemTemplate.itemDefault(idI2);
                        }
                        itemup.sys = sys2;
                        byte nextup = 12;
                        if (item.id == 384) {
                            nextup = 14;
                        } else if (item.id == 385) {
                            nextup = 16;
                        }
                        itemup.isLock = item.isLock;
                        itemup.upgradeNext(nextup);
                        p.c.addItemBag(true, itemup);
                        p.c.removeItemBag(index, 1);
                        break;
                    }
                    //Diều giấy
                    case 434: {
                        if (Server.manager.event != 1) {
                            p.sendAddchatYellow(Language.END_EVENT);
                            return;
                        }
                        if (numbagnull == 0) {
                            p.conn.sendMessageLog("Hành trang không đủ chỗ trống.");
                            return;
                        }
                        if (p.c.level < 20) {
                            p.sendAddchatYellow("Trình độ của bạn không đủ để sử dụng vật phẩm này.");
                            return;
                        }
                        p.c.removeItemBag(index, 1);
                        p.updateExp(1500000L);
                        if (Util.nextInt(10) != 0) {
                            p.updateExp(3000000L);
                        } else {
                            short idI = UseItem.idItemDieuGiay[Util.nextInt(UseItem.idItemDieuGiay.length)];
                            Item itemup = ItemTemplate.itemDefault(idI);
                            itemup.isLock = item.isLock;
                            p.c.addItemBag(true, itemup);
                        }
                        break;
                    }
                    //Diều vải
                    case 435: {
                        if (Server.manager.event != 1) {
                            p.sendAddchatYellow(Language.END_EVENT);
                            return;
                        }
                        if (numbagnull == 0) {
                            p.conn.sendMessageLog("Hành trang không đủ chỗ trống.");
                            return;
                        }
                        if (p.c.level < 20) {
                            p.sendAddchatYellow("Trình độ của bạn không đủ để sử dụng vật phẩm này.");
                            return;
                        }
                        p.c.removeItemBag(index, 1);
                        p.updateExp(2000000L);
                        int perRuong = Util.nextInt(200);
                        if (Util.nextInt(10) != 0) {
                            p.updateExp(5000000L);
                        } else if (perRuong == 50) {
                            p.c.addItemBag(true, ItemTemplate.itemDefault(385, false));
                            Manager.chatKTG("Người chơi " + p.c.name + " sử dụng " + data.name + " đã nhận được " + ItemTemplate.ItemTemplateId(385).name);
                        } else if (perRuong <= 1) {
                            p.c.addItemBag(true, ItemTemplate.itemDefault(384, false));
                        } else {
                            short idI = UseItem.idItemDieuVai[Util.nextInt(UseItem.idItemDieuVai.length)];
                            Item itemup = ItemTemplate.itemDefault(idI);
                            itemup.isLock = item.isLock;
                            p.c.addItemBag(true, itemup);
                        }
                        break;
                    }
                    //Lệnh bài kinh nghiệm gia tộc sơ, trung, cao
                    case 436:
                    case 437:
                    case 438: {
                        ClanManager clan = ClanManager.getClanName(p.c.clan.clanName);
                        if (clan == null || clan.getMem(p.c.name) == null) {
                            p.sendAddchatYellow("Cần có gia tộc để sử dụng");
                            return;
                        }
                        if (item.id == 436) {
                            if (clan.level < 1) {
                                p.sendAddchatYellow("Yêu cầu gia tộc phải đạt cấp 5");
                                return;
                            }
                            p.upExpClan(Util.nextInt(100, 200));
                            p.c.removeItemBag(index, 1);
                        } else if (item.id == 437) {
                            if (clan.level < 10) {
                                p.sendAddchatYellow("Yêu cầu gia tộc phải đạt cấp 10");
                                return;
                            }
                            p.upExpClan(Util.nextInt(300, 800));
                            p.c.removeItemBag(index, 1);
                        } else {
                            if (item.id != 438) {
                                break;
                            }
                            if (clan.level < 15) {
                                p.sendAddchatYellow("Yêu cầu gia tộc phải đạt cấp 15");
                                return;
                            }
                            p.upExpClan(Util.nextInt(1000, 2000));
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    //Chuyển tinh thạch
                    case 454: {
                        if (p.updateSysMounts()) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    //Túi quà noel
                    case 477: {
                        if (Server.manager.event != 3) {
                            p.sendAddchatYellow(Language.END_EVENT);
                            return;
                        }
                        if (numbagnull < 1) {
                            p.conn.sendMessageLog("Hành trang không đủ chỗ trống.");
                            return;
                        }
                        if (p.c.level < 20) {
                            p.sendAddchatYellow("Trình độ của bạn không đủ để sử dụng vật phẩm này.");
                            return;
                        }

//                        Item itemup = ItemTemplate.itemDefault(Util.nextInt(652,655));
//                        itemup.isLock = item.isLock;
//                        p.c.addItemBag(true, itemup);
//                        return;

                        p.c.removeItemBag(index, 1);
                        if (p.status == 1) {
                            p.updateExp(250000L);
                            if (p.c.exptype == 1) {
                                p.c.expTN += 250000 / 1000;
                            }
                        } else {
                            p.updateExp(500000L);
                        }
                        if (Util.nextInt(10) < 7) {
                            if (p.status == 1) {
                                p.updateExp(2500000L);
                                if (p.c.exptype == 1) {
                                    p.c.expTN += 500000 / 1000;
                                }
                            } else {
                                p.updateExp(1000000L);
                            }
                        }
                        else {
                            short idI = UseItem.idItemBanhChocolate[Util.nextInt(UseItem.idItemBanhChocolate.length)];
                            Item itemup = ItemTemplate.itemDefault(idI);
                            itemup.isLock = item.isLock;
                            p.c.addItemBag(true, itemup);
                        }
                        break;
                    }
                    //Hộp quà noel
                    case 478: {
                        if (Server.manager.event != 3) {
                            p.sendAddchatYellow(Language.END_EVENT);
                            return;
                        }
                        if (numbagnull < 1) {
                            p.conn.sendMessageLog("Hành trang không đủ chỗ trống.");
                            return;
                        }
                        if (p.c.level < 20) {
                            p.sendAddchatYellow("Trình độ của bạn không đủ để sử dụng vật phẩm này.");
                            return;
                        }
                        p.c.removeItemBag(index, 1);
                        if (p.status == 1) {
                            p.updateExp(500000L);
                            if (p.c.exptype == 1) {
                                p.c.expTN += 500000 / 1000;
                            }
                        } else {
                            p.updateExp(1000000L);
                        }

                        int perRuong = Util.nextInt(2500);
                        int rhb = Util.nextInt(16500);
                        if (Util.nextInt(10) < 3) {
                            if (p.status == 1) {
                                p.updateExp(1000000L);
                                if (p.c.exptype == 1) {
                                    p.c.expTN += 1000000 / 1000;
                                }
                            } else {
                                p.updateExp(2000000L);
                            }
                            break;
                        } else if (rhb == 16008) {
                            Item itemUp = new Item();
                            itemUp.id = 385;
                            itemUp.quantity = 1;
                            itemUp.isExpires = false;
                            itemUp.isLock = false;
                            p.c.addItemBag(false, itemUp);
                            Manager.chatKTG("Người chơi " + p.c.name + " sử dụng " + data.name + " đã nhận được " + ItemTemplate.ItemTemplateId(385).name);
                            CheckRHB.checkRHBArrayList.add(new CheckRHB(p.c.name, ItemTemplate.ItemTemplateId(385).name, Util.toDateString(Date.from(Instant.now()))));
                            break;
                        } else if (perRuong < 1) {
                            Item itemUp = new Item();
                            itemUp.id = 384;
                            itemUp.quantity = 1;
                            itemUp.isExpires = false;
                            itemUp.isLock = false;
                            p.c.addItemBag(false, itemUp);
                            Manager.chatKTG("Người chơi " + p.c.name + " sử dụng " + data.name + " đã nhận được " + ItemTemplate.ItemTemplateId(384).name);
                            CheckRHB.checkRHBArrayList.add(new CheckRHB(p.c.name, ItemTemplate.ItemTemplateId(384).name, Util.toDateString(Date.from(Instant.now()))));
                            break;
                        } else if (perRuong == 1 || perRuong == 2) {
                            Item itemUp = new Item();
                            itemUp.id = 383;
                            itemUp.quantity = 1;
                            itemUp.isExpires = false;
                            itemUp.isLock = false;
                            p.c.addItemBag(false, itemUp);
                            break;
                        } else if (perRuong == 3) {
                            Item itemUp = new Item();
                            itemUp.id = 540;
                            itemUp.quantity = 1;
                            itemUp.isExpires = false;
                            itemUp.isLock = false;
                            p.c.addItemBag(false, itemUp);
                            break;
                        } else if (perRuong == 4 || perRuong == 5 || perRuong == 6) {
                            Item itemUp = new Item();
                            itemUp.id = 539;
                            itemUp.quantity = 1;
                            itemUp.isExpires = false;
                            itemUp.isLock = false;
                            p.c.addItemBag(false, itemUp);
                            break;
                        } else if (Util.nextInt(150) <= 1) {
                            Item itemup = ItemTemplate.itemDefault(Util.nextInt(652,655));
                            itemup.isLock = false;
                            p.c.addItemBag(false, itemup);
                            break;
                        } else {
                            short idI = UseItem.idItemBanhDauTay[Util.nextInt(UseItem.idItemBanhDauTay.length)];
                            Item itemup = ItemTemplate.itemDefault(idI);
                            if (idI == 781 || idI == 742 || idI == 523) {
                                itemup.quantity = 1;
                                itemup.isExpires = true;
                                itemup.expires = System.currentTimeMillis() + 604800000L;
                            }
                            itemup.isLock = item.isLock;
                            p.c.addItemBag(true, itemup);
                        }
                        break;
                    }
                    //Cổ lệnh
                    case 490: {
                        if (p.c.isNhanban) {
                            p.conn.sendMessageLog(Language.NOT_FOR_PHAN_THAN);
                            return;
                        }
                        if (p.c.pk > 0) {
                            p.sendAddchatYellow("Không thể vào làng cổ khi có điểm hiếu chiến lớn hơn 0");
                            return;
                        }
                        p.c.removeItemBag(index, 1);
                        p.c.tileMap.leave(p);
                        Map map = Server.maps[138];
                        byte k;
                        for (k = 0; k < map.area.length; k++) {
                            if (map.area[k].numplayers < map.template.maxplayers) {
                                map.area[k].EnterMap0(p.c);
                                break;
                            }
                        }
                        p.endLoad(true);
                        break;
                    }
                    //Khai nhãn phù
                    case 537: {
                        if (p.c.get().getEffId(40) == null) {
                            p.setEffect(41, 0, 7200000, 0);
                            p.c.removeItemBag(index, 1);
                        } else {
                            p.sendAddchatYellow("Bạn đã có hiệu quả cao hơn");
                        }
                        break;
                    }
                    //Thiên nhãn phù
                    case 538: {
                        p.setEffect(40, 0, 18000000, 0);
                        p.c.removeItemBag(index, 1);
                        break;
                    }
                    //x3 kinh nghiệm
                    case 539: {
                        p.setEffect(32, 0, 3600000, 3);
                        p.c.removeItemBag(index, 1);
                        break;
                    }
                    //x4 kinh nghiệm
                    case 540: {
                        p.setEffect(33, 0, 3600000, 4);
                        p.c.removeItemBag(index, 1);
                        break;
                    }
                    //Giày rách, bạc, vàng
                    case 549:
                    case 550:
                    case 551: {
                        long yenup = 0L;
                        yenup = 1000000L;
                        if (item.id == 550) {
                            yenup = 2000000L;
                        }
                        if (item.id == 551) {
                            yenup = 3000000L;
                        }
                        p.c.upyenMessage(yenup);
                        p.sendAddchatYellow("Bạn nhận được " + yenup + " yên.");
                        p.c.removeItemBag(index, 1);
                        break;
                    }
                    //Lục thanh hoa
                    case 573: {
                        if (p.updateXpMounts(200, (byte) 0)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    //Tử linh liên hoa
                    case 574: {
                        if (p.updateXpMounts(400, (byte) 0)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    //Linh lang hồ điệp
                    case 575: {
                        if (p.updateXpMounts(600, (byte) 0)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    //Bánh răng
                    case 576: {
                        if (p.updateXpMounts(100, (byte) 1)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    //IK
                    case 577: {
                        if (p.updateXpMounts(250, (byte) 1)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    //Thuốc cải tiến
                    case 578: {
                        if (p.updateXpMounts(500, (byte) 1)) {
                            p.c.removeItemBag(index, 1);
                        }
                        break;
                    }
                    
                    //Thiệp Chúc tết Thường
                    case 833: {
                        Service.sendInputDialog(p, (short) 114, "Nhập tên người cần chúc:");
                        break;
                    }
                    
                    //Thiệp Chúc tết Đặc Biệt
                    case 834: {
                         Service.sendInputDialog(p, (short) 115, "Nhập tên người cần chúc:");
                        break;
                    }
                    
                    
                     //Tràng pháo
                     case 675: {

                    if (Server.manager.event != 4) {
                        p.sendAddchatYellow("Sự kiện này đã kết thúc không còn sử dụng được vật phẩm này nữa");
                        return;
                    }
                    if (p.c.getBagNull() == 0) {
                        p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                        return;
                    } else {

                        p.updateExp(20000000);
                        short[] arId = new short[]{12,12,12,8,9,8,9,275,276,277,278,289,290,291,289,290,291,289,290,291,535,535,536,536,535,536,275,276,277,278,548,12,548,381,382,381,382,381,682,682,682,228,227,226,225,224,223,222,283,436,438,437,436,437,419,403,419,403,407,407,12,254,255,256,12,254,255,256,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,7,8,9,436,437,438,682,384,385,383,382,381,222,223,224,225,226,227,228,251, 308,309,548,275,276,277,278,539,540};
                        short idI = arId[Util.nextInt(arId.length)];
                        Item itemup = ItemTemplate.itemDefault(idI);
                        itemup.isLock = false;
                        p.c.removeItemBag(index, 1);
                        p.c.addItemBag(true, itemup);
                         
                    }
                    break;
                     }
                   
                
                    case 643: { //bánh chưng
                    if (Server.manager.event != 4) {
                        p.sendAddchatYellow("Sự kiện này đã kết thúc không còn sử dụng được vật phẩm này nữa");
                        return;
                    }
                    if (p.c.getBagNull() == 0) {
                        p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                        return;
                    }
                    p.updateExp(20000000);
                    short[] arId = new short[]{12,12,12,8,9,8,9,275,276,277,278,275,276,289,290,291,289,290,291,289,290,291,277,535,536,535,536,535,536,278,548,12,548,381,382,381,382,381,682,682,682,228,227,226,225,224,223,222,283,436,438,437,436,437,419,403,419,403,407,407,12,254,255,256,12,254,255,256,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,7,8,9,436,437,438,682,384,385,383,382,381,222,223,224,225,226,227,228,251, 308,309,548,275,276,277,278,539,540};
                            short idI = arId[Util.nextInt(arId.length)];
                            Item itemup = ItemTemplate.itemDefault(idI);
                            itemup.isLock = false;
                            p.c.removeItemBag(index, 1);
                            p.c.addItemBag(true, itemup);
                }
                break;

                    case 644: {  //bánh tét
                    if (Server.manager.event != 4) {
                        p.sendAddchatYellow("Sự kiện này đã kết thúc không còn sử dụng được vật phẩm này nữa");
                        return;
                    }
                     if (p.c.getBagNull() == 0) {
                        p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                        return;
                    }
                    p.updateExp(10000000);
                    short[] arId = new short[]{12,12,12,8,9,8,9,275,276,277,278,275,289,290,291,289,290,291,289,290,291,276,277,278,535,536,535,536,535,536,548,12,548,381,382,381,382,381,682,682,682,228,227,226,225,224,223,222,283,436,438,437,436,437,419,403,419,403,407,407,12,254,255,256,12,254,255,256,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,7,8,9,436,437,438,682,384,385,383,382,381,222,223,224,225,226,227,228,251, 308,309,548,275,276,277,278,539,540};
                            short idI = arId[Util.nextInt(arId.length)];
                            Item itemup = ItemTemplate.itemDefault(idI);
                            itemup.isLock = false;
                            p.c.removeItemBag(index, 1);
                            p.c.addItemBag(true, itemup);
                    }

                break;


                case 381: { //lì xì nhỏ
                    if (Server.manager.event != 4) {
                        p.sendAddchatYellow("Sự kiện này đã kết thúc không còn sử dụng được vật phẩm này nữa");
                        return;
                    }
                    if (p.c.getBagNull() == 0) {
                        p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                        return;
                    }
                    p.updateExp(5000000);
                    short[] arId = new short[]{12,12,12,8,9,8,9,275,276,277,278,275,289,290,291,289,290,291,289,290,291,535,535,535,536,536,536,276,277,278,548,12,548,381,382,381,382,381,682,682,682,228,227,226,225,224,223,222,283,436,438,437,436,437,419,403,419,403,407,407,12,254,255,256,12,254,255,256,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,7,8,9,436,437,438,682,384,385,383,382,381,222,223,224,225,226,227,228,251, 308,309,548,275,276,277,278,539,540};
                            short idI = arId[Util.nextInt(arId.length)];
                            Item itemup = ItemTemplate.itemDefault(idI);
                            itemup.isLock = false;
                            p.c.removeItemBag(index, 1);
                            p.c.addItemBag(true, itemup);
                }
                break;

                    case 382: {  //lì xì lớn
                    if (Server.manager.event != 4) {
                        p.sendAddchatYellow("Sự kiện này đã kết thúc không còn sử dụng được vật phẩm này nữa");
                        return;
                    }
                     if (p.c.getBagNull() == 0) {
                        p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                        return;
                    }
                    p.updateExp(15000000);
                    short[] arId = new short[]{12,12,12,8,9,8,9,275,276,277,278,289,290,291,289,290,291,289,290,291,275,535,535,536,536,535,536,276,277,278,548,12,548,381,382,381,382,381,682,682,682,228,227,226,225,224,223,222,283,436,438,437,436,437,419,403,419,403,407,407,12,254,255,256,12,254,255,256,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,7,8,9,436,437,438,682,384,385,383,382,381,222,223,224,225,226,227,228,251, 308,309,548,275,276,277,278,539,540};
                            short idI = arId[Util.nextInt(arId.length)];
                            Item itemup = ItemTemplate.itemDefault(idI);
                            itemup.isLock = false;
                            p.c.removeItemBag(index, 1);
                            p.c.addItemBag(true, itemup);
                    }

                break;
                    
                    //Rương ma quái
                    case 647: {
                        if (numbagnull == 0) {
                            p.conn.sendMessageLog("Hành trang không đủ chỗ trống.");
                            return;
                        }

                        p.c.removeItemBag(index, 1);
                        if (Util.nextInt(3) == 0) {
                            int num = Util.nextInt(800000, 12000000);
                            p.c.upyenMessage(num);
                            p.sendAddchatYellow("Bạn nhận được " + num + " yên");
                        } else if (Util.nextInt(130) <= 2) {
                            p.c.addItemBag(false, ItemTemplate.itemDefault(GameSrc.arrNgocRong[Util.nextInt(GameSrc.arrNgocRong.length)], false));
                        } else if (Util.nextInt(800) == 1) {
                            p.c.addItemBag(true, ItemTemplate.itemDefault(28, false));
                        } else if (Util.nextInt(300) == 0) {
                            p.c.addItemBag(true, ItemTemplate.itemDefault(383, false));
                        } else if (Util.nextInt(100) < 10) {
                            Item itemupNew = ItemTemplate.itemDefault(Util.nextInt(801,802), false);
                            if(Util.nextInt(5000) == 4999) {
                                itemupNew.isExpires = false;
                                itemupNew.expires = -1l;
                            } else {
                                itemupNew.isExpires = true;
                                itemupNew.expires = Util.TimeDay(GameSrc.ArrdayLuck[Util.nextInt(GameSrc.ArrdayLuck.length)]);;
                            }
                            p.c.addItemBag(true, itemupNew);
                        } else if (Util.nextInt(12000) == 0) {
                            p.c.addItemBag(true, ItemTemplate.itemDefault(797, false));
                        } else {
                            short idI = UseItem.idItemRuongMaQuai[Util.nextInt(UseItem.idItemRuongMaQuai.length)];
                            ItemTemplate data2 = ItemTemplate.ItemTemplateId(idI);
                            Item itemup;
                            if (data2.type < 10) {
                                if (data2.type == 1) {
                                    itemup = ItemTemplate.itemDefault(idI);
                                    itemup.sys = GameSrc.SysClass(data2.nclass);
                                } else {
                                    byte sys = (byte) Util.nextInt(1, 3);
                                    itemup = ItemTemplate.itemDefault(idI, sys);
                                }
                            } else {
                                itemup = ItemTemplate.itemDefault(idI);
                            }
                            itemup.isLock = false;
                            int idOp2;
                            for (Option Option : itemup.options) {
                                idOp2 = Option.id;
                                Option.param = Util.nextInt(item.getOptionShopMin(idOp2, Option.param), Option.param);
                            }
                            p.c.addItemBag(true, itemup);
                        }
                        break;
                    }
                    //Bánh chocolate
                    case 671: {
                        if (Server.manager.event != 3) {
                            p.sendAddchatYellow(Language.END_EVENT);
                            return;
                        }
                        if (numbagnull == 0) {
                            p.conn.sendMessageLog("Hành trang không đủ chỗ trống.");
                            return;
                        }
                        if (p.c.level < 20) {
                            p.sendAddchatYellow("Trình độ của bạn không đủ để sử dụng vật phẩm này.");
                            return;
                        }
                        p.c.removeItemBag(index, 1);

                        if (p.status == 1) {
                            p.updateExp(750000L);
                            if (p.c.exptype == 1) {
                                p.c.expTN += 750000 / 1000;
                            }
                        } else {
                            p.updateExp(1500000L);
                        }
                        if (Util.nextInt(10) < 4) {
                            if (p.status == 1) {
                                p.updateExp(1500000L);
                                if (p.c.exptype == 1) {
                                    p.c.expTN += 1500000 / 1000;
                                }
                            } else {
                                p.updateExp(3000000L);
                            }
                        } else if (Util.nextInt(160) <= 1) {
                            Item itemup = ItemTemplate.itemDefault(Util.nextInt(654,655));
                            itemup.isLock = false;
                            p.c.addItemBag(false, itemup);
                            break;
                        }else {
                            short idI = UseItem.idItemBanhChocolate[Util.nextInt(UseItem.idItemBanhChocolate.length)];
                            Item itemup = ItemTemplate.itemDefault(idI);
                            itemup.isLock = item.isLock;
                            p.c.addItemBag(true, itemup);
                        }
                        break;
                    }
                    //Bánh dâu tây
                    case 672: {
                        if (Server.manager.event != 3) {
                            p.sendAddchatYellow(Language.END_EVENT);
                            return;
                        }
                        if (numbagnull < 1) {
                            p.conn.sendMessageLog("Hành trang không đủ chỗ trống.");
                            return;
                        }
                        if (p.c.level < 20) {
                            p.sendAddchatYellow("Trình độ của bạn không đủ để sử dụng vật phẩm này.");
                            return;
                        }
                        p.c.removeItemBag(index, 1);

                        if (p.status == 1) {
                            p.updateExp(1000000L);
                            if (p.c.exptype == 1) {
                                p.c.expTN += 1000000 / 1000;
                            }
                        } else {
                            p.updateExp(2000000L);
                        }

                        int perRuong = Util.nextInt(2000);
                        int rhb = Util.nextInt(15000);
                        int phuonghoang = Util.nextInt(10000); 
                        if (Util.nextInt(10) < 3) {
                            if (p.status == 1) {
                                p.updateExp(1500000L);
                                if (p.c.exptype == 1) {
                                    p.c.expTN += 1500000 / 1000;
                                }
                            } else {
                                p.updateExp(3000000L);
                            }
                            break;
                        } else if (Util.nextInt(160) <= 1) {
                            Item itemup = ItemTemplate.itemDefault(Util.nextInt(652,653));
                            itemup.isLock = false;
                            p.c.addItemBag(false, itemup);
                            break;
                        } else if (rhb == 14999) {
                            Item itemUp = new Item();
                            itemUp.id = 385;
                            itemUp.quantity = 1;
                            itemUp.isExpires = false;
                            itemUp.isLock = false;
                            p.c.addItemBag(false, itemUp);
                            Manager.chatKTG("Người chơi " + p.c.name + " sử dụng " + data.name + " đã nhận được " + ItemTemplate.ItemTemplateId(385).name);
                            CheckRHB.checkRHBArrayList.add(new CheckRHB(p.c.name, ItemTemplate.ItemTemplateId(385).name, Util.toDateString(Date.from(Instant.now()))));
                            return;
                        } else if (perRuong == 0) {
                            Item itemUp = new Item();
                            itemUp.id = 384;
                            itemUp.quantity = 1;
                            itemUp.isExpires = false;
                            itemUp.isLock = false;
                            p.c.addItemBag(false, itemUp);
                            Manager.chatKTG("Người chơi " + p.c.name + " sử dụng " + data.name + " đã nhận được " + ItemTemplate.ItemTemplateId(384).name);
                            CheckRHB.checkRHBArrayList.add(new CheckRHB(p.c.name, ItemTemplate.ItemTemplateId(384).name, Util.toDateString(Date.from(Instant.now()))));
                            return;
                        } else if (perRuong == 1 || perRuong == 2) {
                            Item itemUp = new Item();
                            itemUp.id = 383;
                            itemUp.quantity = 1;
                            itemUp.isExpires = false;
                            itemUp.isLock = false;
                            p.c.addItemBag(false, itemUp);
                            return;
                        } else if (perRuong == 4) {
                            Item itemUp = new Item();
                            itemUp.id = 340;
                            itemUp.quantity = 1;
                            itemUp.isExpires = false;
                            itemUp.isLock = false;
                            p.c.addItemBag(false, itemUp);
                            return;
                        } else if (perRuong == 6 || perRuong == 7) {
                            Item itemUp = new Item();
                            itemUp.id = 539;
                            itemUp.quantity = 1;
                            itemUp.isExpires = false;
                            itemUp.isLock = false;
                            p.c.addItemBag(false, itemUp);
                            return;
                        } else {
                            short idI = UseItem.idItemBanhDauTay[Util.nextInt(UseItem.idItemBanhDauTay.length)];
                            Item itemup = ItemTemplate.itemDefault(idI);
                            if (idI == 781 || idI == 742 || idI == 523 || idI == 828) {
                                itemup.quantity = 1;
                                itemup.isExpires = true;
                                itemup.expires = System.currentTimeMillis() + 604800000L;
                            }
                            itemup.isLock = item.isLock;
                            p.c.addItemBag(true, itemup);
                        }
                        break;
                    }
                    case 827: {
                        if (numbagnull < 1) {
                            p.conn.sendMessageLog("Hành trang không đủ chỗ trống.");
                            return;
                        }
                        if (p.c.level < 20) {
                            p.sendAddchatYellow("Trình độ của bạn không đủ để sử dụng vật phẩm này.");
                            return;
                        }
                        p.c.removeItemBag(index, 1);

                        int perRuong = Util.nextInt(2000);
                        int rhb = Util.nextInt(15000);
                        if (Util.nextInt(10) < 3) {
                        } else if (Util.nextInt(160) <= 1) {
                            Item itemup = ItemTemplate.itemDefault(Util.nextInt(652,653));
                            itemup.isLock = false;
                            p.c.addItemBag(false, itemup);
                            break;
                        } else if (rhb == 14999) {
                            Item itemUp = new Item();
                            itemUp.id = 385;
                            itemUp.quantity = 1;
                            itemUp.isExpires = false;
                            itemUp.isLock = false;
                            p.c.addItemBag(false, itemUp);
                            Manager.chatKTG("Người chơi " + p.c.name + " sử dụng " + data.name + " đã nhận được " + ItemTemplate.ItemTemplateId(385).name);
                            CheckRHB.checkRHBArrayList.add(new CheckRHB(p.c.name, ItemTemplate.ItemTemplateId(385).name, Util.toDateString(Date.from(Instant.now()))));
                            return;
                        } else if (perRuong == 0) {
                            Item itemUp = new Item();
                            itemUp.id = 384;
                            itemUp.quantity = 1;
                            itemUp.isExpires = false;
                            itemUp.isLock = false;
                            p.c.addItemBag(false, itemUp);
                            Manager.chatKTG("Người chơi " + p.c.name + " sử dụng " + data.name + " đã nhận được " + ItemTemplate.ItemTemplateId(384).name);
                            CheckRHB.checkRHBArrayList.add(new CheckRHB(p.c.name, ItemTemplate.ItemTemplateId(384).name, Util.toDateString(Date.from(Instant.now()))));
                            return;   
                        } else if (perRuong == 1 || perRuong == 2) {
                            Item itemUp = new Item();
                            itemUp.id = 383;
                            itemUp.quantity = 1;
                            itemUp.isExpires = false;
                            itemUp.isLock = false;
                            p.c.addItemBag(false, itemUp);
                            return;
                        } else if (perRuong == 4) {
                            Item itemUp = new Item();
                            itemUp.id = 340;
                            itemUp.quantity = 1;
                            itemUp.isExpires = false;
                            itemUp.isLock = false;
                            p.c.addItemBag(false, itemUp);
                            return;
                        } else if (perRuong == 6 || perRuong == 7) {
                            Item itemUp = new Item();
                            itemUp.id = 539;
                            itemUp.quantity = 1;
                            itemUp.isExpires = false;
                            itemUp.isLock = false;
                            p.c.addItemBag(false, itemUp);
                            return;
                        } else {
                            short idI = UseItem.idItemHomBlackFriday[Util.nextInt(UseItem.idItemHomBlackFriday.length)];
                            Item itemup = ItemTemplate.itemDefault(idI);
                            if (idI == 781 || idI == 742 || idI == 523 || idI == 828) {
                                itemup.quantity = 1;
                                itemup.isExpires = true;
                                itemup.expires = System.currentTimeMillis() + 604800000L;
                            }
                            itemup.isLock = item.isLock;
                            p.c.addItemBag(true, itemup);
                        }
                        break;
                    }
                    //Đá danh vọng
                    case 695:
                    case 696:
                    case 697:
                    case 698:
                    case 699:
                    case 700:
                    case 701:
                    case 702:
                    case 703:
                    case 704: {
                        if (p.c.isNhanban) {
                            p.sendAddchatYellow(Language.NOT_FOR_PHAN_THAN);
                            return;
                        }
                        if (p.c.getBagNull() < 1) {
                            p.sendAddchatYellow(Language.NOT_ENOUGH_BAG);
                            return;
                        }
                        if (item.id == 704) {
                            p.sendAddchatYellow("Vật phẩm đã đạt cấp độ tối đa.");
                            return;
                        }
                        if (item.quantity < 10) {
                            p.sendAddchatYellow("Bạn cần đủ 10 viên đá để nâng cấp.");
                            return;
                        }
                        int quantity = item.quantity;
                        int plus = item.quantity / 10;
                        p.c.removeItemBag((byte) index, quantity - quantity % 10);
                        Item itemUp = ItemTemplate.itemDefault(item.id + 1, item.isLock);
                        itemUp.quantity = plus;
                        p.c.addItemBag(true, itemUp);
                        break;
                    }
                    //Danh vọng phù
                    case 705: {
                        if (p.c.isNhanban) {
                            p.sendAddchatYellow(Language.NOT_FOR_PHAN_THAN);
                            return;
                        }
                        if (p.c.useDanhVongPhu == 0) {
                            p.conn.sendMessageLog("Số lần sử dụng Danh vọng phú của bạn hôm nay đã hết.");
                            return;
                        }
                        p.c.useDanhVongPhu--;
                        p.c.countTaskDanhVong += 5;
                        p.sendAddchatYellow("Số lần nhận nhiệm vụ Danh vọng tăng thêm 5 lần");
                        p.c.removeItemBag(index, 1);
                        break;
                    }

                    //Mảnh jirai
                    case 733:
                    case 734:
                    case 735:
                    case 736:
                    case 737:
                    case 738:
                    case 739:
                    case 740:
                    case 741: {
                        if(p.c.isNhanban) {
                            p.sendAddchatYellow(Language.NOT_FOR_PHAN_THAN);
                            return;
                        }
                        if(p.c.gender == 0) {
                            p.sendAddchatYellow("Giới tính không phù hợp.");
                            return;
                        }
                        int checkID = item.id-733;
                        if(p.c.ItemBST[checkID] == null) {
                            if(p.c.quantityItemyTotal(item.id) < 100) {
                                p.sendAddchatYellow("Bạn không đủ mảnh để ghép.");
                                return;
                            }
                            p.c.removeItemBag(p.c.getIndexBagid(item.id, true), 100);
                            p.c.ItemBST[checkID] = ItemTemplate.itemDefault(ItemTemplate.checkIdJiraiNam(checkID));
                            p.c.ItemBST[checkID].upgrade = 1;
                            p.c.ItemBST[checkID].isLock = true;
                            p.sendAddchatYellow(ItemTemplate.ItemTemplateId(p.c.ItemBST[checkID].id).name + " đã được thêm vào bộ sưu tập.");
                        } else {
                            if(p.c.ItemBST[checkID].upgrade >= 16) {
                                p.sendAddchatYellow("Bộ sưu tập này đã đạt điểm tối đa, không thể nâng cấp thêm.");
                                return;
                            }
                            if(p.c.quantityItemyTotal(item.id) < (p.c.ItemBST[checkID].upgrade + 1)*100) {
                                p.sendAddchatYellow("Bạn không đủ mảnh để nâng cấp.");
                                return;
                            }
                            p.c.ItemBST[checkID].upgrade += 1;
                            p.c.removeItemBag(p.c.getIndexBagid(item.id, true), p.c.ItemBST[checkID].upgrade*100);
                            p.sendAddchatYellow(ItemTemplate.ItemTemplateId(p.c.ItemBST[checkID].id).name + " đã được nâng cấp.");
                        }
                        break;
                    }

                    //Mảnh jirai
                    case 760:
                    case 761:
                    case 762:
                    case 763:
                    case 764:
                    case 765:
                    case 766:
                    case 767:
                    case 768: {
                        if(p.c.isNhanban) {
                            p.sendAddchatYellow(Language.NOT_FOR_PHAN_THAN);
                            return;
                        }
                        if(p.c.gender == 1) {
                            p.sendAddchatYellow("Giới tính không phù hợp.");
                            return;
                        }
                        int checkID = item.id-760;
                        if(p.c.ItemBST[checkID] == null) {
                            if(p.c.quantityItemyTotal(item.id) < 100) {
                                p.sendAddchatYellow("Bạn không đủ mảnh để ghép.");
                                return;
                            }
                            p.c.removeItemBag(p.c.getIndexBagid(item.id, true), 100);
                            p.c.ItemBST[checkID] = ItemTemplate.itemDefault(ItemTemplate.checkIdJiraiNu(checkID));
                            p.c.ItemBST[checkID].upgrade = 1;
                            p.sendAddchatYellow(ItemTemplate.ItemTemplateId(p.c.ItemBST[checkID].id).name + " đã được thêm vào bộ sưu tập.");
                        } else {
                            if(p.c.ItemBST[checkID].upgrade >= 16) {
                                p.sendAddchatYellow("Bộ sưu tập này đã đạt điểm tối đa, không thể nâng cấp thêm.");
                                return;
                            }
                            if(p.c.quantityItemyTotal(item.id) < (p.c.ItemBST[checkID].upgrade + 1)*100) {
                                p.sendAddchatYellow("Bạn không đủ mảnh để nâng cấp.");
                                return;
                            }
                            p.c.ItemBST[checkID].upgrade += 1;
                            p.c.removeItemBag(p.c.getIndexBagid(item.id, true), p.c.ItemBST[checkID].upgrade*100);
                            p.sendAddchatYellow(ItemTemplate.ItemTemplateId(p.c.ItemBST[checkID].id).name + " đã được nâng cấp.");
                        }
                        break;
                    }

                    //Tuần thú lệnh
                    case 743: {
                        if (Server.manager.event != 3) {
                            p.sendAddchatYellow(Language.END_EVENT);
                            return;
                        }
                        if (p.c.level < 40) {
                            p.conn.sendMessageLog("Trình độ của bạn không đủ để sử dụng vật phẩm này.");
                            return;
                        }
                        if (p.c.tileMap.map.getXHD() != -1 || p.c.mapid == 111 || p.c.mapid == 133 || p.c.tileMap.map.mapChienTruong()) {
                            p.conn.sendMessageLog("Bạn không thể sử dụng vật phẩm này tại đây.");
                            return;
                        }
                        BossTuanLoc bossTuanLoc = new BossTuanLoc(p.c.level);
                        if(bossTuanLoc != null && bossTuanLoc.map[0] != null && bossTuanLoc.map[0].area[0] != null) {
                            p.c.removeItemBag(index, 1);
                            p.c.tileMap.leave(p);
                            bossTuanLoc.map[0].area[0].EnterMap0(p.c);
                        }
                        break;
                    }
                    default: {
                        break;
                    }
                }
                return;
            }

            if(ItemTemplate.checkIdNewItems(item.id)) {
                if (ItemTemplate.checkIdNewWP(item.id) != -1) {
                    p.c.get().ID_WEA_PONE = ItemTemplate.idNewItemWP[1][ItemTemplate.checkIdNewWP(item.id)];
                } else if (ItemTemplate.checkIdNewMatNa(item.id) != -1) {
                    p.c.get().ID_MAT_NA = ItemTemplate.idNewItemMatNa[1][ItemTemplate.checkIdNewMatNa(item.id)];
                }  else if (ItemTemplate.checkIdNewMounts(item.id) != -1) {
                    p.c.get().ID_HORSE = ItemTemplate.idNewItemMounts[1][ItemTemplate.checkIdNewMounts(item.id)];
                }  else if (ItemTemplate.checkIdNewBienHinh(item.id) != -1) {
                    p.c.get().ID_Bien_Hinh = ItemTemplate.idNewItemBienHinh[1][ItemTemplate.checkIdNewBienHinh(item.id)];    
                } else if (ItemTemplate.checkIdNewCaiTrang(item.id) != -1) {
                    p.c.get().ID_HAIR = ItemTemplate.idNewItemCaiTrang[1][ItemTemplate.checkIdNewCaiTrang(item.id)];
                    p.c.get().ID_Body = ItemTemplate.idNewItemCaiTrang[2][ItemTemplate.checkIdNewCaiTrang(item.id)];
                    p.c.get().ID_LEG = ItemTemplate.idNewItemCaiTrang[3][ItemTemplate.checkIdNewCaiTrang(item.id)];
                }
                p.sendInfoMeNewItem();
            } else if (ItemTemplate.checkIdNewYoroi(item.id) != -1) {
                p.c.get().ID_PP = ItemTemplate.idNewItemYoroi[1][ItemTemplate.checkIdNewYoroi(item.id)];
                p.sendInfoMeNewItem();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void useItemChangeMap(Player p, Message m) {
        try {
            byte indexUI = m.reader().readByte();
            byte indexMenu = m.reader().readByte();
            m.cleanup();
            Item item = p.c.ItemBag[indexUI];
            if (item != null && (item.id == 37 || item.id == 35)) {
                if (item.id != 37) {
                    p.c.removeItemBag(indexUI);
                }
                if(p.c.mapid == 111 || p.c.mapid == 133) {
                    p.sendAddchatYellow("Không thể sử dụng chức năng này tại đây");
                    return;
                }
                if (indexMenu == 0 || indexMenu == 1 || indexMenu == 2) {
                    Map ma = Manager.getMapid(Map.arrTruong[indexMenu]);
                    if(ma == null) {
                        return;
                    }
                    for (TileMap area : ma.area) {
                        if (area.numplayers < ma.template.maxplayers) {
                            p.c.tileMap.leave(p);
                            area.EnterMap0(p.c);
                            return;
                        }
                    }
                }
                if (indexMenu == 3 || indexMenu == 4 || indexMenu == 5 || indexMenu == 6 || indexMenu == 7 || indexMenu == 8 || indexMenu == 9) {
                    Map ma = Manager.getMapid(Map.arrLang[indexMenu - 3]);
                    if(ma == null) {
                        return;
                    }
                    for (TileMap area : ma.area) {
                        if (area.numplayers < ma.template.maxplayers) {
                            p.c.tileMap.leave(p);
                            area.EnterMap0(p.c);
                            return;
                        }
                    }
                }
            }
            p.c.get().upDie();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

}
