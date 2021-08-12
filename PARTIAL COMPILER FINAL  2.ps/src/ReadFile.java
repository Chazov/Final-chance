import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class ReadFile {
    private String[] words;

    void prepareFile(String path) {
        try {
            FileReader file = new FileReader(path);
            BufferedReader reader = new BufferedReader(file);
            words = new String[(int) reader.lines().count()];
            file.close();
            reader.close();

            file = new FileReader(path);
            reader = new BufferedReader(file);

            String line;
            int curr = 0;

            while ((line = reader.readLine()) != null) {
                words[curr] = line;
                curr++;
            }
            file.close();
        } catch (IOException e) {
            System.out.println("Error while reading file "
                    + path + ": " + e);
            e.printStackTrace();
        }
    }

    String[] getContent() {
        return words;
    }
}
