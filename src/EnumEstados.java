
public enum EnumEstados {
	LER_MENSAGEM(0),
	ESPERAR_MENSAGEM(1),
	FRENTE(2),
	TRAS(3),
	CURVA_ESQUERDA(4),
	CURVA_DIREITA(5),
	PARAR(6),
	INICIAR_SEQUENCIA(7), 
	TERMINAR_SEQUENCIA(8);

	
	private final int estado;

    private EnumEstados(int estado) {
        this.estado = estado;
    }

	public int getEstado() {
		return estado;
	}
	
	public static EnumEstados getEstadoPorTipo(int tipo) {
		for(EnumEstados e : EnumEstados.values()) {
			if(e.getEstado() == tipo)
				return e;
		}
		return null;
	}
}
