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

package nw.development.util.git;

import java.io.InputStream;
import java.util.Properties;

public class GitPropertiesReader {

  private static final Properties props = new Properties();

  public static String commit() {
    return props.getProperty("git.commit.id.abbrev", "unknown");
  }

  static {
    try (
      InputStream in =
        GitPropertiesReader.class.getClassLoader().getResourceAsStream(
          "git.properties"
        )
    ) {
      if (in != null) props.load(in);
    } catch (Exception ignored) {}
  }
}
