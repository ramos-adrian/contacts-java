package contacts;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Contact {
    private String name;
    private String number;
    private final LocalDateTime timeCreated;

    private static final String REGEX_FIRST_PARENTHESES = "^\\+?\\(\\w{2,}\\)([\\s-][\\w\\s\\-]{2,})*";
    private static final String REGEX_SECOND_PARENTHESES = "(\\+\\w+[\\s\\-]\\(\\w{2,}\\)([\\s\\-]\\w+)*)|(\\w{2,}[\\s\\-]\\(\\w{2,}\\)([\\s\\-]\\w+)*)";
    private static final String REGEX_NO_PARENTHESES = "(\\+\\w+([\\s\\-]\\w{2,})*)|(\\w+([\\s\\-]\\w{2,})*)";
    private static final Pattern PATTERN_FIRST_PARENTHESES = Pattern.compile(REGEX_FIRST_PARENTHESES);
    private static final Pattern PATTERN_SECOND_PARENTHESES = Pattern.compile(REGEX_SECOND_PARENTHESES);
    private static final Pattern PATTERN_NO_PARENTHESES = Pattern.compile(REGEX_NO_PARENTHESES);

    public void setTimeLastEdit(LocalDateTime timeLastEdit) {
        this.timeLastEdit = timeLastEdit.withNano(0);
    }

    private LocalDateTime timeLastEdit;

    public Contact(String name) {
        this.name = name;
        this.number = "";
        this.timeCreated = LocalDateTime.now().withNano(0);
        this.timeLastEdit = LocalDateTime.now().withNano(0);
    }

    public boolean setNumber(String number) {
        if(numberIsValid(number)){
            this.number = number;
            return true;
        }
        this.number = "";
        return false;
    }

    private boolean numberIsValid(String number) {
        boolean pattern1 = PATTERN_FIRST_PARENTHESES.matcher(number).matches();
        boolean pattern2 = PATTERN_SECOND_PARENTHESES.matcher(number).matches();
        boolean pattern3 = PATTERN_NO_PARENTHESES.matcher(number).matches();
        return pattern1 || pattern2 || pattern3;
    }

    public boolean hasNumber() {
        return !number.equals("");
    }

    public String getName() {
        return this.name;
    }

    public String getNumber() {
        return number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public LocalDateTime getTimeLastEdit() {
        return this.timeLastEdit;
    }

    abstract String getContactInfo();

    abstract String getFullName();

    public abstract List<String> getEditableFields();

    public abstract void editField(String field, String newValue);

    public abstract boolean searchRequest(String query);
}
