package model;

import java.awt.Color;

public class MyCircle extends MyEllipse {

    // Construtor aleatório
    public MyCircle(int screenWidth, int screenHeight) {
        super(screenWidth, screenHeight);
        int diameter = Math.min(this.width, this.height);
        this.width = diameter;
        this.height = diameter;
    }

    // Construtor parametrizado para uso em jogos
    public MyCircle(int x, int y, int diameter) {
        super(x, y, diameter, diameter);
    }

    // Construtor parametrizado completo com cor
    public MyCircle(int x, int y, int diameter, Color color) {
        super(x, y, diameter, diameter, color);
    }

    // Garantir integridade do círculo ao alterar dimensões (LSP)
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        super.setHeight(width);
    }

    @Override
    public void setHeight(int height) {
        super.setWidth(height);
        super.setHeight(height);
    }
}
