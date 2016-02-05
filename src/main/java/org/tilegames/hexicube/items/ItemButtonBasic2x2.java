package org.tilegames.hexicube.items;

import org.lwjgl.opengl.GL11;
import org.tilegames.hexicube.FunctionalDecoration;
import org.tilegames.hexicube.blocks.TEButtonPanel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class ItemButtonBasic2x2 extends ItemButton
{
	private static final ResourceLocation texture = new ResourceLocation(FunctionalDecoration.MODID + ":textures/buttons/basic2x2.png");
	
	private static final ModelBase baseModel;
	static
	{
		baseModel = new ModelBase(){};
		baseModel.textureWidth = 8;
		baseModel.textureHeight = 3;
	}
	
	private final ModelRenderer Base;
	
	public ItemButtonBasic2x2()
	{
		super("ButtonBasic2x2");
		setUnlocalizedName("hexifunctionaldecoration.button.basic2x2");
		setTextureName(FunctionalDecoration.MODID + ":buttons/basic2x2");
		
		Base = new ModelRenderer(baseModel, 0, 0);
		Base.addBox(-1, 0, -1, 2, 1, 2);
	}
	
	@Override
	protected void render(int width, int height, int inVal, int outVal, float scale)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		if(outVal != 0)
		{
			GL11.glPushMatrix();
			GL11.glTranslated(0, -0.75*scale, 0);
			Base.render(scale);
			GL11.glPopMatrix();
		}
		else Base.render(scale);
	}
	
	@Override
	public boolean acceptsSize(int width, int height, int depth)
	{
		if(width == 2 && height == 2) return true;
		return false;
	}
	
	@Override
	public boolean allowsButtonPress(ItemStack stack, EntityPlayer player, boolean rightClick)
	{
		if(rightClick) return true;
		return false;
	}
	
	@Override
	public NBTTagCompound getExtraData(EntityPlayer player)
	{
		return null;
	}
	
	@Override
	public void handleButtonPress(ItemStack stack, boolean rightClick, NBTTagCompound extraData, TEButtonPanel panel)
	{
		NBTTagCompound data = stack.getTagCompound();
		if(data != null)
		{
			if(data.getInteger("buttonState") <= 0) data.setInteger("buttonState", 20);
		}
		else
		{
			data = new NBTTagCompound();
			data.setInteger("buttonState", 20);
			stack.setTagCompound(data);
		}
		panel.getWorldObj().playSoundEffect(panel.xCoord + 0.5, panel.yCoord + 0.5, panel.zCoord + 0.5, "random.click", 0.3F, 0.6F);
	}
	
	@Override
	public void handleTick(ItemStack stack, TEButtonPanel panel)
	{
		NBTTagCompound data = stack.getTagCompound();
		if(data != null)
		{
			int val = data.getInteger("buttonState");
			if(val > 0)
			{
				val--;
				if(val == 0) panel.getWorldObj().playSoundEffect(panel.xCoord + 0.5, panel.yCoord + 0.5, panel.zCoord + 0.5, "random.click", 0.3F, 0.5F);
			}
			data.setInteger("buttonState", val);
		}
	}
	
	@Override
	public int getSignal(ItemStack stack)
	{
		NBTTagCompound data = stack.getTagCompound();
		if(data != null)
		{
			if(data.getInteger("buttonState") > 0) return 15;
		}
		return 0;
	}
	
	@Override
	public void clearState(ItemStack stack)
	{
		NBTTagCompound data = stack.getTagCompound();
		if(data != null)
		{
			data.removeTag("buttonState");
			if(data.hasNoTags()) stack.setTagCompound(null);
		}
	}
}