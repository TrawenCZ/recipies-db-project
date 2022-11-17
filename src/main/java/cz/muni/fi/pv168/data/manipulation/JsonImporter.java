package cz.muni.fi.pv168.data.manipulation;

import java.util.List;

public interface JsonImporter{

    public <T> List<T> loadEntities(String filePath, Class<T> contentClass);

}
