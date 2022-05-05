import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.reactive.stage.Stage;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class VertxContextTest {

    static EntityManagerFactory emf;
    static DB db;

    @BeforeAll
    public static void startDatabase() throws ManagedProcessException {
        System.out.println("Starting database");
        db = DB.newEmbeddedDB( 3306 );
        db.start();
        emf = Persistence.createEntityManagerFactory( "reactiveTest" );
    }

    @AfterAll
    public static void stopDatabase() throws ManagedProcessException {
        System.out.println("Stopping database");
        db.stop();
    }
    @Test
    public void test_p1() {
        Stage.SessionFactory sessionFactory = emf.unwrap( Stage.SessionFactory.class );
        StepVerifier.create( Mono.fromCompletionStage(
                sessionFactory.withTransaction( s -> s.persist( new User( 11L, "11" ) ) )
        ) ).verifyComplete();
    }

    @Test
    public void test_p2() {
        Stage.SessionFactory sessionFactory = emf.unwrap(Stage.SessionFactory.class);
        StepVerifier.create(Mono.fromCompletionStage(
                sessionFactory.withTransaction(s -> s.persist(new User(12L, "12")))
        )).verifyComplete();
    }

    @Test
    public void test_p3() {
        Stage.SessionFactory sessionFactory = emf.unwrap(Stage.SessionFactory.class);
        StepVerifier.create(Mono.fromCompletionStage(
                sessionFactory.withTransaction(s -> s.persist(new User(13L, "13")))
        )).verifyComplete();
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
