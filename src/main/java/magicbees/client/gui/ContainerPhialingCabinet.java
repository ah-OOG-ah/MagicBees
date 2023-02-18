package magicbees.client.gui;

/*
 * public class ContainerPhialingCabinet extends ContainerMB { public TileEntityPhialingCabinet phialingCabinet; public
 * int maxSlot = 0; // Constants private static final int SLOT_ONE = 0; private static final int SLOT_TWO = 1; private
 * static final int SLOT_THREE = 2; private static final int SLOT_FOUR = 3; private static final int SLOT_FIVE = 4;
 * private static final int SLOT_SIX = 5; private static final int SLOT_SEVEN = 6; private static final int SLOT_EIGHT =
 * 7; private static final int SLOT_NINE = 8; public ContainerPhialingCabinet(InventoryPlayer inventoryPlayer,
 * TileEntityPhialingCabinet phialingCabinet){ this.phialingCabinet = phialingCabinet; // Queen/Princess slot
 * addSlotToContainer(new SlotCustomItems(this.phialingCabinet, 0, 29, 39, ItemInterface.getItemStack("Forestry",
 * "beeQueenGE", 1), ItemInterface.getItemStack("Forestry", "beePrincessGE", 1))); // Drone slot addSlotToContainer(new
 * SlotCustomItems(this.phialingCabinet, 1, 29, 65, ItemInterface.getItemStack("Forestry", "beeDroneGE", 64))); int
 * currentSlot = 1; // Frame slots for (int x = 0; x < 3; x++){ currentSlot++; addSlotToContainer(new
 * SlotFrame(phialingCabinet, currentSlot, 66, 23 + x * 29)); //LogHelper.info("[1] CURRENTSLOT: " + currentSlot); } for
 * (int x = 0; x < 3; x++){ currentSlot++; addSlotToContainer(new Slot(phialingCabinet, currentSlot, 116, 26 + x * 26));
 * //LogHelper.info("[2] CURRENTSLOT: " + currentSlot); } int j = 0; for (int y = 0; y < 2; y++){ for (int x = 0; x < 2;
 * x++){ currentSlot++; addSlotToContainer(new Slot(phialingCabinet, currentSlot, 95 + x * 42, 39 + j * 26));
 * //LogHelper.info("[3] CURRENTSLOT: " + currentSlot); } j++; } addPlayerInventory(inventoryPlayer, 0, 110); maxSlot =
 * currentSlot; }
 * @Override public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex){ Slot itemSlot =
 * this.getSlot(slotIndex); boolean clearSlot = false; if (itemSlot != null && itemSlot.getHasStack()) { ItemStack
 * srcStack = itemSlot.getStack(); if (slotIndex <= maxSlot && srcStack != null){ clearSlot =
 * this.mergeItemStack(srcStack, maxSlot + 1, maxSlot + 36, false); }else{ if (slotIndex > maxSlot && srcStack != null){
 * if (BeeManager.beeRoot.isMember(srcStack)){ if (!BeeManager.beeRoot.isDrone(srcStack)){ if
 * (this.getSlot(SLOT_ONE).getHasStack() == false) { clearSlot = this.mergeItemStack(srcStack, SLOT_ONE, SLOT_ONE + 1,
 * false); } }else{ if (this.getSlot(SLOT_TWO).isItemValid(srcStack)){ clearSlot = this.mergeItemStack(srcStack,
 * SLOT_TWO, SLOT_TWO + 1, false); } } }else if(srcStack.getItem() instanceof IHiveFrame){ clearSlot =
 * this.mergeItemStack(srcStack, SLOT_THREE, SLOT_THREE + SLOT_FOUR, false); } } } } if (clearSlot){
 * itemSlot.putStack(null); } itemSlot.onSlotChanged(); player.inventory.markDirty(); return null; }
 * @Override public boolean canInteractWith(EntityPlayer entityPlayer) { return
 * phialingCabinet.isUseableByPlayer(entityPlayer); }
 */
// }
