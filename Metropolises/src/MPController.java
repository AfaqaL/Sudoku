import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MPController {
    MPModel model;
    MPView view;
    List<String> titles;
    List<List<String>> data;

    public MPController(MPModel model, MPView view) {
        this.model = model;
        this.view = view;

        initTitles();
        data = new ArrayList<>();

        AbstractTableModel atm = new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return data.size();
            }

            @Override
            public int getColumnCount() {
                return titles.size();
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return data.get(rowIndex).get(columnIndex);
            }

            @Override
            public String getColumnName(int column) {
                return titles.get(column);
            }
        };

        view.setTableModel(atm);


    }

    private void initTitles() {
        titles = new ArrayList<>(3);
        titles.add("Metropolises");
        titles.add("Continent");
        titles.add("Population");
    }

    public void start(){
        ActionListener addList = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                data.clear();
                List<String> arr = model.addAction(view.getFieldInfo());
                for(String s : arr){
                    System.out.println(s);
                }
                data.add(arr);
                view.updateTable();
            }
        };
        ActionListener searchList = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                data = model.searchAction(view.getFieldInfo(), view.populChBxInfo(), view.exactChBxInfo());
                view.updateTable();
            }
        };
        view.registerListeners(addList, searchList);
    }
}
