package com.vityarthi.library;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Main application entry point and UI Launcher.
 * Features:
 * - 100% Guaranteed High-Contrast: NO white-on-white boxes, all buttons and text immediately visible.
 * - Interactive 3D Animated Floating Book Widget with real-time perspective projection.
 * - Tactile 3D Buttons with click depth, bevel, and hover feedback.
 * - Modern SaaS KPI Metrics cards, pill badge tables, and instant live search.
 */
public class Main extends JFrame {

    // Palette Constants
    public static final Color COLOR_PRIMARY = new Color(0x02, 0x84, 0xC7);       // #0284C7 Light/Sky Blue
    public static final Color COLOR_PRIMARY_DARK = new Color(0x03, 0x69, 0xA1);  // #0369A1 Deep Blue
    public static final Color COLOR_PRIMARY_HOVER = new Color(0x38, 0xBD, 0xF8); // #38BDF8 Bright Sky
    public static final Color COLOR_PRIMARY_TINT = new Color(0xE0, 0xF2, 0xFE);  // Soft sky tint

    public static final Color COLOR_YELLOW = new Color(0xFA, 0xCC, 0x15);        // #FACC15 Warm Yellow
    public static final Color COLOR_YELLOW_DARK = new Color(0xCA, 0x8A, 0x04);   // #CA8A04 Amber
    public static final Color COLOR_YELLOW_BG = new Color(0xFE, 0xF9, 0xC3);     // #FEF9C3 Soft Yellow

    public static final Color COLOR_BG = new Color(0xF8, 0xFA, 0xFC);           // #F8FAFC Light Slate
    public static final Color COLOR_CARD = new Color(0xFF, 0xFF, 0xFF);         // #FFFFFF Pure White
    public static final Color COLOR_BORDER = new Color(0xCB, 0xD5, 0xE1);       // #CBD5E1 Slate 300
    public static final Color COLOR_TEXT_DARK = new Color(0x0F, 0x17, 0x2A);    // #0F172A Slate 900
    public static final Color COLOR_TEXT_MUTED = new Color(0x47, 0x55, 0x69);   // #475569 Slate 600

    public static final Color COLOR_SUCCESS_BG = new Color(0xDC, 0xFC, 0xE7);
    public static final Color COLOR_SUCCESS_TEXT = new Color(0x14, 0x53, 0x2D); // Deep forest green
    public static final Color COLOR_DANGER_BG = new Color(0xFE, 0xE2, 0xE2);
    public static final Color COLOR_DANGER_TEXT = new Color(0x7F, 0x1D, 0x1D);  // Deep dark red
    public static final Color COLOR_PURPLE_BG = new Color(0xF3, 0xE8, 0xFF);
    public static final Color COLOR_PURPLE_TEXT = new Color(0x58, 0x1C, 0x87);

    private final BookRepository bookRepo;
    private final MemberRepository memberRepo;
    private final TransactionRepository transactionRepo;
    private final FineCalculatorService fineService;

    // Swing UI Components
    private JTable bookTable;
    private DefaultTableModel bookTableModel;
    private TableRowSorter<DefaultTableModel> bookSorter;

    private JTable memberTable;
    private DefaultTableModel memberTableModel;
    private TableRowSorter<DefaultTableModel> memberSorter;

    private JTable transactionTable;
    private DefaultTableModel transactionTableModel;
    private TableRowSorter<DefaultTableModel> transactionSorter;

    // KPI Metric Labels
    private JLabel statBooksVal;
    private JLabel statCopiesVal;
    private JLabel statMembersVal;
    private JLabel statLoansVal;

    public Main() {
        // Initialize services
        DatabaseManager.getInstance();
        this.bookRepo = new BookRepository();
        this.memberRepo = new MemberRepository();
        this.transactionRepo = new TransactionRepository();
        this.fineService = new FineCalculatorService();

        seedInitialDataIfEmpty();

        // Frame Properties
        setTitle("VIT Library Management System");
        setSize(1280, 860);
        setMinimumSize(new Dimension(1100, 740));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BG);
        setLayout(new BorderLayout());

        // Top Header Banner with 3D Animated Book
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Center Container
        JPanel centerContainer = new JPanel(new BorderLayout(0, 12));
        centerContainer.setBackground(COLOR_BG);
        centerContainer.setBorder(new EmptyBorder(12, 16, 12, 16));

        // KPI Metric Cards
        centerContainer.add(createMetricsPanel(), BorderLayout.NORTH);

        // Tabbed Pane with high contrast styling
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setForeground(COLOR_TEXT_DARK);

        tabbedPane.addTab("  📚 Book Catalog  ", createBooksPanel());
        tabbedPane.addTab("  👥 Members Directory  ", createMembersPanel());
        tabbedPane.addTab("  🔄 Issue & Return Workbench  ", createTransactionsPanel());
        tabbedPane.addTab("  ⚠️ Overdue & Fine Calculator  ", createFinesPanel());

        centerContainer.add(tabbedPane, BorderLayout.CENTER);
        add(centerContainer, BorderLayout.CENTER);

        // Status Bar
        add(createStatusBarPanel(), BorderLayout.SOUTH);

