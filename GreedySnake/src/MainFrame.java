import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class MainFrame extends JFrame {
    private Container cp = getContentPane();
    private int size = 15; //設置地圖為size*size大小
    private ImageIcon headImg = new ImageIcon("head.png");
    private ImageIcon bodyImg = new ImageIcon("body.png");
    private ImageIcon foodImg = new ImageIcon("food.png");
    private JLabel jlbArr[][] = new JLabel[size][size];  //標籤陣列，放置標籤並將標籤放入cp的gridLayout中
    private int dir, headR = 7, headC = 7; //dir:方向判定  headR和headC 用來進行頭移動前移動後row和column操作
    private Timer t;
    private Random rand = new Random();
    private ArrayList<ObjectImg> objectArr = new ArrayList<ObjectImg>();

    public MainFrame(){
        init();
        game();
    }

    void init(){ //初始化
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBounds(550,150,500,500);
        cp.setLayout(new GridLayout(size,size,1,1));
        cp.setBackground(Color.BLACK);
        adjustImg(headImg);
        adjustImg(bodyImg);
        adjustImg(foodImg);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyCode()){
                    case KeyEvent.VK_UP: //上
                        if(objectArr.size() < 2){ //當只有頭
                            dir = 1;
                        }else if (dir != 2){ //不只頭
                            dir = 1;
                        }
                        break;
                    case KeyEvent.VK_DOWN: //下
                        if(objectArr.size() < 2){
                            dir = 2;
                        }else if (dir != 1){
                            dir = 2;
                        }
                        break;
                    case KeyEvent.VK_LEFT: //左
                        if(objectArr.size() < 2){
                            dir = 3;
                        }else if (dir != 4){
                            dir = 3;
                        }
                        break;
                    case KeyEvent.VK_RIGHT: //右
                        if(objectArr.size() < 2){
                            dir = 4;
                        }else if (dir != 3){
                            dir = 4;
                        }
                        break;
                }
            }
        });
    }

    void game(){
        //======初始======
        jlbArrSet(); //先設置完jlb們以及將它們放入gridLayout格子中
        setImg();  //將文字轉圖案
        setFood(); //放食物
        //===============
        directMove();
    }

    void jlbArrSet(){ //生成並放置JLabel到cp的gridLayout格子中
        for (int i=0 ;i<size; i++){ //使用雙迴圈設置按鈕並將按鈕放入cp的gridLayout格子中
            for (int j=0; j<size; j++){
                jlbArr[i][j] = new JLabel();
                cp.add(jlbArr[i][j]);
            }
        }
        objectArr.add(new ObjectImg(headR,headC,"h")); //預設頭的位置
    }

    void setImg(){ //利用雙迴圈將文字轉為圖樣
        for (int i=0 ;i<size; i++){
            for (int j=0; j<size; j++){

                if (jlbArr[i][j].getText() == null){
                    jlbArr[i][j].setIcon(null);
                }
                if (jlbArr[i][j].getText() == "h"){ //頭
                    jlbArr[i][j].setIcon(headImg);
                }
                if (jlbArr[i][j].getText() == "b"){ //身體
                    jlbArr[i][j].setIcon(bodyImg);
                }
                if (jlbArr[i][j].getText() == "f"){ //食物
                    jlbArr[i][j].setIcon(foodImg);
                }

            }
        }
    }

    void setFood(){ //設置食物位置在沒有圖樣的格子中
        while (true){
            int randomR = rand.nextInt(15);
            int randomC = rand.nextInt(15);
            if (jlbArr[randomR][randomC].getText() != "b" && jlbArr[randomR][randomC].getText() != "h"){
                jlbArr[randomR][randomC].setText("f");
                break;
            }
        }

    }

    void isFood(){ //當頭的座標位置文字為f的時候，啟用setFood()設新的食物
        if (jlbArr[headR][headC].getText() == "f"){
            int tmpR = objectArr.get(objectArr.size()-1).preR; //前一個身體的Row
            int tmpC = objectArr.get(objectArr.size()-1).preC; //前一個身體的Column
            objectArr.add(new ObjectImg(tmpR,tmpC,"b"));
            setFood();
        }
    }

    void directMove(){ //頭的移動
        t = new Timer(150, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                jlbArr[headR][headC].setText(null);//將頭移動前位置文字清除
                update(); //在改變座標值前得到改變前的座標值(存取上一個位置)

                switch (dir) {

                    case 1: //上
                        if (headR-1 >= 0) {
                            headR--;
                            if (jlbArr[headR][headC].getText() == "b"){ //咬到自己
                                System.exit(EXIT_ON_CLOSE);
                            }
                        }else{
                            headR = size-1;
                            if (jlbArr[headR][headC].getText() == "b"){
                                System.exit(EXIT_ON_CLOSE);
                            }
                        }
                        break;
                    case 2: //下

                        if (headR+1 < size) {
                            headR++;
                            if (jlbArr[headR][headC].getText() == "b"){
                                System.exit(EXIT_ON_CLOSE);
                            }
                        }else{
                            headR = 0;
                            if (jlbArr[headR][headC].getText() == "b"){
                                System.exit(EXIT_ON_CLOSE);
                            }
                        }

                        break;
                    case 3: //左

                        if (headC-1 >= 0) {
                            headC--;
                            if (jlbArr[headR][headC].getText() == "b"){
                                System.exit(EXIT_ON_CLOSE);
                            }
                        }else{
                            headC = size-1;
                            if (jlbArr[headR][headC].getText() == "b"){
                                System.exit(EXIT_ON_CLOSE);
                            }
                        }

                        break;
                    case 4: //右

                        if (headC+1 < size) {
                            headC++;
                            if (jlbArr[headR][headC].getText() == "b"){
                                System.exit(EXIT_ON_CLOSE);
                            }
                        }else{
                            headC = 0;
                            if (jlbArr[headR][headC].getText() == "b"){
                                System.exit(EXIT_ON_CLOSE);
                            }
                        }

                        break;
                }
                isFood();
                jlbArr[headR][headC].setText("h");
                setImg();
            }

        });
        t.start();
    }

    void update(){  //在改變座標值前得到改變前的座標值(存取上一個位置)
        objectArr.get(0).preR = headR;
        objectArr.get(0).preC = headC;
//        System.out.println("頭的上一個Row:" + objectArr.get(0).preR);
//        System.out.println("頭的上一個Column:" + objectArr.get(0).preC);
//        System.out.println();
        jlbArr[objectArr.get(objectArr.size()-1).preR][objectArr.get(objectArr.size()-1).preC].setText(null);//清除尾端文字
        for (int i=objectArr.size()-1; i>0; i--){ //從尾端開始向前更新每個身體的上一個位置以及設好文字
            objectArr.get(i).preR = objectArr.get(i-1).preR;
            objectArr.get(i).preC = objectArr.get(i-1).preC;
            jlbArr[objectArr.get(i).preR][objectArr.get(i).preC].setText("b");
        }
    }

    void adjustImg(ImageIcon img){ //調整ImageIcon大小
        Image tmp1 = img.getImage();
        Image tmp2 = tmp1.getScaledInstance(25,25,Image.SCALE_SMOOTH);
        img.setImage(tmp2);
    }

    class ObjectImg{  //蛇的物件內部類別
        private int preR, preC; //存取移動前的位置

        public ObjectImg(int r, int c, String w){
            preR = r;
            preC = c;
            jlbArr[r][c].setText(w); //設置文字
        }

    }

}



