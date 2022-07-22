package com.hoiuc.assembly;

import com.hoiuc.io.Message;
import com.hoiuc.template.ItemTemplate;

public class Admission {
    public static void Admission(Player player, byte nclass) {
        Item item;
        switch(nclass) {
            case 1:
                item = ItemTemplate.itemDefault(94, true);
                item.sys = 1;
                player.c.addItemBag(true, item);
                player.c.addItemBag(true, ItemTemplate.itemDefault(40, true));
                break;
            case 2:
                item = ItemTemplate.itemDefault(114, true);
                item.sys = 1;
                player.c.addItemBag(true, item);
                player.c.addItemBag(true, ItemTemplate.itemDefault(49, true));
                break;
            case 3:
                item = ItemTemplate.itemDefault(99, true);
                item.sys = 2;
                player.c.addItemBag(true, item);
                player.c.addItemBag(true, ItemTemplate.itemDefault(58, true));
                break;
            case 4:
                item = ItemTemplate.itemDefault(109, true);
                item.sys = 2;
                player.c.addItemBag(true, item);
                player.c.addItemBag(true, ItemTemplate.itemDefault(67, true));
                break;
            case 5:
                item = ItemTemplate.itemDefault(104, true);
                item.sys = 3;
                player.c.addItemBag(true, item);
                player.c.addItemBag(true, ItemTemplate.itemDefault(76, true));
                break;
            case 6:
                item = ItemTemplate.itemDefault(119, true);
                item.sys = 3;
                player.c.addItemBag(true, item);
                player.c.addItemBag(true, ItemTemplate.itemDefault(85, true));
        }
        player.c.clan.nClass = nclass;
        player.c.get().nclass = nclass;
        player.c.get().skill.clear();
        player.c.get().upHP(player.c.get().getMaxHP());
        player.c.get().upMP(player.c.get().getMaxMP());
        player.c.get().spoint = Level.totalsPoint(player.c.get().level);
        player.c.get().ppoint = Level.totalpPoint(player.c.get().level);
        if (player.c.get().nclass % 2 != 0) {
            player.c.get().potential0 = 10;
            player.c.get().potential1 = 5;
            player.c.get().potential2 = 5;
            player.c.get().potential3 = 5;
        } else {
            player.c.get().potential0 = 5;
            player.c.get().potential1 = 5;
            player.c.get().potential2 = 5;
            player.c.get().potential3 = 10;
        }

        Message m = null;
        try {
            m = new Message(-30);
            m.writer().writeByte(-126);
            m.writer().writeByte(player.c.get().speed());
            m.writer().writeInt(player.c.get().getMaxHP());
            m.writer().writeInt(player.c.get().getMaxMP());
            m.writer().writeShort(player.c.get().potential0);
            m.writer().writeShort(player.c.get().potential1);
            m.writer().writeInt(player.c.get().potential2);
            m.writer().writeInt(player.c.get().potential3);
            m.writer().writeByte(player.c.get().nclass);
            m.writer().writeShort(player.c.get().spoint);
            m.writer().writeShort(player.c.get().ppoint);
            m.writer().flush();
            player.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            m.cleanup();
        }
    }
}
