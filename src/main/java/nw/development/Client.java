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

package nw.development;

import java.io.File;
import java.lang.invoke.MethodHandles;
import meteordevelopment.orbit.EventBus;
import meteordevelopment.orbit.IEventBus;
import net.fabricmc.api.ClientModInitializer;
import nw.development.feature.module.Modules;
import nw.development.util.git.GitPropertiesReader;
import nw.development.util.minecraft.MinecraftInstances;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client implements ClientModInitializer, MinecraftInstances {
  public static final Logger LOGGER = LoggerFactory.getLogger("nw-cheat");
  public static final IEventBus EVENTS = new EventBus();
  public static Modules MODULES;

  public static final File CLIENT_DIR = new File(mc.runDirectory, "nw-cheat");

  public static final String VERSION = GitPropertiesReader.commit();

  @Override
  public void onInitializeClient() {
    EVENTS.registerLambdaFactory(
        "nw.development",
        (method, clazz) ->
            (MethodHandles.Lookup) method.invoke(null, clazz, MethodHandles.lookup()));

    MODULES = new Modules();

    LOGGER.info("nw-cheat:{} has initialized", VERSION);
  }
}
