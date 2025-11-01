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

package nw.development.mixin.minecraft.client;

import net.minecraft.client.Keyboard;
import net.minecraft.client.input.KeyInput;
import nw.development.Client;
import nw.development.events.game.KeyboardKeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
  @Inject(method = "onKey", at = @At("HEAD"))
  private void inject$onKey(long window, int action, KeyInput input, CallbackInfo ci) {
    Client.EVENTS.post(new KeyboardKeyEvent(input.key(), input.scancode(), action));
  }
}
