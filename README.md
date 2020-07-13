## Labeling

#### 목표
- 딥러닝을 위한 라벨링된 데이터를 제작
- 웹 또는 앱에서 접근하여 raw 데이터를 labeling 한다.

#### 기능
- 일반 사용자 (User)
  - 관리자에게 id, pw를 부여 받는다.
  
  - 데이터 라벨링
    - 대시보드 화면 좌측 사이드바에서 `Labeling` 메뉴를 통해 데이터 라벨링 과정 시작
    - 사용자가 설정한 감정 목록을 바탕으로 데이터 라벨링 진행
    
  - 대시보드 홈 화면에서 현재 상황 확인 가능
    - 할당된 데이터 개수
    - 할당된 데이터 중 라벨링 완료한 데이터 개수
    - 할당된 데이터 중 라벨링 해야하는 데이터 개수
    - 라벨링 진행률

- 관리자 (Admin)

  - 사용자 계정 생성
  
  - 사용자 계정 수정
    - pw 수정
    - 할당된 라벨링 개수 변경
    
  - 사용자에게 labeling 할 데이터 할당
  
  - 데이터 라벨링
  
  - 전체 사용자의 라벨링 현황 조회

#### 데이터
- twitter 데이터
  - 2개의 컬럼으로 구성
  - date: 해당 트위터 게시글의 날짜
  - content: 해당 트위터 게시글 내용 

#### DB 스키마
- account 테이블
  - 사용자 계정 저장 테이블
  - id: varchar(20)
  - password: varchar(255)
- labeling 테이블
  - 데이터 저장 테이블
  - id: BIGINT
  - date: varchar(255)
  - content: lob
  - owner: varchar(255)
  - emotion: varchar(255)

#### 라벨 값
- `application.yml` 파일에 emotions로 관리

#### 사용 기술
- MySQL
- Spring Boot

#### 개발 언어
- Java