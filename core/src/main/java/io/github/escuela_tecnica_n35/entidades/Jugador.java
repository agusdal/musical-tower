package entidades;

public class Jugador extends Entidad {
	
	private int monedas;
	private Inventario inventario;
	private Item objetoActivo;
	
	public Jugador(String nombre, Posicion posicion, int vidaMax, double velocidad, int daño) {
		
        super(nombre, posicion, vidaMax, velocidad, daño);
        this.monedas = 0;
        this.inventario = new Inventario();
        
    }
	
	//----------------------------\/MOVIMIENTO\/-----------------------------
	
	public void moverseDerecha() {
	    getPosicion().moverX(10);
	}
	
	public void moverseIzquierda() {
	    getPosicion().moverX(-10);
	}
	
	public void moverseArriba() {
	    getPosicion().moverY(13);
	}
	
	/*public void moverseAbajo() {
	    getPosicion().moverY(10);
	}*/
	
	//----------------------------/\MOVIMIENTO/\-----------------------------
	
	//----------------------------\/EFECTOS A VIDA\/-----------------------------
	
	public void atacar(Entidad objetivo) {
        objetivo.recibirDaño(getDaño());
    }
	
	public void aumentarVidaMax(Entidad objetivo) {
        objetivo.recibirDaño(getVidaMax());
    }
	
	//----------------------------/\EFECTOS A VIDA/\-----------------------------
	
	//----------------------------\/MONEDAS\/-----------------------------
	
	public int agarrarMonedas(int monedasGanadas) {
		
		return monedas += monedasGanadas;
    }
	
	public int restarMonedas(int monedasPerdidas) {
		
		if(monedas >= monedasPerdidas) {
			monedas -= monedasPerdidas;
			
		}
		
		
		return monedas;
    }
	
	public int getMonedas() {
        return monedas;
    }
	
	//----------------------------/\MONEDAS/\-----------------------------
	
	//----------------------------\/OBJETO ACTIVO\/-----------------------------
	
	public Item getObjetoActivo() {
        return objetoActivo;
    }
	
	//----------------------------/\OBJETO ACTIVO/\-----------------------------
	
	//----------------------------\/INVENTARIO\/-----------------------------
	
	public Inventario getInventario() {
        return inventario;
    }
	
	//----------------------------/\INVENTARIO/\-----------------------------
	
	
	
}