package model;

import java.awt.Color;
import java.awt.Graphics2D;

public class MyRectangle extends MyShape {

    // Construtor aleatório
    public MyRectangle(int screenWidth, int screenHeight) {
        super(screenWidth, screenHeight);
    }

    // Construtor parametrizado para uso em jogos
    public MyRectangle(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    // Construtor parametrizado completo com cor
    public MyRectangle(int x, int y, int width, int height, Color color) {
        super(x, y, width, height, color);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(this.color);
        g.fillRect(this.x, this.y, this.width, this.height);
    }
}
