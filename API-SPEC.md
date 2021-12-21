## API 인증 통과하기

### 1. **oauth2 인증 요청하기**

로그인이 안된 사용자거나 처음 회원가입을 하는 사용자라면 다음의 링크로 소셜 로그인을 진행

```java
{서버주소}/oauth2/authorization/{sns타입}?redirect_uri={프론트가 토큰을 받을 페이지}

예시
[http://maenguin.iptime.org:8080/oauth2/authorization/kakao?redirect_uri=http://localhost:3000/oauth/redirect](http://maenguin.iptime.org:8080/oauth2/authorization/kakao?redirect_uri=http://localhost:3000/oauth/redirect)
```

사용자가 인증을 완료하면 redirect_url 명시한 주소로 쿼리스트링에 토큰이 포함되어 전달됨

토큰 쿼리 이름은 `token`

```html
예시
[http://localhost:3000/oauth/redirect](http://maenguin.iptime.org:8080/oauth2/authorization/kakao?redirect_uri=http://localhost:3000/oauth/redirect)?token=ekraweklfawneflkan.asdflkasndflksan.sadflkas
```

### 2. 토큰으로 api 요청하기

토큰을 받았으면 토큰을 헤더에 실어서 api를 요청할 수 있음

api 요청시 토큰이 없거나 잘못된 경우 `401에러`가 반환됨

```jsx
토큰 요청 예시
X-GOLDDDUCK-AUTH : Bearer {토큰}
```

- X-GOLDDDUCK-AUTH 라는 헤더에 담는다
- Bearer 다음에 한칸 띄우고 토큰을 담아서 보내야함

## 공통 응답

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| success | boolean | 이벤트 ID (이벤트코드아님니다!) |
| data | object | success true일때 응답 객체 포함 |
| error | object | success false일때 에러 객체 포함 |
| serverDateTime | string (yyyy-MM-dd HH:mm:ss) | 응답 서버 시간 |

**성공 예시(200)**

```java
{
	"success": true
  "data": {
    "content": "string",
    "giftType": "IMAGE",
    "id": 0,
    "used": true
  },
  "error": null
  "serverDateTime": "yyyy-MM-dd HH:mm:ss",
}
```

**실패 예시(400)**

```java
{
  "success": false,
  "data": null,
  "error": {
    "code": "E001", 
    "message": "해당 엔티티를 찾을 수 없습니다. name : com.dokev.gold_dduck.event.domain.Event, id : 0 "
  },
  "serverDateTime": "2021-12-09 19:41:11"
}
```

## 이벤트 생성

`POST api/v1/events`

**담당자**

**로니**

**요청**

Content-Type : `"multipart/form-data"`

| 필드명 | 데이터 타입 | 설명 | 비고 |
| --- | --- | --- | --- |
| title | string | 이벤트 제목 | NotNull |
| maxParticipantCount | number | 최대 참가자 수 | NotNull |
| startAt | string | 이벤트 시작 시간 | 현재 시간보다 미래 |
| endAt | string | 이벤트 종료 시간 | 현재 시간보다 미래 |
| giftChoiceType | string | 선물 선택 방식(FIFO, RANDOM) | NotNull |
| mainTemplate | string | 프론트에서 가지고 있는 기본 템플릿 | NotNull |
| memberId | number | 멤버 ID | NotNull |
| gifts | array | 선물 배열 | NotNull |

gifts

| 필드명 | 데이터 타입 | 설명 | 비고 |
| --- | --- | --- | --- |
| category | string | 선물 카테고리 | NotNull |
| giftItems | array | 선물 하위의 선물아이템 | NotNull |

giftItems

| 필드명 | 데이터 타입 | 설명 | 비고 |
| --- | --- | --- | --- |
| giftType | string | 선물 타입(IMAGE, TEXT) | NotNull |
| content | string | 선물 내용 | content, image 둘 중 하나는 Not null |
| image | file | 파일 |  |
|  |  |  |  |

```json
{
	"title": "event1"
	"maxParticipantCount": 10,
	"mainTemplate": "template1",
  "giftChoiceType": "FIFO",
  "gifts": [
    {
			"id": number,
      "category": "string",
      "giftItems": 
        {
          "content": "string",
          "giftType": "TEXT"
					"image" : null
        },
        {
          "content": null,
          "giftType": "IMAGE"
					"image" : file
        }
      ]
    }
  ],
 	"startAt": "2021-12-09T11:23:05.379Z",
  "endAt": "2021-12-09T11:23:05.379Z",
  "memberId": 0,
}
```


응답

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
|  | string(uuid) | 이벤트 코드 |
|  |  |  |

```json
"3fa85f64-5717-4562-b3fc-2c963f66afa6"
```

