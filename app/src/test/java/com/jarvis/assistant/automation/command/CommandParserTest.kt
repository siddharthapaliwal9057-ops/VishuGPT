package com.jarvis.assistant.automation.command

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CommandParserTest {

    @Test
    fun testAnswerCallCommands() {
        val testPhrases = listOf(
            "pick up call",
            "answer call",
            "call uthao",
            "phone uthao",
            "call pick karo",
            "call utha lo",
            "phone receive karo",
            "incoming call pick karo",
            "answer",
            "pick up"
        )

        for (phrase in testPhrases) {
            val result = CommandParser.parse(phrase)
            assertNotNull("Failed to parse: $phrase", result.command)
            assertTrue("Expected AnswerCall for '$phrase', got ${result.command}", result.command is JarvisCommand.AnswerCall)
            assertTrue("Expected high confidence for '$phrase'", result.confidence >= 0.90f)
        }
    }

    @Test
    fun testEndCallCommands() {
        val testPhrases = listOf(
            "end call",
            "cut call",
            "hang up",
            "reject call",
            "call cut karo",
            "call kaat do",
            "phone kaat do",
            "call end karo",
            "phone cut karo",
            "disconnect call",
            "call disconnect karo",
            "phone rakh do"
        )

        for (phrase in testPhrases) {
            val result = CommandParser.parse(phrase)
            assertNotNull("Failed to parse: $phrase", result.command)
            assertTrue("Expected EndCall for '$phrase', got ${result.command}", result.command is JarvisCommand.EndCall)
            assertTrue("Expected high confidence for '$phrase'", result.confidence >= 0.90f)
        }
    }

    @Test
    fun testScreenOffCommands() {
        val testPhrases = listOf(
            "screen off",
            "turn off screen",
            "turn screen off",
            "lock screen",
            "lock phone",
            "screen band karo",
            "screen band kar do",
            "screen lock karo",
            "display off",
            "phone lock karo"
        )

        for (phrase in testPhrases) {
            val result = CommandParser.parse(phrase)
            assertNotNull("Failed to parse: $phrase", result.command)
            assertTrue("Expected ScreenOff for '$phrase', got ${result.command}", result.command is JarvisCommand.ScreenOff)
            assertTrue("Expected high confidence for '$phrase'", result.confidence >= 0.90f)
        }
    }

    @Test
    fun testScreenOnCommands() {
        val testPhrases = listOf(
            "screen on",
            "turn on screen",
            "turn screen on",
            "wake screen",
            "wake up screen",
            "wake phone",
            "screen chalu karo",
            "screen on karo",
            "display on",
            "display chalu karo"
        )

        for (phrase in testPhrases) {
            val result = CommandParser.parse(phrase)
            assertNotNull("Failed to parse: $phrase", result.command)
            assertTrue("Expected ScreenOn for '$phrase', got ${result.command}", result.command is JarvisCommand.ScreenOn)
            assertTrue("Expected high confidence for '$phrase'", result.confidence >= 0.90f)
        }
    }
}
