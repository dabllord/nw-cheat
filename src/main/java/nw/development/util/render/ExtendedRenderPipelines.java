/*
 * free open-source fabric based cheat-client
 * Copyright (C) 2025 mishasigmagucci
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package nw.development.util.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.render.VertexFormats;
import nw.development.util.resource.ResourceUtils;

public class ExtendedRenderPipelines {
  private static final RenderPipeline.Snippet MESH_SNIPPET =
      RenderPipeline.builder()
          .withUniform("ModelView", UniformType.UNIFORM_BUFFER)
          .withUniform("Projection", UniformType.UNIFORM_BUFFER)
          .buildSnippet();

  public static final RenderPipeline POS_COLOR =
      insert(
          RenderPipeline.builder(MESH_SNIPPET)
              .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.TRIANGLES)
              .withLocation(ResourceUtils.getOf("pipeline/pos_color_culled"))
              .withFragmentShader(ResourceUtils.getOf("core/pos_color"))
              .withVertexShader(ResourceUtils.getOf("core/pos_color"))
              .withBlend(BlendFunction.TRANSLUCENT)
              .withDepthWrite(false)
              .withCull(true)
              .build());

  public static final RenderPipeline POS_TEX_COLOR =
      insert(
          RenderPipeline.builder(MESH_SNIPPET)
              .withVertexFormat(
                  VertexFormats.POSITION_TEXTURE_COLOR, VertexFormat.DrawMode.TRIANGLES)
              .withLocation(ResourceUtils.getOf("pipeline/pos_color_culled"))
              .withFragmentShader(ResourceUtils.getOf("core/pos_tex_color"))
              .withVertexShader(ResourceUtils.getOf("core/pos_tex_color"))
              .withBlend(BlendFunction.TRANSLUCENT)
              .withDepthWrite(false)
              .withSampler("u_Texture")
              .withCull(true)
              .build());

  private static RenderPipeline insert(RenderPipeline pipeline) {
    RenderPipelines.register(pipeline);
    return pipeline;
  }
}
