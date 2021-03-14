package com.azhu.portScanner;

import java.net.InetAddress;

//扫描Ip地址段查看合法Ip的线程
public class ScanIpThread extends Thread{

    EditorWindow main = null;
    String  Ip ;

    /**
     **构造函数
     */
    ScanIpThread(EditorWindow main,String  Ip ){
        this.main = main;
        this.Ip = Ip ;
    }

    public synchronized void run(){
        try {
            int startPort = main.getStartPort();
            int endPort = main.getEndPort();
            for(int i = startPort;i <= endPort; i++) {
                //扫描开放的Ip
                InetAddress.getByName(Ip);
                int portOfThread = main.getPortOfThread();
                if((i + portOfThread) <= endPort) {
                    new ScanThread(main,i, i + portOfThread,Ip).start();
                    i += portOfThread;
                }
                else {
                    new ScanThread(main,i, endPort,Ip).start();
                    i += portOfThread;
                }
            }
        } catch (Exception e) {
            System.out.println(Ip+"\n");
        }

    }
}
