package com.example.family;

import java.io.*;

public class DiskManager {
    private static final String STORAGE_DIR = "messages";
    private final int myPort; // Hangi portun diski olduğunu tutar

    public DiskManager(int port) {
        this.myPort = port;
        File directory = new File(STORAGE_DIR);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    public boolean saveMessage(int id, String message) {
        try {
            // Dosya ismi artık ID_PORT.msg (Örn: 123_5555.msg)
            File file = new File(STORAGE_DIR, id + "_" + myPort + ".msg");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
                writer.write(message);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public String loadMessage(int id) {
        // Okurken de kendi portuna ait dosyaya bakar
        File file = new File(STORAGE_DIR, id + "_" + myPort + ".msg");
        if (!file.exists()) {
            return null;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            return content.toString();
        } catch (IOException e) {
            return null;
        }
    }
}