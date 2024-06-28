package engine.tile.render

import engine.base.BoundingBox
import engine.base.Color
import engine.base.Rectangle
import engine.render.VertexBuilder
import org.joml.Vector3f

object DefaultTileFaceBuilder {
    const val LIGHTEST = 1.0f
    const val LIGHTER = 0.8f
    const val LIGHT = 0.6f
    const val DARKEN = 0.4f
    
    fun buildTopFace(vb: VertexBuilder, bounds: BoundingBox, rect: Rectangle, tint: Color, applyShadows: Boolean = true) {
        val min = bounds.min
        val max = bounds.max
        val (u, v, w, h) = rect
        val (cr, cg, cb) = if (applyShadows) tint * LIGHTEST else tint
        
        val a = vb.addVertex(min.x, max.y, min.z, u, v, cr, cg, cb)
        val b = vb.addVertex(min.x, max.y, max.z, u, v + h, cr, cg, cb)
        val c = vb.addVertex(max.x, max.y, max.z, u + w, v + h, cr, cg, cb)
        val d = vb.addVertex(max.x, max.y, min.z, u + w, v, cr, cg, cb)

        vb.addIndices(a, b, c, a, c, d)
    }
    
    fun buildBottomFace(vb: VertexBuilder, bounds: BoundingBox, rect: Rectangle, tint: Color, applyShadows: Boolean = true) {
        val min = bounds.min
        val max = bounds.max
        val (u, v, w, h) = rect
        val (cr, cg, cb) = if (applyShadows) tint * DARKEN else tint
        
        val a = vb.addVertex(min.x, min.y, min.z, u + w, v, cr, cg, cb)
        val b = vb.addVertex(max.x, min.y, min.z, u, v, cr, cg, cb)
        val c = vb.addVertex(max.x, min.y, max.z, u, v + h, cr, cg, cb)
        val d = vb.addVertex(min.x, min.y, max.z, u + w, v + h, cr, cg, cb)

        vb.addIndices(a, b, c, a, c, d)
    }

    fun buildRightFace(vb: VertexBuilder, bounds: BoundingBox, rect: Rectangle, tint: Color, applyShadows: Boolean = true) {
        val min = bounds.min
        val max = bounds.max
        val (u, v, w, h) = rect
        val (cr, cg, cb) = if (applyShadows) tint * LIGHTER else tint

        val a = vb.addVertex(max.x, min.y, min.z, u + w, v + h, cr, cg, cb)
        val b = vb.addVertex(max.x, max.y, min.z, u + w, v, cr, cg, cb)
        val c = vb.addVertex(max.x, max.y, max.z, u, v, cr, cg, cb)
        val d = vb.addVertex(max.x, min.y, max.z, u, v + h, cr, cg, cb)

        vb.addIndices(a, b, c, a, c, d)
    }

    fun buildLeftFace(vb: VertexBuilder, bounds: BoundingBox, rect: Rectangle, tint: Color, applyShadows: Boolean = true) {
        val min = bounds.min
        val max = bounds.max
        val (u, v, w, h) = rect
        val (cr, cg, cb) = if (applyShadows) tint * LIGHTER else tint

        val a = vb.addVertex(min.x, min.y, min.z, u, v + h, cr, cg, cb)
        val b = vb.addVertex(min.x, min.y, max.z, u + w, v + h, cr, cg, cb)
        val c = vb.addVertex(min.x, max.y, max.z, u + w, v, cr, cg, cb)
        val d = vb.addVertex(min.x, max.y, min.z, u, v, cr, cg, cb)

        vb.addIndices(a, b, c, a, c, d)
    }

    fun buildFrontFace(vb: VertexBuilder, bounds: BoundingBox, rect: Rectangle, tint: Color, applyShadows: Boolean = true) {
        val min = bounds.min
        val max = bounds.max
        val (u, v, w, h) = rect
        val (cr, cg, cb) = if (applyShadows) tint * LIGHT else tint

        val a = vb.addVertex(min.x, min.y, max.z, u, v + h, cr, cg, cb)
        val b = vb.addVertex(max.x, min.y, max.z, u + w, v + h, cr, cg, cb)
        val c = vb.addVertex(max.x, max.y, max.z, u + w, v, cr, cg, cb)
        val d = vb.addVertex(min.x, max.y, max.z, u, v, cr, cg, cb)

        vb.addIndices(a, b, c, a, c, d)
    }

    fun buildBackFace(vb: VertexBuilder, bounds: BoundingBox, rect: Rectangle, tint: Color, applyShadows: Boolean = true) {
        val min = bounds.min
        val max = bounds.max
        val (u, v, w, h) = rect
        val (cr, cg, cb) = if (applyShadows) tint * LIGHT else tint

        val a = vb.addVertex(min.x, min.y, min.z, u + w, v + h, cr, cg, cb)
        val b = vb.addVertex(min.x, max.y, min.z, u + w, v, cr, cg, cb)
        val c = vb.addVertex(max.x, max.y, min.z, u, v, cr, cg, cb)
        val d = vb.addVertex(max.x, min.y, min.z, u, v + h, cr, cg, cb)

        vb.addIndices(a, b, c, a, c, d)
    }
}
