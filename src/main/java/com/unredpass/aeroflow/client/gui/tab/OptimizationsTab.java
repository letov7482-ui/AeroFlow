package com.unredpass.aeroflow.client.gui.tab;

import com.unredpass.aeroflow.client.gui.AeroFlowScreen;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class OptimizationsTab implements AeroFlowScreen.TabContent {
    private final List<OptimizationEntry> entries = new ArrayList<>();
    private int scrollOffset = 0;
    private int panelX, panelY, panelWidth, panelHeight;

    private record OptimizationEntry(String name, String tooltip, boolean defaultValue) {
    }

    public OptimizationsTab() {
        // 7 примеров — остальные добавьте по той же схеме
        entries.add(new OptimizationEntry("Умное отсечение сущностей",
                "Не рендерит сущности за стенами и вне поля зрения.\nЭффект: прирост FPS на базах и в бою.\nИгроки, боссы и снаряды не затрагиваются.", true));
        entries.add(new OptimizationEntry("LOD для дальних сущностей",
                "Далёкие мобы обновляются реже (каждый 2-й или 3-й тик).\nЭффект: снижение нагрузки на CPU.\nБез визуальной разницы на расстоянии.", false));
        entries.add(new OptimizationEntry("Отсечение табличек",
                "Скрывает имена мобов и игроков за пределами радиуса.\nЭффект: убирает лишние draw calls.\nПомогает в местах скопления мобов.", true));
        entries.add(new OptimizationEntry("Бюджет и LOD частиц",
                "Жёсткий лимит частиц за тик + снижение плотности вдалеке.\nЭффект: устранение просадок в бою и при эффектах.\n100% / 50% / 0% по дистанции.", true));
        entries.add(new OptimizationEntry("Троттлинг тиков по дистанции",
                "Далёкие сущности тикают реже (каждый 2-й / 4-й тик).\nЭффект: снижение нагрузки на главный поток.\nИгроки, боссы и снаряды не затрагиваются.", false));
        entries.add(new OptimizationEntry("TCP_NODELAY",
                "Отключает алгоритм Нейгла: пакеты уходят мгновенно.\nЭффект: снижение задержки в мультиплеере до ~40 мс.\nСодержимое пакетов не меняется.", true));
        entries.add(new OptimizationEntry("Дедупликация пакетов",
                "Отбрасывает дубликаты частиц/звуков в коротком окне.\nЭффект: экономия трафика, меньше нагрузка на Netty.\nБезопасно для античитов.", true));
    }

    @Override
    public void init(AeroFlowScreen screen) {
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta,
                       int panelX, int panelY, int panelWidth, int panelHeight) {
        this.panelX = panelX;
        this.panelY = panelY;
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;

        int entryHeight = 28;
        int startY = panelY + 10;
        int visibleCount = (panelHeight - 20) / entryHeight;

        for (int i = scrollOffset; i < Math.min(entries.size(), scrollOffset + visibleCount); i++) {
            OptimizationEntry entry = entries.get(i);
            int y = startY + (i - scrollOffset) * entryHeight;

            boolean hovered = mouseX >= panelX + 10 && mouseX < panelX + panelWidth - 10
                    && mouseY >= y && mouseY < y + entryHeight - 2;

            int bg = hovered ? 0xFF2A2A4A : 0xFF151525;
            context.fill(panelX + 10, y, panelX + panelWidth - 10, y + entryHeight - 2, bg);

            context.drawTextWithShadow(context.getMatrices() != null ? null : null, // placeholder
                    null, 0, 0, 0, false);

            // Текст названия
            context.drawTextWithShadow(
                    net.minecraft.client.MinecraftClient.getInstance().textRenderer,
                    entry.name, panelX + 18, y + 8, 0xFFFFFF);

            // Индикатор тумблера (справа)
            int toggleX = panelX + panelWidth - 50;
            int toggleY = y + 6;
            int toggleColor = entry.defaultValue ? 0xFF44AA44 : 0xFF444444;
            context.fill(toggleX, toggleY, toggleX + 32, toggleY + 14, toggleColor);
            context.drawBorder(toggleX, toggleY, 32, 14, 0xFF666666);

            // Иконка (i) для подсказки
            context.drawTextWithShadow(
                    net.minecraft.client.MinecraftClient.getInstance().textRenderer,
                    "ⓘ", panelX + panelWidth - 70, y + 7, 0xFF8888FF);
        }
    }

    @Override
    public String getTooltipAt(int mouseX, int mouseY) {
        int entryHeight = 28;
        int startY = panelY + 10;
        int visibleCount = (panelHeight - 20) / entryHeight;

        for (int i = scrollOffset; i < Math.min(entries.size(), scrollOffset + visibleCount); i++) {
            int y = startY + (i - scrollOffset) * entryHeight;
            if (mouseX >= panelX + 10 && mouseX < panelX + panelWidth - 10
                    && mouseY >= y && mouseY < y + entryHeight - 2) {
                return entries.get(i).tooltip;
            }
        }
        return null;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Пока без переключения — логика будет в следующем шаге
        return false;
    }
}
