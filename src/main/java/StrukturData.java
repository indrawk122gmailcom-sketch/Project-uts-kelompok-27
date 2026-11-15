public interface StrukturData<T> {
    void add(T element);
    T get(int index);
    T remove(int index);
    int size();
    boolean isEmpty();
    boolean contains(T element);
    void clear();
    boolean validateInvariant();
}