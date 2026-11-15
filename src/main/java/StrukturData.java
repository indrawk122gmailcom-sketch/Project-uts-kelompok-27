public interface StrukturData<T> {
    void add(T element);
    T get(int index);
    T remove(int index);
    int size();
    boolean isEmpty();
<<<<<<< HEAD
    boolean contains(T value);
=======
    boolean contains(T element);
>>>>>>> origin/main
    void clear();
    boolean validateInvariant();
}