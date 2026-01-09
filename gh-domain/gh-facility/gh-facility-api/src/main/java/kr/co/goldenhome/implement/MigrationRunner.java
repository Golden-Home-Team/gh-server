package kr.co.goldenhome.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
@RequiredArgsConstructor
public class MigrationRunner implements CommandLineRunner {

    private final EmbeddingClient embeddingClient;

    @Override
    public void run(String... args) throws Exception {
        log.info("running ...");
        embeddingClient.migrateToVectorIndex("facilities", 500,  "2382");
        log.info("finished ...");

    }
}
