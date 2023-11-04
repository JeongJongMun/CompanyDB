package dao;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetTableModel extends AbstractTableModel {
    private List<Object> columnNames;
    private List<List<Object>> data;

    public ResultSetTableModel(ResultSet resultSet) {
        columnNames = new ArrayList<>();
        data = new ArrayList<>();

        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // 메타데이터에서 컬럼 이름 가져오기
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }

            // 데이터에 컬럼 이름 추가
            data.add(columnNames);

            // 데이터에 튜플들 추가
            while (resultSet.next()) {
                List<Object> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(resultSet.getObject(i));
                }
                data.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames.get(column).toString();
    }

    @Override
    public Object getValueAt(int row, int column) {
        return data.get(row).get(column);
    }
}

