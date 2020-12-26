package tetris.socket;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JFrame;

/**
 * 본 프로그램의 Main프레임<br>
 * 
 * 사용자의 입력에 따라 {@link TetrisPanel}의 메소드를 호출하는 {@link KeyListener}를 포함한다.
 * @author 김병우
 *
 */
public class Main extends JFrame {

	private static final long serialVersionUID = 1038866172281591450L;
	
	/**
	 * Main프레임 생성자, Main프레임을 생성한 뒤 초기화하고 패널들(sidePanel, tetrisPanel, userInfoPanel)을 add()
	 * 
	 * 사용자의 입력에 따라 {@link TetrisPanel}의 메소드를 호출하는 {@link KeyListener}를 add().
	 * 
	 * @param sidePanel  Main프레임에 add()할 {@link SidePanel}객체
	 * @param tetrisPanel Main프레임에 add()할 {@link TetrisPanel}객체 
	 * @param userInfoPanel Main프레임에 add()할 {@link UserInfoPanel}객체
	 */
	public Main(SidePanel sidePanel, TetrisPanel tetrisPanel, UserInfoPanel userInfoPanel) {
		super("Tetris");
		setSize(494, 665);
		setLocation(800, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout(10, 0));
		add(sidePanel, BorderLayout.EAST);
		add(tetrisPanel, BorderLayout.CENTER);
		add(userInfoPanel, BorderLayout.NORTH);

		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					tetrisPanel.rotate(+1);
					break;
				case KeyEvent.VK_DOWN:
					tetrisPanel.drop();
					break;
				case KeyEvent.VK_LEFT:
					tetrisPanel.move(-1);
					break;
				case KeyEvent.VK_RIGHT:
					tetrisPanel.move(+1);
					break;
				case KeyEvent.VK_SPACE:
					while (!tetrisPanel.isTouched(tetrisPanel.getController().getPieceLocation().x,
							tetrisPanel.getController().getPieceLocation().y + 1, tetrisPanel.getController().rotation)) {
						tetrisPanel.drop();
					}
					tetrisPanel.doOnWallTouched();
					tetrisPanel.getController().addScore(Controller.HARD_DROP_BONUS);
					break;
				}
			}
		});
	}

	/**
	 * Main프레임 및 Controller, SidePanel, TetrisPanel, UserInfoPanel, TetrisUser, OpponentBoard객체 생성,<br>
	 * 각각의 객체들 초기화 및 게임 시작  
	 */
	public static void startGame() {
		Controller controller = new Controller();
		SidePanel sidePanel = new SidePanel(controller);
		TetrisPanel tetrisPanel = new TetrisPanel(controller);
		UserInfoPanel userInfoPanel = new UserInfoPanel(controller);
		TetrisUser tetrisUser = null;
		OpponentBoard opponentBoard = new OpponentBoard();
		try {
			tetrisUser = new TetrisUser(controller);
		} catch (IOException e) {
			e.printStackTrace();
		}
		controller.setSidePanel(sidePanel);
		controller.setTetrisPanel(tetrisPanel);
		controller.setUserInfoPanel(userInfoPanel);
		controller.setTetrisuser(tetrisUser);
		controller.setOpponentBoard(opponentBoard);
		opponentBoard.initOpponentBoard();
		Main main = new Main(sidePanel, tetrisPanel, userInfoPanel);
		JFrame opponentBoardFrame = new JFrame("Opponent");
		opponentBoardFrame.setLocation(1294, 200);
		opponentBoardFrame.setSize(327, 637);
		opponentBoardFrame.setLayout(new GridLayout(1, 1));
		opponentBoardFrame.add(opponentBoard);
		controller.initGame();
		tetrisUser.sendGameInfo();
		opponentBoardFrame.setVisible(true);
		main.setVisible(true);
		new Thread(tetrisUser).start();

	}

	public static void main(String[] args) {

		startGame();

	}
}
