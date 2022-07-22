package com.hoiuc.server;

import com.hoiuc.assembly.*;
import com.hoiuc.io.Message;
import com.hoiuc.io.SQLManager;
import com.hoiuc.io.Util;
import com.hoiuc.stream.*;
import com.hoiuc.template.ItemTemplate;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.Date;

public class Draw {
    public static void Draw(Player p, Message m) {
        try {
            short menuId = m.reader().readShort();
            String str = m.reader().readUTF();
            if(!str.equals("")) {
                Util.Debug("menuId " + menuId + " str " + str);
                byte b = -1;
                if(m.reader().available()  > 0) {
                    try {
                        b = m.reader().readByte();
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                m.cleanup();
                switch (menuId) {
                    case 1: {
                        if (p.c.quantityItemyTotal(279) <= 0) {
                            break;
                        }
                        Char c = Client.gI().getNinja(str);

                        if (c != null && c.tileMap != null && c.tileMap.map != null &&  !c.tileMap.map.LangCo() && c.tileMap.map.getXHD() == -1 && c.mapid != 111 && c.mapid != 133 && !c.tileMap.map.mapChienTruong() && !c.tileMap.map.mapLDGT() && !c.tileMap.map.mapBossTuanLoc() && !c.tileMap.map.mapGTC()) {
                            if(p.c.level < 60 && c.tileMap.map.VDMQ()) {
                                p.conn.sendMessageLog("Trình độ của bạn chưa đủ để di chuyển tới đây");
                                return;
                            }
                            if(p.c.tileMap.map.mapGTC() || p.c.tileMap.map.mapChienTruong() || p.c.tileMap.map.id == 111) {
                                p.c.typepk = 0;
                                Service.ChangTypePkId(p.c, (byte)0);
                            }
                            p.c.tileMap.leave(p);
                            p.c.get().x = c.get().x;
                            p.c.get().y = c.get().y;
                            c.tileMap.Enter(p);
                            return;
                        }
                        p.sendAddchatYellow("Vị trí người này không thể đi tới");
                        break;
                    }
                    case 2: {
                        Char temp = Client.gI().getNinja(str);
                        if(temp != null) {
                            Char friendNinja = p.c.tileMap.getNinja(temp.id);
                            if(friendNinja != null && friendNinja.id == p.c.id) {
                                Service.chatNPC(p, (short) 0, Language.NAME_LOI_DAI);
                            } else if (friendNinja != null && friendNinja.id != p.c.id) {
                                p.sendRequestBattleToAnother(friendNinja, p.c);
                                Service.chatNPC(p, (short) 0, Language.SEND_MESS_LOI_DAI);
                            } else {
                                Service.chatNPC(p, (short) 0, Language.NOT_IN_ZONE);
                            }
                        } else {
                            Service.chatNPC(p, (short) 0, "Người chơi này không ở trong cùng khu với con hoặc không tồn tại, ta không thể gửi lời mời!");
                        }
                        break;
                    }
                    case 3: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericLong(str) || check.equals("") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)37, "Giá trị tiền cược nhập vào không đúng");
                            break;
                        }
                        long tienCuoc = Long.parseLong(str);
                        if(tienCuoc > p.c.xu || p.c.xu < 1000) {
                            Service.chatNPC(p, (short)37, "Con không đủ xu để đặt cược");
                            break;
                        }
                        if(tienCuoc < 1000 || tienCuoc%50!=0 ) {
                            Service.chatNPC(p, (short)37, "Xu cược phải lớn hơn 1000 xu và chia hết cho 50");
                            break;
                        }
                        Dun dun = null;
                        if(p.c.dunId != -1) {
                            if (Dun.duns.containsKey(p.c.dunId)) {
                                dun = Dun.duns.get(p.c.dunId);
                            }
                        }
                        if(dun != null) {
                            if(dun.c1.id == p.c.id) {
                                if(dun.tienCuocTeam2 != 0 && dun.tienCuocTeam2 != tienCuoc) {
                                    Service.chatNPC(p, (short)37, "Đối thủ của con đã đặt cược " + Util.getFormatNumber(dun.tienCuocTeam2) + " xu con hãy đặt lại đi!");
                                    return;
                                }
                                if(dun.tienCuocTeam1 != 0) {
                                    Service.chatNPC(p, (short)37, "Con đã đặt cược trước đó rồi.");
                                    return;
                                }

                                dun.tienCuocTeam1 = tienCuoc;
                                p.c.upxuMessage(-tienCuoc);
                                Service.chatNPC(p, (short)37, "Con đã đặt cược " + dun.tienCuocTeam1 + " xu");
                                dun.c2.p.sendAddchatYellow("Người chơi " + dun.c1.name + " đã được cược "+ Util.getFormatNumber(dun.tienCuocTeam1) + " xu.");

                            } else if(dun.c2.id == p.c.id) {
                                if(dun.tienCuocTeam1 != 0 && dun.tienCuocTeam1 != tienCuoc) {
                                    Service.chatNPC(p, (short)37, "Đối thủ của con đã đặt cược " + Util.getFormatNumber(dun.tienCuocTeam1) + " xu con hãy đặt lại đi!");
                                    return;
                                }
                                if(dun.tienCuocTeam2 != 0) {
                                    Service.chatNPC(p, (short)37, "Con đã đặt cược trước đó rồi.");
                                    return;
                                }

                                dun.tienCuocTeam2 = tienCuoc;
                                p.c.upxuMessage(-tienCuoc);
                                Service.chatNPC(p, (short)37, "Con đã đặt cược " + Util.getFormatNumber(dun.tienCuocTeam2) + " xu");
                                dun.c1.p.sendAddchatYellow("Người chơi " + dun.c2.name + " đã được cược "+ Util.getFormatNumber(dun.tienCuocTeam2) + " xu.");
                            }

                            if(dun.tienCuocTeam1 != 0 && dun.tienCuocTeam2 != 0 && dun.tienCuocTeam1 == dun.tienCuocTeam2 && dun.team1.size() > 0 && dun.team2.size() > 0) {
                                if(dun.tienCuocTeam1 >= 1000000L) {
                                    Manager.serverChat("Server: ", "Người chơi " + dun.c1.name + " ("+dun.c1.level+")"
                                            + " đang thách đấu với " + dun.c2.name + " ("+dun.c2.level+"): " + Util.getFormatNumber(dun.tienCuocTeam1) +" xu tại lôi đài, hãy mau mau đến xem và cổ vũ.");
                                }
                                dun.startDun();
                            }
                        }
                        else {
                            return;
                        }
                        break;
                    }
                    
                    //Làm bánh chưng
                    case 110: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)33, "Giá trị tiền cược nhập vào không đúng");
                            break;
                        }
                        
                        long soluong = Integer.parseInt(str);
                        if (soluong <= 0) {
                        p.lockAcc();
                        }
                        
                        if (p.c.quantityItemyTotal(638) >= 3*soluong && p.c.quantityItemyTotal(639) >= 5*soluong && p.c.quantityItemyTotal(640) >= 1*soluong && p.c.quantityItemyTotal(641) >= 3*soluong && p.c.quantityItemyTotal(642) >= 2*soluong ) {
                            if(p.c.yen < 50000*soluong ) {
                                p.conn.sendMessageLog("Không đủ yên để làm bánh");
                                return;
                            }
                            if(p.c.xu  < 50000*soluong ) {
                                p.conn.sendMessageLog("Không đủ xu để làm bánh");
                                return;
                            }
                            if (p.c.getBagNull() == 0) {
                                p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                            } else {
                                p.c.removeItemBags(638, (int)(3*soluong));
                                p.c.removeItemBags(639, (int)(5*soluong));
                                p.c.removeItemBags(640, (int)(1*soluong));
                                p.c.removeItemBags(641, (int)(3*soluong));
                                p.c.removeItemBags(642, (int)(2*soluong));
                                p.c.upyenMessage(-(50000*soluong));
                                p.c.upxuMessage(-(50000*soluong));
                                Item it = ItemTemplate.itemDefault(643);
                                it.quantity = (int)(1*soluong);
                                p.c.addItemBag(true, it);
                            }
                            return;
                        } else {
                            Service.chatNPC(p, (short) 33, "Hành trang của con không có đủ nguyên liệu");
                        }
                        break;
                    }
                    //Làm bánh tét
                    case 111: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)33, "Giá trị tiền cược nhập vào không đúng");
                            break;
                        }
                        long soluong = Integer.parseInt(str);
                        if (soluong <= 0) {
                        p.lockAcc();
                        }
                        if (p.c.quantityItemyTotal(638) >= 2*soluong && p.c.quantityItemyTotal(639) >= 4*soluong && p.c.quantityItemyTotal(641) >= 2*soluong && p.c.quantityItemyTotal(642) >= 4*soluong) {
                            if(p.c.yen < 40000*soluong ) {
                                p.conn.sendMessageLog("Không đủ yên để làm bánh");
                                return;
                            }
                            if(p.c.xu  < 40000*soluong ) {
                                p.conn.sendMessageLog("Không đủ xu để làm bánh");
                                return;
                            }
                            if (p.c.getBagNull() == 0) {
                                p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                            } else {
                                p.c.removeItemBags(638, (int)(2*soluong));
                                p.c.removeItemBags(639, (int)(4*soluong));
                                p.c.removeItemBags(641, (int)(2*soluong));
                                p.c.removeItemBags(642, (int)(4*soluong));
                                p.c.upyenMessage(-(40000*soluong));
                                p.c.upxuMessage(-(40000*soluong));
                                Item it = ItemTemplate.itemDefault(644);
                                it.quantity = (int)(1*soluong);
                                p.c.addItemBag(true, it);
                            }
                            return;
                        } else {
                            Service.chatNPC(p, (short) 33, "Hành trang của con không có đủ nguyên liệu");
                        }
                        break;
                    }
                    //pháo
                    case 112: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)33, "Giá trị tiền cược nhập vào không đúng");
                            break;
                        }
                        long soluong = Integer.parseInt(str);
                        if (soluong <= 0) {
                        p.lockAcc();
                        }
                        if (p.c.quantityItemyTotal(674) >= 10*soluong){
                            if(p.c.yen < 30000*soluong ) {
                                p.conn.sendMessageLog("Không đủ yên để làm bánh");
                                return;
                            }
                            if(p.c.xu  < 30000*soluong ) {
                                p.conn.sendMessageLog("Không đủ xu để làm bánh");
                                return;
                            }
                            if (p.c.getBagNull() == 0) {
                                p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                            } else {
                                p.c.removeItemBags(674, (int)(10*soluong));
                                 p.c.upyenMessage(-(30000*soluong));
                                 p.c.upxuMessage(-(30000*soluong));
                                Item it = ItemTemplate.itemDefault(675);
                                it.quantity = (int)(1*soluong);
                                p.c.addItemBag(true, it);
                            }
                            return;
                        } else {
                            Service.chatNPC(p, (short) 33, "Hành trang của con không có đủ nguyên liệu");
                        }
                        break;
                    }
                    
                    case 113:
                    p.nameUS = str;
                    Char a10 = Client.gI().getNinja(str);
                    if(a10 != null && a10.id == p.c.id) {
                        p.conn.sendMessageLog("Không thể lì xì cho bản thân.");
                        return;
                    }
                    if (a10 != null ) {
                        p.sendDo();
                    break;
                    } else {
                        p.sendAddchatYellow("Nhân vật này không tồn tại hoặc không online.");
                    }
                    break;
                    
                    //thiệp chúc thường
                    case 114:
                    p.nameUS = str;
                    Char a12 = Client.gI().getNinja(str);
                    
                    if(a12 != null && a12.id == p.c.id) {
                        p.conn.sendMessageLog("Không thể chúc chính mình,đừng thủ dâm tinh thần như thế chứ.");
                        return;
                    }
                    if (a12 != null) {
                        Service.sendInputDialog(p, (short) 116, "Nhập lời nhắn:");
                    } else {
                        p.sendAddchatYellow("Nhân vật này không tồn tại hoặc không online.");
                    }
                    break;
                    
                    //thiệp chúc vip
                    case 115:
                    p.nameUS = str;
                    Char a13 = Client.gI().getNinja(str);
                    if(a13 != null && a13.id == p.c.id) {
                        p.conn.sendMessageLog("Không thể chúc chính mình,đừng thủ dâm tinh thần như thế chứ.");
                        return;
                    }
                    if (a13 != null) {
                       Service.sendInputDialog(p, (short) 117, "Nhập lời nhắn:");
                    } else {
                        p.sendAddchatYellow("Nhân vật này không tồn tại hoặc không online.");
                    }
                    break;
                    
                    //loi nhan thuong
                    case 116:
                    p.messTB = str;
                    p.sendTB();
                    break; 
                    
                    //loi nhan vip
                    case 117:
                    p.messTB = str;
                    p.sendTB2x();
                    break; 

                    //gift code
                    case 4: {
                        String check = str.replaceAll("\\s+", "");
                        if(check.equals("")){
                            p.conn.sendMessageLog("Mã Gift code nhập vào không hợp lệ.");
                            break;
                        }
                        check = check.toUpperCase();
                        try {
                            synchronized (Server.LOCK_MYSQL) {
                                ResultSet red = SQLManager.stat.executeQuery("SELECT * FROM `gift_code` WHERE `code` LIKE '" + check + "';");
                                if (red != null && red.first()) {
                                    int id = red.getInt("id");
                                    String code = red.getString("code");
                                    JSONArray jar = (JSONArray) JSONValue.parse(red.getString("item_id"));
                                    if(p.c.getBagNull() < jar.size()) {
                                        p.conn.sendMessageLog(Language.NOT_ENOUGH_BAG);
                                        break;
                                    }
                                    int j;
                                    int[] itemId = new int[jar.size()];
                                    for (j = 0; j < jar.size(); j++) {
                                        itemId[j] = Integer.parseInt(jar.get(j).toString());
                                    }
                                    jar = (JSONArray) JSONValue.parse(red.getString("item_quantity"));
                                    long[] itemQuantity = new long[jar.size()];
                                    for (j = 0; j < jar.size(); j++) {
                                        itemQuantity[j] = Long.parseLong(jar.get(j).toString());
                                    }
                                    jar = (JSONArray) JSONValue.parse(red.getString("item_isLock"));
                                    byte[] itemIsLock = new byte[jar.size()];
                                    for (j = 0; j < jar.size(); j++) {
                                        itemIsLock[j] = Byte.parseByte(jar.get(j).toString());
                                    }
                                    jar = (JSONArray) JSONValue.parse(red.getString("item_expires"));
                                    long[] itemExpires = new long[jar.size()];
                                    for (j = 0; j < jar.size(); j++) {
                                        itemExpires[j] = Long.parseLong(jar.get(j).toString());
                                    }

                                    int isPlayer = red.getInt("isPlayer");
                                    int isTime = red.getInt("isTime");
                                    if(isPlayer == 1) {
                                        jar = (JSONArray) JSONValue.parse(red.getString("player"));
                                        boolean checkUser = false;
                                        for (j = 0; j < jar.size(); j++) {
                                            if(jar.get(j).toString().equals(p.username)){
                                                checkUser = true;
                                                break;
                                            }
                                        }
                                        if(!checkUser) {
                                            p.conn.sendMessageLog("Bạn không thể sử dụng mã Gift Code này.");
                                            red.close();
                                            break;
                                        }
                                    }
                                    if(isTime == 1) {
                                        if(Date.from(Instant.now()).compareTo(Util.getDate(red.getString("time"))) > 0) {
                                            p.conn.sendMessageLog("Mã Gift code này đã hết hạn sử dụng.");
                                            red.close();
                                            break;
                                        }
                                    }
                                    red.close();
                                    red = SQLManager.stat.executeQuery("SELECT * FROM `history_gift` WHERE `player_id` = "+p.id+" AND `code` = '"+code+"';");
                                    if(red != null && red.first()) {
                                        p.conn.sendMessageLog("Bạn đã sử dụng mã Gift code này rồi.");
                                    }
                                    else {
                                        if(itemId.length == itemQuantity.length) {
                                            ItemTemplate data2;
                                            int i;
                                            for(i = 0; i < itemId.length; i++) {
                                                if(itemId[i] == -3) {
                                                    p.c.upyenMessage(itemQuantity[i]);
                                                } else if(itemId[i] == -2) {
                                                    p.c.upxuMessage(itemQuantity[i]);
                                                } else if(itemId[i] == -1) {
                                                    p.upluongMessage(itemQuantity[i]);
                                                } else {
                                                    data2 = ItemTemplate.ItemTemplateId(itemId[i]);
                                                    if(data2 != null) {
                                                        Item itemup;
                                                        if (data2.type < 10) {
                                                            if (data2.type == 1) {
                                                                itemup = ItemTemplate.itemDefault(itemId[i]);
                                                                itemup.sys = GameSrc.SysClass(data2.nclass);
                                                            } else {
                                                                byte sys = (byte) Util.nextInt(1, 3);
                                                                itemup = ItemTemplate.itemDefault(itemId[i], sys);
                                                            }
                                                        } else {
                                                            itemup = ItemTemplate.itemDefault(itemId[i]);
                                                        }
                                                        itemup.quantity = (int)itemQuantity[i];
                                                        if(itemIsLock[i] == 0) {
                                                            itemup.isLock = false;
                                                        } else {
                                                            itemup.isLock = true;
                                                        }
                                                        if(itemExpires[i] != -1) {
                                                            itemup.isExpires = true;
                                                            itemup.expires = System.currentTimeMillis() + itemExpires[i];
                                                        } else {
                                                            itemup.isExpires = false;
                                                        }
                                                        p.c.addItemBag(true, itemup);
                                                    }
                                                }
                                            }
                                            String sqlSET = "("+p.id+", '" +code+ "', '"+Util.toDateString(Date.from(Instant.now()))+"', '"+Util.toDateString(Date.from(Instant.now()))+"', '"+Util.toDateString(Date.from(Instant.now()))+"');";
                                            SQLManager.stat.executeUpdate("INSERT INTO `history_gift` (`player_id`,`code`,`time`, `created_at`, `updated_at`) VALUES " + sqlSET);
                                        }
                                        else {
                                            p.conn.sendMessageLog("Lỗi xác nhận mã Gift code. Hãy liên hệ Admin để biết thêm chi tiết.");
                                        }
                                    }
                                    jar.clear();
                                    red.close();
                                    break;
                                }
                                else {
                                    p.conn.sendMessageLog("Mã Gift code này đã được sử dụng hoặc không tồn tại.");
                                    red.close();
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }

                    //Mời gia tộc chiến
                    case 5: {
                        ClanManager temp = ClanManager.getClanName(str);
                        ClanManager temp2 = ClanManager.getClanName(p.c.clan.clanName);
                        if(temp != null) {
                            String tocTruong = temp.getmain_name();
                            Char _charTT = Client.gI().getNinja(tocTruong);
                            if(_charTT != null && _charTT.id == p.c.id) {
                                Service.chatNPC(p, (short) 32, "Ngươi muốn thách đấu gia tộc của chính mình à.");
                            } else if (_charTT != null && _charTT.id != p.c.id) {
                                if(temp.gtcID != -1 && temp.gtcClanName != null) {
                                    Service.chatNPC(p, (short) 32, "Gia tộc này đang có lời mời từ gia tộc khác");
                                    return;
                                }
                                Service.startYesNoDlg(_charTT.p, (byte) 4, "Gia tộc "+p.c.clan.clanName+" muốn thách đấu với gia tộc của bạn. Bạn có đồng ý?");
                                GiaTocChien giaTocChien = new GiaTocChien();
                                temp.gtcID = giaTocChien.gtcID;
                                temp.gtcClanName = p.c.clan.clanName;
                                temp2.gtcID = giaTocChien.gtcID;
                                temp2.gtcClanName = str;
                                Service.chatNPC(p, (short) 32, "Ta đã gửi lời mời thách đấu tới gia tộc " + str);
                            } else {
                                Service.chatNPC(p, (short) 32, "Tộc trưởng gia tộc đối phương không online hoặc không tồn tại. Không thể gửi lời mời.");
                            }
                        } else {
                            Service.chatNPC(p, (short) 32, "Gia tộc này không tồn tại, ta không thể gửi lời mời!");
                        }
                        break;
                    }

                    //Làm bánh chocolate
                    case 6: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)33, "Giá trị tiền cược nhập vào không đúng");
                            break;
                        }
                        long soluong = Integer.parseInt(str);
                        if (p.c.quantityItemyTotal(666) >= 2*soluong && p.c.quantityItemyTotal(667) >= 2*soluong && p.c.quantityItemyTotal(668) >= 3*soluong && p.c.quantityItemyTotal(669) >= 1*soluong) {
                            if(p.c.yen < 5000*soluong ) {
                                p.conn.sendMessageLog("Không đủ yên để làm bánh");
                                return;
                            }
                            if (p.c.getBagNull() == 0) {
                                p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                            } else {
                                p.c.removeItemBags(666, (int)(2*soluong));
                                p.c.removeItemBags(667, (int)(2*soluong));
                                p.c.removeItemBags(668, (int)(3*soluong));
                                p.c.removeItemBags(669, (int)(1*soluong));
                                p.c.upyenMessage(-(5000*soluong));
                                Item it = ItemTemplate.itemDefault(671);
                                it.quantity = (int)(1*soluong);
                                p.c.addItemBag(true, it);
                            }
                            return;
                        } else {
                            Service.chatNPC(p, (short) 33, "Hành trang của con không có đủ nguyên liệu");
                        }
                        break;
                    }
                    //Làm bánh dâu tây
                    case 7: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)33, "Giá trị tiền cược nhập vào không đúng");
                            break;
                        }
                        long soluong = Integer.parseInt(str);
                        if (p.c.quantityItemyTotal(666) >= 3*soluong && p.c.quantityItemyTotal(667) >= 3*soluong && p.c.quantityItemyTotal(668) >= 4*soluong && p.c.quantityItemyTotal(670) >= 1*soluong) {
                            if(p.c.yen < 10000*soluong ) {
                                p.conn.sendMessageLog("Không đủ yên để làm bánh");
                                return;
                            }
                            if (p.c.getBagNull() == 0) {
                                p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                            } else {
                                p.c.removeItemBags(666, (int)(3*soluong));
                                p.c.removeItemBags(667, (int)(3*soluong));
                                p.c.removeItemBags(668, (int)(4*soluong));
                                p.c.removeItemBags(670, (int)(1*soluong));
                                p.c.upyenMessage(-(10000*soluong));
                                Item it = ItemTemplate.itemDefault(672);
                                it.quantity = (int)(1*soluong);
                                p.c.addItemBag(true, it);
                            }
                            return;
                        } else {
                            Service.chatNPC(p, (short) 33, "Hành trang của con không có đủ nguyên liệu");
                        }
                        break;
                    }

                    //Đặt cược gia tộc chiến
                    case 8: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericLong(str) || check.equals("") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)37, "Giá trị tiền cược nhập vào không đúng");
                            break;
                        }
                        long tienCuoc = Long.parseLong(str);
                        ClanManager clanManager = ClanManager.getClanName(p.c.clan.clanName);
                        if(tienCuoc > clanManager.coin || clanManager.coin < 1000) {
                            Service.chatNPC(p, (short)40, "Gia tộc của con không đủ ngân sách để đặt cược.");
                            break;
                        }
                        if(tienCuoc < 1000 || tienCuoc%50!=0 ) {
                            Service.chatNPC(p, (short)40, "Xu cược phải lớn hơn 1000 xu và chia hết cho 50");
                            break;
                        }
                        GiaTocChien gtc = null;
                        if(clanManager.gtcID != -1) {
                            if (GiaTocChien.gtcs.containsKey(clanManager.gtcID)) {
                                gtc = GiaTocChien.gtcs.get(clanManager.gtcID);
                            }
                        }
                        if(gtc != null) {
                            if(gtc.clan1.id == clanManager.id) {
                                if(gtc.tienCuoc2 != 0 && gtc.tienCuoc2 != tienCuoc) {
                                    Service.chatNPC(p, (short)40, "Gia tộc đối thủ của con đã đặt cược " + Util.getFormatNumber(gtc.tienCuoc2 ) + " xu con hãy đặt lại đi!");
                                    return;
                                }
                                if(gtc.tienCuoc1 != 0) {
                                    Service.chatNPC(p, (short)37, "Gia tộc của con đã đặt cược trước đó rồi.");
                                    return;
                                }

                                gtc.tienCuoc1 = tienCuoc;
                                clanManager.coin -= tienCuoc;
                                Service.chatNPC(p, (short)40, "Con đã đặt cược " + gtc.tienCuoc1 + " xu");
                                if(gtc.gt2.size() > 0) {
                                    for(int i = 0; i < gtc.gt2.size(); i++) {
                                        gtc.gt2.get(i).p.sendAddchatYellow("Gia tộc " + clanManager.name + " đã được cược "+ Util.getFormatNumber(gtc.tienCuoc1) + " xu.");
                                    }
                                }


                            } else if(gtc.clan2.id == clanManager.id) {
                                if(gtc.tienCuoc1 != 0 && gtc.tienCuoc1 != tienCuoc) {
                                    Service.chatNPC(p, (short)40, "Gia tộc đối thủ của con đã đặt cược " + Util.getFormatNumber(gtc.tienCuoc1) + " xu con hãy đặt lại đi!");
                                    return;
                                }
                                if(gtc.tienCuoc2 != 0) {
                                    Service.chatNPC(p, (short)40, "Con đã đặt cược trước đó rồi.");
                                    return;
                                }

                                gtc.tienCuoc2 = tienCuoc;
                                clanManager.coin -= tienCuoc;
                                Service.chatNPC(p, (short)40, "Con đã đặt cược " + gtc.tienCuoc2 + " xu");
                                if(gtc.gt1.size() > 0) {
                                    for(int i = 0; i < gtc.gt1.size(); i++) {
                                        gtc.gt1.get(i).p.sendAddchatYellow("Gia tộc " + clanManager.name + " đã được cược "+ Util.getFormatNumber(gtc.tienCuoc2) + " xu.");
                                    }
                                }
                            }

                            if(gtc.tienCuoc1 != 0 && gtc.tienCuoc2 != 0 && gtc.tienCuoc1 == gtc.tienCuoc2 && gtc.gt1.size() > 0 && gtc.gt2.size() > 0) {
                                gtc.invite();
                            }
                        }
                        else {
                            return;
                        }
                        break;
                    }
                    //Đổi coin => lượng
                    case 9: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("")) {
                            Service.chatNPC(p, (short)36, "Giá trị coin nhập vào không đúng");
                            break;
                        }
                        long coin = Integer.parseInt(str);
                        try {
                            ResultSet red = SQLManager.stat.executeQuery("SELECT `coin` FROM `player` WHERE `id` = "+p.id+";");
                            if (red != null && red.first()) {
                                int coinP = Integer.parseInt(red.getString("coin"));
                                if(coin <= coinP) {
                                    coinP -= coin;
                                    p.upluongMessage(coin);
                                    SQLManager.stat.executeUpdate("UPDATE `player` SET `coin`=" + coinP + " WHERE `id`=" + p.id + " LIMIT 1;");
                                } else {
                                    p.conn.sendMessageLog("Bạn không đủ coin để đổi ra lượng.");
                                }
                                p.flush();
                                red.close();
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                            p.conn.sendMessageLog("Lỗi đổi coin.");
                        }
                        break;
                    }
                    case 50: {
                        ClanManager.createClan(p, str);
                        break;
                    }
                    case 100: {
                        String num = str.replaceAll(" ", "").trim();
                        if (num.length() > 10 || !Util.checkNumInt(num) || b < 0 || b >= Server.manager.rotationluck.length) {
                            return;
                        }
                        if(!Util.isNumeric(num)) {
                            return;
                        }
                        int xujoin = Integer.parseInt(num);
                        Server.manager.rotationluck[b].joinLuck(p, xujoin);
                        break;
                    }
                    case 101: {
                        if (b < 0 || b >= Server.manager.rotationluck.length) {
                            return;
                        }
                        if(b==0 && p.c.isTaskDanhVong==1 && p.c.taskDanhVong[0] == 0 && p.c.taskDanhVong[1] < p.c.taskDanhVong[2]) {
                            p.c.taskDanhVong[1]++;
                        }
                        if(b==1 && p.c.isTaskDanhVong==1 && p.c.taskDanhVong[0] == 1 && p.c.taskDanhVong[1] < p.c.taskDanhVong[2]) {
                            p.c.taskDanhVong[1]++;
                        }
                        Server.manager.rotationluck[b].luckMessage(p);
                        break;
                    }
                    case 102: {
                        p.typemenu = 92;
                        Menu.doMenuArray(p, new String[] { "Vòng xoay vip", "Vòng xoay thường" });
                        break;
                    }
                    case 9989: {
                        if(!Util.isNumeric(str) || str.equals("")) {
                            p.conn.sendMessageLog("Giá trị nhập vào không hợp lệ");
                            return;
                        }
                        String check = str.replaceAll("\\s+", "");
                        check = str.replaceAll(" ", "").trim();
                        int nhanquatdb = Integer.parseInt(check);
                        Manager.nhanquatdb = nhanquatdb;
                        p.sendAddchatYellow("Bật trạng thái nhận quà");
                        break;
                    }
                    //Thay đổi exp
                    case 9990: {
                        if(!Util.isNumeric(str) || str.equals("")) {
                            p.conn.sendMessageLog("Giá trị nhập vào không hợp lệ");
                            return;
                        }
                        String check = str.replaceAll("\\s+", "");
                        check = str.replaceAll(" ", "").trim();
                        int expup = Integer.parseInt(check);
                        if(expup <= 0) {
                            expup = 1;
                        }
                        Manager.up_exp = expup;
                        p.sendAddchatYellow("Thay đổi tăng giá trị exp thành công");
                        break;
                    }

                    //Thong bao
                    case 9991: {
                        if(str.equals("")) {
                            p.conn.sendMessageLog("Giá trị nhập vào không hợp lệ");
                            return;
                        }
                        Manager.serverChat("Server", str);
                        p.sendAddchatYellow("Đăng thông báo thành công");
                        break;
                    }

                    //kỹ năng
                    case 9992: {
                        if(!Util.isNumeric(str) || str.equals("")) {
                            p.conn.sendMessageLog("Giá trị nhập vào không hợp lệ");
                            return;
                        }
                        String check = str.replaceAll(" ", "").trim();
                        int kynang = Integer.parseInt(check);
                        p.c.spoint += kynang;
                        p.loadSkill();
                        if(kynang >= 0) {
                            p.sendAddchatYellow("Đã tăng thêm " + kynang + " điểm kỹ năng.");
                        } else {
                            p.sendAddchatYellow("Đã giảm đi " + kynang + " điểm kỹ năng.");
                        }
                        break;
                    }

                    //tiềm năng
                    case 9993: {
                        if(!Util.isNumeric(str) || str.equals("")) {
                            p.conn.sendMessageLog("Giá trị nhập vào không hợp lệ");
                            return;
                        }
                        String check = str.replaceAll(" ", "").trim();
                        int tiemnang = Integer.parseInt(check);
                        p.c.get().ppoint += tiemnang;
                        p.loadPpoint();
                        if(tiemnang >= 0) {
                            p.sendAddchatYellow("Đã tăng thêm " + tiemnang + " điểm tiềm năng.");
                        } else {
                            p.sendAddchatYellow("Đã giảm đi " + tiemnang + " điểm tiềm năng.");
                        }
                        break;
                    }

                    //tăng level
                    case 9994: {
                        if(!Util.isNumeric(str) || str.equals("")) {
                            p.conn.sendMessageLog("Giá trị nhập vào không hợp lệ");
                            return;
                        }
                        String check = str.replaceAll(" ", "").trim();
                        int levelup = Integer.parseInt(check);
                        int oldLv = p.c.get().level;
                        p.c.get().level = 1;
                        p.c.get().exp = 0;
                        p.c.get().expdown = 0;
                        p.updateExp(Level.getMaxExp(oldLv + levelup));
                        if(p.c.get().isHuman) {
                            p.c.setXPLoadSkill(p.c.get().exp);
                        } else {
                            p.c.clone.setXPLoadSkill(p.c.get().exp);
                        }
                        p.restPpoint();
                        p.restSpoint();
                        if(levelup >= 0) {
                            p.sendAddchatYellow("Đã tăng thêm " + levelup + " cấp độ.");
                        } else {
                            p.sendAddchatYellow("Đã giảm đi " + levelup + " cấp độ.");
                        }
                        break;
                    }

                    //tăng lượng
                    case 9995: {
                        if(!Util.isNumeric(str) || str.equals("")) {
                            p.conn.sendMessageLog("Giá trị nhập vào không hợp lệ");
                            return;
                        }
                        String check = str.replaceAll(" ", "").trim();
                        int luongup = Integer.parseInt(check);
                        if(luongup>=0) {
                            p.sendAddchatYellow("Đã tăng thêm " + Util.getFormatNumber(luongup) + " lượng.");
                        } else {
                            p.sendAddchatYellow("Đã giảm đi " + Util.getFormatNumber(luongup) + " lượng.");
                        }
                        p.upluongMessage(luongup);
                        break;
                    }

                    //tăng xu
                    case 9996: {
                        if(!Util.isNumeric(str) || str.equals("")) {
                            p.conn.sendMessageLog("Giá trị nhập vào không hợp lệ");
                            return;
                        }
                        String check = str.replaceAll(" ", "").trim();
                        int xuup = Integer.parseInt(str);
                        if(xuup>=0) {
                            p.sendAddchatYellow("Đã tăng thêm " + Util.getFormatNumber(xuup) + " xu.");
                        } else {
                            p.sendAddchatYellow("Đã giảm đi " + Util.getFormatNumber(xuup) + " xu.");
                        }
                        p.c.upxuMessage(xuup);
                        break;
                    }

                    //tăng yên
                    case 9997: {
                        if(!Util.isNumeric(str) || str.equals("")) {
                            p.conn.sendMessageLog("Giá trị nhập vào không hợp lệ");
                            return;
                        }
                        String check = str.replaceAll(" ", "").trim();
                        int yenup = Integer.parseInt(check);
                        if(yenup>=0) {
                            p.sendAddchatYellow("Đã tăng thêm " + Util.getFormatNumber(yenup) + " yên.");
                        } else {
                            p.sendAddchatYellow("Đã giảm đi " + Util.getFormatNumber(yenup) + " yên.");
                        }
                        p.c.upyenMessage(yenup);
                        break;
                    }

                    //bảo trì
                    case 9998: {
                        if(!Util.isNumeric(str) || str.equals("")) {
                            p.conn.sendMessageLog("Giá trị nhập vào không hợp lệ");
                            return;
                        }
                        String check = str.replaceAll(" ", "").trim();
                        int minues = Integer.parseInt(check);
                        if( minues < 0 || minues > 10) {
                            p.conn.sendMessageLog("Giá trị nhập vào từ 0 -> 10 phút");
                            return;
                        }
                        p.sendAddchatYellow("Đã kích hoạt bảo trì Server sau " + minues + " phút.");
                        Thread t1 = new Thread(new Admin(minues, Server.gI()));
                        t1.start();
                        break;
                    }

                    //khoá tài khoản
                    case 9999: {
                        Char temp = Client.gI().getNinja(str);
                        if(temp != null) {
                            Player banPlayer = Client.gI().getPlayer(temp.p.username);
                            if(banPlayer != null && banPlayer.role != 9999) {
                                Client.gI().kickSession(banPlayer.conn);
                                try {
                                    SQLManager.stat.executeUpdate("UPDATE `player` SET `ban`=1 WHERE `id`=" + banPlayer.id + " LIMIT 1;");
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                                p.conn.sendMessageLog("Đã khoá tài khoản: " + banPlayer.username + " - nhân vật: " + temp.name);
                            } else {
                                p.conn.sendMessageLog("Tài khoản này là ADMIN hoặc không tìm thấy tài khoản này!");
                            }
                        } else {
                            p.conn.sendMessageLog("Người chơi này không tồn tại hoặc không online!");
                        }
                        temp = null;
                        break;
                    }

                    default: {
                        break;
                    }
                }
            }
            else {
                if(menuId == 102) {
                    p.typemenu = 92;
                    Menu.doMenuArray(p, new String[]{"Vòng xoay vip", "Vòng xoay thường"});
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
}
