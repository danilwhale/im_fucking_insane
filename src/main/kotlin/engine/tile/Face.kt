package engine.tile

enum class Face(val i: Byte) {
    NONE(0),
    TOP(1),
    BOTTOM(2),
    RIGHT(4),
    LEFT(8),
    FRONT(16),
    BACK(32)
}