package com.example.policetheifgame.game.geometry

import com.example.policetheifgame.game.model.Point2D
import kotlin.math.atan2
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * Result of projecting an arbitrary world point onto the road geometry.
 */
data class RoadProjection(
    val distanceAlongRoadMeters: Float,
    val closestPointOnCenterline: Point2D,
    val lateralOffsetMeters: Float,
    val isOnRoad: Boolean
)

/**
 * Represents the playable road geometry in world coordinates (meters).
 * Decoupled from screen resolution and visual rendering.
 */
class RoadGeometry(
    val levelData: LevelData
) {
    val levelId: String get() = levelData.levelId
    val roadLengthMeters: Float get() = levelData.roadLengthMeters
    val roadWidthMeters: Float get() = levelData.roadWidthMeters
    val startPositionMeters: Float get() = levelData.startPositionMeters
    val finishPositionMeters: Float get() = totalPathLengthMeters

    val pathPoints: List<Point2D> = levelData.roadPath
    val boundaries: List<RoadBoundary> = levelData.boundaries

    val minX: Float = (boundaries.flatMap { listOf(it.left.x, it.right.x) } + pathPoints.map { it.x }).minOrNull() ?: 40f
    val maxX: Float = (boundaries.flatMap { listOf(it.left.x, it.right.x) } + pathPoints.map { it.x }).maxOrNull() ?: 85f
    val centerX: Float get() = (minX + maxX) / 2f
    val centerY: Float get() = (pathPoints.first().y + pathPoints.last().y) / 2f

    // Cumulative distances along centerline vertices
    private val cumulativeDistances: FloatArray

    init {
        require(pathPoints.size >= 2) { "Road must have at least 2 path points" }
        cumulativeDistances = FloatArray(pathPoints.size)
        cumulativeDistances[0] = 0f
        for (i in 0 until pathPoints.size - 1) {
            val segLen = pathPoints[i].distanceTo(pathPoints[i + 1])
            cumulativeDistances[i + 1] = cumulativeDistances[i] + segLen
        }
    }

    /**
     * Total physical arc length of the centerline in meters.
     */
    val totalPathLengthMeters: Float
        get() = cumulativeDistances.last()

    /**
     * Interpolates the road center coordinate at a given distance along the road (in meters).
     */
    fun getRoadCenterAtDistance(distanceMeters: Float): Point2D {
        val clampedDist = distanceMeters.coerceIn(0f, totalPathLengthMeters)

        // Binary search or linear search for the segment
        for (i in 0 until cumulativeDistances.size - 1) {
            val s0 = cumulativeDistances[i]
            val s1 = cumulativeDistances[i + 1]
            if (clampedDist in s0..s1 || i == cumulativeDistances.size - 2) {
                val segLen = s1 - s0
                val t = if (segLen > 1e-4f) (clampedDist - s0) / segLen else 0f
                return pathPoints[i].lerp(pathPoints[i + 1], t)
            }
        }
        return pathPoints.last()
    }

    /**
     * Returns the tangent heading angle in degrees at a given distance.
     * 0 degrees = pointing upwards along +Y axis.
     * Positive degrees = turning towards right (+X axis).
     */
    fun getRoadHeadingAtDistance(distanceMeters: Float): Float {
        val clampedDist = distanceMeters.coerceIn(0f, totalPathLengthMeters)
        for (i in 0 until cumulativeDistances.size - 1) {
            val s0 = cumulativeDistances[i]
            val s1 = cumulativeDistances[i + 1]
            if (clampedDist in s0..s1 || i == cumulativeDistances.size - 2) {
                val dx = pathPoints[i + 1].x - pathPoints[i].x
                val dy = pathPoints[i + 1].y - pathPoints[i].y
                // Math.atan2(dx, dy) returns angle in radians from +Y axis towards +X axis
                val radians = atan2(dx.toDouble(), dy.toDouble())
                return Math.toDegrees(radians).toFloat()
            }
        }
        return 0f
    }

    /**
     * Returns the interpolated left and right boundaries at a given distance along the road.
     */
    fun getRoadBoundariesAtDistance(distanceMeters: Float): Pair<Point2D, Point2D> {
        if (boundaries.size == pathPoints.size && boundaries.isNotEmpty()) {
            val clampedDist = distanceMeters.coerceIn(0f, totalPathLengthMeters)
            for (i in 0 until cumulativeDistances.size - 1) {
                val s0 = cumulativeDistances[i]
                val s1 = cumulativeDistances[i + 1]
                if (clampedDist in s0..s1 || i == cumulativeDistances.size - 2) {
                    val segLen = s1 - s0
                    val t = if (segLen > 1e-4f) (clampedDist - s0) / segLen else 0f
                    val left = boundaries[i].left.lerp(boundaries[i + 1].left, t)
                    val right = boundaries[i].right.lerp(boundaries[i + 1].right, t)
                    return Pair(left, right)
                }
            }
            return Pair(boundaries.last().left, boundaries.last().right)
        }

        // Fallback: normal offset based on roadWidth
        val center = getRoadCenterAtDistance(distanceMeters)
        val headingDeg = getRoadHeadingAtDistance(distanceMeters)
        val rad = Math.toRadians(headingDeg.toDouble())
        val normalX = Math.cos(rad).toFloat()
        val normalY = -Math.sin(rad).toFloat()
        val halfW = roadWidthMeters / 2f
        val left = Point2D(center.x - normalX * halfW, center.y - normalY * halfW)
        val right = Point2D(center.x + normalX * halfW, center.y + normalY * halfW)
        return Pair(left, right)
    }

    /**
     * Checks whether a world position is within the drivable road boundaries,
     * with an optional tolerance in meters.
     */
    fun isPositionOnRoad(point: Point2D, toleranceMeters: Float = 0.5f): Boolean {
        // Quick bounding box check
        if (boundaries.size >= 2) {
            // Check against segment quadrilaterals
            for (i in 0 until boundaries.size - 1) {
                val b0 = boundaries[i]
                val b1 = boundaries[i + 1]
                if (isPointInOrNearQuad(point, b0.left, b0.right, b1.right, b1.left, toleranceMeters)) {
                    return true
                }
            }
            return false
        }

        // Fallback using projection distance to centerline
        val proj = getNearestRoadPosition(point)
        val halfW = roadWidthMeters / 2f + toleranceMeters
        return Math.abs(proj.lateralOffsetMeters) <= halfW &&
                proj.distanceAlongRoadMeters >= -toleranceMeters &&
                proj.distanceAlongRoadMeters <= totalPathLengthMeters + toleranceMeters
    }

    /**
     * Projects an arbitrary point onto the road centerline, returning the distance along the road,
     * lateral offset, closest point, and whether it is on the road.
     */
    fun getNearestRoadPosition(point: Point2D): RoadProjection {
        var minDistanceSq = Float.MAX_VALUE
        var bestCenterPoint = pathPoints.first()
        var bestDistanceAlongRoad = 0f
        var bestLateralOffset = 0f

        for (i in 0 until pathPoints.size - 1) {
            val p0 = pathPoints[i]
            val p1 = pathPoints[i + 1]
            val segVec = p1 - p0
            val segLenSq = segVec.lengthSquared()

            val t = if (segLenSq > 1e-6f) {
                val ptVec = point - p0
                (ptVec.dot(segVec) / segLenSq).coerceIn(0f, 1f)
            } else {
                0f
            }

            val projectedPoint = p0 + segVec * t
            val distSq = point.distanceSquaredTo(projectedPoint)

            if (distSq < minDistanceSq) {
                minDistanceSq = distSq
                bestCenterPoint = projectedPoint
                val segLen = sqrt(segLenSq)
                bestDistanceAlongRoad = cumulativeDistances[i] + t * segLen

                // Compute cross product to determine signed lateral offset (left: negative, right: positive)
                val cross = segVec.x * (point.y - p0.y) - segVec.y * (point.x - p0.x)
                val dist = sqrt(distSq)
                bestLateralOffset = if (cross >= 0f) -dist else dist
            }
        }

        val onRoad = isPositionOnRoad(point, toleranceMeters = 0.5f)
        return RoadProjection(
            distanceAlongRoadMeters = bestDistanceAlongRoad,
            closestPointOnCenterline = bestCenterPoint,
            lateralOffsetMeters = bestLateralOffset,
            isOnRoad = onRoad
        )
    }

    /**
     * Checks if a point is inside a quad (p1, p2, p3, p4 in clockwise or counter-clockwise order)
     * or within tolerance meters from its edges.
     */
    private fun isPointInOrNearQuad(
        p: Point2D,
        q1: Point2D,
        q2: Point2D,
        q3: Point2D,
        q4: Point2D,
        tolerance: Float
    ): Boolean {
        // 1. Check if inside polygon using ray-casting algorithm
        val poly = arrayOf(q1, q2, q3, q4)
        var inside = false
        var j = poly.size - 1
        for (i in poly.indices) {
            val pi = poly[i]
            val pj = poly[j]
            if ((pi.y > p.y) != (pj.y > p.y) &&
                (p.x < (pj.x - pi.x) * (p.y - pi.y) / (pj.y - pi.y) + pi.x)
            ) {
                inside = !inside
            }
            j = i
        }
        if (inside) return true

        if (tolerance <= 0f) return false

        // 2. Check distance to each of the 4 edges
        val tolSq = tolerance * tolerance
        if (distanceToSegmentSq(p, q1, q2) <= tolSq) return true
        if (distanceToSegmentSq(p, q2, q3) <= tolSq) return true
        if (distanceToSegmentSq(p, q3, q4) <= tolSq) return true
        if (distanceToSegmentSq(p, q4, q1) <= tolSq) return true

        return false
    }

    private fun distanceToSegmentSq(p: Point2D, a: Point2D, b: Point2D): Float {
        val ab = b - a
        val ap = p - a
        val lenSq = ab.lengthSquared()
        if (lenSq < 1e-6f) return p.distanceSquaredTo(a)
        val t = (ap.dot(ab) / lenSq).coerceIn(0f, 1f)
        val closest = a + ab * t
        return p.distanceSquaredTo(closest)
    }
}
