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

package nw.development.util.input;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.lwjgl.glfw.GLFW;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Shortcut {
  public static final Shortcut NONE = new Shortcut(GLFW.GLFW_KEY_UNKNOWN, false, Action.PRESS);

  private int code;
  private boolean isMouse;
  private Action action;

  @JsonIgnore
  public String getName() {
    if (code == GLFW.GLFW_KEY_UNKNOWN) {
      return "unknown";
    } else if (isMouse) {
      return "mouse" + code;
    } else {
      return GLFW.glfwGetKeyName(code, 0);
    }
  }

  @AllArgsConstructor
  public enum Action {
    HOLD("hold"),
    PRESS("press");

    @Getter private final String actionName;
  }
}
