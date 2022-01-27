import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sun.misc.IOUtils;

public class GravarFormas implements Runnable {
	
	private InputStream inputstream;
	private OutputStream outputStream;
	private boolean running = true;
	private String estado = "dormir";
	private List<Mensagem> listaDeComandos;
	private String nomeFicheiro = "";

	public GravarFormas() {
		listaDeComandos = new ArrayList<Mensagem>();
		try {
			inputstream = new FileInputStream(nomeFicheiro + ".txt");
			outputStream = new FileOutputStream(nomeFicheiro + ".txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {		
		while(running) {
			switch (estado) {
			case "dormir":
				try {
					Thread.sleep(2000L);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			
			case "gravar":
				//receber mensagem executada
				
				try {
					byte[] bytes = IOUtils.readFully(inputstream, inputstream.available(), true);
					 InputStream inputStream = new ByteArrayInputStream(bytes);

					    StringBuilder textBuilder = new StringBuilder();
					    try (Reader reader = new BufferedReader(new InputStreamReader
					        (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
					      int c = 0;
					      while ((c = reader.read()) != -1) {
					        textBuilder.append((char) c);
					      }
					    }

				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
				
			case "parar":
				
				break;
				
			case "reproduz":
				byte[] bytes;
				try {
					bytes = IOUtils.readFully(inputstream, inputstream.available(), true);
					ByteArrayOutputStream result = new ByteArrayOutputStream();
					 InputStream inputStream = new ByteArrayInputStream(bytes);
					 byte[] buffer = new byte[16644];
					 for (int length; (length = inputStream.read(buffer)) != -1; ) {
					     result.write(buffer, 0, length);
					 }
					 result.toString("UTF-8");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				 
				
				 
				 
				break;
			}
		}
	}
	
	public Mensagem put(Mensagem msg) {
		try {
			if(msg.getTipo() == EnumEstados.LER_MENSAGEM.getEstado()) {
				Mensagem msgParaLer = get();
				return msgParaLer;
			} else {
				put(msg);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void gravar() {
		this.estado = "gravar";
	}
	
	public void reproduzir() {
		this.estado = "reproduz";
	}
	
	public void adicionarMsg(Mensagem msg) {
		listaDeComandos.add(msg);
	}
	
	public String getNomeFicheiro() {
		return nomeFicheiro;
	}

	public void setNomeFicheiro(String nomeFicheiro) {
		this.nomeFicheiro = nomeFicheiro;
	}
}
