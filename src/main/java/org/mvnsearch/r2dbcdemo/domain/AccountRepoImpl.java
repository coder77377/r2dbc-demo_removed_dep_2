package org.mvnsearch.r2dbcdemo.domain;

import io.r2dbc.client.R2dbc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.domain.Sort.Order.desc;

/**
 * account repository implementation
 *
 * @author linux_china
 */
@Repository
public class AccountRepoImpl implements AccountRepo {
    @Autowired
    private R2dbc r2dbc;
    @Autowired
    private DatabaseClient databaseClient;

    @Override
    public Flux<Account> findAll() {
        return databaseClient.select()
                .from(Account.class)
                .orderBy(Sort.by(desc("id")))
                .fetch()
                .all();
    }

    @Override
    public Mono<Integer> updatePassword(Integer id, String newPassword) {
        return databaseClient.execute()
                .sql("update account set passwd = $1 where id = $2")
                .bind("$1", newPassword)
                .bind("$2", id)
                .fetch()
                .rowsUpdated();
    }
}
