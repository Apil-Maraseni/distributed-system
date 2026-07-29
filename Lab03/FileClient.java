import java.io.*;
import java.net.*;
import java.util.Scanner;

public class FileClient {

    private static final String SERVER = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n===== Distributed File System Client =====");
            System.out.println("1. Upload File");
            System.out.println("2. Download File");
            System.out.println("3. Edit File");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 4)
                break;

            try {

                Socket socket = new Socket(SERVER, PORT);

                DataInputStream in =
                        new DataInputStream(socket.getInputStream());

                DataOutputStream out =
                        new DataOutputStream(socket.getOutputStream());

                switch (choice) {

                    case 1:
                        uploadFile(sc, in, out);
                        break;

                    case 2:
                        downloadFile(sc, in, out);
                        break;

                    case 3:
                        editFile(sc, in, out);
                        break;

                    default:
                        System.out.println("Invalid Choice");
                }

                socket.close();

            } catch (Exception e) {

                System.out.println(e.getMessage());

            }

        }

        sc.close();

    }

private static void uploadFile(Scanner sc,
                               DataInputStream in,
                               DataOutputStream out)
        throws IOException {

    System.out.print("Enter filename (inside uploads folder): ");
    String filename = sc.nextLine();

    File folder = new File("uploads");
    if (!folder.exists()) {
        folder.mkdir();
    }

    File file = new File(folder, filename);

    if (!file.exists()) {

        System.out.print("File does not exist. Create it? (Y/N): ");
        String choice = sc.nextLine();

        if (!choice.equalsIgnoreCase("Y")) {
            return;
        }

        file.createNewFile();

        System.out.println("Enter file content (Press Enter when finished):");
        String content = sc.nextLine();

        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.close();

        System.out.println("File created successfully in uploads folder.");
    }

    out.writeUTF("UPLOAD");
    out.writeUTF(filename);
    out.writeLong(file.length());

    FileInputStream fis = new FileInputStream(file);

    byte[] buffer = new byte[4096];
    int count;

    while ((count = fis.read(buffer)) > 0) {
        out.write(buffer, 0, count);
    }

    fis.close();

    System.out.println(in.readUTF());
}


    private static void downloadFile(Scanner sc,
                                     DataInputStream in,
                                     DataOutputStream out)
            throws IOException {

        System.out.print("Enter filename: ");

        String filename = sc.nextLine();

        out.writeUTF("DOWNLOAD");

        out.writeUTF(filename);

        boolean exists = in.readBoolean();

        if (!exists) {

            System.out.println("File not found on server.");
            return;

        }

        long size = in.readLong();

        File file = new File("downloads/" + filename);

        FileOutputStream fos = new FileOutputStream(file);

        byte[] buffer = new byte[4096];

        while (size > 0) {

            int bytes = in.read(buffer, 0,
                    (int) Math.min(buffer.length, size));

            fos.write(buffer, 0, bytes);

            size -= bytes;

        }

        fos.close();

        System.out.println("Downloaded successfully.");

    }

    private static void editFile(Scanner sc,
                                 DataInputStream in,
                                 DataOutputStream out)
            throws IOException {

        System.out.print("Enter filename: ");

        String filename = sc.nextLine();

        System.out.print("Enter new content: ");

        String content = sc.nextLine();

        out.writeUTF("EDIT");

        out.writeUTF(filename);

        out.writeUTF(content);

        System.out.println(in.readUTF());

    }

}