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

package nw.development.feature.hud.elements;

import static nw.development.Client.MODULES;
import static nw.development.util.render.text.Fonts.*;

import java.awt.*;
import meteordevelopment.orbit.EventHandler;
import nw.development.events.render.RenderHudEvent;
import nw.development.feature.hud.HudElement;
import nw.development.feature.module.client.HudModule;
import nw.development.util.render.RenderUtils;

public class WatermarkElement extends HudElement {

  public WatermarkElement() {
    super("watermark");
  }

  @Override
  public double calcWidth() {
    return (
      /*padding between bg and gradient*/ 6 +
      /*padding for text*/ 22 +
      ROBOTO.getWidth("nw-cheat" + mc.getCurrentFps(), 18f) +
      ROBOTO.getWidth("fps", 13f)
    );
  }

  @Override
  public double calcHeight() {
    return (
      /*padding between bg and gradient*/ 6 +
      /*padding for text*/ 10 +
      ROBOTO.getHeight(18f)
    );
  }

  @EventHandler
  private void onRenderHud(RenderHudEvent event) {
    float x = (float) getPositionX(),
      y = (float) getPositionY(),
      w = (float) calcWidth(),
      h = (float) calcHeight();
    Color accentColor = MODULES.get(HudModule.class)
      .getAccentColor()
      .getValue();

    float textHeight = ROBOTO.getHeight(18f),
      textWidth = ROBOTO.getWidth("nw-cheat", 18f);

    RenderUtils.drawQuad(
      x,
      y,
      w,
      h,
      accentColor,
      accentColor,
      new Color(115, 115, 155, 0),
      new Color(115, 115, 155, 0)
    );
    RenderUtils.drawQuad(x + 3, y + 3, w - 6, h - 6, new Color(0, 0, 0, 178));

    ROBOTO.drawText(
      "nw-cheat",
      x + 8,
      y + h / 2 - textHeight / 2,
      18f,
      Color.WHITE
    );
    RenderUtils.drawQuad(
      x + 8 + textWidth + 5,
      y + h / 2 - 5,
      2,
      11,
      new Color(178, 178, 178, 178)
    );
    ROBOTO.drawText(
      String.valueOf(mc.getCurrentFps()),
      x + 8 + textWidth + 12,
      y + h / 2 - textHeight / 2,
      18f,
      Color.WHITE
    );
    ROBOTO.drawText(
      "fps",
      x +
        8 +
        textWidth +
        12 +
        ROBOTO.getWidth(String.valueOf(mc.getCurrentFps()), 18f),
      y + h / 2 + textHeight / 2 - ROBOTO.getHeight(13f),
      13f,
      new Color(178, 178, 178, 178)
    );
  }
}
