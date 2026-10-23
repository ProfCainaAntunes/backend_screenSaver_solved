package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Representa um personagem controlado pelo jogador ou entidade animada baseada em imagem (sprite).
 * Herda as propriedades espaciais e o contrato de desenho da classe MyShape.
 */
public class MyCharacter extends MyShape {

    private Image sprite;
    private Clip collisionClip;

    /**
     * Construtor com redimensionamento personalizado da imagem.
     */
    public MyCharacter(String imagePath, int x, int y, int width, int height, String soundPath) {
        super(x, y, width, height);

        loadImage(imagePath);
        if (this.sprite != null) {
            this.sprite = resizeImage(this.sprite, width, height);
        }

        loadSound(soundPath);
    }

    /**
     * Sobrecarga do construtor para manter as dimensões originais da imagem (sem redimensionar).
     * A largura (width) e altura (height) são obtidas automaticamente do próprio arquivo de imagem.
     */
    public MyCharacter(String imagePath, int x, int y, String soundPath) {
        super(x, y, 0, 0);

        loadImage(imagePath);
        if (this.sprite != null) {
            this.width = this.sprite.getWidth(null);
            this.height = this.sprite.getHeight(null);
        }

        loadSound(soundPath);
    }

    /**
     * Normaliza caminhos de arquivos para busca como recurso do Classpath.
     */
    private String normalizeResourcePath(String path) {
        if (path == null) return "";
        String p = path.replaceFirst("^\\./", ""); // remove './' inicial
        p = p.replaceFirst("^src/", "");          // remove 'src/' se houver
        if (!p.startsWith("/")) {
            p = "/" + p;
        }
        return p;
    }

    /**
     * Carrega a imagem do personagem suportando Classpath e sistema de arquivos local.
     */
    private void loadImage(String imagePath) {
        try {
            // 1. Tenta carregar como recurso do Classpath (ideal para projetos Java)
            String resourcePath = normalizeResourcePath(imagePath);
            URL url = getClass().getResource(resourcePath);
            if (url != null) {
                this.sprite = ImageIO.read(url);
                return;
            }

            // 2. Tenta como arquivo no disco
            File file = new File(imagePath);
            if (!file.exists()) {
                file = new File("src/" + imagePath.replaceFirst("^\\./", ""));
            }

            if (file.exists()) {
                this.sprite = ImageIO.read(file);
            } else {
                System.err.println("Imagem não encontrada: " + imagePath);
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar a imagem: " + e.getMessage());
        }
    }

    /**
     * Redimensiona a imagem com base no width e height fornecidos.
     */
    private Image resizeImage(Image originalImage, int targetWidth, int targetHeight) {
        if (originalImage == null) return null;

        BufferedImage resized = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();

        // Interpolação bilinear para manter nitidez no redimensionamento
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();

        return resized;
    }

    /**
     * Carrega e pré-aloca o som de colisão uma única vez na memória.
     */
    private void loadSound(String soundPath) {
        try {
            AudioInputStream audioStream = null;

            // 1. Tenta carregar como recurso do Classpath
            String resourcePath = normalizeResourcePath(soundPath);
            URL url = getClass().getResource(resourcePath);
            if (url != null) {
                audioStream = AudioSystem.getAudioInputStream(url);
            } else {
                // 2. Tenta como arquivo no disco
                File file = new File(soundPath);
                if (!file.exists()) {
                    file = new File("src/" + soundPath.replaceFirst("^\\./", ""));
                }
                if (file.exists()) {
                    audioStream = AudioSystem.getAudioInputStream(file);
                } else {
                    System.err.println("Arquivo de som não encontrado: " + soundPath);
                    return;
                }
            }

            this.collisionClip = AudioSystem.getClip();
            this.collisionClip.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Erro ao carregar o som: " + e.getMessage());
        }
    }

    /**
     * Executa o som de colisão de forma suave, sem reiniciar a cada frame se já estiver tocando.
     */
    public void playSound() {
        if (collisionClip != null) {
            if (!collisionClip.isRunning()) {
                collisionClip.setFramePosition(0);
                collisionClip.start();
            }
        }
    }

    /**
     * Movimenta o personagem, trava nas bordas da tela e toca o som ao colidir.
     *
     * @param speed        Velocidade do deslocamento em pixels.
     * @param direction    Direção do movimento ("UP", "DOWN", "LEFT", "RIGHT").
     * @param screenWidth  Largura da tela para limitação de borda.
     * @param screenHeight Altura da tela para limitação de borda.
     */
    public void move(int speed, String direction, int screenWidth, int screenHeight) {
        boolean collided = false;

        switch (direction.toUpperCase()) {
            case "UP":
                this.y -= speed;
                if (this.y < 0) {
                    this.y = 0;
                    collided = true;
                }
                break;

            case "DOWN":
                this.y += speed;
                if (this.y + this.height > screenHeight) {
                    this.y = screenHeight - this.height;
                    collided = true;
                }
                break;

            case "LEFT":
                this.x -= speed;
                if (this.x < 0) {
                    this.x = 0;
                    collided = true;
                }
                break;

            case "RIGHT":
                this.x += speed;
                if (this.x + this.width > screenWidth) {
                    this.x = screenWidth - this.width;
                    collided = true;
                }
                break;

            default:
                System.out.println("Direção desconhecida: " + direction);
                return;
        }

        if (collided) {
            playSound();
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (this.sprite != null && g2d != null) {
            g2d.drawImage(this.sprite, this.x, this.y, null);
        } else if (g2d != null) {
            // Fallback visual didático caso a imagem não seja encontrada
            g2d.setColor(Color.MAGENTA);
            g2d.fillRect(this.x, this.y, this.width, this.height);
            g2d.setColor(Color.WHITE);
            g2d.drawString("Personagem", this.x + 10, this.y + (this.height / 2));
        }
    }

    public Image getSprite() {
        return sprite;
    }
}