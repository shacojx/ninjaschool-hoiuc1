package com.hoiuc.stream;

import com.hoiuc.server.ShinwaManager;
import com.hoiuc.template.ShinwaTemplate;

import java.util.List;

public class Shinwa extends Thread{

    @Override
    public void run() {
        try {
            int i;
            int j;
            ShinwaTemplate item;
            List<ShinwaTemplate> list = null;
            while (Server.running) {
                synchronized (ShinwaManager.entrys) {
                    for (i = 0; i <= 11; i++) {
                        list = ShinwaManager.entrys.get(i);
                        if (list != null) {
                            for (j = 0; j < list.size(); ++j) {
                                item = list.get(j);
                                if (item.isExpired()) {
                                    ShinwaManager.update(j, i);
                                }
                            }
                        }
                    }
                    ShinwaManager.flush();
                }
                Thread.sleep(3000L);
            }
            return;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
