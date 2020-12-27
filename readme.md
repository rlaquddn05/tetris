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
>[ TTC 세계 표준 테트로미노 색 ]     
>I형 : 하늘,     
>J형 : 파랑,      
>L형 : 주황,      
>O형 : 노랑,      
>S형 : 연두,      
>T형 : 보라,      
>Z형 : 빨강     

<세부 규칙>     
대다수의 테트리스의 점수 방식은 한번에 더 많은 줄을 없앨수록 더 높은 점수를 얻는 방식이다. 또한 거의 모든 테트리스 게임에서 플레이어가 버튼을 눌러 현재 떨어지는 테트로미노의 속도를 증가시킬수 있으며, 이와 반대로 다 떨어질때까지 기다리는 방법도 있다. 전자를 Hard Drop, 후자를 Soft Drop이라고 하며 본 게임에서는 Hard Drop시 보너스점수를 부여하도록 하였다.
[본 프로젝트의 득점 규칙]     
>-한 피스가 바닥또는 다른 피스에 닿을 때마다 : 5점         
>-Hard Drop보너스 점수 : 10점     
>-1줄 없앨 때마다 : 100점     
>-2줄 없앨 때마다 : 300점     
>-3줄 없앨 때마다 : 600점     
>-4줄 없앨 때마다 : 1000점     

#### 4. 본 프로그램의 장점
- 어떤 기능에 포커스를 두었는지 
- 기능 구현을 위해 어떤 새로운 공부를 했는지
- 설계를 어떻게 했는지 (아주 중요!!!) 
- .... 
	
#### 5. 힘들었던 점
- ~~~ 구현에 ~~~ 지식이 필요했는데 그 부분이 어려웠다. 그래서 이 부분을 ~~~~ 하여 해결했다.
- ~~~~ 하려고 했는데 ~~~ 한 이유로 쉽게 구현할 수 없었다. 그래서 이 부분을 ~~~~ 하여 해결했다.
#### 6. 앞으로 개선 해야 할 부분들
- ~~ 기능을 추가하려했으나 ~이유로 구현하지 못했다. 게 구현하면 될 것 (어떻게 개선할 것인가)
- 나는 ~~~~ 게 구현했는데, ~~~~ 방법도 좋을 것 같다.
#### 7. 시연영상 및 Javadoc 링크
<div>
[시연영상]<br><a href="https://www.youtube.com/watch?v=CrOOapnzwMk" target="_blank"><image src = "https://img.youtube.com/vi/CrOOapnzwMk/mqdefault.jpg"></a>	
</div>

[Javadoc](https://rlaquddn05.github.io/tetris/Tetris/doc/index.html)  
