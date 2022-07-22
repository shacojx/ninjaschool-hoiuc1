package com.hoiuc.assembly;

import com.hoiuc.stream.Client;
import com.hoiuc.stream.Server;

public class Alert {
    private String alert;
    public Alert() {

    }
    public Alert(String alert) {
        this.alert = alert;
    }

    public String getAlert() {
        return this.alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public void sendAlert(Player player) {
        Server.manager.sendTB(player, "Ninja Hồi Ức",  (this.getAlert() == null ? "" : this.getAlert())+"\n\n- Số người online: " + Client.gI().ninja_size());
    }
}
