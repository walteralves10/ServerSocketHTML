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
        System.out.println("Aguardando conexao ...");

        //if (socket.isConnected()) {
        while (true) {

            Socket socket = servidor.accept();

            System.out.println(socket.getRemoteSocketAddress());

            BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String linha = buffer.readLine();
            System.out.println(linha + "\n");
            String[] divideRequisicao;
            String localArquivo = null, protocolo = null, status = null;
            File BuscaHtml = null;
            byte[] conteudo = null;

            if (linha != null) {
                divideRequisicao = linha.split(" ");
                localArquivo = divideRequisicao[1];
                protocolo = divideRequisicao[2];

                if (localArquivo.equals("/")) {
                    localArquivo = "src\\servidorPaginaHtml\\index.html";
                    BuscaHtml = new File(localArquivo);
                    status = protocolo + " 200 OK\r\n";

                    conteudo = Files.readAllBytes(BuscaHtml.toPath());

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
            } else {
                status = protocolo + " 404 Not Found\r\n";
                System.out.println("NAO ACHOU O LOCAL DO ARQUIVO");
            }

            //if (!BuscaHtml.exists()) {
            //    status = protocolo + " 404 Not Found\r\n";
            //    System.out.println("NAO ACHOU O LOCAL DO ARQUIVO");
            //}
        }

        //}
    }
}
