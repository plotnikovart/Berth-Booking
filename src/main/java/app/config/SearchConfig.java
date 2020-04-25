package app.config;

//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.hibernate.search.jpa.FullTextEntityManager;
//import org.hibernate.search.jpa.Search;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;

//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class SearchConfig implements ApplicationListener<ApplicationReadyEvent> {
//
//    private final EntityManager em;
//
//    @Override
//    @Transactional
//    public void onApplicationEvent(ApplicationReadyEvent event) {
//        try {
//            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
//            fullTextEntityManager.createIndexer().startAndWait();
//        } catch (InterruptedException e) {
//            log.error(e.getMessage());
//        }
//    }
//}
