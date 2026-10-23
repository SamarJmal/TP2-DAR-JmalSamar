package clientPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import objectPackage.Operation;

public class Client {
    static final String IP_SERVEUR = "127.0.0.1";
    static final int PORT = 1234;

    public static void main(String[] args) {
        try {
            InetAddress adresseIp = InetAddress.getByName(IP_SERVEUR);
            InetSocketAddress adresseServeur = new InetSocketAddress(adresseIp, PORT);

            System.out.println("Je suis un client pas encore connecté…");
            Socket socket = new Socket();
            socket.connect(adresseServeur, 5000);
            System.out.println("je suis un client connecté");

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.print("Entrez une opération  ou 'fin' : ");
                String saisie = sc.nextLine().trim();

                if (saisie.equalsIgnoreCase("fin")) {
                    oos.writeObject("fin");
                    oos.flush();
                    break;
                }

                String[] parties = saisie.split("\\s+");

                if (parties.length != 3) {
                    System.out.println("Syntaxe incorrecte");
                    continue;
                }

                double a, b;
                try {
                    a = Double.parseDouble(parties[0]);
                    b = Double.parseDouble(parties[2]);
                } catch (NumberFormatException e) {
                    System.out.println("Syntaxe incorrecte : les opérandes doivent être des nombres");
                    continue;
                }

                String operateur = parties[1];
                if (!(operateur.equals("+") || operateur.equals("-")
                        || operateur.equals("*") || operateur.equals("/"))) {
                    System.out.println("Syntaxe incorrecte : opérateurs autorisés : + - * /");
                    continue;
                }

                Operation operation = new Operation(a, operateur.charAt(0), b);
                oos.writeObject(operation);
                oos.flush();
                String resultat = (String) ois.readObject();
                System.out.println("Résultat : " + resultat);
            }

            System.out.println("Fin de la communication");
            sc.close();
            ois.close();
            oos.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
