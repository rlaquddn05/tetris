package tetris.socket;

import java.awt.Color;
import java.awt.Point;

import javax.swing.JOptionPane;

/**
 * 	Main과 다른 객체들을 연결하기 위한 Controller객체
 * @author 김병우
 *
 */
public class Controller {
	
	/**
	 * board의 정사각형들의 한 변의 길이
	 */
	public static final int SQUARE_SIZE = 25;
	
	/**
	 * drop()가 실행되는 주기(ms)
	 */
	public static final int TIME_GAP = 300;
	
	/**
	 * 각 줄 제거시 얻는 점수의 양
	 * 1줄 : 100점
	 * 2줄 : 300점
	 * 3줄 : 600점
	 * 4줄 : 1000점
	 */
	public static final int[] SCORE_PER_LINES_CLEARED = { 100, 300, 500, 1000 };
	
	/**
	 * 1피스당  얻는 점수
	 */
	public static final int SCORE_PER_PIECE = 10;
	
	/**
	 * 하드드롭 수행에 따른 점수 보너스 
	 */
	public static final int HARD_DROP_BONUS = 5;
	
	/**
	 * piece queue의 크기
	 */
	public static final int PIECE_QUEUE_SIZE = 2;
	
	/**
	 * board의 가로 칸 수 
	 */
	public static final int BOARD_WIDTH = 10;
	
	/**
	 * board의 세로 칸 수 
	 */
	public static final int BOARD_HEIGHT = 21;
	
