
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

/**
 * The test class StatisticsPanelTest.
 *
 * @author Huang
 * @version 1.0
 */
public class StatisticsPanelTest
{
    private ArrayList<AirbnbListing> listings;
    private StatisticsPanel statisticsPanel;

    /**
     * Default constructor for test class StatisticsPanelTest
     */
    public StatisticsPanelTest()
    {
        listings = new ArrayList<>();
        listings.add(new AirbnbListing("15896822","Double room in newly refurbished flat","69018624","Dafina","Kingston upon Thames",51.41003566,-0.306322953,"Private room",23,7,1,"03/12/2016",0.32,1,61));
        listings.add(new AirbnbListing("1430028","Artist Loft in Hackney Wick","2445468","Peir","Tower Hamlets",51.53684491,-0.022336202,"Entire home/apt",100,5,1,"07/09/2013",0.02,1,359));
        statisticsPanel = new StatisticsPanel(listings);
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }

    @Test
    public void testAverageReviews() {
        assertEquals("Average number of reviews: \n1", statisticsPanel.getStatistic(0));
    }

    @Test
    public void testTotalNumberOfProperties() {
        assertEquals("Total number of properties: \n2", statisticsPanel.getStatistic(1));
    }

    @Test
    public void testEntireHomeApt() {
        assertEquals("Number of entire homes and apartments: \n1", statisticsPanel.getStatistic(2));
    }

    @Test
    public void testPriciestNeighbourhood() {
        assertEquals("The priciest neighbourhood is: \nTower Hamlets", statisticsPanel.getStatistic(3));
    }

}
