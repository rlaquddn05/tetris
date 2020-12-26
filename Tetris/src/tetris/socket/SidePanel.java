package tetris.socket;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * 게임 진행시간, 다음 피스정보, 점수, 제거한 줄수를 출력하기 위한 패널
 * @author 김병우
 *
 */
public class SidePanel extends JPanel implements Runnable {
	
	private static final long serialVersionUID = 2398616544912257455L;
	
	/**
	 * {@link Controller}객체의 함수들을 호출하기 위한 {@link Controller}형 필드
	 */
	private Controller controller;
	
	/**
	 * 게임 진행시간을 출력할 {@link JPanel}, {@link JLabel}형 time을 add()한다
	 */
	private JPanel timePanel = new JPanel();
	
	/**
	 * nextPiecePanel1, nextPiecePanel2를 add()할 {@link JPanel}
	 */
	private JPanel nextPiecePanels = new JPanel();
	
	/**
	 * 다음 피스 정보를 출력할 {@link JPanel}
	 */
	private JPanel nextPiecePanel1 = new JPanel();
	
	/**
	 * 다다음 피스 정보를 출력할 {@link JPanel}
	 */
	private JPanel nextPiecePanel2 = new JPanel();
	
	/**
	 * score와 lines값을 출력하기 위한 {@link JPanel}, {@link JLabel}형 score와 line을 add() 한다
	 */
	private JPanel scoreAndLines = new JPanel();
	
	/**
	 * 게임 진행시간을 출력하기 위한 {@link JLabel}
	 */
	private JLabel time = new JLabel();
	/**
	 * 현재 점수를 출력하기 위한 {@link JLabel}
	 */
	private JLabel score = new JLabel();
	/**
	 * 현재까지 제거한 줄 수를 출력하기 위한 {@link JLabel}
	 */
	private JLabel lines = new JLabel();

	/**
	 * scoreAndLines 및 nextPiecePanels를 update
	 */
	public void setSidePanel() {
		score.setText(Integer.toString(controller.getScore()));
		lines.setText(Integer.toString(controller.getLines()));
		paintNextPiece();
	}
	
	/**
	 * {@link JLabel}형 객체의 폰트 및 정렬을 세팅한다.
	 * @param label 세팅할 {@link JLabel}객체
	 */
	public void setLabelSettings(JLabel label) {
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(new Font("SansSerif", Font.BOLD, 20));
	}
	
	/**
	 * nextPiecePanel1과 nextPiecePanel2를 update.<br>
	 * 
	 * nextPiecePanel1과 nextPiecePanel2에 대하여 각각 초기화 및 queue에서<br>호출한 다음피스의 모양에 해당하는 jpg파일을 {@link JLabel}에 대입한다.
	 */
	public void paintNextPiece() {
		nextPiecePanel1.removeAll();
		ImageIcon imageicon = new ImageIcon("nextpieces/"+controller.getQueue(0) + ".jpg");
		JLabel nextPiece1 = new JLabel(imageicon);
		nextPiecePanel1.add(nextPiece1);
		nextPiecePanel1.revalidate();
		
		nextPiecePanel2.removeAll();
		ImageIcon imageicon2 = new ImageIcon("nextpieces/"+controller.getQueue(1) + ".jpg");
		JLabel nextPiece2 = new JLabel(imageicon2);
		nextPiecePanel2.add(nextPiece2);
		nextPiecePanel2.revalidate();
		
	}
	
	/**
	 * {@link SidePanel}객체 생성자<br>
	 * SidePanel의 생성 및 초기화, 하위 패널들의 생성, 초기화 및 add()
	 * 
	 * @param controller 다른 클래스의 객체들을 호출하기 위한 {@link Controller}형 레퍼런스
	 */
	public SidePanel(Controller controller) {
		this.controller = controller;
		this.setLayout(new BorderLayout(0,10));
		setBorder(new EmptyBorder(26, 0, 26, 10));

		timePanel.setLayout(new GridLayout(2, 1));
		timePanel.setBorder(new LineBorder(Color.BLACK));
		nextPiecePanels.setLayout(new BorderLayout());
		nextPiecePanels.setBorder(new LineBorder(Color.BLACK));
		scoreAndLines.setLayout(new GridLayout(4, 1));
		scoreAndLines.setBorder(new LineBorder(Color.BLACK));

		this.add(timePanel, BorderLayout.NORTH);
		this.add(nextPiecePanels, BorderLayout.CENTER);
		this.add(scoreAndLines, BorderLayout.SOUTH);
		
		timePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		timePanel.add(new Label("TIME"));
		timePanel.add(time);

		nextPiecePanels.add(new Label("NEXT PIECE"), BorderLayout.NORTH);
		nextPiecePanels.setBorder(new EmptyBorder(10, 10, 10, 10));
		nextPiecePanels.add(nextPiecePanel1, BorderLayout.CENTER);
		nextPiecePanels.add(nextPiecePanel2, BorderLayout.SOUTH);
		
		scoreAndLines.setBorder(new EmptyBorder(10, 10, 10, 10));
		scoreAndLines.add(new JLabel("SCORE"));
		scoreAndLines.add(score);
		scoreAndLines.add(new JLabel("LINES"));
		scoreAndLines.add(lines);

		setLabelSettings(time);
		setLabelSettings(score);
		setLabelSettings(lines);

		setPreferredSize(new Dimension(156, 0));
	}
	
	/**
	 * 게임 진행시간을 출력하는 메소드<br>
	 * 매 초마다 {@link JLabel}형 time에 진행시간을 setText()한다. 
	 */
	@Override
	public void run() {
		while (controller.isPlaying()) {
			try {

				time.setText(Integer.toString(controller.getTimePassed()));
				Thread.sleep(1000);
				controller.setTimePassed(controller.getTimePassed() + 1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
