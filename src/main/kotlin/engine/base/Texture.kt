package engine.base

import de.matthiasmann.twl.utils.PNGDecoder
import de.matthiasmann.twl.utils.PNGDecoder.Format
import org.lwjgl.opengl.GL33.*
import java.nio.ByteBuffer

class Texture(rgbaBuffer: ByteBuffer, width: Int, height: Int) : AutoCloseable {
    private val texture: Int = glGenTextures()

    init {
        loadBuffer(rgbaBuffer, width, height)
    }
    
    private fun loadBuffer(rgbaBuffer: ByteBuffer, width: Int, height: Int) {
        rgbaBuffer.flip()
        bind()
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
        glTexImage2D(
            GL_TEXTURE_2D,
            0,
            GL_RGBA,
            width,
            height,
            0,
            GL_RGBA,
            GL_UNSIGNED_BYTE,
            rgbaBuffer
        )
        glGenerateMipmap(GL_TEXTURE_2D)
        unbind()
    }
    

    fun bind() {
        glBindTexture(GL_TEXTURE_2D, texture)
    }
    
    fun unbind() {
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    override fun close() {
        glDeleteTextures(texture)
    }
}