## 이벤트 조회

### 이벤트 단건 조회

`GET api/v1/events/{event-code}`

**담당자**

라엘

**요청**

Path

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| event-code | uuid | 이벤트 코드 |

```
/api/v1/events/3fa85f64-5717-4562-b3fc-2c963f66afa6
```

**응답**

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| success | boolean | 성공일 경우 true, 실패일 경우 false |
| data | object | success true일때 응답 객체 포함 |
| error | object | 성공일 경우 null입니다. |

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| eventId | number | 이벤트 ID |
| title | String | 이벤트 이름 |
| code | String(uuid) | 이벤트 코드 |
| startAt | String | 이벤트 시작 시간 |
| endAt | String | 이벤트 종료 시간 |
| eventProgressStatus | EventProgressStatus | 이벤트 진행 상태 |
| giftChoiceType | String | 선물 선택 방식(FIFO, RANDOM) |
| mainTemplate | String | 프론트에서 가지고 있는 기본 템플릿 |
| maxParticipantCount | number | 최대 참가자 수 |
| gifts | array | 선물 배열 |
| member | object | 멤버 객체 |

gifts

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| id | number | 선물 ID |
| category | string | 선물 카테고리 |
| itemCount | number | 선물 하위의 선물아이템 개수 |
| soldOut | boolean | 하위의 선물아이템 재고가 전부 소진 되었는지 여부 |
| giftItems | array | 선물 하위의 선물아이템 |

giftItems

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| id | number | 선물 아이템 ID |
| content | string | 선물 내용 |
| giftType | number | 선물 타입(IMAGE, TEXT, URL) |
| used | boolean | 선물 사용 여부 |

member

| 필드명 | 데이터 타입 | 설명 | 예시(현재 DB에 존재하는 데이터) |
| --- | --- | --- | --- |
| id | number | 멤버 ID | 12312321 |
| name | string | 이름(닉네임) | 문승희 |
| profileImage | string | 프로필 이미지 url | kdsjdkjkd |
| socialId | string | sns ID | munshee |
| email | string | 이메일 | munshee@naver.com |

```json
{
	"eventId": 0,
  "code": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
	"startAt": "2021-12-09T10:53:39.275Z" 
  "endAt": "2021-12-09T10:53:39.274Z",
  "eventProgressStatus": "string",  
  "giftChoiceType": "FIFO",
	"mainTemplate": "string",
  "maxParticipantCount": 0,
  "gifts": [
    {
			"id": 0,
      "category": "string",
			"itemCount": 0
      "giftItems": [
        {
					"id": 0,
          "content": "string",
          "giftType": "IMAGE",
          "used": true
        }
      ],
    }
  ],
  "member": {
    "email": "string",
    "id": 0,
    "name": "string",
    "profileImage": "string",
    "socialId": "string"
  },
  
}
```

### 이벤트 전체 조회 - 생성일 DESC

`GET /api/v1/members/{memberId}/events`

**담당자**

맹귄

**요청**

Path

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| memberId | number | 멤버 ID |

Query

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| eventProgressStatus | string | 이벤트 진행 상태
READY - 이벤트 준비중,
RUNNING - 이벤트 진행중
CLOSED - 이벤트 종료
값을 안보낼 경우 전체 조회 |
| page | number | 요청 페이지 번호
- 0 부터 시작
- 값을 안보낼 경우 기본값 0 |
  | size | number | 요청 페이지 사이즈
- 값을 안보낼 경우 기본값 4 |

```
/api/v1/members/1/events?eventProgressStaus=CLOSED&page=0&size=4
```

**응답**

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| pagination | object | 페이징 정보 |
| simpleEventList | array | 이벤트 리스트 |

pagination

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| currentPage | number | 현재 페이지 번호 |
| offset | number | 페이지 오프셋 |
| size | number | 페이지 사이즈 |
| totalElements | number | 총 요소 개수 |
| totalPages | number | 총 페이지 수 |

simpleEventList

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| eventId | number | 이벤트 ID |
| code | string(uuid) | 이벤트 코드 |
| startAt | string | 이벤트 시작 시간 |
| endAt | string | 이벤트 종료 시간 |
| eventProgressStatus | EventProgressStatus | 이벤트 진행 상태 |
| giftChoiceType | string | 선물 선택 방식(FIFO, RANDOM) |
| mainTemplate | string | 프론트에서 가지고 있는 기본 템플릿 |
| maxParticipantCount | number | 최대 참가자 수 |
| title | string | 이벤트 타이틀 |
| createdAt | string(Date) | 이벤트 생성 시간 |

