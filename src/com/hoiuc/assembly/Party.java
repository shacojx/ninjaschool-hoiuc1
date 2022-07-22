package com.hoiuc.assembly;

import com.hoiuc.server.Service;
import com.hoiuc.stream.Cave;
import com.hoiuc.stream.Dun;

import java.util.ArrayList;

public class Party {
    private static int baseId = 10000;
    public static byte MAX_PLAYER = 6;
    public int partyId;
    public int charID;
    public byte numPlayer = 0;
    public ArrayList<Char> aChar = new ArrayList<>();
    public boolean isLock = false;
    public Object LOCK = new Object();
    private static Object LOCK_LOCAL = new Object();
    public Cave cave;
    public Dun dun;

    //Khởi tạo đối tượng
    public Party(Char _char) {
        synchronized (LOCK_LOCAL) {
            this.partyId = baseId++;
            this.numPlayer = (byte)(this.numPlayer + 1);
            this.aChar.add(_char);
            this.charID = _char.id;
        }
    }

    public void openCave(Cave cave, String name) {
        synchronized (this) {
            this.cave = cave;
            byte i;
            for (i = 0; i < this.aChar.size(); ++i) {
                this.aChar.get(i).p.sendAddchatYellow(name + " đã mở cửa hang động");
            }
        }
    }

    //Thêm người chơi vào nhóm
    public void addPlayerParty(Char _char) {
        synchronized (this.LOCK) {
            this.numPlayer = (byte)(this.numPlayer + 1);
            this.aChar.add(_char);
            _char.get().party = this;
        }
    }

    //Xoá người chơi khỏi nhóm
    public void removePlayer(int charId) {
        synchronized (this.LOCK) {
            byte i;
            Char _char;
            for (i = 0; i < this.numPlayer; i = (byte)(i + 1)) {
                _char = this.aChar.get(i);
                if (_char != null && _char.id == charId) {
                    this.numPlayer--;
                    this.aChar.remove(_char);
                    _char.party = null;
                    Service.OutParty(_char);
                    if (_char.tileMap.getNumPlayerParty(this.partyId) == 0) {
                        _char.tileMap.removeParty(this.partyId);
                        break;
                    }
                    if (this.numPlayer > 0 && _char.id == this.charID) {
                        this.charID = this.aChar.get(0).id;
                        byte j;
                        Char player;
                        for (j = 0; j < this.numPlayer; j = (byte)(j + 1)) {
                            player = this.aChar.get(j);
                            if (player != null) {
                                Service.ServerMessage(player, this.aChar.get(0).name + " được lên làm nhóm trưởng.");
                            }
                        }
                    }
                    break;
                }
            }
        }
        refreshPlayer();
    }

    //Tìm kiếm người chơi
    public Char findChar(int charID) {
        Char _char;
        byte i;
        for (i = 0; i < this.numPlayer; i++) {
            _char = this.aChar.get(i);
            if (_char != null && _char.id == charID)
                return _char;
        }
        return null;
    }

    public void clear() {
        synchronized (this.LOCK) {
            if (this.aChar != null) {
                byte i;
                Char player;
                for (i = 0; i < this.numPlayer; i = (byte)(i + 1)) {
                    player = this.aChar.get(i);
                    if (player != null && player.party != null) {
                        player.tileMap.removeParty(this.partyId);
                        player.party = null;
                    }
                }
                this.numPlayer = 0;
                this.aChar.clear();
                this.aChar = null;
            }
        }
    }

    //thay đổi trạng thái nhóm
    public void refreshLock() {
        synchronized (this.LOCK) {
            byte i;
            Char player;
            for (i = 0; i < this.numPlayer; i = (byte)(i + 1)) {
                player = this.aChar.get(i);
                if (player != null)
                    Service.lockParty(player, this.isLock);
            }
        }
    }

    //Làm mới người trong nhóm
    public void refreshPlayer() {
        synchronized (this.LOCK) {
            byte i;
            Char player;
            for (i = 0; i < this.numPlayer; i = (byte)(i + 1)) {
                player = this.aChar.get(i);
                if (player != null) {
                    Service.PlayerInParty(player, this);
                }
            }
        }
    }

    //Tin nhắn nhóm
    public void TeamMessage(String str) {
        synchronized (this.LOCK) {
            byte i;
            Char player;
            for (i = 0; i < this.numPlayer; i = (byte)(i + 1)) {
                player = this.aChar.get(i);
                if (player != null) {
                    Service.ServerMessage(player, str);
                }
            }
        }
    }
}
