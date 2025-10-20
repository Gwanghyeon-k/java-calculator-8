package calculator;

public final class InputValidation {
  private InputValidation() {}

  static void requireNumberToken(String s) {

    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c < '0' || c > '9') {
        throw new IllegalArgumentException("숫자가 아닌 값이 포함되어 있습니다: " + s);
      }
    }
  }

  static int parsePositive(String s) {
    try {
      int v = Integer.parseInt(s);
      if (v <= 0) {
        throw new IllegalArgumentException("양수만 입력 가능합니다: " + v);
      }
      return v;
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("정수 변환에 실패했습니다: " + s);
    }
  }
}
