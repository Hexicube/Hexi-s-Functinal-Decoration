package org.tilegames.hexicube.blocks.keypad;

import org.lwjgl.opengl.GL11;
import org.tilegames.hexicube.FunctionalDecoration;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

public class TEKeypadRenderer extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler
{
	private static final KeypadModel model = new KeypadModel();
	private static final ResourceLocation textures = new ResourceLocation(FunctionalDecoration.MODID + ":textures/blocks/keypad.png");
	
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale)
	{
		if(!(te instanceof TEKeypad)) return; //should never happen
		TEKeypad keypad = (TEKeypad)te;
		
		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(textures);
		
		GL11.glPushMatrix();
		GL11.glRotated(180, 0, 0, 1);
		
		model.render(keypad, 0f, 0f, -0.1f, 0f, 0f, 0.0625f);
		
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(textures);
		GL11.glPushMatrix();
		GL11.glRotated(180, 0, 0, 1);
		GL11.glPushMatrix();
		GL11.glRotated(180, 0, 1, 0);
		model.render((TEKeypad)null, 0f, 0f, -0.1f, 0f, 0f, 0.0625f);
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		return false;
	}
	
	@Override
	public boolean shouldRender3DInInventory(int modelId)
	{
		return true;
	}
	
	@Override
	public int getRenderId()
	{
		return FunctionalDecoration.keypadRenderID;
	}
}