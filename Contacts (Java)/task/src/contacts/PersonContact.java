package contacts;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonContact extends Contact {
    private String surname;
    private LocalDate birthdate = null;
    private Gender gender = null;

    public PersonContact(String name, String surname) {
        super(name);
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String getContactInfo() {
        String birthdateString = getBirthdate() != null ? getBirthdate().toString() : "[no data]";
        String genderString = getGender() != null ? getGender().toString().substring(0, 1) : "[no data]";
        return "Name: %s\nSurname: %s\nBirth date: %s\nGender: %s".formatted(getName(), getSurname(), birthdateString, genderString);
    }

    @Override
    String getFullName() {
        return getName() + " " + getSurname();
    }

    @Override
    public List<String> getEditableFields() {
        return new ArrayList<>(Arrays.asList("name", "surname", "birth", "gender", "number"));
    }

    @Override
    public void editField(String field, String newValue) {
        switch (field) {
            case "name" -> {
                setName(newValue);
                setTimeLastEdit(LocalDateTime.now());
            }
            case "surname" -> {
                setSurname(newValue);
                setTimeLastEdit(LocalDateTime.now());
            }
            case "number" -> {
                if (!setNumber(newValue)) {
                    System.out.println("Wrong number format!");
                } else {
                    setTimeLastEdit(LocalDateTime.now());
                }
            }
            case "birth" -> {
                LocalDate birthdate;
                try {
                    birthdate = LocalDate.parse(newValue);
                    setBirthdate(birthdate);
                    setTimeLastEdit(LocalDateTime.now());
                } catch (DateTimeParseException ignored) {
                    System.out.println("Bad birth date!");
                }
            }
            case "gender" -> {
                Gender gender = null;
                if (newValue.equalsIgnoreCase("M")) {
                    gender = Gender.MALE;
                } else if (newValue.equalsIgnoreCase("F")) {
                    gender = Gender.FEMALE;
                }
                if (gender == null) {
                    System.out.println("Bad gender!");
                } else {
                    setGender(gender);
                    setTimeLastEdit(LocalDateTime.now());
                }
            }
        }
    }

    @Override
    public boolean searchRequest(String query) {
       // System.out.printf("\nSearching '%s' in %s", query, getFullName());
        String[] optionalFields = new String[2];
        optionalFields[0] = birthdate != null ? birthdate.toString() : "";
        optionalFields[1] = gender != null ? gender.toString() : "";

        String rawInfo = String.join(" ", getFullName(), getNumber(), optionalFields[0], optionalFields[1]);
        Pattern pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(rawInfo);
        return matcher.find();
    }
}