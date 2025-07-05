package ar.edu.itba.paw.models.Reports;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum ReportTypesEnum {

    HATEFUL_CONTENT(0, "Hate"),
    ABUSE(1,"Abuse & Harassment"),
    PRIVACY(2,"Privacy"),
    SPAM(3,"Spam");

    final private int type;
    final private String description;

    ReportTypesEnum( int type, String description){
        this.description = description;
        this.type = type;
    }

    private static final Map<String, ReportTypesEnum> stringToEnum =
            Stream.of(values()).collect(
                    toMap(Object::toString, e -> e));

    public static Optional<ReportTypesEnum> fromString(String symbol) {
        return Optional.ofNullable(stringToEnum.get(symbol));
    }

    public int getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
