package Project3_SnakeVerDB;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

public class Graphic extends JFrame {
	Container contentPane = getContentPane();
	JPanel gamePane = new JPanel();
	JPanel scorePane = new JPanel();
	JPanel systemPane = new JPanel();
	JPanel rankPane = new JPanel();
	JPanel sendNamePane = new JPanel();
	JLabel snake[] = new JLabel[5000];

	int snakeLength = 0;
	boolean key[] = new boolean[41];
	int Xsixteen = 16;
	int Ysixteen = 0;

	KeyListen keyL = new KeyListen();
	Direction dir = new Direction();

	JLabel greenbox1 = new JLabel("■");
	JLabel bluebox5 = new JLabel("■");
	JLabel lb_gameover = new JLabel("Game Over");

	JLabel lb_score1 = new JLabel("현재 점수!");
	JLabel lb_score2 = new JLabel("3");

	JLabel lb_system1 = new JLabel("조작법 : 방향키");
	JLabel lb_system2 = new JLabel("초록색 ■ 1점 파란색 ■ 5점");
	JLabel lb_system3 = new JLabel("본 게임의 제작자(박형진)는");
	JLabel lb_system4 = new JLabel("불법 복제와 상업적 이용을 환영합니다!!");

	JTextField tf_name = new JTextField();
	JButton bt_send = new JButton("DB에 전송");
	JButton bt_exit = new JButton("종료");

	int score = 3;
	int cnt_sec3000 = 0;
	boolean one = true;
	
	Random random = new Random();
	int randR = random.nextInt(225);
	int randG = random.nextInt(225);
	int randB = random.nextInt(225);
	Graphic() throws Exception{
		setGraphic();
		play();
	}

	
	void setGraphic() throws Exception{
		snakeLength = 0;
		Xsixteen = 16;
		Ysixteen = 0;
		lb_score2.setText("3");
		score = 3;
		cnt_sec3000 = 0;
		one = true;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1100, 830);
		setTitle("Snake!");
		setLayout(null);
		setResizable(false);

		contentPane.setBackground(Color.black);
		contentPane.addKeyListener(keyL);

		gamePane.setSize(800, 800);
		gamePane.setPreferredSize(new Dimension(800, 800));
		gamePane.setLocation(1, 1);
		gamePane.setBackground(Color.white);
		gamePane.setLayout(null);
		contentPane.add(gamePane);

		scorePane.setSize(300, 150);
		scorePane.setBackground(new Color(253,230,240));
		scorePane.setPreferredSize(new Dimension(300, 150));
		scorePane.setLocation(801, 1);
		scorePane.setLayout(new FlowLayout());
		contentPane.add(scorePane);

		lb_score1.setFont(new Font(null, 1, 55));
		scorePane.add(lb_score1);

		lb_score2.setFont(new Font(null, 1, 55));
		scorePane.add(lb_score2);

		systemPane.setSize(300, 150);
		systemPane.setPreferredSize(new Dimension(300, 150));
		systemPane.setLocation(801, 151);
		systemPane.setLayout(new FlowLayout());
		systemPane.setBackground(new Color(151,234,244));
		contentPane.add(systemPane);

		lb_system1.setFont(new Font(null, 1, 20));
		systemPane.add(lb_system1);
		lb_system2.setFont(new Font(null, 1, 20));
		systemPane.add(lb_system2);
		lb_system3.setFont(new Font(null, 1, 15));
		systemPane.add(lb_system3);
		lb_system4.setFont(new Font(null, 1, 15));
		systemPane.add(lb_system4);

		rankPane.setSize(300, 500);
		rankPane.setPreferredSize(new Dimension(300, 500));
		rankPane.setLocation(801, 301);
		rankPane.setLayout(null);
		rankPane.setBackground(Color.MAGENTA);
		contentPane.add(rankPane);

		DB db = new DB();

		String[] colName = { "유저", "점수" }; // 컬럼이름

		String name = null;
		String point = null;
		DefaultTableModel model = null;
		String[][] rowData = new String[db.name.size()][db.point.size()];
		for (int a = 0; a < db.name.size(); a++) {
			name = "" + db.name.elementAt(a);
			point = "" + db.point.elementAt(a);
			rowData[a][0] = name;
			rowData[a][1] = point;
		}
		model = new DefaultTableModel(rowData, colName) {
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		}; // 모델생성
		// String[][] rowData =
		// {{""+db.name.elementAt(0),""+db.point.elementAt(0)}};

