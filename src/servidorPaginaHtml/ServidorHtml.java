package servidorPaginaHtml;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Date;

public class ServidorHtml {

    public static void main(String[] args) throws IOException {
        
        ServerSocket servidor = new ServerSocket(8530);
        Socket socket = servidor.accept();
        
        if (socket.isConnected()) {
            
            System.out.println(socket.getInetAddress());
            
            BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            String linha = buffer.readLine();
            System.out.println(linha);
            
            String[] dadosReq = linha.split(" ");
            String localArquivo = dadosReq[1];
            String protocolo = dadosReq[2];
            
            if (localArquivo.equals("/"))
                localArquivo = "index.html";
            
            File BuscaHtml = new File(localArquivo);
            String status = protocolo + " 200 OK\r\n";
            
            if (!BuscaHtml.exists()) {
                status = protocolo + " 404 Not Found\r\n";
                System.out.println("NAO ACHOU O LOCAL DO ARQUIVO");
            }

            byte[] conteudo = Files.readAllBytes(BuscaHtml.toPath());
            
            Date data = new Date();
            
            String header = status
                    + "Location: http://localhost:8530/\r\n"
                    + "Date: " + data + "\r\n"
                    + "Server: MeuServidor/1.0\r\n"
                    + "Content-Type: text/html\r\n"
                    + "Content-Length: " + conteudo.length + "\r\n"
                    + "Connection: close\r\n"
                    + "\r\n";
            
            OutputStream resposta = socket.getOutputStream();
            
            resposta.write(header.getBytes());
            resposta.write(conteudo);
            
            resposta.flush();
        }
    }
}