package entidades;

public class Posicion {

    protected int x;
    protected int y;

    public Posicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void moverX(int cantidad) {
        this.x += cantidad;
    }

    public void moverY(int cantidad) {
        this.y += cantidad;
    }
    
}