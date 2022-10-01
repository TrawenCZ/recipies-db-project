package cz.muni.fi.pv168.data;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import java.util.stream.Collectors;

public abstract class AbstractDataGenerator<F> {

    protected final Random random = new Random(37L);

    protected <T> T selectRandom(List<T> data) {
        int index = random.nextInt(data.size());
        return data.get(index);
    }
    
    public List<F> createTestData(int count) {
        return Stream
                .generate(this::createTestEntity)
                .limit(count)
                .collect(Collectors.toList());
    }

    public abstract F createTestEntity();

}
