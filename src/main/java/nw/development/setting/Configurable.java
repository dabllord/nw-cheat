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

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public abstract class Configurable {
  @Getter private final List<Setting<?>> settings = new ArrayList<>();

  protected <T> Setting<T> insert(Setting<T> setting) {
    settings.add(setting);
    return setting;
  }
}
