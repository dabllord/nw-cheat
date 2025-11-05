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

package nw.development.feature.module;

import static nw.development.Client.EVENTS;

import lombok.Getter;
import nw.development.events.client.ModuleEnableEvent;
import nw.development.setting.Setting;
import nw.development.setting.ToggleableConfigurable;
import nw.development.util.input.Shortcut;
import nw.development.util.lang.Languages;

public abstract class Module extends ToggleableConfigurable {

  @Getter
  private final Category category;

  @Getter
  private final Setting<Shortcut> shortcut = shortcutSetting(
    "shortcut",
    Shortcut.NONE
  );

  private final String descriptionKey;

  public Module(String name, boolean defaultState, Category category) {
    super(name, defaultState);
    this.category = category;
    this.descriptionKey =
      "description." + category.getCategoryName() + "." + name.toLowerCase();
  }

  public String getDescription() {
    return Languages.CURRENT.getTranslated(descriptionKey);
  }

  @Override
  protected void onEnable() {
    EVENTS.subscribe(this);
    EVENTS.post(new ModuleEnableEvent(this));
  }

  @Override
  protected void onDisable() {
    EVENTS.unsubscribe(this);
    EVENTS.post(new ModuleEnableEvent(this));
  }
}
