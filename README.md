# spring-boot-jpa-proc
실전! 스프링 부트와 JPA 활용1 - 웹 애플리케이션 개발 - 강의 코드 연습

# 오류내역
## 2022-01-08
- EntityManager 테스트 실행시 오류
  ```
  No EntityManager with actual transaction available for current thread - cannot reliably process 'persist' call; nested exception is javax.persistence.TransactionRequiredException: No EntityManager with actual transaction available for current thread - cannot reliably process 'persist' call

    at
  ```
- EntityManager는 Transaction 상황에서만 실행가능함 Transaction 이 열리지 않으면 위와 같은 에러발생
- 매서드에 @Transactional 어노테이션 추가해 해결

## 2022-01-09
1. 문제상황
   - Book 상품 저장시 dtype 컬럼 Book으로 저장되는 오류 발생
   - 원인
     - Item 클래스에 @DiscriminatorColumn(name = "dtype") 형태로 정의시 상속 받은 클래스 에서 @DiscriminatorValue("M") 선언해 어떤값으로 dtype을 입력할지 지정해야하는데 해당소스 누락됨
   - 해결
     - @DiscriminatorValue("M") 코드 추가