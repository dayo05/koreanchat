package me.ddayo.koreanchat.mixin;

import me.ddayo.koreanchat.client.KoreanInputState;
import me.ddayo.koreanchat.client.ime.acceptable.SignEditScreenAcceptable;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.screen.ingame.AbstractSignEditScreen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSignEditScreen.class)
abstract public class AbstractSignEditScreenJ {
    @Inject(method = "<init>(Lnet/minecraft/block/entity/SignBlockEntity;ZZLnet/minecraft/text/Text;)V", at = @At("RETURN"))
    public void ctorHook(SignBlockEntity blockEntity, boolean front, boolean filtered, Text title, CallbackInfo ci) {
        KoreanInputState.INSTANCE.push(new SignEditScreenAcceptable((AbstractSignEditScreen) (Object) this));
    }

    @Inject(method = "close", at = @At("HEAD"))
    public void closeHook(CallbackInfo ci) {
        KoreanInputState.INSTANCE.pop();
    }
}
