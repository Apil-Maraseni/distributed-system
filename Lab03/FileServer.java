import java.io.*;
import java.net.*;

public class FileServer {

    private static final int PORT = 5000;
    private static final String STORAGE = "server_storage";

    public static void main(String[] args) throws IOException {

        File folder = new File(STORAGE);
        if (!folder.exists())
            folder.mkdir();

        ServerSocket serverSocket = new ServerSocket(PORT);

        System.out.println("Server started on port " + PORT);

        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(() -> handleClient(socket)).start();
        }
    }

    private static void handleClient(Socket socket) {

        try {

            DataInputStream in =
                    new DataInputStream(socket.getInputStream());

            DataOutputStream out =
                    new DataOutputStream(socket.getOutputStream());

            String command = in.readUTF();

            switch (command) {

                case "UPLOAD":
                    uploadFile(in, out);
                    break;

                case "DOWNLOAD":
                    downloadFile(in, out);
                    break;

                case "EDIT":
                    editFile(in, out);
                    break;

                default:
                    out.writeUTF("Invalid Command");
            }

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void uploadFile(DataInputStream in,
                                   DataOutputStream out)
            throws IOException {

        String filename = in.readUTF();

        long size = in.readLong();

        File file = new File(STORAGE + "/" + filename);

        FileOutputStream fos = new FileOutputStream(file);

        byte[] buffer = new byte[4096];

        while (size > 0) {

            int bytes = in.read(buffer, 0,
                    (int) Math.min(buffer.length, size));

            fos.write(buffer, 0, bytes);

            size -= bytes;
        }

        fos.close();

        out.writeUTF("Upload Successful");
    }

    private static void downloadFile(DataInputStream in,
                                     DataOutputStream out)
            throws IOException {

        String filename = in.readUTF();

        File file = new File(STORAGE + "/" + filename);

        if (!file.exists()) {

            out.writeBoolean(false);
            return;

        }

        out.writeBoolean(true);

        out.writeLong(file.length());

        FileInputStream fis = new FileInputStream(file);

        byte[] buffer = new byte[4096];

        int count;

        while ((count = fis.read(buffer)) > 0) {

            out.write(buffer, 0, count);

        }

        fis.close();

    }

    private static void editFile(DataInputStream in,
                                 DataOutputStream out)
            throws IOException {

        String filename = in.readUTF();

        String newContent = in.readUTF();

        File file = new File(STORAGE + "/" + filename);

        if (!file.exists()) {
            out.writeUTF("File Not Found");
            return;
        }
        FileWriter writer = new FileWriter(file);
        writer.write(newContent);
        writer.close();
        out.writeUTF("File Edited Successfully");
    }
}