# Laborat-rio-de-RMI



\# Laboratório de RMI (Java)



Este projeto é um exemplo prático de comunicação remota utilizando \*\*Java RMI (Remote Method Invocation)\*\*. Ele simula um pequeno sistema de mensagens e envio de arquivos entre clientes.



\## Conteúdo do Projeto



\- `Servidor.java`  

&nbsp; Classe principal do servidor RMI. Implementa os métodos remotos disponíveis para os clientes, como envio de mensagens e arquivos.



\- `ServidorRemoto.java`  

&nbsp; Interface que define os métodos remotos que podem ser chamados pelos clientes.



\- `Cliente.java`  

&nbsp; Classe principal que representa um cliente do sistema. Permite:

&nbsp; - Enviar mensagens a outros usuários conectados

&nbsp; - Enviar arquivos para outros clientes (com salvamento automático em uma pasta `recebidos/`)

&nbsp; - Solicitar a data do servidor

&nbsp; - Ver a lista de usuários conectados



\- `ClienteInterface.java`  

&nbsp; Interface remota que define os métodos que um cliente deve implementar para receber mensagens e arquivos.



\- `MainCliente.java`  

&nbsp; Interface de linha de comando para interação com o cliente. O usuário pode digitar comandos para interagir com o sistema.



\- `recebidos/`  

&nbsp; Pasta onde os arquivos recebidos por um cliente são salvos automaticamente.



\## Tecnologias Utilizadas



\- Java 

\- Java RMI





