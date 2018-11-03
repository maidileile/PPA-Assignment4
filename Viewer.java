import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * Write a description of class Viewer here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Viewer {

    // instance variables - replace the example below with your own
    private JFrame frame;
    private ArrayList<JPanel> panels;
    private WelcomePanel welcomePanel;
    private MapPanel mapPanel;
    private StatisticsPanel statisticsPanel;
    private SignIn_UpPanel signPanel;

    private JPanel contentPane;

    private int count;
    //count of number of panels
    private static int fromValue = -1;
    private static int toValue = -1;
    
    private ArrayList<AirbnbListing> listings;
    private ArrayList<AirbnbListing> selectListings;

    /**
     * Constructor for objects of class Viewer
     */
    public Viewer() {
        listings = new AirbnbDataLoader().load();
        selectListings = new ArrayList<>();

        panels = new ArrayList<>();
        welcomePanel = new WelcomePanel();
        mapPanel = new MapPanel();
        statisticsPanel = new StatisticsPanel(selectListings);
        signPanel = new SignIn_UpPanel();
        
        count = 0;
        createPanels();
        makeFrame();
        contentPane.add(panels.get(count), BorderLayout.CENTER);
        updatePanel();
    }

    private void createPanels() {
        panels.add(welcomePanel.getPanel());
        panels.add(mapPanel.getPanel());
        panels.add(statisticsPanel.getPanel());
        panels.add(signPanel.getPanel());
    }

    private void makeFrame() {

        frame = new JFrame("AirBnB London for assingment 4");
        contentPane = (JPanel) frame.getContentPane();
        contentPane.setLayout(new BorderLayout(5, 5));

        JPanel northPanel = new JPanel();
        contentPane.add(northPanel, BorderLayout.NORTH);

        GroupLayout layout = new GroupLayout(northPanel);
        northPanel.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JButton backButton = new JButton("<");
        backButton.setEnabled(false);
        //create back ubtton 
        JButton forwardButton = new JButton(">");
        forwardButton.setEnabled(false);
        //create forward button
        JLabel from = new JLabel("From:");
        //create label--string from
        JLabel to = new JLabel("To:");
        //create label--string to
        JComboBox<Integer> fromPrice = new JComboBox<>();
        JComboBox<Integer> toPrice = new JComboBox<>();
        //two combo box are initilised in integer, must integers in box.
        fromPrice.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if ((e.getStateChange() == ItemEvent.SELECTED)) {
                        if (e.getItem() == null) return;
                        int selectPrice = (int) e.getItem();
                        if (toValue != -1){
                            if (selectPrice > toValue) {
                                fromPrice.setSelectedIndex(-1);
                                JOptionPane.showMessageDialog(null, "Select a minimum price <= maximum price");
                                return;
                            }
                        }
                        fromValue = selectPrice;
                        if (toValue != -1){
                            selectListings();
                        }
                        welcomePanel.updateWelcome();
                    }
                }
            });
        //make the values of from combo box smaller than values of to combo box
        toPrice.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if ((e.getStateChange() == ItemEvent.SELECTED)) {
                        if (e.getItem() == null) return;
                        int selectPrice = (int) e.getItem();
                        if (fromValue != -1){
                            if (selectPrice < fromValue) {
                                toPrice.setSelectedIndex(-1);
                                JOptionPane.showMessageDialog(null, "Select a maximum price >= minimum price");
                                selectListings.clear();
                                backButton.setEnabled(false);
                                forwardButton.setEnabled(false);
                                return;
                            }
                        }
                        toValue = selectPrice;
                        if (fromValue != -1){
                            if (count > 0) backButton.setEnabled(true);
                            if (count < panels.size() - 1) forwardButton.setEnabled(true);
                            selectListings();
                        }
                        welcomePanel.updateWelcome();
                    }
                }
            });

        for (int i = 0; i <= 7000; i+=50) {
            fromPrice.addItem(i);
            toPrice.addItem(i);
        }
        //insert integers in como box
        layout.setHorizontalGroup(layout
            .createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(from)
                .addComponent(fromPrice, 0, GroupLayout.PREFERRED_SIZE, 60)
                .addComponent(to)
                .addComponent(toPrice, 0, GroupLayout.PREFERRED_SIZE, 60)));

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(to).addComponent(fromPrice).addComponent(from).addComponent(toPrice)));

        JPanel southPanel = new JPanel();
        contentPane.add(southPanel, BorderLayout.SOUTH);

        backButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    forwardButton.setEnabled(true);
                    if (count == 0) {
                        backButton.setEnabled(false);
                    } else {
                        backButton.setEnabled(true);
                        contentPane.remove(panels.get(count));
                        contentPane.add(previousPanel(), BorderLayout.CENTER);
                        updatePanel();
                    }
                }
            });
        //disable button if there is no external panel
        forwardButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    backButton.setEnabled(true);
                    if (count == panels.size() - 1) {
                        forwardButton.setEnabled(false);
                    } else {
                        forwardButton.setEnabled(true);
                        contentPane.remove(panels.get(count));
                        contentPane.add(nextPanel(), BorderLayout.CENTER);
                        updatePanel();
                    }
                }
            });
        //set the avaliaility of button
        southPanel.add(backButton);
        southPanel.add(forwardButton);

        frame.setPreferredSize(new Dimension(1400, 1050));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    private void updatePanel() {
        contentPane.invalidate();
        contentPane.validate();
        contentPane.repaint();
        frame.pack();
    }

    private void selectListings() {
        selectListings.clear();
        for (AirbnbListing listing : listings) {
            if (listing.getPrice() >= fromValue && listing.getPrice() <= toValue) {
                selectListings.add(listing);
            }
        }

        mapPanel.updateHouses(selectListings);
        statisticsPanel.updateStatistics();
    }

    private JPanel nextPanel() {
        if (count < panels.size() - 1) {
            count = count + 1;
            return panels.get(count);
        } else {
            return panels.get(count);
        }
    }
    //move to next panel by adding counting 
    private JPanel previousPanel() {
        if (count >= 1) {
            count = count - 1;
            return panels.get(count);
        } else {
            return panels.get(count);
        }
    }
    //move to previous panel.
    public static int getFromValue() {
        return fromValue;
    }
    //get value from combo box and show in main welcome panel
    public static int getToValue() {
        return toValue;
    }

}