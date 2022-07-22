package com.hoiuc.server;

import com.hoiuc.assembly.Char;
import com.hoiuc.assembly.Player;
import com.hoiuc.io.Message;
import com.hoiuc.io.SQLManager;
import com.hoiuc.io.Util;
import com.hoiuc.stream.Client;
import com.hoiuc.stream.Server;

import java.io.IOException;
import java.util.ArrayList;

public class Lucky extends Thread{
    public String title;
    public short time;
    public int totalxu;
    public int maxtotalxu;
    public short numplayers;
    public byte type;
    private int minxu;
    private int maxxu;
    private boolean open;
    private short settime;
    private boolean start;
    private String winerinfo;
    private boolean runing;
    private ArrayList<Players> players;

    protected Lucky(String title, byte type, short time, int minxu, int maxxu, int maxtotal) {
        this.title = null;
        this.time = 120;
        this.totalxu = 0;
        this.numplayers = 0;
        this.type = 1;
        this.open = true;
        this.settime = 0;
        this.start = false;
        this.winerinfo = "Chưa có thông tin";
        this.runing = true;
        this.players = new ArrayList<Players>();
        this.title = title;
        this.type = type;
        this.settime = time;
        this.time = time;
        this.minxu = minxu;
        this.maxxu = maxxu;
        this.maxtotalxu = maxtotal;
    }

    protected int getJoinxu(String njname) {
        for (short i = 0; i < this.players.size(); ++i) {
            if (this.players.get(i).name.equals(njname)) {
                return this.players.get(i).joinxu;
            }
        }
        return 0;
    }

    protected synchronized void joinLuck(Player p, int joinxu) throws IOException {
        if (!this.open || joinxu <= 0) {
            return;
        }
        if (joinxu > p.c.xu) {
            p.conn.sendMessageLog("Bạn không đủ xu.");
            return;
        }
        if (this.totalxu > this.maxtotalxu) {
            p.conn.sendMessageLog("Số lượng xu tối đa là " + Util.getFormatNumber(this.maxtotalxu));
            return;
        }
        Players p2 = null;
        for (short i = 0; i < this.players.size(); ++i) {
            if (this.players.get(i).name.equals(p.c.name)) {
                p2 = this.players.get(i);
                break;
            }
        }
        if (p2 == null && (joinxu > this.maxxu || joinxu < this.minxu)) {
            p.conn.sendMessageLog("Bạn chỉ có thể đặt cược từ " + Util.getFormatNumber(this.minxu) + " đến " + Util.getFormatNumber(this.maxxu) + " xu.");
            return;
        }
        if (p2 == null) {
            p2 = new Players();
            p2.user = p.username;
            p2.name = p.c.name;
            ++this.numplayers;
            this.players.add(p2);
        }
        if (p2.joinxu + joinxu > this.maxxu) {
            p.conn.sendMessageLog("Bạn chỉ có thể đặt cược tối đa " + Util.getFormatNumber(this.maxxu - p2.joinxu) + " xu.");
            return;
        }
        Players players = p2;
        players.joinxu += joinxu;
        p.c.upxuMessage(-joinxu);
        this.totalxu += joinxu;
        if (this.numplayers == 2 && !this.start) {
            this.begin();
            Char ns = Client.gI().getNinja(this.players.get(0).name);
            if (ns != null) {
                this.luckMessage(ns.p);
            }
        }
        this.luckMessage(p);
    }

