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

package nw.development.feature.module;

import lombok.Getter;
import nw.development.Client;
import nw.development.events.client.ModuleEnableEvent;
import nw.development.setting.Configurable;
import nw.development.setting.Setting;
import nw.development.util.input.Shortcut;

@Getter
public class Module extends Configurable {
  private final String name;
  private final Category category;
  private final String descriptionKey;
  private final Setting<Boolean> state = insert(new Setting<>("state", false));
  private final Setting<Shortcut> shortcut = insert(new Setting<>("shortcut", Shortcut.NONE));

  public Module(String name, Category category) {
    this.name = name;
    this.category = category;
    this.descriptionKey = "description." + category.getCategoryName() + "." + name.toLowerCase();
  }

  public void toggle() {
    state.setValue(!state.getValue());

    if (state.getValue()) {
      onEnable();
    } else {
      onDisable();
    }
  }

  protected void onEnable() {
    Client.EVENTS.subscribe(this);
    Client.EVENTS.post(new ModuleEnableEvent(this));
  }

  protected void onDisable() {
    Client.EVENTS.unsubscribe(this);
    Client.EVENTS.post(new ModuleEnableEvent(this));
  }
}
