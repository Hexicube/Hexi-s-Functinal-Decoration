package org.tilegames.hexicube.blocks.keypad;

import org.lwjgl.opengl.GL11;
import org.tilegames.hexicube.items.ItemButton;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;

public class KeypadModel extends ModelBase
{
	private final ModelRenderer Base;
	
	public KeypadModel()
	{
		textureWidth = 34;
		textureHeight = 17;
		
		Base = new ModelRenderer(this, 0, 0);
		Base.addBox(-8, -8, -1, 16, 16, 1);
		Base.setRotationPoint(0, 0, 0);
		Base.setTextureSize(34, 17);
	}
	
	public void render(TEKeypad keypad, float f, float f1, float f2, float f3, float f4, float scale)
	{
		super.render(null, f, f1, f2, f3, f4, scale);
		setRotationAngles(f, f1, f2, f3, f4, scale, null);
		GL11.glPushMatrix();
		if(keypad != null)
		{
			GL11.glRotated(180 + keypad.rotation * 90, 0, 1, 0);
			GL11.glTranslated(0, 0, 0.5);
		}
		Base.render(scale);
		if(keypad != null)
		{
			for(int x = 0; x < 4; x++)
			{
				for(int y = 0; y < 4; y++)
				{
					GL11.glPushMatrix();
					GL11.glTranslated((-4.5f + 3*x) * scale, (-4.5f + 3*y) * scale, -scale);
					GL11.glRotated(270, 1, 0, 0);
					ItemButton button = null;
					if(keypad.buttons[x+y*4].button != null)
					{
						Item i = keypad.buttons[x+y*4].button.getItem();
						if(i instanceof ItemButton)
						{
							button = (ItemButton)i;
						}
					}
					ItemButton.renderFullyOnBlock(button, keypad.buttons[x+y*4].width, keypad.buttons[x+y*4].height, keypad.getInputSignal(keypad.channels[x+y*4]), keypad.states[x+y*4], scale, keypad.channels[x+y*4], 0.2);
					GL11.glPopMatrix();
				}
			}
		}
		GL11.glPopMatrix();
	}
	
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}
}