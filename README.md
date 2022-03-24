<p align="center">
   <img src="https://user-images.githubusercontent.com/73349375/153821898-931569a6-c2a2-4f08-91f9-e4cfe2664578.png" width="300" height="150"/>
</p>
<p align="center">
   <b>재미있는 선물 나눔 서비스, 금나와라뚝딱!</b>
</p>

<p align="center">
안녕하세요!👋</br>
저희는 <b>프로그래머스 데브코스 1기의 👹도깨비팀</b>입니다</br> 
여러분! 혹시 다수의 친구나 지인들에게 선물(ex 기프티콘)을 줄 때 불편했던 경험이 있으신가요?</br>
금나와라뚝딱은 이러한 불편함을 해소하고 재미있게 선물을 전달하기 위해 탄생하였습니다!</br>
전화번호를 몰라도 링크를 알고 있는 누구에게나 선물이 가능하고,</br>
랜덤 또는 선착순인 눈치게임을 통해 게임처럼 재미있게 선물을 나눌 수 있습니다!</br>
지인들에게 재미있는 방식으로 기프티콘과 다양한 메세지들을 선물해보세요!
</p>
<p align="center">
<a href="https://gold-dduck.netlify.app/login"> <b>🎁 서비스 링크</b></a>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<a href="https://www.youtube.com/watch?v=bAdafVG_JWs"> <b>📺 발표 및 시연 영상</b></a>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<a href="https://www.notion.so/API-4f67bb18e01142e09327f78d4d9fe389"> <b>🛰 API</b></a>
</p>

<br>


## 목차

