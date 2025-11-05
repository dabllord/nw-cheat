package nw.development.util.lang;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.dataformat.yaml.YAMLFactory;

public class Language {

  private final Map<String, String> translations;

  public Language(String langName) throws IOException {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    try (
      InputStream io = Language.class.getClassLoader().getResourceAsStream(
        "assets/nw-cheat/lang/" + langName + ".yml"
      )
    ) {
      translations = mapper.readValue(io, new TypeReference<>() {});
    }
  }

  public String getTranslated(String key) {
    return translations.get(key);
  }
}
