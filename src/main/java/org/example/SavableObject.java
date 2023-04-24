package org.example;

public abstract class SavableObject {
    private static final SavableObjectsStorage storage = new SavableObjectsStorage();

    private Long id;

    public SavableObject() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void save() {
        storage.save(this);
    }

    public void delete() {
        storage.delete(this);
    }

    @SuppressWarnings("unchecked")
    public static <T extends SavableObject> T find(Long id) {
        return (T) storage.findById(id);
    }

}
