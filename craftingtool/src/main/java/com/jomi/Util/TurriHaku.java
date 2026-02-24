package com.jomi.Util;

import java.util.List;

import com.jomi.Util.NodeShit.searchAction;

public class TurriHaku {

    public static <T extends searchAction> List<T> search(
            List<T> list,
            String query
    ) {
        if (query == null || query.isBlank()) return list;
        String q = query.toLowerCase();

        return list.stream()
            .filter(obj ->
                obj.getid().toLowerCase().contains(q) ||
                obj.getName().toLowerCase().contains(q)
            )
            .toList();
    }
}
