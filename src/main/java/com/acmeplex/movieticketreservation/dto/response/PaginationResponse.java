package com.acmeplex.movieticketreservation.dto.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * Represents a paginated response, including metadata and the list of results.
 *
 * @param <T> the type of the data contained in the results list
 */
public class PaginationResponse<T> {

    private static final Logger logger = LoggerFactory.getLogger(PaginationResponse.class);  // Logger for this class

    private List<T> results;
    private int currentPage;
    private int totalPages;
    private long totalItems;

    /**
     * Constructs a {@code PaginationResponse} with results and pagination metadata.
     * Logs the creation of the pagination response for traceability.
     *
     * @param results      the list of results on the current page
     * @param currentPage  the current page number
     * @param totalPages   the total number of pages available
     * @param totalItems   the total number of items available
     */
    public PaginationResponse(List<T> results, int currentPage, int totalPages, long totalItems) {
        this.results = results;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        logger.info("PaginationResponse created with {} results, page {} of {} pages, total items: {}",
                results.size(), currentPage, totalPages, totalItems);  // Log pagination details
    }

    /**
     * Returns the list of results on the current page.
     *
     * @return the list of results
     */
    public List<T> getResults() {
        return results;
    }

    /**
     * Sets the list of results on the current page.
     *
     * @param results the list of results to set
     */
    public void setResults(List<T> results) {
        this.results = results;
        logger.debug("Results set to {} items.", results.size());  // Log number of results
    }

    /**
     * Returns the current page number.
     *
     * @return the current page number
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * Sets the current page number.
     *
     * @param currentPage the current page number to set
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        logger.debug("Current page set to {}", currentPage);  // Log current page number
    }

    /**
     * Returns the total number of pages available.
     *
     * @return the total number of pages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * Sets the total number of pages available.
     *
     * @param totalPages the total number of pages to set
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
        logger.debug("Total pages set to {}", totalPages);  // Log total pages count
    }

    /**
     * Returns the total number of items available.
     *
     * @return the total number of items
     */
    public long getTotalItems() {
        return totalItems;
    }

    /**
     * Sets the total number of items available.
     *
     * @param totalItems the total number of items to set
     */
    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
        logger.debug("Total items set to {}", totalItems);  // Log total items count
    }

    /**
     * Logs a warning if any pagination parameter (page number, total items) is set incorrectly.
     * This method is an example of how to handle invalid parameters.
     */
    public void validatePagination() {
        try {
            if (this.currentPage < 1) {
                logger.warn("Invalid currentPage: {}. Should be greater than or equal to 1.", this.currentPage);
                throw new IllegalArgumentException("currentPage must be greater than or equal to 1.");
            }
            if (this.totalItems < 0) {
                logger.warn("Invalid totalItems: {}. Should be greater than or equal to 0.", this.totalItems);
                throw new IllegalArgumentException("totalItems must be greater than or equal to 0.");
            }
        } catch (IllegalArgumentException e) {
            logger.error("Validation failed for pagination parameters: {}", e.getMessage());
            throw e;  // Rethrow the exception after logging
        }
    }
}
