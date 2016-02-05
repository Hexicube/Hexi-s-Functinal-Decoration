package org.tilegames.hexicube.blocks.controlpanel;

import org.tilegames.hexicube.FunctionalDecoration;
import org.tilegames.hexicube.blocks.BlockButtonPanel;

import cpw.mods.fml.common.Optional;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import powercrystals.minefactoryreloaded.api.rednet.connectivity.RedNetConnectionType;

@Optional.Interface(iface="powercrystals.minefactoryreloaded.api.rednet.IRedNetOutputNode", modid="MineFactoryReloaded")
public class BlockControlPanel extends BlockButtonPanel
{
	public BlockControlPanel()
	{
		super(Material.rock);
		setHardness(1.0f);
		setCreativeTab(CreativeTabs.tabMisc);
		setBlockName("hexifunctionaldecoration.controlPanel");
		setBlockTextureName("stone");
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemstack)
	{
		((TEControlPanel)world.getTileEntity(x, y, z)).rotation = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xOffset, float yOffset, float zOffset)
	{
		boolean triggered = super.onBlockActivated(world, x, y, z, player, side, xOffset, yOffset, zOffset);
		if(triggered) return true;
		
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TEControlPanel)
		{
			TEControlPanel panel = (TEControlPanel)te;
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
		return new TEControlPanel();
	}
	
	@Override
	public int getRenderType()
	{
		return FunctionalDecoration.controlPanelRenderID;
	}
	
	@Optional.Method(modid="MineFactoryReloaded")
	@Override
	public RedNetConnectionType getConnectionType(World world, int x, int y, int z, ForgeDirection side)
	{
		if(side == ForgeDirection.DOWN) return RedNetConnectionType.CableAll;
		return RedNetConnectionType.None;
	}
}