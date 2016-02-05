package org.tilegames.hexicube.blocks.keypad;

import org.tilegames.hexicube.FunctionalDecoration;
import org.tilegames.hexicube.blocks.BlockButtonPanel;

import cpw.mods.fml.common.Optional;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import powercrystals.minefactoryreloaded.api.rednet.connectivity.RedNetConnectionType;

public class BlockKeypad extends BlockButtonPanel
{
	public BlockKeypad()
	{
		super(Material.rock);
		setHardness(1.0f);
		setCreativeTab(CreativeTabs.tabMisc);
		setBlockName("hexifunctionaldecoration.keypad");
		setBlockTextureName("stone");
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemstack)
	{
		TEKeypad keypad = (TEKeypad)world.getTileEntity(x, y, z);
		keypad.rotation = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xOffset, float yOffset, float zOffset)
	{
		boolean triggered = super.onBlockActivated(world, x, y, z, player, side, xOffset, yOffset, zOffset);
		if(triggered) return true;
		
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TEKeypad)
		{
			TEKeypad panel = (TEKeypad)te;
			if(player.isSneaking())
			{
				if(FunctionalDecoration.playerHoldingNothing(player))
				{
					if(!world.isRemote)
					{
						panel.rotation = (panel.rotation + 1) % 4;
						panel.notifyOfChange();
					}
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TEKeypad();
	}
	
	@Override
	public int getRenderType()
	{
		return FunctionalDecoration.keypadRenderID;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if(!(te instanceof TEKeypad)) return super.getCollisionBoundingBoxFromPool(world, x, y, z);
		TEKeypad keypad = (TEKeypad)te;
		float minX, minY, minZ, maxX, maxY, maxZ;
		int rot = keypad.rotation % 4;
		if(rot == 0)
		{
			minX = 0;
			minY = 0;
			minZ = 0;
			maxX = 1;
			maxY = 1;
			maxZ = 2.0f/16;
		}
		else if(rot == 1)
		{
			minX = 14.0f/16;
			minY = 0;
			minZ = 0;
			maxX = 1;
			maxY = 1;
			maxZ = 1;
		}
		else if(rot == 2)
		{
			minX = 0;
			minY = 0;
			minZ = 14.0f/16;
			maxX = 1;
			maxY = 1;
			maxZ = 1;
		}
		else
		{
			minX = 0;
			minY = 0;
			minZ = 0;
			maxX = 2.0f/16;
			maxY = 1;
			maxZ = 1;
		}
		return AxisAlignedBB.getBoundingBox((double)x + minX, (double)y + minY, (double)z + minZ, (double)x + maxX, (double)y + maxY, (double)z + maxZ);
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z)
	{
		TileEntity te = blockAccess.getTileEntity(x, y, z);
		if(!(te instanceof TEKeypad)) return;
		int rot = ((TEKeypad)te).rotation % 4;
		if(rot == 0)
		{
			minX = 0;
			minY = 0;
			minZ = 0;
			maxX = 1;
			maxY = 1;
			maxZ = 2.0f/16;
		}
		else if(rot == 1)
		{
			minX = 14.0f/16;
			minY = 0;
			minZ = 0;
			maxX = 1;
			maxY = 1;
			maxZ = 1;
		}
		else if(rot == 2)
		{
			minX = 0;
			minY = 0;
			minZ = 14.0f/16;
			maxX = 1;
			maxY = 1;
			maxZ = 1;
		}
		else
		{
			minX = 0;
			minY = 0;
			minZ = 0;
			maxX = 2.0f/16;
			maxY = 1;
			maxZ = 1;
		}
	}
	
	@Optional.Method(modid="MineFactoryReloaded")
	@Override
	public RedNetConnectionType getConnectionType(World world, int x, int y, int z, ForgeDirection side)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if(!(te instanceof TEKeypad)) return RedNetConnectionType.None;
		TEKeypad keypad = (TEKeypad)te;
		
		if(side == ForgeDirection.NORTH && keypad.rotation == 0) return RedNetConnectionType.CableAll;
		if(side == ForgeDirection.EAST  && keypad.rotation == 1) return RedNetConnectionType.CableAll;
		if(side == ForgeDirection.SOUTH && keypad.rotation == 2) return RedNetConnectionType.CableAll;
		if(side == ForgeDirection.WEST  && keypad.rotation == 3) return RedNetConnectionType.CableAll;
		return RedNetConnectionType.None;
	}
}