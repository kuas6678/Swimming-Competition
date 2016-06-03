import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// �~��JFrame���O, ��@ActionListener����
public class SwinKai extends JFrame implements ActionListener {
	private int[] leader = new int[8]; // ������
	private int[] turn = new int[8]; // ��⩹��
	private int[] ranks = new int[8]; // �ƦW
	private int[] move = new int[8]; // ����
	private int race = 0; // ���ɪ��A: race=0 =>�w��; race=1 =>�h�{; race=2 =>�^�{; race=3 =>��F���I
	private int state = 0; // state=1 => �Ȱ� ; state=2 => �B�i
	private int max = 0, QQ = 0, PP = 1, RR = 0, SS = 0;
	private int width = 450, i = 0, j = 0, k = 0, m = 0;
	private int acc = 15; // �[�t��
	private int acc2 = 2; // �ǤW����
	private String[] laneN = { "A", "B", "C", "D", "E", "F", "G", "H" }; // ���W�r
	private Timer timer;

	JPanel ctr = new JPanel();
	JButton startButton;
	JButton pauseButton;
	JButton readyButton;
	JButton stepButton;

	public SwinKai() { // �L�Ȫ��غc�l >�w�]���D
		super("Swin-Racing");
	}

	public SwinKai(String title) { // ���Ȫ��غc�l >�i�۩w���D
		super(title);
	}

	{// �W���O�غc�l�̦��_�ǤJ�Ȯt�����檺�{���X�A���϶����ۦP���檺�{���X�A�|����W����ثغc�l
		readyButton = new JButton("�w��"); // �w�ƫ��s
		ctr.add(readyButton);
		startButton = new JButton("�}�l"); // �}�l���s
		ctr.add(startButton);
		pauseButton = new JButton("�Ȱ�"); // �Ȱ����s
		ctr.add(pauseButton);
		stepButton = new JButton("�B�i"); // �B�i���s
		ctr.add(stepButton);

		readyButton.addActionListener(this);
		startButton.addActionListener(this);
		pauseButton.addActionListener(this);
		stepButton.addActionListener(this);

		SwinPool pool = new SwinPool();

		timer = new Timer(30, this); // default 30
		timer.setInitialDelay(0);

		Container con = getContentPane(); // �s�W�e��
		con.setBackground(Color.BLUE);
		con.add(ctr, BorderLayout.SOUTH); // �ƪ�
		con.add(pool, BorderLayout.CENTER);
	}

	// ø�Ϥ�k
	public class SwinPool extends JPanel {
		public void paint(Graphics g) {
			int i, l;
			Insets ins = getInsets(); // ���o��u�ؤo
			g.setColor(Color.white); // ��J�I����m
			g.fillRect(ins.left, ins.top, getWidth(), getHeight());
			ctr.setBackground(Color.BLUE);

			g.setColor(Color.BLACK);
			g.drawLine(30, 0, 30, 350); // �_�I
			g.drawLine(407, 0, 407, 350); // ���I
			for (i = 0, l = 0; i <= 8; i++, l += 40)  // �]�w�]�D
				g.drawLine(0, l, 407, l);

			// race = 0: �w�ƪ��A�A�]�w����l��m
			for (i = 0; i < 8; i++)
				if (race == 0 || leader[i] != 0) {
					k = 0;
					for (i = 0; i < 8; i++) {
						g.setColor(Color.BLUE);
						g.drawString(laneN[i], 20 + move[i] + SS, 20 + k); // �]�w����m
						k += 40;
					}
				}

			for (i = 0; i < 8; i++) {
				if (leader[i] > max) { // �P�_���
					max = leader[i];
				}
			}

			// race = 1(�h�{): �e�X�����⪺����u
			for (i = 0; i < 8; i++) {
				if (max == leader[i] && move[i] != 0 && race == 1) {
					g.setColor(Color.RED);
					g.drawLine(27 + move[i], 0, 27 + move[i], 320);
					for (k = 0; k < i; k++)
						j += 40;
					String aa = "Leader  " + laneN[i];
					g.drawString(aa, 30 + move[i], 20 + j);

				}

				// race = 2(�^�{): �e�X�����⪺����u
				else if (max == leader[i] && move[i] != 0 && race == 2) {
					g.setColor(Color.RED);
					g.drawLine(20 + move[i], 0, 20 + move[i], 320);
					for (k = 0; k < i; k++)
						j += 40;
					String aa = "Leader  " + laneN[i];
					g.drawString(aa, move[i] - 40, 20 + j);
				}
				j = 0;

				// race = 3: ��F���I�P�_�ƦW
				if (move[i] == 0 && race == 3) {
				// ---------------------------------------------
					RR = 0;
					if (PP <= 8 && ranks[i] == 0) { // �]�w���"A~H"���ƦW�g�Jranks[i]�}�C
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

	// ��@�ƥ�B�z
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
				leader[i] = move[i]; // �h�{�ɪ�leader[i]�j�p�A�ΨӧP�_���
			}
			if (turn[i] == -1) {
				leader[i] = 1000 - move[i]; // �^�{�ɪ�leader[i]�j�p�A�ΨӧP�_���
			}
			if (move[i] > 375) { // ��F���u�A�Y���
				turn[i] = -1;
				race = 2;
			}
			if (move[i] < 0) { // ��F���I�A��]����m
				move[i] = 0;
				SS = 11;
				race = 3;
			}
		}
	}

	// �D�{��
	public static void main(String[] args) { // �إ�Swing���ε{��
	// SwinKai swin = new SwinKai();
		SwinKai swin = new SwinKai("Swin-Racing"); // �i�ۭq���D

		swin.setDefaultCloseOperation(EXIT_ON_CLOSE);
		swin.setSize(480, 387); // �]�w�����ؤo
		swin.setResizable(false);// �]�w������ܬO�_�i�Y��
		swin.setVisible(true); // ��ܵ���
	 // swin.setLocation(400, 200);// �]�w������ܦb�ù�����m
	}
}