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

package nw.development.feature.hud;

import lombok.Getter;
import lombok.Setter;
import nw.development.setting.RangedSetting;
import nw.development.setting.ToggleableConfigurable;
import nw.development.util.minecraft.MinecraftInstances;

@Getter
public abstract class HudElement
  extends ToggleableConfigurable
  implements MinecraftInstances {

  private final RangedSetting<Double> x = doubleSetting("x", 0.0, -0.5, 0.5);
  private final RangedSetting<Double> y = doubleSetting("y", 0.0, -0.5, 0.5);

  @Setter
  private double w, h;

  public HudElement(String name) {
    super(name, true);
  }

  public double getPositionX() {
    return (
      ((double) mc.getWindow().getFramebufferWidth() / 2) +
      (mc.getWindow().getFramebufferWidth() * x.getValue())
    );
  }

  public double getPositionY() {
    return (
      ((double) mc.getWindow().getFramebufferHeight() / 2) +
      (mc.getWindow().getFramebufferHeight() * y.getValue())
    );
  }

  public abstract double calcWidth();

  public abstract double calcHeight();
}
