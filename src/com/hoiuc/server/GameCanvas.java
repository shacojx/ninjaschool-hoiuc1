package com.hoiuc.server;

import com.hoiuc.io.Message;

public class GameCanvas {
    public static void addInfoDlg(Session session, String s) {
        Message msg = null;
        try {
            msg = Service.messageNotMap((byte)(-86));
            msg.writer().writeUTF(s);
            msg.writer().flush();
            session.sendMessage(msg);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void startOKDlg(Session session, String info) {
        Message msg = null;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(info);
            msg.writer().flush();
            session.sendMessage(msg);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void addEffect(Session session, byte b, int vId, short id, int timelive, int miliSecondWait, boolean isHead) {
        Message msg = null;
        try {
            if(session != null) {
                msg = new Message(125);
                msg.writer().writeByte(0);
                msg.writer().writeByte(b);
                if (b == 1) {
                    msg.writer().writeByte(vId);
                }
                else {
                    msg.writer().writeInt(vId);
                }
                msg.writer().writeShort(id);
                msg.writer().writeInt(timelive);
                msg.writer().writeByte(miliSecondWait);
                msg.writer().writeByte(isHead ? 1 : 0);
                msg.writer().flush();
                session.sendMessage(msg);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void getImgEffect(Session session, short id) {
        Message msg = null;
        try {
            if(session != null) {
                //System.out.println("Lấy ảnh " + id);
                byte[] ab = GameSrc.loadFile("res/Effect/x" + session.zoomLevel + "/ImgEffect/ImgEffect " + id + ".png" ).toByteArray();
                if (ab != null) {
                    msg = new Message(125);
                    msg.writer().writeByte(1);
                    msg.writer().writeByte(id);
                    msg.writer().writeInt(ab.length);
                    msg.writer().write(ab);
                    msg.writer().flush();
                    session.sendMessage(msg);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void getDataEffect(Session session, short id) {
        Message msg = null;
        try {
            if(session != null) {
                //System.out.println("Lấy data " + id);
                byte[] ab = GameSrc.loadFile("res/Effect/x" + session.zoomLevel + "/DataEffect/" + id).toByteArray();
                if (ab != null) {
                    if(id==21){
                        ab[6]=127;
                        ab[8]=127;
                        ab[9]=127;
                        ab[11]=127;
                        ab[12]=127;
                        ab[13]=127;
                        ab[14]=127;
                        ab[18]=127;
                        ab[19]=127;
                        ab[22]=127;
                        ab[23]=127;
                        ab[24]=127;
                        ab[29]=-60;
                        ab[31]=70;
                        ab[37]=-60;
                        ab[39]=70;
                        ab[45]=-60;
                        ab[47]=70;
                        ab[53]=-60;
                        ab[55]=70;
                        ab[59]=127;

                        msg = new Message(125);
                        msg.writer().write(ab);
                        msg.writer().flush();
                        session.sendMessage(msg);
                    }
                    else {
                        msg = new Message(125);
                        msg.writer().write(ab);
                        msg.writer().flush();
                        session.sendMessage(msg);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }
}
