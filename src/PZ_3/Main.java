package PZ_3;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Клас Працівник
class Employee {
    private String firstName;
    private String lastName;
    private double salary;

    public Employee(String firstName, String lastName, double salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public double getSalary() { return salary; }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (Зарплата: " + salary + ")";
    }
}

// Клас Відділ
class Department {
    private String name;
    private Employee head;
    private List<Employee> employees;

    public Department(String name, Employee head, List<Employee> employees) {
        this.name = name;
        this.head = head;
        this.employees = new ArrayList<>(employees);
    }

    public String getName() { return name; }
    public Employee getHead() { return head; }
    public List<Employee> getEmployees() { return employees; }

    @Override
    public String toString() {
        return "Відділ: " + name;
    }
}

// Клас Фірма
class Company {
    private String name;
    private Employee director;
    private List<Department> departments;

    public Company(String name, Employee director, List<Department> departments) {
        this.name = name;
        this.director = director;
        this.departments = new ArrayList<>(departments);
    }

    // Геттери для доступу до приватної інформації (додано для виводу)
    public String getName() { return name; }
    public Employee getDirector() { return director; }

    // Задача 3: Скласти список усіх співробітників фірми (включаючи начальників та директора)
    public List<Employee> getAllEmployees() {
        return Stream.concat(
                Stream.of(director),
                departments.stream().flatMap(dept -> Stream.concat(
                        Stream.of(dept.getHead()),
                        dept.getEmployees().stream()
                ))
        ).collect(Collectors.toList());
    }

    // Задача 1: Знайти значення максимальної заробітної платні
    public double getMaxSalary() {
        return getAllEmployees().stream()
                .mapToDouble(Employee::getSalary)
                .max()
                .orElse(0.0);
    }

    // Задача 2: Визначити відділ, в якому хоча б один співробітник отримує більше за начальника
    public List<Department> getDepartmentsWhereEmployeeEarnsMoreThanHead() {
        return departments.stream()
                .filter(dept -> dept.getEmployees().stream()
                        .anyMatch(emp -> emp.getSalary() > dept.getHead().getSalary()))
                .collect(Collectors.toList());
    }
}

public class Main {
    public static void main(String[] args) {
        // Створюємо працівників
        Employee director = new Employee("Каньє", "Вест", 32000);

        Employee headIt = new Employee("Петро", "Щур", 30000);
        Employee itEmp1 = new Employee("Ігор", "Коломойський", 25000);
        Employee itEmp2 = new Employee("Анна", "Трінчер", 35000);

        Employee headHr = new Employee("Маріанна", "Буданова", 20000);
        Employee hrEmp1 = new Employee("Павло", "Зібров", 15000);

        List<Employee> itEmployees = new ArrayList<>();
        itEmployees.add(itEmp1);
        itEmployees.add(itEmp2);

        List<Employee> hrEmployees = new ArrayList<>();
        hrEmployees.add(hrEmp1);

        Department itDept = new Department("IT Відділ", headIt, itEmployees);
        Department hrDept = new Department("HR Відділ", headHr, hrEmployees);

        List<Department> departments = new ArrayList<>();
        departments.add(itDept);
        departments.add(hrDept);

        Company company = new Company("52 Group", director, departments);

        // --- ВИВІД РЕЗУЛЬТАТІВ ---
        System.out.println("--- ЗАГАЛЬНА ІНФОРМАЦІЯ ПРО ФІРМУ ---");
        System.out.println("Назва фірми: " + company.getName());
        System.out.println("Директор: " + company.getDirector().getFirstName() + " " + company.getDirector().getLastName());
        System.out.println("-------------------------------------");

        // Тест задачі 1
        System.out.println("\n1) Максимальна заробітна платня на фірмі:");
        System.out.println(company.getMaxSalary());

        // Тест задачі 2
        System.out.println("\n2) Відділи, де хоча б один працівник отримує більше за начальника:");
        List<Department> richEmpDepts = company.getDepartmentsWhereEmployeeEarnsMoreThanHead();
        richEmpDepts.forEach(System.out::println);

        // Тест задачі 3
        System.out.println("\n3) Список усіх співробітників фірми (включно з керівництвом):");
        List<Employee> allEmployees = company.getAllEmployees();
        allEmployees.forEach(System.out::println);
    }
}