package org.tilegames.hexicube.blocks.controlpanel;

import org.lwjgl.opengl.GL11;
import org.tilegames.hexicube.items.ItemButton;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.item.Item;

public class ControlPanelModel extends ModelBase
{
	private final ModelRenderer BackBase, BottomBase, SlantBase;
	
	public ControlPanelModel()
	{
		textureWidth = 48;
		textureHeight = 57;
		
		BackBase = new ModelRenderer(this, 0, 0);
		BackBase.addBox(0F, 0F, 0F, 16, 16, 8);
		BackBase.setRotationPoint(-8F, -8F, 0F);
		BackBase.setTextureSize(48, 57);
		setRotation(BackBase, 0F, 0F, 0F);
		
		BottomBase = new ModelRenderer(this, 0, 24);
		BottomBase.addBox(0F, 0F, 0F, 16, 8, 8);
		BottomBase.setRotationPoint(-8F, 0F, -8F);
		BottomBase.setTextureSize(48, 57);
		setRotation(BottomBase, 0F, 0F, 0F);
		
		SlantBase = new ModelRenderer(this, 0, 40);
		SlantBase.addBox(0F, 0F, 0F, 13, 7, 10);
		SlantBase.setRotationPoint(-6.5F, 0F, -8F);
		SlantBase.setTextureSize(48, 57);
		setRotation(SlantBase, 0.6283185F, 0F, 0F);
	}
	
	public void render(TEControlPanel entity, float x, float y, float z, float f3, float f4, float scale)
	{
		super.render(null, x, y, z, f3, f4, scale);
		setRotationAngles(x, y, z, f3, f4, scale);
		
		GL11.glPushMatrix();
		if(entity != null) GL11.glRotated(180 + entity.rotation * 90, 0, 1, 0);
		
		BackBase.render(scale);
		BottomBase.render(scale);
		SlantBase.render(scale);
		
		for(int a = 0; a < 4; a++)
		{
			if(entity != null)
			{
				GL11.glPushMatrix();
				GL11.glTranslated((-4.5 + 3*a) * scale, -1.75 * scale, -5.5 * scale);
				GL11.glRotated(216, 1, 0, 0);
				ItemButton button = null;
				if(entity.buttons[a].button != null)
				{
					Item i = entity.buttons[a].button.getItem();
					if(i instanceof ItemButton)
					{
						button = (ItemButton)i;
					}
				}
				ItemButton.renderFullyOnBlock(button, entity.buttons[a].width, entity.buttons[a].height, entity.getInputSignal(entity.channels[a]), entity.states[a], scale, entity.channels[a], 0.2);
				GL11.glPopMatrix();
			}
		}
		
		for(int a = 0; a < 4; a++)
		{
			if(entity != null)
			{
				GL11.glPushMatrix();
				GL11.glTranslated((-4.5 + 3*a) * scale, -4.666666 * scale, -1.5 * scale);
				GL11.glRotated(216, 1, 0, 0);
				ItemButton button = null;
				if(entity.buttons[a+4].button != null)
				{
					Item i = entity.buttons[a+4].button.getItem();
					if(i instanceof ItemButton)
					{
						button = (ItemButton)i;
					}
				}
				ItemButton.renderFullyOnBlock(button, entity.buttons[a+4].width, entity.buttons[a+4].height, entity.getInputSignal(entity.channels[a+4]), entity.states[a+4], scale, entity.channels[a+4], 0.2);
				GL11.glPopMatrix();
			}
		}
		
		GL11.glPopMatrix();
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
	public void setRotationAngles(float x, float y, float z, float f3, float f4, float scale)
	{
		super.setRotationAngles(x, y, z, f3, f4, scale, null);
	}
}