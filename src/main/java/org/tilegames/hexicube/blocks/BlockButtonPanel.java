package org.tilegames.hexicube.blocks;

import java.util.ArrayList;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mrtjp.projectred.api.ProjectRedAPI;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import powercrystals.minefactoryreloaded.api.rednet.IRedNetOmniNode;
import powercrystals.minefactoryreloaded.api.rednet.connectivity.RedNetConnectionType;

@Optional.Interface(iface="powercrystals.minefactoryreloaded.api.rednet.IRedNetOmniNode", modid="MineFactoryReloaded")
public abstract class BlockButtonPanel extends BlockContainer implements IRedNetOmniNode
{
	protected BlockButtonPanel(Material mat)
	{
		super(mat);
	}
	
	public static int getPartClicked(EntityPlayer player, double reachDistance, TEButtonPanel panel, boolean remote)
	{
		AxisAlignedBB[] wireparts = panel.getParts();
		
		Vec3 playerPosition = Vec3.createVectorHelper(player.posX - panel.xCoord, player.posY - panel.yCoord, player.posZ - panel.zCoord);
		if(!remote) playerPosition.yCoord += player.getEyeHeight();
		Vec3 playerLook = player.getLookVec();
		
		Vec3 playerViewOffset = Vec3.createVectorHelper(playerPosition.xCoord + playerLook.xCoord * reachDistance, playerPosition.yCoord + playerLook.yCoord * reachDistance, playerPosition.zCoord + playerLook.zCoord * reachDistance);
		int closest = -1;
		double closestdistance = Double.MAX_VALUE;
		
		for(int a = 0; a < wireparts.length; a++)
		{
			AxisAlignedBB part = wireparts[a];
			if(part == null)
			{
				continue;
			}
			MovingObjectPosition hit = part.calculateIntercept(playerPosition, playerViewOffset);
			if(hit != null)
			{
				double distance = playerPosition.distanceTo(hit.hitVec);
				if(distance < closestdistance)
				{
					closestdistance = distance;
					closest = a;
				}
			}
		}
		return closest;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xOffset, float yOffset, float zOffset)
	{
		@SuppressWarnings("deprecation")
		PlayerInteractEvent e = new PlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, x, y, z, side);
		if(MinecraftForge.EVENT_BUS.post(e) || e.getResult() == Result.DENY || e.useBlock == Result.DENY)
		{
			return false;
		}
		
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TEButtonPanel)
		{
			int subHit = getPartClicked(player, 5.0F, (TEButtonPanel)te, world.isRemote);
			if(subHit != -1) return ((TEButtonPanel)te).handleRightClick(player, subHit);
		}
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TEButtonPanel)
		{
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			
			TEButtonPanel panel = (TEButtonPanel)te;
			int subHit = getPartClicked(player, 5, panel, world.isRemote);
			if(subHit != -1)
			{
				AxisAlignedBB box = panel.getParts()[subHit];
				return AxisAlignedBB.getBoundingBox((double)x + box.minX, (double)y + box.minY, (double)z + box.minZ, (double)x + box.maxX, (double)y + box.maxY, (double)z + box.maxZ);
			}
		}
		
		return AxisAlignedBB.getBoundingBox((double)x + this.minX, (double)y + this.minY, (double)z + this.minZ, (double)x + this.maxX, (double)y + this.maxY, (double)z + this.maxZ);
	}
	
	private static TEButtonPanel panelBrokenLast;
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TEButtonPanel) panelBrokenLast = (TEButtonPanel)te;
		super.breakBlock(world, x, y, z, block, meta);
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		ArrayList<ItemStack> drops = super.getDrops(world, x, y, z, metadata, fortune);
		if(panelBrokenLast != null)
		{
			for(ButtonSlot slot : panelBrokenLast.buttons)
			{
				if(slot.button != null) drops.add(slot.button);
			}
			panelBrokenLast = null;
		}
		return drops;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Optional.Method(modid="MineFactoryReloaded")
	@Override
	public int getOutputValue(World world, int x, int y, int z, ForgeDirection side, int id)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if(!(te instanceof TEButtonPanel)) return 0;
		return getValue((TEButtonPanel)te, id);
	}
	
	@Optional.Method(modid="MineFactoryReloaded")
	@Override
	public int[] getOutputValues(World world, int x, int y, int z, ForgeDirection side)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if(!(te instanceof TEButtonPanel)) return new int[16];
		TEButtonPanel panel = (TEButtonPanel)te;
		int[] values = new int[16];
		for(int a = 0; a < 16; a++) values[a] = getValue(panel, a);
		return null;
	}
	
	private int getValue(TEButtonPanel panel, int id)
	{
		return panel.getSignal(id);
	}
	
	@Optional.Method(modid="MineFactoryReloaded")
	@Override
	public void onInputChanged(World world, int x, int y, int z, ForgeDirection side, int value)
	{
		//Called when block connects in single channel mode, shouldn't really happen.
		//If a block implements single channels, it's up to them to handle this function.
	}
	
	@Optional.Method(modid="MineFactoryReloaded")
	@Override
	public void onInputsChanged(World world, int x, int y, int z, ForgeDirection side, int[] values)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TEButtonPanel) ((TEButtonPanel)te).redNetInputs[side.ordinal()] = values;
	}
	
	@Optional.Method(modid="ProjRed|Core")
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TEButtonPanel)
		{
			TEButtonPanel panel = (TEButtonPanel)te;
			for(ForgeDirection side : ForgeDirection.values())
			{
				if(side == ForgeDirection.UNKNOWN) continue;
				RedNetConnectionType type = getConnectionType(world, x, y, z, side);
				if(type.isConnected && type.isAllSubnets) panel.bundledInputs[side.ordinal()] = ProjectRedAPI.transmissionAPI.getBundledInput(world, x, y, z, side.ordinal());
				else panel.bundledInputs[side.ordinal()] = new byte[16];
			}
		}
	}
}