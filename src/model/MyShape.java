package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

/**
 * Classe abstrata que serve como base para todas as formas geométricas
 * e elementos visuais do protetor de tela e do jogo.
 */
public abstract class MyShape {

    // Gerador de números aleatórios compartilhado
    protected static final Random random = new Random();

    // Atributos de posição, tamanho, velocidade e cor (Encapsulamento protegido
    // para subclasses)
    protected int x;
    protected int y;
    protected int speedX;
    protected int speedY;
    protected int width;
    protected int height;
    protected Color color;

    /**
     * Construtor que inicializa a forma com posições, dimensões e velocidades
     * aleatórias.
     *
     * @param screenWidth  Largura da tela/canvas.
     * @param screenHeight Altura da tela/canvas.
     */
    public MyShape(int screenWidth, int screenHeight) {
        // Dimensões aleatórias entre 50 e 100 pixels
        this.width = random.nextInt(51) + 50;
        this.height = random.nextInt(51) + 50;

        // Posição inicial aleatória dentro da tela
        int maxX = Math.max(1, screenWidth - this.width);
        int maxY = Math.max(1, screenHeight - this.height);
        this.x = random.nextInt(maxX);
        this.y = random.nextInt(maxY);

        // Velocidades aleatórias entre 1 e 5 em direções aleatórias
        this.speedX = random.nextInt(5) + 1;
        if (random.nextBoolean()) {
            this.speedX *= -1;
        }

        this.speedY = random.nextInt(5) + 1;
        if (random.nextBoolean()) {
            this.speedY *= -1;
        }

        this.color = generateRandomColor();
    }

    /**
     * Construtor parametrizado para definir posição e dimensões explicitamente.
     */
    public MyShape(int x, int y, int width, int height) {
        this(x, y, width, height, null);
    }

    /**
     * Construtor parametrizado completo com cor.
     */
    public MyShape(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speedX = 0;
        this.speedY = 0;
        this.color = (color != null) ? color : generateRandomColor();
    }

    // Métodos Abstratos (Polimorfismo: cada subclasse define sua própria forma de
    // desenho)
    public abstract void draw(Graphics2D g);

    /**
     * Movimenta a forma na tela e rebate nas bordas invertendo a velocidade.
     *
     * @param screenWidth  Largura atual da tela.
     * @param screenHeight Altura atual da tela.
     */
    public void move(int screenWidth, int screenHeight) {
        if (this.x < 0 || this.x + this.width > screenWidth) {
            this.speedX *= -1;
            this.color = generateRandomColor();
        }

        if (this.y < 0 || this.y + this.height > screenHeight) {
            this.speedY *= -1;
            this.color = generateRandomColor();
        }

        this.x += this.speedX;
        this.y += this.speedY;
    }

    /**
     * Gera uma cor aleatória em formato RGB.
     */
    protected Color generateRandomColor() {
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    /**
     * Verifica colisão retangular (Bounding Box) com outra forma.
     * Recurso essencial de POO para o desenvolvimento de jogos.
     */
    public boolean intersects(MyShape other) {
        if (other == null)
            return false;
        return this.x < other.x + other.width &&
                this.x + this.width > other.x &&
                this.y < other.y + other.height &&
                this.y + this.height > other.y;
    }

    // Getters e Setters (Encapsulamento)
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
