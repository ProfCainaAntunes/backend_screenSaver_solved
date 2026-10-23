package model;

import java.awt.Color;

public class MySquare extends MyRectangle {

    // Construtor aleatório
    public MySquare(int screenWidth, int screenHeight) {
        super(screenWidth, screenHeight);
        int side = Math.min(this.width, this.height);
        this.width = side;
        this.height = side;
    }

    // Construtor parametrizado para uso em jogos
    public MySquare(int x, int y, int size) {
        super(x, y, size, size);
    }

    // Construtor parametrizado completo com cor
    public MySquare(int x, int y, int size, Color color) {
        super(x, y, size, size, color);
    }

    // Garantir integridade do quadrado ao alterar dimensões (LSP)
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
