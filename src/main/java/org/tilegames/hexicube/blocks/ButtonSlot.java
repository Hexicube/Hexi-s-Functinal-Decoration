package org.tilegames.hexicube.blocks;

import net.minecraft.item.ItemStack;

public class ButtonSlot
{
	public ButtonSlot(int w, int h, int d)
	{
		width = w;
		height = h;
		depth = d;
	}
	
	public int width, height, depth;
	
	public ItemStack button;
}