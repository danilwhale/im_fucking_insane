package engine.tile

import org.joml.Vector3i

interface IWorldListener {
    fun onAreaUpdate(minX: Int, minY: Int, minZ: Int, maxX: Int, maxY: Int, maxZ: Int)
}