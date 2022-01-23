
public class Mensagem implements iMensagem{
	private int id, tipo, angulo, raio, distancia = 0;
	private long idCliente = 0;
	private String texto = "";
	
	public Mensagem(int tipo, String texto) {
		this.tipo = tipo;
		this.texto = texto;
		tratarTexto(texto);
		
	}
	
	public Mensagem(int tipo, long idCliente) {
		this.tipo = tipo;
		this.idCliente = idCliente;
		criarTexto();
	}

	public Mensagem(int tipo, int raio,int angulo, long idCliente) {
		this.tipo = tipo;
		this.raio = raio;
		this.angulo = angulo;
		this.idCliente = idCliente;
		criarTexto();
	}
	
	public Mensagem(int tipo, int distancia, long idCliente) {
		this.tipo = tipo;
		this.distancia = distancia;
		this.idCliente = idCliente;
		criarTexto();
	}

	private void tratarTexto(String texto) {
		String[] p = texto.split("/");
		for (String x : p) {
			char key = x.charAt(0);
			switch (key) {
			case 'i':
				this.idCliente = Long.parseLong(x.split("=")[1]);
				break;
			case 'a':
				this.angulo = Integer.valueOf(x.split("=")[1]);
				break;
			case 'r':
				this.raio = Integer.valueOf(x.split("=")[1]);
				break;
			case 'd':
				this.distancia = Integer.valueOf(x.split("=")[1]);
				break;
			case 'm':
				this.id = Integer.valueOf(x.split("=")[1]);
				break;
			}
		}
	}
	
	private void criarTexto() {
		this.texto = "";
		if(idCliente != 0) {
			this.texto += "i="+idCliente+"/";
		}
		if(angulo != 0) {
			this.texto += "a="+angulo+"/";
			this.texto += "r="+raio+"/";
		}
		if(distancia != 0) {
			this.texto += "d="+distancia+"/";
		}
		if(id != 0) {
			this.texto += "m="+id+"/";
		}
		
	}
	
	@Override
	public String toString() {
		return "Mensagem [id=" + id + ", tipo=" + tipo + ", angulo=" + angulo + ", raio=" + raio + ", distancia="
				+ distancia + ", idCliente=" + idCliente + ", texto=" + texto + "]";
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getAngulo() {
		return angulo;
	}

	public void setAngulo(int angulo) {
		this.angulo = angulo;
	}

	public int getRaio() {
		return raio;
	}

	public void setRaio(int raio) {
		this.raio = raio;
	}

	public int getDistancia() {
		return distancia;
	}

	public void setDistancia(int distancia) {
		this.distancia = distancia;
	}

	public long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(long idCliente) {
		this.idCliente = idCliente;
	}
	
	
}