```java
{
    "pagination": {
      "currentPage": 0,
      "offset": 0,
      "size": 0,
      "totalElements": 0,
      "totalPages": 0
    },
    "simpleEventList": [
      {
        "code": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
        "createdAt": "string",
        "endAt": "string",
        "eventId": 0,
        "eventProgressStatus": "CLOSED",
        "giftChoiceType": "FIFO",
        "mainTemplate": "string",
        "maxParticipantCount": 0,
        "startAt": "string",
        "title": "string"
      }
    ]
  }
```

## 선물 수령

### 선착순

`POST api/v1/gifts/fifo`

**담당자**

맹귄

**에러코드**

| 코드 | 메시지 | 사유 |
| --- | --- | --- |
| E002 | 이미 참여한 이벤트 입니다. | 멤버가 이미 이벤트에 참여한 경우 |
| G002 | 선물의 재고가 전부 소진되었습니다. | 선물 카테고리 하위의 선물아이템이 전부 소진된 경우 |
| E003 | 종료된 이벤트 입니다. | 이벤트가 종료된 경우 |

**요청**

RequestBody

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| eventId | number | 이벤트 ID (이벤트코드아님니다!) |
| giftId | number | 선물 ID(카테고리ID) |
| memberId | number | 멤버 ID |

```java
{
  "eventId": 0,
  "giftId": 0,
  "memberId": 0
}
```

**응답**

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| id | number | 선물아이템 ID |
| content | string | 선물 내용 |
| giftType | string | 선물 타입 (IMAGE, TEXT) |
| used | boolean | 선물 사용 여부 |
| category | string | 선물 카테고리 |
| mainTemplate | string | 프론트에서 가지고 있는 기본 템플릿 |
| sender | string | 선물을 준 사람의 name (추가 희망 - 프론트) |

```java
{
  "category": "string",
  "content": "string",
  "giftType": "IMAGE",
  "id": 0,
  "used": true,
	"mainTemplate": "string"
}
```

### 랜덤

`POST api/v1/gifts/random`

**담당자**

맹귄

**에러코드**

| 코드 | 메시지 | 사유 |
| --- | --- | --- |
| E002 | 이미 참여한 이벤트 입니다. | 멤버가 이미 이벤트에 참여한 경우 |
| E003 | 종료된 이벤트 입니다. | 이벤트가 종료된 경우 |
| G004 | 꽝을 뽑았습니다. |  |

**요청**

RequestBody

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| eventId | number | 이벤트 ID (이벤트코드아님니다!) |
| memberId | number | 멤버 ID |

```java
{
  "eventId": 0,
  "memberId": 0
}
```

**응답**

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| id | number | 선물아이템 ID |
| content | string | 선물 내용 |
| giftType | string | 선물 타입 (IMAGE, TEXT) |
| used | boolean | 선물 사용 여부 |
| category | string | 선물 카테고리 |
| mainTemplate | string | 프론트에서 가지고 있는 기본 템플릿 |
| sender | string | 선물을 준 사람의 name (추가 희망 - 프론트) |

```java
{
  "category": "string",
  "content": "string",
  "giftType": "IMAGE",
  "id": 0,
  "used": true,
	"mainTemplate": "string"
}
```

## 선물 조회

### 선물 전체 조회 - 수령일 DESC

`GET /api/v1/members/{memberId}/gitfs`

**담당자**

맹귄

**요청**

Path

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| memberId | number | 멤버 ID |

Query

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| used | boolean | 선물 사용 여부
값을 안보낼 경우 전체 조회 |
| page | number | 요청 페이지 번호
- 0 부터 시작
- 값을 안보낼 경우 기본값 0 |
  | size | number | 요청 페이지 사이즈
- 값을 안보낼 경우 기본값 4 |

```
/api/v1/members/1/gifts?used=&page=0&size=4
```

**응답**

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| pagination | object | 페이징 정보 |
| giftItemList | array | 선물 리스트 |

pagination

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| currentPage | number | 현재 페이지 번호 |
| offset | number | 페이지 오프셋 |
| size | number | 페이지 사이즈 |
| totalElements | number | 총 요소 개수 |
| totalPages | number | 총 페이지 수 |

giftItemList

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| id | number | 선물아이템 ID |
| content | string | 선물 내용 |
| giftType | string | 선물 타입 (IMAGE, TEXT) |
| used | boolean | 선물 사용 여부 |
| category | string | 선물 카테고리 |
| mainTemplate | string | 프론트에서 가지고 있는 기본 템플릿 |
| sender | string | 선물을 준 사람의 name (추가 희망 - 프론트) |

```java
{
	"pagination": {
    "currentPage": 0,
    "offset": 0,
    "size": 0,
    "totalElements": 0,
    "totalPages": 0
  },
  "giftItemList": [
    {
      "category": "string",
      "content": "string",
      "giftType": "IMAGE",
      "id": 0,
      "used": true,
			"mainTemplate": "string"
    }
  ]
}
```

