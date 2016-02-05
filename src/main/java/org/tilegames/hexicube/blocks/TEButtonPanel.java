package org.tilegames.hexicube.blocks;

import java.io.IOException;

import org.tilegames.hexicube.FunctionalDecoration;
import org.tilegames.hexicube.items.ItemButton;

import cpw.mods.fml.common.Optional;
import mrtjp.projectred.api.IBundledTile;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

@Optional.Interface(iface="mrtjp.projectred.api.IBundledTile", modid="ProjRed|Core")
public abstract class TEButtonPanel extends TileEntity implements IBundledTile
{
	public TEButtonPanel(int slots)
	{
		numSlots = slots;
		
		buttons = new ButtonSlot[slots];
		states = new int[slots];
		channels = new int[slots];
		
		redNetInputs = new int[6][16];
		bundledInputs = new byte[6][16];
	}
	
	private int numSlots;
	
	public ButtonSlot[] buttons;
	public int[] states;
	public int[] channels;
	
	public int[][] redNetInputs;
	public byte[][] bundledInputs;
	
	/**
	 * Called to obtain bounding boxes for all button slots. null values are accepted in the array if buttons don't always exist, but the order must be consistent.
	 * @return A list of boxes defining button locations.
	 */
	public abstract AxisAlignedBB[] getParts();
	
	public boolean handleLeftClick(EntityPlayer player, int slot)
	{
		if(slot < 0 || slot >= numSlots) return false;
		if(buttons[slot].button == null) return false;
		if(!(buttons[slot].button.getItem() instanceof ItemButton)) return false;
		if(!((ItemButton)buttons[slot].button.getItem()).allowsButtonPress(buttons[slot].button, player, false)) return false;
		if(player.worldObj.isRemote)
		{
			if(player instanceof EntityClientPlayerMP)
			{
				NBTTagCompound compound = new NBTTagCompound();
				compound.setInteger("button", slot);
				compound.setInteger("x", xCoord);
				compound.setInteger("y", yCoord);
				compound.setInteger("z", zCoord);
				compound.setInteger("world", player.worldObj.provider.dimensionId);
				compound.setBoolean("rightmouse", false);
				NBTTagCompound tag = ((ItemButton)buttons[slot].button.getItem()).getExtraData(player);
				if(tag != null) compound.setTag("extradata", tag);
				
				try
				{
					C17PacketCustomPayload packet = new C17PacketCustomPayload("HexiFunctionalDecoration", CompressedStreamTools.compress(compound));
					((EntityClientPlayerMP)player).sendQueue.addToSendQueue(packet);
				}
				catch(IOException e1)
				{
					e1.printStackTrace();
				}
			}
		}
		return true;
	}
	
	public boolean handleRightClick(EntityPlayer player, int slot)
	{
		if(slot < 0 || slot >= numSlots) return false;
		if(FunctionalDecoration.playerHoldingTool(player))
		{
			if(buttons[slot].button == null) return false;
			if(!worldObj.isRemote)
			{
				if(buttons[slot].button.getItem() instanceof ItemButton)
				{
					((ItemButton)buttons[slot].button.getItem()).clearState(buttons[slot].button);
				}
				EntityItem item = player.entityDropItem(buttons[slot].button, 0);
				item.delayBeforeCanPickup = 0;
				buttons[slot].button = null;
				notifyOfSelfChange();
			}
			return true;
		}
		if(FunctionalDecoration.playerHoldingChannelTool(player))
		{
			if(!worldObj.isRemote)
			{
				if(player.isSneaking()) channels[slot] = (channels[slot] + 15) % 16;
				else channels[slot] = (channels[slot] + 1) % 16;
				if(states[slot] > 0) notifyOfChange();
				else notifyOfSelfChange();
			}
			return true;
		}
		if(FunctionalDecoration.playerHoldingButton(player))
		{
			if(buttons[slot].button == null)
			{
				ItemButton button = (ItemButton)player.getHeldItem().getItem();
				if(button.acceptsSize(buttons[slot].width, buttons[slot].height, buttons[slot].depth))
				{
					buttons[slot].button = player.inventory.decrStackSize(player.inventory.currentItem, 1);
					//TODO: play placement sound? this could be custom...
					if(!worldObj.isRemote) notifyOfSelfChange();
					return true;
				}
			}
			return false;
		}
		if(FunctionalDecoration.playerHoldingNothing(player))
		{
			if(buttons[slot].button == null) return false;
			if(!(buttons[slot].button.getItem() instanceof ItemButton)) return false;
			if(!((ItemButton)buttons[slot].button.getItem()).allowsButtonPress(buttons[slot].button, player, true)) return false;
			if(player.worldObj.isRemote)
			{
				if(player instanceof EntityClientPlayerMP)
				{
					NBTTagCompound compound = new NBTTagCompound();
					compound.setInteger("button", slot);
					compound.setInteger("x", xCoord);
					compound.setInteger("y", yCoord);
					compound.setInteger("z", zCoord);
					compound.setInteger("world", player.worldObj.provider.dimensionId);
					compound.setBoolean("rightmouse", true);
					NBTTagCompound tag = ((ItemButton)buttons[slot].button.getItem()).getExtraData(player);
					if(tag != null) compound.setTag("extradata", tag);
					
					try
					{
						C17PacketCustomPayload packet = new C17PacketCustomPayload("HexiFunctionalDecoration", CompressedStreamTools.compress(compound));
						((EntityClientPlayerMP)player).sendQueue.addToSendQueue(packet);
					}
					catch(IOException e1)
					{
						e1.printStackTrace();
					}
				}
			}
			return true;
		}
		return false;
	}
	
