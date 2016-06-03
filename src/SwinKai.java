import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// 繼承JFrame類別, 實作ActionListener介面
public class SwinKai extends JFrame implements ActionListener {
	private int[] leader = new int[8]; // 領先選手
	private int[] turn = new int[8]; // 選手往返
	private int[] ranks = new int[8]; // 排名
	private int[] move = new int[8]; // 移位
	private int race = 0; // 比賽狀態: race=0 =>預備; race=1 =>去程; race=2 =>回程; race=3 =>到達終點
	private int state = 0; // state=1 => 暫停 ; state=2 => 步進
	private int max = 0, QQ = 0, PP = 1, RR = 0, SS = 0;
	private int width = 450, i = 0, j = 0, k = 0, m = 0;
	private int acc = 15; // 加速度
	private int acc2 = 2; // 腎上腺素
	private String[] laneN = { "A", "B", "C", "D", "E", "F", "G", "H" }; // 選手名字
	private Timer timer;

	JPanel ctr = new JPanel();
	JButton startButton;
	JButton pauseButton;
	JButton readyButton;
	JButton stepButton;

	public SwinKai() { // 無值的建構子 >預設標題
		super("Swin-Racing");
	}

	public SwinKai(String title) { // 有值的建構子 >可自定標題
		super(title);
	}

	{// 上面是建構子依有否傳入值差異執行的程式碼，此區塊為相同執行的程式碼，會接續上面兩種建構子
		readyButton = new JButton("預備"); // 預備按鈕
		ctr.add(readyButton);
		startButton = new JButton("開始"); // 開始按鈕
		ctr.add(startButton);
		pauseButton = new JButton("暫停"); // 暫停按鈕
		ctr.add(pauseButton);
		stepButton = new JButton("步進"); // 步進按鈕
		ctr.add(stepButton);

		readyButton.addActionListener(this);
		startButton.addActionListener(this);
		pauseButton.addActionListener(this);
		stepButton.addActionListener(this);

		SwinPool pool = new SwinPool();

		timer = new Timer(30, this); // default 30
		timer.setInitialDelay(0);

		Container con = getContentPane(); // 新增容器
		con.setBackground(Color.BLUE);
		con.add(ctr, BorderLayout.SOUTH); // 排版
		con.add(pool, BorderLayout.CENTER);
	}

	// 繪圖方法
	public class SwinPool extends JPanel {
		public void paint(Graphics g) {
			int i, l;
			Insets ins = getInsets(); // 取得邊線尺寸
			g.setColor(Color.white); // 填入背景色彩
			g.fillRect(ins.left, ins.top, getWidth(), getHeight());
			ctr.setBackground(Color.BLUE);

			g.setColor(Color.BLACK);
			g.drawLine(30, 0, 30, 350); // 起點
			g.drawLine(407, 0, 407, 350); // 終點
			for (i = 0, l = 0; i <= 8; i++, l += 40)  // 設定跑道
				g.drawLine(0, l, 407, l);

			// race = 0: 預備狀態，設定選手初始位置
			for (i = 0; i < 8; i++)
				if (race == 0 || leader[i] != 0) {
					k = 0;
					for (i = 0; i < 8; i++) {
						g.setColor(Color.BLUE);
						g.drawString(laneN[i], 20 + move[i] + SS, 20 + k); // 設定選手位置
						k += 40;
					}
				}

			for (i = 0; i < 8; i++) {
				if (leader[i] > max) { // 判斷領先
					max = leader[i];
				}
			}

			// race = 1(去程): 畫出領先選手的領先線
			for (i = 0; i < 8; i++) {
				if (max == leader[i] && move[i] != 0 && race == 1) {
					g.setColor(Color.RED);
					g.drawLine(27 + move[i], 0, 27 + move[i], 320);
					for (k = 0; k < i; k++)
						j += 40;
					String aa = "Leader  " + laneN[i];
					g.drawString(aa, 30 + move[i], 20 + j);

				}

				// race = 2(回程): 畫出領先選手的領先線
				else if (max == leader[i] && move[i] != 0 && race == 2) {
					g.setColor(Color.RED);
					g.drawLine(20 + move[i], 0, 20 + move[i], 320);
					for (k = 0; k < i; k++)
						j += 40;
					String aa = "Leader  " + laneN[i];
					g.drawString(aa, move[i] - 40, 20 + j);
				}
				j = 0;

				// race = 3: 到達終點判斷排名
				if (move[i] == 0 && race == 3) {
				// ---------------------------------------------
					RR = 0;
					if (PP <= 8 && ranks[i] == 0) { // 設定選手"A~H"的排名寫入ranks[i]陣列
						QQ = 1;
						for (k = 0; k < 8; k++) {
							if (move[k] == 0)
								RR++;
						}
						if (QQ == 1) {
							ranks[i] = PP;
							m++;
							if (m == RR) {
								PP = 1;
								PP += RR;
								RR = 0;
							}
							QQ = 0;
						}
					}
				// ---------------------------------------------
					for (k = 0; k < i; k++)
						j += 40;
					if (ranks[i] == 1) {
						g.setColor(Color.RED);
						String aaa = "Winer is " + laneN[i];
						g.drawString(aaa, 50, 20 + j);
					}
					g.setColor(Color.RED);
					String a2 = Integer.toString(ranks[i]);
					g.drawString(a2, 8, 20 + j);
				}
			}
		}
	}