## 선물 변경

`PATCH /api/v1/members/{memberId}/giftItems/{giftItemId}`

**담당자**

맹귄

**요청**

Path

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| memberId | number | 멤버 ID |
| giftItemId | number | 선물 아이템 ID |

RequestBody

| 필드명 | 데이터 타입 | 설명 | 필수 |
| --- | --- | --- | --- |
| used | boolean | 선물 사용 여부 | true |

**응답**

성공시 응답 data Object 필드 null

```java
{
	"success": true
  "data": null,
  "error": null
  "serverDateTime": "yyyy-MM-dd HH:mm:ss",
}
```

## 유저 정보 조회

`GET /api/v1/members/me`

**담당자**

맹귄

**요청**

유저 정보는 요청시 별도의 데이터 없이 토큰만 보내면 됩니다.

**응답**

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| id | number | 멤버 ID |
| name | string | 이름(닉네임) |
| profileImage | string | 프로필 이미지 url |
| socialId | string | 삭제 예정 |
| email | string | 정보 없음(null) |

```json
{
  "email": "string",
  "id": 0,
  "name": "string",
  "profileImage": "string",
  "socialId": "string"
}
```

## 이벤트 당첨자 조회

`GET /api/v1/members/{memberId}/{eventId}/winners`

**담당자**

라엘

**요청**

Path

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| memberId | number | 멤버 ID |
| eventId | number | 이벤트 ID(Code가 아닙니다.) |

```
/api/v1/members/1/1/winners
```

**응답**

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| success | boolean | 성공일 경우 true, 실패일 경우 false |
| data | array | success true일때 응답 객체 포함 |
| error | object | 성공일 경우 null입니다. |
- **data**

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| category | String | 선물 카테고리 |
| winners | array | 해당 카테고리에 선물을 받은 멤버가 객체 리스트 형태로 주어집니다. |
- **winners**

| 필드명 | 데이터 타입 | 설명 | 예시(현재 DB에 존재하는 데이터) |
| --- | --- | --- | --- |
| id | number | 멤버 ID |  |
| name | string | 이름(닉네임) |  |
| email | string | 이메일 |  |
| profileImage | string | 프로필 이미지 url |  |
| socialId | string | sns ID |  |

## 이벤트 삭제

`DELETE api/v1/members/{memberId}/events/{eventId}`

**담당자**

라엘

**요청**

Path

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| memberId | number | 멤버 ID |
| eventId | number | 이벤트 ID(Code가 아닙니다.) |

```
/api/v1/members/1/events/1
```

**응답**

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| success | boolean | 성공일 경우 true, 실패일 경우 false |
| data | number | success true일때 응답 객체 포함 |
| error | object | 성공일 경우 null입니다. |
- **data**

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| eventId | number | 삭제된 이벤트 id |

## 선물 아이템 단일 조회(제거 예정)

`GET api/v1/members/{memberId}/giftItems/{giftItemId}`

**담당자**

라엘

**요청**

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| memberId | number | 멤버 ID |
| giftItemId | number | 선물아이템 ID |

```
/api/v1/members/1/giftItems/1
```

## 선물 아이템 단일 조회 v2

`GET api/v2/giftItems/{giftItemId}`

**요청**

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| giftItemId | number | 선물아이템 ID |

**응답**

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| success | boolean | 성공일 경우 true, 실패일 경우 false |
| data | object | success true일때 응답 객체 포함 |
| error | object | 성공일 경우 null입니다. |

data

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| id | number | 선물 아이템 ID |
| content | string | 선물 내용 |
| giftType | string | 선물 타입 (IMAGE, TEXT) |
| used | boolean | 선물 사용 여부 |
| category | string | 선물 카테고리 |
| mainTemplate | string | 프론트에서 가지고 있는 기본 템플릿 |
| sender | string | 선물을 준 사람의 name |
| receivedDate | string | 선물을 받은(당첨된) 날짜 |

페이징 정보(동기화용)

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| currentPage | number | 현재 페이지 번호 |
| offset | number | 페이지 오프셋 |
| size | number | 페이지 사이즈 |
| totalElements | number | 총 요소 개수 |
| totalPages | number | 총 페이지 수 |

기프트 아이템 정보(동기화용)

| 필드명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| id | number | 선물아이템 ID |
| content | string | 선물 내용 |
| giftType | string | 선물 타입 (IMAGE, TEXT) |
| used | boolean | 선물 사용 여부 |
| category | string | 선물 카테고리 |
| mainTemplate | string | 프론트에서 가지고 있는 기본 템플릿 |
| sender | string | 선물을 준 사람의 name (추가 희망 - 프론트) |
