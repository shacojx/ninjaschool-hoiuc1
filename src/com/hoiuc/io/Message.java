package com.hoiuc.io;

import java.io.*;

public class Message {
    private byte command;
    private ByteArrayOutputStream os;
    private DataOutputStream dos;
    private ByteArrayInputStream is;
    private DataInputStream dis;

    public Message(int command) {
        this((byte)command);
    }

    public Message(byte command) {
        this.is = null;
        this.dis = null;
        this.command = command;
        this.os = new ByteArrayOutputStream();
        this.dos = new DataOutputStream(this.os);
    }

    public Message(byte command, byte[] data) {
        this.os = null;
        this.dos = null;
        this.command = command;
        this.is = new ByteArrayInputStream(data);
        this.dis = new DataInputStream(this.is);
    }

    public byte[] getData() {
        return this.os.toByteArray();
    }

    public byte getCommand() {
        return this.command;
    }

    public DataInputStream reader() {
        return this.dis;
    }

    public DataOutputStream writer() {
        return this.dos;
    }

    public void cleanup() {
        try {
            if (this.os != null) {
                this.os.close();
            }
            if (this.is != null) {
                this.is.close();
            }
            if (this.dis != null) {
                this.dis.close();
            }
            if (this.dos != null) {
                this.dos.close();
            }
        } catch (IOException var1) {
            var1.printStackTrace();
        }

    }
}