        // Load initial data
        refreshAllData();
    }

    /**
     * Top Header with Gradient Background & Interactive 3D Animated Book Widget
     */
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, COLOR_PRIMARY_DARK, getWidth(), getHeight(), COLOR_PRIMARY);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        header.setBorder(new EmptyBorder(12, 24, 12, 24));

        // Left Branding
        JPanel leftBrand = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 8));
        leftBrand.setOpaque(false);

        JLabel logoIcon = new JLabel("🏛️");
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("VIT Library Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Enterprise Resource & Circulation Dashboard • Dynamic Fine Engine • Real-Time Inventory");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(224, 242, 254));

        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        leftBrand.add(logoIcon);
        leftBrand.add(textPanel);

        // Right side: Interactive 3D Animated Rotating Book Widget!
        JPanel rightAnimationContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rightAnimationContainer.setOpaque(false);

        Book3DAnimationPanel book3D = new Book3DAnimationPanel();
        rightAnimationContainer.add(book3D);

        header.add(leftBrand, BorderLayout.WEST);
        header.add(rightAnimationContainer, BorderLayout.EAST);
        return header;
    }

    /**
     * KPI Stat Metric Cards
     */
    private JPanel createMetricsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 14, 0));
        panel.setOpaque(false);

        statBooksVal = new JLabel("0", SwingConstants.CENTER);
        statCopiesVal = new JLabel("0", SwingConstants.CENTER);
        statMembersVal = new JLabel("0", SwingConstants.CENTER);
        statLoansVal = new JLabel("0", SwingConstants.CENTER);

        panel.add(createMetricCard("📚 Total Book Titles", statBooksVal, COLOR_PRIMARY));
        panel.add(createMetricCard("📦 Available Copies", statCopiesVal, new Color(0x05, 0x96, 0x69))); // Emerald
        panel.add(createMetricCard("👥 Registered Members", statMembersVal, new Color(0x7C, 0x3A, 0xED))); // Purple
        panel.add(createMetricCard("⚠️ Active Borrow Loans", statLoansVal, COLOR_YELLOW_DARK)); // Amber

        return panel;
    }

    private JPanel createMetricCard(String title, JLabel valLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(0, 4)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Top accent bar
                g2d.setColor(accentColor);
                g2d.fillRoundRect(0, 0, getWidth(), 4, 4, 4);
                g2d.dispose();
            }
        };
        card.setBackground(COLOR_CARD);
        card.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(12, 14, 12, 14)
        ));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLbl.setForeground(COLOR_TEXT_MUTED);

        valLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valLabel.setForeground(COLOR_TEXT_DARK);

        card.add(titleLbl, BorderLayout.NORTH);
        card.add(valLabel, BorderLayout.CENTER);
        return card;
    }

    /**
     * Books Catalog Panel with live search & 3D tactile buttons
     */
    private JPanel createBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(14, 14, 14, 14));

        // Toolbar
        JPanel toolbar = new JPanel(new BorderLayout(12, 0));
        toolbar.setOpaque(false);

        // Search Field
        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchBox.setOpaque(false);
        JLabel searchLbl = new JLabel("🔍 Search Catalog:");
        searchLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchLbl.setForeground(COLOR_TEXT_DARK);

        JTextField txtSearch = new JTextField(18);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setForeground(COLOR_TEXT_DARK);
        txtSearch.setBackground(Color.WHITE);
        txtSearch.setBorder(new CompoundBorder(new LineBorder(COLOR_BORDER, 1, true), new EmptyBorder(5, 8, 5, 8)));

        searchBox.add(searchLbl);
        searchBox.add(txtSearch);

        // 3D Tactile Buttons - Always visible!
        JPanel actionBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionBox.setOpaque(false);

        Modern3DButton btnAddBook = new Modern3DButton("➕ Add New Book", COLOR_PRIMARY, Color.WHITE);
        Modern3DButton btnRefresh = new Modern3DButton("🔄 Refresh List", new Color(0x33, 0x41, 0x55), Color.WHITE);

        actionBox.add(btnAddBook);
        actionBox.add(btnRefresh);

        toolbar.add(searchBox, BorderLayout.WEST);
        toolbar.add(actionBox, BorderLayout.EAST);
        panel.add(toolbar, BorderLayout.NORTH);

        // Table Setup
        String[] columns = {"ID", "Book Title", "Author", "ISBN", "Total Copies", "Available Copies", "Status"};
        bookTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        bookTable = createStyledTable(bookTableModel);
        bookSorter = new TableRowSorter<>(bookTableModel);
        bookTable.setRowSorter(bookSorter);

        bookTable.getColumnModel().getColumn(6).setCellRenderer(new BadgeCellRenderer());

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
            private void filter() {
                String text = txtSearch.getText().trim();
                if (text.isEmpty()) {
                    bookSorter.setRowFilter(null);
                } else {
                    bookSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        panel.add(new JScrollPane(bookTable), BorderLayout.CENTER);

        btnAddBook.addActionListener(e -> showAddBookDialog());
        btnRefresh.addActionListener(e -> refreshAllData());

        return panel;
    }

    /**
     * Members Directory Panel
     */
    private JPanel createMembersPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(14, 14, 14, 14));

        JPanel toolbar = new JPanel(new BorderLayout(12, 0));
        toolbar.setOpaque(false);

        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchBox.setOpaque(false);
        JLabel searchLbl = new JLabel("🔍 Search Members:");
        searchLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchLbl.setForeground(COLOR_TEXT_DARK);

        JTextField txtSearch = new JTextField(18);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setForeground(COLOR_TEXT_DARK);
        txtSearch.setBackground(Color.WHITE);
        txtSearch.setBorder(new CompoundBorder(new LineBorder(COLOR_BORDER, 1, true), new EmptyBorder(5, 8, 5, 8)));

        searchBox.add(searchLbl);
        searchBox.add(txtSearch);

        JPanel actionBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionBox.setOpaque(false);
        Modern3DButton btnAddMember = new Modern3DButton("👤 Register Member", COLOR_PRIMARY, Color.WHITE);
        Modern3DButton btnRefresh = new Modern3DButton("🔄 Refresh List", new Color(0x33, 0x41, 0x55), Color.WHITE);

        actionBox.add(btnAddMember);
        actionBox.add(btnRefresh);

        toolbar.add(searchBox, BorderLayout.WEST);
        toolbar.add(actionBox, BorderLayout.EAST);
        panel.add(toolbar, BorderLayout.NORTH);

        String[] columns = {"Member ID", "Full Name", "Email Address", "Member Type", "Max Loans Cap", "Daily Fine Rate"};
        memberTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        memberTable = createStyledTable(memberTableModel);
        memberSorter = new TableRowSorter<>(memberTableModel);
        memberTable.setRowSorter(memberSorter);

        memberTable.getColumnModel().getColumn(3).setCellRenderer(new BadgeCellRenderer());

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
            private void filter() {
                String text = txtSearch.getText().trim();
                if (text.isEmpty()) {
                    memberSorter.setRowFilter(null);
                } else {
                    memberSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        panel.add(new JScrollPane(memberTable), BorderLayout.CENTER);

        btnAddMember.addActionListener(e -> showAddMemberDialog());
        btnRefresh.addActionListener(e -> refreshAllData());

        return panel;
    }

    /**
     * Issue & Return Workbench
     */
    private JPanel createTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(14, 14, 14, 14));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        toolbar.setOpaque(false);

        Modern3DButton btnIssue = new Modern3DButton("📤 Issue Book Loan", COLOR_PRIMARY, Color.WHITE);
        Modern3DButton btnReturn = new Modern3DButton("📥 Return Book & Check Fine", new Color(0x05, 0x96, 0x69), Color.WHITE);
        Modern3DButton btnRefresh = new Modern3DButton("🔄 Refresh Transactions", new Color(0x33, 0x41, 0x55), Color.WHITE);

        toolbar.add(btnIssue);
        toolbar.add(btnReturn);
        toolbar.add(btnRefresh);
        panel.add(toolbar, BorderLayout.NORTH);

        String[] columns = {"Txn ID", "Book ID", "Borrower Member ID", "Issue Date", "Due Date", "Return Date", "Fine Assessed", "Loan Status"};
        transactionTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        transactionTable = createStyledTable(transactionTableModel);
        transactionSorter = new TableRowSorter<>(transactionTableModel);
        transactionTable.setRowSorter(transactionSorter);

        transactionTable.getColumnModel().getColumn(7).setCellRenderer(new BadgeCellRenderer());

        panel.add(new JScrollPane(transactionTable), BorderLayout.CENTER);

        btnIssue.addActionListener(e -> showIssueBookDialog());
        btnReturn.addActionListener(e -> showReturnBookDialog());
        btnRefresh.addActionListener(e -> refreshAllData());

        return panel;
    }

    /**
     * Overdue & Fine Calculator Panel
     */
    private JPanel createFinesPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Warm Yellow Notice Banner
        JPanel badgeBanner = new JPanel(new BorderLayout());
        badgeBanner.setBackground(COLOR_YELLOW_BG);
        badgeBanner.setBorder(new CompoundBorder(
                new LineBorder(COLOR_YELLOW, 2, true),
                new EmptyBorder(14, 18, 14, 18)
        ));

        JLabel badgeTitle = new JLabel("⚠️ OVERDUE FINE CALCULATION ENGINE & NOTICE WORKBENCH");
        badgeTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        badgeTitle.setForeground(new Color(0x85, 0x4D, 0x0E)); // Dark Amber

        JLabel badgeSub = new JLabel("Student Rate: $1.00 / day overdue  •  Faculty Rate: $0.50 / day overdue  •  14-day standard grace loan period");
        badgeSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        badgeSub.setForeground(new Color(0xA1, 0x62, 0x07));

        JPanel textBanner = new JPanel(new GridLayout(2, 1, 0, 4));
        textBanner.setOpaque(false);
        textBanner.add(badgeTitle);
        textBanner.add(badgeSub);
        badgeBanner.add(textBanner, BorderLayout.CENTER);

        panel.add(badgeBanner, BorderLayout.NORTH);

        // Center Card Form
        JPanel cardWrapper = new JPanel(new GridBagLayout());
        cardWrapper.setOpaque(false);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(COLOR_CARD);
        card.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(28, 36, 28, 36)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblMemberId = new JLabel("Member ID:");
        lblMemberId.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMemberId.setForeground(COLOR_TEXT_DARK);
        JTextField txtMemberId = new JTextField(16);
        txtMemberId.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtMemberId.setForeground(COLOR_TEXT_DARK);
        txtMemberId.setBackground(Color.WHITE);
        txtMemberId.setBorder(new CompoundBorder(new LineBorder(COLOR_BORDER, 1, true), new EmptyBorder(5, 8, 5, 8)));

        JLabel lblDueDate = new JLabel("Loan Due Date (YYYY-MM-DD):");
        lblDueDate.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDueDate.setForeground(COLOR_TEXT_DARK);
        JTextField txtDueDate = new JTextField(LocalDate.now().minusDays(5).toString(), 16);
        txtDueDate.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDueDate.setForeground(COLOR_TEXT_DARK);
        txtDueDate.setBackground(Color.WHITE);
        txtDueDate.setBorder(new CompoundBorder(new LineBorder(COLOR_BORDER, 1, true), new EmptyBorder(5, 8, 5, 8)));

        JLabel lblReturnDate = new JLabel("Check / Return Date:");
        lblReturnDate.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblReturnDate.setForeground(COLOR_TEXT_DARK);
        JTextField txtReturnDate = new JTextField(LocalDate.now().toString(), 16);
        txtReturnDate.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtReturnDate.setForeground(COLOR_TEXT_DARK);
        txtReturnDate.setBackground(Color.WHITE);
        txtReturnDate.setBorder(new CompoundBorder(new LineBorder(COLOR_BORDER, 1, true), new EmptyBorder(5, 8, 5, 8)));

        Modern3DButton btnCalc = new Modern3DButton("⚡ Calculate Outstanding Fine", COLOR_PRIMARY, Color.WHITE);

        // Result Box
        JPanel resultCard = new JPanel(new BorderLayout());
        resultCard.setBackground(COLOR_PRIMARY_TINT);
        resultCard.setBorder(new CompoundBorder(
                new LineBorder(COLOR_PRIMARY, 1, true),
                new EmptyBorder(14, 20, 14, 20)
        ));
        JLabel lblResult = new JLabel("Ready: Enter Member ID & Due Date above to calculate fine.", SwingConstants.CENTER);
        lblResult.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblResult.setForeground(COLOR_PRIMARY_DARK);
        resultCard.add(lblResult, BorderLayout.CENTER);

        gbc.gridx = 0; gbc.gridy = 0; card.add(lblMemberId, gbc);
        gbc.gridx = 1; gbc.gridy = 0; card.add(txtMemberId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; card.add(lblDueDate, gbc);
        gbc.gridx = 1; gbc.gridy = 1; card.add(txtDueDate, gbc);

        gbc.gridx = 0; gbc.gridy = 2; card.add(lblReturnDate, gbc);
        gbc.gridx = 1; gbc.gridy = 2; card.add(txtReturnDate, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; card.add(btnCalc, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; card.add(resultCard, gbc);

        cardWrapper.add(card);
        panel.add(cardWrapper, BorderLayout.CENTER);

        btnCalc.addActionListener(e -> {
            String mId = txtMemberId.getText().trim();
            String dueStr = txtDueDate.getText().trim();
            String retStr = txtReturnDate.getText().trim();

            if (!InputValidator.isValidDate(dueStr) || !InputValidator.isValidDate(retStr)) {
                JOptionPane.showMessageDialog(this, "Please enter valid dates in YYYY-MM-DD format.", "Invalid Date Format", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Member m = memberRepo.findById(mId);
            if (m == null) {
                JOptionPane.showMessageDialog(this, "Member ID '" + mId + "' was not found in the library database.", "Member Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }

            LocalDate dueDate = LocalDate.parse(dueStr);
            LocalDate returnDate = LocalDate.parse(retStr);

            long overdueDays = fineService.calculateOverdueDays(dueDate, returnDate);
            double fine = fineService.calculateFine(m, dueDate, returnDate);

            if (overdueDays > 0) {
                lblResult.setText(String.format("⚠️ %s (%s) • Overdue: %d days • Total Fine Due: $%.2f",
                        m.getName(), m.getMemberType(), overdueDays, fine));
                lblResult.setForeground(new Color(0x99, 0x1B, 0x1B));
                resultCard.setBackground(COLOR_YELLOW_BG);
                resultCard.setBorder(new LineBorder(COLOR_YELLOW_DARK, 1, true));
            } else {
                lblResult.setText(String.format("✅ %s (%s) • On-Time Return • No Fine Assessed ($0.00)",
                        m.getName(), m.getMemberType()));
                lblResult.setForeground(COLOR_SUCCESS_TEXT);
                resultCard.setBackground(COLOR_SUCCESS_BG);
                resultCard.setBorder(new LineBorder(COLOR_SUCCESS_TEXT, 1, true));
            }
        });

        return panel;
    }

    private JPanel createStatusBarPanel() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(COLOR_CARD);
        statusBar.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1),
                new EmptyBorder(8, 20, 8, 20)
        ));

        JLabel statusLabel = new JLabel("VIT Library Management System • Ready • SQLite Persistence Active • Standard Grace 14 Days");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(COLOR_TEXT_MUTED);

        statusBar.add(statusLabel, BorderLayout.WEST);
        return statusBar;
    }

    /**
     * Modern 3D Tactile Button Component
     * Completely custom painted: NEVER appears as a white box, 100% visible text at all times.
     */
    public static class Modern3DButton extends JButton {
        private final Color baseColor;
        private final Color shadowColor;
        private boolean isHovered = false;

        public Modern3DButton(String text, Color baseColor, Color textColor) {
            super(text);
            this.baseColor = baseColor;
            this.shadowColor = baseColor.darker();
            setForeground(textColor);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorder(new EmptyBorder(10, 20, 10, 20));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
                @Override
                public void mousePressed(MouseEvent e) {
                    repaint();
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            boolean pressed = getModel().isPressed();

            int depth = 4;
            int yOffset = pressed ? depth : 0;

            // 1. Draw 3D bottom bevel/shadow
            g2.setColor(shadowColor);
            g2.fillRoundRect(0, depth, w, h - depth, 10, 10);

            // 2. Draw Button surface
            Color surfaceColor = isHovered ? baseColor.brighter() : baseColor;
            g2.setColor(surfaceColor);
            g2.fillRoundRect(0, yOffset, w, h - depth, 10, 10);

            // 3. Top glossy highlight reflection
            if (!pressed) {
                g2.setColor(new Color(255, 255, 255, 60));
                g2.fillRoundRect(2, 2, w - 4, (h - depth) / 2, 8, 8);
            }

            // 4. Draw Text centered
            g2.setColor(getForeground());
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int textX = (w - fm.stringWidth(getText())) / 2;
            int textY = (h - depth - fm.getHeight()) / 2 + fm.getAscent() + yOffset;
            g2.drawString(getText(), textX, textY);

            g2.dispose();
        }
    }

    /**
     * Interactive 3D Animated Floating Book Widget
     * Projects and renders a rotating, floating 3D book in real-time with sparkles!
     */
    public static class Book3DAnimationPanel extends JPanel {
        private double angle = 0.0;
        private double bobOffset = 0.0;
        private final Timer timer;
        private final List<Point3D> particles = new ArrayList<>();

        public Book3DAnimationPanel() {
            setPreferredSize(new Dimension(140, 75));
            setOpaque(false);

            // Initialize floating sparkle particles
            for (int i = 0; i < 8; i++) {
                double a = i * (Math.PI / 4.0);
                particles.add(new Point3D(Math.cos(a) * 45, (i % 3 - 1) * 15, Math.sin(a) * 45));
            }

            // 30 FPS Smooth Render Loop
            timer = new Timer(33, e -> {
                angle += 0.035; // Gentle rotation speed
                bobOffset = Math.sin(angle * 2.0) * 4.0; // Gentle floating bob
                repaint();
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int cx = getWidth() / 2;
            int cy = getHeight() / 2 + (int) bobOffset + 4;

            // 1. Draw 3D Shadow on the floor
            int shadowW = (int) (65 + Math.cos(angle * 2.0) * 6);
            int shadowH = 14;
            g2.setColor(new Color(0, 0, 0, 45));
            g2.fillOval(cx - shadowW / 2, getHeight() - 14, shadowW, shadowH);

            // 2. 3D Model Parameters
            double bw = 32.0;  // Book half-width
            double bh = 42.0;  // Book half-height
            double bd = 12.0;  // Book half-thickness

            // Rotation angles
            double cosY = Math.cos(angle);
            double sinY = Math.sin(angle);
            double tiltX = 0.35; // Perspective tilt
            double cosX = Math.cos(tiltX);
            double sinX = Math.sin(tiltX);

            // Calculate 3D rotated vertices
            // Front cover corners
            Point pF1 = project(cx, cy, -bw, -bh, bd, cosY, sinY, cosX, sinX);
            Point pF2 = project(cx, cy,  bw, -bh, bd, cosY, sinY, cosX, sinX);
            Point pF3 = project(cx, cy,  bw,  bh, bd, cosY, sinY, cosX, sinX);
            Point pF4 = project(cx, cy, -bw,  bh, bd, cosY, sinY, cosX, sinX);

            // Back cover corners
            Point pB1 = project(cx, cy, -bw, -bh, -bd, cosY, sinY, cosX, sinX);
            Point pB2 = project(cx, cy,  bw, -bh, -bd, cosY, sinY, cosX, sinX);
            Point pB3 = project(cx, cy,  bw,  bh, -bd, cosY, sinY, cosX, sinX);
            Point pB4 = project(cx, cy, -bw,  bh, -bd, cosY, sinY, cosX, sinX);

            // Determine visibility using normal vector Z
            boolean frontVisible = (cosY > -0.2);

            // 3. Draw 3D Pages / Side
            Polygon pagesPoly = new Polygon();
            pagesPoly.addPoint(pF2.x, pF2.y);
            pagesPoly.addPoint(pB2.x, pB2.y);
            pagesPoly.addPoint(pB3.x, pB3.y);
            pagesPoly.addPoint(pF3.x, pF3.y);
            g2.setColor(new Color(248, 250, 252));
            g2.fillPolygon(pagesPoly);
            g2.setColor(new Color(203, 213, 225));
            g2.drawPolygon(pagesPoly);

            // Page texture lines
            g2.setColor(new Color(226, 232, 240));
            g2.drawLine((pF2.x + pB2.x) / 2, (pF2.y + pB2.y) / 2, (pF3.x + pB3.x) / 2, (pF3.y + pB3.y) / 2);

            // 4. Draw 3D Book Cover
            Polygon coverPoly = new Polygon();
            if (frontVisible) {
                coverPoly.addPoint(pF1.x, pF1.y);
                coverPoly.addPoint(pF2.x, pF2.y);
                coverPoly.addPoint(pF3.x, pF3.y);
                coverPoly.addPoint(pF4.x, pF4.y);

                // Gradient on cover
                g2.setPaint(new GradientPaint(pF1.x, pF1.y, new Color(0x38, 0xBD, 0xF8), pF3.x, pF3.y, new Color(0x02, 0x84, 0xC7)));
                g2.fillPolygon(coverPoly);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawPolygon(coverPoly);

                // Golden VIT Emblem Line on 3D Cover
                g2.setColor(COLOR_YELLOW);
                int midX1 = (pF1.x * 2 + pF2.x) / 3;
                int midY1 = (pF1.y * 2 + pF2.y) / 3;
                int midX2 = (pF4.x * 2 + pF3.x) / 3;
                int midY2 = (pF4.y * 2 + pF3.y) / 3;
                g2.drawLine(midX1, midY1, midX2, midY2);

                // Cover Title text in 3D
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 9));
                g2.drawString("VIT", (pF1.x + pF2.x) / 2 - 8, (pF1.y + pF3.y) / 2);
            } else {
                coverPoly.addPoint(pB1.x, pB1.y);
                coverPoly.addPoint(pB2.x, pB2.y);
                coverPoly.addPoint(pB3.x, pB3.y);
                coverPoly.addPoint(pB4.x, pB4.y);
                g2.setColor(new Color(0x03, 0x69, 0xA1));
                g2.fillPolygon(coverPoly);
                g2.setColor(new Color(0x02, 0x84, 0xC7));
                g2.drawPolygon(coverPoly);
            }

            // 5. Draw 3D Spine
            Polygon spinePoly = new Polygon();
            spinePoly.addPoint(pF1.x, pF1.y);
            spinePoly.addPoint(pB1.x, pB1.y);
            spinePoly.addPoint(pB4.x, pB4.y);
            spinePoly.addPoint(pF4.x, pF4.y);
            g2.setColor(new Color(0x07, 0x59, 0x85));
            g2.fillPolygon(spinePoly);
            g2.setColor(new Color(0x38, 0xBD, 0xF8));
            g2.drawPolygon(spinePoly);

            // 6. Draw Orbiting Sparkle Stars
            g2.setColor(COLOR_YELLOW);
            for (int i = 0; i < particles.size(); i++) {
                double pAng = angle * 1.5 + (i * Math.PI / 4.0);
                int px = cx + (int) (Math.cos(pAng) * 56);
                int py = cy + (int) (Math.sin(pAng) * 20 + Math.sin(pAng * 3) * 6);
                g2.fillOval(px - 2, py - 2, 4, 4);
            }

            g2.dispose();
        }

        private Point project(int cx, int cy, double x, double y, double z,
                              double cosY, double sinY, double cosX, double sinX) {
            // Y rotation
            double x1 = x * cosY - z * sinY;
            double z1 = x * sinY + z * cosY;
            // X tilt
            double y2 = y * cosX - z1 * sinX;
            return new Point(cx + (int) x1, cy + (int) y2);
        }

        private static class Point3D {
            double x, y, z;
            Point3D(double x, double y, double z) { this.x = x; this.y = y; this.z = z; }
        }
    }

    /**
     * Reusable Styled Table with Guaranteed Visible Dark Headers & Readable Text
     */
    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(38);
        table.setGridColor(COLOR_BORDER);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setSelectionBackground(COLOR_PRIMARY_TINT);
        table.setSelectionForeground(COLOR_TEXT_DARK);

        // Header Styling - Explicit Custom Renderer to prevent OS theme from painting white-on-white
        table.getTableHeader().setPreferredSize(new Dimension(0, 42));
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = new JLabel(value != null ? value.toString() : "");
                lbl.setOpaque(true);
                lbl.setBackground(COLOR_PRIMARY);        // Solid Deep Blue Background
                lbl.setForeground(Color.WHITE);          // Crisp White Text on Blue
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
                lbl.setHorizontalAlignment(CENTER);
                lbl.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 1, COLOR_PRIMARY_DARK),
                        BorderFactory.createEmptyBorder(8, 10, 8, 10)
                ));
                return lbl;
            }
        });

        // Default Cell Renderer for all columns (Ensures dark readable text, alternating row background)
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setOpaque(true);
                lbl.setHorizontalAlignment(CENTER);
                lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));

                if (isSelected) {
                    lbl.setBackground(COLOR_PRIMARY_TINT); // Light blue selection
                    lbl.setForeground(COLOR_PRIMARY_DARK); // Dark blue text
                } else {
                    lbl.setBackground(row % 2 == 0 ? Color.WHITE : COLOR_BG);
                    lbl.setForeground(COLOR_TEXT_DARK);    // Deep Slate text (#0F172A)
                }

                lbl.setBorder(new EmptyBorder(4, 8, 4, 8));
                return lbl;
            }
        };
        table.setDefaultRenderer(Object.class, cellRenderer);

        return table;
    }

    /**
     * Custom Pill Badge Cell Renderer for Status and Role Columns (High-Contrast)
     */
    private static class BadgeCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(CENTER);
            label.setOpaque(true);

            String text = value != null ? value.toString().toUpperCase() : "";

            if (text.contains("AVAILABLE") || text.contains("RETURNED")) {
                label.setBackground(COLOR_SUCCESS_BG);
                label.setForeground(COLOR_SUCCESS_TEXT);
                label.setText("● " + text);
            } else if (text.contains("OUT OF STOCK")) {
                label.setBackground(COLOR_DANGER_BG);
                label.setForeground(COLOR_DANGER_TEXT);
                label.setText("● " + text);
            } else if (text.contains("OVERDUE")) {
                label.setBackground(COLOR_YELLOW_BG);
                label.setForeground(new Color(0x85, 0x4D, 0x0E)); // Dark Amber text
                label.setText("⚠️ " + text);
            } else if (text.contains("FACULTY")) {
                label.setBackground(COLOR_PURPLE_BG);
                label.setForeground(COLOR_PURPLE_TEXT);
                label.setText("🎓 " + text);
            } else if (text.contains("STUDENT") || text.contains("ISSUED")) {
                label.setBackground(COLOR_PRIMARY_TINT);
                label.setForeground(COLOR_PRIMARY_DARK);
                label.setText("📘 " + text);
            } else {
                label.setBackground(isSelected ? COLOR_PRIMARY_TINT : Color.WHITE);
                label.setForeground(COLOR_TEXT_DARK);
            }

            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setBorder(new EmptyBorder(4, 10, 4, 10));
            return label;
        }
    }

    // ==========================================
    // Business Dialog Actions
    // ==========================================

    private void showAddBookDialog() {
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField isbnField = new JTextField();
        JTextField copiesField = new JTextField("5");

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("Book Title:")); panel.add(titleField);
        panel.add(new JLabel("Author:")); panel.add(authorField);
        panel.add(new JLabel("ISBN:")); panel.add(isbnField);
        panel.add(new JLabel("Total Copies:")); panel.add(copiesField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Book Entity", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String isbn = isbnField.getText().trim();
            String copiesStr = copiesField.getText().trim();

            if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in Title, Author, and ISBN.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int copies = Integer.parseInt(copiesStr);
                if (copies <= 0) {
                    JOptionPane.showMessageDialog(this, "Total copies must be greater than 0.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Book b = new Book(title, author, isbn, copies);
                if (bookRepo.addBook(b)) {
                    JOptionPane.showMessageDialog(this, "Book '" + title + "' successfully added to inventory!", "Book Added", JOptionPane.INFORMATION_MESSAGE);
                    refreshAllData();
                } else {
                    String reason = bookRepo.getLastError();
                    if (reason.toLowerCase().contains("unique") || reason.toLowerCase().contains("constraint")) {
                        JOptionPane.showMessageDialog(this, "A book with ISBN '" + isbn + "' already exists in the catalog.\nPlease enter a unique ISBN.", "Duplicate ISBN", JOptionPane.WARNING_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to save book.\n\nReason: " + reason, "Database Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Copies must be a valid positive integer.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showAddMemberDialog() {
        JTextField idField = new JTextField("STU-");
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"STUDENT", "FACULTY"});

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("Member ID (e.g. STU-101 / FAC-201):")); panel.add(idField);
        panel.add(new JLabel("Full Name:")); panel.add(nameField);
        panel.add(new JLabel("Email Address:")); panel.add(emailField);
        panel.add(new JLabel("Membership Category:")); panel.add(typeCombo);

        int result = JOptionPane.showConfirmDialog(this, panel, "Register New Library Member", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String type = (String) typeCombo.getSelectedItem();

            if (!InputValidator.isValidMemberId(id) || name.isEmpty() || !InputValidator.isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Invalid Member ID (must start with STU- or FAC-), Name, or Email.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Member m = "FACULTY".equals(type) ? new FacultyMember(id, name, email) : new StudentMember(id, name, email);
            if (memberRepo.addMember(m)) {
                JOptionPane.showMessageDialog(this, "Member '" + name + "' successfully registered!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshAllData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to register member (Duplicate Member ID?).", "Registration Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showIssueBookDialog() {
        JTextField bookIdField = new JTextField();
        JTextField memberIdField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Book ID:")); panel.add(bookIdField);
        panel.add(new JLabel("Member ID:")); panel.add(memberIdField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Issue Book Loan", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int bookId = Integer.parseInt(bookIdField.getText().trim());
                String memberId = memberIdField.getText().trim();

                Book b = bookRepo.findById(bookId);
                Member m = memberRepo.findById(memberId);

                if (b == null || m == null) {
                    JOptionPane.showMessageDialog(this, "Specified Book ID or Member ID does not exist.", "Invalid Target", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!b.isAvailable()) {
                    JOptionPane.showMessageDialog(this, "No available copies remaining for: " + b.getTitle(), "Out of Stock", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int activeLoans = transactionRepo.countActiveLoansByMember(memberId);
                if (!m.canBorrowMore(activeLoans)) {
                    JOptionPane.showMessageDialog(this, String.format("Member borrowing limit reached!\nMax allowed: %d books\nCurrent active loans: %d", m.getMaxBooks(), activeLoans), "Quota Exceeded", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                BorrowTransaction txn = new BorrowTransaction(bookId, memberId, LocalDate.now(), 14);
                if (transactionRepo.addTransaction(txn)) {
                    bookRepo.updateAvailableCopies(bookId, b.getAvailableCopies() - 1);
                    JOptionPane.showMessageDialog(this, "Book successfully issued!\nDue Date: " + txn.getDueDate(), "Transaction Completed", JOptionPane.INFORMATION_MESSAGE);
                    refreshAllData();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Book ID must be an integer.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showReturnBookDialog() {
        JTextField txnIdField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.add(new JLabel("Transaction ID:")); panel.add(txnIdField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Process Return & Assess Fine", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int txnId = Integer.parseInt(txnIdField.getText().trim());
                BorrowTransaction txn = transactionRepo.findById(txnId);

                if (txn == null || "RETURNED".equalsIgnoreCase(txn.getStatus())) {
                    JOptionPane.showMessageDialog(this, "Invalid transaction ID or transaction already returned.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Member m = memberRepo.findById(txn.getMemberId());
                Book b = bookRepo.findById(txn.getBookId());
                LocalDate returnDate = LocalDate.now();

                double fine = fineService.calculateFine(m, txn.getDueDate(), returnDate);

                if (transactionRepo.updateReturnAndFine(txnId, returnDate, fine, "RETURNED")) {
                    if (b != null) {
                        bookRepo.updateAvailableCopies(b.getBookId(), b.getAvailableCopies() + 1);
                    }
                    String msg = String.format("Book returned successfully!\nReturn Date: %s\nOverdue Fine Assessed: $%.2f", returnDate, fine);
                    JOptionPane.showMessageDialog(this, msg, "Return Complete", JOptionPane.INFORMATION_MESSAGE);
                    refreshAllData();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Transaction ID must be an integer.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshAllData() {
        // Refresh Books
        bookTableModel.setRowCount(0);
        List<Book> books = bookRepo.findAll();
        int totalAvailableCopies = 0;
        for (Book b : books) {
            totalAvailableCopies += b.getAvailableCopies();
            bookTableModel.addRow(new Object[]{
                    b.getBookId(), b.getTitle(), b.getAuthor(), b.getIsbn(),
                    b.getTotalCopies(), b.getAvailableCopies(),
                    b.isAvailable() ? "AVAILABLE" : "OUT OF STOCK"
            });
        }
        statBooksVal.setText(String.valueOf(books.size()));
        statCopiesVal.setText(String.valueOf(totalAvailableCopies));

        // Refresh Members
        memberTableModel.setRowCount(0);
        List<Member> members = memberRepo.findAll();
        for (Member m : members) {
            memberTableModel.addRow(new Object[]{
                    m.getMemberId(), m.getName(), m.getEmail(), m.getMemberType(),
                    m.getMaxBooks(), String.format("$%.2f", m.getFineRatePerDay())
            });
        }
        statMembersVal.setText(String.valueOf(members.size()));

        // Refresh Transactions
        transactionTableModel.setRowCount(0);
        List<BorrowTransaction> txns = transactionRepo.findAll();
        int activeLoansCount = 0;
        for (BorrowTransaction t : txns) {
            if ("ISSUED".equalsIgnoreCase(t.getStatus()) || "OVERDUE".equalsIgnoreCase(t.getStatus())) {
                activeLoansCount++;
            }
            transactionTableModel.addRow(new Object[]{
                    t.getTransactionId(), t.getBookId(), t.getMemberId(),
                    t.getIssueDate(), t.getDueDate(),
                    t.getReturnDate() != null ? t.getReturnDate() : "N/A",
                    String.format("$%.2f", t.getFineAmount()), t.getStatus()
            });
        }
        statLoansVal.setText(String.valueOf(activeLoansCount));
    }

    private void seedInitialDataIfEmpty() {
        if (bookRepo.findAll().isEmpty()) {
            bookRepo.addBook(new Book("Clean Code: Agile Software Craftsmanship", "Robert C. Martin", "978-0132350884", 5));
            bookRepo.addBook(new Book("Design Patterns: Elements of Reusable Software", "Erich Gamma et al.", "978-0201633610", 3));
            bookRepo.addBook(new Book("Effective Java (3rd Edition)", "Joshua Bloch", "978-0134685991", 4));
        }

        if (memberRepo.findAll().isEmpty()) {
            memberRepo.addMember(new StudentMember("STU-101", "Aarav Sharma", "aarav.sharma@vityarthi.edu"));
            memberRepo.addMember(new StudentMember("STU-102", "Priya Patel", "priya.patel@vityarthi.edu"));
            memberRepo.addMember(new FacultyMember("FAC-201", "Dr. Rajesh Kumar", "rajesh.kumar@vityarthi.edu"));
        }
    }

    public static void main(String[] args) {
        // Cross-platform anti-aliasing
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);
        });
    }
}
