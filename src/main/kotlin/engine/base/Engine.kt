package engine.base

import org.joml.Vector2i
import org.lwjgl.glfw.Callbacks.*;
import org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWFramebufferSizeCallback
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.glViewport
import org.lwjgl.opengl.GLUtil
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil.*;

abstract class Engine(private val windowSize: Vector2i, private val title: String) {
    protected lateinit var window: Window
    
    protected var fps = 0
    
    private var frames = 0 
    private var lastFpsTime = 0.0
    private var lastTime = 0.0

    private fun setup() {
        GLFWErrorCallback.createPrint(System.err).set()

        if (!glfwInit()) {
            throw IllegalStateException("failed to init glfw")
        }

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

        window = Window(windowSize, title)
        if (!window.isReady()) {
            throw RuntimeException("failed to create window")
        }

        window.setKeyCallback { key, _, action, _ -> 
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                window.close()
            }
        }
        
        window.setFrameBufferResizeCallback { width, height -> 
            glViewport(0, 0, width, height)
        }

        val videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor())!!
        window.position = Vector2i(
            (videoMode.width() - window.size.x) / 2,
            (videoMode.height() - window.size.y) / 2
        )
        
        window.makeCurrentContext()
        glfwSwapInterval(0)
    }
    
    private fun loop() {
        GL.createCapabilities()
        GLUtil.setupDebugMessageCallback()
        
        init()
        
        while (!window.shouldClose()) {
            frames++
            
            if (glfwGetTime() >= lastFpsTime + 1.0) {
                fps = frames
                frames = 0
                lastFpsTime = glfwGetTime()
                println("$fps FPS")
            }
            
            val delta = (glfwGetTime() - lastTime).toFloat()
            lastTime = glfwGetTime()
            
            update(delta)
            render(delta)
            
            window.swapBuffers()
            glfwPollEvents()
        }
        
        unload()
    }
    
    protected abstract fun init();
    protected abstract fun update(delta: Float)
    protected abstract fun render(delta: Float)
    protected abstract fun unload();

    fun start() {
        try {
            setup()
            loop()
        } finally {
            window.freeCallbacks()
            window.destroy()
            
            glfwTerminate()
            glfwSetErrorCallback(null)?.free()
        }
    }
}