package tetris.socket;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

/**
 * board를 출력하기 위한 {@link JPanel}.<br>
 * 사용자의 입력에 따라 board가 변화하는 로직들을 포함한다.
 * @author 김병우
 *
 */
public class TetrisPanel extends JPanel implements Runnable {
	private static final long serialVersionUID = -5284243606316988273L;
	/**
	 * {@link Controller}객체의 함수들을 호출하기 위한 {@link Controller}형 필드
	 */
	private Controller controller;
	
	/**
	 *  controller에 저장된 {@link Controller}객체 반환
	 * @return controller
	 */
	public Controller getController() {
		return controller;
	}
	
	/**
	 * @param controller controller 다른 객체들을 호출하기 위한 {@link Controller}형 레퍼런스
	 */
	public TetrisPanel(Controller controller) {
		super();
		this.controller = controller;
	}
	
	/**
	 * board 초기화 및 현재 피스 설정
	 */
	public void initboard() {
		controller.board = new Color[Controller.BOARD_WIDTH+2][Controller.BOARD_HEIGHT+3];
		for (int i = 0; i < Controller.BOARD_WIDTH+1; ++i) {
			for (int j = 0; j < Controller.BOARD_HEIGHT+2; ++j) {
				if (i == 0 || i == Controller.BOARD_WIDTH+1 || j == 0 || j == Controller.BOARD_HEIGHT+1) {
					controller.board[i][j] = Color.GRAY;
				} else {
					controller.board[i][j] = Color.BLACK;
				}
			}
		}
		controller.shape = controller.getNextPiece();
	}
	
	/**
	 * 현재피스를 board에 출력 
	 * @param g 출력할 {@link Graphics}객체
	 */
	private void drawPiece(Graphics g) {
		g.setColor(Controller.TETRAMINO_COLORS[controller.shape]);
		for (Point p : Controller.TETRAMINOS[controller.shape][controller.rotation]) {
			g.fillRect((p.x + controller.getPieceLocation().x) * (Controller.SQUARE_SIZE + 1),
					(p.y + controller.getPieceLocation().y) * (Controller.SQUARE_SIZE + 1), Controller.SQUARE_SIZE,
					Controller.SQUARE_SIZE);
		}

	}
	
	/**
	 * board를 update하기 위한 paintComponent(Graphics g) 오버라이드<br>
	 * <br>
	 * board객체에 저장된 Color[][] 출력 후 drawPiece(g)를 통해 현재피스 출력.<br>
	 */
	@Override
	public void paintComponent(Graphics g) {
		g.fillRect(0, 0, (Controller.SQUARE_SIZE + 1) * 12, (Controller.SQUARE_SIZE + 1) * 23);
		for (int i = 0; i < Controller.BOARD_WIDTH+2; i++) {
			for (int j = 0; j < Controller.BOARD_HEIGHT+2; j++) {
				g.setColor(controller.board[i][j]);
				g.fillRect((Controller.SQUARE_SIZE + 1) * i, (Controller.SQUARE_SIZE + 1) * j, Controller.SQUARE_SIZE,
						Controller.SQUARE_SIZE);
			}
		}
		drawPiece(g);
	}

	/**
	 * 피스가 벽과 닿는지 여부를 판별하는 메소드<br>
	 * <br>
	 * 피스의 모든 square에 대하여 그 색이  Color.BLACK(벽 또는 기존의 피스들이 아닌 색)이 아닌 경우 벽과 닿아있지 않음을 판별.<br>
	 * 
	 * @param x 피스의 x좌표
	 * @param y 피스의 y좌표
	 * @param rotation 피스의 회전상태
	 * @return 피스가 벽과 닿아있는지 여부
	 */
	public boolean isTouched(int x, int y, int rotation) {
		boolean tf = false;
		for (Point p : Controller.TETRAMINOS[controller.shape][rotation]) {
			if (controller.board[p.x + x][p.y + y] != Color.BLACK) {
				tf = true;
			}
		}
		return tf;
	}
	
	/**
	 * 현재 피스의 회전상태를 i만큼 변경한다.<br>
	 * <br>
	 * 회전상태 변경 후, board를 새로 출력하고 바뀐 board의 정보를 상대방에게도 보낸다.<br>
	 * @param i 회전상태의 변경 값.
	 */
	public void rotate(int i) {
		int newRotation = (controller.rotation + i) % 4;
		if (newRotation < 0) {
			newRotation += 3;
		}

		if (!isTouched(controller.getPieceLocation().x, controller.getPieceLocation().y, newRotation)) {
			controller.rotation = newRotation;
		}
		getParent().repaint();
		controller.getTetrisuser().sendGameInfo();
	}
	
