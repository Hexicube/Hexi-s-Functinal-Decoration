package org.tilegames.hexicube.blocks.controlpanel;

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

public class TEControlPanelRenderer extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler
{
	private static final ControlPanelModel model = new ControlPanelModel();
	private static final ResourceLocation texture = new ResourceLocation(FunctionalDecoration.MODID + ":textures/blocks/controlpanel.png");
	
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale)
	{
		if(!(te instanceof TEControlPanel)) return; //should never happen
		TEControlPanel panel = (TEControlPanel)te;
		
		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		
		GL11.glPushMatrix();
		GL11.glRotated(180, 0, 0, 1);
		
		model.render(panel, 0f, 0f, -0.1f, 0f, 0f, 0.0625f);
		
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		GL11.glPushMatrix();
		GL11.glRotated(180, 0, 0, 1);
		GL11.glPushMatrix();
		GL11.glRotated(180, 0, 1, 0);
		model.render((TEControlPanel)null, 0f, 0f, -0.1f, 0f, 0f, 0.0625f);
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
		return FunctionalDecoration.controlPanelRenderID;
	}
}