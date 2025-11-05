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

package nw.development.util.lang;

import static nw.development.Client.CLIENT_DIR;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.dataformat.yaml.YAMLFactory;

public class Languages {

  private static final List<String> knownLangs = List.of("en_us");
  private static final Map<String, Language> loadedLangs = new HashMap<>();
  private static final File LANGUAGE_CONFIG_FILE = new File(
    CLIENT_DIR,
    "language.yml"
  );
  public static Language CURRENT;

  static {
    for (String langName : knownLangs) {
      try {
        Language language = new Language(langName);
        loadedLangs.put(langName, language);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static void initialize() {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    if (!LANGUAGE_CONFIG_FILE.exists()) {
      mapper.writeValue(LANGUAGE_CONFIG_FILE, "en_us");
    }

    String savedLangName = mapper.readValue(LANGUAGE_CONFIG_FILE, String.class);

    CURRENT = loadedLangs.get(savedLangName);
  }
}
