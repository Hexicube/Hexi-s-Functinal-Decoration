/* Operation embedded to-do list is a go!
 * 
 * GENERAL WORK
 * TODO: Let blocks define label areas that accept name-tags.
 * TODO: Add recipes.
 * TODO: Add reverse recipes for buttons to obtain the frame and button parts.
 * 
 * SPECIFIC BLOCKS
 * TODO: Primitive Biometric Scanner; emits a 4-bit hash of the player UUID when stood on.
 * TODO: Primitive Fingerprint Scanner; emits a 4-bit hash of the player UUID when stood on.
 * TODO: Biometric Scanner; emits an 8-bit hash of the player UUID when stood on.
 * TODO: Fingerprint Scanner; emits an 8-bit hash of the player UUID when activated.
 * TODO: Advanced Biometric Scanner; emits the lower half of the player UUID when stood on.
 * TODO: Advanced Fingerprint Scanner; emits the upper half of the player UUID when activated.
 * TODO: Ultimate Biometric Scanner; emits a specific 8-bit signal for each player as configured.
 * TODO: Ultimate Fingerprint Scanner; emits a specific 8-bit signal for each player as configured.
 * TODO: Player Analyst; used with ultimate scanners, handles the configurations.
 * TODO: Bar-chart; shows recent activity of all channels.
 * TODO: Variable light block; colour assigned via one channel for each wool colour (black interpreted as white, white interpreted as off).
 * TODO: Advanced variable light block; colour assigned via 3 channels for RGB data (brightness determined by highest level).
 * 
 * SPECIFIC PANELS
 * TODO: Version of control panel with 2 4x4 button slots.
 * TODO: Mostly flat block with 14x14 button slot.
 * TODO: Version of keypad with 8 2x4 button slots.
 * 
 * SPECIFIC BUTTONS
 * TODO: 2x2 Light.
 * TODO: 2x4 Lever.
 * TODO: 4x4 Knob; left click reduces by one and right click increases by one. Crouching changes increments to 4.
 * TODO: 2x4 Sliding Lever; functions similarly to knobs.
 * TODO: Light-up buttons; lit while depressed.
 * TODO: Light-up channel buttons; lights channel colour when channel has a signal.
 * TODO: 2x2 Dip-switch variants for lever.
 * TODO: Large red mushroom button (4x4/12x12).
 * TODO: Mollyguard button (crouch to toggle guard, can only use when guard is open).
 * TODO: Toggle button.
 * 
 * CRAFTING COMPONENTS
 * TODO: Button frame (2x2/2x4/4x4/12x12); used to create buttons of specific sizes, made from sticks.
 * TODO: Button acceptor (2x2/2x4/4x4/12x12); used to create panels that accept specific sizes, made from sticks.
 * TODO: Plastic; used for lights and clear surfaces, oredicted.
 * TODO: LED; used for lights, possibly oredicted.
 */

package org.tilegames.hexicube;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tilegames.hexicube.blocks.BlockButtonPanel;
import org.tilegames.hexicube.blocks.TEButtonPanel;
import org.tilegames.hexicube.blocks.controlpanel.*;
import org.tilegames.hexicube.blocks.keypad.*;
import org.tilegames.hexicube.items.*;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mrtjp.projectred.api.IScrewdriver;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;
import powercrystals.minefactoryreloaded.core.MFRUtil;

@Mod(modid=FunctionalDecoration.MODID, version=FunctionalDecoration.VERSION, dependencies="after:ProjRed|Core;after:MineFactoryReloaded")
public class FunctionalDecoration
{
	public static final String MODID = "hexiFunctionalDecoration";
	public static final String VERSION = "0.3";
	
	@Instance(MODID)
	public static FunctionalDecoration instance;
	
	public static final Logger logger = LogManager.getLogger(FunctionalDecoration.MODID);
	
	public static final BlockControlPanel controlPanel = new BlockControlPanel();
	public static int controlPanelRenderID;
	
	public static final BlockKeypad keypad = new BlockKeypad();
	public static int keypadRenderID;
	
	public static final ItemButton buttonBasic2x2 = new ItemButtonBasic2x2();
	public static final ItemButton buttonKnob2x2 = new ItemButtonKnob2x2();
	
	@SidedProxy(clientSide="org.tilegames.hexicube.FunctionalDecorationClientProxy", serverSide="org.tilegames.hexicube.FunctionalDecorationServerProxy")
	public static FunctionalDecorationProxy proxy;
	
	public static final float[][] bandColours;
	static
	{
		bandColours = new float[16][];
		bandColours[0] =  new float[]{255.0f / 255.0f, 255.0f / 255.0f, 255.0f / 255.0f}; //white
		bandColours[1] =  new float[]{255.0f / 255.0f, 128.0f / 255.0f,  64.0f / 255.0f}; //orange
		bandColours[2] =  new float[]{255.0f / 255.0f,  64.0f / 255.0f, 255.0f / 255.0f}; //magenta
		bandColours[3] =  new float[]{128.0f / 255.0f, 128.0f / 255.0f, 255.0f / 255.0f}; //light blue
		bandColours[4] =  new float[]{255.0f / 255.0f, 255.0f / 255.0f,   0.0f / 255.0f}; //yellow
		bandColours[5] =  new float[]{ 64.0f / 255.0f, 255.0f / 255.0f,  64.0f / 255.0f}; //lime
		bandColours[6] =  new float[]{255.0f / 255.0f, 196.0f / 255.0f, 255.0f / 255.0f}; //pink
		bandColours[7] =  new float[]{ 96.0f / 255.0f,  96.0f / 255.0f,  96.0f / 255.0f}; //gray
		bandColours[8] =  new float[]{168.0f / 255.0f, 168.0f / 255.0f, 168.0f / 255.0f}; //light gray
		bandColours[9] =  new float[]{ 64.0f / 255.0f, 128.0f / 255.0f, 128.0f / 255.0f}; //cyan
		bandColours[10] = new float[]{128.0f / 255.0f,   0.0f / 255.0f, 192.0f / 255.0f}; //purple
		bandColours[11] = new float[]{  0.0f / 255.0f,   0.0f / 255.0f, 255.0f / 255.0f}; //blue
		bandColours[12] = new float[]{ 96.0f / 255.0f,  64.0f / 255.0f,  32.0f / 255.0f}; //brown
		bandColours[13] = new float[]{  0.0f / 255.0f, 128.0f / 255.0f,   0.0f / 255.0f}; //green
		bandColours[14] = new float[]{255.0f / 255.0f,   0.0f / 255.0f,   0.0f / 255.0f}; //red
		bandColours[15] = new float[]{ 32.0f / 255.0f,  32.0f / 255.0f,  32.0f / 255.0f}; //black
	}
	
