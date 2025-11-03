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

import java.util.ArrayList;
import nw.development.util.input.Shortcut;

public class Configurable extends Setting<ArrayList<Setting<?>>> {
  public Configurable(String name, ArrayList<Setting<?>> defaultValue) {
    super(name, defaultValue);
  }

  public Configurable(String name) {
    this(name, new ArrayList<>());
  }

  protected Setting<Boolean> booleanSetting(String name, boolean defaultValue) {
    return insert(new Setting<>(name, defaultValue));
  }

  protected Setting<Shortcut> shortcutSetting(String name, Shortcut defaultValue) {
    return insert(new Setting<>(name, defaultValue));
  }

  protected Setting<ArrayList<Setting<?>>> configurableSetting(Configurable configurable) {
    return insert(configurable);
  }

  private <T> Setting<T> insert(Setting<T> setting) {
    getValue().add(setting);
    return setting;
  }

  public void updateChildState(boolean newState) {
    for (Setting<?> setting : getValue()) {
      if (setting instanceof ToggleableConfigurable toggleableConfigurable) {
        toggleableConfigurable.getState().setValue(newState);

        toggleableConfigurable.updateChildState(newState);
      }

      if (setting instanceof Configurable configurable) {
        configurable.updateChildState(newState);
      }
    }
  }
}
