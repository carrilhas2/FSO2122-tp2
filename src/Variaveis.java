
public class Variaveis {

	private String nomeRobot;
	private int distancia, raio, angulo;
	
	public Variaveis() {
		nomeRobot = "Robot";
		distancia = 30;
		raio = 15;
		angulo = 90;
	}
	
	public int getDistancia() {
		return distancia;
	}
	public void setDistancia(int distancia) {
		this.distancia = distancia;
	}
	public int getRaio() {
		return raio;
	}
	public void setRaio(int raio) {
		this.raio = raio;
	}
	public int getAngulo() {
		return angulo;
	}
	public void setAngulo(int angulo) {
		this.angulo = angulo;
	}
	
	public String getNomeRobot() {
		return nomeRobot;
	}
	public void setNomeRobot(String nomeRobot) {
		this.nomeRobot = nomeRobot;
	}

}
