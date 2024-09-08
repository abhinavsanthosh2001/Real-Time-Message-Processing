package com.assignment2.queuingmechanism.constants;

public final class Constants {
    public static final String NO_TOPIC_MESSAGE = "No topic with given topic name.";
    public static final String NO_PRODUCER_MESSAGE = "No producer exists with given producerId.";
    public static final String NO_CONSUMER_MESSAGE = "No customer with given name.";
    public static final String NO_SUBSCRIPTION = "Consumer is not subscribed to given topic.";
    public static final String BAD_REQUEST = "400";
    public static final String NAME_BLANK_MESSAGE = "Name must not blank";
    public static final String DESCRIPTION_SIZE_MESSAGE = "Number of characters in description must be in the range 2-100";
    public static final String ALREADY_SUBSCRIBED = "Consumer is already subscribed to this topic.";
    public static final String MESSAGE_LENGTH_VIOLATION = "Message length must be between 2 and 50 characters.";
    public static final String FORBIDDEN = "403";
    public static final String PROCESSING = "Processing";
    public static final String CONSTRAINS_VIOLATED = "Constraints violated";
    public static final String ERROR = "error";
    public static final String SHOULD_BE_OF_TYPE = " should be of type ";
    public static final String INTERNAL_SERVER_ERROR = "500";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong. ";

    private Constants() {}
}

