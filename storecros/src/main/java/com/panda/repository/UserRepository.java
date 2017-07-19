package com.panda.repository;

import com.panda.domain.User;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Panda.HuangWei.
 * @since 2017-07-16 17:46.
 */
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
