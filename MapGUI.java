import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;

public class MapGUI {

	private JFrame frame;
	private JPanel listingsPanel = new JPanel();
	private JLabel columnsLabel = new JLabel("Host name - Price - Number of reviews - Minimum nights");

	private ArrayList<AirbnbListing> listings;

	public MapGUI(String neighbourhood, ArrayList<AirbnbListing> neighbourhoodListings) {
		this.listings = new ArrayList<>(neighbourhoodListings);

		frame = new JFrame(neighbourhood + " listings");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setPreferredSize(new Dimension(600, 400));
		frame.setLocationRelativeTo(null);

		listingsPanel = new JPanel();
		listingsPanel.setLayout(new BoxLayout(listingsPanel, BoxLayout.Y_AXIS));

		createListingsPanel();

		JComboBox<String> sortBy = new JComboBox<>();
		sortBy.addItem(" ");
		sortBy.addItem("Sort by number of reviews");
		sortBy.addItem("Sort by price");
		sortBy.addItem("Sort by host name");
		sortBy.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if ((e.getStateChange() == ItemEvent.SELECTED)) {
					String selected = (String) e.getItem();
					if (selected.equals("Sort by number of reviews")) {
						listings.sort(new Comparator<AirbnbListing>() {
							@Override
							public int compare(AirbnbListing o1, AirbnbListing o2) {
								return Integer.compare(o1.getNumberOfReviews(), o2.getNumberOfReviews());
							}
						});
					} else if (selected.equals("Sort by price")) {
						listings.sort(new Comparator<AirbnbListing>() {
							@Override
							public int compare(AirbnbListing o1, AirbnbListing o2) {
								return Integer.compare(o1.getPrice(), o2.getPrice());
							}
						});
					} else if (selected.equals("Sort by host name")) {
						listings.sort(new Comparator<AirbnbListing>() {
							@Override
							public int compare(AirbnbListing o1, AirbnbListing o2) {
								return o1.getHost_name().compareTo(o2.getHost_name());
							}
						});
					} else {
						return;
					}
					listingsPanel.removeAll();
					createListingsPanel();
					listingsPanel.validate();
				}
			}
		});

		JPanel contentsPane = (JPanel) frame.getContentPane();
		contentsPane.setLayout(new BorderLayout());
		contentsPane.add(sortBy, BorderLayout.NORTH);
		contentsPane.add(new JScrollPane(listingsPanel), BorderLayout.CENTER);

		frame.pack();
	}

	private void createListingsPanel() {
		listingsPanel.add(columnsLabel);

		for (AirbnbListing listing : listings) {
			JLabel listingLabel = new JLabel(listing.getHost_name() + " - " + "£" + listing.getPrice() + " - " + listing.getNumberOfReviews() + " - " + listing.getMinimumNights());
			listingLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					JOptionPane.showMessageDialog(null, listing.getDetails());
				}
			});
			listingsPanel.add(Box.createVerticalStrut(10));
			listingsPanel.add(listingLabel);
		}
	}

	public void display() {
		frame.setVisible(true);
	}

}
