import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultSetTableModel extends AbstractTableModel {
    private ResultSet resultSet;
    private ResultSetMetaData metaData;
    private int numberOfRows;

    public ResultSetTableModel(ResultSet rs) throws SQLException {
        this.resultSet = rs;
        this.metaData = rs.getMetaData();
        rs.last();
        numberOfRows = rs.getRow();
        fireTableStructureChanged();
    }

    @Override
    public int getRowCount() {
        return numberOfRows;
    }

    @Override
    public int getColumnCount() {
        try {
            return metaData.getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Object getValueAt(int row, int column) {
        try {
            resultSet.absolute(row + 1);
            return resultSet.getObject(column + 1);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        try {
            return metaData.getColumnName(column + 1);
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }
}
