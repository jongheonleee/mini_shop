# 스프링 & JPA 연습용 프로젝트 - 미니 쇼핑몰

### 📌 스터디 목표
> - 해당 규모의 프로젝트 하루만에 다 구현해보기
> - 그러기 위해서는 해당 프로젝트 구현을 반복 숙달해야함!!
> - 이를 통해, 스프링, 스프링부트, JPA에 익숙해지기 

### 프로젝트 사용 라이브러리 및 기술 
- spring boot : 3.2.x
- java : 17 
- maven 
- lomnok, thymeleaf, spring data jpa, spring web, mysql driver, h2 database

## 1. ERD 

<img src="https://github.com/user-attachments/assets/bb1af29b-bd0b-4920-9cfe-49e8936f72a0" width="800" height="500"/>

## 2. API 명세서 

<img src="https://github.com/user-attachments/assets/5b1e6c2d-682a-470c-839d-5331d4ab4139" width="800" height="500"/>


## 3. 기능 요구 사항 및 페이지 처리 과정 

<br>

> - 1️⃣ 회원
> - 2️⃣ 상품
> - 3️⃣ 주문
> - 4️⃣ 장바구니 

<br>

## 1️⃣ 회원

> ### 3 가지 요구사항 존재
> ### - 1. 회원등록(회원가입) 
> ### - 2. 로그인
> ### - 3. 로그아웃 

<br>

### 1. 회원등록(회원가입) 처리 흐름

<img src="https://github.com/user-attachments/assets/ad731848-77f7-4522-9431-77217e133cc0" width="800" height="500"/>


### 2. 로그인 처리 흐름

<img src="https://github.com/user-attachments/assets/8865b709-bc92-45b2-95bd-9c1407215b7d" width="800" height="500"/>



### 3. 로그아웃 처리 흐름

<img src="https://github.com/user-attachments/assets/6d1f7114-702a-4d91-97a6-76f9caefa65b" width="800" height="500"/>



## 2️⃣ 상품

> ### 3 가지 요구사항 존재
> ### - 1. 상품 등록 : 관리자가 상품을 등록하는 기능 -> 상품 등록 페이지, 상품 등록 처리 
> ### - 2. 상품 관리 : 관리란? 기존의 등록된 상품의 정보를 '수정'하는 것을 의미함 -> 관리자 페이지에서 모든 상품 조회, 특정 상품 관리 페이지, 특정 상품 관리 처리 
> ### - 3. 상품 구매 : 회원이 상품을 상세 페이지에서 구매하는 것을 의미 -> 구매 경로 : (1) 장바구니 담기, (2) 주문하기 -> 비동기통신(ajax) 기반으로 처리 

<br>

### 1. 상품 등록 : 관리자가 상품을 등록하는 기능

<br>

- 상품 등록 처리 과정은 크게 2가지 부분을 봐야함
  - (1) 인증/인가 처리 : 상품 등록 폼 페이지 접근
  - (2) 상품 등록 처리 : 상품 필드가 유효한지, 상품 등록 중에 에러가 발생하지 않았는지 확인 

<br>

#### 1-1. 인증/인가 처리


<img src="https://github.com/user-attachments/assets/a258583a-54cc-47a3-aa16-87c984467768" width="800" height="500"/>

#### 1-2. 상품 등록 처리

<img src="https://github.com/user-attachments/assets/1ede9ad0-f627-490b-9657-16c38f416a93" width="800" height="500"/>

### 2. 상품 관리 : 관리란? 기존의 등록된 상품의 정보를 '수정'하는 것을 의미함

<br>

- 상품 관리(수정) 과정은 크게 2가지 부분을 봐야함
    - (1) 인증/인가 처리 : 상품 등록 폼 페이지 접근
    - (2) 상품 관리(수정) 처리 : 상품 필드가 유효한지, 상품 관리(수정) 중에 에러가 발생하지 않았는지 확인

<br>

#### 2-1. 인증/인가 처리


<img src="https://github.com/user-attachments/assets/b40391a1-13dd-4c97-90aa-3797d4d3f818" width="800" height="500"/>

#### 2-2. 상품 관리(수정) 처리


<img src="https://github.com/user-attachments/assets/fe0741f6-40c6-4d74-ac2f-701d04e7ad55" width="800" height="500"/>


### 3. 상품 구매 : 회원이 상품을 상세 페이지에서 구매하는 것을 의미

<br>

- 상품 구매 경로는 크게 2가지가 있음
    - (1) 장바구니 둥록 : 장바구니에서 처리 
    - (2) 바로 구매 : 주문에서 처리 

<br>

