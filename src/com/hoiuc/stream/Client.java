package com.hoiuc.stream;

import com.hoiuc.assembly.Char;
import com.hoiuc.assembly.Player;
import com.hoiuc.io.Message;
import com.hoiuc.server.HandleController;
import com.hoiuc.server.Service;
import com.hoiuc.server.Session;

import java.util.ArrayList;
import java.util.HashMap;

public class Client implements Runnable{
    public static Object LOCK = new Object();
    protected static Client instance;
    private boolean runing = true;
    public static  HashMap<String, Long> timeWaitLogin = new HashMap();
    public ArrayList<Session> conns = new ArrayList<Session>();
    public HashMap<Integer, Session> conns_id = new HashMap<Integer, Session>();
    public HashMap<Integer, Player> players_id = new HashMap<Integer, Player>();
    public HashMap<String, Player> players_uname = new HashMap<String, Player>();
    public HashMap<Integer, Char> ninjas_id = new HashMap<Integer, Char>();
    public HashMap<String, Char> ninjas_name = new HashMap<String, Char>();
    private Thread runClients = new Thread(this);

    public Client()  {
        synchronized (this.LOCK) {
            this.runClients.start();
        }
    }

    public static Client gI() {
        if (Client.instance == null) {
            Client.instance = new Client();
        }
        return Client.instance;
    }

    public void sendMessage(Message m) {
        synchronized (this.conns) {
            for (int i = this.conns.size() - 1; i >= 0; --i) {
                if(this.conns.get(i) != null) {
                    this.conns.get(i).sendMessage(m);
                }
            }
        }
        if(m != null) {
            m.cleanup();
        }
    }

    public void NinjaMessage(Message m) {
        synchronized (this.conns) {
            int i;
            for (i = 0; i < this.conns.size(); i++) {
                if (this.conns.get(i) != null && this.conns.get(i).player != null && this.conns.get(i).player.c != null) {
                    this.conns.get(i).sendMessage(m);
                }
            }
        }
        if(m != null) {
            m.cleanup();
        }
    }

    public void put(Session conn) {
        if (!conns_id.containsValue(conn)) {
            this.conns_id.put(conn.id, conn);
        } else {
            if(conn != null) {
                this.kickSession(conn);
            }
        }
        if (!conns.contains(conn)) {
            this.conns.add(conn);
        } else {
            if(conn != null) {
                this.kickSession(conn);
            }
        }
    }

    public void put(Player p) {
        if (!players_id.containsKey(p.id)) {
            this.players_id.put(p.id, p);
        } else {
            if(p != null && p.conn != null) {
                this.kickSession(p.conn);
            }
        }
        if (!players_uname.containsKey(p.username)) {
            this.players_uname.put(p.username, p);
        } else {
            if(p != null && p.conn != null) {
                this.kickSession(p.conn);
            }
        }
    }

    public void put(Char n) {
        if (!ninjas_id.containsKey(n.id)) {
            this.ninjas_id.put(n.id, n);
        } else {
            if(n != null && n.p != null && n.p.conn != null) {
                this.kickSession(n.p.conn);
            }
        }
        if (!ninjas_name.containsKey(n.name)) {
            this.ninjas_name.put(n.name, n);
        } else {
            if(n != null && n.p != null && n.p.conn != null) {
                this.kickSession(n.p.conn);
            }
        }
    }

    public void remove(Session conn) {
        if (conns_id.containsKey(conn.id)) {
            this.conns_id.remove(conn.id);
        }
        if (conns.contains(conn)) {
            this.conns.remove(conn);
        }
        if (conn.player != null) {
            if(!Client.timeWaitLogin.containsKey(conn.player.username)) {
                Client.timeWaitLogin.put(conn.player.username, System.currentTimeMillis() + 3000L);
            }
            this.remove(conn.player);
        }
    }

    private void remove(Player p) {
        if (players_id.containsKey(p.id)) {
            this.players_id.remove(p.id);
        }
        if (players_uname.containsKey(p.username)) {
            this.players_uname.remove(p.username);
        }
        if (p.c != null) {
            this.remove(p.c);
        }
        p.flush();
        p.close();
    }

