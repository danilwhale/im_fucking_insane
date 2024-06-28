package engine.tile

import engine.base.BoundingBox
import org.joml.Vector3i

class World(val xSize: Int, val ySize: Int, val zSize: Int) {
    val xChunks = xSize / Chunk.SIZE
    val yChunks = ySize / Chunk.SIZE
    val zChunks = zSize / Chunk.SIZE

    val chunks = arrayOfNulls<Chunk>(xChunks * yChunks * zChunks)

    val listeners = mutableListOf<IWorldListener>()

    init {
        for (z in 0..<zChunks) {
            for (y in 0..<yChunks) {
                for (x in 0..<xChunks) {
                    chunks[x + xChunks * (y + yChunks * z)] = Chunk(this, x, y, z)
                }
            }
        }
    }

    fun setTile(x: Int, y: Int, z: Int, value: Byte) {
        if (x < 0 || y < 0 || z < 0 || x >= xSize || y >= ySize || z >= zSize) {
            return
        }

        val chunkX = x / Chunk.SIZE
        val chunkY = y / Chunk.SIZE
        val chunkZ = z / Chunk.SIZE

        val localX = x - chunkX * Chunk.SIZE
        val localY = y - chunkY * Chunk.SIZE
        val localZ = z - chunkZ * Chunk.SIZE

        chunks[chunkX + xChunks * (chunkY + yChunks * chunkZ)]!!.setTile(localX, localY, localZ, value)
        listeners.forEach { it.onAreaUpdate(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1) }
    }
    
    fun setTile(position: Vector3i, value: Byte) = setTile(position.x, position.y, position.z, value)

    fun getTile(x: Int, y: Int, z: Int): Byte {
        if (x < 0 || y < 0 || z < 0 || x >= xSize || y >= ySize || z >= zSize) {
            return 0
        }

        val chunkX = x / Chunk.SIZE
        val chunkY = y / Chunk.SIZE
        val chunkZ = z / Chunk.SIZE

        val localX = x - chunkX * Chunk.SIZE
        val localY = y - chunkY * Chunk.SIZE
        val localZ = z - chunkZ * Chunk.SIZE

        return chunks[chunkX + xChunks * (chunkY + yChunks * chunkZ)]!!.getTile(localX, localY, localZ)
    }
    
    fun getTile(position: Vector3i) = getTile(position.x, position.y, position.z)
    
    fun isEmpty(x: Int, y: Int, z: Int): Boolean {
        return getTile(x, y, z).toInt() == 0
    }
    
    fun isEmpty(position: Vector3i) = isEmpty(position.x, position.y, position.z)

    fun getBoxes(box: BoundingBox): List<BoundingBox> {
        val minX = box.min.x.toInt().coerceIn(0, xSize - 1)
        val minY = box.min.y.toInt().coerceIn(0, ySize - 1)
        val minZ = box.min.z.toInt().coerceIn(0, zSize - 1)

        val maxX = box.max.x.toInt().coerceIn(0, xSize - 1)
        val maxY = box.max.y.toInt().coerceIn(0, ySize - 1)
        val maxZ = box.max.z.toInt().coerceIn(0, zSize - 1)

        val boxes = mutableListOf<BoundingBox>()

        for (z in minZ..<maxZ) {
            for (y in minY..<maxY) {
                for (x in minX..<maxX) {
                    if (isEmpty(x, y, z)) continue
                    boxes.add(
                        BoundingBox(
                            x.toFloat(),
                            y.toFloat(),
                            z.toFloat(),
                            x + 1.0f,
                            y + 1.0f,
                            z + 1.0f
                        )
                    )
                }
            }
        }

        return boxes
    }
}