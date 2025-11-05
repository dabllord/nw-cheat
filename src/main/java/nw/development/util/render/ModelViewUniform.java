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

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import java.nio.ByteBuffer;
import net.minecraft.client.gl.DynamicUniformStorage;
import org.joml.Matrix4f;

public class ModelViewUniform {

  public static final int SIZE = new Std140SizeCalculator().putMat4f().get();

  private static final Data DATA = new Data();

  private static final DynamicUniformStorage<Data> STORAGE =
    new DynamicUniformStorage<>("nw-cheat - model view ubo", SIZE, 16);

  public static void flipFrame() {
    STORAGE.clear();
  }

  public static GpuBufferSlice write(Matrix4f modelView) {
    DATA.modelView = modelView;

    return STORAGE.write(DATA);
  }

  private static final class Data implements DynamicUniformStorage.Uploadable {

    private Matrix4f modelView;

    @Override
    public void write(ByteBuffer buffer) {
      Std140Builder.intoBuffer(buffer).putMat4f(modelView);
    }

    @Override
    public boolean equals(Object o) {
      return false;
    }
  }
}
