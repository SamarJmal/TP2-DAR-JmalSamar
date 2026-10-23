package serverPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import objectPackage.Operation;

public class Server {
    static final String IP_SERVEUR = "127.0.0.1";
    static final int PORT = 1234;

    public static void main(String[] args) {
        try {
            InetAddress adresseIp = InetAddress.getByName(IP_SERVEUR);
            InetSocketAddress adresseServeur = new InetSocketAddress(adresseIp, PORT);

            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(adresseServeur);
            System.out.println("Je suis un serveur en attente la connexion d'un client...");

            Socket socket = serverSocket.accept();
            System.out.println("un client est connecté");

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            while (true) {
                Object recu = ois.readObject();
                if (recu instanceof String) {
                    System.out.println("Le client a mis fin à la communication.");
                    break;
                }

                Operation op = (Operation) recu;
                double a = op.oper1;
                char operateur = op.opr;
                double b = op.oper2;

                String resultat;
                switch (operateur) {
                    case '+': resultat = String.valueOf(a + b); break;
                    case '-': resultat = String.valueOf(a - b); break;
                    case '*': resultat = String.valueOf(a * b); break;
                    case '/':
                        if (b == 0) resultat = "Erreur";
                        else resultat = String.valueOf(a / b);
                        break;
                    default: resultat = "Opérateur inconnu";
                }

                System.out.println("Reçu : " + a + " " + operateur + " " + b + " -> " + resultat);
                oos.writeObject(resultat);
                oos.flush();
            }

            ois.close();
            oos.close();
            socket.close();
            serverSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
