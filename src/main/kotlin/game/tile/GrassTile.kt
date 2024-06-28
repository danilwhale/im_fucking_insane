package game.tile

import engine.base.Color
import engine.tile.Tile
import engine.tile.render.TileBuilder
import engine.tile.render.TileFaces

class GrassTile(textureIndex: Int) : Tile(
    TileFaces(
        textureIndex, textureIndex + 1, 
        textureIndex + 1, textureIndex + 1,
        textureIndex + 1, textureIndex + 1
    ), Color.WHITE
) {
    private val builder = TileBuilder(this)
    override fun getBuilder() = builder
}