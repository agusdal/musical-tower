package io.github.escuela_tecnica_n35.entidades;

public class Jugador extends Entidad {
	
	private int monedas;
	private Inventario inventario;
	private Item objetoActivo;
	
	public Jugador(String nombre, Posicion posicion, int vidaMax, float velocidad, int daño) {
		
        super(nombre, posicion, vidaMax, velocidad, daño);
        this.monedas = 0;
        this.inventario = new Inventario();
        
    }
	
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