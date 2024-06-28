package engine.base

import org.joml.Vector3f

data class BoundingBox(
    val min: Vector3f,
    val max: Vector3f
) {
    constructor(minX: Float, minY: Float, minZ: Float, maxX: Float, maxY: Float, maxZ: Float)
            : this(Vector3f(minX, minY, minZ), Vector3f(maxX, maxY, maxZ))

    constructor(box: BoundingBox)
            : this(box.min, box.max)

    val center: Vector3f
        get() = Vector3f(max).add(min).div(2.0f)

    fun intersect(other: BoundingBox): Boolean {
        if (min.x <= other.max.x && max.x >= other.min.x) {
            return true
        }

        if (min.y <= other.max.y && max.y >= other.min.y) {
            return true
        }

        if (min.z <= other.max.z && max.z >= other.min.z) {
            return true
        }

        return false
    }

    fun grow(scale: Float): BoundingBox {
        min.sub(scale, scale, scale)
        max.add(scale, scale, scale)
        
        return this
    }
    
    fun move(x: Float, y: Float, z: Float): BoundingBox {
        min.add(x, y, z)
        max.add(x, y, z)
        
        return this
    }
    
    fun move(dir: Vector3f): BoundingBox {
        min.add(dir)
        max.add(dir)
        
        return this
    }
}
