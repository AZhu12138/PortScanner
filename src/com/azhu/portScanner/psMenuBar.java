package com.azhu.portScanner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class psMenuBar extends JMenuBar {

    //生成EditorWindow框架对象
    private EditorWindow main = null;

    //生成菜单组
    private JMenu jFile = new JMenu("菜单");
    private JMenu jHelp = new JMenu("帮助");

    //生成菜单项
    private JMenu jItemBackground = new JMenu("更改背景颜色");
    private JMenuItem jItemAbout = new JMenuItem("关于软件");
    private JMenuItem saveItem = new JMenuItem("保存:");
    private JMenuItem jItemExit = new JMenuItem("退出:");

    //生成“更改背景颜色”的菜单项
    private JMenuItem pink = new JMenuItem("粉色紫罗兰");
    private JMenuItem blue = new JMenuItem("梦幻蓝");
    private JMenuItem orange = new JMenuItem("活力橙");
    private JMenuItem gray = new JMenuItem("金属灰");
    private JMenuItem green = new JMenuItem("复古深绿");
    private JMenuItem white = new JMenuItem("默认:天使白");

    /**
     **构造函数
     */
    public psMenuBar(EditorWindow editorWindow){

        this.main = editorWindow;

        /**
         **初始化菜单栏
         */
        jItemBackground.add(pink);
        jItemBackground.add(blue);
        jItemBackground.add(orange);
        jItemBackground.add(gray);
        jItemBackground.add(green);
        jItemBackground.addSeparator();//分隔线
        jItemBackground.add(white);

        //设置快捷键，保存ctrl+s，退出ctrl+e
        saveItem.setMnemonic ('S');
        saveItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_S, InputEvent.CTRL_MASK));
        jItemExit.setMnemonic('E');
        jItemExit.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_E,InputEvent.CTRL_MASK));

        jFile.addSeparator();//分隔线
        jFile.add(jItemBackground);
        jFile.addSeparator();//分隔线
        jFile.add(saveItem);
        jFile.addSeparator();//分隔线
        jFile.add(jItemExit);

        jHelp.add(jItemAbout);

        this.add(jFile);
        this.add(jHelp);

        //为组件添加事件监听并实现
        //"更改背景颜色"
        pink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                main.getMessage().setBackground(new Color(235,100,147));
            }
        });
        blue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                main.getMessage().setBackground(new Color(75, 125, 227));
            }
        });
        orange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                main.getMessage().setBackground(Color.orange);
            }
        });
        gray.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                main.getMessage().setBackground(Color.gray);
            }
        });
        green.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                main.getMessage().setBackground(new Color(0,112,10));
            }
        });
        white.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                main.getMessage().setBackground(Color.white);
            }
        });

        //“保存”
        saveItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showSaveDialog(null);

                //点击“保存”
                if(returnVal == 0){
                    File saveFile = fc.getSelectedFile();
                    try {
                        FileWriter writeOut = new FileWriter(saveFile);
                        writeOut.write(main.getMessage().getText());
                        writeOut.close();
                    }
                    catch (IOException ex) {
                        System.out.println("保存失败");
                    }
                }
                //点击“取消”
                else
                    return;
            }
        });

        //“退出”
        jItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                System.exit(1);
            }
        });

        //“关于”
        jItemAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                new AboutDialog().setLocationRelativeTo(null);
            }
        });
    }
}
