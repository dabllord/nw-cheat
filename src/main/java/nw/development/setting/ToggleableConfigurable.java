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

package nw.development.setting;

import java.util.List;
import lombok.Getter;

public abstract class ToggleableConfigurable extends Configurable {
  @Getter private final Setting<Boolean> state;

  public ToggleableConfigurable(String name, List<Setting<?>> defaultValue, boolean defaultState) {
    super(name, defaultValue);

    state = booleanSetting("state", defaultState);
  }

  public ToggleableConfigurable(String name, boolean defaultState) {
    this(name, List.of(), defaultState);
  }

  public ToggleableConfigurable(String name) {
    this(name, List.of(), false);
  }

  protected abstract void onEnable();

  protected abstract void onDisable();

  public void toggle() {
    state.setValue(!state.getValue());

    updateChildState(state.getValue());

    if (state.getValue()) {
      onEnable();
    } else {
      onDisable();
    }
  }
}