    private void remove(Char n) {
        if (ninjas_id.containsKey(n.id)) {
            this.ninjas_id.remove(n.id);
        }
        if (ninjas_name.containsKey(n.name)) {
            this.ninjas_name.remove(n.name);
        }
        int i;
        Char _char;
        try {
            if(n.party != null && n.party.charID != -1 && n != null && n.party.aChar != null) {
                if(n.party.aChar.contains(n)) {
                    if (n.isInDun && n.dunId != -1 && n.party.charID == n.id && n != null) {
                        for (i = 0; i < n.party.aChar.size(); i++) {
                            if (n.party.aChar.get(i) != null) {
                                _char = n.party.aChar.get(i);
                                if (_char.id != n.id) {
                                    n.party.removePlayer(_char.id);
                                }
                            }
                        }
                        n.party.removePlayer(n.id);
                    } else {
                        HandleController.RoiNhom(n.p);
                    }
                }
            }
        }   catch (Exception e) {}

        if(n.tileMap != null && n.tileMap.map.id == 133 && n.tileMap.map.dun != null) {
            if(n.tileMap.map.dun.c1.id == n.id && n.tileMap.map.id == 133) {
                n.tileMap.map.dun.c1 = null;
                n.tileMap.map.dun.team1.remove(n);
                n.tileMap.map.dun.check1();
            } else if(n.tileMap.map.dun.c2.id == n.id && n.tileMap.map.id == 133) {
                n.tileMap.map.dun.c2 = null;
                n.tileMap.map.dun.team2.remove(n);
                n.tileMap.map.dun.check1();
            }
        }
        else if(n.isInDun && n.dunId != -1) {
            Dun dun = null;
            if (Dun.duns.containsKey(n.dunId)) {
                dun = Dun.duns.get(n.c.dunId);

                if (dun.c1 != null && dun.c1.id == n.id) {
                    dun.c1 = null;
                }
                if (dun.c2 != null && dun.c2.id == n.id) {
                    dun.c2 = null;
                }
                if (dun.team1.size() > 0 && dun.team1 != null) {
                    synchronized (dun.team1) {
                        if (dun.team1.contains(n)) {
                            dun.team1.remove(n);
                        }
                    }
                }

                if(dun.team2.size() > 0 && dun.team2 != null) {
                    synchronized (dun.team2) {
                        if (dun.team2.contains(n)){
                            dun.team2.remove(n);
                        }
                    }
                }

                if(dun.viewer.size() > 0 && dun.viewer != null) {
                    synchronized (dun.viewer) {
                        if (dun.viewer.contains(n)){
                            dun.viewer.remove(n);
                        }
                    }
                }
            }
            Service.ChangTypePkId(n, (byte)0);
            n.isInDun = false;
            n.dunId = -1;
        }

        if(n.tileMap != null && n.p != null) {
            synchronized (n.tileMap.players) {
                n.tileMap.leave(n.p);
            }
        } else if(n.tdbTileMap != null && n.p != null) {
            synchronized (n.tdbTileMap.players) {
                n.tdbTileMap.leave(n.p);
            }
        }

        if(n.isNhanban) {
            n.p.exitNhanBan(true);
        }
        if (n.clone != null) {
            n.clone.flush();
            n.clone.close();
        }
        n.flush();
        n.close();
    }

    public Session getConn(int id) {
        return this.conns_id.get(id);
    }

    public Player getPlayer(int id) {
        return this.players_id.get(id);
    }

    public Player getPlayer(String uname) {
        return this.players_uname.get(uname);
    }

    public Char getNinja(int id) {
        return this.ninjas_id.get(id);
    }

    public Char getNinja(String name) {
        return this.ninjas_name.get(name);
    }

    public int conns_size() {
        return this.conns_id.size();
    }

    public int players_size() {
        return this.players_id.size();
    }

    public int ninja_size() {
        return this.ninjas_id.size();
    }

    public void kickSession(Session conn) {
        this.remove(conn);
        conn.disconnect();
    }

    public void Clear() {
        while (!this.conns.isEmpty()) {
            this.kickSession(this.conns.get(0));
        }
    }

    private void update() {
        Session conn;
        int i;
        for (i = 0; i < conns.size(); i++) {
            if(conns.get(i) != null) {
                conn = conns.get(i);
                if (conn.outdelay > 0) {
                    conn.outdelay--;
                    if (conn.outdelay == 0) {
                        kickSession(conn);
                    }
                }
            }
        }
    }

    public void close() {
        synchronized (this.LOCK) {
            this.runing = false;
            Client.instance = null;
            this.LOCK.notify();
        }
    }
    public void removeClient(Session cl) {
        synchronized (conns) {
            conns.remove(cl);
        }
    }

    public void run() {
        long l1;
        long l2;
        while (runing) {
            try {
                synchronized (this.LOCK) {
                    l1 = System.currentTimeMillis();
                    this.update();
                    l2 = System.currentTimeMillis() - l1;
                    this.LOCK.wait(Math.abs(300L - l2));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
