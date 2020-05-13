
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class MPView {
    private JFrame frame;
    private JTable tb;
    private AbstractTableModel atb;
    private JTextField metField;
    private JTextField contField;
    private JTextField popField;
    private JButton add;
    private JButton search;
    private JComboBox popSearch;
    private JComboBox exactSearch;

    public MPView() {
        JPanel mainPanel = setUpMainPanel();

        JPanel upperPanel = setUpperPanel();

        mainPanel.add(setUpTable(), BorderLayout.CENTER);

        JPanel rightPanel = setUpRightPanel();

        JPanel searchOptions = setUpSearchOptions();

        rightPanel.add(searchOptions);
        rightPanel.add(Box.createVerticalStrut(230));

        mainPanel.add(upperPanel, BorderLayout.NORTH);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JPanel setUpMainPanel(){
        frame = new JFrame("Metropolises");
        frame.setSize(800, 500);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        return mainPanel;
    }

    private JPanel setUpperPanel(){
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new FlowLayout(FlowLayout.LEADING));

        upperPanel.add(new JLabel("Metropolis: "));
        metField = new JTextField(15);
        upperPanel.add(metField);

        upperPanel.add(new JLabel("Continent: "));
        contField = new JTextField(15);
        upperPanel.add(contField);

        upperPanel.add(new JLabel("Population: "));
        popField = new JTextField(15);
        upperPanel.add(popField);
        return upperPanel;
    }

    private JScrollPane setUpTable(){
        tb = new JTable();
        return new JScrollPane(tb);
    }

    private JPanel setUpSearchOptions() {
        JPanel searchOptions = new JPanel();
        searchOptions.setLayout(new BoxLayout(searchOptions, BoxLayout.Y_AXIS));
        searchOptions.setBorder(new TitledBorder("Search Options"));

        //population dropdown
        String[] arrPop = new String[] {"Population Larger Than", "Population Smaller Than"};
        popSearch = new JComboBox(arrPop);
        searchOptions.add(popSearch);

        searchOptions.add(Box.createVerticalStrut(5));

        //exact dropdown
        String[] arrMatch = new String[] {"Exact Match", "Partial Match"};
        exactSearch = new JComboBox(arrMatch);
        searchOptions.add(exactSearch);

        return searchOptions;
    }

    private JPanel setUpRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        createButtons();
        rPanelAddButtons(rightPanel);
        return rightPanel;
    }

    private void rPanelAddButtons(JPanel rightPanel) {
        rightPanel.add(Box.createRigidArea(new Dimension(10, 15)));
        rightPanel.add(add);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(search);
        rightPanel.add(Box.createVerticalStrut(15));
    }

    private void createButtons(){
        add = new JButton("Add");
        search = new JButton("Search");
        Dimension d = search.getMaximumSize();
        add.setMaximumSize(d);
    }


    public void registerListeners(ActionListener addL, ActionListener searchL){
        frame.setVisible(true);
        add.addActionListener(addL);
        search.addActionListener(searchL);
    }

    public String[] getFieldInfo(){
        String[] fields = new String[3];
        fields[0] = metField.getText();
        fields[1] = contField.getText();
        fields[2] = popField.getText();
        return fields;
    }

    public String populChBxInfo(){
        return popSearch.getSelectedItem().toString();
    }
    public String exactChBxInfo(){
        return exactSearch.getSelectedItem().toString();
    }

    public void setTableModel(AbstractTableModel atm) {
        tb.setModel(atm);
        atb = atm;
    }
    public void updateTable(){
        atb.fireTableDataChanged();
    }
}
