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

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class Translations {
  private static final Map<Language, Translation> languages = new HashMap<>();
  public static Translation CURRENT;

  public static void load() {
    for (Language lang : Language.values()) {
      languages.put(lang, Translation.load(lang.getLangName()));
    }

    CURRENT = languages.get(Language.EN_US);
  }

  @AllArgsConstructor
  enum Language {
    EN_US("en_us");

    @Getter private final String langName;
  }
}
