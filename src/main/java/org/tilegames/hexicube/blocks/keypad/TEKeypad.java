package org.tilegames.hexicube.blocks.keypad;

import org.tilegames.hexicube.blocks.ButtonSlot;
import org.tilegames.hexicube.blocks.TEButtonPanel;

import cpw.mods.fml.common.Optional;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

public class TEKeypad extends TEButtonPanel
{
	public int rotation;
	
	public TEKeypad()
	{
		super(16);
		for(int a = 0; a < 16; a++) buttons[a] = new ButtonSlot(2, 2, 1);
	}
	
	@Override
	public AxisAlignedBB[] getParts()
	{
		AxisAlignedBB[] parts = new AxisAlignedBB[16];
		
		int rot = rotation % 4;
		if(rot == 0)
		{
			for(int x = 0; x < 4; x++)
			{
				for(int y = 0; y < 4; y++)
				{
					parts[x+y*4] = AxisAlignedBB.getBoundingBox((2.5+x*3)/16.0, (11.5-y*3)/16.0, 1.0/16, (4.5+x*3)/16.0, (13.5-y*3)/16.0, 2.0/16);
				}
			}
		}
		else if(rot == 1)
		{
			for(int x = 0; x < 4; x++)
			{
				for(int y = 0; y < 4; y++)
				{
					parts[x+y*4] = AxisAlignedBB.getBoundingBox(14.0/16, (11.5-y*3)/16.0, (2.5+x*3)/16.0, 15.0/16, (13.5-y*3)/16.0, (4.5+x*3)/16.0);
				}
			}
		}
		else if(rot == 2)
		{
			for(int x = 0; x < 4; x++)
			{
				for(int y = 0; y < 4; y++)
				{
					parts[x+y*4] = AxisAlignedBB.getBoundingBox((11.5-x*3)/16.0, (11.5-y*3)/16.0, 14.0/16, (13.5-x*3)/16.0, (13.5-y*3)/16.0, 15.0/16);
				}
			}
		}
		else if(rot == 3)
		{
			for(int x = 0; x < 4; x++)
			{
				for(int y = 0; y < 4; y++)
				{
					parts[x+y*4] = AxisAlignedBB.getBoundingBox(1.0/16, (11.5-y*3)/16.0, (11.5-x*3)/16.0, 2.0/16, (13.5-y*3)/16.0, (13.5-x*3)/16.0);
				}
			}
		}
		
		return parts;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		rotation = compound.getInteger("rotation");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setInteger("rotation", rotation);
	}
	
	@Optional.Method(modid="ProjRed|Core")
	@Override
	public boolean canConnectBundled(int side)
	{
		ForgeDirection dir = ForgeDirection.getOrientation(side);
		if(dir == ForgeDirection.NORTH && rotation == 0) return true;
		if(dir == ForgeDirection.EAST  && rotation == 1) return true;
		if(dir == ForgeDirection.SOUTH && rotation == 2) return true;
		if(dir == ForgeDirection.WEST  && rotation == 3) return true;
		return false;
	}
}