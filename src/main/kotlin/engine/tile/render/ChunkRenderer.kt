package engine.tile.render

import engine.base.ShaderProgram
import engine.render.VertexBuilder
import engine.registries.Registries
import engine.tile.Chunk

class ChunkRenderer(val chunk: Chunk) : AutoCloseable {
    var isDirty = true
    private val vb = VertexBuilder()

    fun render(shader: ShaderProgram? = null) {
        if (isDirty) {
            rebuild(shader)
            isDirty = false
        }
        
        vb.draw()
    }

    fun rebuild(shader: ShaderProgram? = null) {
        var count = 0

        for (z in 0..<Chunk.SIZE) {
            for (y in 0..<Chunk.SIZE) {
                for (x in 0..<Chunk.SIZE) {
                    val id = chunk.getTile(x, y, z)
                    val tile = Registries.TILES.getValue(id) ?: continue
                    count += tile.getFaceCount(chunk.world, chunk.globalX + x, chunk.globalY + y, chunk.globalZ + z)
                }
            }
        }
        
        // 4 vertices per face (quad = A, B, C, D)
        // 6 indices per face (quad = A -> B -> C + A -> C -> D)
        vb.begin(count * 4, count * 6)

        for (z in 0..<Chunk.SIZE) {
            for (y in 0..<Chunk.SIZE) {
                for (x in 0..<Chunk.SIZE) {
                    val id = chunk.getTile(x, y, z)
                    val tile = Registries.TILES.getValue(id) ?: continue
                    tile.getBuilder().build(vb, chunk.world, chunk.globalX + x, chunk.globalY + y, chunk.globalZ + z)
                }
            }
        }
        
        vb.end(shader)
    }

    override fun close() {
        vb.close()
    }
}