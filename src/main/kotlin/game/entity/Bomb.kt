package game.entity

import engine.base.*
import engine.render.CuboidRenderer
import engine.render.VertexBuilder
import engine.tile.World
import engine.tile.render.TileFaces
import org.joml.Vector3f
import org.joml.Vector3i
import kotlin.random.Random

class Bomb(
    world: World, 
    entityWorld: EntityWorld, 
    position: Vector3f, 
    private val mass: Int
) : Entity(world, entityWorld) {
    companion object {
        private val FACES = TileFaces(Rectangle(0.0f, 0.0f, 1.0f, 1.0f))
    }
    
    private val vb = VertexBuilder()
    private val shader = ShaderProgram.getDefault()
    private val texture = TextureManager.get("ct.png")
    
    private var velocity = Vector3f()
    
    init {
        box = BoundingBox(Vector3f(position), Vector3f(position)).grow(0.5f)
    }
    
    override fun update(delta: Float) {
        velocity.y -= 0.01f
        box.move(Vector3f(velocity).mul(delta))
        
        val boxes = world.getBoxes(box)
        
        if (boxes.isNotEmpty()) {
            explode()
            entityWorld.remove(this)
        }
    }

    override fun render(delta: Float) {
        texture.bind()
        
        CuboidRenderer.render(vb, box, FACES, Color.WHITE)
        
        texture.unbind()
    }
    
    private fun explode() {
        val center = box.center
        val blockPos = Vector3i(center.x.toInt(), center.y.toInt() - 1, center.z.toInt())
        val radius = mass * 3
        val halfRadius = radius / 2

        for (z in blockPos.z - halfRadius..blockPos.z + halfRadius) {
            for (y in blockPos.y - halfRadius..blockPos.y + halfRadius) {
                for (x in blockPos.x - halfRadius..blockPos.x + halfRadius) {
                    val pos = Vector3i(x, y, z)
                    val dist = pos.distance(blockPos)

                    if (dist > halfRadius) continue
                    if (dist > halfRadius - 1 && Random.nextFloat() > 0.7f) {
                        continue
                    }

                    world.setTile(x, y, z, 0)
                }
            }
        }
    }
}