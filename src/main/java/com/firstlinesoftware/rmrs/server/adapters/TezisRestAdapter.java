package com.firstlinesoftware.rmrs.server.adapters;

import com.firstlinesoftware.base.server.adapters.ImportAdapter;
import com.firstlinesoftware.base.server.importers.ItemImporter;
import com.firstlinesoftware.orgstruct.shared.dto.Department;
import com.firstlinesoftware.orgstruct.shared.dto.Orgstructure;
import com.firstlinesoftware.orgstruct.shared.dto.Person;
import com.firstlinesoftware.orgstruct.shared.dto.Position;
import com.firstlinesoftware.rmrs.server.services.sync.tezis.TezisDept;
import com.firstlinesoftware.rmrs.server.services.sync.tezis.TezisEmployee;
import com.firstlinesoftware.rmrs.server.services.sync.tezis.TezisRestClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

@Component
public class TezisRestAdapter implements ImportAdapter<Orgstructure, String> {
    @Override
    public void doImport(ItemImporter<Orgstructure> importer, String url) {
        final TezisRestClient client = new TezisRestClient(url);
//        logger.trace("Start getting departments tree");
        final TezisDept root = client.getDepartments();
//        logger.trace("End getting departments tree");

        if (root != null) {
            final Orgstructure result = new Orgstructure();
            result.departments = new ArrayList<>();
            result.positions = new ArrayList<>();
            result.persons = new ArrayList<>();
            transform(root, null, result);
            importer.importItem(result);
        }
    }

    private void transform(TezisDept source, String parentId, Orgstructure result) {
        final Department department = transformDepartment(source);
        department.setParentId(parentId);
        result.departments.add(department);

        if (source.employees != null) {
            for (TezisEmployee tezisEmployee : source.employees) {
                final Person person = transformPerson(tezisEmployee);
                result.persons.add(person);
                if(tezisEmployee.positionName != null) {
                    final Position position = transformPosition(tezisEmployee);
                    position.department = department;
                    position.parentId = source.id;
                    position.person = person;
                    result.positions.add(position);
                }
            }
        }

        if (source.subDepts != null) {
            for (TezisDept subDept : source.subDepts) {
                transform(subDept, source.id, result);
            }
        }
    }

    private static Position transformPosition(TezisEmployee employee) {
        final Position position = new Position();
        position.setExternalId(employee.id);
        if(employee.positionName.startsWith("(") && employee.positionName.endsWith(")")) {
            position.setName(employee.positionName.substring(1, employee.positionName.length() - 1));
        } else {
            position.setName(employee.positionName);
        }
        return position;
    }

    private static Person transformPerson(TezisEmployee tezisEmployee) {
        final Person person = new Person();
        person.setFirstName(tezisEmployee.firstName);
        person.setLastName(tezisEmployee.lastName);
        person.setMiddleName(tezisEmployee.middleName);
        person.setUserName(tezisEmployee.login);
        person.setEmail(tezisEmployee.email);
        return person;
    }

    private static Department transformDepartment(TezisDept source) {
        final Department updated = new Department();
        updated.setExternalId(source.id);
        updated.setExternalModifiedDate(new Date());
        updated.setExternalVersionId(String.valueOf(source.version));
        updated.setName(source.name);
        updated.setDepartmentCode(source.code);
        return updated;
    }

}
