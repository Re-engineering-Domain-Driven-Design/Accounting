package reengineering.ddd.archtype;

public interface Entity<Identity, Description> {
    Identity getIdentity();

    Description getDescription();
}
