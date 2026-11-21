import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

// Unit 5: Generics, bounded type parameter
public class Repository<T extends TextEntity> {
    private List<T> items;

    public Repository() {
        this.items = new ArrayList<>();
    }

    public synchronized void add(T item) {
        items.add(item);
    }

    public synchronized List<T> getAll() {
        return Collections.unmodifiableList(new ArrayList<>(items));
    }

    // Unit 5: Collection using Iterators
    public void printAllIds() {
        Iterator<T> iterator = items.iterator();
        while (iterator.hasNext()) {
            System.out.println("Item ID: " + iterator.next().getId());
        }
    }
}