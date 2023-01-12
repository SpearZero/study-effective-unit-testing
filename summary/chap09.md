## 9장

### 속도 개선을 위해서

#### 더 빠르게
테스트가 빨리 끝나야 하는 이유는 피드백이 늦어질수록 피해가 더 커지기 때문이다.
- 개발자가 검증 결과를 기다려야 한다.
- 개발자가 테스트를 빌드 서버에 맡겼을때, 빌드 서버가 뒤늦게 문제를 발견하면 개발자는 하던 일을 멈추고 이전 작업을 다시 검토해야 한다.

#### 상황 속으로
프로파일러(profiler)를 통해 병목 지점을 찾고 성능 저하의 원인을 파악

#### 빌드 프로파일링하기
앤트 빌드, 메이븐 빌드 프로파일링에 대한 설명(252-256p)

#### 테스트 프로파일링하기
테스트 프로파일링에 대한 설명(256-259p)

### 테스트 코드 속도 높이기
테스트 코드 속도 개선의 핵심 전략은 느린 부분을 찾아내어 더 빠르게 만들거나 아예 실행시키지 않는 것이다.

#### 피곤하지 않다면 잠들지 말라
테스트를 빠르게 유지하려면 테스트가 필요 이상으로 <b>멈춰</b>있개 해서는 안된다.

Thread.sleep()은 사용하지 말자. 대신 동기화 객체를 활용하면 훨씬 시뢰할 수 있는 결과를 얻게 될 것이다.

#### 덩치 큰 기반 클래스를 경계하라
기반 클래스는 보통 공통의 셋업과 티어다운 메서드 그리고 여러 가지 유틸리티 메서드를 제공한다.

하위 클래스의 모든 테스트가 이런 유틸리티 기능 전부를 사용하는 경우는 거의 없다. 기능을 필요로 하지 않는 
테스트 케이스에서도 쓸데없이 반복 수행되어 성능을 떨어뜨리지는 않는지 잘 살펴야 한다.

<b>구조적 성능 저하 요인</b>

Junit은 다른 클래스를 상속하면 그 클래스의 셋업과 티어다운읆 매번 실행한다. 계층이 깊어질수록 필요 없는 셋업과 티어다운의 수가 많아질 것이고, 
계층을 오르내리는 데 낭비되는 CPU 시간도 길어질 것이다.

#### 불필요한 셋업과 티어다운을 경계하라
Junit의 @Before와 @After 메서드는 하나당 한 번씩 실행된다. 메서드에서 필요 없는 경우에도 실행된다.

셋업을 한 번만 실행되도 되는 테스트 클래스라면 @Before을 @BeforeClass로 바꿔주면 된다.

(Junit5에서는 @Before -> @BeforeEach, @BeforeClass -> @BeforeAll로 변경)

#### 테스트에 초대할 손님은 까다롭게 선택하라
테스트와 관련 없는 협력 객체를 잘라내어 테스트 대상의 범위를 가능한 한 좁혀주면 된다.

각 테스트에서 중요하지 않은 부분을 스텁으로 교체함으로써 지루한 빌드 시간을 단축할 수 있다.

#### 로컬하게, 그리고 빠르게 유지하라
메모리에서 몇 바이트를 읽는 데는 시간이 얼마 걸리지 않는다. 같은 데이터라도 클라우드 서비스에서 가져오려면 지역 변수를 읽는 것보다 시간이 훨씬 더 걸린다.

단위 테스트에서는 네트워크를 호출하지 않는 것을 기본 원칙으로 한다. 

네트워크 호출 부분을 테스트 더블로 대체하면 테스트를 격리할 수 있고 볼확실한 외부 요소가 사라져서 신뢰성이 높아진다. 더불어 네트워크 장애나 웹서비스
타임아웃 같은 특수 상황도 시뮬레이션 할 수 있게 된다.

#### 데이터베이스의 유혹을 뿌리쳐라

데이터베이스는 되도록 사용하지 않는 것이 좋다. 데이터베이스 접근은 시간이 오래 걸리고, 그곳에 저장할 데이터도 단위 테스트의
검사 내용과는 대부분 무관하기 때문이다. 잘 저장되는지도 분명 확인해야겠지만, 이는 통합 에스트와 같은 별도의 테스트에 맡기면 된다.

> 차선책으로 인메모리(in-memory), 인프로세스(in-process) 데이터베이스를 사용해서 테스트를 할 수 도 있다.(H2와 같은..)

#### 파일 I/O보다 느린 I/O는 없다.
테스트를 빠르게 유지라혀면 파일시스템 접근을 최소화해야 한다.

<b>테스트 코드의 파일 접근을 피하라</b><br>
파일 I/O 없이 처리할 수 있는지 알아보고, 어쩔수 없는 경우라면 한 번 읽은 데이터를 캐싱 해야 한다.

<b>대상 코드의 파일 접근을 가로채라</b><br>
로깅 끄기

<b>간단한 클래스패스 꼼수로 로깅 끄기</b><br>
src/test/resources에 로그를 무시하는 설정을 넣고, 테스트 코드를 작성한다. 메이븐의 경우 검색하는 클래스 패스
순서상 src/main/resources보다 src/test/resources가 앞서기 때문에 테스트용 설정 파일이 먼저 읽힌다.
