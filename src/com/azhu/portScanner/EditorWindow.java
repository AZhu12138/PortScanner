package com.azhu.portScanner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class EditorWindow extends JFrame implements ActionListener {

    private JPanel top = new JPanel();
    private JPanel bottom = new JPanel();

    private JLabel startIp = new JLabel("扫描的Ip");
    private JLabel endIp = new JLabel("结束Ip");
    private JLabel l_startPort = new JLabel("起始端口");
    private JLabel l_endPort = new JLabel("结束端口");
    private JLabel l_portOfThread = new JLabel("每个线程扫描端口数");
    private JLabel showResult = new JLabel("扫描结果");
    private JLabel empty = new JLabel("                      " +
            "                                      ");
    private JLabel type = new JLabel("选择扫描的类型");
    private JLabel status = new JLabel("扫描状态：未开始扫描");
    private JLabel netAddress = new JLabel("扫描的网址");


    private JTextField f_endIp = new JTextField(12);
    private JTextField f_startIp = new JTextField(12);
    private JTextField f_startPort = new JTextField(5);
    private JTextField f_endPort = new JTextField(5);
    private JTextField f_portOfThread = new JTextField(5);
    private JTextField f_netAddress = new JTextField(30);

    private JButton startScanner = new JButton("扫描");
    private JButton localIp = new JButton("查看本机IP");
    private JButton clear = new JButton("清空");
    private JButton reset = new JButton("重置");

    private JTextArea message = new JTextArea(20, 20);
    private JScrollPane result = new JScrollPane(message);
    private JComboBox comboBox = new JComboBox();

    private String startIpStr,endIpStr,netAddressStr;
    private String portType = "0";
    private int startPort,endPort,portOfThread,threadNum;

    /**
     **构造函数
     */
    public EditorWindow() {
        this.setTitle("多线程端口扫描器（阿猪_12138）");

        message.setEditable(false); //设置文本区域不可编辑
        result.setColumnHeaderView(showResult);

        comboBox.addItem("地址");
        comboBox.addItem("地址段");
        comboBox.addItem("网址");

        netAddress.setVisible(false);
        f_netAddress.setVisible(false);

        endIp.setVisible(false) ;
        f_endIp.setVisible(false) ;

        top.add(type);
        top.add(comboBox) ;

        top.add(netAddress);
        top.add(f_netAddress);

        top.add(startIp) ;
        top.add(f_startIp) ;
        top.add(endIp) ;
        top.add(f_endIp) ;
        top.add(l_startPort) ;
        top.add(f_startPort) ;
        top.add(l_endPort) ;
        top.add(f_endPort) ;
        top.add(l_portOfThread) ;
        top.add(f_portOfThread) ;

        bottom.add(status) ;
        bottom.add(empty) ;
        bottom.add(empty) ;
        bottom.add(empty) ;
        bottom.add(empty) ;
        bottom.add(empty) ;
        bottom.add(empty) ;
        bottom.add(startScanner) ;
        bottom.add(clear);
        bottom.add(reset);
        bottom.add(localIp) ;

        this.add(top, BorderLayout.NORTH);
        this.add(result,BorderLayout.CENTER) ;
        this.add(bottom,BorderLayout.SOUTH) ;

        comboBox.addActionListener(this) ;
        startScanner.addActionListener(this) ;
        localIp.addActionListener(this) ;
        clear.addActionListener(this) ;
        reset.addActionListener(this) ;

        setSize(1000, 500);

        //生成psMenuBar对象，并放置在框架之上
        setJMenuBar(new psMenuBar(this));

        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==startScanner){ //点击扫描按钮
            //点击时刻
            //判断下拉栏选择为“网址”
            if(((String) comboBox.getSelectedItem()).equals("网址")){
                netAddressStr = f_netAddress.getText().trim();//得到输入的网址
                if (checkNetAddress(netAddressStr)) {
                    try {
                        startIpStr = InetAddress.getByName(netAddressStr).getHostAddress();//由网址得到IP地址
                    } catch (UnknownHostException unknownHostException) {
                        JOptionPane.showMessageDialog(this, "网址有误或网址不可达！");
                        unknownHostException.printStackTrace();
                        return;
                    }
                }else {
                    JOptionPane.showMessageDialog(this, "网址格式错误");
                    return;
                }
            }else {
                startIpStr = f_startIp.getText().trim();   //得到输入的Ip
            }
            if (checkIP(startIpStr)) {
                //判断是否为数字
                try {
                    startPort = Integer.parseInt(f_startPort.getText().trim());
                    endPort = Integer.parseInt(f_endPort.getText().trim());
                    portOfThread = Integer.parseInt(f_portOfThread.getText().trim());
                    threadNum = (endPort - startPort) / portOfThread + 1;
                    //判断端口号的范围
                    if (startPort < 0 || endPort > 65535 || startPort > endPort) {
                        JOptionPane.showMessageDialog(this, "端口号范围：0~65535,并且最大端口号应大于最小端口号！");
                    } else {
                        if (portOfThread > endPort - startPort || portOfThread < 1) {
                            JOptionPane.showMessageDialog(this, "每个线程扫描的端口数不能大于所有的端口数且不能小于1");
                        } else {
                            //判断下拉栏选择为“地址”或“网址”（非“地址段”）
                            if (!(((String) comboBox.getSelectedItem()).equals("地址段"))) {
                                message.append("************************************************************\n");
                                message.append("正在扫描单个IP地址: " + startIpStr + "\n");
                                message.append("每个线程扫描端口个数: " + portOfThread + "      开启的线程数: " + threadNum + "\n");
                                message.append("开始端口: " + startPort + "         结束端口: " + endPort + "\n");
                                //message.append("主机名: " + getHostname(startIpStr) + "\n");
                                message.append("开放的端口如下:" + "\n");
                                message.append("------------------------------------------------------------\n");
                                for (int i = startPort; i <= endPort; i++) {
                                    if ((i + portOfThread) <= endPort) {
                                        new ScanThread(this, i, i + portOfThread, startIpStr).start();
                                        i += portOfThread;
                                    } else {
                                        new ScanThread(this, i, endPort, startIpStr).start();
                                        i += portOfThread;
                                    }
                                }
                            } else {//下拉栏选择为“地址段”
                                endIpStr = f_endIp.getText();
                                if (checkIP(endIpStr)) {
                                    message.append("************************************************************" + "\n");
                                    message.append("正在扫描多个IP地址:\n");
                                    message.append("开放的端口如下:" + "\n");
                                    message.append("------------------------------------------------------------\n");
                                    //扫描Ip地址段
                                    Set ipSet = new HashSet<Object>();
                                    int start = Integer.valueOf(startIpStr.split("\\.")[3]);
                                    int end = Integer.valueOf(endIpStr.split("\\.")[3]);
                                    String starts = startIpStr.split("\\.")[0] + "." + startIpStr.split("\\.")[1] +
                                            "." + startIpStr.split("\\.")[2];
                                    //生成IP地址
                                    for (int i = start; i <= end; i++) {
                                        ipSet.add(starts + "." + i);    //地址段的每个地址存入集合
                                    }
                                    for (Object str : ipSet) {
                                        new ScanIpThread(this, str.toString()).start();
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(this, "请输入正确的Ip地址");
                                }
                            }
                        }
                    }
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(this, "错误的端口号或端口号和线程数必须为整数");
                }
            } else {
                JOptionPane.showMessageDialog(this, "请输入正确的Ip地址");
            }
        }
        else if(e.getSource()==reset){//点击重置按钮
            f_netAddress.setText("");
            f_startIp.setText("") ;
            f_endIp.setText("");
            f_startPort.setText("");
            f_endPort.setText("");
            f_portOfThread.setText("") ;
        } else if(e.getSource()==clear){//点击清空按钮
            message.setText("") ;
        } else if(e.getSource()==localIp){//点击查看本机IP按钮
            message.append("************************************************************" + "\n");
            try {
                message.append("本机IP地址为："+ InetAddress.getLocalHost().getHostAddress()+"\n");
            } catch (UnknownHostException unknownHostException) {
                message.append("查看出错！\n");
                unknownHostException.printStackTrace();
            }
        }else if(e.getSource()==comboBox){//点击下拉框
            String type=(String) comboBox.getSelectedItem();
            if(type.equals("地址")){
                netAddress.setVisible(false);
                f_netAddress.setVisible(false);
                endIp.setVisible(false) ;
                f_endIp.setVisible(false) ;
                startIp.setVisible(true);
                f_startIp.setVisible(true);
                startIp.setText("扫描的Ip") ;
            }else if(type.equals("地址段")){
                netAddress.setVisible(false);
                f_netAddress.setVisible(false);
                startIp.setVisible(true);
                f_startIp.setVisible(true);
                endIp.setVisible(true) ;
                f_endIp.setVisible(true) ;
                startIp.setText("开始Ip") ;
            }else{
                startIp.setVisible(false);
                f_startIp.setVisible(false);
                endIp.setVisible(false);
                f_endIp.setVisible(false);
                netAddress.setVisible(true);
                f_netAddress.setVisible(true);
            }
        }
    }

    // 判断输入的IP是否合法
    private boolean checkIP(String str) {
        Pattern pattern = Pattern
                .compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]"
                        + "|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
        return pattern.matcher(str).matches();
    }
    //判断输入的网址是否合法
    private boolean checkNetAddress(String str){
        Pattern pattern = Pattern
                .compile("^([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]\\*)?$");
        return pattern.matcher(str).matches();
    }

    //根据Ip获得主机名
    public static synchronized String getHostname(String host){
        InetAddress addr ;
        try {
            addr = InetAddress.getByName(host);
            return addr.getHostName();
        } catch (UnknownHostException e) {
            return "网络不通或您输入的信息无法构造InetAddress对象！";
        }
    }

    public JTextArea getMessage(){
        return message;
    }
    public JLabel getStatus(){
        return status;
    }
    public int getPortOfThread() {
        return portOfThread;
    }
    public int getStartPort() {
        return startPort;
    }
    public int getEndPort() {
        return endPort;
    }
}
