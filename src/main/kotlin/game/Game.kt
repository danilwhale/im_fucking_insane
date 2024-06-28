package game

import FastNoiseLite
import engine.base.*

import engine.registries.Registries
import engine.tile.World
import engine.tile.render.WorldRenderer
import game.entity.EntityWorld
import game.entity.Player
import game.tile.GrassTile
import game.tile.RockTile
import org.joml.Matrix4f
import org.joml.Vector2i
import org.lwjgl.opengl.GL33.*

class Game(windowSize: Vector2i) : Engine(windowSize, "im_fucking_insane") {
    private lateinit var texture: Texture
    private lateinit var shader: ShaderProgram
    
    private lateinit var world: World
    private lateinit var worldRenderer: WorldRenderer

    private lateinit var player: Player
    private val entityWorld = EntityWorld()

    override fun init() {
        texture = TextureManager.get("terrain.png")
        shader = ShaderProgram.getDefault()
        
        world = World(512, 64, 512)
        player = Player(world, entityWorld, window)
        worldRenderer = WorldRenderer(world, player)
        
        entityWorld.add(player)

        Registries.TILES.setValue(1, GrassTile(0))
        Registries.TILES.setValue(2, RockTile(1))
        
        val noise = FastNoiseLite()

        for (z in 0..<world.zSize) {
            for (x in 0..<world.xSize) {
                val level = (noise.GetNoise(x.toFloat(), z.toFloat()) * world.ySize).toInt()
                for (y in 0..level) {
                    world.setTile(x, y, z, if (y < level) 2 else 1)
                }
            }
        }

        glEnable(GL_DEPTH_TEST)
        glEnable(GL_CULL_FACE)

        glClearColor(0.5f, 0.8f, 1.0f, 1.0f)
    }

    override fun update(delta: Float) {
        entityWorld.update(delta)
    }

    override fun render(delta: Float) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        shader.bind()
        shader.setUniform("model", Matrix4f())
        player.setupShader(shader, window.getAspectRatio())

        texture.bind()
        worldRenderer.render(shader)
        texture.unbind()
        
        entityWorld.render(delta)
        
        shader.unbind()
    }

    override fun unload() {
        ShaderProgram.getDefault().close()
        texture.close()
    }
}