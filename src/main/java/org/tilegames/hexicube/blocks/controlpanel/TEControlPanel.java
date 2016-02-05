package org.tilegames.hexicube.blocks.controlpanel;

import org.tilegames.hexicube.blocks.ButtonSlot;
import org.tilegames.hexicube.blocks.TEButtonPanel;

import cpw.mods.fml.common.Optional;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

@Optional.Interface(iface="mrtjp.projectred.api.IBundledTile", modid="ProjRed|Core")
public class TEControlPanel extends TEButtonPanel
{
	public int rotation;
	
	public TEControlPanel()
	{
		super(8);
		for(int a = 0; a < 4; a++) buttons[a] = new ButtonSlot(2, 4, 2);
		for(int a = 4; a < 8; a++) buttons[a] = new ButtonSlot(2, 2, 1);
	}
	
	@Override
	public AxisAlignedBB[] getParts()
	{
		AxisAlignedBB[] parts = new AxisAlignedBB[8];
		
		int rot = rotation % 4;
		if(rot == 0)
		{
			parts[0] = AxisAlignedBB.getBoundingBox( 2.5/16, 8.5/16, 12.0/16,  4.5/16, 12.5/16, 1);
			parts[1] = AxisAlignedBB.getBoundingBox( 5.5/16, 8.5/16, 12.0/16,  7.5/16, 12.5/16, 1);
			parts[2] = AxisAlignedBB.getBoundingBox( 8.5/16, 8.5/16, 12.0/16, 10.5/16, 12.5/16, 1);
			parts[3] = AxisAlignedBB.getBoundingBox(11.5/16, 8.5/16, 12.0/16, 13.5/16, 12.5/16, 1);
			
			parts[4] = AxisAlignedBB.getBoundingBox( 2.5/16, 12.0/16, 9.0/16,  4.5/16, 14.0/16, 11.0/16);
			parts[5] = AxisAlignedBB.getBoundingBox( 5.5/16, 12.0/16, 9.0/16,  7.5/16, 14.0/16, 11.0/16);
			parts[6] = AxisAlignedBB.getBoundingBox( 8.5/16, 12.0/16, 9.0/16, 10.5/16, 14.0/16, 11.0/16);
			parts[7] = AxisAlignedBB.getBoundingBox(11.5/16, 12.0/16, 9.0/16, 13.5/16, 14.0/16, 11.0/16);
		}
		else if(rot == 1)
		{
			parts[0] = AxisAlignedBB.getBoundingBox(0, 8.5/16,  2.5/16, 4.0/16, 12.5/16,  4.5/16);
			parts[1] = AxisAlignedBB.getBoundingBox(0, 8.5/16,  5.5/16, 4.0/16, 12.5/16,  7.5/16);
			parts[2] = AxisAlignedBB.getBoundingBox(0, 8.5/16,  8.5/16, 4.0/16, 12.5/16, 10.5/16);
			parts[3] = AxisAlignedBB.getBoundingBox(0, 8.5/16, 11.5/16, 4.0/16, 12.5/16, 13.5/16);
			
			parts[4] = AxisAlignedBB.getBoundingBox(5.0/16, 12.0/16,  2.5/16, 7.0/16, 14.0/16,  4.5/16);
			parts[5] = AxisAlignedBB.getBoundingBox(5.0/16, 12.0/16,  5.5/16, 7.0/16, 14.0/16,  7.5/16);
			parts[6] = AxisAlignedBB.getBoundingBox(5.0/16, 12.0/16,  8.5/16, 7.0/16, 14.0/16, 10.5/16);
			parts[7] = AxisAlignedBB.getBoundingBox(5.0/16, 12.0/16, 11.5/16, 7.0/16, 14.0/16, 13.5/16);
		}
		else if(rot == 2)
		{
			parts[3] = AxisAlignedBB.getBoundingBox( 2.5/16, 8.5/16, 0,  4.5/16, 12.5/16, 4.0/16);
			parts[2] = AxisAlignedBB.getBoundingBox( 5.5/16, 8.5/16, 0,  7.5/16, 12.5/16, 4.0/16);
			parts[1] = AxisAlignedBB.getBoundingBox( 8.5/16, 8.5/16, 0, 10.5/16, 12.5/16, 4.0/16);
			parts[0] = AxisAlignedBB.getBoundingBox(11.5/16, 8.5/16, 0, 13.5/16, 12.5/16, 4.0/16);
			
			parts[7] = AxisAlignedBB.getBoundingBox( 2.5/16, 12.0/16, 5.0/16,  4.5/16, 14.0/16, 7.0/16);
			parts[6] = AxisAlignedBB.getBoundingBox( 5.5/16, 12.0/16, 5.0/16,  7.5/16, 14.0/16, 7.0/16);
			parts[5] = AxisAlignedBB.getBoundingBox( 8.5/16, 12.0/16, 5.0/16, 10.5/16, 14.0/16, 7.0/16);
			parts[4] = AxisAlignedBB.getBoundingBox(11.5/16, 12.0/16, 5.0/16, 13.5/16, 14.0/16, 7.0/16);
		}
		else
		{
			parts[3] = AxisAlignedBB.getBoundingBox(12.0/16, 8.5/16,  2.5/16, 1, 12.5/16,  4.5/16);
			parts[2] = AxisAlignedBB.getBoundingBox(12.0/16, 8.5/16,  5.5/16, 1, 12.5/16,  7.5/16);
			parts[1] = AxisAlignedBB.getBoundingBox(12.0/16, 8.5/16,  8.5/16, 1, 12.5/16, 10.5/16);
			parts[0] = AxisAlignedBB.getBoundingBox(12.0/16, 8.5/16, 11.5/16, 1, 12.5/16, 13.5/16);
			
			parts[7] = AxisAlignedBB.getBoundingBox(9.0/16, 12.0/16,  2.5/16, 11.0/16, 14.0/16,  4.5/16);
			parts[6] = AxisAlignedBB.getBoundingBox(9.0/16, 12.0/16,  5.5/16, 11.0/16, 14.0/16,  7.5/16);
			parts[5] = AxisAlignedBB.getBoundingBox(9.0/16, 12.0/16,  8.5/16, 11.0/16, 14.0/16, 10.5/16);
			parts[4] = AxisAlignedBB.getBoundingBox(9.0/16, 12.0/16, 11.5/16, 11.0/16, 14.0/16, 13.5/16);
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
		if(ForgeDirection.getOrientation(side) == ForgeDirection.DOWN) return true;
		return false;
	}
}