[1. 프로젝트 소개](#프로젝트-소개)  
[2. 팀원 소개](#팀원-소개)  
[3. 기술 스택 및 협업 도구](#기술-스택-및-협업-도구)  
[4. 협업 규칙](#협업-규칙)  
[5. 기술 적용](#기술-적용)  
[6. 데이터베이스 설계도](#데이터베이스-설계도)  
[7. 패키지 구조](#패키지-구조)

<br>

## 프로젝트 소개

금나와라 뚝딱은 **친구들과 선물을 재미있게 나누기 위한 서비스** 입니다.  
이벤트를 생성한 주최자가 선물을 주고 싶은 사람들에게만 **이벤트 링크**를 나눠주고, 해당 링크로 접속하여 **다양한 게임을 통해 선물을 획득**하는 방법으로 진행됩니다.  
서비스를 이용하기 위해서는 **카카오톡 로그인을 진행**한 후 이벤트에 참여하시거나 이벤트를 생성하시면 됩니다.  
**획득한 선물이나 생성한 이벤트**는 오른쪽 상단의 자신의 카카오톡 프로필 사진을 누르면 **마이페이지**로 이동해 확인할 수 있습니다.  

<br>

## 팀원 소개

<table>
  <tr>
    <td align="center"><img src="https://user-images.githubusercontent.com/73349375/153841934-34565ded-5f38-4c86-8615-c50181c05d4d.jpeg" width="120px" /></td>
    <td align="center"><img src="https://user-images.githubusercontent.com/73349375/153841921-296daf53-2e67-4b5f-993e-3fceef478861.jpeg" width="120px" /></td>
    <td align="center"><img src="https://user-images.githubusercontent.com/73349375/153842237-ebe2865f-d168-4e4b-91a6-827d34385fd6.png" width="120px" /></td>
  </tr>
  <tr>
    <td align="center"><b>한맹희</b></td>
    <td align="center"><b>최영권</b></td>
    <td align="center"><b>신언주</b></td>
  </tr>
  <tr>
    <td align="center"><a href="https://github.com/maenguin">Github</a></td>
    <td align="center"><a href="https://github.com/choiyoungkwon12">Github</a></td>
    <td align="center"><a href="https://github.com/GetMapping">Github</a></td>
  </tr>
</table>

<br>

## 기술 스택 및 협업 도구

### 🛠 개발 환경
- Java 11
- gradle 7
- Spring Boot 2.5.6
- JPA, Spring Data Jpa, QueryDSL
- AWS EC2, S3, RDS(MySQL8)

<br>

### 🪢 협업 도구
- Notion - 필요한 정보 정리를 위한 도구
- Gather - 실시간 소통을 위한 도구
- Slack - 실시간 소통을 위한 도구
- Jira - 이슈를 관리하기 위한 도구

<br>

### ⚙️ 기타
- ERD Cloud
- Postman
- Mysql WorkBench, DataGrip
- Github Action
- Docker

<br>

## 협업 규칙

### 📜 Rule
1. 코어 타임에는 슬랙 활성화 및 게더에 접속해있기 (13:00 ~ 19:00)
2. 프로젝트 진행 중 개발이 힘든 부분은 즉각적으로 해결하기
3. 매주 주말에 회고 진행하기
4. 매일 오후 1시에 데일리 스크럼 진행하기
5. 서로 배려하며 의사소통 열심히 하기

<br>

### 📌 Branch Strategy
[Git Workflow 전략](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow)
- **추가적인 규칙**
  - `지라 1 티켓 = 1PR 원칙`
  - 티켓 마다 feature 브랜치를 생성하고 develop으로 PR을 날립니다.
  - feature 네이밍은 `feature/{티켓번호}`
    ex) feature/GD-01
  - 티켓 단위는 최대한 작게
  - `Merge시 Squash`를 하거나 PR을 올릴때 로컬에서 Squash를 수행해서 commit graph를 최대한 단순하게 유지

<br>

### 📥 Merge Rule
- PR로만 Merge 가능 + 리뷰 Approval이 2개 이상만 Merge 승인  
  <img src="https://user-images.githubusercontent.com/49011919/140630372-2f07e682-ea73-4137-b939-e8c758c70d47.PNG" width="400"/>
- 테스트를 모두 통과해야 Merge가 가능 + 최신 상태의 develop 브랜치에만 Merge 가능  
  <img src="https://user-images.githubusercontent.com/49011919/140630396-fd4bcbfd-10a6-41f3-9749-22a980249b5b.PNG" width="400"/>

<br>

### 📝 Code Convention
- [구글 자바 컨벤션](https://google.github.io/styleguide/javaguide.html)

<br>

### 🖍 Commit Message Convention
- [AngularJS Git Commit Message Conventions](https://docs.google.com/document/d/1QrDFcIiPjSLDn3EL15IJygNPiHORgU1_OOAqWjiDU5Y/edit)

<br>

## 기술 적용

### 🌈 서비스 구현
#### Spring boot
- 백엔드 서버는 **Java를 사용**하여 효율적인 프로젝트 진행을 위해 **Spring Boot를 활용**하였습니다.

#### Github & Docker
- 버전 관리 툴로 **Git과 Github**를 사용했고 **GithubAction**과 **Docker**를 사용해서 **CI/CD**를 구축하였습니다.

#### EC2 & S3
- 교육 과정에서 배운 **AWS** 클라우드 서비스를 적극 사용하여 백엔드 서버는 **EC2**로 구축하고 EC2위에 **docker**를 구동시켜서 **EC2 경량화**를 했습니다.
  또한 선물 이미지를 저장하기 위해 AWS의 S3를 사용하여 이미지를 저장하고 받아오도록 했습니다.

#### RDS & MySQL & Redis
- 데이터베이스는 **MySQL**을 사용하였고 **AWS의 RDS**를 사용하여 **MySQL Server**를 구축하였습니다.
  동시성 처리를 위해서는 DB의 lock을 사용하여 동시성을 제어했습니다.

<br>

### 📈 CI/CD 적용
#### CI - Github Action
- java ci with gradle을 사용하여 지속적인 Build 프로세스 체크

#### CD - Github Action + Docker
- Github Action을 사용하여 Docker Image를 생성하고 Docker Hub로 push  
  그 후 Ec2에서 Docker Image를 pull 받아서 서버를 재실행하도록 구축

<br>

### 🔒 보안 기술 적용
#### 인증 방법
- Jwt 토큰 기반의 oauth2 카카오 소셜 로그인 구축

#### 권한 모델
- User : Group : Role 구조 채택

#### https
- https 및 도메인 적용

<br>

### 🌱 협업 도구 활용

#### Notion
- 한 주의 계획 정리
- 프론트엔드와의 회의 내용 기록
- 한 주의 스프린트 종료 후 회고 기록

#### Jira
- 이슈 생성
  - 백로그로 추가된 이슈는 단위를 작게 쪼개서 하위 작업으로 추가  
    <img src="https://user-images.githubusercontent.com/47075043/146881624-a62d5620-5dc2-4d56-8465-299309ad5f0d.png" width="500"/>

- 자동화
  - 깃허브-지라 연동 스마트 커밋 기능 사용
  - 에픽의 하위 작업이 완료되면, 상위 작업을 완료로 이동
  - 깃허브 PR merge시 이슈 자동 완료 처리  
    <img src="https://user-images.githubusercontent.com/47075043/146881722-52ae2bff-2663-4160-984c-394972aafaf3.png" width="500"/>
    </br>
    <img src="https://user-images.githubusercontent.com/47075043/146881744-cee37f15-cdb7-4241-8d45-488974479f95.png" width="500"/>


#### Slack
- Slack과 Github 레포를 연동하여 새로운 Pull Request 알림  
  <img src="https://user-images.githubusercontent.com/73349375/159948113-2e357bad-6f30-4742-8644-340c1a967523.png" width="500"/>

<br>

## 데이터베이스 설계도

<img src="https://user-images.githubusercontent.com/49011919/146876270-31cf7f27-5507-43f2-8afe-278236473b30.png" width="600"/>

<br>

## 패키지 구조

  <details>
  <summary>Click!</summary>
  <div markdown="1">
  <pre>
  **gold-Dduck**
  ├─classes
  └─java
  ├─main
  │  └─com
  │      └─dokev
  │          └─gold_dduck
  │              ├─aws
  │              │  ├─config
  │              │  └─service
  │              ├─common
  │              │  ├─error
  │              │  ├─exception
  │              │  └─util
  │              ├─config
  │              ├─event
  │              │  ├─controller
  │              │  ├─converter
  │              │  ├─domain
  │              │  ├─dto
  │              │  ├─repository
  │              │  └─service
  │              ├─gift
  │              │  ├─controller
  │              │  ├─converter
  │              │  ├─domain
  │              │  ├─dto
  │              │  ├─repository
  │              │  └─service
  │              ├─jwt
  │              ├─member
  │              │  ├─controller
  │              │  ├─converter
  │              │  ├─domain
  │              │  ├─dto
  │              │  ├─repository
  │              │  └─service
  │              └─oauth2
  └─test
  └─com
  └─dokev
  └─gold_dduck
  ├─aws
  │  └─service
  ├─event
  │  ├─controller
  │  ├─repository
  │  └─service
  ├─factory
  ├─gift
  │  ├─controller
  │  └─service
  └─security
  </pre>
  </div>
  </details>
