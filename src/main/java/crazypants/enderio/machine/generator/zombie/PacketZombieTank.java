package crazypants.enderio.machine.generator.zombie;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import crazypants.enderio.network.MessageTileEntity;
import crazypants.enderio.network.NetworkUtil;

public class PacketZombieTank extends MessageTileEntity<TileZombieGenerator> {

  private NBTTagCompound nbtRoot;

  public PacketZombieTank() {
  }

  public PacketZombieTank(TileZombieGenerator tile) {
    super(tile);
    nbtRoot = new NBTTagCompound();
    if(tile.fuelTank.getFluidAmount() > 0) {
      NBTTagCompound tankRoot = new NBTTagCompound();
      tile.fuelTank.writeToNBT(tankRoot);
      nbtRoot.setTag("tank", tankRoot);
    }
  }

  @Override
  public void toBytes(ByteBuf buf) {
    NetworkUtil.writeNBTTagCompound(nbtRoot, buf);
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    nbtRoot = NetworkUtil.readNBTTagCompound(buf);
  }

  
  @Override
  protected void handleClientSide(EntityPlayer player, World worldObj, TileZombieGenerator tile) {
    if(nbtRoot.hasKey("tank")) {
      NBTTagCompound tankRoot = nbtRoot.getCompoundTag("tank");
      tile.fuelTank.readFromNBT(tankRoot);
    } else {
      tile.fuelTank.setFluid(null);
    }
  }
}