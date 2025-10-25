package Main;

import config.config;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Date;
import java.util.HashMap;

public class Main {

    // === TEMPORARY PASSWORD STORAGE (IN-SYSTEM ONLY) ===
    private static HashMap<String, String> userPasswords = new HashMap<>();

    // === VIEW USERS ===
    public static void viewUsers() {
        String query = "SELECT * FROM tbl_user";
        String[] headers = {"User ID", "Name", "Birthdate", "Address", "Contact", "Role", "Status"};
        String[] columns = {"user_id", "user_name", "user_birthdate", "user_address", "user_contact", "user_role", "user_status"};
        config conf = new config();
        conf.viewRecords(query, headers, columns);
    }

    // === ADD USER ===
    public static void addUser() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        System.out.println("\n=== ADD USER ===");
        System.out.print("Enter User Name: ");
        String userName = sc.nextLine();

        System.out.print("Enter Birthdate (YYYY-MM-DD): ");
        String birth = sc.nextLine();

        System.out.print("Enter Address: ");
        String address = sc.nextLine();

        System.out.print("Enter Contact: ");
        String contact = sc.nextLine();

        System.out.print("Enter Role: ");
        String role = sc.nextLine();

        System.out.print("Enter Status: ");
        String status = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        // Save password only in system memory
        userPasswords.put(userName, password);

        String insertQuery = "INSERT INTO tbl_user (user_name, user_birthdate, user_address, user_contact, user_role, user_status) VALUES (?, ?, ?, ?, ?, ?)";
        conf.addRecord(insertQuery, userName, birth, address, contact, role, status);

