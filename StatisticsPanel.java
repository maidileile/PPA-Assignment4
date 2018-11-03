import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Present a series of statistics.
 *
 * @Xiangyi Zeng
 * @March 27th, 2018
 */

public class StatisticsPanel {    
    private JPanel contentPane = new JPanel();
    private JLabel statisticsPanel;
    ArrayList<AirbnbListing> selectedList;
    ArrayList<String> statisticsList = new ArrayList<>();
    private int i = 0;
    /**
     * Constructor for objects of class statistics.
     */
    public StatisticsPanel(ArrayList<AirbnbListing> selectList) {
        selectedList = selectList;
        addStatisticsList();
        createStatisticsPanel();
    }

    /**
     * Create the Statistics panel and its content.
     */
    private void createStatisticsPanel() {
        contentPane.setLayout(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(12, 12, 12, 12));

        statisticsPanel = new JLabel();
        statisticsPanel.setBorder(new EtchedBorder());
        contentPane.add(statisticsPanel, BorderLayout.CENTER);
        statisticsPanel.setText(statisticsList.get(i));

        JButton left = new JButton("<");
        JButton right = new JButton(">");
        left.setContentAreaFilled(false);
        right.setContentAreaFilled(false);
        left.setPreferredSize(new Dimension(50, 300));
        right.setPreferredSize(new Dimension(50, 300));
        contentPane.add(left, BorderLayout.WEST);
        contentPane.add(right, BorderLayout.EAST);
        left.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                previousStatistics();
            }
        });
        right.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextStatistics();
            }
        });
    }

    /**
     * return the statistics panel.
     */
    public JPanel getPanel() {
        return contentPane;
    }

    /**
     * update the statistics list for different price range choose.
     */
    public void updateStatistics( ) {
        statisticsList.clear();
        addStatisticsList();
        statisticsPanel.setText(statisticsList.get(i));
    }

    /**
     * add the statistics in an arraylist.
     */
    private void addStatisticsList() {
        statisticsList.add(averageReviews());
        statisticsList.add(totalNumberOfProperties());
        statisticsList.add(entireHome_Apt());
        statisticsList.add(priciestNeighbourhood());
        statisticsList.add(priciestProperty());
        statisticsList.add(numberOfAlmostSoldOut());
        statisticsList.add(mostPopularProperty());
        statisticsList.add(averageMonthlyReviews());
    }

    /**
     * nextStatistics method.
     */
    private void nextStatistics() {
        i = (i + 1) % statisticsList.size();
        statisticsPanel.setText(statisticsList.get(i));
    }

    /**
     * previousStatitics method.
     */
    private void previousStatistics() {
        if (i > 0) {
            i--;
        } else {
            i = statisticsList.size() - 1;
        }
        statisticsPanel.setText(statisticsList.get(i));
    }

    // ---- statistics method for this panel ----

    /**
     * Give the average reviews of properties that relates customers' choice.
     */
    private String averageReviews() {
        int totalReviews = 0;
        int averageReviews = 0;
        for (int i = 0; i < selectedList.size(); i++) {
            totalReviews += selectedList.get(i).getNumberOfReviews();
            averageReviews = totalReviews / selectedList.size();
        }
        return "Average number of reviews: " + "\n" + averageReviews;
    }

    /**
     * calculate the total number of properties for specified range.
     */
    private int calculateTotalNumberOfProperties() {
        int availablenumber = selectedList.size();
        for (int i = 0; i < selectedList.size(); i++) {
            if (selectedList.get(i).getAvailability365() == 0) {
                availablenumber--;
            }
        }
        return availablenumber;
    }

    /**
     * return the total number of available properties.
     */
    private String totalNumberOfProperties() {
        return "Total number of properties: " + "\n" + calculateTotalNumberOfProperties();
    }

    /**
     * Give the number of entire homes/apartments.
     */
    private String entireHome_Apt() {
        int numberOfEntireHome_Apt = 0;
        for (int i = 0; i < selectedList.size(); i++) {
            if (selectedList.get(i).getRoom_type().equals("Entire home/apt")) {
                numberOfEntireHome_Apt++;
            }
        }
        return "Number of entire homes and apartments: " + "\n" + numberOfEntireHome_Apt;
    }

    /**
     * get the priciest neighbourhood which avarage price
     * of closed properties of specified price range is the highest one.
     */
    private String priciestNeighbourhood() {
        if (selectedList.size() == 0) return  "The priciest neighbourhood is: " + "\n";
        Map<String, ArrayList<Integer>> neighbourhoods = new HashMap<>();
        for (AirbnbListing selectedList : selectedList) {
            ArrayList<Integer> prices = neighbourhoods.get(selectedList.getNeighbourhood());
            if (prices == null) {
                prices = new ArrayList<>();
                neighbourhoods.put(selectedList.getNeighbourhood(), prices);
            }
            prices.add(selectedList.getPrice() * selectedList.getMinimumNights());
        }
        String priciestNeighbourhood = null;
        double highestAverage = 0;
        for (String neighbourhood : neighbourhoods.keySet()) {
            ArrayList<Integer> prices = neighbourhoods.get(neighbourhood);
            int priceOfNeighbourhood = 0;
            for (Integer price : prices) {
                priceOfNeighbourhood += price;
            }
            double average = priceOfNeighbourhood / (double) prices.size();
            if (average >= highestAverage) {
                priciestNeighbourhood = neighbourhood;
                highestAverage = average;
            }
        }
        return "The priciest neighbourhood is: " + "\n" + priciestNeighbourhood;
    }

    /**
     * get the property which will cost most for specified price range.
     */
    private String priciestProperty() {
        if (selectedList.size() == 0) return "The priciest property is: " + "\n";
        int maxPrice = selectedList.get(0).getPrice() * selectedList.get(0).getMinimumNights();
        String maxName = selectedList.get(0).getName();
        for (int i = 1; i < selectedList.size(); i++) {
            int totalPrice = selectedList.get(i).getPrice() * selectedList.get(i).getMinimumNights();
            String name = selectedList.get(i).getName();
            if (totalPrice > maxPrice) {
                maxPrice = totalPrice;
                maxName = name;
            }
        }
        return "The priciest property is: " + "\n" + maxName;
    }

    /**
     * get the number of properties which availability for 365
     * is greater than 0 and smaller than 20 for specified price range.
     */
    private String numberOfAlmostSoldOut() {
        int numbers = 0;
        for (int i = 0; i < selectedList.size(); i++) {
            int availability = selectedList.get(i).getAvailability365();
            if ((0 < availability) && (availability < 20)) {
                numbers++;
            }
        }
        return "The number of properties which are almost sold out: " + "\n" + numbers;
    }

    /**
     * get the property which have most last reviews for specified price range.
     */
    private String mostPopularProperty() {
        if (selectedList.size() == 0) return "The most popular property is: " + "\n";
        int mostReviews = 0;
        String property = selectedList.get(0).getName();
        for (int i = 0; i < selectedList.size(); i++) {
            int num_reviews = selectedList.get(i).getNumberOfReviews();
            String name = selectedList.get(i).getName();
            if (num_reviews > mostReviews) {
                mostReviews = num_reviews;
                property = name;
            }
        }
        return "The most popular property is: " + "\n" + property;
    }

    /**
     * get the average reviews per month among these properties for specified price range.
     */
    private String averageMonthlyReviews() {
        if (selectedList.size() == 0) return "The average monthly reviews is: " + "\n" + "0";
        int noOfProperties = calculateTotalNumberOfProperties();
        if (noOfProperties == 0) return "The average monthly reviews is: " + "\n" + "0";
        double totalReviews = 0;
        for (int i = 0; i < selectedList.size(); i++) {
            totalReviews += selectedList.get(i).getReviewsPerMonth();
        }
        return "The average monthly reviews is: " + "\n" + totalReviews / noOfProperties;
    }
}
