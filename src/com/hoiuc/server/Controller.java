package com.hoiuc.server;

import com.hoiuc.assembly.Player;
import com.hoiuc.io.IMessageHandler;
import com.hoiuc.io.Message;
import com.hoiuc.io.Util;
import com.hoiuc.stream.Server;

public class Controller implements IMessageHandler {

    @Override
    public void processMessage(Session var1, Message var2) {
        Controller.onMessage(var1, var2);
    }

    @Override
    public void onConnectionFail(Session var1) {}

    @Override
    public void onDisconnected(Session var1) {
        if(var1 != null) {
            var1.outdelay = 5;
        }
    }

    @Override
    public void onConnectOK(Session var1) {}

    public static void onMessage(Session session, Message message) {
        if(session == null) {
            return;
        }
        byte num1 = message.getCommand();
        Util.Debug("Check cmd ---> " + num1);
        Player player = session.player;
        switch (num1) {
            case -30: {
                Controller.messageSubCommand(message, player);
                break;
            }
            case -29: {
                Controller.messageNotLogin(message, session);
                break;
            }
            case -28: {
                Controller.messageNotMap(message, player);
                break;
            }
            case -27: {
                session.hansakeMessage();
                break;
            }

            //Public chat
            case -23: {
                HandleController.publicChat(player, message);
                break;
            }
            //Private chat
            case -22: {
                HandleController.privateChat(player, message);
                break;
            }
            //World chat
            case -21: {
                HandleController.worldChat(player, message);
                break;
            }
            //Party chat
            case -20: {
                HandleController.partyChat(player, message);
                break;
            }
            //Clan chat
            case -19: {
                HandleController.clanChat(player, message);
                break;
            }
            //Next map
            case -17: {
                HandleController.nextMap(player, message);
                break;
            }
            //Pick Item
            case -14: {
                HandleController.pickItem(player, message);
                break;
            }
            //Leave Item To Character
            case -12: {
                HandleController.leaveItemToCharacter(player, message);
                break;
            }
            //Wake Up Die Return
            case -10: {
                HandleController.wakeUpDieReturn(player);
                break;
            }
            //Die Return
            case -9: {
                HandleController.dieReturn(player);
                break;
            }
            //Move
            case 1: {
                HandleController.move(player, message);
                break;
            }
            //Fight All
            case 4:
            case 73: {
                HandleController.fightAll(player, message);
                break;
            }
            //Use Item
            case 11: {
                HandleController.useItem(player, message);
                break;
            }
            //Use Item Change Map
            case 12: {
                HandleController.useItemChangeMap(player, message);
                break;
            }
            //Buy Item
            case 13: {
                HandleController.buyItem(player, message);
                break;
            }
            //Sell Item
            case 14: {
                HandleController.sellItem(player, message);
                break;
            }
            //Item Body To Bag
            case 15: {
                HandleController.itemBodyToBag(player, message);
                break;
            }
            //Item Box To Bag
            case 16: {
                HandleController.itemBoxToBag(player, message);
                break;
            }
            //Item Bag To Box
            case 17: {
                HandleController.itemBagToBox(player, message);
                break;
            }
            //Luyện đá xu
            case 19: {
                HandleController.stoneSmelting(player, message, true);
                break;
            }
            //Luyện đá yên
            case 20: {
                HandleController.stoneSmelting(player, message, false);
                break;
            }
            //Nâng cấp trang bị
            case 21: {
                HandleController.upgradeEquipment(player, message);
                break;
            }
            //Tách trang bị
            case 22: {
                HandleController.splitEquipment(player, message);
                break;
            }
            //Xin vào nhóm
            case 23: {
                HandleController.pleaseParty(player, message);
                break;
            }
            //Đồng ý xin vào nhóm
            case 24: {
                HandleController.acceptPleaseParty(player, message);
                break;
            }
            //Request Players
            case 25: {
                break;
            }
            //Chọn khu
            case 28: {
                HandleController.selectZone(player, message);
                break;
            }
            //Chọn Menu NPC
            case 29: {
                HandleController.selectMenuNpc(player, message);
                break;
            }
            //Mở khu vực
            case 36: {
                HandleController.openZone(player);
                break;
            }
            //Mở menu NPC
            case 40: {
                HandleController.openMenuNpc(player, message);
                break;
            }
            //Dùng Kỹ Năng
            case 41:
            case 74: {
                HandleController.useSkill(player, message);
                break;
            }
            //Lấy thông tin Item
            case 42: {
                HandleController.requestItemInfo(player, message);
                break;
            }
            //Mời giao dịch
            case 43: {
                HandleController.inviteTrade(player, message);
                break;
            }
            //Đồng ý giao dịch
            case 44: {
                HandleController.accpetTrade(player, message);
                break;
            }
            //Khoá giao dịch
            case 45: {
                HandleController.lockTrade(player, message);
                break;
            }
            //Hoàn tất giao dịch
            case 46: {
                HandleController.submitTrade(player);
                break;
            }
            //Chọn menu npc
            case 47: {
                HandleController.selectMenuNpcTileMap(player, message);
                break;
            }
            //Huỷ giao dịch
            case 56: {
                HandleController.closeTrade(player);
                break;
            }
            //Dừng tải
            case 57: {
                HandleController.closeLoad(player);
                break;
            }
            //Thêm bạn bè
            case 59: {
                HandleController.addFriend(player, message);
                break;
            }
            //Tấn công quái
            case 60: {
                HandleController.attackMob(player, message);
                break;
            }
            //Tấn công người
            case 61: {
                HandleController.attackNinja(player, message);
                break;
            }
            //Mời tỷ thí
            case 65: {
                HandleController.inviteSolo(player, message);
                break;
            }
            //Đống ý tỷ thí
            case 66: {
                HandleController.accpetSolo(player, message);
                break;
            }
            //Cừu sát
            case 68: {
                HandleController.startKillNinja(player, message);
                break;
            }
            //Mời vào nhóm
            case 79: {
                HandleController.inviteToParty(player, message);
                break;
            }
            //Đồng ý lời mời vào nhóm
            case 80: {
                HandleController.accpetInviteToParty(player, message);
                break;
            }
            //Rời nhóm
            case 83: {
                HandleController.outParty(player);
                break;
            }
            //Xử lý nhập dữ liệu
            case 92: {
                HandleController.inputValue(player, message);
                break;
            }
            //Xem thông tin người chơi
            case 93: {
                HandleController.viewInfoNinja(player, message);
                break;
            }
            //Xem trang bị người chơi
            case 94: {
                HandleController.viewItemNinja(player, message);
                break;
            }
            //Đồng ý lôi đài
            case 99: {
                HandleController.accpetDun(player, message);
                break;
            }
            //Vào xem lôi đài
            case 100: {
                HandleController.viewDun(player, message);
                break;
            }

            //Bán đấu giá
            case 102: {
                HandleController.sendItemToAuction(player, message);
                break;
            }
            case 103: {
                break;
            }
            case 104: {
                HandleController.viewItemAuction(player, message);
                break;
            }
            //mua đồ đấu giá
            case 105: {
                HandleController.buyItemAuction(player, message);
                break;
            }
            //Gửi giao diện Yes/No
            case 107: {
                HandleController.yesNoDlg(player, message);
                break;
            }
            //Chuyển đồ từ Thứ cưỡi vào hành trang
            case 108: {
                HandleController.itemMountToBag(player, message);
                break;
            }
            //Luyện thạch
            case 110: {
                HandleController.luyenThach(player, message);
                break;
            }
            //Chuyển đồ từ Thứ cưỡi vào hành trang
            case 111: {
                HandleController.tinhLuyen(player, message);
                break;
            }
            //Dịch chuyển trang bị
            case 112: {
                HandleController.dichChuyen(player, message);
                break;
            }
            //Luyện ngọc
            case 121: {
                HandleController.thienDiaBang(player, message);
                break;
            }
            //Luyện ngọc
            case 124: {
                HandleController.luyenNgoc(player, message);
                break;
            }
            //Gửi Effect
            case 125: {
                HandleController.sendEffect(player, message);
                break;
            }
            default: {
                break;
            }
        }
        if(message != null) {
            message.cleanup();
        }
    }

