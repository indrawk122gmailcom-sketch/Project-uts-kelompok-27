public class AuditStack<T> {
    private DynamicArray<T> array;

    public AuditStack() {
        this.array = new DynamicArray<>();
    }

    public void push(T element) {
        array.add(element);
    }

    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Audit stack kosong.");
        }
        return array.remove(array.size() - 1);
    }

    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Audit stack kosong.");
        }
        return array.get(array.size() - 1);
    }

    public int size() {
        return array.size();
    }

    public boolean isEmpty() {
        return array.isEmpty();
    }

    public T get(int index) {
        return array.get(index);
    }
}