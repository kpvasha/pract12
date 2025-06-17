import java.io.*;
import java.util.Scanner;

public class pract12 {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print("Введіть ім'я файлу: ");
        String filename = scanner.nextLine();

        boolean running = true;
        while (running) {
            displayMenu();
            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> writeToFile(filename);
                    case 2 -> readFile(filename);
                    case 3 -> {
                        System.out.println("До побачення");
                        running = false;
                    }
                    case 4 -> readFileRange(filename);
                    case 5 -> insertAtLine(filename);
                    default -> System.out.println("Спробуйте ще раз.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Помилка: Введіть число від 1 до 5.");
            } catch (Exception e) {
                System.out.println("Помилка: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("""
                РЕДАКТОР
                1. Записати до файлу
                2. Прочитати вміст файлу
                3. Вийти з редактора
                4. Прочитати діапазон рядків
                5. Записати в обраний рядок
                """);
        System.out.print("Оберіть: ");
    }

    private static void writeToFile(String filename) {
        System.out.print("Перезаписати файл (1) Додати в кінець (2)? ");
        String writeMode = scanner.nextLine();
        boolean append = !writeMode.equals("1");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, append))) {
            System.out.println("Введіть текст (для завершення введення введіть '/'):\n");

            String[] lines = new String[100];
            int lineCount = 0;

            while (true) {
                System.out.print((lineCount + 1) + ": ");
                String line = scanner.nextLine();
                if (line.equals("/")) break;
                if (lineCount < lines.length) lines[lineCount++] = line;
                else {
                    System.out.println("Досягнуто максимальної кількості рядків.");
                    break;
                }
            }

            for (int i = 0; i < lineCount; i++) {
                writer.write(lines[i]);
                writer.newLine();
            }

            System.out.println("Текст записано у файл '" + filename + "'");
        } catch (IOException e) {
            System.out.println("Помилка при записі у файл: " + e.getMessage());
        }
    }

    private static void readFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String[] lines = new String[1000];
            int lineCount = 0;
            String line;
            while ((line = reader.readLine()) != null && lineCount < lines.length) {
                lines[lineCount++] = line;
            }

            if (lineCount > 0) {
                System.out.println("\nВміст файлу '" + filename + "':");
                for (int i = 0; i < lineCount; i++) {
                    System.out.println((i + 1) + ": " + lines[i]);
                }
            } else {
                System.out.println("Файл '" + filename + "' порожній.");
            }
        } catch (IOException e) {
            System.out.println("Помилка при читанні файлу: " + e.getMessage());
        }
    }

    private static void readFileRange(String filename) {
        System.out.print("Введіть номер початкового рядка: ");
        int start = Integer.parseInt(scanner.nextLine());
        System.out.print("Введіть номер кінцевого рядка: ");
        int end = Integer.parseInt(scanner.nextLine());

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String[] lines = new String[1000];
            int lineCount = 0;
            String line;
            while ((line = reader.readLine()) != null && lineCount < lines.length) {
                lines[lineCount++] = line;
            }

            for (int i = start - 1; i < end && i < lineCount; i++) {
                System.out.println((i + 1) + ": " + lines[i]);
            }
        } catch (IOException e) {
            System.out.println("Помилка при читанні діапазону: " + e.getMessage());
        }
    }

    private static void insertAtLine(String filename) {
        System.out.print("Введіть номер рядка, в який потрібно вставити текст: ");
        int insertIndex = Integer.parseInt(scanner.nextLine()) - 1;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String[] lines = new String[1000];
            int count = 0;
            String line;
            while ((line = reader.readLine()) != null && count < lines.length) {
                lines[count++] = line;
            }

            System.out.println("Введіть текст для вставки (завершіть '/'): ");
            String[] newLines = new String[100];
            int newLineCount = 0;
            while (true) {
                System.out.print((insertIndex + 1 + newLineCount) + ": ");
                String newLine = scanner.nextLine();
                if (newLine.equals("/")) break;
                if (newLineCount < newLines.length) newLines[newLineCount++] = newLine;
                else break;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                for (int i = 0; i < count; i++) {
                    if (i == insertIndex) {
                        for (int j = 0; j < newLineCount; j++) {
                            writer.write(newLines[j]);
                            writer.newLine();
                        }
                    }
                    writer.write(lines[i]);
                    writer.newLine();
                }
                if (insertIndex >= count) {
                    for (int j = 0; j < newLineCount; j++) {
                        writer.write(newLines[j]);
                        writer.newLine();
                    }
                }
            }

            System.out.println("Текст вставлено у рядок " + (insertIndex + 1));

        } catch (IOException e) {
            System.out.println("Помилка вставки: " + e.getMessage());
        }
    }
}
