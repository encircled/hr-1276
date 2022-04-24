import ch.vorburger.mariadb4j.DB;
import org.hibernate.reactive.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class VertxContextTest {

    static EntityManagerFactory emf;

    @BeforeAll
    public static void init() throws Exception {
        DB db = DB.newEmbeddedDB(3306);
        db.start();
        emf = Persistence.createEntityManagerFactory("reactiveTest");
    }

    @Test
    public void test() {
        Stage.SessionFactory sessionFactory = emf.unwrap(Stage.SessionFactory.class);
        StepVerifier.create(
                        Mono.fromCompletionStage(
                                sessionFactory.withTransaction(s -> s.persist(new User(1L, "1"))
                                        .thenCompose(r -> s.createQuery("select u.id from User u where id = 1").getSingleResult()))
                        )
                )
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    public void test2() {
        Stage.SessionFactory sessionFactory = emf.unwrap(Stage.SessionFactory.class);
        StepVerifier.create(
                        Mono.fromCompletionStage(
                                sessionFactory.withTransaction(s -> s.persist(new User(2L, "2"))
                                        .thenCompose(r -> s.createQuery("select u.id from User u where id = 2").getSingleResult()))
                        )
                )
                .expectNext(2L)
                .verifyComplete();
    }

    @Test
    public void test3() {
        Stage.SessionFactory sessionFactory = emf.unwrap(Stage.SessionFactory.class);
        StepVerifier.create(
                        Mono.fromCompletionStage(
                                sessionFactory.withTransaction(s -> s.persist(new User(3L, "3"))
                                        .thenCompose(r -> s.createQuery("select u.id from User u where id = 3").getSingleResult()))
                        )
                )
                .expectNext(3L)
                .verifyComplete();
    }

}
