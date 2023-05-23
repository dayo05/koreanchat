package me.ddayo.koreanchat.mixin;

import me.ddayo.koreanchat.client.KoreanChatClient;
import me.ddayo.koreanchat.client.KoreanInputState;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardJ {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "onChar", at = @At("HEAD"), cancellable = true)
    public void onCharHook(long window, int codePoint, int modifiers, CallbackInfo ci) {
        if(codePoint <= Character.MAX_VALUE && KoreanInputState.INSTANCE.checkCodepoint((char) codePoint))
            ci.cancel();
        else if(KoreanChatClient.isDebug()) LogManager.getLogger().info("Char input: " + codePoint + " " + modifiers);
    }

    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void onKeyHook(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if(KoreanChatClient.isDebug())
            LogManager.getLogger().info("Key input: " + key + " " + scancode + " " + action + " " + modifiers);
        if(KoreanInputState.INSTANCE.isActive()) {
            KoreanInputState.INSTANCE.onKeyInput(key, scancode, modifiers, action);
            if(key == GLFW.GLFW_KEY_BACKSPACE)
                ci.cancel();
        }
    }
}
