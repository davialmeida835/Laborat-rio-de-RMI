import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Cliente extends UnicastRemoteObject implements ClienteChatRemoto {

    private String usuario;
    private ServidorChatRemoto servidor;

    protected Cliente(String usuario, ServidorChatRemoto servidor) throws RemoteException {
        super();
        this.usuario = usuario;
        this.servidor = servidor;
        servidor.cadastrarCliente(usuario, null, 0, this);
    }

    @Override
    public void receberMensagem(String remetente, String mensagem) throws RemoteException {
        System.out.println("[Mensagem direta] " + remetente + ": " + mensagem);
    }

    @Override
    public void receberMensagemSala(String remetente, String mensagem) throws RemoteException {
        System.out.println("[Sala] " + remetente + ": " + mensagem);
    }

    @Override
public void receberArquivo(String remetente, String nomeArquivo, byte[] dados) throws RemoteException {
    System.out.println("Arquivo recebido de [" + remetente + "]: " + nomeArquivo + " (" + dados.length + " bytes)");

    try {
        Files.createDirectories(Paths.get("recebidos"));
        Files.write(Paths.get("recebidos/" + nomeArquivo), dados);
        System.out.println("Arquivo salvo em: recebidos/" + nomeArquivo);
    } catch (Exception e) {
        System.err.println("Erro ao salvar arquivo: " + e.getMessage());
    }
}


    // Enviar mensagem privada
    public void enviarMensagemPrivada(String destinatario, String mensagem) throws RemoteException {
        ClienteChatRemoto clienteDest = servidor.obterCliente(destinatario);
        if (clienteDest != null) {
            clienteDest.receberMensagem(usuario, mensagem);
        } else {
            System.out.println("Cliente não encontrado: " + destinatario);
        }
    }

    // Enviar mensagem para a sala
    public void enviarMensagemSala(String mensagem) throws RemoteException {
        servidor.enviarMensagemSala(usuario, mensagem);
    }

    // Enviar arquivo para outro cliente
    public void enviarArquivo(String destinatario, String caminhoArquivo) {
        try {
            ClienteChatRemoto clienteDest = servidor.obterCliente(destinatario);
            if (clienteDest == null) {
                System.out.println("Cliente destinatário não encontrado.");
                return;
            }
            byte[] dados = Files.readAllBytes(Paths.get(caminhoArquivo));
            String nomeArquivo = Paths.get(caminhoArquivo).getFileName().toString();

            clienteDest.receberArquivo(usuario, nomeArquivo, dados);
            System.out.println("Arquivo enviado para " + destinatario);
        } catch (Exception e) {
            System.out.println("Erro ao enviar arquivo: " + e.getMessage());
        }
    }

    public void listarClientes() throws RemoteException {
        List<String> clientes = servidor.listarClientes();
        System.out.println("Usuários conectados: " + clientes);
    }

    public void sair() throws RemoteException {
        servidor.removerCliente(usuario);
        System.out.println("Você saiu do chat.");
    }

    public static void main(String[] args) {
        try {
            String host = "127.0.0.1";
            if (args.length == 1) host = args[0];

            ServidorChatRemoto servidor = (ServidorChatRemoto) Naming.lookup("//" + host + "/Servidor");

            Scanner scanner = new Scanner(System.in);
            System.out.print("Digite seu @usuario: ");
            String usuario = scanner.nextLine().trim();

            Cliente cliente = new Cliente(usuario, servidor);

            while (true) {
                System.out.println("\n1. Listar clientes");
                System.out.println("2. Enviar mensagem para a sala");
                System.out.println("3. Enviar mensagem privada");
                System.out.println("4. Enviar arquivo para cliente");
                System.out.println("0. Sair");
                System.out.print("Escolha: ");

                String escolha = scanner.nextLine();

                switch (escolha) {
                    case "1":
                        cliente.listarClientes();
                        break;
                    case "2":
                        System.out.print("Digite a mensagem para a sala: ");
                        String msgSala = scanner.nextLine();
                        cliente.enviarMensagemSala(msgSala);
                        break;
                    case "3":
                        System.out.print("Digite o usuário destinatário: ");
                        String destMsg = scanner.nextLine();
                        System.out.print("Digite a mensagem: ");
                        String msgPriv = scanner.nextLine();
                        cliente.enviarMensagemPrivada(destMsg, msgPriv);
                        break;
                    case "4":
                        System.out.print("Digite o usuário destinatário: ");
                        String destArq = scanner.nextLine();

                        System.out.print("Digite o caminho completo do arquivo: ");
                        String caminho = scanner.nextLine();

                        cliente.enviarArquivo(destArq, caminho);
                        break;
                    case "0":
                        cliente.sair();
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println("Opção inválida.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
