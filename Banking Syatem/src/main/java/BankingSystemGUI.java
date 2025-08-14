import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.model.Transaction;
import com.banking.service.BankingService;
import com.banking.util.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import java.awt.image.BufferedImage;

/**
 * Professional Swing GUI Banking System Application
 * Modern banking interface with sidebar navigation and dashboard
 * Enhanced with animations and modern design
 */
public class BankingSystemGUI extends JFrame {
    
    private BankingService bankingService;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JTextField accountNumberField;
    private JPasswordField pinField;
    private JButton signupButton;
    private JLabel welcomeLabel;
    private JLabel balanceLabel;
    private JLabel accountNumberLabel;
    private JLabel accountTypeLabel;
    private JTable transactionTable;
    private DefaultTableModel transactionTableModel;
    private Account currentAccount;
    private Customer currentCustomer;
    
    // Enhanced Modern Colors with Gradients
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210);
    private static final Color PRIMARY_DARK = new Color(13, 71, 161);
    private static final Color SECONDARY_COLOR = new Color(66, 165, 245);
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private static final Color SUCCESS_DARK = new Color(46, 125, 50);
    private static final Color WARNING_COLOR = new Color(255, 152, 0);
    private static final Color WARNING_DARK = new Color(230, 81, 0);
    private static final Color DANGER_COLOR = new Color(244, 67, 54);
    private static final Color DANGER_DARK = new Color(198, 40, 40);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color SIDEBAR_COLOR = new Color(33, 33, 33);
    private static final Color SIDEBAR_HOVER = new Color(55, 55, 55);
    
    // Enhanced Icons with better designs
    private ImageIcon bankIcon;
    private ImageIcon dashboardIcon;
    private ImageIcon depositIcon;
    private ImageIcon withdrawIcon;
    private ImageIcon transferIcon;
    private ImageIcon pinIcon;
    private ImageIcon historyIcon;
    private ImageIcon logoutIcon;
    private ImageIcon userIcon;
    private ImageIcon balanceIcon;
    private ImageIcon accountIcon;
    private ImageIcon securityIcon;
    private ImageIcon chartIcon;
    
    public BankingSystemGUI() {
        bankingService = new BankingService();
        loadEnhancedIcons();
        initializeFrame();
        createLoginPanel();
        createSignupPanel();
        createMainApplication();
        showLoginPanel();
    }
    
    private void loadEnhancedIcons() {
        try {
            // Create enhanced colored icons with better designs
            bankIcon = createEnhancedIcon(PRIMARY_COLOR, "🏦", 32);
            dashboardIcon = createEnhancedIcon(PRIMARY_COLOR, "📊", 24);
            depositIcon = createEnhancedIcon(SUCCESS_COLOR, "💰", 24);
            withdrawIcon = createEnhancedIcon(WARNING_COLOR, "💸", 24);
            transferIcon = createEnhancedIcon(PRIMARY_COLOR, "🔄", 24);
            pinIcon = createEnhancedIcon(SECONDARY_COLOR, "🔐", 24);
            historyIcon = createEnhancedIcon(SECONDARY_COLOR, "📋", 24);
            logoutIcon = createEnhancedIcon(DANGER_COLOR, "🚪", 24);
            userIcon = createEnhancedIcon(PRIMARY_COLOR, "👤", 24);
            balanceIcon = createEnhancedIcon(SUCCESS_COLOR, "💳", 24);
            accountIcon = createEnhancedIcon(SECONDARY_COLOR, "🏛️", 24);
            securityIcon = createEnhancedIcon(PRIMARY_COLOR, "🛡️", 24);
            chartIcon = createEnhancedIcon(SUCCESS_COLOR, "📈", 24);
        } catch (Exception e) {
            System.out.println("Enhanced icons not loaded: " + e.getMessage());
        }
    }
    
    private ImageIcon createEnhancedIcon(Color color, String emoji, int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Enable anti-aliasing and high quality rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // Create gradient background
        GradientPaint gradient = new GradientPaint(0, 0, color.brighter(), size, size, color.darker());
        g2d.setPaint(gradient);
        g2d.fillOval(2, 2, size-4, size-4);
        
        // Add shadow effect
        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.fillOval(4, 4, size-4, size-4);
        
        // Draw emoji
        g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, size-8));
        g2d.setColor(Color.WHITE);
        
        FontMetrics fm = g2d.getFontMetrics();
        int x = (size - fm.stringWidth(emoji)) / 2;
        int y = (size - fm.getHeight()) / 2 + fm.getAscent();
        
        g2d.drawString(emoji, x, y);
        g2d.dispose();
        
        return new ImageIcon(image);
    }
    
    private JButton createEnhancedButton(String text, Color primaryColor, Color darkColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create gradient background based on state
                Color bgColor = getModel().isPressed() ? darkColor : 
                               getModel().isRollover() ? primaryColor.brighter() : primaryColor;
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, bgColor,
                    0, getHeight(), darkColor
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                // Add glow effect on hover
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(255, 255, 255, 30));
                    g2d.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, 23, 23);
                }
                
                // Draw text
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(getText(), textX, textY);
                
                g2d.dispose();
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        button.setMaximumSize(new Dimension(200, 50));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        
        return button;
    }
    
    private void initializeFrame() {
        setTitle("OneStopBank - Professional Banking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(true);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(BACKGROUND_COLOR);
        add(mainPanel);
    }
    
    private void createSignupPanel() {
        JPanel signupPanel = new JPanel(new BorderLayout(10, 10));
        signupPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        signupPanel.setBackground(Color.WHITE);
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        
        // Title
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Form fields
        JTextField firstNameField = createFormField("First Name");
        JTextField lastNameField = createFormField("Last Name");
        JTextField emailField = createFormField("Email");
        JTextField phoneField = createFormField("Phone");
        JTextField addressField = createFormField("Address");
        JTextField dobField = createFormField("Date of Birth (YYYY-MM-DD)");
        JComboBox<String> accountTypeCombo = new JComboBox<>(new String[]{"SAVINGS", "CHECKING"});
        JTextField initialDepositField = createFormField("Initial Deposit");
        JPasswordField pinField = new JPasswordField();
        pinField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        pinField.setMaximumSize(new Dimension(400, 40));
        
        // Add fields to form
        formPanel.add(titleLabel);
        formPanel.add(createFormLabel("Personal Information"));
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(firstNameField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(lastNameField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(phoneField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(addressField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(dobField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(createFormLabel("Account Information"));
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(accountTypeCombo);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(initialDepositField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(createFormLabel("Create PIN (4 digits)"));
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(pinField);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);
        
        JButton createButton = new JButton("Create Account");
        createButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        createButton.setBackground(SUCCESS_COLOR);
        createButton.setForeground(Color.WHITE);
        createButton.setBorderPainted(false);
        createButton.setFocusPainted(false);
        createButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createButton.addActionListener(e -> {
            try {
                // Validate inputs
                if (firstNameField.getText().trim().isEmpty() ||
                    lastNameField.getText().trim().isEmpty() ||
                    emailField.getText().trim().isEmpty() ||
                    pinField.getPassword().length != 4) {
                    JOptionPane.showMessageDialog(this, 
                        "Please fill in all required fields and ensure PIN is 4 digits",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Get PIN from password field
                String pin = new String(pinField.getPassword());
                
                // Create the account with provided PIN
                Account newAccount = bankingService.createCustomerAccount(
                    firstNameField.getText().trim(),
                    lastNameField.getText().trim(),
                    emailField.getText().trim(),
                    phoneField.getText().trim(),
                    addressField.getText().trim(),
                    java.sql.Date.valueOf(dobField.getText().trim()),
                    Account.AccountType.valueOf(accountTypeCombo.getSelectedItem().toString()),
                    new BigDecimal(initialDepositField.getText().trim()),
                    pin
                );
                
                JOptionPane.showMessageDialog(this, 
                    "Account created successfully!\n" +
                    "Account Number: " + newAccount.getAccountNumber() + "\n" +
                    "Please remember your account number and PIN.",
                    "Account Created", JOptionPane.INFORMATION_MESSAGE);
                
                // Clear form and switch back to login
                clearSignupForm();
                cardLayout.show(mainPanel, "LOGIN");
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error creating account: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton backButton = new JButton("Back to Login");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backButton.setBackground(Color.LIGHT_GRAY);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));
        
        buttonPanel.add(createButton);
        buttonPanel.add(backButton);
        
        // Add components to panel
        signupPanel.add(formPanel, BorderLayout.CENTER);
        signupPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(signupPanel, "SIGNUP");
    }
    
    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(100, 100, 100));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    private JTextField createFormField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setMaximumSize(new Dimension(400, 40));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Add placeholder behavior
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().trim().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
        
        // Set initial state
        if (field.getText().equals(placeholder)) {
            field.setForeground(Color.GRAY);
        }
        
        return field;
    }
    
    private void createLoginPanel() {
        JPanel loginPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                
                // Create gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(25, 118, 210, 50),
                    getWidth(), getHeight(), new Color(66, 165, 245, 30)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Add subtle pattern
                g2d.setColor(new Color(255, 255, 255, 20));
                for (int i = 0; i < getWidth(); i += 50) {
                    for (int j = 0; j < getHeight(); j += 50) {
                        g2d.fillOval(i, j, 2, 2);
                    }
                }
                
                g2d.dispose();
            }
        };
        loginPanel.setLayout(new BorderLayout());
        
        // Left side - Branding with enhanced gradient
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                
                // Create enhanced gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_COLOR,
                    getWidth(), getHeight(), PRIMARY_DARK
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Add animated circles in background
                g2d.setColor(new Color(255, 255, 255, 10));
                for (int i = 0; i < 5; i++) {
                    int x = (int) (Math.random() * getWidth());
                    int y = (int) (Math.random() * getHeight());
                    int size = 50 + (int) (Math.random() * 100);
                    g2d.fillOval(x, y, size, size);
                }
                
                g2d.dispose();
            }
        };
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(500, 0));
        leftPanel.setBorder(new EmptyBorder(60, 60, 60, 60));
        
        JLabel brandLabel = new JLabel("OneStopBank");
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        brandLabel.setForeground(Color.WHITE);
        if (bankIcon != null) {
            brandLabel.setIcon(bankIcon);
            brandLabel.setIconTextGap(15);
        }
        
        JLabel taglineLabel = new JLabel("Your Trusted Banking Partner");
        taglineLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        taglineLabel.setForeground(new Color(200, 200, 200));
        
        JPanel leftContent = new JPanel();
        leftContent.setLayout(new BoxLayout(leftContent, BoxLayout.Y_AXIS));
        leftContent.setOpaque(false);
        leftContent.add(brandLabel);
        leftContent.add(Box.createVerticalStrut(20));
        leftContent.add(taglineLabel);
        
        leftPanel.add(leftContent, BorderLayout.CENTER);
        
        // Right side - Login Form
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBackground(CARD_COLOR);
        rightPanel.setBorder(new EmptyBorder(80, 60, 80, 60));
        
        JLabel loginTitle = new JLabel("Welcome Back");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        loginTitle.setForeground(PRIMARY_COLOR);
        
        JLabel loginSubtitle = new JLabel("Sign in to your account");
        loginSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        loginSubtitle.setForeground(Color.GRAY);
        
        // Login Form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(40, 0, 0, 0));
        
        // Account Number
        JLabel accountLabel = new JLabel("Account Number");
        accountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        accountLabel.setForeground(Color.DARK_GRAY);
        accountNumberField = new JTextField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !hasFocus()) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setColor(Color.GRAY);
                    g2d.setFont(getFont().deriveFont(Font.ITALIC));
                    g2d.drawString("Enter Account Number", 15, getHeight() / 2 + 5);
                    g2d.dispose();
                }
            }
        };
        accountNumberField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        accountNumberField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        accountNumberField.setMaximumSize(new Dimension(400, 50));
        
        // PIN
        JLabel pinLabel = new JLabel("PIN");
        pinLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pinLabel.setForeground(Color.DARK_GRAY);
        pinField = new JPasswordField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getPassword().length == 0 && !hasFocus()) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setColor(Color.GRAY);
                    g2d.setFont(getFont().deriveFont(Font.ITALIC));
                    g2d.drawString("Enter PIN", 15, getHeight() / 2 + 5);
                    g2d.dispose();
                }
            }
        };
        pinField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        pinField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        pinField.setMaximumSize(new Dimension(400, 50));
        
        // Login Button
        JButton loginButton = createEnhancedButton("Sign In", PRIMARY_COLOR, PRIMARY_DARK);
        loginButton.setMaximumSize(new Dimension(400, 50));
        
        // Demo Account Info
        JLabel demoLabel = new JLabel("Demo: ACC001 | PIN: 1234", SwingConstants.CENTER);
        demoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        demoLabel.setForeground(Color.GRAY);
        
        // Layout form elements
        formPanel.add(accountLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(accountNumberField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(pinLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(pinField);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(loginButton);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(demoLabel);
        
        // Login Action
        loginButton.addActionListener(e -> login());
        
        // Enter key for login
        Action loginAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        };
        accountNumberField.addActionListener(loginAction);
        pinField.addActionListener(loginAction);
        
        JPanel rightContent = new JPanel(new BorderLayout());
        rightContent.setOpaque(false);
        rightContent.add(loginTitle, BorderLayout.NORTH);
        rightContent.add(Box.createVerticalStrut(10), BorderLayout.CENTER);
        rightContent.add(loginSubtitle, BorderLayout.CENTER);
        rightContent.add(formPanel, BorderLayout.SOUTH);
        
        rightPanel.add(rightContent, BorderLayout.CENTER);
        
        // Add signup button
        signupButton = new JButton("Create New Account");
        signupButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        signupButton.setBackground(Color.WHITE);
        signupButton.setForeground(PRIMARY_COLOR);
        signupButton.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 1));
        signupButton.setFocusPainted(false);
        signupButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupButton.addActionListener(e -> cardLayout.show(mainPanel, "SIGNUP"));
        
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        
        JPanel buttonsWrapper = new JPanel();
        buttonsWrapper.setLayout(new BoxLayout(buttonsWrapper, BoxLayout.Y_AXIS));
        buttonsWrapper.setOpaque(false);
        
        buttonsWrapper.add(loginButton);
        buttonsWrapper.add(Box.createVerticalStrut(10));
        buttonsWrapper.add(signupButton);
        
        buttonPanel.add(buttonsWrapper, new GridBagConstraints());
        formPanel.add(buttonPanel, BorderLayout.CENTER);
        
        loginPanel.add(leftPanel, BorderLayout.WEST);
        loginPanel.add(rightPanel, BorderLayout.CENTER);
        
        mainPanel.add(loginPanel, "LOGIN");
    }
    
    private void createMainApplication() {
        JPanel mainAppPanel = new JPanel(new BorderLayout());
        mainAppPanel.setBackground(BACKGROUND_COLOR);
        
        // Top Header
        createHeader(mainAppPanel);
        
        // Sidebar
        createSidebar(mainAppPanel);
        
        // Main Content Area
        createMainContent(mainAppPanel);
        
        mainPanel.add(mainAppPanel, "MAIN_APP");
    }
    
    private void createHeader(JPanel mainAppPanel) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_COLOR);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        // Welcome and Account Info
        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftHeader.setOpaque(false);
        
        welcomeLabel = new JLabel("Hi, Welcome!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeLabel.setForeground(PRIMARY_COLOR);
        if (userIcon != null) {
            welcomeLabel.setIcon(userIcon);
            welcomeLabel.setIconTextGap(10);
        }
        
        leftHeader.add(welcomeLabel);
        
        // Right side - Logout
        JButton logoutButton = createEnhancedButton("Logout", DANGER_COLOR, DANGER_DARK);
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        if (logoutIcon != null) {
            logoutButton.setIcon(logoutIcon);
            logoutButton.setIconTextGap(8);
        }
        logoutButton.addActionListener(e -> logout());
        
        headerPanel.add(leftHeader, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        mainAppPanel.add(headerPanel, BorderLayout.NORTH);
    }
    
    private void createSidebar(JPanel mainAppPanel) {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(SIDEBAR_COLOR);
        sidebarPanel.setPreferredSize(new Dimension(250, 0));
        sidebarPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        // Sidebar Title
        JLabel sidebarTitle = new JLabel("Navigation");
        sidebarTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sidebarTitle.setForeground(Color.WHITE);
        sidebarTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebarTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Navigation Buttons
        String[] menuItems = {
            "Dashboard", "Deposit", "Withdraw", "Fund Transfer", 
            "Account PIN", "Transaction History"
        };
        
        for (int i = 0; i < menuItems.length; i++) {
            JButton menuButton = createSidebarButton(menuItems[i], i);
            sidebarPanel.add(menuButton);
            sidebarPanel.add(Box.createVerticalStrut(10));
        }
        
        sidebarPanel.add(Box.createVerticalGlue());
        
        mainAppPanel.add(sidebarPanel, BorderLayout.WEST);
    }
    
    private JButton createSidebarButton(String text, int index) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create gradient background based on hover state
                Color bgColor = getModel().isPressed() ? SIDEBAR_HOVER : 
                               getModel().isRollover() ? SIDEBAR_HOVER : SIDEBAR_COLOR;
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, bgColor,
                    getWidth(), getHeight(), bgColor.darker()
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                // Add glow effect on hover
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(255, 255, 255, 30));
                    g2d.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, 8, 8);
                }
                
                // Draw text
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(getText(), textX, textY);
                
                g2d.dispose();
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(SIDEBAR_COLOR);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(230, 45));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        
        // Add icons to buttons
        switch (index) {
            case 0: // Dashboard
                if (dashboardIcon != null) button.setIcon(dashboardIcon);
                break;
            case 1: // Deposit
                if (depositIcon != null) button.setIcon(depositIcon);
                break;
            case 2: // Withdraw
                if (withdrawIcon != null) button.setIcon(withdrawIcon);
                break;
            case 3: // Transfer
                if (transferIcon != null) button.setIcon(transferIcon);
                break;
            case 4: // PIN
                if (pinIcon != null) button.setIcon(pinIcon);
                break;
            case 5: // History
                if (historyIcon != null) button.setIcon(historyIcon);
                break;
        }
        
        button.addActionListener(e -> handleSidebarAction(index));
        
        return button;
    }
    
    private void createMainContent(JPanel mainAppPanel) {
        JPanel contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create all content panels
        createDashboardPanel(contentPanel);
        createDepositPanel(contentPanel);
        createWithdrawPanel(contentPanel);
        createTransferPanel(contentPanel);
        createPinPanel(contentPanel);
        createHistoryPanel(contentPanel);
        
        mainAppPanel.add(contentPanel, BorderLayout.CENTER);
    }
    
    private void createDashboardPanel(JPanel contentPanel) {
        JPanel dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setBackground(BACKGROUND_COLOR);
        
        // Account Summary Cards
        JPanel cardsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        cardsPanel.setOpaque(false);
        
        // Account Details Card
        JPanel accountCard = createCard("Account Details");
        accountCard.setLayout(new BoxLayout(accountCard, BoxLayout.Y_AXIS));
        accountCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add hover effect to account card
        accountCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                accountCard.setBorder(new EmptyBorder(15, 15, 15, 15));
                accountCard.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                accountCard.setBorder(new EmptyBorder(20, 20, 20, 20));
                accountCard.repaint();
            }
        });
        
        JLabel accountTitleLabel = new JLabel("Account Details");
        accountTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        accountTitleLabel.setForeground(PRIMARY_COLOR);
        if (accountIcon != null) {
            accountTitleLabel.setIcon(accountIcon);
            accountTitleLabel.setIconTextGap(10);
        }
        
        accountNumberLabel = new JLabel("Account Number: ---");
        accountNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel emailLabel = new JLabel("Email: ---");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel addressLabel = new JLabel("Address: ---");
        addressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        accountCard.add(accountTitleLabel);
        accountCard.add(Box.createVerticalStrut(15));
        accountCard.add(accountNumberLabel);
        accountCard.add(Box.createVerticalStrut(10));
        accountCard.add(emailLabel);
        accountCard.add(Box.createVerticalStrut(10));
        accountCard.add(addressLabel);
        
        // Balance Card
        JPanel balanceCard = createCard("Current Balance");
        balanceCard.setLayout(new BoxLayout(balanceCard, BoxLayout.Y_AXIS));
        balanceCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add hover effect to balance card
        balanceCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                balanceCard.setBorder(new EmptyBorder(15, 15, 15, 15));
                balanceCard.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                balanceCard.setBorder(new EmptyBorder(20, 20, 20, 20));
                balanceCard.repaint();
            }
        });
        
        JLabel balanceTitleLabel = new JLabel("Current Balance");
        balanceTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        balanceTitleLabel.setForeground(PRIMARY_COLOR);
        balanceTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (balanceIcon != null) {
            balanceTitleLabel.setIcon(balanceIcon);
            balanceTitleLabel.setIconTextGap(10);
        }
        
        balanceLabel = new JLabel("$0.00");
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        balanceLabel.setForeground(PRIMARY_COLOR);
        balanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        accountTypeLabel = new JLabel("Account Type: ---");
        accountTypeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        accountTypeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel branchLabel = new JLabel("Branch: Main Branch");
        branchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        branchLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel ifscLabel = new JLabel("IFSC: MAIN01");
        ifscLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ifscLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        balanceCard.add(balanceTitleLabel);
        balanceCard.add(Box.createVerticalStrut(15));
        balanceCard.add(balanceLabel);
        balanceCard.add(Box.createVerticalStrut(15));
        balanceCard.add(accountTypeLabel);
        balanceCard.add(Box.createVerticalStrut(5));
        balanceCard.add(branchLabel);
        balanceCard.add(Box.createVerticalStrut(5));
        balanceCard.add(ifscLabel);
        
        cardsPanel.add(accountCard);
        cardsPanel.add(balanceCard);
        
        // Transaction History Section
        JPanel historySection = createCard("Recent Transactions");
        historySection.setLayout(new BorderLayout());
        
        JLabel historyTitleLabel = new JLabel("Recent Transactions");
        historyTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        historyTitleLabel.setForeground(PRIMARY_COLOR);
        if (historyIcon != null) {
            historyTitleLabel.setIcon(historyIcon);
            historyTitleLabel.setIconTextGap(10);
        }
        
        JPanel historyHeader = new JPanel(new BorderLayout());
        historyHeader.setOpaque(false);
        historyHeader.add(historyTitleLabel, BorderLayout.WEST);
        historyHeader.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        String[] columnNames = {"Transaction ID", "Amount", "Transaction Type", "Transaction Date", "Source Account", "Target Account"};
        transactionTableModel = new DefaultTableModel(columnNames, 0);
        transactionTable = new JTable(transactionTableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                if (!comp.getBackground().equals(getSelectionBackground())) {
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                }
                return comp;
            }
        };
        transactionTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        transactionTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        transactionTable.setRowHeight(30);
        transactionTable.setGridColor(new Color(220, 220, 220));
        transactionTable.setShowGrid(true);
        transactionTable.setIntercellSpacing(new Dimension(1, 1));
        
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        
        historySection.add(historyHeader, BorderLayout.NORTH);
        historySection.add(scrollPane, BorderLayout.CENTER);
        
        dashboardPanel.add(cardsPanel, BorderLayout.NORTH);
        dashboardPanel.add(Box.createVerticalStrut(20), BorderLayout.CENTER);
        dashboardPanel.add(historySection, BorderLayout.SOUTH);
        
        contentPanel.add(dashboardPanel, "DASHBOARD");
    }
    
    private JPanel createCard(String title) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create shadow effect
                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.fillRoundRect(3, 3, getWidth()-6, getHeight()-6, 15, 15);
                
                // Create main card with gradient
                GradientPaint gradient = new GradientPaint(
                    0, 0, CARD_COLOR,
                    0, getHeight(), new Color(250, 250, 250)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth()-3, getHeight()-3, 15, 15);
                
                // Add subtle border
                g2d.setColor(new Color(200, 200, 200, 100));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth()-3, getHeight()-3, 15, 15);
                
                g2d.dispose();
            }
        };
        
        card.setBackground(CARD_COLOR);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setOpaque(false);
        
        return card;
    }
    
    private void createDepositPanel(JPanel contentPanel) {
        JPanel depositPanel = createCard("Deposit Money");
        depositPanel.setLayout(new BoxLayout(depositPanel, BoxLayout.Y_AXIS));
        
        JLabel amountLabel = new JLabel("Amount ($):");
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        if (balanceIcon != null) {
            amountLabel.setIcon(balanceIcon);
            amountLabel.setIconTextGap(8);
        }
        
        JTextField amountField = new JTextField(20);
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        amountField.setMaximumSize(new Dimension(400, 40));
        amountField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JTextField descField = new JTextField(20);
        descField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descField.setMaximumSize(new Dimension(400, 40));
        descField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        JButton depositButton = createEnhancedButton("Deposit", SUCCESS_COLOR, SUCCESS_DARK);
        if (depositIcon != null) {
            depositButton.setIcon(depositIcon);
            depositButton.setIconTextGap(8);
        }
        
        depositButton.addActionListener(e -> {
            try {
                BigDecimal amount = new BigDecimal(amountField.getText());
                String description = descField.getText();
                
                Account updatedAccount = bankingService.depositMoney(
                    currentAccount.getAccountNumber(), amount, description);
                
                JOptionPane.showMessageDialog(this, 
                    "Deposit successful!\nNew Balance: $" + updatedAccount.getBalance(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Update current account with new balance
                currentAccount = updatedAccount;
                updateDashboard();
                showDashboard();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter a valid amount", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        depositPanel.add(amountLabel);
        depositPanel.add(Box.createVerticalStrut(8));
        depositPanel.add(amountField);
        depositPanel.add(Box.createVerticalStrut(20));
        depositPanel.add(descLabel);
        depositPanel.add(Box.createVerticalStrut(8));
        depositPanel.add(descField);
        depositPanel.add(Box.createVerticalStrut(30));
        depositPanel.add(depositButton);
        
        contentPanel.add(depositPanel, "DEPOSIT");
    }
    
    private void createWithdrawPanel(JPanel contentPanel) {
        JPanel withdrawPanel = createCard("Withdraw Money");
        withdrawPanel.setLayout(new BoxLayout(withdrawPanel, BoxLayout.Y_AXIS));
        
        JLabel amountLabel = new JLabel("Amount ($):");
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        if (balanceIcon != null) {
            amountLabel.setIcon(balanceIcon);
            amountLabel.setIconTextGap(8);
        }
        
        JTextField amountField = new JTextField(20);
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        amountField.setMaximumSize(new Dimension(400, 40));
        amountField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JTextField descField = new JTextField(20);
        descField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descField.setMaximumSize(new Dimension(400, 40));
        descField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        JButton withdrawButton = createEnhancedButton("Withdraw", WARNING_COLOR, WARNING_DARK);
        if (withdrawIcon != null) {
            withdrawButton.setIcon(withdrawIcon);
            withdrawButton.setIconTextGap(8);
        }
        
        withdrawButton.addActionListener(e -> {
            try {
                BigDecimal amount = new BigDecimal(amountField.getText());
                String description = descField.getText();
                
                Account updatedAccount = bankingService.withdrawMoney(
                    currentAccount.getAccountNumber(), amount, description);
                
                JOptionPane.showMessageDialog(this, 
                    "Withdrawal successful!\nNew Balance: $" + updatedAccount.getBalance(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Update current account with new balance
                currentAccount = updatedAccount;
                updateDashboard();
                showDashboard();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter a valid amount", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        withdrawPanel.add(amountLabel);
        withdrawPanel.add(Box.createVerticalStrut(8));
        withdrawPanel.add(amountField);
        withdrawPanel.add(Box.createVerticalStrut(20));
        withdrawPanel.add(descLabel);
        withdrawPanel.add(Box.createVerticalStrut(8));
        withdrawPanel.add(descField);
        withdrawPanel.add(Box.createVerticalStrut(30));
        withdrawPanel.add(withdrawButton);
        
        contentPanel.add(withdrawPanel, "WITHDRAW");
    }
    
    private void createTransferPanel(JPanel contentPanel) {
        JPanel transferPanel = createCard("Fund Transfer");
        transferPanel.setLayout(new BoxLayout(transferPanel, BoxLayout.Y_AXIS));
        
        JLabel toAccountLabel = new JLabel("To Account Number:");
        toAccountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        if (accountIcon != null) {
            toAccountLabel.setIcon(accountIcon);
            toAccountLabel.setIconTextGap(8);
        }
        
        JTextField toAccountField = new JTextField(20);
        toAccountField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        toAccountField.setMaximumSize(new Dimension(400, 40));
        toAccountField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        JLabel amountLabel = new JLabel("Amount ($):");
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        if (balanceIcon != null) {
            amountLabel.setIcon(balanceIcon);
            amountLabel.setIconTextGap(8);
        }
        
        JTextField amountField = new JTextField(20);
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        amountField.setMaximumSize(new Dimension(400, 40));
        amountField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JTextField descField = new JTextField(20);
        descField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descField.setMaximumSize(new Dimension(400, 40));
        descField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        JButton transferButton = createEnhancedButton("Transfer", PRIMARY_COLOR, PRIMARY_DARK);
        if (transferIcon != null) {
            transferButton.setIcon(transferIcon);
            transferButton.setIconTextGap(8);
        }
        
        transferButton.addActionListener(e -> {
            try {
                String toAccount = toAccountField.getText();
                BigDecimal amount = new BigDecimal(amountField.getText());
                String description = descField.getText();
                
                BankingService.TransferResult result = bankingService.transferMoney(
                    currentAccount.getAccountNumber(), toAccount, amount, description);
                
                JOptionPane.showMessageDialog(this, 
                    "Transfer successful!\nAmount: $" + result.getAmount() + 
                    "\nNew Balance: $" + result.getFromAccount().getBalance(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Update current account with new balance
                currentAccount = result.getFromAccount();
                updateDashboard();
                showDashboard();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter a valid amount", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        transferPanel.add(toAccountLabel);
        transferPanel.add(Box.createVerticalStrut(8));
        transferPanel.add(toAccountField);
        transferPanel.add(Box.createVerticalStrut(20));
        transferPanel.add(amountLabel);
        transferPanel.add(Box.createVerticalStrut(8));
        transferPanel.add(amountField);
        transferPanel.add(Box.createVerticalStrut(20));
        transferPanel.add(descLabel);
        transferPanel.add(Box.createVerticalStrut(8));
        transferPanel.add(descField);
        transferPanel.add(Box.createVerticalStrut(30));
        transferPanel.add(transferButton);
        
        contentPanel.add(transferPanel, "TRANSFER");
    }
    
    private void createPinPanel(JPanel contentPanel) {
        JPanel pinPanel = createCard("Update Account PIN");
        pinPanel.setLayout(new BoxLayout(pinPanel, BoxLayout.Y_AXIS));
        
        JLabel currentPinLabel = new JLabel("Current PIN:");
        currentPinLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        if (pinIcon != null) {
            currentPinLabel.setIcon(pinIcon);
            currentPinLabel.setIconTextGap(8);
        }
        
        JPasswordField currentPinField = new JPasswordField(20);
        currentPinField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        currentPinField.setMaximumSize(new Dimension(400, 40));
        currentPinField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        JLabel newPinLabel = new JLabel("New PIN (4 digits):");
        newPinLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JPasswordField newPinField = new JPasswordField(20);
        newPinField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        newPinField.setMaximumSize(new Dimension(400, 40));
        newPinField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        JButton updateButton = createEnhancedButton("Update PIN", SUCCESS_COLOR, SUCCESS_DARK);
        if (pinIcon != null) {
            updateButton.setIcon(pinIcon);
            updateButton.setIconTextGap(8);
        }
        
        updateButton.addActionListener(e -> {
            try {
                String currentPin = new String(currentPinField.getPassword());
                String newPin = new String(newPinField.getPassword());
                
                boolean success = bankingService.updatePin(
                    currentAccount.getAccountNumber(), currentPin, newPin);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, 
                        "PIN updated successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    showDashboard();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Failed to update PIN", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        pinPanel.add(currentPinLabel);
        pinPanel.add(Box.createVerticalStrut(8));
        pinPanel.add(currentPinField);
        pinPanel.add(Box.createVerticalStrut(20));
        pinPanel.add(newPinLabel);
        pinPanel.add(Box.createVerticalStrut(8));
        pinPanel.add(newPinField);
        pinPanel.add(Box.createVerticalStrut(30));
        pinPanel.add(updateButton);
        
        contentPanel.add(pinPanel, "PIN");
    }
    
    private void createHistoryPanel(JPanel contentPanel) {
        JPanel historyPanel = createCard("Transaction History");
        historyPanel.setLayout(new BorderLayout());
        
        String[] columnNames = {"Transaction ID", "Amount", "Transaction Type", "Transaction Date", "Source Account", "Target Account"};
        if (transactionTableModel == null) {
            transactionTableModel = new DefaultTableModel(columnNames, 0);
        }
        transactionTable = new JTable(transactionTableModel);
        transactionTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        transactionTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        transactionTable.setRowHeight(30);
        
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setPreferredSize(new Dimension(0, 400));
        
        JButton refreshButton = createEnhancedButton("Refresh", PRIMARY_COLOR, PRIMARY_DARK);
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refreshButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        if (historyIcon != null) {
            refreshButton.setIcon(historyIcon);
            refreshButton.setIconTextGap(8);
        }
        refreshButton.addActionListener(e -> loadTransactionHistory());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(refreshButton);
        
        historyPanel.add(scrollPane, BorderLayout.CENTER);
        historyPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        contentPanel.add(historyPanel, "HISTORY");
    }
    
    private void login() {
        String accountNumber = accountNumberField.getText().trim();
        String pin = new String(pinField.getPassword());
        
        if (accountNumber.isEmpty() || pin.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter account number and PIN", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Verify account exists and PIN is correct
            if (!bankingService.verifyPin(accountNumber, pin)) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid account number or PIN", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Get account and customer details
            currentAccount = bankingService.getAccountDetails(accountNumber);
            currentCustomer = bankingService.getCustomerDetails(currentAccount.getCustomerId());
            
            updateDashboard();
            showMainApplication();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void logout() {
        currentAccount = null;
        currentCustomer = null;
        accountNumberField.setText("");
        pinField.setText("");
        showLoginPanel();
    }
    
    private void updateDashboard() {
        if (currentAccount != null && currentCustomer != null) {
            // Refresh account data from database
            try {
                currentAccount = bankingService.getAccountDetails(currentAccount.getAccountNumber());
                currentCustomer = bankingService.getCustomerDetails(currentAccount.getCustomerId());
            } catch (Exception e) {
                System.out.println("Error refreshing account data: " + e.getMessage());
            }
            
            welcomeLabel.setText("Hi, " + currentCustomer.getFullName());
            balanceLabel.setText("$" + currentAccount.getBalance());
            accountNumberLabel.setText("Account Number: " + currentAccount.getAccountNumber());
            accountTypeLabel.setText("Account Type: " + currentAccount.getAccountType());
        }
    }
    
    private void loadTransactionHistory() {
        if (currentAccount == null) {
            return;
        }
        
        try {
            List<Transaction> transactions = bankingService.getTransactionHistory(
                currentAccount.getAccountNumber());
            
            transactionTableModel.setRowCount(0);
            
            for (Transaction transaction : transactions) {
                Vector<Object> row = new Vector<>();
                row.add(transaction.getTransactionId());
                row.add("$" + transaction.getAmount());
                row.add(transaction.getTransactionType());
                row.add(transaction.getTransactionDate());
                row.add(transaction.getFromAccountId() != null ? 
                       transaction.getFromAccountId().toString() : "N/A");
                row.add(transaction.getToAccountId() != null ? 
                       transaction.getToAccountId().toString() : "N/A");
                transactionTableModel.addRow(row);
            }
            
        } catch (Exception e) {
            System.out.println("Error loading transaction history: " + e.getMessage());
            // Don't show error dialog for transaction history loading
        }
    }
    
    private void handleSidebarAction(int action) {
        switch (action) {
            case 0: // Dashboard
                showDashboard();
                break;
            case 1: // Deposit
                showDeposit();
                break;
            case 2: // Withdraw
                showWithdraw();
                break;
            case 3: // Transfer
                showTransfer();
                break;
            case 4: // PIN
                showPin();
                break;
            case 5: // History
                showHistory();
                loadTransactionHistory();
                break;
        }
    }
    
    private void showLoginPanel() {
        cardLayout.show(mainPanel, "LOGIN");
    }
    
    private void showMainApplication() {
        cardLayout.show(mainPanel, "MAIN_APP");
        showDashboard();
    }
    
    private void showDashboard() {
        JPanel mainAppPanel = (JPanel) mainPanel.getComponent(1);
        JPanel contentPanel = (JPanel) mainAppPanel.getComponent(2); // Content panel is the 3rd component
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "DASHBOARD");
        
        // Update dashboard and load transaction history
        updateDashboard();
        loadTransactionHistory();
    }
    
    private void showDeposit() {
        JPanel mainAppPanel = (JPanel) mainPanel.getComponent(1);
        JPanel contentPanel = (JPanel) mainAppPanel.getComponent(2);
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "DEPOSIT");
    }
    
    private void showWithdraw() {
        JPanel mainAppPanel = (JPanel) mainPanel.getComponent(1);
        JPanel contentPanel = (JPanel) mainAppPanel.getComponent(2);
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "WITHDRAW");
    }
    
    private void showTransfer() {
        JPanel mainAppPanel = (JPanel) mainPanel.getComponent(1);
        JPanel contentPanel = (JPanel) mainAppPanel.getComponent(2);
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "TRANSFER");
    }
    
    private void showPin() {
        JPanel mainAppPanel = (JPanel) mainPanel.getComponent(1);
        JPanel contentPanel = (JPanel) mainAppPanel.getComponent(2);
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "PIN");
    }
    
    private void showHistory() {
        JPanel mainAppPanel = (JPanel) mainPanel.getComponent(1);
        JPanel contentPanel = (JPanel) mainAppPanel.getComponent(2);
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "HISTORY");
    }
    
    public static void main(String[] args) {
        // Test database connection
        if (!DatabaseConnection.testConnection()) {
            JOptionPane.showMessageDialog(null, 
                "Cannot connect to database!\nPlease ensure MySQL is running and database is set up correctly.",
                "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create and show GUI
        SwingUtilities.invokeLater(() -> {
            BankingSystemGUI gui = new BankingSystemGUI();
            gui.setVisible(true);
        });
    }
}
