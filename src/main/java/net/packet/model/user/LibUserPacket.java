package net.packet.model.user;

import model.user.LibUser;
import net.packet.Packet;
import net.packet.model.ModelPacketType;

/**
 * Packet sent to server to specify LibUser CUD (no READ) operation to be performed
 */
public class LibUserPacket extends Packet {

    private ModelPacketType packetType;
    private LibUser user;

    public LibUserPacket(ModelPacketType packetType, LibUser user) {
        this.packetType = packetType;
        this.user = user;
    }

    public ModelPacketType getPacketType() {
        return packetType;
    }

    public LibUser getUser() {
        return user;
    }
}
