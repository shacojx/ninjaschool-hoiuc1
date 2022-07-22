package com.hoiuc.stream;

import com.hoiuc.assembly.Char;
import com.hoiuc.assembly.DunListWin;
import com.hoiuc.assembly.Item;
import com.hoiuc.assembly.Map;
import com.hoiuc.io.SQLManager;
import com.hoiuc.io.Util;
import com.hoiuc.server.Manager;
import com.hoiuc.server.Service;
import com.hoiuc.template.ItemTemplate;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class Dun {
    public int dunID; //ID của map lôi đài
    private static int idbase = 0; //biến đếm số thứ tự của map lôi đài mỗi khi được tạo mới
    public Map[] map; //Danh sách các map nhỏ [0]:133, [1]:111
    public long time; //thời gian đếm ngược của map

    public long tienCuocTeam1; //biến lưu tiền cược của đối thủ 1
    public long tienCuocTeam2; //biến lưu tiền cược của đối thủ 2

    public Char c1; //biến lưu thông tin đối thủ 1
    public Char c2; //biến lưu thông tin đối thủ 2

    public String name1; //biến lưu thông tin đối thủ 1
    public String name2; //biến lưu thông tin đối thủ 2

    public int lv1; //biến lưu thông tin đối thủ 1
    public int lv2; //biến lưu thông tin đối thủ 2

    public int idC1; //biến lưu ID của đối thủ 1
    public int idC2; //biến lưu ID của đối thủ 2

    public boolean rest; //biến check xử lý hàm rest()
    public boolean check1; //biến check xử lý hàm check1()

    public boolean isMap133; //biến check map 133
    public boolean isStart; //biến check bắt đầu lôi đài
    public boolean isFinish; //biến check kết thúc lôi đài

    Server server;

    public ArrayList<Char> team1; //Mảng lưu thông tin người chơi đội 1
    public ArrayList<Char> team2; //Mảng lưu thông tin người chơi đội 2
    public ArrayList<Char> viewer; //Mảng lưu thông tin người xem
    public static HashMap<Integer, Dun> duns = new HashMap<Integer, Dun>(); //Mảng lưu thông tin của tất cả các map lôi đài được khởi tạo trên toàn server

    //Khởi tạo 1 đối tượng DUN khi được new với các giá trị mặc định của các biến
    public Dun() {
        this.isStart = false;
        this.isFinish = false;
        this.tienCuocTeam1 = 0L;
        this.tienCuocTeam2 = 0L;
        this.c1 = null;
        this.c2 = null;
        this.name1 = null;
        this.name2 = null;
        this.lv1 = 0;
        this.lv2 = 0;
        this.idC1 = -1;
        this.idC2 = -1;
        this.team1 = new ArrayList<Char>();
        this.team2 = new ArrayList<Char>();
        this.viewer = new ArrayList<Char>();
        this.rest = false;
        this.check1 = false;
        this.isMap133 = true;
        this.server = Server.gI();
        this.dunID = Dun.idbase++;
        this.time = System.currentTimeMillis() + 300000L;
        this.map = new Map[2];

        this.initMap(); //call hàm khởi tạo map

        for (byte i = 0; i < this.map.length; ++i) {
            this.map[i].timeMap = this.time;
            this.map[i].start();
        }

        Dun.duns.put(this.dunID, this); //Thêm đối tượng Dun mới được tạo thành vào danh sách các map lôi đài được khởi tạo trên toàn server
    }

    //Khởi tạo 2 map nhỏ trong map lôi đài [0]:133, [1]:111
    private void initMap() {
        this.map[0] = new Map(133, null, this, null, null, null, null);
        this.map[1] = new Map(111, null, this, null, null, null, null);
    }

    //Hàm check hết thời gian của 2 map lôi đài [0]:133, [1]:111
    public void rest() {
        if (!this.rest) {
            this.rest = true;
            try {
                synchronized (this) {
                    if((this.tienCuocTeam1 > 0 || this.tienCuocTeam2 > 0) && this.c1!=null && this.c2!=null && !this.isStart && this.isMap133) {  //nếu 1 trong 2 người chơi đã đặt tiền cược tại map [0]: 133 và trận lôi đài chưa bắt đầu => trả lại tiền cược
                        if(this.tienCuocTeam1 > 0) {
                            this.c1.upxuMessage(this.tienCuocTeam1);
                            this.c1.p.sendAddchatYellow("Nhận được nhận lại " + Util.getFormatNumber(this.tienCuocTeam1) + " xu tiền cược");
                        } else if (this.tienCuocTeam2 > 0) {
                            this.c2.upxuMessage(this.tienCuocTeam2);
                            this.c2.p.sendAddchatYellow("Nhận được nhận lại " + Util.getFormatNumber(this.tienCuocTeam2) + " xu tiền cược");
                        }
                    }

                    //kick người chơi đội 1 ra khỏi map
                    Char nj;
                    Map ma;
                    while (this.team1.size() > 0) {
                        nj = this.team1.remove(0);
                        if(nj.getEffId(14) != null) {
                            nj.p.removeEffect(14);
                        }
                        nj.dunId = -1;
                        nj.isInDun = false;
                        nj.typepk = (byte)0;
                        nj.yDun = 0;
                        Service.ChangTypePkId(nj, (byte)0);
                        if(nj.isDie) {
                            try {
                                nj.p.liveFromDead();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        nj.tileMap.leave(nj.p);
                        nj.p.restCave();
                        ma = Manager.getMapid(nj.mapKanata);
                        for (byte k = 0; k < ma.area.length; ++k) {
                            if (ma.area[k].numplayers < ma.template.maxplayers) {
                                ma.area[k].EnterMap0(nj);
                                break;
                            }
                        }
                    }
                    //kick người chơi đội 2 ra khỏi map
                    while (this.team2.size() > 0) {
                        nj = this.team2.remove(0);
                        if(nj.getEffId(14) != null) {
                            nj.p.removeEffect(14);
                        }
                        nj.dunId = -1;
                        nj.isInDun = false;
                        nj.typepk = (byte)0;
                        nj.yDun = 0;
                        Service.ChangTypePkId(nj, (byte)0);
                        if(nj.isDie) {
                            try {
                                nj.p.liveFromDead();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        nj.tileMap.leave(nj.p);
                        nj.p.restCave();
                        ma = Manager.getMapid(nj.mapKanata);
                        for (byte k = 0; k < ma.area.length; ++k) {
                            if (ma.area[k].numplayers < ma.template.maxplayers) {
                                ma.area[k].EnterMap0(nj);
                                break;
                            }
                        }
                    }
                    //kick người xem ra khỏi map
                    while (this.viewer.size() > 0) {
                        nj = this.viewer.remove(0);
                        nj.dunId = -1;
                        nj.isInDun = false;
                        nj.yDun = 0;
                        nj.tileMap.leave(nj.p);
                        nj.p.restCave();
                        ma = Manager.getMapid(nj.mapKanata);
                        for (byte k = 0; k < ma.area.length; ++k) {
                            if (ma.area[k].numplayers < ma.template.maxplayers) {
                                ma.area[k].EnterMap0(nj);
                                break;
                            }
                        }
                    }

                }

                //Đóng map
                byte i;
                for (i = 0; i < this.map.length; ++i) {
                    this.map[i].close();
                    this.map[i] = null;
                }

                //xoá map khỏi danh sách tất cả map lôi đài trong server
                synchronized (Dun.duns) {
                    if(Dun.duns.containsKey(this.dunID)) {
                        Dun.duns.remove(this.dunID);
                    }
                }
            } catch (Exception e) {
                byte i;
                for (i = 0; i < this.map.length; ++i) {
                    if(this.map[i] != null){
                        this.map[i].close();
                        this.map[i] = null;
                    }
                }
                synchronized (Dun.duns) {
                    if(Dun.duns.containsKey(this.dunID)) {
                        Dun.duns.remove(this.dunID);
                    }
                }
            }
        }
    }


    //Hàm check người chơi rời khỏi map đặt cược [0]:133
    public void check1() {
        if (!this.check1) { //nếu check1 = false mới thực hiện
            this.check1 = true;
            synchronized (this) {
                this.isFinish = true; //isFinish = true
                this.setTime(1000L);

                if (this.c1 == null && this.c2 != null) { //nếu người chơi c1 thoát
                    if (this.tienCuocTeam2 > 0) { //nếu người chơi c2 có tiền cược => trả lại tiền cược
                        this.c2.upxuMessage(this.tienCuocTeam2);
                        this.c2.p.sendAddchatYellow("Đối phương thoát, bạn nhận lại " + Util.getFormatNumber(this.tienCuocTeam2) + " xu. Bạn sẽ được đưa trường sau 3s");
                    } else {
                        this.c2.p.sendAddchatYellow("Đối phương thoát. Cuộc tỷ thí bị huỷ bỏ, bạn sẽ được đưa về trường sau 1s.");
                    }

                    if (this.tienCuocTeam1 != 0) { //nếu người chơi c1 có đặt tiền cược và thoát => trả lại
                        Char checkChar = Client.gI().getNinja(this.idC1);
                        if (checkChar == null) { //ko online
                            try {
                                synchronized (Server.LOCK_MYSQL) {
                                    ResultSet res = SQLManager.stat.executeQuery("SELECT * FROM `ninja` WHERE `id`='" + this.idC1  + "';");
                                    if(res.next()) {
                                        long xuOld = res.getLong("xu");
                                        xuOld += this.tienCuocTeam1;
                                        if(xuOld > 2000000000L) {
                                            xuOld = 2000000000L;
                                        }
                                        SQLManager.stat.executeUpdate("UPDATE `ninja` SET `xu`=" + xuOld + " WHERE `id`='" + this.idC1 + "';");
                                        res.close();
                                    }
                                }
                            } catch (Exception e) {
                            }
                        } else { //online
                            checkChar.upxuMessage(this.tienCuocTeam1);
                            checkChar.p.sendAddchatYellow("Bạn đã rời khỏi lôi đài, nhận lại " + Util.getFormatNumber(this.tienCuocTeam1) + " xu");
                        }
                    }
                }
                else if (this.c2 == null && this.c1 != null) {
                    if (this.tienCuocTeam1 > 0) {
                        this.c1.upxuMessage(this.tienCuocTeam1);
                        this.c1.p.sendAddchatYellow("Đối phương thoát, bạn nhận lại " + Util.getFormatNumber(this.tienCuocTeam1) + " xu. Bạn sẽ được đưa trường sau 3s");
                    } else {
                        this.c1.p.sendAddchatYellow("Đối phương thoát. Cuộc tỷ thí bị huỷ bỏ, bạn sẽ được đưa về trường sau 1s.");
                    }
                    if (this.tienCuocTeam2 != 0) {
                        Char checkChar = Client.gI().getNinja(this.idC2);
                        if (checkChar == null) {
                            try {
                                synchronized (Server.LOCK_MYSQL) {
                                    ResultSet res = SQLManager.stat.executeQuery("SELECT * FROM `ninja` WHERE `id`='" + this.idC2  + "';");
                                    if(res.next()) {
                                        long xuOld = res.getLong("xu");
                                        xuOld += this.tienCuocTeam2;
                                        if(xuOld > 2000000000L) {
                                            xuOld = 2000000000L;
                                        }
                                        SQLManager.stat.executeUpdate("UPDATE `ninja` SET `xu`=" + xuOld + " WHERE `id`='" + this.idC2 + "';");
                                        res.close();
                                    }
                                }
                            } catch (Exception e) {
                            }
                        } else {
                            checkChar.upxuMessage(this.tienCuocTeam2);
                            checkChar.p.sendAddchatYellow("Bạn đã rời khỏi lôi đài, nhận lại " + Util.getFormatNumber(this.tienCuocTeam2) + " xu");
                        }
                    }
                }
            }
        }
    }

    //Hàm check tìm người chơi chiến thăng, kết thúc lôi đài và cộng tiền
    public void check2() {
        if (this.isStart) { //isStart = true sẽ check liên tục về thông tin 2 đội đánh nhau trong lôi đài
            synchronized (this) {
                //nếu người chơi c1 thoát và số người trong đội của người chơi 1 < 0 thì dừng lôi đài, thông báo người chiến thắng
                if (this.c1 == null && this.team1.size() < 1) {
                    this.setTime(10000L);  //10000L
                    
                    
                    this.isStart = false; //set lại biến isStart để check lặp lại vòng lặp check()
                    this.isFinish = true;//set lại biến isFinish để dừng lôi đài

                    this.tienCuocTeam2 = this.tienCuocTeam2*2 - this.tienCuocTeam2*10/100;;
                    this.c2.p.sendAddchatYellow("Đối phương bỏ cuộc, bạn đã giành chiến thắng, và nhận được " + Util.getFormatNumber(this.tienCuocTeam2) + " xu");
                    if(this.c2.isTaskDanhVong == 1 && this.c2.taskDanhVong[0] == 2 && this.c2.taskDanhVong[1]<this.c2.taskDanhVong[2]) {
                        this.c2.taskDanhVong[1]++;
                    }
                    
                    if(this.tienCuocTeam1 >= 10000 && this.tienCuocTeam2>= 10000){
                        c2.nhanTP++;
                        if(c2.nhanTP % 2 == 0){  //5
                        Item it = ItemTemplate.itemDefault(675);
                        it.quantity = 1;
                        c2.addItemBag(true, it);
                        c2.countPhao++;
                    }
                    }
                    
                    int i;
                    for(i = 0; i< this.team1.size(); i++) {
                        if(this.team1.get(i).get().getEffId(14) != null) {
                            this.team1.get(i).p.removeEffect(14);
                        }
                        this.team1.get(i).p.sendAddchatYellow("Đội của bạn đã thua cuộc");
                    }
                    for(i = 0; i< this.team2.size(); i++) {
                        if(this.team2.get(i).get().getEffId(14) != null) {
                            this.team2.get(i).p.removeEffect(14);
                        }
                        this.team2.get(i).p.sendAddchatYellow("Đội của bạn đã giành chiến thắng");
                    }
                    for(i = 0; i< this.viewer.size(); i++) {
                        this.viewer.get(i).p.sendAddchatYellow("Đội của người chơi "+this.name2+" đã giành chiến thắng");
                    }

                    DunListWin dunListWin = new DunListWin(this.name2, this.name1);
                }
                else if (this.c2 == null && this.team1.size() < 1) {
                    this.setTime(10000L);
                    
                    
                    
                    this.isStart = false;
                    this.isFinish = true;

                    this.tienCuocTeam1 = this.tienCuocTeam2*2 - this.tienCuocTeam2*10/100;;
                    this.c1.p.sendAddchatYellow("Đối phương bỏ cuộc, bạn đã giành chiến thắng, và nhận được " + Util.getFormatNumber(this.tienCuocTeam1) + " xu");
                    if(this.c1.isTaskDanhVong == 1 && this.c1.taskDanhVong[0] == 2 && this.c1.taskDanhVong[1]<this.c1.taskDanhVong[2]) {
                        this.c1.taskDanhVong[1]++;
                    }
                    
                    if(this.tienCuocTeam1 >= 10000 && this.tienCuocTeam2>= 10000){
                        
                        c1.nhanTP++;
                        if(c1.nhanTP % 2 == 0){  //5
                        Item it = ItemTemplate.itemDefault(675);
                        it.quantity = 1;
                        c1.addItemBag(true, it);
                        c1.countPhao++;
                    }
                    }
                    
                    
                    
                    int i;
                    for(i = 0; i< this.team1.size(); i++) {
                        if(this.team1.get(i).get().getEffId(14) != null) {
                            this.team1.get(i).p.removeEffect(14);
                        }
                        this.team1.get(i).p.sendAddchatYellow("Đội của bạn đã giành chiến thắng");
                    }
                    for(i = 0; i< this.team2.size(); i++) {
                        if(this.team2.get(i).get().getEffId(14) != null) {
                            this.team2.get(i).p.removeEffect(14);
                        }
                        this.team2.get(i).p.sendAddchatYellow("Đội của bạn đã thua cuộc");
                    }
                    for(i = 0; i< this.viewer.size(); i++) {
                        this.viewer.get(i).p.sendAddchatYellow("Đội của người chơi "+this.name1+" đã giành chiến thắng");
                    }

                    DunListWin dunListWin = new DunListWin(this.name1, this.name2);
                }
                else {
                    //Đếm số người chết của từng đội
                    int countDie1 = 0;
                    int countDie2 = 0;
                    int i;
                    for (i = 0; i < this.team1.size(); i++) {
                        if (this.team1.get(i).isDie) {
                            countDie1++;
                        }
                    }
                    for (i = 0; i < this.team2.size(); i++) {
                        if (this.team2.get(i).isDie) {
                            countDie2++;
                        }
                    }

                    //nếu tất cả người chơi đội 1 chết hết, hoặc đội 2 chết hêt
                    if (countDie1 >= this.team1.size() || countDie2 >= this.team2.size()) {
                        this.setTime(10000L); // set lại thời gian 10 để kich toàn bộ người chơi khỏi lôi đài

                        this.isStart = false;
                        this.isFinish = true;
                        int i2;
                        //Nếu người chơi c1 còn trong lôi đài và đã chết => thực hiện  + tiền cho người chơi c2
                        if (this.c1 != null && this.c1.isDie ) {
                            this.tienCuocTeam2 = this.tienCuocTeam2*2 - this.tienCuocTeam2*10/100; //tính số tiền được nhận -5% số tiền cược

                            if(this.c2 != null) { //Nếu người chơi c2 vẫn trong lôi đài thì + xu trược tiếp
                                this.c2.upxuMessage(this.tienCuocTeam2);
                                this.c2.p.sendAddchatYellow("Bạn đã giành chiến thắng, và nhận được " + Util.getFormatNumber(this.tienCuocTeam2) + " xu");

                                if(this.c2.isTaskDanhVong == 1 && this.c2.taskDanhVong[0] == 2 && this.c2.taskDanhVong[1]<this.c2.taskDanhVong[2]) {
                                    this.c2.taskDanhVong[1]++;
                                }
                                
                                if(this.tienCuocTeam1 >= 10000 && this.tienCuocTeam2>= 10000){
                                    c2.nhanTP++;
                                    if(c2.nhanTP % 2 == 0){  //5
                                    Item it = ItemTemplate.itemDefault(675);
                                    it.quantity = 1;
                                    c2.addItemBag(true, it);
                                    c2.countPhao++;
                                }
                                }

                                

                            } else { //nếu người chơi c2 ko có trong lôi đài
                                Char checkChar = Client.gI().getNinja(this.idC2);
                                if (checkChar == null) { //nếu người chơi c2 ko online
                                    try {
                                        synchronized (Server.LOCK_MYSQL) {
                                            ResultSet res = SQLManager.stat.executeQuery("SELECT * FROM `ninja` WHERE `id`='" + this.idC2  + "';");
                                            if(res.next()) {
                                                long xuOld = res.getLong("xu");
                                                xuOld += this.tienCuocTeam2;
                                                if(xuOld > 2000000000L) {
                                                    xuOld = 2000000000L;
                                                }
                                                SQLManager.stat.executeUpdate("UPDATE `ninja` SET `xu`=" + xuOld + " WHERE `id`='" + this.idC2 + "';");
                                                res.close();
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else { //Người chơi c2 online
                                    checkChar.upxuMessage(this.tienCuocTeam2);
                                    checkChar.p.sendAddchatYellow("Bạn đã giành chiến thắng, và nhận được " + Util.getFormatNumber(this.tienCuocTeam2) + " xu");
                                }
                            }
                            this.c1.p.sendAddchatYellow("Bạn đã thua lôi đài và bị trừ " + Util.getFormatNumber(this.tienCuocTeam1) + " xu");

                            for(i2 = 0; i2< this.team1.size(); i2++) {
                                this.team1.get(i2).p.sendAddchatYellow("Đội của bạn đã thua cuộc");
                            }
                            for(i2 = 0; i2< this.team2.size(); i2++) {
                                this.team2.get(i2).p.sendAddchatYellow("Đội của bạn đã giành chiến thắng");
                            }
                            for(i2 = 0; i2< this.viewer.size(); i2++) {
                                this.viewer.get(i2).p.sendAddchatYellow("Đội của người chơi "+this.name2+" đã giành chiến thắng");
                            }
                            DunListWin dunListWin = new DunListWin(this.name2, this.name1);
                        }
                        //
                        else if (this.c2 != null && this.c2.isDie) {
                            this.tienCuocTeam1 = this.tienCuocTeam1*2 - this.tienCuocTeam1*10/100;
                            if(this.c1 != null) {
                                this.c1.upxuMessage(this.tienCuocTeam1);
                                this.c1.p.sendAddchatYellow("Bạn đã giành chiến thắng, và nhận được " + Util.getFormatNumber(this.tienCuocTeam1) + " xu");

                                if(this.c1.isTaskDanhVong == 1 && this.c1.taskDanhVong[0] == 2 && this.c1.taskDanhVong[1]<this.c1.taskDanhVong[2]) {
                                    this.c1.taskDanhVong[1]++;
                                }
                                
                        if(this.tienCuocTeam1 >= 10000 && this.tienCuocTeam2>= 10000){
                            
                                c1.nhanTP++;
                                 if(c1.nhanTP % 2 == 0){  //5
                                Item it = ItemTemplate.itemDefault(675);
                                it.quantity = 1;
                                c1.addItemBag(true, it);
                                c1.countPhao++;
                            }
                            }

                           

                            } else {
                                Char checkChar = Client.gI().getNinja(this.idC1);
                                if (checkChar == null) {
                                    try {
                                        synchronized (Server.LOCK_MYSQL) {
                                            ResultSet res = SQLManager.stat.executeQuery("SELECT * FROM `ninja` WHERE `id`='" + this.idC1  + "';");
                                            if(res.next()) {
                                                long xuOld = res.getLong("xu");
                                                xuOld += this.tienCuocTeam1;
                                                if(xuOld > 2000000000L) {
                                                    xuOld = 2000000000L;
                                                }
                                                SQLManager.stat.executeUpdate("UPDATE `ninja` SET `xu`=" + xuOld + " WHERE `id`='" + this.idC1 + "';");
                                                res.close();
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                } else {
                                    checkChar.upxuMessage(this.tienCuocTeam1);
                                    checkChar.p.sendAddchatYellow("Bạn đã giành chiến thắng, và nhận được " + Util.getFormatNumber(this.tienCuocTeam1) + " xu");
                                }
                            }
                            this.c2.p.sendAddchatYellow("Bạn đã thua đài và bị trừ " + Util.getFormatNumber(this.tienCuocTeam2) + " xu");

                            for(i2 = 0; i2< this.team1.size(); i2++) {
                                this.team1.get(i2).p.sendAddchatYellow("Đội của bạn đã giành chiến thắng");
                            }
                            for(i2 = 0; i2< this.team2.size(); i2++) {
                                this.team2.get(i2).p.sendAddchatYellow("Đội của bạn đã thua cuộc");
                            }
                            for(i2 = 0; i2< this.viewer.size(); i2++) {
                                this.viewer.get(i2).p.sendAddchatYellow("Đội của người chơi "+this.name1+" đã giành chiến thắng");
                            }
                            DunListWin dunListWin = new DunListWin(this.name1, this.name2);
                        }
                        //
                        else if(this.c1 == null) { //nếu người chơi c1 ko trong lôi đài
                            if(this.c2.isDie && this.c2 != null) { //người chơi c2 trong lôi đài và đã chết => cộng tiền cho người chơi 1
                                this.tienCuocTeam1 = this.tienCuocTeam1*2 - this.tienCuocTeam1*10/100;
                                Char checkChar = Client.gI().getNinja(this.idC1);
                                if (checkChar == null) {//c1 ko online => cộng vào sql
                                    try {
                                        synchronized (Server.LOCK_MYSQL) {
                                            ResultSet res = SQLManager.stat.executeQuery("SELECT * FROM `ninja` WHERE `id`='" + this.idC1  + "';");
                                            if(res.next()) {
                                                long xuOld = res.getLong("xu");
                                                xuOld += this.tienCuocTeam1;
                                                if(xuOld > 2000000000L) {
                                                    xuOld = 2000000000L;
                                                }
                                                SQLManager.stat.executeUpdate("UPDATE `ninja` SET `xu`=" + xuOld + " WHERE `id`='" + this.idC1 + "';");
                                                res.close();
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                } else  { //c1 online + trực tiếp
                                    checkChar.upxuMessage(this.tienCuocTeam1);
                                    checkChar.p.sendAddchatYellow("Bạn đã giành chiến thắng, và nhận được " + Util.getFormatNumber(this.tienCuocTeam1) + " xu");
                                }
                                this.c2.p.sendAddchatYellow("Bạn đã thua đài và bị trừ " + Util.getFormatNumber(this.tienCuocTeam2) + " xu");
                                for(i2 = 0; i2< this.team1.size(); i2++) {
                                    this.team1.get(i2).p.sendAddchatYellow("Đội của bạn đã giành chiến thắng");
                                }
                                for(i2 = 0; i2< this.team2.size(); i2++) {
                                    this.team2.get(i2).p.sendAddchatYellow("Đội của bạn đã thua cuộc");
                                }
                                for(i2 = 0; i2< this.viewer.size(); i2++) {
                                    this.viewer.get(i2).p.sendAddchatYellow("Đội của người chơi "+this.name1+" đã giành chiến thắng");
                                }

                                DunListWin dunListWin = new DunListWin(this.name1, this.name2);
                            }
                            else if(!this.c2.isDie && this.c2 != null) { //người chơi c2 còn sống và trong lôi đài => cộng tiền
                                this.tienCuocTeam2 = this.tienCuocTeam2*2 - this.tienCuocTeam2*10/100;
                                this.c2.upxuMessage(this.tienCuocTeam2);
                                this.c2.p.sendAddchatYellow("Bạn đã giành chiến thắng, và nhận được " + Util.getFormatNumber(this.tienCuocTeam2) + " xu");

                                if(this.c2.isTaskDanhVong == 1 && this.c2.taskDanhVong[0] == 2 && this.c2.taskDanhVong[1]<this.c2.taskDanhVong[2]) {
                                    this.c2.taskDanhVong[1]++;
                                }
                                
                                if(this.tienCuocTeam1 >= 10000 && this.tienCuocTeam2>= 10000){
                                c2.nhanTP++;
                                if(c2.nhanTP % 2 == 0){  //5
                                Item it = ItemTemplate.itemDefault(675);
                                it.quantity = 1;
                                c2.addItemBag(true, it);
                                c2.countPhao++;
                            }
                            }

                            

                                for(i2 = 0; i2< this.team1.size(); i2++) {
                                    this.team1.get(i2).p.sendAddchatYellow("Đội của bạn đã thua cuộc");
                                }
                                for(i2 = 0; i2< this.team2.size(); i2++) {
                                    this.team2.get(i2).p.sendAddchatYellow("Đội của bạn đã giành chiến thắng");
                                }
                                for(i2 = 0; i2< this.viewer.size(); i2++) {
                                    this.viewer.get(i2).p.sendAddchatYellow("Đội của người chơi "+this.name2+" đã giành chiến thắng");
                                }

                                DunListWin dunListWin = new DunListWin(this.name2, this.name1);
                            }
                            else if(countDie1 >= this.team1.size() && this.c2 == null) { //đội người chơi c2 thắng nhưng c2 ko trong lôi đài
                                this.tienCuocTeam2 += this.tienCuocTeam2 * 95 / 100;
                                Char checkChar = Client.gI().getNinja(this.idC2);
                                if (checkChar == null) {
                                    try {
                                        synchronized (Server.LOCK_MYSQL) {
                                            ResultSet res = SQLManager.stat.executeQuery("SELECT * FROM `ninja` WHERE `id`='" + this.idC2  + "';");
                                            if(res.next()) {
                                                long xuOld = res.getLong("xu");
                                                xuOld += this.tienCuocTeam2;
                                                if(xuOld > 2000000000L) {
                                                    xuOld = 2000000000L;
                                                }
                                                SQLManager.stat.executeUpdate("UPDATE `ninja` SET `xu`=" + xuOld + " WHERE `id`='" + this.idC2 + "';");
                                                res.close();
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                } else  {
                                    checkChar.upxuMessage(this.tienCuocTeam2);
                                    checkChar.p.sendAddchatYellow("Bạn đã giành chiến thắng, và nhận được " + Util.getFormatNumber(this.tienCuocTeam2) + " xu");
                                }

                                for(i2 = 0; i2< this.team1.size(); i2++) {
                                    this.team1.get(i2).p.sendAddchatYellow("Đội của bạn đã thua cuộc");
                                }
                                for(i2 = 0; i2< this.team2.size(); i2++) {
                                    this.team2.get(i2).p.sendAddchatYellow("Đội của bạn đã giành chiến thắng");
                                }
                                for(i2 = 0; i2< this.viewer.size(); i2++) {
                                    this.viewer.get(i2).p.sendAddchatYellow("Đội của người chơi "+this.name2+" đã giành chiến thắng");
                                }

                                DunListWin dunListWin = new DunListWin(this.name2, this.name1);
                            }
                        }
                        //
                        else if(this.c2 == null) {
                            if(this.c1.isDie && this.c1 != null) {
                                this.tienCuocTeam2 = this.tienCuocTeam2*2 - this.tienCuocTeam2 * 10 / 100;
                                Char checkChar = Client.gI().getNinja(this.idC2);
                                if (checkChar == null) {
                                    try {
                                        synchronized (Server.LOCK_MYSQL) {
                                            ResultSet res = SQLManager.stat.executeQuery("SELECT * FROM `ninja` WHERE `id`='" + this.idC2  + "';");
                                            if(res.next()) {
                                                long xuOld = res.getLong("xu");
                                                xuOld += this.tienCuocTeam2;
                                                if(xuOld > 2000000000L) {
                                                    xuOld = 2000000000L;
                                                }
                                                SQLManager.stat.executeUpdate("UPDATE `ninja` SET `xu`=" + xuOld + " WHERE `id`='" + this.idC2 + "';");
                                                res.close();
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                } else {
                                    checkChar.upxuMessage(this.tienCuocTeam2);
                                    checkChar.p.sendAddchatYellow("Bạn đã giành chiến thắng, và nhận được " + Util.getFormatNumber(this.tienCuocTeam2) + " xu");
                                }
                                this.c1.p.sendAddchatYellow("Bạn đã thua đài và bị trừ " + this.tienCuocTeam1 + " xu");
                                for(i2 = 0; i2< this.team1.size(); i2++) {
                                    this.team1.get(i2).p.sendAddchatYellow("Đội của bạn đã thua cuộc");
                                }
                                for(i2 = 0; i2< this.team2.size(); i2++) {
                                    this.team2.get(i2).p.sendAddchatYellow("Đội của bạn đã giành chiến thắng");
                                }
                                for(i2 = 0; i2< this.viewer.size(); i2++) {
                                    this.viewer.get(i2).p.sendAddchatYellow("Đội của người chơi "+this.name2+" đã giành chiến thắng");
                                }

                                DunListWin dunListWin = new DunListWin(this.name2, this.name1);
                            }
                            else if(!this.c1.isDie && this.c1 != null) {
                                this.tienCuocTeam1 = this.tienCuocTeam1*2 - this.tienCuocTeam1*10/100;
                                this.c1.upxuMessage(this.tienCuocTeam1);
                                this.c1.p.sendAddchatYellow("Bạn đã giành chiến thắng, và nhận được " + Util.getFormatNumber(this.tienCuocTeam1) + " xu");

                                if(this.c1.isTaskDanhVong == 1 && this.c1.taskDanhVong[0] == 2 && this.c1.taskDanhVong[1]<this.c1.taskDanhVong[2]) {
                                    this.c1.taskDanhVong[1]++;
                                }
                                
                                if(this.tienCuocTeam1 >= 10000 && this.tienCuocTeam2>= 10000){
                                c1.nhanTP++;
                                if(c1.nhanTP % 2 == 0){  //5
                                Item it = ItemTemplate.itemDefault(675);
                                it.quantity = 1;
                                c1.addItemBag(true, it);
                                c1.countPhao++;
                            }
                            }

                            

                                for(i2 = 0; i2< this.team1.size(); i2++) {
                                    this.team1.get(i2).p.sendAddchatYellow("Đội của bạn đã giành chiến thắng");
                                }
                                for(i2 = 0; i2< this.team2.size(); i2++) {
                                    this.team2.get(i2).p.sendAddchatYellow("Đội của bạn đã thua cuộc");
                                }
                                for(i2 = 0; i2< this.viewer.size(); i2++) {
                                    this.viewer.get(i2).p.sendAddchatYellow("Đội của người chơi "+this.name1+" đã giành chiến thắng");
                                }

                                DunListWin dunListWin = new DunListWin(this.name1, this.name2);
                            }
                            else if(countDie2 >= this.team2.size() && this.c1 == null) {
                                this.tienCuocTeam1 = this.tienCuocTeam1*2 - this.tienCuocTeam1*10/100;
                                Char checkChar = Client.gI().getNinja(this.idC1);
                                if (checkChar == null) {
                                    try {
                                        synchronized (Server.LOCK_MYSQL) {
                                            ResultSet res = SQLManager.stat.executeQuery("SELECT * FROM `ninja` WHERE `id`='" + this.idC1  + "';");
                                            if(res.next()) {
                                                long xuOld = res.getLong("xu");
                                                xuOld += this.tienCuocTeam1;
                                                if(xuOld > 2000000000L) {
                                                    xuOld = 2000000000L;
                                                }
                                                SQLManager.stat.executeUpdate("UPDATE `ninja` SET `xu`=" + xuOld + " WHERE `id`='" + this.idC1 + "';");
                                                res.close();
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                } else  {
                                    checkChar.upxuMessage(this.tienCuocTeam2);
                                    checkChar.p.sendAddchatYellow("Bạn đã giành chiến thắng, và nhận được " + Util.getFormatNumber(this.tienCuocTeam1) + " xu");
                                }
                                for(i2 = 0; i2< this.team1.size(); i2++) {
                                    this.team1.get(i2).p.sendAddchatYellow("Đội của bạn đã giành chiến thắng");
                                }
                                for(i2 = 0; i2< this.team2.size(); i2++) {
                                    this.team2.get(i2).p.sendAddchatYellow("Đội của bạn đã thua cuộc");
                                }
                                for(i2 = 0; i2< this.viewer.size(); i2++) {
                                    this.viewer.get(i2).p.sendAddchatYellow("Đội của người chơi "+this.name1+" đã giành chiến thắng");
                                }

                                DunListWin dunListWin = new DunListWin(this.name1, this.name2);
                            }
                        }
                        this.tienCuocTeam1 = 0;
                        this.tienCuocTeam2 = 0;
                    }
                }


            }
        }
    }

    //Hàm check kết thúc lôi đài khi map lôi đài [1]:111 hết thười gian
    public void finish() {
        if(!this.isFinish) { //nếu  isFinish = false thì mới chạy code
            this.setTime(10000L); //set lại thời gian đếm ngược của map để kết thúc lôi đài
            this.isFinish = true; //set lại isFinish = true
            this.isStart = false; //set lại biến  isStart = false
            synchronized (this) {
                byte i2;
                for(i2 = 0; i2< this.team1.size(); i2++) {
                    if(this.team1.get(i2) != null) {
                        this.team1.get(i2).p.setEffect(14,0,10000,0); //set lại hiệu ứng đình chiến cho người chơi trong khi đợi thoát khỏi lôi đài
                        this.team1.get(i2).p.sendAddchatYellow("Kết quả 2 đội hoà nhau");
                    }

                }
                for(i2 = 0; i2< this.team2.size(); i2++) {
                    if(this.team2.get(i2) != null) {
                        this.team2.get(i2).p.setEffect(14,0,10000,0); //set lại hiệu ứng đình chiến cho người chơi trong khi đợi thoát khỏi lôi đài
                        this.team2.get(i2).p.sendAddchatYellow("Kết quả 2 đội hoà nhau");
                    }

                }
                for(i2 = 0; i2< this.viewer.size(); i2++) {
                    if(this.viewer.get(i2) != null) {
                        this.viewer.get(i2).p.sendAddchatYellow("Kết quả 2 đội hoà nhau");
                    }
                }
                this.tienCuocTeam1 -= this.tienCuocTeam1 / 100; //Trừ 1% tiền cược người chơi khi HOÀ
                this.tienCuocTeam2 -= this.tienCuocTeam2 / 100; //Trừ 1% tiền cược người chơi khi HOÀ

                Char checkChar = Client.gI().getNinja(this.idC1);
                if (checkChar == null) {
                    try {
                        synchronized (Server.LOCK_MYSQL) {
                            ResultSet res = SQLManager.stat.executeQuery("SELECT * FROM `ninja` WHERE `id`='" + this.idC1  + "';");
                            if(res.next()) {
                                long xuOld = res.getLong("xu");
                                xuOld += this.tienCuocTeam1;
                                if(xuOld > 2000000000L) {
                                    xuOld = 2000000000L;
                                }
                                SQLManager.stat.executeUpdate("UPDATE `ninja` SET `xu`=" + xuOld + " WHERE `id`='" + this.idC1 + "';");
                                res.close();
                            }
                        }
                    } catch (Exception e) {
                    }
                } else  {
                    checkChar.upxuMessage(this.tienCuocTeam1);
                    checkChar.p.sendAddchatYellow("Kết quả hoà, cuộc so tài đã kết thúc bạn nhận lại " + Util.getFormatNumber(this.tienCuocTeam1) + " xu");
                }

                checkChar = Client.gI().getNinja(this.idC2);
                if (checkChar == null) {
                    try {
                        synchronized (Server.LOCK_MYSQL) {
                            ResultSet res = SQLManager.stat.executeQuery("SELECT * FROM `ninja` WHERE `id`='" + this.idC2  + "';");
                            if(res.next()) {
                                long xuOld = res.getLong("xu");
                                xuOld += this.tienCuocTeam2;
                                if(xuOld > 2000000000L) {
                                    xuOld = 2000000000L;
                                }
                                SQLManager.stat.executeUpdate("UPDATE `ninja` SET `xu`=" + xuOld + " WHERE `id`='" + this.idC2 + "';");
                                res.close();
                            }
                        }
                    } catch (Exception e) {
                    }
                } else  {
                    checkChar.upxuMessage(this.tienCuocTeam2);
                    checkChar.p.sendAddchatYellow("Kết quả hoà, cuộc so tài đã kết thúc bạn nhận lại " + Util.getFormatNumber(this.tienCuocTeam2) + " xu");
                }
            }
        }
    }

    //Hàm khởi tạo mới thời gian cho map
    private void setTime(long time) {
        synchronized (this) {
            this.time = System.currentTimeMillis() + time;
            byte i;
            if(this.team1.size() > 0) {
                for (i = 0; i < this.team1.size(); ++i) {
                    if(this.team1.get(i) != null) {
                        this.team1.get(i).p.setTimeMap((int)(this.time - System.currentTimeMillis()) / 1000);
                    }
                }
            }
            if(this.team2.size() > 0) {
                for (i = 0; i < this.team2.size(); ++i) {
                    if(this.team2.get(i) != null) {
                        this.team2.get(i).p.setTimeMap((int)(this.time - System.currentTimeMillis()) / 1000);
                    }
                }
            }
            if(this.viewer.size() > 0) {
                for (i = 0; i < this.viewer.size(); ++i) {
                    if(this.viewer.get(i) != null) {
                        this.viewer.get(i).p.setTimeMap((int)(this.time - System.currentTimeMillis()) / 1000);
                    }
                }
            }
        }
    }

    //hàm khởi tạo mới map lôi đài [1]:111 sau khi 2 người chơi đặt cược hoàn tất
    public void startDun() {

        if(!this.isStart) { //Kiểm tra biến isStart, nếu = false thì thực hiện hàm
            this.isStart = true; //set lại giá trị cho biến isStart

            //Đợi tất cả khối lệnh bên trong thực hiện hết mới chạy khối lệnh khác
            synchronized (this) {
                this.time = System.currentTimeMillis() + 600000L; //Khởi tạo lại thời gian cho map lôi đài [1]:111 sau khi được tạo mới
                Char _charr;
                //Lặp qua tất cả người chơi trong 2 team để chuyển map
                synchronized (this.team1) {
                    for(int i = 0; i < this.team1.size(); i++) {
                        _charr = this.team1.get(i); //Tạo 1 biến tạm để lưu trữ lại dữ liệu
                       if(_charr != null) {
                           _charr.typepk = (byte)4; //Đổi trạng thái pk = 4
                           Service.ChangTypePkId(_charr, (byte)4); //Send msg trạng thái pk
                           _charr.tileMap.leave(_charr.p); //kích người chơi khỏi map hiện tại
                           _charr.yDun = 264;
                           this.map[1].area[0].EnterMap0WithXY(_charr, (short)265, (short)264); //chuyển người chơi sang map lôi đài [1], khu vực [0], vị trí đứng trong map mới x = 265, y=264
                           _charr.p.setEffect(14,0,60000,0); //Set effect đình chiến cho người chơi
                           _charr.p.setTimeMap((int)(this.time - System.currentTimeMillis()) / 1000); // set thời gian hiển thị giảm theo mỗi giây

                       }
                    }
                }

                synchronized (this.team2) {
                    for(int i = 0; i < this.team2.size(); i++) {
                        _charr = this.team2.get(i);
                        if(_charr != null) {
                            _charr.typepk = (byte)5;
                            Service.ChangTypePkId(_charr, (byte)5);
                            _charr.tileMap.leave(_charr.p);
                            _charr.yDun = 264;
                            this.map[1].area[0].EnterMap0WithXY(_charr, (short)506, (short)264);
                            _charr.p.setEffect(14,0,60000,0);
                            _charr.p.setTimeMap((int)(this.time - System.currentTimeMillis()) / 1000);
                        }
                    }
                }
                this.isMap133 = false; //biến check isMap133 = false
            }
        }
    }
}
