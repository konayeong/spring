package com.example.core.common.aop;

import com.example.core.command.MyCommands;
import com.example.core.common.exception.auth.NotLoggedInException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {
        "spring.shell.interactive.enabled=false",
        "logging.level.com.example=INFO"
})
@ExtendWith(OutputCaptureExtension.class)
class PriceAopTest {

    @Autowired
    private MyCommands myCommands;

    @BeforeEach
    void login() {
        myCommands.login(1L, "1");
    }

    @Test
    void loginCheck_true(CapturedOutput output) {
        myCommands.sector("진도군");
        assertTrue(output.getOut().contains("선도형"));
        assertTrue(output.getOut().contains("([진도군])"));
    }

    @Test
    void writeLog(CapturedOutput output) {
        myCommands.price("진도군", "산업용");
        assertTrue(output.getOut().contains("선도형"));
        assertTrue(output.getOut().contains("Price(id=194, city=진도군, sector=산업용, unitPrice=850)"));
    }

    @Test
    void loginCheck_false(CapturedOutput output) {
        myCommands.logout();

        assertThrows(NotLoggedInException.class, () -> myCommands.city());
        assertTrue(output.getOut().contains("[로그인 필요]"));
    }
}