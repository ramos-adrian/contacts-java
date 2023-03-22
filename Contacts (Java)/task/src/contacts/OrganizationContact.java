package contacts;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrganizationContact extends Contact {
    private String address;
    public OrganizationContact(String name, String address) {
        super(name);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    String getContactInfo() {
        return "Organization name: %s\nAddress: %s".formatted(getName(), getAddress());
    }

    @Override
    String getFullName() {
        return getName();
    }

    @Override
    public List<String> getEditableFields() {
        return new ArrayList<>(Arrays.asList("address", "number"));
    }


    @Override
    public void editField(String field, String newValue) {
        switch (field) {
            case "name" -> {
                setName(newValue);
                setTimeLastEdit(LocalDateTime.now());
            }
            case "address" -> {
                setAddress(newValue);
                setTimeLastEdit(LocalDateTime.now());
            }
        }
    }

    @Override
    public boolean searchRequest(String query) {
       // System.out.printf("\nSearching '%s' in %s", query, getFullName());
        String rawInfo = String.join(" ", getFullName(), getNumber(), getAddress());
        Pattern pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(rawInfo);
        return matcher.find();
    }
}