	/**
	 * 현재 피스의 x좌표를 i만큼 변경한다.<br>
	 * <br>
	 * x좌표 변경 후, board를 새로 출력하고 바뀐 board의 정보를 상대방에게도 보낸다.<br>
	 * @param i x좌표의 변경 값.
	 */
	public void move(int i) {
		if (!isTouched(controller.getPieceLocation().x + i, controller.getPieceLocation().y, controller.rotation)) {
			controller.getPieceLocation().x += i;
		}
		getParent().repaint();
		controller.getTetrisuser().sendGameInfo();
	}
	
	/**
	 * 현재 피스의 y좌표를 +1만큼 변경한다.<br>
	 * <br>
	 * 이동한 y좌표가 벽과 닿는 경우 doOnWallTouched()를 호출한다.<br>
	 * <br>
	 * 이동 후, board를 새로 출력하고 바뀐 board의 정보를 상대방에게도 보낸다.<br>
	 */
	public void drop() {
		if (!isTouched(controller.getPieceLocation().x, controller.getPieceLocation().y + 1, controller.rotation)) {
			controller.getPieceLocation().y += 1;
		} else {
			doOnWallTouched();
		}
		getParent().repaint();
		controller.getTetrisuser().sendGameInfo();
		
	}
	
	/**
	 * 해당 줄을 제거하는 메소드
	 * @param row 제거할 줄의 y좌표
	 */
	public void deleteRow(int row) {
		for (int j = row - 1; j > 0; j--) {
			for (int i = 1; i < 11; i++) {
				controller.board[i][j + 1] = controller.board[i][j];
			}
		}
	}
	
	/**
	 * 모든 줄에 대하여 gap(Color.BLACK 색의 칸)의 여부를 판별하여<br>
	 * gap이 존재하는 경우 그 줄을 제거하는 메소드<br>
	 * <br>
	 * 제거한 줄의 수에 따라 controller.addScore()를 호출한다.<br>
	 */
	public void clearRows() {
		boolean gap;
		int clearNo = 0;
		for (int j = 21; j > 0; j--) {
			gap = false;
			for (int i = 1; i < 11; i++) {
				if (controller.board[i][j] == Color.BLACK) {
					gap = true;
					break;
				}
			}
			if (!gap) {
				deleteRow(j);
				j += 1;
				clearNo += 1;
			}
		}
		controller.addLines(clearNo);
		switch (clearNo) {
		case 1:
			controller.addScore(Controller.SCORE_PER_LINES_CLEARED[clearNo - 1]);
			break;
		case 2:
			controller.addScore(Controller.SCORE_PER_LINES_CLEARED[clearNo - 1]);
			break;
		case 3:
			controller.addScore(Controller.SCORE_PER_LINES_CLEARED[clearNo - 1]);
			break;
		case 4:
			controller.addScore(Controller.SCORE_PER_LINES_CLEARED[clearNo - 1]);
			break;
		}
		clearNo = 0;
	}
	
	/**
	 * 피스가 벽(바닥)에 닿았을 경우 수행할 메소드<br>
	 * board의 피스부분의 색깔을 피스의 색깔과 동일하게 변경하여  isTouched가 true를 반환하도록 만든다<br>
	 * <br> 
	 * 만약  피스의 y좌표가 1이하인 경우 게임을 멈추고, 그 결과를 상대에게 전달한 뒤 패배로 게임을 종료한다.<br> 
	 * <br> 
	 * 그 외의 경우에는 한 피스의 해당하는 점수를 득점한 뒤  clearRows()를 호출하고<br> 
	 * 다음피스의 정보를 받아 세팅한다(controller.getNextPiece()호출)<br> 
	 * <br> 
	 * 마지막으로 변경된 값들을 SidePanel에 반영하고(controller.updateSidePanel()호출)<br> 
	 * 바뀐 board의 정보를 상대방에게도 보낸다. <br> 
	 */
	public void doOnWallTouched() {
		for (Point p : Controller.TETRAMINOS[controller.shape][controller.rotation]) {
			controller.board[controller.getPieceLocation().x + p.x][controller.getPieceLocation().y
					+ p.y] = Controller.TETRAMINO_COLORS[controller.shape];
		}
		if (controller.getPieceLocation().y < 2) {
			controller.setPlaying(false);
			controller.getTetrisuser().sendGameInfo();
			controller.finishGameLose();
		}		
		controller.addScore(Controller.SCORE_PER_PIECE);
		clearRows();
		controller.shape = controller.getNextPiece();
		controller.updateSidePanel();
		controller.getTetrisuser().sendGameInfo();
		
				
	}
	
	/**
	 * 주어진 시간(Controller.TIME_GAP)마다 drop()을 호출하여 게임을 진행시킴
	 */
	@Override
	public void run() {
		while (controller.isPlaying()) {
			try {
				Thread.sleep(Controller.TIME_GAP);
				drop();				
			} catch (InterruptedException e) {
			}
		}
	}

}
