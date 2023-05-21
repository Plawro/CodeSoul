import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

import org.fife.ui.rsyntaxtextarea.*;


public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private int menuIndex; //Přidat, že se dá přesouvat šipkama
    private boolean admin = false;

    private int numberOfCategories;
    private int currentNumberOfCategories;
    int currentLineNumber;
    int numberOfDollars;
    String[] starterTexts = {"CodeSoul","Verze: Release 1.3.0 (R1V3)\nDeveloper: Plawro\nPoužité externí knihovny: RSyntaxTextArea\nPoužité zdroje: Vlastní projekty, Imagine - logo","Rychlý průvodce:\nV hlavní menu najdete nabídku Java komponentů. \nKliknutím na konkrétní položku se zobrazí podrobný tutoriál, jak \ndaný komponent používat. Vybrat si jiný komponent můžete vždy, kdy potřebujete. \nPokud potřeba, kód se dá zkopírovat, nebo upravit podle potřeby."};

    JTextArea textArea1;
    JPanel panel1;
    JScrollPane scrollPane2;
    JPanel gapPanel;
    JTextArea textArea;
    JScrollPane scrollPane1;
    JLabel label1;
    public MainFrame() throws IOException {
        setTitle(starterTexts[0]);
        setSize(640, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        InputStream inputStream = getClass().getResourceAsStream("/resources/logo.png");
        Image image = ImageIO.read(inputStream);
        setIconImage(image);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        setBackground(Color.gray);
        add(mainPanel);

        RoundMenuBar menuBar = new RoundMenuBar();
        setJMenuBar(menuBar);

        panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
        label1 = new JLabel();
        label1.setFont(new Font("Roboto", Font.BOLD, 13));
        label1.setText("Vítejte v " + starterTexts[0] + "u");
        panel1.add(label1);
        textArea1 = new JTextArea();
        textArea1.setText(starterTexts[1]);
        textArea1.setEditable(false);
        textArea1.setFont(new Font("Roboto", Font.PLAIN, 13));
        JLabel label3 = new JLabel();
        label3.setFont(new Font("Roboto", Font.BOLD, 13));
        label3.setText("Procvičovací projekty zde");
        label3.setForeground(Color.BLUE.darker());
        label3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label3.addMouseListener(new MouseAdapter() {

                                    @Override
                                    public void mouseClicked(MouseEvent e) {
                                        try {

                                            Desktop.getDesktop().browse(new URI("https://github.com/stars/Plawro/lists/programov%C3%A1n%C3%AD"));

                                        } catch (IOException | URISyntaxException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                });
        panel1.add(label3);
        scrollPane2 = new JScrollPane(textArea1);
        scrollPane2.setBorder(BorderFactory.createMatteBorder(2, 10, 2, 10,Color.gray));
        panel1.add(scrollPane2);

        panel1.add(label3);

        //gapPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        //panel1.add(gapPanel, BorderLayout.SOUTH);
        textArea = new JTextArea();
        textArea.setText(starterTexts[2]);
        textArea.setEditable(false);
        scrollPane1 = new JScrollPane(textArea);
        scrollPane1.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        panel1.add(scrollPane1);

        mainPanel.removeAll();
        mainPanel.add(panel1);
        mainPanel.revalidate();
        mainPanel.repaint();



        ArrayList<JMenu> menus = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/resources/data.txt")))) {
            String line;
            JMenu currentMenu = null;

            while ((line = br.readLine()) != null) {

                if (line.equals("$")) {
                    currentMenu = null;
                } else if (currentMenu == null) {

                    currentNumberOfCategories++;
                    if(line.equals("|")){
                        numberOfCategories = currentNumberOfCategories +1;
                    }

                    currentMenu = new JMenu(line);
                    menus.add(currentMenu);
                    if(currentNumberOfCategories == numberOfCategories){
                        menuBar.add(Box.createHorizontalGlue());
                    }

                    if(!line.equals("|")) {
                        menuBar.add(currentMenu);
                    }

                } else{

                        String[] parts = line.split("€");

                        JMenuItem item = new JMenuItem(parts[0]);

                        if(parts[0].equals("Data editor")){

                            if(admin) {
                                item.addActionListener(e -> {
                                    try {
                                        editorInitiate();
                                    } catch (IOException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                });
                                currentMenu.add(item);
                            }
                        }else if(parts[0].equals("O programu")){

                            item.addActionListener(e -> {
                                showStarterInfo(this);
                            });
                            currentMenu.add(item);

                    }else if(parts[0].equals("Procvičovací projekty")){
                            item.addActionListener(e -> {
                                try {
                                    Desktop.getDesktop().browse(new URI("https://github.com/stars/Plawro/lists/programov%C3%A1n%C3%AD"));
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                } catch (URISyntaxException ex) {
                                    throw new RuntimeException(ex);
                                }
                            });
                            currentMenu.add(item);
                        }else {
                            item.addActionListener(e -> {
                                label1.setText(parts[0]);
                                textArea1.setText(parts[1].replace("\\n", "\n"));//Zmena "uzitecnych" dat na "ridici" - transparance :)
                                textArea1.setEditable(false);
                                textArea1.setFont(new Font("Roboto", Font.PLAIN, 13));
                                JPanel panel = new JPanel();
                                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                                panel.add(label1);
                                textArea1.setBorder(BorderFactory.createMatteBorder(5, 10, 5, 10, Color.gray));
                                JScrollPane scrollPane0 = new JScrollPane(textArea1);
                                scrollPane0.getVerticalScrollBar().setUI(new CustomScrollBar());
                                panel.add(scrollPane0);

                                RSyntaxTextArea syntaxTextArea = new RSyntaxTextArea();
                                syntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
                                syntaxTextArea.setText(parts[2].replace("\\n", "\n"));
                                JScrollPane scrollPane = new JScrollPane(syntaxTextArea);
                                scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
                                scrollPane.getVerticalScrollBar().setUI(new CustomScrollBar());
                                panel.add(scrollPane);
                                scrollPane.setEnabled(false);

                                mainPanel.removeAll();
                                mainPanel.add(panel);
                                mainPanel.revalidate();
                                mainPanel.repaint();
                            });
                            currentMenu.add(item);

                        }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        MainFrame frame = new MainFrame();
        frame.setVisible(true);
    }


    int lineNumber;
    public void editorInitiate() throws IOException {
        JFrame fr = new JFrame("Editor");
        fr.setVisible(true);
        fr.setTitle( starterTexts[0] + " editor beta");
        fr.setSize(640, 640);
        fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel1 = new JPanel();
        fr.add(mainPanel1);

        mainPanel1.setLayout(new BoxLayout(mainPanel1, BoxLayout.Y_AXIS));
        mainPanel1.setBackground(Color.gray);

        JTextArea newTextArea3 = new JTextArea();
        newTextArea3.setText("Jak použít " + starterTexts[0] + " editor:\n1.) V menu vyberte element, který chcete upravit\n2.) Nastaví se text, podle toho, co je nastaveno v souboru (automaticky)\n3.) Upravte, co potřebujete upravit\n(tlačítko Aktualizovat UI upraví text boxy, aby šel správně vidět text)\n4.) Před uložením, zjistěte ID elementu (v menu vedle názvu elementu)\na napište to ID do textboxu pod název elementu\n5.) Uložte tlačítkem 'Uložit'\n6.) Zavřete editor tlačítkem 'Zavřít'");
        newTextArea3.setFont(new Font("Roboto", Font.PLAIN, 13));
        newTextArea3.setBorder(BorderFactory.createMatteBorder(5, 10, 5, 10, Color.gray));
        newTextArea3.setEditable(false);
        JScrollPane newScrollPane3 = new JScrollPane(newTextArea3);
        newScrollPane3.getVerticalScrollBar().setUI(new CustomScrollBar());
        newScrollPane3.setEnabled(false);
        mainPanel1.add(newScrollPane3);
        JButton btn7 = new JButton("Zavřít");
        mainPanel1.add(btn7);
        btn7.addActionListener(ex -> fr.dispose());


        RoundMenuBar menuBar = new RoundMenuBar();
        fr.setJMenuBar(menuBar);

        ArrayList<JMenu> menus = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/resources/data.txt")))) {
            String line;
            JMenu currentMenu = null;

            while ((line = br.readLine()) != null) {
                numberOfDollars++;
                if (line.equals("$")) {

                    currentMenu = null;
                } else if (currentMenu == null) {

                    currentNumberOfCategories++;
                    if(line.equals("|")){
                        numberOfCategories = currentNumberOfCategories +1;
                    }

                    currentMenu = new JMenu(line);
                    menus.add(currentMenu);
                    if(currentNumberOfCategories == numberOfCategories){
                        menuBar.add(Box.createHorizontalGlue());
                    }

                    if(!line.equals("|")) {
                        menuBar.add(currentMenu);
                    }

                } else {
                    String[] parts_1 = line.split("€");

                    JMenuItem item = new JMenuItem(parts_1[0] + " "+numberOfDollars);


                    if(parts_1[0].equals("Data editor")){


                        item.addActionListener(e -> {
                            try {
                                editorInitiate();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        });
                        currentMenu.add(item);
                    }else {

                        item.addActionListener(e -> {
                            mainPanel1.removeAll();
                            mainPanel1.revalidate();
                            mainPanel1.repaint();
                            JTextField newLabel = new JTextField(parts_1[0]);
                            JTextField newLabel1 = new JTextField();

                            JTextArea newTextArea = new JTextArea(parts_1[1].replace("\\n", "\n"));
                            newTextArea.setFont(new Font("Roboto", Font.PLAIN, 13));
                            newTextArea.setBorder(BorderFactory.createMatteBorder(5, 10, 5, 10, Color.gray));
                            JScrollPane newScrollPane = new JScrollPane(newTextArea);
                            newScrollPane.getVerticalScrollBar().setUI(new CustomScrollBar());

                            RSyntaxTextArea newSyntaxTextArea = new RSyntaxTextArea();
                            newSyntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
                            newSyntaxTextArea.setText(parts_1[2].replace("\\n", "\n"));
                            JScrollPane newSyntaxScrollPane = new JScrollPane(newSyntaxTextArea);
                            newSyntaxScrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
                            newSyntaxScrollPane.getVerticalScrollBar().setUI(new CustomScrollBar());
                            JPanel buttonPanel = new JPanel(new FlowLayout());
                            JButton btn1 = new JButton("Aktualizovat UI");
                            JButton btn2 = new JButton("Uložit");
                            JButton btn3 = new JButton("Zavřít");


                            mainPanel1.add(newLabel);
                            mainPanel1.add(newLabel1);
                            mainPanel1.add(newScrollPane);
                            mainPanel1.add(newSyntaxScrollPane);

                            btn1.addActionListener(ex -> do1(mainPanel1));
                            btn2.addActionListener(ex -> {
                                try {
                                    do2(Integer.parseInt(newLabel1.getText()),newLabel.getText()+"€"+newTextArea.getText()+"€"+newSyntaxTextArea.getText());
                                } catch (NumberFormatException | IOException exc) {
                                    JOptionPane.showMessageDialog(this, "Není zadáno UI elementu (druhý řádek, ten po názvu elementu)");
                                }
                            });
                            btn3.addActionListener(ex -> fr.dispose());
                            buttonPanel.add(btn1);
                            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
                            buttonPanel.add(btn2);
                            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
                            buttonPanel.add(btn3);
                            mainPanel1.add(buttonPanel);

                            mainPanel1.revalidate();
                            mainPanel1.repaint();
                        });
                        currentMenu.add(item);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void do2(int lineNum, String newLine) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(String.valueOf(new InputStreamReader(getClass().getResourceAsStream("/resources/data.txt")))), StandardCharsets.UTF_8);

        if (lineNum >= 0 && lineNum < lines.size()) {
            lines.set(lineNum-1, newLine.replace("\n", "\\n"));
        } else if(String.valueOf(lineNum).equals("")) {
            JOptionPane.showMessageDialog(this, "Žádné zadané UI elementu (druhý řádek)");
        }else {
            JOptionPane.showMessageDialog(this, "Nesprávné zadané UI elementu (druhý řádek)");
        }

        try (PrintWriter writer = new PrintWriter(String.valueOf(new InputStreamReader(getClass().getResourceAsStream("/resources/data.txt"))))) {
            for (String line : lines) {
                writer.println(line);
            }
        }

    }

    void do1(JPanel mainPanel2){
        mainPanel2.revalidate();
        mainPanel2.repaint();
    }

    void showStarterInfo(JFrame frame){
        label1.setText("Vítejte v " + starterTexts[0] + "u");
        textArea1.setText(starterTexts[1]);
        textArea1.setEditable(false);
        textArea1.setFont(new Font("Roboto", Font.PLAIN, 13));
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(label1);
        textArea1.setBorder(BorderFactory.createMatteBorder(5, 10, 5, 10, Color.gray));
        JScrollPane scrollPane0 = new JScrollPane(textArea1);
        scrollPane0.getVerticalScrollBar().setUI(new CustomScrollBar());
        panel.add(scrollPane0);

        JTextArea textArea2 = new JTextArea(starterTexts[2]);
        JScrollPane scrollPane = new JScrollPane(textArea2);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBar());
        panel.add(scrollPane);
        scrollPane.setEnabled(false);

        mainPanel.removeAll();
        mainPanel.add(panel);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

}
class RoundMenuBar extends JMenuBar {
    Color clr1 = new ColorUIResource(75, 0, 130);
    Color clr2 = Color.orange;

    public RoundMenuBar() {


        setOpaque(true);
        setBackground(new Color(75, 0, 130));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        UIManager.put("Menu.background", clr1);
        UIManager.put("Menu.foreground", clr2);
        UIManager.put("Menu.selectionBackground", clr2);
        UIManager.put("Menu.selectionForeground", clr1);
        UIManager.put("MenuItem.background", clr1);
        UIManager.put("MenuItem.foreground", clr2);
        UIManager.put("MenuItem.selectionBackground", clr2);
        UIManager.put("MenuItem.selectionForeground", clr1);
        UIManager.put("MenuItem.borderPainted", false);
        UIManager.put("MenuItem.border", BorderFactory.createEmptyBorder(2, 10, 2, 10));
        UIManager.put("MenuItem.margin", new InsetsUIResource(0, 0, 0, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        g2.dispose();
        super.paintComponent(g);
    }
}

class CustomScrollBar extends BasicScrollBarUI {

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.gray); // set the thumb color
        g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10); // draw the rounded rectangle thumb
        g2.dispose();
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    private JButton createZeroButton() {
        JButton button = new JButton();
        Dimension zeroDim = new Dimension(0, 0);
        button.setPreferredSize(zeroDim);
        button.setMinimumSize(zeroDim);
        button.setMaximumSize(zeroDim);
        return button;
    }
    public CustomScrollBar() {
        UIManager.put("ScrollBar.width", 10);
    }

}

