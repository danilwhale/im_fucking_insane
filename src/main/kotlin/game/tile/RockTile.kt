package game.tile

import engine.base.Color
import engine.tile.Tile
import engine.tile.render.TileBuilder
import engine.tile.render.TileFaces

class RockTile(textureIndex: Int) : Tile(TileFaces(textureIndex), Color.WHITE) {
    private val builder = TileBuilder(this)
    override fun getBuilder() = builder
}