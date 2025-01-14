package ludo.mentis.aciem.auctoritas.util;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import ludo.mentis.aciem.auctoritas.model.PaginationModel;
import ludo.mentis.aciem.auctoritas.model.PaginationStep;


@Component
public class WebUtils {

    public static final String MSG_SUCCESS = "MSG_SUCCESS";
    public static final String MSG_INFO = "MSG_INFO";
    public static final String MSG_ERROR = "MSG_ERROR";
    private static MessageSource messageSource;
    private static LocaleResolver localeResolver;

    public WebUtils(final MessageSource messageSource, final LocaleResolver localeResolver) {
        WebUtils.messageSource = messageSource;
        WebUtils.localeResolver = localeResolver;
    }

    public static HttpServletRequest getRequest() {
    	final var attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
    	if (attributes == null) {
    		throw new IllegalStateException("RequestContextHolder.getRequestAttributes() cannot be null");
    	}
        return attributes.getRequest();
    }

    public static String getMessage(final String code, final Object... args) {
        return messageSource.getMessage(code, args, code, localeResolver.resolveLocale(getRequest()));
    }

    private static String getStepUrl(final Page<?> page, final int targetPage) {
        String stepUrl = "?page=" + targetPage + "&size=" + page.getSize();
        if (getRequest().getParameter("sort") != null) {
            stepUrl += "&sort=" + getRequest().getParameter("sort");
        }
        if (getRequest().getParameter("filter") != null) {
            stepUrl += "&filter=" + getRequest().getParameter("filter");
        }
        return stepUrl;
    }

    public static PaginationModel getPaginationModel(final Page<?> page) {
        if (page == null || page.isEmpty()) {
            return null;
        }

        final var previousPageNumber = page.getPageable().isUnpaged() ? 0 : page.previousOrFirstPageable().getPageNumber();
        final var nextPageNumber = page.getPageable().isUnpaged() ? 0 : page.nextOrLastPageable().getPageNumber();

        final var steps = new ArrayList<PaginationStep>();

        final var previous = new PaginationStep();
        previous.setDisabled(!page.hasPrevious());
        previous.setLabel("Previous");
        previous.setUrl(getStepUrl(page, previousPageNumber));
        steps.add(previous);

        // find a range of up to 5 pages around the current active page
        final int startAt = Math.max(0, Math.min(page.getNumber() - 2, page.getTotalPages() - 5));
        final int endAt = Math.min(startAt + 5, page.getTotalPages());

        for (int i = startAt; i < endAt; i++) {
            final var step = new PaginationStep();
            step.setActive(i == page.getNumber());
            step.setLabel("" + (i + 1));
            step.setUrl(getStepUrl(page, i));
            steps.add(step);
        }

        final var next = new PaginationStep();
        next.setDisabled(!page.hasNext());
        next.setLabel("Next");
        next.setUrl(getStepUrl(page, nextPageNumber));
        steps.add(next);

        final var rangeStart = (long) page.getNumber() * page.getSize() + 1L;
        final var rangeEnd = Math.min(rangeStart + page.getSize() - 1, page.getTotalElements());
        final var range = rangeStart == rangeEnd ? "" + rangeStart : rangeStart + " - " + rangeEnd;
        final var paginationModel = new PaginationModel();
        paginationModel.setSteps(steps);
        paginationModel.setElements(String.format("Item %s of %d", range, page.getTotalElements()));
        return paginationModel;
    }
}
