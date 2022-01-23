import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUIServidor extends JFrame{

	private static final long serialVersionUID = 6753842018479579961L;
	private static final long IDServidor = System.currentTimeMillis();
	private static boolean ativo;
	private static EnumEstados estado = EnumEstados.LER_MENSAGEM;
	private JPanel guiContentPane;
	private JTextField textFldNome;
	private JTextField textFldRaio;
	private JTextField textFldAngulo;
	private JTextField textFldDistancia;
	private JButton btnAbrir;
	private JButton btnFrente;
	private JButton btnTras;
	private JButton btnEsquerda;
	private JButton btnDireita;
	private JButton btnParar;
	private JTextArea textAreaConsola;
	private boolean abrir = false;
	private double zoom = 1;
	private static Variaveis v;
	private int numeroInstrucao = 1;
	private static CanalComunicacao canal = new CanalComunicacao();
	private static MyRobotLego robot;
	private HashMap<String, ArrayList<Mensagem>> queueMap;
	private int velocidadeRobot = 30;
	private ScheduledExecutorService oneThreadScheduleExecutor;
	private String idCliente = "";
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					GUIServidor frame = new GUIServidor();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
	}
	
	private void gerirRobot() {
		Mensagem msgLida = null;
		int distancia = 0;
		int raio = 0;
		double angulo = 0.0;
		long tempoDeExecucao;
		ativo = true;
		
		while(ativo) {
			switch (estado) {
			case LER_MENSAGEM:
				System.out.println("LER_MENSAGEM");
				Mensagem msg = new Mensagem(EnumEstados.LER_MENSAGEM.getEstado(),IDServidor);
				msgLida = canal.getAndSet(msg);
				idCliente = String.valueOf(msgLida.getIdCliente());
				
				if(queueMap.containsKey(idCliente) && msgLida.getTipo() != EnumEstados.TERMINAR_SEQUENCIA.getEstado()) {
					ArrayList<Mensagem> temp = queueMap.get(idCliente);
					temp.add(msgLida);
					queueMap.put(idCliente, temp);
					break;
				}
				else {
					estado = EnumEstados.getEstadoPorTipo(msgLida.getTipo());
				}
				break;
				
			case ESPERAR_MENSAGEM:
				System.out.println("ESPERAR_MENSAGEM");
				estado = EnumEstados.LER_MENSAGEM;
				oneThreadScheduleExecutor.schedule(()->{gerirRobot();}, 2L, TimeUnit.SECONDS);
				ativo = false;
				break;
				
			case INICIAR_SEQUENCIA:
				System.out.println("INICIAR_SEQUENCIA");
				queueMap.put(String.valueOf(msgLida.getIdCliente()), new ArrayList<Mensagem>());
				estado = EnumEstados.LER_MENSAGEM;
				break;
				
			case TERMINAR_SEQUENCIA:
				System.out.println("TERMINAR_SEQUENCIA");
				if(queueMap.containsKey(idCliente) && !queueMap.get(idCliente).isEmpty()) {
					ArrayList<Mensagem> temp = queueMap.get(idCliente);
					temp.sort(new IDSorter());
					msgLida = temp.remove(0);
					queueMap.put(idCliente,temp);
					if(queueMap.get(idCliente).isEmpty()) {
						queueMap.remove(idCliente);
					}
					estado = EnumEstados.getEstadoPorTipo(msgLida.getTipo());
				}else {
					estado = EnumEstados.LER_MENSAGEM;
				}
				
				break;
				
			case CURVA_DIREITA:
				System.out.println("CURVA_DIREITA");
				raio = msgLida.getRaio();
				angulo = msgLida.getAngulo();

				distancia = (int) (Math.toRadians(angulo)  * raio);
				robot.curvarDireita(raio, (int)angulo);
				tempoDeExecucao = (distancia / velocidadeRobot);
				estado = EnumEstados.TERMINAR_SEQUENCIA;
				oneThreadScheduleExecutor.schedule(()->{gerirRobot();}, tempoDeExecucao, TimeUnit.SECONDS);
				ativo = false;
				break;
				
			case CURVA_ESQUERDA:
				System.out.println("CURVA_ESQUERDA");
				raio = msgLida.getRaio();
				angulo = msgLida.getAngulo();

				distancia = (int) (Math.toRadians(angulo)  * raio);
				robot.curvarEsquerda(raio, (int)angulo);
				tempoDeExecucao = (distancia / velocidadeRobot);
				estado = EnumEstados.TERMINAR_SEQUENCIA;
				oneThreadScheduleExecutor.schedule(()->{gerirRobot();}, tempoDeExecucao, TimeUnit.SECONDS);
				ativo = false;
				break;
			
			case FRENTE:
				System.out.println("FRENTE");
				distancia = msgLida.getDistancia();
				robot.reta(distancia);
				tempoDeExecucao = (distancia / velocidadeRobot);
				estado = EnumEstados.TERMINAR_SEQUENCIA;
				oneThreadScheduleExecutor.schedule(()->{gerirRobot();}, tempoDeExecucao*2, TimeUnit.SECONDS);
				ativo = false;
				break;
				
			case PARAR:
				System.out.println("PARAR");
				robot.parar();
				estado = EnumEstados.TERMINAR_SEQUENCIA;
				break;

			case TRAS:
				System.out.println("TRAS");
				distancia = msgLida.getDistancia();
				robot.reta(-distancia); 
				tempoDeExecucao = (distancia / velocidadeRobot);
				estado = EnumEstados.TERMINAR_SEQUENCIA;
				oneThreadScheduleExecutor.schedule(()->{gerirRobot();}, tempoDeExecucao, TimeUnit.SECONDS);
				ativo = false;
				break;
			}
		}
	}
	
	private void inicializarVariaveis() {
		v = new Variaveis();
		queueMap = new HashMap<String, ArrayList<Mensagem>>();
		oneThreadScheduleExecutor = Executors.newScheduledThreadPool(1);
		
	}

	/**
	 * Create the frame.
	 */
	public GUIServidor() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				ativo = false;
				robot.closeRobot();
				canal.fecharCanal();
			}
		});
		inicializarVariaveis();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, (int) (900*zoom), (int) (600*zoom));
		guiContentPane = new JPanel();
		guiContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(guiContentPane);
		guiContentPane.setLayout(null);
		
		textFldNome = new JTextField(v.getNomeRobot());
		textFldNome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				v.setNomeRobot(textFldNome.getText());
				escreverConsola("Novo nome: " +  v.getNomeRobot());

			}
		});
		textFldNome.setBounds(86, 23, (int) (130*zoom), (int) (26*zoom));
		guiContentPane.add(textFldNome);
		textFldNome.setColumns(10);
		
		JLabel lblNome = new JLabel("Nome:");
		lblNome.setBounds(6, 28, (int) (61*zoom), (int) (16*zoom));
		guiContentPane.add(lblNome);
		
		textFldRaio = new JTextField(v.getRaio() + "");
		textFldRaio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				v.setRaio(Integer.parseInt(textFldRaio.getText()));
				escreverConsola("Novo raio: " +  v.getRaio());
			}
		});
		textFldRaio.setColumns(10);
		textFldRaio.setBounds(86, 61, (int) (130*zoom), (int) (26*zoom));
		guiContentPane.add(textFldRaio);
		
		textFldAngulo = new JTextField(v.getAngulo() + "");
		textFldAngulo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				v.setAngulo(Integer.parseInt(textFldAngulo.getText()));
				escreverConsola("Novo angulo: " +  v.getAngulo());

			}
		});
		textFldAngulo.setColumns(10);
		textFldAngulo.setBounds(86, 99, (int) (130*zoom), (int) (26*zoom));
		guiContentPane.add(textFldAngulo);
		
		JLabel lblRaio = new JLabel("Raio:");
		lblRaio.setBounds(6, 66, (int) (61*zoom), (int) (16*zoom));
		guiContentPane.add(lblRaio);
		
		JLabel lblAngulo = new JLabel("Angulo:");
		lblAngulo.setBounds(6, 108, (int) (61*zoom), (int) (16*zoom));
		guiContentPane.add(lblAngulo);
		
		JLabel lblDistncia = new JLabel("Distância:");
		lblDistncia.setBounds(6, 146, (int) (85*zoom), (int) (16*zoom));
		guiContentPane.add(lblDistncia);
		
		textFldDistancia = new JTextField(v.getDistancia() + "");
		textFldDistancia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				v.setDistancia(Integer.parseInt(textFldDistancia.getText()));
				escreverConsola("Nova distancia: " +  v.getDistancia());
			}
		});
		textFldDistancia.setColumns(10);
		textFldDistancia.setBounds(86, 141, (int) (130*zoom), (int) (26*zoom));
		guiContentPane.add(textFldDistancia);
		
		btnAbrir = new JButton("Abrir");
		btnAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrir = !abrir;
				escreverConsola(abrir ? "Abrir conexão ao robot :" + textFldNome.getText() : "Fechar conexão ao robot" + textFldNome.getText());
				btnDireita.setEnabled(abrir);
				btnEsquerda.setEnabled(abrir);
				btnFrente.setEnabled(abrir);
				btnParar.setEnabled(abrir);
				btnTras.setEnabled(abrir);
				
				if(abrir) {
					robot = new MyRobotLego();
					robot.setNomeRobot(v.getNomeRobot());
					robot.startRobot();
					canal.abrirCanal("../teste.txt");
					ativo = true;
					gerirRobot();
				} else {
					ativo = false;
					robot.closeRobot();
					canal.fecharCanal();
				}
			}
		});
		btnAbrir.setBounds(777, 23, (int) (117*zoom), (int) (29*zoom));
		guiContentPane.add(btnAbrir);
		
		btnFrente = new JButton("Frente");
		btnFrente.setEnabled(abrir);
		btnFrente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(abrir) {
				escreverConsola("Andar em frente. Distancia: " + textFldDistancia.getText());
				robot.reta(v.getDistancia());
				
				try {
					long tempoDeExecucao = (v.getDistancia() / velocidadeRobot) * 1000;
					Thread.sleep(tempoDeExecucao);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}}
		});
		btnFrente.setBounds(534, 80, (int) (148*zoom), (int) (45*zoom));
		guiContentPane.add(btnFrente);
		
		btnParar = new JButton("Parar");
		btnParar.setEnabled(abrir);
		btnParar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(abrir) {
					escreverConsola("Parar");
					robot.parar();
			}}
		});
		btnParar.setBounds(534, 133, (int) (148*zoom), (int) (45*zoom));
		guiContentPane.add(btnParar);
		
		btnTras = new JButton("Tras");
		btnTras.setEnabled(abrir);
		btnTras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(abrir) {
					escreverConsola("Andar para tras. Distancia: " + textFldDistancia.getText());
					robot.reta(-v.getDistancia());
					try {
						long tempoDeExecucao = (v.getDistancia() / velocidadeRobot) * 1000;
						Thread.sleep(tempoDeExecucao);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnTras.setBounds(534, 179, (int) (148*zoom), (int) (40*zoom));
		guiContentPane.add(btnTras);
		
		btnEsquerda = new JButton("Esquerda");
		btnEsquerda.setEnabled(abrir);
		btnEsquerda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(abrir) {
					escreverConsola("Andar para a esquerda. Angulo: " + textFldAngulo.getText() + " Raio: " + textFldRaio.getText());
					robot.curvarEsquerda(v.getRaio(), v.getAngulo());
					int distancia = (int) (Math.toRadians(v.getAngulo())  * v.getRaio());

					try {
						long tempoDeExecucao = (distancia / velocidadeRobot) * 1000;
						Thread.sleep(tempoDeExecucao);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnEsquerda.setBounds(375, 133, (int) (147*zoom), (int) (45*zoom));
		guiContentPane.add(btnEsquerda);
		
		btnDireita = new JButton("Direita");
		btnDireita.setEnabled(abrir);
		btnDireita.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(abrir) {
					escreverConsola("Andar para a direita. Angulo: " + textFldAngulo.getText() + " Raio: " + textFldRaio.getText());
					robot.curvarDireita(v.getRaio(), v.getAngulo());
					int distancia = (int) (Math.toRadians(v.getAngulo())  * v.getRaio());

					try {
						long tempoDeExecucao = (distancia / velocidadeRobot) * 1000;
						Thread.sleep(tempoDeExecucao);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnDireita.setBounds(694, 133, (int) (148*zoom), (int) (45*zoom));
		guiContentPane.add(btnDireita);
		
		textAreaConsola = new JTextArea();
		textAreaConsola.setBounds(6, 244, (int) (888*zoom), (int) (328*zoom));
		guiContentPane.add(textAreaConsola);
		
	}
	
	private void escreverConsola(String texto) {
		textAreaConsola.setText("\n" +numeroInstrucao + ": " + texto + textAreaConsola.getText());
		numeroInstrucao++;
	}

	public static CanalComunicacao getCanal() {
		return canal;
	}

}
