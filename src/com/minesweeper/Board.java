package com.minesweeper;

import java.util.Scanner;
import java.io.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Board extends JPanel {

    private final int NUM_IMAGES = 13;
    // size in pixels
    private final int CELL_SIZE = 15;

    private final int COVER_FOR_CELL = 10;
    private final int MARK_FOR_CELL = 10;
    private final int EMPTY_CELL = 0;
    private final int MINE_CELL = 9;
    private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    private final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;

    private final int DRAW_MINE = 9;
    private final int DRAW_COVER = 10;
    private final int DRAW_MARK = 11;
    private final int DRAW_WRONG_MARK = 12;

    // adjust for number of mines and size of game
    private final int N_MINES = 40;
    private final int N_ROWS = 16;
    private final int N_COLS = 16;

    private final int BOARD_WIDTH = N_COLS * CELL_SIZE +1;
    private final int BOARD_HEIGHT = N_ROWS * CELL_SIZE +1;

    private int[] field;
    private boolean inGame;
    private int minesLeft;
    private Image[] img;

    private int allCells;
    private JLabel statusbar;
    
    public Board(JLabel statusbar) {

        this.statusbar = statusbar;
        
        initBoard();
    }

    private void initBoard() {

        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));

        img = new Image[NUM_IMAGES];

        for (int i = 0; i < NUM_IMAGES; i++) {

            var path = "src/resources/" + i + ".png";

            img[i] = (new ImageIcon(path)).getImage();
        }

        addMouseListener(new MinesAdapter());
        newGame();
    }
    public void saveGame() {
    	try {
    	BufferedWriter bw = new BufferedWriter(new FileWriter("src/resources/save.txt"));

        // write text to file
    	bw.write(""+minesLeft);
        for (int i = 0; i < field.length; i++) {
			bw.write(" "+field[i]);
		}

        // close the writer
        bw.close();
	
    	}
    	catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void loadGame() {
    	try {
    		Scanner scanner=new Scanner(new File("src/resources/save.txt"));
        	inGame = true;
            minesLeft = scanner.nextInt();
            statusbar.setText(Integer.toString(minesLeft));
        	allCells = N_ROWS * N_COLS;
            field = new int[allCells];
            // read text from file
        	 for (int i = 0; i < field.length; i++) {
    			field[i]=scanner.nextInt();
    		}

            scanner.close();
		} catch (FileNotFoundException e) {
			// TODO: handle exception
		}
    	
    }
    
    public void newGame() {

        int cell;

        var random = new Random();
        inGame = true;
        minesLeft = N_MINES;

        allCells = N_ROWS * N_COLS;
        field = new int[allCells];

        for (int i = 0; i < allCells; i++) {

            field[i] = COVER_FOR_CELL;
        }

        statusbar.setText(Integer.toString(minesLeft));

        int i = 0;

        while (i < N_MINES) {

            int position = (int) (allCells * random.nextDouble());

            if ((position < allCells)
                    && (field[position] != COVERED_MINE_CELL)) {

                int current_col = position % N_COLS;
                field[position] = COVERED_MINE_CELL;
                i++;

                if (current_col > 0) {
                    cell = position - 1 - N_COLS;
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position - 1;
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }

                    cell = position + N_COLS - 1;
                    if (cell < allCells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                }

                cell = position - N_COLS;
                if (cell >= 0) {
                    if (field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
                }

                cell = position + N_COLS;
                if (cell < allCells) {
                    if (field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
                }

                if (current_col < (N_COLS - 1)) {
                    cell = position - N_COLS + 1;
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position + N_COLS + 1;
                    if (cell < allCells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position + 1;
                    if (cell < allCells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                }
            }
        }
    }
    public void checkEmpty(int cell) {
        	if (field[cell] > COVERED_MINE_CELL)
            {
        		field[cell] -= MARK_FOR_CELL;
        		minesLeft++;
        		String msg = Integer.toString(minesLeft);
        		statusbar.setText(msg);
        		if (field[cell] == EMPTY_CELL) {
                    find_empty_cells(cell);
                }
            }
        	if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL) {
                    find_empty_cells(cell);
                }
        	}
	}
    private void find_empty_cells(int j) {

        int current_col = j % N_COLS;
        int cell;

        if (current_col > 0) {
            cell = j - N_COLS - 1;
            if (cell >= 0) {
            	checkEmpty(cell);
            }

            cell = j - 1;
            if (cell >= 0) {
            	checkEmpty(cell);
            }

            cell = j + N_COLS - 1;
            if (cell < allCells) {
            	checkEmpty(cell);
            }
        }

        cell = j - N_COLS;
        if (cell >= 0) {
        	checkEmpty(cell);
        }

        cell = j + N_COLS;
        if (cell < allCells) {
        	checkEmpty(cell);
        }

        if (current_col < (N_COLS - 1)) {
            cell = j - N_COLS + 1;
            if (cell >= 0) {
            	checkEmpty(cell);
            }

            cell = j + N_COLS + 1;
            if (cell < allCells) {
            	checkEmpty(cell);
            }

            cell = j + 1;
            if (cell < allCells) {
            	checkEmpty(cell);
            }
        }

    }

    @Override
    public void paintComponent(Graphics g) {

        int uncover = 0;

        for (int i = 0; i < N_ROWS; i++) {

            for (int j = 0; j < N_COLS; j++) {

                int cell = field[(i * N_COLS) + j];

                if (inGame && cell == MINE_CELL) {

                    inGame = false;
                }

                if (!inGame) {

                    if (cell == COVERED_MINE_CELL) {
                        cell = DRAW_MINE;
                    } else if (cell == MARKED_MINE_CELL) {
                        cell = DRAW_MARK;
                    } else if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_WRONG_MARK;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                    }
                } else {

                    if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_MARK;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                        uncover++;
                    }
                }

                g.drawImage(img[cell], (j * CELL_SIZE),
                        (i * CELL_SIZE), this);
            }
        }

        if (uncover == 0 && minesLeft == 0) {

            inGame = false;
            statusbar.setText("Game won");

        } else if (!inGame) {
            statusbar.setText("Game lost");
        }
    }

    private class MinesAdapter extends MouseAdapter {
    	boolean doRepaint;
    	public void checkCell(int cell) {
    		if(cell>=0 && cell<N_COLS*N_ROWS)
    		{
        	if (field[cell] > COVERED_MINE_CELL) {

                return;
            }

            if (field[cell] > MINE_CELL ) {

                field[cell] -= COVER_FOR_CELL;
                doRepaint = true;

                if (field[cell] == MINE_CELL) {
                    inGame = false;
                }

                if (field[cell] == EMPTY_CELL) {
                    find_empty_cells(cell);
                }
            }
    		}
    	}
        @Override
        public void mousePressed(MouseEvent e) {

            int x = e.getX();
            int y = e.getY();

            int cCol = x / CELL_SIZE;
            int cRow = y / CELL_SIZE;
            int cell = (cRow * N_COLS) + cCol;
            doRepaint = false;

            if (!inGame) {

                newGame();
                repaint();
            }

            if ((x < N_COLS * CELL_SIZE) && (y < N_ROWS * CELL_SIZE)) {

                if (e.getButton() == MouseEvent.BUTTON3) {

                    if (field[cell] > MINE_CELL) {

                        doRepaint = true;

                        if (field[cell] <= COVERED_MINE_CELL) {

                            if (minesLeft > 0) {
                                field[cell] += MARK_FOR_CELL;
                                minesLeft--;
                                String msg = Integer.toString(minesLeft);
                                statusbar.setText(msg);
                            } else {
                                statusbar.setText("Zero marks left");
                            }
                        } else {

                            field[cell] -= MARK_FOR_CELL;
                            minesLeft++;
                            String msg = Integer.toString(minesLeft);
                            statusbar.setText(msg);
                        }
                    }

                }
                else if(e.getButton() == MouseEvent.BUTTON2)
                {
                	checkCell(cell+1);
                	checkCell(cell-1);
                	checkCell(cell+N_COLS);
                	checkCell(cell-N_COLS);
                	checkCell(cell+N_COLS+1);
                	checkCell(cell+N_COLS-1);
                	checkCell(cell-N_COLS+1);
                	checkCell(cell-N_COLS-1);
                	
                }
                	else {

                	checkCell(cell);
                }

                if (doRepaint)
                {
                    repaint();
                }
            }
        }
    }
}

