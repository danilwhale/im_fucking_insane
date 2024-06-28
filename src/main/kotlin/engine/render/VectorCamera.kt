package engine.render

import org.joml.Math
import org.joml.Matrix4f
import org.joml.Vector3f

class VectorCamera(position: Vector3f, rotation: Vector3f, worldUp: Vector3f, fov: Float) :
    Camera(position, Vector3f(0.0f), worldUp, fov) {
    private var forward = Vector3f(0.0f, 0.0f, -1.0f)
    private var right = Vector3f(1.0f, 0.0f, 0.0f)
    private var up = Vector3f(0.0f, 1.0f, 0.0f)

    var rotation = rotation
        get() = field
        set(value) {
            field = value
            updateVectors()
        }

    private fun updateVectors() {
        forward = Vector3f(0.0f, 0.0f, -1.0f)
            .rotateX(Math.toRadians(rotation.x))
            .rotateY(Math.toRadians(rotation.y))
            .rotateZ(Math.toRadians(rotation.z))
            .normalize()
        right = Vector3f(forward).cross(worldUp).normalize()
        up = Vector3f(right).cross(forward).normalize()
    }

    fun getForward() = forward
    fun getRight() = right
    fun getUp() = up
    
    fun move(xDir: Float, yDir: Float, zDir: Float) {
        position.add(Vector3f(forward).mul(zDir))
        position.add(Vector3f(right).mul(xDir))
        position.add(Vector3f(up).mul(yDir))
    }

    override fun getView() = Matrix4f().lookAt(position, Vector3f(position).add(forward), worldUp)
}