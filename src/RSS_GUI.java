//package RSS;

//import Comm.HttpUrlConnection;
import model.Feed;
import model.FeedMessage;
import read.RSSFeedParser;

//import javax.json.JsonString;
import javax.swing.*;
import java.awt.*;

import java.awt.event.*;
import java.net.URL;
import java.util.*;


import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

//import static Comm.Comm.toMap;

/**
 * The RSS has two main types of units: channel and item.
 * Channel settings usually describe the source of the information and have a list of news items.
 * Each news item has inside it content
 * @see FeedMessage for the content of an item
 * feed = channel , feed message - item of a channel
 * Created by Or on 4/23/2017.
 * difficulties : association between the data of JList , LinkedHashMap and database
 */
public class RSS_GUI extends JFrame implements ActionListener , MouseListener {
    /**
     * The only object of the class
     */
    private static RSS_GUI instance = null;
    /**
     * the panel of this frame class
     */
    private JPanel panel;
    /**
     * scroll option for the panel
     */
    private JScrollPane scrollPanel;
    /**
     * the constraints of the gridBag layout
     */
    private GridBagConstraints c;
    /**
     * label "Channels"
     */
    private JLabel lblChannel;
    /**
     * the content of the channel JList
     */
    private DefaultTableModel channels;
    //private DefaultListModel channels;
    /**
     * channel JList
     */
    private JTable channelTable;
    //private JList channelList;
    /**
     * scroll option for the channel JList
     */
    private JScrollPane scrollChannels;
    /**
     * add rss channel button
     */
    private JButton btnAddChannel;
    /**
     * the text field for feeding a url
     */
    private JTextField urlTxtField;
    /**
     * label "Channel items"
     */
    private JLabel lblItems;
    /**
     * the items of the chosen channel
     */
    private DefaultListModel items;
    /**
     * item JLIst
     */
    private JList itemList;
    /**
     * scroll option for the item JList
     */
    private JScrollPane scrollItems;

    private LinkedHashMap<String, Feed> channelMap = new LinkedHashMap<String, Feed>();

    private ArrayList<String> itemsLinks = new ArrayList<String>();
    /**
     * pop menu
     */
    private JPopupMenu popMenu;
    /**
     * show items button of the pop menu
     */
    private JMenuItem showChannelItems;
    /**
     * delete channel button of the pop menu
     */
    private JMenuItem deleteChannel;

    private RSS_GUI() {
        super("RssGUI");
        setVisible(true);
        setBounds(50, 50, 700, 600);

        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(525, 525));
        scrollPanel = new JScrollPane(panel);
        scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        setContentPane(scrollPanel);

        lblChannel = new JLabel("Channels");
        c = new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(lblChannel, c);


        channels = new DefaultTableModel(new String[]{"title","link","description","languge","copyright","pubdate"},0);//new DefaultListModel();
        channelTable = new JTable(channels);
        scrollChannels = new JScrollPane(channelTable);//scrollChannels = new JScrollPane(channelList);
        c = new GridBagConstraints(0, 1, 2, 1, 0.0, 300, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 500, 200);
        panel.add(scrollChannels, c);

        btnAddChannel = new JButton("Add rss");
        btnAddChannel.setBounds(0, 0, 50, 50);
        c = new GridBagConstraints(0, 2, 1, 1, 0.0, 100000, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 5, 5);
        panel.add(btnAddChannel, c);

        urlTxtField = new JTextField("http://rss.cnn.com/rss/cnn_topstories.rss");
        c = new GridBagConstraints(1, 2, 1, 1, 0.0, 100000, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 5, 12);
        panel.add(urlTxtField, c);

        lblItems = new JLabel("Channel items");
        c = new GridBagConstraints(0, 3, 1, 1, 0.0, 100000, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 5, 12);
        panel.add(lblItems, c);

        items = new DefaultListModel();
        itemList = new JList(items);
        c = new GridBagConstraints(0, 4, 2, 1, 0.0, 10000000, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 500, 200);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemList.setVisibleRowCount(-1);
        scrollItems = new JScrollPane(itemList);
        panel.add(scrollItems, c);

