package com.example.policetheifgame

import com.example.policetheifgame.game.geometry.LevelData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class LevelDataTest {

    @Test
    fun testLevelJsonParsing() {
        val json = """
        {
          "levelId": "test_level",
          "roadLengthMeters": 100.0,
          "roadWidthMeters": 8.0,
          "startPositionMeters": 0.0,
          "finishPositionMeters": 100.0,
          "roadPath": [
            { "x": 50.0, "y": 0.0 },
            { "x": 50.0, "y": 10.0 },
            { "x": 55.0, "y": 20.0 }
          ],
          "boundaries": [
            { "left": [45.0, 0.0], "right": [55.0, 0.0] },
            { "left": [45.0, 10.0], "right": [55.0, 10.0] },
            { "left": [50.0, 20.0], "right": [60.0, 20.0] }
          ]
        }
        """.trimIndent()

        val level = LevelData.fromJson(json)
        assertEquals("test_level", level.levelId)
        assertEquals(100.0f, level.roadLengthMeters, 0.001f)
        assertEquals(3, level.roadPath.size)
        assertEquals(3, level.boundaries.size)
        assertEquals(50.0f, level.roadPath[0].x, 0.001f)
        assertEquals(45.0f, level.boundaries[0].left.x, 0.001f)
    }
}
