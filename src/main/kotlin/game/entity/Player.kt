package game.entity

import engine.base.BoundingBox
import engine.base.ShaderProgram
import engine.base.Window
import engine.render.VectorCamera
import engine.tile.World
import org.joml.Matrix4f
import org.joml.Vector2d
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.system.MemoryStack
import kotlin.random.Random

class Player(
    world: World,
    entityWorld: EntityWorld,
    private val window: Window,
) : Entity(world, entityWorld) {
    private val camera = VectorCamera(
        Vector3f(world.xSize * 0.5f, world.ySize * 0.75f, world.zSize * 0.5f),
        Vector3f(0.0f, -90.0f, 0.0f),
        Vector3f(0.0f, 1.0f, 0.0f),
        70.0f
    )

    var currentView = Matrix4f()
        get() = field
        private set(value) {
            field = value
        }

    var currentProjection = Matrix4f()
        get() = field
        private set(value) {
            field = value
        }

    private var lastMousePos = Vector2d()

    init {
        box = BoundingBox(camera.position, camera.position)

        lastMousePos = window.cursorPos;

        window.setCursorCallback { x, y ->
            val mousePos = Vector2d(x, y)

            val delta = Vector2d(mousePos)
                .sub(lastMousePos)
                .mul(0.2)
                .negate()

            camera.rotation = Vector3f(camera.rotation)
                .add(delta.y.toFloat(), delta.x.toFloat(), 0.0f)

            camera.rotation = Vector3f(camera.rotation.x.coerceIn(-89.9f, 89.9f), camera.rotation.y, camera.rotation.z)

            lastMousePos = mousePos
        }

        window.setKeyCallback { key, _, action, _ -> 
            if (key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
                entityWorld.add(
                    Bomb(
                        world,
                        entityWorld,
                        Vector3f(camera.position).sub(0.0f, 2.0f, 0.0f),
                        if (Random.nextFloat() > 0.9f) Random.nextInt(21, 30)
                        else Random.nextInt(0, 10)
                    )
                )
            }
        }

        window.setInputMode(GLFW_CURSOR, GLFW_CURSOR_DISABLED)
    }

    override fun update(delta: Float) {
        var xDir = 0
        var zDir = 0

        if (window.isKeyDown(GLFW_KEY_W)) {
            zDir++
        }

        if (window.isKeyDown(GLFW_KEY_S)) {
            zDir--
        }

        if (window.isKeyDown(GLFW_KEY_A)) {
            xDir--
        }

        if (window.isKeyDown(GLFW_KEY_D)) {
            xDir++
        }

        val d = 50.0f * delta
        camera.move(xDir * d, 0.0f, zDir * d)
    }

    override fun render(delta: Float) {

    }

    fun setupShader(shader: ShaderProgram, aspectRatio: Float) {
        currentView = camera.getView()
        currentProjection = camera.getProjection(aspectRatio)

        shader.setUniform("view", currentView)
        shader.setUniform("projection", currentProjection)
    }
}