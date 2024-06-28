package engine.registries

import engine.tile.Tile

class TileRegistry(size: Int) : IRegistry<Byte, Tile> {
    private val backend = arrayOfNulls<Tile>(size)
    
    override fun getValue(key: Byte): Tile? {
        if (key >= backend.size) return null
        return backend[key.toInt()]
    }

    override fun setValue(key: Byte, value: Tile?) {
        if (key >= backend.size) return
        backend[key.toInt()] = value
    }
}