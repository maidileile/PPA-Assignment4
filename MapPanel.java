import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MapPanel {

    private static final int HOUSE_SIZE = 32;

    private HashMap<String, int[]> houseLocations;

    private JPanel panel;

    private BufferedImage mapImage;
    private BufferedImage houseImage;

    private JLabel mapLabel;
    private HashMap<String, JLabel> houseLabels = new HashMap<>();

    public MapPanel() {
        panel = new JPanel();
        panel.setLayout(null);
        createMap();
    }

    private void createMap() {
        try {
            mapImage = ImageIO.read(getClass().getResource("map.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error! House image could not load.");
            System.exit(0);
            return;
        }
        try {
            houseImage = ImageIO.read(getClass().getResource("house.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error! House image could not load.");
            System.exit(0);
            return;
        }

        mapLabel = new JLabel(new ImageIcon(mapImage));
        mapLabel.setBounds(0, 0, mapImage.getWidth(), mapImage.getHeight());
        panel.add(mapLabel);

        houseLocations = new HashMap<>();
        houseLocations.put("Hillingdon", new int[]{113, 161});
        houseLocations.put("Harrow", new int[]{180, 160});
        houseLocations.put("Ealing", new int[]{190, 211});
        houseLocations.put("Hounslow", new int[]{137, 310});
        houseLocations.put("Brent", new int[]{235, 177});
        houseLocations.put("Richmond upon Thames", new int[]{190, 335});
        houseLocations.put("Hammersmith and Fulham", new int[]{286, 238});
        houseLocations.put("Barnet", new int[]{302, 93});
        houseLocations.put("Kingston upon Thames", new int[]{230, 400});
        houseLocations.put("Wandsworth", new int[]{345, 323});
        houseLocations.put("Merton", new int[]{295, 365});
        houseLocations.put("Sutton", new int[]{354, 407});
        houseLocations.put("Kensington and Chelsea", new int[]{324, 255});
        houseLocations.put("Westminster", new int[]{361, 244});
        houseLocations.put("Camden", new int[]{350, 195});
        houseLocations.put("Haringey", new int[]{395, 147});
        houseLocations.put("Enfield", new int[]{410, 55});
        houseLocations.put("Islington", new int[]{395, 193});
        houseLocations.put("Lambeth", new int[]{385, 320});
        houseLocations.put("Croydon", new int[]{390, 450});
        houseLocations.put("Southwark", new int[]{420, 280});
        houseLocations.put("Tower Hamlets", new int[]{450, 225});
        houseLocations.put("Lewisham", new int[]{480, 325});
        houseLocations.put("Waltham Forest", new int[]{475, 150});
        houseLocations.put("Redbridge", new int[]{550, 155});
        houseLocations.put("Newham", new int[]{515, 215});
        houseLocations.put("Greenwich", new int[]{530, 290});
        houseLocations.put("Barking and Dagenham", new int[]{600, 205});
        houseLocations.put("City of London", new int[]{405, 235});
        houseLocations.put("Bexley", new int[]{610, 295});
        houseLocations.put("Havering", new int[]{670, 145});
        houseLocations.put("Bromley", new int[]{535, 430});

        for (String neighbourhood : houseLocations.keySet()) {
            int[] position = houseLocations.get(neighbourhood);
            JLabel houseLabel = new JLabel();
            houseLabel.setBounds(position[0], position[1], houseImage.getWidth(), houseImage.getHeight());
            houseLabel.setVisible(false);
            houseLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        ArrayList<AirbnbListing> listings = neighbourhoodListings.get(neighbourhood);
                        if (listings == null) {
                            listings = new ArrayList<>();
                        }
                        MapGUI mapGUI = new MapGUI(neighbourhood, listings);
                        mapGUI.display();
                    }
                });
            mapLabel.add(houseLabel);
            houseLabels.put(neighbourhood, houseLabel);
        }
    }

    public JPanel getPanel() {
        return panel;
    }

    private HashMap<String, ArrayList<AirbnbListing>> neighbourhoodListings = new HashMap<>();

    public void updateHouses(ArrayList<AirbnbListing> selectListings) {
        neighbourhoodListings.clear();
        for (AirbnbListing listing : selectListings) {
            ArrayList<AirbnbListing> neighbourhoodsListings = neighbourhoodListings.get(listing.getNeighbourhood());
            if (neighbourhoodsListings == null) {
                neighbourhoodsListings = new ArrayList<>();
                neighbourhoodListings.put(listing.getNeighbourhood(), neighbourhoodsListings);
            }
            neighbourhoodsListings.add(listing);
        }
        int highestListings = 0;
        for (ArrayList<AirbnbListing> listings : neighbourhoodListings.values()) {
            if (listings.size() > highestListings) highestListings = listings.size();
        }

        for (String neighbourhood : houseLabels.keySet()) {
            JLabel label = houseLabels.get(neighbourhood);
            if (neighbourhoodListings.containsKey(neighbourhood)) {
                int listingCount = neighbourhoodListings.get(neighbourhood).size();
                int imageSize = (int) (HOUSE_SIZE * (0.5 + 0.5 * (listingCount / (double) highestListings)));
                label.setIcon(new ImageIcon(resize(houseImage, imageSize, imageSize)));
                int[] position = houseLocations.get(neighbourhood);
                label.setBounds(position[0] - imageSize / 2, position[1] - imageSize / 2, imageSize, imageSize);
                label.setVisible(true);
            } else {
                label.setVisible(false);
            }
        }
    }

    private static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return dimg;
    }

}
