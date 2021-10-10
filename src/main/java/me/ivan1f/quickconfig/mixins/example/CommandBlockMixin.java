package me.ivan1f.quickconfig.mixins.example;

import me.ivan1f.quickconfig.example.setting.Tweaks;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.CommandBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CommandBlock.class)
public class CommandBlockMixin {
    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    private void getRenderType(BlockState state, CallbackInfoReturnable<BlockRenderType> cir) {
        if (Tweaks.invisibleCommandBlocks) {
            cir.setReturnValue(BlockRenderType.INVISIBLE);
        }
    }
}