	// 實作事件處理
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startButton && race == 0 || state == 1 || state == 2) {
			if (state == 0)
				System.out.println("Start");
			timer.start();
			race = 1;
			state = 0;
			for (i = 0; i < 8; i++){
				if(turn[i] != -1)
					turn[i] = 1;
				else{
					turn[i] = -1;
					race = 2;
					}
			}
		}
		
		if (e.getSource() == readyButton) {
			for (i = 0; i < 8; i++) {
				move[i] = 0;
				turn[i] = 1;
				leader[i] = 0;
				ranks[i] = 0;
			}
			race = 0;
			max = 0;
			PP = 1;
			m = 0;
			SS = 0;
			timer.stop();
			System.out.println("Ready");
		}

		if (e.getSource() == stepButton) {
			timer.stop();
			state = 1;
		}
		if (e.getSource() == pauseButton) {
			timer.stop();
			state = 2;
			System.out.println("pause");
		} else
			repaint();

		if (race != 0)
			for (i = 0; i < 8; i++) {
				move[i] += (int) (Math.random() * acc * turn[i]);
				if (leader[i] < max) {
					if (turn[i] == 1)
						move[i] += acc2;
					else {
						move[i] -= acc2;
					}
				}
			}

		for (i = 0; i < 8; i++) {
			if (turn[i] == 1) {
				leader[i] = move[i]; // 去程時的leader[i]大小，用來判斷領先
			}
			if (turn[i] == -1) {
				leader[i] = 1000 - move[i]; // 回程時的leader[i]大小，用來判斷領先
			}
			if (move[i] > 375) { // 到達折返線，即折返
				turn[i] = -1;
				race = 2;
			}
			if (move[i] < 0) { // 到達終點，初設選手位置
				move[i] = 0;
				SS = 11;
				race = 3;
			}
		}
	}

	// 主程式
	public static void main(String[] args) { // 建立Swing應用程式
	// SwinKai swin = new SwinKai();
		SwinKai swin = new SwinKai("Swin-Racing"); // 可自訂標題

		swin.setDefaultCloseOperation(EXIT_ON_CLOSE);
		swin.setSize(480, 387); // 設定視窗尺寸
		swin.setResizable(false);// 設定視窗顯示是否可縮放
		swin.setVisible(true); // 顯示視窗
	 // swin.setLocation(400, 200);// 設定視窗顯示在螢幕的位置
	}
}