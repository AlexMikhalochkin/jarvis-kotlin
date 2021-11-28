package com.mega.demo

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.test.util.ReflectionTestUtils

/**
 * Verification for [DemoApplication].
 *
 * @author Alex Mikhalochkin
 */
class DemoApplicationTests {

    @Test
    fun testWebClient() {
        val demoApplication = DemoApplication()
        ReflectionTestUtils.setField(demoApplication, "yandexUrl", "http://test.test/test")
        assertNotNull(demoApplication.yandexWebClient())
    }
}
