package engine.render

data class Vertex(
    val x: Float, // position
    val y: Float,
    val z: Float,
    
    val r: Float, // color
    val g: Float,
    val b: Float,
    
    val u: Float, // texcoords
    val v: Float
)