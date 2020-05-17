package net.danielrickman.api.npc;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import lombok.RequiredArgsConstructor;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.trait.MobType;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Optional;

@RequiredArgsConstructor
public class NPCBuilder {

    private final EntityType type;
    private String displayName = "NPC";
    private String uuid, texture, signature;

    public static NPCBuilder of(EntityType type) {
        return new NPCBuilder(type);
    }

    public static NPCBuilder ofPlayer(Player p, String displayName) {
        return NPCBuilder.of(EntityType.PLAYER).withName(displayName).withSkin(p);
    }

    public NPCBuilder withName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public NPCBuilder withSkin(String uuid, String texture, String signature) {
        this.uuid = uuid;
        this.texture = texture;
        this.signature = signature;
        return this;
    }

    public NPCBuilder withSkin(Player player) {
        PlayerProfile profile = player.getPlayerProfile();
        Optional<ProfileProperty> property = profile.getProperties().stream().filter(p -> p.getName().equalsIgnoreCase("textures")).findFirst();
        if (property.isPresent()) {
            this.texture = property.get().getValue();
            this.signature = property.get().getSignature();
        }
        this.uuid = player.getUniqueId().toString();
        return this;
    }

    public void spawnEntity(Location location) {
        var npc = CitizensAPI.getNPCRegistry().createNPC(type, displayName);
        if (npc.getTrait(MobType.class).getType() == EntityType.PLAYER && uuid != null && signature != null && texture != null) {
            npc.getTrait(SkinTrait.class).clearTexture();
            npc.getTrait(SkinTrait.class).setSkinPersistent(uuid, signature, texture);
        }
        npc.spawn(location);
    }
}