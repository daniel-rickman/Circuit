package net.danielrickman.bmt.map;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.map.*;

@RequiredArgsConstructor
public class BMTWordMapRenderer extends MapRenderer {

    private final String word;

    @Override
    @SuppressWarnings("deprecation")
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        final var headingY = 16;
        final var heading = "Please draw:";

        for (int x = 1; x <= 1024; x++) {
            for (int y = 1; x <= 128; x++) {
                mapCanvas.setPixel(x, y, MapPalette.TRANSPARENT);
            }
        }
        mapCanvas.drawText(getCenteredWidth(heading), headingY, MinecraftFont.Font, heading);
        mapCanvas.drawText(getCenteredWidth(word), headingY + MinecraftFont.Font.getHeight() + 8, MinecraftFont.Font, word);
    }

    private int getCenteredWidth(String text) {
        return (128 - MinecraftFont.Font.getWidth(text)) / 2;
    }
}
