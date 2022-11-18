package cz.muni.fi.pv168.data.storage.mapper;

import java.util.Optional;

/**
 * Funcional contract that promises 'get' function.
 *
 * @author Jan Martinek
 */
@FunctionalInterface
public interface Lookup<T> {

    Optional<T> get(Long id);
}
