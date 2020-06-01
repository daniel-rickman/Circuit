package net.danielrickman.bukkit.task;

import net.danielrickman.api.npc.NPCBuilder;
import net.danielrickman.api.rank.Rank;
import net.danielrickman.api.repository.GlobalRepository;
import net.danielrickman.bukkit.Circuit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.EntityType;

public class NPCCreationTask implements Runnable {

    private final static String UNKNOWN_UUID = "9d66f642-4860-4705-94d3-6d566502e828";
    private final static String UNKNOWN_TEXTURE = "eyJ0aW1lc3RhbXAiOjE1NTc5MTUwMjI1OTgsInByb2ZpbGVJZCI6ImNiZGViZGRjODNhNTQ0OWFiZDFiOThhNzBjY2E0ZDhlIiwicHJvZmlsZU5hbWUiOiJDaGVja2lkb2lzIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81ZDhhMWM5YWY2MGM3MmY3ZDdlZDM4ZTdiNWFlZGI1MzIwZDZlMjZkZDNlZDcxYTRlNTFlMDFiNWE0YTdjYTI1In19fQ==";
    private final static String UNKNOWN_SIGNATURE = "qqNv6fePAMo9dK0l0wPnR5tvlO/bhT6zVmQpc5pFy+/79ZTNTNyuJyGTkCRu1qmEquRijvQ/VYwX7s8X3f3d68rvYxVAK4b1KNavHeyePDe+EQ5jmmjegSiZqhHxK/Mkpbo3MzWibQtJN2qvnlnNoUyxbJAmc8qtDn48ab55Q6UqenTYFfeWWbCkOkODzzkrDW16X5wuQmTHQWpfcvupgW+Hgq0mZo1f5GH+8Fcf8QHw0Ujlc0HWQXBjO/s2iIrMlamZvTzF8vg7ZBh0tq5QohcRbKPmT7cKTeOXvFLut8nY6LrgpxjQ/f61n9FrKYsKv8G5ujWOTYK+zm/M9JcPt3seGR+RC5fuFdfkWXNFilO0vFjGWE3XN7M6RGOqxhKTkfgBgWGXUbZExXd5Ogoc/fPbmMwGcQstrdUfQtkYaF5fWRixjQ2aAIlUIUwia+5D4lm3/L8gUgSkbK0d/m/21bJGe7+LC604q2C97WVb1wLUDqhR0HWglW83LMwSBAZXEuMNAKF6p7bWp2mOfX8y0zJsdE5VRataoWk58OAUGUNqhfLyw9Ki/oNtJ1rHzmnTo209p+z5bziQvTBh0rwa1RwZ00UcuKmyUy62uviTlT5iCJw8gMxiiBJ9ra/muwpdutV96DLOk0bFz+jx40UjNXEPHljhZOqQrFMiWbFtknU=";

    private final static String START_NPC_ID = "START_NPC";
    private final static String COINS_NPC_ID = "COINS_NPC";

    private final Circuit circuit;
    private final GlobalRepository global;

    public NPCCreationTask(Circuit circuit, GlobalRepository global) {
        this.circuit = circuit;
        this.global = global;
    }

    @Override
    public void run() {
        var configuration = circuit.getLobbyMap().getConfiguration();

        configuration.getNpcs().forEach(npc -> {
            final var npcID = npc.getId();
            final var location = npc.getMapLocation().toWorldLocation(circuit.getLobbyMap().getWorld());
            circuit.getGameByID(npcID).ifPresentOrElse(
                    game -> NPCBuilder
                            .of(npc.getType())
                            .withName(game.getIdentifier(), true)
                            .withSkin(configuration.getTextureByID(game.getIdentifier()))
                            .spawnEntity(location),
                    () -> {
                        if (npcID.equalsIgnoreCase(START_NPC_ID)) {
                            NPCBuilder
                                    .of(npc.getType())
                                    .withName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Start Game", false)
                                    .withColour(DyeColor.RED)
                                    .spawnEntity(location);
                        } else if (npcID.equalsIgnoreCase(COINS_NPC_ID)) {
                            var topPlayer = Bukkit.getPlayer(global.getTopCoinsPlayer());
                            NPCBuilder builder;
                            if (topPlayer == null) {
                                builder = NPCBuilder
                                        .of(EntityType.PLAYER)
                                        .withName(ChatColor.RED.toString() + ChatColor.BOLD + "Nobody yet!", false)
                                        .withSkin(UNKNOWN_UUID, UNKNOWN_TEXTURE, UNKNOWN_SIGNATURE);
                            } else {
                                builder = NPCBuilder.ofPlayer(topPlayer, Rank.getFormattedName(topPlayer), false);
                            }
                            builder.spawnEntity(location);
                        }
                    });
        });
    }
}