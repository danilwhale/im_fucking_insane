package game.entity

import engine.base.BoundingBox
import engine.tile.World

abstract class Entity(
    protected val world: World, 
    protected val entityWorld: EntityWorld
) {
    lateinit var box: BoundingBox
    
    abstract fun update(delta: Float)
    abstract fun render(delta: Float)
}