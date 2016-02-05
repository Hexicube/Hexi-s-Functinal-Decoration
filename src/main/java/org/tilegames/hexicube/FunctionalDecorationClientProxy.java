package org.tilegames.hexicube;

import org.tilegames.hexicube.blocks.controlpanel.*;
import org.tilegames.hexicube.blocks.keypad.*;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class FunctionalDecorationClientProxy extends FunctionalDecorationProxy
{
	@Override
	public void registerSided()
	{
		FunctionalDecoration.controlPanelRenderID = RenderingRegistry.getNextAvailableRenderId();
		TEControlPanelRenderer renderer = new TEControlPanelRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TEControlPanel.class, renderer);
		RenderingRegistry.registerBlockHandler(renderer);
		
		FunctionalDecoration.keypadRenderID = RenderingRegistry.getNextAvailableRenderId();
		TEKeypadRenderer renderer2 = new TEKeypadRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TEKeypad.class, renderer2);
		RenderingRegistry.registerBlockHandler(renderer2);
	}
}