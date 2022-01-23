import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

public class DesenhaQuadrados extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8601216413492722888L;
	private static final long IDCliente = System.currentTimeMillis()+2L;
	private JPanel contentPane;
	private double zoom = 1;
	private JTextField textFldNQuadrados;
	private JTextField textFldDistancia;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnEsquerda;
	private JRadioButton rdbtnDireita;
	private JTextArea textArea;
	private JButton btnDesenharQuadrado;
	private int numeroInstrucao = 1;
	private VariaveisDesenharQuadrados v;
	private CanalComunicacao canal;

	
	@Override
	public void run() {

	}
	
	private void inicializarVariaveis() {
		v = new VariaveisDesenharQuadrados();
	}

	/**
	 * Create the frame.
	 */
	public DesenhaQuadrados(CanalComunicacao canal) {
		inicializarVariaveis();
		inicializarGui();
		this.canal = canal;
	}

	private void inicializarGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, (int) (450*zoom), (int) (300*zoom));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnDesenharQuadrado = new JButton("Desenhar Quadrado");
		btnDesenharQuadrado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				escreverConsola("Desenhar quadrados. Numero de quadrados: " + v.getnQuadrados() + " Distancia: " + textFldDistancia.getText());
				desenharQuadrados(v.getnQuadrados(), textFldDistancia.getText());
			}
		});
		btnDesenharQuadrado.setBounds(6, 21, 152, 29);
		contentPane.add(btnDesenharQuadrado);
		
		JLabel lblNQuadrados = new JLabel("N Quadrados");
		lblNQuadrados.setBounds(16, 72, 81, 16);
		contentPane.add(lblNQuadrados);
		
		textFldNQuadrados = new JTextField(v.getnQuadrados() + "");
		textFldNQuadrados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				v.setnQuadrados(Integer.valueOf(textFldNQuadrados.getText()));
				escreverConsola("Novo numero de quadrados: " + v.getnQuadrados());
			}
		});
		textFldNQuadrados.setBounds(101, 67, 130, 26);
		contentPane.add(textFldNQuadrados);
		textFldNQuadrados.setColumns(10);
		
		textArea = new JTextArea();
		textArea.setBounds(6, 139, 438, 133);
		contentPane.add(textArea);
		
		textFldDistancia = new JTextField(v.getDistancia() + "");
		textFldDistancia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				v.setDistancia(Integer.valueOf(textFldDistancia.getText()));
				escreverConsola("Nova distancia: " + v.getDistancia());
			}
		});
		textFldDistancia.setBounds(101, 95, 130, 26);
		contentPane.add(textFldDistancia);
		textFldDistancia.setColumns(10);
		
		JLabel lblDistancia = new JLabel("Distancia");
		lblDistancia.setBounds(26, 100, 61, 16);
		contentPane.add(lblDistancia);
		
		rdbtnEsquerda = new JRadioButton("Esquerda");
		rdbtnEsquerda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				escreverConsola("Orientacao: Esquerda");
			}
		});
		rdbtnEsquerda.setSelected(true);
		buttonGroup.add(rdbtnEsquerda);
		rdbtnEsquerda.setBounds(284, 68, 141, 23);
		contentPane.add(rdbtnEsquerda);
		
		rdbtnDireita = new JRadioButton("Direita");
		rdbtnDireita.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				escreverConsola("Orientacao: Direita");
			}
		});
		buttonGroup.add(rdbtnDireita);
		rdbtnDireita.setBounds(284, 96, 141, 23);
		contentPane.add(rdbtnDireita);
		
		JLabel lblOrientacao = new JLabel("Orientacao");
		lblOrientacao.setBounds(303, 26, 81, 16);
		contentPane.add(lblOrientacao);
	}
	
	private void escreverConsola(String texto) {
		textArea.setText("\n" +numeroInstrucao  + ": " + texto + textArea.getText());
		numeroInstrucao++;
	}
	

	private void desenharQuadrados(int nQuadrados, String distancia) {
		
		EnumEstados estado = (buttonGroup.getSelection().getActionCommand() == "direita")? EnumEstados.CURVA_DIREITA:EnumEstados.CURVA_ESQUERDA;
		for(int i = 0; i < nQuadrados; i++) {
			canal.getAndSet(new Mensagem(EnumEstados.INICIAR_SEQUENCIA.getEstado(), IDCliente));
			canal.getAndSet(new Mensagem(EnumEstados.FRENTE.getEstado(), Integer.valueOf(distancia), IDCliente));
			canal.getAndSet(new Mensagem(estado.getEstado(), 0, 84, IDCliente));
			canal.getAndSet(new Mensagem(EnumEstados.FRENTE.getEstado(), Integer.valueOf(distancia), IDCliente));
			canal.getAndSet(new Mensagem(estado.getEstado(), 0, 84, IDCliente));
			canal.getAndSet(new Mensagem(EnumEstados.FRENTE.getEstado(), Integer.valueOf(distancia), IDCliente));
			canal.getAndSet(new Mensagem(estado.getEstado(), 0, 84, IDCliente));
			canal.getAndSet(new Mensagem(EnumEstados.FRENTE.getEstado(), Integer.valueOf(distancia), IDCliente));
			canal.getAndSet(new Mensagem(estado.getEstado(), 0, 84, IDCliente));
			canal.getAndSet(new Mensagem(EnumEstados.TERMINAR_SEQUENCIA.getEstado(), IDCliente));

		}
		
	}
}
