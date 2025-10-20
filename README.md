# Java String Calculator

## 1. 각 기능

### 1.1 쉼표(,) 구분 메서드
- 쉼표(`,`)로 숫자를 분리하여 합계 계산
- 예시: `"1,2,3"` → `6`

### 1.2 콜론(:) 구분 메서드  
- 콜론(`:`)으로 숫자를 분리하여 합계 계산
- 예시: `"1:2:3"` → `6`

### 1.3 커스텀 구분 메서드
- `//구분자\n` 형식으로 사용자 정의 구분자 사용
- 예시: `"//;\n1;2;3"` → `6`

### 1.4 계산 메서드
- `StringCalculator.add()` 메서드로 모든 계산 통합 처리

## 2. 어떻게 구현할 것인지

### 2.1 구분자 파싱
```java
private Parsed parseDelimiters(String input) {
    // 커스텀 구분자 패턴 확인: //구분자\n숫자들
    // 기본 구분자: , 또는 :
}
```

### 2.2 문자열 분리
```java
private String buildSplitRegex(List<String> delimiters) {
    // 구분자들을 정규식으로 변환
    // 예: [",", ":"] → ",|:"
}
```

### 2.3 숫자 변환 및 검증
```java
private int parsePositiveInt(String token) {
    // 문자열을 정수로 변환
    // 음수 체크 → IllegalArgumentException
    // 숫자가 아닌 값 체크 → IllegalArgumentException
}
```

### 2.4 메인 계산 로직
```java
public int add(String input) {
    // 1. 빈 문자열 체크 → 0 반환
    // 2. 구분자 파싱
    // 3. 문자열 분리
    // 4. 각 토큰을 숫자로 변환
    // 5. 합계 계산 및 반환
}
```

## 실행 예시
```bash
./gradlew run
```

## 테스트 실행
```bash
./gradlew test
```