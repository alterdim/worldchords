package li.gerard.worldchords.gui;

import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.DataBindingBuilder;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Button;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Toggle;
import dev.vfyjxf.taffy.style.AlignContent;
import dev.vfyjxf.taffy.style.AlignItems;
import dev.vfyjxf.taffy.style.FlexDirection;
import dev.vfyjxf.taffy.style.TaffyPosition;
import li.gerard.worldchords.capability.SideConfig;
import net.minecraft.core.Direction;

/**
 * Side configuration overlay
 * Creat with
 * <pre>{@code
 * var sculk = new SideConfigPanel("Sculk Force", be.getSculkForceSides());
 * var energy = new SideConfigPanel("Energy", be.getEnergySides());
 * corner.addChildren(sculk.createOpenButton("S"), energy.createOpenButton("E"));
 * root.addChildren(corner, sculk, energy); // overlays last, so they render on top
 * }</pre>
 */
public class SideConfigPanel extends UIElement {

    public SideConfigPanel(String title, SideConfig config) {
        layout(l -> l.positionType(TaffyPosition.ABSOLUTE).top(0).left(0)
                .widthPercent(100).heightPercent(100)
                .justifyContent(AlignContent.CENTER).alignItems(AlignItems.CENTER).gapAll(4));
        addClass("panel_bg");
        setDisplay(false);

        var grid = new UIElement();
        grid.layout(l -> l.gapAll(2).alignItems(AlignItems.CENTER));
        //   [U]
        // [W][N][E][S]
        //   [D]
        grid.addChildren(
                row(sideToggle(config, Direction.UP)),
                row(sideToggle(config, Direction.WEST), sideToggle(config, Direction.NORTH),
                        sideToggle(config, Direction.EAST), sideToggle(config, Direction.SOUTH)),
                row(sideToggle(config, Direction.DOWN)));

        var close = new Button().setText("Close", false);
        close.setOnClick(e -> setDisplay(false));

        addChildren(new Label().setText(title, false), grid, close);
    }

    /**
     * cute open button
     */
    public Button createOpenButton(String label) {
        var open = new Button().setText(label, false);
        open.layout(l -> l.width(14).height(14));
        open.setOnClick(e -> setDisplay(true));
        return open;
    }

    private static UIElement row(UIElement... toggles) {
        var row = new UIElement();
        row.layout(l -> l.flexDirection(FlexDirection.ROW).gapAll(2).justifyContent(AlignContent.CENTER));
        row.addChildren(toggles);
        return row;
    }

    private static UIElement sideToggle(SideConfig config, Direction side) {
        var toggle = new Toggle().setText(side.getName().substring(0, 1).toUpperCase(), false);
        toggle.layout(l -> l.width(24));
        return toggle.bind(DataBindingBuilder.bool(
                () -> config.isEnabled(side),
                enabled -> config.setEnabled(side, enabled)).build());
    }
}
