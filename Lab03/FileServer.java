import java.io.*;
import java.net.*;
import java.util.HashMap;

public class FileServer {
    private static HashMap<String, String> files = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("File Server is running on port 5000...");

        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(() -> handleClient(socket)).start();
        }
    }

    private static void handleClient(Socket socket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String request = in.readLine(); // format: COMMAND|FILENAME|CONTENT (optional)
            if (request == null) {
                out.println("Invalid request.");
                return;
            }

            String[] parts = request.split("\\|", 3);
            if (parts.length < 2) {
                out.println("Invalid command format.");
                return;
            }

            String command = parts[0].trim();
            String filename = parts[1].trim();

            switch (command) {
                case "UPLOAD":
                    if (parts.length < 3) {
                        out.println("Missing content for UPLOAD.");
                        break;
                    }
                    String content = parts[2];
                    files.put(filename, content);
                    out.println("File '" + filename + "' uploaded successfully.");
                    break;

                case "DOWNLOAD":
                    out.println(files.getOrDefault(filename, "Error: File not found."));
                    break;

                case "EDIT":
                    if (parts.length < 3) {
                        out.println("Missing content for EDIT.");
                        break;
                    }
                    if (files.containsKey(filename)) {
                        String newContent = parts[2];
                        files.put(filename, newContent);
                        out.println("File '" + filename + "' edited successfully.");
                    } else {
                        out.println("Error: File does not exist.");
                    }
                    break;

                default:
                    out.println("Invalid command.");
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}