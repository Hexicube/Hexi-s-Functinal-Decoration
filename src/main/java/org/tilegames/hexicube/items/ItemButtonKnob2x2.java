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

public class ItemButtonKnob2x2 extends ItemButton
{
	private static final ResourceLocation texture = new ResourceLocation(FunctionalDecoration.MODID + ":textures/buttons/knob2x2.png");
	
	private static final ModelBase baseModel;
	static
	{
		baseModel = new ModelBase(){};
		baseModel.textureWidth = 60;
		baseModel.textureHeight = 77;
	}
	
	private final ModelRenderer BaseHorz, BaseVert, BaseDiag1, BaseDiag2, BaseBeam;
	
	public ItemButtonKnob2x2()
	{
		super("ButtonBasic2x2");
		setUnlocalizedName("hexifunctionaldecoration.button.knob2x2");
		setTextureName(FunctionalDecoration.MODID + ":buttons/knob2x2");
		
		BaseHorz = new ModelRenderer(baseModel, 0, 0);
		BaseHorz.addBox(-10F, -10F, -5F, 20, 10, 10);
		
		BaseVert = new ModelRenderer(baseModel, 0, 20);
		BaseVert.addBox(-10F, -10F, -5F, 20, 10, 10);
		setRotation(BaseVert, 0F, 1.570796F, 0F);
		
		BaseDiag1 = new ModelRenderer(baseModel, 0, 40);
		BaseDiag1.addBox(-10.5F, -10F, -3.5F, 21, 10, 7);
		setRotation(BaseDiag1, 0F, -0.7853982F, 0F);
		
		BaseDiag2 = new ModelRenderer(baseModel, 0, 57);
		BaseDiag2.addBox(-10.5F, -10F, -3.5F, 21, 10, 7);
		setRotation(BaseDiag2, 0F, 0.7853982F, 0F);
		
		BaseBeam = new ModelRenderer(baseModel, 0, 74);
		BaseBeam.addBox(-10F, -11F, -1F, 11, 1, 2);
		setRotation(BaseBeam, 0F, 1.570796F, 0F);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
	@Override
	protected void render(int width, int height, int inVal, int outVal, float scale)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		double rotateValue = 120 - outVal * 16;
		
		GL11.glPushMatrix();
		GL11.glRotated(rotateValue, 0, 1, 0);
		GL11.glScaled(0.1, 0.1, 0.1);
		GL11.glPushMatrix();
		GL11.glRotated(180, 1, 0, 0);
		BaseHorz.render(scale);
		BaseVert.render(scale);
		BaseDiag1.render(scale);
		BaseDiag2.render(scale);
		BaseBeam.render(scale);
		GL11.glPopMatrix();
		GL11.glPopMatrix();
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
		return true;
	}
	
	@Override
	public NBTTagCompound getExtraData(EntityPlayer player)
	{
		if(player.isSneaking())
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setBoolean("crouch", true);
			return tag;
		}
		return null;
	}
	
	@Override
	public void handleButtonPress(ItemStack stack, boolean rightClick, NBTTagCompound extraData, TEButtonPanel panel)
	{
		boolean crouch = false;
		if(extraData != null)
		{
			if(extraData.getBoolean("crouch")) crouch = true;
		}
		NBTTagCompound data = stack.getTagCompound();
		if(data != null)
		{
			int val = data.getInteger("buttonState");
			if(rightClick)
			{
				val += crouch?4:1;
				if(val > 15) val = 15;
			}
			else
			{
				val -= crouch?4:1;
				if(val < 0) val = 0;
			}
			data.setInteger("buttonState", val);
		}
		else if(rightClick)
		{
			data = new NBTTagCompound();
			data.setInteger("buttonState", crouch?4:1);
			stack.setTagCompound(data);
		}
		panel.getWorldObj().playSoundEffect(panel.xCoord + 0.5, panel.yCoord + 0.5, panel.zCoord + 0.5, "random.click", 0.3F, rightClick?0.6F:0.5F);
	}
	
	@Override
	public void handleTick(ItemStack stack, TEButtonPanel panel)
	{
	}
	
	@Override
	public int getSignal(ItemStack stack)
	{
		NBTTagCompound data = stack.getTagCompound();
		if(data != null)
		{
			return data.getInteger("buttonState");
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