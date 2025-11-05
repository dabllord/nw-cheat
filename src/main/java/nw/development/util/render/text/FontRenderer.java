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

package nw.development.util.render.text;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import nw.development.util.minecraft.MinecraftInstances;
import nw.development.util.render.ExtendedRenderPipelines;
import nw.development.util.render.MeshBuilder;
import nw.development.util.render.MeshRenderer;
import org.joml.Matrix4f;

public class FontRenderer implements MinecraftInstances {

  private static final Gson GSON = new Gson();

  @Getter
  private final GpuTextureView atlasTexture;

  private final Map<Character, GlyphInfo> glyphs;
  private final float atlasWidth;
  private final float atlasHeight;
  private final FontMetrics metrics;

  public FontRenderer(Identifier atlasTexture, Identifier atlasJson) {
    this.atlasTexture = loadTexture(atlasTexture);

    JsonObject json = loadJson(atlasJson);
    JsonObject atlas = json.getAsJsonObject("atlas");
    this.atlasWidth = atlas.get("width").getAsFloat();
    this.atlasHeight = atlas.get("height").getAsFloat();

    JsonObject metricsJson = json.getAsJsonObject("metrics");
    this.metrics = new FontMetrics(
      metricsJson.get("emSize").getAsFloat(),
      metricsJson.get("lineHeight").getAsFloat(),
      metricsJson.get("ascender").getAsFloat(),
      metricsJson.get("descender").getAsFloat(),
      metricsJson.get("underlineY").getAsFloat(),
      metricsJson.get("underlineThickness").getAsFloat()
    );

    this.glyphs = new HashMap<>();
    JsonArray glyphsArray = json.getAsJsonArray("glyphs");

    for (int i = 0; i < glyphsArray.size(); i++) {
      JsonObject glyph = glyphsArray.get(i).getAsJsonObject();
      char unicode = (char) glyph.get("unicode").getAsInt();
      float advance = glyph.get("advance").getAsFloat();

      JsonObject planeBounds = glyph.has("planeBounds")
        ? glyph.getAsJsonObject("planeBounds")
        : null;
      JsonObject atlasBounds = glyph.has("atlasBounds")
        ? glyph.getAsJsonObject("atlasBounds")
        : null;

      if (planeBounds != null && atlasBounds != null) {
        GlyphInfo info = new GlyphInfo(
          unicode,
          advance,
          true,
          planeBounds.get("left").getAsFloat(),
          planeBounds.get("bottom").getAsFloat(),
          planeBounds.get("right").getAsFloat(),
          planeBounds.get("top").getAsFloat(),
          atlasBounds.get("left").getAsFloat(),
          atlasBounds.get("bottom").getAsFloat(),
          atlasBounds.get("right").getAsFloat(),
          atlasBounds.get("top").getAsFloat()
        );
        glyphs.put(unicode, info);
      } else {
        glyphs.put(unicode, new GlyphInfo(unicode, advance));
      }
    }
  }

  public void drawText(String text, float x, float y, float size, Color color) {
    drawText(null, text, x, y, size, color);
  }

  public void drawText(
    Matrix4f transform,
    String text,
    float x,
    float y,
    float size,
    Color color
  ) {
    float scale = size / metrics.getEmSize();
    float baseline = y + metrics.getAscender() * scale;
    float cursorX = x;

    for (char c : text.toCharArray()) {
      GlyphInfo glyph = glyphs.get(c);
      if (glyph == null) {
        glyph = glyphs.get('?');
        if (glyph == null) continue;
      }

      if (glyph.isHasGeometry()) {
        MeshBuilder mesh = new MeshBuilder(ExtendedRenderPipelines.MSDF);
        mesh.begin();
        mesh.ensureQuadCapacity();

        float x0 = cursorX + glyph.getPlaneLeft() * scale;
        float y0 = baseline - glyph.getPlaneBottom() * scale;
        float x1 = cursorX + glyph.getPlaneRight() * scale;
        float y1 = baseline - glyph.getPlaneTop() * scale;

        float u0 = glyph.getAtlasLeft() / atlasWidth;
        float u1 = glyph.getAtlasRight() / atlasWidth;
        float v0 = 1.0f - (glyph.getAtlasTop() / atlasHeight);
        float v1 = 1.0f - (glyph.getAtlasBottom() / atlasHeight);

        mesh.quad(
          mesh.vec3(x0, y1, 0).vec2(u0, v0).color(color).next(),
          mesh.vec3(x1, y1, 0).vec2(u1, v0).color(color).next(),
          mesh.vec3(x1, y0, 0).vec2(u1, v1).color(color).next(),
          mesh.vec3(x0, y0, 0).vec2(u0, v1).color(color).next()
        );

        mesh.end();

        MeshRenderer.begin()
          .attachments(mc.getFramebuffer())
          .mesh(mesh)
          .pipeline(ExtendedRenderPipelines.MSDF)
          .sampler("u_Texture", atlasTexture)
          .transform(transform)
          .end();
      }

      cursorX += glyph.getAdvance() * scale;
    }
  }

  public float getWidth(String text, float size) {
    float scale = size / metrics.getEmSize();
    float width = 0;

    for (char c : text.toCharArray()) {
      GlyphInfo glyph = glyphs.get(c);
      if (glyph == null) {
        glyph = glyphs.get('?');
        if (glyph == null) continue;
      }
      width += glyph.getAdvance() * scale;
    }

    return width;
  }

  public float getHeight(float size) {
    float scale = size / metrics.getEmSize();
    return (metrics.getAscender() - metrics.getDescender()) * scale;
  }

  private GpuTextureView loadTexture(Identifier id) {
    try {
      InputStream stream = mc
        .getResourceManager()
        .getResource(id)
        .orElseThrow()
        .getInputStream();

      NativeImage image = NativeImage.read(stream);
      NativeImageBackedTexture texture = new NativeImageBackedTexture(
        id::getPath,
        image
      );
      texture.getGlTexture().setTextureFilter(FilterMode.LINEAR, false);
      mc.getTextureManager().registerTexture(id, texture);

      return texture.getGlTextureView();
    } catch (Exception e) {
      throw new RuntimeException("failed to load msdf texture: " + id, e);
    }
  }

  private JsonObject loadJson(Identifier id) {
    try (
      InputStream stream = mc
        .getResourceManager()
        .getResource(id)
        .orElseThrow()
        .getInputStream();
      InputStreamReader reader = new InputStreamReader(
        stream,
        StandardCharsets.UTF_8
      )
    ) {
      return GSON.fromJson(reader, JsonObject.class);
    } catch (Exception e) {
      throw new RuntimeException("failed to load msdf atlas json: " + id, e);
    }
  }
}