	public void handleButton(int slot, boolean rightMouse, NBTTagCompound extraData)
	{
		if(buttons[slot].button != null)
		{
			if(!(buttons[slot].button.getItem() instanceof ItemButton)) return;
			((ItemButton)buttons[slot].button.getItem()).handleButtonPress(buttons[slot].button, rightMouse, extraData, this);
			notifyOfSelfChange();
		}
	}
	
	@Override
	public void updateEntity()
	{
		if(!worldObj.isRemote)
		{
			boolean needsUpdate = false;
			for(int a = 0; a < numSlots; a++)
			{
				int prev = states[a];
				int cur = 0;
				if(buttons[a].button != null)
				{
					if(buttons[a].button.getItem() instanceof ItemButton)
					{
						ItemButton button = (ItemButton)buttons[a].button.getItem();
						button.handleTick(buttons[a].button, this);
						cur = button.getSignal(buttons[a].button);
					}
					else cur = 0;
				}
				if(prev != cur)
				{
					states[a] = cur;
					needsUpdate = true;
				}
			}
			if(needsUpdate) notifyOfChange();
		}
	}
	
	public void notifyOfChange()
	{
		notifyOfSelfChange();
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, blockType);
	}
	
	public void notifyOfSelfChange()
	{
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	public int getSignal(int channel)
	{
		int val = 0;
		for(int a = 0; a < numSlots; a++)
		{
			if(channels[a] == channel) val = Math.max(val, states[a]);
		}
		return val;
	}
	
	public int getInputSignal(int slot)
	{
		if(slot < 0 || slot >= 16) return 0;
		
		int val = 0;
		for(int a = 0; a < 6; a++)
		{
			val = Math.max(val, redNetInputs[a][slot]);
			val = Math.max(val, bundledInputs[a][slot]);
		}
		return val;
	}
	
	@Optional.Method(modid="ProjRed|Core")
	@Override
	public byte[] getBundledSignal(int side)
	{
		if(!canConnectBundled(side)) return null;
		byte[] data = new byte[16];
		for(int a = 0; a < 16; a++) data[a] = (byte)getSignal(a);
		return data;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		states = compound.getIntArray("states");
		if(states.length == 0) states = new int[numSlots];
		channels = compound.getIntArray("channels");
		if(channels.length == 0) channels = new int[numSlots];
		NBTTagCompound buttonNBT = compound.getCompoundTag("buttons");
		for(int a = 0; a < numSlots; a++)
		{
			if(buttonNBT.hasKey(""+a))
			{
				buttons[a].button = ItemStack.loadItemStackFromNBT(buttonNBT.getCompoundTag(""+a));
			}
			else buttons[a].button = null;
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setIntArray("states", states);
		compound.setIntArray("channels", channels);
		NBTTagCompound buttonNBT = new NBTTagCompound();
		for(int a = 0; a < numSlots; a++)
		{
			if(buttons[a].button != null)
			{
				buttonNBT.setTag(""+a, buttons[a].button.writeToNBT(new NBTTagCompound()));
			}
		}
		compound.setTag("buttons", buttonNBT);
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound compound = new NBTTagCompound();
		writeToNBT(compound);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, compound);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		if(worldObj.isRemote)
		{
			NBTTagCompound compound = pkt.func_148857_g();
			readFromNBT(compound);
		}
	}
}