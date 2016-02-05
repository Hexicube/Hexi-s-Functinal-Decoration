package org.tilegames.hexicube;

import java.io.ByteArrayInputStream;

import org.tilegames.hexicube.blocks.TEButtonPanel;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;

@Sharable
public class FunctionalDecorationPacketHandler extends SimpleChannelInboundHandler<FMLProxyPacket>
{
	@Override
    protected void channelRead0(ChannelHandlerContext ctx, FMLProxyPacket packet) throws Exception
	{
        if(packet.channel().equals("HexiFunctionalDecoration"))
        {
        	NBTTagCompound c = CompressedStreamTools.readCompressed(new ByteArrayInputStream(packet.payload().array()));
        	int world = c.getInteger("world");
        	WorldProvider provider = DimensionManager.getProvider(world);
        	if(provider != null)
        	{
	        	int x = c.getInteger("x");
	        	int y = c.getInteger("y");
	        	int z = c.getInteger("z");
	        	TileEntity te = provider.worldObj.getTileEntity(x, y, z);
	        	if(te instanceof TEButtonPanel)
	        	{
	        		NBTTagCompound tag = null;
	        		if(c.hasKey("extradata")) tag = c.getCompoundTag("extradata");
	        		((TEButtonPanel)te).handleButton(c.getInteger("button"), c.getBoolean("rightmouse"), tag);
	        	}
        	}
        }
	}
}