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

package nw.development.util.language;

import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class Translation {
  private final Map<String, String> translations;

  private Translation(String langName) {
    Yaml yaml = new Yaml();

    translations =
        yaml.load(
            Translation.class.getClassLoader().getResourceAsStream("lang/" + langName + ".yml"));
  }

  public static Translation load(String langName) {
    return new Translation(langName);
  }

  public String get(String key) {
    return translations.get(key);
  }
}
