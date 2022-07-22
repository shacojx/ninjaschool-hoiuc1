package com.hoiuc.stream;

import com.hoiuc.assembly.Player;
import com.hoiuc.server.Manager;

public class ClearLogin implements Runnable{
    public Player player = null;
    public void run() {
        try {
            Manager.isClearCloneLogin = true;
            if (Manager.isClearCloneLogin) {
                synchronized (Client.gI().conns) {
                    Player player;
                    for(int i = 0; i < Client.gI().conns.size(); i++){
                        if(Client.gI().conns.get(i) != null) {
                            player = Client.gI().conns.get(i).player;
                            if(player != null && player.c == null) {
                                Client.gI().kickSession(player.conn);
                            } else if(player == null) {
                                Client.gI().kickSession(player.conn);
                            }
                        }

                    }
                }

                Manager.isClearCloneLogin = false;
                if(this.player != null &&Manager.isClearCloneLogin) {
                    this.player.conn.sendMessageLog("Đã thực hiện Clear Clone Login");
                }
                System.out.println("-------------------------- Clear clone login: "+Manager.isClearCloneLogin+" -----------------------------------" +!Thread.currentThread().isInterrupted());
                return;
            }
        } catch (Exception e) {
            Manager.isClearCloneLogin = false;
            e.printStackTrace();
            return;
        }
    }
}
