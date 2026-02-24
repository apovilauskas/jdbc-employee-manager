package org.example;

public class Department {

    private int id;
    private String name;
    private String location;

    public Department(int id, String name, String location){
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public Department(){}

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Department{id=" + id + ", name='" + name + "', dept=" + location + "}";
    }
}
