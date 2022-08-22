package org.yjhking.tigercc.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.yjhking.tigercc.doc.CourseDoc;

/**
 * @author YJH
 */
@Repository
public interface CourseESRepository extends ElasticsearchRepository<CourseDoc, Long> {
}
