package net.danielrickman.api.hologram;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.reflect.accessors.FieldAccessor;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import lombok.Getter;
import lombok.Setter;
import net.danielrickman.api.map.MapLocation;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Hologram {

    private static final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    private static final FieldAccessor ENTITY_ID = Accessors.getFieldAccessor(MinecraftReflection.getEntityClass(), "entityCount", true);

    private final MapLocation mapLocation;
    private final UUID uuid;
    private final Integer entityID;
    private final WrappedDataWatcher watcher;
    @Getter
    @Setter
    private String text;

    public Hologram(String text, MapLocation mapLocation) {
        this.text = text;
        this.mapLocation = mapLocation;
        this.uuid = UUID.randomUUID();
        this.entityID = ((AtomicInteger) ENTITY_ID.get(null)).addAndGet(1);
        this.watcher = new WrappedDataWatcher();
    }

    public Hologram(String text, Location location) {
        this(text, MapLocation.fromWorldLocation(location));
    }

    public void spawnEntity(Player player) {
        sendPacket(player, getEntitySpawnPacket());
        sendPacket(player, getArmorStandMetadataPacket());
    }

    public void destroyEntity(Player player) {
        sendPacket(player, getEntityDestroyPacket());
    }

    private void sendPacket(Player player, PacketContainer packet) {
        try {
            protocolManager.sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private PacketContainer getEntitySpawnPacket() {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
        packet.getIntegers()
                .write(0, entityID) //Entity ID
                .write(1, 1); //Entity Type
        packet.getUUIDs().write(0, uuid); //Entity UUID
        packet.getDoubles()
                .write(0, mapLocation.getX()) //Entity X
                .write(1, mapLocation.getY()) //Entity Y
                .write(2, mapLocation.getZ()); //Entity Z
        return packet;
    }

    private PacketContainer getArmorStandMetadataPacket() {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA); //Have to send a separate packet due to [https://github.com/dmulloy2/ProtocolLib/issues/738]
        packet.getIntegers().write(0, entityID);
        watcher.setObject(0, WrappedDataWatcher.Registry.get(Byte.class), (byte) 32);
        watcher.setObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true), Optional.of(WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', text)).getHandle()));
        watcher.setObject(3, WrappedDataWatcher.Registry.get(Boolean.class), Boolean.valueOf(true));
        watcher.setObject(4, WrappedDataWatcher.Registry.get(Boolean.class), Boolean.valueOf(true));
        watcher.setObject(5, WrappedDataWatcher.Registry.get(Boolean.class), Boolean.valueOf(true));

        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        return packet;
    }

    private PacketContainer getEntityDestroyPacket() {
        PacketContainer destroyPacket = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        destroyPacket.getIntegerArrays().write(0, new int[]{entityID});
        return destroyPacket;
    }
}