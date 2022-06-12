package reengineering.ddd.archtype;

public interface Entity<Identity, Description> {
    Identity identity();

    Description description();
}
