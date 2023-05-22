package me.ddayo.koreanchat.mixin;

import me.ddayo.koreanchat.client.KoreanInputState;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextFieldWidget.class)
abstract class TextFieldWidgetJ extends ClickableWidget {
    public TextFieldWidgetJ(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    @Inject(method = "setFocused", at = @At("RETURN"))
    public void setFocusedHook(boolean focused, CallbackInfo ci) {
        if(focused)
            KoreanInputState.INSTANCE.change((TextFieldWidget) (Object) this);
        else KoreanInputState.INSTANCE.change(null);
    }

    @Inject(method = "setCursor", at = @At("RETURN"))
    public void setCursorHook(int cursor, CallbackInfo ci) {
        KoreanInputState.INSTANCE.unmark();
    }
}