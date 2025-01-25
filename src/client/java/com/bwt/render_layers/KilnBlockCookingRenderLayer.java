package com.bwt.render_layers;

import com.bwt.utils.Id;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class KilnBlockCookingRenderLayer {
    private static final Function<Identifier, RenderLayer> KILN_COOKING_RENDER_FUNCTION = Util.memoize(
            texture -> {
                RenderPhase.Texture texture2 = new RenderPhase.Texture(texture, false, false);
                return RenderLayer.of(
                        "kiln_cooking",
                        VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL,
                        VertexFormat.DrawMode.QUADS,
                        1536,
                        false,
                        true,
                        RenderLayer.MultiPhaseParameters.builder()
                                .program(RenderLayer.CRUMBLING_PROGRAM)
                                .texture(texture2)
                                .transparency(RenderLayer.CRUMBLING_TRANSPARENCY)
                                .writeMaskState(RenderLayer.COLOR_MASK)
                                .layering(RenderLayer.POLYGON_OFFSET_LAYERING)
                                .build(false)
                );
            }
    );

    public static final List<Identifier> KILN_COOKING_STAGES = IntStream.range(0, 10)
            .mapToObj(stage -> Id.of("block/kiln_cook_stage_" + stage))
            .collect(Collectors.toList());
    public static final List<Identifier> KILN_COOKING_STAGE_TEXTURES = KILN_COOKING_STAGES.stream()
            .map(id -> id.withPath(path -> "textures/" + path + ".png"))
            .collect(Collectors.toList());
    public static final List<RenderLayer> KILN_COOKING_RENDER_LAYERS = KILN_COOKING_STAGE_TEXTURES.stream()
            .map(KILN_COOKING_RENDER_FUNCTION)
            .collect(Collectors.toList());


}
