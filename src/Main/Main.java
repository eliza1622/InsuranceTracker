package Main;

import config.config;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Date;
import java.util.HashMap;

public class Main {

    private static HashMap<String, String> userPasswords = new HashMap<>();

    public static void viewUsers() {
        String query = "SELECT * FROM tbl_user";
        String[] headers = {"User ID", "Name", "Birthdate", "Address", "Contact", "Role", "Status"};
        String[] columns = {"user_id", "user_name", "user_birthdate", "user_address", "user_contact", "user_role", "user_status"};
        config conf = new config();
        conf.viewRecords(query, headers, columns);
    }

    public static boolean usernameExists(String username) {
        config conf = new config();
        Object[] row = conf.getSingleRecord("SELECT user_id FROM tbl_user WHERE user_name = ?", username);
        return row != null;
    }

    public static void addUser() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        System.out.println("\n=== SIGN UP (CREATE ACCOUNT) ===");
        System.out.print("Enter Username: ");
        String username = sc.nextLine().trim();

        if (username.isEmpty()) {
            System.out.println("Username cannot be empty.");
            return;
        }

        if (usernameExists(username)) {
            System.out.println("Username already exists. Try a different username.");
            return;
        }

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

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

        userPasswords.put(username, password);

        String insertQuery = "INSERT INTO tbl_user (user_name, user_birthdate, user_address, user_contact, user_role, user_status, user_password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        conf.addRecord(insertQuery, username, birth, address, contact, role, status, password);

        addActivityLog(username, "Created new account");
        System.out.println("Account created successfully!");
    }

    public static void deleteUser() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        System.out.println("\n=== DELETE USER ===");
        System.out.print("Enter User ID to delete: ");
        String userId = sc.nextLine();

        String deleteQuery = "DELETE FROM tbl_user WHERE user_id = ?";
        conf.deleteRecord(deleteQuery, userId);

        userPasswords.values().removeIf(v -> true);

        addActivityLog(userId, "Deleted user record");
        System.out.println("User deleted successfully!");
    }

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

        System.out.print("Password (leave blank to keep current): ");
        String newPassword = sc.nextLine();

        if (!newPassword.isEmpty()) {
            userPasswords.put(userName, newPassword);
            String pwdUpdate = "UPDATE tbl_user SET user_password = ? WHERE user_id = ?";
            conf.updateRecord(pwdUpdate, newPassword, userId);
        }

        String updateQuery = "UPDATE tbl_user SET user_name = ?, user_address = ?, user_contact = ?, user_status = ? WHERE user_id = ?";
        conf.updateRecord(updateQuery, userName, newAddress, newContact, newStatus, userId);

        addActivityLog(userId, "Updated user record");
        System.out.println("User updated successfully!");
    }

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
            sc.nextLine();

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

    public static void addActivityLog(String userId, String action) {
        config conf = new config();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String timestamp = sdf.format(new Date());

        String sql = "INSERT INTO tbl_activity_log (user_id, action, timestamp) VALUES (?, ?, ?)";
        conf.addRecord(sql, userId, action, timestamp);
    }

    public static void viewActivityLogs() {
        String query = "SELECT * FROM tbl_activity_log";
        String[] headers = {"Log ID", "User ID", "Action", "Timestamp"};
        String[] columns = {"log_id", "user_id", "action", "timestamp"};
        config conf = new config();
        conf.viewRecords(query, headers, columns);
    }

    public static void viewInfo() {
        String query = "SELECT * FROM tbl_info";
        String[] headers = {"Info ID", "User ID", "Beneficiary Name", "Insurance Type", "Coverage Amount"};
        String[] columns = {"info_id", "user_id", "beneficiary_name", "insurance_type", "coverage_amount"};
        config conf = new config();
        conf.viewRecords(query, headers, columns);
    }

    public static void addInfo() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        System.out.println("\n=== ADD INSURANCE INFO ===");
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
            sc.nextLine();

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

    public static boolean login() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        System.out.println("\n=== LOGIN ===");
        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        Object[] user = conf.getSingleRecord("SELECT * FROM tbl_user WHERE user_name = ? AND user_password = ?", username, password);

        if (user != null) {
            System.out.println("Login successful! Welcome, " + username + ".");
            addActivityLog(username, "Logged in");
            return true;
        } else {
            System.out.println("Invalid username or password.");
            return false;
        }
    }

    public static void mainMenu() {
        Scanner sc = new Scanner(System.in);
        int choice;

        while (true) {
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1. Add User");
            System.out.println("2. Manage User Information");
            System.out.println("3. View Activity Logs");
            System.out.println("4. Add Insurance Info");
            System.out.println("5. Manage Insurance Info");
            System.out.println("6. Logout");
            System.out.print("Enter Choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Invalid input. Numbers only.");
                sc.nextLine();
                continue;
            }

            choice = sc.nextInt();
            sc.nextLine();

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
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== WELCOME =====");
            System.out.println("1. Login");
            System.out.println("2. Sign Up");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            String raw = sc.nextLine();
            if (raw.isEmpty()) {
                System.out.println("Please enter a choice.");
                continue;
            }

            int choice;
            try {
                choice = Integer.parseInt(raw);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Numbers only.");
                continue;
            }

            switch (choice) {
                case 1:
                    if (login()) {
                        mainMenu();
                    }
                    break;

                case 2:
                    addUser();
                    break;

                case 3:
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
