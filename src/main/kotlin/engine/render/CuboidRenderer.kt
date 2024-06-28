package engine.render

import engine.base.BoundingBox
import engine.base.Color
import engine.base.ShaderProgram
import engine.tile.Face
import engine.tile.render.DefaultTileFaceBuilder
import engine.tile.render.TileFaces

object CuboidRenderer {
    fun render(vb: VertexBuilder, bounds: BoundingBox, faces: TileFaces, tint: Color, shader: ShaderProgram? = null, applyShadows: Boolean = true) {
        vb.begin(24, 36)

        DefaultTileFaceBuilder.buildTopFace(
            vb,
            bounds, faces[Face.TOP],
            tint, applyShadows
        )

        DefaultTileFaceBuilder.buildBottomFace(
            vb,
            bounds, faces[Face.BOTTOM],
            tint, applyShadows
        )

        DefaultTileFaceBuilder.buildRightFace(
            vb,
            bounds, faces[Face.RIGHT],
            tint, applyShadows
        )

        DefaultTileFaceBuilder.buildLeftFace(
            vb,
            bounds, faces[Face.LEFT],
            tint, applyShadows
        )

        DefaultTileFaceBuilder.buildFrontFace(
            vb,
            bounds, faces[Face.FRONT],
            tint, applyShadows
        )

        DefaultTileFaceBuilder.buildBackFace(
            vb,
            bounds, faces[Face.BACK],
            tint, applyShadows
        )
        
        vb.flush(shader ?: ShaderProgram.getDefault())
    }
}