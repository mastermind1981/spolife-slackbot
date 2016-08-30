package io.pivotal.spo.search;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Matt Stine
 */
public interface TwitterSearchRepository extends PagingAndSortingRepository<TwitterSearch, String> {
}
