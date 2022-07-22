package com.hoiuc.assembly;

public class TaskOrder {
    public byte TASK_DAY = 0;
    public byte TASK_BOSS = 1;
    public byte TASK_GIOITHIEU = 2;
    public byte TASK_SUKIEN1 = 3;
    public byte TASK_SUKIEN2 = 4;
    public byte TASK_SUKIEN3 = 5;
    public byte TASK_SUKIEN4 = 6;

    public int taskId;
    public int count;
    public int maxCount;
    public String name;
    public String description;
    public byte killId;
    public byte mapId;

    public TaskOrder(byte taskId, int count, int maxCount, String name, String description, byte killId, byte mapId)
    {
        this.count = count;
        this.maxCount = maxCount;
        this.taskId = (int)taskId;
        this.name = name;
        this.description = description;
        this.killId = killId;
        this.mapId = mapId;
    }
}
