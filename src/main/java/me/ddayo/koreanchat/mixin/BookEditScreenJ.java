package me.ddayo.koreanchat.mixin;

import me.ddayo.koreanchat.client.KoreanInputState;
import me.ddayo.koreanchat.client.ime.acceptable.BookEditScreenAcceptable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BookEditScreen.class)
abstract public class BookEditScreenJ extends Screen {
    protected BookEditScreenJ(Text title) {
        super(title);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void ctorHook(PlayerEntity player, ItemStack itemStack, Hand hand, CallbackInfo ci) {
        KoreanInputState.INSTANCE.push(new BookEditScreenAcceptable((BookEditScreen) (Object) this));
    }

    @Override
    public void close() {
        super.close();
        KoreanInputState.INSTANCE.pop();
    }
}
