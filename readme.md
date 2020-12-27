#### 1. 프로젝트 소개
본 프로젝트는 1984년 소련의 프로그래머 알렉세이 파지노프가 만든 고전 퍼즐 게임, 테트리스를 이클립스를 활용하여 자바로 구현하는것이다. 
테트리스의 기본 룰은 간단하다. 블럭을 쌓으면서 한 줄이 꽉 채워지면 그 줄은 사라지고 이런 식으로 블럭이 맨 위까지 안 쌓이게 끝까지 버티는 것이다.

이번 프로젝트는 소켓통신을 활용하여 두명의 사용자가 마라톤모드(더 오래 버티는 모드)에서 대전을 할 수 있도록 구현하는 것을 목표로 제작되었으며
추후 다양한 게임모드 및 다수의 동시접속자 수용, 사용자 등록기능 등을 추가할 수 있도록 하는것을 염두에 두고 작업하였다.

#### 2. 사용된 프로그램 정보
>JDK 1.8    
>IDE : 이클립스    

#### 3. 테트리스란? (본 프로그램의 테트리스 세부 규칙 포함)
테트리스는 4개의 사각형을 조합한 '테트로미노(Tetromino)'를 활용한 퍼즐게임이다. 이 게임의 목표는 무작위로 떨어지는 테트로미노를 움직이고 90도씩 회전하여 수평선을 빈틈 없이 채우는 것이다. 이러한 수평선이 만들어질 때 이 선은 없어지며 그 위의 블록이 아래로 떨어진다. 블록을 꼭대기까지 가득 메워, '테트로미노가 더 들어갈 공간이 없게 되면 게임이 끝나게 된다.

< 테트로미노(Tetromino) >    
-테트로미노들은 그 외형을 본 따 I, J, L, O, S, T, Z의 7종류로 나뉜다.    
-테트로미노들의 색은 여러 게임들에서 다양하게 사용되었지만 본 프로젝트에서는 테트리스의 저작권 소유사인 TTC의 세계 표준 색을 따르고 있다.    
> [ TTC 세계 표준 테트로미노 색 ]     
> - I형 : 하늘,     
> - J형 : 파랑,      
> - L형 : 주황,      
> - O형 : 노랑,      
> - S형 : 연두,      
> - T형 : 보라,      
> - Z형 : 빨강     

<세부 규칙>     
대다수의 테트리스의 점수 방식은 한번에 더 많은 줄을 없앨수록 더 높은 점수를 얻는 방식이다. 또한 거의 모든 테트리스 게임에서 플레이어가 버튼을 눌러 현재 떨어지는 테트로미노의 속도를 증가시킬수 있으며, 이와 반대로 다 떨어질때까지 기다리는 방법도 있다. 전자를 Hard Drop, 후자를 Soft Drop이라고 하며 본 게임에서는 Hard Drop시 보너스점수를 부여하도록 하였다.
> [본 프로젝트의 득점 규칙]     
> - 한 피스가 바닥또는 다른 피스에 닿을 때마다 : 5점         
> - Hard Drop보너스 점수 : 10점     
> - 1줄 없앨 때마다 : 100점     
> - 2줄 없앨 때마다 : 300점     
> - 3줄 없앨 때마다 : 600점     
> - 4줄 없앨 때마다 : 1000점     

#### 4. 본 프로그램의 장점 및 특징
- 향후 게임 기능의 조정을 쉽게 할 수 있도록 코딩하는데에 중접을 두었다. 각종 상수값들을 변경함으로써 게임의 점수체계나 난이도를 변경할 수 있다.

- 테트라미노 피스들의 정보들을 어떤 자료형으로써 저장하고 활용하는것이 좋은지에 대하여 고민을 많이 했다. 모양과 회전상태 그리고 좌표정보를 Point[모양][회전][좌표] 저장한다면 이를 활용하여 Color[][]의 board객체에 각각의 상황을 저장할 수 있으며 최소한의 반복문 수행으로 board의 변화를 출력할 수 있다고 생각했다.  

- 피스의 y좌표가 주어진 시간마다 변하는 drop() 메소드를 만들어 게임이 진행되도록 하였다.   

- 피스가 검정색 배경이아닌 다른 색깔과 만나는지 확인하는 메소드 isTouched()를 통해 바닥/벽 또는 다른 피스와 닿는지를 판별하고 doOnWallTouched()를 통해 피스가 바닥 또는 쌓여있는 다른 피스에 닿았을 시 줄을 없애거나 쌓이도록 구현하였다. 

- hard Drop의 경우 isTouched()가 true를 반환할 때까지 drop()을 반복하고 doOnWallTouched()를 호출하는 구조로 만들어 그 즉시 다음 피스가 등장하지만 소프트 드롭의 경우 주어진 시간마다 발동하게 되므로 그 시간사이에는 바닥에 닿아있는 상태에서도 좌우움직임 또는 회전을 할수 있로록 구현된 점도 기존의 테트리스 게임들과 같다.
	
#### 5. 힘들었던 점
- 서버 구현 과정에서 정보를 Object[]형으로 전송하기로 계획하였다. 그런데 에러는 발생하지 않았으나 Point형 객체와 Color[][]를 전송하는 과정에서 계속해서 초기 설정했던 값만이 전송되는 현상이 발견되었다. 이 부분들을 Point / Color[][] 형이 아닌 int[] / int[][]로 전송함으로써 문제가 해결되었다. 아직 명확한 원인이 밝혀진 것은 아니지만 Immutable Class라 그 값이 변하지 않고 일정한 것이 아닌가 싶다.

- panel을 extend한 클래스들을 생성한 뒤 paintComponent를 override하여 repaint를 호출함으로써 변화가 생길때마다 board를 갱신하는 구조로 board를 구현하였다. 이 과정에서 다른 board가 아닌 다른 panel들에도 paintComponent가 호출되는 문제가 발생하였다. 이 문제는 getParent.repaint를 함으로써 이 메소드를 가진 객체가 아닌 다른 패널들에서는 메소드가 호출되지 않도록 하였다.

#### 6. 앞으로 개선 해야 할 부분들
- db와 연동하여 로그인 기능 및 전적확인 기능을 추가하려했으나 시간상의 이유로 구현하지 못했다.     
현재 TetrisUser의 run()에서는 한 소켓에서 정보를 받아 다른 소켓으로만 보내는데 로그인 정보의 경우 한 소켓에서 받아 다시 그 소켓으로 보내 주어야 하기 때문이다. 이를 해결하기 위해서는 소켓의 추가적인 연결이 필요하다.    

- 현재 먼저 접속한 사람은 다른 사람이 접속할 때까지 화면상에 아무 변화가 일어나지 않는다. 상대를 기달려 달라고 하는 메시지를 띄워 주면 좋을것 같다.    

- 두 사람이 모두 접속한 경우 즉시 게임을 시작하는 것이 아닌 카운트다운 후 게임을 시작한다면 더 좋을것 같다.

- GUI에 신경을 써서 더 보기 좋게 만들면 좋을 것 같다.

#### 7. 시연영상 및 Javadoc 링크
<div>
[시연영상]<br><a href="https://www.youtube.com/watch?v=CrOOapnzwMk" target="_blank"><image src = "https://img.youtube.com/vi/CrOOapnzwMk/mqdefault.jpg"></a>	
</div>

[Javadoc](https://rlaquddn05.github.io/tetris/Tetris/doc/index.html)  
