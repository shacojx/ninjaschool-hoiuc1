package com.hoiuc.assembly;

import com.hoiuc.io.Util;
import com.hoiuc.template.ItemTemplate;

import java.io.Serializable;
import java.util.Objects;

public class ClanThanThu implements Serializable {
    private int type;
    private Item petItem;
    private volatile int curEXP;
    private volatile int maxEXP;
    public static int MAX_THAN_THU_EXPS = 10000;
    public static int HAI_MA_1_ID = 584;
    public static int HAI_MA_2_ID = 585;
    public static int HAI_MA_3_ID = 586;
    public static int DI_LONG_1_ID = 587;
    public static int HOA_LONG_ID = 583;
    public static int DI_LONG_2_ID = 588;
    public static int DI_LONG_3_ID = 589;
    public static int ST_QUAI_ID = 102;
    public static int ST_NGUOI_ID = 103;
    public static int TYPE_DI_LONG = 0;
    public static int TYPE_HAI_MA = 1;
    public static int TYPE_HOA_LONG = 2;

    public ClanThanThu() {
    }

    public ClanThanThu(Item petItem, int curEXP, int maxEXP, int type) {
        this.petItem = petItem;
        this.curEXP = curEXP;
        this.maxEXP = maxEXP;
        this.type = type;
    }

    public int getThanThuId() {
        if (this.petItem.id == 584) {
            return 2502;
        } else if (this.petItem.id == 585) {
            return 2503;
        } else if (this.petItem.id == 586) {
            return 2504;
        } else if (this.petItem.id == 587) {
            return 2506;
        } else if (this.petItem.id == 588) {
            return 2507;
        } else {
            return this.petItem.id == 589 ? 2508 : 2505;
        }
    }

    public int getStars() {
        if (this.curEXP >= this.maxEXP) {
            return 3;
        } else {
            return this.curEXP >= this.maxEXP / 2 ? 2 : 1;
        }
    }

    public int getThanThuIconId() {
        if (this.petItem.id == 584) {
            return 2484;
        } else if (this.petItem.id == 585) {
            return 2485;
        } else if (this.petItem.id == 586) {
            return 2486;
        } else if (this.petItem.id == 587) {
            return 2487;
        } else if (this.petItem.id == 588) {
            return 2488;
        } else {
            return this.petItem.id == 589 ? 2489 : 2490;
        }
    }

    public boolean upExp(int exp) {
        this.curEXP += exp;
        if (this.curEXP > this.maxEXP) {
            this.curEXP = this.maxEXP;
            return false;
        } else {
            return true;
        }
    }

    public int getOption(int index) {
        if (this.getStars() == 1) {
            return ((Option)this.petItem.options.get(index)).param;
        } else {
            return this.getStars() == 2 ? ((Option)this.petItem.options.get(index)).param * 120 / 100 : ((Option)this.petItem.options.get(index)).param * 140 / 100;
        }
    }

    public ClanThanThu.EvolveStatus evolve() {
        if (this.petItem.id != 586 && this.petItem.id != 589) {
            if (this.getStars() == 3) {
                if (this.petItem.id != 584 && this.petItem.id != 585 && this.petItem.id != 587 && this.petItem.id != 588) {
                    return ClanThanThu.EvolveStatus.FAIL;
                } else if (Util.percent(100, 30)) {
                    this.petItem = ItemTemplate.itemDefault(this.petItem.id + 1);
                    this.curEXP -= this.maxEXP;
                    this.maxEXP = MAX_THAN_THU_EXPS * 2;
                    return ClanThanThu.EvolveStatus.SUCCESS;
                } else {
                    return ClanThanThu.EvolveStatus.FAIL;
                }
            } else {
                return ClanThanThu.EvolveStatus.NOT_ENOUGH_STARS;
            }
        } else {
            return ClanThanThu.EvolveStatus.MAX_LEVEL;
        }
    }

    public String getName() {
        return this.petItem == null ? "Không tồn tại" : this.petItem.getData().name;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            ClanThanThu that = (ClanThanThu)o;
            return Objects.equals(this.type, that.type);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.type});
    }

    public int getType() {
        return this.type;
    }

    public Item getPetItem() {
        return this.petItem;
    }

    public int getCurEXP() {
        return this.curEXP;
    }

    public int getMaxEXP() {
        return this.maxEXP;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setPetItem(Item petItem) {
        this.petItem = petItem;
    }

    public void setCurEXP(int curEXP) {
        this.curEXP = curEXP;
    }

    public void setMaxEXP(int maxEXP) {
        this.maxEXP = maxEXP;
    }

    public static enum EvolveStatus {
        SUCCESS,
        FAIL,
        MAX_LEVEL,
        NOT_ENOUGH_STARS;
    }
}
