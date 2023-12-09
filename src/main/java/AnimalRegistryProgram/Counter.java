package AnimalRegistryProgram;

public class Counter implements AutoCloseable {
    private int count;

    public Counter() {
        this.count = 0;
    }

    public void add() {
        count++;
    }

    public int getCount() {
        return count;
    }

    @Override
    public void close() {
        if (count > 1) {System.out.println("Ресурс закрыт.");}
    }
}

