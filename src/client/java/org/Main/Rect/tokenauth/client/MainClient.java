package org.Main.Rect.tokenauth.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

/**
 * TokenAuth Mod - Client Initializer
 * Ported to 1.21.5
 * Author: Rect
 */
public class MainClient implements ClientModInitializer {
    private final LogoRenderer logoRenderer = new LogoRenderer();

    @Override
    public void onInitializeClient() {
        // Initialize Token Auth - save original session
        TokenAuthManager.saveOriginalSession();
        
        // Register HUD render callback for logo
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            logoRenderer.renderHud(drawContext);
        });
    }
}

