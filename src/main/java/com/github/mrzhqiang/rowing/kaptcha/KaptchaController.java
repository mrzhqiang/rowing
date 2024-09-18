package com.github.mrzhqiang.rowing.kaptcha;

import com.github.mrzhqiang.helper.captcha.Captcha;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * 验证码控制器。
 */
@RestController
@RequestMapping("${rowing.kaptcha.path:/kaptcha}")
@RequiredArgsConstructor
public class KaptchaController {

    private final Captcha captcha;
    private final KaptchaProperties properties;

    @GetMapping
    public ResponseEntity<BufferedImage> get(HttpSession session) {
        if (!properties.getEnabled()) {
            return ResponseEntity.noContent().build();
        }

        var code = captcha.text();
        session.setAttribute(KaptchaProperties.KEY_SESSION_CODE, code);
        session.setAttribute(KaptchaProperties.KEY_SESSION_DATE, LocalDateTime.now());

        var headers = new HttpHeaders();
        // Set to expire far in the past
        headers.setExpires(0);
        // Set standard HTTP/1.1 no-cache headers.
        headers.setCacheControl(CacheControl.noStore().mustRevalidate());
        // Set standard HTTP/1.0 no-cache header.
        headers.setPragma("no-cache");
        headers.setContentType(MediaType.IMAGE_JPEG);
        BufferedImage image = captcha.image(code);
        return ResponseEntity.ok()
                .headers(headers)
                // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
                .header(HttpHeaders.CACHE_CONTROL, "post-check=0", "pre-check=0")
                .body(image);
    }

}
