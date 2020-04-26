package net.danielrickman.bukkit.task;

import net.danielrickman.api.npc.NPCBuilder;
import net.danielrickman.api.player.CorePlayer;
import net.danielrickman.bukkit.Circuit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class NPCCreationTask implements Runnable {

    private final static String COMING_SOON_UUID = "9d66f642-4860-4705-94d3-6d566502e828";
    private final static String COMING_SOON_TEXTURE = "eyJ0aW1lc3RhbXAiOjE1NTc5MTUwMjI1OTgsInByb2ZpbGVJZCI6ImNiZGViZGRjODNhNTQ0OWFiZDFiOThhNzBjY2E0ZDhlIiwicHJvZmlsZU5hbWUiOiJDaGVja2lkb2lzIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81ZDhhMWM5YWY2MGM3MmY3ZDdlZDM4ZTdiNWFlZGI1MzIwZDZlMjZkZDNlZDcxYTRlNTFlMDFiNWE0YTdjYTI1In19fQ==";
    private final static String COMING_SOON_SIGNATURE = "qqNv6fePAMo9dK0l0wPnR5tvlO/bhT6zVmQpc5pFy+/79ZTNTNyuJyGTkCRu1qmEquRijvQ/VYwX7s8X3f3d68rvYxVAK4b1KNavHeyePDe+EQ5jmmjegSiZqhHxK/Mkpbo3MzWibQtJN2qvnlnNoUyxbJAmc8qtDn48ab55Q6UqenTYFfeWWbCkOkODzzkrDW16X5wuQmTHQWpfcvupgW+Hgq0mZo1f5GH+8Fcf8QHw0Ujlc0HWQXBjO/s2iIrMlamZvTzF8vg7ZBh0tq5QohcRbKPmT7cKTeOXvFLut8nY6LrgpxjQ/f61n9FrKYsKv8G5ujWOTYK+zm/M9JcPt3seGR+RC5fuFdfkWXNFilO0vFjGWE3XN7M6RGOqxhKTkfgBgWGXUbZExXd5Ogoc/fPbmMwGcQstrdUfQtkYaF5fWRixjQ2aAIlUIUwia+5D4lm3/L8gUgSkbK0d/m/21bJGe7+LC604q2C97WVb1wLUDqhR0HWglW83LMwSBAZXEuMNAKF6p7bWp2mOfX8y0zJsdE5VRataoWk58OAUGUNqhfLyw9Ki/oNtJ1rHzmnTo209p+z5bziQvTBh0rwa1RwZ00UcuKmyUy62uviTlT5iCJw8gMxiiBJ9ra/muwpdutV96DLOk0bFz+jx40UjNXEPHljhZOqQrFMiWbFtknU=";

    private final Circuit circuit;

    public NPCCreationTask(Circuit circuit) {
        this.circuit = circuit;
    }

    @Override
    public void run() {
        var configuration = circuit.getLobby().getConfiguration();
        configuration.getNpcs().forEach(npc -> circuit.getGameByID(npc.getId()).ifPresentOrElse(
                //If game exists
                (game) -> new NPCBuilder()
                        .displayName(game.getDisplayName())
                        .ofType(EntityType.PLAYER)
                        .withSkin(npc.getSkinUuid(), npc.getSkinData(), npc.getSkinSignature())
                        .spawnEntity(npc.getMapLocation().toWorldLocation(circuit.getLobby().getWorld())),

                //If game does not exist
                () -> new NPCBuilder()
                        .displayName(ChatColor.GRAY.toString() + ChatColor.BOLD + "Coming Soon!")
                        .ofType(EntityType.PLAYER)
                        .withSkin(COMING_SOON_UUID, COMING_SOON_TEXTURE, COMING_SOON_SIGNATURE)
                        .spawnEntity(npc.getMapLocation().toWorldLocation(circuit.getLobby().getWorld()))
                ));

        var builder = new NPCBuilder().ofType(EntityType.PLAYER);
        if (getTopCoinsPlayer() == null) {
            builder = builder
                    .displayName(ChatColor.RED.toString() + ChatColor.BOLD + "Nobody!");
        } else {
            builder = builder
                    .displayName(getTopCoinsPlayer().getPlayer().getPlayerListName()).ofType(EntityType.PLAYER)
                    .withSkin(getTopCoinsPlayer().getPlayer());
        }
        builder.spawnEntity(configuration.getTopPlayerNpc().getMapLocation().toWorldLocation(circuit.getLobby().getWorld()));
    }

    private CorePlayer getTopCoinsPlayer() {
        List<CorePlayer> all = new ArrayList<>(circuit.getPlayerRepository().getPlayers());
        if (all.size() <= 1) {
            return null;
        }
        all.sort((cp1, cp2) -> Integer.compare(cp2.getCoins(), cp1.getCoins()));
        if (all.get(0).getCoins() == all.get(1).getCoins()) { //If it's a tie for first, there is no top player
            return null;
        } else { //Otherwise return the top player
            return all.get(0);
        }
    }
}
