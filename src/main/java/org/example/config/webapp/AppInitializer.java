package org.example.config.webapp;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.StatusPrinter;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import org.example.exceptions.ErrorMessages;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.net.URL;

public class AppInitializer implements WebApplicationInitializer {

    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    private static final String DISPATCHER_SERVLET_NAME = "dispatcher";
    private static final String DISPATCHER_MAPPING = "/";

    private static final String THROW_EXCEPTION_IF_NO_HANDLER_FOUND = "throwExceptionIfNoHandlerFound";
    private static final String THROW_EXCEPTION_VALUE = "true";

    private static final String LOG_CONFIG_FILE_NAME = "logback.xml";

    @Override
    public void onStartup(@NonNull ServletContext servletContext) {
        configureLogging();

        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(AppConfig.class);
        servletContext.addListener(new ContextLoaderListener(rootContext));

        registerDispatcherServlet(servletContext, rootContext);
    }

    private void registerDispatcherServlet(ServletContext servletContext, AnnotationConfigWebApplicationContext rootContext) {
        AnnotationConfigWebApplicationContext servletAppContext = new AnnotationConfigWebApplicationContext();
        servletAppContext.setParent(rootContext);
        servletAppContext.register(AppConfig.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(servletAppContext);
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet(DISPATCHER_SERVLET_NAME, dispatcherServlet);

        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping(DISPATCHER_MAPPING);
        dispatcher.setInitParameter(THROW_EXCEPTION_IF_NO_HANDLER_FOUND, THROW_EXCEPTION_VALUE);
    }


    private void configureLogging() {
        try {
            LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            context.reset();

            URL logConfig = getClass().getClassLoader().getResource(LOG_CONFIG_FILE_NAME);
            if (logConfig == null) {
                throw new IllegalStateException(ErrorMessages.LOG_CONFIG_ERROR);
            }

            configurator.doConfigure(logConfig);
            StatusPrinter.printInCaseOfErrorsOrWarnings(context);
        } catch (Exception e) {
            throw new IllegalStateException(ErrorMessages.LOG_CONFIG_ERROR, e);
        }
    }
}
