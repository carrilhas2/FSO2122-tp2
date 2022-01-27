import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;

public class GravarFormasGUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textFieldNomeFicheiro;
	private JButton btnGravarFormas;
	private JButton btnReproduzirFormas;
	private static JTextArea textArea;
	private static int numeroInstrucao = 1;
	private GravarFormas gravarFormas;
	
	public GravarFormasGUI(GravarFormas gravarFormas) {
		inicializarGui();
		this.gravarFormas = gravarFormas;
	}



	private void inicializarGui() {
		getContentPane().setLayout(null);
		
		btnGravarFormas = new JButton("Gravar Formas");
		btnGravarFormas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gravarFormas.setNomeFicheiro(textFieldNomeFicheiro.getText());
				gravarFormas.gravar();
			}
		});
		btnGravarFormas.setBounds(6, 6, 155, 44);
		getContentPane().add(btnGravarFormas);
		
		btnReproduzirFormas = new JButton("Reproduzir Formas");
		btnReproduzirFormas.setBounds(6, 62, 155, 44);
		btnReproduzirFormas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gravarFormas.reproduzir();
			}
		});
		getContentPane().add(btnReproduzirFormas);
		
		JLabel lblNomeDoFicheiro = new JLabel("Nome do Ficheiro:");
		lblNomeDoFicheiro.setBounds(173, 19, 115, 16);
		getContentPane().add(lblNomeDoFicheiro);
		
		textFieldNomeFicheiro = new JTextField();
		textFieldNomeFicheiro.setBounds(295, 14, 149, 26);
		textFieldNomeFicheiro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gravarFormas.setNomeFicheiro(textFieldNomeFicheiro.getText());
			}
		});
		getContentPane().add(textFieldNomeFicheiro);
		textFieldNomeFicheiro.setColumns(10);
		
		JLabel lblNotaDefinirNome = new JLabel("Nota: Definir nome antes de gravar");
		lblNotaDefinirNome.setBounds(173, 90, 271, 16);
		getContentPane().add(lblNotaDefinirNome);
		
		textArea = new JTextArea();
		textArea.setBounds(6, 114, 438, 152);
		getContentPane().add(textArea);
	}
	
	public static void escreverConsola(String texto) {
		textArea.setText("\n" + numeroInstrucao + ": " + texto + textArea.getText());
		numeroInstrucao ++;
	}
}
