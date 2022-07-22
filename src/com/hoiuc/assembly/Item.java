package com.hoiuc.assembly;

import com.hoiuc.template.ItemTemplate;

import java.util.ArrayList;
import java.util.List;

public class Item {
    public short id;
    public boolean isLock;
    public byte upgrade;
    public boolean isExpires;
    public int quantity;
    public long expires;
    public int saleCoinLock;
    public int buyCoin;
    public int buyCoinLock;
    public int buyGold;
    public byte sys;
    public ArrayList<Option> options;
    public List<Item> ngocs;
    private static final short[] DEFAULT_RANDOM_ITEM_IDS = new short[]{7, 8, 9, 436, 437, 438, 695};

    public Item() {
        this.id = -1;
        this.isLock = false;
        this.setUpgrade(0);
        this.isExpires = false;
        this.quantity = 1;
        this.expires = -1L;
        this.saleCoinLock = 0;
        this.buyCoin = 0;
        this.buyCoinLock = 0;
        this.buyGold = 0;
        this.sys = 0;
        this.options = new ArrayList<Option>();
        this.ngocs = new ArrayList();
    }

    public Item clone() {
        Item item = new Item();
        item.id = this.id;
        item.isLock = this.isLock;
        item.setUpgrade(this.getUpgrade());
        item.isExpires = this.isExpires;
        item.quantity = this.quantity;
        item.expires = this.expires;
        item.saleCoinLock = this.saleCoinLock;
        item.buyCoin = this.buyCoin;
        item.buyCoinLock = this.buyCoinLock;
        item.buyGold = this.buyGold;
        item.sys = this.sys;
        for (int i = 0; i < this.options.size(); ++i) {
            item.options.add(new Option(this.options.get(i).id, this.options.get(i).param));
        }
        item.ngocs.addAll(this.ngocs);
        return item;
    }

    public int getUpMax() {
        ItemTemplate data = ItemTemplate.ItemTemplateId(this.id);
        if (data.level >= 1 && data.level < 20) {
            return 4;
        }
        if (data.level >= 20 && data.level < 40) {
            return 8;
        }
        if (data.level >= 40 && data.level < 50) {
            return 12;
        }
        if (data.level >= 50 && data.level < 60) {
            return 14;
        }
        return 16;
    }

    public void upgradeNext(byte next) {
        this.setUpgrade(this.getUpgrade() + next);
        if (this.options != null) {
            short i;
            Option itemOption;
            for (i = 0; i < this.options.size(); ++i) {
                itemOption = this.options.get(i);
                if (itemOption.id == 6 || itemOption.id == 7) {
                    Option option = itemOption;
                    option.param += 15 * next;
                }
                else if (itemOption.id == 8 || itemOption.id == 9 || itemOption.id == 19) {
                    Option option2 = itemOption;
                    option2.param += 10 * next;
                }
                else if (itemOption.id == 10 || itemOption.id == 11 || itemOption.id == 12 || itemOption.id == 13 || itemOption.id == 14 || itemOption.id == 15 || itemOption.id == 17 || itemOption.id == 18 || itemOption.id == 20) {
                    Option option3 = itemOption;
                    option3.param += 5 * next;
                }
                else if (itemOption.id == 21 || itemOption.id == 22 || itemOption.id == 23 || itemOption.id == 24 || itemOption.id == 25 || itemOption.id == 26) {
                    Option option4 = itemOption;
                    option4.param += 150 * next;
                }
                else if (itemOption.id == 16) {
                    Option option5 = itemOption;
                    option5.param += 3 * next;
                }
            }
        }
    }

    public int getOptionShopMin(int opid, int param) {
        if (opid == 0 || opid == 1 || opid == 21 || opid == 22 || opid == 23 || opid == 24 || opid == 25 || opid == 26) {
            return param - 50 + 1;
        }
        if (opid == 6 || opid == 7 || opid == 8 || opid == 9 || opid == 19) {
            return param - 10 + 1;
        }
        if (opid == 2 || opid == 3 || opid == 4 || opid == 5 || opid == 10 || opid == 11 || opid == 12 || opid == 13 || opid == 14 || opid == 15 || opid == 17 || opid == 18 || opid == 20) {
            return param - 5 + 1;
        }
        if (opid == 16) {
            return param - 3 + 1;
        }
        return param;
    }

    public boolean isTypeBody() {
        return ItemTemplate.isTypeBody(this.id);
    }

    public boolean isTypeNgocKham() {
        return ItemTemplate.isTypeNgocKham(this.id);
    }

    public ItemTemplate getData() {
        return ItemTemplate.ItemTemplateId(this.id);
    }

    public byte getUpgrade() {
        return this.upgrade;
    }

    public void setUpgrade(int upgrade) {
        this.upgrade = (byte)upgrade;
    }

    protected boolean isTypeTask() {
        ItemTemplate data = this.getData();
        return data.type == 23 || data.type == 24 || data.type == 25;
    }

    public boolean isLock() {
        return this.isLock;
    }

    public void setLock(boolean lock) {
        this.isLock = lock;
    }
}
