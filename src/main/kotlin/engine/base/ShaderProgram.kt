package engine.base

import org.joml.Matrix4f
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL33.*
import org.lwjgl.system.MemoryUtil

class ShaderProgram(vertFileName: String, fragFileName: String) : AutoCloseable {
    companion object {
        private var defaultShader: ShaderProgram? = null

        fun getDefault(): ShaderProgram {
            if (defaultShader != null) {
                return defaultShader!!
            }

            defaultShader = ShaderProgram("default.vert", "default.frag")
            return defaultShader!!
        }
    }
    
    private val program: Int

    init {
        val vertContent = readText(vertFileName)
        val fragContent = readText(fragFileName)
        
        val vert = createShader(vertContent, GL_VERTEX_SHADER)
        val frag = createShader(fragContent, GL_FRAGMENT_SHADER)
        
        program = glCreateProgram()
        glAttachShader(program, vert)
        glAttachShader(program, frag)
        
        glLinkProgram(program)
        
        if (glGetProgrami(program, GL_LINK_STATUS) != GL_TRUE) {
            throw RuntimeException(glGetProgramInfoLog(program))
        }
        
        glDeleteShader(vert)
        glDeleteShader(frag)
    }
    
    private fun createShader(content: String, type: Int): Int {
        val shader = glCreateShader(type)
        glShaderSource(shader, content)
        glCompileShader(shader)
        
        if (glGetShaderi(shader, GL_COMPILE_STATUS) != GL_TRUE) {
            throw RuntimeException(glGetShaderInfoLog(shader))
        }
        
        return shader
    }
    
    private fun readText(fileName: String): String {
        val stream = javaClass.getResourceAsStream("/$fileName")!!
        val text = stream.bufferedReader().use { it.readText() }
        stream.close()
        
        return text
    }
    
    fun bind() {
        glUseProgram(program)
    }
    
    fun unbind() {
        glUseProgram(0)
    }
    
    fun getUniformLocation(uniformName: String): Int {
        return glGetUniformLocation(program, uniformName)
    }
    
    fun getAttribLocation(attribName: String): Int {
        return GL20.glGetAttribLocation(program, attribName)
    }
    
    fun setUniform(uniformName: String, value: Matrix4f) {
        val buf = MemoryUtil.memAllocFloat(16)
        glUniformMatrix4fv(getUniformLocation(uniformName), false, value[buf])
        MemoryUtil.memFree(buf)
    }

    fun setUniform(uniformName: String, value: Float) {
        glUniform1f(getUniformLocation(uniformName), value)
    }

    override fun close() {
        glDeleteProgram(program)
    }
}