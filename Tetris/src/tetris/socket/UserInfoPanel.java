package tetris.socket;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 사용자 정보를 출력하기 위한 {@link JPanel}객체<br>
 * <br>
 * 주어진 {@link String}형 객체를 출력할 패널만 있는 상태<br>
 * 향후 db와 연결하여 로그인 및 승패정보를 출력할 계획
 * @author 김병우
 *
 */
public class UserInfoPanel extends JPanel { 
	
	private static final long serialVersionUID = -9160131834597381585L;
	/**
	 * {@link Controller}객체의 함수들을 호출하기 위한 {@link Controller}형 필드
	 */
	private Controller controller;
	/**
	 * 사용자 정보를 출력하기 위한 {@link JLabel}형 객체
	 */
	private JLabel userInfo = new JLabel();
	
	/**
	 * UserInfoPanel 생성자<br>
	 * controller.getUserInfo()로 호출한 사용자 정보를 userInfo에 대입하고 이를 add()하여 출력한다.<br>
	 * <br>
	 * @param controller 다른 클래스의 객체들을 호출하기 위한 {@link Controller}형 레퍼런스
	 */
	public UserInfoPanel(Controller controller) {
		super();
		this.controller = controller;
		userInfo.setText(this.controller.getUserInfo());
		this.add(userInfo);
	}
	
}
