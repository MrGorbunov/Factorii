package minirpg;

import java.awt.Color;

import javax.swing.JFrame;
import asciiPanel.AsciiPanel;
import minirpg.inventory.ItemIndex;
import minirpg.screen.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class ApplicationMain extends JFrame implements KeyListener {
    private static final long serialVersionUID = -5955110992780994159L;

    private Screen screen;
    private AsciiPanel terminal;

    public ApplicationMain () {
        super();
        terminal = new AsciiPanel();
        add(terminal);
        pack();

        screen = new StartScreen();
        addKeyListener(this);
        repaint();

        GameState.initGameState(200, 170);
        GameState.makeNewWorld();

        // Temporary
        GameState.inventory.addItem(ItemIndex.CHEST);
        GameState.inventory.addItem(ItemIndex.FUNNEL_COPPER);
        GameState.inventory.addItem(ItemIndex.FUNNEL_WOOD);
        GameState.inventory.addItem(ItemIndex.FUNNEL_WOOD);
        GameState.inventory.addItem(ItemIndex.FUNNEL_IRON);
        GameState.inventory.addItem(ItemIndex.FUNNEL_STEEL);

        GameState.inventory.addItem(ItemIndex.MINING_DRILL);
        GameState.inventory.addItem(ItemIndex.MINING_DRILL);
        GameState.inventory.addItem(ItemIndex.AUTO_MINING_UPGRADE);

        GameState.inventory.addItem(ItemIndex.WORKBENCH);
        GameState.inventory.addItem(ItemIndex.COPPER_WORKBENCH);

        GameState.inventory.addItem(ItemIndex.KILN);
        GameState.inventory.addItem(ItemIndex.FORGE);
    }

    public void repaint () {
        terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
    }

    public void keyPressed (KeyEvent e) {
        screen = screen.handleInput(e);
        repaint();
    }

    public void keyReleased (KeyEvent e) { }
    
    public void keyTyped (KeyEvent e) { }

    public static void main(String[] args) {
        ApplicationMain main = new ApplicationMain();
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setVisible(true);
    }

}
