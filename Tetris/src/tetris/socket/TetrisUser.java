package tetris.socket;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketImpl;
import java.util.Arrays;

/**
 * TetrisServer와 통신하기 위한 객체<br>
 * <br>
 * Object[]형의 gameInfo를 주고받는다. 
 * @author 김병우
 *
 */
public class TetrisUser extends Socket implements Runnable {
	
	/**
	 * {@link Controller}객체의 함수들을 호출하기 위한 {@link Controller}형 필드
	 */
	private Controller controller;
	/**
	 * 정보를 받아 올 {@link ObjectInputStream}형 객체
	 */
	private ObjectInputStream ois;
	/**
	 * 정보를 보낼 {@link ObjectOutputStream}형 스트림 객체
	 */
	private ObjectOutputStream oos;
	/**
	 * 주고받을 정보를 포장하기 위한 Object[]형 객체
	 */
	private Object[] gameInfo;
	
	/**
	 * 주고받을 정보를 Object[]형의 gameInfo로 포장하여 서버로 전송<br>
	 * <br>
	 * gameInfo[0] : controller.board의 Color정보, {@link Integer}[][]형 포장<br>
	 * gameInfo[1] : controller.shape<br>
	 * gameInfo[2] : controller.pieceLocation, {@link Integer}[]형으로 포장<br>
	 * gameInfo[3] : controller.rotation<br>
	 * gameInfo[4] : controller.isPlaying()<br>
	 * 
	 */
	public void sendGameInfo() {
		int[][] boardInt = new int[Controller.BOARD_WIDTH + 2][Controller.BOARD_HEIGHT + 3];
		for (int i = 0; i < Controller.BOARD_WIDTH + 1; ++i) {
			for (int j = 0; j < Controller.BOARD_HEIGHT + 2; ++j) {
				if (controller.board[i][j] == Color.BLACK) {
					boardInt[i][j] = 1;
				} else if (controller.board[i][j] == Color.GRAY) {
					boardInt[i][j] = 2;
				} else if (controller.board[i][j] == Controller.TETRAMINO_COLORS[0]) { 
					boardInt[i][j] = 3;
				} else if (controller.board[i][j] == Controller.TETRAMINO_COLORS[1]) { 
					boardInt[i][j] = 4;
				} else if (controller.board[i][j] == Controller.TETRAMINO_COLORS[2]) { 
					boardInt[i][j] = 5;
				} else if (controller.board[i][j] == Controller.TETRAMINO_COLORS[3]) {
					boardInt[i][j] = 6;
				} else if (controller.board[i][j] == Controller.TETRAMINO_COLORS[4]) {
					boardInt[i][j] = 7;
				} else if (controller.board[i][j] == Controller.TETRAMINO_COLORS[5]) {
					boardInt[i][j] = 8;
				} else if (controller.board[i][j] == Controller.TETRAMINO_COLORS[6]) {
					boardInt[i][j] = 9;
				}
			}
		}

		gameInfo = new Object[5];
		gameInfo[0] = boardInt;
		gameInfo[1] = controller.shape;
		gameInfo[2] = new int[] { controller.getPieceLocation().x, controller.getPieceLocation().y };
		gameInfo[3] = controller.rotation;
		gameInfo[4] = controller.isPlaying();

		try {
			oos.writeObject((Object) gameInfo);
			System.out.println("writeObject : " + Arrays.toString(gameInfo));
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * TetrisUser객체 생성자<br>
	 * 
	 * 객체 생성 후, 서버에 접속 및 {@link Controller}형 객체의 레퍼런스를 controller필드에 저장한다.
	 * 서버 IP: 220.71.67.158<br>
	 * 서버 포트no. : 50000<br>
	 *  
	 * @param controller 다른 클래스의 객체들을 호출하기 위한 {@link Controller}형 레퍼런스
	 * @throws IOException Socket의 자손형인 {@link TetrisUser}의 생성 과정에서  IO에러 발생하는 경우
	 */
	public TetrisUser(Controller controller) throws IOException {
		super("220.71.67.158", 50000);
		this.controller = controller;
		System.out.println("서버 접속!");
		oos = new ObjectOutputStream(getOutputStream());
		ois = new ObjectInputStream(getInputStream());
	}
	
	/**
	 * TetrisUser객체 생성자<br>
	 * {@link TetrisServer}의 accept()메소드 오버라이드를 위함
	 * @param socketImpl the subclass wishes to use on the Socket. 
	 */
	public TetrisUser(SocketImpl socketImpl) {
		super();
	}
	
	/**
	 * 서버에서 보낸 정보를 받을 때마다 이를  opponentBoard에 반영하고 출력하는 메소드<br>
	 * opponentGameInfo[4] == false를 받아 상대의 게임이 먼저 종료된 경우<br> 
	 * controller.finishGameWin()을 호출하고 게임을 승리한다. 
	 */
	@Override
	public void run() {
		Object[] opponentGameInfo;
		try {
			while ((opponentGameInfo = (Object[]) ois.readObject()) != null) {
				int[] pointArr = (int[]) opponentGameInfo[2];

				Color[][] opponentBoardColor = new Color[Controller.BOARD_WIDTH + 2][Controller.BOARD_HEIGHT + 3];
				for (int i = 0; i < Controller.BOARD_WIDTH + 1; ++i) {
					for (int j = 0; j < Controller.BOARD_HEIGHT + 2; ++j) {
						if (((int[][]) opponentGameInfo[0])[i][j] == 1) {
							opponentBoardColor[i][j] = Color.BLACK;
						} else if (((int[][]) opponentGameInfo[0])[i][j] == 2) {
							opponentBoardColor[i][j] = Color.GRAY;
						} else if (((int[][]) opponentGameInfo[0])[i][j] == 3) {
							opponentBoardColor[i][j] = Controller.TETRAMINO_COLORS[0];
						} else if (((int[][]) opponentGameInfo[0])[i][j] == 4) {
							opponentBoardColor[i][j] = Controller.TETRAMINO_COLORS[1];
						} else if (((int[][]) opponentGameInfo[0])[i][j] == 5) {
							opponentBoardColor[i][j] = Controller.TETRAMINO_COLORS[2];
						} else if (((int[][]) opponentGameInfo[0])[i][j] == 6) {
							opponentBoardColor[i][j] = Controller.TETRAMINO_COLORS[3];
						} else if (((int[][]) opponentGameInfo[0])[i][j] == 7) {
							opponentBoardColor[i][j] = Controller.TETRAMINO_COLORS[4];
						} else if (((int[][]) opponentGameInfo[0])[i][j] == 8) {
							opponentBoardColor[i][j] = Controller.TETRAMINO_COLORS[5];
						} else if (((int[][]) opponentGameInfo[0])[i][j] == 9) {
							opponentBoardColor[i][j] = Controller.TETRAMINO_COLORS[6];
						}
						
					}
				}

				controller.setOpponentBoardData(opponentBoardColor, (int) opponentGameInfo[1],
						new Point(pointArr[0], pointArr[1]), (int) opponentGameInfo[3]);
				controller.repaintOpponentBoard();
				if ((boolean) opponentGameInfo[4] == false) {
					controller.finishGameWin();
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
