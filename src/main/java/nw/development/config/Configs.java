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

package nw.development.config;

import static nw.development.Client.CLIENT_DIR;
import static nw.development.Client.MODULES;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import nw.development.feature.module.Module;
import nw.development.setting.Configurable;
import nw.development.setting.Setting;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.dataformat.yaml.YAMLFactory;

public class Configs {
  private static final File MODULES_CONFIG_FILE = new File(CLIENT_DIR, "modules.yml");
  private static final File CONFIGS_DIR = new File(CLIENT_DIR, "config");
  private final ObjectMapper YAML = new ObjectMapper(new YAMLFactory());

  public Configs() {
    if (!MODULES_CONFIG_FILE.exists()) {
      saveCurrentToDefaultConfig();
    } else {
      loadDefaultConfig();
    }
  }

  public void saveCurrentToDefaultConfig() {
    saveCurrentTo(MODULES_CONFIG_FILE);
  }

  public void saveCurrentTo(File file) {
    Map<String, Map<String, Object>> root = new HashMap<>();

    for (Module module : MODULES.getModules()) {
      root.put(module.getName(), serializeSettings(module.getValue()));
    }

    YAML.writeValue(file, root);
  }

  public void loadDefaultConfig() {
    loadFrom(MODULES_CONFIG_FILE);
  }

  public void loadFrom(String configName) {
    if (!CONFIGS_DIR.exists()) {
      CONFIGS_DIR.mkdir();
    }

    loadFrom(new File(CONFIGS_DIR, configName + ".yml"));
  }

  @SuppressWarnings("unchecked")
  public void loadFrom(File config) {
    Map<String, Object> root = YAML.readValue(config, new TypeReference<>() {});

    for (Module module : MODULES.getModules()) {
      Object moduleNode = root.get(module.getName());
      if (moduleNode instanceof Map<?, ?> moduleMap) {
        applySettings(module.getValue(), (Map<String, Object>) moduleMap);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void applySettings(List<Setting<?>> settings, Map<String, Object> yamlData) {
    for (Setting<?> setting : settings) {
      Object yamlValue = yamlData.get(setting.getName());
      if (yamlValue == null) continue;

      if (setting instanceof Configurable configurable) {
        if (yamlValue instanceof Map<?, ?> nestedMap) {
          applySettings(configurable.getValue(), (Map<String, Object>) nestedMap);
        }
      } else {
        Object valueToSet = yamlValue;

        if (yamlValue instanceof Map && !(setting.getDefaultValue() instanceof Map)) {
          valueToSet = YAML.convertValue(yamlValue, setting.getDefaultValue().getClass());
        }

        ((Setting) setting).setValue(valueToSet);
      }
    }
  }

  private Map<String, Object> serializeSettings(List<Setting<?>> settings) {
    Map<String, Object> result = new LinkedHashMap<>();

    for (Setting<?> setting : settings) {
      if (setting instanceof Configurable configurable) {
        result.put(setting.getName(), serializeSettings(configurable.getValue()));
      } else {
        result.put(setting.getName(), setting.getValue());
      }
    }

    return result;
  }
}
