package engine.tile

import engine.base.BoundingBox

class Chunk(
    val world: World,
    val x: Int, val y: Int, val z: Int
) {
    companion object {
        const val SIZE = 16
    }

    val globalX = x * SIZE
    val globalY = y * SIZE
    val globalZ = z * SIZE

    val boundingBox = BoundingBox(
        globalX.toFloat(),
        globalY.toFloat(),
        globalZ.toFloat(),

        globalX + SIZE.toFloat(),
        globalY + SIZE.toFloat(),
        globalZ + SIZE.toFloat()
    )

    private val data: ByteArray = ByteArray(SIZE * SIZE * SIZE) { 0 }

    fun setTile(x: Int, y: Int, z: Int, id: Byte) {
        data[x + SIZE * (y + SIZE * z)] = id
    }

    fun getTile(x: Int, y: Int, z: Int): Byte {
        return data[x + SIZE * (y + SIZE * z)]
    }
}