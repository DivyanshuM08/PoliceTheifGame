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

    @Test
    fun testAssetLevelFilesExistAndParse() {
        var prevLength = 0f
        var prevSpeed = 0f
        var prevWidth = 999f

        for (i in 1..10) {
            val file = java.io.File("src/main/assets/levels/level_$i.json")
            val altFile = java.io.File("app/src/main/assets/levels/level_$i.json")
            val targetFile = if (file.exists()) file else altFile
            org.junit.Assert.assertTrue("File for level $i should exist at ${targetFile.absolutePath}", targetFile.exists())

            val json = targetFile.readText()
            val level = LevelData.fromJson(json)
            assertEquals("level_$i", level.levelId)
            org.junit.Assert.assertTrue("Title for level $i should not be blank", level.title.isNotBlank())
            org.junit.Assert.assertTrue("Path for level $i must have points", level.roadPath.size >= 7)
            org.junit.Assert.assertTrue("Boundaries for level $i must have points", level.boundaries.size >= 7)

            // Progression asserts across levels 1 to 10
            org.junit.Assert.assertTrue(
                "Level $i road length (${level.roadLengthMeters}m) must be greater than previous (${prevLength}m)",
                level.roadLengthMeters > prevLength
            )
            org.junit.Assert.assertTrue(
                "Level $i thief speed (${level.thiefSpeedMps}m/s) must be greater than previous (${prevSpeed}m/s)",
                level.thiefSpeedMps > prevSpeed
            )
            org.junit.Assert.assertTrue(
                "Level $i road width (${level.roadWidthMeters}m) must be narrower than previous (${prevWidth}m)",
                level.roadWidthMeters < prevWidth
            )

            prevLength = level.roadLengthMeters
            prevSpeed = level.thiefSpeedMps
            prevWidth = level.roadWidthMeters
        }

        // Specific checks for Level 1 and Level 10 extremes
        val level1 = LevelData.fromJson(
            (if (java.io.File("src/main/assets/levels/level_1.json").exists())
                java.io.File("src/main/assets/levels/level_1.json")
            else java.io.File("app/src/main/assets/levels/level_1.json")).readText()
        )
        val level10 = LevelData.fromJson(
            (if (java.io.File("src/main/assets/levels/level_10.json").exists())
                java.io.File("src/main/assets/levels/level_10.json")
            else java.io.File("app/src/main/assets/levels/level_10.json")).readText()
        )

        assertEquals(80.0f, level1.roadLengthMeters, 0.001f)
        assertEquals(8.0f, level1.thiefSpeedMps, 0.001f)
        assertEquals(12.0f, level1.roadWidthMeters, 0.001f)

        assertEquals(220.0f, level10.roadLengthMeters, 0.001f)
        assertEquals(31.0f, level10.thiefSpeedMps, 0.001f)
        assertEquals(6.0f, level10.roadWidthMeters, 0.001f)
        org.junit.Assert.assertTrue("Level 10 has more curves/waypoints than Level 1", level10.roadPath.size > level1.roadPath.size)
    }

    @Test
    fun testPuzzleAssetLevelFilesExistAndParse() {
        var prevSpeed = 0f
        for (i in 11..20) {
            val file = java.io.File("src/main/assets/levels/level_$i.json")
            val altFile = java.io.File("app/src/main/assets/levels/level_$i.json")
            val targetFile = if (file.exists()) file else altFile
            org.junit.Assert.assertTrue("File for puzzle level $i should exist at ${targetFile.absolutePath}", targetFile.exists())

            val json = targetFile.readText()
            val level = LevelData.fromJson(json)
            assertEquals("level_$i", level.levelId)
            org.junit.Assert.assertTrue("Title for level $i should not be blank", level.title.isNotBlank())
            org.junit.Assert.assertTrue("Level $i must be marked as puzzle", level.isPuzzle)
            org.junit.Assert.assertTrue("Level $i must have corridors", level.corridors.isNotEmpty())
            org.junit.Assert.assertNotNull("Level $i must have police start", level.policeStartPosition)
            org.junit.Assert.assertNotNull("Level $i must have thief start", level.thiefStartPosition)
            org.junit.Assert.assertNotNull("Level $i must have destination", level.destinationPosition)
            org.junit.Assert.assertTrue("Level $i must have thief route", level.thiefRoute.size >= 2)

            // Thief speed scales up across puzzle levels
            org.junit.Assert.assertTrue(
                "Level $i speed (${level.thiefSpeedMps}) must be greater than previous ($prevSpeed)",
                level.thiefSpeedMps > prevSpeed
            )
            prevSpeed = level.thiefSpeedMps
        }
    }
}


