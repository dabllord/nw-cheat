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

import static org.lwjgl.system.MemoryUtil.*;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.awt.*;
import java.nio.ByteBuffer;
import lombok.Getter;
import net.minecraft.util.math.Vec3d;
import nw.development.util.minecraft.MinecraftInstances;
import org.lwjgl.BufferUtils;

public class MeshBuilder implements MinecraftInstances {

  private final VertexFormat format;
  private final int primitiveVerticesSize;
  private final int primitiveIndicesCount;

  private ByteBuffer vertices = null;
  private long verticesPointerStart, verticesPointer;

  private ByteBuffer indices = null;
  private long indicesPointer;

  private int vertexIndex;

  @Getter
  private int indicesCount;

  @Getter
  private boolean building;

  private double cameraX, cameraZ;

  public MeshBuilder(RenderPipeline pipeline) {
    this(pipeline.getVertexFormat(), pipeline.getVertexFormatMode());
  }

  public MeshBuilder(VertexFormat format, VertexFormat.DrawMode drawMode) {
    this.format = format;
    primitiveVerticesSize = format.getVertexSize();
    primitiveIndicesCount = drawMode.firstVertexCount;
  }

  public MeshBuilder(
    VertexFormat format,
    VertexFormat.DrawMode drawMode,
    int vertexCount,
    int indexCount
  ) {
    this(format, drawMode);
    allocateBuffers(vertexCount, indexCount);
  }

  public void begin() {
    if (building) throw new IllegalStateException(
      "begin called while already building."
    );

    verticesPointer = verticesPointerStart;
    vertexIndex = 0;
    indicesCount = 0;

    if (RenderUtils.rendering3d) {
      Vec3d camera = mc.gameRenderer.getCamera().getPos();

      cameraX = camera.x;
      cameraZ = camera.z;
    } else {
      cameraX = 0;
      cameraZ = 0;
    }

    building = true;
  }

  public MeshBuilder vec3(float x, float y, float z) {
    long p = verticesPointer;

    memPutFloat(p, (float) (x - cameraX));
    memPutFloat(p + 4, y);
    memPutFloat(p + 8, (float) (z - cameraZ));

    verticesPointer += 12;
    return this;
  }

  public MeshBuilder vec2(float x, float y) {
    long p = verticesPointer;

    memPutFloat(p, x);
    memPutFloat(p + 4, y);

    verticesPointer += 8;
    return this;
  }

  public MeshBuilder color(Color c) {
    long p = verticesPointer;

    memPutByte(p, (byte) c.getRed());
    memPutByte(p + 1, (byte) c.getGreen());
    memPutByte(p + 2, (byte) c.getBlue());
    memPutByte(p + 3, (byte) c.getAlpha());

    verticesPointer += 4;
    return this;
  }

  public int next() {
    return vertexIndex++;
  }

  public void line(int i1, int i2) {
    long p = indicesPointer + indicesCount * 4L;

    memPutInt(p, i1);
    memPutInt(p + 4, i2);

    indicesCount += 2;
  }

  public void quad(int i1, int i2, int i3, int i4) {
    long p = indicesPointer + indicesCount * 4L;

    memPutInt(p, i1);
    memPutInt(p + 4, i2);
    memPutInt(p + 8, i3);

    memPutInt(p + 12, i3);
    memPutInt(p + 16, i4);
    memPutInt(p + 20, i1);

    indicesCount += 6;
  }

  public void triangle(int i1, int i2, int i3) {
    long p = indicesPointer + indicesCount * 4L;

    memPutInt(p, i1);
    memPutInt(p + 4, i2);
    memPutInt(p + 8, i3);

    indicesCount += 3;
  }

  public void ensureQuadCapacity() {
    ensureCapacity(4, 6);
  }

  public void ensureTriCapacity() {
    ensureCapacity(3, 3);
  }

  public void ensureLineCapacity() {
    ensureCapacity(2, 2);
  }

  public void ensureCapacity(int vertexCount, int indexCount) {
    if (indexCount % primitiveIndicesCount != 0) {
      throw new IllegalArgumentException(
        "unexpected amount of indices written to builder."
      );
    }

    if (vertices == null || indices == null) {
      allocateBuffers(256 * 4, 512 * 4);
      return;
    }

    if (
      (vertexIndex + vertexCount) * primitiveVerticesSize >= vertices.capacity()
    ) {
      int offset = getVerticesOffset();
      int newSize = Math.max(
        vertices.capacity() * 2,
        vertices.capacity() + vertexCount * primitiveVerticesSize
      );
      ByteBuffer newVertices = BufferUtils.createByteBuffer(newSize);
      memCopy(memAddress0(vertices), memAddress0(newVertices), offset);

      vertices = newVertices;
      verticesPointerStart = memAddress0(vertices);
      verticesPointer = verticesPointerStart + offset;
    }

    if ((indicesCount + indexCount) * Integer.BYTES >= indices.capacity()) {
      int newSize = Math.max(
        indices.capacity() * 2,
        indices.capacity() + indexCount * Integer.BYTES
      );

      ByteBuffer newIndices = BufferUtils.createByteBuffer(newSize);
      memCopy(memAddress0(indices), memAddress0(newIndices), indicesCount * 4L);

      indices = newIndices;
      indicesPointer = memAddress0(indices);
    }
  }

  private void allocateBuffers(int vertexCount, int indexCount) {
    vertices = BufferUtils.createByteBuffer(
      primitiveVerticesSize * vertexCount
    );
    verticesPointer = verticesPointerStart = memAddress0(vertices);

    indices = BufferUtils.createByteBuffer(indexCount * Integer.BYTES);
    indicesPointer = memAddress0(indices);
  }

  public void end() {
    if (!building) throw new IllegalStateException(
      "end called while not building."
    );

    building = false;
  }

  public GpuBuffer getVertexBuffer() {
    vertices.limit(getVerticesOffset());
    return format.uploadImmediateVertexBuffer(vertices);
  }

  public GpuBuffer getIndexBuffer() {
    indices.limit(indicesCount * Integer.BYTES);
    return format.uploadImmediateIndexBuffer(indices);
  }

  private int getVerticesOffset() {
    return (int) (verticesPointer - verticesPointerStart);
  }
}
