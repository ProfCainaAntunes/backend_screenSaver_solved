package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import model.*;

public class ScreenSaver extends JFrame {

    private static final int CANVAS_WIDTH = 800;
    private static final int CANVAS_HEIGHT = 600;
    private static final int DELAY_MS = 16;

    private ScreenSaverPanel canvas;

    public ScreenSaver() {
        super("Protetor de Tela - Demonstração de POO");

        this.canvas = new ScreenSaverPanel();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Configura tela cheia sem bordas (Exclusive / Borderless Fullscreen)
        this.setUndecorated(true);

        // Preenche as dimensões completas do monitor
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(0, 0, screenSize.width, screenSize.height);
        this.setContentPane(canvas);
    }

    public void start() {
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        this.canvas.startAnimation();

        // Transfere o foco do teclado para o canvas assim que a janela abre
        this.canvas.requestFocusInWindow();
    }

    private class ScreenSaverPanel extends JPanel implements ActionListener {

        private Timer timer;

        /*********************************
         ** ↓ Declare suas formas aqui ↓ **
         *********************************/
        // Programar voltado para interfaces (List em vez de ArrayList)
        private List<MyShape> shapes = new ArrayList<>();
        private MyCharacter character;

        // Flags para rastrear teclas pressionadas
        private boolean up = false;
        private boolean down = false;
        private boolean right = false;
        private boolean left = false;

        public ScreenSaverPanel() {
            // this.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
            this.setBackground(Color.BLACK);

            // Habilita o JPanel a receber foco para capturar eventos de teclado
            this.setFocusable(true);

            // Mapeamento de teclas (Setas e WASD)
            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    // Tecla ESC encerra o protetor de tela
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        System.exit(0);
                    }

                    if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)
                        up = true;
                    if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)
                        down = true;
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
                        right = true;
                    if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
                        left = true;
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)
                        up = false;
                    if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)
                        down = false;
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
                        right = false;
                    if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
                        left = false;
                }
            });

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            /***********************************
             ** ↓ Instancie suas formas aqui ↓ **
             ***********************************/

            // Adiciona 10 formas geométricas de cada tipo à lista de formas
            for (int i = 0; i < 10; i++) {
                shapes.add(new MySquare(screenSize.width, screenSize.height));
                shapes.add(new MyRectangle(screenSize.width, screenSize.height));
                shapes.add(new MyEllipse(screenSize.width, screenSize.height));
                shapes.add(new MyCircle(screenSize.width, screenSize.height));
            }

            // Cria o personagem com imagem e som
            character = new MyCharacter("./image/logo_unisenai.png", 50, 50, 284, 100, "./sound/error.wav");

            // Inicia o timer para a animação, não remover
            this.timer = new Timer(DELAY_MS, this);
        }

        @Override
        public Dimension getPreferredSize() {
            return Toolkit.getDefaultToolkit().getScreenSize();
        }

        @Override
        public Dimension getMinimumSize() {
            return new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT);
        }

        public void startAnimation() {
            this.timer.start();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            /******************************
             ** ↓ Mova suas formas aqui ↓ **
             ******************************/
            int screenWidth = this.getWidth();
            int screenHeight = this.getHeight();

            // Percorre a lista de todas as formas
            for (int i = shapes.size() - 1; i >= 0; i--) {
                MyShape shape = shapes.get(i);

                // Movimenta a forma
                shape.move(screenWidth, screenHeight);

                // Verifica colisão com o personagem
                if (character != null && shape.intersects(character)) {
                    // Toca um som ao colidir com o personagem
                    character.playSound();
                    // Remove a forma que tenha colidido com o personagem
                    shapes.remove(i);
                }
            }

            // Movimenta o personagem em função das teclas pressionadas
            if (character != null) {
                if (up)
                    character.move(5, "UP", screenWidth, screenHeight);
                if (down)
                    character.move(5, "DOWN", screenWidth, screenHeight);
                if (right)
                    character.move(5, "RIGHT", screenWidth, screenHeight);
                if (left)
                    character.move(5, "LEFT", screenWidth, screenHeight);
            }

            // Redesenha a tela
            this.repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            /*********************************
             ** ↓ Desenhe suas formas aqui ↓ **
             *********************************/
            for (MyShape shape : shapes) {
                shape.draw(g2d);
            }

            if (character != null) {
                character.draw(g2d);
            }
        }
    }
}