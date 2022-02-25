package org.spoorn.spoornweaponattributes.mixin;

import static org.spoorn.spoornweaponattributes.util.SpoornWeaponAttributesUtil.NBT_KEY;
import static org.spoorn.spoornweaponattributes.util.SpoornWeaponAttributesUtil.REROLL_NBT_KEY;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spoorn.spoornweaponattributes.util.SpoornWeaponAttributesUtil;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {

    @Shadow @Final private Property levelCost;

    @Shadow private int repairItemUsage;

    @Inject(method = "onTakeOutput", at = @At(value = "TAIL"))
    private void reroll(PlayerEntity player, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (stack.hasTag()) {
            NbtCompound root = stack.getTag();
            if (root.getBoolean(REROLL_NBT_KEY)) {
                SpoornWeaponAttributesUtil.rollAttributes(root);
                root.remove(REROLL_NBT_KEY);
            }
        }
    }
    
    @Inject(method = "updateResult", at = @At(value = "RETURN"))
    private void addRerolls(CallbackInfo ci) {
        ForgingScreenHandlerAccessor accessor = (ForgingScreenHandlerAccessor) this;
        Inventory inputInventory = accessor.getInput();
        ItemStack input1 = inputInventory.getStack(0);
        ItemStack input2 = inputInventory.getStack(1);

        ItemStack swaStack = canReroll(input1, input2);
        if (swaStack != null) {
            ItemStack output = swaStack.copy();
            NbtCompound root = output.getTag();
            root.remove(NBT_KEY);
            root.putBoolean(REROLL_NBT_KEY, true);

            this.levelCost.set(1);
            this.repairItemUsage = 1;
            accessor.getOutput().setStack(0, output);
            ((ScreenHandlerAccessor) this).trySendContentUpdates();
        }
    }
    
    private ItemStack canReroll(ItemStack stack1, ItemStack stack2) {
        if (SpoornWeaponAttributesUtil.hasSWANbt(stack1) && SpoornWeaponAttributesUtil.isLapisLazuli(stack2)) {
            return stack1;
        } else if (SpoornWeaponAttributesUtil.hasSWANbt(stack2) && SpoornWeaponAttributesUtil.isLapisLazuli(stack1)) {
            return stack2;
        }
        return null;
    }
}
