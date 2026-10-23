package model;

import java.awt.Color;
import java.awt.Graphics2D;

public class MyEllipse extends MyShape {

    // Construtor aleatório
    public MyEllipse(int screenWidth, int screenHeight) {
        super(screenWidth, screenHeight);
    }

    // Construtor parametrizado para uso em jogos
    public MyEllipse(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    // Construtor parametrizado completo com cor
    public MyEllipse(int x, int y, int width, int height, Color color) {
        super(x, y, width, height, color);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(this.color);
        g.fillOval(this.x, this.y, this.width, this.height);
    }
}
