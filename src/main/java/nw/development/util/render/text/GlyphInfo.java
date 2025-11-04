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

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GlyphInfo {
  private final char unicode;
  private final float advance;
  private final boolean hasGeometry;
  private final float planeLeft, planeBottom, planeRight, planeTop;
  private final float atlasLeft, atlasBottom, atlasRight, atlasTop;

  public GlyphInfo(char unicode, float advance) {
    this.unicode = unicode;
    this.advance = advance;
    this.hasGeometry = false;
    this.planeLeft = this.planeBottom = this.planeRight = this.planeTop = 0;
    this.atlasLeft = this.atlasBottom = this.atlasRight = this.atlasTop = 0;
  }
}
