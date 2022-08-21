import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;



public class Duke {

    public static void newTask(ArrayList<Task> lst, String input) throws DukeException {
        String[] words = input.split(" ",2);
        String first = words[0];

        if (words.length==1) {
            throw new DukeException("The description of a " +  first + " cannot be empty.");
        }

        String s = words[1];

        if (first.equals("deadline")) {
            String[] arr = s.split("/by");
            String desc = arr[0];
            String time = arr[1];
            Deadline t = new Deadline(desc, time);
            lst.add(t);
        }

        else if (first.equals("event")) {
            String[] arr = s.split("/at");
            String desc = arr[0];
            String time = arr[1];
            Event t = new Event(desc, time);
            lst.add(t);
        }

        else if (first.equals("todo")) {
            String desc = words[1];
            ToDo t = new ToDo(desc);
            lst.add(t);
        }

        try {
            writeToFile(lst);

        } catch (FileNotFoundException e) {
            System.out.println("File not found");

        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }

        System.out.println("Got it. I've added this task: \n" + lst.get(lst.size()-1) + "\nNow you have " + lst.size() + " tasks in the list.");
    }

    public static void deleteTask(ArrayList<Task> lst, String input) throws DukeException {
        String[] words = input.split(" ",2);

        if (words.length==1) {
            throw new DukeException("Please specify task to delete");
        }

        int index = Integer.parseInt(words[1]) - 1;

        if (index > lst.size() - 1) {
            throw new DukeException("There is no such task");
        }
        Task t = lst.get(index);
        lst.remove(index);
        System.out.println("Noted. I've removed this task: \n" + t + "\nNow you have " + lst.size() + " tasks in the list.");
    }

    private static void writeToFile(ArrayList<Task> lst) throws IOException {
        String directoryName = "data";
        File directory = new File(directoryName);

        if (!directory.exists()) {
            directory.mkdir();
            System.out.println("created new dir");
        }

        File file = new File(directoryName + "/duke.txt");

        FileWriter fw = new FileWriter(file.getAbsoluteFile()); // create a FileWriter in append mode
        String textToAppend = "Here are the tasks in your list: \n";
        for (int i = 0; i < lst.size(); i++) {
            String s =  (i + 1) + "." + lst.get(i) + "\n";
            textToAppend += s;
        }
        fw.write(textToAppend);
        fw.close();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Hello! I'm Duke.\nWhat can I do for you?\n");
        ArrayList<Task> lst = new ArrayList<Task>();

        while(true) {
            String input = sc.nextLine();
            String[] words = input.split(" ",2);
            String first = words[0];

            try {
                if (first.equals("bye")) {
                    System.out.println("Bye! Hope to see you again soon!");
                    break;
                } else if (first.equals("list")) {
                    for (int i = 0; i < lst.size(); i++) {
                        System.out.println("Here are the tasks in your list: \n" + (i + 1) + "." + lst.get(i));
                    }
                } else if (first.equals("mark")) {
                    char c = input.charAt(5);
                    int index = Integer.parseInt(String.valueOf(c));
                    Task t = lst.get(index - 1);
                    t.markAsDone();
                    lst.set(index - 1, t);
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println(lst.get(index - 1));

                } else if (first.equals("unmark")) {
                    char c = input.charAt(7);
                    int index = Integer.parseInt(String.valueOf(c));

                    Task t = lst.get(index - 1);
                    t.unMark();
                    lst.set(index - 1, t);
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println(lst.get(index - 1));

                } else if (first.equals("deadline") || first.equals("event") || first.equals("todo")) {
                    newTask(lst, input);

                } else if (first.equals("delete")) {
                    deleteTask(lst, input);
                }

                else {
                    throw new DukeException("I'm sorry, but I don't know what that means :-(");
                }
            } catch (DukeException d) {
                System.out.println(d.getMessage());
            }
        }
    }
}
