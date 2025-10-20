package calculator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringCalculator {

  private static final Pattern CUSTOM_PATTERN = Pattern.compile("^//(.)\\n(.*)$", Pattern.DOTALL);
  private static final String DEFAULT_MIXED_REGEX = "[,:]";

  private StringCalculator() {
  }

  /**
   * 쉼표를 기준으로 문자열을 분리하는 메서드 입력이 null/빈 문자열인 경우 빈 배열을 반환
   */
  static String[] splitByComma(String s) {
    if (s == null || s.isEmpty()) {
      return new String[0];
    }
    return s.split(",");
  }

  /**
   * 콜론을 기준으로 문자열을 분리하는 메서드 입력이 null/빈 문자열인 경우 빈 배열을 반환
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

  static String[] tokenizeDefault(String s) {
    if (s == null || s.isEmpty())
      return new String[0];

    boolean hasComma = s.indexOf(',') >= 0;
    boolean hasColon = s.indexOf(':') >= 0;

    if (hasComma && !hasColon)
      return splitByComma(s);
    if (!hasComma && hasColon)
      return splitByColon(s);
    if (hasComma && hasColon)
      return s.split(DEFAULT_MIXED_REGEX);

    // 구분자 없음 → 단일 토큰 반환
    return new String[]{s};
  }

  public static int add(String input) {
    if (input == null || input.isEmpty()) {
      return 0;
    }

    String[] tokens;
    if (input.startsWith("//")) {
      tokens = splitByCustom(input); // 커스텀 형식 검증 포함
    } else {
      tokens = tokenizeDefault(input); // ← 여기서 쉼표/콜론 전용 메서드들이 실제로 사용됨
    }

    if (tokens.length == 0)
      return 0;

    // (원래대로) 검증 + 합산
    long sum = 0L;
    for (int i = 0; i < tokens.length; i++) {
      // 선택: 공백 허용 시 tokens[i] = tokens[i].trim();
      InputValidation.requireNumberToken(tokens[i]);
      int value = InputValidation.parsePositive(tokens[i]);
      sum += value;
      if (sum > Integer.MAX_VALUE) {
        throw new IllegalArgumentException("합이 int 범위를 초과했습니다.");
      }
    }
    return (int) sum;
  }
}
