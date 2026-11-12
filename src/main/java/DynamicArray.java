public class DynamicArray<T> implements StrukturData<T> {
    private Object[] data;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public DynamicArray() {
        this.data = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    @Override
    public void add(T element) {
        if (size == data.length) {
            resize();
        }
        data[size++] = element;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(int index) {
        checkBounds(index);
        return (T) data[index];
    }

    public void set(int index, T element) {
        checkBounds(index);
        data[index] = element;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T remove(int index) {
        checkBounds(index);
        T removedElement = (T) data[index];
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[--size] = null;
        return removedElement;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean validateInvariant() {
        return size >= 0 && size <= data.length;
    }

    private void resize() {
        Object[] newData = new Object[data.length * 2];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }

    private void checkBounds(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Indeks out of bounds: " + index + " (Size: " + size + ")");
        }
    }
}