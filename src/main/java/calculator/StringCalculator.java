package calculator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class StringCalculator {

  // 1) CRLF까지 허용: \r?\n
  private static final Pattern CUSTOM_PATTERN = Pattern.compile("^//(.)\\r?\\n(.*)$", Pattern.DOTALL);
  // 기본 구분자: 쉼표, 콜론, 개행
  private static final String DEFAULT_MIXED_REGEX = "[,:\\n]";

  private StringCalculator() {}

  // 필요 시 쓰는 전용 래퍼 (경고 제거용 + 명세 가독성)
  static String[] splitByComma(String s) {
    if (s == null || s.isEmpty()) return new String[0];
    return s.split(",");
  }

  static String[] splitByColon(String s) {
    if (s == null || s.isEmpty()) return new String[0];
    return s.split(":");
  }

  /**
   * 커스텀 구분자 파싱: //x\n 또는 //x\r\n 또는 //x\\n 모두 허용
   */
  static String[] splitByCustom(String input) {
    if (input == null) {
      throw new IllegalArgumentException("입력이 null입니다.");
    }

    // 우선 정규식으로 //x\r?\nbody 매칭
    Matcher m = CUSTOM_PATTERN.matcher(input);
    if (m.matches()) {
      String custom = m.group(1);
      String numbersPart = m.group(2);
      return splitNumbersWithCustom(custom, numbersPart);
    }

    // 2) 백업 경로: 리터럴 "\\n" 지원 (예: "//;\\n1;2;3")
    //    - 시작이 //, 최소 4글자 이상이어야 한다. (// + 구분자 1 + \ + n)
    if (input.startsWith("//") && input.length() >= 5 && input.charAt(2) != '\\') {
      char custom = input.charAt(2);

      // 다음이 리터럴 "\n" 인지 확인
      if (input.startsWith("\\n", 3)) {
        String numbersPart = input.substring(5); // "//" (0..1), custom(2), '\' (3), 'n'(4)
        return splitNumbersWithCustom(String.valueOf(custom), numbersPart);
      }
    }

    throw new IllegalArgumentException("커스텀 구분자 형식이 올바르지 않습니다.");
  }

  private static String[] splitNumbersWithCustom(String custom, String numbersPart) {
    if (numbersPart.isEmpty()) return new String[0];
    // 커스텀 또는 기본 구분자(, : \n) 모두 허용 (관대한 파서)
    String regex = "(?:" + Pattern.quote(custom) + "|,|:|\\n)";
    return numbersPart.split(regex);
  }

  static String[] tokenizeDefault(String s) {
    if (s == null || s.isEmpty()) return new String[0];

    boolean hasComma = s.indexOf(',') >= 0;
    boolean hasColon = s.indexOf(':') >= 0;
    boolean hasNewline = s.indexOf('\n') >= 0;

    if (hasComma && !hasColon && !hasNewline) return splitByComma(s);
    if (!hasComma && hasColon && !hasNewline) return splitByColon(s);

    return s.split(DEFAULT_MIXED_REGEX);
  }

  public static int add(String input) {
    if (input == null || input.isEmpty()) {
      return 0;
    }

    String[] tokens;
    if (input.startsWith("//")) {
      tokens = splitByCustom(input);
    } else {
      tokens = tokenizeDefault(input);
    }

    if (tokens.length == 0) return 0;

    long sum = 0L;
    for (int i = 0; i < tokens.length; i++) {
      tokens[i] = tokens[i].trim();             // 공백 무시
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