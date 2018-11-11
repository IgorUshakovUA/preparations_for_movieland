package com.ushakov;

import java.util.ArrayList;
import java.util.List;

public class Group {
    int id;
    String tableName;
    String fieldName;
    List<Integer> idList = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Integer value : idList) {
            result.append("INSERT INTO ");
            result.append(tableName);
            result.append(" (id, ");
            result.append(fieldName);
            result.append(") VALUES (");
            result.append(id);
            result.append(", ");
            result.append(value);
            result.append(");\n");
        }
        return result.toString();
    }

    @Override
    public boolean equals(Object object) {
        Group group = (Group) object;

        boolean result = true;
        if(idList.size() == group.idList.size()) {
            for (Integer value : idList) {
                int i = group.idList.indexOf(value);
                if(i == -1) {
                    result = false;
                    break;
                }
            }
        }
        else {
            result = false;
        }

        return result;
    }
}
