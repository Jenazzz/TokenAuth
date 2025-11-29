package org.Main.Rect.tokenauth.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.session.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * TokenAuth Mixin - MinecraftClient
 * Ported to 1.21.5
 * Author: Rect
 */
@Mixin(MinecraftClient.class)
public interface MinecraftClientMixin {
    @Accessor
    @Mutable
    void setSession(Session session);
}

