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