package main.java.com.tracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Json {
    private static final String FILE_NAME = "tasks.json";

    // 1. Fetch File and parse into a List of Tasks
    public static List<Task> fetchFile() {
        Path path = Paths.get(FILE_NAME);
        List<Task> tasks = new ArrayList<>();

        if (!Files.exists(path)) {
            saveToFile(tasks);
            return tasks;
        }

        try {
            String content = Files.readString(path).trim();
            if (content.isEmpty() || content.equals("[]")) {
                return tasks;
            }

            // Simple native JSON parsing for our structure
            content = content.substring(1, content.length() - 1); // remove [ and ]
            String[] items = content.split("},\\s*\\{");

            for (String item : items) {
                item = item.replace("{", "").replace("}", "").replace("\"", "");
                int id = 0;
                String desc = "";
                String status = "pending";

                String[] pairs = item.split(",");
                for (String pair : pairs) {
                    String[] kv = pair.split(":", 2);
                    if (kv.length < 2) continue;
                    String key = kv[0].trim();
                    String val = kv[1].trim();

                    switch (key) {
                        case "id": id = Integer.parseInt(val); break;
                        case "description": desc = val; break;
                        case "status": status = val; break;
                    }
                }
                tasks.add(new Task(id, desc, status));
            }
        } catch (IOException e) {
            System.out.println("Error reading tasks.json");
        }
        return tasks;
    }

    // Save List back to JSON string
    private static void saveToFile(List<Task> tasks) {
        Path path = Paths.get(FILE_NAME);
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            sb.append("  {\n");
            sb.append("    \"id\": ").append(t.getId()).append(",\n");
            sb.append("    \"description\": \"").append(t.getDescription()).append("\",\n");
            sb.append("    \"status\": \"").append(t.getStatus()).append("\",\n");
            sb.append("  }");
            if (i < tasks.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");

        try {
            Files.writeString(path, sb.toString());
        } catch (IOException e) {
            System.out.println("Error writing to tasks.json");
        }
    }

    // 2. Add Task
    public static void addTask(String description) {
        List<Task> tasks = fetchFile();
        int newId = tasks.size() + 1; // auto increment based on current list size
        Task newTask = new Task(newId, description);
        tasks.add(newTask);
        saveToFile(tasks);
        System.out.println("Task added successfully (ID: " + newId + ")");
    }

    // 3. Delete Task & Re-index IDs (1, 2, 3...)
    public static void deleteTask(int taskId) {
        Path path = Paths.get(FILE_NAME);
        if (!Files.exists(path)) {
            System.out.println("No List Found");
            return;
        }

        List<Task> tasks = fetchFile();
        boolean found = false;

        tasks.removeIf(t -> t.getId() == taskId);
        
        // Check if size changed (meaning item was found and removed)
        // Re-index remaining tasks sequentially (1, 2, 3...)
        List<Task> reindexedTasks = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            if (t.getId() == taskId) {
                found = true;
            }
            // Reassign clean sequential ID
            reindexedTasks.add(new Task(i + 1, t.getDescription(), t.getStatus()));
        }

        // Also check if it was found via matching ID directly before removal check
        for (Task t : tasks) {
            if (t.getId() == taskId) found = true;
        }

        // If list size decreased, it was deleted successfully
        saveToFile(reindexedTasks);
        System.out.println("Task deleted and IDs re-indexed successfully.");
    }

    // 4. Update Task Status to Complete
    public static void updateTaskStatus(int taskId) {
        Path path = Paths.get(FILE_NAME);
        if (!Files.exists(path)) {
            System.out.println("Task file not found");
            return;
        }

        List<Task> tasks = fetchFile();
        boolean found = false;

        for (Task t : tasks) {
            if (t.getId() == taskId) {
                t.setStatus("Complete");
                t.setUpdatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("task not found");
            return;
        }

        saveToFile(tasks);
        System.out.println("Task " + taskId + " marked as Complete!");
    }
}