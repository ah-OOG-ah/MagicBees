package magicbees.item;

import magicbees.main.CommonProxy;
import magicbees.main.utils.compat.ThaumcraftHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.IRepairable;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.ThaumcraftApi;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.core.IToolScoop;

@Optional.InterfaceList(
		{
				@Optional.Interface(iface = "thaumcraft.api.IRepairable", modid = ThaumcraftHelper.Name, striprefs = true),
				@Optional.Interface(iface = "thaumcraft.api.IWarpingGear", modid = ThaumcraftHelper.Name, striprefs = true)
		}
)
public class ItemVoidScoop extends Item implements IRepairable, IToolScoop, IWarpingGear
{
	public ItemVoidScoop()
	{
		super();
		this.setMaxStackSize(1);
		this.setMaxDamage(20);
		this.setCreativeTab(forestry.api.core.Tabs.tabApiculture);
		this.setUnlocalizedName("voidScoop");
		this.setHarvestLevel("scoop", 3);
	}

	@Override
	public float func_150893_a(ItemStack itemStack, Block block)
	{
		return 1f;
	}

	@Override
	public float getDigSpeed(ItemStack itemStack, Block block, int metadata)
	{
		return ForgeHooks.isToolEffective(itemStack, block, metadata) ? 4.8f : func_150893_a(itemStack, block);
	}

	@Override
	public boolean onBlockDestroyed(ItemStack itemstack, World world, Block block, int j, int k, int l,
									EntityLivingBase entityliving)
	{
		itemstack.damageItem(1, entityliving);
		return true;
	}
	
	/**
	 * Return the enchantability factor of the item, most of the time is based on material.
	 */
	@Optional.Method(modid = ThaumcraftHelper.Name)
	public int getItemEnchantability()
	{
		return ThaumcraftApi.toolMatVoid.getEnchantability();
	}

	/**
	 * Return the name for this tool's material.
	 */
	@Optional.Method(modid = ThaumcraftHelper.Name)
	public String getToolMaterialName()
	{
		return ThaumcraftApi.toolMatVoid.toString();
	}

	/**
	 * Return whether this item is repairable in an anvil.
	 */
	@Optional.Method(modid = ThaumcraftHelper.Name)
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
	{
		return OreDictionary.itemMatches(ThaumcraftApi.toolMatVoid.getRepairItemStack(), par2ItemStack, true)
				|| super.getIsRepairable(par1ItemStack, par2ItemStack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon(CommonProxy.DOMAIN + ":voidScoop"); //TODO
	}
	
	public EnumRarity getRarity(ItemStack itemstack)
	{
		return EnumRarity.uncommon;
	}
	
	public void onUpdate(ItemStack stack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_)
	{
		super.onUpdate(stack, world, entity, p_77663_4_, p_77663_5_);
		if ((stack.isItemDamaged()) && (entity != null) && (entity.ticksExisted % 20 == 0) && ((entity instanceof EntityLivingBase))) {
			stack.damageItem(-1, (EntityLivingBase)entity);
		}
	}
	  
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		if ((!player.worldObj.isRemote) && ((entity instanceof EntityLivingBase)) && (
			(!(entity instanceof EntityPlayer)) || (MinecraftServer.getServer().isPVPEnabled()))) {
				((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.weakness.getId(), 80));
		}
		return super.onLeftClickEntity(stack, player, entity);
	}
	
	@Override
	public int getWarp(ItemStack itemstack, EntityPlayer player)
	{
		return 1;
	}
	
}
