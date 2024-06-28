package engine.base

import de.matthiasmann.twl.utils.PNGDecoder
import de.matthiasmann.twl.utils.PNGDecoder.Format
import java.nio.ByteBuffer

object TextureManager {
    private val map = mutableMapOf<String, Texture>()
    private lateinit var defaultTexture: Texture

    fun getDefault(): Texture {
        if (!this::defaultTexture.isInitialized) {
            defaultTexture = loadFromColor(1, 1, Color.WHITE)
        }

        return defaultTexture
    }

    fun get(fileName: String): Texture {
        if (map.containsKey(fileName)) return map[fileName]!!
        
        val decoder = PNGDecoder(javaClass.getResourceAsStream("/$fileName"))
        val buf = ByteBuffer.allocateDirect(decoder.width * decoder.height * 4)
        decoder.decode(buf, decoder.width * 4, Format.RGBA)

        val texture = Texture(buf, decoder.width, decoder.height)
        map[fileName] = texture
        
        return texture
    }

    fun loadFromColor(width: Int, height: Int, color: Color): Texture {
        val buf = ByteBuffer.allocateDirect(width * height * 4)
        for (i in 0..<width * height) {
            color.put(buf)
        }
        
        return Texture(buf, width, height)
    }
}