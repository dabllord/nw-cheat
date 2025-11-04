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

import java.awt.*;
import java.io.File;
import java.lang.invoke.MethodHandles;
import meteordevelopment.orbit.EventBus;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.IEventBus;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.util.Identifier;
import nw.development.config.ConfigShutdownHook;
import nw.development.config.Configs;
import nw.development.events.game.HudRenderEvent;
import nw.development.feature.module.Modules;
import nw.development.util.git.GitPropertiesReader;
import nw.development.util.lang.Languages;
import nw.development.util.minecraft.MinecraftInstances;
import nw.development.util.render.ExtendedRenderPipelines;
import nw.development.util.render.MeshBuilder;
import nw.development.util.render.MeshRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client implements ClientModInitializer, MinecraftInstances {
  public static final Logger LOGGER = LoggerFactory.getLogger("nw-cheat");
  public static final IEventBus EVENTS = new EventBus();
  public static Modules MODULES;
  public static Configs CONFIGS;

  public static final File CLIENT_DIR = new File(mc.runDirectory, "nw-cheat");

  public static final String VERSION = GitPropertiesReader.commit();

  @Override
  public void onInitializeClient() {
    if (!CLIENT_DIR.exists()) {
      if (!CLIENT_DIR.mkdir()) {
        LOGGER.error("failed to create client-dir: {}", CLIENT_DIR.getPath());
      }
    }

    EVENTS.registerLambdaFactory(
        "nw.development",
        (method, clazz) ->
            (MethodHandles.Lookup) method.invoke(null, clazz, MethodHandles.lookup()));

    MODULES = new Modules();
    CONFIGS = new Configs();

    Languages.initialize();

    Runtime.getRuntime().addShutdownHook(new ConfigShutdownHook());

    EVENTS.subscribe(this);

    LOGGER.info("nw-cheat:{} has initialized", VERSION);
  }

  @EventHandler
  private void onHudRender(HudRenderEvent event) {
    MeshBuilder mesh = new MeshBuilder(ExtendedRenderPipelines.POS_TEX_COLOR);

    mesh.begin();
    mesh.ensureQuadCapacity();

    mesh.quad(
        mesh.vec3(0.0, 0.0, 0.0).vec2(0.0, 0.0).color(Color.WHITE).next(),
        mesh.vec3(0.0, 100.0, 0.0).vec2(0.0, 1.0).color(Color.WHITE).next(),
        mesh.vec3(100.0, 100.0, 0.0).vec2(1.0, 1.0).color(Color.WHITE).next(),
        mesh.vec3(100.0, 0.0, 0.0).vec2(1.0, 0.0).color(Color.WHITE).next());

    mesh.end();

    AbstractTexture texture = mc.getTextureManager().getTexture(Identifier.ofVanilla("textures/entity/creeper/creeper.png"));

    MeshRenderer.begin()
        .attachments(mc.getFramebuffer())
        .mesh(mesh)
        .pipeline(ExtendedRenderPipelines.POS_TEX_COLOR)
        .sampler("u_Texture", texture.getGlTextureView())
        .end();
  }
}
