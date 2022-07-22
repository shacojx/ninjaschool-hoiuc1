package com.hoiuc.stream;

import com.hoiuc.assembly.ClanManager;
import com.hoiuc.assembly.Map;
import com.hoiuc.assembly.Player;
import com.hoiuc.io.Message;
import com.hoiuc.io.Util;
import com.hoiuc.server.Manager;
import com.hoiuc.server.Service;
import com.hoiuc.server.Session;
import com.hoiuc.server.ThienDiaBangManager;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class RunTimeServer extends Thread{
    private static int[] hoursAutoSaveData = new int[] { 1, 5, 7, 11, 15, 19, 21, 23 };
    private static int[] hoursRefreshBoss = new int[] { 8, 12, 14, 20, 22 };
    private static int[] hoursRefreshBossVDMQ = new int[] { 1, 3, 9, 11, 13, 17, 19, 21, 23 };
    private static boolean[] isRefreshBoss = new boolean[] { false, false, false, false, false, false };
    private static boolean[] isRefreshBossVDMQ = new boolean[] { false, false, false, false, false, false, false, false, false, false, false, false };
    private static short[] mapBossVDMQ = new short[] { 141, 142, 143 };
    private static short[] mapBoss45 = new short[] { 14, 15, 16, 34, 35, 52, 68 };
    private static short[] mapBoss55 = new short[] { 44, 67 };
    private static short[] mapBoss65 = new short[] { 24, 41, 45, 59 };
    private static short[] mapBoss75 = new short[] { 18, 36, 54 };
    private static final short[]mapBossSKTet = new short[]{2,28,39};
    private static final int[] hoursRefreshBossSKTet = new int[]{1,3,5,7,9,10,13,15,17,19,22,23};
    private static final boolean[] isRefreshBossSKTet = new boolean[]{false,false,false,false,false,false,false,false,false,false,false,false};

    @Override
    public void run() {
        try {
            ClanManager clan;
            int i;
            Calendar rightNow;
            int hour;
            int min;
            int sec;
            int j;
            byte k;
            Map map;
            Player player;
            while (Server.running) {
                synchronized (ClanManager.entrys) {
                    for (i = ClanManager.entrys.size() - 1; i >= 0; --i) {
                        if(ClanManager.entrys.get(i) != null) {
                            clan = ClanManager.entrys.get(i) ;
                            if (!Util.isSameWeek(Date.from(Instant.now()), Util.getDate(clan.week))) {
                                clan.payfeesClan();
                            }
                        }
                    }
                }

                synchronized (ThienDiaBangManager.thienDiaBangManager) {
                    if(ThienDiaBangManager.thienDiaBangManager[0] != null) {
                        if (!Util.isSameWeek(Date.from(Instant.now()), Util.getDate2(ThienDiaBangManager.thienDiaBangManager[0].getWeek()))) {
                            ThienDiaBangManager.register = false;
                            ThienDiaBangManager.resetThienDiaBang();
                        }
                    }
                }

                rightNow = Calendar.getInstance();
                hour = rightNow.get(11);
                min = rightNow.get(12);
                sec = rightNow.get(13);

                if(hour % 24 == 0 && min == 0 && sec == 0) {
                    if(ChienTruong.chienTruong != null) {
                        ChienTruong.chienTruong.finish();
                    }
                    ChienTruong.chienTruong30 = false;
                    ChienTruong.chienTruong50 = false;
                    ChienTruong.finish = false;
                    ChienTruong.start = false;
                    ChienTruong.pointHacGia = 0;
                    ChienTruong.pointBachGia = 0;
                    ChienTruong.pheWin = -1;
                    ChienTruong.bxhCT.clear();
                    ChienTruong.chienTruong = null;
                }

                if(hour % 23 == 0 && min >= 44) {
                    ThienDiaBangManager.register = false;
                }
                if(hour % 24 == 0 && min >= 5) {
                    ThienDiaBangManager.register = true;
                }

                if(ChienTruong.chienTruong != null) {
                    if(ChienTruong.bxhCT.size() > 0) {
                        ChienTruong.bxhCT = ChienTruong.sortBXH(ChienTruong.bxhCT);
                        Service.updateCT();
                    }
                }

                if(hour == 19 && min == 00 && sec == 0) {
                    if(ChienTruong.chienTruong != null) {
                        ChienTruong.chienTruong.finish();
                    }
                    if(ChienTruong.chienTruong == null) {
                        Manager.serverChat("Server", "Chiến trường lv30 đã mở báo danh, hãy nhanh chân đến báo danh chuẩn bị chiến đấu.");
                        ChienTruong.chienTruong30 = true;
                        ChienTruong.chienTruong50 = false;
                        ChienTruong.chienTruong = new ChienTruong();
                        ChienTruong.finish = false;
                        ChienTruong.start = false;
                        ChienTruong.pointHacGia = 0;
                        ChienTruong.pointBachGia = 0;
                        ChienTruong.pheWin = -1;
                        ChienTruong.bxhCT.clear();
                    }
                }

                if(ChienTruong.chienTruong != null && hour == 19 && min == 30 && sec == 0) {
                    ChienTruong.start = true;
                }

                if(ChienTruong.chienTruong != null && hour == 20 && min == 30 && sec == 0 && ChienTruong.start) {
                    ChienTruong.chienTruong.finish();
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(0);//thay id npc
                        m.writer().writeUTF("Nếu bạn đợi tôi từ bỏ, bạn sẽ đợi tôi mãi mãi"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(1);//thay id npc
                        m.writer().writeUTF("Cho đến khi tôi chết tôi vẫn sẽ theo đuổi ước mơ của mình"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(2);//thay id npc
                        m.writer().writeUTF("Người đàn ông không bao giờ rút ra lời nói, đó là nhẫn đạo của tôi"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(3);//thay id npc
                        m.writer().writeUTF("Tôi phải tin vào chính mình, tin rằng tôi là người mà họ tin tưởng"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(4);//thay id npc
                        m.writer().writeUTF("Tôi không thích những người nói dối mình"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(5);//thay id npc
                        m.writer().writeUTF("Tương lai nằm trong tay tôi và tôi phải đạt được nó"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(6);//thay id npc
                        m.writer().writeUTF("Không có con đường tắt để trở thành Hokage!"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(7);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(8);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(9);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(10);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(11);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(12);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(13);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(14);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(15);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(16);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(17);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(18);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(19);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(20);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(21);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(22);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(23);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(24);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(25);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(26);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(27);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(28);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(29);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(30);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(31);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(32);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(33);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(34);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(35);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(36);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(37);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(38);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(39);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(40);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(41);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(42);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(43);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
if (sec %5 == 0) {
                    Message m = null;
                    try {
                        m = new Message(38);
                        m.writer().writeShort(43);//thay id npc
                        m.writer().writeUTF("Đừng kéo lời của bạn, bởi vì điều đó sẽ khiến bạn bị hủy hoại"); // thay nội dung chat
                        m.writer().flush();
                        Client.gI().NinjaMessage(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(hour == 21 && min == 0 && sec == 0) {
                    if(ChienTruong.chienTruong != null) {
                        ChienTruong.chienTruong.finish();
                    }
                    if(ChienTruong.chienTruong == null) {
                        Manager.serverChat("Server", "Chiến trường lv50 đã mở báo danh, hãy nhanh chân đến báo danh chuẩn bị chiến đấu.");
                        ChienTruong.chienTruong50 = true;
                        ChienTruong.chienTruong30 = false;
                        ChienTruong.chienTruong = new ChienTruong();
                        ChienTruong.finish = false;
                        ChienTruong.start = false;
                        ChienTruong.pointHacGia = 0;
                        ChienTruong.pointBachGia = 0;
                        ChienTruong.pheWin = -1;
                        ChienTruong.bxhCT.clear();
                    }
                }

                if(ChienTruong.chienTruong != null && hour == 21 && min == 30 && sec == 0) {
                    ChienTruong.start = true;
                }

                if(ChienTruong.chienTruong != null && hour == 22 && min == 30 && sec == 0 && ChienTruong.start) {
                    ChienTruong.chienTruong.finish();
                }

                 if(sec == 10) {              
                     
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
              //      System.out.println(" Clear clone login");
                 }                      
                 }
                if((min == 58 || min == 30)&& sec == 0) {
                    Manager.serverChat("Server", "Hệ thống đang tự động cập nhật dữ liệu người chơi có thể gây lag!");
                    SaveData saveData = new SaveData();
                    Thread t1 = new Thread(saveData);
                    t1.start();
                    if(!Manager.isSaveData) {
                        t1 = null;
                        saveData= null;
                    }
                }

                for(j = 0; j < this.hoursRefreshBossVDMQ.length; ++j) {
                    if (this.hoursRefreshBossVDMQ[j] == hour) {
                        if (!this.isRefreshBossVDMQ[j]) {
                            String textchat = "BOSS đã xuất hiện tại:";
                            for (k = 0; k < this.mapBossVDMQ.length; ++k) {
                                map = Manager.getMapid(this.mapBossVDMQ[k]);
                                if (map != null) {
                                    map.refreshBoss(Util.nextInt(15, 28));
                                    if(k==0) {
                                        textchat = textchat + " " + map.template.name;
                                    } else {
                                        textchat = textchat + ", " + map.template.name;
                                    }
                                    this.isRefreshBossVDMQ[j] = true;
                                }
                            }
                            Manager.chatKTG(textchat);
                        }
                    } else {
                        this.isRefreshBossVDMQ[j] = false;
                    }
                }
                for (j = 0; j < this.hoursRefreshBoss.length; ++j) {
                    if (this.hoursRefreshBoss[j] == hour) {
                        if (!this.isRefreshBoss[j]) {
                            String textchat = "Thần thú đã xuất hiện tại:";
                            for (k = 0; k < Util.nextInt(1, 2); ++k) {
                                map = Manager.getMapid(this.mapBoss75[Util.nextInt(this.mapBoss75.length)]);
                                if (map != null) {
                                    map.refreshBoss(Util.nextInt(15, 28));
                                    textchat = textchat + " " + map.template.name;
                                    this.isRefreshBoss[j] = true;
                                }
                            }
                            for (k = 0; k < Util.nextInt(1, 2); ++k) {
                                map = Manager.getMapid(this.mapBoss65[Util.nextInt(this.mapBoss65.length)]);
                                if (map != null) {
                                    map.refreshBoss(Util.nextInt(15, 28));
                                    textchat = textchat + ", " + map.template.name;
                                    this.isRefreshBoss[j] = true;
                                }
                            }
                            for (k = 0; k < Util.nextInt(1, 2); ++k) {
                                map = Manager.getMapid(this.mapBoss55[Util.nextInt(this.mapBoss55.length)]);
                                if (map != null) {
                                    map.refreshBoss(Util.nextInt(15, 28));
                                    textchat = textchat + ", " + map.template.name;
                                    this.isRefreshBoss[j] = true;
                                }
                            }
                            for (k = 0; k < Util.nextInt(1, 2); ++k) {
                                map = Manager.getMapid(this.mapBoss45[Util.nextInt(this.mapBoss45.length)]);
                                if (map != null) {
                                    map.refreshBoss(Util.nextInt(15, 28));
                                    textchat = textchat + ", " + map.template.name;
                                    this.isRefreshBoss[j] = true;
                                }
                            }
//                                    for (byte k = 0; k < Server.mapBossVDMQ.length; ++k) {
//                                        Map map = Manager.getMapid(Server.mapBossVDMQ[k]);
//                                        if (map != null) {
//                                            map.refreshBoss(util.nextInt(15, 30));
//                                            textchat = textchat + ", " + map.template.name;
//                                            Server.isRefreshBoss[j] = true;
//                                        }
//                                    }
                            Manager.chatKTG(textchat);
                        }
                    }
                    else {
                        this.isRefreshBoss[j] = false;
                    }
                }
                //thông báo Boss Tết ra ...
                for (int i1 = 0; i1 < hoursRefreshBossSKTet.length; i1++) {
                        if (hoursRefreshBossSKTet[i1] == hour) {
                            if (!isRefreshBossSKTet[i1]) {
                                String textchat = "Boss Chuột Canh Tý đã xuất hiện tại";
                                for (byte j1 = 0; j1 < mapBossSKTet.length; j1++) {
                                    map = Manager.getMapid(mapBossSKTet[j1]);
                                    if (map != null) {
                                        map.refreshBossTet(Util.nextInt(25,30));
                                        textchat += ", "+map.template.name;
                                        this.isRefreshBossSKTet[i1] = true;
                                        
                                    }
                                }
                                Manager.chatKTG(textchat);
                            }
                        } else {
                            isRefreshBossSKTet[i1] = false;
                        }
                          
                    }
                Thread.sleep(1000L);
            }
            return;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
