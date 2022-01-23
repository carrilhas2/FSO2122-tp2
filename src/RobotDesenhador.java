import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JScrollBar;

public class RobotDesenhador extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JTextField txtFldEstadoCanal;
	private JPanel panel;
	private JTextArea textArea; 
	private JScrollBar scrollBar;
	private JLabel lblEstadoCanal;
	//private RobotLegoEV3 robot;
	private String nomeRobot;
	
	public void startRobot() {
		//robot.OpenEV3(getNomeRobot());
		txtFldEstadoCanal.setText("INICIAR, Nome do Robot: " + nomeRobot + "\n" +txtFldEstadoCanal.getText());
		System.out.println("Nome do Robot: " + nomeRobot);
	}
	
	public void closeRobot() {
		//robot.CloseEV3();
		txtFldEstadoCanal.setText("Ligacao Terminada \n" +txtFldEstadoCanal.getText());

		System.out.println("Ligacao Terminada");
	}
	
	public void reta(int distancia) {
		//robot.Reta(distancia);
		//robot.Parar(false);
		txtFldEstadoCanal.setText("Reta com distancia: " + distancia + "\n" +txtFldEstadoCanal.getText());
	}
	
	public void curvarEsquerda(int raio, int angulo) {
		//robot.CurvarEsquerda(raio, angulo);
		//robot.Parar(false);
		txtFldEstadoCanal.setText("Curva a Esquerda com raio de " + raio + " cm e angulo de " + angulo + "º. \n" +txtFldEstadoCanal.getText());
	}
	
	public void curvarDireita(int raio, int angulo) {
		//robot.CurvarDireita(raio, angulo);
		//robot.Parar(false);
		txtFldEstadoCanal.setText("Curva a Direita com raio de " + raio + " cm e angulo de " + angulo + "º.\n" +txtFldEstadoCanal.getText());
	}
	
	public void parar() {
		//robot.Parar(true);
		txtFldEstadoCanal.setText("Parar Robot. \n" +txtFldEstadoCanal.getText());
	}

	public String getNomeRobot() {
		return nomeRobot;
	}

	public void setNomeRobot(String nomeRobot) {
		this.nomeRobot = nomeRobot;
	}
	
	public RobotDesenhador() {
		
		panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		textArea = new JTextArea();
		textArea.setBounds(6, 161, 588, 205);
		panel.add(textArea);
		
		scrollBar = new JScrollBar();
		scrollBar.setBounds(579, 161, 15, 205);
		panel.add(scrollBar);
		
		txtFldEstadoCanal = new JTextField();
		txtFldEstadoCanal.setBounds(438, 19, 130, 26);
		panel.add(txtFldEstadoCanal);
		txtFldEstadoCanal.setColumns(10);
		
		lblEstadoCanal = new JLabel("Estado do Canal:");
		lblEstadoCanal.setBounds(297, 24, 130, 16);
		panel.add(lblEstadoCanal);
	}

}
