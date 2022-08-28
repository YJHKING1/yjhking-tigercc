package org.yjhking.tigercc.utils;

import java.util.Collections;
import java.util.List;

/**
 * @author YJH
 */
public class ListUtils {
    public static <T> List<T> getListReversal(List<T> list) {
        Collections.reverse(list);
        return list;
    }
}
