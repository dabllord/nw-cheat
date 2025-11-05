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

import static nw.development.util.render.ExtendedRenderPipelines.POS_COLOR;

import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.*;
import lombok.experimental.UtilityClass;
import net.minecraft.client.render.ProjectionMatrix2;
import nw.development.util.minecraft.MinecraftInstances;

@UtilityClass
public class RenderUtils implements MinecraftInstances {

  private final ProjectionMatrix2 matrix = new ProjectionMatrix2(
    "nw-cheat projection matrix",
    -10,
    100,
    true
  );
  public boolean rendering3d = true;

  public void unscaledProjection() {
    float w = mc.getWindow().getFramebufferWidth();
    float h = mc.getWindow().getFramebufferHeight();

    RenderSystem.setProjectionMatrix(
      matrix.set(w, h),
      ProjectionType.ORTHOGRAPHIC
    );

    rendering3d = false;
  }

  public void scaledProjection() {
    float w =
      (float) mc.getWindow().getFramebufferWidth() /
      mc.getWindow().getScaleFactor();
    float h =
      (float) mc.getWindow().getFramebufferHeight() /
      mc.getWindow().getScaleFactor();

    RenderSystem.setProjectionMatrix(
      matrix.set(w, h),
      ProjectionType.PERSPECTIVE
    );

    rendering3d = true;
  }

  public void drawQuad(
    float x,
    float y,
    float w,
    float h,
    Color rt,
    Color lt,
    Color rb,
    Color lb
  ) {
    MeshBuilder mesh = new MeshBuilder(POS_COLOR);
    mesh.begin();
    mesh.ensureQuadCapacity();

    mesh.quad(
      mesh.vec3(x, y, 0).color(rt).next(),
      mesh.vec3(x, y + h, 0).color(rb).next(),
      mesh.vec3(x + w, y + h, 0).color(lb).next(),
      mesh.vec3(x + w, y, 0).color(lt).next()
    );

    mesh.end();

    MeshRenderer.begin()
      .mesh(mesh)
      .pipeline(POS_COLOR)
      .attachments(mc.getFramebuffer())
      .end();
  }

  public void drawQuad(float x, float y, float w, float h, Color color) {
    drawQuad(x, y, w, h, color, color, color, color);
  }
}
