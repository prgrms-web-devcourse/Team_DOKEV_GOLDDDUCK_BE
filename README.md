# Team_DOKEV_GOLDDDUCK_BE

# 🛎 프로젝트 설명

저희 **도깨비팀**의 프로젝트인 **금나와라 뚝딱**은 이벤트 주최자가 만든 선물을 사용자가 수령하는 서비스 입니다.

# ☑️ 프로젝트 선정 배경

다수의 친구나 지인들에게 선물(ex 기프티콘)을 줄때 일일히 한명 한명 전달해주거나 전화번호를 물어봐야했던 경험이 있으신가요? 금나와라 뚝딱은 이러한 불편함을 없애고 선물을 편하고 재미있게 전달하면 어떨까? 라는 물음에서 탄생했습니다.

금나와라 뚝딱은 단순히 선물을 지목한 사람에게 주는 형식이 아니라, ‘랜덤’ 또는 ‘선착순(눈치게임)’의 방식을 통해 게임처럼 재미있게 선물을 나눌 수 있는 서비스입니다.

# 👨🏻‍💻 팀원 소개

|  | 한맹희 | 최영권 | 신언주 |
| --- | --- | --- | --- |
| 역할 | 팀장 | 팀원 | 팀원 |
| Github | maenguin | choiyoungkwon12 | GetMapping |

# **🚀 기술스택**

### **개발 환경**

- Java 11
- Spring Boot 2.5.6
- JPA, Spring Data Jpa, QueryDSL, Spring Data Redis
- AWS EC2, S3, RDS(MySQL8)
- gradle 7

### **협업 툴**

- Notion
- Slack
- Jira

### **기타**

- ERDCloud
- Postman
- Mysql WorkBench, DataGrip
- Github Action
- Docker

# 🐮 ERD 설계

![https://user-images.githubusercontent.com/49011919/146876270-31cf7f27-5507-43f2-8afe-278236473b30.png](https://user-images.githubusercontent.com/49011919/146876270-31cf7f27-5507-43f2-8afe-278236473b30.png)

# 🏗️ 프로젝트 구조

![https://user-images.githubusercontent.com/49011919/146668046-0d58d47d-b21f-4cfe-8148-74c7757245a8.png](https://user-images.githubusercontent.com/49011919/146668046-0d58d47d-b21f-4cfe-8148-74c7757245a8.png)

<br>

**Spring boot**

백엔드 서버는 교육 과정에서 학습한 **Java**기반의 **Spring Boot** 애플리케이션으로 제작했습니다.

<br>

**Github & Docker**

버전 관리 툴로 **Git과 Github**를 사용했고 **GithubAction**과 **Docker**를 사용해서 **CI/CD**를 구축하였습니다.

<br>

**EC2**

교육 과정에서 배운 **AWS** 클라우드 서비스를 적극 사용하여 백엔드 서버는 **EC2**로 구축하고 EC2위에 **docker**를 구동시켜서 **EC2 경량화**를 시도했고 **Spring Boot** 서버와 **Redis**가 동작하도록 했습니다.

또한 선물 이미지를 저장하기 위해 AWS의 S3를 사용하여 이미지를 저장하고 받아오도록 했습니다.

<br>

**RDS & MySQL & Redis**

데이터베이스는 **MySQL**을 사용하였고 **AWS의 RDS**를 사용하여 **MySQL Server**를 구축하였습니다.

동시성 처리를 위해서는 DB의 트랜잭션을 최소화할 필요가 있었고 이를 위해 **Redis 서버**를 구축하여 메모리 기반의 빠른 처리를 유도 했습니다.

<br>

# 🎁 패키지 구조

```bash
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
```

# 💌 프로젝트 컨벤션

## 코드 컨벤션

[구글 자바 컨벤션](https://google.github.io/styleguide/javaguide.html)

## Git 컨벤션

### Git Flow 전략 사용

**특이사항**

- `지라 1 티켓 = 1PR 원칙`
- 티켓 마다 feature 브랜치를 생성하고 develop으로 PR
- feature 네이밍은 `feature/{티켓번호}`
ex) feature/GD-01
- 티켓 단위는 최대한 작게
- `Merge시 Squash`를 하거나 PR을 올릴때 로컬에서 Squash를 수행해서 commit graph를 최대한 단순하게 유지

### Commit Message 작성 규칙

```bash
1- ⭐ feat : 새로운 기능에 대한 커밋
2- ⚙️ chore : 그 외 자잘한 수정에 대한 커밋
3- 🐞 fix : 버그 수정에 대한 커밋
4- 📖 docs : 문서 수정에 대한 커밋
5- 💅 style : 코드 스타일 혹은 포맷 등에 관한 커밋
6- ♻️ refactor : 코드 리팩토링에 대한 커밋
7- 🚦 test : 테스트 코드 수정에 대한 커밋
8- 🚀 CI : CI/CD
9- 🔖 Release : 제품 출시
10- 🎉 init : 최초 커밋
11- 🛠️ Config : 환경설정에 대한 커밋
12- 🦔 Revert : 리버트
```

## GitHub 컨벤션

- PR로만 merge 가능 + 리뷰 approval이 2개 이상이여야만 merge 승인
    
    ![https://user-images.githubusercontent.com/49011919/140630372-2f07e682-ea73-4137-b939-e8c758c70d47.PNG](https://user-images.githubusercontent.com/49011919/140630372-2f07e682-ea73-4137-b939-e8c758c70d47.PNG)
    
- 테스트를 모두 통과해야 merge가 가능 + 최신 상태의 develop 브랜치에만 merge 가능
    
    ![https://user-images.githubusercontent.com/49011919/140630396-fd4bcbfd-10a6-41f3-9749-22a980249b5b.PNG](https://user-images.githubusercontent.com/49011919/140630396-fd4bcbfd-10a6-41f3-9749-22a980249b5b.PNG)
    

## Jira 컨벤션

### 이슈 생성

백로그로 추가된 이슈는 단위를 작게 쪼개서 하위작업으로 추가

![image](https://user-images.githubusercontent.com/47075043/146881624-a62d5620-5dc2-4d56-8465-299309ad5f0d.png)

### 자동화

- 깃허브-지라 연동 스마트 커밋 기능 사용
- 에픽의 하위 작업이 완료되면, 상위 작업을 완료로 이동
- 깃허브 PR merge시 이슈 자동 완료 처리

![image](https://user-images.githubusercontent.com/47075043/146881722-52ae2bff-2663-4160-984c-394972aafaf3.png)

![image](https://user-images.githubusercontent.com/47075043/146881744-cee37f15-cdb7-4241-8d45-488974479f95.png)


# 😈 CI/CD

## CI : Github Action

java ci with gradle을 사용하여 지속적인 Build 프로세스 체크

## CD : Github Action + Docker

Github Action을 사용하여 Docker Image를 생성하고 Docker Hub로 push

그 후 Ec2에서 Docker Image를 pull 받아서 서버를 재실행하도록 구축

# 🔒 보안

### 인증 방법

Jwt 토큰 기반의 oauth2 카카오 소셜 로그인 구축

### 권한 모델

User : Group : Role 구조 채택

### https

https 및 도메인 적용


# ⚡API
[API-SPEC.md](API-SPEC.md) 참고
