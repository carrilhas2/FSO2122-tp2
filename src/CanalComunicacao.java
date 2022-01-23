import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;


public class CanalComunicacao {

	private static RandomAccessFile memoryMappedFile;
	private static MappedByteBuffer map;
	private static File file;
	private static FileChannel canal;
	
	private static final int MAX_BUFFER = 16644;//512bytes texto + 4bytes id + 4Bytes tipo * 32 mensagens 
	private static final int delta = 520;
	private static int idxPut , idxGet = 4;
	private static int idx = 0;
	
	public CanalComunicacao() {
		memoryMappedFile = null;
		map = null;
		canal = null;
		file = null;
		
	}
	
	public void abrirCanal(String nomeFicheiro) {
		try {
			file = new File(nomeFicheiro);
			memoryMappedFile = new RandomAccessFile(file, "rw");
			canal = memoryMappedFile.getChannel();
			map = canal.map(FileChannel.MapMode.READ_WRITE, 0, MAX_BUFFER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public Mensagem getAndSet(Mensagem msg) {
		try {
			FileLock fl = canal.lock();
			if(msg.getTipo() == EnumEstados.LER_MENSAGEM.getEstado()) {
				Mensagem msgParaLer = get();
				fl.release();
				return msgParaLer;
			} else {
				put(msg);
				fl.release();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void apagarRegisto() {
		map.position(idxGet);
		map.put(new byte[delta]);
	}
	
	private Mensagem get() {
		if(idxGet >= MAX_BUFFER || idxGet == 0) {
			idxGet = 4;
		}
		map.position(idxGet);
		if(map.hasRemaining()) {
			Integer id = map.getInt();
			Integer tipo = map.getInt();
			String texto = "";
			for(int i = 0; i<256; i++) {
				texto += map.getChar();
			}
			if(id != null && EnumEstados.getEstadoPorTipo(tipo) != null && tipo != 0) {
				Mensagem msg = new Mensagem(tipo,texto);
				msg.setId(id);
				apagarRegisto();
				System.out.println(texto);
				idxGet += delta;
				
				return msg;
			}
		}
		else {
			idxGet = 4;
		}
		
		
		return new Mensagem(EnumEstados.ESPERAR_MENSAGEM.getEstado(), "m="+0);
	}

	private void put(Mensagem msg) {
		map.position(0);
		idxPut = map.getInt();
		if(idxPut >= MAX_BUFFER || idxPut == 0) {
			idxPut = 4;
		}
		map.position(idxPut);
		//System.out.println(idxPut);
		map.putInt(idx);
		map.putInt(msg.getTipo());
		char[] temp = transformaTexto(msg.getTexto());
		for( char c : temp) {
			map.putChar(c);
		};
		idxPut += delta;
		idx++;
		map.position(0);
		map.putInt(idxPut);
		
	}

	public void fecharCanal() {
		try {
			canal.close();
			memoryMappedFile.close();
			if (file.delete()) { 
				System.out.println("Ficheiro apagado com sucesso");
			}
			else {
				System.out.println("Nao foi possivel apagar o ficheiro");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private char[] transformaTexto(String texto) {
		char[] resultado = new char[256];

		for (int i = 0; i < 256; i++) {
			resultado[i] = (texto.toCharArray().length>i)?texto.toCharArray()[i]:' ';
		}
		System.out.println(String.valueOf(resultado));
		return resultado;
	}

}
