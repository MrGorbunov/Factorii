package minirpg;

import java.awt.Color;

import javax.swing.JFrame;
import asciiPanel.AsciiPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class ApplicationMain extends JFrame implements KeyListener {
    private static final long serialVersionUID = 1060623638149583738L;

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
