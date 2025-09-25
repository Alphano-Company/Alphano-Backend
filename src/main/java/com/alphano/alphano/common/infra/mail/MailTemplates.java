package com.alphano.alphano.common.infra.mail;

public final class MailTemplates {

    // 제목 헬퍼
    public static String verificationSubject(String serviceName) {
        return serviceName + " 이메일 인증 코드";
    }

    // HTML 본문
    public static String verificationHtml(String code, String serviceName) {
        return """
            <div style="font-family:Arial,Helvetica,sans-serif;line-height:1.5">
              <h2>%s 이메일 인증</h2>
              <p>아래 인증 코드를 입력해 주세요.</p>
              <div style="font-size:32px;font-weight:700;letter-spacing:6px;margin:16px 0">%s</div>
              <p>유효시간은 5분입니다.</p>
              <hr/>
              <p style="color:#666">본 메일은 발신 전용입니다.</p>
            </div>
            """.formatted(serviceName, code);
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
