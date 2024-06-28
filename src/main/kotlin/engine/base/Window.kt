package engine.base

import org.joml.Vector2d
import org.joml.Vector2i
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.Callbacks.*
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil.NULL

data class Window(val window: Long) {
    constructor(size: Vector2i, title: String)
            : this(glfwCreateWindow(size.x, size.y, title, NULL, NULL))
    
    var size: Vector2i
        get() {
            var width: Int
            var height: Int

            MemoryStack.stackPush().use {
                val widthPtr = it.mallocInt(1)
                val heightPtr = it.mallocInt(1)

                glfwGetWindowSize(window, widthPtr, heightPtr)

                width = widthPtr.get(0)
                height = heightPtr.get(0)
            }

            return Vector2i(width, height)
        }
        set(value) = glfwSetWindowSize(window, value.x, value.y)

    var position: Vector2i
        get() {
            var x: Int
            var y: Int

            MemoryStack.stackPush().use {
                val xPtr = it.mallocInt(1)
                val yPtr = it.mallocInt(1)

                glfwGetWindowPos(window, xPtr, yPtr)

                x = xPtr.get(0)
                y = yPtr.get(0)
            }

            return Vector2i(x, y)
        }
        set(value) = glfwSetWindowPos(window, value.x, value.y)
    
    var cursorPos: Vector2d
        get() {
            var x: Double
            var y: Double

            MemoryStack.stackPush().use {
                val xPtr = it.mallocDouble(1)
                val yPtr = it.mallocDouble(1)

                glfwGetCursorPos(window, xPtr, yPtr)

                x = xPtr.get(0)
                y = yPtr.get(0)
            }

            return Vector2d(x, y)
        }
        set(value) = glfwSetCursorPos(window, value.x, value.y)
    
    val input = InputManager(this)
    
    fun isReady() = window != NULL
    fun makeCurrentContext() = glfwMakeContextCurrent(window)
    fun shouldClose() = glfwWindowShouldClose(window)
    fun close() = glfwSetWindowShouldClose(window, true)
    fun swapBuffers() = glfwSwapBuffers(window)
    
    fun freeCallbacks() = glfwFreeCallbacks(window)
    fun destroy() = glfwDestroyWindow(window)
    
    fun getInputMode(mode: Int) = glfwGetInputMode(window, mode)
    
    fun setInputMode(mode: Int, value: Int) {
        glfwSetInputMode(window, mode, value)
    }

    fun getAspectRatio(): Float {
        val size = size
        return size.x.toFloat() / size.y
    }
    
    fun isKeyDown(key: Int): Boolean {
        return glfwGetKey(window, key) == GLFW_PRESS;
    }
    
    fun setCursorCallback(callback: ((x: Double, y: Double) -> Unit)) {
        glfwSetCursorPosCallback(window) { _, xpos, ypos -> 
            callback(xpos, ypos)
        }
    }

    fun setKeyCallback(callback: ((key: Int, scancode: Int, action: Int, mods: Int) -> Unit)) {
        glfwSetKeyCallback(window) { _, key, scancode, action, mods ->
            callback(key, scancode, action, mods)
        }
    }

    fun setFrameBufferResizeCallback(callback: ((width: Int, height: Int) -> Unit)) {
        glfwSetFramebufferSizeCallback(window) { _, width, height ->
            callback(width, height)
        }
    }
}