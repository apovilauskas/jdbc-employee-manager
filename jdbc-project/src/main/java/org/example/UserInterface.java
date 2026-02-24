package org.example;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UserInterface {

    private final Scanner scanner;
    private final DepartmentDAO departmentDAO;
    private final EmployeeDAO employeeDAO;

    public UserInterface(){
        scanner = new Scanner(System.in);
        departmentDAO = new DepartmentDAO();
        employeeDAO = new EmployeeDAO();
    }

    public void BootDatabase() {
        System.out.println("--- Booting up the database ---");
        departmentDAO.createDepartmentTable();
        employeeDAO.createEmployeeTable();

        boolean isRunning = true;
        while (isRunning) {
            printMainMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    handleDepartmentMenu();
                    break;
                case 2:
                    handleEmployeeMenu();
                    break;
                case 3:
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1, 2, or 3.");
            }
        }
        scanner.close();
    }

    private void handleDepartmentMenu() {
        boolean inDeptMenu = true;
        while (inDeptMenu) {
            printDepartmentsMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1: addNewDepartment(); break;
                case 2: viewDepartmentById(); break;
                case 3: viewAllDepartments(); break;
                case 4: updateDepartment(); break;
                case 5: deleteDepartment(); break;
                case 6: inDeptMenu = false; break; // Returns to Main Menu
                default: System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void handleEmployeeMenu() {
        boolean inEmpMenu = true;
        while (inEmpMenu) {
            printEmployeesMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1: addNewEmployee(); break;
                case 2: viewEmployee(); break;
                case 3: viewAllEmployees(); break;
                case 4: updateEmployee(); break;
                case 5: transferAndPromote(); break;
                case 6: deleteEmployee(); break;
                case 7: inEmpMenu = false; break; // Returns to Main Menu
                default: System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n=== COMPANY MANAGER ===");
        System.out.println("1. Manage departments");
        System.out.println("2. Manage employees");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    private void printDepartmentsMenu() {
        System.out.println("\n=== DEPARTMENT MANAGER ===");
        System.out.println("1. Add department");
        System.out.println("2. View department by id");
        System.out.println("3. View all departments");
        System.out.println("4. Update department (name / location)");
        System.out.println("5. Delete department by id");
        System.out.println("6. Back");
        System.out.print("Enter your choice: ");
    }

    private void printEmployeesMenu() {
        System.out.println("\n=== EMPLOYEE MANAGER ===");
        System.out.println("1. Add employee");
        System.out.println("2. View employee (id / email)");
        System.out.println("3. View all employees");
        System.out.println("4. Update employee (name / email / salary / department)");
        System.out.println("5. Transfer and promote employee");
        System.out.println("6. Delete employee by id");
        System.out.println("7. Back");
        System.out.print("Enter your choice: ");
    }

    private int getUserChoice() {
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            return choice;
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Clear the bad input from the scanner
            return -1; // Return an invalid choice to trigger the 'default' switch case
        }
    }

    private void addNewDepartment() {
        System.out.println("\n-- Add department --");
        Department dept = new Department();

        System.out.print("Enter department name: ");
        dept.setName(scanner.nextLine());

        System.out.print("Enter department location: ");
        dept.setLocation(scanner.nextLine());

        departmentDAO.insertDepartment(dept);
    }

    private void addNewEmployee() {
        System.out.println("\n-- Add employee --");
        try {
            Employee emp = new Employee();

            System.out.print("Enter employee name: ");
            emp.setName(scanner.nextLine());

            System.out.print("Enter employee email: ");
            emp.setEmail(scanner.nextLine());

            System.out.print("Enter salary: ");
            emp.setSalary(scanner.nextDouble());
            scanner.nextLine();

            System.out.print("Enter department Id: ");
            emp.setDepartmentId(scanner.nextInt());
            scanner.nextLine();

            employeeDAO.insertEmployee(emp);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Salary and department Id must be numbers.");
            scanner.nextLine();
        }
    }

    private void viewAllDepartments() {
        System.out.println("\n-- All departments --");
        List<Department> depts = departmentDAO.getAllDepartments();
        if (depts.isEmpty()) {
            System.out.println("No departments found.");
            return;
        }
        for (Department d : depts) {
            System.out.printf("ID: %d | Name: %s | Location: %s%n", d.getId(), d.getName(), d.getLocation());
        }
    }

    private void viewAllEmployees() {
        System.out.println("\n-- All employees --");
        List<EmployeeDepartmentDTO> emps = employeeDAO.getAllEmployeesWithDeptInfo();

        if (emps.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }

        System.out.println("ID,Name,Email,Salary,Department,Location");
        System.out.println("--------------------------------------------------------------------------------");

        for (EmployeeDepartmentDTO e : emps) {
            System.out.println(e.getId()+","+ e.getName()+","+e.getEmail()+","+e.getSalary()+","+e.getDepartmentName()+","+e.getLocation());
        }
    }

    public void viewDepartmentById(){
        System.out.println("\n-- View department --");
        try{
            Department dep = new Department();
            System.out.print("Enter department id: ");
            dep.setId(scanner.nextInt());
            Department view = departmentDAO.viewDepartmentById(dep);
            if(view == null){
                System.out.println("Department was not found");
            }else{
                System.out.printf("ID: %d | Name: %s | Location: %s%n", view.getId(), view.getName(), view.getLocation());
            }
        }catch(InputMismatchException e){
            System.out.println("Department id must be a number");
        }
    }

    public void updateDepartment(){
        System.out.println("\n-- Update department --");
        try{
            Department dep = new Department();
            System.out.print("Enter id of the department you wish to update: ");
            dep.setId(scanner.nextInt());
            scanner.nextLine();
            System.out.print("Enter new department name: ");
            dep.setName(scanner.nextLine());
            System.out.print("Enter new location: ");
            dep.setLocation(scanner.nextLine());
            departmentDAO.updateDepartment(dep);
        }catch(InputMismatchException e){
            scanner.nextLine();
            System.out.println("Incorrect format. All parameters should be (number,text,text)");
        }
    }

    public void deleteDepartment(){
        System.out.println("\n-- Delete department --");
        try{
            Department dep = new Department();
            System.out.print("Enter department id: ");
            dep.setId(scanner.nextInt());
            departmentDAO.deleteDepartment(dep);
        }catch(InputMismatchException e){
            System.out.println("Department id must be a number");
        }
    }

    public void viewEmployee(){
        System.out.println("\n-- View employee --");
        try{
            Employee emp = new Employee();
            System.out.print("Find employee by id or email: ");
            if(scanner.hasNextInt()){
                emp.setId(scanner.nextInt());
                scanner.nextLine();
                emp = employeeDAO.findById(emp);
            }else{
                emp.setEmail(scanner.nextLine());
                emp = employeeDAO.findByEmail(emp);
            }
            if(emp == null){
                System.out.println("Employee does not exist");
                return;
            }
            System.out.println("ID,Name,Email,Salary,DepartmentID");
            System.out.println(emp.getId()+","+ emp.getName()+","+emp.getEmail()+","+emp.getSalary()+","+emp.getDepartmentId());
        }catch(InputMismatchException e){
            System.out.println("Enter only id or only email");
        }
    }

    public void updateEmployee(){
        System.out.println("\n-- Update employee --");
        try{
            Employee emp = new Employee();
            System.out.print("Enter id of the employee you wish to update: ");
            emp.setId(scanner.nextInt());
            scanner.nextLine();
            System.out.print("Enter new employee name: ");
            emp.setName(scanner.nextLine());
            System.out.print("Enter new employee email: ");
            emp.setEmail(scanner.nextLine());
            System.out.print("Enter new employee salary: ");
            emp.setSalary(scanner.nextDouble());
            scanner.nextLine();
            System.out.print("Enter new department id: ");
            emp.setDepartmentId(scanner.nextInt());
            scanner.nextLine();
            employeeDAO.updateEmployee(emp);
        }catch(InputMismatchException e){
            scanner.nextLine();
            System.out.println("Incorrect format. All parameters should be (text,text,number,number)");
        }
    }

    public void deleteEmployee() {
        System.out.println("\n-- Delete employee --");
        try{
            Employee emp = new Employee();
            System.out.print("Enter employee id: ");
            emp.setId(scanner.nextInt());
            employeeDAO.deleteEmployee(emp);
        }catch(InputMismatchException e){
            System.out.println("Employee id must be a number");
        }
    }

    public void transferAndPromote(){
        System.out.println("\n-- Transfer and promote employee --");
        int empId, newDeptId;
        double newSalary;
        try{
            System.out.print("Enter employee id: ");
            empId = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter new department id: ");
            newDeptId = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter new salary: ");
            newSalary = scanner.nextDouble();
            employeeDAO.transferAndPromote(empId, newDeptId, newSalary);
        }catch(InputMismatchException e){
            scanner.nextLine();
            System.out.println("Incorrect format. All parameters should be numbers");
        }
    }
}
