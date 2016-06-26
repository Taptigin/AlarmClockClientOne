/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarmclockclientone;

import java.net.*;
import java.io.*;
import AlarmClock.Task;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Timer;
import java.util.TimerTask;


public class AlarmClockClientOne {
    Socket clientSocket;
    ObjectOutputStream out;
    ObjectInputStream in;
    Timer timer = new Timer();
    InetAddress ip;
    ArrayList<Task> list = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        ClientUI.main(args);
        
        
        
    }

    public AlarmClockClientOne()  {
    }
    
    public  void go()  
    {
        
        try{
            
            ip = InetAddress.getByName(ClientUI.getIP());
            
            int port = ClientUI.getPort();
            
            clientSocket = new Socket(ip, port);
            
            System.out.println(clientSocket);    
            
            
            
            try{
                
                String command = "download";
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                out.writeObject(command);out.flush();
                
                in = new ObjectInputStream(clientSocket.getInputStream());
                
                list = (ArrayList<Task>) in.readObject();
                
                for (Task list1 : list) {
                    System.out.println(list1.date);
                }
                for (Task task : list) {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            ClientUI.showTask(task.getName(), task.getDescription());
                        }
                    }, task.getDate());
                }
                
                
            }
            
            catch (ClassNotFoundException | IOException ex) {
                Logger.getLogger(AlarmClockClientOne.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } 
        
        catch (UnknownHostException ex) {
            Logger.getLogger(AlarmClockClientOne.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AlarmClockClientOne.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void updateTimer()
    {
        try {
            timer.cancel(); timer.purge();
            Timer timer = new Timer();
            String command = "download";
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(command);out.flush();
            
            in = new ObjectInputStream(clientSocket.getInputStream());
            
            list = (ArrayList<Task>) in.readObject();
            for (Task list1 : list) {
                System.out.println(list1.date);
            }
            for (Task task : list) {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        ClientUI.showTask(task.getName(), task.getDescription());
                    }
                }, task.getDate());
            }
        } 
        
        catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(AlarmClockClientOne.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void end()
    {
        try {
            String command = "END";
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(command);out.flush();
            in.close();out.close();clientSocket.close();
        } 
        
        catch (IOException ex) {
            Logger.getLogger(AlarmClockClientOne.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
