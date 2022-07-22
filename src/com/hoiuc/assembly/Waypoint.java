package com.hoiuc.assembly;

public class Waypoint {
    public short minX;
    public short minY;
    public short maxX;
    public short maxY;
    public short goX;
    public short goY;
    public short mapid;

    public Waypoint(){}

    public Waypoint(short minX, short minY, short maxX, short maxY, short goX, short goY, short mapid) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.goX = goX;
        this.goY = goY;
        this.mapid = mapid;
    }
}
