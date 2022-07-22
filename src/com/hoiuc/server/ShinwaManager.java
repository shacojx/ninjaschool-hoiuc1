package com.hoiuc.server;

import com.hoiuc.io.SQLManager;
import com.hoiuc.stream.Server;
import com.hoiuc.template.ItemTemplate;
import com.hoiuc.template.ShinwaTemplate;
import org.json.simple.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShinwaManager {
    public static HashMap<Integer, List<ShinwaTemplate>> entrys = new HashMap<>();

    public static synchronized void update(int itemID, int idList) {
        try {
            List<ShinwaTemplate> listItem = ShinwaManager.entrys.get(idList);
            if(listItem.get(itemID) != null) {
                ShinwaManager.entrys.get(-1).add(listItem.remove(itemID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void close(){
        ShinwaManager.flush();
        System.out.println("Close/Flush ShinwaManager");
    };

    public static synchronized void flush(){
        try {
            synchronized(Server.LOCK_MYSQL) {
                synchronized (ShinwaManager.entrys) {
                    for(Map.Entry<Integer, List<ShinwaTemplate>> entry : ShinwaManager.entrys.entrySet()) {
                        Integer key = entry.getKey();
                        List<ShinwaTemplate> list = entry.getValue();
                        JSONArray jarr = new JSONArray();
                        for(ShinwaTemplate item : list) {
                            JSONArray jarr2  = new JSONArray();
                            jarr2.add(ItemTemplate.ObjectItem(item.getItem()));
                            jarr2.add(item.getTimeStart());
                            jarr2.add(item.getSeller());
                            jarr2.add(item.getPrice());
                            jarr.add(jarr2);
                        }
                        String sqlSET = "`data`='" + jarr.toJSONString() + "'";
                        jarr.clear();
                        SQLManager.stat.executeUpdate("UPDATE `shinwa` SET " + sqlSET + " WHERE `id`=" + key + ";");
                    }
                }
            }
        }
        catch (Exception var10) {
            var10.printStackTrace();
        }
    };

}
