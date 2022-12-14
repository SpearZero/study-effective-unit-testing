package exam.testing.chap01;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    static StringUtils stringUtils;

    @BeforeAll
    static void setUp() {
        stringUtils = new StringUtils();
    }

    @DisplayName("StringUtils에서 returnExam을 호출하면 Exam을 반환한다")
    @Test
    void when_string_utils_call_return_exam_then_return_exam() {
        String expect = "Exam";
        String result = stringUtils.returnExam();

        assertEquals(expect, result);
    }
}