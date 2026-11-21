import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class DashboardFrame extends JFrame {
    private static final Color NAVY = new Color(15, 30, 58);
    private static final Color BLUE = new Color(53, 103, 255);
    private static final Color BACKGROUND = new Color(245, 247, 252);
    private final CardLayout applicationCards = new CardLayout();
    private final JPanel root = new JPanel(applicationCards);
    private final CardLayout workspaceCards = new CardLayout();
    private final JPanel workspaceContent = new JPanel(workspaceCards);
    private JTextField usernameField, idField, titleField;
    private JPasswordField passwordField;
    private JTextArea descriptionArea, analysisArea;
    private DefaultTableModel recordsModel;
    private JLabel welcomeLabel, profileLabel, recordCountLabel;
    private final Map<String, String> userCredentials = new LinkedHashMap<>();
    private JLabel adminTotalLabel, adminDuplicateLabel, adminUserLabel;
    private DefaultTableModel adminRecordsModel;
    private String activeUser = "";
    private boolean activeAdmin;

    public DashboardFrame() {
        setTitle("InnovX | Idea Intelligence");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1080, 680));
        setContentPane(root);
        root.add(createLoginPage(), "login");
        root.add(createWorkspace(), "workspace");
        root.add(createAdminDashboard(), "admin");
        loadUserCredentials();
        applicationCards.show(root, "login");
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createLoginPage() {
        Color MAIN_BG = new Color(38, 47, 64);
        Color COMPONENT_BG = new Color(52, 65, 89);
        Color BUTTON_BG = new Color(24, 77, 133);
        Color TEXT_COLOR = new Color(220, 225, 235);

        JPanel page = new JPanel(new GridBagLayout());
        page.setBackground(new Color(180, 185, 195));

        JPanel shell = new JPanel(new BorderLayout());
        shell.setPreferredSize(new Dimension(580, 480));
        shell.setBackground(MAIN_BG);
        shell.setBorder(BorderFactory.createLineBorder(new Color(20, 25, 35), 2));

        // --- HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(MAIN_BG);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(70, 85, 110)),
            new EmptyBorder(20, 30, 20, 30)
        ));

        // Fixed: Logo AND Text side-by-side using FlowLayout
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        logoPanel.setBackground(MAIN_BG);
        
        File logoFile = new File("niet_logo.jpg");
        if (logoFile.exists()) {
            ImageIcon originalIcon = new ImageIcon("niet_logo.jpg");
            Image scaledImg = originalIcon.getImage().getScaledInstance(65, 45, Image.SCALE_SMOOTH);
            logoPanel.add(new JLabel(new ImageIcon(scaledImg)));
        } 
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(MAIN_BG);
        JLabel nietLabel = new JLabel("NIET Greater Noida");
        nietLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        nietLabel.setForeground(Color.LIGHT_GRAY);
        JLabel autoLabel = new JLabel("An Autonomous Institute");
        autoLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        autoLabel.setForeground(Color.GRAY);
        textPanel.add(nietLabel);
        textPanel.add(autoLabel);
        
        logoPanel.add(textPanel);

        JPanel brandPanel = new JPanel(new GridLayout(2, 1));
        brandPanel.setBackground(MAIN_BG);
        JLabel titleLabel = new JLabel("InnovX");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel subTitleLabel = new JLabel("Smart Innovation Cycle");
        subTitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subTitleLabel.setForeground(Color.LIGHT_GRAY);
        subTitleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        brandPanel.add(titleLabel); brandPanel.add(subTitleLabel);

        headerPanel.add(logoPanel, BorderLayout.WEST);
        headerPanel.add(brandPanel, BorderLayout.EAST);
        shell.add(headerPanel, BorderLayout.NORTH);

        // --- FORM ---
        JPanel formPanel = new JPanel(new GridLayout(7, 1, 0, 5));
        formPanel.setBackground(MAIN_BG);
        formPanel.setBorder(new EmptyBorder(25, 40, 30, 40));

        Font labelFont = new Font("SansSerif", Font.PLAIN, 16);
        Font inputFont = new Font("SansSerif", Font.PLAIN, 16);

        JLabel userLabel = new JLabel("Username / ID:");
        userLabel.setForeground(TEXT_COLOR); userLabel.setFont(labelFont);
        
        usernameField = new JTextField();
        styleDarkInput(usernameField, COMPONENT_BG, inputFont);

        JLabel roleLabel = new JLabel("Stakeholder Role:");
        roleLabel.setForeground(TEXT_COLOR); roleLabel.setFont(labelFont);
        
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Innovation Manager", "Idea Submitter", "Technical Reviewer", "System Administrator"});
        styleDarkInput(roleCombo, COMPONENT_BG, inputFont); // Fixed JComboBox width glitch

        JLabel passLabel = new JLabel("Security Pin / Password:");
        passLabel.setForeground(TEXT_COLOR); passLabel.setFont(labelFont);
        
        passwordField = new JPasswordField();
        styleDarkInput(passwordField, COMPONENT_BG, inputFont);

        formPanel.add(userLabel); formPanel.add(usernameField);
        formPanel.add(roleLabel); formPanel.add(roleCombo);
        formPanel.add(passLabel); formPanel.add(passwordField);
        shell.add(formPanel, BorderLayout.CENTER);

        // --- BUTTON ---
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(MAIN_BG);
        buttonPanel.setBorder(new EmptyBorder(0, 40, 40, 40));

        JButton loginButton = new JButton("Login to InnovX Workspace");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(BUTTON_BG);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(0, 45));

        loginButton.addActionListener(e -> authenticate(usernameField.getText().trim(), new String(passwordField.getPassword())));
        passwordField.addActionListener(e -> loginButton.doClick());

        buttonPanel.add(loginButton, BorderLayout.CENTER);
        shell.add(buttonPanel, BorderLayout.SOUTH);

        page.add(shell);
        return page;
    }

    private void styleDarkInput(JComponent comp, Color bg, Font font) {
        comp.setBackground(bg);
        comp.setForeground(Color.WHITE);
        comp.setFont(font);
        
        // Fix for the JComboBox width rendering issue: 
        // Do not apply EmptyBorder to dropdowns, only to text fields.
        boolean isCombo = comp instanceof JComboBox;
        
        if (!isCombo) {
            comp.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 85, 110), 1),
                new EmptyBorder(5, 10, 5, 10)
            ));
        } else {
            comp.setBorder(BorderFactory.createLineBorder(new Color(70, 85, 110), 1));
        }

        // Add FocusListener to highlight the field when clicked
        comp.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (!isCombo) {
                    comp.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(137, 170, 255), 2), // Bright Blue
                        new EmptyBorder(4, 9, 4, 9)
                    ));
                } else {
                    comp.setBorder(BorderFactory.createLineBorder(new Color(137, 170, 255), 2));
                }
                comp.setBackground(new Color(65, 80, 105)); // Lighten background
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (!isCombo) {
                    comp.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(70, 85, 110), 1),
                        new EmptyBorder(5, 10, 5, 10)
                    ));
                } else {
                    comp.setBorder(BorderFactory.createLineBorder(new Color(70, 85, 110), 1));
                }
                comp.setBackground(bg); // Reset background
            }
        });
    }

    private void authenticate(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) { JOptionPane.showMessageDialog(this, "Enter both a username and password.", "Missing details", JOptionPane.ERROR_MESSAGE); return; }
        if ("admin".equals(username)) {
            if ("admin123".equals(password)) openWorkspace(username, true);
            else JOptionPane.showMessageDialog(this, "Incorrect administrator password.", "Login failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String storedCredential = userCredentials.get(username);
        if (storedCredential == null) {
            try {
                userCredentials.put(username, hashPassword(password));
                FileManager.appendUserCredential("pastProjects.txt", username, userCredentials.get(username));
                JOptionPane.showMessageDialog(this, "New user account created. Welcome to InnovX!", "Registration complete", JOptionPane.INFORMATION_MESSAGE);
                openWorkspace(username, false);
            } catch (Exception error) { JOptionPane.showMessageDialog(this, "Could not create the user account: " + error.getMessage(), "Registration failed", JOptionPane.ERROR_MESSAGE); }
        } else if (verifyPassword(password, storedCredential)) openWorkspace(username, false);
        else JOptionPane.showMessageDialog(this, "That username already exists, but the password is incorrect.", "Login failed", JOptionPane.ERROR_MESSAGE);
    }

    private JPanel createWorkspace() {
        JPanel page = new JPanel(new BorderLayout()); page.setBackground(BACKGROUND);
        page.add(createSidebar(), BorderLayout.WEST);
        JPanel center = new JPanel(new BorderLayout()); center.setBackground(BACKGROUND);
        center.add(createTopbar(), BorderLayout.NORTH);
        workspaceContent.setBackground(BACKGROUND);
        workspaceContent.add(createOverviewPage(), "overview");
        workspaceContent.add(createSubmitPage(), "submit");
        workspaceContent.add(createRecordsPage(), "records");
        center.add(workspaceContent, BorderLayout.CENTER); page.add(center, BorderLayout.CENTER); return page;
    }

    private JPanel createSidebar() {
        JPanel side = new JPanel(); side.setBackground(NAVY); side.setPreferredSize(new Dimension(220, 0)); side.setBorder(new EmptyBorder(28, 18, 24, 18)); side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        JLabel brand = new JLabel("INNOVX"); brand.setFont(brand.getFont().deriveFont(Font.BOLD, 21f)); brand.setForeground(Color.WHITE); brand.setAlignmentX(Component.LEFT_ALIGNMENT); side.add(brand);
        JLabel product = textLabel("IDEA INTELLIGENCE", 10f, new Color(137, 170, 255)); product.setAlignmentX(Component.LEFT_ALIGNMENT); side.add(product); side.add(Box.createVerticalStrut(42));
        side.add(navButton("Overview", "overview")); side.add(Box.createVerticalStrut(8)); side.add(navButton("Submit an idea", "submit")); side.add(Box.createVerticalStrut(8)); side.add(navButton("Idea records", "records"));
        side.add(Box.createVerticalGlue());
        JButton switchProfile = new JButton("⇄  Switch profile"); styleNavButton(switchProfile); switchProfile.addActionListener(e -> switchProfile()); side.add(switchProfile);
        return side;
    }

    private JPanel createTopbar() {
        JPanel top = new JPanel(new BorderLayout()); top.setBackground(Color.WHITE); top.setBorder(new EmptyBorder(18, 28, 18, 28));
        welcomeLabel = new JLabel(); welcomeLabel.setForeground(NAVY); welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 18f)); top.add(welcomeLabel, BorderLayout.WEST);
        JButton profile = new JButton(); profileLabel = new JLabel(); profileLabel.setForeground(NAVY); profileLabel.setFont(profileLabel.getFont().deriveFont(Font.BOLD, 13f)); profile.setLayout(new BorderLayout(8, 0)); profile.add(profileLabel, BorderLayout.CENTER); profile.add(new JLabel("⌄"), BorderLayout.EAST); profile.setBackground(new Color(239, 243, 255)); profile.setBorder(new EmptyBorder(9, 12, 9, 12)); profile.addActionListener(e -> switchProfile()); top.add(profile, BorderLayout.EAST); return top;
    }

    private JPanel createOverviewPage() {
        JPanel page = pagePanel();
        page.add(pageTitle("Workspace overview", "Your submission activity and analysis queue at a glance."), BorderLayout.NORTH);
        JPanel cards = new JPanel(new GridLayout(1, 3, 16, 0)); cards.setOpaque(false); cards.setBorder(new EmptyBorder(26, 0, 0, 0));
        JPanel acceptedIdeas = metricPanel("0", "Accepted ideas", new Color(231, 238, 255));
        recordCountLabel = (JLabel) acceptedIdeas.getComponent(0);
        cards.add(acceptedIdeas); cards.add(metricPanel("65%", "Duplicate threshold", new Color(255, 243, 228))); cards.add(metricPanel("LIVE", "Analysis status", new Color(240, 235, 255)));
        page.add(cards, BorderLayout.CENTER);
        JPanel callout = new RoundedPanel(new Color(225, 234, 255), 18); callout.setLayout(new BorderLayout(12, 0)); callout.setBorder(new EmptyBorder(20, 22, 20, 22)); callout.add(textLabel("Ready to explore a new idea?", 18f, NAVY), BorderLayout.CENTER); JButton action = primaryButton("Start a submission"); action.addActionListener(e -> workspaceCards.show(workspaceContent, "submit")); callout.add(action, BorderLayout.EAST); page.add(callout, BorderLayout.SOUTH); return page;
    }

    private JPanel createSubmitPage() {
        JPanel page = pagePanel(); page.add(pageTitle("Submit an idea", "We will analyze it against existing project records and submissions."), BorderLayout.NORTH);
        RoundedPanel form = new RoundedPanel(Color.WHITE, 18); form.setLayout(new GridBagLayout()); form.setBorder(new EmptyBorder(24, 26, 24, 26));
        GridBagConstraints c = constraints(); c.gridx = 0; c.gridy = 0; c.insets = new Insets(0, 0, 5, 0); form.add(fieldLabel("IDEA ID"), c);
        idField = styledTextField(); c.gridy++; c.insets = new Insets(0, 0, 14, 0); form.add(idField, c);
        c.gridy++; c.insets = new Insets(0, 0, 5, 0); form.add(fieldLabel("IDEA TITLE"), c);
        titleField = styledTextField(); c.gridy++; c.insets = new Insets(0, 0, 14, 0); form.add(titleField, c);
        c.gridy++; c.insets = new Insets(0, 0, 5, 0); form.add(fieldLabel("DESCRIPTION"), c);
        descriptionArea = new JTextArea(6, 40); descriptionArea.setLineWrap(true); descriptionArea.setWrapStyleWord(true); descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 14)); JScrollPane descriptionScroll = new JScrollPane(descriptionArea); descriptionScroll.setBorder(BorderFactory.createLineBorder(new Color(207, 215, 234))); c.gridy++; c.insets = new Insets(0, 0, 16, 0); form.add(descriptionScroll, c);
        JButton analyze = primaryButton("Analyze and submit"); c.gridy++; c.anchor = GridBagConstraints.WEST; form.add(analyze, c);
        page.add(form, BorderLayout.CENTER);
        analysisArea = new JTextArea(); analysisArea.setEditable(false); analysisArea.setLineWrap(true); analysisArea.setWrapStyleWord(true); analysisArea.setFont(new Font("SansSerif", Font.PLAIN, 14)); analysisArea.setBackground(new Color(235, 242, 255)); analysisArea.setBorder(new EmptyBorder(18, 20, 18, 20));
        JPanel result = new JPanel(new BorderLayout(0, 8)); result.setOpaque(false); result.setBorder(new EmptyBorder(18, 0, 0, 0)); result.add(fieldLabel("LATEST ANALYSIS"), BorderLayout.NORTH); result.add(analysisArea, BorderLayout.CENTER); page.add(result, BorderLayout.SOUTH);
        analyze.addActionListener(e -> submitIdeaAction()); return page;
    }

    private JPanel createRecordsPage() {
        JPanel page = pagePanel();
        JPanel titleRow = new JPanel(new BorderLayout()); titleRow.setOpaque(false); titleRow.add(pageTitle("Idea records", "Review accepted submissions and the duplicate-analysis result for each."), BorderLayout.WEST);
        JButton refresh = new JButton("Refresh records"); refresh.addActionListener(e -> refreshRecords()); titleRow.add(refresh, BorderLayout.EAST); page.add(titleRow, BorderLayout.NORTH);
        recordsModel = new DefaultTableModel(new String[]{"ID", "Idea", "Status", "Novelty", "Analysis"}, 0) { public boolean isCellEditable(int row, int column) { return false; } };
        JTable table = new JTable(recordsModel); table.setRowHeight(38); table.setShowHorizontalLines(false); table.setSelectionBackground(new Color(227, 236, 255)); table.getTableHeader().setBackground(NAVY); table.getTableHeader().setForeground(Color.WHITE); table.getColumnModel().getColumn(4).setPreferredWidth(360);
        JScrollPane scroll = new JScrollPane(table); scroll.setBorder(BorderFactory.createLineBorder(new Color(221, 226, 239))); scroll.setBackground(Color.WHITE); page.add(scroll, BorderLayout.CENTER); return page;
    }

    private JPanel createAdminDashboard() {
        JPanel page = new JPanel(new BorderLayout()); page.setBackground(BACKGROUND);
        JPanel top = new JPanel(new BorderLayout()); top.setBackground(NAVY); top.setBorder(new EmptyBorder(22, 30, 22, 30));
        JLabel heading = textLabel("INNOVX  |  ADMIN ANALYTICS", 20f, Color.WHITE); top.add(heading, BorderLayout.WEST);
        JButton switchAccount = new JButton("Switch profile"); switchAccount.addActionListener(e -> switchProfile()); top.add(switchAccount, BorderLayout.EAST); page.add(top, BorderLayout.NORTH);
        JPanel body = new JPanel(new BorderLayout(0, 20)); body.setBackground(BACKGROUND); body.setBorder(new EmptyBorder(28, 30, 28, 30));
        body.add(pageTitle("Submission intelligence", "Organization-wide view of registrations, duplicate attempts, and idea quality."), BorderLayout.NORTH);
        JPanel center = new JPanel(new BorderLayout(0, 20)); center.setOpaque(false);
        JPanel metrics = new JPanel(new GridLayout(1, 3, 16, 0)); metrics.setOpaque(false);
        JPanel total = metricPanel("0", "Total submissions", new Color(231, 238, 255)); adminTotalLabel = (JLabel) total.getComponent(0);
        JPanel duplicates = metricPanel("0", "Duplicate attempts", new Color(255, 237, 237)); adminDuplicateLabel = (JLabel) duplicates.getComponent(0);
        JPanel users = metricPanel("0", "Active submitters", new Color(232, 248, 242)); adminUserLabel = (JLabel) users.getComponent(0);
        metrics.add(total); metrics.add(duplicates); metrics.add(users); center.add(metrics, BorderLayout.NORTH);
        adminRecordsModel = new DefaultTableModel(new String[]{"Submitter", "Idea", "Status", "Novelty", "Duplicate analysis"}, 0) { public boolean isCellEditable(int row, int column) { return false; } };
        JTable table = new JTable(adminRecordsModel); table.setRowHeight(38); table.setShowHorizontalLines(false); table.getTableHeader().setBackground(NAVY); table.getTableHeader().setForeground(Color.WHITE); table.getColumnModel().getColumn(4).setPreferredWidth(360);
        center.add(new JScrollPane(table), BorderLayout.CENTER); body.add(center, BorderLayout.CENTER);
        JLabel note = textLabel("Analysis method: token-based Jaccard similarity across all historical projects and every recorded submission. A score of 65% or higher is flagged as a duplicate attempt.", 12f, new Color(92, 104, 130)); body.add(note, BorderLayout.SOUTH);
        page.add(body, BorderLayout.CENTER); return page;
    }

    private void openWorkspace(String username, boolean isAdmin) {
        activeUser = username; activeAdmin = isAdmin;
        welcomeLabel.setText("Good " + timeGreeting() + ", " + (isAdmin ? "Administrator" : "Innovator") + ".");
        profileLabel.setText((isAdmin ? "Admin" : "User") + " • " + username + "  ");
        if (isAdmin) { applicationCards.show(root, "admin"); refreshAdminAnalytics(); }
        else { applicationCards.show(root, "workspace"); workspaceCards.show(workspaceContent, "overview"); refreshRecords(); }
    }

    private void switchProfile() {
        int choice = JOptionPane.showConfirmDialog(this, "Switching returns you to the sign-in screen. Continue?", "Switch profile", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) { usernameField.setText(""); passwordField.setText(""); applicationCards.show(root, "login"); usernameField.requestFocusInWindow(); }
    }

    private void submitIdeaAction() {
        String id = idField.getText().trim(), title = titleField.getText().trim(), description = descriptionArea.getText().trim();
        if (id.isEmpty() || title.isEmpty() || description.isEmpty()) { JOptionPane.showMessageDialog(this, "ID, title, and description are required.", "Missing information", JOptionPane.ERROR_MESSAGE); return; }
        analysisArea.setText("Analyzing this idea against the records…");
        new SwingWorker<ScoreReport, Void>() {
            protected ScoreReport doInBackground() { return new SubmissionClient().sendIdea(new Idea(id, title, description, activeUser)); }
            protected void done() { try { ScoreReport report = get(); String match = report.matchingIdeaId == null ? "No close record found" : "Closest record: " + report.matchingIdeaId; analysisArea.setText((report.duplicate ? "DUPLICATE RISK\n" : "UNIQUE IDEA\n") + report.message + "\nSimilarity: " + Math.round(report.duplicateScore * 100) + "% • " + match); if (!report.duplicate && report.duplicateScore >= 0) { idField.setText(""); titleField.setText(""); descriptionArea.setText(""); refreshRecords(); } } catch (Exception error) { analysisArea.setText("Analysis could not be completed: " + error.getMessage()); } }
        }.execute();
    }

    private void refreshRecords() {
        new SwingWorker<List<Idea>, Void>() {
            protected List<Idea> doInBackground() { return new SubmissionClient().fetchSubmittedIdeas(); }
            protected void done() { try { List<Idea> ideas = get(); recordsModel.setRowCount(0); int personalCount = 0; for (Idea idea : ideas) if (activeUser.equals(idea.getSubmitter())) { recordsModel.addRow(new Object[]{idea.getId(), idea.getTitle(), idea.getStatus(), Math.round(idea.getFinalScore() * 100) + "%", idea.getAnalysisSummary()}); personalCount++; } recordCountLabel.setText(String.valueOf(personalCount)); } catch (Exception ignored) { } }
        }.execute();
    }

    private void refreshAdminAnalytics() {
        new SwingWorker<List<Idea>, Void>() {
            protected List<Idea> doInBackground() { return new SubmissionClient().fetchSubmittedIdeas(); }
            protected void done() {
                try {
                    List<Idea> ideas = get(); int duplicates = 0; java.util.Set<String> submitters = new java.util.HashSet<>();
                    adminRecordsModel.setRowCount(0);
                    for (Idea idea : ideas) {
                        if (idea.getStatus() == IdeaStatus.REJECTED) duplicates++;
                        submitters.add(idea.getSubmitter());
                        adminRecordsModel.addRow(new Object[]{idea.getSubmitter(), idea.getTitle(), idea.getStatus(), Math.round(idea.getFinalScore() * 100) + "%", idea.getAnalysisSummary()});
                    }
                    adminTotalLabel.setText(String.valueOf(ideas.size())); adminDuplicateLabel.setText(String.valueOf(duplicates)); adminUserLabel.setText(String.valueOf(submitters.size()));
                } catch (Exception ignored) { }
            }
        }.execute();
    }

    private void loadUserCredentials() {
        try { userCredentials.putAll(FileManager.loadUserCredentials("pastProjects.txt")); } catch (Exception ignored) { }
        if (!userCredentials.containsKey("user")) { try { userCredentials.put("user", hashPassword("user123")); } catch (Exception ignored) { } }
    }

    private String hashPassword(String password) throws Exception {
        byte[] salt = new byte[16]; new SecureRandom().nextBytes(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 120000, 256);
        byte[] hash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
    }

    private boolean verifyPassword(String password, String stored) {
        try {
            String[] pieces = stored.split(":", 2); if (pieces.length != 2) return false;
            byte[] salt = Base64.getDecoder().decode(pieces[0]), expected = Base64.getDecoder().decode(pieces[1]);
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 120000, expected.length * 8);
            byte[] actual = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
            return MessageDigest.isEqual(actual, expected);
        } catch (Exception ignored) { return false; }
    }

    private JButton navButton(String text, String page) { JButton button = new JButton(text); styleNavButton(button); button.addActionListener(e -> { workspaceCards.show(workspaceContent, page); if ("records".equals(page)) refreshRecords(); }); return button; }
    private void styleNavButton(JButton button) { button.setHorizontalAlignment(SwingConstants.LEFT); button.setMaximumSize(new Dimension(184, 40)); button.setAlignmentX(Component.LEFT_ALIGNMENT); button.setForeground(new Color(220, 228, 250)); button.setBackground(NAVY); button.setBorder(new EmptyBorder(10, 12, 10, 12)); button.setFocusPainted(false); }
    private JPanel pagePanel() { JPanel page = new JPanel(new BorderLayout(0, 0)); page.setBackground(BACKGROUND); page.setBorder(new EmptyBorder(28, 30, 28, 30)); return page; }
    private JComponent pageTitle(String title, String subtitle) { JPanel panel = new JPanel(); panel.setOpaque(false); panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); JLabel main = textLabel(title, 26f, NAVY); JLabel sub = textLabel(subtitle, 14f, new Color(101, 112, 138)); panel.add(main); panel.add(Box.createVerticalStrut(5)); panel.add(sub); return panel; }
    private JPanel metricPanel(String value, String label, Color color) { RoundedPanel panel = new RoundedPanel(Color.WHITE, 16); panel.setLayout(new BorderLayout()); panel.setBorder(new EmptyBorder(20, 20, 20, 20)); JLabel number = textLabel(value, 24f, NAVY); JLabel caption = textLabel(label, 13f, new Color(101, 112, 138)); panel.add(number, BorderLayout.NORTH); panel.add(caption, BorderLayout.SOUTH); panel.setBackground(color); return panel; }
    private JLabel fieldLabel(String text) { JLabel label = textLabel(text, 11f, new Color(92, 104, 130)); label.setFont(label.getFont().deriveFont(Font.BOLD)); return label; }
    private JLabel textLabel(String text, float size, Color color) { JLabel label = new JLabel("<html>" + text.replace("\n", "<br>") + "</html>"); label.setFont(new Font("SansSerif", Font.PLAIN, Math.round(size))); label.setForeground(color); return label; }
    private JTextField styledTextField() { JTextField field = new JTextField(); field.setFont(new Font("SansSerif", Font.PLAIN, 14)); field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(207, 215, 234)), new EmptyBorder(9, 10, 9, 10))); field.setPreferredSize(new Dimension(330, 40)); return field; }
    private JButton primaryButton(String text) { JButton button = new JButton(text); button.setForeground(Color.WHITE); button.setBackground(BLUE); button.setFocusPainted(false); button.setBorder(new EmptyBorder(11, 18, 11, 18)); return button; }
    private GridBagConstraints constraints() { GridBagConstraints c = new GridBagConstraints(); c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1; c.anchor = GridBagConstraints.WEST; return c; }
    private String timeGreeting() { int hour = java.time.LocalTime.now().getHour(); return hour < 12 ? "morning" : hour < 18 ? "afternoon" : "evening"; }

    private static class RoundedPanel extends JPanel { private final int radius; RoundedPanel(Color color, int radius) { this.radius = radius; setOpaque(false); setBackground(color); } protected void paintComponent(Graphics g) { Graphics2D g2 = (Graphics2D) g.create(); g2.setColor(getBackground()); g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius); g2.dispose(); super.paintComponent(g); } }
}