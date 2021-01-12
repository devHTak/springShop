#### 코로나 시대에 놀면 뭐하니 프로젝트 - Spring Shop

##### 0. Intro
간단한 쇼핑몰을 구현하면서 Spring Boot + JPA, Thymeleaf + JUnit5, Mock 활용한 프로젝트

##### 1. 도메인

- Account -> 업체, 고객을 나눠 ROLE을 따로 주어 처리
- Item -> 상품
- Orders -> 주문

##### 2. 기능

- 업체 -> 신규 상품 등록, 배송, 배송완료 확정 등의 기능을 구현했다.
- 고객 -> 상품 검색, 카트, 주문 등을 할 수 있다.

##### 3. 화면

- index -> 신규 상품, best 상품 조회한 화면
- login / sing-up / profile -> 업체, 고객을 나누어 로그인, 회원 가입 화면, 프로필 수정 화면
- new-product / update -> 신규 상품 등록 및 수정
- cart / order -> 상품 카트, 주문 화면
- search -> 상품명 또는 설명 검색 화면
