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

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.awt.*;
import java.util.HashMap;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import nw.development.util.minecraft.MinecraftInstances;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class MeshRenderer implements MinecraftInstances {

  private static final MeshRenderer INSTANCE = new MeshRenderer();

  private static boolean taken;

  private GpuTextureView colorAttachment;
  private GpuTextureView depthAttachment;
  private RenderPipeline pipeline;
  private @Nullable MeshBuilder mesh;
  private @Nullable GpuBuffer vertexBuffer;
  private @Nullable GpuBuffer indexBuffer;
  private Matrix4f matrix;
  private final HashMap<String, GpuBufferSlice> uniforms = new HashMap<>();
  private final HashMap<String, GpuTextureView> samplers = new HashMap<>();

  private MeshRenderer() {}

  public static MeshRenderer begin() {
    if (taken) throw new IllegalStateException(
      "Previous instance of MeshRenderer was not ended"
    );

    taken = true;
    return INSTANCE;
  }

  public MeshRenderer attachments(GpuTextureView color, GpuTextureView depth) {
    colorAttachment = color;
    depthAttachment = depth;
    return this;
  }

  public MeshRenderer attachments(Framebuffer framebuffer) {
    colorAttachment = framebuffer.getColorAttachmentView();
    depthAttachment = framebuffer.getDepthAttachmentView();
    return this;
  }

  public MeshRenderer pipeline(RenderPipeline pipeline) {
    this.pipeline = pipeline;
    return this;
  }

  public MeshRenderer mesh(GpuBuffer vertices, GpuBuffer indices) {
    this.vertexBuffer = vertices;
    this.indexBuffer = indices;
    return this;
  }

  public MeshRenderer mesh(MeshBuilder mesh) {
    this.mesh = mesh;
    return this;
  }

  public MeshRenderer mesh(MeshBuilder mesh, Matrix4f matrix) {
    this.mesh = mesh;
    return this.transform(matrix);
  }

  public MeshRenderer mesh(MeshBuilder mesh, MatrixStack matrices) {
    this.mesh = mesh;
    return this.transform(matrices);
  }

  public MeshRenderer transform(Matrix4f matrix) {
    this.matrix = matrix;
    return this;
  }

  public MeshRenderer transform(MatrixStack matrices) {
    this.matrix = matrices.peek().getPositionMatrix();
    return this;
  }

  public MeshRenderer uniform(String name, GpuBufferSlice slice) {
    uniforms.put(name, slice);
    return this;
  }

  public MeshRenderer sampler(String name, GpuTextureView view) {
    if (name != null && view != null) {
      samplers.put(name, view);
    }

    return this;
  }

  public void end() {
    if (mesh != null && mesh.isBuilding()) {
      mesh.end();
    }

    int indexCount = mesh != null
      ? mesh.getIndicesCount()
      : (indexBuffer != null ? indexBuffer.size() / Integer.BYTES : -1);

    if (indexCount > 0) {
      if (RenderUtils.rendering3d || matrix != null) {
        RenderSystem.getModelViewStack().pushMatrix();
      }

      if (matrix != null) {
        RenderSystem.getModelViewStack().mul(matrix);
      }

      if (RenderUtils.rendering3d) {
        applyCameraPos();
      }

      GpuBuffer vertexBuffer = mesh != null
        ? mesh.getVertexBuffer()
        : this.vertexBuffer;
      GpuBuffer indexBuffer = mesh != null
        ? mesh.getIndexBuffer()
        : this.indexBuffer;

      {
        GpuBufferSlice modelView = ModelViewUniform.write(
          RenderSystem.getModelViewStack()
        );

        RenderPass pass = (depthAttachment != null &&
            pipeline.wantsDepthTexture())
          ? RenderSystem.getDevice()
              .createCommandEncoder()
              .createRenderPass(
                () -> "nw-cheat renderer",
                colorAttachment,
                OptionalInt.empty(),
                depthAttachment,
                OptionalDouble.empty()
              )
          : RenderSystem.getDevice()
              .createCommandEncoder()
              .createRenderPass(
                () -> "nw-cheat renderer",
                colorAttachment,
                OptionalInt.empty()
              );

        pass.setPipeline(pipeline);
        pass.setUniform("ModelView", modelView);
        pass.setUniform("Projection", RenderSystem.getProjectionMatrixBuffer());

        for (var name : uniforms.keySet()) {
          pass.setUniform(name, uniforms.get(name));
        }

        for (var name : samplers.keySet()) {
          pass.bindSampler(name, samplers.get(name));
        }

        pass.setVertexBuffer(0, vertexBuffer);
        pass.setIndexBuffer(indexBuffer, VertexFormat.IndexType.INT);
        pass.drawIndexed(0, 0, indexCount, 1);

        pass.close();
      }

      if (RenderUtils.rendering3d || matrix != null) {
        RenderSystem.getModelViewStack().popMatrix();
      }
    }

    colorAttachment = null;
    depthAttachment = null;
    pipeline = null;
    mesh = null;
    vertexBuffer = null;
    indexBuffer = null;
    matrix = null;
    uniforms.clear();
    samplers.clear();

    taken = false;
  }

  private static void applyCameraPos() {
    Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();
    RenderSystem.getModelViewStack().translate(0, (float) -cameraPos.y, 0);
  }
}
