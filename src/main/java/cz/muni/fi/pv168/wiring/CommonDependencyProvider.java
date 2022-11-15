package cz.muni.fi.pv168.wiring;

import cz.muni.fi.pv168.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.model.Ingredient;

/**
 * Dependency provider common for all environments
 */
public abstract class CommonDependencyProvider implements DependencyProvider {


    //private final Repository<Ingredient> ingredient;
    //private final Repository<IngredientList> blabla;

    protected CommonDependencyProvider(DatabaseManager databaseManager) {
        //Validator<Department> departmentValidator = new DepartmentValidator();
        //Validator<Employee> employeeValidator = new EmployeeValidator();
        /*
        this.departments = new DepartmentRepository(
                new DepartmentDao(databaseManager::getConnectionHandler),
                new DepartmentMapper(departmentValidator)
        );

        this.employees = new EmployeeRepository(
                new EmployeeDao(databaseManager::getConnectionHandler),
                new EmployeeMapper(departments, employeeValidator)
        );
        */
    }
    /*
    @Override
    public Repository<Department> getDepartmentRepository() {
        return departments;
    }

    @Override
    public Repository<Employee> getEmployeeRepository() {
        return employees;
    }

     */
}
