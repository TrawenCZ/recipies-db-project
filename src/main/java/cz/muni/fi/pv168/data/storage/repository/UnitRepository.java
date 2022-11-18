package cz.muni.fi.pv168.data.storage.repository;

import cz.muni.fi.pv168.data.storage.dao.UnitDao;
import cz.muni.fi.pv168.data.storage.entity.UnitEntity;
import cz.muni.fi.pv168.data.storage.mapper.EntityMapper;
import cz.muni.fi.pv168.data.storage.mapper.UnitMapper;
import cz.muni.fi.pv168.model.BaseUnitsEnum;
import cz.muni.fi.pv168.model.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class UnitRepository extends AbstractRepository<UnitDao, UnitEntity, Unit> {

    public UnitRepository(UnitDao dao, EntityMapper<UnitEntity, Unit> mapper) {
        super(dao, mapper, false);
        ((UnitMapper) mapper).updateSuppliers(this::findById, this::findByName);
        addBaseUnits();
        refresh();
    }

    public Optional<Unit> findByName(String name) {
        return entities.stream().filter(e -> Objects.equals(e.getName(), name)).findFirst();
    }

    @Override
    public void refresh() {
        entities = fetchAllEntities();
        addBaseUnits();
    }

    private void addBaseUnits() {
        entities.add(new Unit(1L, "g", 1, BaseUnitsEnum.GRAM));
        entities.add(new Unit(2L, "ml", 1, BaseUnitsEnum.MILLILITER));
        entities.add(new Unit(3L, "pc(s)", 1, BaseUnitsEnum.PIECE));
    }

    private List<Unit> fetchAllEntities() {
        return dao.findAll().stream()
                .filter(e -> !(e.name().equals("g") || e.name().equals("ml") || e.name().equals("pc(s)")))
                .map(mapper::mapToModel)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public String toString() {
        return "Unit";
    }
}
