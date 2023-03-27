package com.minesweeper;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Minesweeper extends JFrame implements ActionListener {

    private JLabel statusbar;
    JMenuBar menuBar;
    JMenu game,Exit;
    JMenuItem newgame,savegame,loadgame,exit;
    public Board board;
    public Minesweeper() {
    	//this.addKeyListener(this);
    	menuBar =new JMenuBar();
    	
    	game=new JMenu("Game");
    	Exit=new JMenu("Exit");
    	
    	newgame=new JMenuItem("New game",new  ImageIcon("src/resources/face.png"));
    	newgame.addActionListener(this);
    	game.add(newgame);
    	
    	savegame=new JMenuItem("Save game",new  ImageIcon("src/resources/save.png"));
    	savegame.addActionListener(this);
    	game.add(savegame);
    	
    	loadgame=new JMenuItem("Load game",new  ImageIcon("src/resources/load.png"));
    	loadgame.addActionListener(this);
    	game.add(loadgame);
    	
    	exit=new JMenuItem("Exit",new  ImageIcon("src/resources/9.png"));
    	exit.addActionListener(this);
    	game.add(exit);
    	

    	menuBar.add(game);
    	this.setJMenuBar(menuBar);
        initUI();
    }
    public void actionPerformed(ActionEvent e)
    {
    	String command=e.getActionCommand();
    	if(command.equals("Exit"))
    	{
    		System.exit(0);
    	}
    	if(command.equals("New game"))
    	{
    		board.newGame();
    		repaint();
    	}
    	if(command.equals("Save game"))
    	{
    		board.saveGame();
    	}
    	if(command.equals("Load game"))
    	{
    		board.loadGame();
    		repaint();
    	}
    }
    private void initUI() {
    	
        statusbar = new JLabel("");
        add(statusbar, BorderLayout.SOUTH);
        board=new Board(statusbar);
        add(board, BorderLayout.CENTER);
        
        setResizable(false);
        pack();

        setTitle("Minesweeper");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

       

            var ex = new Minesweeper();
            ex.setVisible(true);
       
    }
}
