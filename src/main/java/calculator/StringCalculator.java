package calculator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringCalculator {

  private static final Pattern CUSTOM_PATTERN = Pattern.compile("^//(.)\\n(.*)$", Pattern.DOTALL);

  private StringCalculator() {}
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

  /**
   * 콜론을 기준으로 문자열을 분리하는 메서드
   * 입력이 null/빈 문자열인 경우 빈 배열을 반환
   */
  static String[] splitByColon(String s) {
    if (s == null || s.isEmpty()) {
      return new String[0];
    }
    return s.split(":");
  }

  /**
   * 커스텀 구분자를 기준으로 문자열을 분리하는 메서드
   */
  static String[] splitByCustom(String input) {
    if (input == null) {
      throw new IllegalArgumentException("입력이 null입니다.");
    }
    Matcher m = CUSTOM_PATTERN.matcher(input);
    if (!m.matches()) {
      throw new IllegalArgumentException("커스텀 구분자 형식이 올바르지 않습니다.");
    }
    String delimiter = m.group(1);     // 한 글자
    String numbersPart = m.group(2);   // 본문

    if (numbersPart.isEmpty()) {
      return new String[0];
    }
    String regex = Pattern.quote(delimiter);
    return numbersPart.split(regex);
  }
}
