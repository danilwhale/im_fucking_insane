package engine.tile.render

import engine.base.Rectangle
import engine.tile.Face

data class TileFaces(
    val topRect: Rectangle,
    val bottomRect: Rectangle,
    val rightRect: Rectangle,
    val leftRect: Rectangle,
    val frontRect: Rectangle,
    val backRect: Rectangle
) {
    constructor(
        topIndex: Int,
        bottomIndex: Int,
        rightIndex: Int,
        leftIndex: Int,
        frontIndex: Int,
        backIndex: Int
    ) : this(
        getRectForIndex(topIndex),
        getRectForIndex(bottomIndex),
        getRectForIndex(rightIndex),
        getRectForIndex(leftIndex),
        getRectForIndex(frontIndex),
        getRectForIndex(backIndex)
    )
    
    constructor(
        rect: Rectangle
    ) : this(rect, rect, rect, rect, rect, rect)
    
    constructor(
        index: Int
    ) : this(index, index, index, index, index, index)
    
    operator fun get(face: Face): Rectangle {
        return when (face) {
            Face.TOP -> topRect
            Face.BOTTOM -> bottomRect
            Face.RIGHT -> rightRect
            Face.LEFT -> leftRect
            Face.FRONT -> frontRect
            Face.BACK -> backRect
            
            else -> throw IllegalArgumentException("Invalid face")
        }
    }

    companion object {
        private fun getRectForIndex(index: Int): Rectangle {
            return Rectangle(
                (index % 16).toFloat(),
                (index / 16).toFloat(),
                1.0f,
                1.0f
            ) / 16.0f
        }
    }
}
