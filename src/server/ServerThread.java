/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.processing.Messager;

/**
 *
 * @author HP
 */
public class ServerThread implements Runnable {

    public ServerThread(Socket skOfSever, int clientNumber) {
        this.skOfSever = skOfSever;
        this.clientNumber = clientNumber;
        this.isClosed = false;
    }

    private Socket skOfSever;
    private int clientNumber;
    private BufferedReader input;
    private BufferedWriter output;
    private boolean isClosed;

    public BufferedReader getInput() {
        return input;
    }

    public BufferedWriter getOutput() {
        return output;
    }

    public int getClientNumber() {
        return clientNumber;
    }

    @Override
    public void run() {
        try {
            input = new BufferedReader(new InputStreamReader(skOfSever.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(skOfSever.getOutputStream()));
            System.out.println("new thread start up successfully, ID: " + clientNumber);
            write("get-id" + "," + this.clientNumber);
            Server.svThreadBus.sendOnlineList();
            Server.svThreadBus.mutilCastSend("globel-message" + "," + "---Client " + this.clientNumber + "entered the chat room");
            String message;
            while (!isClosed) {
                message = input.readLine();
                if (message == null) {
                    break;
                }
                String[] messageSplit = message.split(",");
                if (messageSplit[0].equals("send-to-global")) {
                    Server.svThreadBus.boardCast(this.getClientNumber(), "global-message" + "," + "Client " + messageSplit[2] + ": " + messageSplit[1]);
                }
                if (messageSplit[0].equals("send-to-person")) {
                    Server.svThreadBus.sendMessageToPersion(Integer.parseInt(messageSplit[3]), "Client " + messageSplit[2] + " (tới bạn): " + messageSplit[1]);
                }
            }
        } catch (IOException ex) {
            isClosed = true;
            Server.svThreadBus.remove(clientNumber);
            System.out.println(this.clientNumber + " đã thoát");
            Server.svThreadBus.sendOnlineList();
            Server.svThreadBus.mutilCastSend("global-message" + "," + "---Client " + this.clientNumber + " left the chat room");
        }
    }

    public void write(String message) throws IOException {
        output.write(message);
        output.newLine();
        output.flush();
    }

}
