import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.util.Random;

public class pacman {
	final JFrame frame = new JFrame();
	final ImageIcon successIcon = new ImageIcon("successIcon2.png");
	final ImageIcon failIcon = new ImageIcon("failIcon.png");
	final ImageIcon smallDot = new ImageIcon("smallDot2.png");
	final ImageIcon bigDot = new ImageIcon("bigDot2.png");
	final ImageIcon wall = new ImageIcon("wall22.png");
	final ImageIcon enemy = new ImageIcon("enemy3.png");
	final ImageIcon pacman = new ImageIcon("pacman2.png");
	final ImageIcon empty = new ImageIcon("empty2.png");
	final JButton button = new JButton(successIcon);
	final JButton button2 = new JButton(failIcon);
	final CardLayout card = new CardLayout();
	final JDialog dialog = new JDialog();
	final JLabel[][] f = new JLabel[14][14];
	int[][] fnum = new int[14][14];
	final JLabel jl = new JLabel();	//밑에 패널
	private static Random random;
	private static int pacmanH, pacmanW, enemyH, enemyW, numOfDot, where, start, cnt;
	private static Icon temp1, temp2, temp3, temp4, temp;
	private static final int FRAME_WIDTH = 690;
	private static final int FRAME_HEIGHT = 770;
	
