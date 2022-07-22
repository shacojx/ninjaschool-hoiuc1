package com.hoiuc.server;

import com.hoiuc.assembly.*;
import com.hoiuc.io.Message;
import com.hoiuc.io.SQLManager;
import com.hoiuc.io.Util;
import com.hoiuc.stream.*;
import com.hoiuc.stream.thiendiabang.ThienDiaBang;
import com.hoiuc.stream.thiendiabang.ThienDiaBangTileMap;
import com.hoiuc.stream.thiendiabang.ThienDiaData;
import com.hoiuc.template.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.sql.ResultSet;
import java.util.ArrayList;

public class HandleController {

    public static void NhuongTruongNhom(Player p, Message msg) {
        try {
            if(p.conn != null) {
                if(p.c.mapid == 133 || p.c.mapid == 111) {
                    p.c.p.conn.sendMessageLog("Không thể sử dụng chức năng này ở đây");
                    return;
                }
                byte index = msg.reader().readByte();
                Char player = p.c.party.aChar.get(index);
                if (player != null && player.id != p.c.id) {
                    synchronized (p.c.party.LOCK) {
                        byte i;
                        for (i = 0; i < p.c.party.numPlayer; i = (byte)(i + 1)) {
                            if (p.c.party.aChar.get(i) != null && p.c.party.aChar.get(i).id == p.c.id) {
                                p.c.party.aChar.set(i, player);
                                p.c.party.charID = player.id;
                                break;
                            }
                        }
                        p.c.party.aChar.set(index, p.c);
                    }
                    p.c.party.refreshPlayer();
                    p.c.party.TeamMessage(player.name + " đã được lên làm trưởng nhóm.");
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void MoiRaKhoiNhom(Player p, Message msg) {
        try {
            if(p.c.mapid == 133 || p.c.mapid == 111) {
                p.c.p.conn.sendMessageLog("Không thể sử dụng chức năng này ở đây");
                return;
            }
            Char player = p.c.party.aChar.get(msg.reader().readByte());
            if (player != null && player.id != p.c.id && player.p.conn != null) {
                p.c.party.removePlayer(player.id);
                Service.ServerMessage(player, "Bạn đã bị trục xuất khỏi nhóm.");
                p.c.party.TeamMessage(player.name + " đã bị trục xuất khỏi nhóm.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(msg != null) {
                msg.cleanup();
            }
        }

    }

    public static void RoiNhom(Player p) {
        if(p.conn != null) {
            if(p.c.mapid == 133 || p.c.mapid == 111) {
                p.c.p.conn.sendMessageLog("Không thể sử dụng chức năng này ở đây");
                return;
            }
            Party party = p.c.party;
            if (party != null) {
                synchronized (party.LOCK) {
                    byte i;
                    for (i = 0; i < party.numPlayer; i = (byte)(i + 1)) {
                        if (party.aChar.get(i) != null && party.aChar.get(i).id != p.c.id)
                            Service.ServerMessage(party.aChar.get(i), p.c.name + " đã rời khỏi nhóm.");
                    }
                }
                party.removePlayer(p.c.id);
            }
        }
    }

    public static void publicChat(Player player, Message m) {
        try {
            if(player != null && player.c != null && m != null && m.reader().available() > 0) {
                String chat = m.reader().readUTF();
                m.cleanup();
                String check = chat.replaceAll("\\s+", "");
                if(chat.equals("info")) {
                    String status = "";
                    if(player.status == 1) {
                        status = "TÀI KHOẢN TRẢI NGHIỆM.";
                    } else if(player.status == 0) {
                        status = "TÀI KHOẢN CHÍNH THỨC.";
                    } else {
                        status = "Không xác định.";
                    }
                    int coinCheck = 0;
                    ResultSet red = SQLManager.stat.executeQuery("SELECT `coin` FROM `player` WHERE `id` = "+player.id+";");
                    if (red != null && red.first()) {
                        coinCheck = Integer.parseInt(red.getString("coin"));
                        red.close();
                    }

                    String info = "- Tài khoản: " + player.username + "\n" +
                            "- Nhân vật: " + player.c.name + "\n" +
                            "- Số coin: " + Util.getFormatNumber(coinCheck) + " Hồi Ức Coin\n" +
                            "- Số lượng: " + Util.getFormatNumber(player.luong) + " Lượng\n" +
                            "- Trạng thái tài khoản: " + status;
                    Server.manager.sendTB(player, "Thông tin", info);
                }
                else if(chat.equals("noel") && Server.manager.event == 3 && !player.c.isNhanban) {
                    String noel = "- Số lần nhận quà tại cây thông Ngay hôm nay: " + player.c.isNhanQuaNoel + "\n" +
                            "- Điểm sự kiện Noel: " + player.c.pointNoel + "\n" +
                            "- Điểm săn Boss Tuần lộc: " + player.c.pointBossTL;
                    Server.manager.sendTB(player, "Sự kiện Noel", noel);
                }
                else if(chat.equals("tp")) {
                    String tet = "- Điểm Lôi Đài Win : " +  player.c.nhanTP + "\n" + "- Tổng số Mảnh Pháo đã nhận: " + player.c.countPhao;
                    Server.manager.sendTB(player, "Sự kiện Tết", tet);
                }
                else if(chat.equals("cct")) {
                    String tet = "- Điểm Ăn Chuột : " +  player.c.pointBossChuot;
                    Server.manager.sendTB(player, "Sự kiện Tết", tet);
                }
                else if (chat.equals("nvdv") && !player.c.isNhanban) {
                    if (player.c.isTaskDanhVong == 1) {
                        String nv = "NHIỆM VỤ LẦN NÀY: \n" + String.format(DanhVongTemplate.nameNV[player.c.taskDanhVong[0]], player.c.taskDanhVong[1], player.c.taskDanhVong[2]) + "\n\n- Số lần nhận nhiệm vụ còn lại là: " + player.c.countTaskDanhVong;
                        Server.manager.sendTB(player, "Nhiệm vụ", nv);
                    } else {
                        player.sendAddchatYellow("Bạn chưa nhận nhiệm vụ danh vọng.");
                    }
                } else if (chat.equals("admin") && player.role == 9999) {
                    Service.ShowAdmin(player);
                }if (chat.equals("a")&& player.role == 9999 ) {
                    player.c.addItemBag(false,ItemTemplate.itemDefault(584) );
                   player.c.addItemBag(false, ItemTemplate.itemDefault(587));
                    player.c.addItemBag(false, ItemTemplate.itemDefault(585));
                  //  player.c.addItemBag(false, ItemTemplate.itemDefault(832));
                 //   player.c.addItemBag(false, ItemTemplate.itemDefault(833));
                 //   player.c.addItemBag(false, ItemTemplate.itemDefault(834));
                } else {
                    m = new Message(-23);
                    m.writer().writeInt(player.c.get().id);
                    m.writer().writeUTF(chat);
                    m.writer().flush();
                    if(player.c.tileMap != null && player.conn != null) {
                        player.c.tileMap.sendMessage(m);
                    } else if(player.c.tdbTileMap != null) {
                        player.c.tdbTileMap.sendMessage(m);
                    }
                    m.cleanup();
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

    public static void privateChat(Player player, Message m) {
        try {
            if(player != null && player.conn != null &&  player.c != null && m != null && m.reader().available() > 0) {
                String name = m.reader().readUTF();
                String chat = m.reader().readUTF();
                m.cleanup();
                Char n = Client.gI().getNinja(name);
                if (n != null && n.id != player.c.id) {
                    m = new Message(-22);
                    m.writer().writeUTF(player.c.name);
                    m.writer().writeUTF(chat);
                    m.writer().flush();
                    n.p.conn.sendMessage(m);
                    m.cleanup();
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

    public static void worldChat(Player p, Message m) {
        try {
            if(p != null && p.conn != null && p.c != null && m != null && m.reader().available() > 0) {
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
                        Service.serverChat(p.c.name, chat);
                    }
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

    public static void partyChat(Player player, Message m) {
        try {
            if(player != null && player.conn != null && player.c != null && player.c.party != null && m != null && m.reader().available() > 0) {
                String text = m.reader().readUTF();
                m.cleanup();
                if (player.c.get().party != null) {
                    m = new Message(-20);
                    m.writer().writeUTF(player.c.name);
                    m.writer().writeUTF(text);
                    m.writer().flush();
                    for (int i = 0; i < player.c.party.aChar.size(); i++) {
                        player.c.party.aChar.get(i).p.conn.sendMessage(m);
                    }
                    m.cleanup();
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

    public static void clanChat(Player player, Message m) {
        try {
            if(player != null && player.conn != null && player.c != null && player.c.clan != null && m != null && m.reader().available() > 0) {
                ClanManager clan = ClanManager.getClanName(player.c.clan.clanName);
                if (clan != null) {
                    clan.chat(player, m);
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

    public static void nextMap(Player p, Message m) {
        if(p != null && p.c != null && !p.c.isDie && p.conn != null) {
            p.c.tileMap.VGo(p, m);
        }
    }

    public static synchronized void pickItem(Player p, Message m) {
        try {
            if(p != null && p.c != null && p.conn != null && !p.c.isDie && m != null && m.reader().available() > 0) {
                if(p.c.tileMap != null ) {
                    p.c.tileMap.pickItem(p, m);
                } else if(p.c.tdbTileMap != null) {
                    p.c.tdbTileMap.pickItem(p, m);
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

    public static void leaveItemToCharacter(Player p, Message m) {
        try {
            if(p != null && p.conn != null && p.c != null && !p.c.isDie && m != null && m.reader().available() > 0) {
                TileMap tileMap = p.c.tileMap;
                ThienDiaBangTileMap tdbTileMap = p.c.tdbTileMap;
                byte index = m.reader().readByte();
                Item itembag = p.c.getIndexBag(index);               
                if (itembag != null && !itembag.isLock) {
                    if(tileMap != null) {
                        if (tileMap.itemMap.size() > 100) {
                            tileMap.removeItemMapMessage((tileMap.itemMap.remove(0)).itemMapId);
                        }
                    } else {
                        if(tdbTileMap != null) {
                            if (tdbTileMap.itemMap.size() > 100) {
                                tdbTileMap.removeItemMapMessage((tdbTileMap.itemMap.remove(0)).itemMapId);
                            }
                        }
                    }

                    short itemmapid = -1;
                    if(tileMap != null) {
                        tileMap.getItemMapNotId();
                    } else if(tdbTileMap != null) {
                        tdbTileMap.getItemMapNotId();
                    }
                    ItemMap item = new ItemMap();
                    item.item = itembag;
                    if(tileMap != null) {
                         p.c.removeItemBag(index);
                    } else if(tdbTileMap != null) {
                         p.c.removeItemBag(index);
                    }
                    p.c.ItemBag[index] = null;
                    m = new Message(-6);
                    m.writer().writeInt(p.c.get().id);
                    m.writer().writeShort(item.itemMapId);
                    m.writer().writeShort(item.item.id);
                    m.writer().writeShort(item.x);
                    m.writer().writeShort(item.y);
                    m.writer().flush();
                    if(tileMap != null) {
                        tileMap.sendMyMessage(p, m);
                    } else if(tdbTileMap != null) {
                        tdbTileMap.sendMyMessage(p, m);
                    }

                    Service.leaveItemBackground(p, item, index);
                }

            }
        } catch (Exception e) {
            System.out.print("loiitemroi");
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void wakeUpDieReturn(Player p) {
        if(p != null && p.conn != null && p.c != null && p.c.isDie && p.c.tileMap != null) {
            TileMap tileMap = p.c.tileMap;
            if (!tileMap.map.LangCo() && tileMap.map.dun == null) {
                if (p.luong < 1) {
                    p.conn.sendMessageLog("Bạn không đủ 1 lượng để hồi sinh!");
                } else {
                    p.c.get().isDie = false;
                    p.luongMessage(-1L);
                    p.c.get().hp = p.c.get().getMaxHP();
                    p.c.get().mp = p.c.get().getMaxMP();
                    p.liveFromDead();
                }
            } else {
                p.conn.sendMessageLog("Bạn không thể hồi sinh tại đây!");
            }
        }
    }

    public static void dieReturn(Player p) {
        if(p != null && p.c != null && p.c.isDie) {
            if(p.c.tileMap != null && p.conn != null) {
                p.c.tileMap.DieReturn(p);
            } else if(p.c.tdbTileMap != null) {
                p.c.tdbTileMap.DieReturn(p);
            }
        }
    }

    public static void move(Player p, Message m) {
        try {
            if(p != null && !p.c.get().isDie && m != null && m.reader().available() > 0) {
                if(p.c.getEffId(16) != null) {
                    p.removeEffect(16);
                }
                TileMap tileMap = p.c.tileMap;
                ThienDiaBangTileMap tdbTileMap = p.c.tdbTileMap;
                if (p.c.get().getEffId(18) == null) {
                    p.c.setTimeKickSession();
                    short x = m.reader().readShort();
                    short y = m.reader().readShort();

                    p.c.get().x = x;
                    p.c.get().y = y;
                    if (p.c.mapid == 111) {
                        p.c.get().y = p.c.get().yDun;
                        if (p.c.get().x < 107) {
                            p.c.get().x = 107;
                        }
                        if (p.c.get().x > 661) {
                            p.c.get().x = 661;
                        }
                    }
//                    if (p.c.isNhanban) {
//                        p.c.clone.x = x;
//                        p.c.clone.y = y;
//                        if (p.c.mapid == 111) {
//                            p.c.clone.y = p.c.yDun;
//                            if (p.c.clone.x < 107) {
//                                p.c.clone.x = 107;
//                            }
//                            if (p.c.clone.x > 661) {
//                                p.c.clone.x = 661;
//                            }
//                        }
//                    }
                    m.cleanup();
                    if(tileMap != null && p.conn != null) {
                        tileMap.move(p.c.get().id, p.c.get().x, p.c.get().y);
                    } else if(tdbTileMap != null) {
                        p.c.get().y = 264;
                        tdbTileMap.move(p.c.get().id, p.c.get().x, p.c.get().y);
                    }
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

    public static void fightAll(Player p, Message m) {
        try {
            if(p != null && p.c != null && !p.c.isDie && p.conn != null && m != null && m.reader().available() > 0) {
                int size = m.reader().readByte();
                if (p.c.get().ItemBody[1] != null) {
                    Skill skill = p.c.get().getSkill(p.c.get().CSkill);
                    if (skill != null) {
                        SkillOptionTemplate data = SkillTemplate.Templates(skill.id, skill.point);
                        if (skill.coolDown <= System.currentTimeMillis() && p.c.get().mp >= data.manaUse && p.c.get().getEffId(6) == null && p.c.get().getEffId(7) == null) {
                            p.c.setTimeKickSession();
                            if (size >= 0 && size <= data.maxFight) {
                                Mob[] arrMob = new Mob[size];
                                Char[] arrChar = new Char[data.maxFight];
                                try {
                                    byte i;
                                    for(i = 0; i < arrMob.length && i < data.maxFight; ++i) {
                                        arrMob[i] = p.c.tileMap.getMob(m.reader().readUnsignedByte());
                                    }

                                    for(i = 0; i < arrChar.length && i < data.maxFight; ++i) {
                                        if (m.reader().available() > 0) {
                                            arrChar[i] = p.c.tileMap.getNinja(m.reader().readInt());
                                        }
                                    }
                                } catch (Exception var9) {
                                    var9.printStackTrace();
                                }
                                p.c.tileMap.PlayerAttack(p.c, arrMob, arrChar);
                            }
                            m.cleanup();
                        }
                    }
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

    public static void useItem(Player p, Message m) {
        try {
            if(p != null && p.c != null && !p.c.isDie && p.conn != null && m != null && m.reader().available() > 0) {
                if(p.c.getEffId(16) != null) {
                    p.removeEffect(16);
                }
                byte index = m.reader().readByte();
                m.cleanup();
                Item item = p.c.getIndexBag(index);
                if (item != null) {
                    if(System.currentTimeMillis() > p.c.get().timeUseItem) {
                        UseItem.uesItem(p, item, index);
                        p.c.get().timeUseItem = System.currentTimeMillis() + 10L;
                    }
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

    public static void useItemChangeMap(Player p, Message m) {
        try {
            if(p != null && p.c != null && p.conn != null && !p.c.isDie && m != null && m.reader().available() > 0) {
                UseItem.useItemChangeMap(p, m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void buyItem(Player p, Message m) {
        try {
            if(p != null && p.c != null && p.conn != null && !p.c.isDie && m != null && m.reader().available() > 0) {
                GameSrc.buyItemStore(p, m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void sellItem(Player p, Message m) {
        try {
            if(p != null && p.c != null && p.conn != null && m != null && m.reader().available() > 0) {
                int index = m.reader().readUnsignedByte();
                int num = 1;
                if (m.reader().available() > 0) {
                    num = m.reader().readInt();
                }
                m.cleanup();
                Item item = p.c.getIndexBag(index);
                if (item != null && (!ItemTemplate.ItemTemplateId(item.id).isUpToUp || num > 0 && num <= item.quantity)) {
                    if (!ItemTemplate.ItemTemplateId(item.id).isUpToUp) {
                        num = 1;
                    }
                    if (ItemTemplate.isTypeBody(item.id) && item.upgrade > 0) {
                        p.conn.sendMessageLog("Không thể bán trang bị còn nâng cấp");
                    } else {
                        ItemTemplate data = ItemTemplate.ItemTemplateId(item.id);
                        if (data.type == 12 || item.id == 385 || item.id == 384 || ItemTemplate.isTypeNgocKham(item.id)) {
                            p.conn.sendMessageLog("Vật phẩm quý giá bạn không thể bán được");
                        } else {
                            item.quantity -= num;
                            if (item.quantity <= 0) {
                                p.c.ItemBag[index] = null;
                            }
                            p.c.upyen((long)(item.saleCoinLock * num));
                            m = new Message(14);
                            m.writer().writeByte(index);
                            m.writer().writeInt(p.c.yen);
                            m.writer().writeShort(num);
                            m.writer().flush();
                            p.conn.sendMessage(m);
                            m.cleanup();
                        }
                    }
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

    public static void itemBodyToBag(Player p, Message m) {
        try {
            if(p != null && p.c != null && p.conn != null && m != null && m.reader().available() > 0) {
                byte index = m.reader().readByte();
                byte idItemBag = p.c.getIndexBagNotItem();
                if (idItemBag == -1) {
                    p.conn.sendMessageLog(Language.NOT_ENOUGH_BAG);
                } else if (index >= 0 || index < p.c.get().ItemBody.length) {
                    Item itembody = p.c.get().ItemBody[index];
                    p.c.ItemBag[idItemBag] = itembody;
                    p.c.get().ItemBody[index] = null;
                    if (itembody != null) {
                        switch (itembody.id) {
                            case 569:
                            case 583: {
                                p.removeEffect(36);
                                break;
                            }
                            case 568: {
                                p.removeEffect(38);
                                break;
                            }
                            case 570: {
                                p.removeEffect(37);
                                break;
                            }
                            case 571: {
                                p.removeEffect(39);
                                break;
                            }
                        }

                    }
                    if (index == 10) {
                        p.mobMeMessage(0, (byte)0);
                    }
                    m = new Message(15);
                    m.writer().writeByte(p.c.get().speed());
                    m.writer().writeInt(p.c.get().getMaxHP());
                    m.writer().writeInt(p.c.get().getMaxMP());
                    m.writer().writeShort(p.c.get().eff5buffHP());
                    m.writer().writeShort(p.c.get().eff5buffMP());
                    m.writer().writeByte(index);
                    m.writer().writeByte(idItemBag);
                    m.writer().writeShort(p.c.get().partHead());
                    m.writer().flush();
                    p.conn.sendMessage(m);
                    m.cleanup();
                    if(itembody != null) {
                        if(ItemTemplate.isIdNewCaiTrang(itembody.id)) {
                            p.c.ID_HAIR = -1;
                            p.c.ID_Body = -1;
                            p.c.ID_LEG = -1;
                            p.sendInfoMeNewItem();
                        } else if(ItemTemplate.checkIdNewWP(itembody.id) != -1) {
                            p.c.ID_WEA_PONE = -1;
                            p.sendInfoMeNewItem();
                        } else if(ItemTemplate.checkIdNewMatNa(itembody.id) != -1) {
                            p.c.ID_MAT_NA = -1;
                            p.sendInfoMeNewItem();
                        } else if(ItemTemplate.checkIdNewYoroi(itembody.id) != -1) {
                            p.c.ID_PP = -1;
                            p.sendInfoMeNewItem();
                        } else if(ItemTemplate.checkIdNewBienHinh(itembody.id) != -1) {
                            p.c.ID_Bien_Hinh = -1;
                            p.sendInfoMeNewItem();
                        }
                    }
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

    public static void itemBoxToBag(Player p, Message m) {
        try {
            if(p != null && p.c != null && p.conn != null && m != null && m.reader().available() > 0) {
                byte index = m.reader().readByte();
                m.cleanup();
                Item item = null;
                switch (p.menuCaiTrang) {
                    case 0: {
                        item = p.c.getIndexBox(index);
                        if (item != null) {
                            ItemTemplate data = ItemTemplate.ItemTemplateId(item.id);
                            byte indexBag = p.c.getIndexBagid(item.id, item.isLock);
                            if (!item.isExpires && data.isUpToUp && indexBag != -1) {
                                p.c.ItemBox[index] = null;
                                Item item2 = p.c.ItemBag[indexBag];
                                item2.quantity += item.quantity;
                            } else {
                                if (p.c.getBagNull() <= 0) {
                                    p.conn.sendMessageLog(Language.NOT_ENOUGH_BOX);
                                    return;
                                }
                                indexBag = p.c.getIndexBagNotItem();
                                p.c.ItemBox[index] = null;
                                p.c.ItemBag[indexBag] = item;
                            }
                            m = new Message(16);
                            m.writer().writeByte(index);
                            m.writer().writeByte(indexBag);
                            m.writer().flush();
                            p.conn.sendMessage(m);
                            m.cleanup();
                        }
                        break;
                    }
                    case 1: {
                        item = p.c.getIndexBST(index);
                        if(p.c.ItemCaiTrang[10] == null) {
                            for(int i = 0; i <= 8; i++) {
                                if(p.c.ItemBST[i] == null) {
                                    p.sendAddchatYellow("Bạn chưa đủ điểm bộ sưu tập để sử dụng.");
                                    return;
                                }
                            }
                            p.c.ItemCaiTrang[10] = ItemTemplate.itemDefault(p.c.gender == 1 ? 711 : 714);
                            p.c.ItemCaiTrang[10].upgrade = 1;
                            p.c.ItemCaiTrang[10].isLock = true;
                            p.c.ItemCaiTrang[10].isExpires = false;
                            p.c.ItemCaiTrang[10].expires = -1L;
                            p.c.ItemCaiTrang[10].options.add(new Option(100,5));
                        } else {
                            if(16 <= p.c.ItemCaiTrang[10].upgrade) {
                                p.sendAddchatYellow("Cải trang đã đạt cấp tối đa.");
                                return;
                            }
                            int count = 0;
                            int upgradeTemp = 16;
                            for (int j = 0; j <= 8; j++) {
                                if(p.c.ItemBST[j] == null) {
                                    return;
                                }
                                if(upgradeTemp > p.c.ItemBST[j].upgrade) {
                                    upgradeTemp = p.c.ItemBST[j].upgrade;
                                }
                            }
                            if(upgradeTemp <= p.c.ItemCaiTrang[10].upgrade) {
                                p.sendAddchatYellow("Bạn chưa đủ điểm bộ sưu tập để nâng cấp cải trang.");
                                return;
                            }
                            int upgradeOld = upgradeTemp - p.c.ItemCaiTrang[10].upgrade;
                            for(int i = 0; i < upgradeOld; i++) {
                                p.c.ItemCaiTrang[10].upgrade++;
                                for(Option op : p.c.ItemCaiTrang[10].options) {
                                    if(op.id == 100) {
                                        op.param += op.param*2/10;
                                    } else if(op.id == 84 || op.id == 86) {
                                        if(p.c.ItemCaiTrang[10].upgrade > 5 && p.c.ItemCaiTrang[10].upgrade <= 10) {
                                            op.param += 5;
                                        } else if(p.c.ItemCaiTrang[10].upgrade > 10 && p.c.ItemCaiTrang[10].upgrade <= 15) {
                                            op.param += 10;
                                        } else {
                                            op.param += 15;
                                        }
                                    } else {
                                        if(p.c.ItemCaiTrang[10].upgrade > 5 && p.c.ItemCaiTrang[10].upgrade <= 10) {
                                            op.param += op.param*1/10;
                                        } else if(p.c.ItemCaiTrang[10].upgrade > 10 && p.c.ItemCaiTrang[10].upgrade <= 15) {
                                            op.param += op.param*2/10;
                                        } else {
                                            op.param += op.param*3/10;
                                        }
                                    }
                                }
                                switch (p.c.ItemCaiTrang[10].upgrade) {
                                    case 2: {
                                        p.c.ItemCaiTrang[10].options.add(new Option(0, 500));
                                        p.c.ItemCaiTrang[10].options.add(new Option(1, 500));
                                        break;
                                    }
                                    case 3: {
                                        p.c.ItemCaiTrang[10].options.add(new Option(6, 500));
                                        p.c.ItemCaiTrang[10].options.add(new Option(7, 500));
                                        break;
                                    }
                                    case 4: {
                                        p.c.ItemCaiTrang[10].options.add(new Option(87, 300));
                                        break;
                                    }
                                    case 5: {
                                        p.c.ItemCaiTrang[10].options.add(new Option(84, 20));
                                        p.c.ItemCaiTrang[10].options.add(new Option(86, 20));
                                        break;
                                    }
                                }
                            }
                        }
                        Service.openMenuCaiTrang(p);
                        break;
                    }
                    case 2: {
                        item = p.c.getIndexCaiTrang(index);
                        p.c.caiTrang = index;
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
                        p.endLoad(true);
                        break;
                    }
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

    public static void itemBagToBox(Player p, Message m) {
        try {
            if(p != null && p.c != null && p.conn != null && m != null && m.reader().available() > 0 && p.menuCaiTrang == 0) {
                byte index = m.reader().readByte();
                m.cleanup();
                Item item = p.c.getIndexBag(index);
                if (item != null) {
                    ItemTemplate data = ItemTemplate.ItemTemplateId(item.id);
                    byte indexBox = p.c.getIndexBoxid(item.id, item.isLock);
                    if (!item.isExpires && data.isUpToUp && indexBox != -1) {
                        p.c.ItemBag[index] = null;
                        Item item2 = p.c.ItemBox[indexBox];
                        item2.quantity += item.quantity;
                    } else {
                        if (p.c.getBoxNull() <= 0) {
                            p.conn.sendMessageLog(Language.NOT_ENOUGH_BOX);
                            return;
                        }
                        indexBox = p.c.getIndexBoxNotItem();
                        p.c.ItemBag[index] = null;
                        p.c.ItemBox[indexBox] = item;
                    }
                    m = new Message(17);
                    m.writer().writeByte(index);
                    m.writer().writeByte(indexBox);
                    m.writer().flush();
                    p.conn.sendMessage(m);
                    m.cleanup();
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

    public static void stoneSmelting(Player p, Message m, boolean isCoin) {
        try {
            if(p != null && p.c != null && p.conn != null && m != null && m.reader().available() > 0) {
                GameSrc.crystalCollect(p, m, isCoin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void upgradeEquipment(Player p, Message m) {
        try {
            if(p != null && p.c != null && p.conn != null && m != null && m.reader().available() > 0) {
                GameSrc.UpGrade(p, m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void splitEquipment(Player p, Message m) {
        try {
            if(p != null && p.c != null && p.conn != null && m != null && m.reader().available() > 0) {
                GameSrc.Split(p, m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void pleaseParty(Player p, Message m) {
        try {
            if(p != null && p.c != null && p.conn != null && !p.c.isDie && m != null && m.reader().available() > 0) {
                if(p.c.mapid == 133 || p.c.mapid == 111) {
                    p.c.p.conn.sendMessageLog("Không thể sử dụng chức năng này ở đây");
                    return;
                }
                String name = m.reader().readUTF();
                m.cleanup();
                Char cplayer = Client.gI().getNinja(name);
                if (cplayer != null && cplayer.party != null) {
                    if(p.c.pheCT != cplayer.pheCT) {
                        p.c.p.conn.sendMessageLog("Không thể xin vào nhóm đối phương của phe khác.");
                        return;
                    }
                    synchronized (p.c.aPartyInvate) {
                        if (p.c.party != null && p.c.party.partyId == cplayer.party.partyId) {
                            Service.ServerMessage(p.c, "Bạn đang trong nhóm này.");
                        } else if (p.c.party != null) {
                            Service.ServerMessage(p.c, "Bạn đang trong nhóm khác, không thể xin gia nhập.");
                        } else if (cplayer.party.numPlayer >= 6) {
                            Service.ServerMessage(p.c, "Nhóm đối phương đã đầy.");
                        } else if (p.c.findPartyInvate(cplayer.id) != null) {
                            Service.ServerMessage(p.c, "Bạn đã gởi yêu cầu xin vào nhóm rồi.");
                        } else if (cplayer.party.isLock) {
                            Service.ServerMessage(p.c, "Nhóm đã được khóa.");
                        } else {
                            p.c.aPartyInvate.add(new PartyPlease(cplayer.party.partyId, cplayer.id, 10000));
                            Service.pleaseInputParty(cplayer, p.c.name);
                        }
                        return;
                    }
                }
                Service.ServerMessage(p.c, "Nhóm không còn tồn tại.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void acceptPleaseParty(Player p, Message m) {
        try {
            if(p != null && p.c != null && p.conn != null && m != null && m.reader().available() > 0) {
                if(p.c.mapid == 133 || p.c.mapid == 111) {
                    p.conn.sendMessageLog("Không thể sử dụng chức năng này ở đây");
                    return;
                }
                String name = m.reader().readUTF();
                m.cleanup();
                Char cplayer = Client.gI().getNinja(name);
                if (cplayer != null) {
                    if(p.c.pheCT != cplayer.pheCT) {
                        p.conn.sendMessageLog("Không thể cho đối phương của phe khác vào nhóm.");
                        return;
                    }
                    synchronized (cplayer.aPartyInvate) {
                        if (cplayer.party != null && p.c.party.partyId == cplayer.party.partyId) {
                            Service.ServerMessage(p.c, p.c.name+" đang là đồng đội của bạn.");
                        } else if (cplayer.party != null) {
                            Service.ServerMessage(p.c, "Đối phương đang ở trong nhóm khác.");
                        } else if (p.c.party.numPlayer >= 6) {
                            Service.ServerMessage(p.c, "Nhóm đã đầy");
                        } else if (cplayer.findPartyInvate(p.c.id) != null && cplayer.tileMap != null) {
                            p.c.party.addPlayerParty(cplayer);
                            cplayer.tileMap.addParty(p.c.party);
                            cplayer.removePartyInvate(p.c.id);
                            p.c.party.refreshPlayer();
                        }
                    }
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
    public static void selectZone(Player p, Message m) {
        try {
            if(p != null && p.c != null && p.conn != null &&  !p.c.isDie && m != null && m.reader().available() > 0) {
                TileMap tileMap = p.c.tileMap;
                if(tileMap != null) {
                    byte zoneid = m.reader().readByte();
                    byte index = m.reader().readByte();
                    if (zoneid != tileMap.id) {
                        Item item = null;
                        if (index >= 0) {
                            try {
                                item = p.c.ItemBag[index];
                            } catch (Exception var8) {
                                var8.printStackTrace();
                            }
                        }
                        boolean isalpha = false;
                        byte i;
                        for(i = 0; i < tileMap.map.template.npc.length; ++i) {
                            if (tileMap.map.template.npc[i].id == 13 && Math.abs(tileMap.map.template.npc[i].x - p.c.get().x) < 50 && Math.abs(tileMap.map.template.npc[i].y - p.c.get().y) < 50) {
                                isalpha = true;
                                break;
                            }
                        }
                        if ((item != null && (item.id == 35 || item.id == 37) || isalpha) && zoneid >= 0 && zoneid < tileMap.map.area.length) {
                            if (tileMap.map.area[zoneid].numplayers < tileMap.map.template.maxplayers) {
                                tileMap.leave(p);
                                tileMap.map.area[zoneid].Enter(p);
                                p.endLoad(true);
                                if (item != null && item.id != 37) {
                                    p.c.removeItemBag(index);
                                }
                            } else {
                                p.sendAddchatYellow("Khu vực này đã đầy.");
                                p.endLoad(true);
                            }
                        }
                        m.cleanup();
                        m = new Message(57);
                        m.writer().flush();
                        p.conn.sendMessage(m);
                        m.cleanup();
                    }
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

    public static void selectMenuNpc(Player p, Message m) {
        try {
            if(p != null && p.c != null && p.conn != null  && !p.c.isDie && m != null && m.reader().available() > 0) {
                Menu.menu(p, m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void openZone(Player p) {
        if(p != null && p.c != null && p.conn != null && !p.c.isDie) {
            TileMap tileMap = p.c.tileMap;
            if(tileMap != null) {
                boolean isalpha = false;
                for(byte i = 0; i < tileMap.map.template.npc.length; ++i) {
                    if (tileMap.map.template.npc[i].id == 13 && Math.abs(tileMap.map.template.npc[i].x - p.c.get().x) < 50 && Math.abs(tileMap.map.template.npc[i].y - p.c.get().y) < 50) {
                        isalpha = true;
                        break;
                    }
                }

                if (p.c.quantityItemyTotal(37) <= 0 && p.c.quantityItemyTotal(35) <= 0 && !isalpha) {
                    p.c.get().upDie();
                } else {
                    Message m = null;
                    try {
                        m = new Message(36);
                        m.writer().writeByte(tileMap.map.area.length);
                        for(byte j = 0; j < tileMap.map.area.length; ++j) {
                            m.writer().writeByte(tileMap.map.area[j].numplayers);
                            m.writer().writeByte(tileMap.map.area[j].numParty);
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
            }
        }
    }

    public static void openMenuNpc(Player p, Message m) {
        try {
            if(p != null && p.c != null && p.conn != null && !p.c.isDie && m != null && m.reader().available() > 0) {
                Menu.menuId(p, m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void useSkill(Player p, Message m) {
        try {
            if(p != null && p.c != null && !p.c.isDie && m != null && m.reader().available() > 0) {
                if(System.currentTimeMillis() > p.c.get().CSkilldelay) {
                    UseSkill.useSkill(p, m);
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

    public static void requestItemInfo(Player p, Message m) {
        try {
            if(p != null && p.c != null && p.conn != null && !p.c.isDie && m != null && m.reader().available() > 0) {
                byte type = m.reader().readByte();
                int index = m.reader().readUnsignedByte();
                Util.Debug("type " + type + " index" + index);
                m.cleanup();
                Item item = null;
                switch(type) {
                    case 2: {
                        item = ItemSell.getItemTypeIndex(type, index);
                        break;
                    }
                    case 3: {
                        if (index >= 0 && index < p.c.maxluggage) {
                            item = p.c.ItemBag[index];
                            break;
                        }
                        return;
                    }
                    case 4: {
                        switch (p.menuCaiTrang) {
                            case 0: {
                                if (index >= 0 && index < 30) {
                                    item = p.c.ItemBox[index];
                                }
                                break;
                            }
                            case 1: {
                                if (index >= 0 && index < 9) {
                                    item = p.c.ItemBST[index];
                                }
                                break;
                            }
                            case 2: {
                                if (index >= 0 && index < 18) {
                                    item = p.c.ItemCaiTrang[index];
                                }
                                break;
                            }
                        }
                        break;
                    }
                    case 5: {
                        if (index < 0 || index > 15) {
                            return;
                        }
                        item = p.c.get().ItemBody[index];
                        break;
                    }
                    case 8: {
                        item = ItemSell.getItemTypeIndex(type, index);
                        break;
                    }
                    case 9: {
                        item = ItemSell.getItemTypeIndex(type, index);
                        break;
                    }
                    case 14: {
                        item = ItemSell.getItemTypeIndex(type, index);
                        break;
                    }
                    case 15: {
                        item = ItemSell.getItemTypeIndex(type, index);
                        break;
                    }
                    case 16:
                        item = ItemSell.getItemTypeIndex(type, index);
                        break;
                    case 17:
                        item = ItemSell.getItemTypeIndex(type, index);
                        break;
                    case 18:
                        item = ItemSell.getItemTypeIndex(type, index);
                        break;
                    case 19:
                        item = ItemSell.getItemTypeIndex(type, index);
                        break;
                    case 20:
                        item = ItemSell.getItemTypeIndex(type, index);
                        break;
                    case 21:
                        item = ItemSell.getItemTypeIndex(type, index);
                        break;
                    case 22:
                        item = ItemSell.getItemTypeIndex(type, index);
                        break;
                    case 23:
                        item = ItemSell.getItemTypeIndex(type, index);
                        break;
                    case 24:
                        item = ItemSell.getItemTypeIndex(type, index);
                        break;
                    case 25:
                        item = ItemSell.getItemTypeIndex(type, index);
                        break;
                    case 26:
                        item = ItemSell.getItemTypeIndex(type, index);
                        break;
                    case 27:
                        item = ItemSell.getItemTypeIndex(type, index);
                        break;
                    case 28:
                        item = ItemSell.getItemTypeIndex(type, index);
                        break;
                    case 29:
                        item = ItemSell.getItemTypeIndex(type, index);
                        break;
                    case 32:
                        item = ItemSell.getItemTypeIndex(type, index);
                        break;
                    case 34:
                        item = ItemSell.getItemTypeIndex(type, index);
                        break;
                    case 39:
                        ClanManager clan = ClanManager.getClanName(p.c.clan.clanName);
                        if (clan != null && index >= 0 && index < clan.items.size()) {
                            item = (Item)clan.items.get(index);
                        }
                        break;
                    case 41: {
                        if (index < 0 || index > 4) {
                            return;
                        }
                        item = p.c.ItemMounts[index];
                    }
                }

                if (item != null) {
                    Service.requestItemInfoMessage(p, item, index, type);
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

    public static void inviteTrade(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && !player.c.isDie && m != null && m.reader().available() > 0) {
                if (player.c.mapid != 111 && player.c.mapid != 133 && !player.c.tileMap.map.mapChienTruong() && !player.c.tileMap.map.mapGTC()) {
                    int ids = m.reader().readInt();
                    m.cleanup();
                    Char _char1 = player.c.tileMap.getNinja(ids);
                    if (_char1 != null) {
                        Player p = _char1.p;
                        if (p == null) {
                            player.sendAddchatYellow("Người này không trong khu hoặc đã offline.");
                        } else if (Math.abs(player.c.get().x - p.c.get().x) <= 100 && Math.abs(player.c.get().y - p.c.get().y) <= 100) {
                            if (player.c.tradeDelay > System.currentTimeMillis()) {
                                player.conn.sendMessageLog("Bàn cần " + (player.c.tradeDelay - System.currentTimeMillis()) / 1000L + "s để tiếp tục giao dịch.");
                            } else if (player.c.rqTradeId > 0) {
                                player.conn.sendMessageLog(p.c.name + " đang có yêu cầu giao dịch.");
                            } else if (p.c.isTrade) {
                                player.conn.sendMessageLog(p.c.name + " đang thực hiện giao dịch.");
                            } else {
                                player.c.tradeDelay = System.currentTimeMillis() + 30000L;
                                p.c.rqTradeId = player.c.get().id;
                                m = new Message(43);
                                m.writer().writeInt(player.c.get().id);
                                m.writer().flush();
                                p.conn.sendMessage(m);
                                m.cleanup();
                            }
                        } else {
                            player.sendAddchatYellow(Language.NOT_ENOUGH_DISTANCE);
                        }

                    }
                }
                else {
                    player.conn.sendMessageLog("Bạn không thể sử dụng chức năng này tại đây");
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

    public static void accpetTrade(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && !player.c.isDie && m != null && m.reader().available() > 0) {
                int ids = m.reader().readInt();
                m.cleanup();
                if (player.c.isTrade) {
                    player.conn.sendMessageLog("Bạn đã có giao dịch.");
                } else {
                    Char _ch = player.c.tileMap.getNinja(ids);
                    if (_ch != null) {
                        Player p = _ch.p;
                        if (p == null) {
                            player.sendAddchatYellow("Người này không trong khu hoặc đã offline.");
                        } else if (Math.abs(player.c.get().x - p.c.get().x) <= 100 && Math.abs(player.c.get().y - p.c.get().y) <= 100) {
                            if (!p.c.isTrade) {
                                p.c.isTrade = true;
                                p.c.tradeId = player.c.id;
                                p.c.tradeLock = 0;
                                player.c.isTrade = true;
                                player.c.tradeId = p.c.id;
                                player.c.tradeLock = 0;
                                player.c.rqTradeId = 0;
                                m = new Message(37);
                                m.writer().writeUTF(p.c.name);
                                m.writer().flush();
                                player.conn.sendMessage(m);
                                m.cleanup();
                                m = new Message(37);
                                m.writer().writeUTF(player.c.name);
                                m.writer().flush();
                                p.conn.sendMessage(m);
                                m.cleanup();
                                return;
                            }
                            player.conn.sendMessageLog(p.c.name + " đã có giao dịch.");
                        } else {
                            player.sendAddchatYellow(Language.NOT_ENOUGH_DISTANCE);
                        }
                        player.c.rqTradeId = 0;
                    }
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

    public static void lockTrade(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && !player.c.isDie && m != null && m.reader().available() > 0) {
                if (player.c.tradeLock == 0) {
                    Char c = player.c;
                    c.tradeLock++;
                    Char n = player.c.tileMap.getNinja(player.c.tradeId);
                    if (n == null) {
                        HandleController.closeLoad(player);
                        return;
                    }

                    int tradexu = m.reader().readInt();
                    if (tradexu > 0 && tradexu <= player.c.xu) {
                        player.c.tradeCoin = tradexu;
                    }
                    byte length = m.reader().readByte();
                    byte i;
                    byte index;
                    Item item;
                    for(i = 0; i < length; ++i) {
                        index = m.reader().readByte();
                        item = player.c.getIndexBag(index);
                        if (player.c.tradeIdItem.size() > 12) {
                            break;
                        }
                        if (item != null && !item.isLock) {
                            player.c.tradeIdItem.add(index);
                        }
                    }
                    if (player.c.tradeIdItem.size() > n.getBagNull()) {
                        HandleController.closeLoad(player);
                        return;
                    }
                    m.cleanup();
                    m = new Message(45);
                    m.writer().writeInt(player.c.tradeCoin);
                    m.writer().writeByte(player.c.tradeIdItem.size());
                    for(i = 0; i < player.c.tradeIdItem.size(); ++i) {
                        Item item2 = player.c.getIndexBag((Byte)player.c.tradeIdItem.get(i));
                        if (item2 != null) {
                            m.writer().writeShort(item2.id);
                            if (ItemTemplate.isTypeBody(item2.id) || ItemTemplate.isTypeNgocKham(item2.id)) {
                                m.writer().writeByte(item2.upgrade);
                            }
                            m.writer().writeBoolean(item2.isExpires);
                            m.writer().writeShort(item2.quantity);
                        }
                    }
                    m.writer().flush();
                    n.p.conn.sendMessage(m);
                    m.cleanup();
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

    public static void submitTrade(Player player) {
        if(player != null && player.c != null && player.conn != null && !player.c.isDie) {
            if (player.c.tradeLock == 1) {
                Char n = player.c.tileMap.getNinja(player.c.tradeId);
                if (n == null) {
                    HandleController.closeLoad(player);
                    return;
                }
                Char c = player.c;
                ++c.tradeLock;
                Message m = null;
                try {
                    m = new Message(46);
                    m.writer().flush();
                    n.p.conn.sendMessage(m);
                    m.cleanup();
                    if (n.tradeLock == 2) {
                        m = new Message(57);
                        m.writer().flush();
                        player.conn.sendMessage(m);
                        n.p.conn.sendMessage(m);
                        m.cleanup();
                        if (n.tradeCoin > 0) {
                            n.upxuMessage((long)(-n.tradeCoin));
                            player.c.upxuMessage((long)n.tradeCoin);
                        }

                        if (player.c.tradeCoin > 0) {
                            player.c.upxuMessage((long)(-player.c.tradeCoin));
                            n.upxuMessage((long)player.c.tradeCoin);
                        }

                        ArrayList<Item> item1 = new ArrayList();
                        ArrayList<Item> item2 = new ArrayList();

                        byte i;
                        Item item3;
                        for(i = 0; i < n.tradeIdItem.size(); ++i) {
                            item3 = n.p.c.getIndexBag((Byte)n.tradeIdItem.get(i));
                            if (item3 != null) {
                                item1.add(item3);
                                n.removeItemBag((Byte)n.tradeIdItem.get(i));
                            }
                        }

                        for(i = 0; i < player.c.tradeIdItem.size(); ++i) {
                            item3 = player.c.getIndexBag((Byte)player.c.tradeIdItem.get(i));
                            if (item3 != null) {
                                item2.add(item3);
                                player.c.removeItemBag((Byte)player.c.tradeIdItem.get(i));
                            }
                        }

                        for(i = 0; i < item1.size(); ++i) {
                            item3 = (Item)item1.get(i);
                            if (item3 != null) {
                                player.c.addItemBag(true, item3);
                            }
                        }

                        for(i = 0; i < item2.size(); ++i) {
                            item3 = (Item)item2.get(i);
                            if (item3 != null) {
                                n.addItemBag(true, item3);
                            }
                        }

                        HandleController.closeTrade(player);
                        HandleController.closeTrade(n.p);
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
    }

    public static void selectMenuNpcTileMap(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && !player.c.isDie && m != null && m.reader().available() > 0) {
                Service.chatNPC(player, (short)m.reader().readByte(), m.reader().readByte() + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void closeTrade(Player player) {
        if(player != null && player.c != null && !player.c.isDie) {
            if (player.c.isTrade) {
                player.c.isTrade = false;
                player.c.tradeCoin = 0;
                player.c.tradeIdItem.clear();
                player.c.tradeLock = -1;
                player.c.tradeDelay = 0L;
                player.c.tradeId = 0;
            } else if (player.c.rqTradeId > 0) {
                player.c.rqTradeId = 0;
            }
            player.c.requestclan = -1;
        }
    }

    public static void closeLoad(Player player) {
        if(player != null && player.c != null && player.conn != null && !player.c.isDie) {
            Message m = null;
            try {
                if (player.c.isTrade) {
                    Char n = Client.gI().getNinja(player.c.tradeId);
                    if (n != null && n.p != null && n.isTrade) {
                        n.p.endDlg(true);
                        n.p.sendAddchatYellow("Giao dịch đã bị huỷ bỏ.");
                        player.sendAddchatYellow("Giao dịch đã bị huỷ bỏ.");
                    }
                    HandleController.closeTrade(player);
                }
                m = new Message(57);
                m.writer().flush();
                player.conn.sendMessage(m);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(m != null) {
                    m.cleanup();
                }
            }
        }
    }

    public static void addFriend(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && !player.c.isDie && m != null && m.reader().available() > 0) {
                String nF = m.reader().readUTF();
                if (nF.equals(player.c.name)) {
                    player.sendAddchatYellow("Không thể thêm chính bản thân vào danh sách bạn bè.");
                } else {
                    Char _char = Client.gI().getNinja(nF);
                    if (_char != null)
                    {
                        if(player.c.vFriend.size() >= 50) {
                            Service.ServerMessage(player.c, "Danh sách bạn bè đã đầy.");
                        }
                        for (short i = 0; i < player.c.vFriend.size(); i = (short)(i + 1)) {
                            Friend friend1 = player.c.vFriend.get(i);
                            if (friend1.friendName.equals(nF)) {
                                Service.ServerMessage(player.c, String.format("%s đã có tên trong danh sách bạn bè hoặc thù địch.", new Object[] { nF }));
                                return;
                            }
                        }
                        byte type = 0;
                        short s1;
                        for (s1 = 0; s1 < _char.vFriend.size(); s1 = (short)(s1 + 1)) {
                            Friend friend1 = _char.vFriend.get(s1);
                            if (friend1.friendName.equals(player.c.name)) {
                                friend1.type = type = 1;
                                break;
                            }
                        }
                        Friend friend = new Friend(nF, type);
                        player.c.vFriend.add(friend);
                        if (friend.type == 1) {
                            Service.addFriend(_char, player.c.name, friend.type);
                        } else {
                            Service.FriendInvate(_char, player.c.name);
                        }
                        Service.addFriend(player.c, friend.friendName, friend.type);
                    }
                    else {
                        Service.ServerMessage(player.c, "Hiện tại người chơi này không online.");
                    }
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

    public static void attackMob(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && !player.c.isDie && m != null && m.reader().available() > 0) {
                player.c.tileMap.FightMob(player, m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void attackNinja(Player player, Message m) {
        try {
            if(player != null && player.c != null && !player.c.isDie && m != null && m.reader().available() > 0) {
                if(player.c.tileMap != null && player.conn != null) {
                    player.c.tileMap.FightNinja(player, m);
                } else if(player.c.tdbTileMap != null) {
                    player.c.tdbTileMap.FightNinja(player, m);
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

    public static void inviteSolo(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && player.c.tileMap != null && !player.c.isDie && m != null && m.reader().available() > 0) {
                if (player.c.mapid != 111 && player.c.mapid != 133 && !player.c.tileMap.map.mapChienTruong()) {
                    Char _char = player.c.tileMap.getNinja(player.c.testCharID);
                    if (player.c.testCharID != -9999 && player != null) {
                        GameCanvas.addInfoDlg(player.conn, Language.TEST_ALREADY);
                    } else {
                        _char = player.c.tileMap.getNinja(m.reader().readInt());
                        m.cleanup();
                        if (_char != null && _char.tileMap != null && !_char.isTest) {
                            player.c.testCharID = _char.id;
                            Service.TestInvite(_char, player.c.id);
                        }

                    }
                } else {
                    player.conn.sendMessageLog("Bạn không thể sử dụng chức năng này tại đây");
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

    public static void accpetSolo(Player player, Message msg) {
        try {
            if(player != null && player.c != null && player.conn != null && player.c.tileMap != null && !player.c.isDie && msg != null && msg.reader().available() > 0) {
                Char _char = player.c.tileMap.getNinja(msg.reader().readInt());
                if (_char != null && _char.testCharID == player.c.id && !_char.isTest) {
                    player.c.testCharID = _char.id;
                    player.c.isTest = true;
                    _char.isTest = true;
                    int i;
                    for(i = 0; i < player.c.tileMap.players.size(); ++i) {
                        if (player.c.tileMap.players.get(i) != null && (player.c.tileMap.players.get(i)).conn != null) {
                            Service.TestAccept((player.c.tileMap.players.get(i)).c, player.c.id, _char.id);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void startKillNinja(Player player, Message msg) {
        try {
            if(player != null && player.c != null && player.conn != null && player.c.tileMap != null && !player.c.isDie && msg != null && msg.reader().available() > 0) {
                if (GameSrc.mapNotPK(player.c.mapid)) {
                    player.conn.sendMessageLog(Language.NOT_CUU_SAT);
                } else if (player.c.mapid != 111 && player.c.mapid != 133 && !player.c.tileMap.map.mapChienTruong()) {
                    if (player.c.pk >= 14) {
                        player.conn.sendMessageLog(Language.MAX_HIEU_CHIEN);
                    } else if (player.c.tileMap.getNinja(player.c.KillCharId) != null) {
                        player.conn.sendMessageLog(Language.MAX_CUU_SAT);
                    } else {
                        Char _char = player.c.tileMap.getNinja(msg.reader().readInt());
                        if (_char != null && !_char.get().isDie) {
                            player.c.KillCharId = _char.id;
                            player.c.isCuuSat = true;
                            Service.AddCuuSat(_char, player.c.id);
                            Service.MeCuuSat(player.c, _char.id);
                        }
                    }
                } else {
                    player.conn.sendMessageLog("Bạn không thể sử dụng chức năng này tại đây");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void inviteToParty(Player player, Message msg) {
        try {
            if(player != null && player.c != null && player.conn != null && player.c.isHuman && !player.c.isDie && msg != null && msg.reader().available() > 0) {
                Char _char = player.c;
                if(_char.mapid == 133 || _char.mapid == 111) {
                    _char.p.conn.sendMessageLog("Không thể sử dụng chức năng này ở đây");
                    return;
                }
                String name = msg.reader().readUTF();
                msg.cleanup();
                Char cplayer = Client.gI().getNinja(name);
                if (cplayer != null) {
                    if((_char.tileMap.map.mapChienTruong() != cplayer.tileMap.map.mapChienTruong())) {
                        _char.p.conn.sendMessageLog("Không thể mời đối phương vào nhóm");
                        return;
                    }else if((_char.tileMap.map.mapChienTruong() || cplayer.tileMap.map.mapChienTruong()) && _char.pheCT != cplayer.pheCT) {
                        _char.p.conn.sendMessageLog("Không thể mời đối phương phe khác vào nhóm");
                        return;
                    } else if ((_char.tileMap.map.mapGTC() || cplayer.tileMap.map.mapGTC()) && _char.clan.clanName != cplayer.clan.clanName) {
                        _char.p.conn.sendMessageLog("Không thể mời đối phương vào nhóm");
                        return;
                    }
                    synchronized (_char.aPartyInvite) {
                        if (_char.findPartyInvite(cplayer.id) != null) {
                            Service.ServerMessage(_char, "Bạn đang trong nhóm khác, không thể chấp nhận vào nhóm này.");
                        } else if (_char.party != null) {
                            if (_char.party.charID != _char.id) {
                                Service.ServerMessage(_char, "Bạn không phải là đội trưởng.");
                            } else if (_char.party.numPlayer >= 6) {
                                Service.ServerMessage(_char, "Nhóm đã đầy");
                            } else if (cplayer.party != null && _char.party.partyId == cplayer.party.partyId) {
                                Service.ServerMessage(_char, cplayer.name + " đang là đồng đội của bạn.");
                            } else if (cplayer.party != null) {
                                Service.ServerMessage(_char, "Đối phương đang ở trong nhóm khác.");
                            } else {
                                _char.aPartyInvite.add(new PartyPlease(_char.party.partyId, cplayer.id, 10000));
                                Service.inviteParty(cplayer, _char.name, _char.id);
                            }
                        } else if (cplayer.party != null) {
                            Service.ServerMessage(_char, "Đối phương đang ở trong nhóm khác.");
                        } else if (_char.tileMap.numParty >= 4) {
                            Service.ServerMessage(_char, "Số nhóm trong khu vực này đã đạt tối đa.");
                        } else {
                            Party party = new Party(_char);
                            _char.party = party;
                            _char.tileMap.addParty(party);
                            _char.aPartyInvite.add(new PartyPlease(party.partyId, cplayer.id, 10000));
                            Service.PlayerInParty(_char, party);
                            Service.inviteParty(cplayer, _char.name, _char.id);
                        }
                    }
                    return;
                }
                Service.ServerMessage(_char, "Hiện tại người chơi này không online.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void accpetInviteToParty(Player player, Message msg) {
        try {
            if(player != null && player.c != null && player.conn != null && player.c.isHuman &&  !player.c.isDie && msg != null && msg.reader().available() > 0) {
                Char _char = player.c;
                if(_char.mapid == 133 || _char.mapid == 111) {
                    _char.p.conn.sendMessageLog("Không thể sử dụng chức năng này ở đây");
                    return;
                }
                int charId = msg.reader().readInt();
                msg.cleanup();
                Char cplayer = Client.gI().getNinja(charId);
                if (cplayer != null) {
                    if(_char.pheCT != _char.pheCT) {
                        _char.p.conn.sendMessageLog("Không thể vào nhóm đối phương của phe khác.");
                        return;
                    }
                    if (cplayer.party != null)
                        synchronized (cplayer.aPartyInvite) {
                            if (_char.party != null && _char.party.partyId == cplayer.party.partyId) {
                                Service.ServerMessage(_char, "Bạn đang trong này.");
                            } else if (_char.party != null) {
                                Service.ServerMessage(_char, "Bạn đang trong nhóm khác, không thể chấp nhận lời mời này.");
                            } else if (cplayer.party.numPlayer >= 6) {
                                Service.ServerMessage(_char, "Nhóm đã đầy");
                            } else if (cplayer.findPartyInvite(_char.id) != null) {
                                cplayer.party.addPlayerParty(_char);
                                cplayer.removePartyInvite(_char.id);
                                _char.party.refreshPlayer();
                            }
                        }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void outParty(Player player) {
        if(player != null && player.c != null && player.conn != null && player.c.party != null) {
            Char _char = player.c;
            if(_char.mapid == 133 || _char.mapid == 111) {
                _char.p.conn.sendMessageLog("Không thể sử dụng chức năng này ở đây");
                return;
            }
            Party party = _char.party;
            if (party != null) {
                synchronized (party.LOCK) {
                    byte i;
                    for (i = 0; i < party.numPlayer; i = (byte)(i + 1)) {
                        if (party.aChar.get(i) != null && party.aChar.get(i).id != _char.id)
                            Service.ServerMessage(party.aChar.get(i), _char.name + " đã rời khỏi nhóm.");
                    }
                }
                party.removePlayer(_char.id);
            }
        }
    }

    public static void inputValue(Player player, Message msg) {
        try {
            if(player != null && player.c != null && player.conn != null && msg != null && msg.reader().available() > 0) {
                Draw.Draw(player, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void viewInfoNinja(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && m != null && m.reader().available() > 0) {
                String playername = m.reader().readUTF();
                m.cleanup();
                if (playername.equals(player.c.name)) {
                    Service.sendInfoPlayers(player, player);
                } else {
                    Char n;
                    if (playername.equals(player.c.name)) {
                        n = player.c;
                    } else {
                        n = Client.gI().getNinja(playername);
                    }
                    if (n == null) {
                        player.sendAddchatYellow("Hiện tại người chơi này không online");
                    } else {
                        n.p.sendAddchatYellow(player.c.name + " đang đứng nhìn bạn");
                        Service.sendInfoPlayers(player, n.p);
                    }
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

    public static void viewItemNinja(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && m != null && m.reader().available() > 0) {
                int pid = m.reader().readInt();
                byte index = m.reader().readByte();
                m.cleanup();
                Char n = Client.gI().getNinja(pid);
                if (n != null && index >= 0 && index <= 31) {
                    Item item = n.get().ItemBody[index];
                    if (item != null) {
                        m = new Message(94);
                        m.writer().writeByte(index);
                        m.writer().writeLong(item.expires);
                        m.writer().writeInt(item.saleCoinLock);
                        m.writer().writeByte(item.sys);
                        short i;
                        for(i = 0; i < item.options.size(); ++i) {
                            m.writer().writeByte(((Option)item.options.get(i)).id);
                            m.writer().writeInt(((Option)item.options.get(i)).param);
                        }
                        m.writer().flush();
                        player.conn.sendMessage(m);
                        m.cleanup();
                    }
                } else if(player.viewChar != null) {
                    Item item = player.viewChar.get().ItemBody[index];
                    if (item != null) {
                        m = new Message(94);
                        m.writer().writeByte(index);
                        m.writer().writeLong(item.expires);
                        m.writer().writeInt(item.saleCoinLock);
                        m.writer().writeByte(item.sys);
                        short i;
                        for(i = 0; i < item.options.size(); ++i) {
                            m.writer().writeByte(((Option)item.options.get(i)).id);
                            m.writer().writeInt(((Option)item.options.get(i)).param);
                        }
                        m.writer().flush();
                        player.conn.sendMessage(m);
                        m.cleanup();
                    }
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

    public static void accpetDun(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null &&  player.c.tileMap != null && !player.c.isDie && m != null && m.reader().available() > 0) {
                if (player.c.isNhanban) {
                    player.conn.sendMessageLog(Language.NOT_FOR_PHAN_THAN);
                } else {
                    int pid = m.reader().readInt();
                    m.cleanup();
                    Player _p = player.c.tileMap.getNinja(pid).p;
                    Dun dun = new Dun();
                    TileMap tileMapTemp = player.c.tileMap;
                    Char _charTemp1 = null;
                    Char _charTemp2 = null;
                    if (_p != null) {
                        Char _charP;
                        int i;
                        if (player.c.party == null) {
                            player.c.isInDun = true;
                            player.c.dunId = dun.dunID;
                            player.c.mapKanata = player.c.mapid;
                            if (player.c.typepk != 0) {
                                Service.ChangTypePkId(player.c, (byte)0);
                            }

                            dun.team1.add(player.c);
                            _charTemp1 = player.c;
                            dun.idC1 = player.c.id;
                            dun.name1 = player.c.name;
                            dun.lv1 = player.c.level;
                            player.c.tileMap.leave(player.c.p);
                            dun.map[0].area[0].EnterMap0WithXY(player.c, (short)398, (short)-1);
                        } else {
                            if (player.c.party.charID != player.c.id) {
                                player.sendAddchatYellow("Bạn không phải trưởng nhóm, nên không thể chấp nhận lời mời lôi đài này");
                                return;
                            }

                            for(i = 0; i < player.c.party.aChar.size(); ++i) {
                                _charP = (Char)player.c.party.aChar.get(i);
                                if (tileMapTemp.getNinja(_charP.id) != null && !_charP.isNhanban) {
                                    _charP.isInDun = true;
                                    _charP.dunId = dun.dunID;
                                    _charP.mapKanata = _charP.c.mapid;
                                    if (_charP.typepk != 0) {
                                        Service.ChangTypePkId(_charP, (byte)0);
                                    }

                                    if (_charP.id == player.c.party.charID) {
                                        _charTemp1 = _charP;
                                        dun.idC1 = _charP.id;
                                        dun.name1 = _charP.name;
                                        dun.lv1 = _charP.level;
                                    }

                                    dun.team1.add(_charP);
                                    _charP.tileMap.leave(_charP.p);
                                    dun.map[0].area[0].EnterMap0WithXY(_charP, (short)398, (short)-1);
                                } else {
                                    player.c.party.removePlayer(_charP.id);
                                }
                            }
                        }
                        if (_p.c.party != null) {
                            for(i = 0; i < _p.c.party.aChar.size(); ++i) {
                                _charP = (Char)_p.c.party.aChar.get(i);
                                if (tileMapTemp.getNinja(_charP.id) != null && !_charP.isNhanban) {
                                    _charP.isInDun = true;
                                    _charP.dunId = dun.dunID;
                                    _charP.mapKanata = _charP.c.mapid;
                                    if (_charP.typepk != 0) {
                                        Service.ChangTypePkId(_charP, (byte)0);
                                    }
                                    if (_charP.id == _p.c.party.charID) {
                                        _charTemp2 = _charP;
                                        dun.idC2 = _charP.id;
                                        dun.name2 = _charP.name;
                                        dun.lv2 = _charP.level;
                                    }
                                    dun.team2.add(_charP);
                                    _charP.tileMap.leave(_charP.p);
                                    dun.map[0].area[0].EnterMap0WithXY(_charP, (short)153, (short)-1);
                                } else {
                                    _p.c.party.removePlayer(_charP.id);
                                }
                            }
                        }
                        else {
                            _p.c.isInDun = true;
                            _p.c.dunId = dun.dunID;
                            _p.c.mapKanata = _p.c.mapid;
                            if (_p.c.typepk != 0) {
                                Service.ChangTypePkId(_p.c, (byte)0);
                            }
                            dun.team2.add(_p.c);
                            _charTemp2 = _p.c;
                            dun.idC2 = _p.c.id;
                            dun.name2 = _p.c.name;
                            dun.lv2 = _p.c.level;
                            _p.c.tileMap.leave(_p.c.p);
                            dun.map[0].area[0].EnterMap0WithXY(_p.c, (short)153, (short)-1);
                        }
                        if(_charTemp1 != null && _charTemp2 != null) {
                            dun.c1 = _charTemp1;
                            dun.c2 = _charTemp2;
                        }
                    }

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

    public static void viewDun(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && player.c.tileMap != null && !player.c.isDie && m != null && m.reader().available() > 0) {
                int idDun = m.reader().readUnsignedByte();
                Char _char = player.c;
                Dun dun = null;
                if (Dun.duns.containsKey(idDun)) {
                    dun = Dun.duns.get(idDun);
                    if(dun != null) {
                        if(!dun.isStart) {
                            _char.p.conn.sendMessageLog( "Trận đấu này đã chưa diễn ra, hãy quay lại sau.");
                            return;
                        }
                        _char.dunId = idDun;
                        _char.isInDun = true;
                        _char.mapKanata = _char.mapid;
                        if(_char.typepk != 0) {
                            Service.ChangTypePkId(_char, (byte)0);
                        }
                        dun.viewer.add(_char);
                        _char.tileMap.leave(_char.p);
                        _char.yDun = 336;
                        dun.map[1].area[0].EnterMap0WithXY(_char, (short)Util.nextInt(280,490) , (short)336);
                    } else {
                        _char.p.conn.sendMessageLog("Trận đấu này đã kết thúc hoặc không tồn tại.");
                        return;
                    }
                } else {
                    _char.p.conn.sendMessageLog( "Gặp lỗi, hãy đăng xuất và vào lại nhé!");
                    return;
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

    public static void sendItemToAuction(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && !player.c.isDie && m != null && m.reader().available() > 0 && player.menuIdAuction == -2) {
                byte index = m.reader().readByte();
                int xu = m.reader().readInt();
                Item item = player.c.ItemBag[index];
                if(item != null) {
                    if(item.isLock() || item.isExpires) {
                        player.sendAddchatYellow("Vật phẩm này không thể rao bán!");
                        return;
                    }
                    if(player.c.xu < 5000) {
                        player.sendAddchatYellow("Bạn không đủ xu để bán vật phẩm!");
                        return;
                    }
                    if(player.luong < 5) {
                        player.sendAddchatYellow("Bạn không đủ lượng để bán vật phẩm");
                        return;
                    }
                    if(xu < 100) {
                        player.sendAddchatYellow("Giá thấp nhất để đặt là 100 xu");
                        return;
                    }

                    ItemTemplate data = ItemTemplate.ItemTemplateId(item.id);
                    if(data.type == 26) {
                        ShinwaManager.entrys.get(0).add(new ShinwaTemplate(item, System.currentTimeMillis(), player.c.name, xu));
                    } else if(data.type >= 0 && data.type <= 9) {
                        ShinwaManager.entrys.get(data.type+1).add(new ShinwaTemplate(item, System.currentTimeMillis(), player.c.name, xu));
                    } else {
                        ShinwaManager.entrys.get(11).add(new ShinwaTemplate(item, System.currentTimeMillis(), player.c.name, xu));
                    }
                    player.c.ItemBag[index] = null;
                    player.c.upxuMessage(-5000);
                    player.upluongMessage(-5);
                    Service.sendItemToAuction(player.c, index, xu);
                    Service.CharViewInfo(player, false);
                } else {
                    return;
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

    public static void viewItemAuction(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && !player.c.isDie && m != null && m.reader().available() > 0) {
                if(player.menuIdAuction == -1) {
                    return;
                }
                int index = m.reader().readInt();
                ShinwaTemplate itemShinwa = ShinwaManager.entrys.get(player.menuIdAuction).get(index);
                if(itemShinwa != null) {
                    Service.viewItemToAuction(player, index, itemShinwa.getItem());
                } else {
                    player.sendAddchatYellow("Vật phẩm không còn tồn tại!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void buyItemAuction(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && !player.c.isDie && m != null && m.reader().available() > 0) {
                if(player.menuIdAuction == -1) {
                    return;
                }
                int index = m.reader().readInt();
                ShinwaTemplate itemShinwa = ShinwaManager.entrys.get(player.menuIdAuction).get(index);
                if(itemShinwa != null) {
                    if(player.c.getBagNull() == 0) {
                        player.sendAddchatYellow(Language.NOT_ENOUGH_BAG);
                        return;
                    }
                    if(player.c.xu < itemShinwa.getPrice()) {
                        player.sendAddchatYellow("Bạn không đủ xu để mua vật phẩm!");
                        return;
                    }
                    Char _char = Client.gI().getNinja(itemShinwa.getSeller());
                    if(_char != null) {
                        _char.upxuMessage((long)itemShinwa.getPrice()*99/100);
                        _char.p.sendAddchatYellow("Bạn vừa nhận được " + itemShinwa.getPrice()*99/100 + " từ đồ đấu giá tại Shinwa.");
                    } else {
                        synchronized (Server.LOCK_MYSQL) {
                            try {
                                ResultSet res = SQLManager.stat.executeQuery("SELECT * FROM `ninja` WHERE `name`='" + itemShinwa.getSeller() + "';");
                                if(res.next()) {
                                    long xuOld = res.getLong("xu");
                                    xuOld += itemShinwa.getPrice()*99/100;
                                    if(xuOld > 2000000000L) {
                                        xuOld = 2000000000L;
                                    }
                                    SQLManager.stat.executeUpdate("UPDATE `ninja` SET `xu`=" + xuOld + " WHERE `name`='" + itemShinwa.getSeller() + "';");
                                    res.close();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                    }
                    ShinwaManager.entrys.get(player.menuIdAuction).remove(index);
                    player.c.upxuMessage(-(long)itemShinwa.getPrice());
                    player.c.addItemBag(true, itemShinwa.getItem());
                    player.endDlg(true);
                } else {
                    player.sendAddchatYellow("Vật phẩm không còn tồn tại!");
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

    public static void yesNoDlg(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && !player.c.isDie && m != null && m.reader().available() > 0) {
                int type = m.reader().readByte();
                switch (type) {
                    case 0: {
                        GameSrc.NangMat(player, player.c.ItemBody[14], 0);
                        break;
                    }
                    case 1: {
                        GameSrc.NangMat(player, player.c.ItemBody[14], 1);
                        break;
                    }
                    case 2: {
                        GameSrc.HuyNhiemVuDanhVong(player);
                        break;
                    }

                    //Mời ldgt
                    case 3: {
                        if(player.c.clan != null && player.c.tileMap != null) {
                            ClanManager clan = ClanManager.getClanName(player.c.clan.clanName);
                            if(clan != null && clan.ldgtID != -1) {
                                if (LanhDiaGiaToc.ldgts.containsKey(clan.ldgtID)) {
                                    LanhDiaGiaToc lanhDiaGiaToc = LanhDiaGiaToc.ldgts.get(clan.ldgtID);
                                    if (lanhDiaGiaToc != null && lanhDiaGiaToc.map[0] != null && lanhDiaGiaToc.map[0].area[0] != null) {
                                        if(lanhDiaGiaToc.ninjas.size() <= 24) {
                                            player.c.ldgtID = clan.ldgtID;
                                            player.c.mapKanata = player.c.mapid;
                                            player.c.tileMap.leave(player);
                                            lanhDiaGiaToc.map[0].area[0].EnterMap0(player.c);
                                            return;
                                        } else {
                                            player.sendAddchatYellow("Số thành viên tham gia Lãnh Địa Gia Tộc đã đạt tối đa.");
                                            return;
                                        }
                                    }
                                }
                            }
                        } else {
                            player.conn.sendMessageLog("Bạn không còn ở trong Gia tộc này nữa.");
                            return;
                        }
                        break;
                    }

                    //Mời GTC
                    case 4: {
                        if(player.c.clan != null && player.c.tileMap != null) {
                            ClanManager clan = ClanManager.getClanName(player.c.clan.clanName);
                            if(clan != null && player.c.clan.typeclan == 4) {
                                if (GiaTocChien.gtcs.containsKey(clan.gtcID)) {
                                    GiaTocChien giaTocChien = GiaTocChien.gtcs.get(clan.gtcID);
                                    ClanManager clan2 = ClanManager.getClanName(clan.gtcClanName);
                                    String main2 = clan2.getmain_name();
                                    Char _charTT = Client.gI().getNinja(main2);
                                    if (giaTocChien != null && giaTocChien.map[0] != null && giaTocChien.map[0].area[0] != null) {
                                        if(_charTT != null) {
                                            giaTocChien.clan1 = clan;
                                            giaTocChien.clan2 = clan2;
                                            player.c.tileMap.leave(player);
                                            giaTocChien.map[0].area[0].EnterMap0WithXY(player.c,(short)117,(short)336);
                                            _charTT.tileMap.leave(_charTT.p);
                                            giaTocChien.map[0].area[0].EnterMap0WithXY(_charTT,(short)673,(short)336);
                                            giaTocChien.join();
                                            return;
                                        } else {
                                            player.conn.sendMessageLog("Tộc trưởng đối phương không online.");
                                            return;
                                        }
                                    }
                                } else {
                                    player.conn.sendMessageLog("Lời mời đã hết hạn.");
                                    return;
                                }
                            }
                        }
                        break;
                    }
                    case 5: {
                        if(player.c != null && player.c.clone != null && player.c.tileMap != null) {
                            player.c.clone.exp = Level.getMaxExp(10);
                            player.c.clone.level = 10;
                            for(int i = 0; i < player.c.clone.ItemBody.length; i++) {
                                player.c.clone.ItemBody[i] = null;
                            }
                            for(int i = 0; i < player.c.clone.ItemMounts.length; i++) {
                                player.c.clone.ItemMounts[i] = null;
                            }
                            player.c.clone.ItemBody[1] = ItemTemplate.itemDefault(194,true);
                            player.c.clone.skill.removeAll(player.c.clone.skill);
                            Skill skill2 = new Skill();
                            player.c.clone.skill.add(skill2);
                            player.c.clone.nclass = 0;
                            player.c.clone.spoint = 0;
                            player.c.clone.ppoint = 100;
                            player.c.clone.potential0 = 15;
                            player.c.clone.potential1 = 5;
                            player.c.clone.potential2 = 5;
                            player.c.clone.potential3 = 5;

                            player.c.clone.KSkill = new byte[3];
                            player.c.clone.OSkill = new byte[5];
                            byte i;
                            for(i = 0; i < player.c.clone.KSkill.length; ++i) {
                                player.c.clone.KSkill[i] = -1;
                            }
                            for(i = 0; i < player.c.clone.OSkill.length; ++i) {
                                player.c.clone.OSkill[i] = -1;
                            }
                            player.c.clone.CSkill = -1;
                            player.c.clone.mobMe = null;
                            player.c.clone.veff.removeAll(player.c.clone.veff);
                            player.c.clone.useTiemNang = 8;
                            player.c.clone.useKyNang = 8;
                            player.c.clone.useBanhPhongLoi = 10;
                            player.c.clone.useBanhBangHoa = 10;
                            player.c.clone.countTayKyNang = 1;
                            player.c.clone.countTayTiemNang = 1;

                            if(player.c.clone.islive) {
                                player.c.clone.islive = false;
                                player.c.clone.isDie = true;
                            }
                        }
                        break;
                    }
                    default: {
                        break;
                    }
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

    public static void itemMountToBag(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && !player.c.isDie && m != null && m.reader().available() > 0) {
                byte index = m.reader().readByte();
                m.cleanup();
                if (index >= 0) {
                    byte indexItemBag = player.c.getIndexBagNotItem();
                    if (indexItemBag <= 0) {
                        player.conn.sendMessageLog(Language.NOT_ENOUGH_BAG);
                    } else if (index <= 4 && index >= 0 && player.c.get().ItemMounts[index] != null) {
                        if (index != 4 || player.c.get().ItemMounts[0] == null && player.c.get().ItemMounts[1] == null && player.c.get().ItemMounts[2] == null && player.c.get().ItemMounts[3] == null) {
                            int idMount = player.c.get().ItemMounts[index].id;
                            player.c.ItemBag[indexItemBag] = player.c.get().ItemMounts[index];
                            player.c.get().ItemMounts[index] = null;
                            m = new Message(108);
                            m.writer().writeByte(player.c.get().speed());
                            m.writer().writeInt(player.c.get().getMaxHP());
                            m.writer().writeInt(player.c.get().getMaxMP());
                            m.writer().writeShort(player.c.get().eff5buffHP());
                            m.writer().writeShort(player.c.get().eff5buffMP());
                            m.writer().writeByte(index);
                            m.writer().writeByte(indexItemBag);
                            m.writer().flush();
                            player.conn.sendMessage(m);
                            m.cleanup();
                            TileMap tileMap = player.c.tileMap;
                            int i;
                            if(tileMap != null) {
                                for (i = tileMap.players.size() - 1; i >= 0; i--) {
                                    tileMap.sendMounts(player.c.get(), tileMap.players.get(i));
                                }
                            } else if(player.c.tdbTileMap != null) {
                                for (i = player.c.tdbTileMap.players.size() - 1; i >= 0; i--) {
                                    player.c.tdbTileMap.sendMounts(player.c.get(), player.c.tdbTileMap.players.get(i));
                                }
                            }
                            if(ItemTemplate.isIdNewMounts(idMount)) {
                                player.c.get().ID_HORSE = -1;
                                player.sendInfoMeNewItem();
                                System.out.println("Update Item Mount");
                            }
                        } else {
                            player.conn.sendMessageLog("Cần phải tháo hết trang bị thú cưỡi ra trước");
                        }
                    }
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

    public static void luyenThach(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && !player.c.isDie && m != null && m.reader().available() > 0) {
                GameSrc.LuyenThach(player, m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void tinhLuyen(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && !player.c.isDie && m != null && m.reader().available() > 0) {
                GameSrc.TinhLuyen(player, m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void dichChuyen(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && !player.c.isDie && m != null && m.reader().available() > 0) {
                GameSrc.DichChuyen(player, m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void luyenNgoc(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && !player.c.isDie && m != null && m.reader().available() > 0) {
                GameSrc.ngocFeature(player, m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void sendEffect(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && !player.c.isDie && m != null && m.reader().available() > 0) {
                byte check = m.reader().readByte();
                short idTemplate = m.reader().readShort();
                switch (check) {
                    case 1: {
                        GameCanvas.getImgEffect(player.conn, idTemplate);
                        break;
                    }
                    case 2: {
                        GameCanvas.getDataEffect(player.conn, idTemplate);
                        break;
                    }
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

    public static void plusPpoint(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && m != null && m.reader().available() > 0) {
                if (player.c.get().nclass != 0) {
                    byte num = m.reader().readByte();
                    short quantity = m.reader().readShort();
                    if (quantity > 0 && quantity <= player.c.get().ppoint) {
                        switch(num) {
                            case 0:
                                player.c.get().potential0 += quantity;
                                break;
                            case 1:
                                player.c.get().potential1 += quantity;
                                break;
                            case 2:
                                player.c.get().potential2 += quantity;
                                break;
                            case 3:
                                player.c.get().potential3 += quantity;
                                break;
                            default:
                                return;
                        }
                        player.c.get().ppoint -= quantity;
                        player.c.get().upHP(player.c.get().getMaxHP());
                        player.c.get().upMP(player.c.get().getMaxMP());
                        player.loadPpoint();
                    }
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

    public static void plusSpoint(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && m != null && m.reader().available() > 0) {
                short sk = m.reader().readShort();
                byte point = m.reader().readByte();
                Skill skill = player.c.get().getSkill(sk);
                if (skill != null && player.c.get().spoint > 0 && point > 0) {
                    if (sk >= 67 && sk <= 72) {
                        player.conn.sendMessageLog("Không thể cộng điểm cho kỹ năng này.");
                    } else {
                        SkillTemplate data = SkillTemplate.Templates(sk);
                        if (skill.point + point > data.maxPoint) {
                            player.conn.sendMessageLog("Cấp tối đa là " + data.maxPoint);
                        } else if (player.c.get().level < ((SkillOptionTemplate)data.templates.get(skill.point + point)).level) {
                            player.conn.sendMessageLog("Bạn chưa đủ cấp để cộng điểm kỹ năng này");
                        } else {
                            skill.point += point;
                            player.c.get().spoint -= point;
                            player.c.get().upHP(player.c.get().getMaxHP());
                            player.c.get().upMP(player.c.get().getMaxMP());
                            player.loadSkill();
                        }
                    }
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

    public static void sortBag(Player player) {
        Message m = null;
        try {
            if(player != null && player.c != null && player.conn != null) {
                byte i;
                byte j;
                for(i = 0; i < player.c.ItemBag.length; ++i) {
                    if (player.c.ItemBag[i] != null && !player.c.ItemBag[i].isExpires && ItemTemplate.ItemTemplateId(player.c.ItemBag[i].id).isUpToUp) {
                        for(j = (byte)(i + 1); j < player.c.ItemBag.length; ++j) {
                            if (player.c.ItemBag[j] != null && !player.c.ItemBag[i].isExpires && player.c.ItemBag[j].id == player.c.ItemBag[i].id && player.c.ItemBag[j].isLock == player.c.ItemBag[i].isLock) {
                                Item item = player.c.ItemBag[i];
                                item.quantity += player.c.ItemBag[j].quantity;
                                player.c.ItemBag[j] = null;
                            }
                        }
                    }
                }

                for(i = 0; i < player.c.ItemBag.length; ++i) {
                    if (player.c.ItemBag[i] == null) {
                        for(j = (byte)(i + 1); j < player.c.ItemBag.length; ++j) {
                            if (player.c.ItemBag[j] != null) {
                                player.c.ItemBag[i] = player.c.ItemBag[j];
                                player.c.ItemBag[j] = null;
                                break;
                            }
                        }
                    }
                }
                m = new Message(-30);
                m.writer().writeByte(-107);
                m.writer().flush();
                player.conn.sendMessage(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void sortBox(Player player) {
        Message m = null;
        try {
            if(player != null && player.c != null && player.conn != null && player.menuCaiTrang == 0) {
                byte i;
                byte j;
                for(i = 0; i < player.c.ItemBox.length; ++i) {
                    if (player.c.ItemBox[i] != null && !player.c.ItemBox[i].isExpires && ItemTemplate.ItemTemplateId(player.c.ItemBox[i].id).isUpToUp) {
                        for(j = (byte)(i + 1); j < player.c.ItemBox.length; j++) {
                            if (player.c.ItemBox[j] != null && !player.c.ItemBox[i].isExpires && player.c.ItemBox[j].id == player.c.ItemBox[i].id && player.c.ItemBox[j].isLock == player.c.ItemBox[i].isLock) {
                                Item item = player.c.ItemBox[i];
                                item.quantity += player.c.ItemBox[j].quantity;
                                player.c.ItemBox[j] = null;
                            }
                        }
                    }
                }

                for(i = 0; i < player.c.ItemBox.length; ++i) {
                    if (player.c.ItemBox[i] == null) {
                        for(j = (byte)(i + 1); j < player.c.ItemBox.length; ++j) {
                            if (player.c.ItemBox[j] != null) {
                                player.c.ItemBox[i] = player.c.ItemBox[j];
                                player.c.ItemBox[j] = null;
                                break;
                            }
                        }
                    }
                }
                Service.mess_30_106(player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void xuToBox(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && m != null && m.reader().available() > 0) {
                int xu = m.reader().readInt();
                if (xu > 0 && xu <= player.c.xu) {
                    if ((long)xu + (long)player.c.xuBox > 2000000000L) {
                        player.conn.sendMessageLog("Bạn chỉ có thể cất thêm " + Util.getFormatNumber((long)xu + (long)player.c.xu - 2000000000L));
                    } else {
                        player.c.xu -= xu;
                        player.c.xuBox += xu;
                        Service.mess_30_105(player, xu);
                    }
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

    public static void xuToBag(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && m != null && m.reader().available() > 0) {
                int xu = m.reader().readInt();
                if (xu > 0 && xu <= player.c.xuBox) {
                    if ((long)xu + (long)player.c.xu > 2000000000L) {
                        player.conn.sendMessageLog("Bạn chỉ có thể rút thêm " + Util.getFormatNumber((long)xu + (long)player.c.xu - 2000000000L));
                    } else {
                        player.c.xu += xu;
                        player.c.xuBox -= xu;
                        Service.mess_30_104(player, xu);
                    }
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

    public static void sendItemInfo(Player player, Message m) {
        try {
            if(player != null && player.c != null&& player.conn != null && m != null && m.reader().available() > 0) {
                GameSrc.ItemInfo(player, m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void changeTypePk(Player player, Message m) {
        try {
            if(player != null && player.c != null  && player.conn != null&& m != null && m.reader().available() > 0) {
                if(player.c.tileMap == null) {
                    return;
                }
                if (player.c.isNhanban) {
                    player.sendAddchatYellow(Language.NOT_FOR_PHAN_THAN);
                } else if (player.c.mapid != 133 && player.c.mapid != 111 && player.c.tileMap != null && !player.c.tileMap.map.mapChienTruong() && !player.c.tileMap.map.mapLDGT() && player.c.tileMap.map.getXHD() == -1 && !player.c.tileMap.map.mapGTC()) {
                    byte pk = m.reader().readByte();
                    if (player.c.pk > 14) {
                        player.sendAddchatYellow(Language.MAX_HIEU_CHIEN);
                    } else if (pk >= 0 && pk <= 3) {
                        player.c.typepk = pk;
                        Service.ChangTypePkId(player.c, pk);
                    }
                } else {
                    player.sendAddchatYellow(Language.DO_NOT_CHANGE_PK);
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

    public static void createPatry(Player player) {
        if(player != null && player.c != null && player.conn != null && player.c.isHuman) {
            if (player.c.mapid != 111 && player.c.mapid != 133) {
                if (player.c.party == null && player.c.tileMap.numParty <= 4) {
                    Party party = new Party(player.c);
                    player.c.party = party;
                    player.c.tileMap.addParty(party);
                    Service.PlayerInParty(player.c, party);
                }
            } else {
                player.conn.sendMessageLog("Không thể sử dụng chức năng này tại đây");
            }
        }
    }

    public static void sendKeyParty(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && m != null && m.reader().available() > 0) {
                if (player.c.mapid != 111 && player.c.mapid != 133) {
                    if (player.c != null && player.c.party != null && player.c.party.charID == player.c.id) {
                        HandleController.NhuongTruongNhom(player, m);
                    }
                } else {
                    player.sendAddchatYellow("Không thể sử dụng chức năng này tại đây");
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

    public static void kickParty(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && m != null && m.reader().available() > 0) {
                if (player.c.mapid != 111 && player.c.mapid != 133) {
                    if (player.c.party != null && player.c.party.charID == player.c.id) {
                        HandleController.MoiRaKhoiNhom(player, m);
                    }
                } else {
                    player.conn.sendMessageLog("Không thể sử dụng chức năng này tại đây");
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

    public static void viewFriendList(Player player) {
        if(player != null && player.c != null && player.conn != null) {
            Service.requestFriend(player.c);
        }
    }

    public static void viewEnemiesList(Player player) {
        if(player != null && player.c != null && player.conn != null) {
            Service.requestEnemies(player.c);
        }
    }

    public static void deleteFriend(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && m != null && m.reader().available() > 0) {
                String name = m.reader().readUTF();
                short i;
                Friend friend;
                for (i = 0; i < player.c.vFriend.size(); i = (short)(i + 1)) {
                    friend = player.c.vFriend.get(i);
                    if (friend.friendName.equals(name)) {
                        player.c.vFriend.remove(i);
                        if (friend.type == 2) {
                            Service.removeEnemies(player.c, name);
                        } else {
                            Service.removeFriend(player.c, name);
                        }
                        return;
                    }
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

    public static void useSkillRevive(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && m != null && m.reader().available() > 0) {
                UseSkill.buffLive(player, m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void findParty(Player player) {
        if(player != null && player.c != null && player.conn != null && player.c.tileMap != null) {
            Service.findParty(player.c);
        }
    }

    public static void statusParty(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null &&  player.c.party != null && m != null && m.reader().available() > 0) {
                player.c.party.isLock = m.reader().readBoolean();
                player.c.party.refreshLock();
                player.sendAddchatYellow(player.c.party.isLock ? "Nhóm đã được khoá" : "Nhóm đã được mở khoá");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void pasteSkill(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && m != null && m.reader().available() > 0) {
                String t1 = m.reader().readUTF();
                String t2 = m.reader().readUTF();
                short lent = m.reader().readShort();
                switch (t1) {
                    case "KSkill": {
                        byte sid;
                        byte i;
                        Skill skill;
                        for (i = 0; i < player.c.get().KSkill.length; ++i) {
                            sid = m.reader().readByte();
                            if (sid != -1) {
                                skill = player.c.get().getSkill(sid);
                                if (skill != null && SkillTemplate.Templates(skill.id).type != 0) {
                                    player.c.get().KSkill[i] = skill.id;
                                }
                            }
                        }
                        break;
                    }
                    case "OSkill": {
                        byte i;
                        byte sid;
                        Skill skill;
                        for (i = 0; i < player.c.get().OSkill.length; ++i) {
                            sid = m.reader().readByte();
                            if (sid != -1) {
                                skill = player.c.get().getSkill(sid);
                                if (skill != null && SkillTemplate.Templates(skill.id).type != 0) {
                                    player.c.get().OSkill[i] = skill.id;
                                }
                            }
                        }
                        break;
                    }
                    default: {
                        break;
                    }
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

    public static void sendSkill(Player player, Message m) {
        try {
            if(player != null && player.c != null && m != null && m.reader().available() > 0) {
                GameSrc.sendSkill(player, m.reader().readUTF());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void inviteClan(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && m != null && m.reader().available() > 0) {
                int charId = m.reader().readInt();
                if (player.c.requestclan != -1) {
                    player.conn.sendMessageLog("Bạn đã gửi lời mời tham gia Gia tộc.");
                } else {
                    ClanManager clan = ClanManager.getClanName(player.c.clan.clanName);
                    if (clan != null && player.c.clan.typeclan > 1) {
                        if (clan.members.size() < clan.getMemMax() && player.c.tileMap != null) {
                            Char n = player.c.tileMap.getNinja(charId);
                            if (n == null) {
                                return;
                            }
                            if (n.requestclan != -1) {
                                player.conn.sendMessageLog("Đối phương đang có lời mời vào gia tộc");
                            } else if (n.clan.clanName.length() > 0) {
                                player.conn.sendMessageLog("Đối phương đã có gia tộc");
                            } else if (Math.abs(player.c.get().x - n.x) < 70 && Math.abs(player.c.get().x - n.x) < 50) {
                                player.c.requestclan = n.id;
                                player.c.deleyRequestClan = System.currentTimeMillis() + 10000L;
                                Service.mess_30_63(player, n.p);
                            } else {
                                player.sendAddchatYellow(Language.NOT_ENOUGH_DISTANCE);
                            }
                        } else {
                            player.conn.sendMessageLog(Language.MAX_QUANTITY_CLAN);
                        }
                    }
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

    public static void acppetInviteClan(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && m != null && m.reader().available() > 0) {
                int charMapid = m.reader().readInt();
                if (player.c.clan.clanName.length() > 0) {
                    player.conn.sendMessageLog("Bạn đã có gia tộc.");
                } else if(player.c.tileMap != null){
                    Char n = player.c.tileMap.getNinja(charMapid);
                    if (n != null && n.requestclan == player.c.id) {
                        ClanManager clan = ClanManager.getClanName(n.clan.clanName);
                        if (clan != null) {
                            if (clan.members.size() >= clan.getMemMax()) {
                                player.conn.sendMessageLog("Gia tộc đã đủ thành viên.");
                            } else if (Math.abs(player.c.get().x - n.x) < 70 && Math.abs(player.c.get().x - n.x) < 50) {
                                player.c.requestclan = -1;
                                player.c.clan.clanName = clan.name;
                                player.c.clan.typeclan = 0;
                                clan.members.add(player.c.clan);
                                player.setTypeClan(player.c.clan.typeclan);
                            } else {
                                player.sendAddchatYellow(Language.NOT_ENOUGH_DISTANCE);
                            }
                        }
                    } else {
                        player.sendAddchatYellow("Lời mời đã hết hạn.");
                    }
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

    public static void pleaseClan(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && m != null && m.reader().available() > 0) {
                int charID = m.reader().readInt();
                if (player.c.clan.clanName.length() > 0) {
                    player.conn.sendMessageLog("Bạn đã có gia tộc");
                } else if(player.c.tileMap != null){
                    Char n = player.c.tileMap.getNinja(charID);
                    if (n == null || n.clan.typeclan < 2) {
                        return;
                    }
                    ClanManager clan = ClanManager.getClanName(n.clan.clanName);
                    if (clan == null) {
                        return;
                    }
                    if (clan.members.size() >= clan.getMemMax()) {
                        player.conn.sendMessageLog(Language.MAX_QUANTITY_CLAN);
                    } else if (player.c.requestclan != -1) {
                        player.conn.sendMessageLog("Bạn đã gửi yêu cầu gia nhập trước rồi");
                    } else if (Math.abs(player.c.x - n.x) < 70 && Math.abs(player.c.x - n.x) < 50) {
                        player.c.requestclan = n.id;
                        player.c.deleyRequestClan = System.currentTimeMillis() + 15000L;
                        Service.sendPleaseClan(n, player.c.id);
                    } else {
                        player.sendAddchatYellow(Language.NOT_ENOUGH_DISTANCE);
                    }
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

    public static void acppetPleaseClan(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && m != null && m.reader().available() > 0) {
                int charID = m.reader().readInt();
                ClanManager clan = ClanManager.getClanName(player.c.clan.clanName);
                if (clan != null && player.c.clan.typeclan >= 2 && player.c.tileMap != null) {
                    Char n = player.c.tileMap.getNinja(charID);
                    if (n != null && n.requestclan == player.c.id) {
                        if (clan.members.size() >= clan.getMemMax()) {
                            player.conn.sendMessageLog(Language.MAX_QUANTITY_CLAN);
                        } else if (n.clan.clanName.length() > 0) {
                            player.conn.sendMessageLog("Đối phương đã có gia tộc.");
                        } else if (Math.abs(player.c.get().x - n.x) < 70 && Math.abs(player.c.get().x - n.x) < 50) {
                            n.requestclan = -1;
                            n.clan.clanName = clan.name;
                            n.clan.typeclan = 0;
                            clan.members.add(n.clan);
                            n.p.setTypeClan(n.clan.typeclan);
                        } else {
                            player.sendAddchatYellow(Language.NOT_ENOUGH_DISTANCE);
                        }
                    } else {
                        player.sendAddchatYellow("Lời mời đã hết hạn.");
                    }
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

    public static void divedeItem(Player player, Message m) {
        try {
            if(player != null && player.c != null && player.conn != null && m != null && m.reader().available() > 0) {
                ItemTemplate.divedeItem(player, m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void selectNinja(Player player, Message m) {
        try {
            if(player != null && player.conn != null) {
                byte i;
                if (m != null && player.c == null ) {
                    String name = m.reader().readUTF();
                    m.cleanup();
                    for(i = 0; i < player.sortNinja.length; ++i) {
                        if (name.equals(player.sortNinja[i])) {
                            player.c = Char.setup(player, player.sortNinja[i]);
                            if (player.c != null) {
                                Client.gI().put(player.c);
                                player.sendInfo();
                                Manager.alert.sendAlert(player);
                                m = new Message(-23);
                                m.writer().writeInt(player.c.get().id);
                                m.writer().writeUTF("Trò chơi dành cho người từ 18 tuổi trở lên. Chơi game quá 180 phút 1 Ngay sẽ ảnh hưởng xấu đến sức khoẻ.");
                                m.writer().flush();
                                player.conn.sendMessage(m);
                                m.cleanup();
                                break;
                            }
                        }
                    }
                }
                else {
                    m = new Message(-28);
                    m.writer().writeByte(-126);
                    byte lent = 0;
                    for(i = 0; i < player.sortNinja.length; ++i) {
                        if (player.sortNinja[i] != null) {
                            lent++;
                        }
                    }
                    m.writer().writeByte(lent);
                    ResultSet red = null;
                    try {
                        for(byte j = 0; j < player.sortNinja.length; ++j) {
                            if (player.sortNinja[j] != null) {
                                synchronized(Server.LOCK_MYSQL) {
                                    red = SQLManager.stat.executeQuery("SELECT `gender`,`name`,`class`,`level`,`head`,`ItemBody` FROM `ninja` WHERE `name`LIKE'" + player.sortNinja[j] + "';");
                                    if (red != null && red.first()) {
                                        m.writer().writeByte(red.getByte("gender"));
                                        m.writer().writeUTF(red.getString("name"));
                                        m.writer().writeUTF(Server.manager.NinjaS[red.getByte("class")]);
                                        m.writer().writeByte(red.getInt("level"));
                                        short head = (short)red.getByte("head");
                                        short weapon = -1;
                                        short body = -1;
                                        short leg = -1;
                                        JSONArray jar = (JSONArray)JSONValue.parse(red.getString("ItemBody"));
                                        Item[] itembody = new Item[32];
                                        JSONObject job = null;
                                        if (jar != null) {
                                            byte k;
                                            byte index;
                                            for(k = 0; k < jar.size(); ++k) {
                                                job = (JSONObject)jar.get(k);
                                                index = Byte.parseByte(job.get("index").toString());
                                                itembody[index] = ItemTemplate.parseItem(jar.get(k).toString());
                                                job.clear();
                                            }
                                        }
                                        if (itembody[11] != null) {
                                            head = ItemTemplate.ItemTemplateId(itembody[11].id).part;
                                            if (itembody[11].id == 745) {
                                                head = 264;
                                            }
                                        }
                                        if (itembody[1] != null) {
                                            weapon = ItemTemplate.ItemTemplateId(itembody[1].id).part;
                                        }
                                        if (itembody[2] != null) {
                                            body = ItemTemplate.ItemTemplateId(itembody[2].id).part;
                                        }
                                        if (itembody[6] != null) {
                                            leg = ItemTemplate.ItemTemplateId(itembody[6].id).part;
                                        }
                                        if (head == 258 || head == 264) {
                                            body = (short)(head + 1);
                                            leg = (short)(head + 2);
                                        }
                                        m.writer().writeShort(head);
                                        m.writer().writeShort(weapon);
                                        m.writer().writeShort(body);
                                        m.writer().writeShort(leg);
                                        if (job != null && !job.isEmpty()) {
                                            job.clear();
                                        }
                                        if (jar != null && !jar.isEmpty()) {
                                            jar.clear();
                                        }
                                        red.close();
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if(red != null) {
                            red.close();
                        }
                    }
                    m.writer().flush();
                    player.conn.sendMessage(m);
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

    public static void createNinja(Player player, Message m) {
        try {
            if(player != null && player.conn != null && m != null & m.reader().available() > 0) {
                if (player.sortNinja[2] == null) {
                    String name = m.reader().readUTF().toLowerCase();
                    byte gender = m.reader().readByte();
                    byte head = m.reader().readByte();
                    if (Util.CheckString(name, "^[a-zA-Z0-9]+$") && name.length() >= 6 && name.length() <= 15) {
                        if (player.sortNinja[0] != null) {
                            player.conn.sendMessageLog("Để tránh tạo nhiều clone gây lag server, không tạo thêm nhân vật!");
                        } else {
                            synchronized(Server.LOCK_MYSQL) {
                                ResultSet red = SQLManager.stat.executeQuery("SELECT `id` FROM `ninja` WHERE `name`LIKE'" + name + "';");
                                if (red != null && red.first()) {
                                    player.conn.sendMessageLog("Tên nhân vật đã tồn tại!");
                                    return;
                                }
                                red.close();
                                SQLManager.stat.executeUpdate("INSERT INTO ninja(`name`,`gender`,`head`,`ItemBag`,`ItemBox`,`ItemBST`,`ItemCaiTrang`,`ItemBody`,`ItemMounts`) VALUES (\"" + name + "\"," + gender + "," + head + ",'[]','[]','[]','[]','[]','[]');");
                                byte i = 0;
                                while(true) {
                                    if (i < player.sortNinja.length) {
                                        if (player.sortNinja[i] != null) {
                                            i++;
                                            continue;
                                        }
                                        player.sortNinja[i] = name;
                                    }
                                    break;
                                }
                            }
                            player.flush();
                            HandleController.selectNinja(player, null);
                        }
                    } else {
                        player.conn.sendMessageLog("Tên nhân vật chỉ chứa các ký tự từ a-z,0-9 và chiều dài từ 6 đến 15 ký tự!");
                    }
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

    

    public static void reciveImage(Player player, Message m) {
        try {
            if(player != null && player.conn != null && m != null && m.reader().available() > 0) {
                GameSrc.reciveImage(player, m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void logClan(Player player) {
        if(player != null && player.conn != null && player.c != null && player.c.clan != null) {
            ClanManager clan = ClanManager.getClanName(player.c.clan.clanName);
            if (clan != null) {
                clan.LogClan(player);
            }
        }
    }

    public static void infoClan(Player player) {
        if(player != null && player.conn != null && player.c != null && player.c.clan != null) {
            ClanManager clan = ClanManager.getClanName(player.c.clan.clanName);
            if (clan != null) {
                clan.requestClanInfo(player);
            }
        }
    }


    public static void infoClanMember(Player player) {
        if(player != null && player.conn != null && player.c != null && player.c.clan != null) {
            ClanManager clan = ClanManager.getClanName(player.c.clan.clanName);
            if (clan != null) {
                clan.requestClanMember(player);
            }
        }
    }

    public static void infoClanItem(Player player) {
        if(player != null && player.conn != null && player.c != null && player.c.clan != null) {
            ClanManager clan = ClanManager.getClanName(player.c.clan.clanName);
            if (clan != null) {
                clan.requestClanItem(player);
            }
        }
    }

    public static void sendMapInfo(Player player, Message m) {
        try {
            if(player != null && player.conn != null && player.c != null && m != null && m.reader().available() > 0) {
                int idMap = m.reader().readByte();
                if(idMap < 0) {
                    idMap += 256;
                }
                Service.sendMapInfo(player, Manager.getMapid( idMap ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void reciveImageMOB(Player player, Message m) {
        try {
            if(player != null && player.conn != null && player.c != null && m != null && m.reader().available() > 0) {
                GameSrc.reciveImageMOB(player, m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void setClanAlert(Player player, Message m) {
        try {
            if(player != null && player.conn != null && player.c != null && player.c.clan != null && m != null && m.reader().available() > 0) {
                ClanManager clan = ClanManager.getClanName(player.c.clan.clanName);
                if (clan != null) {
                    clan.setAlert(player, m);
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

    public static void changeClanType(Player player, Message m) {
        try {
            if(player != null && player.conn != null && player.c != null && player.c.clan != null && m != null && m.reader().available() > 0) {
                ClanManager clan = ClanManager.getClanName(player.c.clan.clanName);
                if (clan != null) {
                    clan.changeClanType(player, m);
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

    public static void moveOutClan(Player player, Message m) {
        try {
            if(player != null && player.conn != null && player.c != null && player.c.clan != null && m != null && m.reader().available() > 0) {
                ClanManager clan = ClanManager.getClanName(player.c.clan.clanName);
                if (clan != null) {
                    clan.moveOutClan(player, m);
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

    public static void outClan(Player player) {
        if(player != null && player.conn != null && player.c != null && player.c.clan != null) {
            ClanManager clan = ClanManager.getClanName(player.c.clan.clanName);
            if (clan != null) {
                clan.OutClan(player);
            }
        }
    }

    public static void upLevelClan(Player player) {
        if(player != null && player.conn != null && player.c != null && player.c.clan != null) {
            ClanManager clan = ClanManager.getClanName(player.c.clan.clanName);
            if (clan != null) {
                clan.clanUpLevel(player);
            }
        }
    }

    public static void inputCoinClan(Player player, Message m) {
        try {
            if(player != null && player.conn != null && player.c != null && player.c.clan != null && m != null && m.reader().available() > 0) {
                ClanManager clan = ClanManager.getClanName(player.c.clan.clanName);
                if (clan != null) {
                    clan.inputCoinClan(player, m);
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

    public static void convertUpgrade(Player player, Message m) {
        try {
            if(player != null && player.conn != null && player.c != null && m != null && m.reader().available() > 0) {
                GameSrc.doConvertUpgrade(player, m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void accpetInviteLDGT(Player player, Message m) {
        try {
            if(player != null && player.conn != null && player.c != null && player.c.tileMap != null && m != null && m.reader().available() > 0) {
                if(player.c.tileMap.map.lanhDiaGiaToc != null && player.c.tileMap.map.mapLDGT()) {
                    if(player.c.tileMap.map.lanhDiaGiaToc.ninjas.size() >= 24) {
                        player.conn.sendMessageLog("Đã đạt số thành viên tham gia Lãnh Địa Gia Tộc tối đa, không thể mời thêm.");
                        return;
                    }
                    if(!player.c.tileMap.map.lanhDiaGiaToc.start) {
                        player.inviteToLDT(m);
                    } else {
                        player.conn.sendMessageLog("Đã mở cửa Lãnh Địa Gia Tộc, không thể mời thêm thành viên.");
                    }
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

    public static void rewardedCave(Player player) {
        if(player != null && player.c != null && player.conn != null) {
            int num = player.c.pointCave / 10;
            if (player.c.isHangDong6x == 1) {
                num = player.c.pointCave / 20;
            }
            if (num > 0) {
                if (player.c.getBagNull() == 0) {
                    player.conn.sendMessageLog(Language.NOT_ENOUGH_BAG);
                    return;
                }
                Item item;
                if (player.c.level < 50) {
                    item = new Item();
                    item.id = 272;
                } else if (player.c.level < 90) {
                    item = new Item();
                    item.id = 282;
                } else {
                    item = new Item();
                    item.id = 647;
                }
                item.quantity = num;
                player.c.addItemBag(true, item);
                player.c.pointCave = 0;
                player.c.isHangDong6x = 0;
                player.c.caveID = -1;
                if(player.c.party != null && player.c.party.charID == player.c.id) {
                    player.c.party.cave = null;
                }
                if (player.c.bagCaveMax < num) {
                    player.c.bagCaveMax = num;
                    player.c.itemIDCaveMax = item.id;
                }
            }
        }
    }

    public static void rewardedCT(Player player) {
        if(player != null && player.c != null && player.conn != null) {
            if(player.c.getBagNull() < 4) {
                player.sendAddchatYellow(Language.NOT_ENOUGH_BAG);
                return;
            }
            switch (player.c.isTakePoint) {
                case 1: {
                    Service.addItemToBagNinja(player.c, 275, true, false, 1, false, -1);
                    Service.addItemToBagNinja(player.c, 276, true, false, 1, false, -1);
                    Service.addItemToBagNinja(player.c, 277, true, false, 1, false, -1);
                    Service.addItemToBagNinja(player.c, 278, true, false, 1, false, -1);
                    player.c.upxuMessage(500000);
                    break;
                }
                case 2: {
                    Service.addItemToBagNinja(player.c, 275, true, false, 10, false, -1);
                    Service.addItemToBagNinja(player.c, 276, true, false, 10, false, -1);
                    Service.addItemToBagNinja(player.c, 277, true, false, 10, false, -1);
                    Service.addItemToBagNinja(player.c, 278, true, false, 10, false, -1);
                    player.c.upxuMessage(3000000);
                    break;
                }
                case 3: {
                    Service.addItemToBagNinja(player.c, 275, true, false, 7, false, -1);
                    Service.addItemToBagNinja(player.c, 276, true, false, 7, false, -1);
                    Service.addItemToBagNinja(player.c, 277, true, false, 7, false, -1);
                    Service.addItemToBagNinja(player.c, 278, true, false, 7, false, -1);
                    player.c.upxuMessage(2000000);
                    break;
                }
                case 4: {
                    Service.addItemToBagNinja(player.c, 275, true, false, 5, false, -1);
                    Service.addItemToBagNinja(player.c, 276, true, false, 5, false, -1);
                    Service.addItemToBagNinja(player.c, 277, true, false, 5, false, -1);
                    Service.addItemToBagNinja(player.c, 278, true, false, 5, false, -1);
                    player.c.upxuMessage(1000000);
                    break;
                }
                default: {
                    break;
                }
            }
            player.c.isTakePoint = 0;
        }
    }

    public static void luckyValue(Player player, Message m) {
        try {
            if(player != null && player.conn != null && player.c != null && m != null && m.reader().available() > 0) {
                GameSrc.LuckValue(player, m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void acceptClanDun(Player player, Message m) {
        try {
            if(player != null && player.conn != null && player.c != null && m != null && m.reader().available() > 0) {
                System.out.println("Accpet Clan Dun ---------------------- ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void openItemClanLevel(Player player) {
        if(player != null && player.conn != null && player.c != null && player.c.clan != null) {
            ClanManager clan = ClanManager.getClanName(player.c.clan.clanName);
            if (clan != null) {
                clan.openItemLevel(player);
            }
        }
    }

    public static void sendItemClanToMember(Player player, Message m) {
        try {
            if(player != null && player.conn != null && player.c != null && player.c.clan != null && m != null && m.reader().available() > 0) {
                ClanManager clan = ClanManager.getClanName(player.c.clan.clanName);
                if (clan != null) {
                    clan.sendClanItem(player, m);
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

    public static void useItemClan(Player player, Message m) {
        try {
            if(player != null && player.conn != null && player.c != null && player.c.clan != null && m != null && m.reader().available() > 0) {
                ClanManager clan = ClanManager.getClanName(player.c.clan.clanName);
                if (clan != null) {
                    clan.useClanItem(player, m);
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

    public static void thienDiaBang(Player player, Message m) {
        try {
            if(player != null && player.conn != null && player.c != null && m != null && m.reader().available() > 0) {
                byte choose = m.reader().readByte();
                switch (choose) {
                    case 0: {
                        String playername = m.reader().readUTF();
                        m.cleanup();
                        if (playername.equals(player.c.name)) {
                            Service.sendInfoPlayers(player, player);
                        } else {
                            Char n = Client.gI().getNinja(playername);
                            if (n == null) {
                                n = HandleController.setupNinjaBot(playername);
                                player.viewChar = n;
                            }
                            Service.sendInfoPlayers(player, n.p);
                        }
                        break;
                    }
                    case 1: {
                        if(player.c.countTDB <= 0) {
                            player.conn.sendMessageLog("Hôm nay bạn đã hết lần thách đấu, hãy quay lại vào Ngay mai");
                            return;
                        }
                        if(player.c.delayJoinTDB > System.currentTimeMillis()) {
                            Service.chatNPC(player, (short) 4, "Con vừa tranh đấu xong, nghỉ mệt đi, 1 phút sau quay lại thi đấu tiếp nhé.");
                            return;
                        }
                        synchronized (ThienDiaBangManager.diaBangList) {
                            synchronized (ThienDiaBangManager.thienBangList) {
                                String playername = m.reader().readUTF();
                                if(playername.equals(player.c.name)) {
                                    player.sendAddchatYellow("Bạn không thể thách đấu chính mình");
                                    return;
                                }
                                ThienDiaData data = null;
                                if(ThienDiaBangManager.diaBangList.containsKey(playername)) {
                                    data = ThienDiaBangManager.diaBangList.get(playername);
                                } else if(ThienDiaBangManager.thienBangList.containsKey(playername)) {
                                    data = ThienDiaBangManager.thienBangList.get(playername);
                                }
                                if(data == null) {
                                    player.sendAddchatYellow("Không tìm thấy đối thủ.");
                                    return;
                                }
                                if(data.getType() == 0) {
                                    player.sendAddchatYellow("Đối thủ đang trong cuộc so tài, không thể thách đấu.");
                                    return;
                                } else {
                                    data.setType(0);
                                    Char ninjaBot = Client.gI().getNinja(playername);
                                    if(ninjaBot != null) {
                                        ninjaBot.flush();
                                    }
                                    ninjaBot = HandleController.setupNinjaBot(playername);
                                    if(ninjaBot != null) {
                                        ninjaBot.p.getMobMe();
                                        if (ninjaBot.get().level >= 90) {
                                            Skill ski = null;
                                            for(int i2 = 67; i2 <= 72; ++i2) {
                                                ski = ninjaBot.get().getSkill(i2);
                                                if (ski != null) {
                                                    ninjaBot.clone = CloneCharacter.getClone(ninjaBot);
                                                    ninjaBot.clone.id = -100000-ninjaBot.id;
                                                    ninjaBot.clone.islive = true;
                                                    ninjaBot.clone.open(System.currentTimeMillis() + 60000 * ninjaBot.get().getPramSkill(i2), ninjaBot.get().getPramSkill(71));
                                                    break;
                                                }
                                            }
                                        }

                                        ThienDiaBang thienDiaBang = new ThienDiaBang(player.c, ninjaBot);
                                        thienDiaBang.map[0].area[0].EnterMap0WithXY(ninjaBot, (short)506, (short)264);
                                        player.c.tileMap.leave(player);
                                        thienDiaBang.map[0].area[0].EnterMap0WithXY(player.c, (short)265, (short)264);
                                        player.setEffect(14,0,10000,0);

                                    } else {
                                        player.sendAddchatYellow("Hiện tại không thể thách đấu với đối phương.");
                                    }
                                }

                            }
                        }
                        break;
                    }
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

    public static Char setupNinjaBot(String name) {
        try {
            Char ninjaBot = Char.getChar(name);
            ninjaBot.get().c = ninjaBot;
            ninjaBot.id += -999;
            ninjaBot.p = new Player();
            ninjaBot.p.id = ninjaBot.id;
            ninjaBot.isHuman = true;
            ninjaBot.isNhanban =false;
            ninjaBot.get().typepk = 3;
            ninjaBot.get().hp = ninjaBot.get().getMaxHP();
            ninjaBot.get().mp = ninjaBot.get().getMaxMP();
            ninjaBot.get().isDie = false;
            ninjaBot.isBot = true;
            ninjaBot.get().setSpeed((byte)12);
            ninjaBot.p.c = ninjaBot;
            return ninjaBot;
        } catch (Exception e) {
            return null;
        }
    }


}
