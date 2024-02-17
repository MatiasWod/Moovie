package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.services.ReviewService;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

public class ReviewController {
    private final ReviewService reviewService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }
}
