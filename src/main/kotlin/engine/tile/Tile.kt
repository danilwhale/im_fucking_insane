package engine.tile

import engine.base.Color
import engine.tile.render.TileBuilder
import engine.tile.render.TileFaces
import kotlin.experimental.and
import kotlin.experimental.or

abstract class Tile(val faces: TileFaces, val tint: Color) {
    open fun getFaces(world: World, x: Int, y: Int, z: Int): Byte {
        var face = Face.NONE.i
        
        if (world.isEmpty(x, y + 1, z)) face = face or Face.TOP.i
        if (world.isEmpty(x, y - 1, z)) face = face or Face.BOTTOM.i
        if (world.isEmpty(x + 1, y, z)) face = face or Face.RIGHT.i
        if (world.isEmpty(x - 1, y, z)) face = face or Face.LEFT.i
        if (world.isEmpty(x, y, z + 1)) face = face or Face.FRONT.i
        if (world.isEmpty(x, y, z - 1)) face = face or Face.BACK.i
        
        return face
    }
    
    open fun getFaceCount(world: World, x: Int, y: Int, z: Int): Int {
        val faces = getFaces(world, x, y, z)
        var count = 0
        
        for (face in Face.entries) {
            if ((faces and face.i) != face.i) {
                continue
            }
            
            count++
        }
        
        return count
    }
    
    abstract fun getBuilder(): TileBuilder
}