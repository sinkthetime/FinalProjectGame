package APCSFinal;

import java.awt.Color; 
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
 

import javax.swing.JOptionPane;
import javax.swing.JPanel;
 
 
public class TileGui extends JPanel {
 
        private static int size = 4;
        private static final int FONT_SIZE = 40;
        private static int difficulty = 1000;
       
        private TileGame game;
        private Font font;
        private Color backgroundColor, numColor, borderColor;
       
        private int slotWidth, slotHeight, slotXOffset, slotYOffset;
        //for moving window when clicked and dragged
        private int winX, winY, boardX, boardY;
        private boolean winmoved;
       
        public TileGui() {
                game = new TileGame(size, difficulty);
                font = new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE);
               
                slotWidth = TileRunner.WIDTH / size;
                slotHeight = TileRunner.HEIGHT / size;
                slotXOffset = slotWidth / 2 - FONT_SIZE / 4;
                slotYOffset = slotHeight / 2 + FONT_SIZE / 3;
                //used later for window/number coloring
                backgroundColor = new Color(153,0,255);
                numColor = new Color(255,173,51);
                borderColor = new Color(0, 0, 102);
                winmoved = false;
               
                setFocusable(true);
                requestFocus();
               
                
                
                //window moves when clicked
                addMouseMotionListener(new MouseMotionAdapter() {
                        @Override
                        public void mouseDragged(MouseEvent e) {
                                if(winmoved) {
                                        winX = e.getXOnScreen() - boardX;
                                        winY = e.getYOnScreen() - boardY;
                                        TileRunner.gui.setLocation(winX, winY);
                                }
                                else {
                                        winmoved = true;
                                        boardX = e.getX();
                                        boardY = e.getY();
                                }
                        }
                       
                        @Override
                        public void mouseMoved(MouseEvent e) {
                                winmoved = false;
                        }
                });
               
                //restart/popup window with multiple options for
                addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                                int key = e.getKeyCode();
                                game.keyPressed(key);
                                repaint();
                                if(game.isCorrect()) {
                                  //add option box
                                	int y = 0;
                                	int n = 1;
                                	int restart = JOptionPane.showConfirmDialog(TileRunner.gui, "Play again","End game", JOptionPane.YES_NO_OPTION);
                                	if (restart == JOptionPane.NO_OPTION){
                                        System.exit(0);
                                	}
                                	difficulty+= 10;
                                	//size+=1;
                                	game = new TileGame(size, difficulty);
                                	repaint();                                        
                                }
                        }
                });
        }
       
        
        //drawing borders around grid/between boxes
        private void drawBorders(Graphics2D board) {
                board.setColor(borderColor);
                for(int i = 0; i < 3; i++)
                        board.drawRect(i, i, TileRunner.WIDTH - 1 - 2 * i, TileRunner.HEIGHT - 1 - 2 * i);
               
                for(int  i = 1; i < 4; i++) {
                        int level = i * slotHeight;
                        board.drawLine(0, level, TileRunner.WIDTH, level);
                        board.drawLine(0, level + 1, TileRunner.WIDTH, level + 1);
                        board.drawLine(0, level + 2, TileRunner.WIDTH, level + 2);
 
                        board.drawLine(level, 0, level, TileRunner.HEIGHT);
                        board.drawLine(level + 1, 0, level + 1, TileRunner.HEIGHT);
                        board.drawLine(level + 2, 0, level + 2, TileRunner.HEIGHT);
                }
        }
        
        @Override
        public void paint(Graphics g) {
                Graphics2D board = (Graphics2D)g;
                board.setFont(font);
                board.setColor(backgroundColor);
                board.fillRect(0, 0, getWidth(), getHeight());
                drawBorders(board);
               
                board.setColor(numColor);
                for(int r = 0; r < 4; r++) {
                        for(int c = 0; c < 4; c++) {
                                if(game.getSpot(r, c) != 0)
                                        board.drawString(""+game.getSpot(r, c), slotXOffset + r * slotWidth, slotYOffset + c * slotHeight);
                        }
                }
        }
}