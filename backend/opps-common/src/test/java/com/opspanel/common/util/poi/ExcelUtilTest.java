package com.opspanel.common.util.poi;

import com.opspanel.common.annotation.Excel;
import com.opspanel.common.annotation.ExcelHandlerAdapter;
import com.opspanel.common.util.poi.ExcelUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

public class ExcelUtilTest {

    public static void main(String[] args) {
        testExport();

        testImport();

        testComplexExport();
    }

    private static void testExport() {
        List<User> users = createTestUsers();

        try (OutputStream os = new FileOutputStream("users_export.xlsx")) {
            ExcelUtil.exportToExcel(users, "user list", User.class, os);
        } catch (Exception e) {
            System.err.println("fail: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testImport() {
        try (InputStream is = new FileInputStream("users_export.xlsx")) {
            List<User> importedUsers = ExcelUtil.importFromExcel(is, User.class);

            System.out.println("success " + importedUsers.size() + " records");
            importedUsers.forEach(user ->
                    System.out.println("  - " + user.getName() + " (" + user.getEmail() + ")")
            );
        } catch (Exception e) {
            System.err.println("fail: " + e.getMessage());
        }
    }

    private static void testComplexExport() {
        List<SalesRecord> records = createSalesRecords();

        try (OutputStream os = new FileOutputStream("sales_report.xlsx")) {
            ExcelUtil.exportToExcel(records, "sales reports", SalesRecord.class, os);
            System.out.println("success: sales_report.xlsx");
        } catch (Exception e) {
            System.err.println("fail: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private static List<User> createTestUsers() {
        List<User> users = new ArrayList<>();

        users.add(new User(1L, "Andy", 28, "male",
                "andy@example.com", new Date(), "Dept1"));
        users.add(new User(2L, "Bob", 32, "female",
                "bob@example.com", new Date(), "Tech Department"));
        users.add(new User(3L, "Cindy", 25, "female",
                "cindy@example.com", new Date(), "Product Department"));
        users.add(new User(4L, "Sam", 35, "female",
                "sam@example.com", new Date(), "Marketing Department"));
        users.add(new User(5L, "Frank", 29, "male",
                "frank@example.com", new Date(), "Tech Department"));

        return users;
    }

    private static List<SalesRecord> createSalesRecords() {
        List<SalesRecord> records = new ArrayList<>();

        records.add(new SalesRecord("Toronto Branch", "Q1", "Product A", 150000.0, 12500.0));
        records.add(new SalesRecord("Toronto Branch", "Q1", "Product B", 230000.0, 19166.67));
        records.add(new SalesRecord("Toronto Branch", "Q2", "Product A", 180000.0, 15000.0));
        records.add(new SalesRecord("Ottawa Branch", "Q1", "Product A", 200000.0, 16666.67));
        records.add(new SalesRecord("Ottawa Branch", "Q1", "Product C", 120000.0, 10000.0));
        records.add(new SalesRecord("Ottawa Branch", "Q2", "Product B", 250000.0, 20833.33));

        return records;
    }

    // ========== Test Entity Classes ==========

    /**
     * User entity - demonstrates basic annotation functionality
     */
    public static class User {

        @Excel(name = "User ID", sort = 1, width = 10,
                headerColor = IndexedColors.WHITE,
                backgroundColor = IndexedColors.BLUE)
        private Long id;

        @Excel(name = "Name", sort = 2, width = 15)
        private String name;

        @Excel(name = "Age", sort = 3, width = 8,
                align = HorizontalAlignment.CENTER)
        private Integer age;

        @Excel(name = "Gender", sort = 4, width = 8,
                readConverterExp = "male=Male,female=Female",
                align = HorizontalAlignment.CENTER)
        private String gender;

        @Excel(name = "Email", sort = 5, width = 25)
        private String email;

        @Excel(name = "Registration Time", sort = 6, width = 20,
                dateFormat = "yyyy-MM-dd HH:mm",
                align = HorizontalAlignment.CENTER)
        private Date registerDate;

        @Excel(name = "Department", sort = 7, width = 15,
                needMerge = true,
                align = HorizontalAlignment.CENTER)
        private String department;

        // Constructors and getters/setters
        public User() {}

        public User(Long id, String name, Integer age, String gender,
                    String email, Date registerDate, String department) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.gender = gender;
            this.email = email;
            this.registerDate = registerDate;
            this.department = department;
        }

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public Date getRegisterDate() { return registerDate; }
        public void setRegisterDate(Date registerDate) { this.registerDate = registerDate; }
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
    }

    /**
     * Sales record - demonstrates advanced features
     */
    public static class SalesRecord {

        @Excel(name = "Branch", sort = 1, width = 15,
                needMerge = true,
                headerColor = IndexedColors.WHITE,
                backgroundColor = IndexedColors.LIGHT_BLUE)
        private String branch;

        @Excel(name = "Quarter", sort = 2, width = 8,
                align = HorizontalAlignment.CENTER)
        private String quarter;

        @Excel(name = "Product", sort = 3, width = 12)
        private String product;

        @Excel(name = "Sales Amount", sort = 4, width = 15,
                align = HorizontalAlignment.RIGHT,
                isStatistics = true,
                scale = 2,
                roundingMode = BigDecimal.ROUND_HALF_UP,
                suffix = " RMB",
                handler = CurrencyFormatter.class)
        private Double amount;

        @Excel(name = "Monthly Average Sales", sort = 5, width = 15,
                align = HorizontalAlignment.RIGHT,
                scale = 2,
                suffix = " RMB/month")
        private Double avgMonthly;

        // Constructors and getters/setters
        public SalesRecord() {}

        public SalesRecord(String branch, String quarter, String product,
                           Double amount, Double avgMonthly) {
            this.branch = branch;
            this.quarter = quarter;
            this.product = product;
            this.amount = amount;
            this.avgMonthly = avgMonthly;
        }

        // Getters and Setters
        public String getBranch() { return branch; }
        public void setBranch(String branch) { this.branch = branch; }
        public String getQuarter() { return quarter; }
        public void setQuarter(String quarter) { this.quarter = quarter; }
        public String getProduct() { return product; }
        public void setProduct(String product) { this.product = product; }
        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }
        public Double getAvgMonthly() { return avgMonthly; }
        public void setAvgMonthly(Double avgMonthly) { this.avgMonthly = avgMonthly; }
    }

    /**
     * Custom formatter - currency formatting
     */
    public static class CurrencyFormatter implements ExcelHandlerAdapter {
        @Override
        public Object format(Object value, String[] args, Cell cell, Workbook wb) {
            if (value == null) return "¥0.00";

            try {
                double amount = ((Number) value).doubleValue();
                return String.format("¥%.2f", amount);
            } catch (Exception e) {
                return value;
            }
        }
    }

    /**
     * Nested object example
     */
    public static class Employee {
        @Excel(name = "Employee Name", sort = 1, width = 15)
        private String name;

        @Excel(name = "Department Name", sort = 2, width = 15, targetAttr = "name")
        private Department department;

        @Excel(name = "Department Code", sort = 3, width = 10, targetAttr = "code")
        private Department dept;

        public Employee() {}

        public Employee(String name, Department department) {
            this.name = name;
            this.department = department;
            this.dept = department;
        }

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Department getDepartment() { return department; }
        public void setDepartment(Department department) {
            this.department = department;
            this.dept = department;
        }
    }

    public static class Department {
        private String code;
        private String name;

        public Department(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}