	private static boolean hasProjRed, hasMFR;
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) throws Exception
	{
		if(Loader.isModLoaded("ProjRed|Core")) hasProjRed = true;
		if(Loader.isModLoaded("MineFactoryReloaded")) hasMFR = true;
		if(!hasProjRed && !hasMFR)
		{
			throw new Exception("Hexi's Functional Decoration requires either Project:Red or MineFactory Reloaded!");
		}
		//TODO: detect wrenches
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e)
	{
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
		
		proxy.registerSided();
		
		GameRegistry.registerBlock(controlPanel, "controlPanel");
		GameRegistry.registerTileEntity(TEControlPanel.class, MODID+"TEControlPanel");
		
		GameRegistry.registerBlock(keypad, "keypad");
		GameRegistry.registerTileEntity(TEKeypad.class, MODID+"TEKeypad");
		
		GameRegistry.registerItem(buttonBasic2x2, MODID+"Button_Basic2x2");
		GameRegistry.registerItem(buttonKnob2x2, MODID+"Button_Knob2x2");
	}
	
	@EventHandler
	public void serverLoad(FMLServerStartingEvent event)
	{
		NetworkRegistry.INSTANCE.newChannel("HexiFunctionalDecoration", new FunctionalDecorationPacketHandler());
	}
	
	private int reclickDelay = -1;
	
	@SubscribeEvent
	@SideOnly(value=Side.CLIENT)
	public void onPlayerInteract(MouseEvent event)
	{
		if(event.button == 0)
		{
			if(event.buttonstate) checkForLeftMouse(event);
			else reclickDelay = -1;
		}
	}
	
	@SubscribeEvent
	@SideOnly(value=Side.CLIENT)
	public void onTick(ClientTickEvent event)
	{
		if(event.phase == Phase.END)
		{
			if(reclickDelay != -1)
			{
				if(reclickDelay > 0) reclickDelay--;
				if(reclickDelay == 0) checkForLeftMouse(null);
			}
		}
	}
	
	private void checkForLeftMouse(MouseEvent event)
	{
		if(Minecraft.getMinecraft().currentScreen == null)
		{
			MovingObjectPosition pos = Minecraft.getMinecraft().objectMouseOver;
			if(pos.typeOfHit == MovingObjectType.BLOCK)
			{
				EntityPlayer player = Minecraft.getMinecraft().thePlayer;
				TileEntity te = player.worldObj.getTileEntity(pos.blockX, pos.blockY, pos.blockZ);
				if(te instanceof TEButtonPanel)
				{
					TEButtonPanel panel = (TEButtonPanel)te;
					int part = BlockButtonPanel.getPartClicked(player, 5.0, panel, true);
					if(part != -1)
					{
						if(panel.handleLeftClick(player, part))
						{
							if(event != null) event.setCanceled(true);
							player.swingItem();
							reclickDelay = 4;
						}
						return;
					}
				}
			}
		}
		reclickDelay = -1;
	}
	
	public static boolean playerHoldingChannelTool(EntityPlayer player)
	{
		if(hasProjRed)
		{
			if(playerHoldingScrewdriver(player)) return true;
		}
		if(hasMFR)
		{
			if(playerHoldingHammer(player)) return true;
		}
		//TODO: detect wrenches
		return false;
	}
	
	@Optional.Method(modid="ProjRed|Core")
	private static boolean playerHoldingScrewdriver(EntityPlayer player)
	{
		ItemStack hand = player.getHeldItem();
		if(hand == null) return false;
		if(hand.getItem() instanceof IScrewdriver) return true;
		return false;
	}
	
	@Optional.Method(modid="MineFactoryReloaded")
	private static boolean playerHoldingHammer(EntityPlayer player)
	{
		return MFRUtil.isHoldingHammer(player);
	}
	
	//TODO: create item for pulling buttons out?
	public static boolean playerHoldingTool(EntityPlayer player)
	{
		ItemStack hand = player.getHeldItem();
		if(hand == null) return false;
		if(hand.getItem().equals(Items.stick)) return true;
		return false;
	}
	
	public static boolean playerHoldingButton(EntityPlayer player)
	{
		ItemStack s = player.getHeldItem();
		if(s != null)
		{
			if(s.getItem() instanceof ItemButton) return true;
		}
		return false;
	}
	
	public static boolean playerHoldingNothing(EntityPlayer player)
	{
		if(playerHoldingChannelTool(player)) return false;
		if(playerHoldingTool(player)) return false;
		if(playerHoldingButton(player)) return false;
		return true;
	}
}