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
      /*padding between bg and gradient*/ 12 +
      /*padding for text*/ 21 +
      UBUNTU.getWidth("nw-cheat" + mc.getCurrentFps() + "fps", 25f)
    );
  }

  @Override
  public double calcHeight() {
    return (
      /*padding between bg and gradient*/ 12 +
      /*padding for text*/ 10 +
      UBUNTU.getHeight(25f)
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
    RenderUtils.drawQuad(x + 6, y + 6, w - 6, h - 6, new Color(0, 0, 0, 178));
  }
}
