package com.datazuul.webapps.datacenter.backend.impl.jpa;

import com.datazuul.webapps.datacenter.backend.impl.jpa.ContactRepositoryImplJpa;
import com.datazuul.webapps.datacenter.backend.impl.jpa.entities.ContactJpaEntity;
import com.datazuul.webapps.datacenter.config.SpringConfigRepositoryTest;
import java.util.Iterator;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author ralf
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfigRepositoryTest.class)
public class ContactRepositoryImplJpaTest {

    @Autowired
    ContactRepositoryImplJpa repository;

    @Test
    public void test_save_findAll_findByEmail() {
        ContactJpaEntity ralf = new ContactJpaEntity("Ralf", "Eichinger");
        ralf.addEmail("ralf.eichinger@gmail.com", null);
        ralf = repository.save(ralf);

        ContactJpaEntity vroni = new ContactJpaEntity("Veronika", "Eichinger");
        vroni.addEmail("geheim@nirgendwo.de", null);
        vroni = repository.save(vroni);

        Iterable<ContactJpaEntity> iterable = repository.findAll();
        Iterator<ContactJpaEntity> iterator = iterable.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        assertThat(count, is(2));

        List<ContactJpaEntity> result = repository.findByEmail("ralf.eichinger@gmail.com");
        assertThat(result.size(), is(1));
//        assertTrue(result.contains(ralf)); // TODO implement hashCode and equals in Contact

        result = repository.findByEmail("geheim@nirgendwo.de");
        assertThat(result.size(), is(1));
//        assertTrue(result.contains(vroni));
    }

}
