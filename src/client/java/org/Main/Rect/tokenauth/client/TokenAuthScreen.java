package org.Main.Rect.tokenauth.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.session.Session;
import net.minecraft.text.Text;

import java.util.UUID;

/**
 * TokenAuth Screen
 * Ported to 1.21.5
 * Author: Rect
 */
public class TokenAuthScreen extends Screen {
    private final Screen parent;
    private TextFieldWidget tokenField;
    private String status = "Enter Bearer Token:";
    
    public TokenAuthScreen(Screen parent) {
        super(Text.literal("Token Auth"));
        this.parent = parent;
    }
    
    @Override
    protected void init() {
        super.init();
        
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        
        // Token input field
        this.tokenField = new TextFieldWidget(
            this.textRenderer,
            centerX - 100,
            centerY - 10,
            200,
            20,
            Text.literal("Token")
        );
        this.tokenField.setMaxLength(32767);
        this.tokenField.setFocused(true);
        this.addDrawableChild(this.tokenField);
        
        // Login button
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Login"),
            button -> this.login()
        ).dimensions(centerX - 100, centerY + 20, 200, 20).build());
        
        // Restore button
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Restore Original Session"),
            button -> this.restore()
        ).dimensions(centerX - 100, centerY + 50, 200, 20).build());
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        
        // Draw status text
        int statusWidth = this.textRenderer.getWidth(status);
        context.drawTextWithShadow(
            this.textRenderer,
            status,
            centerX - statusWidth / 2,
            centerY - 40,
            0xFFFFFF
        );
        
        // Draw current session info
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.getSession() != null) {
            Session session = client.getSession();
            UUID sessionUuid = null;
            try {
                // Try to get UUID using reflection (method name might vary)
                java.lang.reflect.Method getUuidMethod = Session.class.getMethod("getUuid");
                sessionUuid = (UUID) getUuidMethod.invoke(session);
            } catch (Exception e) {
                // Fallback: try getUuidOrNull or other methods
                try {
                    java.lang.reflect.Method getUuidOrNullMethod = Session.class.getMethod("getUuidOrNull");
                    sessionUuid = (UUID) getUuidOrNullMethod.invoke(session);
                } catch (Exception ex) {
                    // If all fails, UUID is null
                }
            }
            String sessionInfo = String.format("Current: %s (UUID: %s)", 
                session.getUsername(), 
                sessionUuid != null ? sessionUuid.toString() : "N/A");
            int infoWidth = this.textRenderer.getWidth(sessionInfo);
            context.drawTextWithShadow(
                this.textRenderer,
                sessionInfo,
                centerX - infoWidth / 2,
                centerY - 60,
                0xAAAAAA
            );
        }
        
        super.render(context, mouseX, mouseY, delta);
    }
    
    private void login() {
        String token = this.tokenField.getText().trim();
        if (token.isEmpty()) {
            this.status = "§cError: Token cannot be empty";
            return;
        }
        
        try {
            TokenAuthManager.SessionInfo sessionInfo = TokenAuthManager.authenticateWithToken(token);
            
            // Set new session using Mixin
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null) {
                // Use reflection or Mixin to set session
                setSession(client, sessionInfo.username, sessionInfo.uuid, sessionInfo.token);
                this.status = "§aSuccess! Session updated.";
                client.setScreen(this.parent);
            }
        } catch (Exception e) {
            this.status = "§cError: " + e.getMessage();
            e.printStackTrace();
        }
    }
    
    private void restore() {
        try {
                Session originalSession = TokenAuthManager.getOriginalSession();
                if (originalSession != null) {
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client != null) {
                        UUID originalUuid = null;
                        try {
                            java.lang.reflect.Method getUuidMethod = Session.class.getMethod("getUuid");
                            originalUuid = (UUID) getUuidMethod.invoke(originalSession);
                        } catch (Exception e) {
                            try {
                                java.lang.reflect.Method getUuidOrNullMethod = Session.class.getMethod("getUuidOrNull");
                                originalUuid = (UUID) getUuidOrNullMethod.invoke(originalSession);
                            } catch (Exception ex) {
                                // UUID will be null
                            }
                        }
                        if (originalUuid != null) {
                            setSession(client, originalSession.getUsername(), originalUuid.toString(), originalSession.getAccessToken());
                        } else {
                            this.status = "§cError: Could not get UUID from original session";
                        }
                    this.status = "§aSession restored.";
                    client.setScreen(this.parent);
                }
            } else {
                this.status = "§cError: No original session saved";
            }
        } catch (Exception e) {
            this.status = "§cError: " + e.getMessage();
            e.printStackTrace();
        }
    }
    
    private void setSession(MinecraftClient client, String username, String uuid, String token) {
        try {
            // Parse UUID (ensure proper format)
            String uuidFormatted = uuid;
            if (!uuid.contains("-")) {
                uuidFormatted = uuid.replaceFirst(
                    "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                    "$1-$2-$3-$4-$5"
                );
            }
            UUID uuidObj = UUID.fromString(uuidFormatted);
            
            // Create new Session - try all available constructors
            Session newSession = null;
            Exception lastException = null;
            StringBuilder constructorInfo = new StringBuilder();
            
            // Get current session to extract default values for Optional parameters
            Session currentSession = client.getSession();
            
            // Get all constructors and try them
            java.lang.reflect.Constructor<?>[] constructors = Session.class.getDeclaredConstructors();
            for (java.lang.reflect.Constructor<?> constructor : constructors) {
                try {
                    constructor.setAccessible(true);
                    Class<?>[] paramTypes = constructor.getParameterTypes();
                    
                    // Log constructor signature for debugging
                    StringBuilder sig = new StringBuilder("Constructor(");
                    for (int i = 0; i < paramTypes.length; i++) {
                        if (i > 0) sig.append(", ");
                        sig.append(paramTypes[i].getSimpleName());
                    }
                    sig.append(")");
                    constructorInfo.append(sig.toString()).append("; ");
                    
                    // Build arguments array based on parameter types
                    Object[] args = new Object[paramTypes.length];
                    boolean canUse = true;
                    
                    // More flexible parameter matching
                    for (int i = 0; i < paramTypes.length; i++) {
                        Class<?> paramType = paramTypes[i];
                        
                        if (paramType == String.class) {
                            // Try to match String parameters
                            if (i == 0) {
                                args[i] = username;
                            } else if (i == 2 || (i == 1 && paramTypes.length == 3)) {
                                args[i] = token;
                            } else if (i == 1 && paramTypes.length >= 3) {
                                // Could be UUID as string or token
                                args[i] = uuidObj.toString();
                            } else if (i == paramTypes.length - 1) {
                                // Last parameter might be account type as string
                                args[i] = "mojang";
                            } else {
                                args[i] = token; // Default to token
                            }
                        } else if (paramType == UUID.class) {
                            args[i] = uuidObj;
                        } else if (paramType == Session.AccountType.class) {
                            args[i] = Session.AccountType.MOJANG;
                        } else if (paramType.getName().equals("java.util.Optional") || 
                                   paramType.getSimpleName().equals("Optional")) {
                            // Handle Optional parameters - use empty Optional
                            try {
                                java.lang.reflect.Method emptyMethod = paramType.getMethod("empty");
                                args[i] = emptyMethod.invoke(null);
                            } catch (Exception e) {
                                // If empty() method doesn't exist, try to create Optional with null
                                try {
                                    java.lang.reflect.Method ofNullableMethod = paramType.getMethod("ofNullable", Object.class);
                                    args[i] = ofNullableMethod.invoke(null, (Object) null);
                                } catch (Exception ex) {
                                    canUse = false;
                                    break;
                                }
                            }
                        } else {
                            // Try to find enum values or use default values for unknown types
                            // Check if it's an enum
                            if (paramType.isEnum()) {
                                Object[] enumConstants = paramType.getEnumConstants();
                                if (enumConstants != null && enumConstants.length > 0) {
                                    args[i] = enumConstants[0]; // Use first enum value
                                } else {
                                    canUse = false;
                                    break;
                                }
                            } else {
                                // For class_321 or other unknown types, try to find a default value
                                // First, try to get value from current session if available
                                if (currentSession != null && i < 6) {
                                    try {
                                        // Try to get field from current session that matches this parameter type
                                        java.lang.reflect.Field[] sessionFields = Session.class.getDeclaredFields();
                                        for (java.lang.reflect.Field field : sessionFields) {
                                            if (field.getType() == paramType) {
                                                field.setAccessible(true);
                                                args[i] = field.get(currentSession);
                                                break;
                                            }
                                        }
                                    } catch (Exception e) {
                                        // Ignore, try other methods
                                    }
                                }
                                
                                // If still null, try to find a static method or field
                                if (args[i] == null) {
                                    try {
                                        // Try to find a static field or method that returns this type
                                        java.lang.reflect.Field[] fields = paramType.getFields();
                                        for (java.lang.reflect.Field field : fields) {
                                            if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) && 
                                                field.getType() == paramType) {
                                                args[i] = field.get(null);
                                                break;
                                            }
                                        }
                                        if (args[i] == null) {
                                            // Try to find a static method
                                            java.lang.reflect.Method[] methods = paramType.getMethods();
                                            for (java.lang.reflect.Method method : methods) {
                                                if (java.lang.reflect.Modifier.isStatic(method.getModifiers()) && 
                                                    method.getReturnType() == paramType && 
                                                    method.getParameterCount() == 0) {
                                                    args[i] = method.invoke(null);
                                                    break;
                                                }
                                            }
                                        }
                                        if (args[i] == null) {
                                            canUse = false;
                                            break;
                                        }
                                    } catch (Exception e) {
                                        canUse = false;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    
                    if (canUse) {
                        newSession = (Session) constructor.newInstance(args);
                        break;
                    }
                } catch (Exception e) {
                    lastException = e;
                    // Log the exception for debugging
                    constructorInfo.append("Exception: ").append(e.getClass().getSimpleName()).append("; ");
                    continue;
                }
            }
            
            if (newSession == null) {
                // Try to find static factory methods
                try {
                    java.lang.reflect.Method[] methods = Session.class.getDeclaredMethods();
                    for (java.lang.reflect.Method method : methods) {
                        if (java.lang.reflect.Modifier.isStatic(method.getModifiers()) && 
                            method.getReturnType() == Session.class) {
                            try {
                                method.setAccessible(true);
                                Class<?>[] paramTypes = method.getParameterTypes();
                                if (paramTypes.length == 3 && 
                                    paramTypes[0] == String.class && 
                                    (paramTypes[1] == UUID.class || paramTypes[1] == String.class) &&
                                    paramTypes[2] == String.class) {
                                    Object[] methodArgs = new Object[]{
                                        username,
                                        paramTypes[1] == UUID.class ? uuidObj : uuidObj.toString(),
                                        token
                                    };
                                    newSession = (Session) method.invoke(null, methodArgs);
                                    break;
                                }
                            } catch (Exception e) {
                                continue;
                            }
                        }
                    }
                } catch (Exception e) {
                    // Ignore
                }
                
                if (newSession == null) {
                    throw new RuntimeException("Failed to create Session object. Available constructors: " + constructors.length + " (" + constructorInfo.toString() + ")", lastException);
                }
            }
            
            // Use Mixin accessor to set session
            try {
                if (client instanceof org.Main.Rect.tokenauth.mixin.client.MinecraftClientMixin) {
                    ((org.Main.Rect.tokenauth.mixin.client.MinecraftClientMixin) client).setSession(newSession);
                } else {
                    throw new RuntimeException("Mixin not applied");
                }
            } catch (Exception mixinEx) {
                // Fallback to reflection: find field by type instead of name
                try {
                    java.lang.reflect.Field sessionField = null;
                    for (java.lang.reflect.Field field : MinecraftClient.class.getDeclaredFields()) {
                        if (field.getType() == Session.class) {
                            sessionField = field;
                            break;
                        }
                    }
                    if (sessionField == null) {
                        throw new RuntimeException("Session field not found in MinecraftClient");
                    }
                    sessionField.setAccessible(true);
                    sessionField.set(client, newSession);
                } catch (Exception ex) {
                    throw new RuntimeException("Failed to set session field", ex);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to set session", e);
        }
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) { // ESC
            this.client.setScreen(this.parent);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}

