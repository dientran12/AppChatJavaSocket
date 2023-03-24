/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP
 */
public class ServerThreadBus {

    private List<ServerThread> listServerThreads;

    public List<ServerThread> getListServerThreads() {
        return listServerThreads;
    }

    public ServerThreadBus() {
        this.listServerThreads = new ArrayList<>();

    }

    public void mutilCastSend(String message) {
        for (ServerThread serverThread : Server.svThreadBus.getListServerThreads()) {
            try {
                serverThread.write(message);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void boardCast(int id, String message) {
        for (ServerThread serverThread : Server.svThreadBus.getListServerThreads()) {
            if (serverThread.getClientNumber() == id) {
                continue;
            } else {
                try {
                    serverThread.write(message);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void sendOnlineList() {
        String res = "";
        List<ServerThread> threadbus = Server.svThreadBus.getListServerThreads();
        for (ServerThread serverThread : threadbus) {
            res += serverThread.getClientNumber() + "-";
        }
        Server.svThreadBus.mutilCastSend("update-online-list" + "," + res);
    }

    public void sendMessageToPersion(int id, String message) {
        for(ServerThread serverThread : Server.svThreadBus.getListServerThreads()) {
            if (serverThread.getClientNumber() == id) {
                try {
                    serverThread.write("global-message" +"," + message);
                    break;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    public void add(ServerThread serverThread){
        listServerThreads.add(serverThread);
    }

    public void remove(int id) {
        for(int i = 0; i<Server.svThreadBus.getLength(); i++) {
            if(Server.svThreadBus.getListServerThreads().get(i).getClientNumber()== id) {
                Server.svThreadBus.listServerThreads.remove(i);
            }
        }
    }
    public int getLength() {
        return listServerThreads.size();
    }

}
