import java.io.*;
import java.net.*;
import java.util.Scanner;

public class FileClient {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\nDistributed File System Client");
            System.out.println("1. Upload File");
            System.out.println("2. Download File");
            System.out.println("3. Edit File");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            if (choice == 4) break;

            System.out.print("Enter filename: ");
            String filename = sc.nextLine().trim();
            String command = "";

            if (choice == 1) {
                System.out.print("Enter file content to upload: ");
                String content = sc.nextLine();
                command = "UPLOAD|" + filename + "|" + content;
            } else if (choice == 2) {
                command = "DOWNLOAD|" + filename;
            } else if (choice == 3) {
                System.out.print("Enter new content to edit: ");
                String newContent = sc.nextLine();
                command = "EDIT|" + filename + "|" + newContent;
            } else {
                System.out.println("Invalid choice. Try again.");
                continue;
            }

            try {
                Socket socket = new Socket("localhost", 5000);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.println(command);
                String response = in.readLine();
                System.out.println("Server Response: " + response);

                socket.close();
            } catch (IOException e) {
                System.out.println("Could not connect to server: " + e.getMessage());
            }
        }

        sc.close();
    }
}