    private void turned() throws Exception {
        int i;
        for (i = 0; i < this.players.size(); ++i) {
            int j;
            String tempuser;
            String tempname;
            for (j = i + 1; j < this.players.size(); ++j) {
                if (this.players.get(i).joinxu < this.players.get(j).joinxu) {
                    tempuser = this.players.get(j).user;
                    tempname = this.players.get(j).name;
                    int tempxu = this.players.get(j).joinxu;
                    this.players.get(j).user = this.players.get(i).user;
                    this.players.get(j).name = this.players.get(i).name;
                    this.players.get(j).joinxu = this.players.get(i).joinxu;
                    this.players.get(i).user = tempuser;
                    this.players.get(i).name = tempname;
                    this.players.get(i).joinxu = tempxu;
                }
            }
        }
        Players p = null;
        for (Players player : this.players) {
            if (this.percentWin(player.name) > Util.nextInt(100)) {
                p = player;
                break;
            }
        }
        if (p == null) {
            p = this.players.get(Util.nextInt(this.players.size()));
        }
        long xuwin = this.totalxu;
        if (this.numplayers > 1) {
            xuwin = xuwin * 95L / 100L;
        }
        this.numplayers = 0;
        this.totalxu = 0;
        Char ns = Client.gI().getNinja(p.name);
        if (ns != null) {
            ns.upxuMessage(xuwin);
        }
        else {
            synchronized (Server.LOCK_MYSQL) {
                SQLManager.stat.executeUpdate("UPDATE `ninja` SET `xu`=`xu`+" + xuwin + " WHERE `name`='" + p.name + "';");
            }
        }
        Manager.serverChat("Server", "Chúc mừng " + p.name.toUpperCase() + " đã chiến thắng " + Util.getFormatNumber(xuwin) + " xu trong trò chơi Vòng xoay may mắn");
        this.winerinfo = "Người vừa chiến thắng:\n" + ((this.type == 0) ? ("c" + Util.nextInt(10)) : "") + "" + p.name + "\nS\u1ed1 xu th\u1eafng: " + Util.getFormatNumber(xuwin) + " xu\nS\u1ed1 xu tham gia: " + Util.getFormatNumber(p.joinxu) + " xu";
        this.players.removeAll(this.players);
        Thread.sleep(1000L);
        this.time = this.settime;
        this.start = false;
        this.open = true;
    }

    private void begin() {
        this.time = this.settime;
        this.start = true;
    }

    protected float percentWin(String njname) {
        short i;
        for (i = 0; i < this.players.size(); ++i) {
            if (this.players.get(i).name.equals(njname)) {
                return this.players.get(i).joinxu * 100.0f / this.totalxu;
            }
        }
        return 0.0f;
    }

    @Override
    public void run() {
        while (this.runing) {
            try {
                Thread.sleep(1000L);
                if (this.time <= 0 || !this.start) {
                    continue;
                }
                --this.time;
                if (this.time == 0) {
                    this.turned();
                }
                else {
                    if (this.time >= 10) {
                        continue;
                    }
                    this.open = false;
                }
                continue;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            break;
        }
        System.out.println("Close Thread Lucky");
    }

    protected void luckMessage(Player p) throws IOException {
        Message m = new Message(53);
        m.writer().writeUTF("typemoi");
        m.writer().writeUTF(this.title);
        m.writer().writeShort(this.time);
        m.writer().writeUTF(Util.getFormatNumber(this.totalxu) + " xu");
        m.writer().writeShort((short)this.percentWin(p.c.name));
        m.writer().writeUTF((Util.parseString("" + this.percentWin(p.c.name), ".") == null) ? "0" : Util.parseString("" + this.percentWin(p.c.name), "."));
        m.writer().writeShort(this.numplayers);
        m.writer().writeUTF(this.winerinfo);
        m.writer().writeByte(this.type);
        m.writer().writeUTF(Util.getFormatNumber(this.getJoinxu(p.c.name)));
        m.writer().flush();
        p.conn.sendMessage(m);
        m.cleanup();
    }

    protected void close() {
        try {
            this.runing = false;
            if (this.numplayers > 0) {
                this.turned();
            }
            this.title = null;
            this.winerinfo = null;
            this.players = null;
            System.gc();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private class Players
    {
        String user;
        String name;
        int joinxu;

        private Players() {
            this.user = null;
            this.name = null;
            this.joinxu = 0;
        }
    }
}
