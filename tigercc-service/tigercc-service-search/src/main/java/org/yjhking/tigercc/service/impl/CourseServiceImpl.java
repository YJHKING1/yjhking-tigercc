package org.yjhking.tigercc.service.impl;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.constants.ESConstants;
import org.yjhking.tigercc.constants.NumberConstants;
import org.yjhking.tigercc.doc.CourseDoc;
import org.yjhking.tigercc.dto.SearchDto;
import org.yjhking.tigercc.mapper.HighlightResultMapper;
import org.yjhking.tigercc.repository.CourseESRepository;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.service.CourseService;
import org.yjhking.tigercc.utils.VerificationUtils;
import org.yjhking.tigercc.vo.AggPageList;
import org.yjhking.tigercc.vo.BucketVO;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author YJH
 */
@Service
public class CourseServiceImpl implements CourseService {
    @Resource
    private CourseESRepository courseESRepository;
    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Resource
    private HighlightResultMapper highlightResultMapper;
    
    @Override
    public JsonResult search(SearchDto searchDto) {
        // 分页
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder().withPageable(PageRequest.of(
                searchDto.getPage() - NumberConstants.ONE, searchDto.getRows()));
        // 排序
        if (VerificationUtils.hasLength(searchDto.getSortField()))
            builder.withSort(new FieldSortBuilder(getSortFieldName(searchDto.getSortField()))
                    .order(getSortOrder(searchDto.getSortType())));
        // 条件
        builder.withQuery(queryBuilder(searchDto));
        // 高亮
        builder.withHighlightFields(new HighlightBuilder.Field(ESConstants.NAME)
                .preTags(ESConstants.PRE_TAGS).postTags(ESConstants.POST_TAGS));
        // 聚合
        /*builder.addAggregation(AggregationBuilders.terms(TigerccConstants.GRADE_NAME_AGG)
                        .field(TigerccConstants.GRADE_NAME))
                .addAggregation(AggregationBuilders.terms(TigerccConstants.CHARGE_NAME_AGG)
                        .field(TigerccConstants.CHARGE_NAME));*/
        // 查询
        // Page<CourseDoc> search = courseESRepository.search(builder.build());
        AggregatedPage<CourseDoc> courseDocs = elasticsearchRestTemplate.queryForPage(
                builder.build(), CourseDoc.class, highlightResultMapper);
        // 聚合结果
        Map<String, List<BucketVO>> aggResultMap = new HashMap<>();
        /*courseDocs.getAggregations().asMap().forEach((aggName, agg) ->
                aggResultMap.put(aggName, ((ParsedStringTerms) agg).getBuckets().stream().map(bucket ->
                        new BucketVO(bucket.getKeyAsString(), bucket.getDocCount())).collect(Collectors.toList())));*/
        return JsonResult.success(
                new AggPageList<>(courseDocs.getTotalElements(), courseDocs.getContent(), null));
    }
    
    /**
     * 排序规则
     */
    private String getSortFieldName(String sn) {
        switch (sn.toLowerCase()) {
            case ESConstants.XL:
                return ESConstants.SALE_COUNT;
            case ESConstants.PL:
                return ESConstants.COMMENT_COUNT;
            case ESConstants.JG:
                return ESConstants.PRICE;
            case ESConstants.RQ:
                return ESConstants.VIEW_COUNT;
            case ESConstants.XP:
                return ESConstants.ONLINE_TIME;
        }
        return ESConstants.SALE_COUNT;
    }
    
    /**
     * 排序顺序
     */
    private SortOrder getSortOrder(String sn) {
        return sn.toLowerCase().equals(ESConstants.ASC) ? SortOrder.ASC : SortOrder.DESC;
    }
    
    /**
     * 查询条件
     */
    private BoolQueryBuilder queryBuilder(SearchDto searchDto) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (VerificationUtils.hasLength(searchDto.getKeyword()))
            boolQueryBuilder.must(QueryBuilders.matchQuery(ESConstants.NAME, searchDto.getKeyword()));
        if (VerificationUtils.hasLength(searchDto.getGradeName()))
            boolQueryBuilder.filter(QueryBuilders.termQuery(ESConstants.GRADE_NAME, searchDto.getGradeName()));
        if (VerificationUtils.isValid(searchDto.getCourseTypeId()))
            boolQueryBuilder.filter(QueryBuilders.termQuery(ESConstants.COURSE_TYPE_ID, searchDto.getCourseTypeId()));
        if (VerificationUtils.hasLength(searchDto.getChargeName()))
            boolQueryBuilder.filter(QueryBuilders.termQuery(ESConstants.CHARGE_NAME, searchDto.getChargeName()));
        if (VerificationUtils.isValid(searchDto.getPriceMax()))
            boolQueryBuilder.filter(QueryBuilders.termQuery(ESConstants.PRICE, searchDto.getPriceMax()));
        if (VerificationUtils.isValid(searchDto.getPriceMin()))
            boolQueryBuilder.filter(QueryBuilders.termQuery(ESConstants.PRICE, searchDto.getPriceMin()));
        return boolQueryBuilder;
    }
}
