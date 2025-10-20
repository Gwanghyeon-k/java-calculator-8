package calculator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class StringCalculator {

  private static final Pattern CUSTOM_PATTERN = Pattern.compile("^//(.)\\n(.*)$", Pattern.DOTALL);
  // 기본 구분자: 쉼표, 콜론, 개행 지원
  private static final String DEFAULT_MIXED_REGEX = "[,:\\n]";

  private StringCalculator() {}

  // 유지: 쉼표만
  static String[] splitByComma(String s) {
    if (s == null || s.isEmpty()) return new String[0];
    return s.split(",");
  }

  // 유지: 콜론만
  static String[] splitByColon(String s) {
    if (s == null || s.isEmpty()) return new String[0];
    return s.split(":");
  }

  // 커스텀: "//x\n" 형태 파싱
  static String[] splitByCustom(String input) {
    if (input == null) {
      throw new IllegalArgumentException("입력이 null입니다.");
    }
    Matcher m = CUSTOM_PATTERN.matcher(input);
    if (!m.matches()) {
      throw new IllegalArgumentException("커스텀 구분자 형식이 올바르지 않습니다.");
    }
    String custom = m.group(1);
    String numbersPart = m.group(2);

    if (numbersPart.isEmpty()) return new String[0];

    // 커스텀 또는 기본 구분자(, : \n) 모두 허용 → 관대한 파서
    String regex = "(?:" + Pattern.quote(custom) + "|,|:|\\n)";
    return numbersPart.split(regex);
  }

  // 기본 구분자 토크나이즈(쉼표/콜론/개행 모두)
  static String[] tokenizeDefault(String s) {
    if (s == null || s.isEmpty()) return new String[0];

    boolean hasComma = s.indexOf(',') >= 0;
    boolean hasColon = s.indexOf(':') >= 0;
    boolean hasNewline = s.indexOf('\n') >= 0;

    if (hasComma && !hasColon && !hasNewline) return splitByComma(s);
    if (!hasComma && hasColon && !hasNewline) return splitByColon(s);

    // 혼합이거나 개행 포함 → 혼합 정규식 사용
    return s.split(DEFAULT_MIXED_REGEX);
  }

  public static int add(String input) {
    if (input == null || input.isEmpty()) {
      return 0;
    }

    String[] tokens;
    if (input.startsWith("//")) {
      tokens = splitByCustom(input); // 커스텀 + 기본 혼용 허용
    } else {
      tokens = tokenizeDefault(input); // 기본(, : \n)
    }

    if (tokens.length == 0) return 0;

    long sum = 0L;
    for (int i = 0; i < tokens.length; i++) {
      // 공백 허용: 토큰마다 trim
      tokens[i] = tokens[i].trim();

      // 빈 토큰 방지 (연속 구분자/끝 구분자 등)
      if (tokens[i].isEmpty()) {
        throw new IllegalArgumentException("빈 토큰은 허용되지 않습니다.");
      }

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