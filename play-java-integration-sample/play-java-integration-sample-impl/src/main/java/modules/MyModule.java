package modules;

import com.google.inject.AbstractModule;
import controllers.CatalogServiceController;
import integration.CatalogServiceImpl;
import integration.MySoapClient;
import lombok.extern.slf4j.Slf4j;
import service.CatalogService;
import util.MyExecutionContext;

@Slf4j
public class MyModule extends AbstractModule {

    @Override
    protected void configure() {
        log.info("Your are here!!! - configure");
        bind(CatalogServiceController.class).asEagerSingleton();
        bind(CatalogService.class).to(CatalogServiceImpl.class).asEagerSingleton();
        bind(MySoapClient.class).asEagerSingleton();
        bind(MyExecutionContext.class).asEagerSingleton();
    }

}

