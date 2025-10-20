package calculator;

public class StringCalculator {

  /**
   * 쉼표를 기준으로 문자열을 분리하는 메서드
   * 입력이 null/빈 문자열인 경우 빈 배열을 반환
   */
  static String[] splitByComma(String s) {
    if (s == null || s.isEmpty()) {
      return new String[0];
    }
    return s.split(",");
  }
}
