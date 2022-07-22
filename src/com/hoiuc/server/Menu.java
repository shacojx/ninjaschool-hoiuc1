package com.hoiuc.server;

import com.hoiuc.assembly.*;
import com.hoiuc.io.Message;
import com.hoiuc.io.SQLManager;
import com.hoiuc.io.Util;
import com.hoiuc.stream.*;
import com.hoiuc.stream.thiendiabang.ThienDiaData;
import com.hoiuc.template.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Menu {
    public void sendMessMenuNhiemVu(Player p, byte npcid, byte menuId, String str) throws IOException {
        NpcTemplate npc = (NpcTemplate)Manager.npcs.get(npcid);
        Message mss = new Message(39);
        DataOutputStream ds = mss.writer();
        ds.writeShort(npcid);
        ds.writeUTF(str);
        ds.writeByte(npc.menu[menuId].length);

        for(int i = 1; i < npc.menu[menuId].length; ++i) {
            ds.writeUTF(npc.menu[menuId][i]);
        }

        ds.flush();
        p.conn.sendMessage(mss);
        mss.cleanup();
    }

    public static void doMenuArray(Player p, String[] menu) {
        Message m = null;
        try {
            m = new Message(63);
            for(byte i = 0; i < menu.length; ++i) {
                m.writer().writeUTF(menu[i]);
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

    public static void sendWrite(Player p, short type, String title) {
        Message m = null;
        try {
            m = new Message(92);
            m.writer().writeUTF(title);
            m.writer().writeShort(type);
            m.writer().flush();
            p.conn.sendMessage(m);
            m.cleanup();
        } catch (IOException var5) {
            var5.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public static void menuId(Player p, Message ms) {
        try {
            short npcId = ms.reader().readShort();
            ms.cleanup();
            p.c.typemenu = 0;
            p.typemenu = npcId;
            if (npcId == 33) {
                switch(Server.manager.event) {
                    case 1: {
                        Menu.doMenuArray(p, new String[]{"Diều giấy", "Diều vải"});
                        break;
                    }
                    case 2: {
                        Menu.doMenuArray(p, new String[]{"Hộp bánh thường", "Hộp bánh thượng hạng", "Bánh thập cẩm", "Bánh dẻo", "Bánh đậu xanh", "Bánh pía"});
                        break;
                    }
                    case 3: {
                        Menu.doMenuArray(p, new String[]{"Bánh Chocolate", "Bánh dâu tây", "Đổi mặt nạ", "Đổi pet","BXH Diệt Boss TL", "Hướng dẫn"});
                        break;
                    }
                    case 4: {
                        Menu.doMenuArray(p, new String[]{"Bánh Chưng","Bánh Tét","Lì xì","Làm Pháo","Top diệt chuột", "Hướng dẫn"});
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
            else if(npcId == 40) {
                switch (p.c.mapid) {
                    case 117: {
                        Menu.doMenuArray(p, new String[]{"Rời khỏi nơi này", "Đặt cược", "Hướng dẫn"});
                        break;
                    }
                    case 118:
                    case 119: {
                        Menu.doMenuArray(p, new String[]{"Rời khỏi nơi này", "Thông tin"});
                        break;
                    }
                }
            }

             ms = new Message((byte)40);
            ms.writer().flush();
            p.conn.sendMessage(ms);
            ms.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(ms != null) {
                ms.cleanup();
            }
        }
    }

    public static void menu(Player p, Message ms) {
        try {
            byte npcId = ms.reader().readByte();
            byte menuId = ms.reader().readByte();
            byte b3 = ms.reader().readByte();
            if (ms.reader().available() > 0) {
                byte var6 = ms.reader().readByte();
            }
            ms.cleanup();
            if ((p.typemenu == -1 || p.typemenu == 0) && p.typemenu != npcId) {
                switch(npcId) {
                    case 0:
                        Menu.npcKanata(p, npcId, menuId, b3);
                        break;
                    case 1:
                        Menu.npcFuroya(p, npcId, menuId, b3);
                        break;
                    case 2:
                        Menu.npcAmeji(p, npcId, menuId, b3);
                        break;
                    case 3:
                        Menu.npcKiriko(p, npcId, menuId, b3);
                        break;
                    case 4:
                        Menu.npcTabemono(p, npcId, menuId, b3);
                        break;
                    case 5:
                        Menu.npcKamakura(p, npcId, menuId, b3);
                        break;
                    case 6:
                        Menu.npcKenshinto(p, npcId, menuId, b3);
                        break;
                    case 7:
                        Menu.npcUmayaki_Lang(p, npcId, menuId, b3);
                        break;
                    case 8:
                        Menu.npcUmayaki_Truong(p, npcId, menuId, b3);
                        break;
                    case 9:
                        Menu.npcToyotomi(p, npcId, menuId, b3);
                        break;
                    case 10:
                        Menu.npcOokamesama(p, npcId, menuId, b3);
                        break;
                    case 11:
                        Menu.npcKazeto(p, npcId, menuId, b3);
                        break;
                    case 12:
                        Menu.npcTajima(p, npcId, menuId, b3);
                        break;
                    case 18:
                        Menu.npcRei(p, npcId, menuId, b3);
                        break;
                    case 19:
                        Menu.npcKirin(p, npcId, menuId, b3);
                        break;
                    case 20:
                        Menu.npcSoba(p, npcId, menuId, b3);
                        break;
                    case 21:
                        Menu.npcSunoo(p, npcId, menuId, b3);
                        break;
                    case 22:
                        Menu.npcGuriin(p, npcId, menuId, b3);
                        break;
                    case 23:
                        Menu.npcMatsurugi(p, npcId, menuId, b3);
                        break;
                    case 24:
                        Menu.npcOkanechan(p, npcId, menuId, b3);
                        break;
                    case 25:
                        Menu.npcRikudou(p, npcId, menuId, b3);
                        break;
                    case 26:
                        Menu.npcGoosho(p, npcId, menuId, b3);
                        break;
                    case 27:
                        Menu.npcTruCoQuan(p, npcId, menuId, b3);
                        break;
                    case 28:
                        Menu.npcShinwa(p, npcId, menuId, b3);
                        break;
                    case 29:
                        Menu.npcChiHang(p, npcId, menuId, b3);
                        break;
                    case 30:
                        Menu.npcRakkii(p, npcId, menuId, b3);
                        break;
                    case 31:
                        Menu.npcLongDen(p, npcId, menuId, b3);
                        break;
                    case 32:
                        Menu.npcKagai(p, npcId, menuId, b3);
                        break;
                    case 33:
                        Menu.npcTienNu(p, npcId, menuId, b3);
                        break;
                    case 34:
                        Menu.npcCayThong(p, npcId, menuId, b3);
                        break;
                    case 35:
                        Menu.npcOngGiaNoen(p, npcId, menuId, b3);
                        break;
                    case 36:
                        Menu.npcVuaHung(p, npcId, menuId, b3);
                        break;
                    case 37:
                        Menu.npcKanata_LoiDai(p, npcId, menuId, b3);
                        break;
                    case 38:
                        Menu.npcAdmin(p, npcId, menuId, b3);
                        break;
                    case 39: {
                        Menu.npcRikudou_ChienTruong(p, npcId, menuId, b3);
                        break;
                    }
                    case 43:{ 
                        Menu.npcHoadao(p, npcId, menuId, b3);
                        break;
                    }
                    case 40: {
                        Menu.npcKagai_GTC(p, npcId, menuId, b3);
                        break;
                    }
                    case 92:
                        p.typemenu = menuId == 0 ? 93 : 94;
                        Menu.doMenuArray(p, new String[]{"Thông tin", "Luật chơi"});
                        break;
                    case 93:
                        if (menuId == 0) {
                            Server.manager.rotationluck[0].luckMessage(p);
                        } else if (menuId == 1) {
                            Server.manager.sendTB(p, "Vòng xoay vip", "Tham gia đi, xem luật làm gì");
                        }
                        break;
                    case 94:
                        if (menuId == 0) {
                            Server.manager.rotationluck[1].luckMessage(p);
                        } else if (menuId == 1) {
                            Server.manager.sendTB(p, "Vòng xoay thường", "Tham gia đi xem luật lm gì");
                        }
                    case 95:
                        break;
                    case 120: {
                        if (menuId > 0 && menuId < 7) {
                            Admission.Admission(p,menuId);
                        }
                    }
                    default: {
                        Service.chatNPC(p, (short) npcId, "Chức năng này đang được cập nhật");
                        break;
                    }
                }
            }
            else if (p.typemenu == npcId) {
                switch(p.typemenu) {
                    case 0:
                        Menu.npcKanata(p, npcId, menuId, b3);
                        break;
                    case 1:
                        Menu.npcFuroya(p, npcId, menuId, b3);
                        break;
                    case 2:
                        Menu.npcAmeji(p, npcId, menuId, b3);
                        break;
                    case 3:
                        Menu.npcKiriko(p, npcId, menuId, b3);
                        break;
                    case 4:
                        Menu.npcTabemono(p, npcId, menuId, b3);
                        break;
                    case 5:
                        Menu.npcKamakura(p, npcId, menuId, b3);
                        break;
                    case 6:
                        Menu.npcKenshinto(p, npcId, menuId, b3);
                        break;
                    case 7:
                        Menu.npcUmayaki_Lang(p, npcId, menuId, b3);
                        break;
                    case 8:
                        Menu.npcUmayaki_Truong(p, npcId, menuId, b3);
                        break;
                    case 9:
                        Menu.npcToyotomi(p, npcId, menuId, b3);
                        break;
                    case 10:
                        Menu.npcOokamesama(p, npcId, menuId, b3);
                        break;
                    case 11:
                        Menu.npcKazeto(p, npcId, menuId, b3);
                        break;
                    case 12:
                        Menu.npcTajima(p, npcId, menuId, b3);
                        break;
                    case 18:
                        Menu.npcRei(p, npcId, menuId, b3);
                        break;
                    case 19:
                        Menu.npcKirin(p, npcId, menuId, b3);
                        break;
                    case 20:
                        Menu.npcSoba(p, npcId, menuId, b3);
                        break;
                    case 21:
                        Menu.npcSunoo(p, npcId, menuId, b3);
                        break;
                    case 22:
                        Menu.npcGuriin(p, npcId, menuId, b3);
                        break;
                    case 23:
                        Menu.npcMatsurugi(p, npcId, menuId, b3);
                        break;
                    case 24:
                        Menu.npcOkanechan(p, npcId, menuId, b3);
                        break;
                    case 25:
                        Menu.npcRikudou(p, npcId, menuId, b3);
                        break;
                    case 26:
                        Menu.npcGoosho(p, npcId, menuId, b3);
                        break;
                    case 27:
                        Menu.npcTruCoQuan(p, npcId, menuId, b3);
                        break;
                    case 28:
                        Menu.npcShinwa(p, npcId, menuId, b3);
                        break;
                    case 29:
                        Menu.npcChiHang(p, npcId, menuId, b3);
                        break;
                    case 30:
                        Menu.npcRakkii(p, npcId, menuId, b3);
                        break;
                    case 31:
                        Menu.npcLongDen(p, npcId, menuId, b3);
                        break;
                    case 32:
                        Menu.npcKagai(p, npcId, menuId, b3);
                        break;
                    case 33:
                        Menu.npcTienNu(p, npcId, menuId, b3);
                        break;
                    case 34:
                        Menu.npcCayThong(p, npcId, menuId, b3);
                        break;
                    case 35:
                        Menu.npcOngGiaNoen(p, npcId, menuId, b3);
                        break;
                    case 36:
                        Menu.npcVuaHung(p, npcId, menuId, b3);
                        break;
                    case 37:
                        Menu.npcKanata_LoiDai(p, npcId, menuId, b3);
                        break;
                    case 38:
                        Menu.npcAdmin(p, npcId, menuId, b3);
                        break;
                    case 39: {
                        Menu.npcRikudou_ChienTruong(p, npcId, menuId, b3);
                        break;
                    }
                    case 40: {
                        Menu.npcKagai_GTC(p, npcId, menuId, b3);
                        break;
                    }
                    case 43:{ 
                        Menu.npcHoadao(p, npcId, menuId, b3);
                        break;
                    }
                    case 92:
                        p.typemenu = menuId == 0 ? 93 : 94;
                        doMenuArray(p, new String[]{"Thông tin", "Luật chơi"});
                        break;
                    case 93:
                        if (menuId == 0) {

                            Server.manager.rotationluck[0].luckMessage(p);
                        } else if (menuId == 1) {

                            Server.manager.sendTB(p, "Vòng xoay vip", "Tham gia đi, xem luật làm gì");
                        }
                        break;
                    case 94:
                        if (menuId == 0) {
                            Server.manager.rotationluck[1].luckMessage(p);
                        } else if (menuId == 1) {
                            Server.manager.sendTB(p, "Vòng xoay thường", "Tham gia đi xem luật lm gì");
                        }
                    case 95:
                        break;
                    case 120: {
                        if (menuId > 0 && menuId < 7) {
                            Admission.Admission(p,(byte)menuId);
                        }
                    }
                    default: {
                        Service.chatNPC(p, (short) npcId, "Chức năng này đang được cập nhật");
                        break;
                    }
                }
            }
            else {
                switch(p.typemenu) {
                    case -125: {
                        Menu.doiGiayVun(p, npcId, menuId, b3);
                        break;
                    }
                    case 92: {
                        switch (menuId) {
                            case 0: {
                                Server.manager.rotationluck[0].luckMessage(p);
                                break;
                            }
                            case 1: {
                                Server.manager.rotationluck[1].luckMessage(p);
                                break;
                            }
                        }
                        break;
                    }
                    case 9999: {
                        Menu.menuAdmin(p, npcId, menuId, b3);
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
            p.typemenu = 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(ms != null) {
                ms.cleanup();
            }
        }
    }
    
    public static void npcHoadao(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch (menuId){
            case 0: {
                if (p.c.quantityItemyTotal(646) < 1){
                        Service.chatNPC(p, (short) npcid, "Con không có Bùa May mắn để Xin Lộc nhé");
                        return;
                       }
                else{
                    if(p.c.getBagNull() == 0){
                           p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                           return;
                }   short[] arId = new short[]{12,12,12,8,9,8,9,275,276,277,278,275,276,277,278,548,12,548,381,382,381,382,381,682,682,682,228,227,226,225,224,223,222,283,436,438,437,436,437,419,403,419,403,407,407,12,254,255,256,12,254,255,256,7,8,9,436,437,438,682,384,385,383,382,381,222,223,224,225,226,227,228,251, 308,309,548,275,276,277,278,539,540,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,289,290,291,289,290,291,289,290,291};
                        short idI = arId[Util.nextInt(arId.length)];
                        Item itemup = ItemTemplate.itemDefault(idI);
                       itemup.isLock = false;
                       itemup.isExpires = true;
                       itemup.expires = Util.TimeDay(7);
                        p.c.removeItemBags(646, 1);
                       p.c.addItemBag(false, itemup);
                       p.sendAddchatYellow("Bạn nhận được " + itemup);
                         p.updateExp(10000000L);
                }
                 break;
            }
            
            case 1: {
                Server.manager.sendTB(p,  "Hướng Dẫn", "Bạn cần 1 Bùa May Mắn để Xin Lộc Đầu Xuân và sẽ nhận được EXP và những món quà bất ngờ.");
                break;
            }              
        }       
    }

    public static void menuAdmin(Player p, byte npcid, byte menuId, byte b3) {
        Player player;
        int i;
        switch(menuId) {
            case 0: {
                Service.sendInputDialog(p, (short) 9998, "Nhập số phút muốn bảo trì 0->10 (0: ngay lập tức)");
                break;
            }
            case 1: {
                Service.KhoaTaiKhoan(p);
                break;
            }
            case 2: {
                Service.AutoSaveData();
                p.sendAddchatYellow("Update thành công");
                break;
            }
            case 3: {
                String chat = "MapID: " + p.c.mapid + " - X: " + p.c.get().x + " - Y: " + p.c.get().y;
                p.conn.sendMessageLog(chat);
                break;
            }
            case 4: {
                Service.sendInputDialog(p, (short) 9996, "Nhập số xu muốn cộng (có thể nhập số âm)");
                break;
            }
            case 5: {
                Service.sendInputDialog(p, (short) 9995, "Nhập số lượng muốn cộng (có thể nhập số âm)");
                break;
            }
            case 6: {
                Service.sendInputDialog(p, (short) 9997, "Nhập số yên muốn cộng (có thể nhập số âm)");
                break;
            }
            case 7: {
                Service.sendInputDialog(p, (short) 9994, "Nhập số level muốn tăng (có thể nhập số âm)");
                break;
            }
            case 8: {
                Service.sendInputDialog(p, (short) 9993, "Nhập số tiềm năng muốn tăng (có thể nhập số âm)");
                break;
            }
            case 9: {
                Service.sendInputDialog(p, (short) 9992, "Nhập số kỹ năng muốn tăng (có thể nhập số âm)");
                break;
            }
            case 10: {
                SaveData saveData = new SaveData();
                saveData.player = p;
                Thread t1 = new Thread(saveData);
                t1.start();
                if (!Manager.isSaveData) {
                    player = null;
                    t1 = null;
                    saveData = null;
                }
                break;
            }
            case 11: {
                Service.sendInputDialog(p, (short) 9991, "Nhập nội dung");
                break;
            }
            case 12: {
                try {
                    Server.manager.sendTB(p, "Kiểm tra", "- Tổng số kết nối: " + Client.gI().conns_size() + "\n\n- Số người đăng nhập: " + Client.gI().players_size() + "\n\n- TỔNG SỐ NGƯỜI CHƠI THỰC TẾ: " + Client.gI().ninja_size());
                } catch (Exception var11) {
                    var11.printStackTrace();
                }
                break;
            }
            case 13: {
                synchronized (Client.gI().conns) {
                    for (i = 0; i < Client.gI().conns.size(); ++i) {
                        Session conn = (Session) Client.gI().conns.get(i);
                        if (conn != null) {
                            player = conn.player;
                            if (player != null) {
                                if (player.c == null) {
                                    Client.gI().kickSession(conn);
                                }
                            } else if (player == null) {
                                Client.gI().kickSession(conn);
                            }
                        }
                    }
                }

                p.conn.sendMessageLog("Dọn clone thành công!");
                break;
            }
            case 14: {
                synchronized (Client.gI().conns) {
                    for (i = 0; i < Client.gI().conns.size(); ++i) {
                        player = ((Session) Client.gI().conns.get(i)).player;
                        if (player != null && player != p) {
                            Client.gI().kickSession(player.conn);
                        }
                    }
                }

                p.conn.sendMessageLog("Dọn Session thành công!");
                break;
            }
            case 15: {
                Service.sendInputDialog(p, (short) 9990, "Nhập giá trị cần thay đổi");
                break;
            }
            case 16: {
                try {
                    String a = "";
                    int i2 = 1;
                    for (CheckRHB check: CheckRHB.checkRHBArrayList) {
                        a += i2 + ". " + check.name + " - " + check.item + " - " + check.time +".\n";
                        i2++;
                    }
                    Server.manager.sendTB(p, "Check RHB", a);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case 17: {
               try {
                   ResultSet red = SQLManager.stat.executeQuery("SELECT * FROM `alert` WHERE `id` = 1;");
                   if (red != null && red.first()) {
                       String alert = red.getString("content");
                       Manager.alert.setAlert(alert);
                       red.close();
                   }
                   p.sendAddchatYellow("Cập nhật thông báo thành công");
                   Manager.alert.sendAlert(p);
               } catch (Exception e) {
                   p.conn.sendMessageLog("Lỗi cập nhật!");
               }
               break;
            }
            case 18: {
                try {
                    Manager.chatKTG("Người chơi " + p.c.name + " sử dụng Bánh khúc cây dâu tây đã nhận được " + ItemTemplate.ItemTemplateId(385).name);
                } catch (Exception e) {
                    p.conn.sendMessageLog("Lỗi cập nhật!");
                }
                break;
            }
            case 19: {
                try {
                    Manager.chatKTG("Người chơi " + p.c.name + " sử dụng Bánh khúc cây dâu tây đã nhận được " + ItemTemplate.ItemTemplateId(384).name);
                } catch (Exception e) {
                    p.conn.sendMessageLog("Lỗi cập nhật!");
                }
                break;
                } 
           case 20: {
                Service.sendInputDialog(p, (short) 9989, "Nhập giá trị cần thay đổi");
                break;
                
            }
        }

    }

    public static void doiGiayVun(Player p, byte npcid, byte menuId, byte b3) {
        switch(menuId) {
            case 0: {
                p.c.removeItemBag(p.c.getIndexBagid(251, false), 250);
                p.c.addItemBag(false, ItemTemplate.itemDefault(252, false));
                break;
            }
            case 1: {
                p.c.removeItemBag(p.c.getIndexBagid(251, false), 300);
                p.c.addItemBag(false, ItemTemplate.itemDefault(253, false));
                break;
            }
        }

    }

    public static void npcKanata(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 0: {
                p.requestItem(2);
                break;
            }
            case 1: {
                switch (b3) {
                    case 0: {
                        if (!p.c.clan.clanName.isEmpty()) {
                            Service.chatNPC(p, (short) npcid, "Hiện tại con đã có gia tộc, không thể thành lập gia tộc được nữa.");
                            return;
                        }

                        if (p.luong < 100000) {
                            Service.chatNPC(p, (short) npcid, "Để thành lập gia tộc, con phải có ít nhất 100000 lượng trong người.");
                            return;
                        }
                        Menu.sendWrite(p, (short) 50, "Tên gia tộc");
                        return;
                    }
                    case 1: {
                        if (p.c.clan.clanName.isEmpty()) {
                            Service.chatNPC(p, (short) npcid, "Hiện tại con chưa có gia tộc, không thể mở Lãnh địa gia tộc.");
                            return;
                        }

                        LanhDiaGiaToc lanhDiaGiaToc = null;
                        if (p.c.ldgtID != -1) {
                            if (LanhDiaGiaToc.ldgts.containsKey(p.c.ldgtID)) {
                                lanhDiaGiaToc = LanhDiaGiaToc.ldgts.get(p.c.ldgtID);
                                if (lanhDiaGiaToc != null && lanhDiaGiaToc.map[0] != null && lanhDiaGiaToc.map[0].area[0] != null) {
                                    if(lanhDiaGiaToc.ninjas.size() <= 24) {
                                        p.c.mapKanata = p.c.mapid;
                                        p.c.tileMap.leave(p);
                                        lanhDiaGiaToc.map[0].area[0].EnterMap0(p.c);
                                        return;
                                    } else {
                                        p.sendAddchatYellow("Số thành viên tham gia Lãnh Địa Gia Tộc đã đạt tối đa.");
                                    }
                                }
                            }
                        }
                        if(lanhDiaGiaToc == null){
                            if(p.c.clan.typeclan < 3) {
                                Service.chatNPC(p, (short) npcid, "Con không phải tộc trưởng hoặc tộc phó, không thể mở Lãnh địa gia tộc.");
                                return;
                            }
                            if(p.c.getBagNull() < 1) {
                                Service.chatNPC(p, (short) npcid, "Hành trang của con không đủ chỗ trống để nhận Chìa khoá LDGT");
                                return;
                            }
                            ClanManager clan = ClanManager.getClanName(p.c.clan.clanName);
                            if (clan != null && p.c.clan.typeclan >= 3) {
                                if(clan.openDun <= 0) {
                                    Service.chatNPC(p, (short) npcid, "Số lần vào LDGT tuần này đã hết.");
                                    return;
                                }
                                if(clan.ldgtID != -1) {
                                    Service.chatNPC(p, (short) npcid, "Lãnh địa gia tộc của con đang được mở rồi.");
                                    return;
                                }
                                clan.openDun--;
                                clan.flush();
                                lanhDiaGiaToc = new LanhDiaGiaToc();

                                Item itemup = ItemTemplate.itemDefault(260);
                                itemup.quantity = 1;
                                itemup.expires = System.currentTimeMillis() + 600000L;
                                itemup.isExpires = true;
                                itemup.isLock = true;
                                if(p.c.quantityItemyTotal(260) > 0) {
                                    p.c.removeItemBags(260, p.c.quantityItemyTotal(260));
                                }
                                p.c.addItemBag(false, itemup);
                                p.c.ldgtID = lanhDiaGiaToc.ldgtID;
                                clan.ldgtID = lanhDiaGiaToc.ldgtID;
                                lanhDiaGiaToc.clanManager = clan;
                                p.c.mapKanata = p.c.mapid;
                                p.c.tileMap.leave(p);
                                lanhDiaGiaToc.map[0].area[0].EnterMap0(p.c);
                                return;
                            }

                        }
                        break;
                    }
                    case 2: {
                        if (p.c.isNhanban) {
                            p.conn.sendMessageLog("Chức năng này không dành cho phân thân");
                            return;
                        }
                        if(p.c.quantityItemyTotal(262) < 500) {
                            Service.chatNPC(p, (short) npcid, "Con cần có 500 Đồng tiền gia tộc để đổi lấy Túi quà gia tộc.");
                            return;
                        }
                        if(p.c.getBagNull() < 1) {
                            Service.chatNPC(p, (short) npcid, Language.NOT_ENOUGH_BAG);
                            return;
                        }
                        p.c.removeItemBags(262, 500);
                        Item itemup = ItemTemplate.itemDefault(263);
                        itemup.quantity = 1;
                        itemup.isLock = true;
                        p.c.addItemBag(true, itemup);
                        break;
                    }
                    case 3:
                    default: {
                        Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật");
                        break;
                    }
                }
                break;
            }
            case 2: {
                if (p.c.isNhanban) {
                    p.conn.sendMessageLog("Chức năng này không dành cho phân thân");
                    return;
                }

                if (b3 == 0) {
                    Service.evaluateCave(p.c);
                    return;
                }

                Cave cave = null;
                if (p.c.caveID != -1) {
                    if (Cave.caves.containsKey(p.c.caveID)) {
                        cave = Cave.caves.get(p.c.caveID);
                        if (cave != null && cave.map[0] != null && cave.map[0].area[0] != null) {
                            p.c.mapKanata = p.c.mapid;
                            p.c.tileMap.leave(p);
                            cave.map[0].area[0].EnterMap0(p.c);
                        }
                    }
                } else if (p.c.party != null && p.c.party.cave == null && p.c.party.charID != p.c.id) {
                    p.conn.sendMessageLog("Chỉ có nhóm trưởng mới được phép mở cửa hang động");
                    return;
                }

                if (cave == null) {
                    if (p.c.nCave <= 0) {
                        Service.chatNPC(p, (short) npcid, "Số lần vào hang động của con hôm nay đã hết, hãy quay lại vào Ngay mai.");
                        return;
                    }
                    if (b3 == 1) {
                        if (p.c.level < 30 || p.c.level > 39) {
                            p.conn.sendMessageLog("Trình độ không phù hợp");
                            return;
                        }
                        if (p.c.party != null) {
                            synchronized (p.c.party.aChar) {
                                for (byte i = 0; i < p.c.party.aChar.size(); ++i) {
                                    if (p.c.party.aChar.get(i).level < 30 || p.c.party.aChar.get(i).level > 39) {
                                        p.conn.sendMessageLog("Thành viên trong nhóm có trình độ không phù hợp");
                                        return;
                                    }
                                }
                            }
                        }
                        if (p.c.party != null) {
                            if (p.c.party.cave == null) {
                                cave = new Cave(3);
                                p.c.party.openCave(cave, p.c.name);
                            } else {
                                cave = p.c.party.cave;
                            }
                        } else {
                            cave = new Cave(3);
                        }
                        p.c.caveID = cave.caveID;
                        p.c.isHangDong6x = 1;
                    }
                    if (b3 == 2) {
                        if (p.c.level < 40 || p.c.level > 49) {
                            p.conn.sendMessageLog("Trình độ không phù hợp");
                            return;
                        }
                        if (p.c.party != null) {
                            synchronized (p.c.party) {
                                for (byte i = 0; i < p.c.party.aChar.size(); ++i) {
                                    if (p.c.party.aChar.get(i).level < 40 || p.c.party.aChar.get(i).level > 49) {
                                        p.conn.sendMessageLog("Thành viên trong nhóm có trình độ không phù hợp");
                                        return;
                                    }
                                }
                            }
                        }
                        if (p.c.party != null) {
                            if (p.c.party.cave == null) {
                                cave = new Cave(4);
                                p.c.party.openCave(cave, p.c.name);
                            } else {
                                cave = p.c.party.cave;
                            }
                        } else {
                            cave = new Cave(4);
                        }
                        p.c.caveID = cave.caveID;
                        p.c.isHangDong6x = 0;
                    }
                    if (b3 == 3) {
                        if (p.c.level < 50 || p.c.level > 59) {
                            p.conn.sendMessageLog("Trình độ không phù hợp");
                            return;
                        }
                        if (p.c.party != null) {
                            synchronized (p.c.party.aChar) {
                                for (byte i = 0; i < p.c.party.aChar.size(); ++i) {
                                    if (p.c.party.aChar.get(i).level < 50 || p.c.party.aChar.get(i).level > 59) {
                                        p.conn.sendMessageLog("Thành viên trong nhóm có trình độ không phù hợp");
                                        return;
                                    }
                                }
                            }
                        }
                        if (p.c.party != null) {
                            if (p.c.party.cave == null) {
                                cave = new Cave(5);
                                p.c.party.openCave(cave, p.c.name);
                            } else {
                                cave = p.c.party.cave;
                            }
                        } else {
                            cave = new Cave(5);
                        }
                        p.c.caveID = cave.caveID;
                    }
                    if (b3 == 4) {
                        if (p.c.level < 60 || p.c.level > 69) {
                            p.conn.sendMessageLog("Trình độ không phù hợp");
                            return;
                        }
                        if (p.c.party != null && p.c.party.aChar.size() > 1) {
                            p.conn.sendMessageLog("Hoạt động này chỉ được phép 1 mình.");
                            return;
                        }
                        cave = new Cave(6);
                        p.c.caveID = cave.caveID;
                        p.c.isHangDong6x = 1;
                    }
                    if (b3 == 5) {
                        if (p.c.level < 70 || p.c.level > 89) {
                            p.conn.sendMessageLog("Trình độ không phù hợp");
                            return;
                        }
                        if (p.c.party != null) {
                            synchronized (p.c.party.aChar) {
                                for (byte i = 0; i < p.c.party.aChar.size(); ++i) {
                                    if (p.c.party.aChar.get(i).level < 70 || p.c.party.aChar.get(i).level > 89) {
                                        p.conn.sendMessageLog("Thành viên trong nhóm có trình độ không phù hợp");
                                        return;
                                    }
                                }
                            }
                        }
                        if (p.c.party != null) {
                            if (p.c.party.cave == null) {
                                cave = new Cave(7);
                                p.c.party.openCave(cave, p.c.name);
                            } else {
                                cave = p.c.party.cave;
                            }
                        } else {
                            cave = new Cave(7);
                        }
                        p.c.caveID = cave.caveID;
                        p.c.isHangDong6x = 0;
                    }
                    if (b3 == 6) {
                        if (p.c.level < 90 || p.c.level > 150) {
                            p.conn.sendMessageLog("Trình độ không phù hợp");
                            return;
                        }
                        if (p.c.party != null) {
                            synchronized (p.c.party.aChar) {
                                for (byte i = 0; i < p.c.party.aChar.size(); ++i) {
                                    if (p.c.party.aChar.get(i).level < 90 || p.c.party.aChar.get(i).level > 151) {
                                        p.conn.sendMessageLog("Thành viên trong nhóm có trình độ không phù hợp");
                                        return;
                                    }
                                }
                            }
                        }
                        if (p.c.party != null) {
                            if (p.c.party.cave == null) {
                                cave = new Cave(9);
                                p.c.party.openCave(cave, p.c.name);
                            } else {
                                cave = p.c.party.cave;
                            }
                        } else {
                            cave = new Cave(9);
                        }
                        p.c.caveID = cave.caveID;
                        p.c.isHangDong6x = 0;
                    }

                    if (cave != null) {
                        p.c.nCave--;
                        p.c.pointCave = 0;

                        if (p.c.party != null && p.c.party.charID == p.c.id) {
                            if(p.c.party.aChar != null && p.c.party.aChar.size() > 0) {
                                synchronized (p.c.party.aChar) {
                                    Char _char;
                                    for (int i = 0; i < p.c.party.aChar.size(); i++) {
                                        if(p.c.party.aChar.get(i) != null) {
                                            _char = p.c.party.aChar.get(i);
                                            if (_char.id != p.c.id && p.c.tileMap.getNinja(_char.id) != null && _char.nCave > 0 && _char.caveID == -1 && _char.level >= 30 && (int) _char.level / 10 == cave.x) {
                                                _char.nCave--;
                                                _char.pointCave = 0;
                                                _char.caveID = p.c.caveID;
                                                _char.isHangDong6x = p.c.isHangDong6x;
                                                _char.mapKanata = _char.mapid;
                                                _char.countHangDong++;
                                                if (_char.pointUydanh < 5000) {
                                                    _char.pointUydanh += 5;
                                                }
                                                _char.tileMap.leave(_char.p);
                                                cave.map[0].area[0].EnterMap0(_char);
                                                _char.p.setPointPB(_char.pointCave);
                                            }
                                        }
                                    }
                                }
                            }

                        }
                        p.c.mapKanata = p.c.mapid;
                        p.c.countHangDong++;
                        if (p.c.pointUydanh < 5000) {
                            p.c.pointUydanh += 5;
                        }
                        p.c.tileMap.leave(p);
                        cave.map[0].area[0].EnterMap0(p.c);
                    }
                }
                p.setPointPB(p.c.pointCave);
                break;
            }
            case 3: {
//                Service.chatNPC(p, (short) npcid, "Chức năng đang bảo trì, vui lòng quay lại sau!");
//                return;
                switch (b3) {
                    case 0: {
                        if (p.c.isNhanban) {
                            p.conn.sendMessageLog(Language.NOT_FOR_PHAN_THAN);
                            return;
                        }

                        if (p.c.party != null && p.c.party.charID != p.c.id) {
                            Service.chatNPC(p, (short) npcid, "Con không phải trưởng nhóm, không thể thực hiện gửi lời mời lôi đài cho người/nhóm khác");
                            return;
                        }

                        Service.sendInputDialog(p, (short) 2, "Nhập tên đối thủ của con");
                        return;
                    }
                    case 1: {
                        Service.sendLoiDaiList(p.c);
                        return;
                    }
                    case 2: {
                        String alert = "";

                        for (int i = 0; i < DunListWin.dunList.size(); ++i) {
                            int temp = i + 1;
                            alert = alert + temp + ". Phe " + ((DunListWin) DunListWin.dunList.get(i)).win + " thắng Phe " + ((DunListWin) DunListWin.dunList.get(i)).lose + ".\n";
                        }
                        Server.manager.sendTB(p, "Kết quả", alert);
                        return;
                    }
                    default: {
                        break;
                    }
                }
                break;
            }
            case 4: {
                Service.chatNPC(p, (short) npcid, "Vũ khí của ta cực sắc bén. Nếu muốn tỷ thí thì cứ đến chỗ ta!");
                break;
            }
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật");
                break;
            }
        }
    }

    public static void npcFuroya(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 0:
                switch(b3) {
                    case 0:
                        p.requestItem(21 - p.c.gender);
                        return;
                    case 1:
                        p.requestItem(23 - p.c.gender);
                        return;
                    case 2:
                        p.requestItem(25 - p.c.gender);
                        return;
                    case 3:
                        p.requestItem(27 - p.c.gender);
                        return;
                    case 4:
                        p.requestItem(29 - p.c.gender);
                        return;
                    default:
                        Service.chatNPC(p, (short)npcid, "Chức năng này đang cập nhật!");
                        return;
                }
            case 1:
                Service.chatNPC(p, (short)npcid, "Tan bán quần áo, mũ nón, găng tay và giày siêu bền, siêu rẻ!");
                break;
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }

    }

    public static void npcAmeji(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 0: {
                switch (b3) {
                    case 0: {
                        p.requestItem(16);
                        break;
                    }
                    case 1: {
                        p.requestItem(17);
                        break;
                    }
                    case 2: {
                        p.requestItem(18);
                        break;
                    }
                    case 3: {
                        p.requestItem(19);
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
            }
            case 1: {
                ItemTemplate data;
                switch (b3) {
                    case 0: {
                        if (p.c.isNhanban) {
                            Service.chatNPC(p, (short) npcid, Language.NOT_FOR_PHAN_THAN);
                            return;
                        }

                        if (p.c.level < 50) {
                            Service.chatNPC(p, (short) npcid, "Cấp độ của con không đủ để nhận nhiệm vụ này");
                            return;
                        }

                        if (p.c.countTaskDanhVong < 1) {
                            Service.chatNPC(p, (short) npcid, "Số lần nhận nhiệm vụ của con hôm nay đã hết");
                            return;
                        }

                        if (p.c.isTaskDanhVong == 1) {
                            Service.chatNPC(p, (short) npcid, "Trước đó con đã nhận nhiệm vụ rồi, hãy hoàn thành đã nha");
                            return;
                        }

                        int type = DanhVongTemplate.randomNVDV();
                        p.c.taskDanhVong[0] = type;
                        p.c.taskDanhVong[1] = 0;
                        p.c.taskDanhVong[2] = DanhVongTemplate.targetTask(type);
                        p.c.isTaskDanhVong = 1;
                        p.c.countTaskDanhVong--;
                        if (p.c.isTaskDanhVong == 1) {
                            String nv = "NHIỆM VỤ LẦN NÀY: \n" +
                                    String.format(DanhVongTemplate.nameNV[p.c.taskDanhVong[0]],
                                            p.c.taskDanhVong[1],
                                            p.c.taskDanhVong[2])
                                    + "\n\n- Số lần nhận nhiệm vụ còn lại là: " + p.c.countTaskDanhVong;
                            Server.manager.sendTB(p, "Nhiệm vụ", nv);
                        }
                        break;
                    }
                    case 1: {
                        if (p.c.isNhanban) {
                            Service.chatNPC(p, (short) npcid, Language.NOT_FOR_PHAN_THAN);
                            return;
                        }

                        if (p.c.isTaskDanhVong == 0) {
                            Service.chatNPC(p, (short) npcid, "Con chưa nhận nhiệm vụ nào cả!");
                            return;
                        }

                        if (p.c.taskDanhVong[1] < p.c.taskDanhVong[2]) {
                            Service.chatNPC(p, (short) npcid, "Con chưa hoàn thành nhiệm vụ ta giao!");
                            return;
                        }

                        if (p.c.getBagNull() < 2) {
                            Service.chatNPC(p, (short) npcid, "Hành trang của con không đủ chỗ trống để nhận thưởng");
                            return;
                        }

                        int point = 3;
                        if (p.c.taskDanhVong[0] == 9) {
                            point = 5;
                        }

                        p.c.isTaskDanhVong = 0;
                        p.c.taskDanhVong = new int[]{-1, -1, -1, 0, p.c.countTaskDanhVong};
                        Item item = ItemTemplate.itemDefault(DanhVongTemplate.randomDaDanhVong(), false);
                        item.quantity = 1;
                        item.isLock = false;
                        if (p.c.pointUydanh < 5000) {
                            ++p.c.pointUydanh;
                        }

                        p.c.addItemBag(true, item);
                        int type = Util.nextInt(10);

                        if (p.c.avgPointDanhVong(p.c.getPointDanhVong(type))) {
                            for (int i = 0; i < 10; i++) {
                                type = i;
                                if (!p.c.avgPointDanhVong(p.c.getPointDanhVong(type))) {
                                    break;
                                }
                            }
                        }
                        p.c.plusPointDanhVong(type, point);

                        if(p.c.countTaskDanhVong % 2 == 0) {
                            Item itemUp = ItemTemplate.itemDefault(p.c.gender == 1 ? 739 : 766, true);
                            itemUp.isLock = true;
                            itemUp.isExpires = false;
                            itemUp.expires = -1L;
                            itemUp.quantity = Util.nextInt(1,2);
                            p.c.addItemBag(true, itemUp);
                        } else {
                            Item itemUp = ItemTemplate.itemDefault(p.c.gender == 1 ? 740 : 767, true);
                            itemUp.isLock = true;
                            itemUp.isExpires = false;
                            itemUp.expires = -1L;
                            itemUp.quantity = Util.nextInt(1,2);
                            p.c.addItemBag(true, itemUp);
                        }

                        Service.chatNPC(p, (short) npcid, "Con hãy nhận lấy phần thưởng của mình.");
                        break;
                    }
                    case 2: {
                        if (p.c.isNhanban) {
                            Service.chatNPC(p, (short) npcid, Language.NOT_FOR_PHAN_THAN);
                            return;
                        }

                        if (p.c.isTaskDanhVong == 0) {
                            Service.chatNPC(p, (short) npcid, "Con chưa nhận nhiệm vụ nào cả!");
                            return;
                        }

                        Service.startYesNoDlg(p, (byte) 2, "Con có chắc chắn muốn huỷ nhiệm vụ lần này không?");
                        break;
                    }
                    case 3: {
                        if (p.c.isNhanban) {
                            Service.chatNPC(p, (short) npcid, Language.NOT_FOR_PHAN_THAN);
                            return;
                        }

                        if (p.c.checkPointDanhVong(1)) {
                            if (p.c.getBagNull() < 1) {
                                Service.chatNPC(p, (short) npcid, "Hành trang của con không đủ chỗ trống để nhận thưởng");
                                return;
                            }

                            Item item = ItemTemplate.itemDefault(685, true);
                            item.quantity = 1;
                            item.upgrade = 1;
                            item.isLock = true;
                            Option op = new Option(6, 1000);
                            item.options.add(op);
                            op = new Option(87, 500);
                            item.options.add(op);
                            p.c.addItemBag(false, item);
                        } else {
                            Service.chatNPC(p, (short) npcid, "Con chưa đủ điểm để nhận mắt");
                        }

                        break;
                    }
                    case 4: {
                        if (p.c.isNhanban) {
                            Service.chatNPC(p, (short) npcid, Language.NOT_FOR_PHAN_THAN);
                            return;
                        }

                        if (p.c.ItemBody[14] == null) {
                            Service.chatNPC(p, (short) npcid, "Hãy đeo mắt vào người trước rồi nâng cấp nhé.");
                            return;
                        }

                        if (p.c.ItemBody[14] == null) {
                            return;
                        }

                        if (p.c.ItemBody[14].upgrade >= 10) {
                            Service.chatNPC(p, (short) npcid, "Mắt của con đã đạt cấp tối đa");
                            return;
                        }

                        if (!p.c.checkPointDanhVong(p.c.ItemBody[14].upgrade)) {
                            Service.chatNPC(p, (short) npcid, "Con chưa đủ điểm danh vọng để thực hiện nâng cấp");
                            return;
                        }

                        data = ItemTemplate.ItemTemplateId(p.c.ItemBody[14].id);
                        Service.startYesNoDlg(p, (byte) 0, "Bạn có muốn nâng cấp " + data.name + " với " + GameSrc.coinUpMat[p.c.ItemBody[14].upgrade] + " yên hoặc xu với tỷ lệ thành công là " + GameSrc.percentUpMat[p.c.ItemBody[14].upgrade] + "% không?");
                        break;
                    }
                    case 5: {
                        if (p.c.isNhanban) {
                            Service.chatNPC(p, (short) npcid, Language.NOT_FOR_PHAN_THAN);
                            return;
                        }

                        if (p.c.ItemBody[14] == null) {
                            Service.chatNPC(p, (short) npcid, "Hãy đeo mắt vào người trước rồi nâng cấp nhé.");
                            return;
                        }

                        if (p.c.ItemBody[14].upgrade >= 10) {
                            Service.chatNPC(p, (short) npcid, "Mắt của con đã đạt cấp tối đa");
                            return;
                        }

                        if (!p.c.checkPointDanhVong(p.c.ItemBody[14].upgrade)) {
                            Service.chatNPC(p, (short) npcid, "Con chưa đủ điểm danh vọng để thực hiện nâng cấp");
                            return;
                        }

                        data = ItemTemplate.ItemTemplateId(p.c.ItemBody[14].id);
                        Service.startYesNoDlg(p, (byte) 1, "Bạn có muốn nâng cấp " + data.name + " với " + GameSrc.coinUpMat[p.c.ItemBody[14].upgrade] + " yên hoặc xu và " + GameSrc.goldUpMat[p.c.ItemBody[14].upgrade] + " lượng với tỷ lệ thành công là " + GameSrc.percentUpMat[p.c.ItemBody[14].upgrade] * 2 + "% không?");
                        break;
                    }
                    case 6: {
                        if (p.c.isNhanban) {
                            Service.chatNPC(p, (short) npcid, Language.NOT_FOR_PHAN_THAN);
                            return;
                        }

                        String nv = "- Hoàn thành nhiệm vụ. Hãy gặp Ameji để trả nhiệm vụ.\n- Hôm nay có thể nhận thêm " + p.c.countTaskDanhVong + " nhiệm vụ trong Ngay.\n- Hôm nay có thể sử dụng thêm " + p.c.useDanhVongPhu + " Danh Vọng Phù để nhận thêm 5 lần làm nhiệm vụ.\n- Hoàn thành nhiệm vụ sẽ nhận ngẫu nhiên 1 viên đá danh vọng cấp 1-10.\n- Khi đủ mốc 100 điểm mỗi loại có thể nhận mắt và nâng cấp mắt.";
                        if (p.c.isTaskDanhVong == 1) {
                            nv = "NHIỆM VỤ LẦN NÀY: \n" + String.format(DanhVongTemplate.nameNV[p.c.taskDanhVong[0]], p.c.taskDanhVong[1], p.c.taskDanhVong[2]) + "\n\n" + nv;
                        }

                        Server.manager.sendTB(p, "Nhiệm vụ", nv);
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
            }
            case 2: {
                Service.chatNPC(p, (short) npcid, "Tan bán các loại trang sức lấp lánh!");
                break;
            }
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }

    }

    public static void npcKiriko(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 0: {
                if (p.c.isNhanban) {
                    Service.chatNPC(p, (short) npcid, Language.NOT_FOR_PHAN_THAN);
                    return;
                }

                p.requestItem(7);
                break;
            }
            case 1: {
                if (p.c.isNhanban) {
                    Service.chatNPC(p, (short) npcid, Language.NOT_FOR_PHAN_THAN);
                    return;
                }

                p.requestItem(6);
                break;
            }
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }

    }

    public static void npcTabemono(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 0:
                if (p.c.isNhanban) {
                    Service.chatNPC(p, (short)npcid, Language.NOT_FOR_PHAN_THAN);
                    return;
                }
                p.requestItem(9);
                break;       
            case 1: {
                Service.chatNPC(p, (short) npcid, "3 đời nhà ta bán thức ăn chưa ai bị đau bụng cả!");
                break;
            }
            case 2: {
                if (p.c.isNhanban) {
                    Service.chatNPC(p, (short) npcid, Language.NOT_FOR_PHAN_THAN);
                    return;
                }
                switch (b3) {
                    case 0: {
                        if (!ThienDiaBangManager.register) {
                            Service.chatNPC(p, (short) npcid, "Đang trong thời gian tổng kết. Hiện tại không thể đăng ký.");
                            return;
                        }
                        if (ThienDiaBangManager.diaBangList.containsKey(p.c.name) || ThienDiaBangManager.thienBangList.containsKey(p.c.name)) {
                            Service.chatNPC(p, (short) npcid, "Con đã đăng ký trước đó rồi");
                            return;
                        }
                        if (p.c.get().level >= 50 && p.c.get().level < 70) {
                            ThienDiaBangManager.diaBangList.put(p.c.name, new ThienDiaData(p.c.name, ThienDiaBangManager.rankDiaBang++, 1));
                            Service.chatNPC(p, (short) npcid, "Con đã đăng ký tham gia trang tài Địa bảng thành công.");
                        } else if (p.c.get().level >= 70) {
                            ThienDiaBangManager.thienBangList.put(p.c.name, new ThienDiaData(p.c.name, ThienDiaBangManager.rankThienBang++, 1));
                            Service.chatNPC(p, (short) npcid, "Con đã đăng ký tham gia tranh tài Thiên bảng thành công.");
                        } else {
                            Service.chatNPC(p, (short) npcid, "Trình độ của con không phù hợp để đăng ký tham gia tranh tài.");
                        }
                        break;
                    }
                    case 1: {
                        if (!ThienDiaBangManager.register) {
                            Service.chatNPC(p, (short) npcid, "Đang trong thời gian tổng kết. Hiện tại không thể thi đấu.");
                            return;
                        }
                        ArrayList<ThienDiaData> list = new ArrayList<>();
                        if (ThienDiaBangManager.diaBangList.containsKey(p.c.name)) {
                            ThienDiaData rank = ThienDiaBangManager.diaBangList.get(p.c.name);
                            for (ThienDiaData data : ThienDiaBangManager.getListDiaBang()) {
                                if (data != null) {
                                    if (rank.getRank() < 10 && (data.getRank() - rank.getRank()) < 20) {
                                        list.add(data);
                                    } else if (data.getRank() < rank.getRank() & (rank.getRank() - data.getRank()) < 10) {
                                        list.add(data);
                                    }
                                }
                            }
                        } else if (ThienDiaBangManager.thienBangList.containsKey(p.c.name)) {
                            ThienDiaData rank = ThienDiaBangManager.thienBangList.get(p.c.name);
                            for (ThienDiaData data : ThienDiaBangManager.getListThienBang()) {
                                if (data != null) {
                                    if (rank.getRank() < 10 && (data.getRank() - rank.getRank()) < 20) {
                                        list.add(data);
                                    } else if (data.getRank() <= rank.getRank() & (rank.getRank() - data.getRank()) < 10) {
                                        list.add(data);
                                    }
                                }
                            }
                        } else {
                            Service.chatNPC(p, (short) npcid, "Con chưa đăng ký tham gia thi đấu.");
                            return;
                        }
                        Service.SendChinhPhuc(p, list);
                        return;
                    }
                    case 2: {
                        String res = "";
                        int count = 1;
                        for (ThienDiaData data : ThienDiaBangManager.getListSortAsc(new ArrayList<ThienDiaData>(ThienDiaBangManager.thienBangList.values()))) {
                            if (count < 11) {
                                res += "Hạng " + count + ": " + data.getName() + ".\n";
                                count++;
                            }
                        }
                        Server.manager.sendTB(p, "Thiên bảng", res);
                        return;
                    }
                    case 3: {
                        String res = "";
                        int count = 1;
                        for (ThienDiaData data : ThienDiaBangManager.getListSortAsc(new ArrayList<ThienDiaData>(ThienDiaBangManager.diaBangList.values()))) {
                            if (count < 11) {
                                res += "Hạng " + count + ": " + data.getName() + ".\n";
                                count++;
                            }
                        }
                        Server.manager.sendTB(p, "Địa bảng", res);
                        return;
                    }
                    case 4: {
                        ResultSet res = null;
                        try {
                            if(Manager.nhanquatdb == 0){
                           Service.chatNPC(p, (short) npcid, "Chỉ nhận quà được vào thứ 2.");
                                return;
                            }
                            if(p.c.rankTDB > 0) {
                                if(p.c.isGiftTDB == 1) {
                                    if(p.c.rankTDB > 20) {
                                        p.upluongMessage(500);
                                        p.c.upxuMessage(500000);
                                    } else {
                                        switch (p.c.rankTDB) {
                                            case 1: {
                                                if(p.c.getBagNull() < 10) {
                                                    Service.chatNPC(p, (short) npcid, "Con cần ít nhất 10 chỗ trống trong hành trang để nhận thưởng.");
                                                    return;
                                                }
                                                Item pl = ItemTemplate.itemDefault(308,false);
                                                pl.quantity = 2;
                                                p.c.addItemBag(true,pl);

                                                pl = ItemTemplate.itemDefault(309,false);
                                                pl.quantity = 2;
                                                p.c.addItemBag(true,pl);

                                                p.c.addItemBag(false,ItemTemplate.itemDefault(540,false));
                                                p.c.addItemBag(false,ItemTemplate.itemDefault(540,false));
                                                p.c.addItemBag(false,ItemTemplate.itemDefault(539,false));
                                                p.c.addItemBag(false,ItemTemplate.itemDefault(539,false));
                                                p.c.addItemBag(false,ItemTemplate.itemDefault(539,false));

                                                p.c.addItemBag(false,ItemTemplate.itemDefault(384,false));
                                                p.c.addItemBag(false,ItemTemplate.itemDefault(383,false));
                                                p.c.addItemBag(false,ItemTemplate.itemDefault(383,false));

                                                p.upluongMessage(20000);
                                                p.c.upxuMessage(20000000);
                                                break;
                                            }
                                            case 2: {
                                                if(p.c.getBagNull() < 7) {
                                                    Service.chatNPC(p, (short) npcid, "Con cần ít nhất 7 chỗ trống trong hành trang để nhận thưởng.");
                                                    return;
                                                }
                                                Item pl = ItemTemplate.itemDefault(308,false);
                                                pl.quantity = 1;
                                                p.c.addItemBag(true,pl);

                                                pl = ItemTemplate.itemDefault(309,false);
                                                pl.quantity = 1;
                                                p.c.addItemBag(true,pl);

                                                p.c.addItemBag(false,ItemTemplate.itemDefault(540,false));
                                                p.c.addItemBag(false,ItemTemplate.itemDefault(539,false));
                                                p.c.addItemBag(false,ItemTemplate.itemDefault(539,false));

                                                p.c.addItemBag(false,ItemTemplate.itemDefault(384,false));
                                                p.c.addItemBag(false,ItemTemplate.itemDefault(383,false));

                                                p.upluongMessage(10000);
                                                p.c.upxuMessage(10000000);
                                                break;
                                            }
                                            case 3: {
                                                if(p.c.getBagNull() < 4) {
                                                    Service.chatNPC(p, (short) npcid, "Con cần ít nhất 4 chỗ trống trong hành trang để nhận thưởng.");
                                                    return;
                                                }
                                                p.c.addItemBag(false,ItemTemplate.itemDefault(540,false));
                                                p.c.addItemBag(false,ItemTemplate.itemDefault(539,false));
                                                p.c.addItemBag(false,ItemTemplate.itemDefault(383,false));
                                                p.c.addItemBag(false,ItemTemplate.itemDefault(383,false));
                                                p.upluongMessage(5000);
                                                p.c.upxuMessage(5000000);
                                                break;
                                            }
                                            case 4:
                                            case 5:
                                            case 6:
                                            case 7:
                                            case 8:
                                            case 9:
                                            case 10: {
                                                if(p.c.getBagNull() < 4) {
                                                    Service.chatNPC(p, (short) npcid, "Con cần ít nhất 2 chỗ trống trong hành trang để nhận thưởng.");
                                                    return;
                                                }
                                                p.c.addItemBag(false,ItemTemplate.itemDefault(539,false));
                                                p.c.addItemBag(false,ItemTemplate.itemDefault(383,false));
                                                p.upluongMessage(3000);
                                                p.c.upxuMessage(3000000);
                                                break;
                                            }
                                            case 11:
                                            case 12:
                                            case 13:
                                            case 14:
                                            case 15:
                                            case 16:
                                            case 17:
                                            case 18:
                                            case 19:
                                            case 20: {
                                                p.upluongMessage(1000);
                                                p.c.upxuMessage(1000000);
                                                break;
                                            }

                                        }
                                    }
                                    p.c.isGiftTDB = 0;
                                } else {
                                    Service.chatNPC(p, (short) npcid, "Con đã nhận thưởng tuần rồi.");
                                    return;
                                }
                            } else {
                                Service.chatNPC(p, (short) npcid, "Tuần trước con chưa tham gia Thiên Địa bảng và chưa có rank, con chưa thể nhận thường.");
                                return;
                            }
                        } catch (Exception e) {
                            p.conn.sendMessageLog("Lỗi nhận thưởng, vui lòng thử lại sau!");
                            return;
                        } finally {
                            if(res != null) {
                                try {
                                    res.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    }
                    case 5: {
                        Server.manager.sendTB(p, "Hướng dẫn", "- Thiên Địa Bảng sẽ được mở hàng tuần. Bắt đầu từ thứ 2 và tổng kết vào chủ nhật.\n" +
                                "- Thiên Địa Bảng sẽ được mở đăng ký và chính phục từ 00h05' đến 23h45' hàng Ngay. Mỗi Ngay sẽ có 20p để tổng kết Ngay, trong thời gian này sẽ không thể đăng ký và chinh phục\n" +
                                "- Trong thời gian tổng kết nếu chiến thắng trong Chinh phục sẽ không được tính rank." +
                                "- Vào Ngay thường sẽ không giới hạn lượt thách đấu.\n" +
                                "- Vào Thứ 7 và Chủ Nhật mỗi Ninja sẽ có 5 lượt thách đấu, Thắng sẽ không bị mất lượt, thua sẽ bị trừ 1 lần thách đấu." +
                                "- Địa Bảng dành cho ninja từ cấp độ 50-69.\n" +
                                "- Thiên Bảng dành cho ninja từ cấp độ trên 70\n" +
                                "- Sau khi đăng ký thành công, hãy Chinh Phục ngay để giành lấy vị trí top đầu.\n" +
                                "- Mỗi lần chiến thắng, nếu vị trí của đối thủ trước bạn, bạn sẽ đổi vị trí của mình cho đối thủ, còn không vị trí của bạn sẽ được giữ nguyên.\n" +
                                "- Phần thưởng sẽ được trả thưởng vào mỗi tuần mới (Lưu ý: Hãy nhận thưởng ngay trong tuần mới đó, nếu sang tuần sau phần thưởng sẽ bị reset).\n\n" +
                                "- PHẦN THƯỞNG: \n" +
                                "Top 1: Hào quang Rank 1 + 2 Bánh Phong Lôi, 2 Bánh Băng Hoả, 2 Nấm x4, 3 Nấm x3, 1 Rương bạch ngân, 2 Bát bảo, 20,000 Lượng, 20,000,000 xu.\n\n" +
                                "Top 2: Hào quang Rank 2 + 1 Bánh Phong Lôi, 1 Bánh Băng Hoả, 1 Nấm x4, 2 Nấm x3, 1 Rương bạch ngân, 1 Bát bảo, 10,000 Lượng, 10,000,000 xu.\n\n" +
                                "Top 3: Hào quang Rank 3 + 1 Nấm x4, 1 Nấm x3, 2 Bát bảo, 5,000 Lượng, 5,000,000 xu.\n\n" +
                                "Top 4-10: 1 Nấm x3, 1 Bát bảo, 3,000 Lượng, 3,000,000 xu.\n\n" +
                                "Top 11-20: 1,000 Lượng, 1,000,000 xu.\n\n" +
                                "Còn lại: 500 Lượng, 500,000 xu.");
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
            }
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }

    }

    public static void npcKamakura(Player p, byte npcid, byte menuId, byte b3) {
        try {
            if (p.c.isNhanban) {
                p.conn.sendMessageLog("Chức năng này không dành cho phân thân.");
                return;
            }
            switch(menuId) {
                case 0:
                    //p.requestItem(4);
                    switch (b3) {
                        case 0: {
                            Service.openMenuBox(p);
                            break;
                        }
                        case 1: {
                            Service.openMenuBST(p);
                            break;
                        }
                        case 2: {
                            Service.openMenuCaiTrang(p);
                            break;
                        }
                        case 3: {
                            //Tháo cải trang
                            p.c.caiTrang = -1;
                            Message m = new Message(11);
                            m.writer().writeByte(-1);
                            m.writer().writeByte(p.c.get().speed());
                            m.writer().writeInt(p.c.get().getMaxHP());
                            m.writer().writeInt(p.c.get().getMaxMP());
                            m.writer().writeShort(p.c.get().eff5buffHP());
                            m.writer().writeShort(p.c.get().eff5buffMP());
                            m.writer().flush();
                            p.conn.sendMessage(m);
                            m.cleanup();
                            Service.CharViewInfo(p, false);
                            p.endLoad(true);
                            break;
                        }
                    }
                    break;
                case 1:
                    if(p.c.tileMap.map.getXHD() != -1 || p.c.tileMap.map.LangCo() || p.c.tileMap.map.mapBossTuanLoc() || p.c.tileMap.map.mapLDGT() || p.c.tileMap.map.mapGTC() || p.c.tileMap.map.id == 111 || p.c.tileMap.map.id == 113) {
                        p.c.mapLTD = 22;
                    } else {
                        p.c.mapLTD = p.c.tileMap.map.id;
                    }
                    Service.chatNPC(p, (short)npcid, "Lưu toạ độ thành công! Khi chết con sẽ được vác xác về đây.");
                    break;
                case 2:
                    switch(b3) {
                        case 0:
                            if (p.c.level < 60) {
                                p.conn.sendMessageLog("Chức năng này yên cầu trình độ 60");
                                return;
                            }

                            Map ma = Manager.getMapid(139);
                            TileMap area;
                            int var8;
                            for(var8 = 0; var8 < ma.area.length; ++var8) {
                                area = ma.area[var8];
                                if (area.numplayers < ma.template.maxplayers) {
                                    p.c.tileMap.leave(p);
                                    area.EnterMap0(p.c);
                                    return;
                                }
                            }
                            return;
                        case 1:
                            Server.manager.sendTB(p, "Hướng dẫn", "- Hướng dẫn vùng đất ma quỷ");
                            return;
                        default:
                            return;
                    }
                case 3:
                    Service.chatNPC(p, (short)npcid, "Ta giữ đồ chưa bao giờ bị mất thứ gì cả!");
                    break;
                default: {
                    Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void npcKenshinto(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        if(p.c.isNhanban) {
            Service.chatNPC(p, (short) npcid, Language.NOT_FOR_PHAN_THAN);
            return;
        }
        switch(menuId) {
            case 0: {
                if (b3 == 0) {
                    p.requestItem(10);
                } else if (b3 == 1) {
                    p.requestItem(31);
                } else if (b3 == 2) {
                    Server.manager.sendTB(p, "Hướng dẫn", "- Hướng dẫn nâng cấp trang bị");
                }
                break;
            }
            case 1: {
                if (b3 == 0) {
                    p.requestItem(12);
                } else if (b3 == 1) {
                    p.requestItem(11);
                }
                break;
            }
            case 2: {
                p.requestItem(13);
                break;
            }
            case 3: {
                p.requestItem(33);
                break;
            }
            case 4: {
                p.requestItem(46);
                break;
            }
            case 5: {
                p.requestItem(47);
                break;
            }
            case 6: {
                p.requestItem(49);
                break;
            }
            case 7: {
                p.requestItem(50);
                break;
            }
            case 8: {
                Service.chatNPC(p, (short) npcid, "Cần nâng cấp trang bị, hãy đến quán của ta!");
                break;
            }
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }

    }

    public static void npcUmayaki_Lang(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 0:
                Service.chatNPC(p, (short)npcid, "Ta kéo xe qua các làng với tốc độ ánh sáng!");
                return;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                TileMap[] var5 = Manager.getMapid(Map.arrLang[menuId - 1]).area;
                int var6 = var5.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    TileMap area = var5[var7];
                    if (area.numplayers < Manager.getMapid(Map.arrLang[menuId - 1]).template.maxplayers) {
                        p.c.tileMap.leave(p);
                        area.EnterMap0(p.c);
                        return;
                    }
                }
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }
    }

    public static void npcUmayaki_Truong(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 0:
            case 1:
            case 2:
                TileMap[] var5 = Manager.getMapid(Map.arrTruong[menuId]).area;
                int var6 = var5.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    TileMap area = var5[var7];
                    if (area.numplayers < Manager.getMapid(Map.arrTruong[menuId]).template.maxplayers) {
                        p.c.tileMap.leave(p);
                        area.EnterMap0(p.c);
                        return;
                    }
                }

                return;
            case 3:
                Service.chatNPC(p, (short)npcid, "Ta kéo xe qua các trường, không qua quán net đâu!");
                return;
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }
    }

    public static void npcToyotomi(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 0:
                switch(b3) {
                    case 0:
                        Server.manager.sendTB(p, "Top đại gia yên", Rank.getStringBXH(0));
                        return;
                    case 1:
                        Server.manager.sendTB(p, "Top cao thủ", Rank.getStringBXH(1));
                        return;
                    case 2:
                        Server.manager.sendTB(p, "Top gia tộc", Rank.getStringBXH(2));
                        return;
                    case 3:
                        Server.manager.sendTB(p, "Top hang động", Rank.getStringBXH(3));
                        return;
                    default:
                        return;
                }
            case 1:
                if (p.c.get().nclass > 0) {
                    Service.chatNPC(p, (short)npcid, "Con đã vào lớp từ trước rồi mà.");
                } else if (p.c.get().ItemBody[1] != null) {
                    Service.chatNPC(p, (short)npcid, "Con cần có 1 tâm hồn trong trắng mới có thể nhập học, hãy tháo vũ khí trên người ra!");
                } else if (p.c.getBagNull() < 2) {
                    Service.chatNPC(p, (short)npcid, "Hành trang cần phải có ít nhất 2 ô trống mới có thể nhập học!");
                } else {
                    if (b3 == 0) {
                        Admission.Admission(p,(byte)1);
                    } else {
                        if (b3 != 1) {
                            Service.chatNPC(p, (short)npcid, "Chức năng này đang cập nhật!");
                            break;
                        }
                        Admission.Admission(p,(byte)2);
                    }

                    Service.chatNPC(p, (short)npcid, "Hãy chăm chỉ luyện tập, có làm thì mới có ăn con nhé.");
                }
                break;
            case 2:
                if (p.c.get().nclass != 1 && p.c.get().nclass != 2) {
                    Service.chatNPC(p, (short)npcid, "Con không phải học sinh của trường này, ta không thể giúp con tẩy điểm dược rồi.");
                } else if (b3 == 0) {
                    if (p.c.get().countTayTiemNang < 1) {
                        Service.chatNPC(p, (short)npcid, "Số lần tẩy điểm kỹ năng của con đã hết.");
                        return;
                    }

                    p.restPpoint();
                    --p.c.get().countTayTiemNang;
                    Service.chatNPC(p, (short)npcid, "Ta đã giúp con tẩy điểm tiềm năng, hãy nâng điểm thật hợp lý nha.");
                    p.sendAddchatYellow("Tẩy điểm tiềm năng thành công");
                } else if (b3 == 1) {
                    if (p.c.get().countTayKyNang < 1) {
                        Service.chatNPC(p, (short)npcid, "Số lần tẩy điểm kỹ năng của con đã hết.");
                        return;
                    }
                    p.restSpoint();
                    --p.c.get().countTayKyNang;
                    Service.chatNPC(p, (short)npcid, "Ta đã giúp con tẩy điểm kỹ năng, hãy nâng điểm thật hợp lý nha.");
                    p.sendAddchatYellow("Tẩy điểm kỹ năng thành công");
                }
                break;
            case 3:
                Service.chatNPC(p, (short)npcid, "Trường ta là 1 ngôi trường danh giá, chỉ giành cho nhưng ninja tính nóng như kem mà thôi.");
                break;
            case 4:
                Service.chatNPC(p, (short)npcid, "Ta đang hơi mệt xíu, ta sẽ giao chiến với con sau nha! Bye bye...");
                break;
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }

    }

    public static void npcOokamesama(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 0:

                switch(b3) {
                    case 0:

                        Server.manager.sendTB(p, "Top đại gia yên", Rank.getStringBXH(0));
                        return;
                    case 1:

                        Server.manager.sendTB(p, "Top cao thủ", Rank.getStringBXH(1));
                        return;
                    case 2:

                        Server.manager.sendTB(p, "Top gia tộc", Rank.getStringBXH(2));
                        return;
                    case 3:

                        Server.manager.sendTB(p, "Top hang động", Rank.getStringBXH(3));
                        return;
                    default:
                        return;
                }
            case 1:
                if (p.c.get().nclass > 0) {
                    Service.chatNPC(p, (short)npcid, "Con đã vào lớp từ trước rồi mà.");
                } else if (p.c.get().ItemBody[1] != null) {
                    Service.chatNPC(p, (short)npcid, "Con cần có 1 tâm hồn trong trắng mới có thể nhập học, hãy tháo vũ khí trên người ra!");
                } else if (p.c.getBagNull() < 2) {
                    Service.chatNPC(p, (short)npcid, "Hành trang cần phải có ít nhất 2 ô trống mới có thể nhập học!");
                } else {
                    if (b3 == 0) {
                        Admission.Admission(p,(byte)3);
                    } else {
                        if (b3 != 1) {
                            Service.chatNPC(p, (short)npcid, "Chức năng này đang cập nhật!");
                            break;
                        }

                        Admission.Admission(p,(byte)4);
                    }

                    Service.chatNPC(p, (short)npcid, "Hãy chăm chỉ luyện tập, có làm thì mới có ăn con nhé.");
                }
                break;
            case 2:
                if (p.c.get().nclass != 3 && p.c.get().nclass != 4) {
                    Service.chatNPC(p, (short)npcid, "Con không phải học sinh của trường này, ta không thể giúp con tẩy điểm dược rồi.");
                } else if (b3 == 0) {
                    if (p.c.get().countTayTiemNang < 1) {
                        Service.chatNPC(p, (short)npcid, "Số lần tẩy điểm kỹ năng của con đã hết.");
                        return;
                    }
                    p.restPpoint();
                    --p.c.get().countTayTiemNang;
                    Service.chatNPC(p, (short)npcid, "Ta đã giúp con tẩy điểm tiềm năng, hãy nâng điểm thật hợp lý nha.");
                    p.sendAddchatYellow("Tẩy điểm tiềm năng thành công");
                } else if (b3 == 1) {
                    if (p.c.get().countTayKyNang < 1) {
                        Service.chatNPC(p, (short)npcid, "Số lần tẩy điểm kỹ năng của con đã hết.");
                        return;
                    }

                    p.restSpoint();
                    --p.c.get().countTayKyNang;
                    Service.chatNPC(p, (short)npcid, "Ta đã giúp con tẩy điểm kỹ năng, hãy nâng điểm thật hợp lý nha.");
                    p.sendAddchatYellow("Tẩy điểm kỹ năng thành công");
                }
                break;
            case 3:
                Service.chatNPC(p, (short)npcid, "Sao hôm nay trời nóng thế nhỉ, hình như biến đổi khí hậu làm tan hết băng trường ta rồi!");
                break;
            case 4:
                Service.chatNPC(p, (short)npcid, "Ta đang hơi mệt xíu, ta sẽ giao chiến với con sau nha! Bye bye...");
                break;
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }

    }

    public static void npcKazeto(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 0:
                switch(b3) {
                    case 0:

                        Server.manager.sendTB(p, "Top đại gia yên", Rank.getStringBXH(0));
                        return;
                    case 1:

                        Server.manager.sendTB(p, "Top cao thủ", Rank.getStringBXH(1));
                        return;
                    case 2:

                        Server.manager.sendTB(p, "Top gia tộc", Rank.getStringBXH(2));
                        return;
                    case 3:

                        Server.manager.sendTB(p, "Top hang động", Rank.getStringBXH(3));
                        return;
                    default:
                        return;
                }
            case 1:
                if (p.c.get().nclass > 0) {
                    Service.chatNPC(p, (short)npcid, "Con đã vào lớp từ trước rồi mà.");
                } else if (p.c.get().ItemBody[1] != null) {
                    Service.chatNPC(p, (short)npcid, "Con cần có 1 tâm hồn trong trắng mới có thể nhập học, hãy tháo vũ khí trên người ra!");
                } else if (p.c.getBagNull() < 2) {
                    Service.chatNPC(p, (short)npcid, "Hành trang cần phải có ít nhất 2 ô trống mới có thể nhập học!");
                } else {
                    if (b3 == 0) {
                        Admission.Admission(p,(byte)5);
                    } else if (b3 == 1) {
                        Admission.Admission(p,(byte)6);
                    }

                    Service.chatNPC(p, (short)npcid, "Hãy chăm chỉ luyện tập, có làm thì mới có ăn con nhé.");
                }
                break;
            case 2:
                if (p.c.get().nclass != 5 && p.c.get().nclass != 6) {
                    Service.chatNPC(p, (short)npcid, "Con không phải học sinh của trường này, ta không thể giúp con tẩy điểm dược rồi.");
                } else if (b3 == 0) {
                    if (p.c.get().countTayTiemNang < 1) {
                        Service.chatNPC(p, (short)npcid, "Số lần tẩy điểm tiềm năng của con đã hết.");
                        return;
                    }
                    p.restPpoint();
                    --p.c.get().countTayTiemNang;
                    Service.chatNPC(p, (short)npcid, "Ta đã giúp con tẩy điểm tiềm năng, hãy nâng điểm thật hợp lý nha.");
                    p.sendAddchatYellow("Tẩy điểm tiềm năng thành công");
                } else if (b3 == 1) {
                    if (p.c.get().countTayKyNang < 1) {
                        Service.chatNPC(p, (short)npcid, "Số lần tẩy điểm kỹ năng của con đã hết.");
                        return;
                    }
                    p.restSpoint();
                    --p.c.get().countTayKyNang;
                    Service.chatNPC(p, (short)npcid, "Ta đã giúp con tẩy điểm kỹ năng, hãy nâng điểm thật hợp lý nha.");
                    p.sendAddchatYellow("Tẩy điểm kỹ năng thành công");
                }
                break;
            case 3:
                Service.chatNPC(p, (short)npcid, "Ngươi là người thổi tan băng của trường Ookaza và mang kem về cho trường Hirosaki đúng không?");
                break;
            case 4:
                Service.chatNPC(p, (short)npcid, "Ta đang hơi mệt xíu, ta sẽ giao chiến với con sau nha! Bye bye...");
                break;
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }

    }

    public static void npcTajima(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 0:
                Service.chatNPC(p, (short)npcid, "Chào mừng con đến với ngôi làng đi đâu cũng phải nhớ về!");
                break;
            case 1:
                Service.chatNPC(p, (short)npcid, "Chức năng Huỷ vật phẩm và nhiệm vụ đang cập nhật!");
                break;
            case 2:
                if (p.c.timeRemoveClone > System.currentTimeMillis()) {
                    p.toNhanBan();
                } else {
                    Service.chatNPC(p, (short)npcid, "Con không có phân thân để sử dụng chức năng này!");
                }
                break;
            case 3:
                if (!p.c.isNhanban) {
                    Service.chatNPC(p, (short)npcid, "Con không phải phân thân để sử dụng chức năng này!");
                    return;
                }
                if (!p.c.clone.isDie && p.c.timeRemoveClone > System.currentTimeMillis()) {
                    p.exitNhanBan(true);
                }
                break;
            case 4:
            case 5:
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }

    }

    public static void npcRei(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 0:
                Service.chatNPC(p, (short)npcid, "Ngươi đến đây làm gì, không có nhiệm vụ cho ngươi đâu!");
                break;
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }

    }

    public static void npcKirin(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 0:
                Service.chatNPC(p, (short)npcid, "Ngươi đến đây làm gì, không có nhiệm vụ cho ngươi đâu!");
                break;
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }

    }

    public static void npcSoba(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 0:
                Service.chatNPC(p, (short)npcid, "Ta sẽ sớm có nhiệm vụ cho con!");
                break;
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }

    }

    public static void npcSunoo(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 0:
                Service.chatNPC(p, (short)npcid, "Khụ khụ...");
                break;
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }

    }

    public static void npcGuriin(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }
    }

    public static void npcMatsurugi(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }
    }

    public static void npcOkanechan(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 0:
                Server.manager.sendTB(p, "Hướng dẫn", "- Để nạp tiền hoặc mua đồ, con hãy lên Website hoặc THAM GIA BOX ZALO của game để nạp nhé!");
                break;
            case 2:
                        if (p.c.isNhanban) {
                            Service.chatNPC(p, (short)npcid, Language.NOT_FOR_PHAN_THAN);
                            return;
                        }

                        if (p.c.pointUydanh < 200) {
                            Service.chatNPC(p, (short)npcid, "Không Đủ Điểm Hoạt Động, Điểm hoạt động của con phải trên 200");
                            return;
                        }
                        if (p.luong < 1000) {
                            Service.chatNPC(p, (short)npcid, "Không Đủ 1000 Lượng");
                            return;
                        } else {
                                p.upluongMessage(-1000L);
                                p.c.luongTN -= 1000;
                                p.c.pointUydanh -= 200;
                                p.c.upxuMessage(100000000L);
                            Service.chatNPC(p, (short)npcid, "Chúc mừng con đã đổi thành công!");
                        }

                        return;    
            case 1:
                if (p.c.isNhanban) {
                    Service.chatNPC(p, (short)npcid, Language.NOT_FOR_PHAN_THAN);
                    return;
                }

                switch(b3) {
                    case 0:
                        if (p.c.isNhanban) {
                            Service.chatNPC(p, (short)npcid, Language.NOT_FOR_PHAN_THAN);
                            return;
                        }

                        if (p.c.getBagNull() < 1) {
                            Service.chatNPC(p, (short)npcid, Language.NOT_ENOUGH_BAG);
                            return;
                        }

                        if (p.c.level >= 10 && p.c.checkLevel[0] == 0) {
                            p.c.addItemBag(false, ItemTemplate.itemDefault(223, true));
                            if(p.status == 1) {
                                p.upluongMessage(1000L);
                                p.c.luongTN += 1000;
                            } else {
                                p.upluongMessage(2000L);
                            }

                            p.c.checkLevel[0] = 1;
                            Service.chatNPC(p, (short)npcid, "Chúc mừng con đã đạt đến cấp độ mới!");
                        } else {
                            Service.chatNPC(p, (short)npcid, "Trình độ của con không đủ hoặc con đã nhận thưởng rồi!");
                        }

                        return;
                    case 1:
                        if (p.c.isNhanban) {
                            Service.chatNPC(p, (short)npcid, Language.NOT_FOR_PHAN_THAN);
                            return;
                        }

                        if (p.c.getBagNull() < 1) {
                            Service.chatNPC(p, (short)npcid, Language.NOT_ENOUGH_BAG);
                            return;
                        }

                        if (p.c.level >= 20 && p.c.checkLevel[1] == 0) {
                            p.c.addItemBag(false, ItemTemplate.itemDefault(224, true));
                            if(p.status == 1) {
                                p.upluongMessage(1000L);
                                p.c.luongTN += 1000;
                            } else {
                                p.upluongMessage(2000L);
                            }
                            p.c.checkLevel[1] = 1;
                            Service.chatNPC(p, (short)npcid, "Chúc mừng con đã đạt đến cấp độ mới!");
                        } else {
                            Service.chatNPC(p, (short)npcid, "Trình độ của con không đủ hoặc con đã nhận thưởng rồi!");
                        }

                        return;
                    case 2:
                        if (p.c.isNhanban) {
                            Service.chatNPC(p, (short)npcid, Language.NOT_FOR_PHAN_THAN);
                            return;
                        }

                        if (p.c.getBagNull() < 1) {
                            Service.chatNPC(p, (short)npcid, Language.NOT_ENOUGH_BAG);
                            return;
                        }

                        if (p.c.level >= 30 && p.c.checkLevel[2] == 0) {
                            p.c.addItemBag(false, ItemTemplate.itemDefault(225, true));
                            if(p.status == 1) {
                                p.upluongMessage(1000L);
                                p.c.luongTN += 1000;
                            } else {
                                p.upluongMessage(2000L);
                            }
                            p.c.checkLevel[2] = 1;
                            Service.chatNPC(p, (short)npcid, "Chúc mừng con đã đạt đến cấp độ mới!");
                        } else {
                            Service.chatNPC(p, (short)npcid, "Trình độ của con không đủ hoặc con đã nhận thưởng rồi!");
                        }

                        return;
                    case 3:
                        if (p.c.isNhanban) {
                            Service.chatNPC(p, (short)npcid, Language.NOT_FOR_PHAN_THAN);
                            return;
                        }

                        if (p.c.getBagNull() < 1) {
                            Service.chatNPC(p, (short)npcid, Language.NOT_ENOUGH_BAG);
                            return;
                        }

                        if (p.c.level >= 40 && p.c.checkLevel[3] == 0) {
                            p.c.addItemBag(false, ItemTemplate.itemDefault(226, true));
                            if(p.status == 1) {
                                p.upluongMessage(1000L);
                                p.c.luongTN += 1000;
                            } else {
                                p.upluongMessage(2000L);
                            }
                            p.c.checkLevel[3] = 1;
                            Service.chatNPC(p, (short)npcid, "Chúc mừng con đã đạt đến cấp độ mới!");
                        } else {
                            Service.chatNPC(p, (short)npcid, "Trình độ của con không đủ hoặc con đã nhận thưởng rồi!");
                        }

                        return;
                    case 4:
                        if (p.c.isNhanban) {
                            Service.chatNPC(p, (short)npcid, Language.NOT_FOR_PHAN_THAN);
                            return;
                        }

                        if (p.c.getBagNull() < 1) {
                            Service.chatNPC(p, (short)npcid, Language.NOT_ENOUGH_BAG);
                            return;
                        }

                        if (p.c.level >= 50 && p.c.checkLevel[4] == 0) {
                            p.c.addItemBag(false, ItemTemplate.itemDefault(227, true));
                            if(p.status == 1) {
                                p.upluongMessage(1500L);
                                p.c.luongTN += 1500;
                            } else {
                                p.upluongMessage(3000L);
                            }
                            p.c.checkLevel[4] = 1;
                            Service.chatNPC(p, (short)npcid, "Chúc mừng con đã đạt đến cấp độ mới!");
                        } else {
                            Service.chatNPC(p, (short)npcid, "Trình độ của con không đủ hoặc con đã nhận thưởng rồi!");
                        }

                        return;
                    case 5:
                        if (p.c.isNhanban) {
                            Service.chatNPC(p, (short)npcid, Language.NOT_FOR_PHAN_THAN);
                            return;
                        }

                        if (p.c.getBagNull() < 1) {
                            Service.chatNPC(p, (short)npcid, Language.NOT_ENOUGH_BAG);
                            return;
                        }

                        if (p.c.level >= 70 && p.c.checkLevel[5] == 0) {
                            p.c.addItemBag(false, ItemTemplate.itemDefault(228, true));
                            if(p.status == 1) {
                                p.upluongMessage(1500L);
                                p.c.luongTN += 1500;
                            } else {
                                p.upluongMessage(3000L);
                            }
                            p.c.checkLevel[5] = 1;
                            Service.chatNPC(p, (short)npcid, "Chúc mừng con đã đạt đến cấp độ mới!");
                        } else {
                            Service.chatNPC(p, (short)npcid, "Trình độ của con không đủ hoặc con đã nhận thưởng rồi!");
                        }

                        return;
                    case 6:
                        if (p.c.isNhanban) {
                            Service.chatNPC(p, (short)npcid, Language.NOT_FOR_PHAN_THAN);
                            return;
                        }

                        if (p.c.level >= 90 && p.c.checkLevel[6] == 0) {
                            if(p.status == 1) {
                                p.upluongMessage(2500L);
                                p.c.luongTN += 2500;
                            } else {
                                p.upluongMessage(5000L);
                            }
                            p.c.checkLevel[6] = 1;
                            Service.chatNPC(p, (short)npcid, "Chúc mừng con đã đạt đến cấp độ mới!");
                        } else {
                            Service.chatNPC(p, (short)npcid, "Trình độ của con không đủ hoặc con đã nhận thưởng rồi!");
                        }

                        return;
                    case 7:
                        if (p.c.isNhanban) {
                            Service.chatNPC(p, (short)npcid, "Chức năng này không dành cho phân thân!");
                            return;
                        }

                        if (p.c.level >= 110 && p.c.checkLevel[7] == 0) {
                            if(p.status == 1) {
                                p.upluongMessage(2500L);
                                p.c.luongTN += 2500;
                            } else {
                                p.upluongMessage(5000L);
                            }
                            p.c.checkLevel[7] = 1;
                            Service.chatNPC(p, (short)npcid, Language.NOT_FOR_PHAN_THAN);
                        } else {
                            Service.chatNPC(p, (short)npcid, "Trình độ của con không đủ hoặc con đã nhận thưởng rồi!");
                        }

                        return;
                    case 8:
                        if (p.c.isNhanban) {
                            Service.chatNPC(p, (short)npcid, Language.NOT_FOR_PHAN_THAN);
                            return;
                        }

                        if (p.c.level >= 130 && p.c.checkLevel[8] == 0) {
                            if(p.status == 1) {
                                p.upluongMessage(3500L);
                                p.c.luongTN += 3500;
                            } else {
                                p.upluongMessage(7000L);
                            }
                            p.c.checkLevel[8] = 1;
                            Service.chatNPC(p, (short)npcid, "Chúc mừng con đã đạt đến cấp độ mới!");
                        } else {
                            Service.chatNPC(p, (short)npcid, "Trình độ của con không đủ hoặc con đã nhận thưởng rồi!");
                        }

                        return;
                    default: {
                        break;
                    }
                }
                break;
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }

    }

    public static void npcRikudou(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        MapTemplate map;
        MobTemplate mob;
        switch(menuId) {
            case 0: {
                Service.chatNPC(p, (short)npcid, "Hãy chăm chỉ lên nha.");
                break;
            }
            case 1: {
                switch(b3) {
                    case 0: {
                        if (p.c.isNhanban) {
                            Service.chatNPC(p, (short)npcid, Language.NOT_FOR_PHAN_THAN);
                            return;
                        }

                        if (p.c.level < 10) {
                            Service.chatNPC(p, (short)npcid, "Con cần đạt cấp độ 10 để có thể nhận nhiệm vụ.");
                            return;
                        }

                        if (p.c.isTaskHangNgay != 0) {
                            Service.chatNPC(p, (short)npcid, "Ta đã giao nhiệm vụ cho con trước đó rồi");
                            return;
                        }

                        if (p.c.countTaskHangNgay >= 20) {
                            Service.chatNPC(p, (short)npcid, "Con đã hoàn thành hết nhiệm vụ Ngay hôm nay rồi, Ngay mai hãy quay lại nha.");
                            return;
                        }

                        mob = Service.getMobIdByLevel(p.c.level);
                        if (mob != null) {
                            map = Service.getMobMapId(mob.id);
                            if (map != null) {
                                p.c.taskHangNgay[0] = 0;
                                p.c.taskHangNgay[1] = 0;
                                p.c.taskHangNgay[2] = Util.nextInt(10, 25);
                                p.c.taskHangNgay[3] = mob.id;
                                p.c.taskHangNgay[4] = map.id;
                                p.c.isTaskHangNgay = 1;
                                p.c.countTaskHangNgay++;
                                Service.getTaskOrder(p.c, (byte)0);
                                Service.chatNPC(p, (short)npcid, "Đây là nhiệm vụ thứ " + p.c.countTaskHangNgay + "/20 trong Ngay của con.");
                            }
                        }
                        break;
                    }

                    case 1: {
                        if (p.c.isNhanban) {
                            Service.chatNPC(p, (short)npcid, Language.NOT_FOR_PHAN_THAN);
                            return;
                        }

                        if (p.c.isTaskHangNgay == 0) {
                            Service.chatNPC(p, (short)npcid, "Con chưa nhận nhiệm vụ nào cả!");
                            return;
                        }

                        p.c.isTaskHangNgay = 0;
                        p.c.countTaskHangNgay--;
                        p.c.taskHangNgay = new int[]{-1, -1, -1, -1, -1, 0, p.c.countTaskHangNgay};
                        Service.clearTaskOrder(p.c, (byte)0);
                        Service.chatNPC(p, (short)npcid, "Con đã huỷ nhiệm vụ lần này.");
                        break;
                    }

                    case 2: {
                        if (p.c.isNhanban) {
                            Service.chatNPC(p, (short)npcid, Language.NOT_FOR_PHAN_THAN);
                            return;
                        }

                        if (p.c.isTaskHangNgay == 0) {
                            Service.chatNPC(p, (short)npcid, "Con chưa nhận nhiệm vụ nào cả!");
                            return;
                        }

                        if (p.c.getBagNull() == 0) {
                            p.conn.sendMessageLog(Language.NOT_ENOUGH_BAG);
                            return;
                        }

                        if (p.c.taskHangNgay[1] < p.c.taskHangNgay[2]) {
                            Service.chatNPC(p, (short)npcid, "Con chưa hoàn thành nhiệm vụ ta giao!");
                            return;
                        }

                        p.c.isTaskHangNgay = 0;
                        p.c.taskHangNgay = new int[]{-1, -1, -1, -1, -1, 0, p.c.countTaskHangNgay};
                        Service.clearTaskOrder(p.c, (byte)0);
                        long luongUp = Util.nextInt(30, 100);
                        if(p.status == 1) {
                            luongUp /= 2;
                            p.c.upxuMessage(150000L);
                            p.upluongMessage(luongUp);
                            p.c.xuTN += 150000;
                            p.c.luongTN += luongUp;
                        } else {
                            p.c.upxuMessage(350000L);
                            p.upluongMessage(luongUp);
                        }

                        if (p.c.pointUydanh < 5000) {
                            p.c.pointUydanh += 2;
                        }

                        if(p.c.countTaskHangNgay % 2 == 0) {
                            Item itemUp = ItemTemplate.itemDefault(p.c.gender == 1 ? 733 : 760, true);
                            itemUp.isLock = true;
                            itemUp.isExpires = false;
                            itemUp.expires = -1L;
                            itemUp.quantity = Util.nextInt(2,3);
                            p.c.addItemBag(true, itemUp);
                        } else {
                            Item itemUp = ItemTemplate.itemDefault(p.c.gender == 1 ? 734 : 761, true);
                            itemUp.isLock = true;
                            itemUp.isExpires = false;
                            itemUp.expires = -1L;
                            itemUp.quantity = Util.nextInt(2,3);
                            p.c.addItemBag(true, itemUp);
                        }

                        Service.chatNPC(p, (short)npcid, "Con hãy nhận lấy phần thưởng của mình.");
                        break;
                    }

                    case 3: {
                        if (p.c.isNhanban) {
                            Service.chatNPC(p, (short)npcid, Language.NOT_FOR_PHAN_THAN);
                            return;
                        }

                        if (p.c.taskHangNgay[4] != -1) {
                            Map ma = Manager.getMapid(p.c.taskHangNgay[4]);
                            int var8;
                            TileMap area;
                            for(var8 = 0; var8 < ma.area.length; ++var8) {
                                area = ma.area[var8];
                                if (area.numplayers < ma.template.maxplayers) {
                                    p.c.tileMap.leave(p);
                                    area.EnterMap0(p.c);
                                    return;
                                }
                            }
                        }
                        Service.chatNPC(p, (short)npcid, "Con chưa nhận nhiệm vụ nào cả!");
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
            }
            case 2: {
                switch(b3) {
                    case 0: {
                        if (p.c.isNhanban) {
                            Service.chatNPC(p, (short)npcid, Language.NOT_FOR_PHAN_THAN);
                            return;
                        }

                        if (p.c.level < 30) {
                            Service.chatNPC(p, (short)npcid, "Con cần đạt cấp độ 30 để có thể nhận nhiệm vụ tà thú.");
                            return;
                        }

                        if (p.c.isTaskTaThu != 0) {
                            Service.chatNPC(p, (short)npcid, "Ta đã giao nhiệm vụ cho con trước đó rồi");
                            return;
                        }

                        if (p.c.countTaskTaThu >= 2) {
                            Service.chatNPC(p, (short)npcid, "Con đã hoàn thành hết nhiệm vụ Ngay hôm nay rồi, Ngay mai hãy quay lại nha.");
                            return;
                        }
                        mob = Service.getMobIdTaThu(p.c.level);
                        if (mob != null) {
                            map = Service.getMobMapIdTaThu(mob.id);
                            if (map != null) {
                                p.c.taskTaThu[0] = 1;
                                p.c.taskTaThu[1] = 0;
                                p.c.taskTaThu[2] = 1;
                                p.c.taskTaThu[3] = mob.id;
                                p.c.taskTaThu[4] = map.id;
                                p.c.isTaskTaThu = 1;
                                ++p.c.countTaskTaThu;
                                Service.getTaskOrder(p.c, (byte)1);
                                Service.chatNPC(p, (short)npcid, "Hãy hoàn thành nhiệm vụ và trở về đây nhận thưởng.");
                            }
                        }
                        break;
                    }
                    case 1: {
                        if (p.c.isNhanban) {
                            Service.chatNPC(p, (short)npcid, Language.NOT_FOR_PHAN_THAN);
                            return;
                        }

                        if (p.c.isTaskTaThu == 0) {
                            Service.chatNPC(p, (short)npcid, "Con chưa nhận nhiệm vụ nào cả!");
                            return;
                        }

                        Service.clearTaskOrder(p.c, (byte)1);
                        p.c.isTaskTaThu = 0;
                        --p.c.countTaskTaThu;
                        p.c.taskTaThu = new int[]{-1, -1, -1, -1, -1, 0, p.c.countTaskTaThu};
                        Service.chatNPC(p, (short)npcid, "Con đã huỷ nhiệm vụ lần này.");
                        break;
                    }

                    case 2: {
                        if (p.c.isNhanban) {
                            Service.chatNPC(p, (short)npcid, Language.NOT_FOR_PHAN_THAN);
                            return;
                        }

                        if (p.c.isTaskTaThu == 0) {
                            Service.chatNPC(p, (short)npcid, "Con chưa nhận nhiệm vụ nào cả!");
                            return;
                        }

                        if (p.c.taskTaThu[1] < p.c.taskTaThu[2]) {
                            Service.chatNPC(p, (short)npcid, "Con chưa hoàn thành nhiệm vụ ta giao!");
                            return;
                        }

                        if (p.c.getBagNull() < 2) {
                            Service.chatNPC(p, (short)npcid, "Hành trang của con không đủ chỗ trống để nhận thưởng");
                            return;
                        }

                        p.c.isTaskTaThu = 0;
                        p.c.taskTaThu = new int[]{-1, -1, -1, -1, -1, 0, p.c.countTaskTaThu};
                        Service.clearTaskOrder(p.c, (byte)1);
                        if (p.c.pointUydanh < 5000) {
                            p.c.pointUydanh += 3;
                        }
                        Item item = ItemTemplate.itemDefault(251, false);
                        item.quantity = Util.nextInt(3, 4);
                        item.isLock = false;
                        p.c.addItemBag(true, item);

                        if(p.c.countTaskTaThu == 1) {
                            Item itemUp = ItemTemplate.itemDefault(p.c.gender == 1 ? 737 : 764, true);
                            itemUp.isLock = true;
                            itemUp.isExpires = false;
                            itemUp.expires = -1L;
                            itemUp.quantity = Util.nextInt(20,30);
                            p.c.addItemBag(true, itemUp);
                        } else {
                            Item itemUp = ItemTemplate.itemDefault(p.c.gender == 1 ? 738 : 765, true);
                            itemUp.isLock = true;
                            itemUp.isExpires = false;
                            itemUp.expires = -1L;
                            itemUp.quantity = Util.nextInt(20,30);
                            p.c.addItemBag(true, itemUp);
                        }

                        Service.chatNPC(p, (short)npcid, "Con hãy nhận lấy phần thưởng của mình.");
                        break;
                    }

                    default: {
                        break;
                    }
                }
                break;
            }
            case 3: {
                switch (b3) {
                    case 0: {
                        if (p.c.isNhanban) {
                            Service.chatNPC(p, (short)npcid, Language.NOT_FOR_PHAN_THAN);
                            return;
                        }
                        if(ChienTruong.chienTruong == null) {
                            Service.chatNPC(p, (short)npcid, "Chiến trường chưa được tổ chức.");
                            return;
                        }
                        if(ChienTruong.chienTruong != null) {
                            if(ChienTruong.chienTruong30 && (p.c.level < 30 || p.c.level >= 50)) {
                                Service.chatNPC(p, (short)npcid, "Bây giờ là thời gian chiến trường cho cấp độ từ 30 đến 49. Trình độ của con không phù hợp để tham gia.");
                                return;
                            }else if(ChienTruong.chienTruong50 && p.c.level < 50) {
                                Service.chatNPC(p, (short)npcid, "Bây giờ là thời gian chiến trường cho cấp độ lớn hơn hoặc bằng 50. Trình độ của con không phù hợp để tham gia.");
                                return;
                            }
                            if((ChienTruong.chienTruong30 ||ChienTruong.chienTruong50) && p.c.pheCT == 1) {
                                Service.chatNPC(p, (short)npcid, "Con đã điểm danh phe Hắc giả trước đó rồi.");
                                return;
                            }
                            if(ChienTruong.start && p.c.pheCT==-1) {
                                Service.chatNPC(p, (short)npcid, "Chiến trường đã bắt đầu, không thể báo danh.");
                                return;
                            }
                            if((ChienTruong.chienTruong30 || ChienTruong.chienTruong50) && p.c.pheCT == -1 ) {
                                if (p.c.pointUydanh < 5000) {
                                    p.c.pointUydanh += 10;
                                }
                                p.c.pheCT = 0;
                                p.c.pointCT = 0;
                                p.c.isTakePoint = 0;
                                p.c.typepk = 4;
                                Service.ChangTypePkId(p.c, (byte)4);
                                Service.updatePointCT(p.c, 0);
                                if(p.c.party != null) {
                                    p.c.party.removePlayer(p.c.id);
                                }
                                if(!ChienTruong.bxhCT.containsKey(p.c)) {
                                    ChienTruong.bxhCT.put(p.c, p.c.pointCT);
                                } else {
                                    ChienTruong.bxhCT.replace(p.c, p.c.pointCT);
                                }
                                Map ma = Manager.getMapid(ChienTruong.chienTruong.map[0].id);
                                for (TileMap area : ma.area) {
                                    if (area.numplayers < ma.template.maxplayers) {
                                        p.c.tileMap.leave(p);
                                        area.EnterMap0(p.c);
                                        return;
                                    }
                                }
                                return;
                            }
                            p.c.typepk = 4;
                            Service.ChangTypePkId(p.c, (byte)4);
                            Service.updatePointCT(p.c, 0);
                            if(p.c.party != null) {
                                p.c.party.removePlayer(p.c.id);
                            }
                            if(!ChienTruong.bxhCT.containsKey(p.c)) {
                                ChienTruong.bxhCT.put(p.c, p.c.pointCT);
                            } else {
                                ChienTruong.bxhCT.replace(p.c, p.c.pointCT);
                            }
                            Map ma = Manager.getMapid(ChienTruong.chienTruong.map[0].id);
                            for (TileMap area : ma.area) {
                                if (area.numplayers < ma.template.maxplayers) {
                                    p.c.tileMap.leave(p);
                                    area.EnterMap0(p.c);
                                    return;
                                }
                            }
                        }
                        return;
                    }
                    case 1: {
                        if (p.c.isNhanban) {
                            Service.chatNPC(p, (short)npcid, Language.NOT_FOR_PHAN_THAN);
                            return;
                        }
                        if(ChienTruong.chienTruong == null) {
                            Service.chatNPC(p, (short)npcid, "Chiến trường chưa được tổ chức.");
                            return;
                        }
                        if(ChienTruong.chienTruong != null) {
                            if( ChienTruong.chienTruong30 && (p.c.level < 30 || p.c.level >= 50)) {
                                Service.chatNPC(p, (short)npcid, "Bây giờ là thời gian chiến trường cho cấp độ từ 30 đến 49. Trình độ của con không phù hợp để tham gia.");
                                return;
                            }else if(ChienTruong.chienTruong50 && p.c.level < 50) {
                                Service.chatNPC(p, (short)npcid, "Bây giờ là thời gian chiến trường cho cấp độ lớn hơn hoặc bằng 50. Trình độ của con không phù hợp để tham gia.");
                                return;
                            }
                            if(ChienTruong.start && p.c.pheCT==-1) {
                                Service.chatNPC(p, (short)npcid, "Chiến trường đã bắt đầu, không thể báo danh.");
                                return;
                            }
                            if((ChienTruong.chienTruong30 ||ChienTruong.chienTruong50) && p.c.pheCT == 0) {
                                Service.chatNPC(p, (short)npcid, "Con đã điểm danh phe Bạch giả trước đó rồi.");
                                return;
                            }
                            if( (ChienTruong.chienTruong30 || ChienTruong.chienTruong50) && p.c.pheCT == -1 ) {
                                if (p.c.pointUydanh < 5000) {
                                    p.c.pointUydanh += 10;
                                }
                                p.c.pheCT = 1;
                                p.c.pointCT = 0;
                                p.c.typepk = 5;
                                p.c.isTakePoint = 0;
                                Service.ChangTypePkId(p.c, (byte)5);
                                Service.updatePointCT(p.c, 0);
                                if(p.c.party != null) {
                                    p.c.party.removePlayer(p.c.id);
                                }
                                if(!ChienTruong.bxhCT.containsKey(p.c)) {
                                    ChienTruong.bxhCT.put(p.c, p.c.pointCT);
                                } else {
                                    ChienTruong.bxhCT.replace(p.c, p.c.pointCT);
                                }
                                Map ma = Manager.getMapid(ChienTruong.chienTruong.map[6].id);
                                for (TileMap area : ma.area) {
                                    if (area.numplayers < ma.template.maxplayers) {
                                        p.c.tileMap.leave(p);
                                        area.EnterMap0(p.c);
                                        return;
                                    }
                                }
                                return;
                            }
                            p.c.typepk = 5;
                            Service.ChangTypePkId(p.c, (byte)5);
                            Service.updatePointCT(p.c, 0);
                            if(p.c.party != null) {
                                p.c.party.removePlayer(p.c.id);
                            }
                            if(!ChienTruong.bxhCT.containsKey(p.c)) {
                                ChienTruong.bxhCT.put(p.c, p.c.pointCT);
                            } else {
                                ChienTruong.bxhCT.replace(p.c, p.c.pointCT);
                            }
                            Map ma = Manager.getMapid(ChienTruong.chienTruong.map[6].id);
                            for (TileMap area : ma.area) {
                                if (area.numplayers < ma.template.maxplayers) {
                                    p.c.tileMap.leave(p);
                                    area.EnterMap0(p.c);
                                    return;
                                }
                            }
                        }
                        return;
                    }
                    case 2: {
                        if(ChienTruong.finish) {
                            Service.evaluateCT(p.c);
                        } else {
                            Server.manager.sendTB(p, "Kết quả", "Chưa có thông tin.");
                        }
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
            }
            case 4: {
                Server.manager.sendTB(p, "Hướng dẫn", "Chiến trường được mở 2 lần mỗi Ngay.\n" +
                        "- Chiến trường lv30: giành cho nhân vật level từ 30 đến 45, điểm danh vào lúc 19h và bắt đầu từ 19h30' đến 20h30'.\n" +
                        "- Chiến trường lv50: giành cho nhân vật level từ 50 trở lên, điểm danh vào lúc 21h và bắt đầu từ 21h30' đến 22h30'.\n\n" +
                        "+ Top1: 10v đan mỗi loại + 3tr xu.\n" +
                        "+ Top 2: 7v đan mỗi loại + 2tr xu.\n" +
                        "+ Top 3: 5v đan mỗi loại + 1tr xu.\n" +
                        "+ Phe thắng: 1v đan mỗi loại + 500k xu.");
                break;
            }
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }

    }

    public static void npcGoosho(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 0:
                p.requestItem(14);
                break;
            case 1:
                p.requestItem(15);
                break;
            case 2:
                p.requestItem(32);
                break;
            case 3:
                p.requestItem(34);
                break;
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }

    }

    public static void npcTruCoQuan(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 0: {
                if(p.c.quantityItemyTotal(260) < 1) {
                    p.sendAddchatYellow("Không có chìa khoá để mở cửa này.");
                    return;
                }
                if(p.c.tileMap.map.lanhDiaGiaToc != null && p.c.tileMap.map.mapLDGT()) {
                    switch (p.c.tileMap.map.id) {
                        case 80: {

                            p.c.tileMap.map.lanhDiaGiaToc.openMap(1, p);
                            break;
                        }
                        case 81: {

                            p.c.tileMap.map.lanhDiaGiaToc.openMap(2, p);
                            break;
                        }
                        case 82: {

                            p.c.tileMap.map.lanhDiaGiaToc.openMap(3, p);
                            break;
                        }
                        case 83: {

                            p.c.tileMap.map.lanhDiaGiaToc.openMap(4, p);
                            break;
                        }
                        case 84: {

                            p.c.tileMap.map.lanhDiaGiaToc.openMap(5, p);
                            break;
                        }
                        case 85: {
                            p.c.tileMap.map.lanhDiaGiaToc.openMap(6, p);
                            break;
                        }
                        case 86: {
                            p.c.tileMap.map.lanhDiaGiaToc.openMap(7, p);
                            break;
                        }
                        case 87: {
                            p.c.tileMap.map.lanhDiaGiaToc.openMap(8, p);
                            Server.manager.sendTB(p, "Ghi chú", "Con đường này sẽ dẫn đến cánh cửa nơi ở của một nhân vật huyền bí đã bị lời nguyền cổ " +
                                    "xưa yểm bùa rằng sẽ không ai có thể đánh bại được nhân vật huyền bí này. Bạn hãy mau tìm cách hoá giải lời nguyền.");
                            break;
                        }
                        case 88: {
                            p.c.tileMap.map.lanhDiaGiaToc.openMap(9, p);
                            Server.manager.sendTB(p, "Ghi chú", "Con đường này sẽ dẫn đến cánh cửa nơi ở của một nhân vật huyền bí đã bị lời nguyền cổ " +
                                    "xưa yểm bùa rằng sẽ không ai có thể đánh bại được nhân vật huyền bí này. Bạn hãy mau tìm cách hoá giải lời nguyền.");
                            break;
                        }
                        case 89: {
                            p.c.tileMap.map.lanhDiaGiaToc.openMap(10, p);
                            Server.manager.sendTB(p, "Ghi chú", "Con đường này sẽ dẫn đến cánh cửa nơi ở của một nhân vật huyền bí đã bị lời nguyền cổ " +
                                    "xưa yểm bùa rằng sẽ không ai có thể đánh bại được nhân vật huyền bí này. Bạn hãy mau tìm cách hoá giải lời nguyền.");
                            break;
                        }
                        default: {
                            break;
                        }

                    }
                }
                break;
            }
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }
    }

    public static void npcShinwa(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 0: {
                p.menuIdAuction = b3;
                final List<ShinwaTemplate> itemShinwas = ShinwaManager.entrys.get((int)b3);
                final Message mess = new Message(103);
                mess.writer().writeByte(b3);
                if(itemShinwas != null) {
                    mess.writer().writeInt(itemShinwas.size());
                    ShinwaTemplate item;
                    for(int i = 0; i < itemShinwas.size(); i++) {
                        item = itemShinwas.get(i);
                        if(item != null) {
                            mess.writer().writeInt(i);
                            mess.writer().writeInt(item.getRemainTime());
                            mess.writer().writeShort(item.getItem().quantity);
                            mess.writer().writeUTF(item.getSeller());
                            mess.writer().writeInt((int)item.getPrice());
                            mess.writer().writeShort(item.getItem().id);
                        } else {
                            mess.writer().writeInt(i);
                            mess.writer().writeInt(-1);
                            mess.writer().writeShort(0);
                            mess.writer().writeUTF("");
                            mess.writer().writeInt(999999999);
                            mess.writer().writeShort(12);
                        }
                    }
                } else {
                    mess.writer().writeInt(0);
                }
                mess.writer().flush();
                p.conn.sendMessage(mess);
                mess.cleanup();
                break;
            }
            case 1: {
                p.menuIdAuction = -2;
                p.requestItem(36);
                break;
            }
            case 2: {
                try {
                    synchronized (ShinwaManager.entrys.get((int)-1)) {
                        List<ShinwaTemplate> itemShinwas = ShinwaManager.entrys.get((int)-1);
                        List<Integer> ind = new ArrayList<>();
                        ShinwaTemplate item;
                        for(int i = itemShinwas.size() - 1; i>=0; i--) {
                            item = itemShinwas.get(i);
                            if(item != null && item.getSeller().equals(p.c.name)) {
                                if(p.c.getBagNull() == 0) {
                                    Service.chatNPC(p, (short) npcid, "Hành trang không đủ chỗ trống để nhận thêm vật phẩm!");
                                    return;
                                } 
                                p.c.addItemBag(true, item.getItem());
                                ind.add(i);
                                itemShinwas.remove(i);
                            }
                        }                       
                        if(ind.size() < 1) {
                            Service.chatNPC(p, (short) npcid, "Con không có đồ để nhận lại!");
                            return;
                        }
                        for(int i : ind) {
                            itemShinwas.remove(i);
                        }
                    }
                } catch (Exception e) {
                    p.conn.sendMessageLog("Có lỗi, vui lòng thử lại sau!");
                }
                break;
            }
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }
    }

    public static void npcChiHang(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }
    }

    public static void npcRakkii(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 0: {
                p.requestItem(38);
                break;
            }
            case 1: {
                if (p.c.isNhanban) {
                    Service.chatNPC(p, (short) npcid, Language.NOT_FOR_PHAN_THAN);
                    return;
                }

                Service.sendInputDialog(p, (short) 4, "Nhập Gift Code tại đây");
                break;
            }
            case 2: {
                switch (b3) {
                    case 0:
                    case 1: {
                        Server.manager.rotationluck[0].luckMessage(p);
                        return;
                    }
                    case 2: {
                        Server.manager.sendTB(p, "Vòng xoay vip", "Hãy đặt cược xu và thử vận may của mình trong 2 phút nha.");
                        return;
                    }
                    default: {
                        return;
                    }
                }
            }
            case 3: {
                switch (b3) {
                    case 0:
                    case 1: {
                        Server.manager.rotationluck[1].luckMessage(p);
                        return;
                    }
                    case 2: {
                        Server.manager.sendTB(p, "Vòng xoay thường", "Hãy đặt cược xu và thử vận may của mình trong 2 phút nha.");
                        return;
                    }
                    default: {
                        return;
                    }
                }
            }
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }

    }

    public static void npcLongDen(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }
    }

    public static void npcKagai(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 1: {
                switch (b3) {
                    case 0: {
                        if (p.c.isNhanban) {
                            p.conn.sendMessageLog(Language.NOT_FOR_PHAN_THAN);
                            return;
                        }
                        if (p.c.clan == null) {
                            Service.chatNPC(p, (short) npcid, "Con chưa có Gia tộc.");
                            return;
                        }
                        if (p.c.clan != null && p.c.clan.typeclan != 4) {
                            Service.chatNPC(p, (short) npcid, "Con không phải tộc trưởng, không thể mời gia tộc chiến.");
                            return;
                        }
                        //Service.sendInputDialog(p, (short) 5, "Nhập tên gia tộc đối phương");
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
            }
            case 3: {
                if (p.c.isNhanban) {
                    Service.chatNPC(p, (short) npcid, Language.NOT_FOR_PHAN_THAN);
                } else {
                    Item it;
                    Char var6;
                    switch (b3) {
                        case 0:
                            if (p.c.pointUydanh < 300) {
                                Service.chatNPC(p, (short) npcid, "Con cần 300 điểm hoạt động để để lấy bí kíp 3 Ngay.");
                                return;
                            } else {
                                if (p.c.getBagNull() < 1) {
                                    Service.chatNPC(p, (short) npcid, Language.NOT_ENOUGH_BAG);
                                } else {
                                    var6 = p.c;
                                    var6.pointUydanh -= 300;
                                    it = ItemTemplate.itemDefault(396 + p.c.nclass);
                                    it.isLock = false;
                                    it.quantity = 1;
                                    it.isExpires = true;
                                    it.expires = System.currentTimeMillis() + 259200000L;
                                    p.c.addItemBag(false, it);
                                    p.c.upxuMessage(3000000);
                                }

                                return;
                            }
                        case 1: {
                            if (p.c.pointUydanh < 700) {
                                Service.chatNPC(p, (short) npcid, "Con cần 700 điểm hoạt động để để lấy bí kíp 7 Ngay.");
                                return;
                            } else {
                                if (p.c.getBagNull() < 1) {
                                    Service.chatNPC(p, (short) npcid, Language.NOT_ENOUGH_BAG);
                                } else {
                                    var6 = p.c;
                                    var6.pointUydanh -= 700;
                                    it = ItemTemplate.itemDefault(396 + p.c.nclass);
                                    it.isLock = false;
                                    it.quantity = 1;
                                    it.isExpires = true;
                                    it.expires = System.currentTimeMillis() + 432000000L;
                                    p.c.addItemBag(false, it);
                                    p.c.upxuMessage(5000000);
                                }
                                return;
                            }

                        }
                        case 2: {
                            if (p.c.pointUydanh < 2000) {
                                Service.chatNPC(p, (short) npcid, "Con cần 2000 điểm hoạt động để để lấy bí kíp 15 Ngay.");
                            } else if (p.c.getBagNull() < 1) {
                                Service.chatNPC(p, (short) npcid, Language.NOT_ENOUGH_BAG);
                            } else {
                                var6 = p.c;
                                var6.pointUydanh -= 2000;
                                it = ItemTemplate.itemDefault(396 + p.c.nclass);
                                it.isLock = false;
                                it.quantity = 1;
                                it.isExpires = true;
                                it.expires = System.currentTimeMillis() + 1296000000L;
                                p.c.addItemBag(false, it);
                                p.c.upxuMessage(10000000);
                            }
                            break;
                        }
                    }
                }
                break;
            }
            case 4: {
                if (p.c.isNhanban) {
                    Service.chatNPC(p, (short) npcid, Language.NOT_FOR_PHAN_THAN);
                    break;
                } else {
                    switch (b3) {
                        case 0: {
                            p.requestItem(43);
                            break;
                        }
                        case 1: {
                            p.requestItem(44);
                            break;
                        }
                        case 2: {
                            p.requestItem(45);
                            break;
                        }
                        case 3: {
                            Server.manager.sendTB(p, "Hướng dẫn", "- Tinh luyện...");
                            break;
                        }
                        default: {
                            Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                            break;
                        }
                    }
                }
                break;
            }
            case 0:
            case 2:
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }

    }

    public static void npcTienNu(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        if (p.typemenu == 33) {
            Item it;
            switch(Server.manager.event) {
                //Hè
                case 1: {
                    if (p.c.isNhanban) {
                        Service.chatNPC(p, (short) npcid, Language.NOT_FOR_PHAN_THAN);
                    } else {
                        switch (menuId) {
                            case 0: {
                                if (p.c.quantityItemyTotal(432) >= 1 && p.c.quantityItemyTotal(428) >= 3 && p.c.quantityItemyTotal(429) >= 2 && p.c.quantityItemyTotal(430) >= 3) {
                                    if (p.c.getBagNull() == 0) {
                                        p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                                    } else {
                                        it = ItemTemplate.itemDefault(434);
                                        p.c.addItemBag(true, it);
                                        p.c.removeItemBags(432, 1);
                                        p.c.removeItemBags(428, 3);
                                        p.c.removeItemBags(429, 2);
                                        p.c.removeItemBags(430, 3);
                                    }

                                    return;
                                } else {
                                    Service.chatNPC(p, (short) npcid, "Hành trang của con không có đủ nguyên liệu");
                                }
                                break;
                            }
                            case 1: {
                                if (p.c.quantityItemyTotal(433) >= 1 && p.c.quantityItemyTotal(428) >= 2 && p.c.quantityItemyTotal(429) >= 3 && p.c.quantityItemyTotal(431) >= 2) {
                                    if (p.c.getBagNull() == 0) {
                                        p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                                    } else {
                                        it = ItemTemplate.itemDefault(435);
                                        p.c.addItemBag(true, it);
                                        p.c.removeItemBags(433, 1);
                                        p.c.removeItemBags(428, 2);
                                        p.c.removeItemBags(429, 3);
                                        p.c.removeItemBags(431, 2);
                                    }
                                } else {
                                    Service.chatNPC(p, (short) npcid, "Hành trang của con không có đủ nguyên liệu");
                                }
                                break;
                            }
                        }
                    }
                    break;
                }
                //Trung thu
                case 2: {
                    if (p.c.isNhanban) {
                        Service.chatNPC(p, (short) npcid, Language.NOT_FOR_PHAN_THAN);
                    } else {
                        switch (menuId) {
                            case 0: {
                                if (p.c.quantityItemyTotal(304) >= 1 && p.c.quantityItemyTotal(298) >= 1 && p.c.quantityItemyTotal(299) >= 1 && p.c.quantityItemyTotal(300) >= 1 && p.c.quantityItemyTotal(301) >= 1) {
                                    if (p.c.getBagNull() == 0) {
                                        p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                                    } else {
                                        it = ItemTemplate.itemDefault(302);
                                        p.c.addItemBag(true, it);
                                        p.c.removeItemBags(304, 1);
                                        p.c.removeItemBags(298, 1);
                                        p.c.removeItemBags(299, 1);
                                        p.c.removeItemBags(300, 1);
                                        p.c.removeItemBags(301, 1);
                                    }

                                    return;
                                } else {
                                    Service.chatNPC(p, (short) npcid, "Hành trang của con không có đủ nguyên liệu");
                                }
                                break;
                            }
                            case 1: {
                                if (p.c.quantityItemyTotal(305) >= 1 && p.c.quantityItemyTotal(298) >= 1 && p.c.quantityItemyTotal(299) >= 1 && p.c.quantityItemyTotal(300) >= 1 && p.c.quantityItemyTotal(301) >= 1) {
                                    if (p.c.getBagNull() == 0) {
                                        p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                                    } else {
                                        it = ItemTemplate.itemDefault(303);
                                        p.c.addItemBag(true, it);
                                        p.c.removeItemBags(305, 1);
                                        p.c.removeItemBags(298, 1);
                                        p.c.removeItemBags(299, 1);
                                        p.c.removeItemBags(300, 1);
                                        p.c.removeItemBags(301, 1);
                                    }

                                    return;
                                } else {
                                    Service.chatNPC(p, (short) npcid, "Hành trang của con không có đủ nguyên liệu");
                                }
                                break;
                            }
                            case 2: {
                                if (p.c.yen >= 10000 && p.c.quantityItemyTotal(292) >= 3 && p.c.quantityItemyTotal(293) >= 2 && p.c.quantityItemyTotal(294) >= 3) {
                                    if (p.c.getBagNull() == 0) {
                                        p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                                    } else {
                                        it = ItemTemplate.itemDefault(298);
                                        p.c.addItemBag(true, it);
                                        p.c.upyenMessage(-10000L);
                                        p.c.removeItemBags(292, 3);
                                        p.c.removeItemBags(293, 2);
                                        p.c.removeItemBags(294, 3);
                                    }

                                    return;
                                } else {
                                    Service.chatNPC(p, (short) npcid, "Hành trang của con không có đủ nguyên liệu");
                                }
                                break;
                            }
                            case 3: {
                                if (p.c.yen >= 10000 && p.c.quantityItemyTotal(292) >= 2 && p.c.quantityItemyTotal(295) >= 3 && p.c.quantityItemyTotal(294) >= 2) {
                                    if (p.c.getBagNull() == 0) {
                                        p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                                    } else {
                                        it = ItemTemplate.itemDefault(299);
                                        p.c.addItemBag(true, it);
                                        p.c.upyenMessage(-10000L);
                                        p.c.removeItemBags(292, 2);
                                        p.c.removeItemBags(295, 3);
                                        p.c.removeItemBags(294, 2);
                                    }

                                    return;
                                } else {
                                    Service.chatNPC(p, (short) npcid, "Hành trang của con không có đủ nguyên liệu");
                                }
                                break;
                            }
                            case 4: {
                                if (p.c.yen >= 10000 && p.c.quantityItemyTotal(292) >= 2 && p.c.quantityItemyTotal(295) >= 3 && p.c.quantityItemyTotal(297) >= 3) {
                                    if (p.c.getBagNull() == 0) {
                                        p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                                    } else {
                                        it = ItemTemplate.itemDefault(300);
                                        p.c.addItemBag(true, it);
                                        p.c.upyenMessage(-10000L);
                                        p.c.removeItemBags(292, 2);
                                        p.c.removeItemBags(295, 3);
                                        p.c.removeItemBags(297, 3);
                                    }

                                    return;
                                } else {
                                    Service.chatNPC(p, (short) npcid, "Hành trang của con không có đủ nguyên liệu");
                                }
                                break;
                            }
                            case 5: {
                                if (p.c.yen >= 10000 && p.c.quantityItemyTotal(292) >= 2 && p.c.quantityItemyTotal(296) >= 2 && p.c.quantityItemyTotal(297) >= 3) {
                                    if (p.c.getBagNull() == 0) {
                                        p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                                    } else {
                                        it = ItemTemplate.itemDefault(301);
                                        p.c.addItemBag(true, it);
                                        p.c.upyenMessage(-10000L);
                                        p.c.removeItemBags(292, 2);
                                        p.c.removeItemBags(296, 2);
                                        p.c.removeItemBags(297, 3);
                                    }
                                } else {
                                    Service.chatNPC(p, (short) npcid, "Hành trang của con không có đủ nguyên liệu");
                                }
                                break;
                            }
                        }
                    }
                    break;
                }

                //Noel
                case 3: {
                    if (p.c.isNhanban) {
                        Service.chatNPC(p, (short) npcid, Language.NOT_FOR_PHAN_THAN);
                        return;
                    }
                    switch (menuId) {
                        case 0: {
                            Service.sendInputDialog(p, (short) 6, "Nhập số lượng bánh Chocolate muốn làm.");
                            break;
                        }
                        case 1: {
                            Service.sendInputDialog(p, (short) 7, "Nhập số lượng bánh  Dâu tây muốn làm.");
                            break;
                        }
                        case 2: {
                            if(p.c.pointNoel < 3500) {
                                Service.chatNPC(p, (short) npcid, "Con cần ít nhất 3500 điểm để đổi mặt nạ 7 Ngay.");
                                return;
                            }
                            p.c.pointNoel -= 3500;
                            it = ItemTemplate.itemDefault(p.c.gender == 1 ? 407 : 408);
                            it.isLock = false;
                            it.quantity = 1;
                            it.isExpires = true;
                            it.expires = System.currentTimeMillis() + 604800000L;
                            p.c.addItemBag(false, it);
                            break;
                        }
                        case 3: {
                            if(p.c.pointNoel < 5000) {
                                Service.chatNPC(p, (short) npcid, "Con cần ít nhất 5000 điểm để đổi pet Hoả long 7 Ngay.");
                                return;
                            }
                            p.c.pointNoel -= 5000;
                            it = ItemTemplate.itemDefault(583);
                            it.isLock = false;
                            it.quantity = 1;
                            it.isExpires = true;
                            it.expires = System.currentTimeMillis() + 604800000L;
                            p.c.addItemBag(false, it);
                            break;
                        }
                        case 4: {
                            String a = "";
                            if(Rank.bxhBossTuanLoc.isEmpty()) {
                                a = "Chưa có thông tin.";
                            }
                            for(Rank.Entry3 item : Rank.bxhBossTuanLoc) {
                                a += item.index +". "+item.name+": "+item.point+" điểm\n";
                            }
                            Server.manager.sendTB(p, "BXH Diệt Boss", a);
                            break;
                        }
                        case 5: {
                            Server.manager.sendTB(p, "Hướng dẫn", "- Số điểm hiện tại của bạn là: "+p.c.pointNoel+"\n" +
                                    "- Kiểm điểm sự kiện bằng cách nhận quà hàng Ngay tại Cây thông (+1 điểm), trang trí cây thông (+10 điểm), giết boss Tuần lộc (+1 điểm).\n" +
                                    "- Dùng điểm để dổi lấy vật phẩm quý giá: Mặt nạ Super Broly/Onna Bugeisha 7 Ngay (3500 điểm), Pet Hoả long 7 Ngay (5000 điểm).\n" +
                                    "- Bánh Chocolate: 2 Bơ + 2 Kem + 3 Đường + 1 Chocolate + 5000 yên.\n" +
                                    "- Bánh Dâu tây: 3 Bơ + 3 Kem + 4 Đường + 1 Dâu tây + 10000 yên.\n");
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                    break;
                }
                //Tết
                case 4: {
                    if (p.c.isNhanban) {
                        Service.chatNPC(p, (short) npcid, Language.NOT_FOR_PHAN_THAN);
                        return;
                    }
                    switch (menuId) {
                        case 0: {
                            Service.sendInputDialog(p, (short) 110, "Nhập số lượng bánh Chưng muốn làm :");
                            break;
                        }
                        case 1: {
                            Service.sendInputDialog(p, (short) 111, "Nhập số lượng bánh Tét muốn làm :");
                            break;
                        }
                        case 2: {
                            if(p.luong < 500){
                                p.conn.sendMessageLog("Bạn không đủ 500 lượng để thực hiện điều này.");
                                return;
                                }else {
                                   Service.sendInputDialog(p, (short) 113, "Nhập tên nhân vật nhận:");
                                }
                            break;
                        }
                        case 3: 
                        {
                            Service.sendInputDialog(p, (short) 112, "Nhập số lượng Pháo muốn làm :");
                            break;
                        }
                        case 4: {
                            String a = "";
                            if(Rank.bxhBossChuot.isEmpty()) {
                                a = "Chưa có thông tin.";
                            }
                            for(Rank.Entry4 item : Rank.bxhBossChuot) {
                                a += item.index +". "+item.name+": "+item.point1+" điểm\n";
                            }
                            Server.manager.sendTB(p, "BXH Diệt Boss Chuột", a);
                            break;
                        }
                        case 5: {
                            Server.manager.sendTB(p, "Hướng dẫn", "----------------- Làm Bánh Chưng -----------------\n +, 3 lá dong + 5 nếp + 1 thịt heo + 3 đậu xanh + 2 lạt tre + 50.000 xu + 50.000 yên.\n" +
                                    "----------------- Làm Bánh Tét -----------------\n +,  2 lá dong + 4 nếp + 2 đậu xanh + 4 lạt tre + 40.000 xu + 40.000 yên.\n" +
                                    "----------------- Làm Pháo -----------------\n +, Ghép 10 mảnh Pháo + 30k xu + 30k yên thành 1 dây Pháo, sử dụng và nhận quà bất ngờ.\n -Bạn có thể mua tại Goosho hoặc tham gia Lôi Đài để nhận Mảnh Pháo ( cứ chiến thắng 5 trận cộng dồn bạn sẽ nhận được 1 mảnh Pháo ).\n" +
                                    "----------------- Săn Boss Sự Kiện -----------------\n +, Trong quá trình diễn ra sự kiện Tết ,Boss Chuột sẽ xuất hiện ngẫu nhiên tại cạnh các Trường ,hãy nhanh tay tiêu diệt chúng và nhận điểm ( cứ giết 1 em bạn sẽ nhận được 1 điểm ) để đổi những Phần Quà có Giá Trị nhé.\n");
                                    
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                    break;
                }
                default: {
                    Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                    break;
                }
            }

        }
    }

    public static void npcCayThong(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 0: {
                if (p.c.isNhanban) {
                    p.conn.sendMessageLog(Language.NOT_FOR_PHAN_THAN);
                    return;
                }
                if(p.c.level < 40) {
                    p.conn.sendMessageLog("Nhân vật phải trên level 40 mới có thể nhận quà và trang trí.");
                    return;
                }
                if(p.c.isNhanQuaNoel < 1) {
                    p.conn.sendMessageLog("Hôm nay bạn đã nhận quà rồi.");
                    return;
                }
                if(p.c.getBagNull() < 1) {
                    p.conn.sendMessageLog("Hành trang không đủ chỗ trống để nhận quà");
                    return;
                }
                p.c.isNhanQuaNoel = 0;
                p.c.pointNoel++;
                int random = Util.nextInt(0,2);
                switch (random) {
                    case 0: {
                        int yen = Util.nextInt(500000,1000000);
                        if(p.status == 1) {
                            yen /= 2;
                            p.c.yenTN += yen;
                        }
                        p.c.upyenMessage(yen);
                        p.sendAddchatYellow("Bạn nhận được " + yen + " yên.");
                        break;
                    }
                    case 1: {
                        int xu = Util.nextInt(100000,300000);
                        if(p.status == 1) {
                            xu /= 2;
                            p.c.xuTN += xu;
                        }
                        p.c.upxuMessage(xu);
                        p.sendAddchatYellow("Bạn nhận được " + xu + " xu.");
                        break;
                    }
                    case 2: {
                        int luong = Util.nextInt(50,150);
                        if(p.status == 1) {
                            luong /= 2;
                            p.c.luongTN += luong;
                        }
                        p.upluongMessage(luong);
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
            }
            case 1: {
                if (p.c.isNhanban) {
                    p.conn.sendMessageLog( Language.NOT_FOR_PHAN_THAN);
                    return;
                }
                if(p.c.level < 40) {
                    p.conn.sendMessageLog("Nhân vật phải trên level 40 mới có thể nhận quà và trang trí.");
                    return;
                }
                if(p.c.quantityItemyTotal(673) < 1) {
                    p.conn.sendMessageLog("Bạn không có đủ Quà trang trí để trang trí cây thông Noel.");
                    return;
                }
                if(p.c.getBagNull() < 1) {
                    p.conn.sendMessageLog("Hành trang không đủ chỗ trống để nhận quà");
                    return;
                }
                p.c.pointNoel += 10;
                p.c.removeItemBag(p.c.getIndexBagid(673, false), 1);
                Item it;
                int per = Util.nextInt(300);
                if(per<1) {
                    it = ItemTemplate.itemDefault(383);
                } else if (per >= 1 && per <= 3) {
                    it = ItemTemplate.itemDefault(775);
                } else {
                    per = Util.nextInt(UseItem.idItemCayThong.length);
                    it = ItemTemplate.itemDefault(UseItem.idItemCayThong[per]);
                }
                it.isLock = false;
                it.quantity = 1;
                p.c.addItemBag(true, it);
                break;
            }
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }
    }

    public static void npcOngGiaNoen(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 0: {
                if(Server.manager.event != 3) {
                    Service.chatNPC(p, (short) npcid, "Hiện tại không trong thời gian sự kiện Noel");
                    return;
                }
                if (p.c.isNhanban) {
                    Service.chatNPC(p, (short) npcid, Language.NOT_FOR_PHAN_THAN);
                    return;
                }
                if(p.c.quantityItemyTotal(775) < 1000) {
                    Service.chatNPC(p, (short) npcid, "Bạn không có đủ 1000 hoa tuyết để đổi mặt nạ.");
                    return;
                }
                if(p.c.getBagNull() < 1) {
                    Service.chatNPC(p, (short) npcid, "Hành trang không đủ chỗ trống để nhận quà");
                    return;
                }
                p.c.removeItemBag( p.c.getIndexBagid(775, false), 1000);
                Item it = ItemTemplate.itemDefault(774);
                it.isLock = false;
                it.quantity = 1;
                it.isExpires = true;
                it.expires = System.currentTimeMillis() + 2592000000L;
                p.c.addItemBag(false, it);
                break;
            }
            case 1: {
                if(Server.manager.event != 3) {
                    Service.chatNPC(p, (short) npcid, "Hiện tại không trong thời gian diễn ra sự kiện Noel");
                    return;
                }
                Server.manager.sendTB(p, "Hướng dẫn", "- Kiếm hoa tuyết bằng cách sử dụng Bánh khúc cây chocolate, Bánh khúc cây dâu tây hoặc trang trí cây thông.\n- Dùng 1000 bông hoa tuyết để đổi lấy mặt nạ Satan với chỉ số khủng.");
                break;
            }
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }
    }

    public static void npcVuaHung(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 0: {
                Service.sendInputDialog(p, (short) 9, "Nhập số COIN muốn đổi.");
                break;
            }
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }
    }

    public static void npcKanata_LoiDai(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 0:
                if (p.c.isNhanban) {
                    Service.chatNPC(p, (short)npcid, Language.NOT_FOR_PHAN_THAN);
                    return;
                }

                if (p.c.party != null && p.c.party.charID != p.c.id) {
                    p.c.party.removePlayer(p.c.id);
                }

                p.c.dunId = -1;
                p.c.isInDun = false;
                p.c.tileMap.leave(p);
                p.restCave();
                p.changeMap(p.c.mapKanata);
                break;
            case 1:
                if (p.c.isNhanban) {
                    Service.chatNPC(p, (short)npcid, Language.NOT_FOR_PHAN_THAN);
                    return;
                }

                if (p.c.party != null && p.c.party.charID != p.c.id) {
                    Service.chatNPC(p, (short)npcid, "Con không phải nhóm trưởng, không thể đặt cược");
                    return;
                }

                Service.sendInputDialog(p, (short)3, "Đặt tiền cược (lớn hơn 1000 xu và chia hết cho 50)");
                break;
            case 2:
                Server.manager.sendTB(p, "Hướng dẫn", "- Mời đối thủ vào lôi đài\n\n- Đặt tiền cược (Lớn hơn 1000 xu và chia hết cho 50)\n\n- Khi cả 2 đã đặt tiền cược, và số tiền phải thống nhất bằng nhau thì trận so tài mới có thể bắt đầu.\n\n- Khi đã đặt tiền cược, nhưng thoát, mất kết nối hoặc thua cuộc, thì người chơi còn lại sẽ giành chiến thắng\n\n- Số tiền thắng sẽ nhận được sẽ bị trừ phí 5%\n\n- Nếu hết thời gian mà chưa có ai giành chiến thắng thì cuộc so tài sẽ tính hoà, và mỗi người sẽ nhận lại số tiền của mình với mức phí bị trừ 1%");
                break;
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }

    }

    public static void npcAdmin(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 0: {
                if (p.c.isNhanban) {
                    Service.chatNPC(p, (short) npcid, Language.NOT_FOR_PHAN_THAN);
                    return;
                }

                if (p.c.isDiemDanh == 0) {
                    if(p.status == 1) {
                        p.upluongMessage(250L);
                        p.c.luongTN += 250;
                    } else {
                        p.upluongMessage(500L);
                    }
                    p.c.isDiemDanh = 1;
                    Service.chatNPC(p, (short) npcid, "Điểm danh thành công, con nhận được 500 lượng.");
                } else {
                    Service.chatNPC(p, (short) npcid, "Hôm nay con đã điểm danh rồi, hãy quay lại vào Ngay hôm sau nha!");
                }
                break;
            }
            case 1: {
                if (p.c.isNhanban) {
                    Service.chatNPC(p, (short) npcid, Language.NOT_FOR_PHAN_THAN);
                    return;
                }

                if (p.c.isQuaHangDong == 1) {
                    Service.chatNPC(p, (short) npcid, "Con đã nhận thưởng hôm nay rồi!");
                    return;
                }

                if (p.c.countHangDong >= 2) {
                    if(p.status == 1) {
                        p.upluongMessage(750L);
                        p.c.luongTN += 750;
                    } else {
                        p.upluongMessage(1500L);
                    }
                    p.c.isQuaHangDong = 1;
                    Service.chatNPC(p, (short) npcid, "Nhận quà hoàn thành hang động thành công, con nhận được 1500 lượng.");
                } else if (p.c.countHangDong < 2) {
                    Service.chatNPC(p, (short) npcid, "Con chưa hoàn thành đủ 2 lần đi hang động, hãy hoàn thành đủ 2 lần và quay lại gặp ta đã nhận thường");
                }
                break;
            }
            case 2: {
                if (p.c.isNhanban) {
                    Service.chatNPC(p, (short) npcid, Language.NOT_FOR_PHAN_THAN);
                    return;
                }

                if (p.c.getBagNull() < 6) {
                    Service.chatNPC(p, (short) npcid, Language.NOT_ENOUGH_BAG);
                    return;
                }

                if (p.c.level == 1) {
                    p.updateExp(Level.getMaxExp(10));
                    if(p.status == 1) {
                        p.upluongMessage(10000L);
                        p.c.upxuMessage(25000000L);
                        p.c.upyenMessage(25000000L);
                        p.c.luongTN +=  10000;
                        p.c.yenTN += 50000000;
                        p.c.xuTN += 50000000;
                        p.c.addItemBag(false, ItemTemplate.itemDefault(222, true));
                        p.c.addItemBag(false, ItemTemplate.itemDefault(539, true));
                        p.c.addItemBag(false, ItemTemplate.itemDefault(383, false));
                    } else {
                        p.upluongMessage(20000L);
                        p.c.upxuMessage(100000000L);
                        p.c.upyenMessage(100000000L);
                        p.c.addItemBag(false, ItemTemplate.itemDefault(222, true));
                        p.c.addItemBag(false, ItemTemplate.itemDefault(539, true));
                        p.c.addItemBag(false, ItemTemplate.itemDefault(539, true));
                        p.c.addItemBag(false, ItemTemplate.itemDefault(539, true));
                        p.c.addItemBag(false, ItemTemplate.itemDefault(383, false));
                        p.c.addItemBag(false, ItemTemplate.itemDefault(383, false));
                    }
                    Service.chatNPC(p, (short) npcid, "Con đã nhận quà tân thủ thành công, chúc con trải nghiệm game vui vẻ.");
                } else {
                    Service.chatNPC(p, (short) npcid, "Con đã nhận quà tân thủ trước đó rồi, không thể nhận lại lần nữa!");
                }
                break;
            }
            case 3: {
                if (p.c.isNhanban) {
                    Service.chatNPC(p, (short) npcid, Language.NOT_FOR_PHAN_THAN);
                    return;
                }
                if(p.c.level == 1) {
                    p.conn.sendMessageLog("Không thể thực hiện thao tác này..");
                    return;
                }
                if(p.c.get().exptype == 1) {
                    p.c.get().exptype = 0;
                    p.sendAddchatYellow("Đã tắt nhận exp.");
                } else {
                    p.c.get().exptype = 1;
                    p.sendAddchatYellow("Đã bật nhận exp.");
                }
                break;
            }
            case 4: {
                if (p.c.isNhanban) {
                    Service.chatNPC(p, (short) npcid, Language.NOT_FOR_PHAN_THAN);
                    return;
                }
                if(p.status == 1) {
                    Service.chatNPC(p, (short) npcid, "Tài khoản của con chưa được nâng cấp lên CHÍNH THỨC, không thể nhận lại phần thưởng.");
                    return;
                }
                switch (b3) {
                    case 0: {
                        if(p.c.yenTN <= 0) {
                            Service.chatNPC(p, (short) npcid, "Con không có yên lưu trữ để nhận lại.");
                            return;
                        }
                        p.c.upyenMessage(p.c.yenTN);
                        p.c.yenTN = 0;
                        break;
                    }
                    case 1: {
                        if(p.c.xuTN <= 0) {
                            Service.chatNPC(p, (short) npcid, "Con không có xu lưu trữ để nhận lại.");
                            return;
                        }
                        p.c.upxuMessage(p.c.xuTN);
                        p.c.xuTN = 0;
                        break;
                    }
                    case 2: {
                        if(p.c.luongTN <= 0) {
                            Service.chatNPC(p, (short) npcid, "Con không có lượng lưu trữ để nhận lại.");
                            return;
                        }
                        p.upluongMessage(p.c.luongTN);
                        p.c.luongTN = 0;
                        break;
                    }
                    case 3: {
                        if(p.c.expTN <= 0) {
                            Service.chatNPC(p, (short) npcid, "Con không có kinh nghiệm lưu trữ để nhận lại.");
                            return;
                        }
                        p.updateExp(p.c.expTN);
                        p.c.expTN = 0;
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
            }
            case 5: {
                if(p.c.isNhanban) {
                    p.conn.sendMessageLog(Language.NOT_FOR_PHAN_THAN);
                    return;
                }
                if(p.c.clone == null) {
                    Service.chatNPC(p, (short) npcid, "Con không có phân thân để sử dụng chức năng này.");
                    return;
                }
                Service.startYesNoDlg(p, (byte) 5, "Sau khi lựa chọn, tất cả dữ liệu như trang bị, thú cưỡi, điểm,... của phân thân sẽ bị reset về ban đầu. Hãy chắc chắn rằng bạn đã tháo toàn bộ trang bị của phân thân và xác nhận muốn reset.");
                break;
            }
            case 6: {
                Server.manager.sendTB(p, "Hướng dẫn", "- Vừa vào chơi, hãy đến chỗ ta nhận quà tân thủ bao gồm: 100tr xu, 20k lượng, 100tr yên \n- Mỗi Ngay con được điềm danh hàng Ngay 1 lần và nhận 500 lượng \n- Nếu mỗi Ngay hoàn thành hang động đủ 2 lần con hãy đến chỗ ta và Nhận quà hang động để nhận 1000 lượng\n\n** Lưu ý, nếu là tài khoản trải nghiệm, con chỉ có thể nhận được 1 nửa phần thường từ ta.");
                break;
            }
  case 7:
                if (p.c.isNhanban) {
                        Service.chatNPC(p, (short) npcid, "Không Dành cho Thứ thân");
                        return;
                    } else if (p.luong < 100000) {
                       Service.chatNPC(p, (short) npcid, "Tẩy phái cần 100.000 lượng con nhé!");
                        return;
                    }
                    p.c.nclass = 0;
                    p.c.skill.clear();
                    p.upluongMessage(-100000L);
                    Manager.chatKTG("Chúc Mừng người chơi " + p.c.name +  " Đã tẩy phái thành công đã có thể tới trường và nhập học lại");
                    break;
                            case 8: {
                                if (p.c.isHuman) {
                        Service.chatNPC(p, (short) npcid, "Không Dành cho Chủ thân");
                        return;
                    } else if (p.luong < 100000) {
                        Service.chatNPC(p, (short) npcid, "Tẩy phái cần 100.000 lượng con nhé!");
                        return;
                    }
                    p.c.clone.nclass = 0;
                    p.c.clone.skill.clear();
                    p.upluongMessage(-100000L);
                    Manager.chatKTG("Chúc Mừng phân thân người chơi " + p.c.name +  " Đã tẩy phái thành công đã có thể tới trường và nhập học lại");}
                                case 9:
                if (p.c.isNhanban) {
                    Service.chatNPC(p, (short)npcid, Language.NOT_FOR_PHAN_THAN);
                    return;
                }

                p.requestItem(8);
                break;
            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }
    }

    public static void npcRikudou_ChienTruong(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch(menuId) {
            case 0: {
                p.c.typepk = 0;
                Service.ChangTypePkId(p.c, (byte)0);
                p.c.tileMap.leave(p);
                p.restCave();
                p.changeMap(p.c.mapLTD);
                break;
            }
            case 1: {
                Service.evaluateCT(p.c);
                break;
            }

            default: {
                Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                break;
            }
        }
    }

    public static void npcKagai_GTC(Player p, byte npcid, byte menuId, byte b3) throws IOException {
        switch (p.c.mapid) {
            case 117: {
                switch(menuId) {
                    case 0: {
                        p.c.typepk = 0;
                        Service.ChangTypePkId(p.c, (byte)0);
                        p.c.tileMap.leave(p);
                        p.restCave();
                        p.changeMap(p.c.mapLTD);
                        break;
                    }
                    case 1: {
                        Service.chatNPC(p, (short) npcid, "Đặt cược");
                        Service.sendInputDialog(p, (short)8, "Đặt tiền cược (lớn hơn 1000 xu và chia hết cho 50)");
                        break;
                    }

                    default: {
                        Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                        break;
                    }
                }
                break;
            }
            case 118:
            case 119: {
                switch(menuId) {
                    case 0: {
                        p.c.typepk = 0;
                        Service.ChangTypePkId(p.c, (byte)0);
                        p.c.tileMap.leave(p);
                        p.restCave();
                        p.changeMap(p.c.mapLTD);
                        break;
                    }
                    case 1: {
                        Service.evaluateCT(p.c);
                        break;
                    }
                    default: {
                        Service.chatNPC(p, (short) npcid, "Chức năng này đang cập nhật!");
                        break;
                    }
                }
                break;
            }
        }
    }
}
