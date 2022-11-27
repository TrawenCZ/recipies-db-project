package cz.muni.fi.pv168.data.storage.repository;

import cz.muni.fi.pv168.data.storage.dao.UnitDao;
import cz.muni.fi.pv168.data.storage.entity.UnitEntity;
import cz.muni.fi.pv168.data.storage.mapper.EntityMapper;
import cz.muni.fi.pv168.data.storage.mapper.UnitMapper;
import cz.muni.fi.pv168.model.BaseUnitsEnum;
import cz.muni.fi.pv168.model.Unit;
import cz.muni.fi.pv168.wiring.Supported;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class UnitRepository extends AbstractRepository<UnitDao, UnitEntity, Unit> {

    public UnitRepository(UnitDao dao, EntityMapper<UnitEntity, Unit> mapper) {
        super(dao, mapper, false);
        ((UnitMapper) mapper).setBaseUnitSupplier(this::findByName);
        loadBaseUnits(dao);
        refresh();
    }

    public Optional<Unit> findByName(String name) {
        return entities.stream().dropWhile(e -> !Objects.equals(e.getName(), name)).findFirst();
    }

    @Override
    public String toString() {
        return Supported.UNIT;
    }

    private void loadBaseUnits(UnitDao dao) {
        var loadedBaseUnits = dao.findAll().stream()
                                            .map(UnitEntity::name)
                                            .filter(e -> BaseUnitsEnum.stringToEnum(e) != null)
                                            .toList();
        for (var missing : Arrays.asList(BaseUnitsEnum.getAllValues())) {
            if (!loadedBaseUnits.contains(missing)) {
                this.create(new Unit(missing, 1.0d, null));
            }
        }
    }
}
