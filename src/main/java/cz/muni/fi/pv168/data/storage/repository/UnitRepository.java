package cz.muni.fi.pv168.data.storage.repository;

import cz.muni.fi.pv168.data.storage.dao.UnitDao;
import cz.muni.fi.pv168.data.storage.entity.UnitEntity;
import cz.muni.fi.pv168.data.storage.mapper.EntityMapper;
import cz.muni.fi.pv168.model.Unit;

public class UnitRepository extends AbstractRepository<UnitDao, UnitEntity, Unit> {
    public UnitRepository(UnitDao dao, EntityMapper<UnitEntity, Unit> mapper) {
        super(dao, mapper);
    }
}
