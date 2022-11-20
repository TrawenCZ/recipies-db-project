package cz.muni.fi.pv168.model;


/**
 * Interface for all classes that have an accessible id.
 *
 * @author Adam Slíva
 */
public interface Identifiable {

    Long getId();

    void setId(Long id);
}
