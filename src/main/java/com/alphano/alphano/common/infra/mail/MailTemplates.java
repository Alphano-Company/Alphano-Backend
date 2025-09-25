package com.alphano.alphano.common.infra.mail;

public final class MailTemplates {

    // 제목 헬퍼
    public static String verificationSubject(String serviceName) {
        return "[" + serviceName + "] 회원가입 이메일 인증";
    }

    // HTML 본문
    public static String verificationHtml(String code, String serviceName) {
        return """
<!DOCTYPE html>
<html lang="ko">
<body style="margin:0;padding:0;background:#f6f8fa;font-family:Arial,Helvetica,'Apple SD Gothic Neo','Malgun Gothic',sans-serif;">
  <div style="padding:24px 12px;background:#f6f8fa;">
    <div style="max-width:560px;width:100%%;margin:0 auto;background:#ffffff;">
      <div style="background:#0C1015;color:#ffffff;text-align:center;padding:18px 20px;
                  font-weight:800;font-size:18px;letter-spacing:1px;">
        %2$s
      </div>

      <div style="padding:24px 28px;color:#222222;">
        <div style="font-size:18px;font-weight:800;margin:0 0 8px;color:#292929;">메일 인증</div>
        <p style="margin:0 0 14px;font-size:14px;line-height:1.7;color:#222222;">
          안녕하세요, %2$s입니다.<br>
          아래 코드를 입력하여 회원가입을 마무리해 주세요.
        </p>

        <div style="text-align:center;margin:40px 0 20px;">
          <p style="font-size:14px;color:#222222;margin:0 0 6px;font-weight:500;">확인 코드</p>
          <div style="font-size:30px;line-height:1;font-weight:700;color:#000000;">%1$s</div>
          <p style="margin:12px 0 0;font-size:12px;color:#292929;">
            (이 코드는 전송 5분 후에 만료됩니다.)
          </p>
        </div>
      </div>
    </div>
  </div>
</body>
</html>
""".formatted(code, serviceName);
    }


    // Plain text 대체 본문
    public static String verificationText(String code, String serviceName) {
        return """
            [%s 이메일 인증]
            인증 코드: %s
            유효시간: 5분
            본 메일은 발신 전용입니다.
            """.formatted(serviceName, code);
    }
}
