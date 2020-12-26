package tetris.socket;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

/**
 * 상대 board 상황을 출력하기 위한 {@link JPanel}객체<br> 
 * 
 * @author 김병우
 *
 */
public class OpponentBoard extends JPanel{

	private static final long serialVersionUID = -5694914321707868199L;
	
	/**
	 * 상대 board의 상황을 저장할 Color[][]형 객체
	 */
	private Color[][] opponentBoard;
	
	/**
	 * 상대 현재 피스의 종류
	 */
	private int opponentShape;
	
	/**
	 * 상대 현재 피스의 위치
	 */
	private Point opponentPieceLocation;
	
	/**
	 * 상대 현재 피스의 회전상태
	 */
	private int opponentRotation;
	
	/**
	 * opponentBoard의 레퍼런스 반환
	 * @return opponentBoard
	 */
	public Color[][] getOpponentBoard() {
		return opponentBoard;
	}
	
	/**
	 * opponentBoard에 Color[][]형 객체 저장
	 * @param opponentBoard 상대 보드의 Color[][]형 색 현황
	 */
	public void setOpponentBoard(Color[][] opponentBoard) {
		this.opponentBoard = opponentBoard;
	}
	
	/**
	 * opponentShape의 값 반환
	 * @return opponentShape
	 */
	public int getOpponentShape() {
		return opponentShape;
	}
	
	/**
	 * opponentShape에 {@link Integer}형 값 저장
	 * @param opponentShape 상대 현재 피스의 shape정보
	 */
	public void setOpponentShape(int opponentShape) {
		this.opponentShape = opponentShape;
	}
	
	/**
	 * opponentPieceLocation값 반환
	 * @return opponentPieceLocation
	 */
	public Point getOpponentPieceLocation() {
		return opponentPieceLocation;
	}
	
	/**
	 * opponentPieceLocation에 {@link Point}형 객체 저장
	 * @param opponentPieceLocation 상대의 현재 PieceLocation 
	 */
	public void setOpponentPieceLocation(Point opponentPieceLocation) {
		this.opponentPieceLocation = opponentPieceLocation;
	}
	
	/**
	 * opponentRotation값 반환
	 * @return opponentRotation
	 */
	public int getOpponentRotation() {
		return opponentRotation;
	}
	
	/**
	 * opponentRotation에 {@link Integer}형 값 저장
	 * @param opponentRotation 상대의 현재 피스의 Rotation정보
	 */
	public void setOpponentRotation(int opponentRotation) {
		this.opponentRotation = opponentRotation;
	}
	
	
	/**
	 * OpponentBoard를 update하기 위한 paintComponent(Graphics g) 오버라이드<br>
	 * <br>
	 * opponentBoard객체에 저장된 Color[][] 출력 후 drawPiece(g)를 통해 현재 상대피스 출력.<br>
	 */
	@Override
	public void paintComponent(Graphics g) {
		g.fillRect(0, 0, (Controller.SQUARE_SIZE + 1) * 12, (Controller.SQUARE_SIZE + 1) * 23);
		for (int i = 0; i < Controller.BOARD_WIDTH+2; i++) {
			for (int j = 0; j < Controller.BOARD_HEIGHT+2; j++) {
				g.setColor(this.opponentBoard[i][j]);
				g.fillRect((Controller.SQUARE_SIZE + 1) * i, (Controller.SQUARE_SIZE + 1) * j, Controller.SQUARE_SIZE,
						Controller.SQUARE_SIZE);				
			}
		}
		drawPiece(g);
	}
	
	/**
	 * 상대방의 현재피스를 opponentBoard에 출력 
	 * @param g 출력할 {@link Graphics}객체
	 */
	private void drawPiece(Graphics g) {
		g.setColor(Controller.TETRAMINO_COLORS[opponentShape]);
		for (Point p : Controller.TETRAMINOS[opponentShape][opponentRotation]) {
			g.fillRect((p.x + opponentPieceLocation.x) * (Controller.SQUARE_SIZE + 1),
					(p.y + opponentPieceLocation.y) * (Controller.SQUARE_SIZE + 1), Controller.SQUARE_SIZE,
					Controller.SQUARE_SIZE);
		}
	}
	
	/**
	 *  opponentBoard 초기화 및 초기 상대 피스 설정<br>
	 *  상대 피스의 초기위치(상대 피스 정보를 받기 전)는 보드 상 출력되지 않는 곳<br>(임의로(-10,-10)으로 설정)으로 설정 
	 *  
	 */
	public void initOpponentBoard() {
		opponentBoard = new Color[Controller.BOARD_WIDTH+2][Controller.BOARD_HEIGHT+3];
		for (int i = 0; i < Controller.BOARD_WIDTH+1; ++i) {
			for (int j = 0; j < Controller.BOARD_HEIGHT+2; ++j) {
				if (i == 0 || i == Controller.BOARD_WIDTH+1 || j == 0 || j == Controller.BOARD_HEIGHT+1) {
					opponentBoard[i][j] = Color.GRAY;
				} else {
					opponentBoard[i][j] = Color.BLACK;
				}
			}
		}		
		opponentPieceLocation = new Point(-10,-10);
	}
	
}