	public pacman() {
		Color col = new Color(255, 150, 203);	//라벨 백그라운드 컬러 지정 위해서
		random = new Random();
		
		dialog.setSize(690,650);
		dialog.setVisible(false);
		
		jl.setOpaque(true); 	//배경색 적용을 위해서
		jl.setBackground(col);
		jl.setFont(new Font("Munro Small", Font.PLAIN, 28));

		cnt = 0; pacmanH=12;  pacmanW=7;  enemyH=5;  enemyW=7;  numOfDot=76;  start=2;  temp=empty;	//numOfDot : 남아있는 코인 개수
		
		for (int i=0; i<14; i++) {
			for(int j=0; j<14; j++) {
				f[i][j] = new JLabel();;
			}
		}

		class Blistener implements ActionListener {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		}

		class TListener implements ActionListener {   //timer클래스 ActionListener
			public void actionPerformed(ActionEvent event)
			{
				if(start<=0) where = 1+random.nextInt(4);
				else { where = 1; start--; }
					switch(where) {
						case 1:
							if(!(f[enemyH-1][enemyW].getIcon()).equals(wall)) {
								temp1=f[enemyH-1][enemyW].getIcon();
								f[enemyH-1][enemyW].setIcon(enemy);
								f[enemyH][enemyW].setIcon(temp);
								temp=temp1;
								enemyH--;
							}
							break;
						case 2:
							if(!(f[enemyH+1][enemyW].getIcon()).equals(wall)) {
								temp2=f[enemyH+1][enemyW].getIcon();
								f[enemyH+1][enemyW].setIcon(enemy);
								f[enemyH][enemyW].setIcon(temp);
								temp=temp2;
								enemyH++;
							}
							break;
						case 3:
							if(!(f[enemyH][enemyW-1].getIcon()).equals(wall)) {
								temp3=f[enemyH][enemyW-1].getIcon();
								f[enemyH][enemyW-1].setIcon(enemy);
								f[enemyH][enemyW].setIcon(temp);
								temp=temp3;
								enemyW--;
							}
							break;
						case 4:
							if(!(f[enemyH][enemyW+1].getIcon()).equals(wall)) {
								temp4=f[enemyH][enemyW+1].getIcon();
								f[enemyH][enemyW+1].setIcon(enemy);
								f[enemyH][enemyW].setIcon(temp);
								temp=temp4;
								enemyW++;
							}
							break;
					}
					if(enemyH==pacmanH && enemyW==pacmanW) {	//적이랑 팩맨 만났을 때 게임오버창띄우기 --> 적이 팩맨이 있는 위치로 와야 게임 종료
						f[enemyH][enemyW].setIcon(enemy);
						
						dialog.add(button2);
						dialog.setVisible(true);
						frame.dispose();
					}

				if(enemyH==5 && enemyW==7) { f[6][7].setIcon(wall); }
			}
		}
		
		class KListener extends KeyAdapter{  //키보드 입력에 따른  KeyListener
			public void keyPressed(KeyEvent e) {
				
				if(numOfDot<=0) {
					frame.dispose();
					dialog.add(button);
					button.setText(Integer.toString(cnt));
					dialog.setVisible(true);
				}
				
				
				int key = e.getKeyCode();
				
				switch(key) {
					case KeyEvent.VK_UP:	//위버튼으로 코인 먹었을 때 코인 사라짐
						if((f[pacmanH-1][pacmanW].getIcon()).equals(smallDot) || (f[pacmanH-1][pacmanW].getIcon()).equals(empty) || (f[pacmanH-1][pacmanW].getIcon()).equals(bigDot) || (f[pacmanH-1][pacmanW].getIcon()).equals(empty)) 
						{
							if((f[pacmanH-1][pacmanW].getIcon()).equals(smallDot)) {
								numOfDot--;
								cnt++;
								SmallEatSound();
							}
							else if((f[pacmanH-1][pacmanW].getIcon()).equals(bigDot)) {
								cnt += 10;
								BigEatSound();
								}
									
							jl.setText("Score : " + Integer.toString(cnt));	
							f[pacmanH-1][pacmanW].setIcon(pacman);
							f[pacmanH][pacmanW].setIcon(empty);
							pacmanH--;
						}
						if((f[pacmanH-1][pacmanW].getIcon()).equals(enemy)) {	//방향키 위버튼 누르다가 적 만났을 때 게임 종료
							f[enemyH][enemyW].setIcon(enemy);
							dialog.add(button2);
							dialog.setVisible(true);
							frame.dispose();
							GameOverSound();
						}
						break;
					case KeyEvent.VK_DOWN:	//아래버튼으로 코인 먹었을 때 코인 사라짐
						if((f[pacmanH+1][pacmanW].getIcon()).equals(smallDot) || (f[pacmanH+1][pacmanW].getIcon()).equals(empty) || (f[pacmanH+1][pacmanW].getIcon()).equals(bigDot) || (f[pacmanH+1][pacmanW].getIcon()).equals(empty)) 
						{
							if((f[pacmanH+1][pacmanW].getIcon()).equals(smallDot)) {
								numOfDot--;
								cnt++;
								SmallEatSound();
							}
							else if((f[pacmanH+1][pacmanW].getIcon()).equals(bigDot)) {
								cnt+=10;
								BigEatSound();
							}
							jl.setText("Score : " + Integer.toString(cnt));
							f[pacmanH+1][pacmanW].setIcon(pacman);
							f[pacmanH][pacmanW].setIcon(empty);
							pacmanH++;
							
						}
						if((f[pacmanH+1][pacmanW].getIcon()).equals(enemy)){	//방향키 아래버튼 누르다가 적 만났을 때 게임 종료
							f[enemyH][enemyW].setIcon(enemy);
							dialog.add(button2);
							dialog.setVisible(true);
							frame.dispose();
							GameOverSound();
						}
						
						break;	
					case KeyEvent.VK_LEFT:	//왼쪽 버튼으로 코인 먹었을 때 코인 사라짐
						if((f[pacmanH][pacmanW-1].getIcon()).equals(smallDot) || (f[pacmanH][pacmanW-1].getIcon()).equals(empty) || (f[pacmanH][pacmanW-1].getIcon()).equals(bigDot) || (f[pacmanH][pacmanW-1].getIcon()).equals(empty))
						{
							if((f[pacmanH][pacmanW-1].getIcon()).equals(smallDot)) {
								numOfDot--;
								cnt++;
								SmallEatSound();
							}
							else if((f[pacmanH][pacmanW-1].getIcon()).equals(bigDot)) {
								cnt+=10;
								BigEatSound();
							}
								
							jl.setText("Score : " + Integer.toString(cnt));
							f[pacmanH][pacmanW-1].setIcon(pacman);
							f[pacmanH][pacmanW].setIcon(empty);
							pacmanW--;
							
						}
						if((f[pacmanH][pacmanW-1].getIcon()).equals(enemy)){	//방향키 왼쪽버튼 누르다가 적 만났을 때 게임 종료
							f[enemyH][enemyW].setIcon(enemy);
							dialog.add(button2);
							dialog.setVisible(true);
							frame.dispose();
							GameOverSound();
						}
						
						break;
					case KeyEvent.VK_RIGHT:	//오른쪽 버튼으로 코인 먹었을 때 코인 사라짐
						if((f[pacmanH][pacmanW+1].getIcon()).equals(smallDot) || (f[pacmanH][pacmanW+1].getIcon()).equals(empty) || (f[pacmanH][pacmanW+1].getIcon()).equals(bigDot) || (f[pacmanH][pacmanW+1].getIcon()).equals(empty))
						{
							if((f[pacmanH][pacmanW+1].getIcon()).equals(smallDot)) {
								numOfDot--;
								cnt++;
								SmallEatSound();
							}
							else if((f[pacmanH][pacmanW+1].getIcon()).equals(bigDot)) {
								cnt+=10;
								BigEatSound();
								
							}
							jl.setText("Score : " + Integer.toString(cnt));
							f[pacmanH][pacmanW+1].setIcon(pacman);
							f[pacmanH][pacmanW].setIcon(empty);
							pacmanW++;
						}
						if((f[pacmanH][pacmanW+1].getIcon()).equals(enemy)) {	//방향키 오른쪽버튼 누르다가 적 만났을 때 게임 종료
							f[enemyH][enemyW].setIcon(enemy);
							dialog.add(button2);
							dialog.setVisible(true);
							frame.dispose();
							GameOverSound();
						}
						break;
				}
				
				System.out.println(numOfDot);
				
			}
		}
		
		KListener listener = new KListener();
		TListener tListener = new TListener();

		button.addActionListener(new Blistener());
		button2.addActionListener(new Blistener());

		Timer t = new Timer(300, tListener);	//enemy 움직이는 속도
		t.start();

		JPanel panel = new JPanel();
		JPanel panel2 = new JPanel();
		
		panel.setLayout(new GridLayout(15,15));
		panel2.setLayout(new GridLayout(1, 15));
		
		frame.requestFocus();
		frame.addKeyListener(new KListener());
		
		fnum= new int[][] 
			  {{0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			   {0,1,0,2,2,2,2,2,2,2,1,2,2,0},
			   {0,2,0,2,0,0,0,2,0,2,0,0,2,0},
			   {0,2,0,2,2,2,0,2,0,0,2,2,2,0},
			   {0,2,0,2,0,2,2,2,2,2,2,2,2,0},
			   {0,2,2,2,0,0,0,4,0,0,0,2,2,0},
			   {0,0,2,0,0,3,3,0,3,3,0,1,0,0},
			   {0,2,2,2,0,3,3,3,3,3,0,2,2,0},
			   {0,2,0,0,0,0,0,0,0,0,0,0,2,0},
			   {0,2,2,2,2,2,2,0,2,2,0,2,2,0},
			   {0,0,0,0,0,2,2,2,2,2,0,0,2,0},
			   {0,1,0,2,0,2,0,2,0,2,0,2,2,0},
			   {0,2,2,1,2,2,0,5,0,2,2,2,0,0},
			   {0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
			   
		for(int i=0; i<14; i++) {	//배열크기만큼 wall 등등(big=1 small=2 wall=else empty= 3 enemy = 4 pacman=5)... 생성
			for(int j=0; j<14; j++) {
				if(fnum[i][j] == 1) {
					f[i][j].setIcon(bigDot);
				}
				else if(fnum[i][j] == 2) {
					f[i][j].setIcon(smallDot);
				}
				else if(fnum[i][j] == 3) {
					f[i][j].setIcon(empty);
				}
				else if(fnum[i][j] == 4) {
					f[i][j].setIcon(enemy);
				}
				else if(fnum[i][j] == 5) {
					f[i][j].setIcon(pacman);
				}
				else {
					f[i][j].setIcon(wall);
				}
				f[i][j].addKeyListener(listener);
				panel.add(f[i][j]);
			}
		}
		
		panel2.add(jl);
		//프레임안에서 패널을 2개를 적절하게 배치하기 위해 사용.
		frame.add(panel, BorderLayout.NORTH);
		frame.add(panel2, BorderLayout.SOUTH);
		frame.setTitle("PACMAN");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
	}
	public void BigEatSound() {
		File file = new File("C:\\Users\\Kawaiii\\eclipse-workspace\\Pacman\\pacman_eatbigDot.wav");
		try {
			AudioInputStream stream = AudioSystem.getAudioInputStream(file);
			Clip clip = AudioSystem.getClip();
			clip.open(stream);
			clip.start();

		} catch(Exception exc) {
			exc.printStackTrace();
		}
	}
	
	public void SmallEatSound() {
		File file = new File("C:\\Users\\Kawaiii\\eclipse-workspace\\Pacman\\pacman_eatsmallDot.wav");
		try {
			AudioInputStream stream = AudioSystem.getAudioInputStream(file);
			Clip clip = AudioSystem.getClip();
			clip.open(stream);
			clip.start();

		} catch(Exception exc) {
			exc.printStackTrace();
		}
	}
	
	public void GameOverSound() {
		File file = new File("C:\\Users\\Kawaiii\\eclipse-workspace\\Pacman\\pacman_gameover.wav");
		try {
			AudioInputStream stream = AudioSystem.getAudioInputStream(file);
			Clip clip = AudioSystem.getClip();
			clip.open(stream);
			clip.start();
		} catch(Exception exc) {
			exc.printStackTrace();
		}
	}

}

		