import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Tetris{
    private final int ROW = 20;
    private final int COL = 12;
    private final int LEN = 35;


    private final int LEFT_MARGIN = LEN;
    private final int UP_MARGIN = LEN;


    private final int AREA_WIDTH = LEN*22;
    private final int AREA_HEIGHT = LEN*22;

    private int score = 0;


    private MyCanvas drawArea = new MyCanvas();

    private JFrame f = new JFrame("Tetris");

    private BufferedImage image = new BufferedImage(AREA_WIDTH, AREA_HEIGHT, BufferedImage.TYPE_INT_RGB);
    private Graphics g = image.createGraphics();

    private int[][] map = new int[COL][ROW];


    private Color[] color = new Color[]{Color.pink, Color.red, Color.white, Color.blue, Color.cyan, Color.yellow, Color.magenta, Color.gray};

    private Color[][] mapColor = new Color[COL][ROW];

    int wordX = LEN*14;
    int wordY = LEN*6;

    private int type, state, x, y, nextType, nextState;

    Random rand = new Random();

    int ShapeColor = rand.nextInt(7);

    private boolean newBegin = true;

    private int[][][][] shape = new int[][][][]{
        { { {0,1,0,0}, {1,1,0,0}, {1,0,0,0}, {0,0,0,0} },
        { {0,0,0,0}, {1,1,0,0}, {0,1,1,0}, {0,0,0,0} },
        { {0,1,0,0}, {1,1,0,0}, {1,0,0,0}, {0,0,0,0} },
        { {0,0,0,0}, {1,1,0,0}, {0,1,1,0}, {0,0,0,0} } },
        { { {0,0,0,0}, {0,1,1,0}, {0,1,1,0}, {0,0,0,0} },
        { {0,0,0,0}, {0,1,1,0}, {0,1,1,0}, {0,0,0,0}  },
        { {0,0,0,0}, {0,1,1,0}, {0,1,1,0}, {0,0,0,0}  },
        { {0,0,0,0}, {0,1,1,0}, {0,1,1,0}, {0,0,0,0}  } },


    };


    private void init(){
        drawArea.setPreferredSize(new Dimension(AREA_WIDTH, AREA_HEIGHT));
        f.add(drawArea);

        f.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        turn();
                        break;
                    case KeyEvent.VK_LEFT:
                        left();
                        break;
                    case KeyEvent.VK_RIGHT:
                        right();
                        break;
                    case KeyEvent.VK_DOWN:
                        down();
                        break;
                    case KeyEvent.VK_F1:
                    	score = score + 100;
                    	Repaint();
                }
            }
        });
        Timer timer = new Timer(1000, new timerListener());
        newShape();
        timer.start();
        f.pack();
        int screenSizeX = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int screenSizeY = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        int fSizeX = (int)f.getSize().getWidth();
        int fSizeY = (int)f.getSize().getHeight();
        f.setResizable(false);
        f.setBounds((screenSizeX-fSizeX)/2, (screenSizeY-fSizeY)/2, fSizeX,fSizeY );
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }


    private void Repaint(){
        g.setColor(Color.pink);
        g.fillRect(0, 0, AREA_WIDTH, AREA_HEIGHT);
        g.setColor(Color.white);
        for (int offset = 0; offset <= 2; offset++){
            g.drawRect(LEFT_MARGIN-offset, UP_MARGIN-offset, COL*LEN+offset*2, ROW*LEN+offset*2);
        }

        g.setColor(Color.gray);
        g.setFont(new Font("Times New Roman", Font.BOLD, 20));
        g.drawString("Next Shape is", wordX, LEN*2);
        int nextX = wordX;
        int nextY = LEN*2;

        g.setColor(color[ShapeColor]);
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if (shape[nextType][nextState][i][j]==1) {
                    g.fill3DRect(nextX+10+i*LEN, nextY+10+j*LEN, LEN, LEN,true);
                }
            }
        }
        g.setColor(Color.black);
        g.setFont(new Font("Times", Font.BOLD, 15));
        g.drawString("Help：", wordX, wordY+LEN*2);
        g.drawString("Up：Rotate Shape", wordX, wordY+LEN*3);
        g.drawString("Left：Move Left", wordX, wordY+LEN*4);
        g.drawString("Right：Move Right", wordX, wordY+LEN*5);
        g.drawString("Down：Fall", wordX, wordY+LEN*6);
        g.drawString("F1：Cheat", wordX, wordY+LEN*7);
        g.setFont(new Font("Times", Font.BOLD, 25));
        g.drawString("Score：" + score, wordX, wordY+LEN*8);
        g.drawString("Chen Yuxuan", wordX, wordY+LEN*10);
        g.drawString("1W15BG12", wordX, wordY+LEN*11);
        g.drawString("Waseda University", wordX, wordY+LEN*12);
        g.setColor(color[ShapeColor]);
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if (shape[type][state][i][j]==1) {
                    g.fill3DRect(LEFT_MARGIN+(x+i)*LEN, UP_MARGIN+(y+j)*LEN, LEN, LEN,true);
                }
            }
        }
        for(int i = 0; i < COL; i++){
            for(int j = 0; j < ROW; j++){
                if (map[i][j] == 1) {
                    g.setColor(mapColor[i][j]);
                    g.fill3DRect(LEFT_MARGIN+i*LEN, UP_MARGIN+j*LEN, LEN, LEN,true);
                }
            }
        }

        drawArea.repaint();
    }

    private class MyCanvas extends JPanel{
        public void paint(Graphics g){
            g.drawImage(image, 0, 0, null);
        }
    }

    private boolean check(int type, int state, int x, int y){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if ( (shape[type][state][i][j] == 1) && ( (x+i>=COL) || (x+i<0 ) || (y+j>=ROW) || (map[x+i][y+j]==1) ) ) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isGameOver(int type, int state, int x, int y){
        return !check(type, state, x, y);
    }


    private void newShape(){
        ShapeColor = rand.nextInt(7);
        if(newBegin){
            type = rand.nextInt(2);
            state = rand.nextInt(4);
            newBegin = false;
        }
        else{
            type = nextType;
            state = nextState;
        }
        nextType = rand.nextInt(2);
        nextState = rand.nextInt(4);
        x = 3;
        y = 0;
        if(isGameOver(type, state, x, y)){
            JOptionPane.showMessageDialog(f, "GAME OVER!");
            newGame();
        }
        Repaint();

    }


    private void newGame(){
        newMap();
        score = 0;
        newBegin = true;
    }


    private void newMap(){
        for(int i = 0; i < COL; i++){
            Arrays.fill(map[i],0);
        }

    }


    private void delLine(){ //Delete Line
        boolean flag = true;
        int addScore = 0;
        for(int j = 0; j < ROW; j++){
            flag = true;
            for( int i = 0; i < COL; i++){
                if (map[i][j]==0){
                    flag = false;
                    break;
                }
            }
            if(flag){
                addScore += 10;
                for(int t = j; t > 0; t--){
                    for(int i = 0; i <COL; i++){
                        map[i][t] = map[i][t-1];
                    }
                }
            }
        }
        score += addScore*addScore/COL;
    }


    private class timerListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(check(type, state , x, y+1) ){
                y = y +1;
            }
            else{
               add(type, state, x, y);
               delLine();
               newShape();
            }
            Repaint();
        }
    }

    private void add(int type, int state, int x, int y){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4 ; j++){
                if((y+j<ROW)&&(x+i<COL)&&(x+i>=0)&&(map[x+i][y+j]==0)){
                    map[x+i][y+j]=shape[type][state][i][j];
                    mapColor[x+i][y+j]=color[ShapeColor];
                }
            }
        }
    }


    private void turn(){
        int tmpState = state;
        state = (state + 1)%4;
        if (!check(type,state, x, y )) {
            state = tmpState;
        }
        Repaint();
    }

    private void left(){
        if(check(type,state, x-1, y)){
            --x;
        }
        Repaint();
    }

    private void right(){
        if (check(type,state, x+1, y)) {
            ++x;
        }
        Repaint();
    }

    private void down(){
        if (check(type,state, x, y+1)) {
            ++y;
        }
        else{
            add(type, state, x, y);
            delLine();
            newShape();
        }
        Repaint();
    }




    public static void main(String[] args){
        new Tetris().init();
    }
}