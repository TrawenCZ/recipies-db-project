package cz.muni.fi.pv168.data.storage.repository;

import cz.muni.fi.pv168.data.storage.dao.DataAccessObject;
import cz.muni.fi.pv168.data.storage.mapper.EntityMapper;
import cz.muni.fi.pv168.model.Identifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractRepository<D extends DataAccessObject<EE>, EE, E extends Identifiable> implements Repository<E> {

    protected final D dao;
    protected final EntityMapper<EE, E> mapper;
    protected List<E> entities = new ArrayList<>();

    public AbstractRepository(D dao, EntityMapper<EE, E> mapper) {
        this(dao, mapper, true);
    }

    public AbstractRepository(D dao, EntityMapper<EE, E> mapper, boolean doRefresh) {
        this.dao = Objects.requireNonNull(dao);
        this.mapper = Objects.requireNonNull(mapper);
        if (doRefresh) this.refresh();
    }

    @Override
    public int getSize() {
        return entities.size();
    }

    @Override
    public Optional<E> findById(long id) {
        return entities.stream().filter(e -> e.getId() == id).findFirst();
    }

    @Override
    public Optional<E> findByIndex(int index) {
        if (index >= 0 && index < getSize())
            return Optional.of(entities.get(index));
        return Optional.empty();
    }

    @Override
    public List<E> findAll() {
        return Collections.unmodifiableList(entities);
    }

    @Override
    public void refresh() {
        entities = fetchAllEntities();
    }

    @Override
    public void create(E newEntity) {
        Stream.of(newEntity)
                .map(mapper::mapToEntity)
                .map(dao::create)
                .map(mapper::mapToModel)
                .forEach(e -> entities.add(e));
    }

    @Override
    public void update(E entity) {
        int index = entities.indexOf(entity);
        Stream.of(entity)
                .map(mapper::mapToEntity)
                .map(dao::update)
                .map(mapper::mapToModel)
                .forEach(e -> entities.set(index, e));
    }

    @Override
    public void deleteByIndex(int index) {
        this.deleteEntityByIndex(index);
        entities.remove(index);
    }

    private void deleteEntityByIndex(int index) {
        dao.deleteById(entities.get(index).getId());
    }

    private List<E> fetchAllEntities() {
        return dao.findAll().stream()
                .map(mapper::mapToModel)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
