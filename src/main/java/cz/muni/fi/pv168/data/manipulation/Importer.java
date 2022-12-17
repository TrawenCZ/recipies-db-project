package cz.muni.fi.pv168.data.manipulation;

public interface Importer {

    public Progress getProgress();

    public void importData(String filePath);

}