	/**
	 *  tetramino piece의 정보, tetramino[shape][rotation][point]로 구성된다
	 *  shape : tetramino piece의 종류, (I, J, L, O, S, T, Z)형으로 나뉜다.
	 *  rotation : tetramino piece의 회전상태, (0, 90, 180, 270)도의 회전상태 4가지로 분류한다.
	 *  point : tetramino piece의 회전상태에 따른 사각형들의 좌표, 사각형 좌측상단의 좌표를 기준으로 한다.
	 */
	public static final Point[][][] TETRAMINOS = {
			{ { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
					{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) } },
			{ { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
					{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0) } },
			{ { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) },
					{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0) } },
			{ { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
					{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
					{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
					{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) } },
			{ { new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
					{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
					{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
					{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) } },
			{ { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
					{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
					{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
					{ new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) } },
			{ { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
					{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) },
					{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
					{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) } } };
	
	/**
	 * tetramino의 color 
	 */
	public static final Color[] TETRAMINO_COLORS = { new Color(0, 216, 255), new Color(1, 0, 255),
			new Color(255, 187, 0), new Color(255, 228, 0), new Color(29, 219, 22), new Color(95, 0, 255),
			new Color(255, 0, 0) };
	
	/**
	 * main프레임의 오른쪽 패널 
	 */
	private SidePanel sidePanel;
	
	/**
	 * main프레임의 중앙 패널
	 */
	private TetrisPanel tetrisPanel;
	
	/**
	 * main프레임의 상단 패널
	 */
	private UserInfoPanel userInfoPanel;
	
	/**
	 * TetrisServer와 통신할 Socket객체
	 */
	private TetrisUser tetrisUser;
	
	/**
	 * 대전상대의 board상황을 출력할 프레임
	 */
	private OpponentBoard opponentBoard;
	
	/**
	 * 테트리스 게임이 이루어지는 보드의 상황을 저장하는 객체 
	 */
	public Color[][] board;
	
	/**
	 * 현재 piece의 종류
	 */
	public int shape;
	
	/**
	 * 현재 piece의 회전상태
	 */
	public int rotation;
	
	/**
	 * 현재 piece의 위치
	 */
	private Point pieceLocation;
	
	/**
	 * 현재 게임의 득점상황
	 */
	private int score = 0;
	
	/**
	 * 다음 2개 piece의 종류 정보
	 */
	private int[] queue = new int[2];
	
	/**
	 * UserInfoPanel에 출력될 String형 정보, 추후 업데이트 예정
	 */
	private String userInfo = "";
	
	/**
	 * 현재 게임에서 제거한 줄의 수
	 */
	private int lines = 0;
	
	/**
	 * 게임의 진행중 여부
	 */
	private boolean playing = false;
	
	/**
	 * 게임 진행 시간
	 */
	private int timePassed = 0;
	
	/**
	 * timePassed객체를 반환.
	 * @return timePassed
	 */
	public int getTimePassed() {
		return timePassed;
	}
	
	/**
	 * timePassed객체에 주어진 값을 대입.
	 * @param timePassed
	 * 		  timePassed에 대입할 int형 객체.
	 */
	public void setTimePassed(int timePassed) {
		this.timePassed = timePassed;
	}
	
	/**
	 * queue의 index = i인 값을 반환.
	 * @param i
	 * 		    반환될 queue의 index값.
	 * @return queue[i]
	 * 		      반환될 index = i 인 queue의값.
	 */
	public int getQueue(int i) {
		return queue[i];
	}

	/**
	 * score값을 반환
	 * @return score
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * score에 주어진 값을 더함.
	 * @param score
	 * 		    현재 score값에 더해질 int형 객체.
	 */
	public void addScore(int score) {
		this.score += score;
	}
	
	/**
	 * playing 값을 반환
	 * @return playing
	 */
	public boolean isPlaying() {
		return playing;
	}
	
	/**
	 * playing의 상태를 저장.
	 * @param playing
	 * 		    게임중 여부를 나타내는 {@link Boolean}형 객체.	
	 */
	public void setPlaying(boolean playing) {
		this.playing = playing;
	}
	
	/**
	 * lines값을 반환하는 메소드.
	 * @return lines
	 */
	public int getLines() {
		return lines;
	}
	
	/**
	 * lines에 주어진 값을 더함.
	 * @param lines
	 * 		    현재 lines 값에 더해질 {@link Integer}형 객체.
	 */
	public void addLines(int lines) {
		this.lines += lines;
	}
	
	/**
	 * sidePanel에 저장된 {@link SidePanel}객체 반환.
	 * @return sidePanel
	 */
	public SidePanel getSidePanel() {
		return sidePanel;
	}
	
	/**
	 * sidePanel에 {@link SidePanel}형 객체를 저장.
	 * @param sidePanel
	 * 		  sidePanel에 저장할 {@link SidePanel}형 객체.
	 */
	public void setSidePanel(SidePanel sidePanel) {
		this.sidePanel = sidePanel;
	}
	
	/**
	 * tetrisPanel에 저장된 {@link TetrisPanel}객체 반환.
	 * @return tetrisPanel 		 
	 */
	public TetrisPanel getTetrisPanel() {
		return tetrisPanel;
	}
	
	/**
	 * tetrisPanel에 {@link TetrisPanel}형 객체를 저장.
	 * @param tetrisPanel
	 * 		  tetrisPanel에 저장할 {@link TetrisPanel}형 객체.
	 */		  
	public void setTetrisPanel(TetrisPanel tetrisPanel) {
		this.tetrisPanel = tetrisPanel;
	}
	
	/**
	 * userInfoPanel에 저장된 {@link UserInfoPanel}객체 반환.
	 * @return userInfoPanel
	 */
	public UserInfoPanel getUserInfoPanel() {
		return userInfoPanel;
	}
	
	/**
	 * userInfoPanel에 {@link UserInfoPanel}형 객체를 저장.
	 * @param userInfoPanel
	 * 		  userInfoPanel에 저장할 {@link UserInfoPanel}형 객체.
	 */
	public void setUserInfoPanel(UserInfoPanel userInfoPanel) {
		this.userInfoPanel = userInfoPanel;
	}
	
	/**
	 * tetrisUser에 저장된 {@link TetrisUser}객체 반환.
	 * @return tetrisUser
	 */
	public TetrisUser getTetrisuser() {
		return tetrisUser;
	}
	
	/**
	 * tetrisUser에 {@link TetrisUser}형 객체를 저장.
	 * @param tetrisuser
	 * 		  tetrisUser에 저장할 {@link TetrisUser}형 객체.
	 */
	public void setTetrisuser(TetrisUser tetrisuser) {
		this.tetrisUser = tetrisuser;
	}
	
	/**
	 * userInfo에 주어진 {@link String}값을 저장.
	 */
	private void setUserInfo() {
		userInfo = " ID : AAA, WIN/LOSE : 123/45 (임시) ";
	}
	
	/**
	 * userInfo에 저장된 {@link String}형 값을 반환.
	 * @return userInfo
	 */
	public String getUserInfo() {
		setUserInfo();
		return userInfo;
	}	
	
	/**
	 * opponentBoard에 저장된{@link OpponentBoard}형 객체를 반환.
	 * @return opponentBoard
	 */
	public OpponentBoard getOpponentBoard() {
		return opponentBoard;
	}
	
	/**
	 * opponentBoard에 {@link OpponentBoard}형 객체를 저장.
	 * @param opponentBoard
	 * 		  opponentBoard에 저장할 {@link OpponentBoard}형 객체.
	 */
	public void setOpponentBoard(OpponentBoard opponentBoard) {
		this.opponentBoard = opponentBoard;
	}
	
	/**
	 * pieceLocation에 저장된 {@link Point}형 객체를 반환.
	 * @return pieceLocation
	 */
	public Point getPieceLocation() {
		return pieceLocation;
	}
	
	/**
	 * pieceLocation에 {@link Point}형 객체를 저장.
	 * @param pieceLocation
	 * 		  pieceLocation에 저장할 {@link Point}형 객체.
	 */
	public void setPieceLocation(Point pieceLocation) {
		this.pieceLocation = pieceLocation;
	}
	
	/**
	 * 현재 피스의 위치와 회전상태를 초기화하고, queue의 다음 피스 값(모양)을 반환하며, queue에 새로운 값을 추가.
	 * @return queue[0]에 저장된 다음피스의 값(모양)
	 */
	public int getNextPiece() {
		setPieceLocation(new Point(5, 1));
		rotation = 0;
		int nextpiece = queue[0];
		queue[0] = queue[1];
		queue[1] = (int) (Math.random() * 7);
		return nextpiece;
	}
	
	/**
	 * queue에 랜덤한 2개의 피스 값(모양)을 저장하여 초기화한다.
	 */
	private void setPieceQueue() {
		queue[0] = (int) (Math.random() * 7);
		queue[1] = (int) (Math.random() * 7);
	}
	
	/**
	 * 게임을 시작하는 메소드.
	 * 게임을 시작하기 위해 queue값 초기화, board초기화, sidePanel초기화를 수행하고 현재피스들의 낙하를 시작한다.
	 * sidePanel의 run()을 호출하여 게임을 완전히 시작하고, playing을 true로 전환한다.  
	 */
	public void initGame() {
		setPieceQueue();
		tetrisPanel.initboard();
		sidePanel.setSidePanel();
		new Thread(tetrisPanel).start();
		playing = true;
		new Thread(sidePanel).start();
	}
	
	/**
	 * {@link SidePanel}의 출력값들(score, lines, nextPiecePanel1, nextPiecePanel2)을 update한다.
	 */
	public void updateSidePanel() {
		sidePanel.setSidePanel();
	}
	
	/**
	 * 게임을 패배로 종료하는 메소드. 게임의 승패와 점수, 시간을 출력하고 playing을 false로 전환한 뒤 프로그램을 종료한다.
	 */
	public void finishGameLose() {
		playing = false;
		JOptionPane.showMessageDialog(null, "GAME OVER\nYOU LOSE");
		JOptionPane.showMessageDialog(null, "SCORE : " + score + "\nTIME : " + timePassed + "s");
		System.exit(0);
	}
	
	/**
	 * 게임을 승리로 종료하는 메소드. 게임의 승패와 점수, 시간을 출력하고 playing을 false로 전환한 뒤 프로그램을 종료한다.
	 */
	public void finishGameWin() {
		playing = false;
		JOptionPane.showMessageDialog(null, "YOU WIN!");
		JOptionPane.showMessageDialog(null, "SCORE : " + score + "\nTIME : " + timePassed + "s");
		System.exit(0);
	}	
	
	/**
	 * {@link OpponentBoard}의 필드들에 주어진 값들을 대입한다.
	 *  
	 * @param opponentBoard 상대 보드의 좌표별 Color값
	 * @param opponentShape 상대의 현재 shape값
	 * @param opponentPieceLocation 상대의 현재 PieceLocation값 
	 * @param opponentRotation 상대의 현재 Rotation값
	 */
	public void setOpponentBoardData(Color[][] opponentBoard, int opponentShape, Point opponentPieceLocation,
			int opponentRotation) {
		getOpponentBoard().setOpponentBoard(opponentBoard);
		getOpponentBoard().setOpponentShape(opponentShape);
		getOpponentBoard().setOpponentPieceLocation(opponentPieceLocation);
		getOpponentBoard().setOpponentRotation(opponentRotation);

	}
	
	/**
	 * opponentBoard객체의 부모(JFrame)의 repaint()호출하여 OpponentBoard의 변화를 출력한다.
	 */
	public void repaintOpponentBoard() {
		opponentBoard.getParent().repaint();
	}
	
}
