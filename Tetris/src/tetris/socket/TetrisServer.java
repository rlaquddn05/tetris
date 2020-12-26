package tetris.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.SocketImpl;
import java.util.Arrays;

/**
 * 서버와 유저 간의 통신을 담당하는 스레드<br>
 * <br>
 * 한 유저의 정보를 받아 다른 유저에게 보낸다 <br>
 * 
 * @author 김병우
 *
 */
class NetworkThread implements Runnable {
	/**
	 * 정보를 받아 올 {@link TetrisUser}형 소켓 객체
	 */
	private TetrisUser fromSocket;

	/**
	 * 정보를 보낼 {@link TetrisUser}형 소켓 객체
	 */
	private TetrisUser toSocket;

	/**
	 * 정보를 받아 올 {@link ObjectInputStream}형 스트림 객체
	 */
	private ObjectInputStream ois;
	/**
	 * 정보를 보낼 {@link ObjectOutputStream}형 스트림 객체
	 */
	private ObjectOutputStream oos;

	/**
	 * 
	 * @param fromSocket 정보를 받아 올 소켓
	 * @param toSocket   정보를 보낼 소켓
	 */
	public NetworkThread(TetrisUser fromSocket, TetrisUser toSocket) {
		this.fromSocket = fromSocket;
		this.toSocket = toSocket;
	}

	/**
	 * 
	 * toSocket에서 정보를 받아 fromSocket으로 보내는 메소드
	 */
	@Override
	public void run() {
		try {
			oos = new ObjectOutputStream(toSocket.getOutputStream());
			ois = new ObjectInputStream(fromSocket.getInputStream());

			Object[] gameInfo;
			while ((gameInfo = (Object[]) ois.readObject()) != null) {
				System.out.println("readObject : " + Arrays.toString(gameInfo));
				oos.writeObject(gameInfo);
				oos.flush();
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
}

/**
 * Main을 실행하는 사용자 둘 간의 통신을 담당하는 서버 
 * 
 * @author 김병우
 *
 */
public class TetrisServer extends ServerSocket {
	
	/**
	 * {@link TetrisServer} 기본 생성자<br>
	 * 포트 no. : 50000<br>
	 * userSocket1, userSocket2에 클라이언트 소켓 접속 후<br> 
	 * 각각을 toSocket, fromSocket으로 하는 NetworkThread를 생성하여 호출한다
	 * @throws IOException ServerSocket의 자손형인 {@link TetrisServer}의 생성 과정에서  IO에러 발생하는 경우
	 */
	public TetrisServer() throws IOException {
		super(50000);
		System.out.println("서버 소켓 생성!");
		try {
			while (true) {
				TetrisUser userSocket1 = null;
				TetrisUser userSocket2 = null;
				userSocket1 = this.accept();
				System.out.println("클라이언트 소켓 접속! IP : " + userSocket1.getInetAddress());
				userSocket2 = this.accept();
				System.out.println("클라이언트 소켓 접속! IP : " + userSocket2.getInetAddress());

				Thread th1 = new Thread(new NetworkThread(userSocket1, userSocket2));
				Thread th2 = new Thread(new NetworkThread(userSocket2, userSocket1));

				th1.start();
				th2.start();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * {@link TetrisUser}형의 Socket객체를 accept()하기 위한 오버라이드
	 */
	@Override
	public TetrisUser accept() throws IOException {
		if (isClosed())
			throw new SocketException("Socket is closed");
		if (!isBound())
			throw new SocketException("Socket is not bound yet");
		final TetrisUser s = new TetrisUser((SocketImpl) null);
		implAccept(s);
		return s;
	}
	
	/**
	 * 서버 메인메소드<br>
	 * {@link TetrisServer}형의 tetrisServer객체를 생성한다.
	 * @param args an array of command-line arguments for the application   
	 */
	public static void main(String[] args) {
		TetrisServer tetrisServer = null;
		try {
			tetrisServer = new TetrisServer();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (tetrisServer != null) {
				try {
					tetrisServer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