		// JTable의 객체를 생성할때 colName[],rowData[][]를 사용해 테이블을 구성할 수 있지만
		// 모델을 사용하면 테이블을 추가, 삭제, 수정등을 하는데 편리하다

		JTable table = new JTable(model);// 모델을 사용해 테이블 생성
		table.setFocusable(false);
		JScrollPane js = new JScrollPane(table);// 테이블을 스크롤패널에 추가
		js.setLocation(0, 0);

		js.setSize(300, 500);
		table.getTableHeader().setReorderingAllowed(false);

		rankPane.add(js);
		setVisible(true);
		contentPane.requestFocus();
	}
	
	void play(){
		for (int i = 2; i >= 0; i--) {
			snake[i] = new JLabel("■");
			snake[i].setForeground(Color.orange);
			snake[i].setFont(new Font(null, 1, 16));
			snake[i].setSize(16, 16);
			snake[i].setLocation(16 * snakeLength, 16 * 24);
			gamePane.add(snake[i]);
			snakeLength++;
		}

		greenbox1.setForeground(new Color(randR, randG, randB));
		greenbox1.setFont(new Font(null, 1, 16));
		greenbox1.setSize(16, 16);
		greenbox1.setLocation(16 * 37, 16 * 27);
		gamePane.add(greenbox1);

		F1: while (true) {
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// X, Y sixteen 비교해서 상황에따라 순서 바꾸기.
			if (Xsixteen == -16 && Ysixteen == 0) { // 왼쪽으로
				if (key[38]) {
					dir.up();
				}
				if (key[40]) {
					dir.down();
				}
			} else if (Xsixteen == 16 && Ysixteen == 0) { // 오른쪽
				if (key[38]) {
					dir.up();
				}
				if (key[40]) {
					dir.down();
				}
			} else if (Xsixteen == 0 && Ysixteen == -16) { // 위
				if (key[37]) {
					dir.left();
				}
				if (key[39]) {
					dir.right();
				}
			} else if (Xsixteen == 0 && Ysixteen == 16) { // 아래
				if (key[37]) {
					dir.left();
				}
				if (key[39]) {
					dir.right();
				}
			}
			if (snake[0].getX() == greenbox1.getX() && snake[0].getY() == greenbox1.getY()) { // 초록색 냠냠하면
				
				Random random = new Random();
				snake[snakeLength] = new JLabel("■");
				
				snake[snakeLength].setForeground(new Color(randR, randG, randB));
				snake[snakeLength].setFont(new Font(null, 1, 16));
				snake[snakeLength].setSize(16, 16);
				snake[snakeLength].setLocation(snake[snakeLength - 1].getX(), snake[snakeLength - 1].getY());
				gamePane.add(snake[snakeLength]);
				snakeLength++;
				
				randR = random.nextInt(225);
				randG = random.nextInt(225);
				randB = random.nextInt(255);
				greenbox1.setForeground(new Color(randR, randG, randB));
				int x = random.nextInt(50);
				int y = random.nextInt(50);
				greenbox1.setLocation(x * 16, y * 16);
				score++;
			}

			if (score == 5 && one) { // 5점되면 파란색 만듬!
				Random random = new Random();
				int x = random.nextInt(50);
				int y = random.nextInt(50);
				bluebox5.setLocation(x * 16, y * 16);

				bluebox5.setForeground(Color.blue);
				bluebox5.setFont(new Font(null, 1, 16));
				bluebox5.setSize(16, 16);
				gamePane.add(bluebox5);
				one = false;
			}
			cnt_sec3000 += 150;
			if (cnt_sec3000 == 3000) {
				Random random = new Random();
				int x = random.nextInt(50);
				int y = random.nextInt(50);
				bluebox5.setLocation(x * 16, y * 16);
				cnt_sec3000 = 0;
			}
			if (snake[0].getX() == bluebox5.getX() && snake[0].getY() == bluebox5.getY()) { // 파란거
																							// 냠냠하면
				Random random = new Random();
				int x = random.nextInt(50);
				int y = random.nextInt(50);
				bluebox5.setLocation(x * 16, y * 16);
				score += 5;
				cnt_sec3000 = 0;

				snake[snakeLength] = new JLabel("■");
				snake[snakeLength].setForeground(Color.BLUE);
				snake[snakeLength].setFont(new Font(null, 1, 16));
				snake[snakeLength].setSize(16, 16);
				snake[snakeLength].setLocation(snake[snakeLength - 1].getX(), snake[snakeLength - 1].getY());
				gamePane.add(snake[snakeLength]);
				snakeLength++;
			}
			lb_score2.setText(score + "");
			for (int i = snakeLength - 1; i > 0; i--) { // 대가리 따라가기
				snake[i].setLocation(snake[i - 1].getX(), snake[i - 1].getY());
			}
			snake[0].setLocation(snake[0].getX() + Xsixteen, snake[0].getY() + Ysixteen); // 대가리 움직움직
			
			if (snake[0].getX() == 16 * 50) { // 오른쪽으로 나가부럿다
				snake[0].setLocation(16 * 0, snake[0].getY()); // 뒤로 돌아와
			}
			if (snake[0].getY() == 16 * 50) { // 아래쪽으로 나가부럿다
				snake[0].setLocation(snake[0].getX(), 16 * 0);
			}
			if (snake[0].getX() == -16) { // 왼쪽으로 훙
				snake[0].setLocation(16 * 49, snake[0].getY());
			}
			if (snake[0].getY() == -16) { // 위쪽으로 휭~
				snake[0].setLocation(snake[0].getX(), 16 * 49);
			}
			
			int i = 4;
			while (i < snakeLength) {
				if (snake[0].getX() == snake[i].getX() && snake[0].getY() == snake[i].getY()) { // 나는 게임을 만든것이고 너는 꼬리를 콱! 물어부른것이여
					gamePane.setBackground(Color.DARK_GRAY);
					lb_gameover.setSize(800, 300);
					lb_gameover.setFont(new Font(null, 1, 130));
					lb_gameover.setForeground(Color.red);
					lb_gameover.setLocation(40, 100);
					gamePane.add(lb_gameover);
					break F1;
				}
				i++;
			}
		}
		// 게임끝나고 무한반복 탈출해버림.
		// 유저이름을 입력받아 DB에 보내야함.

		sendNamePane.setSize(500, 350);
		sendNamePane.setPreferredSize(new Dimension(400, 300));
		sendNamePane.setLocation(150, 350);
		sendNamePane.setLayout(null);
		sendNamePane.setBackground(Color.DARK_GRAY);

		gamePane.add(sendNamePane);

		tf_name.setSize(200, 30);
		tf_name.setLocation(150, 170);
		tf_name.setHorizontalAlignment(JTextField.CENTER);
		sendNamePane.add(tf_name);
		MouseListen mouseL = new MouseListen();
		bt_send.setSize(95, 30);
		bt_send.setLocation(150, 210);
		sendNamePane.add(bt_send);
		bt_send.addMouseListener(mouseL);

		bt_exit.setSize(95, 30);
		bt_exit.setLocation(255, 210);
		sendNamePane.add(bt_exit);
		
		bt_exit.addMouseListener(mouseL);
		repaint();

	}

	class Direction {
		void left() {
			Xsixteen = -16;
			Ysixteen = 0;
			key[37] = false;
		}

		void right() {
			Xsixteen = 16;
			Ysixteen = 0;
			key[39] = false;
		}

		void up() {
			Xsixteen = 0;
			Ysixteen = -16;
			key[38] = false;
		}

		void down() {
			Xsixteen = 0;
			Ysixteen = 16;
			key[40] = false;
		}
	}

	class KeyListen implements KeyListener {
		
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() >= 37 && e.getKeyCode() <= 40) {
				if (e.getKeyCode() == 37 && Xsixteen != 16 && Xsixteen != -16) {
					key[37] = true;
				}
				if (e.getKeyCode() == 38 && Ysixteen != 16 && Ysixteen != -16) {
					key[38] = true;
				}
				if (e.getKeyCode() == 39 && Xsixteen != -16 && Xsixteen != 16) {
					key[39] = true;
				}
				if (e.getKeyCode() == 40 && Ysixteen != -16 && Ysixteen != 16) {
					key[40] = true;
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

	}
	
	class MouseListen implements MouseListener{
		boolean firstClick = false;
		@Override
		public void mouseClicked(MouseEvent e) {
			
			if(e.getComponent() == bt_send && !firstClick){
				try {
					new DB(tf_name.getText(),Integer.parseInt(lb_score2.getText()));
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				firstClick = true;
				tf_name.setText("전송되었습니다.");
			}
			else if(e.getComponent() == bt_send && firstClick){
				tf_name.setText("이미 전송되었습니다.");
			}
			else if(e.getComponent() == bt_exit){
				System.exit(0);
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
}
