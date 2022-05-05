import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.reactive.mutiny.Mutiny;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MutinyVertxContextTest {

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
        Mutiny.SessionFactory sessionFactory = emf.unwrap( Mutiny.SessionFactory.class );
        StepVerifier.create( Mono.fromCompletionStage(
                sessionFactory.withTransaction( s -> s.persist( new User( 11L, "11" ) ) ).subscribeAsCompletionStage()
        ) ).verifyComplete();
    }

    @Test
    public void test_p2() {
        Mutiny.SessionFactory sessionFactory = emf.unwrap(Mutiny.SessionFactory.class);
        StepVerifier.create(Mono.fromCompletionStage(
                sessionFactory.withTransaction(s -> s.persist(new User(12L, "12"))).subscribeAsCompletionStage()
        )).verifyComplete();
    }

    @Test
    public void test_p3() {
        Mutiny.SessionFactory sessionFactory = emf.unwrap(Mutiny.SessionFactory.class);
        StepVerifier.create(Mono.fromCompletionStage(
                sessionFactory.withTransaction(s -> s.persist(new User(13L, "13"))).subscribeAsCompletionStage()
        )).verifyComplete();
    }

    @Test
    public void test() {
        Mutiny.SessionFactory sessionFactory = emf.unwrap(Mutiny.SessionFactory.class);
        StepVerifier.create(
                        Mono.fromCompletionStage(
                                sessionFactory.withTransaction(s -> s.persist(new User(1L, "1"))
                                        .chain(r -> s.createQuery("select u.id from User u where id = 1").getSingleResult()))
                                        .subscribeAsCompletionStage())
                )
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    public void test2() {
        Mutiny.SessionFactory sessionFactory = emf.unwrap(Mutiny.SessionFactory.class);
        StepVerifier.create(
                        Mono.fromCompletionStage(
                                sessionFactory.withTransaction(s -> s.persist(new User(2L, "2"))
                                        .chain(r -> s.createQuery("select u.id from User u where id = 2").getSingleResult()))
                                        .subscribeAsCompletionStage())
                        )
                .expectNext(2L)
                .verifyComplete();
    }

    @Test
    public void test3() {
        Mutiny.SessionFactory sessionFactory = emf.unwrap(Mutiny.SessionFactory.class);
        StepVerifier.create(
                        Mono.fromCompletionStage(
                                sessionFactory.withTransaction(s -> s.persist(new User(3L, "3"))
                                        .chain(r -> s.createQuery("select u.id from User u where id = 3").getSingleResult()))
                                        .subscribeAsCompletionStage()
                        )
                )
                .expectNext(3L)
                .verifyComplete();
    }

}
