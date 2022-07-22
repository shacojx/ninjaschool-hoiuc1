package com.hoiuc.assembly;

import com.hoiuc.io.Message;
import com.hoiuc.io.SQLManager;
import com.hoiuc.io.Util;
import com.hoiuc.server.Service;
import com.hoiuc.stream.Client;
import com.hoiuc.stream.Server;
import com.hoiuc.template.ItemTemplate;
import org.json.simple.JSONArray;

import java.sql.ResultSet;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class ClanManager {
    public int id;
    public String name = "";
    public int exp = 0;
    public int level = 1;
    public int itemLevel = 0;
    public int coin = 1000000;
    public String reg_date = "";
    public String log = "";
    public String alert = "";
    public byte use_card = 4;
    public byte openDun = 3;
    public byte debt = 0;
    public int ldgtID = -1;
    public int gtcID = -1;
    public String gtcClanName = null;
    public ArrayList<ClanMember> members = new ArrayList();
    public ArrayList<Item> items = new ArrayList();
    public String week = "";
    public static ArrayList<ClanManager> entrys = new ArrayList();

    public static ClanManager getClanName(String name) {
        for(int i = 0; i < entrys.size(); ++i) {
            if (((ClanManager)entrys.get(i)).name.equals(name)) {
                return (ClanManager)entrys.get(i);
            }
        }
        return null;
    }

    public static ClanManager getClanId(int id) {
        for(int i = 0; i < entrys.size(); ++i) {
            if (((ClanManager)entrys.get(i)).id == id) {
                return (ClanManager)entrys.get(i);
            }
        }
        return null;
    }

    public void updateCoin(int coin) {
        this.coin += coin;
        if (coin < 0 && this.coin < 0) {
            ++this.debt;
            if (this.debt > 3) {
                this.dissolution();
            }
        } else if (this.coin >= 0) {
            this.debt = 0;
        }

    }

    public String getmain_name() {
        for(short i = 0; i < this.members.size(); ++i) {
            if (((ClanMember)this.members.get(i)).typeclan == 4) {
                return ((ClanMember)this.members.get(i)).cName;
            }
        }

        return "";
    }

    public String getassist_name() {
        for(short i = 0; i < this.members.size(); ++i) {
            if (((ClanMember)this.members.get(i)).typeclan == 3) {
                return ((ClanMember)this.members.get(i)).cName;
            }
        }

        return "";
    }

    public int numElder() {
        int elder = 0;

        for(short i = 0; i < this.members.size(); ++i) {
            if (((ClanMember)this.members.get(i)).typeclan == 2) {
                ++elder;
            }
        }

        return elder;
    }

    public ClanMember getMem(int id) {
        for(short i = 0; i < this.members.size(); ++i) {
            if (((ClanMember)this.members.get(i)).charID == id) {
                return (ClanMember)this.members.get(i);
            }
        }

        return null;
    }

    public ClanMember getMem(String name) {
        for(short i = 0; i < this.members.size(); ++i) {
            if (((ClanMember)this.members.get(i)).cName.equals(name)) {
                return (ClanMember)this.members.get(i);
            }
        }

        return null;
    }

    public int getMemMax() {
        return 45 + this.level * 5;
    }

    public int getexpNext() {
        int expNext = 2000;

        for(int i = 1; i < this.level; ++i) {
            if (i == 1) {
                expNext = 3720;
            } else if (i < 10) {
                expNext = (expNext / i + 310) * (i + 1);
            } else if (i < 20) {
                expNext = (expNext / i + 620) * (i + 1);
            } else {
                expNext = (expNext / i + 930) * (i + 1);
            }
        }

        return expNext;
    }

    public int getfreeCoin() {
        return 30000 + this.members.size() * 5000;
    }

    private int getCoinOpen() {
        if (this.itemLevel == 0) {
            return 1000000;
        } else if (this.itemLevel == 1) {
            return 5000000;
        } else if (this.itemLevel == 2) {
            return 10000000;
        } else if (this.itemLevel == 3) {
            return 20000000;
        } else {
            return this.itemLevel == 4 ? 30000000 : 0;
        }
    }

    public int getCoinUp() {
        int coinUp = 500000;

        for(int i = 1; i < this.level; ++i) {
            if (i < 10) {
                coinUp += 100000;
            } else if (i < 20) {
                coinUp += 200000;
            } else {
                coinUp += 300000;
            }
        }

        return coinUp;
    }

    public void sendMessage(Message m) {
        for(short i = 0; i < this.members.size(); ++i) {
            Char n = Client.gI().getNinja((this.members.get(i)).cName);
            if (n != null) {
                n.p.conn.sendMessage(m);
            }
        }

    }

    public void payfeesClan() {
        this.writeLog("", 4, this.getfreeCoin(), Util.toDateString(Date.from(Instant.now())));
        this.updateCoin(-this.getfreeCoin());

        for(short i = 0; i < this.members.size(); ++i) {
            ((ClanMember)this.members.get(i)).pointClanWeek = 0;
        }
        this.use_card =  4;
        this.openDun  = 3;
        this.week = Util.toDateString(Date.from(Instant.now()));
    }

    public void upExp(int exp) {
        this.exp += exp;
    }

    public void addItem(Item it) {
        for(byte i = 0; i < this.items.size(); ++i) {
            Item it2 = (Item)this.items.get(i);
            if (it2.id == it.id) {
                it2.quantity += it.quantity;
                return;
            }
        }

        this.items.add(it);
    }

    public void removeItem(int id, int quantity) {
        for(byte i = 0; i < this.items.size(); ++i) {
            Item it = (Item)this.items.get(i);
            if (it.id == id) {
                it.quantity -= quantity;
                if (it.quantity <= 0) {
                    this.items.remove(it);
                }

                return;
            }
        }

    }

    public void chat(Player p, Message m) {
        try {
            String text = m.reader().readUTF();
            m.cleanup();
            m = new Message(-19);
            m.writer().writeUTF(p.c.name);
            m.writer().writeUTF(text);
            m.writer().flush();
            this.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void changeClanType(Player p, Message m) {
        try {
            String cName = m.reader().readUTF();
            byte typeclan = m.reader().readByte();
            ClanMember mem = this.getMem(cName);
            if (mem != null && p.c.clan.typeclan == 4 && mem.charID != p.c.id) {
                Char n = Client.gI().getNinja(mem.cName);
                if (typeclan == 0 && mem.typeclan > 1) {
                    if (n != null) {
                        n.p.setTypeClan(typeclan);
                    }
                    mem.typeclan = typeclan;
                    this.requestClanMember(p);
                    m = new Message(-24);
                    m.writer().writeUTF(p.c.name + " đã bị bãi chức");
                    m.writer().flush();
                    this.sendMessage(m);
                    m.cleanup();
                } else if (typeclan == 2) {
                    if (this.numElder() >= 5) {
                        p.conn.sendMessageLog("Đã có đủ trưởng lão");
                        return;
                    }
                    if (n != null) {
                        n.p.setTypeClan(typeclan);
                    }
                    mem.typeclan = typeclan;
                    this.requestClanMember(p);
                    m = new Message(-24);
                    m.writer().writeUTF(p.c.name + " đã được bổ nhiệm làm trưởng lão");
                    m.writer().flush();
                    this.sendMessage(m);
                    m.cleanup();
                } else if (typeclan == 3) {
                    if (this.getassist_name().length() > 0) {
                        p.conn.sendMessageLog("Đã có tộc phó rồi");
                        return;
                    }
                    if (n != null) {
                        n.p.setTypeClan(typeclan);
                    }
                    mem.typeclan = typeclan;
                    this.requestClanMember(p);
                    m = new Message(-24);
                    m.writer().writeUTF(p.c.name + " đã được bổ nhiệm làm tộc phó");
                    m.writer().flush();
                    this.sendMessage(m);
                    m.cleanup();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void openItemLevel(Player p) {
        Message m = null;
        try {
            if (p.c.clan.typeclan == 4 || p.c.clan.typeclan == 3) {
                int coinDown = this.getCoinOpen();
                int lvopen = 5 * (this.itemLevel + 1);
                if (lvopen > this.level) {
                    p.conn.sendMessageLog("Gia tộc chưa đạt cấp " + lvopen);
                } else if (coinDown > this.coin) {
                    p.conn.sendMessageLog("Ngân sách không đủ để khai mở vật phẩm");
                } else if (this.itemLevel == 5) {
                    p.conn.sendMessageLog("Khai mở đã tối đa");
                } else {
                    this.updateCoin(-coinDown);
                    ++this.itemLevel;
                    m = new Message(-28);
                    m.writer().writeByte(-62);
                    m.writer().writeByte(this.itemLevel);
                    m.writer().flush();
                    p.conn.sendMessage(m);
                    m.cleanup();
                    this.requestClanInfo(p);
                    m = new Message(-24);
                    m.writer().writeUTF(p.c.name + " đã khai mở vật phẩm gia tộc, ngân sách giảm " + this.coin + " xu");
                    m.writer().flush();
                    this.sendMessage(m);
                    m.cleanup();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void sendClanItem(Player p, Message m) {
        try {
            byte index = m.reader().readByte();
            String cName = m.reader().readUTF();
            m.cleanup();
            ClanMember mem = this.getMem(cName);
            if (mem != null && p.c.clan.typeclan >= 3 && index >= 0 && index < this.items.size()) {
                Char n = Client.gI().getNinja(mem.cName);
                if (n == null) {
                    p.sendAddchatYellow("Thành viên đã offline");
                } else if (n.getBagNull() == 0) {
                    p.sendAddchatYellow("Hành trang thành viên đã đầy");
                } else {
                    Item item = ((Item)this.items.get(index)).clone();

                    if(item.id == 423 && this.itemLevel < 1) {
                        p.conn.sendMessageLog("Cấp độ gia tộc không đủ để sử dụng vật phẩm này.");
                    } else if(item.id == 424 && this.itemLevel < 2) {
                        p.conn.sendMessageLog("Cấp độ gia tộc không đủ để sử dụng vật phẩm này.");
                    } else if(item.id == 425 && this.itemLevel < 3) {
                        p.conn.sendMessageLog("Cấp độ gia tộc không đủ để sử dụng vật phẩm này.");
                    } else if(item.id == 426 && this.itemLevel < 4) {
                        p.conn.sendMessageLog("Cấp độ gia tộc không đủ để sử dụng vật phẩm này.");
                    } else if(item.id == 427 && this.itemLevel < 5) {
                        p.conn.sendMessageLog("Cấp độ gia tộc không đủ để sử dụng vật phẩm này.");
                    }
                    else {
                        item.expires += System.currentTimeMillis();
                        item.isLock = true;
                        item.quantity = 1;
                        this.removeItem(item.id, 1);
                        n.addItemBag(false, item);
                        this.requestClanItem(p);
                    }
                }
            } else {
                p.conn.sendMessageLog("Thành viên này không có trong gia tộc.");
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void useClanItem(Player p, Message m) {
        try {
            byte index = m.reader().readByte();
            m.cleanup();

            if (p.c.clan.typeclan >= 3 && index >= 0 && index < this.items.size()) {
                if(this.use_card <= 0) {
                    p.conn.sendMessageLog("Gia tộc đã hết số lần sử dụng lệnh bài trong tuần này.");
                    return;
                }
                Item item = (this.items.get(index)).clone();
                item.expires += System.currentTimeMillis();
                item.isLock = true;
                item.quantity = 1;
                this.removeItem(item.id, 1);
                this.use_card--;
                this.openDun++;
                p.sendAddchatYellow("Gia tộc đã tăng thêm 1 lần vào Lãnh địa gia tộc.");
                this.requestClanItem(p);
                this.requestClanInfo(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void setAlert(Player p, Message m) {
        try {
            String newalert = m.reader().readUTF();
            m.cleanup();
            if (p.c.clan.typeclan == 4 || p.c.clan.typeclan == 3) {
                if (newalert.length() > 30) {
                    p.conn.sendMessageLog("Chiều dài không quá 30 ký tự");
                    return;
                }

                if (newalert.isEmpty()) {
                    this.alert = "";
                } else {
                    this.alert = "Ghi chú của " + p.c.name + "\n" + newalert;
                }
                Service.mess_28_95(p, this.alert);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void moveOutClan(Player p, Message m) {
        try {
            String cName = m.reader().readUTF();
            m.cleanup();
            ClanMember mem = this.getMem(cName);
            if (mem != null && p.c.clan.typeclan >= 3 && mem.typeclan != 4 && mem.charID != p.c.id) {
                Char n = Client.gI().getNinja(mem.cName);
                int coinDown = 10000;
                if (mem.typeclan == 3) {
                    coinDown = 100000;
                } else if (mem.typeclan == 2) {
                    coinDown = 50000;
                } else if (mem.typeclan == 1) {
                    coinDown = 20000;
                }

                if (n != null) {
                    n.clan.clanName = "";
                    n.clan.pointClanWeek = 0;
                    n.p.setTypeClan(-1);
                }

                this.writeLog(mem.cName, 1, coinDown, Util.toDateString(Date.from(Instant.now())));
                m = new Message(-24);
                m.writer().writeUTF(mem.cName + " đã bị trục xuất khỏi gia tộc.");
                m.writer().flush();
                this.sendMessage(m);
                m.cleanup();
                this.members.remove(mem);
                this.updateCoin(-coinDown);
                this.requestClanMember(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void OutClan(Player p) {
        Message m = null;
        try {
            ClanMember mem = this.getMem(p.c.id);
            if (p.c.clan.typeclan != 4 && mem != null) {
                int coinDown = 10000;
                if (p.c.clan.typeclan == 3) {
                    coinDown = 100000;
                } else if (p.c.clan.typeclan == 2) {
                    coinDown = 50000;
                } else if (p.c.clan.typeclan == 1) {
                    coinDown = 20000;
                }

                if (coinDown > p.c.xu) {
                    p.conn.sendMessageLog("Bạn không có đủ xu");
                } else {
                    p.c.clan.clanName = "";
                    p.c.clan.pointClanWeek = 0;
                    p.setTypeClan(-1);
                    p.c.upxu((long)(-coinDown));
                    m = new Message(-28);
                    m.writer().writeByte(-90);
                    m.writer().writeInt(p.c.xu);
                    m.writer().flush();
                    p.conn.sendMessage(m);
                    m = new Message(-24);
                    m.writer().writeUTF(mem.cName + " đã rời khỏi gia tộc, trừ -" + coinDown + " xu");
                    m.writer().flush();
                    this.sendMessage(m);
                    m.cleanup();
                    this.members.remove(mem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void clanUpLevel(Player p) {
        Message m = null;
        try {
            if (p.c.clan.typeclan == 4 || p.c.clan.typeclan == 3) {
                int coinDown = this.getCoinUp();
                int expDown = this.getexpNext();
                if (this.getexpNext() > this.exp) {
                    p.conn.sendMessageLog("Kinh nghiệm chưa đủ");
                } else if (this.getCoinUp() > this.coin) {
                    p.conn.sendMessageLog("Ngân sách không đủ");
                } else {
                    this.writeLog(p.c.name, 5, coinDown, Util.toDateString(Date.from(Instant.now())));
                    this.updateCoin(-this.getCoinUp());
                    this.upExp(-expDown);
                    this.level++;
                    m = new Message(-24);
                    m.writer().writeUTF(p.c.name + " đã nâng cấp gia tộc, ngân sách giảm " + coinDown + " xu");
                    m.writer().flush();
                    this.sendMessage(m);
                    m.cleanup();
                    this.requestClanInfo(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void inputCoinClan(Player p, Message m) {
        try {
            int inputcoin = m.reader().readInt();
            m.cleanup();
            if (inputcoin > 0) {
                if (inputcoin > p.c.xu) {
                    p.conn.sendMessageLog("Bạn không có đủ xu.");
                    return;
                }

                if ((long)inputcoin + (long)this.coin > 2000000000L) {
                    p.conn.sendMessageLog("Chỉ còn có thể đóng góp thêm " + (this.coin - inputcoin));
                    return;
                }

                this.writeLog(p.c.name, 2, inputcoin, Util.toDateString(Date.from(Instant.now())));
                this.updateCoin(inputcoin);
                p.c.upxu((long)(-inputcoin));
                m = new Message(-28);
                m.writer().writeByte(-90);
                m.writer().writeInt(p.c.xu);
                m.writer().flush();
                p.conn.sendMessage(m);
                m.cleanup();
                m = new Message(-24);
                m.writer().writeUTF(p.c.name + " đã đóng góp " + inputcoin + " xu vào gia tộc, ngân sách gia tộc tăng " + this.coin + " xu");
                m.writer().flush();
                this.sendMessage(m);
                m.cleanup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void writeLog(String name, int num, int number, String date) {
        String[] array = this.log.split("\n");
        this.log = name + "," + num + "," + number + "," + date + "\n";

        for(int i = 0; i < array.length && i != 10; ++i) {
            this.log = this.log + array[i] + "\n";
        }

    }

    public void LogClan(Player p) {
        Message m = null;
        try {
            m = new Message(-28);
            m.writer().writeByte(-114);
            m.writer().writeUTF(this.log);
            m.writer().flush();
            p.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void requestClanInfo(Player p) {
        Message m = null;
        try {
            m = new Message(-28);
            m.writer().writeByte(-113);
            m.writer().writeUTF(this.name);
            m.writer().writeUTF(this.getmain_name());
            m.writer().writeUTF(this.getassist_name());
            m.writer().writeShort(this.members.size());
            m.writer().writeByte(this.openDun);
            m.writer().writeByte(this.level);
            m.writer().writeInt(this.exp);
            m.writer().writeInt(this.getexpNext());
            m.writer().writeInt(this.coin);
            m.writer().writeInt(this.getfreeCoin());
            m.writer().writeInt(this.getCoinUp());
            m.writer().writeUTF(this.reg_date);
            m.writer().writeUTF(this.alert);
            m.writer().writeInt(this.use_card);
            m.writer().writeByte(this.itemLevel);
            m.writer().flush();
            p.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void requestClanMember(Player p) {
        Message m = null;
        try {
            m = new Message(-28);
            m.writer().writeByte(-112);
            m.writer().writeShort(this.members.size());

            short i;
            Char n;
            for(i = 0; i < this.members.size(); ++i) {
                n = Client.gI().getNinja(((ClanMember)this.members.get(i)).cName);
                m.writer().writeByte(((ClanMember)this.members.get(i)).nClass);
                m.writer().writeByte(((ClanMember)this.members.get(i)).clevel);
                m.writer().writeByte(((ClanMember)this.members.get(i)).typeclan);
                m.writer().writeUTF(((ClanMember)this.members.get(i)).cName);
                m.writer().writeInt(((ClanMember)this.members.get(i)).pointClan);
                m.writer().writeBoolean(n != null);
            }

            for(i = 0; i < this.members.size(); ++i) {
                m.writer().writeInt(((ClanMember)this.members.get(i)).pointClanWeek);
            }

            m.writer().flush();
            p.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void requestClanItem(Player p) {
        Message m = null;
        try {
            m = new Message(-28);
            m.writer().writeByte(-111);
            m.writer().writeByte(this.items.size());
            byte i;
            for(i = 0; i < this.items.size(); ++i) {
                m.writer().writeShort(((Item)this.items.get(i)).quantity);
                m.writer().writeShort(((Item)this.items.get(i)).id);
            }
            m.writer().writeByte(0);
            m.writer().flush();
            p.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void flush() {
        try {
            synchronized(Server.LOCK_MYSQL) {
                JSONArray jarr = new JSONArray();
                String sqlSET = "`exp`=" + this.exp + ",`level`=" + this.level + ",`itemLevel`=" + this.itemLevel + ",`coin`=" + this.coin + ",`log`='" + this.log + "',`alert`='" + this.alert + "',`use_card`=" + this.use_card + ",`openDun`=" + this.openDun + ",`debt`=" + this.debt + "";
                JSONArray jarr2 = null;

                for(int i = 0; i < this.members.size(); ++i) {
                    ClanMember mem = (ClanMember)this.members.get(i);
                    jarr2 = new JSONArray();
                    jarr2.add(mem.charID);
                    jarr2.add(mem.cName);
                    jarr2.add(mem.clanName);
                    jarr2.add(mem.typeclan);
                    jarr2.add(mem.clevel);
                    jarr2.add(mem.nClass);
                    jarr2.add(mem.pointClan);
                    jarr2.add(mem.pointClanWeek);
                    jarr.add(jarr2);
                }

                sqlSET = sqlSET + ",`members`='" + jarr.toJSONString() + "'";
                jarr.clear();

                for(short j = 0; j < this.items.size(); ++j) {
                    jarr.add(ItemTemplate.ObjectItem((Item)this.items.get(j), j));
                }

                sqlSET = sqlSET + ",`items`='" + jarr.toJSONString() + "'";
                jarr.clear();
                sqlSET = sqlSET + ",`week`='" + this.week + "'";
                SQLManager.stat.executeUpdate("UPDATE `clan` SET " + sqlSET + " WHERE `id`=" + this.id + " LIMIT 1;");
                if (jarr != null && !jarr.isEmpty()) {
                    jarr.clear();
                }

                if (jarr2 != null && !jarr2.isEmpty()) {
                    jarr2.clear();
                }
            }
        } catch (Exception var10) {
            var10.printStackTrace();
        }

    }

    public static void createClan(Player p, String name) {
        name = name.toLowerCase();
        if (p.luong < 100000) {
            p.conn.sendMessageLog("Bạn cần có 100000 lượng để thành lập gia tộc");
        }

        ClanManager clan = getClanName(name);
        if (p.c.clan.clanName.isEmpty()) {
            if (Util.CheckString(name, "^[a-zA-Z0-9]+$") && name.length() >= 5 && name.length() <= 10) {
                if (clan != null) {
                    p.conn.sendMessageLog("Tên gia tộc đã tồn tại");
                } else {
                    try {
                        synchronized(Server.LOCK_MYSQL) {
                            clan = new ClanManager();
                            clan.name = name;
                            clan.reg_date = Util.toDateString(Date.from(Instant.now()));
                            ClanMember mem = new ClanMember(name, (byte)4, p.c);
                            clan.members.add(mem);
                            p.c.clan = mem;
                            SQLManager.stat.executeUpdate("INSERT INTO clan(`name`,`reg_date`,`log`,`alert`,`members`) VALUES ('" + clan.name + "','" + clan.reg_date + "','" + clan.log + "','" + clan.alert + "','[]');");
                            ResultSet red = SQLManager.stat.executeQuery("SELECT `id` FROM `clan` WHERE `name`LIKE'" + clan.name + "' LIMIT 1;");
                            red.first();
                            clan.id = red.getInt("id");
                            clan.writeLog("", 0, clan.coin, Util.toDateString(Date.from(Instant.now())));
                            clan.week = Util.toDateString(Date.from(Instant.now()));
                            entrys.add(clan);
                            clan.flush();
                            p.c.flush();
                            p.upluong(-100000L);
                            Message m = new Message(-28);
                            m.writer().writeByte(-96);
                            m.writer().writeUTF(clan.name);
                            m.writer().writeInt(p.luong);
                            m.writer().flush();
                            p.conn.sendMessage(m);
                            m.cleanup();
                            p.setTypeClan(4);
                            red.close();
                           Service.chatNPC(p, Short.valueOf((short)0), "Chúc mừng con đã thành lập gia tộc mới.");
                        }
                    } catch (Exception var9) {
                        var9.printStackTrace();
                    }
                }
            } else {
                p.conn.sendMessageLog("Tên gia tộc chỉ dùng các ký tự a-z,0-9 và chiều dài từ 5 đến 10 ký tự");
            }

        }
    }

    public void dissolution() {
        try {
            synchronized(entrys) {
                entrys.remove(this);
                Message m = new Message(-24);
                m.writer().writeUTF("Gia tộc " + this.name + " đã bị giải tán");
                m.writer().flush();

                while(!this.members.isEmpty()) {
                    ClanMember mem = (ClanMember)this.members.remove(0);
                    mem.typeclan = -1;
                    mem.clanName = "";
                    mem.pointClanWeek = 0;
                    Char n = Client.gI().getNinja(mem.cName);
                    if (n != null) {
                        n.p.setTypeClan(mem.typeclan);
                        n.p.conn.sendMessage(m);
                    }
                }

                m.cleanup();
                synchronized(Server.LOCK_MYSQL) {
                    SQLManager.stat.executeUpdate("DELETE FROM `clan` WHERE `id`=" + this.id + " LIMIT 1;");
                }
            }
        } catch (Exception var10) {
            var10.printStackTrace();
        }

    }

    public static void close() {
        for(int i = 0; i < entrys.size(); ++i) {
            if (entrys.get(i) != null) {
                ((ClanManager)entrys.get(i)).flush();
            }
        }
        System.out.println("Flush/ Close ClanManager");
    }
}
