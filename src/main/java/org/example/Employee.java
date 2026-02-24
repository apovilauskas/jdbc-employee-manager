package org.example;

public class Employee {

    private int id;
    private String name;
    private String email;
    private double salary;
    private int departmentId;

    public Employee() {}

    public Employee(int id, String name, String email, double salary, int departmentId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.salary = salary;
        this.departmentId = departmentId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }

    @Override
    public String toString() {
        return "Employee{id=" + id + ", name='" + name + "', dept=" + departmentId + "}";
    }
}