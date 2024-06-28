package engine.render

import engine.base.Color
import engine.base.ShaderProgram
import org.joml.Vector2f
import org.lwjgl.opengl.GL33.*
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer
import java.nio.IntBuffer

class VertexBuilder : AutoCloseable {
    private lateinit var verticesBuf: FloatBuffer
    private lateinit var indicesBuf: IntBuffer
    
    private val vao = glGenVertexArrays()
    private val vbo = glGenBuffers()
    private val ebo = glGenBuffers()
    
    private var u = 0.0f
    private var v = 0.0f
    
    private var r = 0.0f
    private var g = 0.0f
    private var b = 0.0f
    
    private var vertices = 0
    
    fun begin(verticesAmount: Int) {
        reset()
        freeNioBuffers()
        
        verticesBuf = MemoryUtil.memAllocFloat(verticesAmount * (3 + 3 + 2))
    }
    
    fun begin(verticesAmount: Int, indicesAmount: Int) {
        begin(verticesAmount)
        indicesBuf = MemoryUtil.memAllocInt(indicesAmount)
    }
    
    fun setColor(r: Float, g: Float, b: Float) {
        this.r = r
        this.g = g
        this.b = b
    }
    
    fun setColor(color: Color) {
        setColor(color.r, color.g, color.b)
    }
    
    fun setTexCoords(u: Float, v: Float) {
        this.u = u
        this.v = v
    }
    
    fun setTexCoords(texCoords: Vector2f) {
        setTexCoords(texCoords.x, texCoords.y)
    }

    fun addVertex(x: Float, y: Float, z: Float, u: Float, v: Float, r: Float, g: Float, b: Float): Int {
        verticesBuf
            .put(x).put(y).put(z)
            .put(u).put(v)
            .put(r).put(g).put(b)

        return vertices++
    }

    fun addVertex(v: Vertex): Int {
        return addVertex(v.x, v.y, v.z, v.u, v.v, v.r, v.g, v.b)
    }
    
    fun addVertex(x: Float, y: Float, z: Float): Int {
        return addVertex(x, y, z, this.u, this.v, this.r, this.g, this.b)
    }
    
    fun addVertex(x: Float, y: Float, z: Float, u: Float, v: Float): Int {
        return addVertex(x, y, z, u, v, this.r, this.g, this.b)
    }
    
    fun addVertices(vararg vertices: Vertex): IntArray {
        return vertices.map { addVertex(it) }.toIntArray()
    }
    
    fun addIndex(i: Int) {
        indicesBuf.put(i)
    }
    
    fun addIndices(vararg indices: Int) {
        indicesBuf.put(indices)
    }
    
    fun draw() {
        glBindVertexArray(vao)
        
        if (indicesBuf.limit() > 0) {
            glDrawElements(GL_TRIANGLES, indicesBuf.limit(), GL_UNSIGNED_INT, 0)
        } else {
            glDrawArrays(GL_TRIANGLES, 0, vertices)
        }
        
        glBindVertexArray(0)
    }
    
    fun end(shader: ShaderProgram? = null) {
        val posLoc = (shader ?: ShaderProgram.getDefault()).getAttribLocation("aPos")
        val texCoordLoc = (shader ?: ShaderProgram.getDefault()).getAttribLocation("aTexCoord")
        val colorLoc = (shader ?: ShaderProgram.getDefault()).getAttribLocation("aColor")
        
        glBindVertexArray(vao)
        
        verticesBuf.flip()
        
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, verticesBuf, GL_DYNAMIC_DRAW)
        
        val szFloat = Float.SIZE_BYTES
        val stride = (3 + 2 + 3) * szFloat
        
        // aPos
        glVertexAttribPointer(
            posLoc, 
            3, 
            GL_FLOAT, 
            false, 
            stride, 
            0)
        
        glEnableVertexAttribArray(posLoc)

        // aTexCoords
        glVertexAttribPointer(
            texCoordLoc,
            2,
            GL_FLOAT,
            false,
            stride,
            (3 * szFloat).toLong())

        glEnableVertexAttribArray(texCoordLoc)
        
        // aColor
        glVertexAttribPointer(
            colorLoc, 
            3, 
            GL_FLOAT, 
            false, 
            stride, 
            (5 * szFloat).toLong())
        
        glEnableVertexAttribArray(colorLoc)
        
        indicesBuf.flip()
        
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuf, GL_DYNAMIC_DRAW)
        
        glBindVertexArray(0)
    }
    
    fun flush(shader: ShaderProgram? = null) {
        end(shader)
        draw()
    }
    
    private fun reset() {
        vertices = 0
        
        u = 0.0f
        v = 0.0f
        
        r = 0.0f
        g = 0.0f
        b = 0.0f
    }
    
    private fun freeNioBuffers() {
        if (this::verticesBuf.isInitialized) {
            MemoryUtil.memFree(verticesBuf)
        }
        
        if (this::indicesBuf.isInitialized) {
            MemoryUtil.memFree(indicesBuf)
        }
    }

    override fun close() {
        freeNioBuffers()

        glDeleteVertexArrays(vao)

        glDeleteBuffers(vbo)
        glDeleteBuffers(ebo)
    }
}