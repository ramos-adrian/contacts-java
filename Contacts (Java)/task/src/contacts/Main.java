package contacts;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        MenuActions menuActions = MenuActions.EXIT;
        do {
            System.out.print("[menu] Enter action (add, list, search, count, exit): ");
            try {
                menuActions = MenuActions.valueOf(scanner.nextLine().toUpperCase());
            } catch (IllegalArgumentException ignored) {
                System.out.println("Invalid Action!");
                continue;
            }
            if (menuActions != MenuActions.EXIT) executeOption(phoneBook, scanner, menuActions);
        } while (menuActions != MenuActions.EXIT);
    }

    //TODO options had changed
    private static void executeOption(PhoneBook phoneBook, Scanner scanner, MenuActions menuActions) {
        switch (menuActions) {
            case ADD -> addContactOption(phoneBook, scanner);
            case LIST -> listContactsOption(phoneBook, scanner);
            case COUNT -> countContactsOption(phoneBook);
            case SEARCH -> searchContactsOption(phoneBook, scanner);
        }
    }

    private static void searchContactsOption(PhoneBook phoneBook, Scanner scanner) {
        PhoneBook found = new PhoneBook();
        System.out.print("Enter search query: ");
        String query = scanner.nextLine();
        for (Contact contact : phoneBook) {
            if (contact.searchRequest(query)) {
                found.addContact(contact);
            }
        }
        if (found.isEmpty()) {
            System.out.println("No results!");
            return;
        }
        System.out.printf("Found %d results: \n", found.size());
        listContacts(found.iterator());

        System.out.println();
        searchActionMenu(found, phoneBook, scanner);
    }

    private static void searchActionMenu(PhoneBook found, PhoneBook phoneBook, Scanner scanner) {
        System.out.print("[search] Enter action ([number], back, again): ");
        String option = scanner.nextLine();

        switch (option.toLowerCase()) {
            case "again" -> searchContactsOption(phoneBook, scanner);
            case "back" -> {return;}
        }

        selectContactFromList(found, phoneBook, scanner, option);
    }

    private static void selectContactFromList(PhoneBook found, PhoneBook phoneBook, Scanner scanner, String option) {
        try {
            int index = Integer.parseInt(option) - 1;
            Contact selected = found.get(index);
            System.out.println(selected.getContactInfo());
            String number = selected.hasNumber() ? selected.getNumber() : "[no number]";
            System.out.println("Number: " + number);
            System.out.println("Time created: " + selected.getTimeCreated());
            System.out.println("Time last edit: " + selected.getTimeLastEdit());
            System.out.println();
            recordMenu(selected, phoneBook, scanner);
        } catch (NumberFormatException ignored) {
            System.out.println("Invalid option!");
        }
    }

    private static void recordMenu(Contact selected, PhoneBook phoneBook, Scanner scanner) {
        System.out.print("[record] Enter action (edit, delete, menu): ");
        String actionString = scanner.nextLine();
        RecordAction recordAction;
        try {
            recordAction = RecordAction.valueOf(actionString.toUpperCase());
        } catch (IllegalArgumentException ignored) {
            System.out.println("Invalid action!");
            return;
        }
        switch (recordAction) {
            case EDIT -> {
                editContactOption(selected, scanner);
            }
            case DELETE -> {
                removeContactOption(selected, phoneBook);
            }
            case MENU -> {
                System.out.println();
            }
        }
        System.out.println();
    }

    private static void listContactsOption(PhoneBook phoneBook, Scanner scanner) {
        listContacts(phoneBook.iterator());
        System.out.println();
        ListActionMenu(phoneBook, scanner);
    }

    private static void ListActionMenu(PhoneBook phoneBook, Scanner scanner) {
        System.out.print("[list] Enter action ([number], back): ");
        String option = scanner.nextLine();

        if (option.equalsIgnoreCase("back")) {
            return;
        }

        selectContactFromList(phoneBook, phoneBook, scanner, option);
    }

    private static void listContacts(Iterator<Contact> iterator) {
        int number = 0;
        while (iterator.hasNext()) {
            Contact contact = iterator.next();
            System.out.printf("%d. %s\n", ++number, contact.getFullName());
        }
    }

    private static void addContactOption(PhoneBook phoneBook, Scanner scanner) {
        System.out.print("Enter the type (person, organization): ");
        String contactType = scanner.nextLine();

        Contact contact;
        if (contactType.equalsIgnoreCase("person")) {
            contact = createPersonContact(scanner);
        }
        else {
            contact = createOrganizationContact(scanner);
        }

        System.out.print("Enter the number: ");
        String number = scanner.nextLine();
        if (!contact.setNumber(number)) {
            System.out.println("Wrong number format!");
        }

        phoneBook.addContact(contact);
        System.out.println("The record added.");
        System.out.println();
    }

    private static Contact createOrganizationContact(Scanner scanner) {
        System.out.print("Enter the organization name: ");
        String name = scanner.nextLine();
        System.out.print("Enter the address: ");
        String address = scanner.nextLine();
        return new OrganizationContact(name, address);
    }

    private static Contact createPersonContact(Scanner scanner) {
        System.out.print("Enter the name: ");
        String name = scanner.nextLine();
        System.out.print("Enter the surname: ");
        String surname = scanner.nextLine();
        PersonContact contact = new PersonContact(name, surname);

        askBirthdate(scanner, contact);

        askGender(scanner, contact);
        return contact;
    }

    private static void askGender(Scanner scanner, PersonContact contact) {
        System.out.print("Enter the gender (M, F):");
        String genderString = scanner.nextLine();
        Gender gender = null;
        if (genderString.equalsIgnoreCase("M")) {
            gender = Gender.MALE;
        } else if (genderString.equalsIgnoreCase("F")) {
            gender = Gender.FEMALE;
        }
        if (gender == null) {
            System.out.println("Bad gender!");
        } else {
            contact.setGender(gender);
            contact.setTimeLastEdit(LocalDateTime.now());
        }
    }

    private static void askBirthdate(Scanner scanner, PersonContact contact) {
        System.out.print("Enter the birthdate:");
        String birthdateString = scanner.nextLine();
        LocalDate birthdate = null;
        try {
             birthdate = LocalDate.parse(birthdateString);
             contact.setBirthdate(birthdate);
             contact.setTimeLastEdit(LocalDateTime.now());
        } catch (DateTimeParseException ignored) {
            System.out.println("Bad birth date!");
        }
    }

    private static void removeContactOption(Contact record, PhoneBook phoneBook) {
        phoneBook.remove(record);
        System.out.println("The record removed!");
    }

    private static void editContactOption(Contact contact, Scanner scanner) {
        System.out.printf("Select a field (%s): ", String.join(", ", contact.getEditableFields()));
        String field = scanner.nextLine();

        if (!contact.getEditableFields().contains(field)) {
            System.out.println("Invalid field!");
            return;
        }

        System.out.printf("Enter %s", field);
        String newValue = scanner.nextLine();

        contact.editField(field, newValue);
        System.out.println("The record updated!");

        System.out.println();
    }

    private static void countContactsOption(PhoneBook phoneBook) {
        int size = phoneBook.size();
        System.out.printf("The Phone Book has %d records.", size);
        System.out.println();
    }

}


