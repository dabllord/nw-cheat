/*
 * Copyright (C) 2025 mishasigmagucci
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package nw.development.setting;

import lombok.Getter;

@Getter
public class RangedSetting<T extends Number & Comparable<T>> extends Setting<T> {
  private final T min, max;

  public RangedSetting(String name, T defaultValue, T min, T max) {
    super(name, defaultValue);

    this.min = min;
    this.max = max;

    if (defaultValue.compareTo(min) < 0 || defaultValue.compareTo(max) > 0) {
      throw new IllegalArgumentException("default value out of range: " + defaultValue);
    }
  }

  @Override
  public void setValue(T newValue) {
    if (newValue.compareTo(min) < 0) {
      newValue = min;
    }

    if (newValue.compareTo(max) > 0) {
      newValue = max;
    }

    super.setValue(newValue);
  }
}