        popMenu = new JPopupMenu();
        showChannelItems = new JMenuItem("channel items");
        deleteChannel = new JMenuItem("Delete channel");
        popMenu.add(showChannelItems);
        popMenu.add(deleteChannel);

        showChannelItems.addActionListener(this);
        deleteChannel.addActionListener(this);
        btnAddChannel.addActionListener(this);
        channelTable.addMouseListener(this);
        itemList.addMouseListener(this);

        /*String URLs = null;
        try {
            URLs = HttpUrlConnection.GetPageContent(HttpUrlConnection.serverHost + "rss/?action=get");
            System.out.println(URLs);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> URLmap = toMap(URLs);

        for (Object o : (ArrayList<Object>) URLmap.get("url")) {
            System.out.println(o.toString());

            RSSFeedParser parser = new RSSFeedParser(o.toString());
            Feed feed = parser.readFeed();
            channels.addRow(new String[]{feed.getTitle(),feed.getLink(),feed.getDescription(),feed.getLanguage(),feed.getCopyright(),feed.getPubDate()});//channels.addElement(feed.getDescription());
            channelMap.put(o.toString(), feed);
        }*/
    }

    public static RSS_GUI getInstance() {
        if (instance == null)
            instance = new RSS_GUI();
        else
            instance.setVisible(true);

        return instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Map<String, Object> map = null;
        if (e.getSource() == btnAddChannel) {
            try {
                //http://rssfeeds.usatoday.com/usatoday-NewsTopStories //error
                RSSFeedParser parser = new RSSFeedParser(urlTxtField.getText());
                Feed feed = parser.readFeed();
                try {
                    //   map = toMap(HttpUrlConnection.GetPageContent(HttpUrlConnection.serverHost + "rss/?action=add&content=" + urlTxtField.getText()));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                // if (map.get("status").toString().equals("Success")) {
                channels.addRow(new String[]{feed.getTitle(),feed.getLink(),feed.getDescription(),feed.getLanguage(),feed.getCopyright(),feed.getPubDate()});//channels.addElement(feed.getDescription());//channels.addElement(feed.getDescription());
                channelMap.put(urlTxtField.getText(), feed);
                //}
                // else
                //     JOptionPane.showMessageDialog(this,map.get("status").toString(),"url exist",JOptionPane.WARNING_MESSAGE);
            } catch (Exception el) {

                JOptionPane.showMessageDialog(this,
                        "This is not rss",
                        "error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else if (e.getSource() == showChannelItems) {
            int row = channelTable.getSelectedRow();
            showItemsInJList(row);
        } else if (e.getSource() == deleteChannel) {
            int row = channelTable.getSelectedRow();
            System.out.println(row);

          /*  try {
                System.out.println(HttpUrlConnection.GetPageContent(HttpUrlConnection.serverHost + "rss/?action=del&content=" + channelMap.keySet().toArray()[row]));
            } catch (Exception e1) {
                e1.printStackTrace();
            }*/

            channels.removeRow(row);
            channelMap.remove(channelMap.keySet().toArray()[row]);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getSource() == itemList) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (e.getClickCount() == 2) {
                    int row = itemList.getSelectedIndex();
                    try {
                        URL url = new URL(itemsLinks.get(row));
                        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                        desktop.browse(url.toURI());
                    } catch (Exception el) {
                    }
                }
            }
        } else if (e.getSource() == channelTable) {
            if (SwingUtilities.isRightMouseButton(e)) {
                popMenu.show(e.getComponent(), e.getX(), e.getY());
            } else if (SwingUtilities.isLeftMouseButton(e)) {
                int row = channelTable.rowAtPoint(e.getPoint());
                showItemsInJList(row);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        try{
            int row = channelTable.rowAtPoint(e.getPoint());
            channelTable.setRowSelectionInterval(row,row);
        }
        catch(Exception el)
        {
            //  el.printStackTrace();
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    private void showItemsInJList(int channelIndex) {
        items.clear();
        itemsLinks.clear();
        for (FeedMessage message : channelMap.get(channelMap.keySet().toArray()[channelIndex]).getMessages()) {
            itemsLinks.add(message.getLink());
            String msg = new String();
            msg += "Title: " + message.getTitle() + "\n Description: " + message.getDescription();
            items.addElement(msg);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
