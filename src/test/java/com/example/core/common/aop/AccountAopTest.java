package com.example.core.common.aop;

import com.example.core.account.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {
        "spring.shell.interactive.enabled=false",
        "logging.level.com.example=INFO"
})
@ExtendWith(OutputCaptureExtension.class) // console : 로그/출력 캡처
class AccountAopTest {
    @Autowired
    private AuthenticationService authenticationService;

    @Test
    void loginLog(CapturedOutput output) {
        authenticationService.login(1L, "1");

        assertTrue(output.getAll().contains("login([1, 1])"));
    }

    @Test
    void logoutLog(CapturedOutput output) {
        authenticationService.login(1L, "1");
        authenticationService.logout();

        assertTrue(output.getAll().contains("logout([])"));
    }
}