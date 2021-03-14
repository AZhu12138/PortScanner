package com.azhu.portScanner;

import java.net.Socket;

public class ScanThread extends Thread{

    EditorWindow main = null;
    int maxPort, minPort;
    String Ip;
    String portType = "unknowPort";

    /**
     **构造函数
     */
    ScanThread(EditorWindow main,int minPort, int maxPort,String Ip){
        this.main = main;
        this.minPort=minPort ;
        this.maxPort=maxPort ;
        this.Ip=Ip;
    }

    @SuppressWarnings("unchecked")//告诉编译器忽略指定的警告，不用在编译完成后出现警告信息。
    public void run() {
        main.getStatus().setText("扫描状态：正在扫描，请稍后、、、");
        Socket socket = null ;
        for(int i = minPort;i<maxPort;i++){
            main.getStatus().setText("扫描状态：正在扫描，请稍后、、、");
            try {
                socket = new Socket(Ip, i);
                portType = findPortType(i);//根据端口号，直接判断出端口的类型信息
                main.getMessage().append(" Ip: "+Ip+" 的 "+i+"端口（开放）:"+portType+"\n");
                socket.close();
            } catch (Exception e) {
                //端口为关闭状态
            }
        }
        main.getStatus().setText("扫描状态：扫描完成!") ;
    }

    //根据端口号，直接判断出端口的类型信息
    public String findPortType(int port){
        String porttype;
        switch(port){
            case 20:
                porttype = "(FTP Data)(TCP端口)";
                break;
            case 21:
                porttype = "(FTP 文件传输协议)(TCP端口)";
                break;
            case 23:
                porttype = "(TELNET 远程终端协议)(TCP端口)";
                break;
            case 25:
                porttype = "(SMTP 简单邮件传输协议)(TCP端口)";
                break;
            case 38:
                porttype = "(RAP)";
                break;
            case 53:
                porttype = "(DNS 域名服务器)(UDP端口)";
                break;
            case 69:
                porttype = "(TFTP 简单文件传输协议)(UDP端口)";
                break;
            case 79:
                porttype = "(FINGER)";
                break;
            case 80:
                porttype = "(HTTP 超文本传输协议)(TCP端口)";
                break;
            case 110:
                porttype = "(POP3 邮局协议版本3)(TCP端口)";
                break;
            case 135:
                porttype = "(RPC 远程过程调用)";
                break;
            case 139:
                porttype = "(netBIOS)";
                break;
            case 161:
                porttype = "(SNMP 简单网络管理协议)(UDP端口)";
                break;
            case 443:
                porttype = "(HTTPS 超文本传输安全协议)(TCP端口)";
                break;
            case 445:
                porttype = "(CIFS)";
                break;
            case 1433:
                porttype = "(SQL Server)";
                break;
            case 1521:
                porttype = "(Oracle)";
                break;
            case 3389:
                porttype = "(Terminal Service)";
                break;
            case 8080:
                porttype = "(OICQ)(UDP端口)";
                break;
            default:
                porttype = "(unknowPort)";
                break;
        }
        return porttype;
    }
}