package com.hoiuc.stream;

import com.hoiuc.assembly.ClanManager;
import com.hoiuc.assembly.Player;
import com.hoiuc.server.Manager;
import com.hoiuc.server.Rank;
import com.hoiuc.server.ThienDiaBangManager;

public class SaveData implements Runnable{
    public Player player = null;

    public void run() {
        try {
            Manager.isSaveData = true;
            if (Manager.isSaveData) {
                Player player;
                int i;
                synchronized (Client.gI().conns) {
                    for (i=0; i<Client.gI().conns.size(); i++) {
                        if(Client.gI().conns.get(i) != null && Client.gI().conns.get(i).player != null) {
                            player = Client.gI().conns.get(i).player;
                            player.flush();
                            if (player.c != null) {
                                player.c.flush();
                                if (player.c.clone != null) {
                                    player.c.clone.flush();
                                }
                            }
                        }
                    }
                }
                synchronized (ClanManager.entrys) {
                    for (i=0; i<ClanManager.entrys.size(); i++) {
                        if(ClanManager.entrys.get(i) != null) {
                            ClanManager.entrys.get(i).flush();
                        }
                    }
                }
                synchronized (ThienDiaBangManager.thienDiaBangManager) {
                    ThienDiaBangManager.thienDiaBangManager[0].autoFlush();
                    ThienDiaBangManager.thienDiaBangManager[1].autoFlush();
                }
                Rank.init();

                synchronized (this) {
                    try {
                        Runtime backup = Runtime.getRuntime();
                        String time = String.valueOf(System.currentTimeMillis());
                        String backUpPart = Manager.backup_part +"\\"+ Manager.mysql_database_ninja + time +".sql";
                        String command= "cmd.exe /c "
                                + "\"\""+Manager.mysql_part+"\"  "
                                + " --user="+Manager.mysql_user
                                + " --password="+Manager.mysql_pass
                                + " --host="+Manager.mysql_host
                                + " --protocol=tcp "
                                + " --port="+Manager.mysql_port
                                + " --default-character-set=utf8 "
                                + " --single-transaction=TRUE "
                                + " --routines "
                                + " --events "
                                + "\""+Manager.mysql_database_ninja
                                +"\" "
                                + ">"
                                + " \""
                                + backUpPart
                                + "\""
                                + " \"";
                        backup.exec(command);

                        backUpPart = Manager.backup_part + "\\"+ Manager.mysql_database_data + time +".sql";
                        command= "cmd.exe /c "
                                + "\"\""+Manager.mysql_part+"\"  "
                                + " --user="+Manager.mysql_user
                                + " --password="+Manager.mysql_pass
                                + " --host="+Manager.mysql_host
                                + " --protocol=tcp "
                                + " --port="+Manager.mysql_port
                                + " --default-character-set=utf8 "
                                + " --single-transaction=TRUE "
                                + " --routines "
                                + " --events "
                                + "\""+Manager.mysql_database_data
                                +"\" "
                                + ">"
                                + " \""
                                + backUpPart
                                + "\""
                                + " \"";
                        backup.exec(command);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Manager.isSaveData = false;
            }
            System.out.println("Auto Save Data-----------------------------------------------Auto Save Data");
            if(player != null) {
                player.conn.sendMessageLog("Auto Save Data Success");
            }
            return;
        } catch (Exception e) {
            Manager.isSaveData = false;
            e.printStackTrace();
            return;
        }
    }
}
