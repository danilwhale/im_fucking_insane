package engine.base

import org.joml.Vector2f

data class Rectangle(
    var x: Float,
    var y: Float,
    var width: Float,
    var height: Float
) {
    constructor(rectangle: Rectangle)
        : this(rectangle.x, rectangle.y, rectangle.width, rectangle.height)
    
    constructor(position: Vector2f, size: Vector2f)
            : this(position.x, position.y, size.x, size.y)
    
    operator fun div(scalar: Float): Rectangle {
        x /= scalar
        y /= scalar
        width /= scalar
        height /= scalar
        
        return this
    }
    
    operator fun times(scalar: Float): Rectangle {
        x *= scalar
        y *= scalar
        width *= scalar
        height *= scalar
        
        return this
    }
}
