package com.hoiuc.template;

import com.hoiuc.io.Util;

public class DanhVongTemplate {
    public static int[] idDaDanhVong = new int[]{695, 696, 697, 698, 699, 700, 701, 702, 703, 704};
    public static int[] typeNV = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    public static String[] nameNV = new String[]{"- Tham gia vòng xoay may mắn vip %d/%d lần",
            "- Tham gia vòng xoay may mắn thường %d/%d lần",
            "- Giành chiến thắng và nhận xu trong lôi đài %d/%d lần",
            "- Luyện thành công đá 11: %d/%d lần",
            "- Lật hình %d/%d lần",
            "- Tăng điểm hiếu chiến %d/%d lần",
            "- Hạ gục %d/%d bù nhìn",
            "- Tiêu diệt %d/%d quái thường không lệch quá 10 cấp độ",
            "- Tiêu diệt %d/%d tinh anh không lệch quá 10 cấp độ",
            "- Tiêu diệt %d/%d thủ lĩnh không lệch quá 10 cấp độ"};

    public static int randomNVDV() {
        int ren = Util.nextInt(1,3);
        switch (ren) {
            case 1: {
                return 7;
            }
            case 2: {
                return 4;
            }
            case 3:
            default: {
                return Util.nextInt(DanhVongTemplate.typeNV.length);
            }
        }
    }

    public static int targetTask(int type) {
        if (type != 0 && type != 1 && type != 2 && type != 3 && type != 4 && type != 8 && type != 5) {
            if (type == 9) {
                return 1;
            } else {
                return (type != 6 && type != 7) ? 0 : Util.nextInt(20, 30);
            }
        } else {
            return Util.nextInt(1, 3);
        }
    }

    public static int randomDaDanhVong() {
        int percent = Util.nextInt(101);
        if (percent < 50) {
            return 695;
        } else if (percent >= 50 && percent < 75) {
            return 696;
        } else if (percent >= 75 && percent < 87) {
            return 697;
        } else if (percent >= 87 && percent < 93) {
            return 698;
        } else if (percent != 93 && percent != 94 && percent != 95) {
            if (percent != 96 && percent != 97) {
                if (percent == 98) {
                    return 701;
                } else if (percent == 99) {
                    return 702;
                } else {
                    return percent == 100 ? 703 : 695;
                }
            } else {
                return 700;
            }
        } else {
            return 699;
        }
    }
}
