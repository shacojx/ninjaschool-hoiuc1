package com.hoiuc.io;

import com.hoiuc.server.Session;

public interface IMessageHandler {
    void processMessage(Session var1, Message var2);

    void onConnectionFail(Session var1);

    void onDisconnected(Session var1);

    void onConnectOK(Session var1);
}
