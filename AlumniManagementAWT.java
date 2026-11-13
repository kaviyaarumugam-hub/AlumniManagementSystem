import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AlumniManagementAWT extends Frame {

    // ALUMNI DATA
    class Alumni {
        String name, dept, regNo, batch, passYear;
        String position, company, workYear;
        String email, phone, password;

        Alumni(String[] d) {
            name = d[0];
            dept = d[1];
            regNo = d[2];
            batch = d[3];
            passYear = d[4];
            position = d[5];
            company = d[6];
            workYear = d[7];
            email = d[8];
            phone = d[9];
            password = d[10];
        }
    }

    ArrayList<Alumni> alumniList = new ArrayList<>();
    ArrayList<String> announcements = new ArrayList<>();
    ArrayList<String> notifications = new ArrayList<>();
    ArrayList<String> messages = new ArrayList<>();
    ArrayList<String> updates = new ArrayList<>();

    CardLayout cards = new CardLayout();
    Panel root = new Panel(cards);

    TextField loginEmail;
    TextField loginPassword;
    TextArea output;

    String role = "";
    String currentUser = "";

    public AlumniManagementAWT() {
        setTitle("Alumni Management System");
        setSize(900, 700);
        setLocationRelativeTo(null);

        root.add(welcomePage(), "welcome");
        root.add(loginPage(), "login");
        root.add(signupPage(), "signup");
        root.add(dashboardPage(), "dashboard");

        add(root);
        cards.show(root, "welcome");

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    // 1. WELCOME PAGE
    Panel welcomePage() {
        Panel p = new Panel(new GridLayout(6, 1, 15, 15));

        Label title = new Label(
            "ALUMNI MANAGEMENT SYSTEM", Label.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));

        Label sub = new Label(
            "Welcome to Our College Alumni Network",
            Label.CENTER);

        Button signUp = new Button("SIGN UP");
        Button logIn = new Button("LOG IN");

        signUp.setFont(new Font("Arial", Font.BOLD, 18));
        logIn.setFont(new Font("Arial", Font.BOLD, 18));

        p.add(title);
        p.add(sub);
        p.add(new Label("New Alumni? Create Your Account",
                        Label.CENTER));
        p.add(signUp);
        p.add(new Label("Already Registered? Login",
                        Label.CENTER));
        p.add(logIn);

        signUp.addActionListener(e -> cards.show(root, "signup"));
        logIn.addActionListener(e -> cards.show(root, "login"));

        return p;
    }

    // 2. LOGIN PAGE
    Panel loginPage() {
        Panel p = new Panel(new GridLayout(7, 1, 10, 10));

        Label title = new Label("ALUMNI LOGIN", Label.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 25));

        loginEmail = new TextField();
        loginPassword = new TextField();
        loginPassword.setEchoChar('*');

        Button login = new Button("LOG IN");
        Button back = new Button("BACK");

        p.add(title);
        p.add(new Label("Email ID"));
        p.add(loginEmail);
        p.add(new Label("Password"));
        p.add(loginPassword);
        p.add(login);
        p.add(back);

        login.addActionListener(e -> loginUser());
        back.addActionListener(e -> cards.show(root, "welcome"));

        return p;
    }

    // 3. ALUMNI SIGN UP PAGE
    Panel signupPage() {
        Panel p = new Panel(new GridLayout(13, 1, 5, 5));

        Label title = new Label(
            "ALUMNI REGISTRATION", Label.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        TextField[] f = new TextField[11];

        String[] labels = {
            "Full Name",
            "Department",
            "Register Number",
            "Batch (Example: 2022-2026)",
            "Year of Passing",
            "Current Position",
            "Company Name",
            "Current Working Year",
            "Email ID",
            "Phone Number",
            "Password"
        };

        p.add(title);

        for (int i = 0; i < labels.length; i++) {
            Panel row = new Panel(new BorderLayout());
            row.add(new Label(labels[i]), BorderLayout.NORTH);

            f[i] = new TextField();
            if (i == 10) {
                f[i].setEchoChar('*');
            }

            row.add(f[i], BorderLayout.CENTER);
            p.add(row);
        }

        Panel buttons = new Panel();
        Button register = new Button("REGISTER");
        Button back = new Button("BACK");

        buttons.add(register);
        buttons.add(back);
        p.add(buttons);

        register.addActionListener(e -> {
            String[] d = new String[11];

            for (int i = 0; i < 11; i++) {
                d[i] = f[i].getText().trim();
            }

            if (isEmpty(d)) {
                showMessage("Please Fill All Details!");
                return;
            }

            for (Alumni a : alumniList) {
                if (a.email.equalsIgnoreCase(d[8]) ||
                    a.regNo.equalsIgnoreCase(d[2])) {
                    showMessage(
                        "Email or Register Number Already Exists!");
                    return;
                }
            }

            alumniList.add(new Alumni(d));
            showMessage("Alumni Registration Successful!");

            for (TextField field : f) {
                field.setText("");
            }

            cards.show(root, "login");
        });

        back.addActionListener(e -> cards.show(root, "welcome"));
        return p;
    }

    boolean isEmpty(String[] d) {
        if (d == null) {
            return true;
        }

        for (String s : d) {
            if (s == null || s.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // 4. LOGIN VALIDATION
    void loginUser() {
        String email = loginEmail.getText().trim();
        String pass = loginPassword.getText();

        if (email.equalsIgnoreCase("admin") &&
            pass.equals("admin123")) {
            role = "Admin";
            currentUser = "admin";
            showDashboard();
            return;
        }

        for (Alumni a : alumniList) {
            if (a.email.equalsIgnoreCase(email) &&
                a.password.equals(pass)) {
                role = "Alumni";
                currentUser = a.email;
                showDashboard();
                return;
            }
        }

        showMessage("Invalid Email or Password!");
    }

    // 5. DASHBOARD PAGE
    Panel dashboardPage() {
        Panel p = new Panel(new BorderLayout(10, 10));

        Label heading = new Label(
            "ALUMNI MANAGEMENT DASHBOARD", Label.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 25));

        output = new TextArea();
        output.setEditable(false);

        p.add(heading, BorderLayout.NORTH);
        p.add(output, BorderLayout.CENTER);

        return p;
    }

    // 6. SHOW DASHBOARD
    void showDashboard() {
        Panel p = (Panel) root.getComponent(3);

        Panel menu = new Panel(new GridLayout(0, 3, 5, 5));

        if (role.equals("Admin")) {
            addButton(menu, "Dashboard", this::dashboardDetails);
            addButton(menu, "Add Alumni", this::addAlumni);
            addButton(menu, "View Alumni", this::viewAlumni);
            addButton(menu, "Update Alumni", this::updateAlumni);
            addButton(menu, "Delete Alumni", this::deleteAlumni);
            addButton(menu, "Search Alumni", this::searchAlumni);

            addButton(menu, "Add Announcement", this::addAnnouncement);
            addButton(menu, "View Announcements", this::viewAnnouncements);
            addButton(menu, "Delete Announcement", this::deleteAnnouncement);

            addButton(menu, "Send Message", this::sendMessage);
            addButton(menu, "Share College Updates", this::shareUpdate);
            addButton(menu, "View Messages", this::viewMessages);
        } else {
            addButton(menu, "Dashboard", this::dashboardDetails);
            addButton(menu, "My Profile", this::myProfile);
            addButton(menu, "Update My Profile", this::updateMyProfile);
            addButton(menu, "View Alumni", this::viewAlumni);
            addButton(menu, "Search Alumni", this::searchAlumni);

            addButton(menu, "View Announcements", this::viewAnnouncements);
            addButton(menu, "View Notifications", this::viewNotifications);
            addButton(menu, "View College Updates", this::viewUpdates);

            addButton(menu, "Send Message", this::sendMessage);
            addButton(menu, "View Messages", this::viewMessages);
        }

        addButton(menu, "Contact Alumni", this::contactAlumni);
        addButton(menu, "Logout", this::logout);

        p.removeAll();

        Label heading = new Label(
            "ALUMNI MANAGEMENT DASHBOARD", Label.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 24));

        output = new TextArea();
        output.setEditable(false);

        p.add(heading, BorderLayout.NORTH);
        p.add(output, BorderLayout.CENTER);
        p.add(menu, BorderLayout.SOUTH);

        dashboardDetails();
        p.validate();
        p.repaint();

        cards.show(root, "dashboard");
    }

    void addButton(Panel p, String name, Runnable action) {
        Button b = new Button(name);
        p.add(b);
        b.addActionListener(e -> action.run());
    }

    // 7. DASHBOARD DETAILS
    void dashboardDetails() {
        output.setText(
            "WELCOME " + currentUser + "\n\n" +
            "User Type: " + role + "\n\n" +
            "Total Alumni: " + alumniList.size() + "\n" +
            "Total Announcements: " + announcements.size() + "\n" +
            "Total College Updates: " + updates.size() + "\n\n" +
            "Welcome to the College Alumni Network!"
        );

        if (role.equals("Alumni")) {
            int count = 0;

            for (String n : notifications) {
                if (n.startsWith("ALL:") ||
                    n.startsWith(currentUser + ":")) {
                    count++;
                }
            }

            output.append("\nYour Notifications: " + count);
        }
    }

    // 8. INPUT DIALOG
    String[] ask(String title, String... labels) {
        Dialog d = new Dialog(this, title, true);
        d.setLayout(new GridLayout(labels.length + 1, 2, 5, 5));

        TextField[] fields = new TextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            d.add(new Label(labels[i]));
            fields[i] = new TextField();
            d.add(fields[i]);
        }

        final String[][] result = {null};

        Button save = new Button("SAVE");
        Button cancel = new Button("CANCEL");

        d.add(save);
        d.add(cancel);

        save.addActionListener(e -> {
            result[0] = new String[fields.length];

            for (int i = 0; i < fields.length; i++) {
                result[0][i] = fields[i].getText().trim();
            }

            d.dispose();
        });

        cancel.addActionListener(e -> d.dispose());

        d.setSize(450, Math.max(250, labels.length * 55));
        d.setLocationRelativeTo(this);
        d.setVisible(true);

        return result[0];
    }

    void showMessage(String s) {
        Dialog d = new Dialog(this, "Message", true);
        d.setLayout(new FlowLayout());

        d.add(new Label(s));

        Button ok = new Button("OK");
        ok.addActionListener(e -> d.dispose());
        d.add(ok);

        d.setSize(350, 130);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }

    // 9. ADD ALUMNI
    void addAlumni() {
        String[] d = ask("Add Alumni",
            "Name", "Department", "Register Number",
            "Batch", "Pass Out Year", "Position",
            "Company", "Working Year", "Email",
            "Phone", "Password");

        if (isEmpty(d)) return;

        for (Alumni a : alumniList) {
            if (a.email.equalsIgnoreCase(d[8]) ||
                a.regNo.equalsIgnoreCase(d[2])) {
                showMessage("Duplicate Email or Register Number!");
                return;
            }
        }

        alumniList.add(new Alumni(d));
        showMessage("Alumni Added Successfully!");
    }

    // 10. VIEW ALUMNI
    void viewAlumni() {
        output.setText("REGISTERED ALUMNI DETAILS\n\n");

        for (Alumni a : alumniList) {
            output.append("Name: " + a.name + "\n");
            output.append("Department: " + a.dept + "\n");
            output.append("Register Number: " + a.regNo + "\n");
            output.append("Batch: " + a.batch + "\n");
            output.append("Pass Out Year: " + a.passYear + "\n");
            output.append("Position: " + a.position + "\n");
            output.append("Company: " + a.company + "\n");
            output.append("Working Year: " + a.workYear + "\n");
            output.append("Email: " + a.email + "\n");
            output.append("Phone: " + a.phone + "\n");
            output.append("--------------------------------\n");
        }

        if (alumniList.isEmpty()) {
            output.append("No Alumni Records Found!");
        }
    }

    // 11. UPDATE ALUMNI
    void updateAlumni() {
        String[] s = ask("Update Alumni", "Enter Alumni Email");
        if (isEmpty(s)) return;

        for (Alumni a : alumniList) {
            if (a.email.equalsIgnoreCase(s[0])) {
                String[] n = ask("Update Details",
                    "Name", "Department", "Register Number",
                    "Batch", "Pass Out Year", "Position",
                    "Company", "Working Year", "Email", "Phone");

                if (isEmpty(n)) return;

                for (Alumni other : alumniList) {
                    if (other != a &&
                        (other.email.equalsIgnoreCase(n[8]) ||
                         other.regNo.equalsIgnoreCase(n[2]))) {
                        showMessage(
                            "Email or Register Number Already Exists!");
                        return;
                    }
                }

                a.name = n[0];
                a.dept = n[1];
                a.regNo = n[2];
                a.batch = n[3];
                a.passYear = n[4];
                a.position = n[5];
                a.company = n[6];
                a.workYear = n[7];
                a.email = n[8];
                a.phone = n[9];

                showMessage("Alumni Updated Successfully!");
                return;
            }
        }

        showMessage("Alumni Not Found!");
    }

    // 12. DELETE ALUMNI
    void deleteAlumni() {
        String[] s = ask("Delete Alumni", "Enter Alumni Email");
        if (isEmpty(s)) return;

        for (int i = 0; i < alumniList.size(); i++) {
            if (alumniList.get(i).email.equalsIgnoreCase(s[0])) {
                alumniList.remove(i);
                showMessage("Alumni Deleted Successfully!");
                return;
            }
        }

        showMessage("Alumni Not Found!");
    }

    // 13. SEARCH ALUMNI
    void searchAlumni() {
        String[] s = ask("Search Alumni",
            "Enter Name / Batch / Department");
        if (isEmpty(s)) return;

        output.setText("SEARCH RESULTS\n\n");
        boolean found = false;

        for (Alumni a : alumniList) {
            if (a.name.equalsIgnoreCase(s[0]) ||
                a.batch.equalsIgnoreCase(s[0]) ||
                a.dept.equalsIgnoreCase(s[0])) {
                output.append("Name: " + a.name + "\n");
                output.append("Batch: " + a.batch + "\n");
                output.append("Department: " + a.dept + "\n");
                output.append("Position: " + a.position + "\n");
                output.append("Company: " + a.company + "\n");
                output.append("Email: " + a.email + "\n\n");
                found = true;
            }
        }

        if (!found) {
            output.setText("No Alumni Found!");
        }
    }

    // 14. ADD ANNOUNCEMENT
    void addAnnouncement() {
        String[] s = ask("Add Announcement",
            "Announcement Type",
            "Title",
            "Date",
            "Description");

        if (isEmpty(s)) return;

        String announcement =
            "Title: " + s[1] + "\n" +
            "Category: " + s[0] + "\n" +
            "Date: " + s[2] + "\n" +
            "Description: " + s[3];

        announcements.add(announcement);
        notifications.add("ALL: New Announcement - " + s[1]);

        showMessage(
            "Announcement Added Successfully!\n" +
            "All Alumni Notified!");
    }

    // 15. VIEW ANNOUNCEMENTS
    void viewAnnouncements() {
        output.setText("COLLEGE ANNOUNCEMENTS\n\n");

        for (String a : announcements) {
            output.append(a + "\n");
            output.append("--------------------------------\n\n");
        }

        if (announcements.isEmpty()) {
            output.append("No Announcements Available!");
        }
    }

    // 16. DELETE ANNOUNCEMENT
    void deleteAnnouncement() {
        String[] s = ask("Delete Announcement",
            "Enter Announcement Title");
        if (isEmpty(s)) return;

        for (int i = 0; i < announcements.size(); i++) {
            if (announcements.get(i).startsWith(
                "Title: " + s[0] + "\n")) {
                announcements.remove(i);
                showMessage("Announcement Deleted Successfully!");
                return;
            }
        }

        showMessage("Announcement Not Found!");
    }

    // 17. VIEW NOTIFICATIONS
    void viewNotifications() {
        output.setText("YOUR NOTIFICATIONS\n\n");
        boolean found = false;

        for (String n : notifications) {
            if (n.startsWith("ALL:") ||
                n.startsWith(currentUser + ":")) {
                output.append(
                    n.substring(n.indexOf(":") + 1) + "\n\n");
                found = true;
            }
        }

        if (!found) {
            output.append("No Notifications!");
        }
    }

    // 18. SEND MESSAGE
    void sendMessage() {
        String[] s = ask("Send Message",
            "Receiver Email", "Message");
        if (isEmpty(s)) return;

        boolean found = s[0].equalsIgnoreCase("admin");

        for (Alumni a : alumniList) {
            if (a.email.equalsIgnoreCase(s[0])) {
                found = true;
                break;
            }
        }

        if (!found) {
            showMessage("Receiver Not Found!");
            return;
        }

        messages.add("From: " + currentUser +
            " | To: " + s[0] + " | Message: " + s[1]);

        notifications.add(
            s[0] + ": New Message from " + currentUser);

        showMessage("Message Sent Successfully!");
    }

    // 19. VIEW MESSAGES
    void viewMessages() {
        output.setText("YOUR MESSAGES\n\n");
        boolean found = false;

        for (String m : messages) {
            if (m.contains("To: " + currentUser) ||
                m.contains("From: " + currentUser)) {
                output.append(m + "\n\n");
                found = true;
            }
        }

        if (!found) {
            output.append("No Messages Found!");
        }
    }

    // 20. SHARE COLLEGE UPDATES
    void shareUpdate() {
        String[] s = ask("Share College Update",
            "Update Title", "Update Details");
        if (isEmpty(s)) return;

        updates.add("Title: " + s[0] +
            "\nDetails: " + s[1]);

        notifications.add("ALL: College Update: " + s[0]);

        showMessage("College Update Shared Successfully!");
    }

    // 21. VIEW COLLEGE UPDATES
    void viewUpdates() {
        output.setText("COLLEGE UPDATES\n\n");

        for (String u : updates) {
            output.append(u + "\n");
            output.append("-------------------------------\n\n");
        }

        if (updates.isEmpty()) {
            output.append("No Updates Available!");
        }
    }

    // 22. MY PROFILE
    void myProfile() {
        for (Alumni a : alumniList) {
            if (a.email.equalsIgnoreCase(currentUser)) {
                output.setText("MY ALUMNI PROFILE\n\n");
                output.append("Name: " + a.name + "\n");
                output.append("Department: " + a.dept + "\n");
                output.append("Register Number: " + a.regNo + "\n");
                output.append("Batch: " + a.batch + "\n");
                output.append("Pass Out Year: " + a.passYear + "\n");
                output.append("Position: " + a.position + "\n");
                output.append("Company: " + a.company + "\n");
                output.append("Working Year: " + a.workYear + "\n");
                output.append("Email: " + a.email + "\n");
                output.append("Phone: " + a.phone);
                return;
            }
        }
    }

    // 23. UPDATE OWN PROFILE
    void updateMyProfile() {
        for (Alumni a : alumniList) {
            if (a.email.equalsIgnoreCase(currentUser)) {
                String[] n = ask("Update My Profile",
                    "Name", "Department", "Batch",
                    "Pass Out Year", "Position",
                    "Company", "Working Year", "Phone");

                if (isEmpty(n)) return;

                a.name = n[0];
                a.dept = n[1];
                a.batch = n[2];
                a.passYear = n[3];
                a.position = n[4];
                a.company = n[5];
                a.workYear = n[6];
                a.phone = n[7];

                showMessage("Profile Updated Successfully!");
                return;
            }
        }
    }

    // 24. CONTACT ALUMNI
    void contactAlumni() {
        String[] s = ask("Contact Alumni", "Enter Alumni Name");
        if (isEmpty(s)) return;

        for (Alumni a : alumniList) {
            if (a.name.equalsIgnoreCase(s[0])) {
                output.setText("ALUMNI CONTACT DETAILS\n\n");
                output.append("Name: " + a.name + "\n");
                output.append("Department: " + a.dept + "\n");
                output.append("Batch: " + a.batch + "\n");
                output.append("Email: " + a.email + "\n");
                output.append("Phone: " + a.phone);
                return;
            }
        }

        showMessage("Alumni Not Found!");
    }

    // 25. LOGOUT
    void logout() {
        Dialog d = new Dialog(this, "Logout", true);
        d.setLayout(new FlowLayout());

        d.add(new Label("Are you sure you want to logout?"));

        Button yes = new Button("YES");
        Button no = new Button("NO");

        d.add(yes);
        d.add(no);

        yes.addActionListener(e -> {
            role = "";
            currentUser = "";
            loginEmail.setText("");
            loginPassword.setText("");
            d.dispose();
            cards.show(root, "welcome");
        });

        no.addActionListener(e -> d.dispose());

        d.setSize(350, 130);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }

    // MAIN METHOD
    public static void main(String[] args) {
        new AlumniManagementAWT();
    }
}
