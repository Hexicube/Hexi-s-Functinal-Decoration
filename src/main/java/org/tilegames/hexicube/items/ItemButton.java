package org.tilegames.hexicube.items;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.tilegames.hexicube.FunctionalDecoration;
import org.tilegames.hexicube.blocks.TEButtonPanel;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class ItemButton extends Item
{
	private static final HashMap<String, ItemButton> map = new HashMap<String, ItemButton>();
	
	private final String id;
	public ItemButton(String id)
	{
		if(map.containsKey(id))
		{
			FunctionalDecoration.logger.warn("Overriding existing button type: "+id);
		}
		this.id = id;
		map.put(id, this);
	}
	
	/**
	 * Called whenever the button is rendered. The slot size is 0x0 when renderered in inventories.
	 * @param width The slot width.
	 * @param height The slot height.
	 * @param inVal The signal strength being passed into the panel from other sources (used for lights).
	 * @param outVal The button's current signal strength (used for most buttons).
	 * @param scale The scale value (passed to the ModelRenderer).
	 */
	protected abstract void render(int width, int height, int inVal, int outVal, float scale);
	
	public static final void renderFullyOnBlock(ItemButton item, int width, int height, int inVal, int outVal, float scale, int channel, double channelEdge)
	{
		if(FunctionalDecoration.playerHoldingChannelTool(Minecraft.getMinecraft().thePlayer))
		{
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glColor3f(FunctionalDecoration.bandColours[channel][0], FunctionalDecoration.bandColours[channel][1], FunctionalDecoration.bandColours[channel][2]);
			GL11.glScaled((width/2+channelEdge)*scale, 0.1*scale, (height/2+channelEdge)*scale);
			
			GL11.glBegin(GL11.GL_QUADS);
			
			GL11.glVertex3f( 1.0f,  1.0f, -1.0f);
			GL11.glVertex3f(-1.0f,  1.0f, -1.0f);
			GL11.glVertex3f(-1.0f,  1.0f,  1.0f);
			GL11.glVertex3f( 1.0f,  1.0f,  1.0f);
			
			GL11.glVertex3f( 1.0f, -1.0f,  1.0f);
			GL11.glVertex3f(-1.0f, -1.0f,  1.0f);
			GL11.glVertex3f(-1.0f, -1.0f, -1.0f);
			GL11.glVertex3f( 1.0f, -1.0f, -1.0f);
			
			GL11.glVertex3f( 1.0f,  1.0f,  1.0f);
			GL11.glVertex3f(-1.0f,  1.0f,  1.0f);
			GL11.glVertex3f(-1.0f, -1.0f,  1.0f);
			GL11.glVertex3f( 1.0f, -1.0f,  1.0f);
			
			GL11.glVertex3f( 1.0f, -1.0f, -1.0f);
			GL11.glVertex3f(-1.0f, -1.0f, -1.0f);
			GL11.glVertex3f(-1.0f,  1.0f, -1.0f);
			GL11.glVertex3f( 1.0f,  1.0f, -1.0f);
			
			GL11.glVertex3f(-1.0f,  1.0f,  1.0f);
			GL11.glVertex3f(-1.0f,  1.0f, -1.0f);
			GL11.glVertex3f(-1.0f, -1.0f, -1.0f);
			GL11.glVertex3f(-1.0f, -1.0f,  1.0f);
			
			GL11.glVertex3f( 1.0f,  1.0f, -1.0f);
			GL11.glVertex3f( 1.0f,  1.0f,  1.0f);
			GL11.glVertex3f( 1.0f, -1.0f,  1.0f);
			GL11.glVertex3f( 1.0f, -1.0f, -1.0f);
			
			GL11.glEnd();
			
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glColor3f(1, 1, 1);
			GL11.glPopMatrix();
		}
		
		if(item != null) item.render(width, height, inVal, outVal, scale);
	}
	
	/**
	 * Called whenever the player tries to insert the button into a panel.
	 * @param width The width of the panel slot.
	 * @param height The height of the panel slot.
	 * @param depth The depth of the panel slot.
	 * @return True if the button supports the given size, false otherwise.
	 */
	public abstract boolean acceptsSize(int width, int height, int depth);
	
	/**
	 * Called whenever the user presses a button, return false if the user shouldn't be able to interact with the button.
	 * @param stack The ItemStack being pressed.
	 * @param player The player that pressed the button.
	 * @param rightClick True if the user right-clicked, false otherwise.
	 * @return True if the button can be interacted with.
	 */
	public abstract boolean allowsButtonPress(ItemStack stack, EntityPlayer player, boolean rightClick);
	
	/**
	 * Called whenever the user can press a button.
	 * @param player The player that pressed the button.
	 * @return An NBTTagCompound containing data that gets passed to handleButtonPress.
	 */
	public abstract NBTTagCompound getExtraData(EntityPlayer player);
	
	/**
	 * Called whenever the user presses a button.
	 * @param stack The ItemStack being pressed.
	 * @param rightClick True if the user right-clicked, false otherwise.
	 * @param extraData The NBTTagCompound returned from getExtraData.
	 * @param panel The TEButtonPanel the button is on.
	 */
	public abstract void handleButtonPress(ItemStack stack, boolean rightClick, NBTTagCompound extraData, TEButtonPanel panel);
	
	/**
	 * Called once per tick to allow the button to do tick-based logic.
	 * @param stack The ItemStack being ticked.
	 * @param panel The ticking TEButtonPanel.
	 */
	public abstract void handleTick(ItemStack stack, TEButtonPanel panel);
	
	/**
	 * Called once per tick to get the current strength of the button.
	 * @param stack The ItemStack being checked.
	 * @return An integer from 0 to 15.
	 */
	public abstract int getSignal(ItemStack stack);
	
	/**
	 * Called when the button is removed from a panel.
	 * @param stack The ItemStack that was removed.
	 */
	public abstract void clearState(ItemStack stack);
	
	public final String getIdentifier()
	{
		return id;
	}
	
	public static final ItemButton fromIdentifier(String id)
	{
		return map.get(id);
	}
}