    private static void messageSubCommand(Message message, Player player) {
        try {
            if(message.reader().available() < 0) {
                return;
            }
            byte b = message.reader().readByte();
            Util.Debug("-30 ------------------------> " + b);
            switch (b) {
                case -127: {
                    break;
                }
                case -120: {
                    break;
                }
                //Cộng điểm tiềm năng 1
                case -109: {
                    HandleController.plusPpoint(player, message);
                    break;
                }
                //Cộng điểm kỹ năng
                case -108: {
                    HandleController.plusSpoint(player, message);
                    break;
                }
                //Sắp xếp hành trang
                case -107: {
                    HandleController.sortBag(player);
                    break;
                }
                //Sắp xếp rương
                case -106: {
                    HandleController.sortBox(player);
                    break;
                }
                //Gửi xu
                case -105: {
                    HandleController.xuToBox(player, message);
                    break;
                }
                //Rút xu
                case -104: {
                    HandleController.xuToBag(player, message);
                    break;
                }
                //Xem thông tin item
                case -103: {
                    HandleController.sendItemInfo(player, message);
                    break;
                }
                //Xem thông tin item
                case -93: {
                    HandleController.changeTypePk(player, message);
                    break;
                }
                //Tạo nhóm
                case -88: {
                    HandleController.createPatry(player);
                    break;
                }
                //Nhường chức nhóm trưởng
                case -87: {
                    HandleController.sendKeyParty(player, message);
                    break;
                }
                //Mời ra khỏi nhóm
                case -86: {
                    HandleController.kickParty(player, message);
                    break;
                }
                //Xem danh sách bạn bè
                case -85: {
                    HandleController.viewFriendList(player);
                    break;
                }
                //Xem danh sách bạn bè
                case -84: {
                    HandleController.viewEnemiesList(player);
                    break;
                }
                //Xoá bạn bè/thù địch
                case -83:
                case -82: {
                    HandleController.deleteFriend(player, message);
                    break;
                }
                //Dùng skil hồi sinh
                case -79: {
                    HandleController.useSkillRevive(player, message);
                    break;
                }
                //Tim nhóm
                case -77: {
                    HandleController.findParty(player);
                }
                //Khoá nhóm
                case -76: {
                    HandleController.statusParty(player, message);
                    break;
                }
                //Gán kỹ năng
                case -67: {
                    HandleController.pasteSkill(player, message);
                    break;
                }
                //Gửi thông tin kỹ năng
                case -65: {
                    HandleController.sendSkill(player, message);
                    break;
                }
                //Mời vào gia tộc
                case -63: {
                    HandleController.inviteClan(player, message);
                    break;
                }
                //Chấp nhận mời vào gia tộc
                case -62: {
                    HandleController.acppetInviteClan(player, message);
                    break;
                }
                //Xin vào gia tộc
                case -61: {
                    HandleController.pleaseClan(player, message);
                    break;
                }
                //Đồng ý xin vào gia tộc
                case -60: {
                    HandleController.acppetPleaseClan(player, message);
                    break;
                }
                default: {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(message != null) {
                message.cleanup();
            }
        }
    }

    private static void messageNotLogin(Message message, Session session) {
        try {
            if(message.reader().available() < 0) {
                return;
            }
            byte b = message.reader().readByte();
            Util.Debug("CMD -29 --------------> " + b);
            switch (b) {
                case -127 : {
                    if(session.player == null) {
                        session.loginGame(message);
                    }
                    break;
                }
                case -125 : {
                    session.setConnect(message);
                    break;
                }
                //Tách số lượng vật phẩm
                case -85 : {
                    if (session.player == null) {
                        break;
                    }
                    HandleController.divedeItem(session.player, message);
                    break;
                }
                default: {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(message != null) {
                message.cleanup();
            }
        }
    }

    private static void messageNotMap(Message message, Player player) {
        try {
            if(message.reader().available() < 0) {
                return;
            }
            byte b = message.reader().readByte();
            Util.Debug("-28 ------------------> " + b);
            switch (b) {
                //Chọn nhân vật
                case -126: {
                    HandleController.selectNinja(player, message);
                    break;
                }
                //Tạo mới nhân vật
                case -125: {
                    HandleController.createNinja(player, message);
                    break;
                }
                //Gửi main data
                case -122: {
                    Server.manager.sendData(player);
                   break;
                }
                case -121: {
                    Service.sendMap(player);
                   break;
                }
                //Gdata skill
                case -120: {
                    Server.manager.sendSkill(player);
                   break;
                }
                //Gdata item
                case -119: {
                    Service.sendItem(player);
                   break;
                }
                //Gửi hình ảnh
                case -115: {
                    HandleController.reciveImage(player, message);
                    break;
                }
                //Ghi thông tin LOG clan
                case -114: {
                    HandleController.logClan(player);
                    break;
                }
                //thông tin clan
                case -113: {
                    HandleController.infoClan(player);
                    break;
                }
                //thông tin thành viên clan
                case -112: {
                    HandleController.infoClanMember(player);
                    break;
                }
                //thông tin kho clan
                case -111: {
                    HandleController.infoClanItem(player);
                    break;
                }
                case -109: {
                    HandleController.sendMapInfo(player, message);
                    break;
                }
                //gửi hình ảnh mob
                case -108: {
                    HandleController.reciveImageMOB(player, message);
                    break;
                }
                //Chọn nhân vật null
                case -101: {
                    HandleController.selectNinja(player, null);
                    break;
                }
                //Viết thông báo Clan
                case -95: {
                    HandleController.setClanAlert(player, message);
                    break;
                }
                //Thay đổi kiểu Clan
                case -94: {
                    HandleController.changeClanType(player, message);
                    break;
                }
                //Rời clan
                case -93: {
                    HandleController.moveOutClan(player, message);
                    break;
                }
                case -92: {
                    HandleController.outClan(player);
                    break;
                }
                //Tăng cấp clan
                case -91: {
                    HandleController.upLevelClan(player);
                    break;
                }
                //Đóng góp clan
                case -90: {
                    HandleController.inputCoinClan(player, message);
                    break;
                }
                //Hoán chuyển trang bị
                case -88: {
                    HandleController.convertUpgrade(player, message);
                    break;
                }
                //Mời lãnh địa gia tộc
                case -87: {
                    HandleController.accpetInviteLDGT(player, message);
                    break;
                }
                //tách item
                case -85: {
                    HandleController.divedeItem(player, message);
                    break;
                }
                //Nhận thưởng rương hang động
                case -82: {
                    HandleController.rewardedCave(player);
                    break;
                }
                //Nhận thưởng điểm chiến trường
                case -79: {
                    HandleController.rewardedCT(player);
                    break;
                }
                //Vòng Xoay May Mắn
                case -72: {
                    HandleController.luckyValue(player, message);
                    break;
                }
                //Đồng ý gia tộc chiến
                case -68: {
                    HandleController.acceptClanDun(player, message);
                    break;
                }
                //Mở item clan
                case -62: {
                    HandleController.openItemClanLevel(player);
                    break;
                }
                //gửi item cho thành viên
                case -61: {
                    HandleController.sendItemClanToMember(player, message);
                    break;
                }
                //Dùng item clan
                case -60: {
                    HandleController.useItemClan(player, message);
                    break;
                }
                default: {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(message != null) {
                message.cleanup();
            }
        }
    }


}
