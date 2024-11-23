package rare.peepo.network;

import dev.architectury.networking.NetworkChannel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import rare.peepo.BetterUX;

public final class Network {
    final static NetworkChannel channel = NetworkChannel.create(Identifier.of(BetterUX.ID, "netchan"));
    
    public static void init() {
        // Register our packets.
        channel.register(SortPacket.class, SortPacket::encode,
                SortPacket::new, SortPacket::handle);
        channel.register(TransferPacket.class, TransferPacket::encode,
                TransferPacket::new, TransferPacket::handle);
    }
    
    @Environment(EnvType.CLIENT)
    public static <T> void sendToServer(T packet) {
        channel.sendToServer(packet);
    }
    
    public static <T> void sendToPlayer(ServerPlayerEntity player, T packet) {
        channel.sendToPlayer(player, packet);
    }
}
