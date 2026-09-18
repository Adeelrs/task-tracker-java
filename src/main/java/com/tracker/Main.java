package main.java.com.tracker;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        

        while(running)
        {
            try{
                new ProcessBuilder("cmd", "/c","cls").inheritIO().start().waitFor();
            } catch(Exception e){}

            System.out.println("\n=== TASK TRACKER MENU ===");
            System.out.println("1) Show list");
            System.out.println("2) Add to List");
            System.out.println("3) Delete from List");
            System.out.println("4) Mark Task as Done");
            System.out.println("5) Exit");
            System.out.print("Please choose an option: ");

            String choice = scanner.nextLine();
            

            switch (choice) {
                case "1": // Print Lists of tasks
                    List<Task> tasks = Json.fetchFile();
                    if(tasks.isEmpty()){System.out.println("No Task found");}
                    else{
                        System.out.println("\n--- YOUR TASKS ---");
                        for(Task t : tasks)
                        {
                            System.out.println("["+ t.getId()+"] ["+t.getDescription()+"] ["+ t.getStatus()+"]");
                        }
                        System.out.println("Press Enter to continue");
                        scanner.nextLine();
                    }
                    System.out.println("\n Press enter to continue");
                    scanner.nextLine();
                break;
                case "2": // Add task
                    System.out.print("Enter Task description: ");

                    String description = scanner.nextLine();
                    Json.addTask(description);

                    System.out.println("Task added successfully, Press enter to continue");
                    scanner.nextLine();
                break;
                case "3": // Delete Task at given Id
                    boolean validDelId = false;
                while(!validDelId)
                {
                    System.out.print("Select ID of the Task you choose to delete: ");

                    try {
                        int delId = Integer.parseInt(scanner.nextLine());
                        Json.deleteTask(delId);
                        validDelId = true;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid Input format please choose a positive number and try again.");
                    }
                }
                    
                System.out.println("Task deleted successfully, Press enter to continue");
                scanner.nextLine();
                break;
                case "4": // Mark Task done at given Id
                    System.out.print("Select ID of the task you want to mark as done");
                    boolean validUpdateId = false;
                    while(!validUpdateId)
                    {
                        try{
                            int updateID = Integer.parseInt(scanner.nextLine());
                            Json.UpdateStatus(updateID);
                            validUpdateId = true;
                        }catch(NumberFormatException e){ System.out.println("Invalid Input format please choose a positive number and try again.");}
                        System.out.println("Task Status updated successfully");
                        System.out.println("Press enter to continue");
                        scanner.nextLine();

                    }
                    
                break;
                case "5":
                    running = false;
                    System.out.println("Exiting task");
                break;
            
                default:
                    System.out.println("Invalid input, please input a number 1 - 5");
                    System.out.println("\n Press Enter to continue");
                    break;
            }
        }

       scanner.close();
    }

    
    



}
