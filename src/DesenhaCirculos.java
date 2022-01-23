import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class DesenhaCirculos extends JFrame implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8601216413492722888L;
	private static final long IDCliente = System.currentTimeMillis()+1L;
	private JPanel contentPane;
	private double zoom = 1;
	private JTextField textFldNCirculos;
	private JTextField textFldRaio;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnEsquerda;
	private JRadioButton rdbtnDireita;
	private JTextArea textArea;
	private JButton btnDesenharCirculo;
	private int numeroInstrucao = 1;
	private VariaveisDesenharCirculos v;
	private CanalComunicacao canal;
	private String estado;
	private boolean running;
	private List<Mensagem> listaMensagens;
	
	/**
	 * Create the frame.
	 */
	public DesenhaCirculos(CanalComunicacao canal) {
		inicializarVariaveis();
		inicializarGui();
		this.canal = canal;
	}
	
	@Override
	public void run() {
		while(running) {
			switch(estado) {
				case "dormir":
					if(listaMensagens.isEmpty()) {
						try {
							Thread.sleep(2000L);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						estado = "escrever";
					}
					break;
				case "escrever":
					break;
				
			}
		}
	}
	
	private void inicializarVariaveis() {
		listaMensagens = new ArrayList<Mensagem>();
		v = new VariaveisDesenharCirculos();
	}

	private void inicializarGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, (int) (450*zoom), (int) (300*zoom));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnDesenharCirculo = new JButton("Desenhar Circulo");
		btnDesenharCirculo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				escreverConsola("Desenhar circulos. Numero de circulos: " + v.getnCirculos() + " Raio: " + textFldRaio.getText());
				desenharCirculos(v.getnCirculos(), textFldRaio.getText());
			}
		});
		btnDesenharCirculo.setBounds(6, 21, 152, 29);
		contentPane.add(btnDesenharCirculo);
		
		JLabel lblNCirculos = new JLabel("N Circulos");
		lblNCirculos.setBounds(16, 72, 81, 16);
		contentPane.add(lblNCirculos);
		
		textFldNCirculos = new JTextField(v.getnCirculos() + "");
		textFldNCirculos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				v.setnCirculos(Integer.valueOf(textFldNCirculos.getText()));
				escreverConsola("Novo numero de circulos: " + v.getnCirculos());
			}
		});
		textFldNCirculos.setBounds(101, 67, 130, 26);
		contentPane.add(textFldNCirculos);
		textFldNCirculos.setColumns(10);
		
		textArea = new JTextArea();
		textArea.setBounds(6, 139, 438, 133);
		contentPane.add(textArea);
		
		textFldRaio = new JTextField(v.getRaio() + "");
		textFldRaio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				v.setRaio(Integer.valueOf(textFldRaio.getText()));
				escreverConsola("Novo raio: " + v.getRaio());
			}
		});
		textFldRaio.setBounds(101, 95, 130, 26);
		contentPane.add(textFldRaio);
		textFldRaio.setColumns(10);
		
		JLabel lblRaio = new JLabel("Raio");
		lblRaio.setBounds(26, 100, 61, 16);
		contentPane.add(lblRaio);
		
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
		rdbtnDireita.setActionCommand("direita");
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
	
	private void desenharCirculos(int nCirculos, String raio) {
		listaMensagens.add(new Mensagem(EnumEstados.INICIAR_SEQUENCIA.getEstado(), IDCliente)); 
		for (int i = 0; i < nCirculos; i++) {
			if(buttonGroup.getSelection().getActionCommand() == "direita") {
				listaMensagens.add(new Mensagem(EnumEstados.CURVA_DIREITA.getEstado(), Integer.valueOf(raio), 350, IDCliente));
			} else {
				listaMensagens.add(new Mensagem(EnumEstados.CURVA_ESQUERDA.getEstado(), Integer.valueOf(raio), 350, IDCliente));
			}			
		}
		listaMensagens.add(new Mensagem(EnumEstados.TERMINAR_SEQUENCIA.getEstado(), IDCliente));

	}
}
