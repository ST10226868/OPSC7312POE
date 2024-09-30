package com.example.opsc7312poe

import org.junit.Test
import org.junit.Assert.assertEquals

class HelloWorldTest {

    @Test
    fun testHelloWorld() {
        // This is a simple test that asserts the expected output.
        val message = "Hello World"
        println(message) // This will output "Hello World" to the test log
        assertEquals("Hello World", message) // This assertion will pass
    }
}