        addActivityLog(userName, "Added new user record");
        System.out.println("User added successfully!");
    }

    // === DELETE USER ===
    public static void deleteUser() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        System.out.println("\n=== DELETE USER ===");
        System.out.print("Enter User ID to delete: ");
        String userId = sc.nextLine();

        String deleteQuery = "DELETE FROM tbl_user WHERE user_id = ?";
        conf.deleteRecord(deleteQuery, userId);

        // Remove password from memory (if exists)
        userPasswords.remove(userId);

        addActivityLog(userId, "Deleted user record");
        System.out.println("User deleted successfully!");
    }

    // === UPDATE USER ===
    public static void updateUser() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        System.out.println("\n=== UPDATE USER ===");
        System.out.print("Enter User ID to update: ");
        String userId = sc.nextLine();

        System.out.print("Enter new User Name: ");
        String userName = sc.nextLine();

        System.out.print("Enter new Address: ");
        String newAddress = sc.nextLine();

        System.out.print("Enter new Contact: ");
        String newContact = sc.nextLine();

        System.out.print("Enter new Status: ");
        String newStatus = sc.nextLine();

        System.out.print("Password: ");
        String newPassword = sc.nextLine();

        // Update password in memory only
        userPasswords.put(userName, newPassword);

        String updateQuery = "UPDATE tbl_user SET user_name = ?, user_address = ?, user_contact = ?, user_status = ? WHERE user_id = ?";
        conf.updateRecord(updateQuery, userName, newAddress, newContact, newStatus, userId);

        addActivityLog(userId, "Updated user record");
        System.out.println("User updated successfully!");
    }

    // === USER INFORMATION MANAGEMENT ===
    public static void manageUserInfo() {
        Scanner sc = new Scanner(System.in);
        int choice;

        while (true) {
            System.out.println("\n=== USER INFORMATION MANAGEMENT ===");
            System.out.println("1. View Users");
            System.out.println("2. Update User");
            System.out.println("3. Delete User");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter Choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Invalid input. Numbers only.");
                sc.nextLine();
                continue;
            }

            choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {
                case 1:
                    viewUsers();
                    break;
                case 2:
                    updateUser();
                    break;
                case 3:
                    deleteUser();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // === ACTIVITY LOG ===
    public static void addActivityLog(String userId, String action) {
        config conf = new config();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String timestamp = sdf.format(new Date());

        String sql = "INSERT INTO tbl_activity_log (user_id, action, timestamp) VALUES (?, ?, ?)";
        conf.addRecord(sql, userId, action, timestamp);
    }

    // === VIEW ACTIVITY LOGS ===
    public static void viewActivityLogs() {
        String query = "SELECT * FROM tbl_activity_log";
        String[] headers = {"Log ID", "User ID", "Action", "Timestamp"};
        String[] columns = {"log_id", "user_id", "action", "timestamp"};
        config conf = new config();
        conf.viewRecords(query, headers, columns);
    }

    // === VIEW INSURANCE INFO ===
    public static void viewInfo() {
        String query = "SELECT * FROM tbl_info";
        String[] headers = {"Info ID", "User ID", "Beneficiary Name", "Insurance Type", "Coverage Amount"};
        String[] columns = {"info_id", "user_id", "beneficiary_name", "insurance_type", "coverage_amount"};
        config conf = new config();
        conf.viewRecords(query, headers, columns);
    }

    // === ADD INSURANCE INFO ===
    public static void addInfo() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        System.out.println("\n=== ADD INSURANCE INFO ===");

        // Display users to pick from
        System.out.println("Select User from the list below:");
        viewUsers();
        System.out.print("Enter User ID: ");
        String userId = sc.nextLine();

        System.out.print("Enter Beneficiary Name: ");
        String beneficiaryName = sc.nextLine();

        System.out.print("Enter Insurance Type: ");
        String insuranceType = sc.nextLine();

        System.out.print("Enter Coverage Amount: ");
        String coverageAmount = sc.nextLine();

        String sql = "INSERT INTO tbl_info (user_id, beneficiary_name, insurance_type, coverage_amount) VALUES (?, ?, ?, ?)";
        conf.addRecord(sql, userId, beneficiaryName, insuranceType, coverageAmount);

        addActivityLog(userId, "Added insurance info");
        System.out.println("Insurance info added!");
    }

    // === DELETE INSURANCE INFO ===
    public static void deleteInfo() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        System.out.println("\n=== DELETE INSURANCE INFO ===");
        System.out.print("Enter Info ID to delete: ");
        String infoId = sc.nextLine();

        String deleteQuery = "DELETE FROM tbl_info WHERE info_id = ?";
        conf.deleteRecord(deleteQuery, infoId);

        addActivityLog("System", "Deleted insurance info record");
        System.out.println("Insurance info deleted successfully!");
    }

    // === UPDATE INSURANCE INFO ===
    public static void updateInfo() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        System.out.println("\n=== UPDATE INSURANCE INFO ===");
        System.out.print("Enter Info ID to update: ");
        String infoId = sc.nextLine();

        System.out.print("Enter new Insurance Type: ");
        String newType = sc.nextLine();

        System.out.print("Enter new Coverage Amount: ");
        String newCoverage = sc.nextLine();

        String sql = "UPDATE tbl_info SET insurance_type = ?, coverage_amount = ? WHERE info_id = ?";
        conf.updateRecord(sql, newType, newCoverage, infoId);

        addActivityLog("System", "Updated insurance info record");
        System.out.println("Insurance info updated!");
    }

    // === MANAGE INSURANCE INFO MENU ===
    public static void manageInsuranceInfo() {
        Scanner sc = new Scanner(System.in);
        int choice;

        while (true) {
            System.out.println("\n=== INSURANCE INFORMATION MANAGEMENT ===");
            System.out.println("1. View Info");
            System.out.println("2. Update Info");
            System.out.println("3. Delete Info");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Invalid input. Numbers only.");
                sc.nextLine();
                continue;
            }

            choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {
                case 1:
                    viewInfo();
                    break;
                case 2:
                    updateInfo();
                    break;
                case 3:
                    deleteInfo();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // === MAIN MENU ===
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        while (true) {
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1. Add User");
            System.out.println("2. Manage User Information");
            System.out.println("3. View Activity Logs");
            System.out.println("4. Add Insurance Info");
            System.out.println("5. Manage Insurance Info");
            System.out.println("6. Exit");
            System.out.print("Enter Choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Invalid input. Numbers only.");
                sc.nextLine();
                continue;
            }

            choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {
                case 1:
                    addUser();
                    break;
                case 2:
                    manageUserInfo();
                    break;
                case 3:
                    System.out.println("\n=== ACTIVITY LOGS ===");
                    viewActivityLogs();
                    break;
                case 4:
                    addInfo();
                    break;
                case 5:
                    manageInsuranceInfo();
                    break;
                case 6:
                    System.out.println("Exiting program...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
