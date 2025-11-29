package org.Main.Rect.tokenauth.mixin.client;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.Main.Rect.tokenauth.client.TokenAuthManager;
import org.Main.Rect.tokenauth.client.TokenAuthScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * TokenAuth Mixin - MultiplayerScreen
 * Ported to 1.21.5
 * Author: Rect
 */
@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin extends Screen {
    
    protected MultiplayerScreenMixin(Text title) {
        super(title);
    }
    
    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        // Save original session on first init
        TokenAuthManager.saveOriginalSession();
        
        // Add TokenAuth button
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Token Auth"),
            button -> {
                if (this.client != null) {
                    this.client.setScreen(new TokenAuthScreen(this));
                }
            }
        ).dimensions(5, 5, 100, 20).build());
    }
    
    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        // Draw current session info
        if (this.client != null && this.client.getSession() != null) {
            net.minecraft.client.session.Session session = this.client.getSession();
            java.util.UUID sessionUuid = null;
            try {
                java.lang.reflect.Method getUuidMethod = net.minecraft.client.session.Session.class.getMethod("getUuid");
                sessionUuid = (java.util.UUID) getUuidMethod.invoke(session);
            } catch (Exception e) {
                try {
                    java.lang.reflect.Method getUuidOrNullMethod = net.minecraft.client.session.Session.class.getMethod("getUuidOrNull");
                    sessionUuid = (java.util.UUID) getUuidOrNullMethod.invoke(session);
                } catch (Exception ex) {
                    // UUID will be null
                }
            }
            String status = String.format("User: §a%s §rUUID: §a%s", 
                session.getUsername(), 
                sessionUuid != null ? sessionUuid.toString() : "N/A");
            context.drawTextWithShadow(
                this.textRenderer,
                status,
                115,
                10,
                0xFFFFFF
            );
        }
    }
}

