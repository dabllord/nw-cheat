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

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import meteordevelopment.orbit.EventHandler;
import nw.development.Client;
import nw.development.events.game.KeyboardKeyEvent;
import nw.development.events.game.MouseButtonEvent;
import nw.development.util.input.Shortcut;

public class Modules {
  @Getter private final List<Module> modules = new ArrayList<>();

  public Modules() {
    Client.EVENTS.subscribe(this);
  }

  public <T extends Module> T get(Class<T> clazz) throws IllegalArgumentException {
    for (Module module : modules) {
      if (clazz.isInstance(module)) {
        return clazz.cast(module);
      }
    }

    throw new IllegalArgumentException("no module registered for class: " + clazz.getSimpleName());
  }

  @EventHandler
  private void onKeyboardKey(KeyboardKeyEvent event) {
    if (event.getAction() == GLFW_REPEAT) {
      return;
    }

    for (Module module : modules) {
      Shortcut shortcut = module.getShortcut().getValue();

      if (shortcut.getCode() != event.getCode() || shortcut.isMouse()) {
        continue;
      }

      handleModuleShortcut(shortcut, module, event.getAction());
    }
  }

  @EventHandler
  private void onMouseButton(MouseButtonEvent event) {
    if (event.getAction() == GLFW_REPEAT) {
      return;
    }

    for (Module module : modules) {
      Shortcut shortcut = module.getShortcut().getValue();

      if (shortcut.getCode() != event.getButton() || !shortcut.isMouse()) {
        continue;
      }

      handleModuleShortcut(shortcut, module, event.getAction());
    }
  }

  private static void handleModuleShortcut(Shortcut shortcut, Module module, int event) {
    switch (shortcut.getAction()) {
      case HOLD -> module.toggle();
      case PRESS -> {
        if (event == GLFW_PRESS) {
          module.toggle();
        }
      }
    }
  }
}
