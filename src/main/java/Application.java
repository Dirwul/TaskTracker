import java.io.*;
import java.util.*;

public class Application {

    static Scanner in;

    static PrintWriter out;

    static List<Task> folder;

    static int UNDEF = -1;

    List<Task> loadDB() throws FileNotFoundException {
        Scanner inDB = new Scanner(new File("src/main/resources/DB.txt"));
        List<Task> db = new ArrayList<>();
        while (inDB.hasNextLine()) {
            String name = inDB.nextLine();
            boolean done = Boolean.parseBoolean(inDB.nextLine());
            db.add(new Task(name, done));
        }
        return db;
    }

    void closeDB() throws FileNotFoundException {
        PrintWriter outDB = new PrintWriter("src/main/resources/DB.txt");
        for (Task cur : folder) {
            outDB.println(cur.name);
            outDB.println(cur.done);
        }
        outDB.close();
    }

    static class Task {

        private String name;

        private boolean done = false;

        Task(String name) {
            this.name = name;
        }

        Task(String name, boolean done) {
            this.name = name;
            this.done = done;
        }

        static int find(String name) {
            for (int i = 0; i < folder.size(); i++) {
                if (folder.get(i).name.equals(name)) {
                    return i;
                }
            }
            return UNDEF;
        }

        static void create(String name) {
            folder.add(new Task(name));
            out.println("Create success!");
            out.flush();
        }

        static void delete(String name) {
            int index = find(name);
            if (index == UNDEF) {
                out.println("can't delete task %s because of it's not exist".formatted(name));
                out.flush();
                return;
            }
            folder.remove(index);
            out.println("delete success");
            out.flush();
        }

        static void edit(String name, String newName) {
            int index = find(name);
            if (index == UNDEF) {
                out.println("can't edit task %s because of it's not exist".formatted(name));
                out.flush();
                return;
            }
            folder.set(index, new Task(newName, folder.get(index).done));
            out.println("edit success");
            out.flush();
        }

        static void view(String name) {
            int index = find(name);
            if (index == UNDEF) {
                out.println("can't viw task %s because of it's not exist".formatted(name));
                out.flush();
                return;
            }
            out.println("Name: %s\nDone: %s".formatted(name, folder.get(index).done ? "Yes" : "No"));
            out.flush();
        }

        static void mark(String name) {
            int index = find(name);
            if (index == UNDEF) {
                out.println("can't mark task %s because of it's not exist".formatted(name));
                out.flush();
                return;
            }
            folder.set(index, new Task(folder.get(index).name, !folder.get(index).done));
            out.println("mark success");
            out.flush();
        }

        static void list() {
            for (Task item : folder) {
                out.println(item.toString());
            }
            if (folder.isEmpty()) {
                out.println("Empty");
            }
            out.flush();
        }

        static void clear() {
            folder.clear();
            out.println("Clear success");
            out.flush();
        }

        @Override
        public String toString() {
            return "Name: %s\nDone: %s\n---".formatted(name, done ? "Yes" : "No");
        }
    }

    void run() throws FileNotFoundException {
        folder = loadDB();
        while (true) {
            String command = in.next();
            switch (command) {
                case "help" -> {
                    out.println("Hello, now u can:\n");
                    out.println("1) create <name>");
                    out.println("2) delete <name>");
                    out.println("3) edit <name><new name>");
                    out.println("4) view <name>");
                    out.println("5) find <name>");
                    out.println("6) mark <name>");
                    out.println("7) list");
                    out.println("8) clear");
                    out.println("9) quit");
                    out.flush();
                }
                case "quit" -> {
                    out.println("Goodbye!");
                    out.flush();
                    closeDB();
                    return;
                }
                case "create" -> Task.create(in.next());
                case "delete" -> Task.delete(in.next());
                case "edit" -> Task.edit(in.next(), in.next());
                case "view" -> Task.view(in.next());
                case "find" -> {
                    out.println((Task.find(in.next()) != UNDEF) ? "Exist" : "Not found");
                    out.flush();
                }
                case "mark" -> Task.mark(in.next());
                case "list" -> Task.list();
                case "clear" -> Task.clear();
                default -> {
                    out.println("Unknown command %s".formatted(command));
                    out.flush();
                }
            }
        }
    }

    private boolean auth() {
        out.println("Hello! Enter your nickname:");
        out.flush();
        String username = in.next();
        out.println("Password: ");
        out.flush();
        String passwd = in.next();
        if (username.equalsIgnoreCase("dirwul") &&
                passwd.equals("AloneNya")
        ) {
            out.printf("Welcome, %s!\n", username);
            out.flush();
            return true;
        }
        out.println("Sorry, uncorrect nickname/password :(");
        out.flush();
        return false;
    }

    private void init() {
        try {
            // TODO: auto flush
            in = new Scanner(System.in);
            out = new PrintWriter(System.out);
            if (auth()) {
                run();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Application().init();
    }
}
