/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class SpawnSpecterBangleParticlesPacket implements ClientPlayNetworking.PlayChannelHandler {
	public static final Identifier ID = Bewitchment.id("spawn_specter_bangle_particles");

	public static void send(PlayerEntity player, Entity entity) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(entity.getId());
		ServerPlayNetworking.send((ServerPlayerEntity) player, ID, buf);
	}

	@Override
	public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		int id = buf.readInt();
		client.execute(() -> {
			ClientWorld world = client.world;
			if (world != null) {
				Entity entity = world.getEntityById(id);
				if (entity != null) {
					world.addParticle(ParticleTypes.SMOKE, entity.getParticleX(1), entity.getY(), entity.getParticleZ(1), 0, 0, 0);
				}
			}
		});
	}
}
