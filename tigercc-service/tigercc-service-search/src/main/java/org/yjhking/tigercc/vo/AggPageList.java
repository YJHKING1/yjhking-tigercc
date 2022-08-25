package org.yjhking.tigercc.vo;

import org.yjhking.tigercc.result.PageList;

import java.util.List;
import java.util.Map;

/**
 * @author YJH
 */
public class AggPageList<T> extends PageList<T> {
    private Map<String, List<BucketVO>> aggMap;
    public AggPageList(long total, List<T> rows, Map<String, List<BucketVO>> aggMap){
        super(total , rows);
        this.aggMap = aggMap;
    }
}
