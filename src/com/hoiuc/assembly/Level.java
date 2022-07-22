package com.hoiuc.assembly;

import java.util.ArrayList;

public class Level {
    public int level;
    public long exps;
    public short ppoint;
    public short spoint;
    public static ArrayList<Level> entrys = new ArrayList<Level>();

    public static long[] getLevelExp(long exp) {
        long num;
        int i;
        for (num = exp, i = 0; i < Level.entrys.size() && num >= Level.entrys.get(i).exps; num -= Level.entrys.get(i).exps, ++i) {}
        return new long[] { i, num };
    }

    public static short totalpPoint(int level) {
        try {
            short ppoint = 0;
            for (short i = 0; i < Level.entrys.size(); ++i) {
                if (Level.entrys.get(i).level <= level) {
                    ppoint += Level.entrys.get(i).ppoint;
                }
            }
            return ppoint;
        } catch (Exception e) {
            return 0;
        }
    }

    public static short totalsPoint(int level) {
        try {
            short spoint = 0;
            for (short i = 0; i < Level.entrys.size(); ++i) {
                if (Level.entrys.get(i).level <= level) {
                    spoint += Level.entrys.get(i).spoint;
                }
            }
            return spoint;
        } catch (Exception e) {
            return  0;
        }
    }

    public static long getMaxExp(int level) {
        try {
            long num = 0L;
            for (int i = 0; i < level; ++i) {
                num += getLevel(i).exps;
            }
            return num;
        } catch (Exception e) {
            return 0;
        }
    }

    public static Level getLevel(int level) {
        try {
            for (short i = 0; i < Level.entrys.size(); ++i) {
                if (Level.entrys.get(i).level == level) {
                    return Level.entrys.get(i);
                }
            }
            return null;
        } catch (Exception e) {
            return  null;
        }
    }
}
