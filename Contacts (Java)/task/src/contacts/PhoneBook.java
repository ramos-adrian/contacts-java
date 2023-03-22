package contacts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PhoneBook implements Iterable<Contact> {
    private final List<Contact> contacts = new ArrayList<>();

    public int size() {
        return contacts.size();
    }

    public void addContact(Contact contact) {
        this.contacts.add(contact);
    }

    @Override
    public Iterator<Contact> iterator() {
        return contacts.iterator();
    }

    public Contact get(int index) {
        return contacts.get(index);
    }

    public void remove(int record) {
        contacts.remove(record);
    }

    public void remove(Contact contact) {
        contacts.remove(contact);
    }

    public boolean isEmpty() {
        return contacts.isEmpty();
    }
}
