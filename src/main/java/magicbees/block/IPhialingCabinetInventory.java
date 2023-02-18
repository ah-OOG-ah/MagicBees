package magicbees.block;

import net.minecraft.item.ItemStack;

public interface IPhialingCabinetInventory {

    ItemStack getSlot1();

    ItemStack getSlot2();

    ItemStack getSlot3();

    ItemStack getSlot4();

    ItemStack getSlot5();

    ItemStack getSlot6();

    ItemStack getSlot7();

    ItemStack getSlot8();

    ItemStack getSlot9();

    void setSlot1(ItemStack itemStack);

    void setSlot2(ItemStack itemStack);

    void setSlot3(ItemStack itemStack);

    void setSlot4(ItemStack itemStack);

    void setSlot5(ItemStack itemStack);

    void setSlot6(ItemStack itemStack);

    void setSlot7(ItemStack itemStack);

    void setSlot8(ItemStack itemStack);

    void setSlot9(ItemStack itemStack);
}
