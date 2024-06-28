package engine.tile.render

import engine.base.BoundingBox
import engine.base.Rectangle
import engine.render.VertexBuilder
import engine.tile.Face
import engine.tile.Tile
import engine.tile.World
import org.joml.Vector3f

open class TileBuilder(val tile: Tile)  {
    open fun build(vb: VertexBuilder, world: World, x: Int, y: Int, z: Int) {
        vb.setColor(tile.tint)
        
        if (world.isEmpty(x, y + 1, z)) buildFace(vb, Face.TOP, x, y, z)
        if (world.isEmpty(x, y - 1, z)) buildFace(vb, Face.BOTTOM, x, y, z)
        if (world.isEmpty(x + 1, y, z)) buildFace(vb, Face.RIGHT, x, y, z)
        if (world.isEmpty(x - 1, y, z)) buildFace(vb, Face.LEFT, x, y, z)
        if (world.isEmpty(x, y, z + 1)) buildFace(vb, Face.FRONT, x, y, z)
        if (world.isEmpty(x, y, z - 1)) buildFace(vb, Face.BACK, x, y, z)
    }
    
    protected open fun getTextureRect(face: Face): Rectangle {
        return tile.faces[face]
    }
    
    protected open fun buildFace(vb: VertexBuilder, face: Face, x: Int, y: Int, z: Int) {
        val min = Vector3f(x.toFloat(), y.toFloat(), z.toFloat())
        val max = Vector3f(x + 1.0f, y + 1.0f, z + 1.0f)
        val bounds = BoundingBox(min, max)
        
        val rect = Rectangle(getTextureRect(face))
        
        when (face) {
            Face.TOP -> DefaultTileFaceBuilder.buildTopFace(vb, bounds, rect, tile.tint)
            Face.BOTTOM -> DefaultTileFaceBuilder.buildBottomFace(vb, bounds, rect, tile.tint)
            Face.RIGHT -> DefaultTileFaceBuilder.buildRightFace(vb, bounds, rect, tile.tint)
            Face.LEFT -> DefaultTileFaceBuilder.buildLeftFace(vb, bounds, rect, tile.tint)
            Face.FRONT -> DefaultTileFaceBuilder.buildFrontFace(vb, bounds, rect, tile.tint)
            Face.BACK -> DefaultTileFaceBuilder.buildBackFace(vb, bounds, rect, tile.tint)
            else -> { return }
        }
    }
}