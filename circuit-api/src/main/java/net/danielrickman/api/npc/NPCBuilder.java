package net.danielrickman.api.npc;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import lombok.RequiredArgsConstructor;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.MobType;
import net.citizensnpcs.trait.SkinTrait;
import net.citizensnpcs.trait.WoolColor;
import net.danielrickman.api.map.lobby.SkinTexture;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.material.Colorable;

import java.util.Optional;

@RequiredArgsConstructor
public class NPCBuilder {

    private final EntityType type;
    private String displayName = "NPC";
    private String uuid, texture, signature;
    private boolean hasHiddenName = false;
    private DyeColor color;
    private boolean hasNoAI = false;

    public static NPCBuilder of(EntityType type) {
        return new NPCBuilder(type);
    }

    public static NPCBuilder ofPlayer(Player p, String displayName, boolean hasHiddenName) {
        return NPCBuilder.of(EntityType.PLAYER).withName(displayName, hasHiddenName).withSkin(p);
    }

    public NPCBuilder withName(String displayName, boolean hasHiddenName) {
        this.displayName = displayName;
        this.hasHiddenName = hasHiddenName;
        return this;
    }

    public NPCBuilder withSkin(SkinTexture skinTexture) {
        if (skinTexture != null) {
            this.uuid = skinTexture.getUuid();
            this.texture = skinTexture.getData();
            this.signature = skinTexture.getSignature();
        }
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

    public NPCBuilder withColour(DyeColor color) {
        this.color = color;
        return this;
    }

    public NPCBuilder withNoAI() {
        this.hasNoAI = true;
        return this;
    }

    public void spawnEntity(Location location) {
        var npc = CitizensAPI.getNPCRegistry().createNPC(type, displayName);
        if (npc.getTrait(MobType.class).getType() == EntityType.PLAYER && uuid != null && signature != null && texture != null) {
            npc.getTrait(SkinTrait.class).clearTexture();
            npc.getTrait(SkinTrait.class).setSkinPersistent(uuid, signature, texture);
        }
        if (hasHiddenName) {
            npc.data().setPersistent(NPC.NAMEPLATE_VISIBLE_METADATA, false);
        }
        npc.setUseMinecraftAI(hasNoAI);
        npc.spawn(location);
        if (npc.getEntity() instanceof Colorable) {
            ((Colorable) npc.getEntity()).setColor((color != null) ? color : DyeColor.WHITE);
        }
    }
}