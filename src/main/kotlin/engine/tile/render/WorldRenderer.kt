package engine.tile.render

import engine.base.ShaderProgram
import engine.tile.Chunk
import engine.tile.IWorldListener
import engine.tile.World
import game.entity.Player
import org.joml.FrustumIntersection
import org.joml.Matrix4f

class WorldRenderer(private val world: World, private val player: Player) : IWorldListener, AutoCloseable {
    private val chunks = arrayOfNulls<ChunkRenderer>(world.chunks.size)

    init {
        world.listeners.add(this)
        
        for (z in 0..<world.zChunks) {
            for (y in 0..<world.yChunks) {
                for (x in 0..<world.xChunks) {
                    val i = x + world.xChunks * (y + world.yChunks * z)
                    chunks[i] = ChunkRenderer(world.chunks[i]!!)
                }
            }
        }
    }

    fun render(shader: ShaderProgram? = null) {
        val frustum = FrustumIntersection(Matrix4f(player.currentProjection).mul(player.currentView))
        
        for (chunkRenderer in chunks) {
            if (chunkRenderer == null) continue
            
            val chunk = chunkRenderer.chunk
            
            if (!frustum.testAab(chunk.boundingBox.min, chunk.boundingBox.max)) {
                continue
            }
            
            chunkRenderer.render(shader)
        }
    }

    override fun onAreaUpdate(minX: Int, minY: Int, minZ: Int, maxX: Int, maxY: Int, maxZ: Int) {
        val minChunkX = (minX / Chunk.SIZE).coerceIn(0, world.xChunks - 1)
        val minChunkY = (minY / Chunk.SIZE).coerceIn(0, world.yChunks - 1)
        val minChunkZ = (minZ / Chunk.SIZE).coerceIn(0, world.zChunks - 1)

        val maxChunkX = (maxX / Chunk.SIZE).coerceIn(0, world.xChunks - 1)
        val maxChunkY = (maxY / Chunk.SIZE).coerceIn(0, world.yChunks - 1)
        val maxChunkZ = (maxZ / Chunk.SIZE).coerceIn(0, world.zChunks - 1)

        for (z in minChunkZ..maxChunkZ) {
            for (y in minChunkY..maxChunkY) {
                for (x in minChunkX..maxChunkX) {
                    chunks[x + world.xChunks * (y + world.yChunks * z)]?.isDirty = true
                }
            }
        }
    }

    override fun close() {
        chunks.forEach { it!!.close() }
    }
}