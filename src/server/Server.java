/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author HP
 */
public class Server {

    public static volatile ServerThreadBus svThreadBus;
    public static Socket skOfServer;

    public static void main(String[] args) {
        ServerSocket listener = null;
        svThreadBus = new ServerThreadBus();
        System.out.println("Server is waiting to accept user...");
        int clientNumber = 0;

        try {
            listener = new ServerSocket(3333);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                10,
                100,
                10,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(8)
        );
        try {
            while (true) {
                skOfServer = listener.accept();
                ServerThread serverThread = new ServerThread(skOfServer, clientNumber++);
                svThreadBus.add(serverThread);
                System.out.println("Số thread đang chạy là: " + svThreadBus.getLength());
                executor.execute(serverThread);
            }
        } catch (IOException ex) {
            System.out.println("Có lỗi");
        } finally {
            try {
                listener.close();
            } catch (IOException ex) {
            	ex.printStackTrace();
            }
        }
    }
}
