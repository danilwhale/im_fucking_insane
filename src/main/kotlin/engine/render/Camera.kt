package engine.render

import org.joml.Math
import org.joml.Matrix4f
import org.joml.Vector3f

open class Camera(position: Vector3f, target: Vector3f, worldUp: Vector3f, fov: Float) {
    var position = position
    var target = target
    val worldUp = worldUp
    var fov = fov

    open fun getView() = Matrix4f().lookAt(position, target, worldUp)
    open fun getProjection(aspectRatio: Float) = Matrix4f().perspective(
        Math.toRadians(fov),
        aspectRatio,
        0.01f,
        1000.0f
    )
}