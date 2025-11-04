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

import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.ProjectionMatrix2;
import nw.development.util.minecraft.MinecraftInstances;

public class RenderUtils implements MinecraftInstances {
  private static final ProjectionMatrix2 matrix =
      new ProjectionMatrix2("nw-cheat projection matrix", -10, 100, true);
  public static boolean rendering3d = true;

  public static void unscaledProjection() {
    float w = mc.getWindow().getFramebufferWidth();
    float h = mc.getWindow().getFramebufferHeight();

    RenderSystem.setProjectionMatrix(matrix.set(w, h), ProjectionType.ORTHOGRAPHIC);

    rendering3d = false;
  }

  public static void scaledProjection() {
    float w = (float) mc.getWindow().getFramebufferWidth() / mc.getWindow().getScaleFactor();
    float h = (float) mc.getWindow().getFramebufferHeight() / mc.getWindow().getScaleFactor();

    RenderSystem.setProjectionMatrix(matrix.set(w, h), ProjectionType.PERSPECTIVE);

    rendering3d = true;
  }
}
